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
 * Definition of constants for ActivityStreamService.
 */
define([],function() {
    return {
        sbtErrorCodes:{
            badRequest         :400
        },
        
        sbtErrorMessages:{
        },
        
        xpath_search : {
            "id" : "a:id",
            "title" : "a:title",
            "summary" : "a:summary",
            "updated" : "a:updated",
            "relevance" : "relevance:score",
            "authorName" : "a:author/a:name",
            "authorUid" : "a:author/snx:userid",
            "authorEmail" : "a:author/a:email",
            "authorState" : "a:author/snx:userState",
            "type" : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term",
            "application" : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/component']/@term",
            "applicationCount" : "count(a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/component']/@term)",
            "primaryComponent" : "a:category[ibmsc:field[@id='primaryComponent']]/@term",
            "tags" : "a:category[not(@scheme)]/@term",
            "commentCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']",
            "resultLink" : "a:link[not(@rel)]/@href",
            "bookmarkLink" : "ibmsc:field[@id='dogearURL']",
            "eventStartDate" : "ibmsc:field[@id='eventStartDate']",
            "authorJobTitle" : "a:content/xhtml:div/xhtml:span/xhtml:div[@class='title']",
            "authorJobLocation" : "a:content/xhtml:div/xhtml:span/xhtml:div[@class='location']",
            "authorCount" : "count(a:contributor)",
            "contributorCount" : "count(a:author)",
            "tagCount" : "count(a:category[not(@scheme)])",
            "highlightField" : "ibmsc:field[@id='highlight']",
            "fileExtension" : "ibmsc:field[@id='fileExtension']",
            "memberCount" : "snx:membercount",
            "communityUuid" : "snx:communityUuid",
            "containerType" : "ibmsc:field[@id='container_type']",
            "communityParentLink" : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/container' and @type='text/html']/@href",
            "parentageMetaID" : "ibmsc:field[contains(@id, 'ID')]/@id",
            "parentageMetaURL" : "ibmsc:field[contains(@id, 'URL')]",
            "parentageMetaURLID" : "ibmsc:field[contains(@id, 'URL')]/@id",
            "objectRefDisplayName" : "ibmsc:field[@id='FIELD_OBJECT_REF_DISPLAY_NAME']",
            "objectRefUrl" : "ibmsc:field[@id='FIELD_OBJECT_REF_URL']",
            "accessControl" : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/accesscontrolled']/@term",
            "commentsSummary" : "ibmsc:field[@id='commentsSummary']"
        }
    };
});