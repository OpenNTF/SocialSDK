/*
 * © Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK. 
 * Sample dojo component that 
 */
define(['dojo/cache','dijit/_Widget','dijit/_Templated','dijit/form/_FormWidget','sbt/Endpoint','sbt/config','sbt/xml','sbt/xpath','sbt/connections/core','sbt/dom'],
		function(cache,widget,templated,formWidget,_endpoint,_config,xml,xpath,_connections,dom) {

return dojo.declare(
	'sbt.dojo.connections.DisplayProfile',
	[dijit._Widget, dijit._Templated],
	//dijit.form._FormValueWidget,
	{
		id: "",
		endpoint: "",
		templateString: dojo.cache("sbt.dojo.connections", "templates/DisplayProfile.html"),
		
        postCreate: function() {
        	console.log("DisplayProfile created!");
            this.inherited(arguments);
            //this.setValue(this.id);
        },
        
	/*	
        _setValueAttr: function(value, priorityChange){
        	this.inherited(arguments);
        	if(value) {
        		dojo.style(main,"display","block");
        	} else {
        		dojo.style(main,"display","none");
        	}
        }
*/         	
        setValue: function(value) {
        	//console.log("SetValue: "+value);
			this.id = value;
			var _this = this;
			var content = {};
			if(this._isEmail(value)) {
				content.email = value; 
			} else {
				content.userid = value; 
			}
			if(!value) {
				_this._setProfile(null);
			} else {
				var ep = sbt.Endpoints[this.endpoint||'connections'];
				if(ep) {
					dojo.style(this.loading,"display","block");
					ep.xhrGet({
						serviceUrl:	"/profiles/atom/profile.do",
						handleAs:	"text",
						content: content,
						load: function(response) {
							_this._setProfile(response);
						},
						error: function(error){
							_this._setProfile(null);
						}
					});
				}
			}
		},
			
		_setProfile: function(doc) {
			if(doc) {
				var x = xml.parse(doc);
				dom.setText(this.title,xpath.selectText(x,"/a:feed/a:entry/a:contributor/a:name",sbt.connections.namespaces));
				dom.setText(this.email,xpath.selectText(x,"/a:feed/a:entry/a:contributor/a:email",sbt.connections.namespaces));
				this.picture.setAttribute("src",xpath.selectText(x,"/a:feed/a:entry/a:content/h:div/h:span/h:div/h:img[@class='photo']/@src",sbt.connections.namespaces));
				dojo.style(this.main,"display","block");
			} else {
				dojo.style(this.main,"display","none");
			}
			dojo.style(this.loading,"display","none");
		},
		
		_isEmail: function(id) {
			return id && id.indexOf('@')>=0;
		},
	}
);
});