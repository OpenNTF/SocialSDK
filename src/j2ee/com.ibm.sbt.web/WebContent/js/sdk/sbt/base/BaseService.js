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
define(['../declare','../config','../lang','../base/core','../xml','../xpath','../Cache','../Endpoint', '../base/BaseConstants', 
        "../validate", '../log', '../stringUtil', '../base/BaseHandler','../util','./BaseEntity'],
		function(declare,cfg,lang,con,xml,xpath,Cache,Endpoint, BaseConstants, validate, log, stringUtil, BaseHandler, util, BaseEntity) {
	
	var requests = {};
	
	function notifyCallbacks(id,param) {
		log.debug("notifyCallbacks() : called with id : {0}, and param : {1}", id, param);
		var r = requests[id];
  		if(r) {
	  		delete requests[id];
	  		for(var i=0; i<r.length; i++) {
	  			r[i](param);
	  		}
  		}
	}
	
	/**
	Base service class.

	@class BaseService
	@constructor
	@param {Object} options  Options object
	@param {String} [options.endpoint]  Endpoint to be used.
		
	**/
	var BaseService = declare(null, {
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
			}
		},
		
		_createEntityObject : function (service, id, requestArgs, args){
			return requestArgs.entity.apply(null,[service, id]);
		},
		
		_getOne: function(args,getArgs) {			
			var entity = this._createEntityObject(this, args.id, getArgs, args);			
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
			var cachedData = null;
			if(getArgs.cachingEnabled){
				cachedData = this._find(entity._id);
			}
			if(cachedData) {				
					entity.setData(cachedData);		
					this._notifyResponse(args, entity);
			}else{
				if (getArgs.cachingEnabled && requests[entity._id]) {
					this._stackRequestsForExistingId(entity, args);				
				} else {
					if (getArgs.cachingEnabled) {
						this._stackRequestsForNewId(entity, args);
					}
					this._endpoint.xhrGet({
						serviceUrl:	_self._constructServiceUrl(getArgs),
						handleAs:	handleAs,
						content:	_self._constructQueryObj(args,getArgs),
						load: function(data, ioArgs) {
							_self._loadOnLoad(data, ioArgs, entity, args, getArgs);	
						},
						error: function(error){
							_self._loadOnError(error, args);
						}
					});
				}
			}
		},
		
		_loadOnLoad : function (data, ioArgs, entity, args, getArgs){
			var dataHandler = (getArgs.dataHandler) ? getArgs.dataHandler : entity._dataHandler ;			
			entity.setData(dataHandler._extractEntryFromSingleEntryFeed(data, ioArgs));			
			if (getArgs.cachingEnabled) {
				if(this._cache){
					this._cache.put(dataHandler._extractIdFromEntry(entity._data),entity._data);
				}
				notifyCallbacks(entity._id, entity);
			} else {
				this._notifyResponse(args, entity);
			}	
		},
		
		_loadOnError : function (error, args){
			util.notifyError(error, args);
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
		
		_constructPayload: function(requestArgs){
			var dataHandler = (requestArgs.dataHandler) ? requestArgs.dataHandler : requestArgs.entity._dataHandler ;			
			return dataHandler._constructPayload(requestArgs);
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
			var requestObject = lang.mixin({
				postData:_self._constructPayload(postArgs),
				load:function(data, ioArgs){
					_self._createEntityOnLoad(data, ioArgs, entity, args, postArgs);
				},
				error:function(error){
					_self._createEntityOnError(error, args);
				}
			}, _self._createRequestObject(args, postArgs));
			this._endpoint.xhrPost(requestObject);
		},
		
		_createEntityOnLoad : function (data, ioArgs, entity, args, postArgs){
			var dataHandler = (postArgs.dataHandler) ? postArgs.dataHandler : entity._dataHandler ;						
			entity._data = dataHandler._extractEntryFromSingleEntryFeed(data, ioArgs);
			this._notifyResponse(args,entity);
		},
		
		_createEntityOnError : function (error, args){
			util.notifyError(error, args);
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
			var requestObject = lang.mixin({
				putData:_self._constructPayload(putArgs),
				load:function(data, ioArgs){
					_self._updateEntityOnLoad(data, ioArgs, entity, args, putArgs);
				},
				error: function(error){
					_self._updateEntityOnError(error, args);
				}
			}, _self._createRequestObject(args, putArgs));
			this._endpoint.xhrPut(requestObject);
		},
		
		_updateEntityOnLoad : function (data, ioArgs, entity, args, putArgs){
			var dataHandler = (putArgs.dataHandler) ? putArgs.dataHandler : entity._dataHandler ;						
			entity._data = dataHandler._extractEntryFromSingleEntryFeed(data, ioArgs);
			this._notifyResponse(args,entity);
		},
		
		_updateEntityOnError : function (error, args){
			util.notifyError(error, args);
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
			var requestObject = lang.mixin({
				load:function(data, ioArgs){
					_self._deleteEntityOnLoad(data, ioArgs, entity, args);
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
			util.notifyError(error, args);
		},
		
		_constructServiceUrl: function (requestArgs){			
			var authType = requestArgs.authType;
			authType = (requestArgs.authType)?requestArgs.authType : "";
			
			var url = this.Constants.entityServiceBaseUrl + authType + (this.Constants.serviceEntity[requestArgs.serviceEntity]?this.Constants.serviceEntity[requestArgs.serviceEntity]: "") + (this.Constants.entityType[requestArgs.entityType] ? this.Constants.entityType[requestArgs.entityType] : "");
			
			if (requestArgs.replaceArgs) {
				url = stringUtil.replace(url, requestArgs.replaceArgs);
			}
			
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
			var requestObject = lang.mixin({
				content: _self._constructQueryObj(args,getArgs),
				load:function(data, ioArgs){
					summary = _self._getSummaryOnLoad(data, getArgs, args);
					entities = _self._getEntititiesOnLoad(data, getArgs, args);
					_self._notifyResponse(args, entities, summary);
				},
				error: function(error){
					_self._getEntitiesOnError(error, args);
				}
				
			}, _self._createRequestObject(args, getArgs));			
			this._endpoint.xhrGet(requestObject);
		},
		
		_getEntitiesOnError : function (error, args){
			util.notifyError(error, args);
		},
		
		_getSummaryOnLoad: function (data, getArgs, args){
			var dataHandler = getArgs.dataHandler;			
			return dataHandler._extractSummaryFromEntitiesFeed(data);
		},
		
		_getEntititiesOnLoad: function (data, getArgs, args){			
			var dataHandler = getArgs.dataHandler ;			
			var entities = [];			
			var entry = dataHandler._extractEntriesFromEntitiesFeed(data);
			for(var count=0; count < entry.length; count++){
				 var node = entry[count];
				 var entityId = dataHandler._extractIdFromEntry(node);
				 entityId = getArgs.parseId ? getArgs.parseId(entityId) : entityId;			
				 var entity = this._createEntityObject(this, entityId, getArgs, args);
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
