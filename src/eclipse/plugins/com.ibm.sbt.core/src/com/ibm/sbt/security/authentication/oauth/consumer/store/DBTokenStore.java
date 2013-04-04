package com.ibm.sbt.security.authentication.oauth.consumer.store;

/*
 * © Copyright IBM Corp. 2012
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.ConsumerToken;

/**
 * @author mkataria
 * @date Dec 5, 2012
 */
public class DBTokenStore implements TokenStore {

	// Consumer token .. application token
	// Access token .. user token

	static final String		sourceClass		= DBTokenStore.class.getName();
	static final Logger		logger			= Logger.getLogger(sourceClass);

	private static boolean	driverLoaded	= false;
	private String			jndiName;
	private String			jdbcDriverClass;
	private String			defaultJndiName = "jdbc/ibmsbt-dbtokenstore";
	private TokenStoreEncryptor encryptorClass;
	
	public DBTokenStore() {
//		setJdbcDriverClass("org.apache.derby.jdbc.EmbeddedDriver");
	}

	@Override
	public ConsumerToken loadConsumerToken(String appId, String serviceName) throws OAuthException {
		return getAppToken(appId, serviceName);
	}

	@Override
	public void saveConsumerToken(String appId, String serviceName, ConsumerToken token)
			throws OAuthException {
		setAppToken(appId, serviceName, token);

	}

	@Override
	public AccessToken loadAccessToken(String appId, String serviceName, String consumerKey, String userId)
			throws OAuthException {
		return loadAccessToken(appId, serviceName, consumerKey, null, null, userId);
	}

	@Override
	public AccessToken loadAccessToken(String appId, String serviceName, String consumerKey, String moduleId,
			String tokenName, String userId) throws OAuthException {
		return getConsumerToken(appId, serviceName, userId);
	}

	@Override
	public void saveAccessToken(AccessToken token) throws OAuthException {
		setConsumerToken(token.getAppId(), token.getServiceName(), token.getConsumerKey(),
				token.getModuleId(), token.getTokenName(), token.getUserId(), token);
	}

	@Override
	public void deleteAccessToken(String appId, String serviceName, String consumerKey, String userId)
			throws OAuthException {

		deleteAccessToken(appId, serviceName, consumerKey, null, null, userId);

	}

	@Override
	public void deleteAccessToken(String appId, String serviceName, String consumerKey, String moduleId,
			String tokenName, String userId) throws OAuthException {

		deleteConsumerToken(appId, serviceName, userId);

	}

	/*
	 * DAO Specific functions
	 */


