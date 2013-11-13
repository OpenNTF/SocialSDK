/**
 * Widget for an individual flight row.
 */
define(['dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin', 'acme/templateUtils',
        'dojo/query', 'dojo/NodeList-manipulate', 'dojo/NodeList-dom', 'dojo/ready', 
        'dojo/domReady!'],
        function(declare, WidgetBase, TemplatedMixin, templateUtils, query) {
            return declare('MyFlightRowWidget', [ WidgetBase, TemplatedMixin ], {
                //Template string to use for all of the user's flights
                templateString : templateUtils.getTemplateString('#myFlightRow')
            });
        });