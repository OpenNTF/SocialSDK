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
define(["sbt/declare",
        "sbt/controls/grid/connections/ConnectionsGridRenderer",
        "dojo/_base/lang", "dojo/string", "sbt/i18n",
        "dojo/i18n!sbt/controls/grid/connections/nls/SearchGridRenderer",
        "dojo/text!sbt/controls/grid/connections/templates/search/BookmarkBody.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/CalendarBody.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/CommunityBody.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/DefaultBody.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/DefaultHeader.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/DefaultSummary.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/ProfileBody.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/ProfileHeader.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/PersonCard.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/StatusUpdateExtraHeader.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/StatusUpdateHeader.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/a.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/tr.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/li.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/ul.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/span.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/img.html",
        "dojo/text!sbt/controls/grid/connections/templates/search/em.html"],
        function(declare, ConnectionsGridRenderer, lang, string, i18n, nls, bookmarkBodyTemplate, calendarBodyTemplate, communityBodyTemplate, defaultBodyTemplate, defaultHeaderTemplate, defaultSummaryTemplate, profileBodyTemplate, profileHeaderTemplate, personCardTemplate, statusUpdateExtraHeaderTemplate, statusUpdateHeaderTemplate, aElement, trElement, liElement, ulElement, spanElement, imgElement, emElement) {

    /**
     * @class SearchGridRenderer
     * @module sbt.controls.grid.connections.SearchGridRenderer
     * @namespace sbt.controls.grid.connections
     */
    declare("sbt.controls.grid.connections.SearchGridRenderer", ConnectionsGridRenderer, {

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
            
            return string.substitute(html, args);
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
            var tagCount = item.tagCount;
            if(tagCount === 0)
                return "";
            
            var resultLiRole = "listitem";
            
            var ulClass = "lotusInlinelist";
            var ulStyle = "display:inline";
            var ulContent = "";
            
            var tags = item.tags;
            
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
                
                if(item.highlightField)
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
            var summaryImageClass = "lconn-ftype16 lconn-ftype16-" + item.fileExtension, summaryImageSrc = "", summaryImageAlt = "", summaryImageTitle = "", summaryImageRole = "";
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
                summaryImageClass = item.fileExtension ? summaryImageClass : "lconnSprite lconnSprite-iconFiles16";
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
                summaryImageClass = item.fileExtension.length !== 0 ? summaryImageClass : "lconnSprite lconnSprite-iconWikis16";
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
            if(item.summary.length!=0){
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
                    
                var summarySpan = this.buildElement(spanElement, {
                    classAttr: "lotusMeta lconnSearchHighlight",
                    content: item.summary
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
            if(item.commentsSummary.length > 0 && this.resultType != this.resultTypes.statusUpdates){
                var divSpan = this.buildElement(spanElement, {
                    classAttr: "lotusMeta lconnSearchHighlight",
                    content: this._nls.comment + item.commentSummary
                });
                return this.buildElement(divElement, {
                    styleAttr: "clear:both;",
                    content: divSpan
                });
            } else{
                return "";
            }
        },
        
        parentageMeta: function(grid, item, i, items){
            //TODO When search index is updated test that this works with all types of search result. May need to move the span tag into here instead of leaving it in the template. Last switch entry is possibly incorrect.
            switch(item.parentageMetaURLID){
            case 'blogURL':
                var aHref = item.parentageMetaURL;
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
                    hrefAttr: "item.parentageMetaURL",
                    content: this._nls.fromAForum
                }) + " > ";
            case 'wikiURL':
                return this.buildElement(aElement, {
                    hrefAttr: "item.parentageMetaURL",
                    content: this._nls.fromAWiki
                }) + " > ";
            case 'activityURL':
                if(item.primaryComponent.indexOf("activities") === 0 || item.primaryComponent.indexOf("communities:activities") === 0){
                    if(item.primaryComponent === "activities:bookmark" || item.primaryComponent === "communities:activities:bookmark" || this.componentContains("activities:section") || this.componentContains("activities:task") || this.componentContains("activities:entry")){
                        return this.buildElement(aElement, {
                            hrefAttr: "item.parentageMetaURL",
                            content: this._nls.fromAnActivity
                        }) + " > ";
                    }
                    if(item.parentageMetaID === "activityEntryURL" || item.primaryComponent === "activities:bookmark"){
                        return this.buildElement(aElement, {
                            hrefAttr: "item.parentageMetaURL",
                            content: this._nls.entry
                        }) + " > ";
                    }
                }
                
            }
            return "";
        },
        
        communityParent: function(grid, item, i, items){
            if(item.communityUuid.length!=0 && item.containerType != "stand-alone" && item.primaryComponent != "communities:entry" && item.primaryComponent.indexOf("communities") === 0){
                return this.buildElement(aElement, {
                    hrefAttr: item.communityParentLink,
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
                spanContent = this._nls.statusUpdate;
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
            if(item.primaryComponent === appString)
                return true;
            
            for(var key in item.application){
                var app = item.application[key];
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
            var liMembersContent = item.memberCount + " " + this._nls.members;
            
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
            
            if(item.allDayEvent ==="true")
                allDayEventLi = this.buildElement(liElement,{
                    roleAttr: "listitem",
                    content: this._nls.eventIsAllDay
                });
            
            if(item.repeatingEvent ==="true")
                allDayEventLi = this.buildElement(liElement,{
                    roleAttr: "listitem",
                    content: this._nls.eventRepeats
                });
            
            if(item.location.length > 1)
                allDayEventLi = this.buildElement(liElement,{
                    roleAttr: "listitem",
                    content: item.location
                });
            
            return allDayEventLi + "\n" + repeatingEventLi + "\n" + locationLi + "\n";
        },
        
        bodyBookmarkLiContent : function(grid, item, i, items){
            if(item.bookmarkLink.length > 0 && (item.contributorCount + item.authorcount) > 1){
                var spanA = this.buildElement(aElement, {
                    hrefAttr: item.bookmarkLink,
                    content: (item.contributorCount + item.authorCount) + this._nls.people
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
            if(item.authorState != 'active')
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
            if(item.authorJobTitle && item.authorJobTitle.length != 0)
                return this.buildElement(liElement, {
                    content: item.authorJobTitle+"&nbsp;",
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
            if(item.authorName.length != 0){
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
            if(item.authorName.length==0)
                liClass+= " lotusFirst";
            return this.buildElement(liElement, {
                classAttr: liClass,
                content: this.updatedLabel(grid, item, i, items)
            });
        },
        
        bodyCommentCountLi: function(grid, item, i, items){
            if(item.commentCount >= 1){
                var liContent = item.commentCount === 1 ? this._nls.oneComment : item.commentCount + this._nls.comments;
                return this.buildElement(liElement, {
                    classAttr: "comments",
                    roleAttr: "listitem",
                    content: liContent
                });
            }
        },
        
        objectReferenceLi: function(grid, item, i, items){
            if(item.objectRefDisplayName.length != 0 && item.objectRefUrl.length != 0){
                var liContent = this.buildelement(aElement, {
                    hrefAttr: item.objectRefUrl,
                    content: item.objectRefDisplayName
                });
                
                return this.buildElement(liElement, {
                    roleAttr: "listitem",
                    content: liContent
                });
            }
        },
        
        bodyBookmarkLi: function(grid, item, i, items){
            if(((this.application=='dogear' && item.applicationCount > 1) || (this.application=='activities:bookmark' && item.applicationCount > 2) || (this.application=='communities:bookmark' && item.applicationCount > 2) ) && item.accessControl=='public'){
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
            if(item.authorState === "inactive")
                return "lotusDim";
            else
                return "lconnSearchComponentCategory";
        },
        

        summaryStyle: function(grid, item, i, items){
            if(item.authorState === "inactive")
                return "filter: alpha(opacity = 50)";
            else
                return "";
        },
        
        getApplication: function(item){
            if(typeof item.application === "string"){
                return item.application;
            }
            else{
                for(var key in item.application){
                    var app = item.application[key];
                    if(app.indexOf(":") ===-1){
                        return app;
                    }
                }
            }
        },
        
        getResultType: function(item){
            var primaryComponent = item.primaryComponent;
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
            //TODO Update search index and test all of these work!!!!
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
                return statusUpdateExtraHeaderTemplate + statusUpdateHeaderTemplate + defaultBodyTemplate + statusUpdateSummaryTemplate;
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
            //var content = profileHeaderTemplate + profileBodyTemplate + defaultSummaryTemplate;
            this.resultType = this.getResultType(item);
            var content = this.getRowContent(this.resultType);
            
            
            // Build tr, adding attributes and content.
            var trClass = undefined;
            if(i===0)
                trClass = "lotusFirst";
            var trColspan = undefined;
            if(true)
                trColspan = "2";
            return this.buildElement(trElement, {
                content: content,
                classAttr: trClass,
                colspanAttr: trColspan
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
        
        emptyClass: "lconnEmpty",
        
        //TODO Handle empty grid. Should override renderEmpty?
        
        renderItem : function(grid, el, data, item, i, items){
            this.template = this.getTemplate(item, i);
            
            this.inherited(arguments);
        },
        
        tableClass: "lotusTable lconnSearchResults"
    });    
    return sbt.controls.grid.connections.SearchGridRenderer;
});