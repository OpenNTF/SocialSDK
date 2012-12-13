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
 * Social Business Toolkit SDK - Utilities script
 */
define(["sbt/log"], function(log) {
	var errorCode = 400;	
	function _notifyError(error, args) {
		if (args.error || args.handle) {
			if (args.error) {
				try {
					args.error(error);
				} catch (error) {
					log.error("Error running error callback.");
				}
			}
			if (args.handle) {
				try {
					args.handle(error);
				} catch (error) {
					log.error("Error running handle callback.");
				}
			}
		} else {
			log.error("Error received. Error Code = %d. Error Message = %s", error.code, error.message);
		}
	}
	return {
		notifyError : _notifyError,
		_validateInputTypeAndNotify : function(objects, classNames, args) {
			for ( var counter in objects) {
				var object = objects[counter];
				var className = classNames[counter];
				var message = "Invalid Input for " + className;
				if (object.declaredClass != className) {
					if (args) {
						_notifyError({
							code : errorCode,
							message : message
						}, args);
					} else {
						log.error(message);
					}
					return false;
				}
			}
			return true;

		},
		_validateEmptyInputAndNotify : function(className, methodName, parametersMap, args) {
			for ( var argumentName in parametersMap) {
				var argumentValue = parametersMap[argumentName];
				var message = className + " " + methodName + " Argument " + argumentName + ", passed was null";
				if (!argumentValue) {
					if (args) {
						_notifyError({
							code : errorCode,
							message : message
						}, args);
					} else {
						log.error(message);
					}
					return false;
				}
			}
			return true;

		}
	};
});