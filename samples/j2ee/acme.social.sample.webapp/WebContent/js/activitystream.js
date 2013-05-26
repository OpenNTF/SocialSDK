/**
 * Helper module for posting to the activity stream.
 * @module
 */
define(["sbt/declare", "sbt/Endpoint", "sbt/connections/ActivityStreamService"], 
    function(declare, Endpoint, ActivityStreamService) {

    return {
        /**
         * Posts an activity stream entry to the feed of the first line manager of the user
         * who booked the flight.
         * @param {Object} flight The flight that is being booked.
         * @param {Object} personObj The OpenSocial person object of the user who booked the flight.
         * @param {String} approverId The Connections id of the user's first line manager.
         */
        postASEntry : function(flight, personObj, approverId) {
            var entry = this.getActivityEntry(flight, personObj, approverId);
            entry.openSocial.embed = this.getEEDataModel(flight);
            var self = this;
            var activityStreamService = new ActivityStreamService({endpoint:"conAcmeApp"});
            activityStreamService.postEntry(entry).then(
                function(response) {
                    console.log(response);
                },
                function(error) {
                    alert(error.message);
                }
            );
        },
        
        /**
         * Gets the activity entry for booking a flight.
         * @param {Object} personObj The OpenSocial person object for the person booking the flight.
         * @param {String} approverId The Connections ID of the first line manager booking the flight.
         * @return {Object} The activity entry to post to the activity stream.
         */
        getActivityEntry : function(flight, personObj, approverId) {
            var homePageUrl = this.getHomePageUrl();
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
        },
        
        /**
         * Gets the embedded experience data model.
         * See the definition of the embedded experience data model in the 
         * <a href="http://opensocial-resources.googlecode.com/svn/spec/2.5/Core-Gadget.xml#Embedded-Experiences">
         * OpenSocial spec</a>.
         * @return {Object} The embedded experience data model.
         */
        getEEDataModel : function(flight) {
            return {
                "gadget" : this.getGadgetUrl(),
                "context" : flight
            };
        },
        
        /**
         * Gets the home page URL of the Acme Airlines app.
         * @return {String} The home page URL of the Acme Airlines app.
         */
        getHomePageUrl : function() {
            var endpoint = Endpoint.find('acmeAir');
            var baseUrl = endpoint.baseUrl;
            var path = require.toUrl('acmesocial');
            path = path.substring(0, path.lastIndexOf('/')+1);
            return baseUrl + path;
        },
        
        /**
         * Gets the embedded experience gadget URL.
         * @return {String} The embedded experience gadget URL.
         */
        getGadgetUrl : function() {
            return this.getHomePageUrl() + 'gadgets/airlines/airlines.xml';
        }        
        
    };
});