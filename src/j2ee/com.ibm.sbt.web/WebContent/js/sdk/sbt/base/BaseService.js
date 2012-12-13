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
define(['sbt/_bridge/declare','sbt/config','sbt/lang','sbt/base/core','sbt/xml','sbt/xpath','sbt/Cache','sbt/Endpoint', 'sbt/base/BaseConstants'],
		function(declare,cfg,lang,con,xml,xpath,Cache,Endpoint, BaseConstants) {
	
	var requests = {};
	
	function notifyCb(id,param) {
		var r = requests[id];
  		if(r) {
	  		delete requests[id];
	  		for(var i=0; i<r.length; i++) {
	  			r[i](param);
	  		}
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
			this._service =	null,
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
			if(fieldName == "tags"){
				return this._getTags(fieldName);
			}
			return this._fields[fieldName] || this.xpath(this.fieldXPathForEntry(fieldName)) || this.xpath(this.fieldXPathForFeed(fieldName));				
		},
		
		set: function(fieldName,value) {
			this._fields[fieldName] = value;
		},
		
		setData: function(data) {
			this._data = data;
		},
		
		remove: function(fieldName) {
			delete this._fields[fieldName];
		},
		_getTags: function(fieldName){			
				var tagsObj = this.xpathArray(this.fieldXPathForEntry(fieldName)).slice(1);
				var tags = [];
				for(var count=0; count < tagsObj.length; count++){
					var node = tagsObj[count];
					var tag = node.text || node.textContent;
					tags.push(tag);
				}
				return tags;					
		},
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
			var options = _options || {};
			this._endpoint = Endpoint.find(options.endpoint);
			this.Constants = lang.mixin(lang.mixin({}, BaseConstants), options.Constants);
			this._con = options.con || con; //NameSpaces
		},
		
		_notifyError: function(args, error){
			if (args.handle) {
				try {
					args.handle(error);
				} catch (ex) {
					//TODO log an error
				}
			}
			if (args.error) {
				try {
					args.error(error);
				} catch (ex) {
					//TODO log an error
				}
			}
		},
		
		_notifyResponse: function(args, response){
			if (args.load || args.handle) {
				if (args.handle) {
					try {
						args.handle(response);
					} catch (ex) {
						//TODO log an error
					}
				}
				if (args.load) {
					try {
						args.load(response);
					} catch (ex) {
						//TODO log an error
					}
				}
			}
		},
		
		_getOne: function(args,getArgs) {
			var entity = getArgs.entity.apply(null, [this, args.id]);
			if(args.loadIt == false){
				this._notifyResponse(args,entity);
			}else{
				this._load(entity,args,getArgs);
			}
			return entity;
		},
		
		_load: function (entity,args,getArgs) {
			if(!(entity._id)){
				this._notifyError(args,{code:this.Constants.sbtErrorCodes.badRequest,message:this.Constants.sbtErrorMessages["null_"+entity._entityName+"Id"]});
				return;
			}
			var loadCb = args.load;
			var handleCb = args.handle;
			var _self = this;
			if(requests[entity._id]) {
				// If there is a pending request for this id, then we simply add this callback to it
				if(loadCb){
					requests[entity._id].push(loadCb);
				}
				if(handleCb){
					requests[entity._id].push(handleCb);
				}
			} else {
				if(loadCb) {
					requests[entity._id] = [loadCb];
				}
				if(handleCb && requests[entity._id]) {
					requests[entity._id].push(handleCb);
				}else if (handleCb){
					requests[entity._id] = [handleCb];
				}
				this._endpoint.xhrGet({
					serviceUrl:	this._constructServiceUrl(getArgs),
					handleAs:	"text",
					content:	getArgs.content,
					load: function(data) {
						entity.setData(xml.parse(data));					      		
						notifyCb(entity._id,entity);
					},
					error: function(error){
						_self._notifyError(args, error);
						
					}
				});
			}
		},
		
		getIdFromIdOrObject: function(inputEntity){
			return entityId = (!(typeof inputEntity == "object")) ? inputEntity._id : inputEntity;
		},
		
		_constructXMLPayload: function(xmlPayload,data){
			return xmlPayload.replace(/{(\w*)}/g,function(m,key){return data.hasOwnProperty(key)?xml.encodeXmlEntry(data[key]):"";});
		},
		
		_createRequestObject: function(args, requestArgs){
			var headers = {"Content-Type" : "application/atom+xml"};
			var _self = this;
			if (args.parameters) {
				requestArgs.urlParams = {};
				lang.mixin(requestArgs.urlParams, args.parameters);
			}
			return requestObject = {
					serviceUrl:_self._constructServiceUrl(requestArgs),	
					headers:headers,
					error: function(error){
						_self._notifyError(args, error);
					}
				};
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
		createEntity: function (args, postArgs) {
			var _self = this;
			var requestObject = lang.mixin({
				postData:_self._constructXMLPayload(postArgs.xmlPayloadTemplate, postArgs.xmlData),
				load:function(data, ioArgs){
					if(args && args.loadIt != false){
						var newEntityUrl = ioArgs.xhr.getResponseHeader("Location");					
						var entityId = postArgs.parseId ? postArgs.parseId(newEntityUrl) : newEntityUrl;					
						var newEntityObj = postArgs.entity.apply(null, [_self, entityId]);
						_self._load(newEntityObj, args);
					}else{
						_self._notifyResponse(args);
					}
				},
			}, _self._createRequestObject(args, postArgs));
			this._endpoint.xhrPost(requestObject);
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
		updateEntity: function (args, putArgs) {
			if(!(putArgs.entity._id)){
				this._notifyError(args,{code:this.Constants.sbtErrorCodes.badRequest,message:this.Constants.sbtErrorMessages["null_"+putArgs.entityName+"Id"]});
				return;
			}
			var _self = this;
			var entity = putArgs.entity;
			var requestObject = lang.mixin({
				putData:_self._constructXMLPayload(putArgs.xmlPayloadTemplate, putArgs.xmlData),
				load:function(data){
					entity.data = xml.parse(data);
					_self._notifyResponse(args,entity);
				}
			}, _self._createRequestObject(args, putArgs));
			this._endpoint.xhrPut(requestObject);
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
		deleteEntity: function (args, deleteArgs) {
			var _self = this;
			var requestObject = lang.mixin({
				load:function(data){
					_self._notifyResponse(args);
				}
			}, _self._createRequestObject(args, deleteArgs));
			
			this._endpoint.xhrDelete(requestObject);
		},
		
		_getAuthType: function(){
			var authType = "";
			//SmartCloud doesn't support /oauth in urls until LotusLive is updated to a version >= 4
			if (this._endpoint.proxyPath == "smartcloud") 
				return authType;
			var endpointAuthType = this._endpoint.authType;
			switch (endpointAuthType) {
				case "basic":
					break;
				case "oauth":
					authType = "/oauth";
					break;
				default:
					break;
			}
			return authType;
		},
		
		_constructServiceUrl: function (requestArgs){
			var authType = this._getAuthType();
			var url = this.Constants.entityServiceBaseUrl + authType + this.Constants.serviceEntity[requestArgs.serviceEntity] + this.Constants.entityType[requestArgs.entityType];
			
			if (requestArgs.urlParams) {
				url += "?";
				var additionalElement = false;
				for (param in requestArgs.urlParams) {
					if (additionalElement) {
						url+= "&";
					}
					else {
						additionalElement = true;
					}
					url += param + requestArgs.urlParams[param];
				}
			}
			
			return url;
		},
		
		_constructQueryObj: function (args, requestArgs){
			var params = args.parameters;
			if(requestArgs.content){				
				params = lang.mixin(params, requestArgs.content);
			}
			return params;
		},
		
		_getEntities: function (args,getArgs) {
			var _self=this;
			var xpath_map = _self.Constants["xpath_feed_"+getArgs.entityName];
			var requestObject = lang.mixin({
					content:_self._constructQueryObj(args,getArgs),
					load:function(data){
						var entities = [];
						var entry = xpath.selectNodes(xml.parse(data),xpath_map["entry"],_self._con.namespaces);
						for(var count=0; count < entry.length; count++){
							var node = entry[count];
							var entityId = xpath.selectText(node,xpath_map["id"],_self._con.namespaces);
							entityId = getArgs.parseId ? getArgs.parseId(entityId) : entityId;
							var entity = getArgs.entity.apply(null, [_self, entityId]);
							entity.setData(node);
							entities.push(entity);
						}
						_self._notifyResponse(args, entities);
					}
				}, _self._createRequestObject(args, getArgs));
			this._endpoint.xhrGet(requestObject);
		},
		
		_isEmail: function(id) {
			return id && id.indexOf('@')>=0;
		},
	});
	return BaseService;
});
