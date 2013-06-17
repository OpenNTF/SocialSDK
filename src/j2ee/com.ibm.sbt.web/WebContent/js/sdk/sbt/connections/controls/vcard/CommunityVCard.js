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
 * 
 */
define(["../../../declare",
        "../../../dom",
        "../../../widget/_TemplatedWidget",
        "../../../lang",
        "../../../connections/controls/vcard/SemanticTagService",
        "../../../text!sbt/connections/controls/vcard/templates/CommunityVCard.html"], 
        function(declare, dom, _TemplatedWidget, lang, SemanticTagService, template) {

    /**
     * @class sbt.controls.CommunityVCard
     */
    var communityVCard = declare([ _TemplatedWidget ], {

        /**
         * The html template of the vcard.
         * 
         * @property configUtil 
         * @type String
         */
        templateString: template,
        
        /**
         * Whether or not to encode HTML.
         * 
         * @property encodeHtml 
         * @type Boolean
         * @default true
         */
        encodeHtml: true,
        
        /**
         * The constructor
         * 
         * @method constructor
         * @param {Object} args
         *     @param {String} [args.name] The name to display on the community vcard.
         *     @param {String} args.uuid The uuid of the community.
         *     @param {String} [args.selectedWidgetId] a text string that corresponds to the widgetDefId of the widget that has been added to the community. 
         *     This text string is used to highlight the menu item in the navigation bar. The <widget_id> element is optional, and must only be provided for iWidgets that are integrated into Communities. 
         *     The widget ID is defined by the iWidget developer, and you need to request it from your administrator or the iWidget developer.
         */
        constructor: function(args) {
            if(!args.selectedWidgetId)
                args.selectedWidgetId="";
        	lang.mixin(args);
        },
        
        /**
         * @method postCreate
         */
        postCreate: function() {
            this.inherited(arguments);
            
            SemanticTagService.loadSemanticTagService();
        }
        
    });
    
    return communityVCard;
});