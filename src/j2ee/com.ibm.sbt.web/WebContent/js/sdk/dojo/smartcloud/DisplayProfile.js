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

define(['dojo/cache','dijit/_Widget','dijit/_Templated','dijit/form/_FormWidget','sbt/Endpoint','sbt/config','sbt/xml','sbt/xpath','sbt/smartcloud/core','sbt/dom','sbt/smartcloud/Subscriber'],
		function(cache,widget,templated,formWidget,_endpoint,_config,_xml,_xpath,_smartcloud,dom,Subscriber) {

dojo.declare(
	'sbt.smartcloud.profiles.DisplayProfile',
	[dijit._Widget, dijit._Templated],
	//dijit.form._FormValueWidget,
	{
		id: "",
		endpoint: "",
		templateString: dojo.cache("sbt.smartcloud.profiles", "templates/DisplayProfileSmartCloud.html"),
		
        postCreate: function() {
        	console.log("sbt.smartcloud.profiles.DisplayProfile created!");
            this.inherited(arguments);
        },
        
	/* _setValueAttr: function(value, priorityChange){
        	this.inherited(arguments);
        	if(value) {
        		dojo.style(main,"display","block");
        	} else {
        		dojo.style(main,"display","none");
        	}
        }*/
         	
        /*
         * APIs
         * 
         * getProfileByUserGUID
         * /lotuslive-shindig-server/social/rest/people/lotuslive:user:{subscriberId}/@self?format={format}
         * 
         * getProfileByUserGUID_Header
         * /lotuslive-shindig-server/social/rest/people/lotuslive:user:{subscriberId}/@self
         * 
         */
        setValue: function(value) {
			this.id = value;
			var _this = this;
			if(!value) {
				_this._setProfile(null);
			} else {
				var ep = sbt.Endpoints[this.endpoint||'smartcloud'];
				//console.log("value of ep = " + ep);
				if(ep) {
					dojo.style(this.loading,"display","block");
					var subscriberId = null;
					var subscriber = new Subscriber(ep);
					subscriber.load(function(subscriber,response){
						if(subscriber) {
							subscriberId = subscriber.getSubscriberId(response);
						}
						ep.xhrGet({
							serviceUrl:	"/lotuslive-shindig-server/social/rest/people/lotuslive:user:"+ subscriberId +"/@self",
							handleAs:	"json",
							content: {
								format:	"json"
							},
							load: function(response) {
								_this._setProfile(response);
							},
							error: function(error){
								_this._setProfile(null);
							}
						});
					});
				}
			}
		},
		_setProfile: function(doc) {
			if(doc) {
				dom.setText(this.title, doc.entry.displayName);
				dom.setText(this.email, doc.entry.emailAddress);
				var ep = sbt.Endpoints['smartcloud'];
				this.picture.setAttribute("src", ep.baseUrl +"/contacts/img/photos/" + doc.entry.photo);
				dojo.style(this.main,"display","block");
			} else {
				dojo.style(this.main,"display","none");
			}
			dojo.style(this.loading,"display","none");
		}
	}
);

return sbt.smartcloud.profiles.DisplayProfile;
});