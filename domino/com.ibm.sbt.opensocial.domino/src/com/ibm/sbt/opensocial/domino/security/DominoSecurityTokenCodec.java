package com.ibm.sbt.opensocial.domino.security;

import java.util.Collection;
import java.util.Map;

import org.apache.shindig.auth.AnonymousSecurityToken;
import org.apache.shindig.auth.BasicSecurityTokenCodec;
import org.apache.shindig.auth.BlobCrypterSecurityTokenCodec;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.auth.SecurityTokenCodec;
import org.apache.shindig.auth.SecurityTokenException;
import org.apache.shindig.config.ContainerConfig;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Security token codec responsible for encrypting/decrypting security tokens.
 *
 */
@Singleton
public class DominoSecurityTokenCodec implements SecurityTokenCodec {
	private static final String SECURITY_TOKEN_TYPE = "gadgets.securityTokenType";
	  private SecurityTokenCodec secureCodec;
	  private SecurityTokenCodec insecureCodec;
	  private Map<String, String> tokenTypes;
	  private ContainerConfig config;

	  @Inject
	  public DominoSecurityTokenCodec(ContainerConfig config) {
	    this.config = config;
	    this.tokenTypes = Maps.newHashMap();
	    populateTokenTypes(config, config.getContainers(), this.tokenTypes);
	  }

	  private void populateTokenTypes(ContainerConfig config, Collection<String> containerNames,
	          Map<String, String> tokenTypes) {
	    for (String container : containerNames) {
	      tokenTypes.put(container, config.getString(container, SECURITY_TOKEN_TYPE));
	    }
	  }

	  public SecurityToken createToken(Map<String, String> tokenParameters)
	          throws SecurityTokenException {
	    // FIXME: This is so gross that I have to do this. Shindig needs to be fixed so I can
	    // consistently get the container from tokenParameters.
	    String token = tokenParameters.get(SecurityTokenCodec.SECURITY_TOKEN_NAME);
	    if (token == null || token.length() == 0) {
	      return new AnonymousSecurityToken();
	    }

	    String[] tokenParts = token.split(":");
	    String container;
	    if (tokenParts.length == 2) {
	      // BlobCrypter. Part 0 is the container. Part 1 is the encrypted blob.
	      container = tokenParts[0];
	    } else {
	      // BasicCrypter. 6 is the magic number that is private static final in
	      // BasicBlobCrypterSecurityToken
	      container = tokenParts[6];
	    }

	    return getCodec(container).createToken(tokenParameters);
	  }

	  public String encodeToken(SecurityToken token) throws SecurityTokenException {
	    if (token == null) {
	      return null;
	    }
	    return getCodec(token.getContainer()).encodeToken(token);
	  }

	  @Deprecated
	  public int getTokenTimeToLive() {
	    return getCodec("default").getTokenTimeToLive("defualt");
	  }

	  public int getTokenTimeToLive(String container) {
	    return getCodec(container).getTokenTimeToLive(container);
	  }

	  private SecurityTokenCodec getCodec(String container) {
	    String tokenType = this.tokenTypes.get(container);
	    return getCodecByType(tokenType);
	  }

	  SecurityTokenCodec getCodecByType(String tokenType) {
	    if ("insecure".equals(tokenType)) {
	      if (this.insecureCodec == null) {
	        this.insecureCodec = new BasicSecurityTokenCodec(this.config);
	      }
	      return this.insecureCodec;
	    }

	    if ("secure".equals(tokenType)) {
	      if (this.secureCodec == null) {
	        this.secureCodec = new BlobCrypterSecurityTokenCodec(this.config);
	      }
	      return this.secureCodec;
	    }

	    throw new RuntimeException("Unknown security token type specified in "
	            + ContainerConfig.DEFAULT_CONTAINER + " container configuration. "
	            + SECURITY_TOKEN_TYPE + ": " + tokenType);
	  }
}
