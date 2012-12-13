/**
 * Widget which represents the my flights page.
 */
define(['dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin', 'acme/widgets/MyFlightRowWidget',
        'acme/flights', 'dojo/_base/array', 'acme/templateUtils', 'sbt/Endpoint', 'dojo/query',
        'dojo/NodeList-manipulate', 'dojo/NodeList-dom', 'dojo/ready', 'dojo/domReady!'],
        function(declare, WidgetBase, TemplatedMixin, MyFlightRowWidget, flights,
                 arrayUtil, templateUtils, endpoint, query) {
            return declare('MyFlightsWidget', [WidgetBase, TemplatedMixin], {
                //Template for the widget contained in the DOM
                templateString : templateUtils.getTemplateString('#myFlightsTmpl')
            });
        });