/**
 * Main JS file for the embedded experiences view of the Acme Airlines gadget.
 */
//TODO Consider breaking some of the functionality in here out into its own class, maybe move it into the GadgetApprovalWidget?
require(['dojo/domReady!'], function() {
    require(['dojo/_base/array', 'dojo/query', 'acme/flights', 
             'acme/widgets/gadgets/airlines/GadgetApprovalWidget', 'sbt/lang', 'dojo/NodeList-manipulate', 
             'dojo/NodeList-dom'], 
              function(arrayUtil, query, flights, GadgetApprovalWidget, lang) {
        
        var getAlertHtml = function(cssClass, text) {
            return '<div class="alert eeAlert ' + cssClass + '">' + text + '</div>';
        };
        
        var flightCallback = function(response, myFlight) {
            var flightFound = false;
            arrayUtil.some(response, function(flight) {
               if(flight.Flight == myFlight.FlightId) {
                   flightFound = true;
                   var mixedFlight = lang.mixin(flight, myFlight);
                   var eeWidget = new GadgetApprovalWidget(mixedFlight);
                   var mainContainerSpan = query('#mainContainer span');
                   mainContainerSpan.replaceWith(eeWidget.domNode);
                   gadgets.window.adjustHeight();
                   return false;
               } 
            });
            if(!flightFound) {
                query('#mainContainer span').innerHTML(getAlertHtml('alert-info', 
                        'We could not find a flight for this request, no action is required now.'));
            }
        };
        
        var myFlightsCallback = function(response, flightId, userId) {
            var flightFound = false;
            arrayUtil.some(response, function(flight) {
                if(flight.FlightId == flightId && flight.UserId.toUpperCase() == userId.toUpperCase()) {
                    flightFound = true;
                    if(flight.state) {
                        if(flight.state.toUpperCase() != 'STARTED') {
                            query('#mainContainer span').innerHTML(getAlertHtml('alert-success', 
                                    'This flight request has already been responded to.'));
                            //break out of the loop
                            return false;
                        }
                        
                        //Get all the flights so we can find the other details about the flight that 
                        //the user needs to approve
                        flights.getAllFlights({
                            loadCallback : function(response) {
                                flightCallback(response, flight);
                            },
                            errorCallback : function(response) {
                                console.error("There was an error retrieving flight information");
                            }});
                        //break out of the loop
                        return false;
                    }
                }
            });
            if(!flightFound) {
                query('#mainContainer span').innerHTML(getAlertHtml('alert-info', 
                        'We could not find a flight for this request, no action is required now.'));
            }
        };
    });   
});
