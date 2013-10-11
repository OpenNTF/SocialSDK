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
 * AtomEntity class represents an entry from an IBM Connections ATOM feed.
 * 
 * @module sbt.base.AtomEntity
 */
define([ "../declare", "../lang", "../stringUtil", "./BaseConstants", "./BaseEntity", "./XmlDataHandler" ], 
    function(declare,lang,stringUtil,BaseConstants,BaseEntity,XmlDataHandler) {

    var EntryTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
    				"<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">" +
    					"<title type=\"text\">${getTitle}</title>" +
    					"<content type=\"${contentType}\">${getContent}</content>" +
    					"${categoryScheme}${createEntryData}" + 
    				"</entry>";

    /**
     * AtomEntity class represents an entry from an IBM Connections ATOM feed.
     * 
     * @class AtomEntity
     * @namespace sbt.base
     */
    var AtomEntity = declare(BaseEntity, {
    	
    	contentType : "html",
    	categoryScheme : null,

        /**
         * Construct an AtomEntity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        	// create XML data handler
        	this.dataHandler = new XmlDataHandler({
                service : args.service,
                data : args.data,
                namespaces : lang.mixin(BaseConstants.Namespaces, args.namespaces || {}),
                xpath : lang.mixin({}, BaseConstants.AtomEntryXPath, args.xpath || this.xpath || {})
            });
        },
        
        /**
         * Return the value of id from ATOM entry document.
         * 
         * @method getId
         * @return {String} ID of the ATOM entry
         */
        getId : function() {
            return this.getAsString("id");
        },

        /**
         * Return the value of title from ATOM entry document.
         * 
         * @method getTitle
         * @return {String} ATOM entry title
         */
        getTitle : function() {
            return this.getAsString("title");
        },

        /**
         * Sets title of ATOM entry.
         * 
         * @method setTitle
         * @param {String} title ATOM entry title
         */
        setTitle : function(title) {
            return this.setAsString("title", title);
        },
        
        /**
         * Return the value of summary from ATOM entry document.
         * 
         * @method getSummary
         * @return {String} ATOM entry summary
         */
        getSummary : function() {
            return this.getAsString("summary");
        },

        /**
         * Sets summary of ATOM entry.
         * 
         * @method setSummary
         * @param {String} title ATOM entry summary
         */
        setSummary : function(summary) {
            return this.setAsString("summary", summary);
        },
        
        /**
         * Return the content from ATOM entry document.
         * 
         * @method getContent
         * @return {Object} Content
         */
        getContent : function() {
            return this.getAsString("content");
        },

        /**
         * Sets content of ATOM entry.
         * 
         * @method setContent
         * @param {String} content
         */
        setContent : function(content) {
            return this.setAsString("content", content);
        },

        /**
         * Return array of category terms from ATOM entry document.
         * 
         * @method getTags
         * @return {Object} Array of categories of the ATOM entry
         */
        getCategoryTerms : function() {
            return this.getAsArray("categoryTerm");
        },

        /**
         * Set new category terms to be associated with this ATOM entry document.
         * 
         * @method setCategories
         * @param {Object} Array of categories to be added to the ATOM entry
         */

        setCategoryTerms : function(categoryTerms) {
            return this.setAsArray("categoryTerm", categoryTerms);
        },

        /**
         * Gets an author of IBM Connections Forum Reply.
         * 
         * @method getAuthor
         * @return {Member} author Author of the Forum Reply
         */
        getAuthor : function() {
            return this.getAsObject(
            		[ "authorUserid", "authorName", "authorEmail", "authorUserState" ],
            		[ "userid", "name", "email", "userState" ]);
        },

        /**
         * Gets a contributor of IBM Connections forum.
         * 
         * @method getContributor
         * @return {Member} contributor Contributor of the forum
         */
        getContributor : function() {
            return this.getAsObject(
            		[ "contributorUserid", "contributorName", "contributorEmail", "contributorUserState" ],
            		[ "userid", "name", "email", "userState" ]);
        },
        
        /**
         * Return the published date of the IBM Connections Forum Reply from
         * Forum Reply ATOM entry document.
         * 
         * @method getPublished
         * @return {Date} Published date of the Forum Reply
         */
        getPublished : function() {
            return this.getAsDate("published");
        },

        /**
         * Return the last updated date of the IBM Connections Forum Reply from
         * Forum Reply ATOM entry document.
         * 
         * @method getUpdated
         * @return {Date} Last updated date of the Forum Reply
         */
        getUpdated : function() {
            return this.getAsDate("updated");
        },
        
        /**
         * Return the alternate url of the ATOM entry document.
         * 
         * @method getAlternateUrl
         * @return {String} Alternate url
         */
        getAlternateUrl : function() {
            return this.getAsString("alternateUrl");
        },
                
        /**
         * Return the self url of the ATOM entry document.
         * 
         * @method getSelfUrl
         * @return {String} Self url
         */
        getSelfUrl : function() {
            return this.getAsString("selfUrl");
        },
        
        /**
         * Return the edit url of the ATOM entry document.
         * 
         * @method getEditUrl
         * @return {String} Edit url
         */
        getEditUrl : function() {
            return this.getAsString("editUrl");
        },

        /**
         * Create ATOM entry XML
         * 
         * @returns
         */
        createPostData : function() {
            var transformer = function(value,key) {
            	if (key == "getContent" && this.contentType == "html") {
            		value = (value && lang.isString(value)) ? value.replace(/</g,"&lt;").replace(/>/g,"&gt;") : value; 
            	}
                return value;
            };
            var postData = stringUtil.transform(EntryTmpl, this, transformer, this);
            return stringUtil.trim(postData);
        },
        
        /**
         * Return extra entry data to be included in post data for this entity.
         * 
         * @returns {String}
         */
        createEntryData : function() {
        	return "";
        }

    });
    
    return AtomEntity;
});
