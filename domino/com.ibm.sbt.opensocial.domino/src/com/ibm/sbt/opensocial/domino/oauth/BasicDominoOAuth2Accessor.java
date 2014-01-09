package com.ibm.sbt.opensocial.domino.oauth;

import org.apache.shindig.common.crypto.BlobCrypter;
import org.apache.shindig.common.servlet.Authority;
import org.apache.shindig.gadgets.oauth2.BasicOAuth2Accessor;
import org.apache.shindig.gadgets.oauth2.OAuth2Accessor;
import org.apache.shindig.gadgets.oauth2.OAuth2CallbackState;

/**
 * Basic implementation of {@link DominoOAuth2Accessor}.
 *
 */
public class BasicDominoOAuth2Accessor extends BasicOAuth2Accessor implements
DominoOAuth2Accessor {
	private static final String HTTP = "http";
	private static final String HTTP_PORT = ":80";
	private static final String HTTPS = "https";
	private static final String HTTPS_PORT = ":443";
	private static final long serialVersionUID = 5737430333471234340L;
	private String container;
	private DominoOAuth2CallbackState state;

	/**
	 * Creates a BasicDominoOAuth2Accessor.
	 */
	public BasicDominoOAuth2Accessor() {
		super();
		this.state = new DominoOAuth2CallbackState();
	}

	/**
	 * Creates a BasicDominoOAuth2Accessor from a {@link DominoOAuth2Accessor}
	 * @param accessor The {@link DominoOAuth2Accessor} to use.
	 */
	public BasicDominoOAuth2Accessor(DominoOAuth2Accessor accessor) {
		super(accessor);
		this.container = accessor.getContainer();
		this.state = (DominoOAuth2CallbackState)accessor.getState();
	}

	/**
	 * Creates a BasicDominoOAuth2Accessor.
	 * @param gadgetUri The gadget URI.
	 * @param serviceName The OAuth 2 service name from the gadget.
	 * @param user The ID of the user rendering the gadget.
	 * @param scope The scope of the OAuth 2 service.
	 * @param allowModuleOverrides True to allow the gadget to override OAuth 2 information, false otherwise.
	 * @param cypter The crypter to use to exchange OAuth information.
	 * @param globalRedirectUri The global redirect URI for the OpenSocial server.
	 * @param authority The authority to use to construct URLs.
	 * @param contextRoot The context root to use to construct URLs.
	 * @param container The container rendering the gadget.
	 */
	public BasicDominoOAuth2Accessor(final String gadgetUri, final String serviceName, final String user,
			final String scope, final boolean allowModuleOverrides, final BlobCrypter cypter,
			final String globalRedirectUri, final Authority authority, final String contextRoot, 
			final String container) {
		super(gadgetUri, serviceName, user, scope, allowModuleOverrides, null, globalRedirectUri, authority, contextRoot);
		this.container = container;
		this.state = new DominoOAuth2CallbackState(cypter);
		this.state.setGadgetUri(gadgetUri);
		this.state.setContainer(container);
		this.state.setScope(scope);
		this.state.setServiceName(serviceName);
		this.state.setUser(user);
	}

	@Override
	public String getContainer() {
		return container;
	}

	@Override
	public void setContainer(String container) {
		this.container = container;
	}

	@Override
	public OAuth2CallbackState getState() {
		return state;
	}

	@Override
	public String getRedirectUri() {
		String redirect = super.getRedirectUri();
		//If we are using standard ports no need to put them in the URL
		if(redirect.contains(HTTP_PORT)) {
			redirect = redirect.replace(HTTP_PORT, "");
		}
		if(redirect.contains(HTTPS_PORT)) {
			redirect = redirect.replace(HTTPS_PORT, "");
		}
		setRedirectUri(redirect);
		return redirect;
	}
}
