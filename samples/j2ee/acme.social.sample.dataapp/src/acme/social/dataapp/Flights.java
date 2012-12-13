package acme.social.dataapp;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

public class Flights {
	static Flights fs = null;
	JSONObject flights = null;
	final static String FLIGHTS = "flights.json";

	/**
	 * private constructor
	 */
	private Flights() {
		InputStream is = this.getClass().getResourceAsStream(FLIGHTS);
		load(is);
	}

	/**
	 * get instance
	 * 
	 * @return
	 */
	public static Flights getInstance() {
		if (fs == null) {
			fs = new Flights();
		}
		return fs;
	}

	/**
	 * loads the codes from a path
	 * 
	 * @param codestream
	 */
	public void load(InputStream codestream) {
		try {
			flights = new JSONObject(codestream);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * adds a flight to the JSON Array for flights
	 * 
	 * @param flight
	 * @param depart
	 * @param arrive
	 * @param time
	 * @param flightTime
	 */
	public void add(String flight, String depart, String arrive, String time,
			String flightTime) {
		try {
			// Creates a new flight
			JSONObject newFlight = new JSONObject();
			newFlight.put("Flight", flight);
			newFlight.put("Depart", depart);
			newFlight.put("Arrive", arrive);
			newFlight.put("Time", time);
			newFlight.put("FlightTime", flightTime);

			JSONArray flts = (JSONArray) flights.get("flights");
			flts.add(newFlight);

			flights.remove("flights");
			flights.put("flights", flights);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * removes a flight give the flight identifier
	 * 
	 * @param flight
	 */
	public void removeFlight(String flight) {
		try {
			JSONArray flts = (JSONArray) flights.get("flights");
			flts.remove(flight);

			flights.remove("flights");
			flights.put("flights", flights);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * returns all flights to the calling object
	 * 
	 * @return
	 */
	public JSONObject getFlights() {
		return flights;
	}

	/**
	 * only returns the flight details for the one object
	 * 
	 * @param flight
	 * @return
	 */
	public JSONObject getFlightsByID(String flight) {
		JSONObject res = null;
		try {
			JSONArray flts = (JSONArray) flights.get("flights");
			
			@SuppressWarnings("rawtypes")
			Iterator iter = flts.iterator();
			boolean found = false; 
			while(iter.hasNext() && !found){
				JSONObject i = (JSONObject) iter.next();
				String flightId = i.getString("Flight");
				if(flightId.compareTo(flight)==0){
					res = i;
					found = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * updates the flights
	 * 
	 * @param flight
	 * @param depart
	 * @param arrive
	 * @param time
	 * @param flightTime
	 */
	public void update(String flight, String depart, String arrive, String time,
			String flightTime){
		this.removeFlight(flight);
		this.add(flight, depart, arrive, time, flightTime);
	}
}