	/*
	 * Fetch the consumer token from DB
	 */
	private ConsumerToken getAppToken(String applicationId, String serviceName) throws OAuthException {
		Object token = null;
		try {
			Connection connection = getConnectionUsingJNDI();
			try {
				PreparedStatement stmt = connection
						.prepareStatement("SELECT CONSUMERTOKEN FROM APPTOKEN WHERE APPID = ? AND SERVICENAME = ?");
				try {
					stmt.setString(1, applicationId);
					stmt.setString(2, serviceName);
					ResultSet rs = stmt.executeQuery();
					try {
						token = deSerializeObject(rs);
						byte[] fetchedToken = getEncryptorClass().decrypt(convertToBytes(token));
						token = deSerializeObject(fetchedToken);
					} finally {
						rs.close();
					}
				} finally {
					stmt.close();
				}
			} finally {
				connection.close();
			}
			return (ConsumerToken) token;

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "getAppToken : ", e);
			throw new OAuthException(e, "DBTokenStore.java : getAppToken caused a SQLException");
		}
	}

	/*
	 * Insert app token
	 */
	private void setAppToken(String application, String serviceName, ConsumerToken token) throws OAuthException {
		try {
			Connection connection = getConnectionUsingJNDI();
			try {
				PreparedStatement ps = connection.prepareStatement("INSERT INTO APPTOKEN VALUES (?, ?, ?)");
				try {
					byte[] byteapptoken = convertToBytes(token);
					byteapptoken = getEncryptorClass().encrypt(byteapptoken);
					ps.setString(1, application);
					ps.setString(2, serviceName);
					ps.setObject(3, byteapptoken);
					ps.executeUpdate();
				} finally {
					ps.close();
				}
			} finally {
				connection.close();
			}

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "setAppToken : ", e);
			throw new OAuthException(e, "DBTokenStore.java : setAppToken caused a SQLException");
		}
	}

	/*
	 * Fetch user token
	 */
	private AccessToken getConsumerToken(String appId, String serviceName, String userId) throws OAuthException {
		Object token = null;
		try {
			Connection connection = getConnectionUsingJNDI();
			try {
					PreparedStatement stmt = connection
							.prepareStatement("SELECT ACCESSTOKEN FROM USERTOKEN WHERE APPID = ? AND SERVICENAME = ? AND USERID = ?");
					try {
						stmt.setString(1, appId);
						stmt.setString(2, serviceName);
						stmt.setString(3, userId);
						ResultSet rs = stmt.executeQuery();
						try {
							token = deSerializeObject(rs);
							System.err.println(" trying to print token information "+((AccessToken)token).getAppId());
							byte[] fetchedToken = getEncryptorClass().decrypt(convertToBytes(token));
							token = deSerializeObject(fetchedToken);
						} finally {
							rs.close();
						}
					} finally {
						stmt.close();
					}

			} finally {
				connection.close();
			}
			return (AccessToken) token;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new OAuthException(e, "DBTokenStore.java : getConsumerToken caused a SQLException");
		}
	}

	/*
	 * Insert user token
	 */
	private void setConsumerToken(String appId, String serviceName, String consumerKey, String moduleId,
			String tokenName, String userId, Object token) throws OAuthException {

		try {
			Connection connection = getConnectionUsingJNDI();
			try {
				PreparedStatement ps = connection
						.prepareStatement("INSERT INTO USERTOKEN VALUES (?, ?, ?, ?)");
				try {
					ps.setString(1, appId);
					ps.setString(2, serviceName);
					ps.setString(3, userId);

					byte[] data = convertToBytes(token);
					data = getEncryptorClass().encrypt(data);
					
					ps.setObject(4, data);
					ps.executeUpdate();
				} finally {
					ps.close();
				}
			} finally {
				connection.close();
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "setConsumerToken : ", e);
			throw new OAuthException(e, "DBTokenStore.java : setConsumerToken caused a SQLException");
		}
	}

	/*
	 * Delete user token
	 */
	private void deleteConsumerToken(String appId, String serviceName, String userId) throws OAuthException {
		try {
			Connection connection = getConnectionUsingJNDI();
			try {
				PreparedStatement ps = connection
						.prepareStatement("DELETE FROM USERTOKEN WHERE APPID = ? AND SERVICENAME = ? AND USERID = ?");
				try {
					ps.setString(1, appId);
					ps.setString(2, serviceName);
					ps.setString(3, userId);
					ps.execute();
				} finally {
					ps.close();
				}

			} finally {
				connection.close();
			}

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "deleteConsumerToken : ", e);
			throw new OAuthException(e, "DBTokenStore.java : deleteConsumerToken caused a SQLException");
		}
	}

	/*
	 * Bean Properties
	 */

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public String getJdbcDriverClass() {
		return jdbcDriverClass;
	}

	public void setJdbcDriverClass(String jdbcDriverClass) {
		this.jdbcDriverClass = jdbcDriverClass;
	}
	
	public TokenStoreEncryptor getEncryptorClass() {
		return encryptorClass;
	}

	public void setEncryptorClass(TokenStoreEncryptor encryptorClass) {
		this.encryptorClass = encryptorClass;
	}

	// All utility methods here
	
	/*
	 * Method for de-serializing the object from db to java object
	 */
	private Object deSerializeObject(ResultSet rs) throws OAuthException{
		try {
			rs.next();
			byte[] buf = rs.getBytes(1);
			if (buf != null) {
					Object deSerializedObject = deSerializeObject(buf);
					return deSerializedObject;
				} 
		} catch (Exception e) {
			throw new OAuthException(e);
		}
		return null;
	}
	
	
	/*
	 * Method for de-serializing the object from bytes to java object
	 */
	private Object deSerializeObject(byte[] tokendata) throws OAuthException{
		try {
			ObjectInputStream objectIn = null;
			if (tokendata != null) {
					objectIn = new ObjectInputStream(new ByteArrayInputStream(tokendata));
					Object deSerializedObject = objectIn.readObject();
					return deSerializedObject;
				} 
		} catch (Exception e) {
			throw new OAuthException(e);
		}
		return null;
	}
	
	/*
	 * Read the database settings from JNDI
	 */
	private Connection getConnectionUsingJNDI() throws OAuthException {
		loadDBDriver();
		try {
			Connection conn = null;
			String jndikey = getJndiName();
			InitialContext initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			if(StringUtil.isEmpty(getJndiName())){
				logger.log(Level.INFO, "DBTokenStore.java : getConnectionUsingJNDI : JNDI Key was blank in bean using the default");
				jndikey = defaultJndiName;
			}
			DataSource ds = (DataSource) envCtx.lookup(jndikey);
			conn = ds.getConnection();
			return conn;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "DBTokenStore.java : getConnectionUsingJNDI : ", e);
			throw new OAuthException(e,"DBTokenStore.java : getConnectionUsingJNDI caused exeption" );
		}
	}
	
	/*
	 * Converts the input object to byte array for serializing into DB
	 */
	public byte[] convertToBytes(Object object) throws OAuthException {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);

			oos.writeObject(object);
			oos.flush();
			oos.close();
			bos.close();

			byte[] data = bos.toByteArray();
			return data;
		} catch (Exception e) {
			throw new OAuthException(e,"DBTokenStore.java : getConnectionUsingJNDI caused exeption" );
		}
	}
	
	/*
	 * Load the DB Drivers
	 */
	private void loadDBDriver() throws OAuthException {
		if (driverLoaded) {
			return;
		}
		String driver = getJdbcDriverClass();
		try {
			Class.forName(driver).newInstance(); // Load driver
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "loadDBDriver : Could not load driver for class" + driver, ex);
			throw new OAuthException(ex,"DBTokenStore.java : loadDBDriver Could not load driver for class" + driver );
		}
		driverLoaded = true;
	}

}
