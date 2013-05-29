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
 * 
 * @author Vimal Dhupar 
 * Definition of a Subscriber Helper for getting the Subscriber ID for a SmartCloud User.
 */
define([ "../declare", "../Endpoint" ], function(declare, Endpoint) {
	/**
	 * Subscriber Helper Class for getting the Subscriber ID for a SmartCloud User.
	 * @class Subscriber
	 * @namespace sbt.smartcloud
	 */
	var Subscriber = declare(null, {
		
		endpoint : null,
		
		/**
		 * @constructor
		 * @param endpoint
		 * @param callback
		 */
		constructor : function(endpoint, callback) {
			this.endpoint = endpoint;
			if (callback)
				this.load(callback);
		},
		
		/**
		 * Load method is responsible for making the network call to fetch the user identity
		 * @method load
		 * @param callback
		 */
		load : function(callback) {
			var _self = this;
			this.endpoint.xhrGet({
				serviceUrl : "/manage/oauth/getUserIdentity",
				loginUi : this.endpoint.loginUi,
				handleAs : "json",
				load : function(response) {
					callback(_self, response);
				},
				error : function(error) {
					callback(_self, null);
					console.log("Error fetching feed for getUserIdentity");
				}
			});
		},
		
		/**
		 * Method to get the Subscriber Id of the user.
		 * @method getSubscriberId
		 * @param response
		 * @returns
		 */
		getSubscriberId : function(response) {
			if (response && response.subscriberid) {
				return response.subscriberid;
			}
			return null;
		}
	});
	return Subscriber;
});