/**
 * Widget which represents the my flights page.
 */
define(['dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin', 'acme/widgets/MyFlightRowWidget',
        'acme/flights', 'dojo/_base/array', 'acme/templateUtils', 'sbt/Endpoint', 'dojo/query',
        'dojo/NodeList-manipulate', 'dojo/NodeList-dom', 'dojo/ready', 'dojo/domReady!'],
        function(declare, WidgetBase, TemplatedMixin, MyFlightRowWidget, flights,
                 arrayUtil, templateUtils, endpoint, query) {
            return declare('MyFlightsWidget', [WidgetBase, TemplatedMixin], {
                //Template for the widget contained in the DOM
                templateString : templateUtils.getTemplateString('#myFlightsTmpl'),
                
                //Connections endpoint
                conEndpoint : endpoint.find('connections'),
                
                postCreate : function() {
                    var self = this;
                    this.conEndpoint.xhrGet({
                        serviceUrl: "/connections/opensocial/oauth/rest/people/@me/",
                        loginUi: "popup",
                        handleAs: "json",
                        load: function(response) {
                            self.personCallback.call(self, response);
                        },
                        error: function(response) {
                            self.errorCallback.call(self, response);
                        }
                    });
                },
                
                /**
                 * Callback for the request to get the current users information from Connections.
                 * @param {Object} response The response from making the request to get the current 
                 * user's information.
                 */
                personCallback : function(response) {
                    var self = this;
                    var emails = response.entry.emails;
                    arrayUtil.some(emails, function(emailObj){
                       if(emailObj.primary) {
                           flights.getFlightsForUser({
                               loadCallback: function(response) {
                                   self.myFlightsCallback.call(self, response);
                               }, 
                               errorCallback: function(response) {
                                   self.errorCallback(self, response);
                               }, 
                               userId: emailObj.value});
                           //this is the same as breaking in a normal for loop
                           return false;
                       } 
                    });
                },
                
                /**
                 * Error callback for all requests.
                 */
                //TODO Improve error handling
                errorCallback : function(response) {
                    console.error("There was an error");
                },
                
                /**
                 * Called after request all the flights for the current user.
                 * @param {Object} response The response from requesting all the user's flights.
                 */
                myFlightsCallback : function(response) {
                    var self = this;
                    arrayUtil.forEach(response, function(flight) {
                        var row = new MyFlightRowWidget(flight);
                        query('#myFlightsTable tbody').append(row.domNode);
                        self.startup();
                    });
                }
            });
        });