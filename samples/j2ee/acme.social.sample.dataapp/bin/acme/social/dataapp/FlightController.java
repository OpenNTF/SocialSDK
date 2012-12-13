package acme.social.dataapp;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

/**
 * controls the current state of the flights
 *
 */
public class FlightController {
	final static String FLIGHTSTATE = "flightcontroller.json";
	static FlightController fc = null;

	private JSONObject flightController = null;

	private FlightController() {
		InputStream is = this.getClass().getResourceAsStream(FLIGHTSTATE);
		load(is);
	}

	/**
	 * gets the single instance to the flight controller
	 * 
	 * @return
	 */
	public static FlightController getInstance() {
		if (fc == null) {
			fc = new FlightController();
		}
		return fc;
	}

	/**
	 * loads the codes from a path
	 * 
	 * @param codestream
	 */
	public void load(InputStream codestream) {
		try {
			flightController = new JSONObject(codestream);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * gets the flight status and returns as a String
	 * 
	 * @param flightId
	 * @return
	 */
	public String getFlightStatus(String flightId) {
		String res = "";
		try {
			JSONArray flts = (JSONArray) flightController.get("controller");

			@SuppressWarnings("rawtypes")
			Iterator iter = flts.iterator();
			boolean found = false;
			while (iter.hasNext() && !found) {
				JSONObject i = (JSONObject) iter.next();
				String flight = i.getString("Flight");
				if (flightId.compareTo(flight) == 0) {
					res = i.getString("State");
					found = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * updates the flight status of the given flight
	 * @param flightId
	 * @param state
	 */
	public void updateFlightStatus(String flightId, String state) {
		this.removeFlightStatus(flightId);
		this.addFlight(flightId, state);
	}

	/**
	 * removes the flight and the associated status
	 * @param flightId
	 */
	public void removeFlightStatus(String flightId) {
		try {
			JSONArray flts = (JSONArray) flightController.get("controller");

			@SuppressWarnings("rawtypes")
			Iterator iter = flts.iterator();
			boolean found = false;
			while (iter.hasNext() && !found) {
				JSONObject i = (JSONObject) iter.next();
				String flight = i.getString("Flight");
				if (flightId.compareTo(flight) == 0) {
					flts.remove(i);
					found = true;
				}
			}
			
			flightController.remove("controller");
			flightController.put("controller",flts);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * adds a flight to the list
	 * @param flightId
	 * @param state
	 */
	public void addFlight(String flightId, String state) {
		try {
			JSONArray flts = (JSONArray) flightController.get("controller");
			JSONObject o = new JSONObject();
			o.put("Flight", flightId);
			o.put("State", state);
			flts.add(o);
			
			flightController.remove("controller");
			flightController.put("controller",flts);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns all the flight statuses
	 * @return
	 */
	public JSONObject getAllFlightStatus() {
		return flightController;
	}

}
