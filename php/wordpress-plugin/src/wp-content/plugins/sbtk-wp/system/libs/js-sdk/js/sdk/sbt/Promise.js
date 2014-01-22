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
        _isRejected : false,
        _isFulfilled : false,
        _isCanceled : false,
        _deferreds : null,
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
                this._deferreds = [];
            }
        },
        
        /*
         * Add new callbacks to the promise.
         */
        then: function(fulfilledHandler, errorHandler) {
        	var promise = new Promise();
            if (this._isFulfilled) {
            	this._fulfilled(fulfilledHandler, errorHandler, promise, this.data);
            } else if (this._isRejected) {
            	this._rejected(errorHandler, promise, this.error);
            } else {
                this._deferreds.push([ fulfilledHandler, errorHandler, promise ]);
            }
            return promise;
        },

        /*
         * Inform the deferred it may cancel its asynchronous operation.
         */
        cancel: function(reason, strict) {
            this._isCanceled = true;
        },

        /*
         * Checks whether the promise has been resolved.
         */
        isResolved: function() {
            return this._isRejected || this._isFulfilled;
        },

        /*
         * Checks whether the promise has been rejected.
         */
        isRejected: function() {
            return this._isRejected;
        },

        /*
         * Checks whether the promise has been resolved or rejected.
         */
        isFulfilled: function() {
            return this._isFulfilled;
        },

        /*
         * Checks whether the promise has been canceled.
         */
        isCanceled: function() {
            return this._isCanceled;
        },

        /*
         * Called if the promise has been fulfilled
         */
        fulfilled : function(data) {
            if (this._isCanceled) {
                return;
            }
            
            this._isFulfilled = true;
            this.data = data;
            
            if (this._deferreds) {
                while (this._deferreds.length > 0) {
                    var deferred = this._deferreds.shift();
                    var fulfilledHandler = deferred[0];
                    var errorHandler = deferred[1];
                    var promise = deferred[2];
                	this._fulfilled(fulfilledHandler, errorHandler, promise, data);
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
            
            this._isRejected = true;
            this.error = error;
            
            if (this._deferreds) {
                while (this._deferreds.length > 0) {
                    var deferred = this._deferreds.shift();
                    var errorHandler = deferred[1];
                    var promise = deferred[2];
                	this._rejected(errorHandler, promise, error);
                }
            }
        },
        
        _fulfilled : function(fulfilledHandler, errorHandler, promise, data) {
            if (fulfilledHandler) {
            	try {
                	var retval = fulfilledHandler(data);
                	if (retval instanceof Promise) {
                		retval.then(
                			function(data) {
                				promise.fulfilled(data);
                			},
                			function(error) {
                				promise.rejected(error);
                			}
                		);
                	} else {
                		promise.fulfilled(retval);
                	}
            	} catch (error) {
            		promise.rejected(error);
            	}
            } else {
            	promise.fulfilled(data);
            }
        },
        
        _rejected : function(errorHandler, promise, error) {
            if (errorHandler) {
            	try {
                	var retval = errorHandler(error);
                	if (!retval) {
                		// stop propogating errors
                		promise.rejected(retval);
                	}
            	} catch (error1) {
            		promise.rejected(error1);
            	}
            } else {
            	promise.rejected(error);
            }
        }
	
	});
	return Promise;
});