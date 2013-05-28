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
 * JavaScript API for IBM Connections Profile Service.
 * 
 * @module sbt.connections.ProfileService
 */
define([ "../declare", "../lang", "../config", "../stringUtil", "./XmlDataHandler" ], 
        function(declare,lang,config,stringUtil,XmlDataHandler) {

    /**
     * VCardDataHandler class.
     * 
     * @class ProfileDataHandler
     * @namespace sbt.connections
     */
    var VCardDataHandler = declare(XmlDataHandler, {
        
        lineDelim : "\n",
        itemDelim : ":",
        
        _vcard : null,
        
        /**
         * @constructor
         * @param {Object}
         *            args Arguments for this data handler.
         */
        constructor : function(args) {
            this.parseVCard();
        },
        
        /**
         * Parse the vcard data from the specified element.
         * 
         * @method parseVCard 
         */
        parseVCard : function() {
            var content = stringUtil.trim(this.getAsString("vcard"));
            var lines = content.split(this.lineDelim);
            this._vcard = {};
            for (var i=1; i<lines.length-1; i++) {
                var line = stringUtil.trim(lines[i]);
                var index = line.indexOf(this.itemDelim);
                var key = line.substring(0, index);
                var value = line.substring(index+1);
                this._vcard[key] = value;
            }
        },
        
        /*
         * Override this method to handle VCard properties.
         */
        _selectText : function(property) {
            var xpath = this._getXPath(property);
            if (this._vcard && this._vcard.hasOwnProperty(xpath)) {
                return this._vcard[xpath];
            } else {
                try {
                    return this.inherited(arguments, [ property ]);
                } catch (error) {
                    // vcard expressions may cause an error
                    // if they are treated as xpath expressions
                    return null;
                }
            }
        }
        
    });
    return VCardDataHandler;
});