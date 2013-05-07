/**
 * Helper module for send an embedded experience email.
 * @module
 */
define(["sbt/_bridge/declare", "sbt/Endpoint", "sbt/emailservice"], 
    function(declare, Endpoint, emailService) {

    return {
        /**
         * Sends an email notification about a flight being booked.
         * @param {Object} flight The flight being booked.
         * @param {String} displayName The name of the user who booked the flight.
         */
        sendEmail : function(flight, displayName, eeDataModel) {
            var self = this;
            var homePageUrl = this.getHomePageUrl();
            var email =
                {
                    "to" : [flight.ApproverId],
                    "cc" : [flight.UserId],
                    "subject" : "Travel Request For " + displayName,
                    "mimeParts" : 
                        [
                            {
                                "mimeType" : "text/plain",
                                "content" : "Please approve the travel request for " + displayName + " at " + homePageUrl,
                            },
                            {
                                "mimeType" : "text/html",
                                "content" : "Please approve the travel request for " + displayName + " at <a href=\""+ homePageUrl + "\">Acme Air</a>.",
                            },
                            {
                                "mimeType" : "application/embed+json",
                                "content" : eeDataModel                                             
                            }
                        ]
                };
            
            emailservice.send(email, {
                error: function(response) {
                    alert(response);
                },
                load : function(response) {
                    console.log(response);
                }
            });
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
        }
        
    };
});