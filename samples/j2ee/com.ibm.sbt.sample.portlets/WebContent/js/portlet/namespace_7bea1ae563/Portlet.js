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
/****************************************************************************************************************************************************
 * Portlet.js defines two classes- Portlet and PortletHelper.
 * The Portlet class is used to create a global object for the portlet. It assists in handling namespaced objects and variables present in the portlet jsp file.
 * These classes define bare minimal functionality along with some commonly used variable fields.
 * This class will be added in all the portlet jsps using Dojo toolkit.
 * You may add or remove some functionality in this class if you want to use them in all the portlets,
 * or create a child classes of PortletHelper class in another js file that can be included in particular portlet jsp file.
 *****************************************************************************************************************************************************/
dojo.provide("portlet.namespace_7bea1ae563.Portlet");
dojo.declare("portlet.namespace_7bea1ae563.Portlet", null,{
	namespace   : null, // namespace of the portlet in portal environment
	contextPath : null, // the context path which is the path prefix associated with the deployed portlet application
	actionUrl   : null, // action url of the portlet
	renderUrl   : null, // render url of the portlet
	remoteUser  : null, // the login of the user making this request, if the user has been authenticated, or null if the user has not been authenticated.
	portletMode : null, // the current portlet mode of the portlet
	windowState : null, // the current window state of the portlet
	scheme      : null, // the name of the scheme used to make the request like http or https
	
	/**
	 * Constructor sets all variables passed in the format like
	 * var portlet_<portlet:namespace/> = new Portlet({namespace: <portlet:namespace/> , contextPath: renderResponse.encodeURL(renderRequest.getContextPath())});
	 * will set the namespace and contextPath in the created object.
	 */
	constructor: function(args){
		dojo.mixin(this, args);	
	},
	
	/**
	 * returns the reference to the DOM element present in the current portlet
	 * which has id in one of the following format
	 * element_<portlet:namespace/>
	 * element<portlet:namespace/>
	 * element
	 * the first match found is returned.
	 * The id may itself be the complete name, for example: element_<portlet:namespace>, if used from the portlet jsp file.
	 * @param id
	 * @return 
	 */
	byId: function(/*String*/ id){
		var result = dojo.byId(id + "_" + this.namespace);
		if(result == null)
			result = dojo.byId(id + this.namespace);
		if(result == null)
			result = dojo.byId(id);
		return result;
	},
	
	/**
	 * returns the reference to the dijit widget instance present in the current portlet
	 * which has the id in one of the following format
	 * element_<portlet:namespace/>
	 * element<portlet:namespace/>
	 * element
	 * the first match found is returned.
	 * The id may itself be the complete name, for example:element_<portlet:namespace>, if used from the portlet jsp file.
	 * @param id
	 * @return
	 */
	byDijitId: function(/*String*/ id){
		var result = dijit.byId(id + "_" + this.namespace);
		if(result == null)
			result = dijit.byId(id + this.namespace);
		if(result == null)
			result = dijit.byId(id);
		return result;
	},
	
	/**
	 * returns the reference to the object instance present in the current portlet 
	 * which has the id in one of the following format
	 * object_<portlet:namespace/>
	 * object<portlet:namespace/>
	 * object
	 * the first match found is returned.
	 * The id may itself be the complete name, for example: element_<portlet:namespace>, if used from portlet jsp
	 * @param id
	 * @return
	 */
	getObject: function(/*String*/ id){
		var result = dojo.getObject(id + "_" + this.namespace);
		if(result == null)
			result = dojo.getObject(id + this.namespace);
		if(result == null)
			result = dojo.getObject(id);
		return result;
	},
	
	/**
	 * returns name appended with '_<portlet:namespace/>'
	 * @param name
	 * @return
	 */
	getFullName: function(/*String*/ name){
		return name + "_" + this.namespace;
	}
});



/********************************************************************************************
 * PortletHelper class is used to define the functionality specific to a particular portlet.
 * The functionality common to all the portlets may be added to the Portlet class.
 * The Portlet.js file will be included in all the portlets using Dojo technology.
 ********************************************************************************************/
dojo.declare("portlet.namespace_7bea1ae563.PortletHelper", null,{
	//used to hold portlet_<portlet:namespace/> global JavaScript variable created earlier 
	portlet: null,
	/**
	 * Constructor will set all the parameters to the object, generally the args will be
	 * the Portlet object created earlier.
	 */
	constructor: function(args){
		dojo.mixin(this, args);	
	}
});
