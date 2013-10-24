/*
 * © Copyright IBM Corp. 2012, 2013
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
 * 
 * @module sbt.connections.Tag
 * @author Rajmeet Bal
 */
define(["../declare", "../base/BaseEntity" ], 
    function(declare, BaseEntity) {

	 /**
     * Tag class.
     * 
     * @class Tag
     * @namespace sbt.connections
     */
    var Tag = declare(BaseEntity, {

        /**
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {            
        },

        /**
         * Get term of the tag
         * 
         * @method getTerm
         * @return {String} term of the tag
         * 
         */
        getTerm : function() {
            return this.getAsString("term");
        },
        
        /**
         * Get frequency of the tag
         * 
         * @method getFrequency
         * @return {Number} frequency of the tag
         * 
         */
        getFrequency : function() {
            return this.getAsNumber("frequency");
        }
    });
    return Tag;
});
