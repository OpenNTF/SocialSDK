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
 * @module sbt.util
 */
define(['./lang','sbt/i18n!sbt/nls/util','./log'],function(lang, nls, log) {
	var errorCode = 400;	
	function _notifyError(error, args){	
		if (args && (args.error || args.handle)) {
			if (args.error) {
				try {
					args.error(error);
				} catch (error) {
					log.error(nls.notifyError_catchError, error);
				}
			}
			if (args.handle) {
				try {
					args.handle(error);
				} catch (error) {
					log.error(nls.notifyError_catchError, error);
				}
			}
		} else {
			log.error(nls.notifyError_console, error.code, error.message);
		}
	}	
	return {
		notifyError: _notifyError,			
		checkObjectClass: function(object, className, message, args){
			if(object.declaredClass != className){
				if(args){
					_notifyError({code:errorCode,message:message},args);
				}else{
					log(message);
				}
				return false;
			}else{
				return true;
			}
		},
		checkNullValue: function(object, message, args){
			if(!object){
				if(args){
					_notifyError({code:errorCode,message:message},args);
				}else{
					log(message);
				}
				return false;
			}else{
				return true;
			}
		},
		minVersion: function(required, used) {
		    var reqParts = required.split('.');
		    var usedParts = used.split('.');
		    
		    for (var i = 0; i < reqParts.length; ++i) {
		        if (usedParts.length == i) {
		            return false;
		        }
		        
		        if (reqParts[i] == usedParts[i]) {
		            continue;
		        }
		        else if (reqParts[i] > usedParts[i]) {
		            return false;
		        }
		        else {
		            return true;
		        }
		    }
		    
		    if (reqParts.length != usedParts.length) {
		        return true;
		    }
		    
		    return true;
		},
        getAllResponseHeaders: function(xhr) {
            var headers = {};
            try {
                var headersStr = xhr.getAllResponseHeaders();
                if (headersStr) {
                    var headersStrs = headersStr.split('\n');
                    for (var i=0; i<headersStrs.length; i++) {
                        var index = headersStrs[i].indexOf(':');
                        var key = lang.trim(headersStrs[i].substring(0, index));
                        var value = lang.trim(headersStrs[i].substring(index+1));
                        if (key.length > 0) {
                            headers[key] = value;
                        }
                    }
                }
            } catch(ex) {
                console.log(ex);
            }
            return headers;
        },
        
        /**
         * Takes an object mapping query names to values, and formats them into a query string separated by the delimiter.
         * e.g.
         * createQuery({height: 100, width: 200}, ",")
         * 
         * returns "height=100,width=200"
         * 
         * @method createQuery
         * 
         * @param {Object} queryMap An object mapping query names to values, e.g. {height:100,width:200...}
         * @param {String} delimiter The string to delimit the queries
         */
		createQuery: function(queryMap, delimiter){
	        if(!queryMap){
	            return null;
	        }
	        var delim = delimiter;
	        if(!delim){
	            delim = ",";
	        }
	        var pairs = [];
	        for(var name in queryMap){
	            var value = queryMap[name];
	            pairs.push(encodeURIComponent(name) + "=" + encodeURIComponent(value));
	        }
	        return pairs.join(delim);
	    }
	};
});