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
 */
define(['sbt/_bridge/declare','sbt/lang','sbt/Promise'], function(declare,lang,Promise) {
    return declare("sbt.ErrorTransport", null, {
        _called: false,
        _endpointName: null,
        _message: null,
        
        constructor: function(endpointName, message) {
            this._endpointName = endpointName;
            if (message) {
                this._message = message;
            } else {
                this._message = "Required endpoint is not available: " + endpointName;
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
                alert(this._message);
                this._called = true;
            }
            var _handle = args.handle;
            if (lang.isFunction(_handle)) {
                var error = new Error(this._message);
                error.status = 400;
                _handle(error);
            }
        }
    });
});