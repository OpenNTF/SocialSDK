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
    				"<entry xmlns=\"http://www.w3.org/2005/Atom\" ${createNamespaces}>" +
    					"${categoryScheme}${createTitle}${createContent}${createSummary}${createContributor}${createTags}${createEntryData}" + 
    				"</entry>";
	var TitleTmpl = "<title type=\"text\">${title}</title>";
	var ContentTmpl = "<content type=\"${contentType}\">${content}</content>";
	var SummaryTmpl = "<summary type=\"text\">${summary}</summary>";
	var ContributorTmpl = "<contributor>${contributor}</contributor>";
    var EmailTmpl = "<email>${email}</email>";
    var UseridTmpl = "<snx:userid xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">${userid}</snx:userid>";
    var CategoryTmpl = "<category term=\"${tag}\"></category>";
    
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
        	if (args.data) {
	        	// create XML data handler
	        	this.dataHandler = this.createDataHandler(
	        		args.service, args.data || null, args.response || null,
	        		args.namespaces || this.namespaces || BaseConstants.Namespaces,
	                args.xpath || this.xpath || BaseConstants.AtomEntryXPath
	            );
        	} else {
        		this.service = args.service || this.service;
        		this.namespaces = args.namespaces || this.namespaces || BaseConstants.Namespaces;
        		this.xpath = args.xpath || this.xpath || BaseConstants.AtomEntryXPath;
        	}
        },
        
        /**
         * Create the DataHandler for this entity.
         * 
         * @method createDataHandler
         */
        createDataHandler : function(service, data, response, namespaces, xpath) {
        	return new XmlDataHandler({
                service : service,
                data : data,
                namespaces : namespaces,
                xpath : xpath
            });
        },
        
        /**
         * Called to set the entity data after the entity
         * was loaded. This will cause the existing fields to be cleared.
         * 
         * @param data
         */
        setData : function(data, response) {
        	// create XML data handler
    		this.dataHandler = this.createDataHandler(
    			this.service, 
    			data, response || null,
        		this.namespaces || BaseConstants.Namespaces,
                this.xpath || BaseConstants.AtomEntryXPath
            );
        	
        	this.inherited(arguments);
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
         * Gets an author of the ATOM entry
         * 
         * @method getAuthor
         * @return {Object} author Author of the ATOM entry
         */
        getAuthor : function() {
            return this.getAsObject(
            		[ "authorUserid", "authorName", "authorEmail", "authorUserState" ],
            		[ "userid", "name", "email", "userState" ]);
        },

        /**
         * Gets a contributor of the ATOM entry
         * 
         * @method getContributor
         * @return {Object} contributor Contributor of the ATOM entry
         */
        getContributor : function() {
            return this.getAsObject(
            		[ "contributorUserid", "contributorName", "contributorEmail", "contributorUserState" ],
            		[ "userid", "name", "email", "userState" ]);
        },
        
        /**
         * Sets the contributor of the ATOM entry
         * 
         * @method setContributor
         * @return {Object} contributor Contributor of the ATOM entry
         */
        setContributor : function(contributor) {
            return this.setAsObject(contributor);
        },
        
        /**
         * Return the published date of the ATOM entry document.
         * 
         * @method getPublished
         * @return {Date} Published date of the entry
         */
        getPublished : function() {
            return this.getAsDate("published");
        },

        /**
         * Return the last updated date of the ATOM entry document.
         * 
         * @method getUpdated
         * @return {Date} Last updated date of the entry
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
         * @method createPostData
         * @returns
         */
        createPostData : function() {
            var postData = stringUtil.transform(EntryTmpl, this, function(v,k) { return v; }, this);
            return stringUtil.trim(postData);
        },
        
        /**
         * Return title element to be included in post data for this ATOM entry.
         * 
         * @method createTitle
         * @returns {String}
         */
        createTitle : function() {
        	var title = this.getTitle();
        	if (title) {
        		return stringUtil.transform(TitleTmpl, { "title" : title });
        	}
        	return "";
        },
        
        /**
         * Return content element to be included in post data for this ATOM entry.
         * 
         * @method createContent
         * @returns {String}
         */
        createContent : function() {
        	var content = this.getContent();
        	if (content) {
        		if (this.contentType == "html") {
        			content = (content && lang.isString(content)) ? content.replace(/</g,"&lt;").replace(/>/g,"&gt;") : content; 
            	}
        		return stringUtil.transform(ContentTmpl, { "contentType" : this.contentType, "content" : content });
        	}
        	return "";
        },
        
        /**
         * Return summary element to be included in post data for this ATOM entry.
         * 
         * @method createSummary
         * @returns {String}
         */
        createSummary : function() {
        	var summary = this.getSummary();
        	if (summary) {
        		return stringUtil.transform(SummaryTmpl, { "summary" : summary });
        	}
        	return "";
        },
        
        /**
         * Return contributor element to be included in post data for this ATOM entry.
         * 
         * @method createContributor
         * @returns {String}
         */
        createContributor : function() {
        	var contributor = this.getContributor();
        	if (contributor) {
        		var value = "";
        		var email = contributor.email || ((this.getEmail) ? this.getEmail() : null);
        		if (email) {
                    value += stringUtil.transform(EmailTmpl, { "email" : email });
            	}
        		var userid = contributor.userid || ((this.getUserid) ? this.getUserid() : null);
            	if (userid) {
                    value += stringUtil.transform(UseridTmpl, { "userid" : userid });
            	}
            	if (value.length > 0) {
            		value = stringUtil.transform(ContributorTmpl, { "contributor" : value });
            	}
            	return value;
        	}
        	return "";
        },
        
        /**
         * Return tags elements to be included in post data for this ATOM entry.
         * 
         * @method createTags
         * @returns {String}
         */
        createTags : function() {
        	if (this.getTags && this.getTags()) {
                var value = "";
        		var tags = this.getTags();
                for (var tag in tags) {
                    value += stringUtil.transform(CategoryTmpl, { "tag" : tags[tag] });
                }
                return value;
            }
        	return "";
        },
        
        /**
         * Return extra entry data to be included in post data for this ATOM entry.
         * 
         * @method createEntryData
         * @returns {String}
         */
        createEntryData : function() {
        	return "";
        },
        
        /**
         * return namespaces for this ATOM entry.
         * 
         * @method createNamespaces
         */
        createNamespaces : function() {
        	var namespaceData = "";
        	var namespaces = this.dataHandler ? this.dataHandler.namespaces : this.namespaces;
        	for (prefix in namespaces) {
        		if (prefix != "a") { // ATOM automatically included
        			namespaceData += (namespaceData.length > 0) ? " " : "";
        			namespaceData += "xmlns:"+prefix+"=\"" + namespaces[prefix] + "\"";
        		}
        	}
        	return namespaceData;
        }

    });
    
    return AtomEntity;
});
