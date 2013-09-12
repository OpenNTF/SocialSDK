/*
 * � Copyright IBM Corp. 2010
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
dojo.provide("extlib.dijit.ProfilesVCardInline");

dojo.require("dojo.cache")
dojo.require("dijit._Widget")
dojo.require("dijit._Templated")

dojo.declare(
     'extlib.dijit.ProfilesVCardInline',
     [dijit._Widget, dijit._Templated],
     {
          userId: "",
          userName: "",
  		  templateString: dojo.cache("extlib.dijit","templates/ProfilesVCardInline.html"),

          postCreate: function() {
        	  dojo.attr(this.idNode,"class",this.userId && this.userId.indexOf("@")>=0 ? "email" : "x-lconn-userid");
              this.inherited(arguments);
              dojo.addOnLoad(dojo.hitch(this,function(){ SemTagSvc.parseDom(null, this.id, 0 );}));
              //setTimeout("SemTagSvc.parseDom(null, '"+this.id+"')", 0 );
          }
     }
);
