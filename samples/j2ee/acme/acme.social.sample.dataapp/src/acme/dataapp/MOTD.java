package acme.dataapp;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Random;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;

/**
 * MOTD.class - Message of the Day Manager
 * 
 */
public class MOTD {
	final static String MESSAGEOFTHEDAY = "motd.json";
	static MOTD motd = null;
	
	long count = 0;

	JSONObject m = null;

	/**
	 * constructor
	 */
	private MOTD() {
		InputStream is = this.getClass().getResourceAsStream(MESSAGEOFTHEDAY);
		load(is);
	}

	/**
	 * loads the JSON Object with the message of the day.
	 * 
	 * @param mIs
	 */
	public void load(InputStream mIs) {
		try {
			m = new JSONObject(mIs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * gets the instance for the message of the day class
	 * 
	 * @return
	 */
	public static MOTD getInstance() {
		if (motd == null) {
			motd = new MOTD();
		}
		return motd;
	}

	/**
	 * get message of the day
	 */
	public JSONObject getRandomMessageOfTheDay() {
		JSONObject res = null;
		try {
			JSONArray msgs = (JSONArray) m.get("motd");
			
			long numMessages = (long) m.getInt("length");
			
			int index =  Long.valueOf(System.currentTimeMillis() % numMessages).intValue();
			
			@SuppressWarnings("rawtypes")
			Iterator iter = msgs.iterator();
			int i = 0;
			while(iter.hasNext() && i < index){
				iter.next();
				i++;
			}
			res = (JSONObject) iter.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
