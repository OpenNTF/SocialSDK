package com.ibm.sbt.services.endpoints;

import org.apache.http.client.HttpClient;

public interface LoginHandler {
    boolean login(String user, String password, HttpClient client);
}
