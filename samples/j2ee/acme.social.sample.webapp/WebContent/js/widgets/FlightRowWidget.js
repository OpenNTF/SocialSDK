/**
 * Widget for an individual flight row.
 */
define(['require', 'dojo/_base/declare', 'dojo/query', 'acme/widgets/FlightRowWidget', 'acme/flights', 'acme/templateUtils',
        'acmesocial/login', 'acmesocial/email', 'acmesocial/activitystream', 'sbt/controls/grid/connections/ColleagueGrid' ],
        function(require, declare, query, FlightRowWidget, flights, templateUtils, login, email, activitystream, ColleagueGrid) {
            return declare('FlightRowWidget', [ FlightRowWidget ], {
                
                postCreate : function() {
                    var self = this;
                    query('button', this.domNode).on('click', function(e) {
                        self.bookFlight.call(self, e);
                    });
                   
                    var gridDiv = query('div', this.domNode);
                    if (gridDiv && gridDiv.length > 0) {
                        var self = this;
                        var successCallback = function(users) {
                            //if (!users || users.length == 0) {
                            //    return;
                            //}
                            try {
                            var grid = new ColleagueGrid({
                                type: "dynamic",
                                rendererArgs: { hideViewAll: true, template: templateUtils.getTemplateString("#flightColleagues") },
                                email: login.getUserEmail(),
                                targetEmails: users
                            });
                            gridDiv[0].appendChild(grid.domNode);
                            } catch (error) {
                                var msg = error.message;
                            }
                        };
                        flights.getUsersForFlight({flightId: this.Flight, loadCallback: successCallback});
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