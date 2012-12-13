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
package com.ibm.sbt.services.util;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.ClientService.Args;




/**
 * SSL Utilities.
 * 
 * @author Philippe Riand
 */
public class SSLUtil {

    // Wrap for trusting all the certificates
    public static DefaultHttpClient wrapHttpClient(DefaultHttpClient base) {
        try {
            // Create and assign a dummy TrustManager
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
				public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override
				public void checkClientTrusted(X509Certificate[] cert, String s) throws CertificateException {
                }
                @Override
				public void checkServerTrusted(X509Certificate[] cert, String s) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            
            // When Apache Client AllowAllHostnameVerifier is strict, this should be used
            // Stays here for reference
            X509HostnameVerifier verifier = new X509HostnameVerifier() {
                @Override
				public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
                @Override
				public void verify(String s, SSLSocket sslSession) throws IOException {
                }
                @Override
				public void verify(String s, String[] ss1, String[] ss2) throws SSLException {
                }
                @Override
				public void verify(String s, X509Certificate cerst) throws SSLException {
                }
                
            };
            ssf.setHostnameVerifier(verifier);

            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
