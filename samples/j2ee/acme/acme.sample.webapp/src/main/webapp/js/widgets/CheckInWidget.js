/**
 * Widget which represents the check-in page.
 */
define(['dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin'],
        function(declare, WidgetBase, TemplatedMixin) {
            return declare('CheckInWidget', [WidgetBase, TemplatedMixin], {
                templateString : '<h1>Check In</h1>',
            });
        });