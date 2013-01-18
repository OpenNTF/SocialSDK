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
 * Defination of the endpoint module
 * @module sbt.Endpoint
 */

/**
 * This class encapsulate an actual endpoint, with its URL, proxy and its authentication
 * mechanism.
 * @class Endpoint
 * 
 */
define(['sbt/_bridge/declare','sbt/lang','sbt/ErrorTransport'],function(declare,lang,ErrorTransport) {


var Endpoint = declare("sbt.Endpoint", null, {
	
	/**
	 * URL of the server used to connect to the endpoint
	 * @property baseUrl
	 * @type String
	 */
	baseUrl: 		null,
	
	/**
	 * Proxy to be used
	 * @property proxy
	 * @type String 
	 */
	proxy:			null,
	
	/**
	 * Path to be added to the proxy url, if any
	 * @property proxyPath
	 * @type String 
	 */
	proxyPath:		null,
	
	/**
	 * Transport to be used
	 * @property transport
	 * @type String
	 */
	transport:		null,
	
	/**
	 * Authenticator to be used
	 * @property authenticator
	 * @type String
	 */
	authenticator:	null,
	
	/**
	 * Auth Type to be used
	 * @property authType
	 * @type String
	 */
	authType:	null,
	
	/**
	 * UI Login mode: mainWindow, dialog or popup
	 * @property loginUi
	 * @type String
	 */
	loginUi:		"",
	
	/**
	 * Page for login form for mainWindow and popup
	 * @property loginPage
	 * @type String
	 */
	loginPage:      null,
	
	/**
	 * Page for login form for dialog
	 * @property dialogLoginPage
	 * @type String
	 */
	dialogLoginPage:null,
	
	/**
	 * Simple constructor that mixes in its parameters as object properties
	 * @constructor
	 * @param {Array} args
	 */
	constructor: function(args) {
		lang.mixin(this, args || {});	
	},
	
    /**
     * @method _notifyError
     * @param error
     */
	_notifyError: function(args, error) {
        if (args.handle) {
            try {
                args.handle(error);
            } catch (ex) {
                // TODO log an error
            }
        }
        if (args.error) {
            try {
                args.error(error);
            } catch (ex) {
                // TODO log an error
            }
        }
	},
	
    /**
     * @method _notifyResponse
     * @param error
     */
    _notifyResponse: function(args, data, ioArgs) {
        if (args.handle) {
            try {
                args.handle(data, ioArgs);
            } catch (ex) {
                // TODO log an error
            }
        }
        if (args.load) {
            try {
                args.load(data, ioArgs);
            } catch (ex) {
                // TODO log an error
            }
        }
    },
    
	/**
	 * @method xhr
	 * @param method
	 * @param _args
	 * @param hasBody
	 */
	xhr: function(method,_args,hasBody) {
		var self = this;
		var args = lang.mixin({},_args);
		if(args.serviceUrl) {
			args.url = this.baseUrl+args.serviceUrl;
			delete args.serviceUrl;
		}
		if(this.proxy) {
			args.url = this.proxy.rewriteUrl(args.url,this.proxyPath);
		}
		// Make sure the initial methods are not called
		// seems that Dojo still call error(), even when handle is set
		delete args.load; delete args.error;
		args.handle = function(data,ioArgs) {
			if(data instanceof Error) {
				var error = data;
				// check for if authentication is required
				if (error.code == 401 || (self.authType == 'oauth' && error.code == 403)) {
					if(self.authenticator) {
						options = {
							dialogLoginPage:self.loginDialogPage,
							loginPage:self.loginPage,
							transport:self.transport, 
							proxy: self.proxy,
							proxyPath: self.proxyPath,
							loginUi: args.loginUi || self.loginUi,
							callback: function() {
								self.xhr(method,_args,hasBody);
							}
						};
						if(self.authenticator.authenticate(options)) {
							return;
						}
					}
				} 

                // notify handle and error callbacks is available
				self._notifyError(_args, error);
			} else {
			    // notify handle and load callbacks is available
			    self._notifyResponse(_args, data, ioArgs);
			}
		};	
		this.transport.xhr(method, args, hasBody);
	},
	
	/**
	 * @method xhrGet
	 * @param args
	 */
	xhrGet: function(args) {
		this.xhr("GET",args);
	},
	
	/**
	 * @method xhrPost
	 * @param args
	 */
	xhrPost: function(args){
		this.xhr("POST", args, true); 
	},
	
	/**
	 * @method xhrPut
	 * @param args
	 */
	xhrPut: function(args){
		this.xhr("PUT", args, true);
	},
	
	/**
	 * @method xhrDelete
	 * @param args
	 */
	xhrDelete: function(args){
		this.xhr("DELETE", args);
	}	
});

sbt.Endpoints = {}; // Initially empty

/**
 * Find the specified Endpoint and return it. If the named Endpoint is
 * not available then an Endpoint is created with an error transport and
 * this is returned.
 * 
 * @method find
 * @param name
 */
Endpoint.find = function(name){
    if (!sbt.Endpoints[name]){
        console.log("Unable to find endpoint named %s, creating it now with an error transport.", name);
        var transport = new ErrorTransport(name);
        sbt.Endpoints[name] = new Endpoint({
            "transport" : transport,
            "baseUrl"   : ""
        });
    }
    return sbt.Endpoints[name];
};

return Endpoint;
});
