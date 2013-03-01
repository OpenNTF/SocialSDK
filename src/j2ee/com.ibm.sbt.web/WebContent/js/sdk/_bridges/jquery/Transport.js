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
define(['jquery', 'sbt/_bridge/declare', 'sbt/util' ], function($, declare, util) {
	return declare("sbt._bridge.Transport", null, {
		xhr: function(method, args, hasBody) {
		    var url = args.url;
		    var self = this;
		    var usedJQVersion = $().jquery;
		    var requiredJQVersion = "1.8";
		    var jQ_v_gte_18 = util.minVersion(requiredJQVersion, usedJQVersion);
		    var settings = {
		        type: method,
		        data: args.content,
		        dataType: args.handleAs || "text"
		    };
		    
		    if (!jQ_v_gte_18) {
		    	settings = $.extend(settings, {
		    		success: function(data, textStatus, jqXHR) {
		    			self.handleSuccess(args, data, textStatus, jqXHR);
		    		},
		    		error: function(jqXHR, textStatus, errorThrown)  {
		    			self.handleError(args, jqXHR, textStatus, errorThrown);
		    		}
		    	});
		    }
		    
		    var jqXHR = $.ajax(url, settings);
		    
		    if (jQ_v_gte_18) {
		    	jqXHR.done(function(data, textStatus, jqXHR) {
		            self.handleSuccess(args, data, textStatus, jqXHR);
		        }).fail(function(jqXHR, textStatus, errorThrown)  {
		            self.handleError(args, jqXHR, textStatus, errorThrown);
		        });	
		    }
		},
		handleSuccess: function(args, data, textStatus, jqXHR) {
		    if (args.handle) {
                var _ioArgs = {
                    'args' : args,
                    'headers' : util.getAllResponseHeaders(jqXHR),
                    '_ioargs' : jqXHR
                };
		        args.handle(data, _ioArgs);
		    }
		},
		handleError: function(args, jqXHR, textStatus, errorThrown) {
			var error = this.createError(jqXHR, textStatus, errorThrown);
            if (args.handle) {
		        args.handle(error, args);
            }
		},
		createError: function(jqXHR, textStatus, errorThrown) {
            var _error = new Error();
            _error.code = jqXHR.status || 400;
            _error.message = this.getErrorMessage(jqXHR);
            _error.cause = errorThrown || jqXHR;
            _error.response = jqXHR.getAllResponseHeaders();
            return _error;
        },
        getErrorMessage: function(jqXHR, textStatus) {
            var text = jqXHR.responseText;
            if (!text && (text=jqXHR.responseXML)) {
                try {
                    text = $($($.parseXML(text)).find("message")[0]).text().trim();
                } catch(ex) {
                    console.log(ex);
                }
                return text || textStatus;
            } else {
                return jqXHR;
            }
        }
	});
});