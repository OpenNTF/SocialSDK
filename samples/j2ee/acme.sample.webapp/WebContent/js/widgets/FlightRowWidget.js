/**
 * Widget for an individual flight row.
 */
define(['require', 'dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin', 'dojo/_base/array',
        'acme/templateUtils', 'sbt/Endpoint', 'dojo/query', 'acme/flights', 'dojo/NodeList-manipulate', 'dojo/NodeList-dom', 'dojo/ready', 'dojo/domReady!' ],
        function(require, declare, WidgetBase, TemplatedMixin, arrayUtil,  templateUtils, endpoint, query, flights) {
            return declare('FlightRowWidget', [ WidgetBase, TemplatedMixin ], {
                
                //Template defined in the DOM for this widget
                templateString : templateUtils.getTemplateString('#flightRow'),
                
                //Acme data service endpoint
                acmeEndpoint : endpoint.find('acmeAir'),
                
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
                    var newFlight = {
                            UserId : "Anonymous",
                            FlightId : this.Flight,
                            ApproverId : "Anonymous"
                        };
                    var self = this;
                    flights.bookFlight({
                        flight: newFlight, 
                        loadCallback: function(response){
                            self.bookingCallback.call(self, response, newFlight);
                        }, 
                        errorCallback: function(response){
                            self.errorCallback.call(self, response);
                        }
                    });
                },
                
                /**
                 * General error callback for all requests.
                 * @param {Object} response The response object for the request.
                 */
                errorCallback : function(response) {
                    console.error("There was an error.");
                },
                
                /**
                 * General error callback for all requests.
                 * @param {Object} response The response object for the request.
                 */
                bookingCallback : function(response, flight) {
                    query('button', this.domNode).replaceWith('<button class="btn btn-success" disabled="disabled" type="button">Booked!</button>');
                },
                
                /**
                 * Gets the home page URL of the Acme Airlines app.
                 * @return {String} The home page URL of the Acme Airlines app.
                 */
                getHomePageUrl : function() {
                    var baseUrl = this.acmeEndpoint.baseUrl;
                    var path = require.toUrl('acme') + '/../';
                    return baseUrl + path;
                }
            });
        });