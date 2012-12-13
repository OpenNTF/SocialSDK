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
 * Social Business Toolkit SDK.
 *  
 * Helpers for accessing the Connections files services
 */
define(['sbt/_bridge/declare','sbt/config','sbt/lang','sbt/smartcloud/core','sbt/Cache','sbt/smartcloud/Subscriber','sbt/Endpoint','sbt/xml','sbt/xpath'],
		function(declare,cfg,lang,con,Cache,Subscriber, Endpoint, Xml, xpath) {

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
	
	var API = {
		"ServiceUrl"			: "/files/basic/cmis/repository/p!",
		"WithMe"				: "/folderc/snx:virtual!.!filessharedwith",
		"My"					: "/folderc/snx:files"
	};
	
	var File = declare("sbt.smartcloud.File", null, {
		_service:	null,
		_id:		"",
		data:		null,
		fields:		{},		// Updated fields 
		
		constructor: function(svc,id) {
			this._service = svc;
			this._id = id;
		},
		load: function(callback) {
			if(!data) {
				data = this._service._load(this,callback);
			}
		},
		get: function(fieldName){
			
		},
		getMyFiles: function() {
			var files =  xpath.selectNodes(this.data, "/a:feed/a:entry/a:title", sbt.smartcloud.namespaces);
			for(var i = 0; i < files.length; i++) {
				files[i] = files[i].text || files[i].textContent;
			}
			return files;
		},
		getFilesSharedWithMe: function() {
			//TODO implementation
		}
	});

	var FileService = declare("sbt.smartcloud.FileService", null, {
		_files: null,
		_api:null,
		
		constructor: function(options) {
			options = options || {};
			_endpoint = sbt.Endpoints[options.endpoint||'smartcloud'];
			var cs = options.cacheSize;
			if(cs && cs>0) {
				this._files = new Cache(cs);
			}
			if(options.tag)
			{
				this._api = API[options.tag];
			}
		},
		
		getFile: function(id,cb,options) {
			return lang.isArray(id) ? this._getMultiple(id,cb,options) : this._getOne(id,cb,options);
		},
		/**
		 * Get a file object for a single entry.
		 * @param id
		 * @param cb
		 * @param options
		 * @returns {file}
		 */
		_getOne: function(id,cb,options) {
			var p = new File(this,id);
			if(cb) {
				this._load(p,cb,options);
			}
			return p;
		},
		/**
		 * Get an array of file objects based on an array of Ids.
		 * @param ids
		 * @param cb
		 * @param options
		 * @returns {Array}
		 */
		/*
		_getMultiple: function(ids,cb,options) {
			// For now. Should later use a single call for multiple entries
			var a = [];
			for(var i=0; i<ids.length; i++) {
				a[i] = this._getOne(ids[i],cb,options);
			}
			return a;
		},*/
		/**
		 * Load the JSON data for a file object and call the callback when done
		 * @param p
		 * @param cb
		 */
		_load: function (p,cb,options) {
			var _self = this;
			var x = this._find(p._id);
			if(x) {// the cached item is found. the call back function is called.
				p.data = x;
				if(cb) cb(p);
			} else {// a network call is made to fetch the file object and the cache is populated.
				if(requests[p._id]) {
					// If there is a pending request for this id, then we simply add this callback to it
					requests[p._id].push(cb);
				} else {
					if(cb) {
						requests[p._id] = [cb];
					}
					var content = {format:	"text"};

					// here we first get the user's subscriber id from smartcloud using the Subscriber Helper.
					var subscriberId = null;
					var subscriber = new Subscriber(_endpoint);
					subscriber.load(function(subscriber,response){
						if(subscriber) {
							subscriberId = subscriber.getSubscriberId(response);
						}
						var url = API.ServiceUrl + subscriberId + _self._api;
						_endpoint.xhrGet({ 
						serviceUrl:	url,
						handleAs:	"text",
						content:	content,
						load: function(response) {
							
				      		p.data = Xml.parse(response);
				      		//alert(p.data.toSource());
				      		if(_self._files) {
				      			_self._files.put(p._id,p.data);
				      		}
							notifyCb(p._id,p);
						},
						error: function(error){
							notifyCb(p._id,null);
						}
					});
				});
			}
		}
	},
		/**
		 * Find the cached data for an id or an email.
		 * @param id
		 * @returns
		 */
		_find: function(id) {
			// Case of an email: find it in the cache
			if(this._isEmail(id)) {
				if(this._files) {
					var _self = this;
					return this._files.browse(function(k,v) {
						
						if(k==id) {
							return _self._files.get(k);
						}
					});
				}
				return null;
			}
			// Else, assume it is a user id
			return this._files.get(id);
		},
		/**
		 * Check if the address is an actual email.
		 * @param id
		 * @returns {Boolean}
		 */
		_isEmail: function(id) {
			return id && id.indexOf('@')>=0;
		}
	});
	
	return FileService;
});
