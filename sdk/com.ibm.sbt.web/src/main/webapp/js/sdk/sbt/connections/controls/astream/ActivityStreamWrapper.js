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
define(["../../../declare", "../../../lang", "../../../url", 
        "../../../config", "../../../util", "../../../stringUtil", "../../../connections/controls/WidgetWrapper", 
        "../../../text!../../../connections/controls/astream/templates/ActivityStreamContent.html",
        "../../../i18n!sbt/connections/controls/astream/nls/ActivityStreamWrapper"], 
        function(declare, lang, Url, config, util, stringUtil, WidgetWrapper, defaultTemplate, nls) {

    /**
     * The wrapper for the ActivityStream.
     * This class just has to provide its own template and the args it receives back to to the WidgetWrapper, which will take care of everything else.
     * 
     * Takes EITHER a feedUrl and an optional extensions object, OR an ActivityStream config object. 
     * If a feedUrl is specified any config object supplied will be ignored.
     * 
     * The feedUrl is relative to the opensocial context root, and takes the following form for different authentication:
     * sso    url pattern: '.../opensocial/rest/...'
     * oauth  url pattern: '.../opensocial/oauth/rest/...'
     * basic  url pattern: '.../opensocial/basic/rest/...'
     * public url pattern: '.../opensocial/anonymous/rest/...'
     * 
     * The endpoint you use should support the authentication you specify with the url.
     * 
     * @method constructor
     * 
     * @param {Object} args
     *     @params {String} args.feedUrl The url of the feed to populate the ActivityStream with.
     *     @params {Object} [args.extensions] A simple list of extensions to load.
     *         @params {Boolean} [args.extensions.commenting] If true load the commenting extension.
     *         @params {Boolean} [args.extensions.saving] If true load the saving extension.
     *         @params {Boolean} [args.extensions.refreshButton] If true load the refresh button extension.
     *         @params {Boolean} [args.extensions.DeleteButton] If true load the delete button extension.
     *         
     *     @params {Object} args.config An ActivityStream config object. Only specify this without a feedUrl argument.
     * 
     * @class sbt.controls.astream.ActivityStreamWrapper
     */
    var ActivityStreamWrapper = declare([ WidgetWrapper ], {
        
        /**
         * Set the html template which will go inside the iframe.
         * 
         * @property defaultTemplate
         * @type String
         */
        defaultTemplate: defaultTemplate,
        
        /**
         * Overriding the method in WidgetWrapper for providing the substitutions for variables in the template.
         * 
         * Replacements needed: 
         * proxyUrl, connectionsContextRoot, 
         * 
         * @method getTemplateReplacements
         * @returns {Object}
         */
        getTemplateReplacements: function(){
            var proxyUrl = this._endpoint.proxy.proxyUrl + "/" + this._endpoint.proxyPath;
            var connectionsUrl = this._endpoint.baseUrl;
            var libUrl = new Url(config.Properties.libraryUrl);
            var libQuery = libUrl.getQuery();
            var libQueryObj = {};
            var serviceMappings = this.getEndpoint().serviceMappings;
            var connectionsServiceMapping = serviceMappings ? this.getEndpoint().serviceMappings.connections : null;
            var connectionsContextRoot = connectionsServiceMapping ? connectionsServiceMapping : "connections";
            
            if(libQuery){
                libQueryObj = util.splitQuery(libQuery, "&");
            }
            
            lang.mixin(libQueryObj, {
                lib: "dojo",
                ver: "1.4.3"
            });
            libQuery = util.createQuery(libQueryObj, "&");
            libUrl.setQuery(libQuery);
            
            var cssUrl = stringUtil.substitute("{0}/{1}/resources/web/com.ibm.social.as/css/activityStream.css", [connectionsUrl, connectionsContextRoot]);
            if(this.cssUrl){ // in case there is one supplied in the constructor args
                cssUrl = connectionsUrl + this.cssUrl;
            }
            
            var sbtProps = lang.mixin({}, config.Properties);
            lang.mixin(sbtProps, {
                libraryUrl: libUrl.getUrl(),
                loginUi: "popup"
            });
            
            var templateReplacements = {
                args: JSON.stringify(this.args),
                proxyUrl: proxyUrl,
                connectionsUrl: connectionsUrl,
                libraryUrl: libUrl.getUrl(),
                sbtProps: JSON.stringify(sbtProps),
                cssUrl: cssUrl,
                connectionsContextRoot: connectionsContextRoot
            };
            
            return templateReplacements;
        },
        
        /**
         * Store the args so that they can be substituted into the defaultTemplate.
         * 
         * @property args
         * @type Object
         * @default null
         */
        args: null,
        
        /**
         * Can be supplied in args to override the location of the ActivityStream's css. This url is relative to the connections server.
         * 
         * @property cssUrl
         * @type String
         * @default ""
         */
        cssUrl: "",
        
        constructor: function(args){
            this.args = args;
        }
    });
    
    return ActivityStreamWrapper;
});