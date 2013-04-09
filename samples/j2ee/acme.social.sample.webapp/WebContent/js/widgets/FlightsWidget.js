/**
 * Widget which represents the flights page.
 */
define(['dojo/_base/declare', 'acme/widgets/FlightsWidget', 'acmesocial/widgets/FlightRowWidget'],
        function(declare, FlightsWidget, FlightRowWidget) {
            return declare('FlightsWidget', [ FlightsWidget ], {
                /**
                 * 
                 */
                createFlightRow : function(flight) {
                    return new FlightRowWidget(flight);
                }
            });
        });