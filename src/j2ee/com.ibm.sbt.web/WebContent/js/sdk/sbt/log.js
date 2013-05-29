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
 * Social Business Toolkit SDK - Logging Utilities
 * @module sbt.log
 */
define(['./stringUtil'], function(stringUtil) {

	var loggingEnabled = function isLoggingEnabled(){
		return sbt.Properties["js.logging.enabled"] ? sbt.Properties["js.logging.enabled"].toLowerCase() == "true" : true;
	};

	var Level = {
		DEBUG : 1,
		INFO : 2,
		WARN : 3,
		ERROR : 4
	};
	
	var loggingLevel = function getLoggingLevel(){
		 return Level[sbt.Properties["js.logging.level"] ? sbt.Properties["js.logging.level"] : "DEBUG"];
	};

	return {
		/**
		 * Sets the logging level.
		 * @param {String} [l ]logging level. Possible values are DEBUG, INFO, WARN, ERROR
		 * @static
		 * @method setLevel
		 */
		setLevel : function(l) {
			loggingLevel = Level[l];
		},
		/**
		 * Enables/disables logging.
		 * @param {Boolean} [enabled] logging enabled true or false
		 * @static
		 * @method setEnabled
		 */
		setEnabled : function(enabled) {
			loggingEnabled = enabled;
		},
		/**
		 * log a debug statement.
		 * @static
		 * @method debug
		 */
		debug : function() {
			if (!loggingEnabled) {
				return;
			}
			if (loggingLevel > 1) {
				return;
			}
			if (console && arguments.length > 0) {
				var args = Array.prototype.slice.call(arguments);
				var text = args[0];
				args = args.slice(1);
				var formattedText = stringUtil.substitute(text, args);
				if (console.debug) {
					console.debug(formattedText);
				} else {
					console.log("DEBUG : " + formattedText);
				}
			}
		},
		/**
		 * log an info statement.
		 * @static
		 * @method info
		 */
		info : function() {
			if (!loggingEnabled) {
				return;
			}
			if (loggingLevel > 2) {
				return;
			}
			if (console && arguments.length > 0) {
				var args = Array.prototype.slice.call(arguments);
				var text = args[0];
				args = args.slice(1);
				var formattedText = stringUtil.substitute(text, args);
				if (console.info) {
					console.info(formattedText);
				} else {
					console.log("INFO : " + formattedText);
				}
			}
		},
		/**
		 * log a warning statement.
		 * @static
		 * @method warn
		 */
		warn : function() {
			if (!loggingEnabled) {
				return;
			}
			if (loggingLevel > 3) {
				return;
			}
			if (console && arguments.length > 0) {
				var args = Array.prototype.slice.call(arguments);
				var text = args[0];
				args = args.slice(1);
				var formattedText = stringUtil.substitute(text, args);
				if (console.warn) {
					console.warn(formattedText);
				} else {
					console.log("WARN : " + formattedText);
				}
			}
		},
		/**
		 * log an error statement.
		 * @static
		 * @method error
		 */
		error : function() {
			if (!loggingEnabled) {
				return;
			}
			if (console && arguments.length > 0) {
				var args = Array.prototype.slice.call(arguments);
				var text = args[0];
				args = args.slice(1);
				var formattedText = stringUtil.substitute(text, args);
				if (console.error) {
					console.error(formattedText);
				} else {
					console.log("ERROR : " + formattedText);
				}
			}
		},
		/**
		 * log an exception
		 * @static
		 * @method error
		 */
		exception : function() {
			if (!loggingEnabled) {
				return;
			}
			if (console && arguments.length > 0) {
				var args = Array.prototype.slice.call(arguments);
				var text = args[0];				
				args = args.slice(1);
				var formattedText = stringUtil.substitute(text, args);
				if (console.error) {
					console.error("EXCEPTION : " + formattedText);
				} else {
					console.log("EXCEPTION : " + formattedText);
				}
			}
		}
	};
});