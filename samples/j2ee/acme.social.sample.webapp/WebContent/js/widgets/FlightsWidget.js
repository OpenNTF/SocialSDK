/**
 * Widget which represents the flights page.
 */
define(['dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin',
        'acme/widgets/FlightRowWidget', 'acme/flights', 'dojo/_base/array',
        'acme/templateUtils', 'dojo/query', 'dojo/NodeList-manipulate', 'dojo/NodeList-dom', 
        'dojo/ready', 'dojo/domReady!'],
        function(declare, WidgetBase, TemplatedMixin,FlightRowWidget, flights,
                arrayUtil, templateUtils, query) {
            return declare('FlightsWidget', [ WidgetBase, TemplatedMixin ], {
                //Template defined in the DOM for this widget
                templateString : templateUtils.getTemplateString('#flightsTmpl'),
                
                postCreate : function() {
                    var self = this;
                    var successCallback = function(flights) {
                        arrayUtil.forEach(flights, function(flight) {
                            var row = new FlightRowWidget(flight);
                            query('#flightsTable tbody').append(row.domNode);
                            self.startup();
                        });
                    };
                    
                    var error = function(error) {
                        console.error("There was an error retrieving flight information");
                    };
                    
                    flights.getAllFlights({loadCallback: successCallback, errorCallback: error});
                }
            });
        });