/*
 * © Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

/**
 * 
 */
define([ "../../../declare", "../../../Endpoint", "../../../xml", "../../../xpath", "../../../connections/ConnectionsConstants",
         "../../../connections/controls/profiles/ProfileGrid", 
         "../../../connections/controls/profiles/ColleagueGridRenderer", 
         "../../../connections/controls/ViewAllAction" ], 
        function(declare, Endpoint, xml, xpath, conn, ProfileGrid, ColleagueGridRenderer, ViewAllAction) {

    /**
     * @class ColleagueGrid
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ColleagueGrid
     */
	var ColleagueGrid = declare(ProfileGrid, {
    	
        /**
         * A view all action, defines default behaviour for when 
         * View All link is selected
         */
        viewAllAction: new ViewAllAction(),
                
        /**
         * Creates a renderer for the grid.The renderer is responsible for 
         * loading the grid's HTML content.
         * @method createDefaultRenderer
         * @param args sets the template the renderer will use, by checking args.type, but for
         * profile grids this will always be "profile"
         * @returns an instance of a  profile gird renderer.
         */
        createDefaultRenderer : function(args) {
            return new ColleagueGridRenderer(args);
        },
        
        /**
         * The post create function is called, after the grid has been created.
         * The function will call the super classes post create
         * then if target emails/ids were set will.
         * @method postCreate
         */
        postCreate: function() {            
            this.inherited(arguments);
            
            if (this.targetEmails || this.targetUserids) {
                this.addColleagues();
            }
        },
        
        addColleagues: function() {
            var targets = arguments.targetEmails || arguments.targetUserids || this.targetEmails || this.targetUserids;
            var id = arguments.email || arguments.userid || this.email || this.userid;
            if (!targets || !id) {
                this.renderer.renderEmpty(this, this.gridNode);
                return;
            }
            var endpoint = Endpoint.find(this.endpoint || "connections");
            var baseUrl = null;
            if (this.targetEmails) {
                baseUrl = "/profiles/atom/connection.do?connectionType=colleague&sourceEmail=" + 
                              encodeURIComponent(this.email) + "&targetEmail=";
            } else {
                baseUrl = "/profiles/atom/connection.do?connectionType=colleague&sourceUserid=" + 
                              encodeURIComponent(this.userid) + "&targetUserid=";
            }
            var self = this;
            for (var i=0; i<targets.length; i++) {
                if (id == targets[i]) {
                    self.addProfile(endpoint, id);
                } else {
                    var targetUrl = baseUrl + encodeURIComponent(targets[i]);      
                    endpoint.xhrGet({
                        serviceUrl: targetUrl,
                        handleAs: "text",
                        load: function(response) {
                            var document = xml.parse(response);
                            var email = xpath.selectText(document, "/a:entry/a:contributor/a:email", conn.Namespaces);
                            self.addProfile(endpoint, email);
                        },
                        error: function(error) {
                            // can ignore this, means user is not a colleague
                        }
                    });
                }
            }
        },
        
        addProfile: function(endpoint, id, index) {
            if (!index) {
                index = 0;
            }
            var self = this;
            var content = {};
            if (id.indexOf('@')>=0) {
                content.email = id;
            } else {
                content.userid = id;
            }
            endpoint.xhrGet({
                serviceUrl : "/profiles/atom/profile.do",
                handleAs: "text",
                content: content,
                load: function(profile) {
                    self.insertItem(xml.parse(profile), index);
                    self.update();
                },
                error: function(error) {
                    self._updateWithError(error);
                }
            });
        },
        
        /**
         * In the grid HTML an element can have an event attached 
         * using dojo-attach-event="onClick: handleViewAll".
         * This method is the handler for the onclick event.
         * @method handleViewAll
         * @param el the element that fired the event
         * @param data all of the items from the current row of the grid. 
         * @param ev the event 
         */
        handleViewAll: function(el, data, ev) {
            if (this.viewAllAction) {
                console.log(data);
                dojo.stopEvent(ev); // TODO add this to the bridge. dojo/_base/event
                
                this.viewAllAction.execute(data, { grid : this.grid }, ev);
            }
        },
        
        _constructTargetUrl: function() {
            
        }
        
    });

    return ColleagueGrid;
});