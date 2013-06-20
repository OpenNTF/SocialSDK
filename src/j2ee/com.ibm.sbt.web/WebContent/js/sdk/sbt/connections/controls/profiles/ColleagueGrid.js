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
define([ "../../../declare", "../../../config", "../../../xml", "../../../xpath", "../../../stringUtil", 
         "../../../connections/ConnectionsConstants",
         "./ProfileGrid", "./ColleagueGridRenderer", "../ViewAllAction" ], 
        function(declare, config, xml, xpath, stringUtil, conn, ProfileGrid, ColleagueGridRenderer, ViewAllAction) {

    /**
     * @class ColleagueGrid
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ColleagueGrid
     */
	var ColleagueGrid = declare(ProfileGrid, {

		/**
		 * Flag used to display the View All link, set
		 * to true to hide this link
		 */
        hideViewAll: false,
        
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
            if (!id || (!targets || targets.length == 0)) {
                return;
            }
            var endpoint = config.findEndpoint(this.endpoint || "connections");
            var baseUrl = "/profiles/atom/connection.do?connectionType=colleague";
            if (this._isEmail(id)) {
                baseUrl += "&sourceEmail=" + encodeURIComponent(id);
            } else {
            	baseUrl += "&sourceUserid=" + encodeURIComponent(id);
            }
            if (arguments.targetEmails || this.targetEmails) {
                baseUrl += "&targetEmail=";
            } else {
                baseUrl += "&targetUserid=";
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
                            var email = self._getColleagueEmail(document, id);
                            if (email) {
                            	self.addProfile(endpoint, email);
                            }
                        },
                        error: function(error) {
                            // can ignore this, means user is not a colleague
                        }
                    });
                }
            }
        },
        
        /**
         * @method addProfile
         * @param endpoint
         * @param id
         * @param index
         */
        addProfile: function(endpoint, id, index) {
            if (!index) {
                index = 0;
            }
            var self = this;
            var content = {};
            if (this._isEmail(id)) {
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
                this._stopEvent(ev);
                
                this.viewAllAction.execute(data, { grid : this.grid }, ev);
            }
        },
        
        _constructTargetUrl: function() {
        },
        
        _getColleagueEmail: function(doc, id) {
        	var userids = this._selectArray(doc, "/a:entry/snx:connection/a:contributor/snx:userid");
        	var emails = this._selectArray(doc, "/a:entry/snx:connection/a:contributor/a:email");
        	if (this._isEmail(id) && emails.indexOf(id) >= 0 && emails.length > 1) {
        		var index = (emails.indexOf(id) == 0) ? 1 : 0;
                return emails[index];
            } else if(userids.indexOf(id) >= 0 && userids.length > 1) {
        		var index = (userids.indexOf(id) == 0) ? 1 : 0;
                return userids[index];
            }
        },
        
        _selectArray : function(doc, expr) {
            var nodes = xpath.selectNodes(doc, expr, conn.Namespaces);
            var ret = [];
            if (nodes) {
                for ( var i = 0; i < nodes.length; i++) {
                    ret.push(stringUtil.trim(nodes[i].text || nodes[i].textContent));
                }
            }
            return ret;
        },
        
        _isEmail: function(id) {
        	return id && id.indexOf('@') >= 0;
        }
        
    });

    return ColleagueGrid;
});