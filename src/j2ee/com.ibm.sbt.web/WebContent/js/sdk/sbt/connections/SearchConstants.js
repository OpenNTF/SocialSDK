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
 * Definition of constants for SearchService.
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin(conn, {
    	
        /**
         * Namespaces to be used when reading the Search ATOM feed
         */
        Namespaces : {
            a : "http://www.w3.org/2005/Atom",
            ibmsc : "http://www.ibm.com/search/content/2010",
            opensearch : "http://a9.com/-/spec/opensearch/1.1/",
            relevance : "http://a9.com/-/opensearch/extensions/relevance/1.0/",
            snx : "http://www.ibm.com/xmlns/prod/sn",
            spelling : "http://a9.com/-/opensearch/extensions/spelling/1.0/"
        },
        
        /**
         * XPath expressions used when parsing a Connections Search ATOM feed
         */
        SearchFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * XPath expressions to be used when reading a search result
         */
        SearchXPath : {
            // used by getEntityData
            entry : "/a:entry",
            // used by getEntityId
            uid : "a:id",
            // used by getters
            title : "a:title",
            content : "a:content",
            updated : "a:updated",
            authorUserid : "a:author/snx:userid",
            authorName : "a:author/a:name",
            authorEmail : "a:author/a:email",
            rank : "snx:rank",
            relevance : "relevance:score",
            summary : "a:summary",
            authorState : "a:author/snx:userState",
            type : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term",
            application : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/component']/@term",
            applicationCount : "count(a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/component']/@term)",
            primaryComponent : "a:category[ibmsc:field[@id='primaryComponent']]/@term",
            tags : "a:category[not(@scheme)]/@term",
            commentCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']",
            resultLink : "a:link[not(@rel)]/@href",
            bookmarkLink : "ibmsc:field[@id='dogearURL']",
            eventStartDate : "ibmsc:field[@id='eventStartDate']",
            authorJobTitle : "a:content/xhtml:div/xhtml:span/xhtml:div[@class='title']",
            authorJobLocation : "a:content/xhtml:div/xhtml:span/xhtml:div[@class='location']",
            authorCount : "count(a:contributor)",
            contributorCount : "count(a:author)",
            tagCount : "count(a:category[not(@scheme)])",
            highlightField : "ibmsc:field[@id='highlight']",
            fileExtension : "ibmsc:field[@id='fileExtension']",
            memberCount : "snx:membercount",
            communityUuid : "snx:communityUuid",
            containerType : "ibmsc:field[@id='container_type']",
            communityParentLink : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/container' and @type='text/html']/@href",
            parentageMetaID : "ibmsc:field[contains(@id, 'ID')]/@id",
            parentageMetaURL : "ibmsc:field[contains(@id, 'URL')]",
            parentageMetaURLID : "ibmsc:field[contains(@id, 'URL')]/@id",
            objectRefDisplayName : "ibmsc:field[@id='FIELD_OBJECT_REF_DISPLAY_NAME']",
            objectRefUrl : "ibmsc:field[@id='FIELD_OBJECT_REF_URL']",
            accessControl : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/accesscontrolled']/@term",
            commentsSummary : "ibmsc:field[@id='commentsSummary']"
        },
        
		/**
         * Search IBM Connections for public information.
         */
        publicSearch : "/search/atom/search/results",
        
        /**
         * Search IBM Connections for both public information and private information that you have access to.
         */
        mySearch : "/search/atom/mysearch/results",

		/**
         * Search IBM Connection for public information, and then return the people associated with the results.
         */
        peopleSearch : "/search/atom/search/facets/people",
        
        /**
         * Search Lotus Connections for both public information and private information that you have access to, and then return the people associated with the results.
         */
        myPeopleSearch : "/search/atom/mysearch/facets/people",

		/**
         * Search IBM Connection for public information, and then return the tags associated with the results.
         */
        tagsSearch : "/search/atom/search/facets/tags",
        
        /**
         * Search Lotus Connections for both public information and private information that you have access to, and then return the tags associated with the results.
         */
        myTagsSearch : "/search/atom/mysearch/facets/tags",

		/**
         * Search IBM Connection for public information, and then return the dates associated with the results.
         */
        dateSearch : "/search/atom/search/facets/date",
        
        /**
         * Search Lotus Connections for both public information and private information that you have access to, and then return the dates associated with the results.
         */
        myDateSearch : "/search/atom/mysearch/facets/date",

		/**
         * Search IBM Connection for public information, and then return the applications associated with the results.
         */
        sourceSearch : "/search/atom/search/facets/source",
        
        /**
         * Search Lotus Connections for both public information and private information that you have access to, and then return the applications associated with the results.
         */
        mySourceSearch : "/search/atom/mysearch/facets/source"

    });
});