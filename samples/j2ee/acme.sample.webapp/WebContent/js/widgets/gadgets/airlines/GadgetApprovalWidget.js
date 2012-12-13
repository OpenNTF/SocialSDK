/**
 * Approval widget.
 */
define(['dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin',
        'acme/templateUtils', 'dojo/query', 'acme/flights', 'dojo/NodeList-manipulate', 
        'dojo/NodeList-dom', 'dojo/ready', 'dojo/domReady!'],
        function(declare, WidgetBase, TemplatedMixin, templateUtils, query, flights) {
            return declare('GadgetApprovalWidget', [WidgetBase, TemplatedMixin], {
                //Template For approval UI in the DOM
                templateString : templateUtils.getTemplateString('#eeTmpl'),
            });
        });