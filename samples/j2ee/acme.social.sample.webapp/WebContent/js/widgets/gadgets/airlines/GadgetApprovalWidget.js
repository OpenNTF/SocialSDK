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
                
                postCreate : function() {
                    var self = this;
                    query('.btn-primary', this.domNode).on('click', function(e) {
                        self.updateFlight(e, 'approved');
                    });
                    query('.btn-danger', this.domNode).on('click', function(e) {
                        self.updateFlight(e, 'denied');
                    });
                },
                
                /**
                 * Called when the button to approve or deny the travel request is clicked.
                 * @param {Object} event The onclick event.
                 * @param {String} status The status of the approval, either approved or denied.
                 */
                updateFlight : function(event, status) {
                    flights.updateMyFlight({
                        flight : {
                            FlightId : this.FlightId,
                            UserId : this.UserId,
                            ApproverId : this.ApproverId,
                            state : status
                        },
                        errorCallback : function(response) {
                            console.error('There was an error updating the flight.');
                        },
                        loadCallback : function(response) {
                            query('#mainContainer div').replaceWith('<div class="alert alert-info eeAlert">Response submitted</div>');
                        }
                    });
                }
            });
        });