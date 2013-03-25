package com.ibm.sbt.core.configuration;

public class Configuration {
	
    public static final String ENCODING = "UTF-8";
    public static final String FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE = "content-type";
    
	public static final String OAUTH_VERSION1 = "1.0";
	public static final String PLAINTEXT_SIGNATURE = "PLAINTEXT";

	public static final String CONSUMER_KEY = "oauth_consumer_key";
	public static final String SIGNATURE_METHOD = "oauth_signature_method";
	public static final String SIGNATURE = "oauth_signature";
	public static final String TIMESTAMP = "oauth_timestamp";
	public static final String NONCE = "oauth_nonce";
	public static final String VERSION = "oauth_version";
	public static final String OAUTH_TOKEN = "oauth_token";
	public static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";
	/* oauth1 URL the Service Provider will use to redirect the User back to the Consumer */
	public static final String CALLBACK = "oauth_callback";
	/* oauth1 parameter returned in callback that must be present to get an access token */
	public static final String OAUTH_VERIFIER = "oauth_verifier";
	public static final String TOKEN_EXPIRED = "oauth_token_expired";
	public static final String OAUTH1_EXPIRESIN = "expires_in";
	public static final String OAUTH1_ISSUEDON = "issued_on";

	
	/* OAuth version 2.0 values */
	public static final String OAUTH_VERSION2 = "2.0";
	public static final String OAUTH2_CLIENT_ID = "client_id";
	public static final String OAUTH2_CLIENT_SECRET = "client_secret";
	public static final String OAUTH2_RESPONSE_TYPE = "response_type";
	public static final String OAUTH2_GRANT_TYPE = "grant_type";
	public static final String OAUTH2_CODE = "code";
	public static final String OAUTH2_CALLBACK_URI = "callback_uri";
	public static final String OAUTH2_REDIRECT_URI = "redirect_uri";
	public static final String OAUTH2_AUTHORIZATION_CODE = "authorization_code";
	public static final String OAUTH2_ACCESS_TOKEN = "access_token";
	public static final String OAUTH2_REFRESH_TOKEN = "refresh_token";
	public static final String OAUTH2_ISSUEDON = "issued_on";
	public static final String OAUTH2_EXPIRESIN = "expires_in";
	public static final String OAUTH2_TOKENTYPE = "token_type";
	public static final String OAUTH2_ACCESS_TOKEN_EXPIRED = "oauth_access_token_expired";
	public static final String OAUTH_SCOPE = "scope";
	public static final String OAUTH_STATE = "state";
	
	public static final String OAUTH_ERROR = "oauth_error";
	public static final String OAUTH_DENIED = "oauth_denied";

	public static final String OAUTH_HANDLER = "OAuthHandlerObject";
	public static final String API_REQUEST = "ApiRequestObject";
	public static final String API_REQUEST_REDO = "showPageWithApiResults";
	
	public static final String SHOW_OAUTH_FLOW_KEY = "OAuthFlow";
	public static final String SHOW_NAVBAR = "Navbar";

	/* Authorization types */
	public static final String AUTH_TYPE_OAUTH1 = "oauth1";
	public static final String AUTH_TYPE_OAUTH2 = "oauth2";
	public static final String AUTH_TYPE_BASIC = "basic";
	
	/* Other */
	public static final String SERVER_URL = "server_url";
	public static final String AUTH_TYPE = "auth_type";


}
