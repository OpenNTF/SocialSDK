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
 * Defination of some string Utilities
 * 
 * @module sbt.stringutil
 */

/**
 * Implements String helpers.
 * 
 * @class stringutil
 * @static
 */
define(['sbt/xml'], function(xml) {

	var _regex = new RegExp("{-?[0-9]+}", "g");

	return {
		/**
		 * Substitutes the String with pattern {<<number>>} with argument array provided. {-1} is for printing '{' and {-2} is for printing '}' in the text
		 * 
		 * @param {String} [str] String to be formatted
		 * @param {Array} [args] arguments Array
		 * @param {Boolean} [useBlankForUndefined = false] optional flag to indicate to user blank String in case index is not found in args. 
		 * @static
		 * @method substitute
		 */
		substitute : function(str, args, useBlankForUndefined) {
			if (str && args) {
				return str.replace(_regex, function(item) {
					var intVal = parseInt(item.substring(1, item.length - 1));
					var replace;
					if (intVal >= 0) {
						replace = args[intVal] ? args[intVal] : useBlankForUndefined ? "" : "undefined";
					} else if (intVal == -1) {
						replace = "{";
					} else if (intVal == -2) {
						replace = "}";
					} else {
						replace = "";
					}
					return replace;
				});
			}
			return str;
		},
		/**
		 * Replaces the String with pattern {<<string>>} with argument map provided. Replaces blank if key to be replaces is not found in argsMap.
		 * 
		 * @param {String} [str] String to be formatted
		 * @param {Array} [argsMap] arguments Map		 
		 * @static
		 * @method replace
		 */
		replace : function(str, argsMap) {
			if (str && argsMap) {
				return str.replace(/{(\w*)}/g, function(m, key) {					
					var replace;
					replace = argsMap.hasOwnProperty(key) ? xml.encodeXmlEntry(argsMap[key]) : "";
					return replace;
				});
			}
			return str;
		}
	};
});