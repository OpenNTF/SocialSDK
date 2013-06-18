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

import com.ibm.commons.runtime.Application;
import com.ibm.commons.util.StringUtil;

public class DBCredentialStore extends BaseStore{
	
	private final String	DEFAULTJNDINAME = "jdbc/ibmsbt-dbtokenstore";
	private final String	DEFAULTDBTABLE  = "SBTKREP";
	
	static final String		sourceClass		= DBCredentialStore.class.getName();
	static final Logger		logger			= Logger.getLogger(sourceClass);
	private static boolean	driverLoaded;

	/*
	 * Properties to access the underlying db repository.
	 * If jdbc url is provided application will use that, otherwise jndi
	 * If none of these properties is set, application would try with default jndi
	 */
	private String			jdbcUrl;
	private String			jndiName;
	private String			tableName;
	private String			jdbcDriverClass;
	

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
		
		storeInDB(service,type,user,credentials);
		
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
		String application = findApplicationName();
		if(StringUtil.isEmpty(user)){ // User id could be empty for consumer tokens
			// Derive value of user from concatenation of serviceName and application
			user = serviceName+application;
		}
		try {
			Connection connection = getConnection();
			try {
				PreparedStatement stmt = connection
					.prepareStatement("SELECT CREDENTIALTOKEN FROM "+getTableName()+" WHERE APPID = ? AND SERVICENAME = ? and TYPE = ? AND USERID = ?");
				try {
				
					stmt.setString(1, application);
					stmt.setString(2, serviceName);
					stmt.setString(3, type);
					stmt.setString(4, user);
					
					ResultSet rs = stmt.executeQuery();
					try {
						if(rs.next()){
							byte[] buf = rs.getBytes(1);
							return(deSerialize(buf));
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
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "getAppToken : ", e);
			throw new CredentialStoreException(e, "DBCredentialStore.java : getAppToken caused a SQLException");
		}
		return null;
	}
	
	
	
	/*
	 * Insert app token
	 */
	private void storeInDB(String serviceName, String type, String user, Object token) throws CredentialStoreException {
		try {
			Connection connection = getConnection();
			try {
				String application = findApplicationName();
				if(StringUtil.isEmpty(user)){
					user = serviceName+application;
				}
				PreparedStatement ps = connection.prepareStatement("INSERT INTO "+getTableName()+" VALUES (?, ?, ?, ?, ?)");
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
			try {
				String application = findApplicationName();
				if(StringUtil.isEmpty(userId)){
					userId = serviceName+application;
				}
				PreparedStatement ps = connection
					.prepareStatement("DELETE FROM "+getTableName()+" WHERE APPID = ? AND SERVICENAME = ? AND USERID = ? AND TYPE = ?");
				try {
					ps.setString(1, application);
					ps.setString(2, serviceName);
					ps.setString(3, userId);
					ps.setString(4, type);
					
					ps.executeUpdate();
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
	private synchronized void loadDBDriver() throws CredentialStoreException {
		if (driverLoaded) {
			return;
		}
		String driver = getJdbcDriverClass();
		if(StringUtil.isNotEmpty(driver)){
			try {
				if(Application.getUnchecked()!=null){
					Application.getUnchecked().getClassLoader().loadClass(driver); //Class.forname does not work on OSGI
				}else{
					Class.forName(driver).newInstance(); // Load driver
				}
			driverLoaded = true;
			} catch (Exception ex) {
				logger.log(Level.SEVERE, "loadDBDriver : Could not load driver for class" + driver, ex);
				throw new CredentialStoreException(ex,"DBCredentialStore.java : loadDBDriver Could not load driver for class" + driver );
			}
		}else{
			logger.log(Level.SEVERE, "loadDBDriver : Could not find driver details");
			throw new CredentialStoreException(new Exception("DBCredentialStore.java : loadDBDriver Driver not found"));
		}
	}
	
	/*
	 * Method decides if Connection should be fetched through URL or JNDI
	 */
	
	private Connection getConnection() throws CredentialStoreException{
		if(StringUtil.isNotEmpty(getJdbcUrl())){
			try {
				loadDBDriver();
				return DriverManager.getConnection(getJdbcUrl());
			} catch (Exception e) {
				logger.log(Level.SEVERE, "DBTokenStore.java : getConnection() : ", e);
				throw new CredentialStoreException(e,"Problem occured in getting connection using JDBC" );
			}
		}else {
			return getConnectionUsingJNDI();
		}
		
	}
	
	
	/*
	 * Read the database settings from JNDI
	 */
	private Connection getConnectionUsingJNDI() throws CredentialStoreException {
		try {
			String jndikey = getJndiName();
			InitialContext initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			if(StringUtil.isEmpty(getJndiName())){
				logger.log(Level.INFO, "DBTokenStore.java : getConnectionUsingJNDI : JNDI Key was blank in bean using the default");
				jndikey = DEFAULTJNDINAME;
			}
			DataSource ds = (DataSource) envCtx.lookup(jndikey);
			return(ds.getConnection());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "DBTokenStore.java : getConnectionUsingJNDI : ", e);
			throw new CredentialStoreException(e,"Problem occured in getting connection using JNDI" );
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
	
	public String getTableName() {
		if(StringUtil.isEmpty(tableName)){
			tableName = DEFAULTDBTABLE;
		}
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
}
