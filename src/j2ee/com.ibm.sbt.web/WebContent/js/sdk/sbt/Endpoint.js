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
 * Endpoint which defines a connection to a back-end server.
 * 
 * @module sbt.Endpoint
 */
define(['./declare','./lang','./ErrorTransport','./Promise','./pathUtil','./compat','./log', './stringUtil', 'sbt/i18n!sbt/nls/Endpoint', './xml'],
function(declare,lang,ErrorTransport,Promise,pathUtil,compat,log,stringUtil,nls,xml) {

/**
 * This class encapsulates an actual endpoint, with its URL, proxy and its authentication mechanism.
 * 
 * @class sbt.Endpoint
 */
var Endpoint = declare(null, {
	
	/**
	 * URL of the server used to connect to the endpoint
	 * @property baseUrl
	 * @type String
	 */
	baseUrl: null,
	
	/**
	 * Proxy to be used
	 * @property proxy
	 * @type String 
	 */
	proxy: null,
	
	/**
	 * Path to be added to the proxy url, if any
	 * @property proxyPath
	 * @type String 
	 */
	proxyPath: null,
	
	/**
	 * Transport to be used
	 * @property transport
	 * @type String
	 */
	transport: null,
	
	/**
	 * Authenticator to be used
	 * @property authenticator
	 * @type String
	 */
	authenticator: null,
	
	/**
	 * Auth Type to be used
	 * @property authType
	 * @type String
	 */
	authType: null,
	
	/**
	 * UI Login mode: mainWindow, dialog or popup
	 * @property loginUi
	 * @type String
	 */
	loginUi: "",
	
	/**
	 * Page for login form for mainWindow and popup
	 * @property loginPage
	 * @type String
	 */
	loginPage: null,
	
	/**
	 * Page for login form for dialog
	 * @property dialogLoginPage
	 * @type String
	 */
	dialogLoginPage: null,
	
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
     * Provides an asynchronous request using the associated Transport.
     * 
     * @method request
     * @param {String)
     *            url The URL the request should be made to.
     * @param {String)
     *            loginUi The type of UI to use when authenticating,
     *            valid values are: mainWindow, popup, dialog.
     * @param {Boolean)
     *            authAuthenticate if true the Endpoint with authenticate
     *            when a 401 (or associated authenication code) is received.
     * @param {Object}
     *            [options] Optional A hash of any options for the provider.
     * @param {String|Object}
     *            [options.data=null] Data, if any, that should be sent with
     *            the request.
     * @param {String|Object}
     *            [options.query=null] The query string, if any, that should
     *            be sent with the request.
     * @param {Object}
     *            [options.headers=null] The headers, if any, that should
     *            be sent with the request.
     * @param {Boolean}
     *            [options.preventCache=false] If true will send an extra
     *            query parameter to ensure the the server won’t supply
     *            cached values.
     * @param {String}
     *            [options.method=GET] The HTTP method that should be used
     *            to send the request.
     * @param {Integer}
     *            [options.timeout=null] The number of milliseconds to wait
     *            for the response. If this time passes the request is
     *            canceled and the promise rejected.
     * @param {String}
     *            [options.handleAs=text] The content handler to process the
     *            response payload with.
     * @return {sbt.Promise}
     */
    request : function(url, options) {
        // rewrite the url if needed
        var qurl = url;
        if (qurl.indexOf("http") != 0) {
            if (this.proxy) {
                qurl = this.proxy.rewriteUrl(this.baseUrl, url, this.proxyPath);
            } else {
                qurl = pathUtil.concat(this.baseUrl, url);
            }
        }
        
        if (!options) {
        	options = { 
        	   	method : "GET", 
        	   	handleAs : "text"
        	};
        }
        
        var promise = new Promise();
        promise.response = new Promise();       

		var self = this;
		this.transport.request(qurl, options).response.then(function(response) {
			
			// Check for form based authentication
			if(self.authType == "form"){
				var authRequiredFlag = self._isAuthRequiredFormBasedEP(response, options);
					if(authRequiredFlag){
						self._authenticate(url, options, promise);
					}
			}
			
			promise.fulfilled(response.data);
			promise.response.fulfilled(response);
		}, function(error) {
			if (!error.message) {
				error.message = self.getErrorMessage(error.cause);
			}
			var authRequiredPromise = self._isAuthRequired(error, options);
			authRequiredPromise.then(
				function(response) {
					if (response) {
						self._authenticate(url, options, promise);
					} else {
						promise.rejected(error);
						promise.response.rejected(error);
					}
				}, 
				function(error) {
					promise.rejected(error);
					promise.response.rejected(error);
				}
			);
		});
        
        return promise;
    },
	
	/*
	 * Sends a request using XMLHttpRequest with the given URL and options.
	 * 
	 * @method xhr 
	 * @param {String} [method] The HTTP method to use to make the request. Must be uppercase. Default is 'GET'. 
	 * @param {Object} [args]
	 * 		@param {String} [args.url] 
	 * 		@param {Function} [args.handle] 
	 * 		@param {Function} [args.load] 
	 *  	@param {Function} [args.error] 
	 *  	@param {Boolean} [hasBody]
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
				if(!error.message){
					error.message = self.getErrorMessage(error.cause);
				} 
				var isForbiddenErrorButAuthenticated = false;
				// check for if authentication is required
				if(error.code == 403 && self.authenticationErrorCode == 403){ 
					// case where 403 is configured to be treated similar to 401 (unAuthorized)
		        	// checking if we are getting 403 inspite of being already authenticated (eg. Get Public Files/Folders API on Smartcloud
		        	if(self.isAuthenticated){
		        		isForbiddenErrorButAuthenticated = true;
		        	}
		        }
				if ((error.code == 401)|| (!isForbiddenErrorButAuthenticated && error.code == self.authenticationErrorCode)) {
					var autoAuthenticate =  _args.autoAuthenticate || self.autoAuthenticate;
					if(autoAuthenticate == undefined){
						autoAuthenticate = true;
					}
					if(autoAuthenticate){
						if(self.authenticator) {
							options = {
								dialogLoginPage:self.loginDialogPage,
								loginPage:self.loginPage,
								transport:self.transport, 
								proxy: self.proxy,
								proxyPath: self.proxyPath,
								loginUi: _args.loginUi || self.loginUi,
								name: self.name,
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
				self._notifyError(args, error, ioArgs);
			} else {
			    // notify handle and load callbacks is available
			    self._notifyResponse(args, data, ioArgs);
			}
		};	
		this.transport.xhr(method, _args, hasBody);
	},
	
	/*
	 * @method xhrGet
	 * @param args
	 */
	xhrGet: function(args) {
		this.xhr("GET",args);
	},
	
	/*
	 * @method xhrPost
	 * @param args
	 */
	xhrPost: function(args){
		this.xhr("POST", args, true); 
	},
	
	/*
	 * @method xhrPut
	 * @param args
	 */
	xhrPut: function(args){
		this.xhr("PUT", args, true);
	},
	
	/*
	 * @method xhrDelete
	 * @param args
	 */
	xhrDelete: function(args){
		this.xhr("DELETE", args);
	},
	
	/**
	 * authenticate to an endpoint
	 *
	 * @method authenticate
	 * @param {Object} [args]  Argument object
	 *		@param {boolean} [args.forceAuthentication] Whether authentication is to be forced in case user is already authenticated.
	 *		@param {String} [args.loginUi] LoginUi to be used for authentication. possible values are: 'popup', 'dialog' and 'mainWindow'
	 *		@param {String} [args.loginPage] login page to be used for authentication. this property should be used in case default
	 *		login page is to be overridden. This only applies to 'popup' and 'mainWindow' loginUi
	 *		@param {String} [args.dialogLoginPage] dialog login page to be used for authentication. this property should be used in
	 *		case default dialog login page is to be overridden. This only applies to 'dialog' loginUi.
	 */
	authenticate : function(args) {
		var promise = new Promise();
		args = args || {};
		if (args.forceAuthentication || !this.isAuthenticated) {
			var options = {
				dialogLoginPage : this.loginDialogPage,
				loginPage : this.loginPage,
				transport : this.transport,
				proxy : this.proxy,
				proxyPath : this.proxyPath,
				loginUi : args.loginUi || this.loginUi,
				name: this.name,
				callback: function(response) {
					promise.fulfilled(response);
				},
				cancel: function(response) {
					promise.rejected(response);
				}
			};
			this.authenticator.authenticate(options);
		} else {
			promise.fulfilled(true);
		}
		return promise;
	},
	
	/**
	 * Logout from an endpoint
	 *
	 * @method logout
	 * @param {Object} [args]  Argument object
	 */
	logout : function(args) {
		var promise = new Promise();
		args = args || {};
		var self = this;
		var proxy = this.proxy.proxyUrl;
		var actionURL = proxy.substring(0, proxy.lastIndexOf("/")) + "/authHandler/" + this.proxyPath + "/logout";
		this.transport.xhr('POST',{
			handleAs : "json",
			url : actionURL,
			load : function(response) {
				self.isAuthenticated = false;
				promise.fulfilled(response);
			},
			error : function(response) {
				self.isAuthenticated = false;
				promise.rejected(response);
			}
		}, true);		
		return promise;
	},
	
	/**
	 * Find whether endpoint is authenticated or not.
	 *
	 * @method isAuthenticated
	 * @param {Object} [args]  Argument object
	 */
	isAuthenticated : function(args) {
		var promise = new Promise();
		args = args || {};
		var self = this;
		var proxy = this.proxy.proxyUrl;
		var actionURL = proxy.substring(0, proxy.lastIndexOf("/")) + "/authHandler/" + this.proxyPath + "/isAuth";
		this.transport.xhr('POST',{
			handleAs : "json",
			url : actionURL,
			load : function(response) {
				self.isAuthenticated = true;
				promise.fulfilled(response);
			},
			error : function(response) {
				promise.rejected(response);
			}
		}, true);		
		return promise;
	},
	
	/**
	 Find whether endpoint authentication is valid or not.
	 
	 @method isAuthenticationValid
	 @param {Object} [args]  Argument object
			@param {Function} [args.load] This is the function which isAuthenticationValid invokes when 
			authentication information is retrieved.
			@param {Function} [args.error] This is the function which isAuthenticationValid invokes if an error occurs.
			result property in response object returns true/false depending on whether authentication is valid or not.
	*/
	isAuthenticationValid : function(args) {
		var promise = new Promise();
		args = args || {};
		var self = this;
		var proxy = this.proxy.proxyUrl;
		var actionURL = proxy.substring(0, proxy.lastIndexOf("/")) + "/authHandler/" + this.proxyPath + "/isAuthValid";
		this.transport.xhr('POST',{			
			handleAs : "json",
			url : actionURL,
			load : function(response) {				
				self.isAuthenticated = response.result;
				promise.fulfilled(response);
			},
			error : function(response) {
				promise.rejected(response);
			}
		}, true);
		return promise;
	},
	
	// Internal stuff goes here and should not be documented
	
    /*
     * Invoke error function with the error
     */
    _notifyError: function(args, error, ioArgs) {
        if (args.handle) {
            try {
                args.handle(error, ioArgs);
            } catch (ex) {
                // TODO log an error
                var msg = ex.message;
            }
        }
        if (args.error) {
            try {
                args.error(error, ioArgs);
            } catch (ex) {
                // TODO log an error
                var msg = ex.message;
            }
        }
    },
    
    /*
     * Invoke handle and/or load function with the response
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

    /*
     * Invoke automatic authentication for the specified request.
     */
    _authenticate: function(url, options, promise) {
        var self = this;
        var authOptions = {
            dialogLoginPage: this.loginDialogPage,
            loginPage: this.loginPage,
            transport: this.transport, 
            proxy: this.proxy,
            proxyPath: this.proxyPath,
            loginUi: options.loginUi || this.loginUi,
            name: this.name,
            callback: function() {
                self.request(url, options).response.then(
                    function(response) {
                        promise.fulfilled(response.data);
                        promise.response.fulfilled(response);
                    }, function(error) {
                        promise.rejected(error);
                        promise.response.rejected(error);
                    }
                );
            },
            cancel: function() {
                self._authRejected = true;
                var error = new Error();
                error.message = "Authentication is required and has failed or has not yet been provided.";
                error.code = 401;
                promise.rejected(error);
                promise.response.rejected(error);
            }
        };
        
        return this.authenticator.authenticate(authOptions);
    },

    /*
     * Return true if automatic authentication is required. This method returns a promise with the success callback returning
	 * a boolean whether authentication is required. It first checks if the client is already authenticated
	 * and if yes, whether the authentication is valid. Else, it checks for the status code and other
	 * configuration paramters to decide if authentication is required.
     */
 	_isAuthRequired : function(error, options) {		
		var promise = new Promise();
		var status = error.response.status || null;
		var isAuthErr = status == 401 || status == this.authenticationErrorCode;
		if (this.isAuthenticated) {
			if (!isAuthErr) {
				promise.fulfilled(false);
			} else {
				this.isAuthenticationValid().then(
					function(response) {
						promise.fulfilled(!response.result);
					}, 
					function(response) {
						promise.rejected(response);
					}
				);
			}
		} else {
			// User can mention autoAuthenticate as part service wrappers call that is the args json variable or
			// as a property of endpoint in managed-beans.xml. 
			// isAutoAuth variable is true when autoAuthenticate property is true in args json variable or 
			// autoAuthenticate property in endpoint defination in managed-beans.xml is true. It is false otherwise.
			var isAutoAuth = options.autoAuthenticate || this.autoAuthenticate;
			if (isAutoAuth == undefined) {
				isAutoAuth = true;
			}
			// The response is returned as a boolean value as an argument to the success callback of the promise. This 
			// value is true when the error code is 401 or any authentication error code for a particular endpoint
			// (isAuthErr variable) and autoAuthenticate parameter is mentioned true (based on isAutoAuth variable)
			// and authenticator property the endpoint (could be js object of type Basic or OAuth)is defined and the 
			// authentication was not rejected earlier. 
			// It is false otherwise. The true value of this expression triggers the authentication process from the client.
			promise.fulfilled(isAuthErr && isAutoAuth && this.authenticator && !this._authRejected);
		}
		return promise;
	},
	
	/*
	 * Method ensures we trigger authentication for Smartcloud when response code is 200 and content is login page
	 */
	_isAuthRequiredFormBasedEP : function (response, options){
		if(response.status == 200 && response.getHeader("Content-Type") == "text/html"){
			return true;
		}else{
			return false;
		}
	},
	
    getErrorMessage: function(error) {    	
        var text = error.responseText || (error.response&&error.response.text) ;
        if (text) {
            try {            	
                var dom = xml.parse(text);
                var messages = dom.getElementsByTagName("message");
                if (messages && messages.length != 0) {                	
                    text = messages[0].text || messages[0].textContent;                	
                    text = lang.trim(text);
                }
            } catch(ex) {                
            }  
            var trimmedText = text.replace(/(\r\n|\n|\r)/g,"");
            if(!(trimmedText)){            	
            	return error.message;
            }else{
            	return text;
            }
        } else {
            return error;
        }
    }
	
});

return Endpoint;
});
