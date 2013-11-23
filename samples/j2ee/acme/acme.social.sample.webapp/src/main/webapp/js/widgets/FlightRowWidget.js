/**
 * Widget for an individual flight row.
 */
define(['require', 'dojo/_base/declare', 'dojo/query', 'acme/widgets/FlightRowWidget', 'acme/flights', 'acme/templateUtils',
        'acmesocial/login', 'acmesocial/email', 'acmesocial/activitystream'],
        function(require, declare, query, FlightRowWidget, flights, templateUtils, login, email, activitystream) {
            return declare('FlightRowWidget', [ FlightRowWidget ], {
                
                postCreate : function() {
                    var self = this;
                    query('button', this.domNode).on('click', function(e) {
                        self.bookFlight.call(self, e);
                    });
                   
                    var url = require.toUrl('acmesocial');
                    //We cannot support Dijits across domains yet due to the use of the Dojo text plugin
                    //making XHR calls.
                    //When the module path is on the same domain as the page than require.toUrl
                    //will return something like /path/to/modules.  When it is on a different domain
                    //it will return something like http://domain/path/to/module.
                    if(url.indexOf('/') === 0 && url.charAt(1) !== '/'){
                    	require(['sbt/connections/controls/profiles/ColleagueGrid'], function(ColleagueGrid) {
                    		var gridDiv = query('div', self.domNode);
                            if (gridDiv && gridDiv.length > 0) {
                                var successCallback = function(users) {
                                    //if (!users || users.length == 0) {
                                    //    return;
                                    //}
                                    try {
                                    var grid = new ColleagueGrid({
                                        type: "dynamic",
                                        hideViewAll: true,
                                        rendererArgs: { template: templateUtils.getTemplateString("#flightColleagues") },
                                        email: login.getUserEmail(),
                                        targetEmails: users
                                    });
                                    gridDiv[0].appendChild(grid.domNode);
                                    } catch (error) {
                                        var msg = error.message;
                                    }
                                };
                                flights.getUsersForFlight({flightId: self.Flight, loadCallback: successCallback});
                            }
                    	});
                    }
                    
                },
                
                /**
                 * The onclick handler for when the user books a flight.
                 * @param {Object} e The on click event.
                 */
                bookFlight : function(e) {
                    var userEmail = login.getUserEmail();
                    var approverEmail = login.getApproverEmail();
                    if (approverEmail) {
                        this.doBookFlight(userEmail, approverEmail);
                        return;
                    }
                    
                    var self = this;
                    login.doGetReportingChain({
                        success: function(response) {
                            approverEmail = login.getApproverEmail();
                            self.doBookFlight(userEmail, approverEmail);
                        },
                        error: function(error) {
                            alert(error.message);
                        }
                    });
                },
                
                /**
                 * 
                 */
                doBookFlight : function(userId, approverId) {
                    var newFlight = {
                            UserId : userId,
                            FlightId : this.Flight,
                            ApproverId : approverId,
                            Arrive : this.Arrive,
                            Depart : this.Depart
                        };
                    var self = this;
                    flights.bookFlight({
                        flight: newFlight, 
                        loadCallback: function(response){
                            self.bookingCallback.call(self, response, newFlight);
                        }, 
                        errorCallback: function(response){
                            self.errorCallback.call(self, response);
                        }
                    });
                },
                
                /**
                 * General error callback for all requests.
                 * @param {Object} response The response object for the request.
                 */
                bookingCallback : function(response, flight) {
                    query('button', this.domNode).replaceWith('<button class="btn btn-success" disabled="disabled" type="button">Booked!</button>');
                    try {
                    email.sendEmail(flight, login.getUserDisplayName(), activitystream.getEEDataModel(flight));
                    activitystream.postASEntry(flight, login.getPerson(), login.getApproverUserId());
                    } catch (error) {
                    console.error(error.message);
                    }
                }
            });
        });