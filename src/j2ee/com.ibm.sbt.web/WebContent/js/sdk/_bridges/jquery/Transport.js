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
 * Social Business Toolkit SDK. 
 * 
 * Implementation of a XML HTTP Request using the JQuery API.
 */
define([ 'dojo/_base/declare' ], function(declare) {
	return declare("sbt/base/Transport", null, {
		xhr: function(method, args, hasBody) {
		    var url = args.url;
		    var settings = {
		        type: method,
		        data: args.content,
		        dataType: args.handleAs,
		        success: function(data, textStatus, jqXHR) {
		            handleSuccess(args, data, textStatus, jqXHR);
		        },
		        error: function(jqXHR, textStatus, errorThrown)  {
		            handleError(args, jqXHR, textStatus, errorThrown);
		        }
		    };
		    
		    jQuery.ajax(url, settings);
		},
		handleSuccess: function(args, data, textStatus, jqXHR) {
		    if (args.handle) {
		        
		    }
		    if (args.load) {
		        
		    }
		},
		handleError: function(args, jqXHR, textStatus, errorThrown) {
            if (args.handle) {
                
            }
            if (args.error) {
                
            }
		}
	});
});