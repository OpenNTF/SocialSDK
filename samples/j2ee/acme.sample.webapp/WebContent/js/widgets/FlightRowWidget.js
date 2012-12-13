/**
 * Widget for an individual flight row.
 */
define(['require', 'dojo/_base/declare', 'dijit/_WidgetBase', 'dijit/_TemplatedMixin', 'dojo/_base/array',
        'acme/templateUtils', 'sbt/Endpoint', 'sbt/xpath', 'sbt/xml', 'sbt/connections/core',
        'dojo/query', 'acme/flights', 'sbt/emailservice', 'sbt/connections/ActivityStreamService', 
        'dojo/NodeList-manipulate', 'dojo/NodeList-dom', 'dojo/ready', 'dojo/domReady!' ],
        function(require, declare, WidgetBase, TemplatedMixin, arrayUtil,  templateUtils, endpoint,
                xpath, xml, connections, query, flights, emailService, ActivityStreamService) {
            return declare('FlightRowWidget', [ WidgetBase, TemplatedMixin ], {
                
                //Template defined in the DOM for this widget
                templateString : templateUtils.getTemplateString('#flightRow'),
                
                //XPath to get a user's first line manager's email from a report chain feed
                firstLineEmailXPath : '/a:feed/a:entry[2]/a:contributor/a:email',
                
                //XPath to get a user's first line manager's Connections ID from a report chain feed
                firstLineConIdXPath : '/a:feed/a:entry[2]/a:contributor/snx:userid',
                
                //Acme data service endpoint
                acmeEndpoint : endpoint.find('acmeAir'),
                
                /**
                 * Gets the Connection id from an OpenSocial person object.
                 * @param {Object} personObject The OpenSocial person object.
                 * @return {String} The Connections ID.
                 */
                getConId : function(personObject) {
                    //Remove the extra OpenSocial cruff to get the connections id
                    return personObject.id.replace('urn:lsid:lconn.ibm.com:profiles.person:', '');
                },
                
                /**
                 * Gets the embedded experience gadget URL.
                 * @return {String} The embedded experience gadget URL.
                 */
                getGadgetUrl : function() {
                    var baseUrl = this.acmeEndpoint.baseUrl;
                    var path = require.toUrl('acme') + '/../gadgets/airlines/airlines.xml';
                    return baseUrl + path;
                },
                
                /**
                 * Gets the home page URL of the Acme Airlines app.
                 * @return {String} The home page URL of the Acme Airlines app.
                 */
                getHomePageUrl : function() {
                    var baseUrl = this.acmeEndpoint.baseUrl;
                    var path = require.toUrl('acme') + '/../';
                    return baseUrl + path;
                },
                /**
                 * Gets the activity entry for booking a flight.
                 * @param {Object} personObj The OpenSocial person object for the person booking the flight.
                 * @param {String} firstLineConId The Connections ID of the first line manager booking the flight.
                 * @return {Object} The activity entry to post to the activity stream.
                 */
                getActivityEntry : function(personObj, firstLineConId) {
                    var homePageUrl = this.getHomePageUrl();
                    var requestorId = this.getConId(personObj);
                    var id = requestorId + ':' + firstLineConId + ":" + this.Flight;
                    return {
                        generator : {
                            image : {
                                url : homePageUrl + "favicon.ico"
                            },
                            id : "AcmeAirlines",
                            displayName : "Acme Airlines",
                            url : this.getHomePageUrl(),
                        },
                        actor : {
                            id : requestorId,
                        },
                        verb : "created",
                        title : "${Actor} created a ${Object} in the ${Target}.",
                        content : "Please approve " + personObj.displayName + "'s flight request for flight " + this.Flight + ".",
                        updated : "2012-01-01T12:00:00.000Z",
                        object : {
                            summary : personObj.displayName + " is requesting to fly from " + this.Depart + " to " + this.Arrive + " on flight "+ this.Flight + ".",
                            objectType : "flight",
                            id : this.Flight,
                            displayName : "Flight Request",
                            url : homePageUrl,
                        },
                        target : {
                            summary : "Airlines Booking App",
                            objectType : "application",
                            id : "AcmeAirlines",
                            displayName : "Acme Airlines Booking App",
                            url : this.getHomePageUrl()
                        },
                        openSocial : {
                            deliverTo : [{
                                objectType : "person",
                                id : firstLineConId
                            }]
                        },
                        connections : {
                            rollupid : id,
                            actionable: "true"
                        }
                    };
                }
            });
        });