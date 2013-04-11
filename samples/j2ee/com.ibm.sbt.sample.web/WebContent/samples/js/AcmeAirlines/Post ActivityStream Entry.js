require(["sbt/Endpoint", "sbt/connections/ActivityStreamService", "sbt/json", "sbt/dom"], 
    function(Endpoint, ActivityStreamService, json, dom) {

    var results = [];
    /**
     * Posts an activity stream entry to the feed of the first line manager of the user
     * who booked the flight.
     * @param {Object} flight The flight that is being booked.
     * @param {Object} personObj The OpenSocial person object of the user who booked the flight.
     * @param {String} approverId The Connections id of the user's first line manager.
     */
    function postASEntry(flight, personObj, approverId) {
        var entry = getActivityEntry(flight, personObj, approverId);
        entry.openSocial.embed = getEEDataModel(flight);
        
        // display the entry to be posted
        results.push(entry);
        dom.setText("json", json.jsonBeanStringify(results));
        
        var self = this;
        var activityStreamService = new ActivityStreamService({endpoint:"conAcmeApp"});
        activityStreamService.postEntry({
            postData : entry,
            load : function(response) {
                results.push(response);
                dom.setText("json", json.jsonBeanStringify(results));
            },
            error : function(error) {
                results.push(error);
                dom.setText("json", json.jsonBeanStringify(results));
            }
        });
    }
    
    /**
     * Gets the activity entry for booking a flight.
     * @param {Object} personObj The OpenSocial person object for the person booking the flight.
     * @param {String} approverId The Connections ID of the first line manager booking the flight.
     * @return {Object} The activity entry to post to the activity stream.
     */
    function getActivityEntry(flight, personObj, approverId) {
        var homePageUrl = getHomePageUrl();
        var id = personObj.id + ':' + approverId + ":" + flight.FlightId;
        return {
            generator : {
                image : {
                    url : homePageUrl + "favicon.ico"
                },
                id : "AcmeAirlines",
                displayName : "Acme Airlines",
                url : homePageUrl,
            },
            actor : {
                id : personObj.id,
            },
            verb : "created",
            title : "${Actor} created a ${Object} in the ${Target}.",
            content : "Please approve " + personObj.displayName + "'s flight request for flight " + flight.FlightId + ".",
            updated : "2012-01-01T12:00:00.000Z",
            object : {
                summary : personObj.displayName + " is requesting to fly from " + flight.Depart + " to " + flight.Arrive + " on flight "+ flight.FlightId + ".",
                objectType : "flight",
                id : flight.FlightId,
                displayName : "Flight Request",
                url : homePageUrl,
            },
            target : {
                summary : "Airlines Booking App",
                objectType : "application",
                id : "AcmeAirlines",
                displayName : "Acme Airlines Booking App",
                url : homePageUrl
            },
            openSocial : {
                deliverTo : [{
                    objectType : "person",
                    id : approverId
                }]
            },
            connections : {
                rollupid : id,
                actionable: "true"
            }
        };
    }
    
    /**
     * Gets the embedded experience data model.
     * See the definition of the embedded experience data model in the 
     * <a href="http://opensocial-resources.googlecode.com/svn/spec/2.5/Core-Gadget.xml#Embedded-Experiences">
     * OpenSocial spec</a>.
     * @return {Object} The embedded experience data model.
     */
    function getEEDataModel(flight) {
        return {
            "gadget" : getGadgetUrl(),
            "context" : flight
        };
    };
    
    /**
     * Gets the home page URL of the Acme Airlines app.
     * @return {String} The home page URL of the Acme Airlines app.
     */
    function getHomePageUrl() {
        return "http://acmeairlines.com:8080/acme.social.sample.webapp/";
    };
    
    /**
     * Gets the embedded experience gadget URL.
     * @return {String} The embedded experience gadget URL.
     */
    function getGadgetUrl() {
        return getHomePageUrl() + 'gadgets/airlines/airlines.xml';
    };       
    
    // demonstrate calling the method
    var flight = {
        "FlightId":"203", "UserId":"%{sample.email1}", "ApproverId":"%{sample.email3}", "Arrive":"CDG", "Depart":"ORD"
    };
    var personObj = {
        "displayName": "%{sample.displayName1}",
        "emails": [
            {
                "type": "primary",
                "value": "%{sample.email1}",
                "primary": true
            }
        ],
        "id": "%{sample.userId1}"
    };
    var approverId = "%{sample.userId3}";
    postASEntry(flight, personObj, approverId);    
});