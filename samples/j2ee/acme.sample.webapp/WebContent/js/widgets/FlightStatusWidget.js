/**
 * Widget which represents the flight status page.
 */
define(['dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin'],
        function(declare, WidgetBase, TemplatedMixin) {
            return declare('FlightStatusWidget', [ WidgetBase, TemplatedMixin ], {
                templateString : '<h1>Flight Status</h1>',
            });
        });