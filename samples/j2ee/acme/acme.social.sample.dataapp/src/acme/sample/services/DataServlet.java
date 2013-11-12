package acme.sample.services;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.wink.json4j.JSONObject;

import acme.dataapp.AirportCodes;
import acme.dataapp.FlightController;
import acme.dataapp.Flights;
import acme.dataapp.MOTD;
import acme.dataapp.MyFlights;
import acme.interfaces.Operation;

/**
 * Class that mediates the in-memory requests to various Acme Services
 */
//@WebServlet({ "/DataServlet", "/RestServlet", "/rest/*" })
public class DataServlet extends HttpServlet {

	private static Logger log = java.util.logging.Logger.getAnonymousLogger();

	private static final long serialVersionUID = 1L;

	/*
	 * holds the map to the services objects
	 */
	private static HashMap<String, Operation> servicesMap = new HashMap<String, Operation>();

	/**
	 * Populates the Services list
	 */
	public DataServlet() {
		super();

	}
	
    /**
	 * Maps to the Operations For Get/Read
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Logs out the URL
		log.log(Level.INFO, "Component of the URL is " + request.getPathInfo());

		/*
		 * sets the response type
		 */
		response.setContentType("application/json");
		JSONObject res = new JSONObject();

		// Processes the Get Methods
		try {
			String path = request.getPathInfo().toLowerCase();

			if (path.contains("/api/airportcodes")) {
				// Outputs the airport codes
				AirportCodes ac = AirportCodes.getInstance();
				res = ac.getCodes();

			} else if (path.contains("/api/airportcodebycity/")) {
				// Gets Airport by City
				int x = path.lastIndexOf("bycity/") + 7;
				String p = path.substring(x);
				log.info("Get Airport Code by City : " + p);
				AirportCodes ac = AirportCodes.getInstance();
				String code = ac.getCodeByCity(p);
				res.put("code", code);

			} else if (path.contains("/api/airportcodebycityandsate/")) {
				// Gets Airport Code by City and State
				int x = path.lastIndexOf("/api/airportcodebycityandsate/")
						+ "/api/airportcodebycityandsate/".length();
				String p = path.substring(x);
				String[] par = p.split("/");
				log.info("Get Airport Code by City and State : " + par[0] + " "
						+ par[1]);
				AirportCodes ac = AirportCodes.getInstance();
				String code = ac.getCode(par[0], par[1]);
				res.put("code", code);

			} else if (path.contains("/api/flights/all")) {
				// Gets the details of all flights
				Flights fs = Flights.getInstance();
				res = fs.getFlights();

			} else if (path.contains("/api/flight/")) {
				// Gets the details of a flight
				Flights fs = Flights.getInstance();
				int idx = path.indexOf("/api/flight/")
						+ "/api/flight/".length();
				String flightId = path.substring(idx);
				res = fs.getFlightsByID(flightId);

			} else if (path.contains("/api/logout")) {
				// Logs out the user
				HttpSession cur = request.getSession(false);
				if (cur != null) {
					cur.invalidate();
//					request.logout();
				}
				res.put("result", "loggedout");

			} else if (path.contains("/api/messageofday/")) {
				// Returns the message of the day for Acme Airlines
				MOTD motd = MOTD.getInstance();
				res = motd.getRandomMessageOfTheDay();
				
			} else if (path.contains("/api/fc/all")) {
				FlightController fc = FlightController.getInstance();
				res = fc.getAllFlightStatus();
				
			} else if (path.contains("/api/fc/")) {
				int idx = path.indexOf("/api/fc/") + "/api/fc/".length();
				String flightId = path.substring(idx);
				FlightController fc = FlightController.getInstance();
				String status = fc.getFlightStatus(flightId);
				res.put("status", status);

			} else if (path.contains("/api/myflights/")) { //-
				// gets flights for users
				Principal prin = request.getUserPrincipal();
				if (prin != null) {
					String userId = prin.getName();

					MyFlights mf = MyFlights.getInstance();
					res = mf.getMyFlights(userId);

					res.put("result", "success");
				} else {
					res.put("result", "notloggedin");
				}

			} else if (path.contains("/api/allflightrequests")) {
				// gets all flight requests
				Principal prin = request.getUserPrincipal();

				if (prin != null) {
					String flightId = request.getParameter("flightId");
					if (flightId != null) {
						MyFlights mf = MyFlights.getInstance();
						res = mf.getCustomersByFlight(flightId);
					} else {
						if (request.isUserInRole("admin")) {
							MyFlights mf = MyFlights.getInstance();
							res = mf.getAll();

						} else {
							res.put("result", "not in admin role");
						}
					}

				} else {
					res.put("result", "notloggedin");
				}

			} else if (path.contains("/api/myflightreason")) {
				// gets the reason for travel for a selected user and flight
				Principal prin = request.getUserPrincipal();

				if (prin != null) {
					String flightId = request.getParameter("flightId");
					String userId = request.getParameter("userId");

					if (flightId != null && userId != null) {
						MyFlights mf = MyFlights.getInstance();
						res = mf.getReasonForTravel(userId, flightId);
					} else {

						res.put("result", "no parameters");

					}

				} else {
					res.put("result", "notloggedin");
				}

			}

			res.write(response.getWriter());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Maps to the Operations for Post/Create
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();
		log.log(Level.INFO, "Component of the URL is " + path);

		/*
		 * sets the response type
		 */
		response.setContentType("application/json");
		JSONObject res = new JSONObject();

		try {

			if (path.contains("/api/flight")) {
				// adds a new flight to the flights controller
				JSONObject flightObj = new JSONObject(request.getInputStream());

				FlightController fc = FlightController.getInstance();
				String flightId = (String) flightObj.get("flightId");
				String state = (String) flightObj.get("state");
				fc.addFlight(flightId, state);
				res.put("result", "added");

			} else if (path.contains("/api/flights")) {
				// adds a flight status
				Flights fs = Flights.getInstance();
				JSONObject o = new JSONObject(request.getInputStream());
				String flight = (String) o.get("flight");
				String depart = (String) o.get("depart");
				String arrive = (String) o.get("arrive");
				String time = (String) o.get("time");
				String flightTime = (String) o.get("flighttime");
				fs.add(flight, depart, arrive, time, flightTime);
				res.put("result", "added");

			} else if (path.contains("/api/addmyflight")) {

			} else if (path.contains("/api/login")) {

				// Manages the Login without the ugly basic popup window
				JSONObject loginObj = new JSONObject(request.getInputStream());
				String user = (String) loginObj.get("username");
				String pass = (String) loginObj.get("password");

				// Logs in the user
//				request.login(user, pass);

				if (request.getUserPrincipal().getName().compareTo(user) == 0) {
					res.put("status", "loggedin");
				} else {
					res.put("status", "error in login");
				}
			} else if (path.contains("/api/fc/")) {
				// Creates a new flight status
				JSONObject o = new JSONObject(request.getInputStream());
				String flightId = o.getString("flightId");
				String state = o.getString("state");
				FlightController fc = FlightController.getInstance();
				fc.addFlight(flightId, state);
				res.put("result", "added");
			} else if (path.contains("/api/myflights/")) {
				// Adds a flight
				Principal prin = request.getUserPrincipal();
				if (prin != null) {
					String userId = prin.getName();
					JSONObject o = new JSONObject(request.getInputStream());
					String flightId = o.getString("FlightId");
					String approverId = o.getString("ApproverId");
					String reason = o.getString("Reason");
					MyFlights mf = MyFlights.getInstance();
					mf.addMyFlight(flightId, userId, approverId, reason);
					res.put("result", "success");
				} else {
					res.put("result", "notloggedin");
				}

			} else if (path.contains("/api/approveflight/")) {
				// approves a flight
				Principal prin = request.getUserPrincipal();
				if (prin != null) {
					String approverId = prin.getName();
					JSONObject o = new JSONObject(request.getInputStream());
					String userId = o.getString("userId");
					String flightId = o.getString("flightId");
					MyFlights mf = MyFlights.getInstance();
					mf.approveMyFlight(userId, flightId, approverId);
					res.put("result", "success");
				} else {
					res.put("result", "notloggedin");
				}

			}

			res.write(response.getWriter());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Maps to the Operations for Delete/Delete
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();
		log.log(Level.INFO, "Component of the URL is " + path);

		/*
		 * sets the response type
		 */
		response.setContentType("application/json");
		JSONObject res = new JSONObject();

		try {
			if (path.contains("/api/flights")) {
				// deletes a flight status
				Flights fs = Flights.getInstance();
				JSONObject o = new JSONObject(request.getInputStream());
				String flight = (String) o.get("flight");
				fs.removeFlight(flight);

			} else if (path.contains("/api/fc/")) {
				// deletes a new flight status
				JSONObject o = new JSONObject(request.getInputStream());
				String flightId = o.getString("flightId");
				FlightController fc = FlightController.getInstance();
				fc.removeFlightStatus(flightId);
				res.put("result", "deleted");
			} else if (path.contains("/api/myflights/")) {
				// gets all flight requests
				Principal prin = request.getUserPrincipal();

				if (prin != null) {
					MyFlights mf = MyFlights.getInstance();
					JSONObject o = new JSONObject(request.getInputStream());
					String userId = o.getString("userId");
					String flightId = o.getString("flightId");
					mf.removeMyFlights(userId, flightId);
					res.put("result", "flightremoved");

				} else {
					res.put("result", "notloggedin");
				}

			}
			res.write(response.getWriter());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Maps to Operations for Put/Update
	 */
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();
		log.log(Level.INFO, "Component of the URL is " + path);

		/*
		 * sets the response type
		 */
		response.setContentType("application/json");
		JSONObject res = new JSONObject();

		try {
			if (path.contains("/api/flightstatus")) {
				// updates a flight status
				Flights fs = Flights.getInstance();
				JSONObject o = new JSONObject(request.getInputStream());
				String flight = (String) o.get("flight");
				String depart = (String) o.get("depart");
				String arrive = (String) o.get("arrive");
				String time = (String) o.get("time");
				String flightTime = (String) o.get("flighttime");
				fs.update(flight, depart, arrive, time, flightTime);

			}

			else if (path.contains("/api/fc/")) {
				// Creates a new flight status
				JSONObject o = new JSONObject(request.getInputStream());
				String flightId = o.getString("flightId");
				String state = o.getString("state");
				FlightController fc = FlightController.getInstance();
				fc.updateFlightStatus(flightId, state);
				res.put("result", "updated");
			}

			else if (path.contains("/api/myflights/")) {
				// updates a flight
				Principal prin = request.getUserPrincipal();
				if (prin != null) {
					String userId = prin.getName();
					JSONObject o = new JSONObject(request.getInputStream());
					String flightId = o.getString("FlightId");
					String approverId = o.getString("ApproverId");
					String reason = o.getString("Reason");
					String state = o.getString("state");
					MyFlights mf = MyFlights.getInstance();
					mf.updateMyFlight(userId, flightId, approverId, reason,
							state);
					res.put("result", "success");
				} else {
					res.put("result", "notloggedin");
				}

			}

			res.write(response.getWriter());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
