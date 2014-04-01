package com.ibm.sbt.services.endpoints;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;

import com.ibm.sbt.security.authentication.AuthenticationException;

public interface FormLoginHandler {
	boolean accept(String url);
    boolean login(String user, String password, HttpClient client, CookieStore c, String url) throws AuthenticationException;
}
