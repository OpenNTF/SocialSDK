/**
 * Widget for an individual flight row.
 */
define(['require', 'dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin', 'dojo/_base/array',
        'acme/templateUtils', 'sbt/Endpoint', 'sbt/xpath', 'sbt/xml', 'sbt/connections/core',
        'dojo/query', 'acme/flights', 'sbt/emailservice', 'sbt/connections/ActivityStreamService', 
        'dojo/NodeList-manipulate', 'dojo/NodeList-dom', 'dojo/ready', 'dojo/domReady!' ],
        function(require, declare, WidgetBase, TemplatedMixin, arrayUtil,  templateUtils, endpoint,
                xpath, xml, connections, query, flights, emailService, ActivityStreamService) {
            return declare('FlightRowWidget', [ WidgetBase, TemplatedMixin ], {
                
                //Template defined in the DOM for this widget
                templateString : templateUtils.getTemplateString('#flightRow'),
                
                //Connections endpoint
                conEndpoint : endpoint.find('connections'),
                
                //Acme data service endpoint
                acmeEndpoint : endpoint.find('acmeAir'),
                
                //XPath to get a user's first line manager's email from a report chain feed
                firstLineEmailXPath : '/a:feed/a:entry[2]/a:contributor/a:email',
                
                //XPath to get a user's first line manager's Connections ID from a report chain feed
                firstLineConIdXPath : '/a:feed/a:entry[2]/a:contributor/snx:userid',
                
                postCreate : function() {
                    var self = this;
                    query('button', this.domNode).on('click', function(e) {
                        self.bookFlight.call(self, e);
                    });
                },
                
                /**
                 * The onclick handler for when the user books a flight.
                 * @param {Object} e The on click event.
                 */
                bookFlight : function(e) {
                    var self = this;
                    this.conEndpoint.xhrGet({
                        serviceUrl: "/connections/opensocial/oauth/rest/people/@me/",
                        handleAs: 'json',
                        loginUi: 'popup',
                        load: function(response) {
                            self.personCallback.call(self, response);
                        },
                        error: function(response) {
                            self.errorCallback.call(self, response);
                        }
                    });
                },
                
                /**
                 * General error callback for all requests.
                 * @param {Object} response The response object for the request.
                 */
                //TODO Need to improve error handling
                errorCallback : function(response) {
                    console.error("There was an error.");
                },
                
                /**
                 * Gets the Connection id from an OpenSocial person object.
                 * @param {Object} personObject The OpenSocial person object.
                 * @return {String} The Connections ID.
                 */
                getConId : function(personObject) {
                    //Remove the extra OpenSocial cruff to get the connections id
                    return personObject.id.replace('urn:lsid:lconn.ibm.com:profiles.person:', '');
                },
                
                /**
                 * The callback called after fetching the information about the current user.
                 * @param {Object} response The response to fetching the current user.
                 */
                personCallback : function(response) {
                    var self = this;
                    var conId = this.getConId(response.entry);
                    var personResponse = response;
                    this.conEndpoint.xhrGet({
                        serviceUrl: "/profiles/oauth/atom/reportingChain.do?userid=" + conId,
                        loginUi: 'popup',
                        load: function(response) {
                            self.reportChainCallback.call(self, response, personResponse.entry);
                        },
                        error: function(response) {
                            self.errorCallback.call(self, response);
                        }
                    });
                },
                
                /**
                 * The callback which handles the response from requesting the current user's report chain.
                 * @param {Object} response The response to the request for the user' report chain.
                 * @param {Object} personObj The OpenSocial person for the current user.
                 */
                reportChainCallback : function(response, personObj) {
                    var data = xml.parse(response);
                    var email = xpath.selectText(data, this.firstLineEmailXPath, connections.namespaces);
                    var conId = xpath.selectText(data, this.firstLineConIdXPath, connections.namespaces);
                    var userId = '';
                    arrayUtil.some(personObj.emails, function(emailObj){
                        if(emailObj.primary) {
                            userId = emailObj.value;
                            //this is the same as breaking in a normal for loop
                            return false;
                        } 
                     });
                    var newFlight = {
                        UserId : userId,
                        FlightId : this.Flight,
                        ApproverId : email
                    };
                    var self = this;
                    flights.bookFlight({
                        flight: newFlight, 
                        loadCallback: function(response){
                            self.bookingCallback.call(self, response, newFlight, personObj, conId);
                        }, 
                        errorCallback: function(response){
                            self.errorCallback.call(self, response);
                        }});
                },
                
                /**
                 * Gets the embedded experience gadget URL.
                 * @return {String} The embedded experience gadget URL.
                 */
                getGadgetUrl : function() {
                    var baseUrl = this.acmeEndpoint.baseUrl;
                    var path = require.toUrl('acme') + '/../gadgets/airlines/airlines.xml';
                    return baseUrl + path;
                },
                
                /**
                 * Gets the home page URL of the Acme Airlines app.
                 * @return {String} The home page URL of the Acme Airlines app.
                 */
                getHomePageUrl : function() {
                    var baseUrl = this.acmeEndpoint.baseUrl;
                    var path = require.toUrl('acme') + '/../';
                    return baseUrl + path;
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
                 * Gets the activity entry for booking a flight.
                 * @param {Object} personObj The OpenSocial person object for the person booking the flight.
                 * @param {String} firstLineConId The Connections ID of the first line manager booking the flight.
                 * @return {Object} The activity entry to post to the activity stream.
                 */
                getActivityEntry : function(personObj, firstLineConId) {
                    var homePageUrl = this.getHomePageUrl();
                    var requestorId = this.getConId(personObj);
                    var id = requestorId + ':' + firstLineConId + ":" + this.Flight;
                    return {
                        generator : {
                            image : {
                                url : homePageUrl + "favicon.ico"
                            },
                            id : "AcmeAirlines",
                            displayName : "Acme Airlines",
                            url : this.getHomePageUrl(),
                        },
                        actor : {
                            id : requestorId,
                        },
                        verb : "created",
                        title : "${Actor} created a ${Object} in the ${Target}.",
                        content : "Please approve " + personObj.displayName + "'s flight request for flight " + this.Flight + ".",
                        updated : "2012-01-01T12:00:00.000Z",
                        object : {
                            summary : personObj.displayName + " is requesting to fly from " + this.Depart + " to " + this.Arrive + " on flight "+ this.Flight + ".",
                            objectType : "flight",
                            id : this.Flight,
                            displayName : "Flight Request",
                            url : homePageUrl,
                        },
                        target : {
                            summary : "Airlines Booking App",
                            objectType : "application",
                            id : "AcmeAirlines",
                            displayName : "Acme Airlines Booking App",
                            url : this.getHomePageUrl()
                        },
                        openSocial : {
                            deliverTo : [{
                                objectType : "person",
                                id : firstLineConId
                            }]
                        },
                        connections : {
                            rollupid : id,
                            actionable: "true"
                        }
                    };
                },
                
                /**
                 * Sends an email notification about a flight being booked.
                 * @param {Object} flight The flight being booked.
                 * @param {String} displayName The name of the user who booked the flight.
                 */
                sendEmail : function(flight, displayName) {
                    var self = this;
                    var homePageUrl = this.getHomePageUrl();
                    var emails =
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
                                        "content" : this.getEEDataModel(flight)                                             
                                    }
                                ]
                        };
                    emailService.send(emails, {error: function(response) {
                        self.errorCallback.call(self, response);
                    },
                    success : function(response) {
                        if(response.error && response.error.length > 0) {
                            self.errorCallback.call(self, response);
                        }
                    }});
                },
                
                /**
                 * Posts an activity stream entry to the feed of the first line manager of the user
                 * who booked the flight.
                 * @param {Object} flight The flight that is being booked.
                 * @param {Object} personObj The OpenSocial person object of the user who booked the flight.
                 * @param {String} firstLineConId The Connections id of the user's first line manager.
                 */
                postASEntry : function(flight, personObj, firstLineConId) {
                    var entry = this.getActivityEntry(personObj, firstLineConId);
                    entry.openSocial.embed = this.getEEDataModel(flight);
                    var self = this;
                    var activityStreamService = new ActivityStreamService({endpoint:"conAcmeApp"});
                    activityStreamService.postEntry({postData : entry,
                        error : function(error){
                            self.errorCallback.call(self, response);
                        }});
                },
                
                /**
                 * The callback to the request made to book the flight.
                 * @param {Object} response The response to the request made to book the flight.
                 * @param {Object} flight The flight that was booked.
                 * @param {Object} The OpenSocial person object of the user who booked the flight.
                 * @param {String} The Connections ID of the user's first line manager.
                 */
                bookingCallback : function(response, flight, personObj, firstLineConId) {
                    this.sendEmail(flight, personObj.displayName, firstLineConId);
                    this.postASEntry(flight, personObj, firstLineConId);
                    query('button', this.domNode).replaceWith('<button class="btn btn-success" disabled="disabled" type="button">Booked!</button>');
                }
            });
        });