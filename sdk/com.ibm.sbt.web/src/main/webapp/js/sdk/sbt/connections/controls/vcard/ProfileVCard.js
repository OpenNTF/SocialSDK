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
        "../../../text!./templates/ProfileVCard.html"], 
        function(declare, dom, _TemplatedWidget, lang, SemanticTagService, template) {

    /**
     * @class sbt.controls.vcard.connections.ProfileVCard
     */
    var profileVCard = declare([ _TemplatedWidget ], {

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
         * The href of the vcard link. By default clicking the link does nothing.
         * 
         * @property href 
         * @type String
         * @default "javascript:void(0);"
         */
        href: "javascript:void(0);",
        /**
         * The person's name to be displayed with this vcard.
         * 
         * @property userName 
         * @type String
         * @default ""
         */
        userName: "",
        
        /**
         * A url flag specifying whether or not connections should include the css when loading the Semantic Tag Service.
         * @property inclCss
         * @type Boolean
         * @default false
         */
        inclCss: false,
        
        /**
         * A url flag specifying whether or not connections should include dojo when loading the Semantic Tag Service.
         * 
         * Careful.
         * 
         * @property inclDojo
         * @type Boolean
         * @default false
         */
        inclDojo: false,
        
        /**
         * The class of the html error element.
         *
         * @property errorClass 
         * @type String
         * @default ""
         */
        errorClass: "",
        
        /**
         * @property endpointName 
         * @type String
         * @default ""
         */
        endpointName: "connections",
        
        /**
         * 
         * @param args
         */
        constructor: function(args) {
        	lang.mixin(args);
        },
        
        /**
         * @method postCreate
         */
        postCreate: function() {
        	dom.setAttr(this.idNode, "class", (this.userId.indexOf("@") >= 0)  ? "email" : "x-lconn-userid");
        	
        	this.inherited(arguments);
        	SemanticTagService.loadSemanticTagService({
        	    endpoint: this.endpointName,
        	    inclDojo: this.inclDojo,
        	    inclCss: this.inclCss
        	});
        },
        
        /**
         * @method renderError
         * @param el
         * @param error
         */
        renderError: function(el, error) {
            var ediv = domConstruct.create("div", {
                "class": this.errorClass,
                role: "alert",
                tabIndex: 0
              }, el, "only");
            dom.setText(ediv, error.message);
        }
        
    });
    
    return profileVCard;
});