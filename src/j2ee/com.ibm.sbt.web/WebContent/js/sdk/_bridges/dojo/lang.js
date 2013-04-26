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
 * Social Business Toolkit SDK - Some language utilities.
 */
define([],function() {
	return {
		mixin: function(dest,sources) {
			return dojo.mixin.apply(this, arguments);
		},
		isArray: function(o) {
			return dojo.isArray(o);
		},
		isString: function(o) {
			return dojo.isString(o);
		},
		isFunction: function(o) {
            return typeof o == 'function';
        },
        isObject: function(o) {
            return typeof o == 'object';
        },
        clone: function(o) {
			return dojo.clone(o);
		},
        concatPath: function () {
        	var a = arguments;
        	if(a.length==1 && this.isArray(a[0])) {
        		a = a[0];
        	}
        	var s = "";
        	for(var i=0; i<a.length; i++) {
        		var o = a[i].toString();
        		if(s) {
        			s = s + "/";
        		}
        		s = s + (o.charAt(0)=='/'?o.substring(1):o);
        	}
        	return s;
        },
        trim: function(str) {
            return dojo.trim(str);
        }
	};
});