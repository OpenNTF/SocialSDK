package nsf.playground.environments;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ibm.sbt.opensocial.domino.oauth.DominoOAuthClient;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuthStore;
import com.ibm.sbt.opensocial.domino.oauth.clients.DropBoxOAuthClient;
import com.ibm.sbt.opensocial.domino.oauth.clients.SmartCloudOAuthClient;
import com.ibm.xsp.sbtsdk.playground.sbt.extension.SbtEndpoints;

/**
 * An OpenSocial OAuth 1.0a store.
 *
 */
public class PlaygroundOAuthStore implements DominoOAuthStore {
	private Map<String, DominoOAuthClient> clients;
	
	/**
	 * Creates an OpenSocial 1.0a store from an environment.
	 * @param env The environment to create the store from.
	 */
	public PlaygroundOAuthStore(PlaygroundEnvironment env) {
		clients = new HashMap<String, DominoOAuthClient>();
		populateOAuthClients(env.getFieldMap());
	}

	private void populateOAuthClients(Map<String, String> fieldMap) {
		clients.put(StringUtils.defaultIfBlank(fieldMap.get(SbtEndpoints.SMA_OA_GADGET_SERVICE), SbtEndpoints.DEFAULT_SC_SERVICE_NAME), 
				createSmartCloudClient(fieldMap));
		clients.put(StringUtils.defaultIfBlank(fieldMap.get(SbtEndpoints.DROPBOX_GADGET_OA_SERVICE_NAME), SbtEndpoints.DEFAULT_DROPBOX_SERVICE_NAME), 
				createDropBoxClient(fieldMap));
	}

	private DominoOAuthClient createDropBoxClient(Map<String, String> fieldMap) {
		DominoOAuthClient client = new DropBoxOAuthClient(StringUtils.trim(fieldMap.get(SbtEndpoints.DROPBOX_OA_CONSUMERKEY)),
				StringUtils.trim(fieldMap.get(SbtEndpoints.DROPBOX_OA_CONSUMERSECRET)));
		return client;
	}
	
	private DominoOAuthClient createSmartCloudClient(Map<String, String> fieldMap) {
		DominoOAuthClient client = new SmartCloudOAuthClient(StringUtils.trim(fieldMap.get(SbtEndpoints.SMA_OA_COUNSUMERKEY)),
				StringUtils.trim(fieldMap.get(SbtEndpoints.SMA_OA_CONSUMERSECRET)));
		return client;
	}

	@Override
	public DominoOAuthClient getClient(String user, String container,
			String service, String gadgetUri) {
		return clients.get(service);
	}
}