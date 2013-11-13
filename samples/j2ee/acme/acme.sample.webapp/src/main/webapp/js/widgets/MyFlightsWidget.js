/**
 * Widget which represents the my flights page.
 */
define(['dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin', 'acme/widgets/MyFlightRowWidget',
        'acme/flights', 'dojo/_base/array', 'acme/templateUtils', 'dojo/query',
        'dojo/NodeList-manipulate', 'dojo/NodeList-dom', 'dojo/ready', 'dojo/domReady!'],
        function(declare, WidgetBase, TemplatedMixin, MyFlightRowWidget, flights,
                 arrayUtil, templateUtils, query) {
            return declare('MyFlightsWidget', [WidgetBase, TemplatedMixin], {
                //Template for the widget contained in the DOM
                templateString : templateUtils.getTemplateString('#myFlightsTmpl'),
                
                postCreate : function() {
                    var self = this;
                    flights.getFlightsForUser({
                        loadCallback: function(response) {
                            self.myFlightsCallback.call(self, response);
                        }, 
                        errorCallback: function(response) {
                            self.errorCallback(self, response);
                        }, 
                        userId: this.getUserId()
                    });
                },
                
                getUserId: function() {
                    return "anonymous";
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