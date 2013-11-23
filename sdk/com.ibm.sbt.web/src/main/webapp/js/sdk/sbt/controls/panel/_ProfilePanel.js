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
 * @module sbt.controls.panel._ProfilePanel
 */
define(["../../declare", "../../lang", "../../dom", "../../widget/_TemplatedWidget"], 
        function(declare, lang, dom, _TemplatedWidget) {

    /**
     * @module sbt.controls.panel._ProfilePanel
     */
    var _ProfilePanel = declare([ _TemplatedWidget ], {
    	
    	templateString: "<div><strong> Loading profile... </strong></div>",
    	template: null,
    	
    	profile: null,
    	
    	errorClass: null,
    	
        constructor: function(args) {
            lang.mixin(this, args);
            
            if (this.templateId) {
            	this.template = this._getTemplate(this.templateId);
            }
        },
        
        postMixInProperties: function() {
        },

        postCreate: function() {
        	this.inherited(arguments);
        	
        	if (this.email || this.userid) {
        		this.getProfile(this.email || this.userid);
        	} else {
        		this.getMyProfile();
        	}
        },
        
        getMyProfile: function() {
        },
        
        getProfile: function(id) {
        },
        
        showProfile: function(profile) {
        	this.profile = profile || this.profile;
        	if (!this.profile) {
        		this._displayError(new Error("Invalid profile"));
        		return;
        	}
        	
        	try {
            	var el = this.domNode;
            	while (el.childNodes[0]) {
                    this._destroy(el.childNodes[0]);
                }
            	
            	var node;
                if (this._isString(this.template)) {
                    var domStr = this._substituteItems(this.template, this.profile);
                    node = this._toDom(domStr, el.ownerDocument);
                } else {
                    node = this.template.cloneNode(true);
                }
                el.appendChild(node);
        	} catch (error) {
        		this._displayError(error);
        	}
        },
        
        getThumbnailAlt: function() {
        	return this.profile.getName() || "";
        },
        
        // Internals
        
        _substituteItems : function(template, profile) {
            var self = this;
            return this._substitute(template, profile, function(value,key) {
                if (typeof value == "undefined") {
                    // check the self for the property
                    value = this._getObject(key, false, self);
                }

                if (typeof value == 'function') {
                    // invoke function to return the value
                    try {
                        value = value.apply(profile);
                    } catch (ex) {
                        try {
                            value = value.apply(self, [profile]);
                        } catch (ex1) {
                            value = "ERROR:" + key + " " + ex1;
                        }
                    }
                }

                if (typeof value == "undefined" || value == null) {
                    return "";
                }

                return value;
            }, this);
        },
                
        _displayError: function(error) {
        	var el = this.domNode;
            while (el.childNodes[0]) {
                this._destroy(el.childNodes[0]);
            }
           var ediv = this._create("div", {
              "class": this.errorClass,
              innerHTML: error,
              role: "alert",
              tabIndex: 0
            }, el, "only");
        },
        
    	_getTemplate: function(domId) {
            var domNode = dom.byId(domId);
            return domNode ? domNode.innerHTML : "<strong>Unable to load template: "+domId+"</strong>";
        }

    });
    
    return _ProfilePanel;
});