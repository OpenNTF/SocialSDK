package nsf.playground.environments;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2Client;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2Store;
import com.ibm.sbt.opensocial.domino.oauth.clients.ConnectionsOAuth2Client;
import com.ibm.sbt.opensocial.domino.oauth.clients.GoogleOAuth2Client;
import com.ibm.xsp.sbtsdk.playground.sbt.extension.SbtEndpoints;

/**
 * OpenSocial OAuth 2.0 store.
 *
 */
public class PlaygroundOAuth2Store implements DominoOAuth2Store {
	private Map<String, DominoOAuth2Client> clients;
	
	/**
	 * Creates an OAuth 2.0 store from an environment.
	 * @param env The environment to create an OAuth 2.0 store from.
	 */
	public PlaygroundOAuth2Store(PlaygroundEnvironment env) {
		this.clients = new HashMap<String, DominoOAuth2Client>();
		populateClients(env.getFieldMap());
	}

	@Override
	public DominoOAuth2Client getClient(String user, String service,
			String container, String scope, String gadgetUri) {
		return clients.get(service);
	}
	
	
	private void populateClients(Map<String, String> fieldMap) {
		String connectionsServiceName = StringUtils.defaultIfBlank(fieldMap.get(SbtEndpoints.CON_GADGET_OA2_SERVICE_NAME), 
				SbtEndpoints.DEFAULT_CONNECTIONS_SERVICE_NAME);
		String googleServiceName = StringUtils.defaultIfBlank(fieldMap.get(SbtEndpoints.GOOGLE_GADGET_OA_SERVICE_NAME), 
				SbtEndpoints.DEFAULT_GOOGLE_SERVICE_NAME);
		clients.put(connectionsServiceName, createConnectionsClient(fieldMap));
		clients.put(googleServiceName, createGoogleClient(fieldMap));
	}
	
	private DominoOAuth2Client createConnectionsClient(Map<String, String> fieldMap) {
		DominoOAuth2Client client = new ConnectionsOAuth2Client(StringUtils.trim(fieldMap.get(SbtEndpoints.CON_OA2_AUTHORIZATIONURL)),
				StringUtils.trim(fieldMap.get(SbtEndpoints.CON_OA2_ACCESSTOKENURL)),
				StringUtils.trim(fieldMap.get(SbtEndpoints.CON_GADGET_OA2_CONSUMERKEY)),
				StringUtils.trim(fieldMap.get(SbtEndpoints.CON_GADGET_OA2_CONSUMERSECRET)));
		return client;
	}
	
	private DominoOAuth2Client createGoogleClient(Map<String, String> fieldMap) {
		DominoOAuth2Client client = new GoogleOAuth2Client(StringUtils.trim(fieldMap.get(SbtEndpoints.GOOGLE_GADGET_OA_CONSUMERKEY)),
				StringUtils.trim(fieldMap.get(SbtEndpoints.GOOGLE_GADGET_OA_CONSUMERSECRET)));
		return client;
	}
}
