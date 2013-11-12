/**
 * Widget which represents the services page for a gadget.
 */
define(['dojo/_base/declare', 'acme/widgets/ServicesWidget'],
        function(declare, ServicesWidget) {
            return declare('GadgetServicesWidget', [ ServicesWidget ], {
                templateString : '<h1>Gadget Services</h1>',
            });
        });