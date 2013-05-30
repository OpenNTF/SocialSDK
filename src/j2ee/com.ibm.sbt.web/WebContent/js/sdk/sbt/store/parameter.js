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
 * 
 */
define(["../stringUtil", "../Endpoint"], function(stringUtil, Endpoint) {
    var Formatter = {
        defaultFormat: function(param, val) {
            return param.key + "=" + val;
        },
        
        ascSortOrderBoolean: function(param, val) {
            var v = (val === "asc") ? true : false;
            return param.key + "=" + v;
        },
        
        fileSortOrder: function(param,val){
        	var v = (val === "asc") ? "desc" : "asc";
            return param.key + "=" + v;
        },
        
        oneBasedInteger: function(param, val) {
            var v = Math.floor(val);
            if(v < 1) {
                v = 1;
            }
            return param.key + "=" + v;
        },
        
        zeroBaseInteger: function(param, val) {
            var v = Math.floor(val);
            if(v < 0) {
                v = 0;
            }
            return param.key + "=" + v;
        },
        
        sortField: function(vals) {
            return function(param, val) {
                var v = vals[val] || "";
                return param.key + "=" + v;
            };
        }
    };
    
    
    var Parameter = function(args) {
        var self = this;
        this.key = args.key;
        var formatFunc = args.format || Formatter.defaultFormat;
        this.format = function(val) {
            return formatFunc(self, val);
        };
    };
    
    // TODO we must use the endpoint being used by the grid
    var endpoint; 
    var isV4OrHigher = true;
    
    var setEndpoint = function(args){
    	endpoint = Endpoint.find(args.endpoint || "connections");
	    if (endpoint.apiVersion) {
	        var parts = endpoint.apiVersion.split(".");
	        isV4OrHigher = !(parts.length > 1 && parts[0] < 4);
	    }
    };
    
    var sortVals = null;
    if (isV4OrHigher) {
	    sortVals = {
	     communities: {
	            all: {
	                date: "modified",
	                popularity: "count",
	                name: "title"
	            }
	        },
	        files:{
	        	all:{
		        	name: "title",
		        	updated: "modified",
		        	downloads: "downloaded",
		        	comments: "commented",
		        	likes: "recommended",
		        	files: "itemCount",
		        	created: "created",
		        	modified: "modified"
	        	}
	        },
	        profiles:{
	        	all:{
	        		displayName: "displayName",
	        		recent: "3" //there is nothing in the API doc for sorting by recent, but connections use this parameter  
	        	}
	        }
	    };
    } else {
	    sortVals = {
   	        communities: {
   	            all: {
   	                date: "lastmod",
   	                popularity: "count",
   	                name: "name"
   	            }
   	        },
   	        files:{
   	        	all:{
   		        	name: "title",
   		        	updated: "modified",
   		        	downloads: "downloaded",
   		        	comments: "commented",
   		        	likes: "recommended",
   		        	files: "itemCount",
   		        	created: "created",
   		        	modified: "modified"
   	        	}
   	        },
   	        profiles:{
   	        	all:{
   	        		displayName: "displayName",
   	        		recent: "3" //there is nothing in the API doc for sorting by recent, but connections use this parameter  
   	        	}
   	        }
   	    };
    }
    
    return {
        communities: {
            all: {
                pageNumber: new Parameter({ key: "page", format: Formatter.oneBasedInteger }),
                pageSize: new Parameter({ key: "ps", format: Formatter.oneBasedInteger }),
                sortBy: new Parameter({ key: "sortField", format: Formatter.sortField(sortVals.communities.all) }),
                sortOrder: new Parameter({ key: "asc", format: Formatter.ascSortOrderBoolean })
            }
        },
        files:{
        	all:{
	        	pageNumber: new Parameter({ key: "page", format: Formatter.oneBasedInteger }),
	            pageSize: new Parameter({ key: "pageSize", format: Formatter.oneBasedInteger }),
	            sortBy: new Parameter({ key: "sortBy", format: Formatter.sortField(sortVals.files.all) }),
	            sortOrder: new Parameter({ key: "sortOrder", format: Formatter.fileSortOrder })
        	}
        },
        profiles:{
        	all:{
	        	pageNumber: new Parameter({ key: "page", format: Formatter.oneBasedInteger }),
	            pageSize: new Parameter({ key: "ps", format: Formatter.oneBasedInteger }),
	            sortBy: new Parameter({ key: "sortBy", format: Formatter.sortField(sortVals.profiles.all) }),
	            sortOrder: new Parameter({ key: "sortOrder", format: Formatter.fileSortOrder })
        	}
        },
        
        /**
         * 
         * @param key
         * @returns
         */
        zeroBasedInteger :  function(key) {
        	return new Parameter({ key: key, format: Formatter.zeroBasedInteger });
        },
        
        /**
         * 
         * @param key
         * @returns
         */
        oneBasedInteger :  function(key) {
        	return new Parameter({ key: key, format: Formatter.oneBasedInteger });
        }
    };
});