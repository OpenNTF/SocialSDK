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
        "../ConnectionsGridRenderer",
        "../../../lang", "../../../stringUtil", "../../../i18n",
        "../../../i18n!sbt/connections/controls/search/nls/SearchGridRenderer",
        "../../../text!sbt/connections/controls/search/templates/BookmarkBody.html",
        "../../../text!sbt/connections/controls/search/templates/CalendarBody.html",
        "../../../text!sbt/connections/controls/search/templates/CommunityBody.html",
        "../../../text!sbt/connections/controls/search/templates/DefaultBody.html",
        "../../../text!sbt/connections/controls/search/templates/DefaultHeader.html",
        "../../../text!sbt/connections/controls/search/templates/DefaultSummary.html",
        "../../../text!sbt/connections/controls/search/templates/ProfileBody.html",
        "../../../text!sbt/connections/controls/search/templates/ProfileHeader.html",
        "../../../text!sbt/connections/controls/search/templates/PersonCard.html",
        "../../../text!sbt/connections/controls/search/templates/StatusUpdateExtraHeader.html",
        "../../../text!sbt/connections/controls/search/templates/StatusUpdateHeader.html",
        "../../../text!sbt/connections/controls/search/templates/a.html",
        "../../../text!sbt/connections/controls/search/templates/td.html",
        "../../../text!sbt/connections/controls/search/templates/tr.html",
        "../../../text!sbt/connections/controls/search/templates/li.html",
        "../../../text!sbt/connections/controls/search/templates/ul.html",
        "../../../text!sbt/connections/controls/search/templates/span.html",
        "../../../text!sbt/connections/controls/search/templates/img.html",
        "../../../text!sbt/connections/controls/search/templates/em.html"],
        function(declare, ConnectionsGridRenderer, lang, stringUtil, i18n, nls, bookmarkBodyTemplate, calendarBodyTemplate, communityBodyTemplate, defaultBodyTemplate, defaultHeaderTemplate, defaultSummaryTemplate, profileBodyTemplate, profileHeaderTemplate, personCardTemplate, statusUpdateExtraHeaderTemplate, statusUpdateHeaderTemplate, aElement, tdElement, trElement, liElement, ulElement, spanElement, imgElement, emElement) {

    /**
     * @class SearchGridRenderer
     * @module sbt.controls.grid.connections.SearchGridRenderer
     * @namespace sbt.controls.grid.connections
     */
    var searchGridRenderer = declare(ConnectionsGridRenderer, {

        _nls: nls,
        
        resultType: null,
        
        application: null,
        
        /**
         * Takes a html template and a list of arguments. These arguments are substituted into the template and the template is returned with attributes set.
         * 
         * Used for creating html elements. 
         * 
         * e.g. 
         * buildElement("<li ${classAttr} ${roleAttr}>${content}</li>", {classAttr: "lotusFirst", roleAttr: "listitem", content: "content"});
         * 
         * returns
         * 
         * "<li class="lotusFirst" role = "listitem">content</li>"
         * 
         * @param html
         * @param args
         * @returns The html template with the substituted values.
         */
        buildElement: function(html, args){
            args.classAttr = args.classAttr ? 'class="' + args.classAttr + '"' : "";
            args.styleAttr = args.styleAttr ? 'style="' + args.styleAttr + '"' : "";
            args.roleAttr = args.roleAttr ? 'role="' + args.roleAttr + '"' : "";
            args.hrefAttr = args.hrefAttr ? 'href="' + args.hrefAttr + '"' : "";
            args.onclickAttr = args.onclickAttr ? 'onclick="' + args.onclickAttr + '"' : "";
            args.colspanAttr = args.colspanAttr ? 'colspan="' + args.colspanAttr + '"' : "";
            args.altAttr = args.altAttr ? 'alt="' + args.altAttr + '"' : "";
            args.srcAttr = args.srcAttr ? 'src="' + args.srcAttr + '"' : "";
            args.titleAttr = args.titleAttr ? 'title="' + args.titleAttr + '"' : "";
            args.widthAttr = args.widthAttr ? 'width="' + args.widthAttr + '"' : "";
            args.heightAttr = args.heightAttr ? 'height="' + args.heightAttr + '"' : "";
            
            return stringUtil.transform(html, args);
        },
        
        resultTypes: {
            activities : "activities",
            blogs : "blogs",
            bookmark: "bookmark",
            calendar : "calendar",
            communities : "communities",
            files : "files",
            forums : "forums",
            inactiveProfiles : "inactiveProfiles",
            profiles : "profiles",
            statusUpdates : "status_updates",
            wiki : "wiki"
        },

        /**
         * Creates an li containing a list of tags.
         * 
         * @param grid
         * @param item
         * @param i
         * @param items
         * @returns An li containing a list of tags.
         */
        tagsList: function(grid, item, i, items){
            var tagCount = item.getValue("tagCount");
            tagCount = parseInt(tagCount);
            if(tagCount === 0)
                return "";
            
            var resultLiRole = "listitem";
            
            var ulClass = "lotusInlinelist";
            var ulStyle = "display:inline";
            var ulContent = "";
            
            var tags = item.getValue("tags");
            
            var i;
            for(i = 0; i < tagCount && i < 3; i++){
                var currentTag = typeof tags === 'string' ? tags : tags[i];
                var liStyle = "padding:0px";
                var liClass = "lotusFirst";
                if(i === 2 || i === tagCount - 1)
                    liClass = "lotusLast";
                
                var aHref = "javascript:void(0);";
                var aOnClick = "onclick=\"searchObject.performTagFilter('" + tags[i] + "');\"";
                var aAlt = currentTag;
                
                if(item.getValue("highlightField"))
                    var aClass = currentTag;
                else 
                    aClass = undefined;
                
                var aContent = currentTag;
                
                var a = this.buildElement(aElement, {
                    hrefAttr: aHref,
                    onclickAttr: aOnClick,
                    altAttr: aAlt,
                    classAttr: aClass,
                    content: aContent
                });
                var liContent = a;
                if(i != tagCount - 1 && i != 2)
                    liContent += ",&nbsp";
                
                var li = this.buildElement(liElement, {
                    content: liContent,
                    classAttr: liClass,
                    styleAttr: liStyle
                });
                ulContent += li + "\n";
            }
            
            var more = tagCount > 3 ? this._nls.tagsMore.replace("{0}", tagCount-3) : "";
            var ul = this.buildElement(ulElement, {
                content: ulContent,
                classAttr: ulClass,
                styleAttr: ulStyle
            });
            var span = this.buildElement(spanElement, {
                content: this._nls.tags + "&nbsp; " + ul + "&nbsp;" + more
            });
            
            return this.buildElement(liElement, {
                roleAttr: resultLiRole,
                content: span
            });
        },
        
        /**
         * Returns the img tag defining the image to use as the application icon.
         * 
         * @param grid
         * @param item
         * @param i
         * @param items
         * @returns {String}
         */
        summaryIcon: function(grid, item, i, items){
            var fileExtension = item.getValue("fileExtension");
            var summaryImageClass = "lconn-ftype16 lconn-ftype16-" + fileExtension, summaryImageSrc = "", summaryImageAlt = "", summaryImageTitle = "", summaryImageRole = "";
            switch(this.resultType){
            case this.resultTypes.activities:
                summaryImageClass = "lconnSprite lconnSprite-iconActivities16";
                summaryImageSrc = "images/blank.gif";
                summaryImageAlt = this._nls.activities;
                summaryImageTitle = this._nls.activities;
                summaryImageRole = "presentation";
                break;
            case this.resultTypes.blogs:
                summaryImageClass = "lconnSprite lconnSprite-iconBlogs16";
                summaryImageSrc = "images/blank.gif";
                summaryImageAlt = this._nls.blogs;
                summaryImageTitle = this._nls.blogs;
                summaryImageRole = "presentation";
                break;
            case this.resultTypes.bookmark:
                summaryImageClass = "lconnSprite lconnSprite-iconBookmarks16";
                summaryImageSrc = "images/blank.gif";
                summaryImageAlt = this._nls.bookmarks;
                summaryImageTitle = this._nls.bookmarks;
                summaryImageRole = "presentation";
                break;
            case this.resultTypes.calendar:
                summaryImageClass = "lconnSprite lconnSprite-iconCalendar16";
                summaryImageSrc = "images/blank.gif";
                summaryImageAlt = this._nls.calendar;
                summaryImageTitle = this._nls.calendar;
                summaryImageRole = "presentation";
                break;
            case this.resultTypes.communities:
                summaryImageClass = this.componentContains(item, "communities:feed") ? "lconnSprite lconnSprite-iconFeed16" : "lconnSprite lconnSprite-iconCommunities16";
                summaryImageSrc = "images/blank.gif";
                summaryImageAlt = this._nls.communities;
                summaryImageTitle = this._nls.communities;
                summaryImageRole = "presentation";
                break;    
            case this.resultTypes.files:
                summaryImageClass = fileExtension ? summaryImageClass : "lconnSprite lconnSprite-iconFiles16";
                summaryImageSrc = "images/blank.gif";
                summaryImageAlt = this._nls.files;
                summaryImageTitle = this._nls.files;
                summaryImageRole = "presentation";
                break;
            case this.resultTypes.forums:
                summaryImageClass = "lconnSprite lconnSprite-iconForums16";
                summaryImageSrc = "images/blank.gif";
                summaryImageAlt = this._nls.forums;
                summaryImageTitle = this._nls.forums;
                summaryImageRole = "presentation";
                break;
            case this.resultTypes.profiles:
                summaryImageClass = "lconnSprite lconnSprite-iconProfiles16";
                summaryImageSrc = "images/blank.gif";
                summaryImageAlt = this._nls.profiles;
                summaryImageTitle = this._nls.profiles;
                summaryImageRole = "presentation";
                break;
            case this.resultTypes.statusUpdates:
                summaryImageClass = "lconnSprite lconnSprite-iconStatusUpdate16";
                summaryImageSrc = "images/blank.gif";
                summaryImageAlt = this._nls.statusUpdates;
                summaryImageTitle = this._nls.statusUpdates;
                summaryImageRole = "presentation";
                break;
            case this.resultTypes.wikis:
                summaryImageClass = fileExtension.length !== 0 ? summaryImageClass : "lconnSprite lconnSprite-iconWikis16";
                summaryImageSrc = "images/blank.gif";
                summaryImageAlt = this._nls.wikis;
                summaryImageTitle = this._nls.wikis;
                summaryImageRole = "presentation";
                break;
            }
            return this.buildElement(imgElement, {
                classAttr: summaryImageClass,
                srcAttr: summaryImageSrc,
                altAttr: summaryImageAlt,
                titleAttr: summaryImageTitle,
                roleAttr: summaryImageRole
            });
        },
        
        /**
         * Returns a result Summary, which is a span element containing a summary but includes an extra ul if the result type is statusUpdate.
         * @param grid
         * @param item
         * @param i
         * @param items
         * @returns
         */
        resultSummary: function(grid, item, i, items){
            var summary = item.getValue("summary");
            if(summary.length!==0){
                var statusUpdateUl = "";
                if(this.resultType === this.resultTypes.statusUpdates){
                    var statusUpdateLi = this.buildElement(liElement, {
                        classAttr: "lotusMeta lconnSearchHighlight",
                        content: this._nls.comment
                    });
                    statusUpdateUl = this.buildElement(ulElement, {
                        styleAttr: "display: inline",
                        classAttr: "lotusInlinelist",
                        roleAttr: "presentation",
                        content: statusUpdateLi
                    });
                }
                summary = summary.replace(/&lt;b&gt;/g, "<b>"); // replace the encoded <b> tags...
                summary = summary.replace(/&lt;\/b&gt;/g, "</b>");
                
                var summarySpan = this.buildElement(spanElement, {
                    classAttr: "lotusMeta lconnSearchHighlight",
                    content: summary
                });
                
                return statusUpdateUl + "\n" + summarySpan + "\n";
            }
            else if(this.resultType != this.resultTypes.statusUpdates){
                return this.buildElement(emElement, {
                    content: this._nls.noDescription,
                    classAttr: "lotusMeta"
                });
            }
                
        },
        
        resultComment: function(grid, item, i, items){
            if(item.getValue("commentsSummary").length > 0 && this.resultType != this.resultTypes.statusUpdates){
                var divSpan = this.buildElement(spanElement, {
                    classAttr: "lotusMeta lconnSearchHighlight",
                    content: this._nls.comment + item.getValue("commentSummary")
                });
                return this.buildElement(divElement, {
                    styleAttr: "clear:both;",
                    content: divSpan
                });
            } else{
                return "";
            }
        },
        
        formattedTitle: function(grid, item, i, items){
            var title = item.getValue("title");
            if(title.length !== 0){
                title = title.replace(/&lt;b&gt;/g, "<b>"); // replace the encoded <b> tags...
                title = title.replace(/&lt;\/b&gt;/g, "</b>");
            }
            
            return title;
        },
        
        parentageMeta: function(grid, item, i, items){
            switch(item.getValue("parentageMetaURLID")){
            case 'blogURL':
                var aHref = item.getValue("parentageMetaURL");
                var aContent = "";
                if(this.componentcontains(item, "blogs:ideationblogs:idea"))
                    aContent = this._nls.fromAnIdeationBlog;
                else
                    aContent = this._nls.fromABlog;
                return this.buildElement(aElement, {
                    hrefAttr: aHref,
                    content: aContent
                }) + " > ";
            case 'forumURL':
                return this.buildElement(aElement, {
                    hrefAttr: item.getValue("parentageMetaURL"),
                    content: this._nls.fromAForum
                }) + " > ";
            case 'wikiURL':
                return this.buildElement(aElement, {
                    hrefAttr: item.getValue("parentageMetaURL"),
                    content: this._nls.fromAWiki
                }) + " > ";
            case 'activityURL':
                if(item.getValue("primaryComponent").indexOf("activities") === 0 || item.getValue("primaryComponent").indexOf("communities:activities") === 0){
                    if(item.getValue("primaryComponent") === "activities:bookmark" || item.getValue("primaryComponent") === "communities:activities:bookmark" || this.componentContains("activities:section") || this.componentContains("activities:task") || this.componentContains("activities:entry")){
                        return this.buildElement(aElement, {
                            hrefAttr: item.getValue("parentageMetaURL"),
                            content: this._nls.fromAnActivity
                        }) + " > ";
                    }
                    if(item.getValue("parentageMetaID") === "activityEntryURL" || item.getValue("primaryComponent") === "activities:bookmark"){
                        return this.buildElement(aElement, {
                            hrefAttr: item.getValue("parentageMetaURL"),
                            content: this._nls.entry
                        }) + " > ";
                    }
                }
                
            }
            return "";
        },
        
        communityParent: function(grid, item, i, items){
            if(item.getValue("communityUuid").length!=0 && item.getValue("containerType") != "stand-alone" && item.getValue("primaryComponent") != "communities:entry" && item.getValue("primaryComponent").indexOf("communities") === 0){
                return this.buildElement(aElement, {
                    hrefAttr: item.getValue("communityParentLink"),
                    content: this._nls.fromACommunity
                }) + " > ";
            }
            else return "";
        },
        /**
         * @param grid
         * @param item
         * @param i
         * @param items
         * @returns
         */
        summaryTypeLabel: function(grid, item, i, items){
            var spanContent = "";
            var resultTypes = this.resultTypes;
            switch(this.resultType){
            case resultTypes.activities:
                if(this.componentContains(item, "activities:task")){
                    spanContent = this._nls.activityToDo;
                }
                else if(this.componentContains(item, "activities:activity") || this.componentContains(item, "activities:community_activity") || this.componentContains(item, "activities:community_activity+members") || this.componentContains(item, "activities:explicit_membership_community_activity")){
                    spanContent = this._nls.activity;
                } else if(this.componentContains(item, "activities:bookmark")){
                    spanContent = this._nls.activityBookmark;
                } else if(this.componentContains(item, "activities:section")){
                    spanContent = this._nls.activitySection;
                } else if(this.componentContains(item, "activities:reply")){
                    spanContent = this._nls.activityComment;
                } else{
                    spanContent = this._nls.activityEntry;
                }
                return this.buildElement(spanElement, {
                    classAttr: "lotusMeta",
                    content: spanContent
                 });
            case resultTypes.blogs:
                if(this.componentContains(item, "blogs:ideationblogs:ideationblog")){
                    spanContent = this._nls.ideationBlog;
                }
                else if(this.componentContains(item, "blogs:ideationblogs:idea")){
                    spanContent = this._nls.idea;
                } else if(this.componentContains(item, "blogs:ideationblogs:comment")){
                    spanContent = this._nls.ideaComment;
                } else if(this.componentContains(item, "blogs:entry")){
                    spanContent = this._nls.blogEntry;
                } else if(this.componentContains(item, "blogs:comment")){
                    spanContent = this._nls.blogComment;
                } else{
                    spanContent = this._nls.blog;
                }
                return this.buildElement(spanElement, {
                    classAttr: "lotusMeta",
                    content: spanContent
                 });
            case resultTypes.bookmark:
                spanContent = this._nls.bookmark;
                return this.buildElement(spanElement, {
                    classAttr: "lotusMeta",
                    content: spanContent
                 });
            case resultTypes.calendar:
                spanContent = this._nls.calendar;
                return this.buildElement(spanElement, {
                    classAttr: "lotusMeta",
                    content: spanContent
                 });
            case resultTypes.communities:
                if(this.componentContains(item, "communities:entry")){
                    return this.buildElement(spanElement, {
                        classAttr: "lotusMeta",
                        content: this._nls.community
                     });
                } else if(this.componentContains(item, "communities:feed")){
                    return this.buildElement(spanElement, {
                        classAttr: "lotusMeta",
                        content: this._nls.feed
                     });
                } else if(this.componentContains(item, "communities:bookmark")){
                    return this.buildElement(spanElement, {
                        classAttr: "lotusMeta",
                        content: this._nls.bookmark
                     });
                }
            case resultTypes.files:
                spanContent = this._nls.file;
                return this.buildElement(spanElement, {
                    classAttr: "lotusMeta",
                    content: spanContent
                 });
            case resultTypes.forums:
                if(this.componentContains(item, "communities:forums:forum")){
                    spanContent = this._nls.forum;
                } else if(this.componentContains(item, "communities:forums:category")){
                    spanContent = this._nls.forumCategory;
                } else{
                    spanContent = this._nls.forumTopic;
                }
                return this.buildElement(spanElement, {
                    classAttr: "lotusMeta",
                    content: spanContent
                 });
            case resultTypes.profiles:
                spanContent = this._nls.profile;
                return this.buildElement(spanElement, {
                    classAttr: "lotusMeta",
                    content: spanContent
                 });
            case resultTypes.statusUpdates:
                spanContent = this._nls.fromAStatusUpdate;
                return this.buildElement(spanElement, {
                    classAttr: "lotusMeta",
                    content: spanContent
                 });
            case resultTypes.wikis:
                if(this.componentContains(item, "wikis:wiki")){
                    spanContent = this._nls.wikiType;
                } else if(this.componentContains(item, "wikis:file")){
                    spanContent = this._nls.wikiFile;
                } else{
                    spanContent = this._nls.wikiPage;
                }
                return this.buildElement(spanElement, {
                    classAttr: "lotusMeta",
                    content: spanContent
                 });
            }
        },
        
        /**
         * UtilityFunction, used to test if the item application array contains a certain string. Or if it is in the primaryComponent.
         * 
         * @param item
         * @param appString
         */
        componentContains: function(item, appString){
            if(item.getValue("primaryComponent") === appString)
                return true;
            
            for(var key in item.getValue("application")){
                var app = item.getValue("application")[key];
                if(app === appString){
                    return true;
                }
            }
            return false;
        },
        
        communityMembers: function(grid, item, i, items){
            var isCommunitiesEntry = this.componentContains(item, "communities:entry");
            if(!isCommunitiesEntry){
                return this.buildElement(liElement, {
                    content: this._substituteItem(personCardTemplate, grid, item, i, items),
                    classAttr: "lotusFirst",
                    roleAttr: "listitem"
                });
            }
            var liMembersContent = item.getValue("memberCount") + " " + this._nls.members;
            
            var liMembers = this.buildElement(liElement, {
                content: liMembersContent,
                classAttr: "lotusFirst members",
                roleAttr: "listitem"
            });
            
            var liPersonCard = this.buildElement(liElement, {
                content: this._substituteItem(personCardTemplate, grid, item, i, items),
                roleAttr: "listitem"
            });
            
            return liMembers + "\n" + liPersonCard;
        },
        
        bodyCalendarLis: function(grid, item, i, items){
            var allDayEventLi = "", repeatingEventLi = "", locationLi = "";
            
            if(item.getValue("allDayEvent") ==="true")
                allDayEventLi = this.buildElement(liElement,{
                    roleAttr: "listitem",
                    content: this._nls.eventIsAllDay
                });
            
            if(item.getValue("repeatingEvent") ==="true")
                allDayEventLi = this.buildElement(liElement,{
                    roleAttr: "listitem",
                    content: this._nls.eventRepeats
                });
            
            if(item.getValue("location.length") > 1)
                allDayEventLi = this.buildElement(liElement,{
                    roleAttr: "listitem",
                    content: item.getValue("location")
                });
            
            return allDayEventLi + "\n" + repeatingEventLi + "\n" + locationLi + "\n";
        },
        
        bodyBookmarkLiContent : function(grid, item, i, items){
            var contributorCount = parseInt(item.getValue("contributorCount"));
            var authorCount = parseInt(item.getValue("authorcount"));
            
            if(item.getValue("bookmarkLink").length > 0 && (contributorCount + authorCount) > 1){
                var spanA = this.buildElement(aElement, {
                    hrefAttr: item.getValue("bookmarkLink"),
                    content: contributorCount + authorCount + this._nls.people
                });
                return this.buildElement(spanElement, {
                    content: spanA
                });
            }
            else{
                return this._substituteItem(personCardTemplate, grid, item, i, items);
            }
        },
        
        /**
         * Returns the class to be used for a vcard. 
         * 
         * @param grid
         * @param item
         * @param i
         * @param items
         * @returns {String}
         */
        cardClass: function(grid, item, i, items){
            if(item.getValue("authorState") != 'active')
                return "lotusPersonInactive";
            else
                return "vcard";
        },
        
        /**
         * Returns an li with the author's job title if it exists, otherwise an empty string.
         * 
         * @param grid
         * @param item
         * @param i
         * @param items
         * @returns
         */
        profileBodyJobTitle: function(grid, item, i, items){
            if(item.getValue("authorJobTitle") && item.getValue("authorJobTitle").length != 0)
                return this.buildElement(liElement, {
                    content: item.getValue("authorJobTitle")+"&nbsp;",
                    classAttr: "lotusFirst",
                    roleAttr: "listitem"
                });
            else
                return this._nls.emptyString;
        },
        
        /**
         * 
         * 
         * @returns {String}
         */
        bodyPersonCardLi: function(grid, item, i, items){
            if(item.getValue("authorName").length != 0){
                return this.buildElement(liElement, {
                    content: this._substituteItem(personCardTemplate, grid, item, i, items),
                    roleAttr: "listitem",
                    classAttr: "lotusFirst"
                });
            }
            else
                return "";
        },
        
        bodyUpdatedLi: function(grid, item, i, items){
            var liClass = "searchDateClass";
            if(item.getValue("authorName").length==0)
                liClass+= " lotusFirst";
            return this.buildElement(liElement, {
                classAttr: liClass,
                content: this.updatedLabel(grid, item, i, items)
            });
        },
        
        bodyCommentCountLi: function(grid, item, i, items){
            var commentcount = parseInt(item.getValue("commentCount"));
            if(commentcount >= 1){
                var liContent = commentcount === 1 ? this._nls.oneComment : commentcount + " " + this._nls.comments;
                return this.buildElement(liElement, {
                    classAttr: "comments",
                    roleAttr: "listitem",
                    content: liContent
                });
            }
        },
        
        objectReferenceLi: function(grid, item, i, items){
            if(item.getValue("objectRefDisplayName").length != 0 && item.getValue("objectRefUrl").length != 0){
                var liContent = this.buildElement(aElement, {
                    hrefAttr: item.getValue("objectRefUrl"),
                    content: item.getValue("objectRefDisplayName")
                });
                
                return this.buildElement(liElement, {
                    roleAttr: "listitem",
                    content: liContent
                });
            }
        },
        
        bodyBookmarkLi: function(grid, item, i, items){
            var applicationCount = parseInt(item.getValue("applicationCount"));
            if(((this.application=='dogear' && applicationCount > 1) || (this.application=='activities:bookmark' && applicationCount > 2) || (this.application=='communities:bookmark' && applicationCount > 2) ) && item.getValue("accessControl")=='public'){
                var aImg = this.buildElement(imgElement, {
                    classAttr: "lconnSprite lconnSprite-iconHelp16",
                    srcAttr: "images/blank.gif",
                    titleAttr: this._nls.help,
                    altAttr: this._nls.help
                });
                var aSpan = this.buildElement(spanElement, {
                    classAttr: "lotusAltText",
                    content: "?"
                });
                var aResult = this.buildElement(aElement, {
                    classAttr: "lconnSearchBookmarkHelpButton",
                    hrefAttr: "javascript:;",
                    content: aImg + "\n" +  aSpan
                });
                
                var ulContent = "";
                
                if(this.application ==="dogear")
                    ulContent += this.buildElement(liElement, {
                        content: this._nls.bookmarksTitle
                    }) + "\n";
                
                if(this.application ==="activities:bookmark")
                    ulContent += this.buildElement(liElement, {
                        content: this._nls.activitiesTitle
                    }) + "\n";
                
                if(this.application ==="communities:bookmark")
                    ulContent += this.buildElement(liElement, {
                        content: this._nls.communitiesTitle
                    }) + "\n";
                
                var spanUl = this.buildElement(ulElement, {
                    content: ulContent
                });
                
                var divSpan = this.buildelement(spanElement, {
                    classAttr: "lotusLeft lconnSearchBookmarkHelpText",
                    content: spanUl
                });
                var divResult = this.buildElement(divElement, {
                    styleAttr: "display:none",
                    content: divSpan
                });
                
                return aResult + "\n" + divResult + "\n";
            }
        },
        
        /**
         * Returns the commentOn nls string if it is an activities:reply
         * @param grid
         * @param item
         * @param i
         * @param items
         * @returns
         */
        commentOn: function(grid, item, i, items){
            if(this.componentContains(item, "activities:reply"))
                return this._nls.commentOn;
            else
                return this._nls.emptyString;
        },
        
        /**
         * 
         * 
         * @param grid
         * @param item
         * @param i
         * @param items
         * @returns
         */
        ltr: function(grid, item, i, items){
            if(this.componentContains(item, "wikis:file"))
                return 'dir="' + this._nls.ltr + '"';
            else
                return this._nls.emptyString;
        },
        
        inactiveLabel: function(grid, item, i, items){
            if(this.componentContains(item, "profiles"))
                return this._nls.inactiveLabel;
            else
                return this._nls.emptyString;
        },
        
        colspan: function(grid, item, i, items){
            if(!this.componentContains(item, "status_update"))
                return 'colspan="2"';
            else
                return '';
        },
         
        /**
         * Returns the app label (e.g. profile) but capitalised (e.g. Profile).
         *  
         * @param grid
         * @param item
         * @param i
         * @param items
         * @returns
         */
        applicationLabel: function(grid, item, i, items){
            var application = this.application;
            return application.charAt(0).toUpperCase() + application.slice(1);
        },
        
        summaryClass: function(grid, item, i, items){
            if(item.getValue("authorState") === "inactive")
                return "lotusDim";
            else
                return "lconnSearchComponentCategory";
        },
        

        summaryStyle: function(grid, item, i, items){
            if(item.getValue("authorState") === "inactive")
                return "filter: alpha(opacity = 50)";
            else
                return "";
        },
        
        getApplication: function(item){
            if(typeof item.getValue("application") === "string"){
                return item.getValue("application");
            }
            else{
                for(var key in item.getValue("application")){
                    var app = item.getValue("application")[key];
                    if(app.indexOf(":") ===-1){
                        return app;
                    }
                }
            }
        },
        
        getResultType: function(item){
            var primaryComponent = item.getValue("primaryComponent");
            var resultTypes = this.resultTypes;
            
            this.application = this.getApplication(item);
            
            switch(this.application){
            case "blogs": 
                return resultTypes.blogs;
            case "calendar": 
                return resultTypes.calendar;
            case "dogear": 
                return resultTypes.bookmark;
            case "files": 
                return resultTypes.files;
            case "forums": 
                return resultTypes.forums;
            case "profiles":
                return resultTypes.profiles;
            case "status_updates": 
                return resultTypes.statusUpdates;
            case "wikis": 
                return resultTypes.wikis;
                
            }
            
            if(primaryComponent.indexOf("activities") === 0 || primaryComponent.indexOf("communities:activities") === 0){
                if(primaryComponent === "activities:bookmark" || primaryComponent === "communities:activities:bookmark"){
                    return resultTypes.bookmarks;
                }else{
                    return resultTypes.activities;
                }
            } else if (primaryComponent.indexOf("communities") === 0){
                if(primaryComponent === "communities:bookmark"){
                    return resultTypes.bookmark;
                }else{
                    return resultTypes.communities;
                }
            }
            
        },
        
        getRowContent: function(resultType){
            var resultTypes = this.resultTypes;
            switch(resultType){
            case resultTypes.activities:
                return defaultHeaderTemplate + defaultBodyTemplate + defaultSummaryTemplate;
            case resultTypes.blogs: 
                return defaultHeaderTemplate + defaultBodyTemplate + defaultSummaryTemplate;
            case resultTypes.bookmark: 
                return defaultHeaderTemplate + bookmarkBodyTemplate + defaultSummaryTemplate;
            case resultTypes.calendar: 
                return defaultHeaderTemplate + calendarBodyTemplate + defaultSummaryTemplate;
            case resultTypes.communities: 
                return defaultHeaderTemplate + communityBodyTemplate + defaultSummaryTemplate;
            case resultTypes.files: 
                return defaultHeaderTemplate + defaultBodyTemplate + defaultSummaryTemplate;
            case resultTypes.forums: 
                return defaultHeaderTemplate + defaultBodyTemplate + defaultSummaryTemplate;
            case resultTypes.profiles:
                return profileHeaderTemplate + profileBodyTemplate + defaultSummaryTemplate;
            case resultTypes.statusUpdates: 
                return statusUpdateHeaderTemplate + defaultBodyTemplate + defaultSummaryTemplate;
            case resultTypes.wikis: 
                return defaultHeaderTemplate + defaultBodyTemplate + defaultSummaryTemplate;
            default: return defaultHeaderTemplate + defaultBodyTemplate + defaultSummaryTemplate;
            }
        },
        
        /**
         * Returns a tr with the correct content templates for a particular search result.
         * @param item
         * @param i
         * @returns
         */
        getTemplate: function(item, i){
            this.resultType = this.getResultType(item);
            var tdContent = this.getRowContent(this.resultType);
            
            // Build tr, adding attributes and content.
            var trClass = undefined;
            if(i===0)
                trClass = "lotusFirst";
            var tdColspan = undefined;
            if(true)
                tdColspan = "2";
            var trContent = this.buildElement(tdElement, {
                content: tdContent,
                colspanAttr: tdColspan
            });
            
            if(this.resultType === this.resultTypes.statusUpdates){
                var statusUpdateExtraHeader = this.buildElement(tdElement, {
                    content: statusUpdateExtraHeaderTemplate,
                    widthAttr: "65",
                    heightAttr: "55",
                    classAttr: "lotusFirstCell"
                });

                trContent = statusUpdateExtraHeader + trContent;
            }
            
            return this.buildElement(trElement, {
                content: trContent,
                classAttr: trClass,
            });
        },
        
        createdLabel: function(grid, item, i, items){
            var result = i18n.getSearchUpdatedLabel(item.getValue('created'));
            return result;
        },
        
        updatedLabel: function(grid, item, i, items){
            var result = i18n.getSearchUpdatedLabel(item.getValue('updated'));
            return result;
        },
        
        /**
         * 
         * @param args
         */
        constructor: function(args) {
            
        },
        
        emptyClass: "lconnEmpty lotusui",
        
        //TODO Handle empty grid. Should override renderEmpty?
        
        renderItem : function(grid, el, data, item, i, items){
            this.template = this.getTemplate(item, i);
            
            this.inherited(arguments);
        },
        
        /**
         * Creates a Div, with a different CSS class, to display a grid that has no results
         * @method - renderEmpty
         * @param - grid - The Grid
         * @param - el - The Current Element
         */
        renderEmpty: function(grid, el) {
           while (el.childNodes[0]) {
               this._destroy(el.childNodes[0]);
           }
           var lotusUiDiv = this._create("div", { // here purely so a parent of the empty div has the lotusui class...
             "class": "lotusui lconnSearchResults",
             innerHTML: ""
           }, el);
           var lotusEmptyDiv = this._create("div", {
               "class": this.emptyClass,
               innerHTML: "",
               "aria-relevant": "all",
               "aria-live": "assertive"
             }, lotusUiDiv);
           this._create("span", {
               innerHTML: this._nls.empty,
             }, lotusEmptyDiv);
        },
        
        tableClass: "lotusTable lconnSearchResults"
    });    
    return searchGridRenderer;
});