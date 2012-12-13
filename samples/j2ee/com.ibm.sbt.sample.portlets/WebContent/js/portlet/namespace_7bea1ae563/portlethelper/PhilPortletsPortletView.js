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
dojo.provide("portlet.namespace_7bea1ae563.portlethelper.PhilPortletsPortletView");

dojo.require("portlet.namespace_7bea1ae563.Portlet");
/**
 * The PorletHelper class may have all the business logic related to JavaScript for its portlet jsp.
 */
dojo.declare("portlet.namespace_7bea1ae563.portlethelper.PhilPortletsPortletView", portlet.namespace_7bea1ae563.PortletHelper,{
	constructor : function(args){
		dojo.mixin(this, args);	
	}
	
	//create handler specific to the portlet
	//handler : function(args){
	//	 var somePotletElement = this.portlet.byId("someId");
	//}
});