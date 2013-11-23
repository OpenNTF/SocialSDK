/**
 * Widget which represents the services page.
 */
define(['dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin'],
        function(declare, WidgetBase, TemplatedMixin) {
            return declare('ServicesWidget', [ WidgetBase, TemplatedMixin ], {
                templateString : '<h1>Services</h1>',
            });
        });