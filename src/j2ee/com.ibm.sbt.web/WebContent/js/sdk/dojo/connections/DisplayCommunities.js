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
 * Sample dojo component that displays a list of communities. 
 */
define(["sbt/xml","sbt/xpath"],function(xmlsbt, xmlxpath) {

dojo.require("sbt.connections.core");

dojo.require("dojo.cache");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dijit.form._FormWidget");

dojo.declare(
	'sbt.connections.communities.DisplayCommunities',
	[dijit._Widget, dijit._Templated],
	{
		id: "",
		templateString: dojo.cache("sbt.dojo.connections", "templates/DisplayCommunities.html"),
		
        postCreate: function() {
        	console.log("sbt.dojo.connections.DisplayCommunities created!");
            this.inherited(arguments);
        },
        
        setValue: function(value) {
        	console.log("SetValue: "+value);
			this.id = value;
			if(!value) {
				_this._setProfile(null);
			} else {
				var ep = sbt.Endpoints['connections'];
				if(ep) {
					var url = null;
					var content = {};
					if(value=="all") {
						url = "/communities/service/atom/communities/all";
					} else if(value=="my") {
						url = "/communities/service/atom/communities/my";
					} else {
						url = "/communities/service/atom/communities/my";
						content.email = value;
					}
					var _this = this;
					dojo.style(this.loading,"display","block");
					ep.xhrGet({
						serviceUrl:	url,
						handleAs:	"text",
						content: content,
						load: function(response) {
							_this._setCommunities(response);
						},
						error: function(error){
							_this._setCommunities(null);
						}
					});
				}
			}
		},
			
		_setCommunities: function(doc) {
			dojo.empty(this.main);
			if(doc) {
				var xml = xmlsbt.parse(doc);
				var nodes = xmlxpath.selectNodes(xml,"/a:feed/a:entry",sbt.connections.namespaces);
				if(nodes) {
					var l = nodes.length;
					for(var i=0; i<l; i++) {
						var node = nodes[i];
						var div = dojo.create("div");
						this._setText(div,xmlxpath.selectText(node,"a:title",sbt.connections.namespaces));
						this.main.appendChild(div); 		
					}
				}
				
		      	//pre.appendChild(dojo.doc.createTextNode(doc));
			}
			dojo.style(this.main,"display","block");
			dojo.style(this.loading,"display","none");
		},
		
		_setText: function(node,value) {
			while(node.firstChild) node.removeChild(node.firstChild);
			node.appendChild(dojo.doc.createTextNode(value)); 		
		}
	}
);

});