/**
 * All of the menu items for the Acme Airlines app.
 */
define(['acme/widgets/HomeWidget', 'acme/widgets/CheckInWidget', 'acme/widgets/FlightStatusWidget',
        'acme/widgets/ServicesWidget', 'acme/widgets/FlightsWidget', 'acme/widgets/MyFlightsWidget', 
        'acme/widgets/gadgets/airlines/GadgetFlightsWidget', 'acme/widgets/gadgets/airlines/GadgetCheckInWidget', 
        'acme/widgets/gadgets/airlines/GadgetFlightStatusWidget', 'acme/widgets/gadgets/airlines/GadgetServicesWidget',
        'acme/widgets/gadgets/airlines/GadgetMyFlightsWidget', 'dojo/domReady!'], 
        function(HomeWidget, CheckInWidget, FlightStatusWidget, ServicesWidget, FlightsWidget, MyFlightsWidget,
                GadgetFlightsWidget, GadgetCheckInWidget, GadgetFlightStatusWidget, GadgetServiceWidget, GadgetMyFlightsWidget){
    return [ {
        name : "Home",
        active : true,
        widget : HomeWidget
    }, {
        name : "Flights",
        active : false,
        widget : FlightsWidget,
        gadgetWidget : GadgetFlightsWidget
    }, { 
        name : "My Flights",
        active : false,
        widget : MyFlightsWidget,
        gadgetWidget : GadgetMyFlightsWidget
    }, {
        name : "Flight Status",
        active : false,
        widget : FlightStatusWidget,
        gadgetWidget : GadgetFlightStatusWidget
    }, {
        name : "Check-in",
        active : false,
        widget : CheckInWidget,
        gadgetWidget : GadgetCheckInWidget
    }, {
        name : "Services",
        active : false,
        widget : ServicesWidget,
        gadgetWidget : GadgetServicesWidget
    } ];
});