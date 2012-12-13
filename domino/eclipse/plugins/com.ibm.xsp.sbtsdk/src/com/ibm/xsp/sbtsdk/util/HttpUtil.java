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

package com.ibm.xsp.sbtsdk.util;

import java.util.Vector;

import lotus.domino.Session;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.util.ExtLibUtil;



public class HttpUtil {

	private static Integer httpsPort;
	
	public static int findHttpsPort() {
		if(httpsPort==null) {
			synchronized(HttpUtil.class) {
				try {
					Session session = ExtLibUtil.getCurrentSessionAsSigner();
					String server = session.getServerName();
					String nabCandidates = session.getEnvironmentString("names",true); // There is a possibility that the variable is names=names.nsf,somotherdb.nsf
					String nab = StringUtil.isNotEmpty(nabCandidates) ? StringUtil.splitString(nabCandidates,',')[0] : "names.nsf";
					String formula = "@DbLookup(\"Notes\":\"Cache\";\""+server+"\":\""+nab+"\";\"($Servers)\";\""+server+"\";\"HTTP_SSLPort\";[FAILSILENT])";
					Vector<?> result = session.evaluate(formula);
					if(result.size()==1) {
						Object port = result.get(0);
						if(port instanceof Number) {
							httpsPort = ((Number)port).intValue();
						} else if(port instanceof Number) {
							httpsPort = Integer.parseInt((String)port);
						}
					}
				} catch(Exception e) {
					Platform.getInstance().log(e);
				}
				if(httpsPort==null) {
					httpsPort = 443; // Default for SSL...
				}
			}
		}
		return httpsPort;
	}

}
