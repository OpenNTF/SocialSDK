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
 * Helpers for the base capabilities of data handlers.
 */
define(['sbt/_bridge/declare', 'sbt/lang', 'sbt/xml','sbt/xpath', 'sbt/base/BaseConstants'],function(declare, lang, xml, xpath, BaseConstants) {

    /**
    Base Entity class
    @class BaseEntity
    @constructor
    @param {Object} service  Service object
    @param {String} id id associated with the entity.
    **/     
    var BaseEntity = declare("sbt.base.BaseEntity", null, {
        "-chains-" : {
            constructor : "manual"
        },
        
        _id:        "",
        
        constructor: function(svc,id,args) {
            this._service = svc;
            this._id = id;          
            this._fields = {};
            this._data =    null;
            
            this._entityName = args.entityName;
            this._Constants = lang.mixin(lang.mixin({}, BaseConstants), args.Constants);
            this._xpath = args.xpath || "xpath_"+this._entityName;
            this._xpath_feed = args.xpath_feed || "xpath_feed_"+this._entityName;
            this._con = args.con || con; //NameSpaces
            if(args.dataHandler){
                this._dataHandler = args.dataHandler;
            }else{
                this._dataHandler = new BaseHandler();
            }
        },
        
        /**
        Loads the entity object with the atom entry associated with the entity. By
        default, a network call is made to load the atom entry document in the entity object.

        @method load
        @param {Object} [args]  Argument object         
            @param {Boolean} [args.loadIt=true] Loads the entity object with atom entry document of the entity. To 
            instantiate an empty entity object associated with an entity (with no atom entry
            document), the load method must be called with this parameter set to false. By default, this 
            parameter is true.
            @param {Function} [args.load] The function entity.load invokes when the entity is 
            loaded from the server. The function expects to receive one parameter, 
            the loaded entity object.
            @param {Function} [args.error] Sometimes the load calls fail. Often these are 404 errors 
            or server errors such as 500. The error parameter is another callback function
            that is only invoked when an error occurs. This allows to control what happens
            when an error occurs without having to put a lot of logic into your load function
            to check for error conditions. The parameter passed to the error function is a 
            JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
            JavaScript library error object, the status code and the error message.
            @param {Function} [args.handle] This callback is called regardless of whether 
            the call to load the entity completes or fails. The parameter passed to this callback
            is the entity object (or error object). From the error object. one can get access to the 
            JavaScript library error object, the status code and the error message.
        
        **/
        load: function(args) {
            if(!this._data) {
                this._data = this._service._load(this,args);
            }
        },
        /**
        Updates the entity object.

        @method update
        @param {Object} [args]  Argument object         
            @param {Function} [args.load] The function entity.load invokes when the entity is 
            loaded from the server. The function expects to receive one parameter, 
            the loaded entity object.
            @param {Function} [args.error] Sometimes the load calls  fail. Often these are due to bad request 
            like http error code 400 or server errors like http error code 500. The error parameter is another callback function
            that is only invoked when an error occurs. This allows to control what happens
            when an error occurs without having to put a lot of logic into your load function
            to check for error conditions. The parameter passed to the error function is a 
            JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
            javascript library error object, the status code and the error message.
            @param {Function} [args.handle] This callback is called regardless of whether 
            the call to load the entity completes or fails. The  parameter passed to this callback
            is the entity object (or error object). From the error object. one can get access to the 
            javascript library error object, the status code and the error message.
        
        **/
        update: function(args) {
            this._service.updateEntity(this,args);          
        },
        
        updateData: function(field, value) {
            for (var field in this._fields) {
                this._data.getElementsByTagName(field)[0].childNodes[0].nodeValue=this._fields[field];
            }
        },
        
        getUpdatePayload: function(){
            return xml.asString(this._data);
        },
        
        /**
        Return the xpath expression for a field in the atom entry document of the entity.
        @method fieldXPathForEntry
        @param {String} fieldName Xml element name in atom entry document of the entity.
        @return {String} xpath for the element in atom entry document of the entity.    
        **/
        fieldXPathForEntry: function(fieldName) {
            return this._Constants[this._xpath][fieldName];
        },
        /**
        Return the xpath expression for a field in the atom entry of the entity
        within a feed of entities.
        @method fieldXPathForFeed
        @param {String} fieldName Xml element name in entry of the entity.
        @return {String} xpath for the element in entry of the entity.  
        **/
        fieldXPathForFeed: function(fieldName){
            return this._Constants[this._xpath_feed][fieldName];
        },
        /**
        Return the value of a field in the entity entry using xpath expression
        @method xpath
        @param {String} path xpath expression
        @return {String} value of a field in entity entry using the xpath expression
        **/
        xpath: function(path) {
            return this._data && path ? xpath.selectText(this._data,path,this._con.namespaces) : null;
        },
        /**
        Return an array of nodes from a entity entry using xpath expression
        @method xpathArray
        @param {String} path xpath expression
        @return {Object} an array of nodes from a entity entry using xpath expression
        **/
        xpathArray: function(path){
            return this._data && path ? xpath.selectNodes(this._data,path,this._con.namespaces) : null;
        },
        get: function(fieldName) {  
            if(this._fields[fieldName]){
                return this._fields[fieldName];
            }else if (this._dataHandler._dataType == "xml"){
               return this.xpath(this.fieldXPathForEntry(fieldName));
            }
        },
        
        set: function(fieldName,value) {
            this._fields[fieldName] = value;
        },
        
        setData: function(data) {
            this._data = data;
        },
        
        remove: function(fieldName) {
            delete this._fields[fieldName];
        }
        
    });
    return BaseEntity;
});