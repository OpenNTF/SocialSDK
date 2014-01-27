package demo;

import com.ibm.sbt.opensocial.domino.config.DefaultContainerConfig;
import com.ibm.sbt.opensocial.domino.config.OpenSocialContainerConfig;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPoint;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointException;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2Client;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2Store;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuthClient;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuthStore;

public class SocialEnablerContainerExtPoint implements ContainerExtPoint {

	private OpenSocialContainerConfig config = new DefaultContainerConfig();

	public OpenSocialContainerConfig getContainerConfig() {
		return config;
	}

	public DominoOAuth2Store getContainerOAuth2Store()
			throws ContainerExtPointException {
		return new DominoOAuth2Store() {

			public DominoOAuth2Client getClient(String arg0, String arg1,
					String arg2, String arg3, String arg4) {
				return null;
			}
			
		};
	}

	public String getId() throws ContainerExtPointException {
		return "com.ibm.sbt.socialenablercontainer";
	}

	public DominoOAuthStore getContainerOAuthStore()
			throws ContainerExtPointException {
		return new DominoOAuthStore() {

			public DominoOAuthClient getClient(String arg0, String arg1,
					String arg2, String arg3) {
				return null;
			}
			
		};
	}

}
