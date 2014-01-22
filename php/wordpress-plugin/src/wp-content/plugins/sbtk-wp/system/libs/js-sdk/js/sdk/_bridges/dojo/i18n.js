/*
 * © Copyright IBM Corp. 2013
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
 */
define([],function() {
    dojo.require("dojo.i18n");
    dojo.require("dojo.date.locale");
    var i18n = dojo.i18n;
    var dateLocale = dojo.date.locale;
    
    var load = function(id, require, callback){         
        i18n.load(id, require, callback);
    };
    
    return {
        load : load,
        
        getLocalizedTime: function(date) {
            return dateLocale.format(date, {selector:"time", formatLength:"short"});
        },
            
        getLocalizedDate: function(date) {
            return dateLocale.format(date, {selector:"date", formatLength:"medium"});
        }
    }; 
});


