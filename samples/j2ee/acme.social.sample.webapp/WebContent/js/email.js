/**
 * Helper module for send an embedded experience email.
 * @module
 */
define(["sbt/declare", "sbt/config", "sbt/emailService"], 
    function(declare, config, emailService) {

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
            
            emailService.send(email).then(function(response) {
                console.log(response);
            }, function(response) {
                alert(response.cause.message);
            });
        },
        
        /**
         * Gets the home page URL of the Acme Airlines app.
         * @return {String} The home page URL of the Acme Airlines app.
         */
        getHomePageUrl : function() {
            var endpoint = config.findEndpoint('acmeAir');
            var baseUrl = endpoint.baseUrl;
            var path = require.toUrl('acmesocial');
            path = path.substring(0, path.lastIndexOf('/')+1);
            return baseUrl + path;
        }
        
    };
});