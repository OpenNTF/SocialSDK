package com.ibm.sbt.security.authentication.oauth.consumer.store;

import java.util.Calendar;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;

public class dbtester {

	public static void main(String[] args) {
		try {

			System.err.println("INSERTING tokens ");

			new AccessToken("manishapplicationid", "makservice", "makconsumerkey", "accesstoken",
					"tokensecret", "makuserid", Calendar.getInstance().getTime(), Calendar.getInstance()
							.getTime(), "sessionhandle"

			);

			System.err.println("INSERTING 2ND tokens ");

			new AccessToken("manishapplicationid", "makservice", "makconsumerkey", "accesstoken",
					"tokensecretfor2", "makuserid2", Calendar.getInstance().getTime(), Calendar.getInstance()
							.getTime(), "sessionhandle"

			);

			System.err.println("INSERTING 3RD tokens ");
			new AccessToken("manishapplicationid", "makservice", "makconsumerkey", "accesstoken",
					"tokensecretfor3", "makuserid3", Calendar.getInstance().getTime(), Calendar.getInstance()
							.getTime(), "sessionhandle"

			);

			DBTokenStore tokenstore = new DBTokenStore();
			tokenstore.setJdbcDriverClass("org.apache.derby.jdbc.EmbeddedDriver");
			tokenstore.setJdbcUser("manish");
			tokenstore.setJdbcPassword("manish");
			tokenstore
					.setJdbcUrl("jdbc:derby:F:\\official\\AppDev\\DBWORK\\SBTK;bootPassword=manishsbtkrepository';user=sbtkadmin;password=sbtkadmin");
			/*
			 * tokenstore.saveAccessToken(accesstoken); tokenstore.saveAccessToken(accesstoken2); tokenstore.saveAccessToken(accesstoken3);
			 */

			System.err.println("retrive tokens ");

			AccessToken rettoken = tokenstore.loadAccessToken("manishapplicationid", "makservice",
					"makconsumerkey", "makuserid");
			System.err.println("Token secret is " + rettoken.getTokenSecret());

			rettoken = tokenstore.loadAccessToken("manishapplicationid", "makservice", "makconsumerkey",
					"makuserid2");
			System.err.println("Token secret is " + rettoken.getTokenSecret());

			rettoken = tokenstore.loadAccessToken("manishapplicationid", "makservice", "makconsumerkey",
					"makuserid3");
			System.err.println("Token secret is " + rettoken.getTokenSecret());

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
