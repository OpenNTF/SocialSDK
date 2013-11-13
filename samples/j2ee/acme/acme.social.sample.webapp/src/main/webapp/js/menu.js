/**
 * All of the menu items for the Acme Airlines app.
 */
define(['acme/widgets/HomeWidget', 'acme/widgets/CheckInWidget', 'acme/widgets/FlightStatusWidget',
        'acme/widgets/ServicesWidget', 'acmesocial/widgets/FlightsWidget', 'acmesocial/widgets/MyFlightsWidget',
        'dojo/domReady!'], 
        function(HomeWidget, CheckInWidget, FlightStatusWidget, ServicesWidget, FlightsWidget, MyFlightsWidget){
    return [ {
        name : "Home",
        active : true,
        widget : HomeWidget
    }, {
        name : "Flights",
        active : false,
        widget : FlightsWidget
    }, { 
        name : "My Flights",
        active : false,
        widget : MyFlightsWidget
    }, {
        name : "Flight Status",
        active : false,
        widget : FlightStatusWidget
    }, {
        name : "Check-in",
        active : false,
        widget : CheckInWidget
    }, {
        name : "Services",
        active : false,
        widget : ServicesWidget
    } ];
});