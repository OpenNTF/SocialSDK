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
 * Social Business Toolkit SDK - JS Validation Utilities
 * @module sbt.validate
 */
define([ "./log", "./stringUtil", "sbt/i18n!sbt/nls/validate","./util" ],
		function(log, stringUtil, nls, util) {
			var errorCode = 400;
			
			return {
				
				/**
				 * Validates Input to be not null and of expected Type
				 * @param {String} [className] class which called this utility
				 * @param {String} [methodName] method which called this utility
				 * @param {String} [fieldName] Name of Field which is being validated
				 * @param {Object} [object] object to be validated
				 * @param {String} [expectedType] expected type of the object
				 * @param {Object} [args] Arguments containing callbacks
				 * @param {Function} [args.error] The error parameter is a callback function that is only invoked when an error occurs. This allows to write
				 * logic when an error occurs. The parameter passed to the error function is a JavaScript Error object indicating what the failure was. From the
				 * error object. one can access the javascript library error object, the status code and the error message.
				 * @param {Function} [args.handle = null] This callback function is called regardless of whether the call to update the file completes or fails.
				 * The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to the javascript
				 * library error object, the status code and the error message.
				 * @static
				 * @method _validateInputTypeAndNotify
				 */
				_validateInputTypeAndNotify : function(className, methodName, fieldName, object, expectedType, args) {
					if (!object || (typeof object == "object" && object.declaredClass && object.declaredClass != expectedType)
							|| (typeof object == "object" && !object.declaredClass && typeof object != expectedType)
							|| (typeof object != "object" && typeof object != expectedType)) {
						var message;
						if (!object) {
							message = stringUtil.substitute(nls.validate_nullObject, [ className, methodName,
									fieldName, expectedType ]);
						} else {
							var actualType;
							if (typeof object == "object" && object.declaredClass) {
								actualType = object.declaredClass;
							} else {
								actualType = typeof object;
							}
							message = stringUtil.substitute(nls.validate_expectedType, [ className, methodName,
									actualType, expectedType, fieldName ]);
						}
						util.notifyError({
							code : errorCode,
							message : message
						}, args);
						return false;
					}
					return true;

				},
				/**
				 * Validates Input to be not null and of expected Type
				 * @param {String} [className] class which called this utility
				 * @param {String} [methodName] method which called this utility
				 * @param {Object} [fieldNames] List of Names of Fields which are being validated
				 * @param {Object} [objects] List of objects to be validated
				 * @param {Object} [expectedTypes] List of expected types of the objects
				 * @param {Object} [args] Arguments containing callbacks
				 * @param {Function} [args.error] The error parameter is a callback function that is only invoked when an error occurs. This allows to write
				 * logic when an error occurs. The parameter passed to the error function is a JavaScript Error object indicating what the failure was. From the
				 * error object. one can access the javascript library error object, the status code and the error message.
				 * @param {Function} [args.handle = null] This callback function is called regardless of whether the call to update the file completes or fails.
				 * The parameter passed to this callback is the FileEntry object (or error object). From the error object. one can get access to the javascript
				 * library error object, the status code and the error message.
				 * @static
				 * @method _validateInputTypeAndNotify
				 */
				_validateInputTypesAndNotify : function(className, methodName, fieldNames, objects, expectedTypes, args) {
					for ( var counter in objects) {
						var object = objects[counter];
						var expectedType = expectedTypes[counter];
						var fieldName = fieldNames[counter];
						if (!(this._validateInputTypeAndNotify(className, methodName, fieldName, object, expectedType, args))) {
							return false;
						}
					}
					return true;

				},
			};
		});