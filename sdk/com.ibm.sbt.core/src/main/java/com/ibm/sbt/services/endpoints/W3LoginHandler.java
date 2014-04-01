package com.ibm.sbt.services.endpoints;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.soap.Node;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.w3c.dom.Document;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.NamespaceContextImpl;
import com.ibm.commons.xml.io.XmlSerializer;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespaces;

public class W3LoginHandler implements FormLoginHandler {

	@Override
	public boolean login(String user, String password, HttpClient client, CookieStore cs, String url) throws AuthenticationException {
		try {
		
		HttpResponse resp = client.execute(RequestBuilder.get().setUri(url).build());
		check(resp,200);
		extractHtmlCookie(cs, resp);
		resp = client.execute(RequestBuilder.post().
				setUri("https://w3-03.sso.ibm.com/pkmslogin.form").
				addParameter("ibm-submit", "Sign in").
				addParameter("login-form-type","pwd").
				addParameter("password",password).
				addParameter("username", user).
				build()
				);
		//200 means invalid password. api returns 302 on success.
		if (resp.getStatusLine().getStatusCode() == 200) return false;
		check(resp,302);
		String postLoginUrl = resp.getFirstHeader("Location").getValue();
		consume(resp);
		resp = client.execute(RequestBuilder.get().setUri(postLoginUrl).build());
		extractHtmlCookie(cs, resp);
		String samlDomain = "https://w3-03.sso.ibm.com";
		String samlAuthenticator = null;
		
		for (Cookie ck : cs.getCookies()) {
			if (ck.getName().equals("IBM_W3SSO_ACCESS")) {
				//TODO: this is a javascript created cookie, currently not working
				//it overrides the sso domain for a subset of users
				samlDomain =ck.getValue();
			}
			if (ck.getName().equals("PD-W3-SSO-REFPAGE")) {
				samlAuthenticator = URLDecoder.decode(ck.getValue()); 
				BasicClientCookie2 ck2= new BasicClientCookie2(ck.getName(), "invoked");
				ck2.setDomain(ck.getDomain());
				ck2.setPath(ck.getPath());
				cs.addCookie(ck);
				ck2=new BasicClientCookie2("PD-W3-SSO-REFPAGE-HOLDER", ck.getValue());
				ck2.setDomain(ck.getDomain());
				ck2.setPath(ck.getPath());
				cs.addCookie(ck);
			}

		}
		
		if (samlDomain == null || samlAuthenticator == null) {
			throw new AuthenticationException(new IllegalStateException("missing referral cookies"));
		}
		String samlEndpoint = samlDomain+samlAuthenticator;
		if (!samlEndpoint.startsWith("https://")) {
			samlEndpoint = "https://" + samlEndpoint;
		}
		
		resp = client.execute(RequestBuilder.get().setUri(samlEndpoint).build());
		check(resp,200);
		XPathExpression exp = DOMUtil.createXPath("//input");
		
		String doc = IOUtils.toString(resp.getEntity().getContent());
		// //input[@name=\"SAMLResponse\"]/@value was failing for some reason
		
		samlEndpoint = doc.replaceFirst("(?s).*<form method=\\\"post\\\" action=\\\"","");
		String saml = samlEndpoint.replaceFirst("(?s).*<input type=\\\"hidden\\\" name=\\\"SAMLResponse\\\" value=\\\"", "");
		samlEndpoint = samlEndpoint.replaceAll("(?s)\\\".*", "");
		saml = saml.replaceAll("(?s)\\\".*", "");

		resp = client.execute(RequestBuilder.post().setUri(samlEndpoint).
				addParameter("RelayState","").
				addParameter("SAMLResponse", saml).
				build());
		check(resp,302);
		extractHtmlCookie(cs, resp);
		String getLoginUrl = resp.getFirstHeader("Location").getValue();
		resp = client.execute(RequestBuilder.get().setUri(postLoginUrl).build());
		extractHtmlCookie(cs, resp);

		} catch(Exception e) {
			throw new AuthenticationException(e);
		}
		return true;
	}

	private void extractHtmlCookie(CookieStore cs, HttpResponse resp)
			throws IOException {
		String page = IOUtils.toString(resp.getEntity().getContent());
		Pattern p = Pattern.compile("<meta.*http-equiv=[\\\"']?set-cookie[\\\"']?.*content=[\\\"'](.*)[\\\"'].*>", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(page);
		while (m.find() ) {
			String cookie = m.group(1);
			String[] parts = cookie.split(";");
			String[] nv = parts[0].trim().split("=");
			String name = nv[0].trim();
			String value = nv.length == 2? nv[1].trim():"";
			String path = null;
			String domain = null;
			if (parts.length>1) {
				if (parts[1].trim().startsWith("path")) {
					path = parts[1].trim().replace("path=", "");
				}
				if (parts[1].trim().startsWith("domain")) {
					domain = parts[1].trim().replace("domain=", "");
				}
			}
			if (parts.length>2) {
				if (parts[2].trim().startsWith("path")) {
					path = parts[2].trim().replace("path=", "");
				}
				if (parts[2].trim().startsWith("domain")) {
					domain = parts[2].trim().replace("domain=", "");
				}
			}
			BasicClientCookie2 ck = new BasicClientCookie2(name, value);
			if(path!=null) ck.setPath(path);
			if (domain!= null) ck.setDomain(domain);
			cs.addCookie(ck);
		}
	}

	private void consume(HttpResponse resp) throws Exception{
		if (resp.getEntity() !=null) 
		IOUtils.toString(resp.getEntity().getContent());
	}

	private void check(HttpResponse resp, int i) throws AuthenticationException {
		if(resp.getStatusLine().getStatusCode()!=i) {
			throw new AuthenticationException(new IllegalStateException("unrecognized code received"));
		}
	}

	@Override
	public boolean accept(String url) {
		return url.equals("https://w3-03.sso.ibm.com/FIM/sps/IBM_W3_SAML20_EXTERNAL/saml20/logininitial?PartnerId=https://apps.na.collabserv.com/sps/sp/saml/v2_0&TARGET=https://apps.na.collabserv.com");
	}
}
