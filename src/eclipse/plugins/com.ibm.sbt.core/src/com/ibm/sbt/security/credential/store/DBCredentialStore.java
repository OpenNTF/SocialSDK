package com.ibm.sbt.security.credential.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.oauth.consumer.ConsumerToken;
import com.ibm.sbt.security.authentication.oauth.consumer.store.TokenStoreEncryptor;

public class DBCredentialStore extends BaseStore{
	
	static final String		sourceClass		= DBCredentialStore.class.getName();
	static final Logger		logger			= Logger.getLogger(sourceClass);
	private static boolean	driverLoaded	= false;

	private String			jdbcDriverClass;
	private String			defaultJndiName = "jdbc/ibmsbt-dbtokenstore";
	
	/*
	 * Properties to access the underlying db repository.
	 * If jdbc url is provided application will use that, otherwise jndi
	 * If none of these properties is set, application would try with default jndi
	 */
	private String			jdbcUrl;
	private String			jndiName;
	

	@Override
	public Object load(String service, String type,
			String user)
			throws CredentialStoreException {
		
		return getFromDB(service, type, user);
	}

	@Override
	public void store(String service, String type,
			String user, Object credentials)
			throws CredentialStoreException {
		
		storeinDB(service,type,user,credentials);
		
	}

	@Override
	public void remove(String service, String type,
			String user) throws CredentialStoreException {
		
		removeFromDB(service, type, user);
		
	}
	
	/*
	 * Fetch the consumer token from DB
	 */
	private Object getFromDB(String serviceName, String type, String user) throws CredentialStoreException {
		Object token = null;
		String application = findApplicationName();
		try {
			Connection connection = getConnection();
			try {
				PreparedStatement stmt = connection
					.prepareStatement("SELECT CREDENTIALTOKEN FROM SBTKREP WHERE APPID = ? AND SERVICENAME = ? and TYPE = ? AND USERID = ?");
				try {
					stmt.setString(1, application);
					stmt.setString(2, serviceName);
					stmt.setString(3, type);
					stmt.setString(4, user);
					
					ResultSet rs = stmt.executeQuery();
					rs.next();
					try {
						byte[] buf = rs.getBytes(1);
						token = deSerialize(buf);
					} finally {
						rs.close();
					}
				} finally {
					stmt.close();
				}
			} finally {
				connection.close();
			}
			return (Object) token;

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "getAppToken : ", e);
			throw new CredentialStoreException(e, "DBCredentialStore.java : getAppToken caused a SQLException");
		}
	}
	
	
	
	/*
	 * Insert app token
	 */
	private void storeinDB(String serviceName, String type, String user, Object token) throws CredentialStoreException {
		try {
			Connection connection = getConnection();
			String application = findApplicationName();
			try {
				PreparedStatement ps = connection.prepareStatement("INSERT INTO SBTKREP VALUES (?, ?, ?, ?, ?)");
				try {
					byte[] apptoken = serialize(token);
					ps.setString(1, application);
					ps.setString(2, serviceName);
					ps.setObject(3, type);
					ps.setObject(4, user);
					ps.setObject(5, apptoken);
					ps.executeUpdate();
				} finally {
					ps.close();
				}
			} finally {
				connection.close();
			}

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "setAppToken : ", e);
			throw new CredentialStoreException(e, "DBTokenStore.java : setAppToken caused a SQLException");
		}
	}
	
	
	/*
	 * Delete user token
	 */
	private void removeFromDB(String serviceName, String type, String userId) throws CredentialStoreException {
		try {
			Connection connection = getConnection();
			String application = findApplicationName();
			try {
				PreparedStatement ps = connection
					.prepareStatement("DELETE FROM SBTKREP WHERE APPID = ? AND SERVICENAME = ? AND USERID = ? AND TYPE = ?");
				try {
					ps.setString(1, application);
					ps.setString(2, serviceName);
					ps.setString(3, userId);
					ps.setString(4, type);
					ps.execute();
				} finally {
					ps.close();
				}

			} finally {
				connection.close();
			}

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "deleteConsumerToken : ", e);
			throw new CredentialStoreException(e, "DBTokenStore.java : deleteConsumerToken caused a SQLException");
		}
	}
	
	/*
	 * Load the DB Drivers
	 */
	private void loadDBDriver() throws CredentialStoreException {
		if (driverLoaded) {
			return;
		}
		String driver = getJdbcDriverClass();
		try {
			Class.forName(driver).newInstance(); // Load driver
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "loadDBDriver : Could not load driver for class" + driver, ex);
			throw new CredentialStoreException(ex,"DBCredentialStore.java : loadDBDriver Could not load driver for class" + driver );
		}
		driverLoaded = true;
	}
	
	/*
	 * Method decides if Connection should be fetched through URL or JNDI
	 */
	
	private Connection getConnection() throws CredentialStoreException{
		loadDBDriver();
		if(StringUtil.isNotEmpty(getJdbcUrl())){
			Connection conn;
			try {
				conn = DriverManager.getConnection(getJdbcUrl());
				return conn;
			} catch (Exception e) {
				logger.log(Level.SEVERE, "DBTokenStore.java : getConnection() : ", e);
				throw new CredentialStoreException(e,"DBCredentialStore.java : getConnection() caused exeption" );
			}
		}else{
			return getConnectionUsingJNDI();
		}
	}
	
	
	/*
	 * Read the database settings from JNDI
	 */
	private Connection getConnectionUsingJNDI() throws CredentialStoreException {
		
		try {
			Connection conn;
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
			throw new CredentialStoreException(e,"DBCredentialStore.java : getConnectionUsingJNDI caused exeption" );
		}
	}
	
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
	
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	
}
