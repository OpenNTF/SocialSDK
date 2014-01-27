package com.ibm.sbt.opensocial.domino.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
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
	private static final String CLASS = DominoSecurityTokenCodec.class.getName();
	private static final String SECURITY_TOKEN_TYPE = "gadgets.securityTokenType";
	private SecurityTokenCodec secureCodec;
	private SecurityTokenCodec insecureCodec;
	private Map<String, String> tokenTypes;
	private ContainerConfig config;
	private final Logger log;
	private ContainerConfig.ConfigObserver observer = new ContainerConfig.ConfigObserver() {
		@Override
		public void containersChanged(ContainerConfig config,
				Collection<String> changed, Collection<String> removed) {
			populateTokenTypes(config, changed, removed, DominoSecurityTokenCodec.this.tokenTypes);
		}
	};

	@Inject
	public DominoSecurityTokenCodec(ContainerConfig config, Logger log) {
		this.config = config;
		this.tokenTypes = Maps.newHashMap();
		this.log = log;
		config.addConfigObserver(observer, false);
		populateTokenTypes(config, config.getContainers(), Collections.EMPTY_LIST, this.tokenTypes);
	}

	private void populateTokenTypes(ContainerConfig config, Collection<String> added, Collection<String> removed,
			Map<String, String> tokenTypes) {
		synchronized(tokenTypes) {
			for (String container : added) {
				tokenTypes.put(container, config.getString(container, SECURITY_TOKEN_TYPE));
			}
			for(String container : removed) {
				tokenTypes.remove(container);
			}
		}
	}

	public SecurityToken createToken(Map<String, String> tokenParameters)
			throws SecurityTokenException {
		final String method = "createToken";
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
		
		SecurityTokenCodec codec = getCodec(container);
		if(codec == null) {
			//Shindig is really bad about making sure containers are encoded before they
			//are placed on URLs.  It basically makes the assumption that container ids do
			//not need to be encoded and just sticks them on the URLs.  This is the case when
			//it makes the request to the /rpc endpoint so just to be sure lets encode the container
			//and try to look up the token type
			try {
				String encodedContainer = URLEncoder.encode(container, "UTF-8");
				codec = getCodec(encodedContainer);
				if(codec!= null) {
					//Since the container was not properly encoded make sure it is correct in the token parameters
					putContainerInTokenParams(tokenParameters, encodedContainer);
				} else {
					throw new RuntimeException("Could not find security token codec for container " + container);
				}
			} catch (UnsupportedEncodingException e) {
				log.logp(Level.WARNING, CLASS, method, "Error while encoding container.");
			}
		}
			
		return codec.createToken(tokenParameters);
	}
	
	public void putContainerInTokenParams(Map<String, String> tokenParams, String container) {
		String token = StringUtils.defaultString(tokenParams.get(SecurityTokenCodec.SECURITY_TOKEN_NAME));
		String[] parts = token.split(":");
		if(parts.length == 2) {
			String newToken = container + ":" + parts[1];
			tokenParams.put(SecurityTokenCodec.SECURITY_TOKEN_NAME, newToken);
		}
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
		synchronized(tokenTypes) {
			String tokenType = this.tokenTypes.get(container);
			return getCodecByType(tokenType);
		}
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
		return null;
	}
}
