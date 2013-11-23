package acme.dataapp;

import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

public class MyFlights {
    private static Logger log = java.util.logging.Logger.getAnonymousLogger();
	JSONObject myFlights = null;
	static MyFlights fs = null;

	final static String FLIGHTS = "myflights.json";

	/**
	 * private constructor
	 */
	private MyFlights() {
		InputStream is = this.getClass().getResourceAsStream(FLIGHTS);
		load(is);
	}

	/**
	 * get instance
	 * 
	 * @return
	 */
	public static MyFlights getInstance() {
		if (fs == null) {
			fs = new MyFlights();
		}
		return fs;
	}

	/**
	 * loads the JSONObject from a local resource
	 * 
	 * @param is
	 */
	public void load(InputStream is) {
		try {
			myFlights = new JSONObject(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * adds a current flight to the flights list
	 * 
	 * @param flightId
	 * @param userId
	 * @param approverId
	 * @param reason
	 */
	public void addMyFlight(String flightId, String userId, String approverId,
			String reason) {
		try {
			JSONArray flts = (JSONArray) myFlights.get("myflights");
			JSONObject o = new JSONObject();
			o.put("FlightId", flightId);
			o.put("UserId", userId);
			o.put("ApproverId", approverId);
			o.put("Reason", reason);
			o.put("state", "started");
			flts.put(o);
			
			myFlights.remove("myflights");
			myFlights.put("myflights", flts);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param userId
	 *            should be from the request.getUserPrincipal
	 */
	public JSONObject getMyFlights(String userId) {
		JSONObject res = new JSONObject();
		JSONArray arr = new JSONArray();
		try {
			JSONArray flts = (JSONArray) myFlights.get("myflights");

			@SuppressWarnings("rawtypes")
			Iterator iter = flts.iterator();
			while (iter.hasNext()) {
				JSONObject o = (JSONObject) iter.next();
				String uId = o.getString("UserId");

				// Checks to see if the uid exists
				if (uId.equalsIgnoreCase(userId)) {
					arr.put(o);
				}

			}

			res.put("myflights", arr);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

    /**
     * 
     * @param flightId
     *            
     */
    public JSONArray getUsers(String flightId) {
        JSONArray arr = new JSONArray();
        try {
            JSONArray flts = (JSONArray) myFlights.get("myflights");

            @SuppressWarnings("rawtypes")
            Iterator iter = flts.iterator();
            while (iter.hasNext()) {
                JSONObject o = (JSONObject) iter.next();
                String fId = o.getString("FlightId");

                // Checks to see if the fid exists
                if (fId.equalsIgnoreCase(flightId)) {
                    String uId = o.getString("UserId");
                    arr.put(uId);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arr;
    }

	/**
	 * removes a flight from the schedule
	 * 
	 * @param userId
	 * @param flightId
	 */
	public void removeMyFlights(String userId, String flightId) {
		try {
			JSONArray flts = (JSONArray) myFlights.get("myflights");

			@SuppressWarnings("rawtypes")
			Iterator iter = flts.iterator();
			while (iter.hasNext()) {
				JSONObject o = (JSONObject) iter.next();
				String fId = o.getString("UserId");
				String uId = o.getString("FlightId");

				// Checks to see if the flight id and uid exist
				if (fId.compareTo(flightId) == 0 && uId.compareTo(userId) == 0) {
					flts.remove(o);
				}

			}

			flts.remove(flightId);

			myFlights.remove("myflights");
			myFlights.put("myflights", flts);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * approves a flight only when the give approverId has approved it
	 * 
	 * @param userId
	 * @param flightId
	 * @param approverId
	 */
	public void approveMyFlight(String userId, String flightId,
			String approverId) {
		try {
			JSONArray flts = (JSONArray) myFlights.get("myflights");

			@SuppressWarnings("rawtypes")
			Iterator iter = flts.iterator();
			while (iter.hasNext()) {
				JSONObject o = (JSONObject) iter.next();
				String fId = o.getString("UserId");
				String uId = o.getString("FlightId");
				String aId = o.getString("ApproverId");

				// Checks to see if the flight id and uid and aId exist
				if (fId.compareTo(flightId) == 0 && uId.compareTo(userId) == 0
						&& aId.compareTo(approverId) == 0) {
					flts.remove(o);
					o.put("state", "approved");
					flts.put(o);
				}

			}

			flts.remove(flightId);

			myFlights.remove("myflights");
			myFlights.put("myflights", flts);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * updates a flight only when non approved
	 * 
	 * @param userId
	 * @param flightId
	 * @param approverid
	 * @param reason
	 * @param state
	 * @throws JSONException 
	 */
	public JSONObject updateMyFlight(String userId, String flightId,
			String approverId, String reason,String state) throws JSONException {
	    this.removeMyFlights(userId, flightId);
	    JSONArray flts = (JSONArray) myFlights.get("myflights");
	    Iterator<JSONObject> iter = flts.iterator();
	    while(iter.hasNext()) {
	        JSONObject flight = iter.next();
	        String oldFlightId = flight.getString("FlightId");
	        String oldUserId = flight.getString("UserId");
	        if(oldFlightId.equalsIgnoreCase(flightId) && oldUserId.equalsIgnoreCase(userId)) {
	            flts.remove(flight);
	            break;
	        }
	    }
	    JSONObject o = new JSONObject();
	    o.put("FlightId", flightId);
	    o.put("UserId", userId);
	    o.put("ApproverId", approverId);
	    o.put("Reason", reason);
	    o.put("state", state);
	    flts.put(o);

	    myFlights.remove("myflights");
	    myFlights.put("myflights", flts);
	    return o;

		
	}

	/**
	 * gets the list of customers on a flight
	 * 
	 * @param flightId
	 * @return
	 */
	public JSONObject getCustomersByFlight(String flightId) {
		JSONObject res = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			JSONArray flts = (JSONArray) myFlights.get("myflights");

			@SuppressWarnings("rawtypes")
			Iterator iter = flts.iterator();
			while (iter.hasNext()) {
				JSONObject i = (JSONObject) iter.next();

				String flight = i.getString("FlightId");
				if (flightId.compareTo(flight) == 0) {
					array.put(i);
				}
			}

			res.put("customers", array);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * gets the list of all travel
	 * 
	 * @return
	 */
	public JSONObject getAll() {
		return myFlights;
	}

	/**
	 * gets the reason for a travel request and the associated title with the
	 * travel request gets the approvers id as well
	 * 
	 * @param userId
	 * @param flightId
	 * @return
	 */
	public JSONObject getReasonForTravel(String userId, String flightId) {
		JSONObject res = new JSONObject();
		String r = "";
		try {
			JSONArray flts = (JSONArray) myFlights.get("myflights");

			@SuppressWarnings("rawtypes")
			Iterator iter = flts.iterator();
			boolean found = false;
			while (iter.hasNext() && !found) {
				JSONObject i = (JSONObject) iter.next();
				
				String flight = i.getString("FlightId");
				String user = i.getString("UserId");
				if (flightId.compareTo(flight) == 0 && user.compareTo(userId)==0) {
					r = i.getString("Reason");
					res.put("reason", r);
					found = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * Gets a specific flight user's flight;
	 * @param userId The user id.
	 * @param flightId The flight id.
	 * @return The flight.
	 */
    public JSONObject getFlight(String userId, String flightId) {
        final String method = "getFlight";
        JSONObject res = new JSONObject();
        JSONObject resObject = this.getMyFlights(userId);
        try {
            JSONArray flts = resObject.getJSONArray("myflights");
            Iterator<JSONObject> iter = flts.iterator();
            while(iter.hasNext()) {
                JSONObject flt = iter.next();
                if(flt.getString("FlightId").equalsIgnoreCase(flightId)){
                    res = flt;
                    break;
                }
            }
            
        } catch (JSONException e) {
            log.logp(Level.SEVERE, MyFlights.class.getName(), method, e.getMessage(), e);
        }
        return res;
    }
}
