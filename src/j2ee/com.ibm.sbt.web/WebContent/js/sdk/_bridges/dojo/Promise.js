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
 * Promise implementation which is used for communication between asynchronous
 * threads.
 * 
 * @module sbt._bridge.Promise
 */
define([ "sbt/_bridge/declare", "sbt/lang" ], function(declare,lang) {

    /**
     * Promise class
     * 
     * @class Promise
     * @namespace sbt._bridge
     */
    var Promise = declare("sbt._bridge.Promise", null, {

        // private
        _isRejected : false,
        _isFulfilled : false,
        _isCancelled : false,
        _callbacks : [],
        _errbacks : [],

        /*
         * Constructor for the Promise.
         */
        constructor : function() {

        },

        /*
         * Add new callbacks to the promise.
         */
        then : function(callback,errback,progback) {
            if (this._isFulfilled) {
                callback(this.items);
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
        cancel : function(reason,strict) {
            this._isCancelled = true;
        },

        /*
         * Checks whether the promise has been resolved.
         */
        isResolved : function() {
            return this._isRejected || this._isFulfilled;
        },

        /*
         * Checks whether the promise has been rejected.
         */
        isRejected : function() {
            return this._isRejected;
        },

        /*
         * Checks whether the promise has been resolved or rejected.
         */
        isFulfilled : function() {
            return this._isFulfilled;
        },

        /*
         * Checks whether the promise has been canceled.
         */
        isCanceled : function() {
            return this._isCancelled;
        },

        // Internals

        _fullFilled : function(totalCount) {
            if (this._isCancelled) {
                return;
            }
            this._isFulfilled = true;
            while (this._callbacks.length > 0) {
                var callback = this._callbacks.shift();
                callback(totalCount);
            }
        },

        _rejected : function(error) {
            if (this._isCancelled) {
                return;
            }
            this._isRejected = true;
            while (this._errbacks.length > 0) {
                var errback = this._errbacks.shift();
                errback(error);
            }
        }

    });
    return Promise;
});