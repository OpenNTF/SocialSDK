/*
 * © Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Helpers for the base capabilities of data
 * handlers.
 * 
 * @module sbt.base.DataHandler
 */
define([ "../declare", "../lang" ], function(declare,lang) {

    /**
     * DataHandler class
     * 
     * @class DataHandler
     * @namespace sbt.base
     */
    var DataHandler = declare(null, {

        /**
         * Data type for this DataHandler
         */
        dataType : null,

        /**
         * Data for this DataHandler
         */
        data : null,

        /**
         * @constructor
         * @param {Object}
         *            args Arguments for this data handler.
         */
        constructor : function(args) {
            lang.mixin(this, args);
        },

        /**
         * @method getAsString
         * @param data
         * @returns
         */
        getAsString : function(property) {
            return null;
        },

        /**
         * @method getAsNumber
         * @returns
         */
        getAsNumber : function(property) {
            return null;
        },

        /**
         * @method getAsDate
         * @returns
         */
        getAsDate : function(property) {
            return null;
        },

        /**
         * @method getAsBoolean
         * @returns
         */
        getAsBoolean : function(property) {
            return null;
        },

        /**
         * @method getAsArray
         * @returns
         */
        getAsArray : function(property) {
            return null;
        },

        /**
         * @method getEntityId
         * @returns
         */
        getEntityId : function(data) {
            return null;
        },

        /**
         * @param parent
         * @returns
         */
        getEntityData : function(parent) {
            return data;
        },

        /**
         * @method getSummary
         * @returns
         */
        getSummary : function() {
            return null;
        },

        /**
         * @method getEntitiesDataArray
         * @returns {Array}
         */
        getEntitiesDataArray : function() {
            return [];
        }

    });
    return DataHandler;
});