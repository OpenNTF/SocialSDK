/*
 * © Copyright IBM Corp. 2013
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
 * @module sbt.Promise
 */
define(["./declare","./log"], function(declare,log) {

    /**
     * Promise class
     * 
     * @class Promise
     * @namespace sbt
     */     
	var Promise = declare(null, {	
		
        // private
        _rejected : false,
        _fulfilled : false,
        _canceled : false,
        _callbacks : null,
        _errbacks : null,
        response : null,
        error : null,
        
        /*
         * Constructor for the promise.
         */
        constructor: function(response) {
            if (response) {
                if (response instanceof Error) {
                    this.rejected(response);
                } else {
                    this.fulfilled(response);
                }
            } else {
                this._callbacks = [];
                this._errbacks = [];
            }
        },
        
        /*
         * Add new callbacks to the promise.
         */
        then: function(callback, errback) {
            if (this._fulfilled) {
                if (callback) {
                    callback(this.data);
                }
                return;
            }
            if (this._rejected) {
                if (errback) {
                    errback(this.error);
                }
                return;
            }
            
            if (callback) {
                this._callbacks.push(callback);
            }
            if (errback) {
                this._errbacks.push(errback);
            }
        },

        /*
         * Inform the deferred it may cancel its asynchronous operation.
         */
        cancel: function(reason, strict) {
            this._canceled = true;
        },

        /*
         * Checks whether the promise has been resolved.
         */
        isResolved: function() {
            return this._rejected || this._fulfilled;
        },

        /*
         * Checks whether the promise has been rejected.
         */
        isRejected: function() {
            return this._rejected;
        },

        /*
         * Checks whether the promise has been resolved or rejected.
         */
        isFulfilled: function() {
            return this._fulfilled;
        },

        /*
         * Checks whether the promise has been canceled.
         */
        isCanceled: function() {
            return this._canceled;
        },

        /*
         * Called if the promise has been fulfilled
         */
        fulfilled : function(data) {
            if (this._canceled) {
                return;
            }
            
            this._fulfilled = true;
            this.data = data;
            
            if (this._callbacks) {
                while (this._callbacks.length > 0) {
                    var callback = this._callbacks.shift();
                    try {
                        callback(data);
                    } catch (err) {
                        log.error("Callback error: "+err);
                    }
                }
            }
        },
        
        /*
         * Call if the promise has been rejected
         */
        rejected : function(error) {
            if (this._canceled) {
                return;
            }
            
            this._rejected = true;
            this.error = error;
            
            if (this._errbacks) {
                while (this._errbacks.length > 0) {
                    var errback = this._errbacks.shift();
                    try {
                        errback(error);
                    } catch (err) {
                        var msg = err.message;
                    }
                }
            }
        }
	
	});
	return Promise;
});