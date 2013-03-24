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
 * Definition of the endpoint module
 * @module sbt.Endpoint
 */

/**
 * This class encapsulates an actual endpoint, with its URL, proxy and its authentication
 * mechanism.
 * @class Endpoint
 * 
 */
define(['sbt/_bridge/declare','sbt/lang','sbt/ErrorTransport','sbt/pathUtil'],function(declare,lang,ErrorTransport,pathUtil) {


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
	 * Whether auth dialog should come up automatically or not. In case of not 401 would be propagated to user.
	 * @property autoAuthenticate
	 * @type String
	 */
	autoAuthenticate: null,
	
	/**
	 * Whether user is authenticated to endpoint or not.
	 * @property isAuthenticated
	 * @type String
	 */
	isAuthenticated: false,
	
	/**
	 * The error code that is returned from the endpoint on authentication failure.
	 * @property authenticationErrorCode
	 * @type String
	 */
	authenticationErrorCode: null,
	
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
                var msg = ex.message;
            }
        }
        if (args.error) {
            try {
                args.error(error);
            } catch (ex) {
                // TODO log an error
                var msg = ex.message;
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
                var msg = ex.message;
            }
        }
        if (args.load) {
            try {
                args.load(data, ioArgs);
            } catch (ex) {
                // TODO log an error
                var msg = ex.message;
            }
        }
    },
    
	/**
	 * Sends a request using XMLHttpRequest with the given URL and options.
	 * 
	 * @method xhr
	 * @param {String} [method] The HTTP method to use to make the request. Must be uppercase. Default is 'GET'.
	 * @param {Object} [args]
     *     @param {String} [args.url]
     *     @param {Function} [args.handle]
     *     @param {Function} [args.load]
     *     @param {Function} [args.error]
	 * @param {Boolean} [hasBody]
	 */
	xhr: function(method,args,hasBody) {
		var self = this;
		var _args = lang.mixin({},args);
		// We make sure that args has a 'url' member, with or without a proxy 
		if(!_args.url) {
			if(this.proxy) {
				_args.url = this.proxy.rewriteUrl(this.baseUrl,_args.serviceUrl,this.proxyPath);
			} else {
				_args.url = pathUtil.concat(this.baseUrl,_args.serviceUrl);
			}
		}
		// Make sure the initial methods are not called
		// seems that Dojo still call error(), even when handle is set
		delete _args.load; delete _args.error;
		_args.handle = function(data,ioArgs) {
			if(data instanceof Error) {
				var error = data;
				// check for if authentication is required				
				if (error.code == 401 || error.code == self.authenticationErrorCode) {
					var autoAuthenticate =  _args.autoAuthenticate || self.autoAuthenticate || sbt.Properties["autoAuthenticate"] || "true";
					if(autoAuthenticate == "true"){
						if(self.authenticator) {
							options = {
								dialogLoginPage:self.loginDialogPage,
								loginPage:self.loginPage,
								transport:self.transport, 
								proxy: self.proxy,
								proxyPath: self.proxyPath,
								loginUi: _args.loginUi || self.loginUi,
								callback: function() {
									self.xhr(method,args,hasBody);
								}
							};
							if(self.authenticator.authenticate(options)) {
								return;
							}
						}
					}
				} 

                // notify handle and error callbacks is available
				self._notifyError(args, error);
			} else {
			    // notify handle and load callbacks is available
			    self._notifyResponse(args, data, ioArgs);
			}
		};	
		this.transport.xhr(method, _args, hasBody);
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
	},
	
	/**
	 authenticate to an endpoint
	 
	 @method authenticate
	 @param {Object} [args]  Argument object
			@param {boolean} [args.forceAuthentication] Whether authentication is to be forced in case user is already authenticated.
			@param {String} [args.loginUi] LoginUi to be used for authentication. possible values are: 'popup', 'dialog' and 'mainWindow'
			@param {String} [args.loginPage] login page to be used for authentication. this property should be used in case default
			login page is to be overridden. This only applies to 'popup' and 'mainWindow' loginUi
			@param {String} [args.dialogLoginPage] dialog login page to be used for authentication. this property should be used in
			case default dialog login page is to be overridden. This only applies to 'dialog' loginUi.
			@param {Function} [args.success] This is the function which authenticate invokes when the authentication is successful.
			@param {Function} [args.cancel] This is the function which authenticate invokes when cancel button of authenticator is clicked.
	 */
	authenticate : function(args) {
		args = args || {};
		var options = {
			dialogLoginPage : this.loginDialogPage,
			loginPage : this.loginPage,
			transport : this.transport,
			proxy : this.proxy,
			proxyPath : this.proxyPath,
			loginUi : args.loginUi || this.loginUi,
			callback: args.success,
			cancel: args.cancel
		};
		if(args.forceAuthentication || !this.isAuthenticated) {
			this.authenticator.authenticate(options);
		}else{//call success if authentication is not required.
			if (args.success) {
				args.success();
			}
		}
	},
	
	/**
	 logout from an endpoint
	 
	 @method logout
	 @param {Object} [args]  Argument object
			@param {Function} [args.success] This is the function which authenticate invokes when the logout is successful.
			@param {Function} [args.failure] This is the function which authenticate invokes when the logout is unsuccessful.
	 */
	logout : function(args) {
		args = args || {};
		var self = this;
		var proxy = this.proxy.proxyUrl;
		var actionURL = proxy.substring(0, proxy.lastIndexOf("/")) + "/authHandler/" + this.proxyPath + "/logout";
		this.transport.xhr('POST',{
			handleAs : "json",
			url : actionURL,
			handle : function(response) {
				sbt.Endpoints[self.proxyPath].isAuthenticated = false;
				if (args.success && response.success) {
					args.success(response);
				} else if (args.failure && !response.success) {
					args.failure(response);
				}
			}
		}, true);
	},
	
	/**
	 Find whether endpoint is authenticated or not.
	 
	 @method isAuthenticated
	 @param {Object} [args]  Argument object
			@param {Function} [args.load] This is the function which isAuthenticated invokes when authentication information is retrieved.
			result property in response object returns true/false depending on whether endpoint is authenticated or not.
	*/
	isAuthenticated : function(args) {
		args = args || {};
		var proxy = this.proxy.proxyUrl;
		var actionURL = proxy.substring(0, proxy.lastIndexOf("/")) + "/authHandler/" + this.proxyPath + "/isAuth";
		this.transport.xhr('POST',{
			handleAs : "json",
			url : actionURL,
			handle : function(response) {
				if (args.load) {
					args.load(response);
				}
			}
		}, true);
	},
	
	/**
	 Find whether endpoint authentication is valid or not.
	 
	 @method isAuthenticationValid
	 @param {Object} [args]  Argument object
			@param {Function} [args.load] This is the function which isAuthenticationValid invokes when 
			authentication information is retrieved.
			result property in response object returns true/false depending on whether authentication is valid or not.
	*/
	isAuthenticationValid : function(args) {
		args = args || {};
		var proxy = this.proxy.proxyUrl;
		var actionURL = proxy.substring(0, proxy.lastIndexOf("/")) + "/authHandler/" + this.proxyPath + "/isAuthValid";
		this.transport.xhr('POST',{
			handleAs : "json",
			url : actionURL,
			handle : function(response) {
				if (args.load) {
					args.load(response);
				}
			}
		}, true);
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
