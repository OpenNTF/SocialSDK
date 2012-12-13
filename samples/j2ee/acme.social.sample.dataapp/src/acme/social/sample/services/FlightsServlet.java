package acme.social.sample.services;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import acme.social.dataapp.Flights;
import acme.social.dataapp.MyFlights;

/**
 * Handles request regarding flights. 
 *
 */
public class FlightsServlet extends HttpServlet {
    
    /**
     * 
     */
    private static final long serialVersionUID = 6312354232706602537L;
    private static Logger log = java.util.logging.Logger.getAnonymousLogger();
    private static final String FLIGHT_ID = "FlightId";
    private static final String USER_ID = "UserId";
    private static final String APPROVER_ID = "ApproverId";
    private static final String REASON = "Reason";
    private static final String STATE = "state";

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /*
         * Example requests
         * flights/lists -> gets all flights
         * flights/lists/{flightId} -> gets a flight with the given id
         * flights/{userId}/lists -> gets all flights for a specific user
         * flights/{userId}/lists/{flightId} -> gets a flight for a specific user
         */
        String[] paths = getPaths(req.getPathInfo().toLowerCase());
        
        if(paths[0].equalsIgnoreCase("lists")){
            //requesting a specific flight or all flights
            String id = (paths.length > 1) ? paths[1] : null;
            getFlights(id, req, resp);
        } else if(paths.length > 0 && paths[1].equalsIgnoreCase("lists")){
            //Must be flights for a specific user
            String userId = paths[0];
            String flightId = (paths.length > 2) ? paths[2] : null;
            getFlightsForUser(userId, flightId, req, resp);
            		
        }
        
    }
    
    private String[] getPaths(String path) {
        if(path.startsWith("/")) {
            path = path.replaceFirst("/", "");
        }
        return path.split("/");
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String contentType = req.getContentType();
        if(contentType == null || !contentType.startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Content-Type, must be application/json.");
            return;
        }
        String[] paths = getPaths(req.getPathInfo().toLowerCase());
        
        if(!"lists".equalsIgnoreCase(paths[0]) && paths.length > 0 && paths[1].equalsIgnoreCase("lists")){
            //Must be flights for a specific user
            String userId = paths[0];
            String postBody = getBody(req);
            try {
                JSONObject flight = new JSONObject(postBody);
                if(!flight.has(FLIGHT_ID) || !flight.has(APPROVER_ID)) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                            "The POST body must have the following properties: FlightId, UserId, ApproverId");
                    return;
                }
                String flightId = flight.getString(FLIGHT_ID);
                if(flightAlreadyBooked(flightId, userId)) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    resp.flushBuffer();
                    return;
                }
                MyFlights.getInstance().addMyFlight(flight.getString(FLIGHT_ID), userId, 
                        flight.getString(APPROVER_ID), flight.optString(REASON));
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                resp.flushBuffer();
                return;
            } catch (JSONException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                return;
            }                    
        }
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /*
         * Examples
         * flights/{userid}/lists/{flightid} 
         */
        String contentType = req.getContentType();
        if(contentType == null || !contentType.startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Content-Type, must be application/json.");
            return;
        }
        String[] paths = getPaths(req.getPathInfo().toLowerCase());
        
        if(paths.length > 1 && paths[1].equalsIgnoreCase("lists")){
            String putBody = getBody(req);
            String userId = paths[0];
            String flightId = paths[2];
            if(!flightAlreadyBooked(flightId, userId)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no flight booked for user " + userId + 
                        " with the flight number " + flightId + ".");
                return;
            }
            try {
                MyFlights myFlights = MyFlights.getInstance();
                JSONObject updatedFlight = new JSONObject(putBody);
                JSONObject currentFlight = myFlights.getFlight(userId, flightId);
                String approverId = updatedFlight.has(APPROVER_ID) ? updatedFlight.getString(APPROVER_ID) : 
                    currentFlight.getString(APPROVER_ID);
                String reason = updatedFlight.has(REASON) ? updatedFlight.getString(REASON) : 
                    currentFlight.optString(REASON);
                String state = updatedFlight.has(STATE) ? updatedFlight.getString(STATE) : currentFlight.getString(STATE);
                JSONObject responseFlight = myFlights.updateMyFlight(userId, flightId, approverId, reason, state);
                writeJsonObjectResponse(responseFlight, resp);
                resp.flushBuffer();
            } catch (JSONException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return;
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
        }
    }

    private boolean flightAlreadyBooked(String flightId, String userId) {
        JSONObject myFlightsObj = MyFlights.getInstance().getMyFlights(userId);
        if(myFlightsObj.has("myflights")) {
            try {
                JSONArray myFlights = myFlightsObj.getJSONArray("myflights");
                Iterator<JSONObject> iter = myFlights.iterator();
                while(iter.hasNext()) {
                    if(iter.next().getString(FLIGHT_ID).equals(flightId)) {
                        return true;
                    }
                }
            } catch (JSONException e) {
                return false;
            }
        }
        return false;
    }

    private void getFlights(String id, HttpServletRequest req, HttpServletResponse resp) {
        final String method = "getFlights";
        if(id == null) {
            try {
                writeJsonArrayResponse(Flights.getInstance().getFlights().getJSONArray("flights"), resp);
            } catch (JSONException e) {
                try {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                } catch (IOException e1) {
                    log.logp(Level.SEVERE, FlightsServlet.class.getName(), method, e.getMessage(), e);
                }
            }
        } else {
            writeJsonObjectResponse(Flights.getInstance().getFlightsByID(id), resp);
        }
        
    }
    
    private void getFlightsForUser(String userId, String flightId, HttpServletRequest req, HttpServletResponse resp) {
        final String method = "getFlights";
        if(flightId == null) {
            try {
                writeJsonArrayResponse(MyFlights.getInstance().getMyFlights(userId).getJSONArray("myflights"), resp);
            } catch (JSONException e) {
                try {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                } catch (IOException e1) {
                    log.logp(Level.SEVERE, FlightsServlet.class.getName(), method, e.getMessage(), e);
                }
            }
        } else {
            writeJsonObjectResponse(MyFlights.getInstance().getFlight(userId, flightId), resp);
        }
    }
    
    private void writeJsonObjectResponse(JSONObject json, HttpServletResponse resp) {
        final String method = "writeJsonObjectResponse";
        resp.setContentType("application/json");
        try {
            json.write(resp.getWriter());
        } catch (Exception e) {
            try {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (IOException e1) {
                log.logp(Level.SEVERE, FlightsServlet.class.getName(), method, e.getMessage(), e);
            }
        } finally {
            try {
                resp.flushBuffer();
            } catch (IOException e) {
                log.logp(Level.SEVERE, FlightsServlet.class.getName(), method, e.getMessage(), e);
            }
        }
    }
    
    private void writeJsonArrayResponse(JSONArray json, HttpServletResponse resp) {
        final String method = "writeJsonArrayResponse";
        resp.setContentType("application/json");
        try {
            json.write(resp.getWriter());
        } catch (Exception e) {
            try {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } catch (IOException e1) {
                log.logp(Level.SEVERE, FlightsServlet.class.getName(), method, e.getMessage(), e);
            }
        } finally {
            try {
                resp.flushBuffer();
            } catch (IOException e) {
                log.logp(Level.SEVERE, FlightsServlet.class.getName(), method, e.getMessage(), e);
            }
        }
    }

    private String getBody(HttpServletRequest request) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while (result != -1) {
            byte b = (byte) result;
            buf.write(b);
            result = bis.read();
        }
        buf.close();
        String body = buf.toString();
        return body;
    }
    
}
