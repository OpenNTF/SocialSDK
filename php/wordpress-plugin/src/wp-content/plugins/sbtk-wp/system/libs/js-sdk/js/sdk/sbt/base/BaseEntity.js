/*
 * © Copyright IBM Corp. 2012, 2013
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
 * Javascript Base APIs for IBM Connections
 * 
 * @module sbt.base.BaseEntity
 */
define([ "../declare", "../lang", "../log", "../stringUtil" ], 
    function(declare,lang,log,stringUtil) {

    var BadRequest = 400;
    
    var requests = {};

    /**
     * BaseEntity class
     * 
     * @class BaseEntity
     * @namespace sbt.base
     */
    var BaseEntity = declare(null, {

        /**
         * The identifier for this entity.
         */
        id : null,

        /**
         * The service associated with the entity.
         */
        service : null,

        /**
         * The DataHandler associated with this entity.
         */
        dataHandler : null,

        /**
         * The fields which have been updated in this entity.
         * 
         * @private
         */
        _fields : null,

        /**
         * Constructor for BaseEntity
         * 
         * @constructor
         * @param {Object} args Arguments for this entity.
         */
        constructor : function(args) {
            lang.mixin(this, args);
            
            if (!this._fields) {
                this._fields = {};
            }
            
            if (!this.service) {
                var msg = "Invalid BaseEntity, an associated service must be specified.";
                throw new Error(msg);
            }
        },
        
        /**
         * Called to set the entity DataHandler after the entity
         * was loaded. This will cause the existing fields to be cleared.
         * 
         * @param datahandler
         */
        setDataHandler : function(dataHandler) {
        	this._fields = {};
        	this.dataHandler = dataHandler;
        },
        
        /**
         * Called to set the entity data after the entity
         * was loaded. This will cause the existing fields to be cleared.
         * 
         * @param data
         */
        setData : function(data, response) {
        	this._fields = {};
        	this.dataHandler.setData(data);
        },
        
        /**
         * Return true if this entity has been loaded.
         *  
         * @returns true if this entity has been loaded
         */
        isLoaded : function() {
        	if (this.dataHandler) {
        		return this.dataHandler.getData() ? true : false;
        	}
            return false;
        },
        
        /**
         * Get the string value of the specified field.
         * 
         * @method getAsString
         * @param fieldName
         * @returns
         */
        getAsString : function(fieldName) {
            this._validateFieldName(fieldName, "getAsString");

            if (this._fields.hasOwnProperty(fieldName)) {
                return this._fields[fieldName];
            } else if (this.dataHandler) {
                return this.dataHandler.getAsString(fieldName);
            } else {
                return null;
            }
        },

        /**
         * Get the number value of the specified field.
         * 
         * @method getAsNumber
         * @param fieldName
         * @returns
         */
        getAsNumber : function(fieldName) {
            this._validateFieldName(fieldName, "getAsNumber");

            if (this._fields.hasOwnProperty(fieldName)) {
                return this._fields[fieldName];
            } else if (this.dataHandler) {
                return this.dataHandler.getAsNumber(fieldName);
            } else {
                return null;
            }
        },

        /**
         * Get the date value of the specified field.
         * 
         * @method getAsDate
         * @param fieldName
         * @returns
         */
        getAsDate : function(fieldName) {
            this._validateFieldName(fieldName, "getAsDate");

            if (this._fields.hasOwnProperty(fieldName)) {
                return this._fields[fieldName];
            } else if (this.dataHandler) {
                return this.dataHandler.getAsDate(fieldName);
            } else {
                return null;
            }
        },

        /**
         * Get the boolean value of the specified field.
         * 
         * @method getAsBoolean
         * @param fieldName
         * @returns
         */
        getAsBoolean : function(fieldName) {
            this._validateFieldName(fieldName, "getAsBoolean");

            if (this._fields.hasOwnProperty(fieldName)) {
                return this._fields[fieldName];
            } else if (this.dataHandler) {
                return this.dataHandler.getAsBoolean(fieldName);
            } else {
                return false;
            }
        },

        /**
         * Get the array value of the specified field.
         * 
         * @method getAsArray
         * @param fieldName
         * @returns
         */
        getAsArray : function(fieldName) {
            this._validateFieldName(fieldName, "getAsArray");

            if (this._fields.hasOwnProperty(fieldName)) {
                return this._fields[fieldName];
            } else if (this.dataHandler) {
                return this.dataHandler.getAsArray(fieldName);
            } else {
                return null;
            }
        },

		 /**
         * Get the nodes array value of the specified field.
         * 
         * @method getAsNodesArray
         * @param fieldName
         * @returns
         */
        getAsNodesArray : function(fieldName) {
            this._validateFieldName(fieldName, "getAsNodesArray");

            if (this._fields.hasOwnProperty(fieldName)) {
                return this._fields[fieldName];
            } else if (this.dataHandler) {
                return this.dataHandler.getAsNodesArray(fieldName);
            } else {
                return null;
            }
        },
        /**
         * Get an object containing the values of the specified fields.
         * 
         * @method getAsObject
         * @param fieldNames
         * @returns
         */
        getAsObject : function(fieldNames, objectNames) {
            var obj = {};
            for (var i=0; i<fieldNames.length; i++) {
                this._validateFieldName(fieldNames[i], "getAsObject");
                var fieldValue = this.getAsString(fieldNames[i]);
                if (fieldValue) {
                    if (objectNames) {
                    	obj[objectNames[i]] = fieldValue;
                    } else {
                    	obj[fieldNames[i]] = fieldValue;
                    }
                }
            }
            return obj;
        },

        /**
         * @method setAsString
         * @param data
         * @returns
         */
        setAsString : function(fieldName,string) {
            this._validateFieldName(fieldName, "setAsString", string);

            if (string === null || string === undefined) {
                delete this._fields[fieldName];
            } else {
                this._fields[fieldName] = string.toString();
            }
            return this;
        },

        /**
         * @method setAsNumber
         * @returns
         */
        setAsNumber : function(fieldName,numberOrString) {
            this._validateFieldName(fieldName, "setAsNumber", numberOrString);

            if (numberOrString instanceof Number) {
                this._fields[fieldName] = numberOrString;
            } else {
                if (numberOrString) {
                    var n = new Number(numberOrString);
                    if (isNaN(n)) {
                        var msg = stringUtil.substitute("Invalid argument for BaseService.setAsNumber {0},{1}", [ fieldName, numberOrString ]);
                        throw new Error(msg);
                    } else {
                        this._fields[fieldName] = n;
                    }
                } else {
                    delete this._fields[fieldName];
                }
            }
            return this;
        },

        /**
         * @method setAsDate
         * @returns
         */
        setAsDate : function(fieldName,dateOrString) {
            this._validateFieldName(fieldName, "setAsDate", dateOrString);

            if (dateOrString instanceof Date) {
                this._fields[fieldName] = dateOrString;
            } else {
                if (dateOrString) {
                    var d = new Date(dateOrString);
                    if (isNaN(d.getDate())) {
                        var msg = stringUtil.substitute("Invalid argument for BaseService.setAsDate {0},{1}", [ fieldName, dateOrString ]);
                        throw new Error(msg);
                    } else {
                        this._fields[fieldName] = d;
                    }
                } else {
                    delete this._fields[fieldName];
                }
            }
            return this;
        },

        /**
         * @method setAsBoolean
         * @returns
         */
        setAsBoolean : function(fieldName,booleanOrString) {
            this._validateFieldName(fieldName, "setAsBoolean", booleanOrString);

            if (booleanOrString != null) {
                this._fields[fieldName] = booleanOrString ? true : false;
            } else {
                delete this._fields[fieldName];
            }
            return this;
        },

        /**
         * @method setAsArray
         * @returns
         */
        setAsArray : function(fieldName,arrayOrString) {
            this._validateFieldName(fieldName, "setAsArray", arrayOrString);

            if (lang.isArray(arrayOrString)) {
                this._fields[fieldName] = arrayOrString.slice(0);
            } else if (lang.isString(arrayOrString)) {
                if (arrayOrString.length > 0) {
                    this._fields[fieldName] = arrayOrString.split(/[ ,]+/);
                } else {
                    this._fields[fieldName] = [];
                }
            } else {
                if (arrayOrString) {
                    this._fields[fieldName] = [ arrayOrString ];
                } else {
                    delete this._fields[fieldName];
                }
            }

            return this;
        },

        /**
         * Set an object containing the values of the specified fields.
         * 
         * @method setAsObject
         * @param theObject
         * @returns
         */
        setAsObject : function(theObject) {
            for (var property in theObject) {
                if (theObject.hasOwnProperty(property)) {
                    var value = theObject[property];
                    if (value) {
                        this._fields[property] = value;
                    } else {
                        delete this._fields[property];
                    }
                }
            }
        },

        /**
         * Remove the value of the specified field.
         * 
         * @method remove
         * @param fieldName
         */
        remove : function(fieldName) {
            delete this._fields[fieldName];
        },
        
        /**
         * Return the json representation of the entity
         * 
         * @method toJson
         * @returns {Object}
         */
        toJson : function() {
        	return (this.dataHandler) ? this.dataHandler.toJson() : {};
        },

        /*
         * Validate there is a valid field name
         */
        _validateFieldName : function(fieldName, method, value) {
            if (!fieldName) {
                var msg = stringUtil.substitute("Invalid argument for BaseService.{1} {0},{2}", [ fieldName, method, value || "" ]);
                throw new Error(msg);
            }
        }
    });

    return BaseEntity;
});
