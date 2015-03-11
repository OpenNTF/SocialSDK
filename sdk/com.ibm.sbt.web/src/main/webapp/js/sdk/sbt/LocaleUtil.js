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
 * @module sbt.LocaleUtil
 */
define(['./_bridge/LocaleUtil', 'sbt/i18n!sbt/nls/Locale'],function(localeUtil,nls) {
	return{
		
		 nls : nls,
	    
	    getUpdatedLabel : function(dateStr) {
	        var date = new Date(dateStr);
	        var dateClone = new Date(date.getTime());
	        var now = new Date();
	        if (dateClone.setHours(0,0,0,0) == now.setHours(0,0,0,0)) {
	            return this.nls.todayAt + this.getLocalizedTime(date);
	        } else {
	            return this.nls.on + this.getLocalizedDate(date);
	        }
	    },
	        
	    getSearchUpdatedLabel : function(dateStr) {
	        var date = new Date(dateStr);
	        var dateClone = new Date(date.getTime());
	        var now = new Date();
	        if (dateClone.setHours(0,0,0,0) == now.setHours(0,0,0,0)) {
	            return this.nls.todayAt + this.getLocalizedTime(date);
	        } else {
	            return this.getLocalizedDate(date);
	        }
	    }
	};
});


