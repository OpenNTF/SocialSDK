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
 * JQuery implementation of the JSON methods.
 */
define(['./jquery'],function() {
    return {
        /**
         * Parses a String of JSON and returns a JSON Object.
         * @param {String} jsonString A String of JSON.
         * @returns {Object} A JSON Object.
         * @static
         */
        parse : function(jsonString) {
            return jQuery.parseJSON(jsonString);
        },
        
        /**
         * Returns the JSON object represented as a String.
         * @param {Object} jsonObj A JSON Object.
         * @returns {String} The JSON Object represented as a String.
         */
        stringify : function(jsonObj) {
        	var result;
        	if (typeof JSON != "undefined") {
        		result = JSON.stringify(jsonObj);
        	} else {
        		var toJsonIndentStr = "\t";
				result = _stringify(jsonObj, function(key, value){
					if(value){
						var tf = value.__json__||value.json;
						if(typeof tf == "function"){
							return tf.call(value);
						}
					}
					return value;
				}, toJsonIndentStr);
        	}
            return result;
        },
        
		_stringify: function(value, replacer, spacer){
			var undef;
			if(typeof replacer == "string"){
				spacer = replacer;
				replacer = null;
			};
			function escapeString(/*String*/str){
				// summary:
				//		Adds escape sequences for non-visual characters, double quote and
				//		backslash and surrounds with double quotes to form a valid string
				//		literal.
				return ('"' + str.replace(/(["\\])/g, '\\$1') + '"').
					replace(/[\f]/g, "\\f").replace(/[\b]/g, "\\b").replace(/[\n]/g, "\\n").
					replace(/[\t]/g, "\\t").replace(/[\r]/g, "\\r"); // string
			};
			function __stringify(it, indent, key){
				if(replacer){
					it = replacer(key, it);
				}
				var val, objtype = typeof it;
				if(objtype == "number"){
					return isFinite(it) ? it + "" : "null";
				}
				if(objtype == "boolean"){
					return it + "";
				}
				if(it === null){
					return "null";
				}
				if(typeof it == "string"){
					return escapeString(it);
				}
				if(objtype == "function" || objtype == "undefined"){
					return undef; // undefined
				}
				// short-circuit for objects that support "json" serialization
				// if they return "self" then just pass-through...
				if(typeof it.toJSON == "function"){
					return __stringify(it.toJSON(key), indent, key);
				}
				if(it instanceof Date){
					return '"{FullYear}-{Month+}-{Date}T{Hours}:{Minutes}:{Seconds}Z"'.replace(/\{(\w+)(\+)?\}/g, function(t, prop, plus){
						var num = it["getUTC" + prop]() + (plus ? 1 : 0);
						return num < 10 ? "0" + num : num;
					});
				}
				if(it.valueOf() !== it){
					// primitive wrapper, try again unwrapped:
					return __stringify(it.valueOf(), indent, key);
				}
				var nextIndent= spacer ? (indent + spacer) : "";
				/* we used to test for DOM nodes and throw, but FF serializes them as {}, so cross-browser consistency is probably not efficiently attainable */ 
			
				var sep = spacer ? " " : "";
				var newLine = spacer ? "\n" : "";
			
				// array
				if(it instanceof Array){
					var itl = it.length, res = [];
					for(key = 0; key < itl; key++){
						var obj = it[key];
						val = __stringify(obj, nextIndent, key);
						if(typeof val != "string"){
							val = "null";
						}
						res.push(newLine + nextIndent + val);
					}
					return "[" + res.join(",") + newLine + indent + "]";
				}
				// generic object code path
				var output = [];
				for(key in it){
					var keyStr;
					if(it.hasOwnProperty(key)){
						if(typeof key == "number"){
							keyStr = '"' + key + '"';
						}else if(typeof key == "string"){
							keyStr = escapeString(key);
						}else{
							// skip non-string or number keys
							continue;
						}
						val = __stringify(it[key], nextIndent, key);
						if(typeof val != "string"){
							// skip non-serializable values
							continue;
						}
						// At this point, the most non-IE browsers don't get in this branch 
						// (they have native JSON), so push is definitely the way to
						output.push(newLine + nextIndent + keyStr + ":" + sep + val);
					}
				}
				return "{" + output.join(",") + newLine + indent + "}"; // String
			};
			return __stringify(value, "", "");
		}
    };
});