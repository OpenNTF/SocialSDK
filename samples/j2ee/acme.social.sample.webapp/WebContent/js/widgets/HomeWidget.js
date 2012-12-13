/**
 * Widget which represents the home page.
 */
define(['dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin',
        'acme/templateUtils'],
        function(declare, WidgetBase, TemplatedMixin, templateUtils) {
            return declare('HomeWidget', [ WidgetBase, TemplatedMixin ], {
                
                //Template to use from the DOM
                templateString : templateUtils.getTemplateString('#homeTmpl')
            });
        });