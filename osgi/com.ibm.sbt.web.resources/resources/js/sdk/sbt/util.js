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

dojo.provide('sbt.util');
/**
 * @module sbt.util
 */
define('sbt/util',['sbt/lang','sbt/i18n!sbt/nls/util','sbt/log'],function(lang, nls, log) {
	var errorCode = 400;	
	function _notifyError(error, args){	
		if (args && (args.error || args.handle)) {
			if (args.error) {
				try {
					args.error(error);
				} catch (error1) {
					log.error(nls.notifyError_catchError, error1);
				}
			}
			if (args.handle) {
				try {
					args.handle(error);
				} catch (error2) {
					log.error(nls.notifyError_catchError, error2);
				}
			}
		} else {
			log.error(nls.notifyError_console, error.code, error.message);
		}
	}	
	return {
		notifyError: _notifyError,	
		isEmptyObject: function(obj){
            var isEmpty = true;
            for( var key in obj ){
                if(obj.hasOwnProperty(key)){
                    isEmpty = false;
                    break;
                }
            }
            return isEmpty;
        },
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
	    },
	    
	    /**
         * Takes a query string and returns an equivalent object mapping.
         * e.g.
         * splitQuery("height=100,width=200", ",")
         * 
         * returns {height: 100, width: 200}
         * 
         * @method splitQuery
         * 
         * @param {String} query A query string, e.g. "height=100,width=200"
         * @param {String} delimiter The string which delimits the queries
         */
	    splitQuery: function(query, delimiter){
	        var i;
	        var result = {};
	        var part;
	        var parts;
	        var length;
	        
	        query = query.replace("?", "");
	        parts = query.split(delimiter);
	        length = parts.length;
	        
	        for (i = 0; i < length; i++) {
	            if(!parts[i]){
	                continue;
	            }
                part = parts[i].split('=');
                result[part[0]] = part[1];
            }
	        
	        return result;
	    },
	    
	    /**
	     * Returns the JavaScript Library and version used
	     * @returns {String} JavaScript Library with version
	     */
	    getJavaScriptLibrary : function(){
	    	var jsLib = "Unknown";
	    	if(window.dojo) {
	    		if(dojo.version) {
	    			jsLib = "Dojo "+dojo.version;
	    		}
	    	} else if(define && define.amd && define.amd.vendor && define.amd.vendor === "dojotoolkit.org") {
	    		require(["dojo/_base/kernel"], function(kernel){ 
	    			jsLib = "Dojo AMD "+kernel.version;
	    		});
	    	} else if(window.jQuery) {
	    		jsLib = "JQuery "+jQuery.fn.jquery;
	    	} 
	    	return jsLib;
	    }
	};
});
