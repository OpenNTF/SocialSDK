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
define([ "../declare", "../lang", "../stringUtil", "../xml", "../xpath", "./DataHandler" ], 
	function(declare,lang,stringUtil,xml,xpath,DataHandler) {

    /**
     * XmlDataHandler class
     * 
     * @class XmlDataHandler
     * @namespace sbt.base
     */
    var XmlDataHandler = declare(DataHandler, {

        /**
         * Data type for this DataHandler is 'xml'
         */
        dataType : "xml",

        /**
         * Set of XPath expressions used by this handler. Required for entity:
         * uid, entry Required for summary: totalResults, startIndex,
         * itemsPerPage
         */
        xpath : null,

        /**
         * Set of namespaces used by this handler.
         */
        namespaces : null,

        /**
         * Set of values that have already been read.
         */
        _values : null,

        /**
         * Summary of a feed.
         */
        _summary : null,

        /**
         * @constructor
         * @param {Object}
         *            args Arguments for this data handler.
         */
        constructor : function(args) {
            lang.mixin(this, args);

            this._values = {}; // TODO option to disable cache
            this.data = this._fromNodeOrString(args.data);
        },

        /**
         * Called to set the handler data.
         * 
         * @param data
         */
        setData : function(data) {
            this._values = {}; // TODO option to disable cache
            this.data = this._fromNodeOrString(data);
        },        
        
        /**
         * @method getAsString
         * @param property
         * @returns
         */
        getAsString : function(property) {
            this._validateProperty(property, "getAsString");
            
            if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._selectText(property);
                }
                return this._values[property];
            } else {
                return _selectText(property);
            }
        },

        /**
         * @method getAsNumber
         * @param property
         * @returns
         */
        getAsNumber : function(property) {
            this._validateProperty(property, "getAsNumber");
            
            if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._selectNumber(property);
                }
                return this._values[property];    
            } else {
                return this._selectNumber(property);
            }
        },

        /**
         * @method getAsDate
         * @param property
         * @returns
         */
        getAsDate : function(property) {
            this._validateProperty(property, "getAsDate");
            
            if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._selectDate(property);
                }
                return this._values[property];    
            } else {
                return this._selectDate(property);
            }
        },

        /**
         * @method getAsBoolean
         * @param property
         * @returns
         */
        getAsBoolean : function(property) {
            this._validateProperty(property, "getAsBoolean");
            
            if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._selectBoolean(property);
                }
                return this._values[property];    
            } else {
                return this._selectBoolean(property);
            }
        },

        /**
         * @method getAsArray
         * @param property
         * @returns
         */
        getAsArray : function(property) {
            this._validateProperty(property, "getAsArray");
            
            if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._selectArray(property);
                }
                return this._values[property];    
            } else {
                return this._selectArray(property);
            }
        },
        
        /**
         * @method getNodesArray
         * @param property
         * @returns
         */
        getAsNodesArray : function(property) {
            this._validateProperty(property, "getNodesArray");
            
            if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._selectNodesArray(property);
                }
                return this._values[property];    
            } else {
                return this._selectNodesArray(property);
            }
        },

        /**
         * @method getEntityId
         * @returns
         */
        getEntityId : function() {
            return stringUtil.trim(this.getAsString("uid"));
        },

        /**
         * getEntityData
         * 
         * @returns
         */
        getEntityData : function(document) {
            var entry = this.xpath["entry"];
            if (!entry) {
                return document;
            }
            if (!this._values["entry"]) {
                var nodes = xpath.selectNodes(document, entry, this.namespaces);
                this._values["entry"] = nodes[0] || [];
            }
            return this._values["entry"];
        },

        /**
         * @method getSummary
         * @returns
         */
        getSummary : function() {
            if (!this._summary && this._getXPath("totalResults")) {
                this._summary = {
                    totalResults : xpath.selectNumber(this.data, this._getXPath("totalResults"), this.namespaces),
                    startIndex : xpath.selectNumber(this.data, this._getXPath("startIndex"), this.namespaces),
                    itemsPerPage : xpath.selectNumber(this.data, this._getXPath("itemsPerPage"), this.namespaces)
                };
            }
            return this._summary;
        },

        /**
         * @method getEntitiesDataArray
         * @returns {Array}
         */
        getEntitiesDataArray : function() {
            var entries = this.xpath["entries"];
            if (!entries) {
                return this.data;
            }
            if (!this._values["entries"]) {
                this._values["entries"] = xpath.selectNodes(this.data, entries, this.namespaces);
            }
            return this._values["entries"];
        },
        
        /**
         * @method toJson
         * @returns {Object}
         */
        toJson : function() {
        	var jsonObj = {};
        	
        	for (var name in this.xpath) {
                if (this.xpath.hasOwnProperty(name)) {
                    jsonObj[name] = this.getAsString(name);
                }
            }
        	
        	return jsonObj;
        },        

        /*
         * Convert the input to a node by parsing as string and using
         * getEntityData, if not already one
         */
        _fromNodeOrString : function(nodeOrString) {
            if (lang.isString(nodeOrString)) {
                nodeOrString = stringUtil.trim(nodeOrString);
                var document = xml.parse(nodeOrString);
                return this.getEntityData(document);
            }
            return nodeOrString;
        },

        /*
         * Return xpath expression from the set or the property itself (assume
         * it's already xpath)
         */
        _getXPath : function(property) {
            return this.xpath[property] || property;
        },
        
        /*
         * Validate that the property is valid
         */
        _validateProperty : function(property, method) {
            if (!property) {
                var msg = stringUtil.substitute("Invalid argument for XmlDataHandler.{1} {0}", [ property, method ]);
                throw new Error(msg);
            }
        },
        
        /*
         * Select xpath as string
         */
        _selectText : function(property) {
        	if (!this.data) {
        		return null;
        	}
            return stringUtil.trim(xpath.selectText(this.data, this._getXPath(property), this.namespaces));
        },
        
        /*
         * Select xpath as number
         */
        _selectNumber : function(property) {
        	if (!this.data) {
        		return null;
        	}
            return xpath.selectNumber(this.data, this._getXPath(property), this.namespaces);
        },
        
        /*
         * Select xpath as date
         */
        _selectDate : function(property) {
        	if (!this.data) {
        		return null;
        	}
            var text = this._selectText(property);
            return text ? new Date(text) : null;
        },
        
        /*
         * Select xpath as boolean
         */
        _selectBoolean : function(property) {
        	if (!this.data) {
        		return false;
        	}
            var nodes = xpath.selectNodes(this.data, this._getXPath(property), this.namespaces);
            var ret = false;
            if (nodes) {
           		ret = (nodes.length > 0);
            }
            return ret;
        },
        
        /*
         * Select xpath as array
         */
        _selectArray : function(property) {
        	if (!this.data) {
        		return null;
        	}
            var nodes = xpath.selectNodes(this.data, this._getXPath(property), this.namespaces);
            var ret = null;
            if (nodes) {
                ret = [];
                for ( var i = 0; i < nodes.length; i++) {
                    ret.push(stringUtil.trim(nodes[i].text || nodes[i].textContent));
                }
            }
            return ret;
        },
        
        /*
         * Select xpath as nodes array
         */
        _selectNodesArray : function(property) {
        	if (!this.data) {
        		return null;
        	}
            return xpath.selectNodes(this.data, this._getXPath(property), this.namespaces);            
        }
       

    });
    return XmlDataHandler;
});