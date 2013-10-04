/*
 * © Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package grantaccess.webapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.sbt.security.credential.store.CredentialStore;
import com.ibm.sbt.security.credential.store.CredentialStoreException;
import com.ibm.sbt.security.credential.store.CredentialStoreFactory;
import com.ibm.sbt.security.credential.store.DBCredentialStore;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.communities.CommunityList;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.endpoints.ConnectionsOAuth2Endpoint;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.services.endpoints.SmartCloudOAuthEndpoint;

/**
 * @author mwallace
 *
 */
public class Users {
	
	private final static String DEFAULT_JNDINAME = "jdbc/ibmsbt-dbtokenstore";
	
	private static boolean driverLoaded;
	
	private static DBCredentialStore store;
	
	static public List<String> getUsers() {
		List<String> users = new ArrayList<String>();
		try {
			if (store == null) {
				store = (DBCredentialStore)CredentialStoreFactory.getCredentialStore("CredStoreDB");
			}
			Connection connection = getConnection();
			try {
				PreparedStatement stmt = connection.prepareStatement("SELECT USERID FROM "+store.getTableName()+" WHERE APPID = ? AND SERVICENAME = ?");
				try {
					stmt.setString(1, store.findApplicationName());
					stmt.setString(2, "localga");
					
					ResultSet rs = stmt.executeQuery();
					try {
						while(rs.next()) {
							users.add(rs.getString(1));
						}
					} finally {
						rs.close();
					}
				} finally {
					stmt.close();
				}
			} finally {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}
	
	static public int getCommunityMembershipCount(String user) {
		int ret = -1;
		try {
			Endpoint endpoint = createEndpoint(user);
			if (endpoint != null) {
				CommunityService service = new CommunityService(endpoint);
			
				CommunityList list = service.getMyCommunities();
				return list.getTotalResults();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	static private Endpoint createEndpoint(String user) throws ClientServicesException {
		Endpoint endpoint = getEndpoint("connections");
		if (endpoint instanceof SmartCloudOAuthEndpoint) {
			SmartCloudOAuthEndpoint smartcloud = (SmartCloudOAuthEndpoint)endpoint;
			SmartCloudOAuthEndpoint cloned = new SmartCloudOAuthEndpoint();
			cloned.setApiVersion(smartcloud.getApiVersion());
			cloned.setUrl(smartcloud.getUrl());
			cloned.setForceTrustSSLCertificate(smartcloud.isForceTrustSSLCertificate());
			cloned.setConsumerKey(smartcloud.getConsumerKey());
			cloned.setConsumerSecret(smartcloud.getConsumerSecret());
			cloned.setRequestTokenURL(smartcloud.getRequestTokenURL());
			cloned.setAuthorizationURL(smartcloud.getAuthorizationURL());
			cloned.setAccessTokenURL(smartcloud.getAccessTokenURL());
			cloned.setSignatureMethod(smartcloud.getSignatureMethod());
			cloned.setCredentialStore(smartcloud.getCredentialStore());
			cloned.setServiceName(smartcloud.getServiceName());
			cloned.setAppId(smartcloud.getAppId());
			cloned.setAuthenticationService(smartcloud.getAuthenticationService());
			if (cloned.login(user)) {
				return cloned;
			}
		}
		if (endpoint instanceof ConnectionsOAuth2Endpoint) {
			ConnectionsOAuth2Endpoint connections = (ConnectionsOAuth2Endpoint)endpoint;
			ConnectionsOAuth2Endpoint cloned = new ConnectionsOAuth2Endpoint();
			cloned.setApiVersion(connections.getApiVersion());
			cloned.setUrl(connections.getUrl());
			cloned.setForceTrustSSLCertificate(connections.isForceTrustSSLCertificate());
			cloned.setConsumerKey(connections.getConsumerKey());
			cloned.setConsumerSecret(connections.getConsumerSecret());
			cloned.setAuthorizationURL(connections.getAuthorizationURL());
			cloned.setAccessTokenURL(connections.getAccessTokenURL());
			cloned.setCredentialStore(connections.getCredentialStore());
			cloned.setServiceName(connections.getServiceName());
			cloned.setAppId(connections.getAppId());
			cloned.setAuthenticationService(connections.getAuthenticationService());
			if (cloned.login(user)) {
				return cloned;
			}
		}
		
		return null;
	}
	
	/*
	 * Return the endpoint with the specified alias
	 */
    static private Endpoint getEndpoint(String endpointName){
    	com.ibm.commons.runtime.Context context = com.ibm.commons.runtime.Context.getUnchecked();
    	if (context == null) {
    		return null;
    	}
        String environment = context.getProperty("environment");
        environment = (environment == null) ? "defaultEnvironment" : environment;
        if(environment != null) {
            SBTEnvironment env = (SBTEnvironment) context.getBean(environment);
            SBTEnvironment.Endpoint[] endpointsArray = env.getEndpointsArray();
            for(SBTEnvironment.Endpoint endpoint : endpointsArray){
                if(StringUtil.equals(endpointName, endpoint.getAlias())){
                    endpointName = endpoint.getName();
                    break;
                } else if (StringUtil.equals(endpointName, endpoint.getName())){
                    break;
                }
            }
        }
        return EndpointFactory.getEndpoint(endpointName);
    }
	
	/*
	 * Load the DB Drivers
	 */
	static private synchronized void loadDBDriver() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		if (driverLoaded) {
			return;
		}
		String driver = store.getJdbcDriverClass();
		if(StringUtil.isNotEmpty(driver)){
			if(Application.getUnchecked()!=null){
				Application.getUnchecked().getClassLoader().loadClass(driver); //Class.forname does not work on OSGI
			}else{
				Class.forName(driver).newInstance(); // Load driver
			}
			driverLoaded = true;
		}
	}
	
	/*
	 * Method decides if Connection should be fetched through URL or JNDI
	 */
	
	static private Connection getConnection() throws CredentialStoreException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, NamingException{
		if(StringUtil.isNotEmpty(store.getJdbcUrl())){
			loadDBDriver();
			return DriverManager.getConnection(store.getJdbcUrl());
		}else {
			return getConnectionUsingJNDI();
		}
	}
	
	/*
	 * Read the database settings from JNDI
	 */
	static private Connection getConnectionUsingJNDI() throws CredentialStoreException, NamingException, SQLException {
		String jndikey = store.getJndiName();
		InitialContext initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		if(StringUtil.isEmpty(jndikey)){
			jndikey = DEFAULT_JNDINAME;
		}
		DataSource ds = (DataSource) envCtx.lookup(jndikey);
		return ds.getConnection();
	}	

}
