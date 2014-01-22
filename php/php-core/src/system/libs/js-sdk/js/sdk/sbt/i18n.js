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
 * @module sbt.i18n
 */
define(['./_bridge/i18n'],function(i18n) {
    var nls = {
        todayAt : "today at ",
        on : "on "
    };
    
    i18n.getUpdatedLabel = function(dateStr) {
        var date = new Date(dateStr);
        var dateClone = new Date(date.getTime());
        var now = new Date();
        if (dateClone.setHours(0,0,0,0) == now.setHours(0,0,0,0)) {
            return nls.todayAt + this.getLocalizedTime(date);
        } else {
            return nls.on + this.getLocalizedDate(date);
        }
    };
        
    i18n.getSearchUpdatedLabel = function(dateStr) {
        var date = new Date(dateStr);
        var dateClone = new Date(date.getTime());
        var now = new Date();
        if (dateClone.setHours(0,0,0,0) == now.setHours(0,0,0,0)) {
            return nls.todayAt + this.getLocalizedTime(date);
        } else {
            return this.getLocalizedDate(date);
        }
    };
    return i18n;
});


