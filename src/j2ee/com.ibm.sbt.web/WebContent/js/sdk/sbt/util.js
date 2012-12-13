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
define([],function() {
	var errorCode = 400;	
	function log (text, arg1, arg2) {
		if (console) {	
			if(arg1 && arg2){
				console.log(text,arg1, arg2);
			}else if (arg1){
				console.log(text,arg1);
			}else {
				console.log(text);
			}				
		}
	}	
	function _notifyError(error, args){	
		if(args.error || args.handle){
			if(args.error){
				try{
					args.error(error);
				}catch(error){
					log("Error running error callback.");
				}
			}
			if(args.handle){
				try{
					args.handle(error);
				}catch(error){
					log("Error running handle callback.");
				}
			}
		}
		else{
			log("Error received. Error Code = %d. Error Message = %s" , error.code, error.message);
		}
	}	
	return {
		notifyError: _notifyError,		
		log: log,		
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
		}		
	};
});