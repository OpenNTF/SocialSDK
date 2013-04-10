require([ "sbt/Endpoint", "sbt/emailservice", "sbt/json", "sbt/dom"], 
    function(Endpoint, emailService, json, dom) {

    /**
     * Sends an email notification about a flight being booked.
     * @param {Object} flight The flight being booked.
     * @param {String} displayName The name of the user who booked the flight.
     */
    function sendEmail(flight, displayName, eeDataModel) {
        var self = this;
        var homePageUrl = getHomePageUrl();
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
        
        emailService.send(email, {
            error: function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            },
            success : function(response) {
                dom.setText("json", json.jsonBeanStringify(response));
            }
        });
    };
    
    /**
     * Gets the home page URL of the Acme Airlines app.
     * @return {String} The home page URL of the Acme Airlines app.
     */
    function getHomePageUrl() {
        var serviceUrl = sbt.Properties.serviceUrl;
        return serviceUrl.substring(0, serviceUrl.lastIndexOf('/')+1);
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
        "FlightId":"103", "UserId":"%{sample.email1}", "ApproverId":"%{sample.email3}", "Reason":"business", "state":"started"
    };
    var displayName = "%{sample.displayName1}";
    var eeDataModel = {
        "gadget" : getGadgetUrl(), "context" : flight
    };
    sendEmail(flight, displayName, eeDataModel);
});