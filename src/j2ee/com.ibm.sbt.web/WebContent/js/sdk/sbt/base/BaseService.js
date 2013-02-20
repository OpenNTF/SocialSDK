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
 * 
 * Javascript Base APIs for IBM Connections 
 * @module sbt.connections.BaseService
 * @author Carlos Manias
 * 
 */
define(['sbt/_bridge/declare','sbt/config','sbt/lang','sbt/base/core','sbt/xml','sbt/xpath','sbt/Cache','sbt/Endpoint', 'sbt/base/BaseConstants', 
        "sbt/validate", 'sbt/log', 'sbt/stringutil'],
		function(declare,cfg,lang,con,xml,xpath,Cache,Endpoint, BaseConstants, validate, log, stringutil) {
	
	var requests = {};
	
	function notifyCb(id,param) {
		log.debug("notifyCb() : called with id : {0}, and param : {1}", id, param);
		var r = requests[id];
  		if(r) {
	  		delete requests[id];
	  		for(var i=0; i<r.length; i++) {
	  			r[i](param);
	  		}
  		}
	}
	
	function notifyCbNoCache(args, param) {

		if (args) {
			if (args.load)
				args.load(param);
			else if (args.handle)
				args.handle(param);
		} else {
			log.error("Callbacks not defined. Return Value={0}", param);
		}
	}
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
		
		_id:		"",
		
		constructor: function(svc,id,args) {
			this._service = svc;
			this._id = id;			
			this._fields = {};
			this._data =	null;
			
			this._entityName = args.entityName;
			this._Constants = lang.mixin(lang.mixin({}, BaseConstants), args.Constants);
			this._xpath = args.xpath || "xpath_"+this._entityName;
			this._xpath_feed = args.xpath_feed || "xpath_feed_"+this._entityName;
			this._con = args.con || con; //NameSpaces
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
			return this._fields[fieldName] || this.xpath(this.fieldXPathForEntry(fieldName));				
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
	
	/**
	Base service class.

	@class BaseService
	@constructor
	@param {Object} options  Options object
	@param {String} [options.endpoint]  Endpoint to be used.
		
	**/
	var BaseService = declare("sbt.base.BaseService", null, {
		"-chains-" : {
			constructor : "manual"
		},
		
		
		_endpoint: null,
		
		constructor: function(_options) {			
			this._endpoint = Endpoint.find(_options.endpoint);
			this.Constants = lang.mixin(lang.mixin({}, BaseConstants), _options.Constants);
			this._con = _options.con || con; //NameSpaces
			var cacheSize = _options.cacheSize;
			if(cacheSize && cacheSize>0) {
				this._cache = new Cache(cacheSize);
			}
			
		},
		
		_notifyResponse: function(args, response, summary){
			if (args.load || args.handle) {
				if (args.handle) {
					try {
						args.handle(response, summary);
					} catch (error) {
						log.error("Error running handle callback : {0}", error);
					}
				}
				if (args.load) {
					try {
						args.load(response, summary);
					} catch (error) {
						log.error("Error running load callback : {0}", error);
					}
				}
			} else {
				log.error("Error received. Couldn't find load or handle callbacks");
			}
		},
		
		_getOne: function(args,getArgs) {			
			var entity = this._createEntityObject(this, args.id, getArgs.entityName);			
			if(args.loadIt == false){
				this._notifyResponse(args,entity);
			}else{
				this._load(entity,args,getArgs);
			}
			return entity;
		},
		
		_load: function (entity,args,getArgs) {
			getArgs.methodType = "get";
			var _self = this;
			var handleAs = (getArgs.handleAs)?getArgs.handleAs : "text";
			if (getArgs.cachingEnabled && requests[entity._id]) {
				this._stackRequestsForExistingId(entity, args);				
			} else {
				if (getArgs.cachingEnabled) {
					this._stackRequestsForNewId(entity, args);
				}
				this._endpoint.xhrGet({
					serviceUrl:	this._constructServiceUrl(getArgs),
					handleAs:	handleAs,
					content:	_self._constructQueryObj(args,getArgs),
					load: function(data, ioArgs) {
						if(handleAs == "text"){							
							_self._loadOnLoad(data, ioArgs, entity, args, getArgs);	
						}
					},
					error: function(error){
						_self._loadOnError(error, args);
					}
				});
			}
		},
		
		_loadOnLoad : function (data, ioArgs, entity, args, getArgs){
			var xpath_map = this.Constants["xpath_" + getArgs.entityName];
			var entry = xpath.selectNodes(xml.parse(data), xpath_map["entry"], this._con.namespaces);
			var node = entry[0];
			entity.setData(node);
			if (getArgs.cachingEnabled) {
				if(this._cache){
					this._cache.put(xpath.selectText(entity._data,xpath_map.uid,con.namespaces),entity._data);
				}
				notifyCb(entity._id, entity);
			} else {
				notifyCbNoCache(args, entity);
			}	
		},
		
		_loadOnError : function (error, args){
			validate.notifyError(error, args);
		},
		
		_stackRequestsForExistingId : function (entity, args){
			if(args.load){
				requests[entity._id].push(args.load);
			}
			if(args.handle){
				requests[entity._id].push(args.handle);
			}
		},
		
		_stackRequestsForNewId : function (entity, args){
			if(args.load) {
				requests[entity._id] = [args.load];
			}
			if(args.handle){
				if(requests[entity._id]) {
					requests[entity._id].push(args.handle);
				}else {
					requests[entity._id] = [args.handle];
				}
			}	
		},
		
		getIdFromIdOrObject: function(inputEntity){
			return entityId = (!(typeof inputEntity == "object")) ? inputEntity._id : inputEntity;
		},
		
		_constructXMLPayload: function(getArgs){
			if(getArgs.xmlPayload){
				return getArgs.xmlPayload;
			}else{
				return getArgs.xmlPayloadTemplate.replace(/{$(\w*)}/g,function(m,key){return getArgs.xmlData.hasOwnProperty(key)?xml.encodeXmlEntry(getArgs.xmlData[key]):"";});
			}
		},
		
		_createRequestObject: function(args, requestArgs){
			var _self = this;
			var requestObject = {
					serviceUrl:_self._constructServiceUrl(requestArgs)									
			};
			if (args.parameters) {
				if(!(requestArgs.urlParams)){
					requestArgs.urlParams = {};
				}
				requestArgs.urlParams = lang.mixin(requestArgs.urlParams, args.parameters);
			}
			if(requestArgs.headers){
				requestObject.headers = requestArgs.headers;
			}
			if(requestArgs.handleAs){
				requestObject.handleAs = requestArgs.handleAs;
			}
			return requestObject;
		},
		
		/**
			Create a new entity
			
			@method createEntity
			@param {Object} entity  Entity object
			@param {Object} [args]  Argument object
			@param {Boolean} [args.loadIt=true] 
			@param {Function} [args.load] 
			@param {Function} [args.error] 
			@param {Function} [args.handle] 
		
		**/
		_createEntity: function (args, postArgs) {
			var _self = this;
			var entity = postArgs.entity;
			var handleAs = (postArgs.handleAs)?getArgs.handleAs : "text";
			var requestObject = lang.mixin({
				postData:_self._constructXMLPayload(postArgs),
				load:function(data, ioArgs){
					if(handleAs == "text"){
						_self._createEntityOnLoad(data, ioArgs, entity, args);
					}
				},
				error:function(error){
					_self._createEntityOnError(error, args);
				}
			}, _self._createRequestObject(args, postArgs));
			this._endpoint.xhrPost(requestObject);
		},
		
		_createEntityOnLoad : function (data, ioArgs, entity, args){
			entity.data = xml.parse(data);
			this._notifyResponse(args,entity);
		},
		
		_createEntityOnError : function (error, args){
			validate.notifyError(error, args);
		},
		
		/**
		Update an existing entity
		
		@method updateEntity
		@param {Object} entity  Entity object
		@param {Object} [args]  Argument object			
			@param {Function} [args.load] 
			@param {Function} [args.error] 
			@param {Function} [args.handle] 
		
		**/
		_updateEntity: function (args, putArgs) {
			
			var _self = this;
			var entity = putArgs.entity;
			var handleAs = (putArgs.handleAs)?getArgs.handleAs : "text";
			var requestObject = lang.mixin({
				putData:_self._constructXMLPayload(putArgs),
				load:function(data, ioArgs){
					if(handleAs == "text"){
						_self._updateEntityOnLoad(data, ioArgs, entity, args);
					}
				},
				error: function(error){
					_self._updateEntityOnError(error, args);
				}
			}, _self._createRequestObject(args, putArgs));
			this._endpoint.xhrPut(requestObject);
		},
		
		_updateEntityOnLoad : function (data, ioArgs, entity, args){
			entity.data = xml.parse(data);
			this._notifyResponse(args,entity);
		},
		
		_updateEntityOnError : function (error, args){
			validate.notifyError(error, args);
		},
			
		/**
		Delete an existing entity
		
		@method deleteEntity
		@param {String/Object} entity  id of the entity or the entity object.
		@param {Object} [args]  Argument object			
			@param {Function} [args.load] 
			@param {Function} [args.error] 
			@param {Function} [args.handle] 
		
		**/
		_deleteEntity: function (args, deleteArgs) {
			var _self = this;
			var entity = deleteArgs.entity;
			var handleAs = (deleteArgs.handleAs)?getArgs.handleAs : "text";
			var requestObject = lang.mixin({
				load:function(data, ioArgs){
					if(handleAs == "text"){
						_self._deleteEntityOnLoad(data, ioArgs, entity, args);
					}
				},
				error: function(error){
					_self._deleteEntityOnError(error, args);
				}
			}, _self._createRequestObject(args, deleteArgs));
			this._endpoint.xhrDelete(requestObject);
		},
		
		_deleteEntityOnLoad : function (data, ioArgs, entity, args){
			if(this._cache) {
				this._deleteIdFromCache(entity._id);
      		}
			this._notifyResponse(args);
		},
		
		_deleteEntityOnError : function (error, args){
			validate.notifyError(error, args);
		},
		
		_constructServiceUrl: function (requestArgs){			
			var authType = requestArgs.authType;
			authType = (requestArgs.authType)?requestArgs.authType : "";
			
			var url = this.Constants.entityServiceBaseUrl + authType + (this.Constants.serviceEntity[requestArgs.serviceEntity]?this.Constants.serviceEntity[requestArgs.serviceEntity]: "") + (this.Constants.entityType[requestArgs.entityType] ? this.Constants.entityType[requestArgs.entityType] : "");
			
			if(requestArgs.methodType != "get"){
				if (requestArgs.urlParams) {
					url += "?";
					var additionalElement = false;
					for (param in requestArgs.urlParams) {
						if (additionalElement) {
							url+= "&";
						}
						else {
							additionalElement = true;
						}// encode url parameters
						url += param + "=" + encodeURIComponent(requestArgs.urlParams[param]);
					}
				}
			}
			return url;
		},
		
		_constructQueryObj: function (args, requestArgs){
			var params = args.parameters;
			if(requestArgs.urlParams){				
				params = lang.mixin(params, requestArgs.urlParams);
			}
			return params;
		},
		
		_getEntities: function (args,getArgs) {
			var _self=this;
			getArgs.methodType = "get";	
			var handleAs = (getArgs.handleAs)?getArgs.handleAs : "text";
			var requestObject = lang.mixin({
				content: _self._constructQueryObj(args,getArgs),
				load:function(data, ioArgs){
					if(handleAs == "text"){
						summary = _self._getSummaryOnLoad(data, getArgs);
						entities = _self._getEntititiesOnLoad(data, getArgs);
						_self._notifyResponse(args, entities, summary);
					}
				}
				
			}, _self._createRequestObject(args, getArgs));			
			this._endpoint.xhrGet(requestObject);
		},
		
		_getSummaryOnLoad: function (data, getArgs){
			var xpath_map = this.Constants["xpath_feed_"+getArgs.entityName];
			var xmlData = xml.parse(data); 
			var summary = {};
			summary.totalResults = xpath.selectText(xmlData, xpath_map.totalResults, this._con.namespaces);
			summary.startIndex = xpath.selectText(xmlData, xpath_map.startIndex, this._con.namespaces);
			summary.itemsPerPage = xpath.selectText(xmlData, xpath_map.itemsPerPage, this._con.namespaces);	
			return summary;
		},
		_getEntititiesOnLoad: function (data, getArgs){
			console.log(data);
			var entities = [];
			var xpath_map = this.Constants["xpath_feed_"+getArgs.entityName];
			var xmlData = xml.parse(data);	
			var entry = xpath.selectNodes(xmlData,xpath_map["entry"],this._con.namespaces);
			for(var count=0; count < entry.length; count++){
				var node = entry[count];
				 var entityId = xpath.selectText(node,xpath_map["id"],this._con.namespaces);
				 entityId = getArgs.parseId ? getArgs.parseId(entityId) : entityId;			
				 var entity = this._createEntityObject(this, entityId, getArgs.entityName);
				if(!(getArgs.ignoreData)){
					entity.setData(node);
				}
				entities.push(entity);
			}
			return entities;
		},
		
		_isEmail: function(id) {
			return id && id.indexOf('@')>=0;
		},
		_deleteIdFromCache:function(_id){
			if(this._cache) {
				this._cache.remove(_id);
			}
		},
		
		_find: function(id) {
			if(this._cache) {
				return this._cache.get(id);
			}
			else{			
				return null;
			}
		}
	});
	return BaseService;
});
