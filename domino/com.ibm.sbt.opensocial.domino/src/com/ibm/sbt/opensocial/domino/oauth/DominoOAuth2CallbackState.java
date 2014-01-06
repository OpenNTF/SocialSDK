package com.ibm.sbt.opensocial.domino.oauth;

import java.util.Map;
import java.util.logging.Level;

import org.apache.shindig.common.crypto.BlobCrypter;
import org.apache.shindig.common.crypto.BlobCrypterException;
import org.apache.shindig.gadgets.oauth2.OAuth2CallbackState;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.ibm.sbt.opensocial.domino.internal.OpenSocialPlugin;

/**
 * An OAuth 2.0 callback state object that includes the container.
 *
 */
public class DominoOAuth2CallbackState extends OAuth2CallbackState {
	private static final long serialVersionUID = -2681620021214587536L;
	private static final String GADGET_URI_KEY = "g";
	private static final String CONTAINER_KEY = "c";
	private static final String SERVICE_NAME = "sn";
	private static final String USER = "u";
	private static final String SCOPE = "sc";
	private static final String CLASS = OAuth2CallbackState.class.getName();
	
	private transient BlobCrypter crypter;
	private Map<String, String> state;

	/**
	 * Creates a new DominoOAuth2CallbackState object.
	 */
	public DominoOAuth2CallbackState() {
		this.state = Maps.newHashMapWithExpectedSize(5);
		this.crypter = null;
	}

	/**
	 * Creates a new DominoOAuth2CallbackState object.
	 * @param crypter The crypter to use to encrypt and decrypt the callback state.
	 */
	public DominoOAuth2CallbackState(final BlobCrypter crypter) {
		this();
		this.crypter = crypter;
	}

	/**
	 * Creates a new DominoOAuth2CallbackState object.
	 * @param crypter The crypter to use to encrypt and decrypt the callback state.
	 * @param stateBlob The encrypted state blob to instantiate the callback object with.
	 */
	public DominoOAuth2CallbackState(final BlobCrypter crypter, final String stateBlob) {
		final String method = "constructor";
		this.crypter = crypter;

		Map<String, String> state = null;
		if (stateBlob != null && crypter != null) {
			try {
				state = crypter.unwrap(stateBlob);

				if (state == null) {
					state = Maps.newHashMap();
				}
				this.state = state;
			} catch (final BlobCrypterException e) {
				// Too old, or corrupt. Ignore it.
				state = null;
				OpenSocialPlugin.getLogger().logp(Level.WARNING, CLASS, method, "Error decrypting state.", e);
			}
		}
		if (state == null) {
			this.state = Maps.newHashMapWithExpectedSize(5);
		}
	}
	
	@Override
	public String getEncryptedState() throws BlobCrypterException {
		String ret = null;
		if (this.crypter != null) {
			ret = this.crypter.wrap(this.state);
		}
		return ret;
	}

	@Override
	public String getGadgetUri() {
		return this.state.get(GADGET_URI_KEY);
	}

	@Override
	public void setGadgetUri(String gadgetUri) {
		this.state.put(GADGET_URI_KEY, gadgetUri);
	}

	@Override
	public String getServiceName() {
		return this.state.get(SERVICE_NAME);
	}

	@Override
	public void setServiceName(String serviceName) {
		this.state.put(SERVICE_NAME, serviceName);
	}

	@Override
	public String getUser() {
		return this.state.get(USER);
	}

	@Override
	public void setUser(String user) {
		this.state.put(USER, user);
	}

	@Override
	public String getScope() {
		return this.state.get(SCOPE);
	}

	@Override
	public void setScope(String scope) {
		this.state.put(SCOPE, scope);
	}
	
	/**
	 * Gets the container.
	 * @return The container.
	 */
	public String getContainer() {
		return this.state.get(CONTAINER_KEY);
	}

	/**
	 * Sets the container.
	 * @param container The container.
	 */
	public void setContainer(String container) {
		this.state.put(CONTAINER_KEY, container);
	}

	@Override
	public boolean equals(Object o) {
		boolean result = false;
		if(o instanceof OAuth2CallbackState) {
			DominoOAuth2CallbackState state = (DominoOAuth2CallbackState)o;
			result = getContainer() != null ? getContainer().equals(state.getContainer()) : getContainer() == state.getContainer();
			result &= getGadgetUri() != null ? getGadgetUri().equals(state.getGadgetUri()) : getGadgetUri() == state.getGadgetUri();
			result &= getScope() != null ? getScope().equals(state.getScope()) : getScope() == state.getScope();
			result &= getServiceName() != null ? getServiceName().equals(state.getServiceName()) : getServiceName() == state.getServiceName();
			result &= getUser() != null ? getUser().equals(state.getUser()) : getUser() == state.getUser();
		}
		return result;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(state);
	}
	
	
}
