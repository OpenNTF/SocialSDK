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
 * @module i18n
 */
define(['dojo/i18n', 'dojo/date/locale'],function(i18n, dateLocale) {
	    var load = function(id, require, callback){	    	
	    	i18n.load(id, require, callback); 
	    };
	    
	    var nls = {
	            todayAt : "today at ",
	            on : "on "
	    };
	    return {
	    	load : load,
	    	
	    	getUpdatedLabel: function(dateStr) {
	            var date = new Date(dateStr);
	            var dateClone = new Date(date.getTime());
	            var now = new Date();
	            if (dateClone.setHours(0,0,0,0) == now.setHours(0,0,0,0)) {
	                return nls.todayAt + this.getLocalizedTime(date);
	            } else {
	                return nls.on + this.getLocalizedDate(date);
	            }
	        },
	        
	        getSearchUpdatedLabel: function(dateStr) {
	            var date = new Date(dateStr);
	            var dateClone = new Date(date.getTime());
	            var now = new Date();
	            if (dateClone.setHours(0,0,0,0) == now.setHours(0,0,0,0)) {
	                return nls.todayAt + this.getLocalizedTime(date);
	            } else {
	                return this.getLocalizedDate(date);
	            }
	        },
	        
	        getLocalizedTime: function(date) {
	            return dateLocale.format(date, { selector:"time",formatLength:"short" });
	        },
	            
	        getLocalizedDate: function(date) {
	            return dateLocale.format(date, { selector:"date",formatLength:"medium" });
	        }
	    }; 
});


