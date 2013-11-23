/**
 * Widget which represents the flights page.
 */
define(['dojo/_base/declare', 'acme/widgets/MyFlightsWidget', 'acmesocial/login'],
        function(declare, MyFlightsWidget, login) {
            return declare('MyFlightsWidget', [ MyFlightsWidget ], {
                /**
                 * 
                 */
                getUserId: function() {
                    return login.getUserEmail();
                }
            });
        });