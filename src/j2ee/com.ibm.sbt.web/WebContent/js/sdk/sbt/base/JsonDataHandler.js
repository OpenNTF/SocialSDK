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
 * Social Business Toolkit SDK. 
 * Helpers for the base capabilities of data handlers.
 * 
 * @module sbt.base.JsonDataHandler
 */
define(["../declare", "../lang", "../json", "./DataHandler", "../Jsonpath", "../stringUtil"], 
    function(declare, lang, json, DataHandler, jsonPath, stringUtil) {

    /**
     * JsonDataHandler class
     * 
     * @class JsonDataHandler
     * @namespace sbt.base
     */     
	var JsonDataHandler = declare(DataHandler, {	
		
        /**
         * Data type for this DataHandler is 'json'
         */
        dataType : "json",
		
        /**
         * Set of jsonpath expressions used by this handler.
         */
        jsonpath : null,
        
        /**
         * Set of values that have already been read.
         */
        _values : null,
        
	    /**
	     * @constructor
	     * @param {Object} args Arguments for this data handler.
	     */     
		constructor : function(args) {
            lang.mixin(this, args);
            this._values = {}; 
            this.data = args.data;
		},
        
        /**
         * @method getAsString
         * @param data
         * @returns
         */
        getAsString : function(property) {
        	this._validateProperty(property, "getAsString");
        	if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._get(property).toString();
                }
                return this._values[property];
            } else {
                return _get(property);
            }
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
        	this._validateProperty(property, "getAsString");
        	if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._get(property);
                }
                return this._values[property];
            } else {
                return _get(property);
            }       
        },

        /**
         * @method getEntityId
         * @returns
         */
        getEntityId : function(data) {
        	return stringUtil.trim(this.getAsString(this.jsonpath["id"]));
        },

        /**
         * getEntityData
         * @returns
         */
        getEntityData : function() {            
            return this.data;
        },
        
        /**
         * @method getSummary
         * @returns
         */
        getSummary : function() {
        	if (!this._summary && this.data.totalResults) {
                this._summary = {
                    totalResults : this._get("totalResults"),
                    startIndex : this._get("startIndex"),
                    itemsPerPage : this._get("itemsPerPage")
                };
            }
            return this._summary;
        },
        
        /**
         * @method getEntitiesDataArray
         * @returns {Array}
         */
        getEntitiesDataArray : function() {
        	var entityJPath = this._getJPath("entry");
        	return this._get(entityJPath);
        },
        
        /**
         Validate that the property is valid
         **/
        _validateProperty : function(property, method) {
            if (!property) {
                var msg = stringUtil.substitute("Invalid argument for JsonDataHandler.{1} {0}", [ property, method ]);
                throw new Error(msg);
            }
        },
        /**
         Validate that the object is valid
         **/
        _validateObject : function(object) {
            if (!object) {
                var msg = stringUtil.substitute("Invalid argument for JsonDataHandler.{0}", [ object ]);
                throw new Error(msg);
            }
        },
        
		_evalJson: function(jsonQuery){
			return jsonPath(this.data,jsonQuery);			
		},
	
		_evalJsonWithData: function(jsonQuery, data){
			return jsonPath(data,jsonQuery);			
		},
		
		_get: function(property){

			this._validateObject(this.data);
			var jsonQuery = this._getJPath(property);

			var result = this._evalJson(jsonQuery);
			return result;
		},
		
		_getJPath: function(property) {
			return this.jsonpath[property] || property;
		},
		
		extractFirstElement: function(result){
			this._validateObject(result);
			return this._evalJsonWithData("$[0]", result);
		}
	});
	return JsonDataHandler;
});