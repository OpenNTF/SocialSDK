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
 * ConnEntity class represents an entry from an IBM Connections feed.
 * 
 * @module sbt.base.ConnEntity
 */
define([ "../declare", "../lang", "../stringUtil", "./AtomEntity" ], 
    function(declare,lang,stringUtil,AtomEntity,XmlDataHandler) {

    /**
     * ConnEntity class represents an entry from an IBM Connections feed.
     * 
     * @class ConnEntity
     * @namespace sbt.base
     */
    var ConnEntity = declare(BaseEntity, {
    	
    	entity : null,
    	
        /**
         * Construct an ConnEntity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        	entity = new AtomEntity(args);
        	
        	lang.mixin(this, entity);
        }

    });
    
    return ConnEntity;
});
