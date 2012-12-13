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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.ConsumerToken;

/**
 * @author mkataria
 * @date Dec 5, 2012
 */
public class DBTokenStore implements TokenStore {

	// Consumer token .. app token
	// Access token .. user token

	static final String		sourceClass		= DBTokenStore.class.getName();
	static final Logger		logger			= Logger.getLogger(sourceClass);

	private static boolean	driverLoaded	= false;
	private String			jndiName;
	private String			jdbcDriverClass;
	private String			jdbcUrl;
	private String			jdbcUser;
	private String			jdbcPassword;

	public DBTokenStore() {
		System.err.println("Token store initialised");
	}

	@Override
	public ConsumerToken loadConsumerToken(String appId, String serviceName) throws OAuthException {
		System.err.println("loadConsumerToken called");
		return getAppToken(appId, serviceName);
	}

	@Override
	public void saveConsumerToken(String appId, String serviceName, ConsumerToken token)
			throws OAuthException {
		System.err.println("saveConsumerToken called");
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
		return getConsumerToken(appId, serviceName, consumerKey, moduleId, tokenName, userId);
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

		deleteConsumerToken(appId, serviceName, consumerKey, moduleId, tokenName, userId);

	}

	/*
	 * DAO Specific functions
	 */

	private void loadDBDriver() {
		if (driverLoaded) {
			return;
		}

		String driver = getJdbcDriverClass();
		try {
			Class.forName(driver).newInstance(); // Load driver
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "loadDBDriver : Could not load driver for class" + driver, ex);
		}
		driverLoaded = true;
		return;
	}

	/*
	 * Converts the input object to byte array for serializing into DB
	 */
	public byte[] convertToBytesForUpload(Object object) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);

			oos.writeObject(object);
			oos.flush();
			oos.close();
			bos.close();

			byte[] data = bos.toByteArray();
			return data;
		} catch (Exception e) {}
		return null;
	}

	public ConsumerToken getAppToken(String applicationId, String serviceName) {
		Object deSerializedObject = null;
		try {
			loadDBDriver();
			Connection connection = DriverManager.getConnection(getJdbcUrl(), getJdbcUser(),
					getJdbcPassword());
			try {
				PreparedStatement stmt = connection
						.prepareStatement("SELECT CONSUMERTOKEN FROM APPTOKEN WHERE APPID = ? AND SERVICENAME = ?");
				try {
					stmt.setString(1, applicationId);
					stmt.setString(2, serviceName);
					ResultSet rs = stmt.executeQuery();
					try {
						rs.next();
						byte[] buf = rs.getBytes(1);
						ObjectInputStream objectIn = null;

						if (buf != null) {
							try {
								objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
								deSerializedObject = objectIn.readObject();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
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
			return (ConsumerToken) deSerializedObject;

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "getAppToken : ", e);
		}
		return null;
	}

	public void setAppToken(String application, String serviceName, ConsumerToken token) {
		try {
			loadDBDriver();
			Connection connection = DriverManager.getConnection(getJdbcUrl(), getJdbcUser(),
					getJdbcPassword());
			try {
				PreparedStatement ps = connection.prepareStatement("INSERT INTO APPTOKEN VALUES (?, ?, ?)");
				try {
					byte[] byteapptoken = convertToBytesForUpload(token);
					ps.setString(1, application);
					ps.setString(2, serviceName);
					ps.setObject(3, byteapptoken);
					ps.executeUpdate();
					connection.commit();
				} finally {
					ps.close();
				}
			} finally {
				connection.close();
			}

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "setAppToken : ", e);
		}
	}

	public AccessToken getConsumerToken(String appId, String serviceName, String consumerKey,
			String moduleId, String tokenName, String userId) {
		Object deSerializedObject = null;
		try {
			loadDBDriver();
			Connection connection = DriverManager.getConnection(getJdbcUrl(), getJdbcUser(),
					getJdbcPassword());
			try {
				if (StringUtil.isNotEmpty(moduleId) && StringUtil.isNotEmpty(tokenName)) {
					PreparedStatement stmt = connection
							.prepareStatement("SELECT ACCESSTOKEN FROM USERTOKEN WHERE APPID = ? AND SERVICENAME = ? AND CONSUMERKEY = ? AND MODULEID = ? AND TOKENNAME = ? AND USERID = ?");
					try {
						stmt.setString(1, appId);
						stmt.setString(2, serviceName);
						stmt.setString(3, consumerKey);
						stmt.setString(4, moduleId);
						stmt.setString(5, tokenName);
						stmt.setString(6, userId);
						ResultSet rs = stmt.executeQuery();
						try {
							rs.next();
							byte[] buf = rs.getBytes(1);
							ObjectInputStream objectIn = null;
							if (buf != null) {
								try {
									objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
									deSerializedObject = objectIn.readObject();
								} catch (IOException e) {} catch (ClassNotFoundException e) {}
							}
						} finally {
							rs.close();
						}

					} finally {
						stmt.close();
					}

				} else {
					PreparedStatement stmt = connection
							.prepareStatement("SELECT ACCESSTOKEN FROM USERTOKEN WHERE APPID = ? AND SERVICENAME = ? AND CONSUMERKEY = ?  AND USERID = ?");
					try {
						stmt.setString(1, appId);
						stmt.setString(2, serviceName);
						stmt.setString(3, consumerKey);
						stmt.setString(4, userId);
						ResultSet rs = stmt.executeQuery();
						try {
							rs.next();
							byte[] buf = rs.getBytes(1);
							ObjectInputStream objectIn = null;
							if (buf != null) {
								try {
									objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
									deSerializedObject = objectIn.readObject();
								} catch (IOException e) {} catch (ClassNotFoundException e) {}
							}
						} finally {
							rs.close();
						}
					} finally {
						stmt.close();
					}
				}

			} finally {
				connection.close();
			}
			return (AccessToken) deSerializedObject;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public void setConsumerToken(String appId, String serviceName, String consumerKey, String moduleId,
			String tokenName, String userId, Object token) {

		try {
			loadDBDriver();
			Connection connection = DriverManager.getConnection(getJdbcUrl(), getJdbcUser(),
					getJdbcPassword());
			try {
				PreparedStatement ps = connection
						.prepareStatement("INSERT INTO USERTOKEN VALUES (?, ?, ?, ?, ?, ?, ?)");
				try {
					ps.setString(1, appId);
					ps.setString(2, serviceName);
					ps.setString(3, consumerKey);
					ps.setString(4, moduleId);
					ps.setString(5, tokenName);
					ps.setString(6, userId);

					byte[] data = convertToBytesForUpload(token);
					ps.setObject(7, data);
					ps.executeUpdate();
					connection.commit();
				} finally {
					ps.close();
				}
			} finally {
				connection.close();
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "setConsumerToken : ", e);
		}
	}

	public void deleteConsumerToken(String appId, String serviceName, String consumerKey, String moduleId,
			String tokenName, String userId) {
		try {
			loadDBDriver();
			Connection connection = DriverManager.getConnection(getJdbcUrl(), getJdbcUser(),
					getJdbcPassword());
			try {
				PreparedStatement ps = connection
						.prepareStatement("DELETE FROM USERTOKEN VALUES (?, ?, ?, ?, ?, ?, ?)");
				try {
					ps.setString(1, appId);
					ps.setString(2, serviceName);
					ps.setString(3, consumerKey);
					ps.setString(4, moduleId);
					ps.setString(5, tokenName);
					ps.setString(6, userId);

					ps.execute();
					connection.commit();
				} finally {
					ps.close();
				}

			} finally {
				connection.close();
			}

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "deleteConsumerToken : ", e);
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
		System.err.println("DBTokenStore.java : setJdbcDriverClass" + jdbcDriverClass);
		this.jdbcDriverClass = jdbcDriverClass;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getJdbcUser() {
		return jdbcUser;
	}

	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}

	public String getJdbcPassword() {
		return jdbcPassword;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

}
