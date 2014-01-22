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
 * Implementation of a transport that emits an error the first time it is invoked.
 * @module sbt.ErrorTransport
 */
define(['./declare','./lang','./Promise','./stringUtil','./log','sbt/i18n!sbt/nls/ErrorTransport'], function(declare,lang,Promise,stringUtil,log,nls) {
    return declare(null, {
        _called: false,
        _endpointName: null,
        _message: null,
        
        constructor: function(endpointName, message) {
            this._endpointName = endpointName;
            if (message) {
                this._message = message;
            } else {
            	this._message = stringUtil.substitute(nls.endpoint_not_available, [endpointName]);
            }
        },
        
        request : function(url,options) {
            if (!this._called) {
                alert(this._message);
                this._called = true;
            }
            var promise = new Promise();
            var error = new Error(this._message);
            error.status = 400;
            promise.rejected(error);
            return promise;
        },
        
        xhr: function(method, args, hasBody) {
            if (!this._called) {
                log.error(this._message);
                this._called = true;
            }
            var _handle = args.handle;
            var _error = args.error;
            if (lang.isFunction(_error) || lang.isFunction(_handle)) {
                var error = new Error(this._message);
                error.status = 400;
                if(lang.isFunction(_error)){
                	_error(error);
                }
                if(lang.isFunction(_handle)){
                	_handle(error);
                }                
            }
        }
    });
});