package acme.social.interfaces;

import java.util.HashMap;

/**
 * Signatures for the REST Operations which are supported by the in memory data
 *
 */
public interface Operation {
	
	public org.apache.wink.json4j.JSONObject create(String path, org.apache.wink.json4j.JSONObject data, HashMap<String,String> params);
	public org.apache.wink.json4j.JSONObject delete(String path, int id, HashMap<String,String> params);
	public org.apache.wink.json4j.JSONObject read(String path, HashMap<String,String> params);
	public org.apache.wink.json4j.JSONObject update(String path, int id, org.apache.wink.json4j.JSONObject data);
	
}
