/**
 * Widget which represents the check-in page for a gadget.
 */
define(['dojo/_base/declare', 'acme/widgets/CheckInWidget'],
        function(declare, CheckInWidget) {
            return declare('GadgetCheckInWidget', [CheckInWidget], {
                templateString : '<h1>Gadget Check In</h1>',
            });
        });