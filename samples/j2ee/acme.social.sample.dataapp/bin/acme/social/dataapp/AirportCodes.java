package acme.social.dataapp;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

/**
 * Airport Codes are from the list of IBM Locations
 */
public class AirportCodes {

	static AirportCodes ac = null;
	final static String AIRPORTCODES = "airportcodes.json";
	JSONObject codes = new JSONObject();

	/**
	 * constructor for the single instance, there should never be two of these
	 */
	private AirportCodes() {
		InputStream is = this.getClass().getResourceAsStream(AIRPORTCODES);
		load(is);
	}

	/**
	 * get instance for the airport codes
	 * 
	 * @return
	 */
	public static AirportCodes getInstance() {
		if (ac == null) {
			ac = new AirportCodes();
		}
		return ac;
	}

	/**
	 * loads the codes from a path
	 * 
	 * @param codestream
	 */
	public void load(InputStream codestream) {
		try {
			codes = new JSONObject(codestream);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * gets the current codes
	 * 
	 * @return
	 */
	public JSONObject getCodes() {
		return codes;
	}

	/**
	 * get the code for a city and state
	 */
	public String getCode(String city, String state) {
		String code = "unknown";

		try {
			Object o = codes.get("airports");
			JSONArray ja = (JSONArray) o;
			@SuppressWarnings("rawtypes")
			Iterator iter = ja.iterator();
			while(iter.hasNext()){
				JSONObject jo = (JSONObject) iter.next();
				String cityT = jo.getString("city");
				String stateT = jo.getString("state");
				
				if(cityT.toLowerCase().compareTo(city.toLowerCase())==0 &&
						stateT.toLowerCase().compareTo(state.toLowerCase())==0){
					code = (String) jo.get("code");
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return code;
	}

	/**
	 * gets the code by city
	 * @param city
	 * @return
	 */
	public String getCodeByCity(String city) {
		String code = "unknown";

		try {
			Object o = codes.get("airports");
			JSONArray ja = (JSONArray) o;
			@SuppressWarnings("rawtypes")
			Iterator iter = ja.iterator();
			while(iter.hasNext()){
				JSONObject jo = (JSONObject) iter.next();
				if(((String) jo.get("city")).toLowerCase().compareTo(city.toLowerCase())==0){
					code = (String) jo.get("code");
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return code;
	}

}
