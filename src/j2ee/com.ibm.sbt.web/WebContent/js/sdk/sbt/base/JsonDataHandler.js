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
        	this._validateProperty(property, "getAsNumber");
        	if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._getNumber(property); 
                }
                return this._values[property];
            } else {
                return _getNumber(property);
            }
        },

        /**
         * @method getAsDate
         * @returns
         */
        getAsDate : function(property) {
        	this._validateProperty(property, "getAsDate");
        	if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._getDate(property); 
                }
                return this._values[property];
            } else {
                return _getDate(property);
            }
        },

        /**
         * @method getAsBoolean
         * @returns
         */
        getAsBoolean : function(property) {
        	this._validateProperty(property, "getAsBoolean");
        	if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._getBoolean(property); 
                }
                return this._values[property];
            } else {
                return _getBoolean(property);
            }                    
        },

        /**
         * @method getAsArray
         * @returns
         */
        getAsArray : function(property) {
        	this._validateProperty(property, "getAsArray");
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
        	if (!this._summary && this._get("totalResults")[0]) {
                this._summary = {
                	totalResults : this.getAsNumber("totalResults"),
                	startIndex : this.getAsNumber("startIndex"),
                	itemsPerPage : this.getAsNumber("itemsPerPage")
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
        	var resultingArray = this._get(entityJPath);
        	return resultingArray[0];
        },
        
        _getDate : function(property) {
        	var text = this._get(property)[0];
        	if(text instanceof Date) {
        		return text;
        	}
        	else {
        		return new Date(text);
        	}
        },
        
        _getBoolean : function(property) {
        	var text = this._get(property)[0];
        	return text ? true : false;
        },
        
        _getNumber : function(property) {
        	var text = this._get(property)[0];
        	// if it is a Number
        	if(typeof text === 'number')
        		return text;
        	//if its an array, we return the length of the array
        	else if(lang.isArray(text))
        		return text.length;
        	//if its a string or any other data type, we convert to number and return. Invalid data would throw an error here.
        	else return Number(text);
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