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
define(["../stringUtil", "../config"], function(stringUtil, config) {
    var Formatter = {
        defaultFormat: function(param, val) {
            return param.key + "=" + val;
        },
        sortField: function(vals) {
            return function(param, val) {
                var v = vals[val] || "";
                return param.key + "=" + v;
            };
        },
        
        ascSortOrderBoolean: function(param, val) {
            var v = (val === "asc") ? true : false;
            return param.key + "=" + v;
        },
        
        sortOrder: function(param,val){
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

    };
    
    
    var Parameter = function(args) {
        var self = this;
        this.key = args.key;
        var formatFunc = args.format || Formatter.defaultFormat;
        this.format = function(val) {
            return formatFunc(self, val);
        };
    };
    
   
    return {
    	 defaultFormat: function (key){
			 return new Parameter({key: key, format: Formatter.defaultFormat}); 
		 },
		 
		 sortField: function(key, sortVals){
			return new Parameter({key: key, format: Formatter.sortField(sortVals)}); 
		 },
		 
		 sortOrder: function(key){
			 return new Parameter({key: key, format: Formatter.sortOrder});
		 },
		 
		 booleanSortOrder: function (key){
			 return new Parameter({key: key, format: Formatter.ascSortOrderBoolean});
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