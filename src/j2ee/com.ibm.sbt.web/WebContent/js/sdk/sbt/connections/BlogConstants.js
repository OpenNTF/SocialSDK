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
 * Social Business Toolkit SDK. Definition of constants for BlogService.
 * 
 * @module sbt.connections.BlogConstants
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin({
        BlogFeedXPath : conn.ConnectionsFeedXPath,
        BlogXPath : {
            entry : "/a:entry",
            uid : "a:id",
            blogUuid : "a:id",
            title : "a:title",
            summary : "a:summary[@type='html']",
            blogUrl : "a:link[@rel='alternate']/@href",
            published : "a:published",
            updated : "a:updated",
            authorUserid : "a:author/snx:userid",
            authorName : "a:author/a:name",
            authorEmail : "a:author/a:email",
            handle : "snx:handle",
            timezone : "snx:timezone",
            rank : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']",
            categoryupdates : "a:category[@term='updates']",
            categoryfaq : "a:category[@term='faq']",
            categorywith : "a:category[@term='with']",
            categoryshared : "a:category[@term='shared']"
        },
        
        BlogEntryXPath : {
            entry : "/a:entry",
            uid : "a:id",
            blogid : "a:id",
            title : "a:title",
            summary : "a:summary[@type='html']",
            blogUrlAlternate : "a:link[@rel='alternate']/@href",
            blogUrl : "a:link[@rel='self']/@href",
            replies : "a:link[@rel='replies']/@href",
            blogUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/recommendations']/@href",
            content : "a:content[@type='html']",
            published : "a:published",
            updated : "a:updated",
            authorUserid : "a:author/snx:userid",
            authorName : "a:author/a:name",
            authorEmail : "a:author/a:email",
            authorState : "a:author/a:state",
            rankRecommendations : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']",
            rankComment : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']",
            rankHit : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/hit']",
            sourceId : "a:source/a:id",
            sourceTitle  : "a:source/a:title ",
            sourceLink : "a:source/a:link[@rel='self']/@href",
            sourceLinkAlternate : "a:source/a:link[@rel='alternate']/@href",
            sourceUpdated : "a:source/a:updated",
            sourceCategory : "a:source/a:link[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term"
        },
        MemberXPath : {
            entry : "/a:entry",
            uid : "a:contributor/snx:userid",
            userid : "a:contributor/snx:userid",
            name : "a:contributor/a:name",
            email : "a:contributor/a:email",
            role : "snx:role"
        },
        AtomBlogsAll : "/blogs/${blogHandle}/feed/blogs/atom",
        AtomBlogsMy : "/blogs/${blogHandle}/api/blogs",
        AtomEntriesAll : "/blogs/${blogHandle}/feed/entries/atom",
        AtomBlogEnties : "/blogs/${blogHandle}/feed/entries/atom",
        AtomEntryEdit : "/blogs/${blogHandle}/api/blogs/${blogId}",
        AtomEntryCreate : "blogs/${blogHandle}/api/entries",
        AtomBlogCreate : "/blogs/home/api/blogs",
        AtomEntryComments : "/blogs/${blogHandle}/feed/entrycomments/${entryName}/atom",
        AtomCommentAdd : "/blogs/${entryName}/feed/entry/atom"
    }, conn);
});