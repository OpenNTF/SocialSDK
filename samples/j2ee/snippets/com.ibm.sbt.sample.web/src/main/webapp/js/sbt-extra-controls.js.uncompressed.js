require({cache:{
'sbt/control-main':function(){
/*
* © Copyright IBM Corp. 2012
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at:
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an AS IS BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
* implied. See the License for the specific language governing
* permissions and limitations under the License.
*/

/**
* @module sbt.main
*/
define([
    'sbt/widget/_TemplatedWidget',
    'sbt/connections/controls/ConnectionsGridRenderer',
    'sbt/connections/controls/ViewAllAction',
    'sbt/connections/controls/WidgetWrapper',
    'sbt/connections/controls/_ConnectionsWidget',
    'sbt/controls/grid/Grid',
    'sbt/controls/grid/GridAction',
    'sbt/controls/grid/GridRenderer',
    'sbt/controls/grid/ViewProfileAction',
    'sbt/controls/panel/_ProfilePanel',
    'sbt/smartcloud/controls/BaseGridRenderer',
    'sbt/widget/grid/_Grid',
    'sbt/widget/grid/_GridRenderer',
    'sbt/connections/controls/activities/ActivityAction',
    'sbt/connections/controls/activities/ActivityGrid',
    'sbt/connections/controls/activities/ActivityGridRenderer',
    'sbt/connections/controls/astream/ActivityStreamWrapper',
    'sbt/connections/controls/astream/_ActivityStream',
    'sbt/connections/controls/astream/_SbtAsConfigUtil',
    'sbt/connections/controls/astream/_XhrHandler',
    'sbt/connections/controls/bookmarks/BookmarkGrid',
    'sbt/connections/controls/bookmarks/BookmarkGridRenderer',
    'sbt/connections/controls/bootstrap/CommunityRendererMixin',
    'sbt/connections/controls/bootstrap/FileRendererMixin',
    'sbt/connections/controls/bootstrap/ProfileRendererMixin',
    'sbt/connections/controls/communities/CommunityAction',
    'sbt/connections/controls/communities/CommunityGrid',
    'sbt/connections/controls/communities/CommunityGridRenderer',
    'sbt/connections/controls/communities/CommunityMembersAction',
    'sbt/connections/controls/communities/CommunityMembersGrid',
    'sbt/connections/controls/communities/CommunityMembersGridRenderer',
    'sbt/connections/controls/files/FileAction',
    'sbt/connections/controls/files/FileGrid',
    'sbt/connections/controls/files/FileGridRenderer',
    'sbt/connections/controls/forums/BackAction',
    'sbt/connections/controls/forums/ForumAction',
    'sbt/connections/controls/forums/ForumGrid',
    'sbt/connections/controls/forums/ForumGridRenderer',
    'sbt/connections/controls/nls/ConnectionsGridRenderer',
    'sbt/connections/controls/nls/WidgetWrapper',
    'sbt/connections/controls/profiles/ColleagueGrid',
    'sbt/connections/controls/profiles/ColleagueGridRenderer',
    './connections/controls/profiles/EditProfilePhoto.html',
    'sbt/connections/controls/profiles/ProfileAction',
    'sbt/connections/controls/profiles/ProfileGrid',
    'sbt/connections/controls/profiles/ProfileGridRenderer',
    'sbt/connections/controls/profiles/ProfilePanel',
    'sbt/connections/controls/profiles/ProfileTagAction',
    'sbt/connections/controls/profiles/ProfileTagsGrid',
    'sbt/connections/controls/profiles/ProfileTagsGridRenderer',
    'sbt/connections/controls/search/SearchBox',
    'sbt/connections/controls/search/SearchBoxRenderer',
    'sbt/connections/controls/search/SearchGrid',
    'sbt/connections/controls/search/SearchGridRenderer',
    'sbt/connections/controls/sharebox/InputFormWrapper',
    'sbt/connections/controls/sharebox/_InputForm',
    'sbt/connections/controls/templates/FileGridWrapperContent.html',
    'sbt/connections/controls/templates/LoadingPage.html',
    'sbt/connections/controls/templates/ProfileCardWrapperContent.html',
    'sbt/connections/controls/templates/WidgetFrame.html',
    'sbt/connections/controls/vcard/CommunityVCard',
    'sbt/connections/controls/vcard/ProfileVCard',
    'sbt/connections/controls/vcard/ProfileVCardInline',
    'sbt/connections/controls/vcard/SemanticTagService',
    'sbt/connections/controls/wrappers/FileGridWrapper',
    'sbt/connections/controls/wrappers/ProfileCardWrapper',
    'sbt/controls/grid/bootstrap/GridRendererMixin',
    'sbt/controls/grid/templates/Grid.html',
    'sbt/controls/grid/templates/GridFooter.html',
    'sbt/controls/grid/templates/GridPager.html',
    'sbt/controls/grid/templates/GridSorter.html',
    'sbt/controls/grid/templates/SortAnchor.html',
    'sbt/smartcloud/controls/nls/BaseGridRenderer',
    'sbt/smartcloud/controls/profiles/ColleagueGrid',
    'sbt/smartcloud/controls/profiles/ColleagueGridRenderer',
    'sbt/smartcloud/controls/profiles/ProfileAction',
    'sbt/smartcloud/controls/profiles/ProfileGrid',
    'sbt/smartcloud/controls/profiles/ProfileGridRenderer',
    'sbt/smartcloud/controls/profiles/ProfilePanel',
    'sbt/connections/controls/activities/nls/ActivityGridRenderer',
    'sbt/connections/controls/activities/templates/ActivityRow.html',
    'sbt/connections/controls/astream/templates/ActivityStreamContent.html',
    'sbt/connections/controls/bookmarks/nls/BookmarkGridRenderer',
    'sbt/connections/controls/bookmarks/templates/BookmarkRow.html',
    'sbt/connections/controls/bookmarks/templates/TagAnchor.html',
    'sbt/connections/controls/bootstrap/templates/CommunityRow.html',
    'sbt/connections/controls/bootstrap/templates/FileRow.html',
    'sbt/connections/controls/bootstrap/templates/ProfileRow.html',
    'sbt/connections/controls/bootstrap/templates/TagAnchor.html',
    'sbt/connections/controls/communities/nls/CommunityGridRenderer',
    'sbt/connections/controls/communities/nls/CommunityMembersGridRenderer',
    'sbt/connections/controls/communities/templates/CommunityMemberRow.html',
    'sbt/connections/controls/communities/templates/CommunityRow.html',
    'sbt/connections/controls/communities/templates/TagAnchor.html',
    'sbt/connections/controls/files/nls/FileGridRenderer',
    'sbt/connections/controls/files/templates/CommentRow.html',
    'sbt/connections/controls/files/templates/FileRow.html',
    'sbt/connections/controls/files/templates/FolderRow.html',
    'sbt/connections/controls/files/templates/RecycledFileRow.html',
    'sbt/connections/controls/forums/nls/ForumGridRenderer',
    'sbt/connections/controls/forums/templates/ForumRow.html',
    'sbt/connections/controls/forums/templates/MyTopicsBreadCrumb.html',
    'sbt/connections/controls/forums/templates/ReplyBreadCrumb.html',
    'sbt/connections/controls/forums/templates/ReplyHeader.html',
    'sbt/connections/controls/forums/templates/ReplyRow.html',
    'sbt/connections/controls/forums/templates/TableHeader.html',
    'sbt/connections/controls/forums/templates/TopicBreadCrumb.html',
    'sbt/connections/controls/forums/templates/TopicHeader.html',
    'sbt/connections/controls/forums/templates/TopicRow.html',
    'sbt/connections/controls/profiles/nls/ColleagueGridRenderer',
    'sbt/connections/controls/profiles/nls/ProfileGridRenderer',
    'sbt/connections/controls/profiles/nls/ProfileTagsGridRenderer',
    'sbt/connections/controls/profiles/templates/ColleagueItem.html',
    'sbt/connections/controls/profiles/templates/ColleagueItemFull.html',
    'sbt/connections/controls/profiles/templates/ColleagueRow.html',
    'sbt/connections/controls/profiles/templates/CommunityMemberRow.html',
    'sbt/connections/controls/profiles/templates/ProfilePanel.html',
    'sbt/connections/controls/profiles/templates/ProfileRow.html',
    'sbt/connections/controls/profiles/templates/SharedConnectionsRow.html',
    'sbt/connections/controls/profiles/templates/StatusUpdateRow.html',
    'sbt/connections/controls/profiles/templates/TagListHeader.html',
    'sbt/connections/controls/profiles/templates/TagListRow.html',
    'sbt/connections/controls/profiles/templates/ViewAll.html',
    'sbt/connections/controls/search/nls/SearchBoxRenderer',
    'sbt/connections/controls/search/nls/SearchGridRenderer',
    'sbt/connections/controls/search/templates/BookmarkBody.html',
    'sbt/connections/controls/search/templates/CalendarBody.html',
    'sbt/connections/controls/search/templates/CommunityBody.html',
    'sbt/connections/controls/search/templates/DefaultBody.html',
    'sbt/connections/controls/search/templates/DefaultHeader.html',
    'sbt/connections/controls/search/templates/DefaultSummary.html',
    'sbt/connections/controls/search/templates/MemberListItemTemplate.html',
    'sbt/connections/controls/search/templates/MemberListTemplate.html',
    'sbt/connections/controls/search/templates/NoResults.html',
    'sbt/connections/controls/search/templates/PersonCard.html',
    'sbt/connections/controls/search/templates/PopUpTemplate.html',
    'sbt/connections/controls/search/templates/ProfileBody.html',
    'sbt/connections/controls/search/templates/ProfileHeader.html',
    'sbt/connections/controls/search/templates/SearchBoxTemplate.html',
    'sbt/connections/controls/search/templates/SearchSuggestTemplate.html',
    'sbt/connections/controls/search/templates/SingleApplicationSearch.html',
    'sbt/connections/controls/search/templates/SingleSearchPopUp.html',
    'sbt/connections/controls/search/templates/StatusUpdateExtraHeader.html',
    'sbt/connections/controls/search/templates/StatusUpdateHeader.html',
    'sbt/connections/controls/search/templates/SuggestPopUpTemplate.html',
    'sbt/connections/controls/search/templates/a.html',
    'sbt/connections/controls/search/templates/div.html',
    'sbt/connections/controls/search/templates/em.html',
    'sbt/connections/controls/search/templates/img.html',
    'sbt/connections/controls/search/templates/li.html',
    'sbt/connections/controls/search/templates/span.html',
    'sbt/connections/controls/search/templates/td.html',
    'sbt/connections/controls/search/templates/tr.html',
    'sbt/connections/controls/search/templates/ul.html',
    'sbt/connections/controls/sharebox/templates/InputFormContent.html',
    'sbt/connections/controls/vcard/templates/CommunityVCard.html',
    'sbt/connections/controls/vcard/templates/ProfileVCard.html',
    'sbt/connections/controls/vcard/templates/ProfileVCardInline.html',
    'sbt/controls/grid/bootstrap/templates/GridPager.html',
    'sbt/controls/grid/bootstrap/templates/GridSorter.html',
    'sbt/controls/grid/bootstrap/templates/SortAnchor.html',
    'sbt/smartcloud/controls/profiles/nls/ColleagueGridRenderer',
    'sbt/smartcloud/controls/profiles/nls/ProfileGridRenderer',
    'sbt/smartcloud/controls/profiles/templates/ColleagueItem.html',
    'sbt/smartcloud/controls/profiles/templates/ColleagueItemFull.html',
    'sbt/smartcloud/controls/profiles/templates/ProfilePanel.html',
    'sbt/smartcloud/controls/profiles/templates/ProfileRow.html',
    'sbt/smartcloud/controls/profiles/templates/ViewAll.html'
],function() {
       return;
});

},
'sbt/widget/_TemplatedWidget':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../_bridge/declare",
         "dojo/_base/lang", "dojo/string", "dojo/dom-construct", "dojo/_base/connect", "dojo/touch", "dijit/_WidgetBase", "dijit/_TemplatedMixin"], 
        function(declare, lang, string, domConstruct, connect, touch, _WidgetBase, _TemplatedMixin) {

    /**
     * @module sbt.widget._TemplatedWidget
     */
    var _TemplatedWidget = declare([ dijit._WidgetBase, dijit._TemplatedMixin ], {
        
        _blankGif: dijit._WidgetBase.prototype._blankGif,

        _place: function(node, refNode, pos) {
        	domConstruct.place(node, refNode, pos);
        },
        
        _stopEvent: function(event) {
            dojo.stopEvent(event);
        },
        
        _connect: function(object, event, method) {
            // TODO handle disconnecting when widget is destroyed
            return connect.connect(object, touch[event] || event, method);
        },
        
        _mixin: function(dest,sources) {
        	return lang.mixin.apply(this, arguments);
        },
        
        _destroy: function(node) {
            domConstruct.destroy(node);
        },
        
        _create: function(name, attribs, parent) {
            return domConstruct.create(name, attribs, parent);
        },
        
        _toDom: function(template, parent) {
            return domConstruct.toDom(template, parent);
        },
        
        _isString: function(obj) {
            return lang.isString(obj);
        },
        
        _substitute: function(template, map, transform, thisObject) {
        	return string.substitute(template, map, transform, thisObject);
        },
        
        _getObject: function(name, create, context) {
            return lang.getObject(name, create, context);
        },
        
        _hitch: function(scope, method) {
            if (arguments.length > 2) {
                return lang._hitchArgs.apply(dojo, arguments);
            } else {
                return lang.hitch(scope, method);
            }
        },
        
        _doAttachPoints: function(scope,el){
       	 var nodes = (el.all || el.getElementsByTagName("*"));
	            for (var i in nodes) {
	                var attachPoint = (nodes[i].getAttribute) ? nodes[i].getAttribute("data-dojo-attach-point") : null;
	                if (attachPoint) {
	                	
	                	var att = nodes[i].getAttribute("data-dojo-attach-point");
	                	scope[att] = el;
	                }
	            }
       },      
        _doAttachEvents: function(el, scope) {
            var nodes = (el.all || el.getElementsByTagName("*"));
            for (var i in nodes) {
                var attachEvent = (nodes[i].getAttribute) ? nodes[i].getAttribute(this.AttachEventAttribute) : null;
                if (attachEvent) {
                    nodes[i].removeAttribute(this.AttachEventAttribute);
                    var event, events = attachEvent.split(/\s*,\s*/);
                    while((event = events.shift())) {
                        if (event) {
                            var func = null;
                            if (event.indexOf(":") != -1) {
                                var eventFunc = event.split(":");
                                event = this._trim(eventFunc[0]);
                                func = this._trim(eventFunc[1]);
                            } else {
                                event = this._trim(event);
                            }
                            if (!func) {
                                func = event;
                            }
                            var callback = this._hitch(this, this[func], nodes[i], scope);
                            this._connect(nodes[i], event, callback);
                        }
                    }
                }
            }
        }
    
    });
    
    return _TemplatedWidget;
});
},
'sbt/_bridge/declare':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * 
 * declare() function.
 */
define(['dojo/_base/declare'],function(declare) {
	return declare;
});

},
'sbt/connections/controls/ConnectionsGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../declare", "../../controls/grid/GridRenderer",
        "../../text!../../controls/grid/templates/GridPager.html", 
        "../../text!../../controls/grid/templates/GridSorter.html",
        "../../text!../../controls/grid/templates/SortAnchor.html",
        "../../i18n!./nls/ConnectionsGridRenderer",
        "../../text!../../controls/grid/templates/GridFooter.html"],
        function(declare, GridRenderer, GridPager, GridSorter, SortAnchor, nls, GridFooter) {

    /**
     * @module sbt.connections.controls.ConnectionsGridRenderer
     * @class ConnectionsGridRenderer
     * @namespace sbt.connections.controls
     */
    var ConnectionsGridRenderer = declare(GridRenderer, {
    	
    	/**Strings used in the grid*/
        nls : {},
        /**CSS class for tables*/
        tableClass : "lotusTable",
        /**CSS Class for empty icon*/
        emptyClass : "lconnEmpty",
        /**CSS Class for an error on an icon*/
        errorClass : "lconnEmpty",
        /**The css class to use when the grid is loading,null here as an image is used instead*/
        loadingClass : "",
        /**The loading image*/
        loadingImgClass : "lotusLoading",
        /**The css class for the first row of the grid*/
        firstClass : "lotusFirst",
        /**CSS classes for sorting*/
        defaultSortClass : "lotusActiveSort lotusDescending",
        ascendingSortClass : "lotusActiveSort lotusAscending",
        descendingSortClass : "lotusActiveSort lotusDescending",
        /**The HTML template to use to show paging (moving forward and backward through sets of results)*/
        pagerTemplate : GridPager,
        /**The HTML template to use to show the grid footer */
        footerTemplate : GridFooter,
        /**The HTML template to show sorting options*/
        sortTemplate : GridSorter,
        /**The HTML template for sort Anchors*/
        sortAnchor : SortAnchor,

        /**
         * Merge this class with the GridRenderer Class
         * @method constructor
         * @param args
         */
        constructor : function(args) {
            this._mixin(this, args);
            this.nls = this._mixin(nls, this._nls);
        }

    });

    return ConnectionsGridRenderer;
});
},
'sbt/declare':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK - declare() function.
 * 
 * @module sbt.declare
 */
define(['./_bridge/declare'],function(declare) {
	return declare;
});

},
'sbt/controls/grid/GridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../declare", "../../dom", "../../lang", "../../widget/grid/_GridRenderer" ], 
         function(declare, dom, lang, _GridRenderer) {

    /**
     * @module sbt.controls.grid.GridRenderer
     * @class  GridRenderer
     * @namespace  sbt.controls.grid
     */
    var GridRenderer = declare([ _GridRenderer ], {
        /**
         * Strings used in the grid
         */
    	nls: null,
    	
    	/**
    	 * CSS class to be used for tables - see ConnectionsGridRenderer
    	 */
        tableClass: null,
        emptyClass: null,
        errorClass: null,
        loadingClass: null,
        loadingImgClass: null,
        
        /**
         * Constructor function
         * @method - constructor
         */
        constructor: function(args) {
           lang.mixin(this, args);
        },
        
        /**
         * Function to render the Paging , sorting and the table
         * @method - render
         * @param grid - the grid
         * @param el - the grid DOM node
         * @param items - the items in the grid row, for example icon, displayName,email etc.
         * @param data - the data associated with the grid row
         */
        render: function(grid, el, items, data) {
           while (el.childNodes[0]) {
               this._destroy(el.childNodes[0]);
           }
           var size = items.length;
           if (size === 0) {
              this.renderEmpty(grid, el);
           }
           else {
              this.renderPager(grid, el, items, data);
              this.renderSorter(grid, el, data);
              var tbody = this.renderTable(grid, el, items, data);
              for (var i=0; i<items.length; i++) {
                  this.renderItem(grid, tbody, data, items[i], i, items);
              }
              this.renderFooter(grid, el, items, data);
           }
        },
        
        /**
         * Checks if a pagerTemplate exists ,if so, the HTML template is converted to
         * a DOM node, and attached to the body.
         * @method - renderPager
         * @param grid - the grid
         * @param el - the current element
         * @param items - the items in the current element
         * @param data - the data associated with the current element
         */
        renderPager : function(grid,el,items,data) {
            if (this.pagerTemplate && !grid.hidePager) {
                var node;
                if (this._isString(this.pagerTemplate)) {
                    var domStr = this._substituteItems(this.pagerTemplate, grid, this, items, data);
                    node = this._toDom(domStr, el.ownerDocument);
                } else {
                    node = this.pagerTemplate.cloneNode(true);
                }
                el.appendChild(node);
                
                this._doAttachEvents(grid, el, data);
            }
        },
        
        /**
         * Checks if a footerTemplate exists ,if so, the HTML template is converted to
         * a DOM node, and attached to the body.
         * @method - renderFooter
         * @param grid - the grid
         * @param el - the current element
         * @param items - the items in the current element
         * @param data - the data associated with the current element
         */
        renderFooter : function(grid,el,items,data) {
            if (this.footerTemplate && !grid.hidePager) {
                var node;
                if (this._isString(this.footerTemplate)) {
                    var domStr = this._substituteItems(this.footerTemplate, grid, this, items, data);
                    node = this._toDom(domStr, el.ownerDocument);
                } else {
                    node = this.footerTemplate.cloneNode(true);
                }
                el.appendChild(node);
                
                this._doAttachEvents(grid, el, data);
            }
        },
        
        /**
         * Converts an HTML sortTemplate to a DOM node and attaches it 
         * @method - renderSorter
         * @param grid - the Grid
         * @param el - the current element
         * @param data - the data associated with the current element
         */
        renderSorter : function(grid,el,data) {
            if (this.sortTemplate && !grid.hideSorter) {
                var sortInfo = grid.getSortInfo();
                if (sortInfo) {
                    var node;
                    if (this._isString(this.sortTemplate)) {
                        var domStr = this._substituteItems(this.sortTemplate, grid, this, sortInfo);
                        node = this._toDom(domStr, el.ownerDocument);
                    } else {
                        node = this.sortTemplate.cloneNode(true);
                    }
                    el.appendChild(node);
                    
                    this._doAttachEvents(grid, el, data);
                }
            }
        },
        
        /***
         * Creates a table and table body, Attaches the table body to the 
         * table, and returns the table body
         * @method - renderTable
         * @param grid - the grid
         * @param el - the current element
         * @param items - all of the items in the current row
         * @param data - the data associated with the current row
         * @returns - A table body element, that is attached to a table
         */
        renderTable: function(grid, el, items, data) {       	
            var table = this._create("table", {
                "class": this.tableClass,
                border:"0",
                cellspacing:"0",
                cellpadding:"0",
               // summary:this.nls.summary
                role:"presentation"
            }, el);
            var tbody = this._create("tbody", null, table);
            return tbody;
        },
        
        /**
         * Creates a DIV and attaches it to the current element
         * Then creates the loading image and attaches it to the DIV 
         * @method - renderLoading
         * @param grid - The grid
         * @param el - The Current Element
         */
        renderLoading: function(grid, el) {
           var div = this._create("div", {
              "class": this.loadingClass,
              innerHTML: this.nls.loading
           }, el, "only");
           var img = this._create("img", {
              "class": this.loadingImgClass,
              src: this._blankGif,
              role: "presentation"
           }, div, "first");
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
           var ediv = this._create("div", {
             "class": this.emptyClass,
             innerHTML: this.nls.empty,
             role: "document",
             tabIndex: 0
           }, el, "only");
           //dijit.setWaiState(ediv, "label", this.nls.empty);
        },
        
        /**Creates a div to display a grid that has returned an error
         * @method - renderError
         * @param - grid - The Grid
         * @param - el - The Current Element
         * @param - error - The error message to be displayed*/
        renderError: function(grid, el, error) {
            while (el.childNodes[0]) {
                this._destroy(el.childNodes[0]);
            }
           var ediv = this._create("div", {
              "class": this.errorClass,
              innerHTML: error,
              role: "alert",
              tabIndex: 0
            }, el, "only");
        },
        
        /**
         * @method getSortInfo
         */
        getSortInfo: function() {
        },       
        
        /**
         * Returns the paging results,  - How many pages of results there are
         * @method - pagingResults
         * @return - A String for paging - for example "0 - 5 Of 5"
         */
        pagingResults : function(grid,renderer,items,data) {
            return this._substitute(renderer.nls.pagingResults, data);
        },
        
        /**If the user is on the first page of results, they cannot click
         * to go back a page, this function hides the back link
         * @method - hidePreviousLink
         * @return - A String used as a CSS style 
         */
        hidePreviousLink : function(grid,renderer,items,data) {
            return (data.start > 1) ? "" : "display: none;";
        },
        
        /**Hides the back page label
         * @method - hidePreviousLabel
         * @return - A String used as a CSS style
         */
        hidePreviousLabel : function(grid,renderer,items,data) {
            return (data.start == 0) ? "" : "display: none;";
        },
        
        /**If there is only one page of results the user cannot move forward 
         * to the next page, this function hides the next link
         * @method - hideNextLink
         * @return - A String used as CSS style
         */
        hideNextLink : function(grid,renderer,items,data) {
            return (data.start + data.count < data.totalCount) ? "" : "display: none;";
        },
        
        /**
         * If there is only one page of results the user cannot move forward 
         * to the next page, this function hides the next label
         * @method - hideNextLabel
         * @return - A String used as CSS style
         */
        hideNextLabel : function(grid,renderer,items,data) {
            return (data.start + data.count >= data.totalCount) ? "" : "display: none;";
        },

        /**
         * @method updateSort
         * @param el
         */
        updateSort : function(el) {
        },

        /**
         * @method renderSort
         * @param el
         * @param sort
         * @param i
         * @param sorts
         */
        renderSort : function(el,sort,i,sorts) {
        },
        
        /**
         * A sort anchor is what the grid is sorted against.
         * For example sort by name, date etc. This function returns a string of HTML links
         * @method - sortAnchors
         * @param grid - the grid
         * @param renderer - the associated renderer
         * @param items - items contains a list of sort anchors, which will be substituted into the html
         * @returns {String} - a HTML string consisting of the various sort anchors
         */
        sortAnchors : function(grid,renderer,items) {
            if (items.list == undefined || items.list.length == 0) {
                return "";
            } else {
                var sortStr = "";
                for ( var i = 0; i < items.list.length; i++) {
                    sortStr += this._substituteItem(this.sortAnchor, grid, items.list[i], i, items);
                }
                return sortStr;
            }
        },

        /**
         * @method sortItemClass
         * @param grid
         * @param item
         * @param i
         * @param items
         * @returns
         */
        sortItemClass : function(grid,item,i,items) {
            return (i === 0 ? this.firstClass : "");
        },
        
        /**
         * Sets the CSS class for the sort anchor which is currently being used
         * Also sets CSS classes for whether the results are ascending or descending
         * @method - sortAnchorClass
         * @param grid - The Grid
         * @param item - the current sort anchor - for example "name"
         * @param i - the number of the grid row
         * @param items - all of the sort anchors
         * @returns - A CSS class
         */
        sortAnchorClass : function(grid,item,i,items) {
            if(item !== items.active.anchor) {
                return "";
            }
            
            if(items.active.isDesc) {
                return this.descendingSortClass;
            }
            else {
                return this.ascendingSortClass;
            }
        },
        
        /**
         * Converts the HTML template into a dom node and attaches it.
         * @method - rendeItem
         * @param grid - the grid
         * @param el - the current element
         * @param data - the data for the current row, ie name, last updated etc
         * @param item - the current item and its associated data
         * @param i - the number of the grid row
         * @param items - All of the elements & data for each row
         */
        renderItem : function(grid,el,data,item,i,items) {
            if (this.template) {
                var node;
                if (this._isString(this.template)) {
                    var domStr = this._substituteItem(this.template, grid, item, i, items);
                    node = this._toDom(domStr, el.ownerDocument);
                } else {
                    node = this.template.cloneNode(true);
                }
                el.appendChild(node);
                
                this._doAttachEvents(grid, el, item);
            }
        },

        // Internals

        /*
         * Does substitution of ${foo} type properties in template string
         */
        _substituteItem : function(template,grid,item,i,items) {
            var self = this;
            return this._substitute(template, item, function(value,key) {
                if (typeof value == "undefined") {
                    // check the renderer for the property
                    value = this._getObject(key, false, self);
                }

                if (typeof value == 'function') {
                    // invoke function to return the value
                    try {
                        value = value.apply(self, [grid, item, i, items]);
                    } catch (ex) {
                        value = "ERROR:" + key + " " + ex;
                    }
                }
                
                if (typeof value == "undefined" && typeof item.getValue == 'function') {
                    // invoke function to return the value
                    try {
                        value = item.getValue(key);
                    } catch (ex) {
                        value = "ERROR:" + key + " " + ex;
                    }
                }

                if (typeof value == "undefined" || value == null) {
                    return "";
                }

                return value;
            }, this);
        },

        _substituteItems : function(template,grid,renderer,items,data) {
            var self = this;
            return this._substitute(template, renderer, function(value,key) {
                if (typeof value == "undefined") {
                    // check the renderer for the property
                    value = this._getObject(key, false, self);
                }

                if (typeof value == 'function') {
                    // invoke function to return the value
                    try {
                        value = value.apply(self, [grid, renderer, items, data]);
                    } catch (ex) {
                        value = "ERROR:" + key + " " + ex;
                    }
                }

                if (typeof value == "undefined" || value == null) {
                    return "";
                }

                return value;
            }, this);
        },
        
        _doAttachEvents: function(grid, el, data) {
            var nodes = (el.all || el.getElementsByTagName("*"));
            for (var i in nodes) {
                var attachEvent = (nodes[i].getAttribute) ? nodes[i].getAttribute("data-dojo-attach-event") : null;
                if (attachEvent) {
                    nodes[i].removeAttribute("data-dojo-attach-event");
                    var event, events = attachEvent.split(/\s*,\s*/);
                    while((event = events.shift())) {
                        if (event) {
                            var func = null;
                            if (event.indexOf(":") != -1) {
                                var eventFunc = event.split(":");
                                event = this._trim(eventFunc[0]);
                                func = this._trim(eventFunc[1]);
                            } else {
                                event = this._trim(event);
                            }
                            if (!func) {
                                func = event;
                            }
                            var callback = this._hitch(grid, grid[func], nodes[i], data);
                            grid._connect(nodes[i], event, callback);
                        }
                    }
                }
            }
        },
        
        getProfileUrl: function(grid,id){
        	var endpoint = grid.store.getEndpoint();
        	var profileURL = "/${profiles}/html/profileView.do?userid="+id;
        	profileURL = grid.constructUrl(profileURL,{},{},endpoint);
        	profileURL = endpoint.baseUrl+profileURL;
        	return profileURL;
        }
        
    });
    
    return GridRenderer;
});

},
'sbt/dom':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Definition of some DOM utilities.
 * 
 * @module sbt.dom
 */
define(['./_bridge/dom'],function(dom) {
	// The actual implementation is library dependent
	// NOTE: dom.byId returns either a DOM element or false (null/undefined) 
	return dom;
});

},
'sbt/_bridge/dom':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK - Some DOM utilities.
 */
define(['dojo/dom','dojo/_base/window', 'dojo/dom-construct', 'dojo/dom-class'],function(dom,win,domConstruct,domClass) {
	return {
		byId: function(id) {
			return dom.byId(id);
		},
		createTextNode: function(text) {
			//return dojo.doc.createTextNode(text);
			//change also made to define, added 'dojo/_base/window'
			return win.doc.createTextNode(text);
		},
		create: function(element, props, refNode) {
			return domConstruct.create(element, props, refNode);
		},
        destroy: function(node) {
            return domConstruct.destroy(node);
        },
		removeAll: function(node) {
			node = this.byId(node);
			if(node) {
				while(node.firstChild) node.removeChild(node.firstChild);
			}
            return node;
		},
		setText: function(node,text) {
			node = this.byId(node);
			if(node) {
				this.removeAll(node);
				node.appendChild(this.createTextNode(text)); 		
			}
            return node;
		},	
		setAttr: function(node,attr,text) {
			node = this.byId(node);
			if(node) {
				if(text) {
					node.setAttribute(attr,text);
				} else {
					node.removeAttribute(attr);
				}
			}
            return node;
		}	
	};
});
},
'sbt/lang':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK - Some language utilities.
 */
define(['./_bridge/lang'],function(lang) {
	// The actual implementation is library dependent
	return lang;
});

},
'sbt/_bridge/lang':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * Social Business Toolkit SDK - Some language utilities.
 */
define(['dojo/_base/lang'],function(lang) {
	return {
		mixin: function(dest,sources) {
		    return lang.mixin.apply(this, arguments);
		},
		isArray: function(o) {
			return lang.isArray(o);
		},
		isString: function(o) {
			return lang.isString(o);
		},
        isFunction: function(o) {
            return typeof o == 'function';
        },
        isObject: function(o) {
            return typeof o == 'object';
        },
		clone: function(o) {
			return lang.clone(o);
		},
        concatPath: function() {
        	var a = arguments;
        	if(a.length==1 && this.isArray(a[0])) {
        		a = a[0];
        	}
        	var s = "";
        	for(var i=0; i<a.length; i++) {
        		var o = a[i].toString();
        		if(s) {
        			s = s + "/";
        		}
        		s = s + (o.charAt(0)=='/'?o.substring(1):o);
        	}
        	return s;
        },
        trim: function(str) {
            return lang.trim(str);
        }
	};
});
},
'sbt/widget/grid/_GridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../_bridge/declare", "dojo/_base/lang", "dojo/string", "dojo/dom-construct", "dijit/_WidgetBase", "dojo/dom-class"], 
         function(declare, lang, string, domConstruct, _WidgetBase, domClass) {

    /*
     * @module sbt.widget.grid._GridRenderer
     */
    var _GridRenderer = declare(null, {
        
        _blankGif: dijit._WidgetBase.prototype._blankGif,
        
        /*
         * Utility to mix to objects or classes together
         */
        _mixin: function(dest,sources) {
        	return lang.mixin.apply(this, arguments);
        },
        
        /*
         * Destroy a DOM node
         * @node - the node to be destroyed
         */
        _destroy: function(node) {
            domConstruct.destroy(node);
        },
        
        /*
         * Create a DOM node
         * @name - the name of the element, for example DIV
         * @attribs - the attributes of the element, for example id
         * @parent - the element to which the newly created node will be attached to
         */
        _create: function(name, attribs, parent) {
            return domConstruct.create(name, attribs, parent);
        },
        
        /*
         * Similar to create, takes an HTML String an converts it into a DOM node
         * This function takes a HTML template which is converted to a DOM node
         * @template - the HTML template to be used
         * @parent - the parent node that the newly created node will attach to
         */
        _toDom: function(template, parent) {
            return domConstruct.toDom(template, parent);
        },
        
        /*
         * Check if an objects type is String
         */
        _isString: function(obj) {
            return lang.isString(obj);
        },
        
        /*
         * Performs parameterized substitutions on a string.
         */
        _substitute: function(template, map, transformer, thisObject) {
        	if (!transformer) {
        		transformer = function(value, key) {
                    if (typeof value == "undefined") {
                        // check the renderer for the property
                        value = this._getObject(key, false, self);
                    }

                    if (typeof value == "undefined" || value == null) {
                        return "";
                    }

                    return value;
        		};
        	}
        	if (!thisObject) {
        		thisObject = this;
        	}
        	return string.substitute(template, map, transformer, thisObject);
        },
        
        /*
         * Get an object
         */
        _getObject: function(name, create, context) {
            return lang.getObject(name, create, context);
        },
        
        /*
         * Removes White space from the beginning and end of a string
         * @str - the string to remove white space from
         */
        _trim: function(str) {
            return lang.trim(str);
        },
       
        /*
         * A function that allows a function to execute in a different scope
         * @scope - the scope you wish to execute in
         * @method - the function to execute
         */
        _hitch: function(scope, method) {
            if (arguments.length > 2) {
                return lang._hitchArgs.apply(dojo, arguments);
            } else {
                return lang.hitch(scope, method);
            }
        },
        
        /*
         * Add a CSS class
         */
        _addClass: function(node,className){
			return domClass.add(node,className);
		},
		
		/*
		 * Remove a CSS class
		 */
		_removeClass: function(node, className){
			return domClass.remove(node,className);
		}
               
    });
    
    return _GridRenderer;
});
},
'sbt/_bridge/text':function(){
/*
 * � Copyright IBM Corp. 2012
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
 */
define([ "dojo/text" ], function(text) {
    var load = function(id,require,load) {
        text.load(id, require, load);
    };
    return {
        load : load
    };
});

},
'sbt/_bridge/i18n':function(){
/*
 * � Copyright IBM Corp. 2013
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
 */
define(['dojo/i18n', 'dojo/date/locale'],function(i18n, dateLocale) {
	    var load = function(id, require, callback){	    	
	    	i18n.load(id, require, callback); 
	    };
	    
	    return {
	    	load : load,
	    	
	        getLocalizedTime: function(date) {
	            return dateLocale.format(date, { selector:"time",formatLength:"short" });
	        },
	            
	        getLocalizedDate: function(date) {
	            return dateLocale.format(date, { selector:"date",formatLength:"medium" });
	        }
	    }; 
});



},
'sbt/connections/controls/ViewAllAction':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.connections.controls.ViewAllAction
 */
define([ "../../declare", "../../controls/grid/GridAction" ], 
        function(declare, GridAction) {

    /**
     * @class sbt.connections.controls.ViewAllAction
     * @namespace sbt.connections.controls
     */
    var ViewAllAction = declare(GridAction, {
        
    	/**Strings used in the actions */
        nls: {
            tooltip: "View All"
        },
        
        /**ViewAllAction Constructor function
         * @method - constructor
         * */
        constructor: function() {
        },
        
        /**
         * Handles displaying a tooltip for an item
         * For profiles, the tooltip by default will be a business card
         * So nothing is done in this function
         * @method - getTooltip
         * @param item - The element that will use the tooltip
         */
        getTooltip: function(item) {
            return "derp";
        },
        
        /**
         * The execute function is called from the handle click function
         * For Profiles by default the business card functionality is used
         * which works from the Semantic tag service so nothing is done here.
         * @method - execute
         * @param item - The item which fired the event
         * @param opts
         * @param event - The event
         */
        execute: function(item, opts, event) {
            opts.grid.renderer.hideViewAll = true;
            opts.grid.renderer.template = opts.grid.renderer.fullTemplate;
            opts.grid.renderer.render(opts.grid, opts.grid.gridNode, item.items, item);
            console.log("clicked the view all link");
        }

    });

    return ViewAllAction;
});
},
'sbt/controls/grid/GridAction':function(){
/*
 * � Copyright IBM Corp. 2013
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
 *  @module sbt.controls.grid.GridAction
 */
define(["../../declare"], function(declare) {

    /**
     * @class sbt.controls.grid.GridAction
     * @namespace sbt.controls.grid
     */
    var GridAction = declare(null, {
         
    	 /**
    	  * Grid Action Constructor function
    	  * @constructor
    	  */
         constructor: function() {
         },
         
         /**
          * Gets the string to be displayed as an elements tooltip
          * @method getTooltip
          * @param item the HTML element 
          * @returns {String} contains the text for the tooltip
          */
         getTooltip: function(item) {
            return "sbt.controls.GridAction No tooltip specified";
         },
         
         /** 
          * Default action for the grid
          * @method execute
          * @param item The element that fired the event
          * @param opts
          * @param event the Event, for example onClick
          */
         execute: function(item, opts, event) {
            dojo.stopEvent(event);
         },
         
         /**
          * Action to view the user's profile in connections. 
          * When the user clicks on a link to a person's profile
          * the HREF will have the URL and redirect the user to the person's
          * profile, if this action needs to be changed it can be overridden using this function
          *@method viewUserProfile 
          */
         viewUserProfile: function(){
        	 //do nothing by default
         }

    });
    
    return GridAction;
});
},
'sbt/connections/controls/WidgetWrapper':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../declare", "../../config", "../../lang", "../../widget/_TemplatedWidget", "../../stringUtil", "../../json", 
        "../../text!./templates/WidgetFrame.html", "../../i18n!sbt/connections/controls/nls/WidgetWrapper" ], 
        function(declare, config, lang, _TemplatedWidget, stringUtil, JSON, frameTemplate, nls) {

    /**
     * Base class for wrapped widgets. 
     * This class manages population of an iframe with a template which should be provided by subclasses.
     * 
     * @class sbt.controls.WidgetWrapper
     */
    var WidgetWrapper = declare([ _TemplatedWidget ], {
        /**
         * The templateString from dijit's _TemplatedMixin. Set it to be our iframe Template.
         * 
         * @property templateString 
         * @type String
         * @default frameTemplate
         */
        templateString: frameTemplate,
        
        /**
         * The url of our default frame content. In this case, a simple loading page.
         * 
         * @property frameContent
         * @type String
         * @default "/sbt/connections/controls/templates/LoadingPage.html"
         */
        frameContent: "/sbt/connections/controls/templates/LoadingPage.html",
        
        /**
         * The attach point for the iframe. This is specified in the frameTemplate.
         * 
         * @property iframeNode
         * @type Object
         * @default null
         */
        iframeNode: null,
        
        /**
         * The endpoint we will authenticate before it is used by the underlying widget.
         * @property endpoint
         * @type Object
         * @default null
         */
        _endpoint: null,
        
        /**
         * Constructor function. 
         * 
         * @method constructor
         * @param {Object} args
         *     @param {String} [args.endpoint] If this is provided it will look for an endpoint with this name which will be used throughout the widget.
         *     Otherwise it will default to the "connections" endpoint.
         */
        constructor: function(args){
            var endpointName = args.endpoint || "connections";
            var ep = config.findEndpoint(endpointName);
            this._endpoint = ep;
        },

        /**
         * After the widget is created but BEFORE it is added to the dom.
         * @method postCreate
         */
        postCreate: function() {
        	this.inherited(arguments);
            
        	if (this.frameContent) {
        	    this.iframeNode.src = config.Properties.sbtUrl + this.frameContent;
        	}
        },
        
        /**
         * Get an object with the transformations to be performed on the template. Subclasses should override this to provide values for any substitution variables in their templates.
         * 
         * @method getTransformObject
         * @returns {Object}
         */
        getTransformObject: function(){
            return {};
        },
        
        /**
         * After the widget has been created AND added to the dom. 
         * 
         * When instantiating programmatically, this method should ALWAYS be called explicitly after creation.
         * 
         * @method startup
         */
        startup: function() {
            this.inherited(arguments);
            if (this.frameContent) {
                var iframe = (this.iframeNode.contentWindow || this.iframeNode.contentDocument);
                if (iframe.document){
                    iframe = iframe.document;
                }
                if(this.defaultTemplate){
                    
                    this.defaultTemplate = stringUtil.transform(this.defaultTemplate, this.getTransformObject(), function(value, key){
                        if(!value){
                            return "${" + key + "}";
                        }
                        else{
                            return value;
                        }
                    });
                    this.defaultTemplate = stringUtil.transform(this.defaultTemplate,  this._endpoint.serviceMappings, function(value, key){
                        if(!value){
                            return key;
                        }
                        else{
                            return value;
                        }
                    });
                }
                else{
                    this.defaultTemplate = nls.root.defaultTemplate;
                }
                
                iframe.open();
                iframe.write(this.defaultTemplate);
                iframe.close();
            }
        }
                        
    });
    
    return WidgetWrapper;
});
},
'sbt/config':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Definition of config module.
 * 
	 * @module sbt.config
 */
define(['./defer!./_config'],function(cfg){
    return cfg;
});
},
'sbt/defer':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Defer plugin.
 * @author Carlos Manias
 */
define([], function(text) {
    return {
    	load: function (id, require, load) {
            require([id], function (value) {
                load(value);
            });
        }
    };
});

},
'sbt/stringUtil':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Defination of some string Utilities
 * 
 * @module sbt.stringUtil
 */
define(['./xml'], function(xml) {

	var _regex = new RegExp("{-?[0-9]+}", "g");

	return {
		/**
		 * Substitutes the String with pattern {<<number>>} with argument array provided. {-1} is for printing '{' and {-2} is for printing '}' in the text
		 * 
		 * @param {String} [str] String to be formatted
		 * @param {Array} [args] arguments Array
		 * @param {Boolean} [useBlankForUndefined = false] optional flag to indicate to user blank String in case index is not found in args. 
		 * @static
		 * @method substitute
		 */
		substitute : function(str, args, useBlankForUndefined) {
			if (str && args) {
				return str.replace(_regex, function(item) {
					var intVal = parseInt(item.substring(1, item.length - 1));
					var replace;
					if (intVal >= 0) {
						replace = args[intVal] ? args[intVal] : useBlankForUndefined ? "" : "undefined";
					} else if (intVal == -1) {
						replace = "{";
					} else if (intVal == -2) {
						replace = "}";
					} else {
						replace = "";
					}
					return replace;
				});
			}
			return str;
		},
		
		/**
		 * Replaces the String with pattern {<<string>>} with argument map provided. Replaces blank if key to be replaces is not found in argsMap.
		 * 
		 * @param {String} [str] String to be formatted
		 * @param {Array} [argsMap] arguments Map		 
		 * @static
		 * @method replace
		 */
		replace : function(str, argsMap) {
			if (str && argsMap) {
				return str.replace(/{(\w*)}/g, function(m, key) {					
					var replace;
					replace = argsMap.hasOwnProperty(key) ? xml.encodeXmlEntry(argsMap[key]) : "";
					return replace;
				});
			}
			return str;
		},
		
		trim: function x_trim(s) {
			return s ? s.replace(/^\s+|\s+$/g,"") : s;
		},
		
		startsWith: function x_sw(s,prefix) {
			return s.length>=prefix.length && s.substring(0,prefix.length)==prefix;
		},
		
		endsWith: function x_ew(s,suffix) {
			return s.length>=suffix.length && s.substring(s.length-suffix.length)==suffix;
		},
		
		transform: function(template, map, transformer, thisObject) {
		    return template.replace(/\$\{([^\s\:\}]+)(?:\:([^\s\:\}]+))?\}/g,
		        function(match, key, format){
		            var value = map[key] || "";
                    if (typeof value == 'function') {
                        // invoke function to return the value
                        try {
                            value = value.apply(thisObject, [ map ]);
                        } catch (ex) {
                            value = "ERROR:" + key + " " + ex;
                        }
                    }
                    if (transformer) {
		                value = transformer.call(thisObject, value, key);
		            } 
	                if (typeof value == "undefined" || value == null) {
	                    return "";
	                }
		            return value.toString();
		        }
		    );
		},
		
        hashCode: function(str) {
            if (str.length == 0) {
            	return 0;
            }
            var hash = 0, i, charStr;
            for (i = 0, l = str.length; i < l; i++) {
            	charStr = str.charCodeAt(i);
                hash = ((hash<<5)-hash)+charStr;
                hash |= 0;
            }
            return hash;
        }

	};
});

},
'sbt/xml':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK - XML utilities.
 */
define(['./lang'], function(lang) {
	var xml_to_encoded = {
		'&': '&amp;',
		'"': '&quot;',
		'<': '&lt;',
		'>': '&gt;'
	};

	var encoded_to_xml = {
		'&amp;': '&',
		'&quot;': '"',
		'&lt;': '<',
		'&gt;': '>'
	};
		
	return {
		/**
		 * XML Parser.
		 * This function parses an XML string and returns a DOM.
		 */
		parse: function(xml) {
			var xmlDoc=null;
			try {
				if(navigator.appName == 'Microsoft Internet Explorer'){
					xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
					xmlDoc.async="false";
					xmlDoc.loadXML(xml);
				}else{
					if(window.DOMParser){
						parser=new DOMParser();
						xmlDoc=parser.parseFromString(xml,"text/xml");
					}
				}
			}catch(ex){
				console.log(ex.message);
			}
			return xmlDoc;
		},
		asString: function(xmlDoc) {
			if (xmlDoc==null) {
				return "";
			} else if(window.ActiveXObject){
				return xmlDoc.xml;
			} else {
				return (new XMLSerializer()).serializeToString(xmlDoc);
			}
		},
		getText : function (xmlElement){
			if(navigator.appName == 'Microsoft Internet Explorer'){
				return xmlElement.text;
			}else{
				return xmlElement.textContent;
			}
		},
		encodeXmlEntry: function(string) {
		    if (lang.isArray(string)) {
		        string = string.join();
		    }
		    if (!lang.isString(string)) {
		        string = string.toString();
		    }
			return string.replace(/([\&"<>])/g, function(str, item) {
				return xml_to_encoded[item];
			});
		},
		decodeXmlEntry: function (string) {
			return string.replace(/(&quot;|&lt;|&gt;|&amp;)/g,function(str, item) {
				return encoded_to_xml[item];
			});
		}
	};
});
},
'sbt/json':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Implements some JSON helpers.  Will uses the browser version
 * if available else it will delegate to the Javascript library being used.
 * 
 * @module sbt.json
 */
define(['./_bridge/json', './_bridge/lang', './log', './stringUtil'], function(jsonLib, lang, log, stringUtil) {
	
	/**
	 * @static
	 */
    return {
        /**
         * Parses a String of JSON and returns a JSON Object.
         * @param {String} jsonString A String of JSON.
         * @returns {Object} A JSON Object.
         * @static
         * @method parse
         */
        parse : function(jsonString) {
            var jsonImpl = JSON || jsonLib;
            return jsonImpl.parse(jsonString);
        },
        
        /**
         * Returns the JSON object represented as a String.
         * @param {Object} jsonObj A JSON Object.
         * @returns {String} The JSON Object represented as a String.
         * @method stringify
         */
        stringify : function(jsonObj) {
            var jsonImpl = JSON || jsonLib;
            return jsonImpl.stringify(jsonObj);
        },
        
        /**
         * @method jsonBeanStringify
         * @param theObj
         * @returns
         */
        jsonBeanStringify: function(theObj) {
            if (lang.isArray(theObj)) {
                var jsonObjs = "[";
                for (var i=0; i<theObj.length; i++) {
                    jsonObjs += this._jsonBeanStringify(theObj[i]);
                    if ((i+1)<theObj.length) {
                        jsonObjs += ",\n";
                    }
                }
                jsonObjs += "]";
                return jsonObjs;
            } else {
                return this._jsonBeanStringify(theObj);
            }
        },
        
        /**
         * @method jsonBean
         * @param theObj
         * @returns
         */
        jsonBean: function(theObj) {
            if (lang.isArray(theObj)) {
                var jsonObjs = [];
                for (var i=0; i<theObj.length; i++) {
                    jsonObjs.push(this._jsonBean(theObj[i]));
                }
                return jsonObjs;
            } else {
                return this._jsonBean(theObj);
            }
        },
        
        // Internals
        
        _jsonBeanStringify: function(theObj) {
            var jsonObj = this.jsonBean(theObj);
            return this._stringifyCyclicCheck(jsonObj, 4);
        },
        
        _stringifyCyclicCheck: function(jsonObj, indent) {
            var jsonImpl = JSON || jsonLib;
            var seen = [];
            var self = this;
            return jsonImpl.stringify(jsonObj, function(key, val) {
                if(self._isDomNode(val)){
                    return {};
                }
                if (lang.isObject(val)) {
                    if (seen.indexOf(val) >= 0 && !self._isBuiltin(val))
                        return undefined;
                    seen.push(val);
                } else if(lang.isFunction(val)){
                    return undefined;
                }
                return val;
            }, indent);
        },
        
        _jsonBean: function(theObj, seen) {
            // first check for cyclic references
            if (!seen) {
                seen = [];
            }
            if (seen.indexOf(theObj) >= 0) {
                return undefined;
            }
            seen.push(theObj);
            
            var jsonObj = {};
            for (var property in theObj) {
                var value = this._getObjectValue(theObj, property, seen);
                if (value || !isNaN(value)) {
                    jsonObj[property] = value;
                }
            }
            return jsonObj;
        },
        
        _notReserved: function(property) {
        	return property!=='isInstanceOf' && property!=='getInherited';
        },
        
        _getObjectValue: function(theObj, property, seen) {
        	var self = this;
            if (lang.isFunction(theObj[property])) {
                if ((stringUtil.startsWith(property, "get") || stringUtil.startsWith(property, "is")) && self._notReserved(property)) {
                    try {
                        var value = theObj[property].apply(theObj);
                        if (value && !this._isBuiltin(value) && lang.isObject(value)) {
                            return this._jsonBean(value, seen);
                        }
                        return value;
                    } catch(error) {
                        //log.error("Error {0}.{1} caused {2}", theObj, property, error);
                    }
                }
            } else {
                if (!stringUtil.startsWith(property, "_") && !stringUtil.startsWith(property, "-")) {
                    return theObj[property];
                }
            }
            return undefined;
        },
        
        _isBuiltin: function(value) {
            return ((value instanceof Date) || 
                    (value instanceof Number) || 
                    (value instanceof Boolean) || 
                    lang.isArray(value));
        },
        
        _isDomNode : function(value) {
            return (value && value.nodeName && value.nodeType && typeof value.nodeType === "number" && typeof value.nodeName === "string");
        }
    };
});

},
'sbt/_bridge/json':function(){
/**
 * Dojo AMD JSON implementation.
 */
define(['dojo/json'],function(json) {
    return json;
});
},
'sbt/log':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK - Logging Utilities
 * @module sbt.log
 */
define(['./stringUtil'], function(stringUtil) {

	var loggingEnabled = function isLoggingEnabled(){
		return sbt.Properties["js.logging.enabled"] ? sbt.Properties["js.logging.enabled"].toLowerCase() == "true" : true;
	};

	var Level = {
		DEBUG : 1,
		INFO : 2,
		WARN : 3,
		ERROR : 4
	};
	
	var loggingLevel = function getLoggingLevel(){
		 return Level[sbt.Properties["js.logging.level"] ? sbt.Properties["js.logging.level"] : "DEBUG"];
	};

	return {
		/**
		 * Sets the logging level.
		 * @param {String} [l ]logging level. Possible values are DEBUG, INFO, WARN, ERROR
		 * @static
		 * @method setLevel
		 */
		setLevel : function(l) {
			loggingLevel = Level[l];
		},
		/**
		 * Enables/disables logging.
		 * @param {Boolean} [enabled] logging enabled true or false
		 * @static
		 * @method setEnabled
		 */
		setEnabled : function(enabled) {
			loggingEnabled = enabled;
		},
		/**
		 * log a debug statement.
		 * @static
		 * @method debug
		 */
		debug : function() {
			if (!loggingEnabled) {
				return;
			}
			if (loggingLevel > 1) {
				return;
			}
			if (console && arguments.length > 0) {
				var args = Array.prototype.slice.call(arguments);
				var text = args[0];
				args = args.slice(1);
				var formattedText = stringUtil.substitute(text, args);
				if (console.debug) {
					console.debug(formattedText);
				} else {
					console.log("DEBUG : " + formattedText);
				}
			}
		},
		/**
		 * log an info statement.
		 * @static
		 * @method info
		 */
		info : function() {
			if (!loggingEnabled) {
				return;
			}
			if (loggingLevel > 2) {
				return;
			}
			if (console && arguments.length > 0) {
				var args = Array.prototype.slice.call(arguments);
				var text = args[0];
				args = args.slice(1);
				var formattedText = stringUtil.substitute(text, args);
				if (console.info) {
					console.info(formattedText);
				} else {
					console.log("INFO : " + formattedText);
				}
			}
		},
		/**
		 * log a warning statement.
		 * @static
		 * @method warn
		 */
		warn : function() {
			if (!loggingEnabled) {
				return;
			}
			if (loggingLevel > 3) {
				return;
			}
			if (console && arguments.length > 0) {
				var args = Array.prototype.slice.call(arguments);
				var text = args[0];
				args = args.slice(1);
				var formattedText = stringUtil.substitute(text, args);
				if (console.warn) {
					console.warn(formattedText);
				} else {
					console.log("WARN : " + formattedText);
				}
			}
		},
		/**
		 * log an error statement.
		 * @static
		 * @method error
		 */
		error : function() {
			if (!loggingEnabled) {
				return;
			}
			if (console && arguments.length > 0) {
				var args = Array.prototype.slice.call(arguments);
				var text = args[0];
				args = args.slice(1);
				var formattedText = stringUtil.substitute(text, args);
				if (console.error) {
					console.error(formattedText);
				} else {
					console.log("ERROR : " + formattedText);
				}
			}
		},
		/**
		 * log an exception
		 * @static
		 * @method error
		 */
		exception : function() {
			if (!loggingEnabled) {
				return;
			}
			if (console && arguments.length > 0) {
				var args = Array.prototype.slice.call(arguments);
				var text = args[0];				
				args = args.slice(1);
				var formattedText = stringUtil.substitute(text, args);
				if (console.error) {
					console.error("EXCEPTION : " + formattedText);
				} else {
					console.log("EXCEPTION : " + formattedText);
				}
			}
		}
	};
});
},
'sbt/connections/controls/_ConnectionsWidget':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../declare", "../../config", "../../connections/controls/astream/_XhrHandler", "dijit/_Widget"], function(declare, config, _XhrHandler){
    
    /*
     * Base class for common functionality of all connections widgets.
     * 
     * @class sbt.controls._ConnectionsWidget
     */
    var _ConnectionsWidget = declare([dijit._Widget],
    {
        /*
         * The url of the resources/web folder, which contains the projects we need.
         * 
         * @property baseUrl
         * @type String
         */
        baseUrl: config.Properties.serviceUrl + "/proxy/connections/${connections}/resources/web/",
        
        /*
         * The _XhrHandler to use in this connections widget.
         * 
         * @property xhrHandler
         * @type {Object}
         */
        xhrHandler: null,
        
        /*
         * Setup with common Connections widget functionality.
         * @param {Object} args
         *     @param {Object} [args.xhrHandler] The _XhrHandler to be used throughout this widget.
         *     @param {String} [endpoint] The endpoint to be used when creating an _XhrHandler for this connections widget.
         */
        constructor: function(args){
            window.ibmConfig = window.ibmConfig || {};
            window.ibmConfig.versionStamp = window.ibmConfig.versionStamp || 1234567890;
            
            dojo.config.locale="en";
            this.registerModulePaths();
            this.xhrHandler = args.xhrHandler || this.requireXhr(args.endpoint);
        },
        
        /*
         * Register the connections modules we will be using.
         * 
         * @method registerModulePaths
         */
        registerModulePaths: function(){
            dojo.registerModulePath("com", this.baseUrl + "com");
            dojo.registerModulePath("lconn", this.baseUrl + "lconn");
            dojo.registerModulePath("net", this.baseUrl + "net");
        },
        
        /*
         * Create the _XhrHandler from the endpoint name.
         * 
         * @method requireXhr
         * @param endpointName
         */
        requireXhr: function(endpointName){
            var _endpointName = endpointName || "connections";
            return new _XhrHandler(_endpointName);
        }
        
    });
    
    return _ConnectionsWidget;
});


},
'sbt/connections/controls/astream/_XhrHandler':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../../declare", "../../../config", "../../../stringUtil", "../../../lang", "../../../util"], function(declare, config, stringUtil, lang, util){
    /*
     * @class _XhrHandler
     */
    var _XhrHandler = declare(null,
    {
        /*
         * The endpoint to use when making xhr requests.
         * 
         * @property endpoint 
         * @type Object
         * @default null
         */
        endpoint: null,
        
        contextRootMap: {
            connections: "connections"
        },
        
        /*
         * A helper method to ensure things like a proxy and and authentication protocol are present in the url.
         * 
         * Most internal connections ActivityStream urls don't use an authentication protocol.
         * 
         * @method modifyUrl
         * 
         * @param {Object} args
         * 
         * @param {String} args.url The url which we will xhr to. If this is not present this method does nothing.
         */
        modifyUrl: function(args){
            if(this.endpoint){
                lang.mixin(this.contextRootMap, this.endpoint.serviceMappings);
                
                if(this.contextRootMap && !util.isEmptyObject(this.contextRootMap)){
                    var url = args.url || args.serviceUrl;
                    url = stringUtil.transform(url, this.contextRootMap, function(value, key){
                        if(!value){
                            return key;
                        }
                        else{
                            return value;
                        }
                    }, this);
                    if(args.url){
                        args.url = url;
                    }
                    else{
                        args.serviceUrl = url;
                    }
                }
            }
            
            if(!args.url){
                return;
            }
            if (args.url.indexOf("/") === 0){
                args.serviceUrl = args.url;
                delete args.url;
            } else if (args.url.indexOf("proxy") === -1){
                if(args.url.indexOf("https") !== -1)
                    args.url = this.endpoint.proxy.proxyUrl + "/" + this.endpoint.proxyPath + "/" + args.url.replace(/https:\/\/.[^\/]+\//, "");
                else if(args.url.indexOf("http") !== -1)
                    args.url = this.endpoint.proxy.proxyUrl + "/" + this.endpoint.proxyPath + "/" + args.url.replace(/http:\/\/.[^\/]+\//, "");
            }
            if(args.url.indexOf("opensocial/rest") !== -1)
                args.url = args.url.replace("opensocial/","opensocial/" + this.endpoint.authType +  "/");
        },
        
        /*
         * The constructor.
         * 
         * @method constructor
         * @param {String} endpointName The name of the endpoint to use.
         */
        constructor: function(endpointName){
            this.endpoint = config.findEndpoint(endpointName);
        },
        
        /*
         * Make an XHR HEAD request.
         * @method xhrGet
         * @param args The xhr args.
         */
        xhrGet: function(args){
            this.modifyUrl(args);
            return this.xhr("GET", args);
        },
        
        /*
         * Make an XHR POST request.
         * @param args xhr args
         */
        xhrPost: function(args){
            this.modifyUrl(args);
            return this.xhr("POST", args);
        },
        
        /*
         * Make an XHR PUT request.
         * @param args xhr args
         */
        xhrPut: function(args){
            this.modifyUrl(args);
            return this.xhr("PUT", args);
        },
        
        /*
         * Make an XHR DELETE request.
         * @param args xhr args
         */
        xhrDelete: function(args){
            this.modifyUrl(args);
            return this.xhr("DELETE", args);
        },
        
        /*
         * Make an XHR GET request.
         * @param args xhr args
         */
        xhrHead: function(args){
            this.modifyUrl(args);
            return this.xhr("HEAD", args);
        },
        
        /*
         * An xhrHandler needs to have an xhr function to use the xhr handler init in connections >= 4.5
         */
        xhr: function(method, args){
            this.modifyUrl(args);
            return this.endpoint.xhr(method, args);
        }
    });
    
    return _XhrHandler;
});

},
'sbt/util':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * @module sbt.util
 */
define(['./lang','sbt/i18n!sbt/nls/util','./log'],function(lang, nls, log) {
	var errorCode = 400;	
	function _notifyError(error, args){	
		if (args && (args.error || args.handle)) {
			if (args.error) {
				try {
					args.error(error);
				} catch (error1) {
					log.error(nls.notifyError_catchError, error1);
				}
			}
			if (args.handle) {
				try {
					args.handle(error);
				} catch (error2) {
					log.error(nls.notifyError_catchError, error2);
				}
			}
		} else {
			log.error(nls.notifyError_console, error.code, error.message);
		}
	}	
	return {
		notifyError: _notifyError,	
		isEmptyObject: function(obj){
            var isEmpty = true;
            for( var key in obj ){
                if(obj.hasOwnProperty(key)){
                    isEmpty = false;
                    break;
                }
            }
            return isEmpty;
        },
		checkObjectClass: function(object, className, message, args){
			if(object.declaredClass != className){
				if(args){
					_notifyError({code:errorCode,message:message},args);
				}else{
					log(message);
				}
				return false;
			}else{
				return true;
			}
		},
		checkNullValue: function(object, message, args){
			if(!object){
				if(args){
					_notifyError({code:errorCode,message:message},args);
				}else{
					log(message);
				}
				return false;
			}else{
				return true;
			}
		},
		minVersion: function(required, used) {
		    var reqParts = required.split('.');
		    var usedParts = used.split('.');
		    
		    for (var i = 0; i < reqParts.length; ++i) {
		        if (usedParts.length == i) {
		            return false;
		        }
		        
		        if (reqParts[i] == usedParts[i]) {
		            continue;
		        }
		        else if (reqParts[i] > usedParts[i]) {
		            return false;
		        }
		        else {
		            return true;
		        }
		    }
		    
		    if (reqParts.length != usedParts.length) {
		        return true;
		    }
		    
		    return true;
		},
        getAllResponseHeaders: function(xhr) {
            var headers = {};
            try {
                var headersStr = xhr.getAllResponseHeaders();
                if (headersStr) {
                    var headersStrs = headersStr.split('\n');
                    for (var i=0; i<headersStrs.length; i++) {
                        var index = headersStrs[i].indexOf(':');
                        var key = lang.trim(headersStrs[i].substring(0, index));
                        var value = lang.trim(headersStrs[i].substring(index+1));
                        if (key.length > 0) {
                            headers[key] = value;
                        }
                    }
                }
            } catch(ex) {
                console.log(ex);
            }
            return headers;
        },
        
        /**
         * Takes an object mapping query names to values, and formats them into a query string separated by the delimiter.
         * e.g.
         * createQuery({height: 100, width: 200}, ",")
         * 
         * returns "height=100,width=200"
         * 
         * @method createQuery
         * 
         * @param {Object} queryMap An object mapping query names to values, e.g. {height:100,width:200...}
         * @param {String} delimiter The string to delimit the queries
         */
		createQuery: function(queryMap, delimiter){
	        if(!queryMap){
	            return null;
	        }
	        var delim = delimiter;
	        if(!delim){
	            delim = ",";
	        }
	        var pairs = [];
	        for(var name in queryMap){
	            var value = queryMap[name];
	            pairs.push(encodeURIComponent(name) + "=" + encodeURIComponent(value));
	        }
	        return pairs.join(delim);
	    },
	    
	    /**
         * Takes a query string and returns an equivalent object mapping.
         * e.g.
         * splitQuery("height=100,width=200", ",")
         * 
         * returns {height: 100, width: 200}
         * 
         * @method splitQuery
         * 
         * @param {String} query A query string, e.g. "height=100,width=200"
         * @param {String} delimiter The string which delimits the queries
         */
	    splitQuery: function(query, delimiter){
	        var i;
	        var result = {};
	        var part;
	        var parts;
	        var length;
	        
	        query = query.replace("?", "");
	        parts = query.split(delimiter);
	        length = parts.length;
	        
	        for (i = 0; i < length; i++) {
	            if(!parts[i]){
	                continue;
	            }
                part = parts[i].split('=');
                result[part[0]] = part[1];
            }
	        
	        return result;
	    },
	    
	    /**
	     * Returns the JavaScript Library and version used
	     * @returns {String} JavaScript Library with version
	     */
	    getJavaScriptLibrary : function(){
	    	var jsLib = "Unknown";
	    	if(window.dojo) {
	    		if(dojo.version) {
	    			jsLib = "Dojo "+dojo.version;
	    		}
	    	} else if(define && define.amd && define.amd.vendor && define.amd.vendor === "dojotoolkit.org") {
	    		require(["dojo/_base/kernel"], function(kernel){ 
	    			jsLib = "Dojo AMD "+kernel.version;
	    		});
	    	} else if(window.jQuery) {
	    		jsLib = "JQuery "+jQuery.fn.jquery;
	    	} 
	    	return jsLib;
	    }
	};
});
},
'sbt/controls/grid/Grid':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../declare", "../../lang", "../../itemFactory", "../../stringUtil", "../../widget/grid/_Grid", "../../util"], 
        function(declare, lang, itemFactory, stringUtil, _Grid, util) {

    /**
     * @class grid
     * @namespace sbt.controls.grid
     * @module sbt.controls.grid.Grid
     */
    var Grid = declare([ _Grid ], {

    	/**
    	 * Data associated with this Grid
    	 */
        data: null,
        
        /**
         * The renderer associated with the grid
         */
        renderer: null,
        
        /**
         * Encode all of the data coming from the connections server 
         * as HTML entities, to prevent XSS attacks
         */
        encodeHtml: true,
        
        /**
         * The number of grid rows displayed per page
         */
        pageSize: 5,
        
        /**
         * Flag to hide the pager
         */
        hidePager: false,
        
        /**
         * Flag to hide the sorter
         */
        hideSorter: false,
        
        /**
         * FilterTag, is used for sorting and paging, as to only sort as filtered set of results
         */
        filterTag: "",
        
        /**
         * Selected rows are the rows of the grid that have been selected by checking a check box 
         */
        selectedRows: null,
        
        /*
         * TODO remove this?
         */
        _strings: {},

        /*
         * Arguments for the associated data store
         */
        _storeArgs: null,

        /*
         * Regular expression used to remove // from url's
         */
        _regExp : new RegExp("/{2}"),

        /**
         * Empty context root map, can be overridden by subclasses of Grid. Represents Connections context roots.
         */
        contextRootMap: {},
        
        /**
         * Constructor method for the grid.
         * Creates a default store and renderer, if none have been already created
         * @method constructor
         * @param args
         */
        constructor: function(args) {
            lang.mixin(this, args);
            
            this.selectedRows = [];
  
            
            if (!this.store) {
                if (args && args.storeArgs) {
                    this._storeArgs = args.storeArgs;
                    this._storeArgs.endpoint = this.endpoint;
                    this.store = this.createDefaultStore(args.storeArgs);
                } else if (this.options) {
                    this._storeArgs = this.options[this.defaultOption].storeArgs;
                    this._storeArgs.endpoint = this.endpoint;
                    if (args && args.type && this.options.hasOwnProperty(args.type)) {
                        lang.mixin(this._storeArgs, this.options[args.type].storeArgs); 
                    }   
                }
                this.store = this.createDefaultStore(this._storeArgs);
            }
            
            if (!this.renderer) {
                if (args && args.rendererArgs) {
                    this.renderer = this.createDefaultRenderer(args.rendererArgs);
                } else if (this.options) {
                    var rendererArgs = this.options[this.defaultOption].rendererArgs;
                    if (args && args.type && this.options.hasOwnProperty(args.type)) {
                        rendererArgs = this.options[args.type].rendererArgs;
                    }

                    this.renderer = this.createDefaultRenderer(rendererArgs);
                }
            }
        },
        
        /**
         * Create the store to be used with this Grid.
         * 
         * @method - createDefaultStore
         * @param args - the arguments to pass to the atom store, such as URL and attributes
         * @returns - an atom store instance
         */
        createDefaultStore: function(args) {
            var store = this._createDefaultStore(args);
            var url = store.getUrl();
            if (url) {
                url = this.buildUrl(url, args, store.getEndpoint());
            }
            store.setUrl(url);

            return store;
        },
        
        /**
         * Allow Grid to build the complete URL before it is passed to the store.
         * 
         * @method buildUrl
         * @param url base url
         * @param args arguments that will be passed to the store
         * @param endpoint The endpoint, needed to verify if custom service mappings are present.
         * @returns Built url
         */
        buildUrl: function(url, args, endpoint) {
            var params = {};
            if (this.query) {
                params = lang.mixin(params, this.query);
            }
            return this.constructUrl(url, params, this.getUrlParams(), endpoint);
        },

        /**
         * Create the renderer to be used with this Grid.
         * 
         * @method - createDefaultRenderer
         * @param args
         */
        createDefaultRenderer: function(args) {
        },
        
        /**
         * Post create function is called after grid has been created.
         * @method - postCreate
         */
        postCreate: function() {
           this.inherited(arguments);      
           
           if (this.renderer && this.store) {
               this.renderer.renderLoading(this, this.gridNode);
           }
           
           if (this.updateOnCreate) {
               this.created = true;
               this.update();
           }
        },
        
        /**
         * Refresh the grid
         * @method - refresh
         */
        refresh: function() {
           if (this.data) {
              if (this.store) {
                 this.update(null);
              } else {
                 this.update();
              }
           }
        },
        
        /**
         * Update the grid
         * @method - update
         */
        update: function(data) {
           if (arguments.length > 0) {
              this.data = data;
           }
           if (this.data) {
               this.renderer.render(this, this.gridNode, this.data.items, this.data);
               this.onUpdate(this.data);
           } else if (this.store) {
        	   if (this._activeSortAnchor && this._activeSortIsDesc !== undefined) {
        		   this._doQuery(this.store, { start : 0, count : this.pageSize, sort: [{ attribute : this._activeSortAnchor.sortParameter, descending : this._activeSortIsDesc }] });   
        	   } else {
        		   this._doQuery(this.store, { start : 0, count : this.pageSize });
        	   }
               
           } else {
              this.renderer.renderEmpty(this, this.gridNode, this.data);
              this.onUpdate(this.data);
           }
        },

        /**
         * @method onUpdate
         * @param data
         */
        onUpdate: function(data) {
        },
        
        /**
         * @method getSortInfo
         */
        getSortInfo: function() {
        },
        
        /**
         * Go back to the previous page
         * @method - prevPage
         * @param el - The element that fired the event, typically an anchor 
         * @param data - the data associated with element
         * @param ev - the event, for example onClick
         */
        prevPage: function(el, data, ev) {
            this._stopEvent(ev);
            
            if (this.store) {
            	
            	//if sorting
            	if(this._activeSortAnchor){
	                var options = { 
	                		start : 0, count : this.pageSize,
	                		sort: [{ attribute: this._activeSortAnchor.sortParameter }]
	                };
	               
	                if(this._activeSortIsDesc !== undefined){
	                	options.sort[0].descending = this._activeSortIsDesc;
	                }
            	}else{
            		var options = { 
	                		start : 0, count : this.pageSize
	                };
            	}
            if (this.data) {
                options.start = Math.max(0, this.data.start - options.count);
            }
            
            if(this.filterTag != "" && this.filterTag != null){
           	 options.tag = this.filterTag;
            }
            
            this._doQuery(this.store, options);
            }
        },
        
        show10ItemsPerPage: function(el, data, ev) {
        	this.showItemsPerPage(el, data, ev, 10);
        },
        
        show25ItemsPerPage: function(el, data, ev) {
        	this.showItemsPerPage(el, data, ev, 25);
        },
        
        show50ItemsPerPage: function(el, data, ev) {
        	this.showItemsPerPage(el, data, ev, 50);
        },
        
        show100ItemsPerPage: function(el, data, ev) {
        	this.showItemsPerPage(el, data, ev, 100);
        },
        
        /**
         * Displays the feed for the content that is currently shown.
         * @method - viewFeed
         * @param el - The element that fired the event, typically an anchor 
         * @param data - the data associated with element
         * @param ev - the event, for example onClick
         */
        viewFeed: function(el, data, ev) {
        	var endpoint = this.store.getEndpoint();
        	var proxy = endpoint.proxy;
        	var baseUrl = endpoint.baseUrl;
        	var proxyPath = endpoint.proxyPath;
        	var url = proxy.rewriteUrl(baseUrl, this.store.getUrl(), proxyPath);
        	window.open(url, "_new");
        },
        
        /**
         * Show "count" items per page
         * @method - showItemsPerPage
         * @param el - The element that fired the event, typically an anchor 
         * @param data - the data associated with element
         * @param ev - the event, for example onClick
         * @param count - the number of items to display per page
         */
        showItemsPerPage: function(el, data, ev, count) {
        	this._stopEvent(ev);
        	if (this.store) {
        		this.pageSize = count;
        		this.update(null);
        	}
        },
        
        
        /**
         * Move forward to the next page of grid rows
         * @method - nextPage
         * @param el - The element that fired the event, typically an anchor 
         * @param data - the data associated with element
         * @param ev - the event, for example onClick
         */
        nextPage: function(el, data, ev) {
            this._stopEvent(ev);
            
            if (this.store) {
            	//if there is sorting available
            	if(this._activeSortAnchor){
	            	var options = { 
	                    start : 0, count : this.pageSize ,
	                    sort: [{ attribute: this._activeSortAnchor.sortParameter }]
	                };
	            	if(this._activeSortAnchor !== undefined){
	               	   options.sort[0].descending = this._activeSortIsDesc;
	                }
            	} else {
            		var options = { 
	                    start : 0, count : this.pageSize 
	                };
            	}
                if (this.data) {
                    options.start = this.data.start + options.count;
                    options.count = this.pageSize;
                    options.total = this.data.totalCount;
                }
                if(this.filterTag != "" && this.filterTag != null){
            	    options.tag = this.filterTag;
                }
                this._doQuery(this.store, options);
            }
        },
        
        /**
         * Called when the user clicks a checkbox 
         * The row gets added or removed to an array, 
         * to retrieve the array call getSelected
         * @method handleCheckBox
         */
        handleCheckBox: function (el, data, ev){       	
        	if (el.checked) {
        		this.selectedRows.push(data);
        	} else if (!el.checked) {
        		var rows = this.getSelected();
        		for(var i=0;i<rows.length;i++){
        			if(rows[i].data == data){
        				//selected row
        				this.selectedRows.splice(i,1);
        			}
        		}
        	}
        },
        
        /**
         * If the grid rows have checkboxes , get a list of the rows which are currently selected
         * (That have a checked checkbox)
         * @method - getSelected
         * 
         */
        getSelected: function() {
            var items = [];
            if (this.selectedRows) {
                for (var i=0; i<this.selectedRows.length; i++) {
                        var item = {
                            data: this.selectedRows[i]
                        };
                        items.push(item);
                }
            }
            return items;
        },
        
        /**
         * Add an item using the specified Document
         * 
         * @method addItem
         * @param document
         */
        addItems: function(document) {
            if (!this.data) {
                this.data = { items: [], start: 0, end: 0, count: 0, totalCount: 0 };
            }
            var attributes = this._storeArgs.attributes;
            var items = itemFactory.createItems(document, attributes, this);
            this.data.items = this.data.items.concat(items); 
            this.data.totalCount += this.data.items.length;
            this.data.end = this.data.count = this.data.totalCount;
        },
        
        /**
         * Insert the specified item into the grid at the specified index.
         * 
         * TODO This is here so that we can insert the local user grid at the start of the items array, mainly.
         * The args like email are not available at the start. So try to see if the user can be inserted to the start of some asrray before it goes to get the different profiles.
         * @param e
         */
        insertItem: function(document, index) {
            if(!this.data){
                console.log("Data is not yet present, adding to beginning.");
                this.data = { items: [], start: 0, end: 0, count: 0, totalCount: 0 };
                index = 0;
            }
            var attributes = this._storeArgs.attributes;
            var items = itemFactory.createItems(document, attributes, this);
            this.data.items.splice(index, 0, items[0]); 
            this.data.totalCount = this.data.items.length;
            this.data.end = this.data.count = this.data.totalCount;
        },
        
        /**
         * @method encodeImageUrl
         * @param url
         */
        encodeImageUrl: function(url) {
        	var ep = this.store.getEndpoint();
        	return ep.proxy.rewriteUrl(ep.baseUrl, url, ep.proxyPath);
        },
        
        /**
         * Return the auth type to be  used
         * @returns {String}
         */
        getAuthType: function() {
        	return "";
        },
        
        /**
         * Return the url parameters to be used
         * @returns {Object}
         */
        getUrlParams: function() {
        	return { authType : this.getAuthType() };
        },
        
        /**
         * Construct a url using the specified parameters 
         * @method constructUrl
         * @param url
         * @param params
         * @param urlParams
         * @param endpoint An endpoint which may contain custom service mappings.
         * @returns
         */
        constructUrl : function(url,params,urlParams, endpoint) {
            if (!url) {
                throw new Error("Grid.constructUrl: Invalid argument, url is undefined or null.");
            }
            
            if(endpoint){
                lang.mixin(this.contextRootMap, endpoint.serviceMappings);
                
                if(this.contextRootMap){
                    url = stringUtil.transform(url, this.contextRootMap, function(value, key){
                        if(!value){
                            return key;
                        }
                        else{
                            return value;
                        }
                    }, this);
                }
            }
            
            if (urlParams) {
                url = stringUtil.replace(url, urlParams);
                
                if (url.indexOf("//") != -1) {
                	// handle empty values
                	url = url.replace(this._regExp, "/");
                }
            }
            if (params) {
                for (param in params) {
                    if (params[param]) {
                        if (url.indexOf("?") == -1) {
                            url += "?";
                        } else if (url.indexOf("&") != (url.length - 1)) {
                            url += "&";
                        }
                        url += param + "=" + encodeURIComponent(params[param]);
                    }
                }
            }
            return url;
        },

        // Internals
        
        /*
         * Sort the contents
         */
        _sort: function(index, defaultOrder, el, data, ev) {
        	this._stopEvent(ev);
            var options = {
                start: data.start, count: this.pageSize, 
                sort: [{ attribute: index }]
            };
            
            if(this.filterTag != "" && this.filterTag != null){
           	 options.tag = this.filterTag;
            }
            
            if(this._activeSortAnchor === this._sortInfo[index]) {
                this._activeSortIsDesc = !this._activeSortIsDesc; // Flip sort order
            }
            else { // Change active sort to anchor clicked and its default order
                this._activeSortAnchor = this._sortInfo[index];
                this._activeSortIsDesc = defaultOrder;
            }
            options.sort[0].descending = this._activeSortIsDesc;

            this._doQuery(this.store, options);

        },
        
        /*
         * Filter the contents
         */
        _filter: function(args, data){
        	var options = {
        			start: 0, count: this.pageSize,
                    sort: [{ attribute: this._activeSortAnchor.sortParameter  }]
                };        	
        	options.sort[0].descending = this._activeSortIsDesc;
        	
        	var query = {};
        	if(args.tag){
        		query.tag = args.tag;
        		this.filterTag = args.tag;
        	}

            this._doQuery(this.store, options,query);
        },
        
        /*
         * Display the specified error message 
         */
        _updateWithError: function(e) {
        	console.error(e.message);
            this.renderer.renderError(this, this.gridNode, e.message);
        }

    });
    
    return Grid;
});

},
'sbt/itemFactory':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * I18n utilities
 */
define(["./lang", "./xpath", "./base/core"], function(lang, xpath, core) {
    
    var XPathCountFunction = /^count\(.*\)$/;

    /**
     * @module sbt.itemFactory
     */
    return {
        
        createItems: function(document, attributes, thisObject, decoder) {
            var nodes = xpath.selectNodes(document, core.feedXPath.entry, core.namespaces);
            var items = [];
            if (nodes.length == 0) {
                nodes = xpath.selectNodes(document, "a:entry", core.namespaces);
            } 
            for (var i=0; i<nodes.length; i++) {
                items.push(this.createItem(nodes[i], attributes, thisObject, decoder));
            }
            return items;
        },
        
        createItem: function(element, attributes, thisObject, decoder) {
            // TODO add item.index and item.attribs
            var item = { 
                element : element,
                getValue : function(attrib) { return this[attrib]; }
            };
            var attribs = this.getAttribs(attributes);
            for (var i=0; i<attribs.length; i++) {
                var attrib = attribs[i];
                var access = attributes[attrib];
                if (lang.isFunction(access)) {
                    item[attrib] = access(thisObject, item);
                } else if (access.match(XPathCountFunction)){
                    item[attrib] = xpath.selectNumber(element, access, core.namespaces);
                } else {
                    var nodes = xpath.selectNodes(element, access, core.namespaces);
                    if (nodes && nodes.length == 1) {
                        item[attrib] = nodes[0].text || nodes[0].textContent;
                    } else if (nodes) {
                        item[attrib] = [];
                        for (var j=0; j<nodes.length; j++) {
                            item[attrib].push(nodes[j].text || nodes[j].textContent);
                        }
                    } else {
                        item[attrib] = null;
                    }
                }
                
                item[attrib] = (decoder) ? decoder.decode(item[attrib]) : item[attrib];
            }
            return item;
        },
        
        getAttribs: function(attributes) {
            var attribs = [];
            for (var name in attributes) {
                if (attributes.hasOwnProperty(name)) {
                    attribs.push(name);
                }
            }
            return attribs;
        }
        
    };
    
});
},
'sbt/xpath':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK - XPath utilities.
 * 
 * @module sbt.xpath
 */
define(['./declare'],function(declare) {
	/*
	 * @class sbt.xpath.XPathExpr
	 */
	var XPathExpr = declare(null, {
		ie:		false,
		constructor: function(xpath, nsArray){
		     this.xpath = xpath;    
		     this.nsArray = nsArray || {};
		     if (!document.createExpression) {
		    	 this.ie = true;
		         this.nsString = "";
		         if (this.nsArray) {
		             for(var ns in this.nsArray) {
		                 this.nsString += ' xmlns:' + ns + '="' + this.nsArray[ns] + '"';
		             }
		         }
		     }
		},
	
		selectSingleNode : function(xmlDomCtx) {
			var doc = xmlDomCtx.ownerDocument || xmlDomCtx;
			if (this.ie) {
				try {
					doc.setProperty("SelectionLanguage", "XPath");
					doc.setProperty("SelectionNamespaces", this.nsString);
					if (xmlDomCtx === doc) xmlDomCtx = doc.documentElement;
					return xmlDomCtx.selectSingleNode(this.xpath);
				} catch (ex) {
					throw "XPath is not supported";
				}
			} else {
				var _this = this;
				var result = doc.evaluate(this.xpath, xmlDomCtx,
					function(prefix) {
						return _this.nsArray[prefix];
					}, XPathResult.FIRST_ORDERED_NODE_TYPE, null);
				return result.singleNodeValue;
			}
		},	
		
        selectNumber : function(xmlDomCtx){
            var doc = xmlDomCtx.ownerDocument || xmlDomCtx;
            if (this.ie) {
            	return this.selectText(xmlDomCtx);
            } else {
                var _this = this;
                var result = doc.evaluate(this.xpath, xmlDomCtx,
                    function(prefix) {
                        return _this.nsArray[prefix];
                    }, XPathResult.NUMBER_TYPE, null);
                return result.numberValue;
            }
        },
		
		selectNodes : function(xmlDomCtx) {
			var doc = xmlDomCtx.ownerDocument || xmlDomCtx;
			if (this.ie) {
				try {
					doc.setProperty("SelectionLanguage", "XPath");
					doc.setProperty("SelectionNamespaces", this.nsString);
					if (xmlDomCtx === doc) xmlDomCtx = doc.documentElement;
					return xmlDomCtx.selectNodes(this.xpath);
				} catch (ex) {
					throw "XPath is not supported";
				}
			} else {
				var _this = this;
				var result = doc.evaluate(this.xpath, xmlDomCtx, 
					function(prefix) {
						return _this.nsArray[prefix];
					}, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
				var r = [];
				for(var i = 0; i < result.snapshotLength; i++) {
					r.push(result.snapshotItem(i));
				}
				return r;
			}
		},
		
		selectText : function(node) {
			var result = this.selectSingleNode(node);
			return result ? (result.text || result.textContent) : null;
		}
	});
	
	return {
		/**
		 * Selects nodes from XML data object
		 * 
		 * @method selectNodes
		 * @param {Object}
		 *            node xml data to be parsed
		 * @param {String}
		 *            xpath xpath expression
		 * @param {Array}
		 *            nsArray Array of namespaces for the xml.
		 * @return {Array} Array of nodes
		 * @static
		 */
		selectNodes : function(node, xpath, nsArray) {
			var expr = new XPathExpr(xpath, nsArray);
			return expr.selectNodes(node);
		},

		/**
		 * Selects single node from XML data object
		 * 
		 * @method selectSingleNode
		 * @param {Object}
		 *            node xml data to be parsed
		 * @param {String}
		 *            xpath xpath expression
		 * @param {Array}
		 *            nsArray Array of namespaces for the xml.
		 * @return {Object} selected node object
		 * @static
		 */
		selectSingleNode : function(node, xpath, nsArray) {
			var expr = new XPathExpr(xpath, nsArray);
			return expr.selectSingleNode(node);
		},
		

		/**
		 * Selects text from a single node from XML data object
		 * 
		 * @method selectText
		 * @param {Object}
		 *            node xml data to be parsed
		 * @param {String}
		 *            xpath xpath expression
		 * @param {Array}
		 *            nsArray Array of namespaces for the xml.
		 * @return {String} inner text of the node object
		 * @static
		 */
		selectText : function(node, xpath, nsArray) {
			var expr = new XPathExpr(xpath, nsArray);
			return expr.selectText(node);
		},
		
		/**
		 * 
		 * @param node
		 * @param xpath
		 * @param nsArray
		 * @returns
		 */
		selectNumber : function(node, xpath, nsArray){
		    var expr = new XPathExpr(xpath, nsArray);
		    return expr.selectNumber(node);
		}
	};
});
},
'sbt/base/core':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Helpers for the core capabilities
 */
define(['../config'],function(sbt) {

	/**
	 * Global Namespaces Object.
	 */
	return {
		// Namespaces used when parsing Atom feeds
        namespaces : {
            o : "http://ns.opensocial.org/2008/opensocial",
            app : "http://www.w3.org/2007/app",
            thr : "http://purl.org/syndication/thread/1.0",
            fh : "http://purl.org/syndication/history/1.0",
            snx : "http://www.ibm.com/xmlns/prod/sn",
            opensearch : "http://a9.com/-/spec/opensearch/1.1/",
            a : "http://www.w3.org/2005/Atom",
            h : "http://www.w3.org/1999/xhtml",
            td: "urn:ibm.com/td",
            relevance: "http://a9.com/-/opensearch/extensions/relevance/1.0/",
            ibmsc: "http://www.ibm.com/search/content/2010",
            xhtml: "http://www.w3.org/1999/xhtml"
        },
        
        feedXPath : {
            "entry" : "/a:feed/a:entry",
            "entries" : "/a:feed/a:entry",
            "totalResults" : "/a:feed/opensearch:totalResults",
            "startIndex" : "/a:feed/opensearch:startIndex",
            "itemsPerPage" : "/a:feed/opensearch:itemsPerPage"
        },
                
        entryXPath : {
            "title" : "a:title",
            "summaryText" : "a:summary[@type='text']",
            "selfUrl" : "a:link[@rel='self']/@href",
            "terms" : "a:category/@term",
            "contentHtml" : "a:content[@type='html']",
            "published" : "a:published",
            "updated" : "a:updated",
            "authorId" : "a:author/snx:userid",
            "contributorId" : "a:contributor/snx:userid"
        }	
	};
});
},
'sbt/widget/grid/_Grid':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../declare", "../../store/AtomStore", "dojo/_base/lang", "dojo/_base/Deferred", "../../widget/_TemplatedWidget"], 
        function(declare, AtomStore, lang, Deferred, _TemplatedWidget) {

    /*
     * @module sbt._bridge.grid._Grid
     */
    var _Grid = declare([ _TemplatedWidget ], {

        templatePath: require.toUrl("sbt/controls/grid/templates/Grid.html"),
        
        /*
         * Creates an instance of an atom store.
         * @param args - atom store arguments - such as url and attributes
         * @returns {AtomStore} - an instance of an atom store
         */
        _createDefaultStore: function(args) {
            return new AtomStore(args);
        },        
        
        /*retrieves the data from the Atom Store*/
        _doQuery: function(store, options, query) {
            query = query || {};
            var self = this;
            var errCallback = lang.hitch(this, this._updateWithError);
            var results = store.query(query, options);
            
            Deferred.when(results.total, function(totalCount) {
                Deferred.when(results, function(items) {
                    self.data = {
                     items : items,
                     start : options.start,
                     end : options.start + items.length,
                     count : items.length,
                     totalCount : totalCount,
                     response : results.response
                    };
                    try {
                        self.renderer.render(self, self.gridNode, items, self.data);
                        self.onUpdate(self.data);
                    } catch (error) {
                        self.renderer.renderError(self, self.gridNode, error);
                    }
                    return results;
                }, errCallback);
            }, errCallback);
        }
    
    });
    
    return _Grid;
});
},
'sbt/store/AtomStore':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.store.AtomStore
 */
define(["../declare","../config","../lang", "../base/core", "../xml", "../xpath", "../itemFactory",
        "dojo/_base/Deferred", "dojo/promise/Promise", "dojo/store/util/QueryResults", "dojox/html/entities"], 
        function(declare,config,lang, core, xml, xpath, itemFactory, Deferred, Promise, QueryResults, entities) {
  
    /**
     * @class sbt.store.AtomStore
     */
    var AtomStorePromise = declare(Promise, {
        // private
        _store : null,
        _isRejected : false,
        _isFulfilled : false,
        _isCancelled : false,
        _callbacks : [],
        _errbacks :  [],
        _endpoint : null,
        _xmlData : null,
        // read only
        totalResults : null,
        startIndex : 0,
        itemsPerPage : 5,
        items : null,
        // public
        url : "",
        sendQuery : true,
        unescapeHTML : false,
        atom : core.feedXPath,
        attributes : core.entryXPath,
        namespaces : core.namespaces,
        paramSchema: {},
        
        /**
         * Constructor for the AtomStore promise.
         * @param args requires
         * 	endpoint: the endpoint to be used
         */
        constructor: function(args, query, options) {
            this._endpoint = config.findEndpoint(args.endpoint || "connections");
            this._options = options;
            this._callbacks = [];
            this._errbacks = [];
            
            if (args) {
                this.url = args.url;
                this.attributes = args.attributes || this.attributes;
                this.atom = args.feedXPath || this.atom;
                this.namespaces = args.namespaces || this.namespaces;
                this.sendQuery = args.hasOwnProperty("sendQuery") ? args.sendQuery : this.sendQuery;
                this.unescapeHTML = args.unescapeHTML || this.unescapeHTML;
                this.paramSchema = args.paramSchema || this.paramSchema;
            }
            
            // add paging information to the query
            if (this.paramSchema.pageNumber) {
            	var page = Math.floor(options.start / options.count) + 1;
            	query.pageNumber = query.pageNumber || page;
            }
            if (this.paramSchema.startIndex) {
            	query.startIndex = query.startIndex || options.start;
            }
            if (this.paramSchema.pageSize) {
            	query.pageSize = query.pageSize || options.count;
            }
            
            // add the sorting information to the query
            if(options.sort && options.sort[0]) {
                if(options.sort[0].attribute) {
                    query.sortBy = options.sort[0].attribute;
                }

                if(options.sort[0].descending === true) {
                    query.sortOrder = "desc";
                }
                else if(options.sort[0].descending === false) {
                    query.sortOrder = "asc";
                }
            }

            var fetchArgs = {
                query : query
            };

            this._doFetch(fetchArgs);
        },

        /*
         * Add new callbacks to the promise.
         */
        then: function(callback, errback, progback) {
            if (this._isFulfilled) {
                callback(this.items);
                return;
            }
            
            if (callback) {
                this._callbacks.push(callback);
            }
            if (errback) {
                this._errbacks.push(errback);
            }
        },

        /*
         * Inform the deferred it may cancel its asynchronous operation.
         */
        cancel: function(reason, strict) {
            this._isCancelled = true;
        },

        /*
         * Checks whether the promise has been resolved.
         */
        isResolved: function() {
            return this._isRejected || this._isFulfilled;
        },

        /*
         * Checks whether the promise has been rejected.
         */
        isRejected: function() {
            return this._isRejected;
        },

        /*
         * Checks whether the promise has been resolved or rejected.
         */
        isFulfilled: function() {
            return this._isFulfilled;
        },

        /*
         * Checks whether the promise has been canceled.
         */
        isCanceled: function() {
            return this._isCancelled;
        },
        
        // Internals
        
        /*
         * Given a query and set of defined options, such as a start and count of items to return,
         * this method executes the query and makes the results available as data items.
         */
        _doFetch: function(args) {
            var self = this;
            var scope = args.scope || self;
            
            var serviceUrl = this._getServiceUrl(args.query);
            if (!serviceUrl) {
                if (args.onError) {
                    args.onError.call(new Error("sbt.store.AtomStore: No service URL specified."));
                }
                return;
            }
            
            this._endpoint.xhrGet({
                serviceUrl : serviceUrl,
                handleAs : "text",
                load : function(response) {
                    try {
                        // parse the data
                    	self.response = response;
                        self._xmlData = xml.parse(response);
                        self.totalResults = parseInt(xpath.selectText(self._xmlData, self.atom.totalResults, self.namespaces));
                        self.startIndex = parseInt(xpath.selectText(self._xmlData, self.atom.startIndex, self.namespaces));
                        self.itemsPerPage = parseInt(xpath.selectText(self._xmlData, self.atom.itemsPerPage, self.namespaces));
                        self.items = self._createItems(self._xmlData);
                        //self.items = itemFactory.createItems(self._xmlData, self.attributes, self, entities);
                        
                        if (self._options.onComplete) {
                            self._options.onComplete.call(self._options.scope || self, self.items, self._options);
                        }
                        // invoke callbacks
                        self._fulfilled(self.totalResults);
                    } catch (error) {
                        self._rejected(error);
                    }
                },
                error : function(error) {
                    self._rejected(error);
                }
            });
        },
        
        /*
         * Create the service url and include query params
         */
        _getServiceUrl: function(query) {
            if (!this.sendQuery) {
                return this.url;
            }
            if (!query) {
                return this.url;
            }
            if (lang.isString(query)) {
            	return this.url + (~this.url.indexOf('?') ? '&' : '?') + query;
            }
            
            var pairs = [];
            var paramSchema = this.paramSchema;
            for(var key in query) {
                if (key in paramSchema) {
                    var val = paramSchema[key].format(query[key]);
                    if (val) {
                    	pairs.push(val);
                    }
                } else {
                	pairs.push(encodeURIComponent(key) + "=" + encodeURIComponent(query[key]));
                }
            }
            if (pairs.length == 0) {
                return this.url;
            }
            
            return this.url + (~this.url.indexOf('?') ? '&' : '?') + pairs.join("&");
        },
        
        /*
         * Create a query string from an object
         */
        _createQuery: function(queryMap) {
            if (!queryMap) {
                return null;
            }
            var pairs = [];
            for(var name in queryMap){
                var value = queryMap[name];
                pairs.push(encodeURIComponent(name) + "=" + encodeURIComponent(value));
            }
            return pairs.join("&");
        },        

        _createItems: function(document) {
            var nodes = xpath.selectNodes(document, this.atom.entries, this.namespaces);
            var items = [];
            for (var i=0; i<nodes.length; i++) {
                items.push(this._createItem(nodes[i]));
            }
            return items;
        },
        
        _createItem: function(element) {
            var attribs = this._getAttributes();
            var xpathCountFunction = /^count\(.*\)$/;
            
            // TODO add item.index and item.attribs
            var item = { 
                element : element,
                getValue : function(attrib) { 
                	var result = [];
                	 if(typeof this[attrib] == "object"){
                     	for(var i=0;i<this[attrib].length; i++){
                     		result[i] = entities.encode(this[attrib][i]);
                     	}
                     }
                     else{
                    	 result = entities.encode(this[attrib]);
                     }
                	
                	return result; 
                }
            };
            for (var i=0; i<attribs.length; i++) {
                var attrib = attribs[i];
                var access = this.attributes[attrib];
                if (lang.isFunction(access)) {
                    item[attrib] = access(this, item);
                } else if (access.match(xpathCountFunction)){
                    item[attrib] = xpath.selectNumber(element, access, this.namespaces)+"";
                } else {
                    var nodes = xpath.selectNodes(element, access, this.namespaces);
                    if (nodes && nodes.length == 1) {
                        item[attrib] = nodes[0].text || nodes[0].textContent;
                    } else if (nodes) {
                        item[attrib] = [];
                        for (var j=0; j<nodes.length; j++) {
                            item[attrib].push(nodes[j].text || nodes[j].textContent);
                        }
                    } else {
                        item[attrib] = null;
                    }
                }
                
                //item[attrib] = (this.unescapeHTML) ? entities.decode(item[attrib]) : item[attrib];
            }
           
            return item;
        },
        
        _getAttributes: function() {
            var result = [];
            for (var name in this.attributes) {
                if (this.attributes.hasOwnProperty(name)) {
                    result.push(name);
                }
            }
            return result;
        },
        
        _fulfilled : function(totalCount) {
            if (this._isCancelled) {
                return;
            }
            this._isFulfilled = true;
            while (this._callbacks.length > 0) {
                var callback = this._callbacks.shift();
                callback(totalCount);
            }
        },
        
        _rejected : function(error) {
            if (this._isCancelled) {
                return;
            }
            this._isRejected = true;
            while (this._errbacks.length > 0) {
                var errback = this._errbacks.shift();
                errback(error);
            }
        }
        
    });
    
    /**
      * @module sbt.store.AtomStore
      */
    var AtomStore = declare(null, {
        
        // Indicates the property to use as the identity property. The values of this
        // property should be unique.
        idProperty: "id",
        
        _args : null,
        
        /**
         * Constructor for the Atom store.
         * 
         * @param args
         * An anonymous object to initialize properties. It expects the following values:
         *   url: The url to a service or an XML document that represents the store 
         *   unescapeHTML: A boolean to specify whether or not to unescape HTML text 
         *   sendQuery: A boolean indicate to add a query string to the service URL  
         */
        constructor: function(args) {
            this._args = args;

            //if(!args.url) {
            //    throw new Error("sbt.store.AtomStore: A service URL must be specified when creating the data store");
            //}
        },
        
        /**
         * @method getEndpoint
         * @returns
         */
        getEndpoint: function() {
        	return config.findEndpoint(this._args.endpoint || "connections");
        },
        
        /**
         * Retrieves an object by its identity
         * @method get
         * @param id
         */
        get: function(id) {
            throw new Error("sbt.store.AtomStore: Not implemented yet!");
        },

        /**
         * Returns an object's identity
         * @method getIdentity
         * @param object
         */
        getIdentity: function(object) {
            return object.id;
        },
        
        setUrl: function(url){
        	this._args.url = url;
        },
        
        getUrl: function(){
            return this._args.url;
        },
        
        setAttributes: function(attributes){
        	this._args.attributes = attributes;
        },
        
        /**
         * Queries the store for objects. This does not alter the store, but returns a set of data from the store.
         * @method query
         * @param query
         * @param options
         */
        query: function(query, options) {
            var results = new AtomStorePromise(this._args, query, options);
            results.total = results;
            return QueryResults(results);
        }
    });
    return AtomStore;
    
});

},
'sbt/controls/grid/ViewProfileAction':function(){
/*
 * � Copyright IBM Corp. 2013
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

define([ "../../declare", "./GridAction","../../stringUtil"], 
        function(declare, GridAction, stringUtil) {

    /**
     * @class ViewProfileAction
     * @namespace sbt.connections.controls
     * @module sbt.connections.controls.ViewProfileAction
     */
    var ViewProfileAction = declare(GridAction, {
       
    	
        /**ViewProfileAction Constructor function
         * @method constructor
         * */
        constructor: function(args) {

        },
        
      
        openAuthorProfile: function(data,store,baseProfilesUrl){
        	var endpoint = store.getEndpoint();
        	var profileURL = endpoint.baseUrl + baseProfilesUrl +"/html/myProfileView.do?userid="+data.authorId;
        	document.location.href = profileURL;
        }

    });

    return ViewProfileAction;
});
},
'sbt/controls/panel/_ProfilePanel':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.controls.panel._ProfilePanel
 */
define(["../../declare", "../../lang", "../../dom", "../../widget/_TemplatedWidget"], 
        function(declare, lang, dom, _TemplatedWidget) {

    /**
     * @module sbt.controls.panel._ProfilePanel
     */
    var _ProfilePanel = declare([ _TemplatedWidget ], {
    	
    	templateString: "<div><strong> Loading profile... </strong></div>",
    	template: null,
    	
    	profile: null,
    	
    	errorClass: null,
    	
        constructor: function(args) {
            lang.mixin(this, args);
            
            if (this.templateId) {
            	this.template = this._getTemplate(this.templateId);
            }
        },
        
        postMixInProperties: function() {
        },

        postCreate: function() {
        	this.inherited(arguments);
        	
        	if (this.email || this.userid) {
        		this.getProfile(this.email || this.userid);
        	} else {
        		this.getMyProfile();
        	}
        },
        
        getMyProfile: function() {
        },
        
        getProfile: function(id) {
        },
        
        showProfile: function(profile) {
        	this.profile = profile || this.profile;
        	if (!this.profile) {
        		this._displayError(new Error("Invalid profile"));
        		return;
        	}
        	
        	try {
            	var el = this.domNode;
            	while (el.childNodes[0]) {
                    this._destroy(el.childNodes[0]);
                }
            	
            	var node;
                if (this._isString(this.template)) {
                    var domStr = this._substituteItems(this.template, this.profile);
                    node = this._toDom(domStr, el.ownerDocument);
                } else {
                    node = this.template.cloneNode(true);
                }
                el.appendChild(node);
        	} catch (error) {
        		this._displayError(error);
        	}
        },
        
        getThumbnailAlt: function() {
        	return this.profile.getName() || "";
        },
        
        // Internals
        
        _substituteItems : function(template, profile) {
            var self = this;
            return this._substitute(template, profile, function(value,key) {
                if (typeof value == "undefined") {
                    // check the self for the property
                    value = this._getObject(key, false, self);
                }

                if (typeof value == 'function') {
                    // invoke function to return the value
                    try {
                        value = value.apply(profile);
                    } catch (ex) {
                        try {
                            value = value.apply(self, [profile]);
                        } catch (ex1) {
                            value = "ERROR:" + key + " " + ex1;
                        }
                    }
                }

                if (typeof value == "undefined" || value == null) {
                    return "";
                }

                return value;
            }, this);
        },
                
        _displayError: function(error) {
        	var el = this.domNode;
            while (el.childNodes[0]) {
                this._destroy(el.childNodes[0]);
            }
           var ediv = this._create("div", {
              "class": this.errorClass,
              innerHTML: error,
              role: "alert",
              tabIndex: 0
            }, el, "only");
        },
        
    	_getTemplate: function(domId) {
            var domNode = dom.byId(domId);
            return domNode ? domNode.innerHTML : "<strong>Unable to load template: "+domId+"</strong>";
        }

    });
    
    return _ProfilePanel;
});
},
'sbt/smartcloud/controls/BaseGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../declare", "../../controls/grid/GridRenderer",
        "../../text!../../controls/grid/templates/GridPager.html", 
        "../../text!../../controls/grid/templates/GridSorter.html",
        "../../text!../../controls/grid/templates/SortAnchor.html",
        "../../i18n!./nls/BaseGridRenderer" ],
        function(declare, GridRenderer, GridPager, GridSorter, SortAnchor, nls) {

    /**
     * @module sbt.smartcloud.controls.BaseGridRenderer
     * @class BaseGridRenderer
     * @namespace sbt.smartcloud.controls
     */
    var BaseGridRenderer = declare(GridRenderer, {
    	
    	/**Strings used in the grid*/
        nls : {},
        /**CSS class for tables*/
        tableClass : "lotusTable",
        /**CSS Class for empty icon*/
        emptyClass : "lconnEmpty",
        /**CSS Class for an error on an icon*/
        errorClass : "lconnEmpty",
        /**The css class to use when the grid is loading,null here as an image is used instead*/
        loadingClass : "",
        /**The loading image*/
        loadingImgClass : "lotusLoading",
        /**The css class for the first row of the grid*/
        firstClass : "lotusFirst",
        /**CSS classes for sorting*/
        defaultSortClass : "lotusActiveSort lotusDescending",
        ascendingSortClass : "lotusActiveSort lotusAscending",
        descendingSortClass : "lotusActiveSort lotusDescending",
        /**The HTML template to use to show paging (moving forward and backward through sets of results)*/
        pagerTemplate : GridPager,
        /**The HTML template to show sorting options*/
        sortTemplate : GridSorter,
        /**The HTML template for sort Anchors*/
        sortAnchor : SortAnchor,

        /**
         * Merge this class with the GridRenderer Class
         * @method constructor
         * @param args
         */
        constructor : function(args) {
            this._mixin(this, args);
            this.nls = this._mixin(nls, this._nls);
        }

    });

    return BaseGridRenderer;
});
},
'sbt/connections/controls/activities/ActivityAction':function(){
/*
 * � Copyright IBM Corp. 2013
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

define([ "../../../declare", "../../../controls/grid/GridAction"], 
        function(declare, GridAction) {

    /**
     * @class ActivityAction
     * @namespace sbt.connections.controls.activities
     * @module sbt.connections.controls.activities.ActivityAction
     */
    var ActivityGridAction = declare(GridAction, {
       
    	
        /**ForumAction Constructor function
         * @method constructor
         * @param args
         */
        constructor: function(args) {

        },
        
        /**
         * Handles displaying a tooltip for an item
         * @method getTooltip
         * @param item The element that will use the tooltip
         */
        getTooltip: function(item) {
        	 return item.getValue("title");
        },
        
        /**
         * The execute function is called from the handle click function
         * 
         * @method execute
         * @param item The item which fired the event
         * @param opts
         * @param event The event
         */
        execute: function(item, grid, event) {
        	document.location.href = item.getValue("recentUpdatesUrl");
        }


    });

    return ActivityGridAction;
});
},
'sbt/connections/controls/activities/ActivityGrid':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../../declare",
         "../../../controls/grid/Grid",
         "../../../store/parameter",
         "./ActivityGridRenderer",
         "./ActivityAction",
         "../../../connections/ActivityConstants"], 

function(declare, Grid, parameter, ActivityGridRenderer, ActivityAction, consts) {
	
	/**Sorting Values*/
	var sortVals = {
			modified: "lastmod",
			dueDate: "duedate",
			name: "title"
	};
	
	/**URL parameters */
	var ParamSchema = {
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortfields",sortVals),
		sortOrder: parameter.sortOrder("sortorder")						
	};
	
	/**
     * @class ActivityGrid
     * @namespace  sbt.connections.controls.activities
     * @module sbt.connections.controls.activities.ActivityGrid
     */
	var ActivityGrid = declare(Grid,{

		options : {
	            "my" : {
	                storeArgs : {
	                    url : consts.AtomActivitiesMy,
	                    attributes : consts.ActivityNodeXPath,
	                    feedXPath : consts.ActivitiesFeedXPath,
	                    paramSchema: ParamSchema
	                },
	                rendererArgs : {
	                    type : "my"
	                }
	            }
		 },
		
		 /**The default type to use if none is specified */
		defaultOption: "my", 
		
		/**class to handle on click and tooltip actions  */
		activityAction : new ActivityAction(),
		
		/**
		 * ActivitiesGrid Constructor
		 * @method constructor
		 * @param args
		 */
		constructor: function(args){
			/**
             * Set the sorting information
             */
            this._sortInfo = {
                modified: { 
                    title: this.renderer._nls.modified, 
                    sortMethod: "sortBylastModified",
                    sortParameter: "date"
                },
                dueDate: {
                    title: this.renderer._nls.dueDate, 
                    sortMethod: "sortByDueDate",
                    sortParameter: "dueDate"
                },
                name: { 
                    title: this.renderer._nls.name, 
                    sortMethod: "sortByName",
                    sortParameter: "name"
                }
            };
            this._activeSortAnchor = this._sortInfo.name;
            this._activeSortIsDesc = true;
		},
		
		/**
		 * Returns Sorting info
		 * @method getSortInfo
		 * @returns {#an object containing sorting information}
		 */
		getSortInfo : function() {
			return {
				active : {
					anchor : this._activeSortAnchor,
					isDesc : this._activeSortIsDesc
				},
				list : [ this._sortInfo.modified, this._sortInfo.dueDate,
						this._sortInfo.name ]
			};
		},
		
		/**
		 * Sort the activities by last modification date
		 * @method sortByLastModified
		 * @param el The Grid Element
		 * @param data The grid data
		 * @param ev the event
		 */
		sortBylastModified: function(el, data, ev){
			this._sort("modified", true, el, data, ev);
		},
		
		/**
		 * Sort the activities by Due Date
		 * @method sortByDueDate
		 * @param el The Grid Element
		 * @param data The grid data
		 * @param ev the event
		 */
		sortByDueDate: function(el, data, ev){
			this._sort("dueDate", true, el, data, ev);
		},
		
		/**
		 * Sort the activities by name
		 * @method sortByName
		 * @param el The Grid Element
		 * @param data The grid data
		 * @param ev the event
		 */
		sortByName: function(el, data, ev){
			this._sort("name", true, el, data, ev);
		},
		
		/**
		 * Event handler function for onClick events 
		 * @method handleClick
		 * @param el The Grid Element
		 * @param data The grid data
		 * @param ev the event
		 */
		handleClick: function(el, data, ev) {
	        if (this.activityAction) {
	           this._stopEvent(ev);
	           this.activityAction.execute(data, this , ev);
	        }
	    },
		 
		 /**
         * Creates a renderer for the grid.The renderer is responsible for 
         * loading the grid's HTML content.
         * @method createDefaultRenderer
         * @param args sets the template the renderer will use, by checking args.type
         * @returns an instance of an ActivitiesGridRenderer.
         */
        createDefaultRenderer : function(args) {
            return new ActivityGridRenderer(args);
        }
				
	});
	
	return ActivityGrid;
});
},
'sbt/store/parameter':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../stringUtil", "../config"], function(stringUtil, config) {
    var Formatter = {
        defaultFormat: function(param, val) {
            return param.key + "=" + val;
        },
        sortField: function(vals) {
            return function(param, val) {
                var v = vals[val] || "";
                return param.key + "=" + v;
            };
        },
        
        ascSortOrderBoolean: function(param, val) {
            var v = (val === "asc") ? true : false;
            return param.key + "=" + v;
        },
        
        sortOrder: function(param,val){
        	var v = (val === "asc") ? "desc" : "asc";
            return param.key + "=" + v;
        },
        
        oneBasedInteger: function(param, val) {
            var v = Math.floor(val);
            if(v < 1) {
                v = 1;
            }
            return param.key + "=" + v;
        },
        
        zeroBaseInteger: function(param, val) {
            var v = Math.floor(val);
            if(v < 0) {
                v = 0;
            }
            return param.key + "=" + v;
        }

    };
    
    
    var Parameter = function(args) {
        var self = this;
        this.key = args.key;
        var formatFunc = args.format || Formatter.defaultFormat;
        this.format = function(val) {
            return formatFunc(self, val);
        };
    };
    
   
    return {
    	 defaultFormat: function (key){
			 return new Parameter({key: key, format: Formatter.defaultFormat}); 
		 },
		 
		 sortField: function(key, sortVals){
			return new Parameter({key: key, format: Formatter.sortField(sortVals)}); 
		 },
		 
		 sortOrder: function(key){
			 return new Parameter({key: key, format: Formatter.sortOrder});
		 },
		 
		 booleanSortOrder: function (key){
			 return new Parameter({key: key, format: Formatter.ascSortOrderBoolean});
		 },
        
        /**
         * 
         * @param key
         * @returns
         */
        zeroBasedInteger :  function(key) {
        	return new Parameter({ key: key, format: Formatter.zeroBasedInteger });
        },
        
        /**
         * 
         * @param key
         * @returns
         */
        oneBasedInteger :  function(key) {
        	return new Parameter({ key: key, format: Formatter.oneBasedInteger });
        }
    };
});

},
'sbt/connections/controls/activities/ActivityGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

define(["../../../declare",
        "../ConnectionsGridRenderer",
        "../../../i18n",
        "../../../text!./templates/ActivityRow.html",
        "../../../i18n!./nls/ActivityGridRenderer"], 

    function(declare, ConnectionsGridRenderer, i18n, ActivityRow, nls){
		
		/**
		 * @class ActivityGridRenderer
		 * @namespace sbt.connections.controls.activities
		 * @module sbt.connections.controls.forum.ActivityGridRenderer
		 */
	    var ActivityGridRenderer = declare(ConnectionsGridRenderer,{
	    	
	    	/**Strings used by the forum grid */
	    	_nls:nls,
  	
	    	/**
	    	 * The constructor function
	    	 * @method constructor
	    	 * @param args
	    	 */
	    	constructor: function(args){
	    		this.template = ActivityRow;
	    	},

	        /**
	          * Displays a tooltip by calling the getTooltip function in the ForumAction class
	          * @method tooltip
	          * @param grid The Grid element
	          * @param item the element to display the tooltip
	          * @param i the number of the current row
	          * @param items all of the items in the grid row
	          * @returns A String used as a tooltip
	          */
	         tooltip: function(grid, item, i, items) {
	             if (grid.activitiesAction) {
	                 return grid.activitiesAction.getTooltip(item);
	             }
	         },
	         
	         getUserProfileHref: function(grid,item,i,items){
	        	 return this.getProfileUrl(grid,item.getValue("authorUserId"));
	         }
	    	
	    });
	
	return ActivityGridRenderer;
});
},
'sbt/i18n':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * @module sbt.i18n
 */
define(['./_bridge/i18n'],function(i18n) {
    var nls = {
        todayAt : "today at ",
        on : "on "
    };
    
    i18n.getUpdatedLabel = function(dateStr) {
        var date = new Date(dateStr);
        var dateClone = new Date(date.getTime());
        var now = new Date();
        if (dateClone.setHours(0,0,0,0) == now.setHours(0,0,0,0)) {
            return nls.todayAt + this.getLocalizedTime(date);
        } else {
            return nls.on + this.getLocalizedDate(date);
        }
    };
        
    i18n.getSearchUpdatedLabel = function(dateStr) {
        var date = new Date(dateStr);
        var dateClone = new Date(date.getTime());
        var now = new Date();
        if (dateClone.setHours(0,0,0,0) == now.setHours(0,0,0,0)) {
            return nls.todayAt + this.getLocalizedTime(date);
        } else {
            return this.getLocalizedDate(date);
        }
    };
    return i18n;
});



},
'sbt/connections/ActivityConstants':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * Social Business Toolkit SDK. Definition of constants for ActivityService.
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang, conn) {

	return lang.mixin(conn, {

		/**
		 * Map to store all possible types of activity node entry types
		 */
		ActivityNodeTypes : {
			Activity : "activity",
			Chat : "chat",
			Email : "email",
			Entry : "entry",
			EntryTemplate : "entrytemplate",
			Reply : "reply",
			Section : "section",
			ToDo : "todo"
		},
		
		/**
		 * XPath expressions used when parsing a Connections Activities ATOM feed
		 */
		ActivitiesFeedXPath : conn.ConnectionsFeedXPath,

		/**
		 * XPath expressions for Person Fields
		 */
		PersonFieldXPath : {
			name : "a:name",
			userId : "snx:userid",
			email : "a:email"

		},

		/**
		 * XPath expressions for File Fields
		 */
		FileFieldXPath : {
			url : "a:link[@rel='enclosure']/@href",
			type : "a:link[@rel='enclosure']/@type",
			size : "a:link[@rel='enclosure']/@size",
			length : "a:link/@length"
		},

		/**
		 * XPath expressions for Link Fields
		 */
		LinkFieldXPath : {
			url : "a:link/@href",
			title : "a:link/@title"
		},

		/**
		 * XPath expressions for Text Fields
		 */
		TextFieldXPath : {
			summary : "a:summary"
		},

		/**
		 * XPath expressions to be used when reading an activity Node entry
		 */
		ActivityNodeXPath : {
			entry : "/a:entry",
			uid : "a:id",
			activityUuid : "snx:activity",
			title : "a:title",
			updated : "a:updated",
			published : "a:published",
			categoryFlagCompleted : "a:category[@term='completed']/@label",
			categoryFlagTemplate : "a:category[@term='template']/@label",
			categoryFlagDelete : "a:category[@term='deleted']/@label",

			authorName : "a:author/a:name",
			authorUserId : "a:author/snx:userid",
			authorEmail : "a:author/a:email",
			authorUserState : "a:author/snx:userState",
			authorLdapid : "a:author/snx:ldapid",

			contributorName : "a:contributor/a:name",
			contributorUserId : "a:contributor/snx:userid",
			contributorEmail : "a:contributor/a:email",
			contributorUserState : "a:contributor/snx:userState",
			contributorLdapid : "a:contributor/snx:ldapid",

			type : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@label",
			priority : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/priority']/@label",

			coummunityUuid : "snx:communityUuid",
			communityUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/container']/@href",

			dueDate : "snx:duedate",
			membersUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/member-list']/@href",
			historyUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/history']/@href",
			templatesUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/templates']/@href",

			editUrl : "a:link[@rel='edit']/@href",
			selfUrl : "a:link[@rel='self']/@href",
			recentUpdatesUrl : "a:link[@rel='alternate']/@href",

			position : "snx:position",
			depth : "snx:depth",
			permissions : "snx:permissions",
			iconUrl : "snx:icon",
			content : "a:content",

			tags : "a:category[not(@scheme)]/@term",

			inReplyToId : "thr:in-reply-to/@ref",
			inReplyToUrl : "thr:in-reply-to/@href",
			inReplyToActivity : "thr:in-reply-to/snx:activity",

			assignedToName : "snx:assignedto/@name",
			assignedToUserId : "snx:assignedto/@userid",
			assignedToEmail : "snx:assignedto",

			textFields : "snx:field[@type='text']",
			linkFields : "snx:field[@type='link']",
			personFields : "snx:field[@type='person']",
			dateFields : "snx:field[@type='date']",
			fileFields : "snx:field[@type='file']"

		},

		/**
		 * XPath expressions to be used when reading an Tag Node entry
		 */
		TagXPath : {
			term : "@term",
			frequency : "@snx:frequency",
			entries : "app:categories/a:category",
			uid : "@term",
			bin : "@snx:bin"
		},

		/**
		 * XPath expressions to be used when reading an Member Node entry
		 */
		MemberXPath : {			
			uid : "a:id",
			entry : "a:feed/a:entry",
			name : "a:contributor/a:name",
			email : "a:contributor/a:email",
			userId : "a:contributor/snx:userid",
			role : "a:contributor/snx:role",
			userState : "a:contributor/snx:userState",
			title : "a:title",
			updated : "a:updated",
			summary : "a:summary",
			editUrl : "a:link[@rel='edit']/@href",
			category : "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term",
			role : "snx:role",
			permissions : "snx:permissions"
		},

		/**
		 * Search for content in all of the activities, both completed and active, that matches a specific criteria.
		 */
		AtomActivitiesEverything : "/${activities}/service/atom2/everything",

		/**
		 * Get a feed of all active activities that match a specific criteria.
		 */
		AtomActivitiesMy : "${activities}/service/atom2/activities",

		/**
		 * Get a feed of all active activities in trash
		 */
		AtomActivitiesTrash : "${activities}/service/atom2/trash",

		/**
		 * Search for a set of completed activities that match a specific criteria.
		 */
		AtomActivitiesCompleted : "${activities}/service/atom2/completed",

		/**
		 * Get Activity node feed
		 */
		AtomActivityNode : "${activities}/service/atom2/activitynode",
		
		/**
		 * Get feed of all Activity Nodes in an Activity
		 */
		AtomActivityNodes : "${activities}/service/atom2/descendants", //?nodeUuid="

		/**
		 * Get Activity Node feed from Trash
		 */
		AtomActivityNodeTrash : "${activities}/service/atom2/trashednode",

		/**
		 * Create a new Activity
		 */
		AtomCreateActivityNode : "${activities}/service/atom2/activity",

		/**
		 * Get a Feeds of all ToDo Entries in an activity
		 */
		AtomActivitiesToDos : "${activities}/service/atom2/todos",

		/**
		 * Get a feed of Activity Members
		 */
		AtomActivitiesMembers : "${activities}/service/atom2/acl",
		
		/**
		 * Get a member for an activity
		 */
		AtomActivitiesMember : "${activities}/service/atom2/acl?activityUuid={activityUuid}&amp;memberid={memberId}",

		/**
		 * Get all tags for an activity
		 */
		AtomActivitiesTags : "${activities}/service/atom2/tags"

	}, conn);
});
},
'sbt/connections/ConnectionsConstants':function(){
/*
 * � Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Definition of constants for IBM Connections.
 * 
 * @module sbt.connections.ConnectionsConstants
 */
define([ "../lang", "../base/BaseConstants" ], function(lang, base) {

    return lang.mixin(base, {

        /**
         * Error code used for a bad request
         */
        BadRequest : 400,

        /**
		 * XPath expressions used when parsing a Connections ATOM feed
		 */
        ConnectionsFeedXPath : {
            // used by getEntitiesDataArray
            entries : "/a:feed/a:entry",
            // used by getSummary
            totalResults : "/a:feed/opensearch:totalResults",
            startIndex : "/a:feed/opensearch:startIndex",
            itemsPerPage : "/a:feed/opensearch:itemsPerPage"
        },

        /**
         * AuthType variable values for endpoint
         */
        AuthTypes : {
        	OAuth : "oauth",
        	Basic : "basic"
        },
        
        /**
         * XPath expressions to be used when reading a Connections entity
         * 
         * @property TagsXPath
         * @type Object
         * @for sbt.connections.Tag
         */
        TagsXPath : {
        	entries : "app:categories/a:category",
			term : "@term",
			frequency : "@snx:frequency",
			uid : "@term"
		}

    });
});
},
'sbt/base/BaseConstants':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK. Definition of constants for BaseService.
 */
define([ "../config" ], function(sbt) {
    return {
    	/**
    	 * Error codes used by the SBT based service implementations.
    	 */
        sbtErrorCodes : {
            badRequest : 400
        },
    
        /**
         * Namespaces to be used when reading the Connections ATOM field
         */
		Namespaces : {
			o : "http://ns.opensocial.org/2008/opensocial",
			app : "http://www.w3.org/2007/app",
			thr : "http://purl.org/syndication/thread/1.0",
			fh : "http://purl.org/syndication/history/1.0",
			snx : "http://www.ibm.com/xmlns/prod/sn",
			opensearch : "http://a9.com/-/spec/opensearch/1.1/",
			a : "http://www.w3.org/2005/Atom",
			h : "http://www.w3.org/1999/xhtml",
			td : "urn:ibm.com/td",
			relevance : "http://a9.com/-/opensearch/extensions/relevance/1.0/",
			ibmsc : "http://www.ibm.com/search/content/2010",
			xhtml : "http://www.w3.org/1999/xhtml",
			spelling : "http://a9.com/-/opensearch/extensions/spelling/1.0/",
			ca : "http://www.ibm.com/xmlns/prod/composite-applications/v1.0 namespace"
		},
        
	    /**
		 * XPath expressions used when parsing an ATOM feed
		 */
	    AtomFeedXPath : {
	        // used by getEntitiesDataArray
	        entries : "/a:feed/a:entry",
	        // used by getSummary
	        totalResults : "/a:feed/opensearch:totalResults",
	        startIndex : "/a:feed/opensearch:startIndex",
	        itemsPerPage : "/a:feed/opensearch:itemsPerPage"
	    },
	
        /**
         * XPath expressions to be used when reading an forum topic entry
         */
        AtomEntryXPath : {
            // used by getEntityData
            entry : "/a:entry",
            // used by getEntityId
            uid : "a:id",
            // used by getters
            id : "a:id",
            title : "a:title",
            updated : "a:updated",
            published : "a:published",
            authorName : "a:author/a:name",
            authorEmail : "a:author/a:email",
            authorUserid : "a:author/snx:userid",
            authorUserState : "a:author/snx:userState",
            contributorName : "a:contributor/a:name",
            contributorEmail : "a:contributor/a:email",
            contributorUserid : "a:contributor/snx:userid",
            contributorUserState : "a:contributor/snx:userState",
    		content : "a:content[@type='html']",
    		summary : "a:summary[@type='text']",
    		categoryTerm : "a:category/@term",
            editUrl : "a:link[@rel='edit']/@href",
            selfUrl : "a:link[@rel='self']/@href",
            alternateUrl : "a:link[@rel='alternate']/@href"
        },
        
	    /**
	     * 
	     */
	    AtomXmlHeaders : {
	        "Content-Type" : "application/atom+xml"
	    }
	    
    };
});
},
'sbt/connections/controls/astream/ActivityStreamWrapper':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../../declare", "../../../url", "../../../config", "../../../util", "../../../lang", "../../../connections/controls/WidgetWrapper", "../../../text!../../../connections/controls/astream/templates/ActivityStreamContent.html"], function(declare, Url, config, util, lang, WidgetWrapper, defaultTemplate) {

    /**
     * The wrapper for the ActivityStream.
     * This class just has to provide its own template and the args it receives back to to the WidgetWrapper, which will take care of everything else.
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
         * @method getTransformObject
         * @returns {Object}
         */
        getTransformObject: function(){
            var proxyUrl = this._endpoint.proxy.proxyUrl + "/" + this._endpoint.proxyPath;
            var connectionsUrl = this._endpoint.baseUrl;
            var libUrl = new Url(config.Properties.libraryUrl);
            var libQuery = libUrl.getQuery();
            var libQueryObj = util.splitQuery(libQuery, "&");
            
            lang.mixin(libQueryObj, {
                lib: "dojo",
                ver: "1.4.3"
            });
            libQuery = util.createQuery(libQueryObj, "&");
            libUrl.setQuery(libQuery);
            
            var connectionsSideNav = "~com.ibm.social.as.gadget.viewnav.ASGadgetViewSideNav.js";
            var cssUrl = connectionsUrl + "/${connections}/resources/web/com.ibm.social.as/css/activityStream.css";
            if(this._endpoint.name == "w3connections"){
                connectionsSideNav = "~com.ibm.social.as.nav.ASSideNav.js";
                cssUrl = connectionsUrl + "/${connections}/resources/web/_lconnappstyles/gen4/activityStream.css?version=oneui3&rtl=false";
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
                connectionsASNav: connectionsSideNav,
                cssUrl: cssUrl
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
         * The constructor. This will set up the connections ActivityStream dijit with a config object, and create the ShareBox and SideNav.
         * Takes EITHER a feedUrl and an optional extensions object, OR an ActivityStream config object. 
         * If a feedUrl is specified any config object supplied will be ignored.
         * 
         * @method constructor
         * 
         * @param {Object} args
         *     @params {String} args.activityStreamNode The id of the html element to attach the ActivityStream to.
         *     @params {String} args.shareBoxNode The id of the html element to attach the share box to.
         *     @params {String} args.sideNavNode The id of the html element to attach the views side nav to.
         *     @params {String} args.feedUrl The url of the feed to populate the ActivityStream with.
         *     @params {Object} [args.extensions] A simple list of extensions to load.
         *         @params {Boolean} [args.extensions.commenting] If true load the commenting extension.
         *         @params {Boolean} [args.extensions.saving] If true load the saving extension.
         *         @params {Boolean} [args.extensions.refreshButton] If true load the refresh button extension.
         *         @params {Boolean} [args.extensions.DeleteButton] If true load the delete button extension.
         *         
         *     @params {Object} args.config An ActivityStream config object. Only specify this without a feedUrl argument.
         * @param {String} activityStreamNode: The node to attach the ActivityStream to. This should have its div created in the defaultTemplate.
         * @param {String} shareBoxNode: The node to attach the ShareBox to. This should have its div created in the defaultTemplate.
         * @param {String} sideNavNode: The node to attach the views SideNav to. This should have its div created in the defaultTemplate.
         * 
         */
        constructor: function(args){
            this.args = args;
        }
        
    });
    
    return ActivityStreamWrapper;
});
},
'sbt/url':function(){
define(['./declare'], function(declare){
    // regexp    /^(?:(scheme)(:))?(\/\/)(?:(userna)(:)(passwo)(@))?(domain  )(?:(:)(port   ))?(path      )?(?:(\?)(query ))?(?:(#)(fr))?$/
    var URL_RE = /^(?:([A-z]+)(:))?(\/\/)(?:([^?#]*)(:)([^?#]*)(@))?([\w.\-]+)(?:(:)(\d{0,5}))?([\w.\/\-]+)?(?:(\?)([^?#]*))?(?:(#)(.*))?$/;
    var URL_RE_GROUPS = {
        'URL': 0,
        'SCHEME': 1,
        'COLON1': 2,
        'SLASHES': 3,
        'USER': 4,
        'COLON2':5,
        'PASSWORD': 6,
        'AT': 7,
        'HOSTNAME': 8,
        'COLON3':9,
        'PORT': 10,
        'PATH': 11,
        'QUESTION':12,
        'QUERY': 13,
        'HASHSIGN': 14,
        'FRAGMENT': 15
    };
    
    /**
    A class for representing urls.

    @class url 
    @constructor
    **/
    var url = declare(null, {
        /*
        Holds the parts of the url after URL_RE parses the url.

        @property _resultStore 
        @type Array
        **/
        _resultStore: [],
        
        /*
        @method constructor
        **/
        constructor: function(url){
            this.setUrl(url);
        },
        
        /**
        @method getUrl
        @return {String} Returns the url in its current state.
        **/
        getUrl: function(){
            return this._resultStore.slice(1).join("");
        },
        
        /**
        @method setUrl
        @param url {String}
        **/
        setUrl: function(url){
            this._resultStore = URL_RE.exec(url);
        },
        
        /**
        @method getScheme
        @return {String} Returns the scheme in its current state.
        **/
        getScheme: function(){
            return this._resultStore[URL_RE_GROUPS.SCHEME];
        },
        
        /**
        @method setScheme
        @param scheme {String}
        **/
        setScheme: function(scheme){
            this._resultStore[URL_RE_GROUPS.SCHEME] = scheme;
        },
        
        /**
        @method getUser
        @return {String} Returns the username in its current state.
        **/
        getUser: function(){
            return this._resultStore[URL_RE_GROUPS.USER];
        },
        
        /**
        @method setUser
        @param user {String}
        **/
        setUser: function(user){
            this._resultStore[URL_RE_GROUPS.USER] = user;
        },
        
        /**
        @method getPassword
        @return {String} Returns the password (domain) in its current state.
        **/
        getPassword: function(){
            return this._resultStore[URL_RE_GROUPS.PASSWORD];
        },
        
        /**
        @method setPassword
        @param password {String}
        **/
        setPassword: function(password){
            this._resultStore[URL_RE_GROUPS.PASSWORD] = password;
        },
        
        /**
        @method getHostName
        @return {String} Returns the hostname (domain) in its current state.
        **/
        getHostName: function(){
            return this._resultStore[URL_RE_GROUPS.HOSTNAME];
        },
        
        /**
        @method setHostName
        @param hostName {String}
        **/
        setHostName: function(hostName){
            this._resultStore[URL_RE_GROUPS.HOSTNAME] = hostName;
        },

        /**
        @method getPort
        @return {String} Returns the port in its current state.
        **/
        getPort: function(){
            return this._resultStore[URL_RE_GROUPS.PORT];
        },
        
        /**
        @method setPort
        @param port {Number}
        **/
        setPort: function(port){
            this._resultStore[URL_RE_GROUPS.PORT] = port;
        },
        
        /**
        @method getQuery
        @return {String} Returns the query in its current state.
        **/
        getQuery: function(){
            return this._resultStore[URL_RE_GROUPS.QUERY];
        },
        
        /**
        @method setQuery
        @param query {String}
        **/
        setQuery: function(query){
            this._resultStore[URL_RE_GROUPS.QUERY] = query;
        },
        
        /**
        @method getFragment
        @return {String} Returns the fragment in its current state.
        **/
        getFragment: function(){
            return this._resultStore[URL_RE_GROUPS.FRAGMENT];
        },
        
        /**
        @method setFragment
        @param fragment {String}
        **/
        setFragment: function(fragment){
            this._resultStore[URL_RE_GROUPS.FRAGMENT] = fragment;
        },
        
        /**
        @method getBaseUrl
        @return {String} Utility method, returns the url up until before the query.
        **/
        getBaseUrl: function(){
            return this._resultStore.slice(1, URL_RE_GROUPS.QUERY).join("");
        }
    });
    return url;
});
},
'sbt/connections/controls/astream/_ActivityStream':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../../declare", "../../../lang", "../../../dom", "../../../connections/controls/_ConnectionsWidget", "../../../connections/controls/astream/_SbtAsConfigUtil", "../../../connections/controls/sharebox/_InputForm"], function(declare, lang, dom, _ConnectionsWidget, _SbtAsConfigUtil, _InputForm){
    /**
     * Wrapper for the connections ActivityStream Dijit.
     * 
     * @class sbt.controls.astream._ActivityStream
     */
    var _ActivityStream = declare([_ConnectionsWidget],
    {    
        /**
         * The ConfigUtil will be held here.
         * 
         * @property configUtil 
         * @type Object
         * @default null
         */
        configUtil: null,

        /**
         * The InputForm will be held here.
         * 
         * @property inputForm 
         * @type Object
         * @default null
         */
        inputForm: null,

        /**
         * This wraps the given node in a div with class="lotusContent lotusBoard" since the ActivityStream css rules expect such a layout.
         * Used to wrap the ActivityStream's dom node.
         * 
         * @method wrapDomNode
         * 
         * @param {String} domNodeId The id of the dom node to wrap.
         */
        wrapDomNode: function(domNodeId){
            var asNode = document.getElementById(domNodeId);
            var parent = asNode.parentNode;
            var wrapperDiv = document.createElement('div');
            wrapperDiv.className= "lotusContent lotusBoard";
            
            parent.appendChild(wrapperDiv);
            wrapperDiv.appendChild(asNode);
        },
        
        /**
         * The constructor. This will set up the connections ActivityStream dijit with a config object, and create the ShareBox and SideNav.
         * Takes EITHER a feedUrl and an optional extensions object, OR an ActivityStream config object. 
         * If a feedUrl is specified any config object supplied will be ignored.
         * 
         * @method constructor
         * 
         * @param {Object} args
         *     @params {String} args.activityStreamNode The id of the html element to attach the ActivityStream to.
         *     @params {String} args.shareBoxNode The id of the html element to attach the share box to.
         *     @params {String} args.sideNavNode The id of the html element to attach the views side nav to.
         *     @params {String} args.feedUrl The url of the feed to populate the ActivityStream with.
         *     @params {Object} [args.extensions] A simple list of extensions to load.
         *         @params {Boolean} [args.extensions.commenting] If true load the commenting extension.
         *         @params {Boolean} [args.extensions.saving] If true load the saving extension.
         *         @params {Boolean} [args.extensions.refreshButton] If true load the refresh button extension.
         *         @params {Boolean} [args.extensions.DeleteButton] If true load the delete button extension.
         *         
         *     @params {Object} args.config An ActivityStream config object. Only specify this without a feedUrl argument.
         * @param {String} activityStreamNode: The node to attach the ActivityStream to. This should have its div created in the defaultTemplate.
         * @param {String} shareBoxNode: The node to attach the ShareBox to. This should have its div created in the defaultTemplate.
         * @param {String} sideNavNode: The node to attach the views SideNav to. This should have its div created in the defaultTemplate.
         */
        constructor: function(args){
            this.mixinXhrHandler();
            var configUtil = new _SbtAsConfigUtil(this.xhrHandler);
            
            args.xhrHandler = this.xhrHandler;
            if(args.activityStreamNode){
                this.wrapDomNode(args.activityStreamNode);
            }
            var self = this;
            
            configUtil.buildSbtConfig(args).then(function(cfg){
                if(args.sideNavNode && Object.keys(cfg.views).length > 1){
                    if(com.ibm.social.as.nav){
                        new com.ibm.social.as.nav.ASSideNav({
                            configObject : cfg
                        }, dom.byId(args.sideNavNode));
                    }else{
                        new com.ibm.social.as.gadget.viewnav.ASGadgetViewSideNav({
                            configObject : cfg
                        }, dom.byId(args.sideNavNode));
                    }
                }
                if(args.activityStreamNode){
                    window.activityStreamConfig = cfg;
                    new com.ibm.social.as.ActivityStream({
                        configObject: cfg,
                        domNode: args.activityStreamNode,
                        isGadget: false,
                        selectedState: true
                    });
                }
            });
            
        },
        
        /**
         * Overwrite the ActivityStream's XhrHandler with our own.
         * 
         * @method mixinXhrHandler
         */
        mixinXhrHandler: function(){
            if(com.ibm.social.as.util.xhr.XhrHandler.init !== undefined){
                com.ibm.social.as.util.xhr.XhrHandler.init(this.xhrHandler);
            }
            else{
                lang.mixin(com.ibm.social.as.util.AbstractHelper.prototype, this.xhrHandler);
            }
        },
        
        /**
         * Function to update the ActivityStream with the latest data.
         * 
         * @method update
         */
        update: function(){
            dojo.publish("com/ibm/social/as/event/updatestate", [true]);
        }
        
    });
    return _ActivityStream;
});

},
'sbt/connections/controls/astream/_SbtAsConfigUtil':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../../declare", "../../../config", "../../../url"], function(declare, config, Url){
    /*
     * @class sbt.controls.astream._SbtAsConfigUtil A helper module for building ActivityStream config objects.
     */
    var _SbtAsConfigUtil = declare([com.ibm.social.as.gadget.ActivityStreamConfigUtil],
    {
        /*
         * The constructor. 
         * 
         * @method constructor
         * @param {Object} xhrHandler The xhrHandler to use when making requests for user info.
         */
        constructor: function(xhrHandler){
            this.xhrHandler = xhrHandler;
        },
        
        /**
         * Retrieves user info from connections. 
         * @method getUserInfo
         * @returns {lconn.core.util.LCDeferred} A promise. Resolves with a user info object containing a user id, an os id and a displayName.
         */
        getUserInfo: function() {
            var promise = new lconn.core.util.LCDeferred();
            var microbloggingUrl = lconn.core.config.services.microblogging.secureUrl;
            var relativeUrl = "";
            var fullProxy = this.xhrHandler.endpoint.proxy.proxyUrl + "/" + this.xhrHandler.endpoint.proxyPath;
            var index = microbloggingUrl.indexOf(fullProxy);
            if(index !== -1){
                relativeUrl = microbloggingUrl.slice(index + fullProxy.length);
            }
            else{
                relativeUrl = new Url(microbloggingUrl).getPath();
            }
            var serviceUrl = relativeUrl + "/" + this.xhrHandler.endpoint.authType + "/rest/people/@me/@self";
            
            this.xhrHandler.xhrGet({
                serviceUrl: serviceUrl, //TODO Change these when opensocial oauth and/or endpoints become available
                handleAs: "json",
                load: function(resp) {
                    var user = resp && resp.entry;
                    if (user && user.id && user.displayName) {
                        
                        var connId = user.id;
                        if (connId && connId.indexOf('urn:lsid:lconn.ibm.com:') != -1) {
                            var endPrefix = connId.lastIndexOf(':');
                            if (endPrefix != -1) {
                                connId = connId.substr(endPrefix+1);
                            }
                        }

                        var ui = {
                                id: connId,
                                osId: user.id,
                                displayName: user.displayName
                        };
                        promise.resolve(ui);
                    }
                },
                error: function() {
                    promise.reject();
                }
            });
            
            return promise;
        },
        
        /*
         * Require only the extensions that we need.
         * 
         * @method requireExtensions
         * @param {Array} extensionsArray Array containing a list of the modules to require.
         */
        requireExtensions: function(extensionsArray){
            var i;
            for(i = 0; i < extensionsArray.length; i++){
                var ext = extensionsArray[i];
                dojo.require(ext);
            }
        },
        
        getBoardIdFromUrl: function(url){
            if(url.indexOf("urn:lsid:")===-1) // is it a community?
                return "@me";
            var stream = "/activitystreams/";
            var index = url.indexOf(stream)+stream.length;
            var idString = url.slice(index);
            index = 0;
            var result = idString.substring(index, idString.indexOf("/"));
            
            return result;
        },
        
        getBoardIdFromAppId: function(appId){
            if(appId.indexOf("urn:lsid:" === -1)){//is it a community?
                return "@me";
            }
            return appId;
        },
        
        /*
         * Build the config from a full config object. Adds missing information if
         * needed, such as user info.
         * 
         * @method buildSbtConfigFull
         * @param {Object} cfg An ActivityStream config object. If this does not contain user info then user info will be added based on the currently authenticated user.
         * @returns {lconn.core.util.LCDeferred} A promise which, when resolved, will contain a completed ActivityStream config object.
         */
        buildSbtConfigFull: function(cfg){
            if(cfg.eeManager)
                delete cfg.eeManager;
            var cfgPromise = new lconn.core.util.LCDeferred();
            if (!cfg.userInfo || !cfg.userInfo.id || !cfg.userInfo.displayName) {
                cfg.boardId = this.getBoardIdFromAppId(cfg.defaultUrlTemplateValues.appId);
                this.getUserInfo().then(
                    function(ui) {
                        if (ui && ui.id && ui.displayName)
                            cfg.userInfo = ui;
                        cfgPromise.resolve(cfg);
                    },
                    function(error) {
                        cfgPromise.reject(error);
                    });
            } else {
                cfgPromise.resolve(cfg);
            }
            if(cfg.extensions)
                this.requireExtensions(cfg.extensions);
            return cfgPromise;
        },
        
        /*
         * Builds a config object from a feedUrl and an optional extensions object.
         * The extensions object contains simple true or false values as a shortcut for four common extensions, 
         * e.g. 
         * extensions: {
         *   commenting: true,
         *   saving: true,
         *   refreshButton: true,
         *   deleteButton: true
         * }
         * These will be required if needed.
         * @method buildSbtConfigFromFeed
         * @param {Object} args This should contain a feedUrl, and an optional extensions object.
         *     @param {String} args.feedUrl The url of the ActivityStream feed.
         *     @params {Object} [args.extensions] A simple list of extensions to load.
         *         @params {Boolean} [args.extensions.commenting] If true load the commenting extension.
         *         @params {Boolean} [args.extensions.saving] If true load the saving extension.
         *         @params {Boolean} [args.extensions.refreshButton] If true load the refresh button extension.
         *         @params {Boolean} [args.extensions.DeleteButton] If true load the delete button extension.
         *     
         * 
         * @returns {lconn.core.util.LCDeferred} A promise which, when resolved, will contain a completed ActivityStream config object.
         */
        buildSbtConfigFromFeed: function(args){
            var cfgPromise = new lconn.core.util.LCDeferred();
            
            var cfg = {
                defaultUrlTemplate : args.feedUrl,
                defaultUrlTemplateValues : {},
                views : {
                    main : {}
                },
                eeManager : "com.ibm.social.as.ee.EEManager",
                extensions : [
                    "com.ibm.social.as.lconn.extension.GadgetPreloaderExtension"
                ],
                boardId: this.getBoardIdFromUrl(args.feedUrl)
            };
            
            if(args.extensions){
                if(args.extensions.commenting)
                    cfg.extensions.push("com.ibm.social.as.extension.CommentExtension");
                if(args.extensions.saving)
                    cfg.extensions.push("lconn.homepage.as.extension.SavedActionExtension");
                if(args.extensions.refreshButton)
                    cfg.extensions.push("com.ibm.social.as.gadget.refresh.RefreshButtonExtension");
                if(args.extensions.deleteButton)
                    cfg.extensions.push("com.ibm.social.as.lconn.extension.MicroblogDeletionExtension");
            }
            if(cfg.eeManager)
                delete cfg.eeManager;
            
            if(args.feedUrl.indexOf("anonymous") === -1){
                this.getUserInfo().then(
                    function(ui) {
                        if (ui && ui.id && ui.displayName)
                            cfg.userInfo = ui;
                        cfgPromise.resolve(cfg);
                    },
                    function(error) {
                        cfgPromise.reject(error);
                });
            } else{
                this.requireExtensions(cfg.extensions);
                cfgPromise.resolve(cfg);
            }
            this.requireExtensions(cfg.extensions);
            return cfgPromise;        
        },
        
        /*
         * If a feed is present build from feed, otherwise build from full config
         * object, augmenting if needed.
         * 
         * @method buildSbtConfig
         * @param args If this contains a feedUrl then builds a config from it. Otherwise it should have an args.config object.
         * @returns {lconn.core.util.LCDeferred} A promise which, when resolved, will contain a completed ActivityStream config object.
         */
        buildSbtConfig: function(args){
            if(args.feedUrl)
                return this.buildSbtConfigFromFeed(args);
            else
                return this.buildSbtConfigFull(args.config);
        }
    });
    
    return _SbtAsConfigUtil;
});

},
'sbt/connections/controls/sharebox/_InputForm':function(){
define(["sbt/declare", "sbt/json", "sbt/connections/controls/_ConnectionsWidget"],function(declare, json, _ConnectionsWidget){
    /**
     * InputForm
     */
    var _InputForm = declare([_ConnectionsWidget],
    {    
        /**
         * The connections InputForm.
         * 
         * @property inputForm
         * @type Object
         */
        inputForm: null,
        
        /**
         * Set up the connections InputForm.
         * 
         * @method constructor
         * @param {Object} args 
         *     @param {String} args.shareBoxNode Should contain the id of the html element to add the InputForm to.
         */
        constructor: function(args){
            var url = "/${connections}/opensocial/basic/rest/ublog/@config/settings";
            var self = this;
            
            var xhrArgs = {
                serviceUrl: url, //TODO Change these when opensocial oauth and/or endpoints become available
                handleAs: "json",
                load: function(data) {
                    var settings = {
                        maxNumberChars: 1000,
                        boardId: args.boardId || "@me",
                        postType: lconn.news.microblogging.sharebox.Context.SU_CONTEXT
                    };
                    var connectionsSettings = data && data.entry;
                    if(connectionsSettings){
                        settings.maxNumberChars = connectionsSettings["com.ibm.connections.ublog.microblogEntryMaxChars"];
                        self.inputForm = new lconn.news.microblogging.sharebox.InputForm({
                            params : settings,
                            "xhrHandler" : self.xhrHandler,
                            "UBLOG_RELATIVE_PATH" : "/basic/rest/ublog/",
                            isASGadget : false
                        }, dojo.byId(args.shareBoxNode));
                        if(self.inputForm.attachActionButtonNode){
                            dojo.empty(self.inputForm.attachActionButtonNode);
                        }
                        
                        self.modifyPostFunction();
                    }
                },
                error: function() {
                    console.log("Failed to find InputForm settings at " + url);
                }
            };
            
            this.xhrHandler.xhrGet(xhrArgs);
        },
        
        /**
         * A hack needed to overcome a hardcoded dojo.xhr request in the connections InputForm. Overwrites the offending function.
         * 
         * @method modifyPostFunction
         */
        modifyPostFunction: function(){
            this.inputForm.postMicroblog = function (microblogMessage, fileAttachment) {
                this._setPostButtonLabel(this._resourceBundle.POSTING);
                this._setSubmitState(true);
                var requestObj = this._buildRequestObj(microblogMessage, fileAttachment);
                var postUrl = this._getPostUrlForContext();
                postUrl = postUrl.slice(postUrl.lastIndexOf("/connections"));
                this.xhrHandler.xhrPost({
                    serviceUrl: postUrl,
                    postData: json.stringify(requestObj),
                    handleAs: "json",
                    load: dojo.hitch(this, function (data) {
                        if (this.isGlobalSharebox){
                            dojo.publish(this.AS_UPDATE, [data.entry.id]);
                        } else {
                            dojo.publish(this.TOPIC_POST_MESSAGE, [data]);
                        }           
                        this._setInitialState();
                        this._setSubmitState(false);
                        this._displaySuccessMessage();
                        if (this.isGlobalSharebox) {
                            this._closeSharebox();
                            this.setShareMode(lconn.news.microblogging.sharebox.constants.SHAREWITHEVERYONE);
                            dojo.publish(lconn.news.microblogging.sharebox.events.STATUSSHAREBOX_CLOSING);
                        } else {
                            this.mbSuccessClose.focus();
                            this.textBoxControl.collapseTextBox();
                        }

                        this._setPostButtonLabel(this._resourceBundle.POST);

                    }),
                    error: dojo.hitch(this, function (data) {
                        this._handlePostError(data);
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                });
            };
        }
        
    });

    return _InputForm;
});

},
'sbt/connections/controls/bookmarks/BookmarkGrid':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../../declare",
        "../../../controls/grid/Grid",
        "../../../store/parameter",
        "../../../connections/BookmarkConstants",
        "../../../connections/CommunityConstants",
        "./BookmarkGridRenderer"], 
    function(declare,Grid,parameter,consts,communityConstants,BookmarkGridRenderer){
	
	/**
	 * Sorting values
	 */
	var sortVals = {
			date: "created",
            popularity: "popularity"
	};
	
	/**
	 * Sorting and paging parameters
	 */
	var ParamSchema = {	
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortBy",sortVals),
		sortOrder: parameter.sortOrder("sortOrder")			
	};
	
	/**
	 * @class BookmarkGrid
     * @namespace sbt.connections.controls.bookmarks
     * @module sbt.connections.controls.bookmarks.BookmarkGrid
	 */
	var BookmarkGrid = declare(Grid,{
		
		 options : {
	            "any" : {
	                storeArgs : {
	                    url : consts.AtomBookmarksAll,
	                    attributes : consts.BookmarkXPath,
	                    feedXPath : consts.BookmarkFeedXPath,
	                    paramSchema : ParamSchema
	                },
	                rendererArgs : null
	            },
	            "community": {
	                storeArgs : {
                        url : communityConstants.AtomCommunityBookmarks,
                        attributes : consts.BookmarkXPath,
                        feedXPath : consts.BookmarkFeedXPath,
                        paramSchema : ParamSchema
                    },
                    rendererArgs : null
	            }
	     },
	        
	     /**
	      * Default grid option.
	      */
	     defaultOption: "any", 
	     
	     /**
	      * The grid constructor function
	      * @method constructor
	      * @param args
	      */
	     constructor: function(args){
	    	 
	    	var nls = this.renderer.nls;
            this._sortInfo = {
                date: { 
                    title: nls.date, 
                    sortMethod: "sortByDate",
                    sortParameter: "date"
                },
                popularity: {
                    title: nls.popularity, 
                    sortMethod: "sortByPopularity",
                    sortParameter: "popularity"
                }
            },
            this._activeSortAnchor = this._sortInfo.date;
            this._activeSortIsDesc = true;
	     }, 
	     
	     /**
	      * Used to add parameters to the URL
	      * @method buildUrl
	      * @param url the url to add parameters to
	      * @param args
	      * @param endpoint An endpoint which may contain custom service mappings.
	      * @returns the url with parameters 
	      */
	     buildUrl: function(url, args, endpoint) {
	    	 var urlParams;
	    	 
	    	 if(this.type == "private"){
	    		 urlParams = { access: "private"};
	    	 }else if(this.type == "public") {
	    		 urlParams = {access: "public"};
	    	 }else if(this.type == "community"){
	    	     urlParams = {communityUuid: this.communityUuid};
	    	 }else{
	    		 urlParams = {access: "any"};
	    	 }

	          return this.constructUrl(url, urlParams, {}, endpoint);
	        },
	     
        /**
         * Creates a Renderer for this grid.
         * @param args
         * @returns {BookmarkGridRenderer}
         */
	     createDefaultRenderer: function(args){
	    	 return new BookmarkGridRenderer(args);
	     },
	     
	     /**
         * Gets sorting information, such as
         * if the results are ascending or descending, and the sort anchors
         * @method getSortInfo
         * @returns An object containing sorting information
         */
        getSortInfo: function() {
            return {
                active: {
                    anchor: this._activeSortAnchor,
                    isDesc: this._activeSortIsDesc
                },
                list: [this._sortInfo.date, this._sortInfo.popularity]
            };
        },
	     
	    /**
         * Filter the bookmarks by specified tag
         * @method filterByTag
         * @param el The Grid Dom node
         * @param data The Grid's data
         * @param ev The Event
         */
        filterByTag: function(el, data, ev){
        	this._stopEvent(ev);
            
        	var options = {
            	tag: el.text
            };

        	this._filter(options,data);
        },
        
        /**
         * Sorts the grid by date of creation.
         * @method sortByDate
         * @param el The Grid dom Element
         * @param data the Grid's data
         * @param ev the event
         */
        sortByDate: function(el, data, ev){
        	this._sort("date", true, el, data, ev);
        },
        
        /**
         * Sort the Grid by popularity
         * @method sortByPopularity
         * @param el The grid element
         * @param data the Grids data
         * @param ev The event
         */
        sortByPopularity: function(el, data, ev){
        	this._sort("popularity", true, el, data, ev);
        },
        
        /**
         * Event Handler for onClick events
         * @method handleClick
         * @param el the grid element
         * @param data the grids data
         * @param ev the event
         */
        handleClick: function(el, data, ev){
        	
        	this._stopEvent(ev);
        	this.bookmarkAction.execute(data);
        },
        
        /**
         * Default Action for handling click events and returning tooltip text.
         */
        bookmarkAction: {
        	
        	/**
        	 * Returns a string to be used in a tooltip or title. 
        	 * @method getTooltip 
        	 * @param item
        	 * @returns A String to be used in a tooltip. 
        	 */
        	getTooltip: function(item){
        		return item.getValue("title");
        	},
        	
        	/**
        	 * The code is executed from an onClick event
        	 * @method execute
        	 * @param data the Data associated with the grid
        	 */
        	execute: function(data){
        		var url = data.getValue("url");
        		window.location.assign(url);
        	}
        }
	
	});
	
	
	return BookmarkGrid;
	
});

},
'sbt/connections/BookmarkConstants':function(){
/*
 * � Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Definition of constants for BookmarkService.
 * 
 * @module sbt.connections.BookmarkConstants
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin({
    	
    	BookmarkFeedXPath : conn.ConnectionsFeedXPath,
    	
        /**
         * XPath expressions
         * 
         * @property BookmarkXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        BookmarkXPath : {
        	entry : "/a:entry",
        	uid : "a:id",
            BookmarkUuid: "a:id",
            title: "a:title",
            privateFlag: "a:category[@term='private' and @scheme='http://www.ibm.com/xmlns/prod/sn/flags']",
            categoryType: "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term",
            content: "a:content",
            updated: "a:updated",
            published: "a:published",
            link: "a:link[not(@rel)]/@href",
            linkEdit: "a:link[@rel='edit']/@href",
            linkSame: "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/same']/@href",
            url: "a:link[1]/@href",
            authorId:"a:author/snx:userid",
            authorName: "a:author/a:name",
            authorEmail: "a:author/a:email",
            authorUri: "a:author/a:uri",
            tags : "a:category[not(@scheme)]/@term",
            clickcount: "snx:clickcount"
        },

        /**
         * A feed of all bookmarks.
         *  
         * @property AtomBookmarkssAll
         * @type String
         * @for sbt.connections.BoomarkService
         */
        AtomBookmarksAll : "/${dogear}/atom",

        /**
         * A feed of popular bookmarks.
         *  
         * @property AtomBookmarkssAll
         * @type String
         * @for sbt.connections.BoomarkService
         */
        AtomBookmarksPopular : "/${dogear}/atom/popular",

        /**
         * A feed of bookmarks that others notified me about.
         *  
         * @property AtomBookmarkssAll
         * @type String
         * @for sbt.connections.BoomarkService
         */
        AtomBookmarksMyNotifications : "/${dogear}/atom/mynotifications",

        /**
         * A feed of bookmarks about which I notified others.
         *  
         * @property AtomBookmarkssAll
         * @type String
         * @for sbt.connections.BoomarkService
         */
        AtomBookmarksINotifiedMySentNotifications : "/${dogear}/atom/mysentnotifications",

        /**
         * A feed of all bookmark tags.
         *  
         * @property AtomBookmarksTags
         * @type String
         * @for sbt.connections.BoomarkService
         */
        AtomBookmarksTags : "/${dogear}/tags",

        /**
         * create delete or update a bookmark.
         *  
         * @property AtomBookmarkCreateUpdateDelete
         * @type String
         * @for sbt.connections.BoomarkService
         */
        AtomBookmarkCreateUpdateDelete : "/${dogear}/api/app"

    }, conn);
});

},
'sbt/connections/CommunityConstants':function(){
/*
 * � Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Definition of constants for CommunityService.
 * 
 * @module sbt.connections.CommunityConstants
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin({
        
        /**
         * Public community
         * 
         * @property Public
         * @type String
         * @for sbt.connections.Community
         */
        Public : "public",

        /**
         * Moderated community
         * 
         * @property Moderated
         * @type String
         * @for sbt.connections.Community
         */
        Moderated : "publicInviteOnly",

        /**
         * Restricted community
         * 
         * @property Restricted
         * @type String
         * @for sbt.connections.Community
         */
        Restricted : "private",

        /**
         * Community owner
         * 
         * @property Owner
         * @type String
         * @for sbt.connections.Member
         */
        Owner : "owner",
        
        /**
         * Community member
         * 
         * @property Member
         * @type String
         * @for sbt.connections.Member
         */
        Member : "member",
        
        /**
         * XPath expressions used when parsing a Connections Communities ATOM feed
         * 
         * @property CommunityFeedXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        CommunityFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * XPath expressions to be used when reading a Community Entry
         * 
         * @property CommunityXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        CommunityXPath : {
            // used by getEntityData
            entry : "/a:entry",
            // used by getEntityId
            uid : "a:id",
            // used by getters
            communityUuid : "a:id",
            communityTheme : "snx:communityTheme",
            title : "a:title",
            summary : "a:summary[@type='text']",
            communityUrl : "a:link[@rel='alternate']/@href",
            communityAtomUrl : "a:link[@rel='self']/@href",
            logoUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href",
            tags : "a:category[not(@scheme)]/@term",
            content : "a:content[@type='html']",
            memberCount : "snx:membercount",
            communityType : "snx:communityType",
            published : "a:published",
            updated : "a:updated",
            authorUserid : "a:author/snx:userid",
            authorName : "a:author/a:name",
            authorEmail : "a:author/a:email",
            contributorUserid : "a:contributor/snx:userid",
            contributorName : "a:contributor/a:name",
            contributorEmail : "a:contributor/a:email"
        },
        
        /**
         * XPath expressions to be used when reading a Community Member Entry
         * 
         * @property MemberXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        MemberXPath : {
            // used by getEntityData
            entry : "/a:entry",
            // used by getEntityId
            uid : "a:contributor/snx:userid",
            // used by getters
            userid : "a:contributor/snx:userid",
            name : "a:contributor/a:name",
            email : "a:contributor/a:email",
            role : "snx:role"
        },

        /**
         * XPath expressions to be used when reading a Community Invite Entry
         * 
         * @property InviteXPath
         * @type Object
         * @for sbt.connections.CommunityService
         */
        InviteXPath : {
            // used by getEntityData
            entry : "/a:entry",
            // used by getEntityId
            uid : "a:id",
            // used by getters
            inviteUuid : "a:id",
            title : "a:title",
            content : "a:content[@type='text']",
            updated : "a:updated",
            authorUserid : "a:author/snx:userid",
            authorName : "a:author/a:name",
            contributorUserid : "a:contributor/snx:userid",
            contributorName : "a:contributor/a:name",
            contributorEmail : "a:contributor/a:email",
            contributorUserState : "a:contributor/snx:userState",
            communityUuid : "snx:communityUuid",
            communityUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/community']/@href",
            editUrl : "a:link[@rel='edit']/@href"
        },
        
        EventXPath : {
            // used by getEntityData
            entry : "/a:entry",
            // used by getEntityId
            uid : "a:id",
            // used by getters
            eventUuid : "snx:eventUuid",
            eventInstUuid : "snx:eventInstUuid",
            title : "a:title",
            eventAtomUrl : "a:link[@rel='self']/@href",
            content : "a:content[@type='html']",
            location : "snx:location",
            authorUserid : "a:author/snx:userid",
            authorName : "a:author/a:name",
            authorEmail : "a:author/a:email",
            authorState : "a:author/snx:userState",
            updated : "a:updated",
            communityLink : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/container']/@href",
            eventAtomInstances : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/calendar/event/instances']/@href",
            eventAtomAttendees : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/calendar/event/attend']/@href",
            eventAtomFollowers : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/calendar/event/follow']/@href",
            frequency : "snx:recurrence/@frequency",
            interval : "snx:recurrence/@interval",
            until : "snx:recurrence/snx:until",
            allDay : "snx:recurrence/snx:allday",
            startDate : "snx:recurrence/snx:startDate",
            endDate : "snx:recurrence/snx:endDate",
            byDay : "snx:recurrence/snx:byDay"
        },

        /**
         * asc  Specifies whether the results should be displayed in ascending order. Options are true or false.
         * email   Internet email address. Search for communities of which the user you specify by email address is a member.
         * ps  Page size. Specify the number of entries to return per page.
         * search  Well-formed full text search query. Performs a text search on community titles and descriptions.
         * since   Includes in the resulting feed all communities updated after a specified date. Specify the date using a date-time value that conforms to RFC3339. Use an upper case "T" to separate the date and time, and an uppercase "Z" in the absence of a numeric time zone offset. For example: 2009-01-04T20:32:31.171Z.
         * sortField   Order in which to sort the results. Options are:
         *                                          lastmod � Sorts the results by last modified date.
         *                                          name � Sorts the results by entry name.
         *                                          count � Sorts the results by relevance.
         * tag Returns communities with the specified tag. Search by one tag at a time.
         * userid  Unique ID that represents a specific person.
         */
        
        /**
         * A feed of all public communities.
         *  
         * Get the All Communities feed to see a list of all public communities to which the authenticated user has access or pass in parameters to search for communities that match a specific criteria.
         * 
         * Supports: asc, email, ps, search, since, sortField, tag, userid
         * 
         * @property AtomCommunitiesAll
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunitiesAll : "/${communities}/service/atom/communities/all",

        /**
         * A feed of communities of which the authenticated user is a member.
         * 
         * Get the My Communities feed to see a list of the communities to which the authenticated user is a member or pass in parameters to search for a subset of those communities that match a specific criteria.
         * 
         * Supports: asc, email, ps, search, since, sortField, tag, userid
         * 
         * @property AtomCommunitiesMy
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunitiesMy : "/${communities}/service/atom/communities/my",
        
        /**
         * A feed of invitations.
         * 
         * Get a list of the outstanding community invitations of the currently authenticated user or provide parameters to search for a subset of those invitations.
         * 
         * Supports: asc, ps, search, since, sortField
         * 
         * @property AtomCommunityInvitesMy
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityInvitesMy : "/${communities}/service/atom/community/invites/my",
        
        /**
         * URL to delete/create Community Invites
         * 
         * @property AtomCommunityInvites
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityInvites : "${communities}/service/atom/community/invites",
        
        /**
         * A feed of subcommunities.
         * 
         * Get a list of subcommunities associated with a community.
         * 
         * Supports: asc, page, ps, since, sortBy, sortOrder, sortField
         * 
         * @property AtomCommunitySubCommunities
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunitySubCommunities : "${communities}/service/atom/community/subcommunities",
            
        /**
         * A feed of members.
         * 
         * Retrieve the members feed to view a list of the members who belong to a given community.
         * 
         * Supports: asc, email, page, ps, role, since, sortField, userid
         * 
         * @property AtomCommunityMembers
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityMembers : "${communities}/service/atom/community/members",
        
        /**
         * A community entry.
         * 
         * @property AtomCommunityInstance
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityInstance : "${communities}/service/atom/community/instance",
        
        /**
         * Get a feed that includes the topics in a specific community forum.
         * 
         * @property AtomCommunityForumTopics
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityForumTopics : "/${communities}/service/atom/community/forum/topics",
        
        /**
         * Get a feed of a Community's bookmarks. Requires a url parameter of the form communityUuid=xxx
         * @property AtomCommunityBookmarks
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityBookmarks : "/${communities}/service/atom/community/bookmarks",
        
        /**
         * Get a feed of a Community's events.
         * 
         * Required url parameters: 
         *   calendarUuid - The uuid of the community to get events from.
         *   
         *   startDate and/or endDate. At least one of these must be present. Format is any date-time that conforms to rfc3339. 
         *   startDate - Include events that end after this date.
         *   endDate - Include events that end before this date.
         *   
         * Optional Url parameters
         *   page - Page number, specifies the page to be returned. Default value is page 1.
         *   ps - Page size. Number of entries to return per page. Defaule value is 10, max is 100.
         *   tags - Tag filter, only return events with these tags. Multiple tags are separated using +, e.g. tag1+tag2
         *   
         * @property AtomCommunityEvents
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityEvents : "/${communities}/calendar/atom/calendar/event",
        
        /**
         * Get full atom event.
         * 
         * Required url parameters: 
         *   eventInstUuid - The uuid of the event, gotten from the AtomCommunityEvents feed.
         *   
         * @property AtomCommunityEvent
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityEvent : "/${communities}/calendar/atom/calendar/event",
        
        /**
         * Obtain a full representation of the invitations as an Atom entry document.
         * 
         * @property AtomCommunityInvites
         * @type String
         * @for sbt.connections.CommunityService
         */
        AtomCommunityInvites : "/${communities}/service/atom/community/invites"
        
    }, conn);
});
},
'sbt/connections/controls/bookmarks/BookmarkGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

define(["../../../declare", "../../../i18n", 
        "../../../connections/controls/ConnectionsGridRenderer",
        "../../../text!../../../connections/controls/bookmarks/templates/BookmarkRow.html",
        "../../../text!../../../connections/controls/bookmarks/templates/TagAnchor.html",
        "../../../i18n!../../../connections/controls/bookmarks/nls/BookmarkGridRenderer",
        "../../../lang"], 
        function(declare, i18n,  ConnectionsGridRenderer, BookmarkRow,TagAnchor, nls, lang ) {

		/**
		* @class BookmarkGridRenderer
	    * @namespace sbt.connections.controls.bookmarks
	    * @module sbt.connections.controls.bookmarks.BookmarkGridRenderer
	    * */
		var BookmarkGridRenderer = declare(ConnectionsGridRenderer,{
			
			_nls: nls,
			
			/**
			 * The Constructor function
			 * @method constructor
			 * @param args
			 */
			constructor: function(args){
				this.template = BookmarkRow;
				this.tagAnchorTemplate = TagAnchor;
			},
			
			 /**
	          * Displays a tooltip by calling the getTooltip function in the bookmarkAction.
	          * @method tooltip
	          * @param grid The Grid element
	          * @param item the element to display the tooltip
	          * @param i the number of the current row
	          * @param items all of the items in the grid row
	          * @returns A String used as a tooltip
	          */
	         tooltip: function(grid, item, i, items) {
	                return grid.bookmarkAction.getTooltip(item);

	         },
			/**
	          * Substitutes tag labels(Strings) from the nls file into the tag
	          * Anchor template which creates a tag links, and displays these tags on the page 
	          * @param grid The Grid Element
	          * @param item An Object containing all of the data for the current row
	          * @param i The number of the current row
	          * @param items  an object array containing the data for all of the grid rows
	          * @returns {String}
	          */
	         tagsAnchors: function(grid, item, i, items) {
	             var tags = item.getValue("tags");
	             if (tags == undefined) {
	                 return "";
	             } else {
	                 var tagsStr = "";
	                 if (lang.isArray(tags)) {
	                     for (var i=0; i<tags.length; i++) {
	                         tagsStr += this._substitute(this.tagAnchorTemplate, { tagName : tags[i] });
	                         if (i+1 < tags.length) {
	                             tagsStr += ", ";
	                         }
	                     }
	                 } else if (lang.isString(tags)) {
	                	 tagsStr = this._substitute(this.tagAnchorTemplate, { tagName : tags });
	                 }
	                 return tagsStr;
	             }
	         },
	         
	         getUserProfileHref: function(grid,item,i,items){
	        	 return this.getProfileUrl(grid,item.getValue("authorId"));
	         }
			
		});
		
		
	
		return BookmarkGridRenderer;
});

},
'sbt/connections/controls/bootstrap/CommunityRendererMixin':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../../lang", "../../../controls/grid/bootstrap/GridRendererMixin",
         "../../../text!./templates/CommunityRow.html",
         "../../../text!./templates/TagAnchor.html" ], 
        function(lang, GridRendererMixin, CommunityRow, TagAnchor) {

    /**
     * @module sbt.controls.grid.bootstrap.CommunityRendererMixin
     */
    return lang.mixin(GridRendererMixin, { template : CommunityRow, tagAnchorTemplate: TagAnchor });
    
});
},
'sbt/controls/grid/bootstrap/GridRendererMixin':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../../text!./templates/GridPager.html", 
        "../../../text!./templates/GridSorter.html",
        "../../../text!./templates/SortAnchor.html" ], 
        function(GridPager, GridSorter, SortAnchor) {

    /**
     * @module sbt.controls.grid.bootstrap.GridRendererMixin
     */
    return {
        tableClass : "table  table-striped table-bordered",
        emptyClass : "label label-info",
        errorClass : "label label-warning",
        loadingClass : "label label-info",
        loadingImgClass : "icon-refresh",
        firstClass : "",
        defaultSortClass : "icon-arrow-down",
        ascendingSortClass : "icon-arrow-up",
        descendingSortClass : "icon-arrow-down",
        pagerTemplate : GridPager,
        sortTemplate : GridSorter,
        sortAnchor : SortAnchor
    };
    
});
},
'sbt/connections/controls/bootstrap/FileRendererMixin':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../../lang", "../../../controls/grid/bootstrap/GridRendererMixin",
         "../../../text!./templates/FileRow.html" ], 
        function(lang, GridRendererMixin, FileRow) {

    /**
     * @module sbt.controls.grid.bootstrap.FileRendererMixin
     */
    return lang.mixin(GridRendererMixin, { template : FileRow });
    
});
},
'sbt/connections/controls/bootstrap/ProfileRendererMixin':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../../lang", "../../../controls/grid/bootstrap/GridRendererMixin",
         "../../../text!./templates/ProfileRow.html" ], 
        function(lang, GridRendererMixin, ProfileRow) {

    /**
     * @module sbt.controls.grid.bootstrap.ProfileRendererMixin
     */
    return lang.mixin(GridRendererMixin, { template : ProfileRow });
    
});
},
'sbt/connections/controls/communities/CommunityAction':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.connections.controls.communities.CommunityAction
 */
define([ "../../../declare", "../../../controls/grid/GridAction", "../../../stringUtil" ], 
        function(declare, GridAction, stringUtil) {

    /**
     * 
     * @class CommunityAction
     * @namespace sbt.connections.controls.communities
     */
    var CommunityAction = declare(GridAction, {
        
        nls: {
            tooltip: "{title}"
        },
       
        /**
         * CommunityGridAction Constructor
         * @method  constructor
         */
        constructor: function() {
        },
        
        /**
         * @method getTooltip
         * @param item The element that the tooltip will display for
         * @returns A String to be displayed in an elements tooltip 
         */
        getTooltip: function(item) {
           return stringUtil.replace(this.nls.tooltip, { title : item.getValue("title") });
        },
        
        /**
         * When an event is fired, this is the action that will get called
         * from the hanleClick function
         * @method  execute
         * @param item  The element that fired the event and called this function 
         * @param opts
         * @param event  the Event
         */
        execute: function(item, opts, event) {
           window.open(item.getValue("communityUrl"));
        }

    });

    return CommunityAction;
});
},
'sbt/connections/controls/communities/CommunityGrid':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.connections.controls.communities.CommunityGrid
 */
define([ "../../../declare", 
         "../../../controls/grid/Grid", 
         "./CommunityGridRenderer", 
         "./CommunityAction", 
         "../../../connections/controls/vcard/SemanticTagService",
         "../../../config",
         "../../../connections/CommunityConstants",
         "../../../store/parameter"], 
        function(declare, Grid, CommunityGridRenderer, CommunityAction, SemanticTagService, sbt, consts, parameter) {
	
	var sortVals = {
			date: "modified",
            popularity: "count",
            name: "title"
	};
	
	var ParamSchema = {	
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortField",sortVals),
		sortOrder: parameter.booleanSortOrder("asc")			
	};
	
    /**
     * @class  CommunityGrid
     * @namespace  sbt.connections.controls.communities
     */
    var CommunityGrid = declare(Grid, {
        
        contextRootMap: {
            communities: "communities"
        },
    	
    	/**
    	 * Options are for which type of community grid is to be created
    	 * public will show public communities and my will show communities
    	 * the specified user is a member or owner of. 
    	 */
        options : {
            "public" : {
                storeArgs : {
                    url : consts.AtomCommunitiesAll,
                    attributes : consts.CommunityXPath,
                    feedXPath : consts.CommunityFeedXPath,
                    paramSchema : ParamSchema
                },
                rendererArgs : null
            },
            "my" : {
                storeArgs : {
                    url : consts.AtomCommunitiesMy,
                    attributes : consts.CommunityXPath,
                    feedXPath : consts.CommunityFeedXPath,
                    paramSchema : ParamSchema
                },
                rendererArgs : null
            },
            "invited" : {
                storeArgs : {
                    url : consts.AtomCommunityInvitesMy,
                    attributes : consts.CommunityXPath,
                    feedXPath : consts.CommunityFeedXPath,
                    paramSchema : ParamSchema
                },
                rendererArgs : null
            }
        },
        
        /**
         * set the grids action to be an instance of CommunityAction
         * This means an event will be provided for when the user moves the mouse over the 
         * community name or clicks on  a community.
         */
        communityAction: new CommunityAction(),
        
        /**
         * Default grid option is to display a list of public communities.
         */
        defaultOption: "public",
            
        /**
         * Constructor function
         * @method constructor
         */
        constructor: function(args) {
            var nls = this.renderer.nls;
            
            /**
             * Set the sorting information
             */
            this._sortInfo = {
                date: { 
                    title: nls.date, 
                    sortMethod: "sortByDate",
                    sortParameter: "date"
                },
                popularity: {
                    title: nls.popularity, 
                    sortMethod: "sortByPopularity",
                    sortParameter: "popularity"
                },
                name: { 
                    title: nls.name, 
                    sortMethod: "sortByName",
                    sortParameter: "name"
                }
            };
            this._activeSortAnchor = this._sortInfo.date;
            this._activeSortIsDesc = true;
        },
        
        /**
         * Create the CommunityGrid Renderer
         * @method createDefaultRenderer
         * @param args Arguments that can be passed to the renderer
         * @returns {CommunityGridRenderer}
         */
        createDefaultRenderer : function(args) {
            return new CommunityGridRenderer(args);
        },
        
        /**
         * Function is called after the grid is created,
         * Here we call the superclass postCreate and then load the 
         * Semantic tag service, which will handle business card functionality
         * @method postCreate
         */
        postCreate: function() {        	
        	this.inherited(arguments);
        	
        	SemanticTagService.loadSemanticTagService();
        },
        
        /**
         * Event handler for onClick Events
         * Stops the default onClick event and calls the CommunityAction execute function
         * @method handleClick
         * @param el The element that fired the event
         * @param data the data associated with the row
         * @param ev the event
         */
        handleClick: function(el, data, ev) {
            if (this.communityAction) {
            	this._stopEvent(ev);
                
                this.communityAction.execute(data, { grid : this.grid }, ev);
            }
        },
        
        /**
         * Filter the community by specified tag
         * @param el
         * @param data
         * @param ev
         */
        filterByTag: function(el, data, ev){
        	this._stopEvent(ev);
            
        	var options = {
            	tag: el.text
            };

        	this._filter(options,data);
        },
        
        /**
         * Gets sorting information, such as
         * if the results are ascending or descending, and the sort anchors
         * @method getSortInfo
         * @returns An object containing sorting information
         */
        getSortInfo: function() {
            return {
                active: {
                    anchor: this._activeSortAnchor,
                    isDesc: this._activeSortIsDesc
                },
                list: [this._sortInfo.date, this._sortInfo.popularity, this._sortInfo.name]
            };
        },
                
        /**
         * Sort the grid rows by last modified date
         * @method sortByLastModified
         * @param el The element that was clicked, typically a "sort by" button
         * @param data the data associated with the element
         * @param ev the event
         */
        sortByDate: function(el, data, ev) {
            this._sort("date", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by popularity
         * @method sortByPopularity
         * @param el The element that was clicked, typically a "sort by" button
         * @param data the data associated with the element
         * @param ev the event
         */
        sortByPopularity: function(el, data, ev) {
            this._sort("popularity", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by title
         * @method sortByTitle
         * @param el The element that was clicked, typically a "sort by" button
         * @param data the data associated with the element
         * @param ev the event
         */
        sortByName: function(el, data, ev) {
            this._sort("name", false, el, data, ev);
        }
        
    });

    return CommunityGrid;
});

},
'sbt/connections/controls/communities/CommunityGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.connections.controls.communities.CommunityGridRenderer
 */
define(["../../../declare", "../../../stringUtil", "../../../i18n", "../../../lang",
        "../../../connections/controls/ConnectionsGridRenderer",
        "../../../text!../../../connections/controls/communities/templates/CommunityRow.html",
        "../../../text!../../../connections/controls/communities/templates/TagAnchor.html",
        "../../../i18n!../../../connections/controls/communities/nls/CommunityGridRenderer"], 
        function(declare, stringUtil, i18n, lang, ConnectionsGridRenderer, CommunityRow, TagAnchor, nls) {

    /**
     * @class CommunityGridRenderer
     * @namespace sbt.connections.controls.communities
     */
    var CommunityGridRenderer = declare(ConnectionsGridRenderer, {
    	
    	 /**
    	  * Strings used in the grid
    	  */
         _nls: nls,
         
         /**
          * The HTML template to be used for this grid
          */
         template: CommunityRow,
         
         /**
          * The HTML template for tag anchors
          */
         tagAnchorTemplate: TagAnchor,
        
         /**
          * @param args
          */
         constructor: function(args) {
         },
         
         /**
          * Sets the class for the Current grid row
          * @param grid The Grid Element
          * @param item An Object containing all of the data for the current row
          * @param i The number of the current grid row
          * @param items an object array containing the data for all of the grid rows
          * @returns A CSS Class
          */
         rowClass: function(grid, item, i, items) {
             return (i === 0 ? "placeRow lotusFirst" : "placeRow");
         },
         
         /**
          * Handles Displaying a Tooltip for an item
          * @param grid The grid Element
          * @param item An Object containing all of the data for the current row
          * @param i the number of the current grid row
          * @param items an object array containing the data for all of the grid rows
          * @returns A String, with the text to be displayed in the tooltip
          */
         tooltip: function(grid, item, i, items) {
             if (grid.communityAction) {
                 return grid.communityAction.getTooltip(item);
             }
         },
         
         /**
          * Handles encoding the image url
          * @param grid The grid Element
          * @param item An Object containing all of the data for the current row
          * @param i the number of the current grid row
          * @param items an object array containing the data for all of the grid rows
          * @returns The image url
          */
         communityImage: function(grid, item, i, items) {
        	 var logoUrl = item.getValue("logoUrl");
             return grid.encodeImageUrl(logoUrl);
         },
         
         /**
          * Returns the number of members for a community
          * @param grid The Grid Element
          * @param item An Object containing all of the data for the current row
          * @param i the Number of the current grid
          * @param items an object array containing the data for all of the grid rows
          * @returns A String with the number of members in a community
          */
         numOfMembers: function(grid, item, i, items) {
             var memberCount = item.getValue("memberCount");
             if (memberCount == 1) {
                 return this.nls.person;
             } else {
                 return stringUtil.replace(this.nls.people, { "memberCount" : memberCount });
             }
         },
         
         /**
          * Gets the last updated date for a community
          * @param grid The Grid Element
          * @param item An Object containing all of the data for the current row
          * @param i the number of the current grid row
          * @param items an object array containing the data for all of the grid rows
          * @returns The date when the community was last updates
          */
         updatedDate: function(grid, item, i, items) {
        	 return i18n.getUpdatedLabel((item.getValue("updated")));
         },
         
         /**
          * Displays the tags for a community
          * @param grid The Grid Element
          * @param item An Object containing all of the data for the current row
          * @param i The number of the current row
          * @param items  an object array containing the data for all of the grid rows
          * @returns {String}
          */
         displayTags: function(grid, item, i, items) {
        	 var tags = item.getValue("tags");
             if (tags == undefined) {
                 return "display: none";
             } else {
                 return "";
             }
         },
         
         /**
          * Displays the restricted image for a community
          * @method displayRestricted
          * @param grid The Grid Element
          * @param item An Object containing all of the data for the current row
          * @param i The number of the current row
          * @param items  an object array containing the data for all of the grid rows
          * @returns {String}
          */
         displayRestricted: function(grid, item, i, items) {
        	 var communityType = item.getValue("communityType");
             if (communityType == undefined || communityType != "publicInviteOnly") {
                 return "display: none;";
             } else {
                 return "display: inline-block;";
             }
         },
         
         /**
          * Get the tags for a community
          * @param grid The Grid Element
          * @param item An Object containing all of the data for the current row
          * @param i The number of the current row
          * @param items  an object array containing the data for all of the grid rows
          * @returns an array of strings, that are tags for a community
          */
         tagsLabel: function(grid, item, i, items) {
        	 var tags = item.getValue("tags");
             if (tags == undefined) {
                 return "";
             } else {
                 return this.nls.tags;
             }
         },
         
         /**
          * Substitutes tag labels(Strings) from the nls file into the tag
          * Anchor template which creates a tag links, and displays these tags on the page 
          * @param grid The Grid Element
          * @param item An Object containing all of the data for the current row
          * @param i The number of the current row
          * @param items  an object array containing the data for all of the grid rows
          * @returns {String}
          */
         tagsAnchors: function(grid, item, i, items) {
             var tags = item.getValue("tags");
             if (tags == undefined) {
                 return "";
             } else {
                 var tagsStr = "";
                 if (lang.isArray(tags)) {
                     for (var i=0; i<tags.length; i++) {
                         tagsStr += this._substitute(this.tagAnchorTemplate, { tagName : tags[i] });
                         if (i+1 < tags.length) {
                             tagsStr += ", ";
                         }
                     }
                 } else if (lang.isString(tags)) {
                	 tagsStr = this._substitute(this.tagAnchorTemplate, { tagName : tags });
                 }
                 return tagsStr;
             }
         },
         
         getUserProfileHref: function(grid,item,i,items){
        	 return this.getProfileUrl(grid,item.getValue("authorUserid"));
         }

         
    });
    
    return CommunityGridRenderer;
});
},
'sbt/connections/controls/vcard/SemanticTagService':function(){
/*
 * � Copyright IBM Corp. 2013
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

define([ "../../../declare", "../../../Endpoint", "dojo/_base/config", "sbt/config"], function(declare, Endpoint, dojoConfig, config) {

    /**
     * The class which handles loading of the semantic tag service for connections, this is needed for displaying vcards.
     * 
     * @class sbt.controls.vcard.connections.SemanticTagService
     */
    var SemanticTagService = declare(null, {
    });

    /**
     * Whether or not to enable bidirectional language support.
     * 
     * @property SemTagSvcConfig
     * @type Object
     */
    var SemTagSvcConfig = { 
        isBidiRTL: true
    };

    /**
     * Do not support loading semantic tag service from multiple different locations.
     * 
     * @method SemanticTagService.loadSemanticTagService
     * 
     * @param {Object} [args] Can contain an error function.
     *     @param {Function} [args.error]
     *     
     */
    SemanticTagService.loadSemanticTagService = function(args) {
        if (SemTagSvcConfig.error) {
            if (args.error) {
                args.error(SemTagSvcConfig.error);
            }
            return;
        }

        // load the semantic tag service (only once!!!)
        if (SemTagSvcConfig.loading || SemTagSvcConfig.loaded) {
            return;
        }
        SemTagSvcConfig.loading = true;
        
        var inclDojo = false;
        if (args && args.inclDojo) {
            inclDojo = args.inclDojo;
        }
        
        var endpoint = config.findEndpoint("connections");
        if (args && args.endpoint) {
            endpoint = args.endpoint;
        }
        var proxy = endpoint.proxy.proxyUrl + "/" + endpoint.proxyPath;
        if (!SemTagSvcConfig.baseUrl) {
            SemTagSvcConfig.baseUrl = endpoint.baseUrl; 
            SemTagSvcConfig.proxyURL = proxy;
        }

        var serviceUrl = "/profiles/ibm_semanticTagServlet/javascript/semanticTagService.js?inclDojo=" + inclDojo;
        
        var locale = "en";
        if (dojoConfig) {
        	dojoConfig.proxy = proxy;
        	locale = dojoConfig.locale;
        }
        endpoint.xhrGet({
            serviceUrl : serviceUrl,
            handleAs : "text",
            headers: {
                "Accept-Language": locale + ",en;q=0.8" // set request locale to dojo locale, fall back to english if unavailable.
            },
            load : function(response) {
                SemTagSvcConfig.loading = false;
                try {
                    var re = new RegExp(endpoint.baseUrl, "g");
                    var _response = response.replace(re, proxy);
                    eval(_response);
                    SemTagSvcConfig.loaded = true;
                } catch (error) {
                    SemTagSvcConfig.error = error;
                    if (args && args.error) {
                        args.error(error);
                    }
                }
            },
            error : function(error) {
                SemTagSvcConfig.loading = false;
                SemTagSvcConfig.error = error;
                if (args && args.error) {
                    args.error(error);
                }
            }
        });
        
    };
    
    return SemanticTagService;
});

},
'sbt/Endpoint':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Definition of the endpoint module
 * @module sbt.Endpoint
 */

/**
 * Endpoint which defines a connection to a back-end server.
 * 
 * @module sbt.Endpoint
 */
define(['./declare','./lang','./ErrorTransport','./Promise','./pathUtil','./compat','./log', './stringUtil', 'sbt/i18n!sbt/nls/Endpoint', './xml'],
function(declare,lang,ErrorTransport,Promise,pathUtil,compat,log,stringUtil,nls,xml) {

/**
 * This class encapsulates an actual endpoint, with its URL, proxy and its authentication mechanism.
 * 
 * @class sbt.Endpoint
 */
var Endpoint = declare(null, {
	
	/**
	 * URL of the server used to connect to the endpoint
	 * @property baseUrl
	 * @type String
	 */
	baseUrl: null,
	
	/**
	 * Proxy to be used
	 * @property proxy
	 * @type String 
	 */
	proxy: null,
	
	/**
	 * Path to be added to the proxy url, if any
	 * @property proxyPath
	 * @type String 
	 */
	proxyPath: null,
	
	/**
	 * Transport to be used
	 * @property transport
	 * @type String
	 */
	transport: null,
	
	/**
	 * Authenticator to be used
	 * @property authenticator
	 * @type String
	 */
	authenticator: null,
	
	/**
	 * Auth Type to be used
	 * @property authType
	 * @type String
	 */
	authType: null,
	
	/**
	 * UI Login mode: mainWindow, dialog or popup
	 * @property loginUi
	 * @type String
	 */
	loginUi: "",
	
	/**
	 * Page for login form for mainWindow and popup
	 * @property loginPage
	 * @type String
	 */
	loginPage: null,
	
	/**
	 * Page for login form for dialog
	 * @property dialogLoginPage
	 * @type String
	 */
	dialogLoginPage: null,
	
	/**
	 * Whether auth dialog should come up automatically or not. In case of not 401 would be propagated to user.
	 * @property autoAuthenticate
	 * @type String
	 */
	autoAuthenticate: null,
	
	/**
	 * Whether user is authenticated to endpoint or not.
	 * @property isAuthenticated
	 * @type String
	 */
	isAuthenticated: false,
	
	/**
	 * The error code that is returned from the endpoint on authentication failure.
	 * @property authenticationErrorCode
	 * @type String
	 */
	authenticationErrorCode: null,
	
	/**
	 * Simple constructor that mixes in its parameters as object properties
	 * @constructor
	 * @param {Array} args
	 */	
	constructor: function(args) {
		lang.mixin(this, args || {});	
	},
	
    /**
     * Provides an asynchronous request using the associated Transport.
     * 
     * @method request
     * @param {String)
     *            url The URL the request should be made to.
     * @param {String)
     *            loginUi The type of UI to use when authenticating,
     *            valid values are: mainWindow, popup, dialog.
     * @param {Boolean)
     *            authAuthenticate if true the Endpoint with authenticate
     *            when a 401 (or associated authenication code) is received.
     * @param {Object}
     *            [options] Optional A hash of any options for the provider.
     * @param {String|Object}
     *            [options.data=null] Data, if any, that should be sent with
     *            the request.
     * @param {String|Object}
     *            [options.query=null] The query string, if any, that should
     *            be sent with the request.
     * @param {Object}
     *            [options.headers=null] The headers, if any, that should
     *            be sent with the request.
     * @param {Boolean}
     *            [options.preventCache=false] If true will send an extra
     *            query parameter to ensure the the server won�t supply
     *            cached values.
     * @param {String}
     *            [options.method=GET] The HTTP method that should be used
     *            to send the request.
     * @param {Integer}
     *            [options.timeout=null] The number of milliseconds to wait
     *            for the response. If this time passes the request is
     *            canceled and the promise rejected.
     * @param {String}
     *            [options.handleAs=text] The content handler to process the
     *            response payload with.
     * @return {sbt.Promise}
     */
    request : function(url, options) {
        // rewrite the url if needed
        var qurl = url;
        if (qurl.indexOf("http") != 0) {
            if (this.proxy) {
                qurl = this.proxy.rewriteUrl(this.baseUrl, url, this.proxyPath);
            } else {
                qurl = pathUtil.concat(this.baseUrl, url);
            }
        }
        
        if (!options) {
        	options = { 
        	   	method : "GET", 
        	   	handleAs : "text"
        	};
        }
        
        var promise = new Promise();
        promise.response = new Promise();       

		var self = this;
		this.transport.request(qurl, options).response.then(function(response) {
			
			// Check for form based authentication
			if(self.authType == "form"){
				var authRequiredFlag = self._isAuthRequiredFormBasedEP(response, options);
					if(authRequiredFlag){
						self._authenticate(url, options, promise);
					}
			}
			
			promise.fulfilled(response.data);
			promise.response.fulfilled(response);
		}, function(error) {
			if (!error.message) {
				error.message = self.getErrorMessage(error.cause);
			}
			var authRequiredPromise = self._isAuthRequired(error, options);
			authRequiredPromise.then(
				function(response) {
					if (response) {
						self._authenticate(url, options, promise);
					} else {
						promise.rejected(error);
						promise.response.rejected(error);
					}
				}, 
				function(error) {
					promise.rejected(error);
					promise.response.rejected(error);
				}
			);
		});
        
        return promise;
    },
	
	/*
	 * Sends a request using XMLHttpRequest with the given URL and options.
	 * 
	 * @method xhr 
	 * @param {String} [method] The HTTP method to use to make the request. Must be uppercase. Default is 'GET'. 
	 * @param {Object} [args]
	 * 		@param {String} [args.url] 
	 * 		@param {Function} [args.handle] 
	 * 		@param {Function} [args.load] 
	 *  	@param {Function} [args.error] 
	 *  	@param {Boolean} [hasBody]
	 */
	xhr: function(method,args,hasBody) {
		var self = this;
		var _args = lang.mixin({},args);
		// We make sure that args has a 'url' member, with or without a proxy 
		if(!_args.url) {
			if(this.proxy) {
				_args.url = this.proxy.rewriteUrl(this.baseUrl,_args.serviceUrl,this.proxyPath);
			} else {
				_args.url = pathUtil.concat(this.baseUrl,_args.serviceUrl);
			}
		}
		// Make sure the initial methods are not called
		// seems that Dojo still call error(), even when handle is set
		delete _args.load; delete _args.error;
		_args.handle = function(data,ioArgs) {
			if(data instanceof Error) {
				var error = data;
				if(!error.message){
					error.message = self.getErrorMessage(error.cause);
				} 
				var isForbiddenErrorButAuthenticated = false;
				// check for if authentication is required
				if(error.code == 403 && self.authenticationErrorCode == 403){ 
					// case where 403 is configured to be treated similar to 401 (unAuthorized)
		        	// checking if we are getting 403 inspite of being already authenticated (eg. Get Public Files/Folders API on Smartcloud
		        	if(self.isAuthenticated){
		        		isForbiddenErrorButAuthenticated = true;
		        	}
		        }
				if ((error.code == 401)|| (!isForbiddenErrorButAuthenticated && error.code == self.authenticationErrorCode)) {
					var autoAuthenticate =  _args.autoAuthenticate || self.autoAuthenticate;
					if(autoAuthenticate == undefined){
						autoAuthenticate = true;
					}
					if(autoAuthenticate){
						if(self.authenticator) {
							options = {
								dialogLoginPage:self.loginDialogPage,
								loginPage:self.loginPage,
								transport:self.transport, 
								proxy: self.proxy,
								proxyPath: self.proxyPath,
								loginUi: _args.loginUi || self.loginUi,
								name: self.name,
								callback: function() {
									self.xhr(method,args,hasBody);
								}
							};
							if(self.authenticator.authenticate(options)) {
								return;
							}
						}
					}
				} 

                // notify handle and error callbacks is available
				self._notifyError(args, error, ioArgs);
			} else {
			    // notify handle and load callbacks is available
			    self._notifyResponse(args, data, ioArgs);
			}
		};	
		this.transport.xhr(method, _args, hasBody);
	},
	
	/*
	 * @method xhrGet
	 * @param args
	 */
	xhrGet: function(args) {
		this.xhr("GET",args);
	},
	
	/*
	 * @method xhrPost
	 * @param args
	 */
	xhrPost: function(args){
		this.xhr("POST", args, true); 
	},
	
	/*
	 * @method xhrPut
	 * @param args
	 */
	xhrPut: function(args){
		this.xhr("PUT", args, true);
	},
	
	/*
	 * @method xhrDelete
	 * @param args
	 */
	xhrDelete: function(args){
		this.xhr("DELETE", args);
	},
	
	/**
	 * authenticate to an endpoint
	 *
	 * @method authenticate
	 * @param {Object} [args]  Argument object
	 *		@param {boolean} [args.forceAuthentication] Whether authentication is to be forced in case user is already authenticated.
	 *		@param {String} [args.loginUi] LoginUi to be used for authentication. possible values are: 'popup', 'dialog' and 'mainWindow'
	 *		@param {String} [args.loginPage] login page to be used for authentication. this property should be used in case default
	 *		login page is to be overridden. This only applies to 'popup' and 'mainWindow' loginUi
	 *		@param {String} [args.dialogLoginPage] dialog login page to be used for authentication. this property should be used in
	 *		case default dialog login page is to be overridden. This only applies to 'dialog' loginUi.
	 */
	authenticate : function(args) {
		var promise = new Promise();
		args = args || {};
		if (args.forceAuthentication || !this.isAuthenticated) {
			var options = {
				dialogLoginPage : this.loginDialogPage,
				loginPage : this.loginPage,
				transport : this.transport,
				proxy : this.proxy,
				proxyPath : this.proxyPath,
				loginUi : args.loginUi || this.loginUi,
				name: this.name,
				callback: function(response) {
					promise.fulfilled(response);
				},
				cancel: function(response) {
					promise.rejected(response);
				}
			};
			this.authenticator.authenticate(options);
		} else {
			promise.fulfilled(true);
		}
		return promise;
	},
	
	/**
	 * Logout from an endpoint
	 *
	 * @method logout
	 * @param {Object} [args]  Argument object
	 */
	logout : function(args) {
		var promise = new Promise();
		args = args || {};
		var self = this;
		var proxy = this.proxy.proxyUrl;
		var actionURL = proxy.substring(0, proxy.lastIndexOf("/")) + "/authHandler/" + this.proxyPath + "/logout";
		this.transport.xhr('POST',{
			handleAs : "json",
			url : actionURL,
			load : function(response) {
				self.isAuthenticated = false;
				promise.fulfilled(response);
			},
			error : function(response) {
				self.isAuthenticated = false;
				promise.rejected(response);
			}
		}, true);		
		return promise;
	},
	
	/**
	 * Find whether endpoint is authenticated or not.
	 *
	 * @method isAuthenticated
	 * @param {Object} [args]  Argument object
	 */
	isAuthenticated : function(args) {
		var promise = new Promise();
		args = args || {};
		var self = this;
		var proxy = this.proxy.proxyUrl;
		var actionURL = proxy.substring(0, proxy.lastIndexOf("/")) + "/authHandler/" + this.proxyPath + "/isAuth";
		this.transport.xhr('POST',{
			handleAs : "json",
			url : actionURL,
			load : function(response) {
				self.isAuthenticated = true;
				promise.fulfilled(response);
			},
			error : function(response) {
				promise.rejected(response);
			}
		}, true);		
		return promise;
	},
	
	/**
	 Find whether endpoint authentication is valid or not.
	 
	 @method isAuthenticationValid
	 @param {Object} [args]  Argument object
			@param {Function} [args.load] This is the function which isAuthenticationValid invokes when 
			authentication information is retrieved.
			@param {Function} [args.error] This is the function which isAuthenticationValid invokes if an error occurs.
			result property in response object returns true/false depending on whether authentication is valid or not.
	*/
	isAuthenticationValid : function(args) {
		var promise = new Promise();
		args = args || {};
		var self = this;
		var proxy = this.proxy.proxyUrl;
		var actionURL = proxy.substring(0, proxy.lastIndexOf("/")) + "/authHandler/" + this.proxyPath + "/isAuthValid";
		this.transport.xhr('POST',{			
			handleAs : "json",
			url : actionURL,
			load : function(response) {				
				self.isAuthenticated = response.result;
				promise.fulfilled(response);
			},
			error : function(response) {
				promise.rejected(response);
			}
		}, true);
		return promise;
	},
	
	// Internal stuff goes here and should not be documented
	
    /*
     * Invoke error function with the error
     */
    _notifyError: function(args, error, ioArgs) {
        if (args.handle) {
            try {
                args.handle(error, ioArgs);
            } catch (ex) {
                // TODO log an error
                var msg = ex.message;
            }
        }
        if (args.error) {
            try {
                args.error(error, ioArgs);
            } catch (ex) {
                // TODO log an error
                var msg = ex.message;
            }
        }
    },
    
    /*
     * Invoke handle and/or load function with the response
     */
    _notifyResponse: function(args, data, ioArgs) {
        if (args.handle) {
            try {
                args.handle(data, ioArgs);
            } catch (ex) {
                // TODO log an error
                var msg = ex.message;
            }
        }
        if (args.load) {
            try {
                args.load(data, ioArgs);
            } catch (ex) {
                // TODO log an error
                var msg = ex.message;
            }
        }
    },

    /*
     * Invoke automatic authentication for the specified request.
     */
    _authenticate: function(url, options, promise) {
        var self = this;
        var authOptions = {
            dialogLoginPage: this.loginDialogPage,
            loginPage: this.loginPage,
            transport: this.transport, 
            proxy: this.proxy,
            proxyPath: this.proxyPath,
            loginUi: options.loginUi || this.loginUi,
            name: this.name,
            callback: function() {
                self.request(url, options).response.then(
                    function(response) {
                        promise.fulfilled(response.data);
                        promise.response.fulfilled(response);
                    }, function(error) {
                        promise.rejected(error);
                        promise.response.rejected(error);
                    }
                );
            },
            cancel: function() {
                self._authRejected = true;
                var error = new Error();
                error.message = "Authentication is required and has failed or has not yet been provided.";
                error.code = 401;
                promise.rejected(error);
                promise.response.rejected(error);
            }
        };
        
        return this.authenticator.authenticate(authOptions);
    },

    /*
     * Return true if automatic authentication is required. This method returns a promise with the success callback returning
	 * a boolean whether authentication is required. It first checks if the client is already authenticated
	 * and if yes, whether the authentication is valid. Else, it checks for the status code and other
	 * configuration paramters to decide if authentication is required.
     */
 	_isAuthRequired : function(error, options) {		
		var promise = new Promise();
		var status = error.response.status || null;
		var isAuthErr = status == 401 || status == this.authenticationErrorCode;
		if (this.isAuthenticated) {
			if (!isAuthErr) {
				promise.fulfilled(false);
			} else {
				this.isAuthenticationValid().then(
					function(response) {
						promise.fulfilled(!response.result);
					}, 
					function(response) {
						promise.rejected(response);
					}
				);
			}
		} else {
			// User can mention autoAuthenticate as part service wrappers call that is the args json variable or
			// as a property of endpoint in managed-beans.xml. 
			// isAutoAuth variable is true when autoAuthenticate property is true in args json variable or 
			// autoAuthenticate property in endpoint defination in managed-beans.xml is true. It is false otherwise.
			var isAutoAuth = options.autoAuthenticate || this.autoAuthenticate;
			if (isAutoAuth == undefined) {
				isAutoAuth = true;
			}
			// The response is returned as a boolean value as an argument to the success callback of the promise. This 
			// value is true when the error code is 401 or any authentication error code for a particular endpoint
			// (isAuthErr variable) and autoAuthenticate parameter is mentioned true (based on isAutoAuth variable)
			// and authenticator property the endpoint (could be js object of type Basic or OAuth)is defined and the 
			// authentication was not rejected earlier. 
			// It is false otherwise. The true value of this expression triggers the authentication process from the client.
			promise.fulfilled(isAuthErr && isAutoAuth && this.authenticator && !this._authRejected);
		}
		return promise;
	},
	
	/*
	 * Method ensures we trigger authentication for Smartcloud when response code is 200 and content is login page
	 */
	_isAuthRequiredFormBasedEP : function (response, options){
		if(response.status == 200 && response.getHeader("Content-Type") == "text/html"){
			return true;
		}else{
			return false;
		}
	},
	
    getErrorMessage: function(error) {    	
        var text = error.responseText || (error.response&&error.response.text) ;
        if (text) {
            try {            	
                var dom = xml.parse(text);
                var messages = dom.getElementsByTagName("message");
                if (messages && messages.length != 0) {                	
                    text = messages[0].text || messages[0].textContent;                	
                    text = lang.trim(text);
                }
            } catch(ex) {                
            }  
            var trimmedText = text.replace(/(\r\n|\n|\r)/g,"");
            if(!(trimmedText)){            	
            	return error.message;
            }else{
            	return text;
            }
        } else {
            return error;
        }
    }
	
});

return Endpoint;
});

},
'sbt/ErrorTransport':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Implementation of a transport that emits an error the first time it is invoked.
 * @module sbt.ErrorTransport
 */
define(['./declare','./lang','./Promise','./stringUtil','./log','sbt/i18n!sbt/nls/ErrorTransport'], function(declare,lang,Promise,stringUtil,log,nls) {
    return declare(null, {
        _called: false,
        _endpointName: null,
        _message: null,
        
        constructor: function(endpointName, message) {
            this._endpointName = endpointName;
            if (message) {
                this._message = message;
            } else {
            	this._message = stringUtil.substitute(nls.endpoint_not_available, [endpointName]);
            }
        },
        
        request : function(url,options) {
            if (!this._called) {
                alert(this._message);
                this._called = true;
            }
            var promise = new Promise();
            var error = new Error(this._message);
            error.status = 400;
            promise.rejected(error);
            return promise;
        },
        
        xhr: function(method, args, hasBody) {
            if (!this._called) {
                log.error(this._message);
                this._called = true;
            }
            var _handle = args.handle;
            var _error = args.error;
            if (lang.isFunction(_error) || lang.isFunction(_handle)) {
                var error = new Error(this._message);
                error.status = 400;
                if(lang.isFunction(_error)){
                	_error(error);
                }
                if(lang.isFunction(_handle)){
                	_handle(error);
                }                
            }
        }
    });
});
},
'sbt/Promise':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * 
 * @module sbt.Promise
 */
define(["./declare","./log"], function(declare,log) {

    /**
     * Promise class
     * 
     * @class Promise
     * @namespace sbt
     */     
	var Promise = declare(null, {	
		
        // private
        _isRejected : false,
        _isFulfilled : false,
        _isCanceled : false,
        _deferreds : null,
        response : null,
        error : null,
        
        /*
         * Constructor for the promise.
         */
        constructor: function(response) {
            if (response) {
                if (response instanceof Error) {
                    this.rejected(response);
                } else {
                    this.fulfilled(response);
                }
            } else {
                this._deferreds = [];
            }
        },
        
        /*
         * Add new callbacks to the promise.
         */
        then: function(fulfilledHandler, errorHandler) {
        	var promise = new Promise();
            if (this._isFulfilled) {
            	this._fulfilled(fulfilledHandler, errorHandler, promise, this.data);
            } else if (this._isRejected) {
            	this._rejected(errorHandler, promise, this.error);
            } else {
                this._deferreds.push([ fulfilledHandler, errorHandler, promise ]);
            }
            return promise;
        },

        /*
         * Inform the deferred it may cancel its asynchronous operation.
         */
        cancel: function(reason, strict) {
            this._isCanceled = true;
        },

        /*
         * Checks whether the promise has been resolved.
         */
        isResolved: function() {
            return this._isRejected || this._isFulfilled;
        },

        /*
         * Checks whether the promise has been rejected.
         */
        isRejected: function() {
            return this._isRejected;
        },

        /*
         * Checks whether the promise has been resolved or rejected.
         */
        isFulfilled: function() {
            return this._isFulfilled;
        },

        /*
         * Checks whether the promise has been canceled.
         */
        isCanceled: function() {
            return this._isCanceled;
        },

        /*
         * Called if the promise has been fulfilled
         */
        fulfilled : function(data) {
            if (this._isCanceled) {
                return;
            }
            
            this._isFulfilled = true;
            this.data = data;
            
            if (this._deferreds) {
                while (this._deferreds.length > 0) {
                    var deferred = this._deferreds.shift();
                    var fulfilledHandler = deferred[0];
                    var errorHandler = deferred[1];
                    var promise = deferred[2];
                	this._fulfilled(fulfilledHandler, errorHandler, promise, data);
                }
            }
        },
        
        /*
         * Call if the promise has been rejected
         */
        rejected : function(error) {
            if (this._canceled) {
                return;
            }
            
            this._isRejected = true;
            this.error = error;
            
            if (this._deferreds) {
                while (this._deferreds.length > 0) {
                    var deferred = this._deferreds.shift();
                    var errorHandler = deferred[1];
                    var promise = deferred[2];
                	this._rejected(errorHandler, promise, error);
                }
            }
        },
        
        _fulfilled : function(fulfilledHandler, errorHandler, promise, data) {
            if (fulfilledHandler) {
            	try {
                	var retval = fulfilledHandler(data);
                	if (retval instanceof Promise) {
                		retval.then(
                			function(data) {
                				promise.fulfilled(data);
                			},
                			function(error) {
                				promise.rejected(error);
                			}
                		);
                	} else {
                		promise.fulfilled(retval);
                	}
            	} catch (error) {
            		promise.rejected(error);
            	}
            } else {
            	promise.fulfilled(data);
            }
        },
        
        _rejected : function(errorHandler, promise, error) {
            if (errorHandler) {
            	try {
                	var retval = errorHandler(error);
                	if (!retval) {
                		// stop propogating errors
                		promise.rejected(retval);
                	}
            	} catch (error1) {
            		promise.rejected(error1);
            	}
            } else {
            	promise.rejected(error);
            }
        }
	
	});
	return Promise;
});
},
'sbt/pathUtil':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK - URL utilities
 */
define(['./stringUtil'],function(stringUtil) {
	return {
		concat: function(path1,path2) {
			if(!path1) {
				return path2;
			}
			if(!path2) {
				return path1;
			}
			if(stringUtil.endsWith(path1,"/")) {
				path1 = path1.substring(0,path1.length-1);
			}
			if(stringUtil.startsWith(path2,"/")) {
				path2 = path2.substring(1);
			}
			return path1 + "/" + path2;
		},
		isAbsolute: function(url) {
			return url.indexOf("://")>=0;
		}
	}
});
},
'sbt/compat':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK - Compatibility with older browsers
 * 
 *  @module sbt.compat
 */
define([],function() {
	if (!Array.prototype.indexOf){
		Array.prototype.indexOf = function(item, start) {
			 var index = start || 0;
			 var max = this.length;
		     for (; index < max; index++) {
		         if (this[index] === item) { return index; }
		     }
		     return -1;
		};
	}
	return {
		
	};
});
},
'sbt/connections/controls/communities/CommunityMembersAction':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(
		[ "../../../declare", "../../../controls/grid/GridAction",
				"sbt/connections/CommunityService",
				"sbt/connections/CommunityConstants" ],

		function(declare, GridAction, CommunityService, CommunityConstants) {

			/**
			 * @class ProfileAction
			 * @namespace sbt.connections.controls.communities
			 * @module sbt.connections.controls.communities.CommunityMembersAction
			 */
			var CommunityMembersAction = declare(
					GridAction,
					{

						/** Strings used in the actions */
						nls : {
							tooltip : "Go to ${name}"
						},

						/**
						 * ProfileAction Constructor function
						 * 
						 * @method constructor
						 */
						constructor : function() {
						},

						/**
						 * Handles displaying a tooltip for an item For
						 * profiles, the tooltip by default will be a business
						 * card So nothing is done in this function
						 * 
						 * @method getTooltip
						 * @param item
						 *            The element that will use the tooltip
						 */
						getTooltip : function(item) {

							// for default the semantic tag service will pop up
							// the business card
							// so do nothing here

						},

						/**
						 * The execute function is called from the handle click
						 * function For Profiles by default the business card
						 * functionality is used which works from the Semantic
						 * tag service so nothing is done here.
						 * 
						 * @method execute
						 * @param item
						 *            The item which fired the event
						 * @param opts
						 * @param event
						 *            The event
						 */
						execute : function(item, opts, event) {

							// for default the semantic tag service will pop up
							// the business card
							// so do nothing here
						},

						/**
						 * The removeMember function is called from the
						 * removeMember function and removes members from the
						 * community specified by currentCommunity
						 * 
						 * @method removeMember
						 * @param grid
						 *            The grid to update after the member has
						 *            been removed.
						 * @param communityService
						 *            The community service instance used to
						 *            remove the member.
						 * @param currentCommunity
						 *            The ID of the community from which to
						 *            remove the member.
						 * @param data
						 *            The data row from which the event was
						 *            fired.
						 */
						removeMember : function(grid, currentCommunity, data) {
							_removeMember(grid, currentCommunity, data);
						},
						
						/**
						 * Updates a community member.
						 * 
						 * @method updateMember
						 * @param grid
						 *            The grid to update.
						 * @param el
						 *            the element that fired the event
						 * @param data
						 *            all of the items from the current row of
						 *            the grid.
						 * @param ev
						 *            the event
						 * @param grid
						 *            The grid to update after the member has
						 *            been removed.
						 * 
						 * @param communityUuid
						 *            The ID of the community from which to
						 *            remove the member.
						 * @param data
						 *            The data row from which the event was
						 *            fired.
						 */
						updateMember : function(grid, el, data, ev, communityUuid) {
							var communityService = new CommunityService();
							var rbOwnerId = "rbOwner" + data.uid;
							var rbOwner = document.getElementById(rbOwnerId);
							communityService.getMembers(communityUuid).then(
									function(members) {
										for ( var i = 0; i < members.length; i++) {
											var member = members[i];
											if (member.getUserid() == data.uid) {
												if (rbOwner.checked) {
													member.setRole(CommunityConstants.Owner);
												} else {
													member.setRole(CommunityConstants.Member);
												}

												// Update community
												var promise = communityService.updateMember(
															communityUuid, member);
												
												promise.then(function(data) {
													grid.update(null);
												}, function(error) {
													console.log(error);
												});
												break;
										}
									}
								}, function(error) {
									console.log(error);
							});
						},
						
						/**
						 * Closes the edit form.
						 * 
						 * @method removeMember
						 * @param grid
						 *            The grid to update.
						 * @param el
						 *            the element that fired the event
						 * @param data
						 *            all of the items from the current row of
						 *            the grid.
						 * @param ev
						 *            the event
						 * @param grid
						 *            The grid to update after the member has
						 *            been removed.
						 * @param data
						 *            The data row from which the event was
						 *            fired.
						 */
						closeEditForm : function(grid, el, data, ev) {
							var id = "editTable" + data.uid;
			                document.getElementById(id).style.display = "none";
						},
						
						/**
						 * Opens the edit form.
						 * 
						 * @method openEditForm
						 * @param data
						 *            The data row from which the event was
						 *            fired.
						 */
						openEditForm : function(data) {
							var id = "editTable" + data.uid;
			                document.getElementById(id).style.display = "block";
						},
						

						/**
						 * Removes a community member.
						 * 
						 * @method removeMember
						 * @param grid
						 *            The grid to update.
						 * @param el
						 *            the element that fired the event
						 * @param data
						 *            all of the items from the current row of
						 *            the grid.
						 * @param ev
						 *            the event
						 * @param grid
						 *            The grid to update after the member has
						 *            been removed.
						 * 
						 * @param communityUuid
						 *            The ID of the community from which to
						 *            remove the member.
						 * @param data
						 *            The data row from which the event was
						 *            fired.
						 */
						removeMember : function(grid, el, data, ev, communityUuid) {
							var communityService = new CommunityService();

							communityService.getMembers(currentCommunity).then(
									function(members) {
										for (var i = 0; i < members.length; i++) {
											var member = members[i];
											if (member.getUserid() == data.uid) {
												// Remove member
												communityService.getCommunity(currentCommunity).then(
														function(community) {
															community.removeMember(member.getUserid(), {}).then(
																	function(memberId) {
																		grid.update(null);
																		el.parentNode.removeChild(container);
																	},
																	function(error) {
																		console.log(error);
																	});
														},
														function(error) {
															console.log(error);
														});
												break;
											}
										}
									}, function(error) {
										console.log(error);
									});
						}
					});
			return CommunityMembersAction;
		});

},
'sbt/connections/CommunityService':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * The Communities API allows application programs to retrieve community information, subscribe to community updates, and create or modify communities.
 * 
 * @module sbt.connections.CommunityService
 */
define([ "../declare", "../config", "../lang", "../stringUtil", "../Promise", "./CommunityConstants", "../base/BaseService",
         "../base/BaseEntity", "../base/XmlDataHandler", "./ForumService", "../pathUtil" ], 
    function(declare,config,lang,stringUtil,Promise,consts,BaseService,BaseEntity,XmlDataHandler,ForumService,pathUtil) {

    var CommunityTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\"><title type=\"text\">${getTitle}</title><content type=\"html\">${getContent}</content><category term=\"community\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>${getTags}<snx:communityType>${getCommunityType}</snx:communityType><snx:isExternal>false</snx:isExternal>${getCommunityUuid}${getCommunityTheme}</entry>";
    var CategoryTmpl = "<category term=\"${tag}\"></category>";
    var CommunityUuidTmpl = "<snx:communityUuid xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">${communityUuid}</snx:communityUuid>";
    var CommunityThemeTmpl = "<snx:communityTheme xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" snx:uuid=\"default\">${ommunityTheme}</snx:communityTheme>";
    var MemberTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\"><contributor>${getEmail}${getUserid}</contributor><snx:role xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" component=\"http://www.ibm.com/xmlns/prod/sn/communities\">${getRole}</snx:role><category term=\"person\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category></entry>";
    var EmailTmpl = "<email>${email}</email>";
    var UseridTmpl = "<snx:userid xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">${userid}</snx:userid>";
    var TitleTmpl = "<title type=\"text\">${title}</title>";
    var ContentTmpl = "<content type=\"text\">${content}</content>";
    var InviteTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\"><category term=\"invite\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>${getTitle}${getContent}<contributor>${getEmail}${getUserid}</contributor></entry>";
    
    /*
     * CommunityDataHandler class.
     */
    var CommunityDataHandler = declare(XmlDataHandler, {
        /**
         * @method getEntityId
         * @returns
         */
        getEntityId : function() {
            var entityId = stringUtil.trim(this.getAsString("uid"));
            return extractCommunityUuid(this.service, entityId);
        }
    });
    
    /**
     * Community class represents an entry for a Community feed returned by the
     * Connections REST API.
     * 
     * @class Community
     * @namespace sbt.connections
     */
    var Community = declare(BaseEntity, {

        /**
         * Construct a Community entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of IBM Connections community ID from community ATOM
         * entry document.
         * 
         * @method getCommunityUuid
         * @return {String} Community ID of the community
         */
        getCommunityUuid : function() {
            var communityUuid = this.getAsString("communityUuid");
            return extractCommunityUuid(this.service, communityUuid);
        },

        /**
         * Sets id of IBM Connections community.
         * 
         * @method setCommunityUuid
         * @param {String} communityUuid Id of the community
         */
        setCommunityUuid : function(communityUuid) {
            return this.setAsString("communityUuid", communityUuid);
        },

        /**
         * Return the value of IBM Connections community title from community
         * ATOM entry document.
         * 
         * @method getTitle
         * @return {String} Community title of the community
         */
        getTitle : function() {
            return this.getAsString("title");
        },

        /**
         * Sets title of IBM Connections community.
         * 
         * @method setTitle
         * @param {String} title Title of the community
         */
        setTitle : function(title) {
            return this.setAsString("title", title);
        },

        /**
         * Return the community type of the IBM Connections community from
         * community ATOM entry document.
         * 
         * @method getCommunityType
         * @return {String} Type of the Community
         */
        getCommunityType : function() {
            return this.getAsString("communityType");
        },

        /**
         * Set the community type of the IBM Connections community.
         * 
         * @method setCommunityType
         * @param {String} Type of the Community
         */
        setCommunityType : function(communityType) {
            return this.setAsString("communityType", communityType);
        },

        /**
         * Return the community theme of the IBM Connections community from
         * community ATOM entry document.
         * 
         * @method getCommunityTheme
         * @return {String} Theme of the Community
         */
        getCommunityTheme : function() {
            return this.getAsString("communityTheme");
        },

        /**
         * Set the community theme of the IBM Connections community.
         * 
         * @method setCommunityTheme
         * @param {String} Theme of the Community
         */
        setCommunityTheme : function(communityTheme) {
            return this.setAsString("communityTheme", communityTheme);
        },

        /**
         * Return the value of IBM Connections community description from
         * community ATOM entry document.
         * 
         * @method getContent
         * @return {String} Community description of the community
         */
        getContent : function() {
            return this.getAsString("content");
        },

        /**
         * Sets description of IBM Connections community.
         * 
         * @method setContent
         * @param {String} content Description of the community
         */
        setContent : function(content) {
            return this.setAsString("content", content);
        },

        /**
         * Return tags of IBM Connections community from community ATOM entry
         * document.
         * 
         * @method getTags
         * @return {Object} Array of tags of the community
         */
        getTags : function() {
            return this.getAsArray("tags");
        },

        /**
         * Set new tags to be associated with this IBM Connections community.
         * 
         * @method setTags
         * @param {Object} Array of tags to be added to the community
         */

        setTags : function(tags) {
            return this.setAsArray("tags", tags);
        },

        /**
         * Gets an author of IBM Connections community.
         * 
         * @method getAuthor
         * @return {Object} Author of the community
         */
        getAuthor : function() {
            return this.getAsObject([ "authorUserid", "authorName", "authorEmail" ]);
        },

        /**
         * Gets a contributor of IBM Connections community.
         * 
         * @method getContributor
         * @return {Object} Contributor of the community
         */
        getContributor : function() {
            return this.getAsObject([ "contributorUserid", "contributorName", "contributorEmail" ]);
        },

        /**
         * Return the value of IBM Connections community description summary
         * from community ATOM entry document.
         * 
         * @method getSummary
         * @return {String} Community description summary of the community
         */
        getSummary : function() {
            return this.getAsString("summary");
        },

        /**
         * Return the value of IBM Connections community URL from community ATOM
         * entry document.
         * 
         * @method getCommunityUrl
         * @return {String} Community URL of the community
         */
        getCommunityUrl : function() {
            return this.getAsString("communityUrl");
        },

        /**
         * Return the value of IBM Connections community Logo URL from community
         * ATOM entry document.
         * 
         * @method getLogoUrl
         * @return {String} Community Logo URL of the community
         */
        getLogoUrl : function() {
            return this.getAsString("logoUrl");
        },

        /**
         * Return the member count of the IBM Connections community from
         * community ATOM entry document.
         * 
         * @method getMemberCount
         * @return {Number} Member count for the Community
         */
        getMemberCount : function() {
            return this.getAsNumber("memberCount");
        },

        /**
         * Return the published date of the IBM Connections community from
         * community ATOM entry document.
         * 
         * @method getPublished
         * @return {Date} Published date of the Community
         */
        getPublished : function() {
            return this.getAsDate("published");
        },

        /**
         * Return the last updated date of the IBM Connections community from
         * community ATOM entry document.
         * 
         * @method getUpdated
         * @return {Date} Last updated date of the Community
         */
        getUpdated : function() {
            return this.getAsDate("updated");
        },

        /**
         * Get a list for forum topics that includes the topics in this community.
         * 
         * @method getForumTopics
         * @param {Object} args
         */
        getForumTopics : function(args) {
        	return this.service.getForumTopics(this.getCommunityUuid(), args);
        },
        
        /**
         * Create a forum topc by sending an Atom entry document containing the 
         * new forum to the forum replies resource.
         * 
         * @method createForumTopic
         * @param {Object} forumTopic Forum topic object which denotes the forum topic to be created.
         * @param {Object} [args] Argument object
         */
        createForumTopic : function(communityUuid, topicOrJson, args) {
        	return this.service.createForumTopic(this.getCommunityUuid(), topicOrJson, args);
        },
        
        /**
         * Get sub communities of a community.
         * 
         * @method getSubCommunities
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of members of a
         * community. The parameters must be exactly as they are
         * supported by IBM Connections like ps, sortBy etc.
         */
        getSubCommunities : function(args) {
            return this.service.getSubCommunities(this.getCommunityUuid(), args);
        },

        /**
         * Get members of this community.
         * 
         * @method getMembers
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of members of a
         * community. The parameters must be exactly as they are
         * supported by IBM Connections like ps, sortBy etc.
         */
        getMembers : function(args) {
            return this.service.getMembers(this.getCommunityUuid(), args);
        },

        /**
         * Add member to a community
         * 
         * @method addMember
         * @param {Object} [args] Argument object
         * @param {Object} [args.member] Object representing the member to be added
         * @param {String} [args.email] Object representing the email of the memeber to be added
         * @param {String} [args.id] String representing the id of the member to be added
         */
        addMember : function(member,args) {
            return this.service.addMember(this.getCommunityUuid(), member, args);
        },

        /**
         * Remove member of a community
         * 
         * @method removeMember
         * @param {String} Member id of the member 
         * @param {Object} [args] Argument object
         */
        removeMember : function(memberId,args) {
            return this.service.removeMember(this.getCommunityUuid(), memberId, args);
        },
        
        /**
         * Loads a member object with the atom entry associated with the
         * member of the community. By default, a network call is made to load the atom entry
         * document in the member object.
         * 
         * @method getMember
         * @param {String} member id of the member.
         * @param {Object} [args] Argument object
         */
        getMember : function(memberId, args) {
        	return this.service.getMember(this.getCommunityUuid(), memberId, args);
        },
        
        /**
         * Get a list of the outstanding community invitations for the specified community. 
         * The currently authenticated user must be an owner of the community.
         * 
         * @method getAllInvites
         * @param {Object} [args]
         */
        getAllInvites : function(args) {
        	return this.service.getAllInvites(this.getCommunityUuid(), args);
        },

        /**
         * Get the list of community forums.
         * 
         * @method getForums
         * @param {Object} [args] Argument object
         */
        getForums : function(args) {
        	var forumService = this.service.getForumService();
        	var requestArgs = lang.mixin(args || {}, { communityUuid : this.getCommunityUuid() });
        	return forumService.getForums(requestArgs);
        },
        
        /**
         * Loads the community object with the atom entry associated with the
         * community. By default, a network call is made to load the atom entry
         * document in the community object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var communityUuid = this.getCommunityUuid();
            var promise = this.service._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.setDataHandler(new CommunityDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.CommunityXPath
                    }));
                    return self;
                }
            };

            var requestArgs = lang.mixin({
                communityUuid : communityUuid
            }, args || {});
            var options = {
                handleAs : "text",
                query : requestArgs
            };
            
            return this.service.getEntity(consts.AtomCommunityInstance, options, communityUuid, callbacks);
        },

        /**
         * Remove this community
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
            return this.service.deleteCommunity(this.getCommunityUuid(), args);
        },

        /**
         * Update this community
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        update : function(args) {
            return this.service.updateCommunity(this, args);
        },
        
        /**
         * Save this community
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        save : function(args) {
            if (this.getCommunityUuid()) {
                return this.service.updateCommunity(this, args);
            } else {
                return this.service.createCommunity(this, args);
            }
        }
        
    });

    /**
     * Member class represents an entry for a Member feed returned by the
     * Connections REST API.
     * 
     * @class Member
     * @namespace sbt.connections
     */
    var Member = declare(BaseEntity, {

        /**
         * The UUID of the community associated with this Member
         */
        communityUuid : null,

        /**
         * Constructor for Member entity
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
            if (!this.getRole()) {
                this.setRole(consts.Member);
            }
        },

        /**
         * Return the community UUID.
         * 
         * @method getCommunityUuid
         * @return {String} communityUuid
         */
        getCommunityUuid : function() {
            return this.communityUuid;
        },

        /**
         * Return the community member name.
         * 
         * @method getName
         * @return {String} Community member name
         */

        getName : function() {
            return this.getAsString("name");
        },

        /**
         * Set the community member name.
         * 
         * @method setName
         * @param {String} Community member name
         */

        setName : function(name) {
            return this.setAsString("name", name);
        },

        /**
         * Return the community member userId.
         * 
         * @method getUserid
         * @return {String} Community member userId
         */
        getUserid : function() {
            return this.getAsString("userid");
        },

        /**
         * Set the community member userId.
         * 
         * @method getId
         * @return {String} Community member userId
         */
        setUserid : function(userid) {
            return this.setAsString("userid", userid);
        },

        /**
         * Return the community member email.
         * 
         * @method getName
         * @return {String} Community member email
         */
        getEmail : function() {
            return this.getAsString("email");
        },

        /**
         * Return the community member email.
         * 
         * @method getName
         * @return {String} Community member email
         */

        setEmail : function(email) {
            return this.setAsString("email", email);
        },

        /**
         * Return the value of community member role from community member ATOM
         * entry document.
         * 
         * @method getRole
         * @return {String} Community member role
         */
        getRole : function() {
            return this.getAsString("role");
        },

        /**
         * Sets role of a community member
         * 
         * @method setRole
         * @param {String} role Role of the community member.
         */
        setRole : function(role) {
            return this.setAsString("role", role);
        },
        
        /**
         * Loads the member object with the atom entry associated with the
         * member. By default, a network call is made to load the atom entry
         * document in the member object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var communityUuid = this.communityUuid;
            var promise = this.service._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.setDataHandler(new XmlDataHandler({
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.MemberXPath
                    }));
                    self.id = self.dataHandler.getEntityId();
                    return self;
                }
            };

            var requestArgs = {
                communityUuid : communityUuid
            };
            var memberId = null;
            if (this.getUserid()) {
                memberId = requestArgs.userid = this.getUserid();
            } else {
                memberId = requestArgs.email = this.getEmail();
            }
            requestArgs = lang.mixin(requestArgs, args || {});
            var options = {
                handleAs : "text", 
                query : requestArgs
            };

            return this.service.getEntity(consts.AtomCommunityMembers, options, memberId, callbacks);
        },
        
        /**
         * Remove this member from the community.
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
        	var memberId = this.getUserid() || this.getEmail();
            return this.service.removeMember(this.getCommunityUuid(), memberId, args);
        }

    });

    /**
     * Invite class represents an entry for a Invite feed returned by the
     * Connections REST API.
     * 
     * @class Invite
     * @namespace sbt.connections
     */
    var Invite = declare(BaseEntity, {

        /**
         * The UUID of the community associated with this Invite
         */
        communityUuid : null,

        /**
         * The userid of the invitee
         */
        userid : null,
        
        /**
         * The UUID if the invitee associated with this Invite
         */
        inviteeUuid : null,

        /**
         * Constructor for Invite
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
            this.inherited(arguments, [ args ]);
        },

        /**
         * Return the value of IBM Connections invite ID from invite ATOM
         * entry document.
         * 
         * @method getInviteUuid
         * @return {String} Invite ID of the invite
         */
        getInviteUuid : function() {
            var inviteUuid = this.getAsString("inviteUuid");
            return extractInviteUuid(inviteUuid);
        },

        /**
         * Sets id of IBM Connections invite.
         * 
         * @method setInviteUuid
         * @param {String} inviteUuid Id of the invite
         */
        setInviteUuid : function(inviteUuid) {
            return this.setAsString("inviteUuid", inviteUuid);
        },

        /**
         * Set the community UUID.
         * 
         * @method setCommunityUuid
         * @return {String} communityUuid
         */
        setCommunityUuid : function(communityUuid) {
			this.communityUuid = communityUuid;
			return this;
        },
        
        /**
         * Return the community UUID.
         * 
         * @method getCommunityUuid
         * @return {String} communityUuid
         */
        getCommunityUuid : function() {
        	if (!this.communityUuid) {
				this.communityUuid = this.service.getUrlParameter(this.getAsString("communityUrl"), "communityUuid");
			} 
			return this.communityUuid;
        },
        
        /**
         * Set the invitee UUID.
         * 
         * @method setInviteeUuid
         * @return {String} inviteeUuid
         */
        setInviteeUuid : function(inviteeUuid) {
			this.inviteeUuid = inviteeUuid;
			return this;
        },
        
        /**
         * Return the value of IBM Connections invitee ID from invite ATOM
         * entry document.
         * 
         * @method getInviteeUuid
         * @return {String} Invitee ID of the invite
         */
        getInviteeUuid : function() {
        	if (!this.inviteeUuid) {
            	var inviteUuid = this.getInviteUuid();
            	this.inviteeUuid = extractInviteeUuid(inviteUuid, this.getCommunityUuid());
        	}
        	return this.inviteeUuid;
        },

        /**
         * Set the user id of the invitee.
         * 
         * @method setUserid
         * @return {String} userid
         */
        setUserid : function(userid) {
			this.userid = userid;
			return this;
        },
        
        /**
         * Return the user id of the invitee.
         * 
         * @method getUserid
         * @return {String} userid
         */
        getUserid : function() {
        	if (!this.userid && this.getContributor()) {
				this.userid = this.getContributor().userid || null;
			} 
			return this.userid;
        },
        
        /**
         * Set the email of the invitee.
         * 
         * @method setEmail
         * @return {String} email
         */
        setEmail : function(email) {
			this.email = email;
			return this;
        },
        
        /**
         * Return the email of the invitee.
         * 
         * @method getEmail
         * @return {String} email
         */
        getEmail : function() {
        	if (!this.email && this.getContributor()) {
				this.email = this.getContributor().email || null;
			} 
			return this.email;
        },
        
        /**
         * Return the id of the invite.
         * 
         * @method getId
         * @return {String} id
         */
        getId: function() {
    		return this.getAsString("uid");
    	},

        /**
         * Return the community invite title.
         * 
         * @method getTitle
         * @return {String} Community invite title
         */
        getTitle : function() {
            return this.getAsString("title");
        },

        /**
         * Set the community invite title.
         * 
         * @method setTitle
         * @param {String} Community invite title
         */
        setTitle : function(name) {
            return this.setAsString("title", name);
        },

        /**
         * Return the community invite content.
         * 
         * @method getId
         * @return {String} Community invite content
         */
        getContent : function() {
            return this.getAsString("content");
        },

        /**
         * Set the community invite content.
         * 
         * @method getId
         * @return {String} Community invite content
         */
        setContent : function(userid) {
            return this.setAsString("content", userid);
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
         * Gets an author of IBM Connections community invite.
         * 
         * @method getAuthor
         * @return {Object} Author of the community invite
         */
        getAuthor : function() {
            return this.getAsObject(
            		[ "authorUserid", "authorName" ],
            		[ "userid", "name" ]);
        },

        /**
         * Gets a contributor of IBM Connections community invite.
         * 
         * @method getContributor
         * @return {Object} Contributor of the community invite
         */
        getContributor : function() {
            return this.getAsObject(
            		[ "contributorUserid", "contributorName", "contributorEmail", "contributorUserState" ],
            		[ "userid", "name", "email", "userState" ]);
        },
        
        /**
         * Loads the invite object with the atom entry associated with the
         * invite. By default, a network call is made to load the atom entry
         * document in the invite object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var communityUuid = this.getCommunityUuid();
            var promise = this.service._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }
            var userid = this.getInviteeUuid();
            promise = this.service._validateUserid(userid);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.setDataHandler(new XmlDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.InviteXPath
                    }));
                    return self;
                }
            };

            var requestArgs = lang.mixin({
                communityUuid : communityUuid,
                userid : userid
            }, args || {});
            var options = {
                handleAs : "text",
                query : requestArgs
            };
            
            return this.service.getEntity(consts.AtomCommunityInvites, options, communityUuid + "-" + userid, callbacks);
        },

        /**
         * Remove this invite
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
            return this.service.removeInvite(this, args);
        },

        /**
         * Update this invite
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        update : function(args) {
            return this.service.updateInvite(this, args);
        },
        
        /**
         * Save this invite
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        save : function(args) {
            if (this.getInviteUuid()) {
                return this.service.updateInvite(this, args);
            } else {
                return this.service.createInvite(this, args);
            }
        }        

    });
    
    
    /**
     * Event class represents an entry for an Events feed returned by the Connections REST API.
     * 
     * @class Event
     * @namespace sbt.connections
     */
    var Event = declare(BaseEntity, {

        /**
         * Constructor for Event.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
            this.inherited(arguments, [ args ]);
        },

        /**
         * Return the community UUID.
         * 
         * @method getCommunityUuid
         * @return {String} communityUuid
         */
        getCommunityUuid : function() {
            return this.getAsString("communityUuid");
        },
        
        /**
         * Return the id of the event.
         * 
         * @method getId
         * @return {String} id
         */
        getId : function() {
            return this.getAsString("uid");
        },
        
        /**
         * The Uuid of the event. This is per event rather than per event instance. 
         * 
         * e.g. if an event spans multiple days it will have multiple instances, yet each even will have the same Uuid.
         * @method getEventUuid
         * @return {String} Uuid of the event.
         */
        getEventUuid : function(){
            return this.getAsString("eventUuid");
        },
        
        /**
         * The event instance uuid. This is per event instance, rather than per event. 
         * e.g. if an event spans multiple days each day will have its own eventInstUuid.
         * 
         * Can be used with the{{#crossLink "CommunityService/getEvent:method"}}{{/crossLink}} method to retrieve event instances.
         * @method getEventInstUuid
         * @return {String} Uuid of the event instance.
         */
        getEventInstUuid : function(){
            return this.getAsString("eventInstUuid");
        },

        /**
         * Return the community event title.
         * 
         * @method getTitle
         * @return {String} Community event title
         */

        getTitle : function() {
            return this.getAsString("title");
        },

        /**
         * Set the community event title.
         * 
         * @method setTitle
         * @param {String} Community event title
         */
        setTitle : function(name) {
            return this.setAsString("title", name);
        },

        /**
         * Return the community event summary.
         * 
         * @method getSummary
         * @return {String} Community event summary
         */
        getSummary : function() {
            return this.getAsString("summary");
        },

        /**
         * Set the community event summary.
         * 
         * @method setSummary
         * @return {String} Community event summary
         */
        setSummary : function(summary) {
            return this.setAsString("summary", summary);
        },
        
        /**
         * return the atom event url.
         * 
         * @returns
         */
        getEventAtomUrl : function(){
            return this.getAsString("eventAtomUrl");
        },
        
        /**
         * Get the full event description, with content.
         * @returns
         */
        getFullEvent : function(){
            return this.service.getEvent(this.getEventInstUuid());
        },
        
        /**
         * 
         * @returns
         */
        getContent : function(){
            return this.getAsString("content");
        },
        
        /**
         * 
         * @returns
         */
        getLocation : function(){
            return this.getAsString("location");
        },

        /**
         * Gets an author of IBM Connections community event.
         * 
         * @method getAuthor
         * @return {Object} Author of the community event
         */
        getAuthor : function() {
            if (!this._author) {
                this._author = {
                    userid : this.getAsString("authorUserid"),
                    name : this.getAsString("authorName"),
                    email : this.getAsString("authorEmail"),
                    authorState : this.getAsString("authorState")
                };
            }
            return this._author;
        },
        
        /**
         * Gets the recurrence information of the event.
         * 
         * Recurrence information object consists of:
         * frequency - 'daily' or 'weekly'
         * interval - Week interval. Value is int between 1 and 5.
         * until - The end date of the repeating event.
         * allDay - 1 if an all day event, 0 otherwise.
         * startDate - Start time of the event
         * endDate - End time of the event
         * byDay - Days of the week this event occurs, possible values are: SU,MO,TU,WE,TH,FR,SA
         * 
         * @method getRecurrence
         * @return {Object} An object containing the above recurrence information of the community event.
         */
        getRecurrence : function() {
            if (!this._recurrence) {
                this._recurrence = {
                    frequency : this.getAsString("frequency"),
                    interval : this.getAsString("interval"),
                    until : this.getAsString("until"),
                    allDay : this.getAsString("allDay"),
                    startDate : this.getAsString("startDate"),
                    endDate : this.getAsString("endDate"),
                    byDay : this.getAsString("byDay")
                };
            }
            return this._recurrence;
        },

        /**
         * Gets a contributor of IBM Connections community event.
         * 
         * @method getContributor
         * @return {Member} contributor Contributor of the community event
         */
        getContributor : function() {
            if (!this._contributor) {
                this._contributor = {
                    userid : this.getAsString("contributorUserid"),
                    name : this.getAsString("contributorName")
                };
            }
            return this._contributor;
        }

    });

    /*
     * Method used to extract the community uuid for an id url.
     */
    var extractCommunityUuid = function(service, uid) {
        if (uid && uid.indexOf("http") == 0) {
            return service.getUrlParameter(uid, "communityUuid");
        } else {
            return uid;
        }
    };
    
    /*
     * Method used to extract the invite uuid for an id url.
     */
    var extractInviteUuid = function(uid) {
    	if (uid && uid.indexOf("urn:lsid:ibm.com:communities:invite-") == 0) {
            return uid.substring("urn:lsid:ibm.com:communities:invite-".length);
        } else {
            return uid;
        }
    };
    
    /*
     * Method used to extract the invitee uuid for an id url.
     */
    var extractInviteeUuid = function(uid, communityUuid) {
    	if (uid && uid.indexOf(communityUuid) == 0) {
            return uid.substring(communityUuid.length + 1);
        } else {
            return uid;
        }
    };
    
    /*
     * Callbacks used when reading a feed that contains Community entries.
     */
    var ConnectionsCommunityFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new CommunityDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.CommunityFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new CommunityDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.CommunityXPath
            });
            return new Community({
                service : service,
                dataHandler : entryHandler
            });
        }
    };

    /*
     * Callbacks used when reading a feed that contains Member entries.
     */
    var ConnectionsMemberFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.CommunityFeedXPath
            });
        }
    };

    /*
     * Callbacks used when reading a feed that contains Invite entries.
     */
    var ConnectionsInviteFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.CommunityFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.InviteXPath
            });
            return new Invite({
                service : service,
                id : entryHandler.getEntityId(),
                dataHandler : entryHandler
            });
        }
    };
    
    /*
     * Callbacks used when reading a feed that contains Event entries.
     */
    var ConnectionsEventFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.CommunityFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.EventXPath
            });
            return new Event({
                service : service,
                id : entryHandler.getEntityId(),
                dataHandler : entryHandler
            });
        }
    };
    
    var ConnectionsEventCallbacks = {
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.EventXPath
            });
            return new Event({
                service : service,
                id : entryHandler.getEntityId(),
                dataHandler : entryHandler
            });
        }
    };

    /*
     * Callbacks used when reading an entry that contains a Community.
     */
    var ConnectionsCommunityCallbacks = {
        createEntity : function(service,data,response) {
            var entryHandler = new CommunityDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.CommunityXPath
            });
            return new Community({
                service : service,
                id : entryHandler.getEntityId(),
                dataHandler : entryHandler
            });
        }
    };
    
    /*
     * Callbacks used when reading an feed that contains community events.
     */
    var ConnectionsForumTopicFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.CommunityFeedXPath
            });
        },
        createEntity : function(service,data,response) {
        	var forumService = service.getForumService();
        	var forumTopic = forumService.newForumTopic({});
        	forumTopic.setData(data);
            return forumTopic;
        }
    };

    // TODO test all action methods work with args == undefined

    /**
     * CommunityService class.
     * 
     * @class CommunityService
     * @namespace sbt.connections
     */
    var CommunityService = declare(BaseService, {
    	
    	forumService : null,
    	
    	contextRootMap: {
            communities: "communities"
        },

        /**
         * Constructor for CommunityService
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        	if (!this.endpoint) {
                this.endpoint = config.findEndpoint(this.getDefaultEndpointName());
            }
        },

        /**
         * Return the default endpoint name if client did not specify one.
         * @returns {String}
         */
        getDefaultEndpointName : function() {
            return "connections";
        },
        
        /**
         * Return a ForumService instance
         * @returns {ForumService}
         */
        getForumService : function() {
        	if (!this.forumService) {
        		this.forumService = new ForumService();
        		this.forumService.endpoint = this.endpoint;
        	}
        	return this.forumService;
        },
        
        /**
         * Get the All Communities feed to see a list of all public communities to which the 
         * authenticated user has access or pass in parameters to search for communities that 
         * match a specific criteria.
         * 
         * @method getPublicCommunities
         * 
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of my communities. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getPublicCommunities : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomCommunitiesAll, options, this._getCommunityFeedCallbacks());
        },

        /**
         * Get the My Communities feed to see a list of the communities to which the 
         * authenticated user is a member or pass in parameters to search for a subset 
         * of those communities that match a specific criteria.
         * 
         * @method getMyCommunities
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of my communities. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getMyCommunities : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomCommunitiesMy, options, this._getCommunityFeedCallbacks());
        },

        /**
         * Retrieve the members feed to view a list of the members who belong 
         * to a given community.
         * 
         * @method getMembers
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of members of a
         * community. The parameters must be exactly as they are
         * supported by IBM Connections like ps, sortBy etc.
         */
        getMembers : function(communityUuid,args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin({
                communityUuid : communityUuid
            }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            var callbacks = this._getMemberFeedCallbacks(communityUuid);
            
            return this.getEntities(consts.AtomCommunityMembers, options, callbacks);
        },

        /**
         * Retrieve the member entry to view a member who belongs to a given community.
         * 
         * @method getMembers
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of members of a
         * community. The parameters must be exactly as they are
         * supported by IBM Connections like ps, sortBy etc.
         */
        getMember : function(communityUuid,memberId,args) {
            var member = new Member({
                service : this,
                communityUuid : communityUuid
            });
            
            if (this.isEmail(memberId)) {
                member.setEmail(memberId);
            } else {
                member.setUserid(memberId);
            }
            
            return member.load(args);
        },
        
        /**
         * Get the Events for a community. See {{#crossLink "CommunityConstants/AtomCommunityEvents:attribute"}}{{/crossLink}} for a complete listing of parameters.
         * 
         * These results do not include all details of the event, such as content. However summaries are available.
         * 
         * @param communityId The uuid of the Community.
         * @param startDate Include events that end after this date.
         * @param endDate Include events that end before this date.
         * @param args url parameters.
         * 
         * @returns
         */
        getCommunityEvents : function(communityUuid, startDate, endDate, args){
        	var promise = this._validateCommunityUuid(communityUuid) || this._validateDateTimes(startDate, endDate);
            if (promise) {
                return promise;
            }
            var requiredArgs = {
                calendarUuid : communityUuid
            };
            if(startDate){
                lang.mixin(requiredArgs, {
                    startDate : startDate
                });
            } 
            if(endDate){
                lang.mixin(requiredArgs, {
                    endDate : endDate
                });
            }
            
            args = lang.mixin(args, requiredArgs);
            
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
                
            return this.getEntities(consts.AtomCommunityEvents, options, this._getEventFeedCallbacks());
        },
        
        /**
         * Used to get the event with the given eventInstUuid. 
         * 
         * This will include all details of the event, including its content. 
         * 
         * @param eventInstUuid - The id of the event, also used as an identifier when caching the response
         * @returns
         */
        getEvent : function(eventInstUuid){
            var options = {
                method : "GET",
                handleAs : "text",
                query : {
                    eventInstUuid: eventInstUuid
                }
            };
                
            return this.getEntity(consts.AtomCommunityEvent, options, eventInstUuid, this._getEventCallbacks());
        },

        /**
         * Get a list of the outstanding community invitations of the currently authenticated 
         * user or provide parameters to search for a subset of those invitations.
         * 
         * @method getMyInvites
         * @param {Object} [args] 
         */
        getMyInvites : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomCommunityInvitesMy, options, this._getInviteFeedCallbacks());
        },      

        /**
         * Get a list of the outstanding community invitations for the specified community. 
         * The currently authenticated user must be an owner of the community.
         * 
         * @method getAllInvites
         * @param communityUuid
         * @param {Object} [args]
         */
        getAllInvites : function(communityUuid, args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin({
                communityUuid : communityUuid
            }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            
            return this.getEntities(consts.AtomCommunityInvites, options, this._getInviteFeedCallbacks());
        },      

        /**
         * Get a list of subcommunities associated with a community.
         * 
         * @method getSubCommunities
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of members of a
         * community. The parameters must be exactly as they are
         * supported by IBM Connections like ps, sortBy etc.
         */
        getSubCommunities : function(communityUuid,args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin({
                communityUuid : communityUuid
            }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            
            return this.getEntities(consts.AtomCommunitySubCommunities, options, this._getCommunityFeedCallbacks());
        },

        /**
         * Get a list for forum topics for th specified community.
         * 
         * @method getForumTopics
         * @param communityUuid
         * @param args
         * @returns
         */
        getForumTopics: function(communityUuid, args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin(
            	{ communityUuid : communityUuid }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            
            return this.getEntities(consts.AtomCommunityForumTopics, options, this._getForumTopicFeedCallbacks());
        },
        
        /**
         * Create a Community object with the specified data.
         * 
         * @method newCommunity
         * @param {Object} args Object containing the fields for the 
         * new Community 
         */
        newCommunity : function(args) {
            return this._toCommunity(args);
        },
        
        /**
         * Create a Member object with the specified data.
         * 
         * @method newMember
         * @param {Object} args Object containing the fields for the 
         * new Member 
         */
        newMember : function(args) {
            return this._toMember(args);
        },
        
        /**
         * Create a Invite object with the specified data.
         * 
         * @method newInvite
         * @param {String} communityUuid
         * @param {Object} args Object containing the fields for the 
         * new Invite 
         */
        newInvite : function(communityUuid,args) {
            return this._toInvite(communityUuid,args);
        },
        
        /**
         * Retrieve a community entry, use the edit link for the community entry 
         * which can be found in the my communities feed.
         * 
         * @method getCommunity
         * @param {String } communityUuid
         * @param {Object} args Object containing the query arguments to be 
         * sent (defined in IBM Connections Communities REST API) 
         */
        getCommunity : function(communityUuid, args) {
            var community = new Community({
                service : this,
                _fields : { communityUuid : communityUuid }
            });
            return community.load(args);
        },

        /**
         * Create a community by sending an Atom entry document containing the 
         * new community to the My Communities resource.
         * 
         * @method createCommunity
         * @param {Object} community Community object which denotes the community to be created.
         * @param {Object} [args] Argument object
         */
        createCommunity : function(communityOrJson,args) {
            var community = this._toCommunity(communityOrJson);
            var promise = this._validateCommunity(community, false, args);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                var communityUuid = this.getLocationParameter(response, "communityUuid");
                community.setCommunityUuid(communityUuid);
                return community;
            };

            var options = {
                method : "POST",
                query : args || {},
                headers : consts.AtomXmlHeaders,
                data : this._constructCommunityPostData(community)
            };
            
            return this.updateEntity(consts.AtomCommunitiesMy, options, callbacks, args);
        },

        /**
         * Update a community by sending a replacement community entry document in Atom format 
         * to the existing community's edit web address.
         * All existing community entry information will be replaced with the new data. To avoid 
         * deleting all existing data, retrieve any data you want to retain first, and send it back 
         * with this request. For example, if you want to add a new tag to a community entry, retrieve 
         * the existing tags, and send them all back with the new tag in the update request.
         * 
         * @method updateCommunity
         * @param {Object} community Community object
         * @param {Object} [args] Argument object
         */
        updateCommunity : function(communityOrJson,args) {
            var community = this._toCommunity(communityOrJson);
            var promise = this._validateCommunity(community, true, args);
            if (promise) {
                return promise;
            }
            
            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
            	// preserve the communityUuid
            	var communityUuid = community.getCommunityUuid();
            	if (data) {
            		var dataHandler = new CommunityDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.CommunityXPath
                    });
                	community.setDataHandler(dataHandler);
            	}
            	community.setCommunityUuid(communityUuid);
                return community;
            };

            var requestArgs = lang.mixin({
                communityUuid : community.getCommunityUuid()
            }, args || {});
            
            var options = {
                method : "PUT",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : this._constructCommunityPostData(community)
            };
            
            return this.updateEntity(consts.AtomCommunityInstance, options, callbacks, args);
        },

        /**
         * Delete a community, use the HTTP DELETE method.
         * Only the owner of a community can delete it. Deleted communities cannot be restored
         * 
         * @method deleteCommunity
         * @param {String/Object} community id of the community or the community object (of the community to be deleted)
         * @param {Object} [args] Argument object
         */
        deleteCommunity : function(communityUuid,args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }            
           
            var requestArgs = lang.mixin({
                communityUuid : communityUuid
            }, args || {});
            
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            
            return this.deleteEntity(consts.AtomCommunityInstance, options, communityUuid);
        },

        /**
         * Add member to a community
         * 
         * @method addMember
         * @param {String/Object} community id of the community or the community object.
         * @param {Object} member member object representing the member of the community to be added
         * @param {Object} [args] Argument object
         */
        addMember : function(communityUuid,memberOrId,args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }
            var member = this._toMember(memberOrId);
            promise = this._validateMember(member);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                var userid = this.getLocationParameter(response, "userid");
                member.setUserid(userid);
                member.communityUuid = communityUuid;
                return member;
            };

            var requestArgs = lang.mixin({
                communityUuid : communityUuid
            }, args || {});
            var options = {
                method : "POST",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : this._constructMemberPostData(member)
            };
            
            return this.updateEntity(consts.AtomCommunityMembers, options, callbacks, args);
        },
        
		/**
		 * Updates a member in the access control list for an application, sends a replacement member entry document in Atom format to the existing ACL
		 * node's edit web address.
		 * @method updateMember
		 * @param {String} activityUuid
		 * @param {Object} memberOrJson
		 */
		updateMember : function(communityUuid, memberOrJson, args) {
			var promise = this.validateField("communityUuid", communityUuid);
			if (promise) {
				return promise;
			}
			var member = this._toMember(memberOrJson);
			promise = this._validateMember(member, true, true);
			if (promise) {
				return promise;
			}

			var payload = this._constructMemberPostData(member);
			var requestArgs = {
	                communityUuid : communityUuid
	        };
	        var key = member.getEmail() ? "email" : "userid";
	        var value = member.getEmail() ? member.getEmail() : member.getUserid();
	        requestArgs[key] = value;
	        requestArgs = lang.mixin(requestArgs, args || {});

			var options = {
				method : "PUT",
				headers : consts.AtomXmlHeaders,
				query : requestArgs,
				data : payload
			};

			var callbacks = {
				createEntity : function(service, data, response) {
					return response;
				}
			};

			return this.updateEntity(consts.AtomCommunityMembers, options, callbacks);

		},

        /**
         * Remove member of a community
         * 
         * @method
         * @param {String/Object} community id of the community or the community object.
         * @param {String} memberId id of the member
         * @param {Object} [args] Argument object
         */
        removeMember : function(communityUuid,memberId,args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }
            var member = this._toMember(memberId);
            promise = this._validateMember(member);
            if (promise) {
                return promise;
            }

            var requestArgs = {
                communityUuid : communityUuid
            };
            var key = member.getEmail() ? "email" : "userid";
            var value = member.getEmail() ? member.getEmail() : member.getUserid();
            requestArgs[key] = value;
            requestArgs = lang.mixin(requestArgs, args || {});
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            
            return this.deleteEntity(consts.AtomCommunityMembers, options, value);
        },
        
        /**
         * Retrieve a community invite.
         * 
         * @method getInvite
         * @param {String} communityUuid
         * @param (String} userid
         */
        getInvite : function(communityUuid, userid) {
            var invite = new Invite({
                service : this,
                _fields : { communityUuid : communityUuid, userid : userid }
            });
            return invite.load();
        },

        /**
         * Create an invite to be a member of a community.
         * 
         * @method createInvite
         * @param {Object} inviteOrJson
         * @param {Object} [args] Argument object
         */
        createInvite: function(inviteOrJson, args) {
            var invite = this._toInvite(inviteOrJson);
            var promise = this._validateInvite(invite, true);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
            	var dataHandler = new XmlDataHandler({
            		data : data, 
            		namespaces : consts.Namespaces, 
            		xpath : consts.InviteXPath 
            	});
                invite.setDataHandler(dataHandler);
                return invite;
            };

            var requestArgs = lang.mixin({
                communityUuid : invite.getCommunityUuid()
            }, args || {});
            
            var options = {
                method : "POST",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : this._constructInvitePostData(invite)
            };
            
            return this.updateEntity(consts.AtomCommunityInvites, options, callbacks, args);
        },
           
        /**
         * Decline or revoke an invite to be a member of a community
         * 
         * @method removeInvite
         * @param {Object} inviteOrJson
         * @param {Object} [args] Argument object
         */
        removeInvite: function(inviteOrJson, args) {
            var invite = this._toInvite(inviteOrJson);
            var promise = this._validateInvite(invite, true);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin({
                communityUuid : invite.getCommunityUuid(),
                userid : invite.getInviteeUuid()
            }, args || {});
            
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            var entityId = invite.getCommunityUuid() + "-" + invite.getInviteeUuid();
            return this.deleteEntity(consts.AtomCommunityInvites, options, entityId);
        },
        
        /**
         * Accept an invite to be a member of a community.
         * 
         * @method acceptInvite
         * @param {Object} inviteOrJson.
         * @param {Object} [args] Argument object
         */
        acceptInvite: function(inviteOrJson, args) {
            var invite = this._toInvite(inviteOrJson);
            var promise = this._validateInvite(invite, true);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin({
                communityUuid : invite.getCommunityUuid()
            }, args || {});
            
            var options = {
            	method : "POST",
        		query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : this._constructInvitePostData(invite)
            };
            
            // return the community id for the community whose invite is accepted in the argument of the success promise.
            var callbacks = {}; 
            callbacks.createEntity = function(service,data,response) {                
                return invite;
            };
            
            return this.updateEntity(consts.AtomCommunityMembers, options, callbacks, args);
        },
        
        /**
         * Create a forum topc by sending an Atom entry document containing the 
         * new forum to the forum replies resource.
         * 
         * @method createForumTopic
         * @param {String} communityUuid Community UUID of the community for this forum topic
         * @param {Object} forumTopic Forum topic object which denotes the forum topic to be created.
         * @param {Object} [args] Argument object
         */
        createForumTopic : function(communityUuid, topicOrJson, args) {
            var promise = this._validateCommunityUuid(communityUuid);
            if (promise) {
                return promise;
            }
        	
        	var forumService = this.getForumService();
            var forumTopic = forumService.newForumTopic(topicOrJson);
            var promise = forumService._validateForumTopic(forumTopic, false, args);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                var topicUuid = this.getLocationParameter(response, "topicUuid");
                forumTopic.setTopicUuid(topicUuid);
                forumTopic.setData(data);
                return forumTopic;
            };

            var requestArgs = lang.mixin(
                	{ communityUuid : communityUuid }, args || {});
            
            var options = {
                method : "POST",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : forumTopic.createPostData()
            };
            
            return this.updateEntity(consts.AtomCommunityForumTopics, options, callbacks, args);
        },
                
        /**
		 * Updates the Logo picture of a community
		 * @method updateCommunityLogo
		 * @param {Object} fileControlOrId The Id of html control or the html control
		 * @param {String} communityUuid the Uuid of community
		 */
		updateCommunityLogo : function(fileControlOrId, communityUuid) {
			var promise = this.validateField("File Control Or Id", fileControlOrId);
			if (promise) {
				return promise;
			}
			promise = this.validateHTML5FileSupport();
			if (promise) {
				return promise;
			}
			promise = this.validateField("CommunityUuid", communityUuid);
			if (promise) {
				return promise;
			}

			var files = null;
			var fileControl = this.getFileControl(fileControlOrId);
			if(!fileControl){
				return this.createBadRequestPromise("File Control or ID is required");
			}
			filePath = fileControl.value;
			files = fileControl.files;

			if (files.length != 1) {
				return this.createBadRequestPromise("Only one file needs to be provided to this API");
			}

			var file = files[0];
			var formData = new FormData();
			formData.append("file", file);
			var requestArgs = {
				"communityUuid" : communityUuid
			};
			var url = this.constructUrl(consts.AtomUpdateCommunityLogo, null, {
				endpointName : this.endpoint.proxyPath,
				fileName : encodeURIComponent(file.name)
			});
			if (this.endpoint.proxy) {
                url = config.Properties.serviceUrl + url;
            } else {
            	return this.createBadRequestPromise("File Proxy is required to run this API");
            }
					
			var headers = {
				"Content-Type" : false,
				"Process-Data" : false //processData = false is reaquired by jquery
			};
			var options = {
				method : "PUT",
				headers : headers,
				query : requestArgs,
				data : formData
			};
			var callbacks = {
				createEntity : function(service, data, response) {
					return data; // Since this API does not return any response in case of success, returning empty data
				}
			};

			return this.updateEntity(url, options, callbacks);
		},

		//
		// Internals
		//
       
        /*
         * Callbacks used when reading a feed that contains Community entries.
         */
        _getCommunityFeedCallbacks: function() {
            return ConnectionsCommunityFeedCallbacks;
        },

        /*
         * Callbacks used when reading a feed that contains forum topic entries.
         */
        _getForumTopicFeedCallbacks: function() {
            return ConnectionsForumTopicFeedCallbacks;
        },

        /*
         * Callbacks used when reading a feed that contains Member entries.
         */
        _getMemberFeedCallbacks: function(communityUuid) {
            var self = this;
            return lang.mixin({
                createEntity : function(service,data,response) {
                    var entryHandler = new CommunityDataHandler({
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.MemberXPath
                    });
                    return new Member({
                        service : service,
                        id : entryHandler.getEntityId(),
                        communityUuid : communityUuid,
                        dataHandler : entryHandler
                    });
                }
            }, ConnectionsMemberFeedCallbacks);
        },

        /*
         * Callbacks used when reading a feed that contains Invite entries.
         */
        _getInviteFeedCallbacks: function() {
            return ConnectionsInviteFeedCallbacks;
        },
        
        /*
         * Callbacks used when reading a feed that contains Event entries.
         */
        _getEventFeedCallbacks: function() {
            return ConnectionsEventFeedCallbacks;
        },
        
        /*
         * Callbacks used when reading a entry that contains an Event.
         */
        _getEventCallbacks: function(){
            return ConnectionsEventCallbacks;
        },

        /*
         * Callbacks used when reading an entry that contains a Community.
         */
        getCommunityCallbacks: function() {
            return ConnectionsCommunityCallbacks;
        },
        
        /*
         * Return a Community instance from Community or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toCommunity : function(communityOrJsonOrString) {
            if (communityOrJsonOrString instanceof Community) {
                return communityOrJsonOrString;
            } else {
                if (lang.isString(communityOrJsonOrString)) {
                    communityOrJsonOrString = {
                        communityUuid : communityOrJsonOrString
                    };
                }
                return new Community({
                    service : this,
                    _fields : lang.mixin({}, communityOrJsonOrString)
                });
            }
        },

        /*
         * Return as Invite instance from Invite or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toInvite : function(inviteOrJsonOrString, args){
            if (inviteOrJsonOrString instanceof Invite) {
                return inviteOrJsonOrString;
            } else {
                if (lang.isString(inviteOrJsonOrString)) {
                	inviteOrJsonOrString = {
                        communityUuid : inviteOrJsonOrString
                    };
                }
                return new Invite({
                    service : this,
                    _fields : lang.mixin({}, inviteOrJsonOrString)
                });
            }
        },

        /*
         * Return a Community UUID from Community or communityUuid. Throws an
         * error if the argument was neither or is invalid.
         */
        _toCommunityUuid : function(communityOrUuid) {
            var communityUuid = null;
            if (communityOrUuid) {
                if (lang.isString(communityOrUuid)) {
                    communityUuid = communityOrUuid;
                } else if (communityOrUuid instanceof Community) {
                    communityUuid = communityOrUuid.getCommunityUuid();
                }
            }

            return communityUuid;
        },
        
        /*
         * Return a Community Member from Member or memberId. Throws an error if
         * the argument was neither or is invalid.
         */
        _toMember : function(idOrJson) {
            if (idOrJson) {
                if (idOrJson instanceof Member) {
                    return idOrJson;
                }
                var member = new Member({
                    service : this
                });
                if (lang.isString(idOrJson)) {
                    if (this.isEmail(idOrJson)) {
                        member.setEmail(idOrJson);
                    } else {
                        member.setUserid(idOrJson);
                    }
                } else {
                	if(idOrJson.id && !idOrJson.userid && !idOrJson.email){
                		this.isEmail(idOrJson.id) ? idOrJson.email = idOrJson.id : idOrJson.userid = idOrJson.id;
                		delete idOrJson.id;
                	}
                    member._fields = lang.mixin({}, idOrJson);
                }
                return member;
            }
        },

        /*
         * Validate a community UUID, and return a Promise if invalid.
         */
        _validateCommunityUuid : function(communityUuid) {
            if (!communityUuid || communityUuid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected communityUuid.");
            }
        },
        
        /*
         * Validate a userid, and return a Promise if invalid.
         */
        _validateUserid : function(userid) {
            if (!userid || userid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected userid.");
            }
        },
        
        /*
         * Validate that the date-time is not empty, return a promise if invalid
         */
        _validateDateTimes : function(startDate, endDate){
            if ((!startDate || startDate.length === 0) && (!endDate || endDate.length === 0)) {
                return this.createBadRequestPromise("Invalid date arguments, expected either a startDate, endDate or both as parameters.");
            }
        },

        /*
         * Validate contributor id
         */
        _validateContributorId : function(contributorId) {
        	if (!contributorId || contributorId.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected contributorId.");
            }
        },

        /*
         * Validate a community, and return a Promise if invalid.
         */
        _validateCommunity : function(community,checkUuid) {
            if (!community || !community.getTitle()) {
                return this.createBadRequestPromise("Invalid argument, community with title must be specified.");
            }
            if (checkUuid && !community.getCommunityUuid()) {
                return this.createBadRequestPromise("Invalid argument, community with UUID must be specified.");
            }
        },

        /*
         * Validate an invite, and return a Promise if invalid.
         */
        _validateInvite : function(invite, checkCommunityUuid) {
            if (!invite || (!invite.getEmail() && !invite.getUserid() && !invite.getInviteeUuid())) {
                return this.createBadRequestPromise("Invalid argument, invite with email or userid or invitee must be specified.");
            }
            if (checkCommunityUuid && !invite.getCommunityUuid()) {
                return this.createBadRequestPromise("Invalid argument, invite with community UUID must be specified.");
            }
        },

        /*
         * Validate a member, and return a Promise if invalid.
         */
        _validateMember : function(member,args) {
            if (!member || (!member.getUserid() && !member.getEmail())) {
                return this.createBadRequestPromise("Invalid argument, member with userid or email must be specified.");
            }
        },

        /*
         * Construct a post data for a Community
         */
        _constructCommunityPostData : function(community) {
            var transformer = function(value,key) {
                if (key == "getTags") {
                    var tags = value;
                    value = "";
                    for (var tag in tags) {
                        value += stringUtil.transform(CategoryTmpl, {
                            "tag" : tags[tag]
                        });
                    }
                } else if (key == "getCommunityType" && !value) {
                    value = consts.Restricted;
                } else if (key == "getCommunityUuid" && value) {
                    value = stringUtil.transform(CommunityUuidTmpl, { "communityUuid" : value });
                } else if (key == "getCommunityTheme" && value) {
                    value = stringUtil.transform(CommunityThemeTmpl, { "communityTheme" : value });
                }
                return value;
            };
            
            var postData = stringUtil.transform(CommunityTmpl, community, transformer, community);
            return stringUtil.trim(postData);
        },

        /*
         * Construct a post data for a Member
         */
        _constructMemberPostData : function(member) {
            var transformer = function(value,key) {
                if (key == "getEmail") {
                    if (value) {
                        value = stringUtil.transform(EmailTmpl, {
                            "email" : value
                        });
                    }
                }
                if (key == "getUserid") {
                    if (value) {
                        value = stringUtil.transform(UseridTmpl, {
                            "userid" : value
                        });
                    }
                }
                if(key == "getRole") {
                	value = member.getRole();
                }
                return value;
            };
            return stringUtil.transform(MemberTmpl, member, transformer, member);
        },
        
        /*
         * Construct post data for an invite
         */
        _constructInvitePostData: function(invite){
        	var transformer = function(value,key) {        		
        		var returnVal = null;
                if (key == "getTitle" && invite.getTitle()) {
                   	returnVal = stringUtil.transform(TitleTmpl, {
                           "title" : invite.getTitle()
                    });
                };
                if (key == "getContent" && invite.getContent()) {
                   	returnVal = stringUtil.transform(ContentTmpl, {
                           "content" : invite.getContent()
                    });
                };
                if (key == "getEmail" && invite.getEmail()) {
                   	returnVal = stringUtil.transform(EmailTmpl, {
                           "email" : invite.getEmail()
                    });
                };
                if (key == "getUserid" && invite.getUserid()) {
                   	returnVal = stringUtil.transform(UseridTmpl, {
                           "userid" : invite.getUserid()
                    });
                }
                return returnVal;
            };
            return stringUtil.transform(InviteTmpl, invite, transformer, null);
        }

    });
    return CommunityService;
});
},
'sbt/base/BaseService':function(){
/*
 * � Copyright IBM Corp. 2012, 2013
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
 * Javascript Base APIs for IBM Connections
 * 
 * @module sbt.base.BaseService
 * @author Carlos Manias
 */
define(["../config", "../declare", "../lang", "../log", "../stringUtil", "../Cache", "../Promise", "../util" ], 
    function(config, declare,lang,log,stringUtil,Cache,Promise, util) {
	// TODO sbt/config is required here to solve module loading
	// issues with jquery until we remove the global sbt object
	
    var BadRequest = 400;
    
    var requests = {};

    /**
     * BaseService class.
     * 
     * @class BaseService
     * @namespace sbt.base
     */
    var BaseService = declare(null, {
    	
        /**
         * The Endpoint associated with the service.
         */
        endpoint : null,

        /*
         * The Cache associated with the service.
         */
        _cache : null,
        
        /*
         * Regular expression used to remove // from url's
         */
        _regExp : new RegExp("/{2}"),
        
        /**
         * A map of default context roots to custom, if any. This will be implemented in subClasses of BaseService.
         */
        contextRootMap: {},

        /**
         * Constructor for BaseService
         * 
         * An endpoint is required so subclasses must check if one
         * was created here and if not set the default endpoint.
         * 
         * @constructor
         * @param {Object} args Arguments for this service.
         */
        constructor : function(args) {
            // create an endpoint if name was specified
            if (args && args.endpoint) {
                this.endpoint = config.findEndpoint(args.endpoint);
            }

            // optionally create a cache
            if (args && args.cacheSize) {
                this._cache = new Cache(args.cacheSize);
            }
        },

        /**
         * Construct a url using the specified parameters 
         * 
         * @method constructUrl
         * @param url Base part of the URL to construct
         * @param params Params to be encoded in the URL
         * @param urlParams Params to be encoded in the URL query
         * @returns The constructed URL
         */
        constructUrl : function(url,params,urlParams) {
            if (!url) {
                throw new Error("BaseService.constructUrl: Invalid argument, url is undefined or null.");
            }
            
            if(this.endpoint){
                lang.mixin(this.contextRootMap, this.endpoint.serviceMappings);
                
                url = stringUtil.transform(url, this.contextRootMap, function(value, key){
                    if(!value){
                        return key;
                    }
                    else{
                        return value;
                    }
                }, this);
            }
            
            if (urlParams) {
                url = stringUtil.replace(url, urlParams);
                
                if (url.indexOf("//") != -1) {
                	// handle empty values
                	url = url.replace(this._regExp, "/");
                }
            }
            if (params) {
                for (param in params) {
                    if (url.indexOf("?") == -1) {
                        url += "?";
                    } else if (url.indexOf("&") != (url.length - 1)) {
                        url += "&";
                    }
                    var value = encodeURIComponent(params[param]);
                    if (value) {
                        url += param + "=" + value;
                    }
                }
            }
            return url;
        },
        
        /**
         * Get a collection of entities.
         * 
         * @method getEntities
         * @param url The URL to get the entities.
         * @param options Optional. Options for the request.
         * @param callbacks Callbacks used to parse the response and create the entities.
         * @returns {sbt/Promise}
         */
        getEntities : function(url,options,callbacks) {
            url = this.constructUrl(url);
            var self = this;
            var promise = new Promise();
            this.request(url,options,null,promise).response.then(
                function(response) {
                    promise.response = response;
                    try {
	                    var feedHandler = callbacks.createEntities.apply(self, [ self, response.data, response ]);
	                    var entitiesArray = feedHandler.getEntitiesDataArray();
	                    var entities = [];
	                    for ( var i = 0; i < entitiesArray.length; i++) {
	                        var entity = callbacks.createEntity.apply(self, [ self, entitiesArray[i], response ]);
	                        entities.push(entity);
	                    }
	                    promise.summary = feedHandler.getSummary();
	                    promise.fulfilled(entities);
                    } catch (cause) {
                    	var error = new Error("Invalid response");
                    	error.cause = cause;
                    	promise.rejected(error);
                    }
                },
                function(error) {
                    promise.rejected(error);
                }
            );
            return promise;
        },

        /**
         * Get a single entity.
         * 
         * @method getEntity
         * @param url The URL to get the entity.
         * @param options Options for the request.
         * @param callbacks Callbacks used to parse the response and create the entity.
         * @returns {sbt/Promise}
         */
        getEntity : function(url,options,entityId,callbacks) {
            url = this.constructUrl(url);
            var promise = this._validateEntityId(entityId);
            if (promise) {
                return promise;
            }
            
            // check cache
            var promise = new Promise();
            var data = this.getFromCache(entityId);
            if (data) {
                promise.fulfilled(data);
                return promise;
            }

            var self = this;
            this.request(url,options,entityId,promise).response.then(
                function(response) {
                    promise.response = response;
                    try {
                        var entity = callbacks.createEntity.apply(self, [ self, response.data, response ]);
                        if (self._cache && entityId) {
                            self.fullFillOrRejectPromises.apply(self, [ entityId, entity, response ]);
                        } else {
                            promise.fulfilled(entity);
                        }
                    } catch (cause) {
                    	var error = new Error("Invalid response");
                    	error.cause = cause;
                        if (self._cache && entityId) {
                            self.fullFillOrRejectPromises.apply(self, [ entityId, error ]);
                        } else {
                            promise.rejected(error);
                        }
                    }
                },
                function(error) {
                    if (self._cache && entityId) {
                        self.fullFillOrRejectPromises.apply(self, [ entityId, error ]);
                    } else {
                        promise.rejected(error);
                    }
                }
            );
            return promise;
        },
        
        /**
         * Update the specified entity.
         * 
         * @method updateEntity
         * @param url The URL to update the entity.
         * @param options Options for the request.
         * @param callbacks Callbacks used to parse the response.
         * @param sbt/Promise
         */
        updateEntity : function(url, options, callbacks) {
            url = this.constructUrl(url);
            var self = this;
            var promise = new Promise();
            this.endpoint.request(url,options,null,promise).response.then(
                function(response) {
                    promise.response = response;
                    var entity = callbacks.createEntity.apply(self, [ self, response.data, response ]);
                    // callback can return a promise if an additional
                    // request is required to load the associated entity
                    if (entity instanceof Promise) {
                        entity.then(
                            function(response) {                                
                            	// it is the responsibility of the createEntity callback to clear the cache in this case.
                                promise.fulfilled(response);
                            },
                            function(error) {
                                promise.rejected(error);
                            }
                        );
                    } else {
                    	if(entity.id){
                    		self.removeFromCache(entity.id);
                    	}
                    	if(entity.id && entity.data){
                    		self.addToCache(entity.id, entity);
                    	}
                        promise.fulfilled(entity);
                    }
                },
                function(error) {
                    promise.rejected(error);
                }
            );
            return promise;
        },

        /**
         * Delete the specified entity.
         * 
         * @method deleteEntity
         * @param url The URL to delete the entity.
         * @param options Options for the request.
         * @param entityId Id of the entity to delete.
         * @param sbt/Promise
         */
        deleteEntity : function(url,options,entityId) {
            url = this.constructUrl(url);
            var promise = this._validateEntityId(entityId);
            if (promise) {
                return promise;
            }

            var self = this;
            var promise = new Promise();
            this.endpoint.request(url,options,entityId,promise).response.then(
                function(response) {
                    promise.response = response;
                    promise.fulfilled(entityId);
                    self.removeFromCache(entityId);
                },
                function(error) {
                    promise.rejected(error);
                }
            );
            return promise;
        },
        
        /**
         * Perform an XML HTTP Request with cache support.
         * 
         * @method request
         * @param url URL to request
         * @param options Options for the request.
         * @param entityId Id of the rntity associated with the request.
         * @param promise Promise being returned
         * @param sbt/Promise
         */
        request : function(url,options,entityId,promise) {
            url = this.constructUrl(url);
            if (this._cache && entityId) {
                this.pushPromise(entityId, promise);
            }
            return this.endpoint.request(url,options);
        },

        /**
         * Push set of promise onto stack for specified request id.
         * 
         * @method pushPromise
         * @param id Id of the request.
         * @param promise Promise to push.
         */
        pushPromise : function(id,promise) {
            log.debug("pushPromise, id : {0}, promise : {1}", id, promise);
            if (!requests[id]) {
                requests[id] = [];
            }
            requests[id].push(promise);
        },

        /**
         * Notify set of promises and pop from stack for specified request id.
         * 
         * @method fullFillOrRejectPromises
         * @param id
         * @param data
         * @param response
         */
        fullFillOrRejectPromises : function(id,data,response) {
            log.debug("fullFillOrRejectPromises, id : {0}, data : {1}, response : {2}", id, data, response);
            this.addToCache(id, data);
            var r = requests[id];
            if (r) {
                delete requests[id];
                for ( var i = 0; i < r.length; i++) {
                    var promise = r[i];
                    this.fullFillOrReject.apply(this, [ promise, data, response ]);
                }
            }
        },

        /**
         * Fullfill or reject specified promise.
         * 
         * @method fullFillOrReject
         * @param promise
         * @param data
         * @param response
         */
        fullFillOrReject : function(promise,data,response) {
            if (promise) {
                try {
                    promise.response = response;
                    if (data instanceof Error) {
                        promise.rejected(data);
                    } else {
                        promise.fulfilled(data);
                    }
                } catch (error) {
                    log.debug("fullFillOrReject: " + error.message);
                }
            }
        },

        /**
         * Add the specified data into the cache.
         * 
         * @method addToCache
         * @param id
         * @param data
         */
        addToCache : function(id, data) {
            if (this._cache && !(data instanceof Error)) {
                this._cache.put(id, data);
            }
        },
        
        /**
         * Remove the cached data for the specified id.
         * 
         * @method removeFromCache
         * @param id
         */
        removeFromCache : function(id) {
            if (this._cache) {
                this._cache.remove(id);
            }
        },
                
        /**
         * Get the cached data for the specified id.
         * 
         * @method getFromCache
         * @param id
         */
        getFromCache : function(id) {
            if (this._cache) {
                return this._cache.get(id);
            }
        },
                
        /**
         * Create a bad request Error.
         * 
         * @method createBadRequestError
         * @param message
         * @returns {Error}
         */
        createBadRequestError : function(message) {
            var error = new Error();
            error.code = BadRequest;
            error.message = message;
            return error;
        },
        
        /**
         * Create a bad request Promise.
         * 
         * @method createBadRequestPromise
         * @param message
         * @returns {sbt/Promise}
         */
        createBadRequestPromise : function(message) {
            return new Promise(this.createBadRequestError(message));
        },
        
        /**
         * Return true if the specified id is an email.
         * 
         * @method isEmail
         * @param id
         * @returns {Boolean}
         */
        isEmail : function(id) {
            return id && id.indexOf('@') >= 0;
        },
        
        /**
         * Extract the Location parameter from a URL.
         * 
         * @method getLocationParameter
         * @param ioArgs
         * @param name
         * @returns {String}
         */
        getLocationParameter: function (response, name) {
            var location = response.getHeader("Location") || undefined;
            if (location) {
                return this.getUrlParameter(location, name);
            }
        },
        
        /**
         * Extract the specified parameter from a URL.
         * 
         * @mehtod getUrlParameter
         * @param url
         * @param name
         * @returns {Boolean}
         */
        getUrlParameter : function (url, name) {
            return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(url)||[,""])[1].replace(/\+/g, '%20'))||null;
        },
        
        /**
         * Validate a string field, and return a Promise if invalid.
         * 
         * @param fieldName
         * @param fieldValue
         */
        validateField : function(fieldName, fieldValue) {
            if (!fieldValue) {
                var message = "Invalid value {0} for field {1}, the field must not be empty or undefined.";
                message = stringUtil.substitute(message, [ fieldValue || "'undefined'", fieldName ]);
                return this.createBadRequestPromise(message);
            }
        },
        
        /**
         * Validate a map of fields, and return a Promise for first invalid field found.
         * 
         * @param fieldMap
         */
        validateFields : function(fieldMap) {
            for(var name in fieldMap){
                var value = fieldMap[name];
                var promise = this.validateField(name, value);
                if (promise) {
                    return promise;
                }
            }
        },
        
        /**
         * Validate HTML5 File API Support for browser and JS Library	
         */
        validateHTML5FileSupport : function() {
        	if (!window.File || !window.FormData) {
				var message = "HTML 5 File API is not supported by the Browser.";
				return this.createBadRequestPromise(message);
			}
        	// Dojo 1.4.3 does not support HTML5 FormData
			if(util.getJavaScriptLibrary().indexOf("Dojo 1.4") != -1) {
				return this.createBadRequestPromise("Dojo 1.4.* is not supported for Update Profile Photo");
			}
        },
        
        /*
         * Validate the entityId and if invalid notify callbacks
         */
        _validateEntityId : function(entityId) {
            if (!entityId || !lang.isString(entityId)) {
                var message = "Invalid argument {0}, expected valid entity identifier.";
                message = stringUtil.substitute(message, [ entityId || "'undefined'" ]);
                return this.createBadRequestPromise(message);
            }
        }

    });
    return BaseService;
});

},
'sbt/Cache':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * The cache implements the Least Recently Used (LRU)Algorithm by doubly linked list. Every node has 4 variables - key, value, next and previous.
 * The key stores the key of the node and value stores the actual data for the key. The next and previous variables point to the 
 * next and previous nodes in the cache. 
 *  The cache has a head variable pointing to the top cache node and a tail variable pointing to the last cache node.
 *
 *   Head ->  A --->  B  --->  C ---> D  <--  Tail
 *              <---     <---   <---
 *
 * Suppose the cache has 4 entries and its max size limit is 4 (the cache is full right now). The structure of the cache would be as described by figure above.
 * The entries are listed as per their order of recent access. 
 * So when a new entry E is added to the cache, the new order of the cache entries would be EABC. D would be deleted from the cache.
 * 
 * @module sbt.Cache
 */
define(['./declare'],function(declare) {
	var Cache = declare(null, {	
		constructor: function(max) {
			this.limit = max;// This is the maximum limit of the cache.
			this._cache = {};//Variable to hold the items in the cache.
			this.head = null;// Pointer to the head of the cache
			this.tail = null;// Pointer to the tail of the cache
			this.count = 0;// Counter for number of items in the cache
		},
		get: function _cg(key) { //Retrieves a cached item.
			var k = this._cache[key];
			if(k){//Item found in the cache. Move the accessed node to the top of the cache.
				if(this.head == k){return k.value;} // the node is already at the top, no need to shift, just return the value.
				else{// shift the node to the top and return the value
					if(k.prev)k.prev.next = k.next;
					if(k.next){k.next.prev = k.prev;} else {this.tail=k.prev;}
					k.next  = this.head; 
					this.head.prev = k; 
					k.prev = null; 
					this.head = k; 
				}
				return k.value;
			}
			return null; // the node is not in the cache
		},
		put: function _cp(key,value) {// puts a node in the cache if the node is not present in the cache. The node is put at the top of the cache.
			if(this._cache[key])//remove the asked node
				{this.remove(key); this.count --;}
			
			var k = this._cache[key] ={key:key, value:value};
			if(this.count==this.limit) //if the cache is full, remove the last node
				{this.remove(this.tail.key);this.count --;}
	        //add the asked node to the top of the list.
			k.next = this.head;
		    if(k.next)k.next.prev = k;else this.tail = k;
		    this.head = k;
		    k.prev = null;
		    this.count ++;
		 },
		remove: function _cr(key) {//removes a node from the cache and updates the next and prev attributes of the surrounding nodes.
			var k = this._cache[key];
			if(k){
				if(k.next)k.next.prev = k.prev;else this.tail = k.prev;
				if(k.prev)k.prev.next = k.next; else this.head = k.next;
				k.next = k.prev = null;
				delete this._cache[key];
				this.count -- ;
			}
			
		},
		
		/**
		 * Function that browse the content of the cache and call a call back method for each entry.
		 * 
		 * @param callback the callback method to invoke for each entry
		 */
		browse: function(callback) {
			if(callback) {
				for(var i in this._cache) {
					var e = this._cache[i];
					var r = callback(e.key,e.value);
					if(r) {
						return r;
					}
				}
				return null;
			}
			return null;
		}
	});

	return Cache;
});
},
'sbt/base/BaseEntity':function(){
/*
 * � Copyright IBM Corp. 2012, 2013
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
 * Javascript Base APIs for IBM Connections
 * 
 * @module sbt.base.BaseEntity
 */
define([ "../declare", "../lang", "../log", "../stringUtil" ], 
    function(declare,lang,log,stringUtil) {

    var BadRequest = 400;
    
    var requests = {};

    /**
     * BaseEntity class
     * 
     * @class BaseEntity
     * @namespace sbt.base
     */
    var BaseEntity = declare(null, {

        /**
         * The identifier for this entity.
         */
        id : null,

        /**
         * The service associated with the entity.
         */
        service : null,

        /**
         * The DataHandler associated with this entity.
         */
        dataHandler : null,

        /**
         * The fields which have been updated in this entity.
         * 
         * @private
         */
        _fields : null,

        /**
         * Constructor for BaseEntity
         * 
         * @constructor
         * @param {Object} args Arguments for this entity.
         */
        constructor : function(args) {
            lang.mixin(this, args);
            
            if (!this._fields) {
                this._fields = {};
            }
            
            if (!this.service) {
                var msg = "Invalid BaseEntity, an associated service must be specified.";
                throw new Error(msg);
            }
        },
        
        /**
         * Called to set the entity DataHandler after the entity
         * was loaded. This will cause the existing fields to be cleared.
         * 
         * @param datahandler
         */
        setDataHandler : function(dataHandler) {
        	this._fields = {};
        	this.dataHandler = dataHandler;
        },
        
        /**
         * Called to set the entity data after the entity
         * was loaded. This will cause the existing fields to be cleared.
         * 
         * @param data
         */
        setData : function(data) {
        	this._fields = {};
        	this.dataHandler.setData(data);
        },
        
        /**
         * Return true if this entity has been loaded.
         *  
         * @returns true if this entity has been loaded
         */
        isLoaded : function() {
        	if (this.dataHandler) {
        		return this.dataHandler.getData() ? true : false;
        	}
            return false;
        },
        
        /**
         * Get the string value of the specified field.
         * 
         * @method getAsString
         * @param fieldName
         * @returns
         */
        getAsString : function(fieldName) {
            this._validateFieldName(fieldName, "getAsString");

            if (this._fields.hasOwnProperty(fieldName)) {
                return this._fields[fieldName];
            } else if (this.dataHandler) {
                return this.dataHandler.getAsString(fieldName);
            } else {
                return null;
            }
        },

        /**
         * Get the number value of the specified field.
         * 
         * @method getAsNumber
         * @param fieldName
         * @returns
         */
        getAsNumber : function(fieldName) {
            this._validateFieldName(fieldName, "getAsNumber");

            if (this._fields.hasOwnProperty(fieldName)) {
                return this._fields[fieldName];
            } else if (this.dataHandler) {
                return this.dataHandler.getAsNumber(fieldName);
            } else {
                return null;
            }
        },

        /**
         * Get the date value of the specified field.
         * 
         * @method getAsDate
         * @param fieldName
         * @returns
         */
        getAsDate : function(fieldName) {
            this._validateFieldName(fieldName, "getAsDate");

            if (this._fields.hasOwnProperty(fieldName)) {
                return this._fields[fieldName];
            } else if (this.dataHandler) {
                return this.dataHandler.getAsDate(fieldName);
            } else {
                return null;
            }
        },

        /**
         * Get the boolean value of the specified field.
         * 
         * @method getAsBoolean
         * @param fieldName
         * @returns
         */
        getAsBoolean : function(fieldName) {
            this._validateFieldName(fieldName, "getAsBoolean");

            if (this._fields.hasOwnProperty(fieldName)) {
                return this._fields[fieldName];
            } else if (this.dataHandler) {
                return this.dataHandler.getAsBoolean(fieldName);
            } else {
                return false;
            }
        },

        /**
         * Get the array value of the specified field.
         * 
         * @method getAsArray
         * @param fieldName
         * @returns
         */
        getAsArray : function(fieldName) {
            this._validateFieldName(fieldName, "getAsArray");

            if (this._fields.hasOwnProperty(fieldName)) {
                return this._fields[fieldName];
            } else if (this.dataHandler) {
                return this.dataHandler.getAsArray(fieldName);
            } else {
                return null;
            }
        },

		 /**
         * Get the nodes array value of the specified field.
         * 
         * @method getAsNodesArray
         * @param fieldName
         * @returns
         */
        getAsNodesArray : function(fieldName) {
            this._validateFieldName(fieldName, "getAsNodesArray");

            if (this._fields.hasOwnProperty(fieldName)) {
                return this._fields[fieldName];
            } else if (this.dataHandler) {
                return this.dataHandler.getAsNodesArray(fieldName);
            } else {
                return null;
            }
        },
        /**
         * Get an object containing the values of the specified fields.
         * 
         * @method getAsObject
         * @param fieldNames
         * @returns
         */
        getAsObject : function(fieldNames, objectNames) {
            var obj = {};
            for (var i=0; i<fieldNames.length; i++) {
                this._validateFieldName(fieldNames[i], "getAsObject");
                var fieldValue = this.getAsString(fieldNames[i]);
                if (fieldValue) {
                    if (objectNames) {
                    	obj[objectNames[i]] = fieldValue;
                    } else {
                    	obj[fieldNames[i]] = fieldValue;
                    }
                }
            }
            return obj;
        },

        /**
         * @method setAsString
         * @param data
         * @returns
         */
        setAsString : function(fieldName,string) {
            this._validateFieldName(fieldName, "setAsString", string);

            if (string === null || string === undefined) {
                delete this._fields[fieldName];
            } else {
                this._fields[fieldName] = string.toString();
            }
            return this;
        },

        /**
         * @method setAsNumber
         * @returns
         */
        setAsNumber : function(fieldName,numberOrString) {
            this._validateFieldName(fieldName, "setAsNumber", numberOrString);

            if (numberOrString instanceof Number) {
                this._fields[fieldName] = numberOrString;
            } else {
                if (numberOrString) {
                    var n = new Number(numberOrString);
                    if (isNaN(n)) {
                        var msg = stringUtil.substitute("Invalid argument for BaseService.setAsNumber {0},{1}", [ fieldName, numberOrString ]);
                        throw new Error(msg);
                    } else {
                        this._fields[fieldName] = n;
                    }
                } else {
                    delete this._fields[fieldName];
                }
            }
            return this;
        },

        /**
         * @method setAsDate
         * @returns
         */
        setAsDate : function(fieldName,dateOrString) {
            this._validateFieldName(fieldName, "setAsDate", dateOrString);

            if (dateOrString instanceof Date) {
                this._fields[fieldName] = dateOrString;
            } else {
                if (dateOrString) {
                    var d = new Date(dateOrString);
                    if (isNaN(d.getDate())) {
                        var msg = stringUtil.substitute("Invalid argument for BaseService.setAsDate {0},{1}", [ fieldName, dateOrString ]);
                        throw new Error(msg);
                    } else {
                        this._fields[fieldName] = d;
                    }
                } else {
                    delete this._fields[fieldName];
                }
            }
            return this;
        },

        /**
         * @method setAsBoolean
         * @returns
         */
        setAsBoolean : function(fieldName,booleanOrString) {
            this._validateFieldName(fieldName, "setAsBoolean", booleanOrString);

            if (booleanOrString != null) {
                this._fields[fieldName] = booleanOrString ? true : false;
            } else {
                delete this._fields[fieldName];
            }
            return this;
        },

        /**
         * @method setAsArray
         * @returns
         */
        setAsArray : function(fieldName,arrayOrString) {
            this._validateFieldName(fieldName, "setAsArray", arrayOrString);

            if (lang.isArray(arrayOrString)) {
                this._fields[fieldName] = arrayOrString.slice(0);
            } else if (lang.isString(arrayOrString)) {
                if (arrayOrString.length > 0) {
                    this._fields[fieldName] = arrayOrString.split(/[ ,]+/);
                } else {
                    this._fields[fieldName] = [];
                }
            } else {
                if (arrayOrString) {
                    this._fields[fieldName] = [ arrayOrString ];
                } else {
                    delete this._fields[fieldName];
                }
            }

            return this;
        },

        /**
         * Set an object containing the values of the specified fields.
         * 
         * @method setAsObject
         * @param theObject
         * @returns
         */
        setAsObject : function(theObject) {
            for (var property in theObject) {
                if (theObject.hasOwnProperty(property)) {
                    var value = theObject[property];
                    if (value) {
                        this._fields[property] = value;
                    } else {
                        delete this._fields[property];
                    }
                }
            }
        },

        /**
         * Remove the value of the specified field.
         * 
         * @method remove
         * @param fieldName
         */
        remove : function(fieldName) {
            delete this._fields[fieldName];
        },
        
        /**
         * Return the json representation of the entity
         * 
         * @method toJson
         * @returns {Object}
         */
        toJson : function() {
        	return (this.dataHandler) ? this.dataHandler.toJson() : {};
        },

        /*
         * Validate there is a valid field name
         */
        _validateFieldName : function(fieldName, method, value) {
            if (!fieldName) {
                var msg = stringUtil.substitute("Invalid argument for BaseService.{1} {0},{2}", [ fieldName, method, value || "" ]);
                throw new Error(msg);
            }
        }
    });

    return BaseEntity;
});

},
'sbt/base/XmlDataHandler':function(){
/*
 * � Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Helpers for the base capabilities of data
 * handlers.
 * 
 * @module sbt.base.DataHandler
 */
define([ "../declare", "../lang", "../stringUtil", "../xml", "../xpath", "./DataHandler" ], 
	function(declare,lang,stringUtil,xml,xpath,DataHandler) {

    /**
     * XmlDataHandler class
     * 
     * @class XmlDataHandler
     * @namespace sbt.base
     */
    var XmlDataHandler = declare(DataHandler, {

        /**
         * Data type for this DataHandler is 'xml'
         */
        dataType : "xml",

        /**
         * Set of XPath expressions used by this handler. Required for entity:
         * uid, entry Required for summary: totalResults, startIndex,
         * itemsPerPage
         */
        xpath : null,

        /**
         * Set of namespaces used by this handler.
         */
        namespaces : null,

        /**
         * Set of values that have already been read.
         */
        _values : null,

        /**
         * Summary of a feed.
         */
        _summary : null,

        /**
         * @constructor
         * @param {Object}
         *            args Arguments for this data handler.
         */
        constructor : function(args) {
            lang.mixin(this, args);

            this._values = {}; // TODO option to disable cache
            this.data = this._fromNodeOrString(args.data);
        },

        /**
         * Called to set the handler data.
         * 
         * @param data
         */
        setData : function(data) {
            this._values = {}; // TODO option to disable cache
            this.data = this._fromNodeOrString(data);
        },        
        
        /**
         * @method getAsString
         * @param property
         * @returns
         */
        getAsString : function(property) {
            this._validateProperty(property, "getAsString");
            
            if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._selectText(property);
                }
                return this._values[property];
            } else {
                return _selectText(property);
            }
        },

        /**
         * @method getAsNumber
         * @param property
         * @returns
         */
        getAsNumber : function(property) {
            this._validateProperty(property, "getAsNumber");
            
            if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._selectNumber(property);
                }
                return this._values[property];    
            } else {
                return this._selectNumber(property);
            }
        },

        /**
         * @method getAsDate
         * @param property
         * @returns
         */
        getAsDate : function(property) {
            this._validateProperty(property, "getAsDate");
            
            if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._selectDate(property);
                }
                return this._values[property];    
            } else {
                return this._selectDate(property);
            }
        },

        /**
         * @method getAsBoolean
         * @param property
         * @returns
         */
        getAsBoolean : function(property) {
            this._validateProperty(property, "getAsBoolean");
            
            if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._selectBoolean(property);
                }
                return this._values[property];    
            } else {
                return this._selectBoolean(property);
            }
        },

        /**
         * @method getAsArray
         * @param property
         * @returns
         */
        getAsArray : function(property) {
            this._validateProperty(property, "getAsArray");
            
            if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._selectArray(property);
                }
                return this._values[property];    
            } else {
                return this._selectArray(property);
            }
        },
        
        /**
         * @method getNodesArray
         * @param property
         * @returns
         */
        getAsNodesArray : function(property) {
            this._validateProperty(property, "getNodesArray");
            
            if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._selectNodesArray(property);
                }
                return this._values[property];    
            } else {
                return this._selectNodesArray(property);
            }
        },

        /**
         * @method getEntityId
         * @returns
         */
        getEntityId : function() {
            return stringUtil.trim(this.getAsString("uid"));
        },

        /**
         * getEntityData
         * 
         * @returns
         */
        getEntityData : function(document) {
            var entry = this.xpath["entry"];
            if (!entry) {
                return document;
            }
            if (!this._values["entry"]) {
                var nodes = xpath.selectNodes(document, entry, this.namespaces);
                this._values["entry"] = nodes[0] || [];
            }
            return this._values["entry"];
        },

        /**
         * @method getSummary
         * @returns
         */
        getSummary : function() {
            if (!this._summary && this._getXPath("totalResults")) {
                this._summary = {
                    totalResults : xpath.selectNumber(this.data, this._getXPath("totalResults"), this.namespaces),
                    startIndex : xpath.selectNumber(this.data, this._getXPath("startIndex"), this.namespaces),
                    itemsPerPage : xpath.selectNumber(this.data, this._getXPath("itemsPerPage"), this.namespaces)
                };
            }
            return this._summary;
        },

        /**
         * @method getEntitiesDataArray
         * @returns {Array}
         */
        getEntitiesDataArray : function() {
            var entries = this.xpath["entries"];
            if (!entries) {
                return this.data;
            }
            if (!this._values["entries"]) {
                this._values["entries"] = xpath.selectNodes(this.data, entries, this.namespaces);
            }
            return this._values["entries"];
        },
        
        /**
         * @method toJson
         * @returns {Object}
         */
        toJson : function() {
        	var jsonObj = {};
        	
        	for (var name in this.xpath) {
                if (this.xpath.hasOwnProperty(name)) {
                    jsonObj[name] = this.getAsString(name);
                }
            }
        	
        	return jsonObj;
        },        

        /*
         * Convert the input to a node by parsing as string and using
         * getEntityData, if not already one
         */
        _fromNodeOrString : function(nodeOrString) {
            if (lang.isString(nodeOrString)) {
                nodeOrString = stringUtil.trim(nodeOrString);
                var document = xml.parse(nodeOrString);
                return this.getEntityData(document);
            }
            return nodeOrString;
        },

        /*
         * Return xpath expression from the set or the property itself (assume
         * it's already xpath)
         */
        _getXPath : function(property) {
            return this.xpath[property] || property;
        },
        
        /*
         * Validate that the property is valid
         */
        _validateProperty : function(property, method) {
            if (!property) {
                var msg = stringUtil.substitute("Invalid argument for XmlDataHandler.{1} {0}", [ property, method ]);
                throw new Error(msg);
            }
        },
        
        /*
         * Select xpath as string
         */
        _selectText : function(property) {
        	if (!this.data) {
        		return null;
        	}
            return stringUtil.trim(xpath.selectText(this.data, this._getXPath(property), this.namespaces));
        },
        
        /*
         * Select xpath as number
         */
        _selectNumber : function(property) {
        	if (!this.data) {
        		return null;
        	}
            return xpath.selectNumber(this.data, this._getXPath(property), this.namespaces);
        },
        
        /*
         * Select xpath as date
         */
        _selectDate : function(property) {
        	if (!this.data) {
        		return null;
        	}
            var text = this._selectText(property);
            return text ? new Date(text) : null;
        },
        
        /*
         * Select xpath as boolean
         */
        _selectBoolean : function(property) {
        	if (!this.data) {
        		return false;
        	}
            var nodes = xpath.selectNodes(this.data, this._getXPath(property), this.namespaces);
            var ret = false;
            if (nodes) {
           		ret = (nodes.length > 0);
            }
            return ret;
        },
        
        /*
         * Select xpath as array
         */
        _selectArray : function(property) {
        	if (!this.data) {
        		return null;
        	}
            var nodes = xpath.selectNodes(this.data, this._getXPath(property), this.namespaces);
            var ret = null;
            if (nodes) {
                ret = [];
                for ( var i = 0; i < nodes.length; i++) {
                    ret.push(stringUtil.trim(nodes[i].text || nodes[i].textContent));
                }
            }
            return ret;
        },
        
        /*
         * Select xpath as nodes array
         */
        _selectNodesArray : function(property) {
        	if (!this.data) {
        		return null;
        	}
            return xpath.selectNodes(this.data, this._getXPath(property), this.namespaces);            
        }
       

    });
    return XmlDataHandler;
});
},
'sbt/base/DataHandler':function(){
/*
 * � Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Helpers for the base capabilities of data
 * handlers.
 * 
 * @module sbt.base.DataHandler
 */
define([ "../declare", "../lang" ], function(declare,lang) {

    /**
     * DataHandler class
     * 
     * @class DataHandler
     * @namespace sbt.base
     */
    var DataHandler = declare(null, {

        /**
         * Data type for this DataHandler
         */
        dataType : null,

        /**
         * Data for this DataHandler
         */
        data : null,

        /**
         * @constructor
         * @param {Object}
         *            args Arguments for this data handler.
         */
        constructor : function(args) {
            lang.mixin(this, args);
        },

        /**
         * Called to set the handler data.
         * 
         * @param data
         */
        setData : function(data) {
        	this.data = data;
        },
        
        /**
         * Called to get the handler data.
         * 
         * @param data
         */
        getData : function() {
        	return this.data;
        },
        
        /**
         * @method getAsString
         * @param data
         * @returns
         */
        getAsString : function(property) {
            return null;
        },

        /**
         * @method getAsNumber
         * @returns
         */
        getAsNumber : function(property) {
            return null;
        },

        /**
         * @method getAsDate
         * @returns
         */
        getAsDate : function(property) {
            return null;
        },

        /**
         * @method getAsBoolean
         * @returns
         */
        getAsBoolean : function(property) {
            return null;
        },

        /**
         * @method getAsArray
         * @returns
         */
        getAsArray : function(property) {
            return null;
        },

        /**
         * @method getEntityId
         * @returns
         */
        getEntityId : function(data) {
            return null;
        },

        /**
         * @param parent
         * @returns
         */
        getEntityData : function(parent) {
            return data;
        },

        /**
         * @method getSummary
         * @returns
         */
        getSummary : function() {
            return null;
        },

        /**
         * @method getEntitiesDataArray
         * @returns {Array}
         */
        getEntitiesDataArray : function() {
            return [];
        }, 
        
        /**
         * @method toJso
         * @returns {Object}
         */
        toJson : function() {
        }

    });
    return DataHandler;
});
},
'sbt/connections/ForumService':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * The Forums application of IBM� Connections enables a team to discuss issues that are pertinent to their work. 
 * The Forums API allows application programs to create new forums, and to read and modify existing forums.
 * 
 * @module sbt.connections.ForumService
 */
define([ "../declare", "../config", "../lang", "../stringUtil", "../Promise", "./ForumConstants", "../base/BaseService", "../base/AtomEntity", "../base/XmlDataHandler" ], 
    function(declare,config,lang,stringUtil,Promise,consts,BaseService,AtomEntity,XmlDataHandler) {
	
	var CategoryForum = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"forum-forum\"></category>";
	var CategoryTopic = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"forum-topic\"></category>";
	var CategoryReply = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"forum-reply\"></category>";
	var CategoryRecommendation = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"recommendation\"></category>";
    
	var CommunityTmpl = "<snx:communityUuid xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">${getCommunityUuid}</snx:communityUuid>";
	var TopicTmpl = "<thr:in-reply-to xmlns:thr=\"http://purl.org/syndication/thread/1.0\" ref=\"urn:lsid:ibm.com:forum:${getForumUuid}\" type=\"application/atom+xml\" href=\"\"></thr:in-reply-to>";
	var ReplyTmpl = "<thr:in-reply-to xmlns:thr=\"http://purl.org/syndication/thread/1.0\" ref=\"urn:lsid:ibm.com:forum:${getTopicUuid}\" type=\"application/atom+xml\" href=\"\"></thr:in-reply-to>";
	var FlagTmpl = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/flags\" term=\"${flag}\"></category>";
	
    /**
     * Forum class represents an entry from a Forums feed returned by the
     * Connections REST API.
     * 
     * @class Forum
     * @namespace sbt.connections
     */
    var Forum = declare(AtomEntity, {
    	
    	xpath : consts.ForumXPath,
    	contentType : "html",
    	categoryScheme : CategoryForum,
    	
        /**
         * Construct a Forum entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return extra entry data to be included in post data for this entity.
         * 
         * @returns {String}
         */
        createEntryData : function() {
        	if (!this.getCommunityUuid()) {
        		return "";
        	}
            var transformer = function(value,key) {
                return value;
            };
            var postData = stringUtil.transform(CommunityTmpl, this, transformer, this);
            return stringUtil.trim(postData);
        },

        /**
         * Return the value of id from Forum ATOM
         * entry document.
         * 
         * @method getForumUuid
         * @return {String} ID of the Forum
         */
        getForumUuid : function() {
            var uid = this.getAsString("forumUuid");
            return extractForumUuid(uid);
        },

        /**
         * Sets id of IBM Connections forum.
         * 
         * @method setForumUuid
         * @param {String} forumUuid Id of the forum
         */
        setForumUuid : function(forumUuid) {
            return this.setAsString("forumUuid", forumUuid);
        },

        /**
         * Return the value of communityUuid from Forum ATOM
         * entry document.
         * 
         * @method getCommunityUuid
         * @return {String} Uuid of the Community
         */
        getCommunityUuid : function() {
            return this.getAsString("communityUuid");
        },

        /**
         * Sets communityUuid of IBM Connections forum.
         * 
         * @method setCommunityUuid
         * @param {String} communityUuid Community Uuid of the forum
         */
        setCommunityUuid : function(communityUuid) {
            return this.setAsString("communityUuid", communityUuid);
        },

        /**
         * Return the moderation of the IBM Connections forum from
         * forum ATOM entry document.
         * 
         * @method getModeration
         * @return {String} Moderation of the forum
         */
        getModeration : function() {
            return this.getAsDate("moderation");
        },
        
        /**
         * Return the thread count of the IBM Connections forum from
         * forum ATOM entry document.
         * 
         * @method getThreadCount
         * @return {Number} Thread count of the forum
         */
        getThreadCount : function() {
            return this.getAsNumber("threadCount");
        },
        
        /**
         * Return the url of the IBM Connections forum from
         * forum ATOM entry document.
         * 
         * @method getForumUrl
         * @return {String} Url of the forum
         */
        getForumUrl : function() {
            return this.getAlternateUrl();
        },
                
        /**
         * Get a list for forum topics that includes the topics in the specified forum.
         * 
         * @method getTopics
         * @param {Object} args
         */
        getTopics : function(args) {
        	return this.service.getTopics(this.getForumUuid(), args);
        },

        /**
         * Loads the forum object with the atom entry associated with the
         * forum. By default, a network call is made to load the atom entry
         * document in the forum object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var forumUuid = this.getForumUuid();
            var promise = this.service._validateForumUuid(forumUuid);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.setData(data);
                    return self;
                }
            };

            var requestArgs = lang.mixin({
                forumUuid : forumUuid
            }, args || {});
            var options = {
                handleAs : "text",
                query : requestArgs
            };
            
            return this.service.getEntity(consts.AtomForum, options, forumUuid, callbacks);
        },

        /**
         * Remove this forum
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
            return this.service.deleteForum(this.getForumUuid(), args);
        },

        /**
         * Update this forum
         * 
         * @method update
         * @param {Object} [args] Argument object
         */
        update : function(args) {
            return this.service.updateForum(this, args);
        },
        
        /**
         * Save this forum
         * 
         * @method save
         * @param {Object} [args] Argument object
         */
        save : function(args) {
            if (this.getForumUuid()) {
                return this.service.updateForum(this, args);
            } else {
                return this.service.createForum(this, args);
            }
        }        
       
    });
    
    /**
     * ForumTopic class represents an entry for a forums topic feed returned by the
     * Connections REST API.
     * 
     * @class ForumTopic
     * @namespace sbt.connections
     */
    var ForumTopic = declare(AtomEntity, {

    	xpath : consts.ForumTopicXPath,
    	contentType : "html",
    	categoryScheme : CategoryTopic,
    	
        /**
         * Construct a ForumTopic entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return extra entry data to be included in post data for this entity.
         * 
         * @returns {String}
         */
        createEntryData : function() {
        	var entryData = "";
        	if (this.isPinned()) {
        		entryData += stringUtil.transform(FlagTmpl, this, function(v,k) { return consts.FlagPinned; }, this);
        	}
        	if (this.isLocked()) {
        		entryData += stringUtil.transform(FlagTmpl, this, function(v,k) { return consts.FlagLocked; }, this);
        	}
        	if (this.isQuestion()) {
        		entryData += stringUtil.transform(FlagTmpl, this, function(v,k) { return consts.FlagQuestion; }, this);
        	}
            return stringUtil.trim(entryData);
        },

        /**
         * Return the value of id from Forum Topic ATOM
         * entry document.
         * 
         * @method getTopicUuid
         * @return {String} ID of the Forum Topic
         */
        getTopicUuid : function() {
            var uid = this.getAsString("topicUuid");
            return extractForumUuid(uid);
        },

        /**
         * Sets id of IBM Connections Forum Topic.
         * 
         * @method setTopicUuid
         * @param {String} topicUuid Id of the forum topic
         */
        setTopicUuid : function(topicUuid) {
            return this.setAsString("topicUuid", topicUuid);
        },

        /**
         * Return the value of IBM Connections forum ID from forum ATOM
         * entry document.
         * 
         * @method getForumUuid
         * @return {String} Forum ID of the forum
         */
        getForumUuid : function() {
            var uid = this.getAsString("forumUuid");
            return extractForumUuid(uid);
        },

        /**
         * Sets id of IBM Connections forum.
         * 
         * @method setForumUuid
         * @param {String} forumUuid Id of the forum
         */
        setForumUuid : function(forumUuid) {
            return this.setAsString("forumUuid", forumUuid);
        },

        /**
         * Return the value of communityUuid from Forum ATOM
         * entry document.
         * 
         * @method getCommunityUuid
         * @return {String} Uuid of the Community
         */
        getCommunityUuid : function() {
            return this.getAsString("communityUuid");
        },

        /**
         * Sets communityUuid of IBM Connections forum.
         * 
         * @method setCommunityUuid
         * @param {String} communityUuid Community Uuid of the forum
         * @return {ForumTopic}
         */
        setCommunityUuid : function(communityUuid) {
            return this.setAsString("communityUuid", communityUuid);
        },

        /**
         * Return the url of the IBM Connections forum from
         * forum ATOM entry document.
         * 
         * @method getTopicUrl
         * @return {String} Url of the forum
         */
        getTopicUrl : function() {
            return this.getAsString("alternateUrl");
        },
                
        /**
         * Return the permissions of the IBM Connections forum topic from
         * forum ATOM entry document.
         * 
         * @method getPermisisons
         * @return {String} Permissions of the forum topic
         */
        getPermisisons : function() {
            return this.getAsString("permissions");
        },
                
        /**
         * True if you want the topic to be added to the top of the forum thread.
         * 
         * @method isPinned
         * @return {Boolean} 
         */
        isPinned : function() {
        	return this.getAsBoolean("pinned");
        },
        
        /**
         * Set to true if you want the topic to be added to the top of the forum thread.
         * 
         * @method setPinned
         * @param pinned
         * @return {ForumTopic} 
         */
        setPinned : function(pinned) {
        	return this.setAsBoolean("pinned", pinned);
        },
        
        /**
         * If true, indicates that the topic is locked. 
         * 
         * @method isLocked
         * @return {Boolean} 
         */
        isLocked : function() {
        	return this.getAsBoolean("locked");
        },
        
        /**
         * Set to true, indicates that the topic is locked. 
         * 
         * @method isLocked
         * @param located
         * @return {ForumTopic} 
         */
        setLocked : function(locked) {
        	return this.setAsBoolean("locked", locked);
        },
        
        /**
         * If true, indicates that the topic is a question. 
         * 
         * @method isQuestion
         * @return {Boolean} 
         */
        isQuestion : function() {
        	return this.getAsBoolean("question");
        },
        
        /**
         * Set to true, indicates that the topic is a question. 
         * 
         * @method setQuestion
         * @param question
         * @return {Boolean} 
         */
        setQuestion : function(question) {
        	return this.setAsBoolean("question", question);
        },
        
        /**
         * If true, indicates that the topic is a question that has been answered.
         * 
         * @method isAnswered
         * @return {Boolean} 
         */
        isAnswered : function() {
        	return this.getAsBoolean("answered");
        },
        
        /**
         * If true, this forum topic has not been recommended by the current user.
         * 
         * @method isNotRecommendedByCurrentUser
         * @returns {Boolean}
         */
        isNotRecommendedByCurrentUser : function() {
        	return this.getAsBoolean("notRecommendedByCurrentUser");
        },
        
        /**
         * Return an array containing the tags for this forum topic.
         * 
         * @method getTags
         * @return {Array}
         */
        getTags : function() {
        	return this.getAsArray("tags");
        },
        
        /**
         * Return an array containing the tags for this forum topic.
         * 
         * @method setTags
         * @param {Array}
         */
        setTags : function(tags) {
        	return this.setAsArray("tags", tags);
        },
        
        /**
         * Return the recommendations url of the forum topic.
         * 
         * @method getRecommendationsUrl
         * @return {String} Recommendations url
         */
        getRecommendationsUrl : function() {
            return this.getAsString("recommendationsUrl");
        },

        /**
         * Get a list for forum recommendations that includes the recommendations for this forum topic.
         * 
         * @method getRecommendations
         */
        getRecommendations : function(args) {
        	return this.service.getForumRecommendations(this.getTopicUuid(), args);
        },
        
        /**
         * Get a list for forum replies that includes the replies for this forum topic.
         * 
         * @method getReplies
         */
        getReplies : function(args) {
        	return this.service.getForumTopicReplies(this.getTopicUuid(), args);
        },
        
        /**
         * To like this topic in a stand-alone forum, create forum recommendation to the forum topic resources.
         * 
         * @method deleteRecommendation
         * @param args
         */
        createRecommendation : function(args) {
            return this.service.createForumRecommendation(this.getTopicUuid(), args);
        },
        
        /**
         * Delete a recommendation of this topic in the forum.
         * Only the user who have already recommended the post can delete it's own recommendation.
         * 
         * @method deleteRecommendation
         * @param args
         */
        deleteRecommendation : function(args) {
            return this.service.deleteForumRecommendation(this.getTopicUuid(), args);
        },
        
        /**
         * Loads the forum topic object with the atom entry associated with the
         * forum topic. By default, a network call is made to load the atom entry
         * document in the forum topic object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var topicUuid = this.getTopicUuid();
            var promise = this.service._validateTopicUuid(topicUuid);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.setData(data);
                    return self;
                }
            };

            var requestArgs = lang.mixin({
                topicUuid : topicUuid
            }, args || {});
            var options = {
                handleAs : "text",
                query : requestArgs
            };
            
            return this.service.getEntity(consts.AtomTopic, options, topicUuid, callbacks);
        },

        /**
         * Remove this forum topic
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
            return this.service.deleteForumTopic(this.getTopicUuid(), args);
        },

        /**
         * Update this forum topic
         * 
         * @method update
         * @param {Object} [args] Argument object
         */
        update : function(args) {
            return this.service.updateForumTopic(this, args);
        },
        
        /**
         * Save this forum topic
         * 
         * @method save
         * @param {Object} [args] Argument object
         */
        save : function(args) {
            if (this.getTopicUuid()) {
                return this.service.updateForumTopic(this, args);
            } else {
                return this.service.createForumTopic(this, args);
            }
        }        
               
    });
    
    /**
     * ForumReply class represents an entry for a forums reply feed returned by the
     * Connections REST API.
     * 
     * @class ForumReply
     * @namespace sbt.connections
     */
    var ForumReply = declare(AtomEntity, {

    	xpath : consts.ForumReplyXPath,
    	contentType : "html",
    	categoryScheme : CategoryReply,
    	
        /**
         * Construct a ForumReply entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
        
        /**
         * Return extra entry data to be included in post data for this entity.
         * 
         * @returns {String}
         */
        createEntryData : function() {
        	if (!this.getTopicUuid()) {
        		return "";
        	}
        	var entryData = "";
        	if (this.isAnswer()) {
        		entryData += stringUtil.transform(FlagTmpl, this, function(v,k) { return consts.FlagAnswer; }, this);
        	}
        	entryData += stringUtil.transform(ReplyTmpl, this, function(v,k) { return v; }, this);
            return stringUtil.trim(entryData);
        },

        /**
         * Return the value of id from Forum Reply ATOM
         * entry document.
         * 
         * @method getReplyUuid
         * @return {String} ID of the Forum Reply
         */
        getReplyUuid : function() {
            var uid = this.getAsString("replyUuid");
            return extractForumUuid(uid);
        },

        /**
         * Sets id of IBM Connections Forum Reply.
         * 
         * @method setReplyUuid
         * @param {String} replyUuid Id of the forum reply
         */
        setReplyUuid : function(replyUuid) {
            return this.setAsString("replyUuid", replyUuid);
        },

        /**
         * Return the value of IBM Connections topic ID from forum ATOM
         * entry document.
         * 
         * @method getTopicUuid
         * @return {String} ID of the forum reply
         */
        getTopicUuid : function() {
            var uid = this.getAsString("topicUuid");
            return extractForumUuid(uid);
        },

        /**
         * Sets id of IBM Connections forum reply.
         * 
         * @method setTopicUuid
         * @param {String} topicUuid Id of the forum topic
         */
        setTopicUuid : function(topicUuid) {
            return this.setAsString("topicUuid", topicUuid);
        },
        
        /**
         * Return the value of id of the post that this is a repy to.
         * 
         * @method getReplyToPostUuid
         * @returns {String} postUuid Id of the forum post
         */
        getReplyToPostUuid : function() {
        	var uid = this.getAsString("replyTo");
        	return extractForumUuid(uid);
        },

        /**
         * Sets the value of id of the post that this is a repy to.
         * 
         * @method setReplyToPostUuid
         * @param {String} postUuid Id of the forum post
         */
        setReplyToPostUuid : function(postUuid) {
        	return this.setAsString("replyTo", postUuid);
        },

        /**
         * Return the url of the IBM Connections Forum Reply reply from
         * forum ATOM entry document.
         * 
         * @method getReplyUrl
         * @return {String} Url of the forum
         */
        getReplyUrl : function() {
            return this.getAlternateUrl();
        },
                
        /**
         * Return the permissions of the IBM Connections Forum Reply from
         * forum ATOM entry document.
         * 
         * @method getPermisisons
         * @return {String} Permissions of the Forum Reply
         */
        getPermisisons : function() {
            return this.getAsString("permissions");
        },
        
        /**
         * If true, indicates that the reply is an accepted answer.
         * 
         * @method isAnswered
         * @return {Boolean} 
         */
        isAnswer : function() {
        	return this.getAsBoolean("answer");
        },
        
        /**
         * Set to true, indicates that the reply is an accepted answer. 
         * 
         * @method setAnswer
         * @param answer
         * @return {Boolean} 
         */
        setAnswer : function(answer) {
        	return this.setAsBoolean("answer", answer);
        },
        
        /**
         * If true, this forum reply has not been recommended by the current user.
         * 
         * @method isNotRecommendedByCurrentUser
         * @returns {Boolean}
         */
        isNotRecommendedByCurrentUser : function() {
        	return this.getAsBoolean("notRecommendedByCurrentUser");
        },
        
        /**
         * Return the recommendations url of the forum reply.
         * 
         * @method getRecommendationsUrl
         * @return {String} Recommendations url
         */
        getRecommendationsUrl : function() {
            return this.getAsString("recommendationsUrl");
        },

        /**
         * Get a list for forum recommendations that includes the recommendations for this forum reply.
         * 
         * @method getRecommendations
         */
        getRecommendations : function(args) {
        	return this.service.getForumRecommendations(this.getReplyUuid(), args);
        },
        
        /**
         * Get a list for forum replies that includes the replies for this forum reply.
         * 
         * @method getReplies
         */
        getReplies : function(args) {
        	return this.service.getForumReplyReplies(this.getReplyUuid(), args);
        },
        
        /**
         * To like this reply in a stand-alone forum, create forum recommendation to the forum reply resources.
         * 
         * @method deleteRecommendation
         * @param args
         */
        createRecommendation : function(args) {
            return this.service.createForumRecommendation(this.getReplyUuid(), args);
        },
        
        /**
         * Delete a recommendation of this reply in the forum.
         * Only the user who have already recommended the post can delete it's own recommendation.
         * 
         * @method deleteRecommendation
         * @param args
         */
        deleteRecommendation : function(args) {
            return this.service.deleteForumRecommendation(this.getReplyUuid(), args);
        },
        
        /**
         * Loads the forum reply object with the atom entry associated with the
         * forum reply. By default, a network call is made to load the atom entry
         * document in the forum reply object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var replyUuid = this.getReplyUuid();
            var promise = this.service._validateReplyUuid(replyUuid);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.setData(data);
                    return self;
                }
            };

            var requestArgs = lang.mixin({
                replyUuid : replyUuid
            }, args || {});
            var options = {
                handleAs : "text",
                query : requestArgs
            };
            
            return this.service.getEntity(consts.AtomReply, options, replyUuid, callbacks);
        },

        /**
         * Remove this forum reply
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
            return this.service.deleteForumReply(this.getReplyUuid(), args);
        },

        /**
         * Update this forum reply
         * 
         * @method update
         * @param {Object} [args] Argument object
         */
        update : function(args) {
            return this.service.updateForumReply(this, args);
        },
        
        /**
         * Save this forum reply
         * 
         * @method save
         * @param {Object} [args] Argument object
         */
        save : function(args) {
            if (this.getReplyUuid()) {
                return this.service.updateForumReply(this, args);
            } else {
                return this.service.createForumReply(this, args);
            }
        }        
               
    });
    
    /**
     * ForumMember class represents an entry for a forums member feed returned by the
     * Connections REST API.
     * 
     * @class ForumMember
     * @namespace sbt.connections
     */
    var ForumMember = declare(AtomEntity, {

    	categoryScheme : null,
    	
        /**
         * Construct a Forum Tag entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        }
    
    });
    
    /**
     * ForumRecommendation class represents an entry for a forums recommendation feed returned by the
     * Connections REST API.
     * 
     * @class ForumTag
     * @namespace sbt.connections
     */
    var ForumRecommendation = declare(AtomEntity, {

    	xpath : consts.ForumRecommendationXPath,
    	contentType : "text",
    	categoryScheme : CategoryRecommendation,
    	
        /**
         * Construct a Forum Tag entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
    
        /**
         * Return the value of title from ATOM entry document.
         * 
         * @method getTitle
         * @return {String} ATOM entry title
         */
        getTitle : function() {
            return this.getAsString("title") || "liked";
        },

	    /**
	     * Return the value of IBM Connections recommendation ID from recommendation ATOM
	     * entry document.
	     * 
	     * @method getRecommendationUuid
	     * @return {String} ID of the recommendation topic
	     */
	    getRecommendationUuid : function() {
	        var uid = this.getAsString("id");
            return extractForumUuid(uid);
	    },

        /**
         * Return the value of IBM Connections post ID from recommendation ATOM
         * entry document.
         * 
         * @method getPostUuid
         * @return {String} ID of the forum post
         */
        getPostUuid : function() {
        	var postUuid = this.getAsString("postUuid");
            return this.service.getUrlParameter(postUuid, "postUuid") || postUuid;
        },
        
        /**
         * Set the value of IBM Connections post ID from recommendation ATOM
         * entry document.
         * 
         * @method setPostUuid
         * @return {String} ID of the forum post
         */
        setPostUuid : function(postUuid) {
            return this.setAsString("postUuid", postUuid);
        }        

    });
    
    /**
     * ForumTag class represents an entry for a forums tag feed returned by the
     * Connections REST API.
     * 
     * @class ForumTag
     * @namespace sbt.connections
     */
    var ForumTag = declare(AtomEntity, {

    	categoryScheme : null,
    	
        /**
         * Construct a Forum Tag entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        }
    
    });
    
    /*
     * Method used to extract the forum uuid for an id string.
     */
    var extractForumUuid = function(uid) {
        if (uid && uid.indexOf("urn:lsid:ibm.com:forum:") == 0) {
            return uid.substring("urn:lsid:ibm.com:forum:".length);
        } else {
            return uid;
        }
    }; 
    
    /*
     * Callbacks used when reading a feed that contains forum entries.
     */
    var ForumFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ForumsFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            return new Forum({
                service : service,
                data : data
            });
        }
    };

    /*
     * Callbacks used when reading a feed that contains forum topic entries.
     */
    var ForumTopicFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ForumsFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            return new ForumTopic({
                service : service,
                data : data
            });
        }
    };

    /*
     * Callbacks used when reading a feed that contains forum topic reply entries.
     */
    var ForumReplyFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ForumsFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            return new ForumReply({
                service : service,
                data : data
            });
        }
    };

    /*
     * Callbacks used when reading a feed that contains forum recommendation entries.
     */
    var ForumRecommendationFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ForumsFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            return new ForumRecommendation({
                service : service,
                data : data
            });
        }
    };

    
    /**
     * ForumsService class.
     * 
     * @class ForumsService
     * @namespace sbt.connections
     */
    var ForumService = declare(BaseService, {

        contextRootMap: {
            forums: "forums"
        },
        
        /**
         * Constructor for ForumsService
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
            if (!this.endpoint) {
                this.endpoint = config.findEndpoint(this.getDefaultEndpointName());
            }
        },

        /**
         * Return the default endpoint name if client did not specify one.
         * @returns {String}
         */
        getDefaultEndpointName: function() {
            return "connections";
        },
        
        /**
         * Create a Forum object with the specified data.
         * 
         * @method newForum
         * @param {Object} args Object containing the fields for the 
         * new Forum 
         */
        newForum : function(args) {
            return this._toForum(args);
        },
        
        /**
         * Create a ForumTopic object with the specified data.
         * 
         * @method newForumTopic
         * @param {Object} args Object containing the fields for the 
         * new ForumTopic
         */
        newForumTopic : function(args) {
            return this._toForumTopic(args);
        },
        
        /**
         * Create a ForumReply object with the specified data.
         * 
         * @method newForumReply
         * @param {Object} args Object containing the fields for the 
         * new ForumReply 
         */
        newForumReply : function(args) {
            return this._toForumReply(args);
        },
        
        /**
         * Get a feed that includes forums created by the authenticated user or associated with communities to which the user belongs.
         * 
         * @method getMyForums
         * @param args
         */
        getMyForums: function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomForumsMy, options, ForumFeedCallbacks);
        },      
        
        /**
         * Get a feed that includes the topics that the authenticated user created in stand-alone forums and in forums associated 
         * with communities to which the user belongs.
         * 
         * @method getMyForums
         * @param requestArgs
         */
        getMyTopics: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.AtomTopicsMy, options, ForumTopicFeedCallbacks);
        },      
        
        /**
         * Get a feed that includes all stand-alone and forum forums created in the enterprise.
         * 
         * @method getAllForums
         * @param requestArgs
         */
        getAllForums: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.AtomForums, options, ForumFeedCallbacks);
        },      
        
        /**
         * Get a feed that includes all stand-alone and forum forums created in the enterprise.
         * 
         * @method getAllForums
         * @param args
         */
        getPublicForums: function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomForumsPublic, options, ForumFeedCallbacks);
        },
        
        /**
         * Get a list for forum topics that includes the topics in the specified forum.
         * 
         * @method getTopics
         * @param forumUuid
         * @param args
         * @returns
         */
        getForumTopics: function(forumUuid, args) {
            var promise = this._validateForumUuid(forumUuid);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin({ forumUuid : forumUuid }, args || {});
            
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            
            return this.getEntities(consts.AtomTopics, options, ForumTopicFeedCallbacks);
        },
        
        /**
         * Get a list for forum recommendations that includes the recommendations in the specified post.
         * 
         * @method getRecommendations
         * @param postUuid
         * @param args
         * @returns
         */
        getForumRecommendations: function(postUuid, args) {
            var promise = this._validatePostUuid(postUuid);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin({ postUuid : postUuid }, args || {});
            
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            
            return this.getEntities(consts.AtomRecommendationEntries, options, ForumRecommendationFeedCallbacks);
        },
        
        /**
         * Get a list for forum replies that includes the replies in the specified topic.
         * 
         * @method getForumTopicReplies
         * @param topicUuid
         * @param args
         * @returns
         */
        getForumTopicReplies: function(topicUuid, args) {
            var promise = this._validateTopicUuid(topicUuid);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin({ topicUuid : topicUuid }, args || {});
            
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            
            return this.getEntities(consts.AtomReplies, options, ForumReplyFeedCallbacks);
        },
        
        /**
         * Get a list for forum replies that includes the replies in the specified reply.
         * 
         * @method getForumReplyReplies
         * @param replyUuid
         * @param args
         * @returns
         */
        getForumReplyReplies: function(replyUuid, args) {
            var promise = this._validateReplyUuid(replyUuid);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin({ replyUuid : replyUuid }, args || {});
            
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            
            return this.getEntities(consts.AtomReplies, options, ForumReplyFeedCallbacks);
        },
        
        /**
         * Get a list for forum replies that includes the replies in the specified post.
         * The post uuid must be specified in the args as either:
         *     { topicUuid : "<topicUuid>" } or { replyUuid : "<replyUuid>" } 
         * 
         * @method getForumReplies
         * @param topicUuid
         * @param args
         * @returns
         */
        getForumReplies: function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args
            };
            
            return this.getEntities(consts.AtomReplies, options, ForumReplyFeedCallbacks);
        },
        
        /**
         * Retrieve a list of all forum entries, add communityUuid to the requestArgs object to get the forums related to a specific community.
         * 
         * @method getForums
         * @param {Object} requestArgs Object containing the query arguments to be 
         * sent (defined in IBM Connections Communities REST API) 
         */
        getForums : function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
                
            return this.getEntities(consts.AtomForums, options, ForumFeedCallbacks);
        },

        /**
         * Retrieve a forum entry, use the edit link for the forum entry 
         * which can be found in the my communities feed.
         * 
         * @method getForum
         * @param {String } forumUuid
         * @param {Object} args Object containing the query arguments to be 
         * sent (defined in IBM Connections Communities REST API) 
         */
        getForum : function(forumUuid, args) {
            var forum = new Forum({
                service : this,
                _fields : { forumUuid : forumUuid }
            });
            return forum.load(args);
        },
        
        /**
         * Create a forum by sending an Atom entry document containing the 
         * new forum to the My Forums resource.
         * 
         * @method createForum
         * @param {Object} forum Forum object which denotes the forum to be created.
         * @param {Object} [args] Argument object
         */
        createForum : function(forumOrJson,args) {
            var forum = this._toForum(forumOrJson);
            var promise = this._validateForum(forum, false, args);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                forum.setData(data);
                var forumUuid = this.getLocationParameter(response, "forumUuid");
                forum.setForumUuid(forumUuid);
                return forum;
            };

            var options = {
                method : "POST",
                query : args || {},
                headers : consts.AtomXmlHeaders,
                data : forum.createPostData()
            };
            
            return this.updateEntity(consts.AtomForumsMy, options, callbacks, args);
        },

        /**
         * Update a forum by sending a replacement forum entry document in Atom format 
         * to the existing forum's edit web address.
         * All existing forum entry information will be replaced with the new data. To avoid 
         * deleting all existing data, retrieve any data you want to retain first, and send it back 
         * with this request. For example, if you want to add a new tag to a forum entry, retrieve 
         * the existing tags, and send them all back with the new tag in the update request.
         * 
         * @method updateForum
         * @param {Object} forum Forum object
         * @param {Object} [args] Argument object
         */
        updateForum : function(forumOrJson,args) {
            var forum = this._toForum(forumOrJson);
            var promise = this._validateForum(forum, true, args);
            if (promise) {
                return promise;
            }
            
            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
            	// preserve the forumUuid
            	var forumUuid = forum.getForumUuid();
                forum.setData(data);
                forum.setForumUuid(forumUuid);
                return forum;
            };

            var requestArgs = lang.mixin({
                forumUuid : forum.getForumUuid()
            }, args || {});
            
            var options = {
                method : "PUT",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : forum.createPostData()
            };
            
            return this.updateEntity(consts.AtomForum, options, callbacks, args);
        },

        /**
         * Delete a forum, use the HTTP DELETE method.
         * Only the owner of a forum can delete it. Deleted communities cannot be restored
         * 
         * @method deleteForum
         * @param {String/Object} forum id of the forum or the forum object (of the forum to be deleted)
         * @param {Object} [args] Argument object
         */
        deleteForum : function(forumUuid,args) {
            var promise = this._validateForumUuid(forumUuid);
            if (promise) {
                return promise;
            }            
           
            var requestArgs = lang.mixin({
                forumUuid : forumUuid
            }, args || {});
            
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            
            return this.deleteEntity(consts.AtomForum, options, forumUuid);
        },
        
        /**
         * Retrieve a forum topic entry, use the edit link for the forum topic entry 
         * which can be found in the My Forums feed.
         * 
         * @method getForumTopic
         * @param {String } topicUuid
         * @param {Object} args Object containing the query arguments to be 
         * sent (defined in IBM Connections Communities REST API) 
         */
        getForumTopic : function(topicUuid, args) {
            var forumTopic = new ForumTopic({
                service : this,
                _fields : { topicUuid : topicUuid }
            });
            return forumTopic.load(args);
        },

        /**
         * Create a forum topc by sending an Atom entry document containing the 
         * new forum to the forum replies resource.
         * 
         * @method createForumTopic
         * @param {Object} forumTopic Forum topic object which denotes the forum topic to be created.
         * @param {Object} [args] Argument object
         */
        createForumTopic : function(topicOrJson,args) {
            var forumTopic = this._toForumTopic(topicOrJson);
            var promise = this._validateForumTopic(forumTopic, false, args);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                var topicUuid = this.getLocationParameter(response, "topicUuid");
                forumTopic.setTopicUuid(topicUuid);
                forumTopic.setData(data);
                return forumTopic;
            };

            var requestArgs = lang.mixin({
                forumUuid : forumTopic.getForumUuid()
            }, args || {});
            
            var options = {
                method : "POST",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : forumTopic.createPostData()
            };
            
            return this.updateEntity(consts.AtomTopics, options, callbacks, args);
        },

        /**
         * Update a forum topic by sending a replacement forum entry document in Atom format 
         * to the existing forum topic's edit web address.
         * All existing forum entry information will be replaced with the new data. To avoid 
         * deleting all existing data, retrieve any data you want to retain first, and send it back 
         * with this request. For example, if you want to add a new tag to a forum entry, retrieve 
         * the existing tags, and send them all back with the new tag in the update request.
         * 
         * @method updateForumTopic
         * @param {Object} topicOrJson Forum topic object
         * @param {Object} [args] Argument object
         */
        updateForumTopic : function(topicOrJson,args) {
            var forumTopic = this._toForumTopic(topicOrJson);
            var promise = this._validateForumTopic(forumTopic, true, args);
            if (promise) {
                return promise;
            }
            
            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
            	// preserve the topicUuid
            	var topicUuid = forumTopic.getTopicUuid();
            	forumTopic.setData(data);
            	forumTopic.setTopicUuid(topicUuid);
                return forumTopic;
            };

            var requestArgs = lang.mixin({
                topicUuid : forumTopic.getTopicUuid()
            }, args || {});
            
            var options = {
                method : "PUT",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : forumTopic.createPostData()
            };
            
            return this.updateEntity(consts.AtomTopic, options, callbacks, args);
        },

        /**
         * Delete a forum topic, use the HTTP DELETE method.
         * Only the owner of a forum topic can delete it. Deleted forum topics cannot be restored
         * 
         * @method deleteForumTopic
         * @param {String/Object} id of the forum topic to be deleted
         * @param {Object} [args] Argument object
         */
        deleteForumTopic : function(topicUuid,args) {
            var promise = this._validateTopicUuid(topicUuid);
            if (promise) {
                return promise;
            }            
           
            var requestArgs = lang.mixin({
                topicUuid : topicUuid
            }, args || {});
            
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            
            return this.deleteEntity(consts.AtomTopic, options, topicUuid);
        },
        
        /**
         * Retrieve a forum reply entry, use the edit link for the forum reply entry 
         * which can be found in the my communities feed.
         * 
         * @method getForumReply
         * @param {String } replyUuid
         * @param {Object} args Object containing the query arguments to be 
         * sent (defined in IBM Connections Communities REST API) 
         */
        getForumReply : function(replyUuid, args) {
            var forumReply = new ForumReply({
                service : this,
                _fields : { replyUuid : replyUuid }
            });
            return forumReply.load(args);
        },

        /**
         * Create a forum reply by sending an Atom entry document containing the 
         * new forum reply to the My Communities resource.
         * 
         * @method createForumReply
         * @param {Object} reply ForumReply object which denotes the forum to be created.
         * @param {Object} [args] Argument object
         */
        createForumReply : function(replyOrJson,args) {
            var forumReply = this._toForumReply(replyOrJson);
            var promise = this._validateForumReply(forumReply, false, args);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                var replyUuid = this.getLocationParameter(response, "replyUuid");
                forumReply.setReplyUuid(replyUuid);
                forumReply.setData(data);
                return forumReply;
            };

            var options = {
                method : "POST",
                query : args || { topicUuid : forumReply.getTopicUuid() },
                headers : consts.AtomXmlHeaders,
                data : forumReply.createPostData()
            };
            
            return this.updateEntity(consts.AtomReplies, options, callbacks, args);
        },

        /**
         * Update a forum by sending a replacement forum entry document in Atom format 
         * to the existing forum's edit web address.
         * All existing forum entry information will be replaced with the new data. To avoid 
         * deleting all existing data, retrieve any data you want to retain first, and send it back 
         * with this request. For example, if you want to add a new tag to a forum entry, retrieve 
         * the existing tags, and send them all back with the new tag in the update request.
         * 
         * @method updateForumReply
         * @param {Object} replyOrJson Forum reply object
         * @param {Object} [args] Argument object
         */
        updateForumReply : function(replyOrJson,args) {
            var forumReply = this._toForumReply(replyOrJson);
            var promise = this._validateForumReply(forumReply, true, args);
            if (promise) {
                return promise;
            }
            
            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
            	// preserve the replyUuid
            	var replyUuid = forumReply.getReplyUuid();
            	forumReply.setData(data);
            	forumReply.setReplyUuid(replyUuid);
                return forumReply;
            };

            var requestArgs = lang.mixin({
            	replyUuid : forumReply.getReplyUuid()
            }, args || {});
            
            var options = {
                method : "PUT",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : forumReply.createPostData()
            };
            
            return this.updateEntity(consts.AtomReply, options, callbacks, args);
        },

        /**
         * Delete a forum reply, use the HTTP DELETE method.
         * Only the owner of a forum reply can delete it. Deleted forum replies cannot be restored
         * 
         * @method deleteForumReply
         * @param {String/Object} Id of the forum reply to be deleted
         * @param {Object} [args] Argument object
         */
        deleteForumReply : function(replyUuid,args) {
            var promise = this._validateReplyUuid(replyUuid);
            if (promise) {
                return promise;
            }            
           
            var requestArgs = lang.mixin({
            	replyUuid : replyUuid
            }, args || {});
            
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            
            return this.deleteEntity(consts.AtomReply, options, replyUuid);
        },

        /**
         * Retrieve complete information about recommendations to a post(topic/reply) in a stand-alone forum.
         * 
         * @method getForumRecommendation
         * @param postUuid
         * @param args
         */
        getForumRecommendation : function(postUuid, args) {
            var forumRecommendation = new ForumRecommendation({
                service : this,
                _fields : { postUuid : postUuid }
            });
            return forumRecommendation.load(args);
        },
        
        /**
         * To like a post(topic/reply) in a stand-alone forum, create forum recommendation to the forum topic/reply resources.
         * 
         * @method createForumRecommendation
         * @param recommendationOrJson
         * @param args
         */
        createForumRecommendation : function(recommendationOrJson, args) {
            var forumRecommendation = this._toForumRecommendation(recommendationOrJson);
            var promise = this._validateForumRecommendation(forumRecommendation, false, args);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                forumRecommendation.setData(data);
                return forumRecommendation;
            };

            var options = {
                method : "POST",
                query : args || { postUuid : forumRecommendation.getPostUuid() },
                headers : consts.AtomXmlHeaders,
                data : forumRecommendation.createPostData()
            };
            
            return this.updateEntity(consts.AtomRecommendationEntries, options, callbacks, args);
        },
        
        /**
         * Delete a recommendation of a post(topic or reply) in a forum.
         * Only the user who have already recommended the post can delete it's own recommendation.
         * 
         * @method deleteForumRecommendation
         * @param postUuid
         * @param args
         */
        deleteForumRecommendation : function(postUuid, args) {
            var promise = this._validatePostUuid(postUuid);
            if (promise) {
                return promise;
            }            
           
            var requestArgs = lang.mixin({
            	postUuid : postUuid
            }, args || {});
            
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            
            return this.deleteEntity(consts.AtomRecommendationEntries, options, postUuid);
        },

        //
        // Internals
        //
        
        /*
         * Validate a forum and return a Promise if invalid.
         */
        _validateForum : function(forum,checkUuid) {
            if (!forum || !forum.getTitle()) {
                return this.createBadRequestPromise("Invalid argument, forum with title must be specified.");
            }
            if (checkUuid && !forum.getForumUuid()) {
                return this.createBadRequestPromise("Invalid argument, forum with UUID must be specified.");
            }
        },
        
        /*
         * Validate a forum topic and return a Promise if invalid.
         */
        _validateForumTopic : function(forumTopic,checkUuid) {
            if (!forumTopic || !forumTopic.getTitle()) {
                return this.createBadRequestPromise("Invalid argument, forum topic with title must be specified.");
            }
            if (checkUuid && !forumTopic.getTopicUuid()) {
                return this.createBadRequestPromise("Invalid argument, forum topic with UUID must be specified.");
            }
        },
        
        /*
         * Validate a forum reply and return a Promise if invalid.
         */
        _validateForumReply : function(forumReply,checkUuid) {
            if (!forumReply || !forumReply.getTitle()) {
                return this.createBadRequestPromise("Invalid argument, forum reply with title must be specified.");
            }
            if (checkUuid && !forumReply.getReplyUuid()) {
                return this.createBadRequestPromise("Invalid argument, forum reply with UUID must be specified.");
            }
        },
        
        /*
         * Validate a forum recommendation and return a Promise if invalid.
         */
        _validateForumRecommendation : function(forumRecommendation,checkUuid) {
            if (!forumRecommendation || !forumRecommendation.getPostUuid()) {
                return this.createBadRequestPromise("Invalid argument, forum recommendation with postUuid must be specified.");
            }
            if (checkUuid && !forumRecommendation.getRecommendationUuid()) {
                return this.createBadRequestPromise("Invalid argument, forum recommendation with UUID must be specified.");
            }
        },
        
        /*
         * Validate a forum UUID, and return a Promise if invalid.
         */
        _validateForumUuid : function(forumUuid) {
            if (!forumUuid || forumUuid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected forumUuid.");
            }
        },
        
        /*
         * Validate a post UUID, and return a Promise if invalid.
         */
        _validatePostUuid : function(postUuid) {
            if (!postUuid || postUuid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected postUuid.");
            }
        },
        
        /*
         * Validate a topic UUID, and return a Promise if invalid.
         */
        _validateTopicUuid : function(topicUuid) {
            if (!topicUuid || topicUuid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected topicUuid.");
            }
        },
        
        /*
         * Validate a reply UUID, and return a Promise if invalid.
         */
        _validateReplyUuid : function(replyUuid) {
            if (!replyUuid || replyUuid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected replyUuid.");
            }
        },
        
        /*
         * Return a Forum instance from Forum or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toForum : function(forumOrJsonOrString) {
            if (forumOrJsonOrString instanceof Forum) {
                return forumOrJsonOrString;
            } else {
                if (lang.isString(forumOrJsonOrString)) {
                    forumOrJsonOrString = {
                        forumUuid : forumOrJsonOrString
                    };
                }
                return new Forum({
                    service : this,
                    _fields : lang.mixin({}, forumOrJsonOrString)
                });
            }
        },

        /*
         * Return a ForumTopic instance from Forum or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toForumTopic : function(topicOrJsonOrString) {
            if (topicOrJsonOrString instanceof ForumTopic) {
                return topicOrJsonOrString;
            } else {
                if (lang.isString(topicOrJsonOrString)) {
                    topicOrJsonOrString = {
                        forumTopicUuid : topicOrJsonOrString
                    };
                }
                return new ForumTopic({
                    service : this,
                    _fields : lang.mixin({}, topicOrJsonOrString)
                });
            }
        },

        /*
         * Return a Forum instance from ForumReply or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toForumReply : function(replyOrJsonOrString) {
            if (replyOrJsonOrString instanceof ForumReply) {
                return replyOrJsonOrString;
            } else {
                if (lang.isString(replyOrJsonOrString)) {
                	replyOrJsonOrString = {
                        forumReplyUuid : replyOrJsonOrString
                    };
                }
                return new ForumReply({
                    service : this,
                    _fields : lang.mixin({}, replyOrJsonOrString)
                });
            }
        },
        
        /*
         * Return a ForumRecommendation instance from ForumRecommendation, ForumTopic, 
         * ForumReply or JSON or String. Throws an error if the argument was neither.
         */
        _toForumRecommendation : function(entityOrJsonOrString) {
            if (entityOrJsonOrString instanceof ForumRecommendation) {
                return entityOrJsonOrString;
            } else {
                if (lang.isString(entityOrJsonOrString)) {
                	entityOrJsonOrString = {
                        postUuid : entityOrJsonOrString
                    };
                }
                if (entityOrJsonOrString instanceof ForumTopic) {
                	entityOrJsonOrString = {
                        postUuid : entityOrJsonOrString.getTopicUuid()
                    };
                }
                if (entityOrJsonOrString instanceof ForumReply) {
                	entityOrJsonOrString = {
                        postUuid : entityOrJsonOrString.getReplyUuid()
                    };
                }
                return new ForumRecommendation({
                    service : this,
                    _fields : lang.mixin({}, entityOrJsonOrString)
                });
            }
        }
        
    });
    return ForumService;
});

},
'sbt/connections/ForumConstants':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * Definition of constants for ForumService.
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin(conn, {
    	
    	/**
    	 * Term value with a forum recommendation 
    	 */
    	FlagAnswer : "recommendation",
    	
    	/**
    	 * Term value when a forum reply is an accepted answer 
    	 */
    	FlagAnswer : "answer",
    	
    	/**
    	 * Term value when a forum topic is pinned 
    	 */
    	FlagPinned : "pinned",
    	
    	/**
    	 * Term value when a forum topic is locked 
    	 */
    	FlagLocked : "locked",
    	
    	/**
    	 * Term value when a forum topic is a question 
    	 */
    	FlagQuestion : "question",
    	
    	/**
    	 * Term value when a forum topic is an answered question 
    	 */
    	FlagAnswered : "answered",
    	    	
        /**
         * XPath expressions used when parsing a Connections Forums ATOM feed
         */
    	ForumsFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * XPath expressions to be used when reading an forum entry
         */
    	ForumXPath : lang.mixin({
            forumUuid : "a:id",
            content : "a:content[@type='text']",
            moderation : "snx:moderation/@status",
            threadCount: "a:link[@rel='replies']/@thr:count",	
            forumUrl : "a:link[@rel='alternate']/@href",
            communityUuid : "snx:communityUuid"
        }, conn.AtomEntryXPath),
        
        /**
         * XPath expressions to be used when reading an forum topic entry
         */
        ForumTopicXPath : lang.mixin({
            topicUuid : "a:id",
            forumUuid : "thr:in-reply-to/@ref",	
        	tags : "a:category[not(@scheme)]/@term",
        	permissions : "snx:permissions",
            communityUuid : "snx:communityUuid",
            threadCount: "a:link[@rel='replies']/@thr:count",
            locked: "a:category[@term='locked' and @scheme='http://www.ibm.com/xmlns/prod/sn/flags']",
            pinned: "a:category[@term='pinned' and @scheme='http://www.ibm.com/xmlns/prod/sn/flags']",
            question: "a:category[@term='question' and @scheme='http://www.ibm.com/xmlns/prod/sn/flags']",
            answered: "a:category[@term='answered' and @scheme='http://www.ibm.com/xmlns/prod/sn/flags']",
            notRecommendedByCurrentUser: "a:category[@term='NotRecommendedByCurrentUser']",
            threadRecommendationCount: "a:category[@term='ThreadRecommendationCount']/@label",
            recommendationsUrl : "a:link[@rel='recommendations']/@href"
        }, conn.AtomEntryXPath),
        
        /**
         * XPath expressions to be used when reading an forum reply entry
         */
        ForumReplyXPath : lang.mixin({
            replyUuid : "a:id",
        	topicUuid : "thr:in-reply-to/@ref",
            permissions : "snx:permissions",
            communityUuid : "snx:communityUuid",
            answer: "a:category[@term='answer' and @scheme='http://www.ibm.com/xmlns/prod/sn/flags']",
            replyTo: "thr:in-reply-to/@ref",
            notRecommendedByCurrentUser: "a:category[@term='NotRecommendedByCurrentUser']",
            recommendationsUrl : "a:link[@rel='recommendations']/@href"
        }, conn.AtomEntryXPath),
        
        /**
         * XPath expressions to be used when reading an forum recommendation entry
         */
        ForumRecommendationXPath : lang.mixin({
            postUuid : "a:link[@rel='self']/@href"
        }, conn.AtomEntryXPath),
        
        /**
		 * Edit link for a forum entry.  
         */
        AtomForum : "${forums}/atom/forum",
        
        /**
		 * Edit link for a forum topic entry.  
         */
        AtomTopic : "/${forums}/atom/topic",
        
		/**
		 * Edit link for a forum reply entry.  
         */
        AtomReply : "/${forums}/atom/reply",
        
		/**
		 * Get a feed that includes all stand-alone and community forums created in the enterprise. 
         */
        AtomForums : "/${forums}/atom/forums",
        
		/**
		 * Get a feed that includes all of the forums hosted by the Forums application. 
         */
        AtomForumsPublic : "/${forums}/atom/forums/public",
        
		/**
		 * Get a feed that includes forums created by the authenticated user or associated with communities to which the user belongs.  
         */
        AtomForumsMy : "/${forums}/atom/forums/my",
        
		/**
		 * Get a feed that includes the topics in a specific stand-alone forum.  
         */
        
        AtomTopics : "/${forums}/atom/topics",
        
        /**
         * Get a feed that includes the topics that the authenticated user created in stand-alone forums and in forums associated 
         * with communities to which the user belongs. 
         */
        AtomTopicsMy : "/${forums}/atom/topics/my",
        
        /**
         * Get a feed that includes all of the replies for a specific forum topic. 
         */
        AtomReplies : "/${forums}/atom/replies",
        
        /**
         * Get a category document that lists the tags that have been assigned to forums. 
         */
        AtomTagsForum : "/atom/tags/forums",
        
        /**
         * Get a category document that lists the tags that have been assigned to forum topics. 
         */
        AtomTagsTopics : "/atom/tags/topics",
        
        /**
         * Get a feed that includes all of the recommendations for a specific forum post.
         */
        AtomRecommendationEntries : "/${forums}/atom/recommendation/entries"

    });
});
},
'sbt/base/AtomEntity':function(){
/*
 * � Copyright IBM Corp. 2013
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
    					"<title type=\"text\">${getTitle}</title>" +
    					"<content type=\"${contentType}\">${getContent}</content>" +
    					"<summary type=\"text\">${getSummary}</summary>" +
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
         * Gets an author of the ATOM entry
         * 
         * @method getAuthor
         * @return {Member} author Author of the ATOM entry
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
         * @return {Member} contributor Contributor of the ATOM entry
         */
        getContributor : function() {
            return this.getAsObject(
            		[ "contributorUserid", "contributorName", "contributorEmail", "contributorUserState" ],
            		[ "userid", "name", "email", "userState" ]);
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
        },
        
        createNamespaces : function() {
        	if (!this.dataHandler) {
        		return "";
        	}
        	var namespaceData = "";
        	var namespaces = this.dataHandler.namespaces;
        	for (prefix in namespaces) {
        		namespaceData += "xmlns:"+prefix+"=\"" + namespaces[prefix] + "\" ";
        	}
        	return namespaceData;
        }

    });
    
    return AtomEntity;
});

},
'sbt/connections/controls/communities/CommunityMembersGrid':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.connections.controls.profiles.ProfileGrid
 */
define([ "../../../declare", 
		 "../../../config",
		 "../../../lang",
		 "../../../controls/grid/Grid", 
		 "./CommunityMembersGridRenderer", 
		 "./CommunityMembersAction", 
		 "../../../connections/controls/vcard/SemanticTagService", 
		 "../../../store/parameter",
		 "../../../connections/ProfileConstants",
		 "../../../connections/CommunityConstants",
		 "sbt/connections/CommunityService"], 
        function(declare, sbt, lang, Grid, ProfileGridRenderer, CommunityMembersAction, SemanticTagService, parameter, consts, communities,
        		CommunityService) {

	var sortVals = {
			displayName: "displayName",
       		recent: "3" 
	};
	
	var communityMembersSortVals = {
			displayName: "displayName",
       		created: "created" 
	};
	
	var ParamSchema = {
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortBy",sortVals),
		sortOrder: parameter.sortOrder("sortOrder")
	};
	
	var CommunityMembersParamSchema = {
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortBy", communityMembersSortVals),
		sortOrder: parameter.sortOrder("sortOrder")
	};
	
    /**
     * @class ProfileGrid
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ProfileGrid
     */
    var CommunityMembersGrid = declare(Grid, {
    	
    	/**
    	 * @param options, This is a list of all
    	 * the different types of profile grids available.
    	 * Depending on which one is selected specific arguments will be given to
    	 * the atom store and grid renderer.
    	 */
        options : {
            "profile" : {
                storeArgs : {
                    url : consts.AtomProfileDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "colleagues" : {
                storeArgs : {
                     url : consts.AtomConnectionsDo,
                     attributes : consts.ProfileXPath,
                     feedXPath : consts.ProfileFeedXPath,
                     paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "connectionsInCommon" : {
                storeArgs : {
                    url : consts.AtomConnectionsInCommonDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "peopleManaged" : {
                storeArgs : {
                    url : consts.AtomPeopleManagedDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "reportingChain" : {
                storeArgs : {
                    url : consts.AtomReportingChainDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "search" : {
                storeArgs : {
                    url : consts.AtomSearchDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "dynamic" : {
                storeArgs : {
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "communityMembers" : {
                storeArgs : {
                	url : communities.AtomCommunityMembers,
                    attributes : communities.MemberXPath,
                    feedXPath : communities.CommunityFeedXPath,
                    paramSchema: CommunityMembersParamSchema
                },
                rendererArgs : {
                    type : "communityMembers"
                }
            }
        },
        
        /**
         * A community members action, defines default behaviour for when 
         * items in the grid are clicked on or hovered on,
         * it is possible to override these actions
         */
        communityMembersAction: new CommunityMembersAction(),
        
        /**
         * This is the default grid that will be created if no 
         * arguments are given.
         */
        defaultOption: "colleagues",
        
        /**Constructor function
         * @method constructor
         * */
        constructor: function(args){
        	if(args.type == "peopleManaged" || args.type == "reportingChain" || args.type == "profile") {
        		this.hideSorter = true;
        	} 	
        	
            var nls = this.renderer.nls;

            if (args.type == "communityMembers") {
            	
            	this._sortInfo = {
            			displayName: { 
            				title: nls.displayName, 
       	                    sortMethod: "sortByTitle",
       	                    sortParameter: "title"   
            			},
       	                recent: {
       	                	title: nls.created, 
       	                    sortMethod: "sortByCreated",
       	                    sortParameter: "created"   
       	                }
            	};
       		 	this._activeSortAnchor = this._sortInfo.created;
       		 	this._activeSortIsDesc = false;
            } else {
            	this._sortInfo = {
            			displayName: { 
            				title: nls.displayName, 
            				sortMethod: "sortByDisplayName",
            				sortParameter: "displayName" 
            			},
            			recent: {
            				title: nls.recent, 
            				sortMethod: "sortByRecent",
            				sortParameter: "recent"   
            			}
               
            	};
            	this._activeSortAnchor = this._sortInfo.recent;
                this._activeSortIsDesc = false;
            }
            
        },
        
        contextRootMap: {
            profiles: "profiles"
        },
        
        /**
         * Override buildUrl to add outputType, format and email/userid's
         * 
         * @method buildUrl
         * @param url base url
         * @param args arguments that will be passed to the store
         * @param endpoint An endpoint which may contain custom service mappings.
         * @returns Built url
         */
        buildUrl: function(url, args, endpoint) {
            var params = { 
            	outputType : "profile",
            	format : "full"
            };
            
            if (this.query) {
            	params = lang.mixin(params, this.query);
            }
            if (this.type == "colleagues") {
            	params = lang.mixin(params, { connectionType : "colleague" });
            } else if (this.type == "communityMembers") {
            	params = lang.mixin(params, { communityUuid : this.communityUuid });
            }
         
            if (this.email) {
            	params = lang.mixin(params, { email : this.email });
            } 
            if (this.email1 && this.email2) {
            	params = lang.mixin(params, { email : this.email1 + "," + this.email2 });
            } 
            if (this.userid) {
            	params = lang.mixin(params, { userid : this.userid });
            } 
            if (this.userid1 && this.userid2) {
            	params = lang.mixin(params, { userid : this.userid1 + "," + this.userid2 });
            } 

            return this.constructUrl(url, params, this.getUrlParams(), endpoint);
        },

        /**
         * The post create function is called, after the grid has been created.
         * The function will call the super classes post create
         * then load the semantic tag service. The semantic tag service
         * is Javascript for creating business card functionality.
         * @method postCreate
         */
        postCreate: function() {        	
        	this.inherited(arguments);
        	
        	SemanticTagService.loadSemanticTagService();
        },
        
        /**
         * Creates a renderer for the grid.The renderer is responsible for 
         * loading the grid's HTML content.
         * @method createDefaultRenderer
         * @param args sets the template the renderer will use, by checking args.type, but for
         * profile grids this will always be "profile"
         * @returns an instance of a  profile gird renderer.
         */
        createDefaultRenderer : function(args) {
            return new ProfileGridRenderer(args);
        },
        
        /**
         * In the grid HTML an element can have an event attached 
         * using dojo-attach-event="onClick: handleClick".
         * This method is the handler for the onclick event.
         * @method handleClick
         * @param el the element that fired the event
         * @param data all of the items from the current row of the grid. 
         * @param ev the event 
         */
        handleClick: function(el, data, ev) {
            if (this.communityMembersAction) {
                this._stopEvent(ev);
                
                this.communityMembersAction.execute(data, { grid : this.grid }, ev);
            }
        },
        
        updateMember: function(el, data, ev) {
        	if (this.communityMembersAction) {
                this._stopEvent(ev);
   
                this.communityMembersAction.updateMember(this, el, data, ev, this.communityUuid);
        	}
        },
        
        removeMember: function(el, data, ev) {
        	if (this.communityMembersAction) {
                this._stopEvent(ev);
   
                this.communityMembersAction.removeMember(this, el, data, ev, this.communityUuid);
        	}
        },
        
        closeEditForm: function(el, data, ev) {
        	if (this.communityMembersAction) {
                this._stopEvent(ev);
                this.communityMembersAction.closeEditForm(this, el, data, ev);
        	}
        },
        
        openEditForm: function(el, data, ev) {
        	if (this.communityMembersAction) {
                this._stopEvent(ev);
                this.communityMembersAction.openEditForm(data);
        	}
        },
        
        /**
         * In the grid HTML an element can have an event attached 
         * using dojo-attach-event="onClick: removeMember".
         * This method is the handler for for the onclick event fired when
         * clicking the "remove member" link on a member row.
         * @method handleClick
         * @param el the element that fired the event
         * @param data all of the items from the current row of the grid. 
         * @param ev the event 
         */
        removeMember: function(el, data, ev) {
        	if (this.communityMembersAction) {
                this._stopEvent(ev);
                this.communityMembersAction.removeMember(this, this.communityUuid, data);
        	}
        },
        
        /**
         * @method getSortInfo
         * @returns A list of strings that describe how the grid can be sorted
         * for profile grids these strings are "Display Name" and "Recent"
         */
        getSortInfo: function() {
        	return {
        		active: {
        			anchor: this._activeSortAnchor,
        			isDesc: this._activeSortIsDesc
        		},
        		list: [this._sortInfo.displayName, this._sortInfo.recent]
            };
        	
        },
                
        sortByDisplayName: function(el, data, ev){
        	this._sort("displayName", true, el, data, ev);
        },

        sortByRecent: function(el, data, ev){
        	this._sort("recent", true, el, data, ev);
        },
        
        sortByCreated: function(el, data, ev){
        	this._sort("created", true, el, data, ev);
        },
        
        sortByTitle: function(el, data, ev){
        	this._sort("title", true, el, data, ev);
        }

        // Internals
        
    });

    return CommunityMembersGrid;
});
},
'sbt/connections/controls/communities/CommunityMembersGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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
        "../../../i18n!./nls/CommunityMembersGridRenderer",
        "../../../text!./templates/CommunityMemberRow.html"], 
        function(declare, ConnectionsGridRenderer, nls, communityMemberTemplate) {
		
    /**
     * @class ProfileGridRenderer
     * @namespace sbt.connections.controls.communities
     * @module sbt.connections.controls.communities.CommunityMembersGrid
     */
    var CommunityMembersGridRenderer = declare(ConnectionsGridRenderer, {

          /**The strings used in the grid, these are stored in a separate file, in the nls folder*/
         _nls: nls, 
        
         /**
          * The constructor function
          * @method constructor
          * @param args
          */
         constructor: function(args) {
             if (args.type == "communityMembers") {
            	 this.template = communityMemberTemplate;
             }
         },

         /**
          * Sets the css class for the row
          * @method rowClass
          * @param grid The Grid Dijit 
          * @param item the current row
          * @param i the number of the current row, ie 0, 1, 2 etc
          * @param items all of the rows in the grid
          */
         rowClass: function(grid, item, i, items) {
             item.rowClass = (i === 0 ? "lotusFirst" : (i % 2 === 1 ? "lotusAltRow" : null));
         },
         
         /**
          * Displays a tooltip by calling the getTooltip function in the ProfileAction class
          * @method tooltip
          * @param grid The Grid Dijit
          * @param item the element to display the tooltip
          * @param i the number of the current row
          * @param items all of the items in the grid row
          * @returns A Tooltip the default for profiles is to display the vCard
          */
         tooltip: function(grid, item, i, items) {
             if (grid.profileAction) {
                 return grid.profileAction.getTooltip(item);
             }
         },
         
         /**
          * Generates the profile photo URL for displaying the photos of community members
          * @method photoUrl
          * @param grid The Grid Dijit
          * @param item the element containing the uid
          * @param i the number of the current row
          * @param items all of the items in the grid row
          * @returns A profile photo URL for retrieving a user's profile picture
          */
         photoUrl: function(grid, item, i, items) {
        	 var store = grid.store;
        	 var endpoint = store.getEndpoint();
        	 var photoUrl = endpoint.baseUrl + "/profiles/photo.do?email=" + item.getValue("email");
      	 
        	 return photoUrl;
         }
    });
    
    return CommunityMembersGridRenderer;
});
},
'sbt/connections/ProfileConstants':function(){
/*
 * � Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Definition of constants for CommunityService.
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin(conn, {
        
        /**
         * Default size for the profile cache
         */
        DefaultCacheSize : 10,
        
        /**
         * Fields used to populate the Address object
         */
        AddressFields : [ "streetAddress", "extendedAddress", "locality", "region", "postalCode", "countryName" ],

        /**
         * XPath expressions used when parsing a Connections Profiles ATOM feed
         */
        ProfileFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * Connection type colleague
         */
        TypeColleague : "colleague",
        
        /**
         * Status flag
         */
        StatusPending : "pending",
        
        /**
         * XPath expressions to be used when reading a Profile Entry
         */
        ProfileXPath : {
            // used by getEntityData
            entry : "/a:feed/a:entry",
            // used by getEntityId
            uid : "a:contributor/snx:userid",
            // used by getters
            id : "a:id",
            userid : "a:contributor/snx:userid",
            name : "a:contributor/a:name",
            email : "a:contributor/a:email",
            title : "a:title",
            updated : "a:updated",
            altEmail : "a:content/h:div/h:span/h:div[@class='x-groupwareMail']", // TODO do we need this? it's a dupe of groupwareMail
            photoUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/image']/@href", 
            fnUrl : "a:content/h:div/h:span/h:div/h:a[@class='fn url']/@href",
            soundUrl : "a:content/h:div/h:span/h:div/h:a[@class='sound url']/@href",
            jobTitle : "a:content/h:div/h:span/h:div[@class='title']",
            organizationUnit : "a:content/h:div/h:span/h:div[@class='org']/h:span[@class='organization-unit']",
            telephoneNumber : "a:content/h:div/h:span/h:div[@class='tel']/h:span[@class='value']",
            building : "a:content/h:div/h:span/h:div/h:span[@class='x-building']",
            floor : "a:content/h:div/h:span/h:div/h:span[@class='x-floor']",
            officeNumber : "a:content/h:div/h:span/h:div/h:span[@class='x-office-number']",
            streetAddress : "a:content/h:div/h:span/h:div/h:div[@class='street-address']",
            extendedAddress : "a:content/h:div/h:span/h:div/h:div[@class='extended-address x-streetAddress2']",
            locality : "a:content/h:div/h:span/h:div/h:span[@class='locality']",
            postalCode : "a:content/h:div/h:span/h:div/h:span[@class='postal-code']",
            region : "a:content/h:div/h:span/h:div/h:span[@class='region']",
            countryName : "a:content/h:div/h:span/h:div/h:div[@class='country-name']",
            summary : "a:summary",
            groupwareMail : "a:content/h:div/h:span/h:div[@class='x-groupwareMail']",
            blogUrl : "a:content/h:div/h:span/h:div/h:a[@class='x-blog-url url']/@href",
            role : "a:content/h:div/h:span/h:div[@class='role']",
            managerUid : "a:content/h:div/h:span/h:div[@class='x-manager-uid']",
            isManager : "a:content/h:div/h:span/h:div[@class='x-is-manager']"
        },
        
        /**
         * XPath expressions to be used when reading a ColleagueConnection Entry
         */
        ColleagueConnectionXPath : {
        	entry : "/a:feed/a:entry",
        	uid : "a:id",
        	id : "a:id",
            updated : "a:updated",
            authorUserid : "a:author/snx:userid",
            contributorUserid : "a:contributor/snx:userid",
            authorName : "a:author/snx:name",
            contributorName : "a:contributor/snx:name",
            authorEmail : "a:author/snx:email",
            contributorEmail : "a:contributor/snx:email",
            title : "a:title",
            content : "a:content",
            selfLink : "a:link[@rel='self']/@href", 
            editLink : "a:link[@rel='edit']/@href"
        },
        
        /**
         * XPath expressions to be used when reading a Community Entry with VCard content
         */
        ProfileVCardXPath : {
            // used by getEntityData
            entry : "/a:feed/a:entry",
            // used by getEntityId
            uid : "a:contributor/snx:userid",
            // used by parseVCard
            vcard : "a:content",
            // used by getters
            id : "a:id",
            userid : "a:contributor/snx:userid",
            name : "a:contributor/a:name",
            email : "a:contributor/a:email",
            title : "a:title",
            updated : "a:updated",
            altEmail : "EMAIL;X_GROUPWARE_MAIL", // TODO do we need this? it's a dupe of groupwareMail
            photoUrl : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/image']/@href",
            fnUrl : "URL",
            soundUrl : "SOUND;VALUE=URL",
            jobTitle : "TITLE",
            organizationUnit : "a:content/h:div/h:span/h:div[@class='org']/h:span[@class='organization-unit']",
            telephoneNumber : "TEL;WORK",
            building : "X_BUILDING",
            floor : "X_FLOOR",
            officeNumber : "X_OFFICE_NUMBER",
            workLocation : "ADR;WORK",
            streetAddress : "a:content/h:div/h:span/h:div/h:div[@class='street-address']",
            extendedAddress : "a:content/h:div/h:span/h:div/h:div[@class='extended-address x-streetAddress2']",
            locality : "a:content/h:div/h:span/h:div/h:span[@class='locality']",
            postalCode : "a:content/h:div/h:span/h:div/h:span[@class='postal-code']",
            region : "a:content/h:div/h:span/h:div/h:span[@class='region']",
            countryName : "a:content/h:div/h:span/h:div/h:div[@class='country-name']",
            summary : "a:summary",
            groupwareMail : "EMAIL;X_GROUPWARE_MAIL"
        },
        
        /**
         * XPath expressions to be used when reading a Profile Tag feed
         */
        ProfileTagsXPath : {
        	// used by getEntitiesDataArray
        	entries : "/app:categories/a:category",
        	// used to access data from the feed
        	targetEmail : "app:categories/snx:targetEmail",
        	numberOfContributors : "app:categories/@snx:numberOfContributors",
            // used by getEntityData
            entry : "/app:categories/a:category",
            // used by getEntityId
            uid : "@term",
            // used by getters
            id : "@term",
            term : "@term",
            frequency : "@snx:frequency",
            intensity : "@snx:intensityBin", 
            visibility : "@snx:visibilityBin",
            contributorName : "a:name",
            contributorUserid : "a:userid",
            contributorEmail : "a:email"
        },
        
        /**
         * XPath expressions to be used when reading an invite entry
         */
        InviteXPath : lang.mixin({
            connectionType: "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/connection/type']/@term",
            status: "a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/status']/@term"
        }, conn.AtomEntryXPath),
        
        /**
         * XML elements to be used when creating a Profile Entry
         *                    
         **/        
        profileCreateAttributes : {
			guid : "com.ibm.snx_profiles.base.guid",
			email : "com.ibm.snx_profiles.base.email",
			uid : "com.ibm.snx_profiles.base.uid",
			distinguishedName : "com.ibm.snx_profiles.base.distinguishedName",
			displayName : "com.ibm.snx_profiles.base.displayName",
			givenNames : "com.ibm.snx_profiles.base.givenNames",
			surname : "com.ibm.snx_profiles.base.surname",
			userState :"com.ibm.snx_profiles.base.userState"
		},
        
		/**
         * Retrieve a profile entry.
         */
        AtomProfileDo : "/${profiles}{authType}/atom/profile.do",
        
        /**
         * Update a profile entry.
         */
        AtomProfileEntryDo : "/${profiles}{authType}/atom/profileEntry.do",
        
        /**
         * Retrieve a feed that lists the contacts that a person has designated as colleagues.
         */
        AtomConnectionsDo : "/${profiles}{authType}/atom/connections.do",
        
        /**
         * Retrieve the profiles of the people who comprise a specific user's report-to chain.
         */
        AtomReportingChainDo : "/${profiles}{authType}/atom/reportingChain.do",
        
        /**
         * Retrieve the people managed by a specified person.
         */
        AtomPeopleManagedDo : "/${profiles}{authType}/atom/peopleManaged.do",
        
        /**
         * Retrieve status updates for a specified person.
         */
        AtomConnectionsInCommonDo : "/${profiles}{authType}/atom/connectionsInCommon.do",
        
        /**
         * Search for a set of profiles that match a specific criteria and return them in a feed.
         */
        AtomSearchDo : "/${profiles}{authType}/atom/search.do",
        
        /**
         * Retrieve the profiles of the people who report to a specific user. 
         */
        AtomPeopleManagedDo : "/${profiles}{authType}/atom/peopleManaged.do",
        
        /**
         * Retrieve the tags assigned to a profile from the Profiles tag collection.
         */
        AtomTagsDo : "/${profiles}{authType}/atom/profileTags.do",
        
        /**
         * Admin API - create a new profile.
         */
        AdminAtomProfileDo : "/${profiles}/admin/atom/profiles.do",
        
        /**
         * Admin API - delete a  profile.
         */
        AdminAtomProfileEntryDo : "/${profiles}/admin/atom/profileEntry.do"
        
    });
});
},
'sbt/connections/controls/files/FileAction':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../../declare", "../../../stringUtil", "../../../controls/grid/GridAction" ], 
        function(declare, stringUtil, GridAction) {

    /**
     * @class FileAction
     * @namespace sbt.connections.controls.files
     * @module sbt.connections.controls.files.FileAction
     */
    var FileAction = declare(GridAction, {
        
    	/**Strings*/
        nls: {
            tooltip: "Click to download ${title}"
        },
       
        /**Constructor function
         * @method constructor
         *  */
        constructor: function() {
        },
        
        /**function to get the string to be displayed in an elements tooltip
         * @method getTooltip
         * @param item The element for which the tolltip will be displayed
         * @return A String, with the text to be displayed in the elements tooltip
         * */
        getTooltip: function(item) {
        	return stringUtil.replace(this.nls.tooltip, { title : item.getValue("title") });
        },
        
        /**
         * Execute function provides the default action for files
         * This function is called from the handle click function.
         * @method execute
         * @param item the element that fired the event
         * @param opts
         * @param event the event
         */
        execute: function(item, opts, event) {
        	window.open(item.getValue("fileUrl"));
        }

    });

    return FileAction;
});

},
'sbt/connections/controls/files/FileGrid':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../../declare",
         "../../../lang",
         "../../../dom",
         "../../../stringUtil",
         "../../../config",  
         "../../../store/parameter",
         "../../../controls/grid/Grid", 
         "./FileGridRenderer", 
         "./FileAction",
         "../../../connections/controls/vcard/SemanticTagService", 
         "../../../connections/FileService",
         "../../../connections/FileConstants"], 
        function(declare, lang, dom, stringUtil, sbt, parameter, Grid, FileGridRenderer, FileAction, SemanticTagService, FileService, FileConstants) {

	// TODO use values from constants and handle authType
	var xpath_files = {
		"id" : "id",
		"uuid" : "td:uuid",
		"title" : "a:title",
		"label" : "td:label",
		"fileSize" : "td:totalMediaSize",
		"fileUrl" : "a:link[@rel='alternate']/@href",
		"downloadUrl" : "a:link[@rel='edit-media']/@href",
		"tags" : "a:category/@term",
		"summary" : "a:summary[@type='text']",
		"content" : "a:content[@type='html']",
		"visibility" : "td:visibility",
		"notification" : "td:notification",
		"versionUuid" : "td:versionUuid",
		"versionLabel" : "td:versionLabel",
		"documentVersionUuid" : "td:documentVersionUuid",
		"documentVersionLabel" : "td:documentVersionLabel",
		"shareCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/share']",
		"commentCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']",
		"hitCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/hit']",
		"anonymousHitCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/anonymous_hit']",
		"attachmentCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/attachments']",
		"referenceCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/references']",
		"recommendationCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']",
		"collectionCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/collections']",
		"versionCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/versions']",
		"modified" : "td:modified",
		"created" : "td:created",
		"published" : "a:published",
		"updated" : "a:updated",
		"authorName" : "a:author/a:name",
		"authorUid" : "a:author/snx:userid",
		"authorEmail" : "a:author/a:email",
		"modifierName" : "a:modifier/a:name",
		"modifierId" : "a:modifier/snx:userid",
		"modifierEmail" : "a:modifier/a:email"
	};
	var xpath_folders = {
		"id" : "id",
		"uuid" : "td:uuid",
		"title" : "a:title",
		"label" : "td:label",
		"folderUrl" : "a:link[@rel='alternate']/@href",
		"logoUrl" : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href",
		"tags" : "a:category/@term",
		"summary" : "a:summary[@type='text']",
		"content" : "a:content[@type='html']",
		"visibility" : "td:visibility",
		"notification" : "td:notification",
		"versionUuid" : "td:versionUuid",
		"versionLabel" : "td:versionLabel",
		"documentVersionUuid" : "td:documentVersionUuid",
		"documentVersionLabel" : "td:documentVersionLabel",
		"itemCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/item']",
		"shareCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/user']",
		"groupShareCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/group']",
		"modified" : "td:modified",
		"created" : "td:created",
		"updated" : "a:updated",
		"authorName" : "a:author/a:name",
		"authorUid" : "a:author/snx:userid",
		"authorEmail" : "a:author/a:email",
		"content" : "a:content[@type='text']",
		"modifierName" : "a:modifier/a:name",
		"modifierId" : "a:modifier/snx:userid",
		"modifierEmail" : "a:modifier/a:email"
	};
	var xpath_comments = {
		"id" : "a:id",
		"uuid" : "td:uuid",
		"title" : "a:title",
		"content" : "a:content[@type='text']",
		"created" : "td:created",
		"modified" : "td:modified",
		"versionLabel" : "td:versionLabel",
		"updated" : "a:updated",
		"published" : "a:published",
		"modifierName" : "a:modifier/a:name",
		"modifierId" : "a:modifier/snx:userid",
		"modifierEmail" : "a:modifier/a:email",
		"authorName" : "a:author/a:name",
		"authorUid" : "a:author/snx:userid",
		"authorEmail" : "a:author/a:email"
	};
	var xpath_shares = {
		"id" : "id",
		"uuid" : "td:uuid",
		"title" : "a:title",
		"summary" : "a:summary[@type='text']",
		"sharedResourceType" : "td:sharedResourceType", // always set to document, but will add the attribute anyway
		"sharePermission" : "td:sharePermission",
		"sharedWhat" : "td:sharedWhat",
		"sharedWithName" : "a:sharedWith/a:name",
		"sharedWithId" : "a:sharedWith/snx:userid",
		"sharedWithEmail" : "a:sharedWith/a:email",
		"documentOwner" : "td:documentOwner",
		"updated" : "a:updated",
		"published" : "a:published",
		"authorName" : "a:author/a:name",
		"authorUid" : "a:author/snx:userid",
		"authorEmail" : "a:author/a:email"
	};
	var sortVals = {
			name: "title",
        	updated: "modified",
        	downloads: "downloaded",
        	comments: "commented",
        	likes: "recommended",
        	files: "itemCount",
        	created: "created",
        	modified: "modified"
	};
	
	var ParamSchema = {
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortBy",sortVals),
		sortOrder: parameter.sortOrder("sortOrder")				
	};
	
    /**
     * @class FileGrid
     * @namespace sbt.connections.controls.files
     * @module sbt.connections.controls.files.FileGrid
     */
   var FileGrid =  declare(Grid, {

    	gridSortType: "",
    	fileService: null,
    	
    	/**
    	 * Options determine which type of file grid will be created
    	 */
        options : {
            "library" : {
                storeArgs : {
                    url : FileConstants.AtomFilesMy,
                    attributes : xpath_files,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "file"
                }
            },
            "publicFiles" : {
                storeArgs : {
                    url : FileConstants.AtomFilesPublic,
                    attributes : xpath_files,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "file"
                }
            },
            "pinnedFiles" : {
                storeArgs : {
                    url : FileConstants.AtomFilesMyPinned,
                    attributes : xpath_files,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "file"
                }
            },
            "folders" : {
                storeArgs : {
                    url : FileConstants.AtomFoldersMy,
                    attributes : xpath_folders,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "folder"
                }
            },
            "publicFolders" : {
                storeArgs : {
                    url : FileConstants.AtomFoldersPublic,
                    attributes : xpath_folders,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "folder"
                }
            },
            "pinnedFolders" : {
                storeArgs : {
                    url : FileConstants.AtomFilesMyPinned,
                    attributes : xpath_folders,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "folder"
                }
            },
            "activeFolders" : {
                storeArgs : {
                    url : FileConstants.AtomFoldersActive,
                    attributes : xpath_folders,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "folder"
                }
            },
            "shares" : {
                storeArgs : {
                    url : FileConstants.AtomFilesShared,
                    attributes : xpath_files,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "file"
                }
            },
            "recycledFiles" : {
                storeArgs : {
                    url : FileConstants.AtomFilesRecycled,
                    attributes : xpath_files,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "recycledFile"
                }
            },
            "fileComments" : {
                storeArgs : {
                    url : FileConstants.AtomFileCommentsMy,
                    attributes : xpath_comments,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "comment"
                }
            },
            "fileShares" : {
                storeArgs : {
                    url : FileConstants.AtomFilesShared,
                    attributes : xpath_files,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "file"
                }
            }

        },
        
        contextRootMap: {
            files: "files"
        },
        
        /**
         * The default grid, if no options are selected
         */
        defaultOption: "publicFiles",
        
        /**
         * FileAction defines the default actions for files, which can be overridden 
         */
        fileAction: new FileAction(),
        	
        /**
         * Constructor function
         * @method constructor
         */
        constructor: function(args){

        	this.fileService = new FileService(this.endpointName || "connections");
        	
        	/**gridSortType is used to determine what sorting anchors should be used,
        	 * for example folders have different sort anchors than files, file comments have no anchors etc*/
        	if(args.type=="fileShares" || args.type == "library" || args.type == "pinnedFiles"){
        		gridSortType = "file";
        	}else if(args.type == "fileComments" || args.type == "recycledFiles"){
        		gridSortType= "";
        	}else if(args.type == "publicFiles"){
        		gridSortType="publicFiles";
        	}else{
        		gridSortType="folder";
        	}

            var nls = this.renderer.nls;
            this._sortInfo = {
                name: { 
                    title: nls.name, 
                    sortMethod: "sortByName",
                    sortParameter: "name"
                },
                updated: {
                    title: nls.updated, 
                    sortMethod: "sortByLastUpdated",
                    sortParameter: "updated"
                },
                downloads: { 
                    title: nls.downloads, 
                    sortMethod: "sortByDownloads",
                    sortParameter: "downloads"
                },
                comments: { 
                    title: nls.comments, 
                    sortMethod: "sortByComments",
                    sortParameter: "comments"
                },
                likes: { 
                    title: nls.likes, 
                    sortMethod: "sortByLikes",
                    sortParameter: "likes"
                },
                created:{
                	title: nls.created,
                	sortMethod: "sortByCreatedDate",
                    sortParameter: "created"
                },
                files:{
                	title: nls.files,
                	sortMethod: "sortByNumberOfFiles",
                    sortParameter: "files"
                }
            };

            if(args.type == "publicFiles"){
            	this._activeSortAnchor = this._sortInfo.created;
            	this._activeSortIsDesc = false;  
            }else if(args.type == "folders"){
            	this._activeSortAnchor = this._sortInfo.name;
            	this._activeSortIsDesc = true;  
            }else {
            	this._activeSortAnchor = this._sortInfo.updated; 
            	this._activeSortIsDesc = true;  
            }
            
            if(args && args.pinFile){
            	this.renderer.pinFiles = args.pinFile;
            }
              
        },
        
        /**
         * Override buildUrl to add direction, userId and fileId
         * 
         * @method buildUrl
         * @param url base url
         * @param args arguments that will be passed to the store
         * @param endpoint An endpoint which may contain custom service mappings.
         * @returns Built url
         */
        buildUrl: function(url, args, endpoint) {
            var params = { format : this.format };
            
            if (this.query) {
            	params = lang.mixin(params, this.query);
            }
            if (this.direction) {
            	params = lang.mixin(params, { direction : this.direction });
            } 

            return this.constructUrl(url, params, this.getUrlParams(), endpoint);
        },
        
        /**
         * Return the url parameters to be used
         * @returns {Object}
         */
        getUrlParams: function() {
        	var params = { authType : this.getAuthType() };
        	
        	if (this.userId) {
            	params = lang.mixin(params, { userId : this.userId });
            } 
            if (this.documentId) {
            	params = lang.mixin(params, { documentId : this.documentId });
            }
           
            return params;
        },
        
        /**
         * Instantiates a FileGridRenderer
         * @method createDefaultRenderer
         * @param args
         * @returns {FileGridRenderer}
         */
        createDefaultRenderer : function(args) {
            return new FileGridRenderer(args);
        },
        
        /**
         * Called after the grid is created
         * The semanticTagService is loaded, which is responsible for displaying business card functionality.
         * @method postCreate
         */
        postCreate: function() {        	
        	this.inherited(arguments);
        	SemanticTagService.loadSemanticTagService();
        },
        
        /**
         * Event handler for onClick events
         * @method handleClick
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        handleClick: function(el, data, ev) {
            if (this.fileAction) {
                this._stopEvent(ev);
                
                this.fileAction.execute(data, { grid : this.grid }, ev);
            }
        },
        
        /**
         * @method getSortInfo
         * @returns A List of Strings,that describe how the grid can be sorted
         */        
        getSortInfo: function() {
            if(gridSortType == "file"){

	        	return {
	                active: {
	                    anchor: this._activeSortAnchor,
	                    isDesc: this._activeSortIsDesc
	                },
	                list: [this._sortInfo.name, this._sortInfo.updated, this._sortInfo.downloads, this._sortInfo.comments, this._sortInfo.likes]
	            };
            }else if (gridSortType == "folder"){
            	return {
	                active: {
	                    anchor: this._activeSortAnchor,
	                    isDesc: this._activeSortIsDesc
	                },
	                list: [this._sortInfo.name, this._sortInfo.updated, this._sortInfo.created,this._sortInfo.files]
	            };
            }else if(gridSortType == "publicFiles"){
            	return {
	                active: {
	                    anchor: this._activeSortAnchor,
	                    isDesc: this._activeSortIsDesc
	                },
	                list: [ this._sortInfo.created, this._sortInfo.downloads, this._sortInfo.comments, this._sortInfo.likes]
	            };
            }
        },
        
        /**
         * Sort the grid rows by name
         * @method  sortByName
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByName: function(el, data, ev) {
            this._sort("name", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by last modified date
         * @method  sortByLastUpdated
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByLastUpdated: function(el, data, ev) {
            this._sort("updated", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by the amount of times a file has been downloaded. 
         * @method  sortByDownloads
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByDownloads: function(el, data, ev) {
            this._sort("downloads", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by the amount of comments a file has.
         * @method sortByComments
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByComments: function(el, data, ev) {
            this._sort("comments", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by the number of "likes" that a file has. 
         * @method  sortByLikes
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByLikes: function(el, data, ev) {
            this._sort("likes", true, el, data, ev);
        },
        
        /**
         * Sort the grid rows by when the files were first created
         * @method  sortByCreatedDate
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByCreatedDate:function(el, data, ev) {
            this._sort("created", true, el, data, ev);
        },
        
        /**
         * Sorts the grid, based on the number of files contained in each folder.
         * This is for grids that display folders.
         * @method sortByNumberOfFiles
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        sortByNumberOfFiles: function(el, data, ev) {
            this._sort("files", true, el, data, ev);
        },
       
        /**
         * Event handler to show and hide the more options in the files grid
         * @method showMore
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        showMore: function(el, data, ev){
        	/**TODO to be implemented in iteration 9 */	
        },
        
        /**
         * @method onUpdate This is called after the grid is updated
         * In this implementation, a list of pinned files is received from the server
         * then all of the pin file links, are retrieved by class name,
         * then if the file is a pinned file, its css class will be changed to reflect this 
         * NOTE: this function will only execute, if file pin functionality is passed as an argument to the grid
         */
        onUpdate: function(){
        	
	        if(this.renderer.pinFiles){
	        	
	        	//Get all of the pin file img tags, we do this by classname
	        	var pinElements = document.getElementsByClassName(this.renderer.unPinnedClass);
	        	//ids will hold the ID of each element, the id of the element is the uuid of the file.
	        	var ids = [];
	        	//set the Ids into the ids array
	        	for(var x =0;x <pinElements.length;x++){
	        		ids[x] = pinElements[x].id;
	        	}
	            
	        	//create an args object containing these three vars to hitch. 
	        	var pinClass = this.renderer.pinnedClass;
	            var unPinnedClass = this.renderer.unPinnedClass
	            var renderer = this.renderer;
	            var args = {pinClass:pinClass,unPinnedClass:unPinnedClass,ids:ids,renderer:renderer};
	            
	        	//we use the array of ids, and not the array of elements
	        	//because as we remove a class from an element, the array of elements will dynamically reduce
	        	this.renderer._hitch(args,this.fileService.getPinnedFiles().then(
	        		function(files) {
	        			for(var k=0;k<args.ids.length;k++){
	        				for(var i=0;i<files.length;i++){
	        					if(args.ids[k] == files[i].getId()){
	        						args.renderer._removeClass(args.ids[k],args.unPinnedClass);
	        						args.renderer._addClass(args.ids[k],args.pinClass);
	        					}
	        				}
	        			}
	        		},
	        		function(error) {
	        			console.log("error getting pinned files");
	        		}
	        	));
	        	
	          }
        },	
        
        /**This function pins(favourites) a file,
         * It will send a request to the server using the file service API,
         * And when the request returns successfully, the css clas
         * of the link will be change to reflect that the file is now pinned. 
         * If the file is already pinned, it will remove the pin from the file.
         * @method doPinFile
         * @param el The element that fired the event
         * @param data The data associated with this table row
         * @param ev The event, onclick
         */
        doPinFile: function(el, data, ev){
        	var uuid = "";
        	if(data.uuid){
        		uuid = data.uuid;
        	}else if(data._attribs.uuid){
        		uuid = data._attribs.uuid;
        	}
        	
        	//create an args object containing these three vars to hitch. 
        	var pinClass = this.renderer.pinnedClass;
            var unPinnedClass = this.renderer.unPinnedClass;
            var renderer = this.renderer;
            var args = {pinClass:pinClass,unPinnedClass:unPinnedClass,el:el,renderer:renderer};
        	
        	if(el.firstElementChild.className == this.renderer.unPinnedClass){
	        	 this.renderer._hitch(args,this.fileService.pinFile(uuid).then(
	        		function(response) {
	        			args.renderer._removeClass(args.el.firstElementChild, args.unPinnedClass);
	        			args.renderer._addClass(args.el.firstElementChild, args.pinClass);	
	
	        		},
	        		function(response) {
	        			console.log("Error pinning file");
	        		}
	        	));
        	} else if (el.firstElementChild.className == this.renderer.pinnedClass){
        		this.renderer._hitch(args,this.fileService.unpinFile(uuid).then(
        			function(data) {
        				args.renderer._removeClass(args.el.firstElementChild, args.pinClass);
        				args.renderer._addClass(args.el.firstElementChild, args.unPinnedClass);	
        			},
        			function(error) {
        				console.log("error removing pin from file");
        			}
        		));
        	}
        	
        }

        // Internals

    });

    return FileGrid;
});

},
'sbt/connections/controls/files/FileGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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
        "../../../stringUtil", 
        "../../../i18n",
        "../ConnectionsGridRenderer",
        "../../../i18n!./nls/FileGridRenderer",
        "../../../text!./templates/FileRow.html",
        "../../../text!./templates/RecycledFileRow.html",
        "../../../text!./templates/FolderRow.html", 
        "../../../text!./templates/CommentRow.html"], 
        function(declare, stringUtil, i18n, ConnectionsGridRenderer, nls, fileTemplate, recycledFileTemplate, folderTemplate, commentTemplate) {
    
    /**
     * @class FileGridRenderer
     * @namespace sbt.connections.controls.files
     * @module sbt.connections.controls.files.FileGridRenderer
     */
    var FileGridRenderer = declare(ConnectionsGridRenderer, {
    	
    	/**
    	 * Strings for the Grid
    	 */
        _nls: nls,
        
        pinFiles: false,
        pinnedClass: "lconnSprite lconnSprite-iconPinned16-on",
        unPinnedClass: "lconnSprite lconnSprite-iconPinned16-off",
    
         /**
          * @method constructor
          * @param args, setting args.type, will set the appropriate template
          */
         constructor: function(args) {
             if (args.type == "file") {
                 this.template = fileTemplate;
             } else if (args.type == "recycledFile") {
                 this.template = recycledFileTemplate;
             } else if (args.type == "folder") {
                 this.template = folderTemplate;
             } else if (args.type == "comment"){
                 this.template = commentTemplate;
             }
         },
         
         /**
          * Sets the CSS Class for each row
          * @method renderItem
          * @param grid The Grid Element
          * @param el The Table Body Element, that contains the rows of the grid
          * @param data An object containing data for all of the rows
          * @param item An object containing all of the items in the current row
          * @param i the number of the current grid row
          * @param items An Object Containing data for all of the rows
          */
         renderItem: function(grid, el, data, item, i, items) {
             item.rowClass = (i === 0 ? "lotusFirst" : (i % 2 === 1 ? "lotusAltRow" : null));
             this.inherited(arguments);
         },
         
         /**
          * Gets the tooltip text to be displayed for a HTML element
          * @method tooltip
          * @param grid The grid Element
          * @param item The item for which the tolltip is displayed
          * @param i A number representing Current Grid row
          * @param items All of the rows in the grid
          * @returns A String containing the Tooltip text 
          */
         tooltip: function(grid, item, i, items) {
             if (grid.fileAction) {
                 return grid.fileAction.getTooltip(item);
             }
         },
         
         /**
          * Functions that returns the date a file was created, as a String
          * @method createdLabel
          * @param grid The Grid Element
          * @param item The HTML element/Item which will use the label
          * @param i A Number representing the current grid row
          * @param items An object containg all of the Items in each grid row
          * @returns A String, when the File was created
          */
         createdLabel: function(grid, item, i, items){
             var result = i18n.getUpdatedLabel(item.getValue('created'));
             return result;
         },
         
         formattedContent: function(grid, item, i, items){
             var result = item.getValue('content').replace("\n", "<br><br>");
             return result;
         },
         
         /**
          * Returns a String, containing when the file was last modified
          * @method modifiedLabel
          * @param grid The Grid
          * @param item The HTML element which will use the string
          * @param i the Number of a the current grid row
          * @param items An Object containing data for each grid row
          * @returns String, Last modified date
          */
         modifiedLabel: function(grid, item, i, items){
             var result = i18n.getUpdatedLabel(item.getValue('modified'));
             return result;
         },
         
         /**
          * Returns a String, containing when the file was last "edited"
          * @method commentEditedLabel
          * @param grid The Grid
          * @param item The HTML element which will use the string
          * @param i the Number of a the current grid row
          * @param items An Object containing data for each grid row
          * @returns String, Last modified date
          */
         commentEditedLabel: function(grid, item, i, items){
             var result = "";
             var modified = i18n.getUpdatedLabel(item.getValue('modified'));
             var created = i18n.getUpdatedLabel(item.getValue('created'));
             
             if(modified != created)
                 result = "Edited " + modified;
             return result;
         },
         /**
          * Returns a label to say if the file is public, private or shared 
          * @method shareLabel
          * @param grid The Grid Element
          * @param item An object containing all of the data in the row
          * @param i The Number of the current row
          * @param items An object containing all of the data for each row.
          * @returns A Label
          */
         shareLabel: function(grid, item, i, items) {
             return item.getValue('visibility')=='shared' ? 'shared with ' + item.getValue('shareCount') : item.getValue('visibility');
         },
         
         /**
          * Returns the visibility of a file, for example shared, private etc.
          * @method visibilityLabel
          * @param grid The Grid Element
          * @param item An object containing all of the data in the row
          * @param i The Number of the current row
          * @param items An object containing all of the data for each row.
          * @returns The Visibility Label
          */
         visibilityLabel: function(grid, item, i, items){
             var visibility = item.getValue('visibility');
             return visibility.charAt(0).toUpperCase() + visibility.slice(1);
         },
         
         /**
          * Returns the Full UTC time for when a file was created
          * @method detailedCreated
          * @param grid The Grid Element
          * @param item An object containing all of the data in the row
          * @param i The Number of the current row
          * @param items An object containing all of the data for each row.
          * @returns
          */
         detailedCreated: function(grid, item, i, items){
             return (new Date(item.getValue('created'))).toUTCString();
         },
         
         /**
          * Returns the file type, so an appropriate icon can be used
          * @method ftype
          * @param grid The Grid Element
          * @param item An object containing all of the data in the row
          * @param i The Number of the current row
          * @param items An object containing all of the data for each row.
          * @returns The File Type, A String containing the extension
          */
         ftype: function(grid, item, i, items) {
             return item.getValue('title').slice(-3);
         },
         
         /**
          * Returns the label, for the "Likes icon"
          * If there are zero "likes" it will display "No one likes this"
          * If there is one like, "One person likes this" etc.
          * @method recommendationLabel
          * @param grid The Grid Element
          * @param item An object containing all of the data in the row
          * @param i The Number of the current row
          * @param items An object containing all of the data for each row.
          * @returns A String to be used as a label for the "likes icon"
          */
         recommendationLabel : function(grid, item, i, items) {
            if (item.getValue('recommendationCount') == 0) {
                return this._nls.noLikes;
            } else if (item.getValue('recommendationCount') == 1) {
                return this._nls.oneLike;
            } else {
                return stringUtil.replace(this._nls.nLikes, item);
            }
        },
        
        pinFileOnOrOff: function(grid, item, i, items){
        	if(!this.pinFiles){
        		return "";
        	}else if (this.pinFiles){
        		return this.unPinnedClass;
        	}
        	
        },
        
        getUserProfileHref: function(grid,item,i,items){
       	 return this.getProfileUrl(grid,item.getValue("authorUid"));
        }

       
    });
    
    return FileGridRenderer;
});
},
'sbt/connections/FileService':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * JavaScript API for IBM Connections File Service.
 * 
 * @module sbt.connections.FileService
 */

define(
		[ "../declare", "../lang", "../stringUtil", "../Promise", "./FileConstants", "../base/BaseService", "../base/BaseEntity", "../base/XmlDataHandler",
				"../config", "../util", "../xml" ],
		function(declare, lang, stringUtil, Promise, consts, BaseService, BaseEntity, XmlDataHandler, config, util, xml) {

			var FolderTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\">${getCategory}${getId}${getFolderLabel}${getTitle}${getSummary}${getVisibility}${getVisibilityShare}</entry>";
			var FolderLabelTmpl = "<label xmlns=\"urn:ibm.com/td\" makeUnique=\"true\">${label}</label>";
			var FileVisibilityShareTmpl = "<sharedWith xmlns=\"urn:ibm.com/td\"> <member xmlns=\"http://www.ibm.com/xmlns/prod/composite-applications/v1.0\" ca:type=\"${shareWithWhat}\" xmlns:ca=\"http://www.ibm.com/xmlns/prod/composite-applications/v1.0\" ca:id=\"${shareWithId}\" ca:role=\"${shareWithRole}\"/> </sharedWith>";
			var FileFeedTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><feed xmlns=\"http://www.w3.org/2005/Atom\">${getEntries}</feed>";
			var FileEntryTmpl = "<entry xmlns=\"http://www.w3.org/2005/Atom\">${getCategory}${getId}${getUuid}${getLabel}${getTitle}${getSummary}${getVisibility}${getItem}${getTags}${getNotification}</entry>";
			var FileItemEntryTmpl = "<entry>${getCategory}${getItem}</entry>";
			var FileCommentsTmpl = "<entry xmlns=\"http://www.w3.org/2005/Atom\">${getCategory}${getDeleteComment}${getContent}</entry>";
			var FileCategoryTmpl = "<category term=\"${category}\" label=\"${category}\" scheme=\"tag:ibm.com,2006:td/type\"/>";
			var FileContentTmpl = "<content type=\"text/plain\">${content}</content>";
			var FileDeleteCommentTmpl = "<deleteWithRecord xmlns=\"urn:ibm.com/td\">${deleteWithRecord}</deleteWithRecord>";
			var FileIdTmpl = "<id>${id}</id>";
			var FileUuidTmpl = "<uuid xmlns=\"urn:ibm.com/td\">${uuid}</uuid>";
			var FileLabelTmpl = "<label xmlns=\"urn:ibm.com/td\">${label}</label>";
			var FileTitleTmpl = "<title>${title}</title>";
			var FileSummaryTmpl = "<summary type=\"text\">${summary}</summary>";
			var FileVisibilityTmpl = "<visibility xmlns=\"urn:ibm.com/td\">${visibility}</visibility>";
			var FileItemIdTmpl = "<itemId xmlns=\"urn:ibm.com/td\">${fileId}</itemId>";
			var TagsTmpl = "<category term=\"${tag}\" /> ";
			var NotificationTmpl = "<td:notification>${notification}</td:notification>";

			/**
			 * Comment class associated with a file comment.
			 * 
			 * @class Comment
			 * @namespace sbt.connections
			 */
			var Comment = declare(BaseEntity, {

				/**
				 * Returned the Comment Id
				 * 
				 * @method getCommentId
				 * @rturns {String} File Id
				 */
				getCommentId : function() {
					return this.id || this.getAsString("uid");
				},
				/**
				 * Returns Comment Title
				 * 
				 * @method getTitle
				 * @returns {String} title
				 */
				getTitle : function() {
					return this.getAsString("title");
				},
				/**
				 * Returns the Comment Content
				 * 
				 * @method getContent
				 * @returns {String} content
				 */
				getContent : function() {
					return this.getAsString("content");
				},
				/**
				 * Returns The create Date
				 * 
				 * @method getCreated
				 * @returns {Date} create Date
				 */
				getCreated : function() {
					return this.getAsDate("created");
				},
				/**
				 * Returns The modified Date
				 * 
				 * @method getModified
				 * @returns {Date} modified Date
				 */
				getModified : function() {
					return this.getAsDate("modified");
				},
				/**
				 * Returns the version label
				 * 
				 * @method getVersionLabel
				 * @returns {String} version label
				 */
				getVersionLabel : function() {
					return this.getAsString("versionLabel");
				},
				/**
				 * Returns the updated Date
				 * 
				 * @method getModified
				 * @returns {Date} modified Date
				 */
				getUpdated : function() {
					return this.getAsDate("updated");
				},
				/**
				 * Returns the published Date
				 * 
				 * @method getPublished
				 * @returns {Date} modified Date
				 */
				getPublished : function() {
					return this.getAsDate("published");
				},
				/**
				 * Returns the modifier
				 * 
				 * @method getModifier
				 * @returns {Object} modifier
				 */
				getModifier : function() {
					return this.getAsObject([ "modifierName", "modifierUserId", "modifierEmail", "modifierUserState" ]);
				},
				/**
				 * Returns the author
				 * 
				 * @method getAuthor
				 * @returns {Object} author
				 */
				getAuthor : function() {
					return this.getAsObject([ "authorName", "authorUserId", "authorEmail", "authorUserState" ]);
				},
				/**
				 * Returns the language
				 * 
				 * @method getLanguage
				 * @returns {String} language
				 */
				getLanguage : function() {
					return this.getAsString("language");
				},
				/**
				 * Returns the flag for delete with record
				 * 
				 * @method getDeleteWithRecord
				 * @returns {Boolean} delete with record
				 */
				getDeleteWithRecord : function() {
					return this.getAsBoolean("deleteWithRecord");
				}
			});

			/**
			 * File class associated with a file.
			 * 
			 * @class File
			 * @namespace sbt.connections
			 */
			var File = declare(BaseEntity, {

				/**
				 * Returns the file Id
				 * 
				 * @method getFileId
				 * @returns {String} file Id
				 */
				getFileId : function() {
					return this.id || this._fields.id || this.getAsString("uid");
				},
				/**
				 * Returns the label
				 * 
				 * @method getLabel
				 * @returns {String} label
				 */
				getLabel : function() {
					return this.getAsString("label");
				},
				/**
				 * Returns the self URL
				 * 
				 * @method getSelfUrl
				 * @returns {String} self URL
				 */
				getSelfUrl : function() {
					return this.getAsString("selfUrl");
				},
				/**
				 * Returns the alternate URL
				 * 
				 * @method getAlternateUrl
				 * @returns {String} alternate URL
				 */
				getAlternateUrl : function() {
					return this.getAsString("alternateUrl");
				},
				/**
				 * Returns the download URL
				 * 
				 * @method getDownloadUrl
				 * @returns {String} download URL
				 */
				getDownloadUrl : function() {
					return config.Properties.serviceUrl + "/files/" + this.service.endpoint.proxyPath + "/" + "connections" + "/" + "DownloadFile" + "/"
							+ this.getFileId() + "/" + this.getLibraryId();
					;
				},
				/**
				 * Returns the type
				 * 
				 * @method getType
				 * @returns {String} type
				 */
				getType : function() {
					return this.getAsString("type");
				},
				/**
				 * Returns the Category
				 * 
				 * @method getCategory
				 * @returns {String} category
				 */
				getCategory : function() {
					return this.getAsString("category");
				},
				/**
				 * Returns the size
				 * 
				 * @method getSize
				 * @returns {Number} length
				 */
				getSize : function() {
					return this.getAsNumber("length");
				},
				/**
				 * Returns the Edit Link
				 * 
				 * @method getEditLink
				 * @returns {String} edit link
				 */
				getEditLink : function() {
					return this.getAsString("editLink");
				},
				/**
				 * Returns the Edit Media Link
				 * 
				 * @method getEditMediaLink
				 * @returns {String} edit media link
				 */
				getEditMediaLink : function() {
					return this.getAsString("editMediaLink");
				},
				/**
				 * Returns the Thumbnail URL
				 * 
				 * @method getThumbnailUrl
				 * @returns {String} thumbnail URL
				 */
				getThumbnailUrl : function() {
					return this.getAsString("thumbnailUrl");
				},
				/**
				 * Returns the Comments URL
				 * 
				 * @method getCommentsUrl
				 * @returns {String} comments URL
				 */
				getCommentsUrl : function() {
					return this.getAsString("commentsUrl");
				},
				/**
				 * Returns the author
				 * 
				 * @method getAuthor
				 * @returns {Object} author
				 */
				getAuthor : function() {
					return this.getAsObject([ "authorName", "authorUserId", "authorEmail", "authorUserState" ]);
				},
				/**
				 * Returns the Title
				 * 
				 * @method getTitle
				 * @returns {String} title
				 */
				getTitle : function() {
					return this.getAsString("title");
				},
				/**
				 * Returns the published date
				 * 
				 * @method getPublished
				 * @returns {Date} published date
				 */
				getPublished : function() {
					return this.getAsDate("published");
				},
				/**
				 * Returns the updated date
				 * 
				 * @method getUpdated
				 * @returns {Date} updated date
				 */
				getUpdated : function() {
					return this.getAsDate("updated");
				},
				/**
				 * Returns the created date
				 * 
				 * @method getCreated
				 * @returns {Date} created date
				 */
				getCreated : function() {
					return this.getAsDate("created");
				},
				/**
				 * Returns the modified date
				 * 
				 * @method getModified
				 * @returns {Date} modified date
				 */
				getModified : function() {
					return this.getAsDate("modified");
				},
				/**
				 * Returns the last accessed date
				 * 
				 * @method getLastAccessed
				 * @returns {Date} last accessed date
				 */
				getLastAccessed : function() {
					return this.getAsDate("lastAccessed");
				},
				/**
				 * Returns the modifier
				 * 
				 * @method getModifier
				 * @returns {Object} modifier
				 */
				getModifier : function() {
					return this.getAsObject([ "modifierName", "modifierUserId", "modifierEmail", "modifierUserState" ]);
				},
				/**
				 * Returns the visibility
				 * 
				 * @method getVisibility
				 * @returns {String} visibility
				 */
				getVisibility : function() {
					return this.getAsString("visibility");
				},
				/**
				 * Returns the library Id
				 * 
				 * @method getLibraryId
				 * @returns {String} library Id
				 */
				getLibraryId : function() {
					return this.getAsString("libraryId");
				},
				
				/**
				 * Sets the library Id
				 * 
				 * @method setLibraryId
				 * @param libaryId
				 */
				setLibraryId : function(libraryId) {
					return this.setAsString("libraryId", libraryId);
				},
				/**
				 * Returns the library Type
				 * 
				 * @method getLibraryType
				 * @returns {String} library Type
				 */
				getLibraryType : function() {
					return this.getAsString("libraryType");
				},
				/**
				 * Returns the version Id
				 * 
				 * @method getVersionUuid
				 * @returns {String} version Id
				 */
				getVersionUuid : function() {
					return this.getAsString("versionUuid");
				},
				/**
				 * Returns the version label
				 * 
				 * @method getVersionLabel
				 * @returns {String} version label
				 */
				getVersionLabel : function() {
					return this.getAsString("versionLabel");
				},
				/**
				 * Returns the propagation
				 * 
				 * @method getPropagation
				 * @returns {String} propagation
				 */
				getPropagation : function() {
					return this.getAsString("propagation");
				},
				/**
				 * Returns the recommendations Count
				 * 
				 * @method getRecommendationsCount
				 * @returns {Number} recommendations Count
				 */
				getRecommendationsCount : function() {
					return this.getAsNumber("recommendationsCount");
				},
				/**
				 * Returns the comments Count
				 * 
				 * @method getCommentsCount
				 * @returns {Number} comments Count
				 */
				getCommentsCount : function() {
					return this.getAsNumber("commentsCount");
				},
				/**
				 * Returns the shares Count
				 * 
				 * @method getSharesCount
				 * @returns {Number} shares Count
				 */
				getSharesCount : function() {
					return this.getAsNumber("sharesCount");
				},
				/**
				 * Returns the folders Count
				 * 
				 * @method getFoldersCount
				 * @returns {Number} folders Count
				 */
				getFoldersCount : function() {
					return this.getAsNumber("foldersCount");
				},
				/**
				 * Returns the attachments Count
				 * 
				 * @method getAttachmentsCount
				 * @returns {Number} attachments Count
				 */
				getAttachmentsCount : function() {
					return this.getAsNumber("attachmentsCount");
				},
				/**
				 * Returns the versions Count
				 * 
				 * @method getVersionsCount
				 * @returns {Number} versions Count
				 */
				getVersionsCount : function() {
					return this.getAsNumber("versionsCount");
				},
				/**
				 * Returns the references Count
				 * 
				 * @method getReferencesCount
				 * @returns {Number} references Count
				 */
				getReferencesCount : function() {
					return this.getAsNumber("referencesCount");
				},
				/**
				 * Returns the total Media Size
				 * 
				 * @method getTotalMediaSize
				 * @returns {Number} total Media Size
				 */
				getTotalMediaSize : function() {
					return this.getAsNumber("totalMediaSize");
				},
				/**
				 * Returns the Summary
				 * 
				 * @method getSummary
				 * @returns {String} Summary
				 */
				getSummary : function() {
					return this.getAsString("summary");
				},
				/**
				 * Returns the Content URL
				 * 
				 * @method getContentUrl
				 * @returns {String} Content URL
				 */
				getContentUrl : function() {
					return this.getAsString("contentUrl");
				},
				/**
				 * Returns the Content Type
				 * 
				 * @method getContentType
				 * @returns {String} Content Type
				 */
				getContentType : function() {
					return this.getAsString("contentType");
				},
				/**
				 * Returns the objectTypeId
				 * 
				 * @method getObjectTypeId
				 * @returns {String} objectTypeId
				 */
				getObjectTypeId : function() {
					return this.getAsString("objectTypeId");
				},
				/**
				 * Returns the lock state
				 * 
				 * @method getLockType
				 * @returns {String} lock state
				 */
				getLockType : function() {
					return this.getAsString("lock");
				},
				/**
				 * Returns the permission ACLs
				 * 
				 * @method getAcls
				 * @returns {String} ACLs
				 */
				getAcls : function() {
					return this.getAsString("acls");
				},
				/**
				 * Returns the hit count
				 * 
				 * @method getHitCount
				 * @returns {Number} hit count
				 */
				getHitCount : function() {
					return this.getAsNumber("hitCount");
				},
				/**
				 * Returns the anonymous hit count
				 * 
				 * @method getAnonymousHitCount
				 * @returns {Number} anonymous hit count
				 */
				getAnonymousHitCount : function() {
					return this.getAsNumber("anonymousHitCount");
				},
				/**
				 * Returns the tags
				 * 
				 * @method getTags
				 * @returns {Array} tags
				 */
				getTags : function() {
					return this.getAsArray("tags");
				},
				/**
				 * Returns the tags
				 * 
				 * @method setTags
				 * @param {Array} tags
				 */
				setTags : function(tags) {
					return this.setAsArray("tags", tags);
				},
				/**
				 * Sets the label
				 * 
				 * @method setLabel
				 * @param {String} label
				 */
				setLabel : function(label) {
					return this.setAsString("label", label);
				},
				/**
				 * Sets the summary
				 * 
				 * @method setSummary
				 * @param {String} summary
				 */
				setSummary : function(summary) {
					return this.setAsString("summary", summary);
				},
				/**
				 * Sets the visibility
				 * 
				 * @method setVisibility
				 * @param {String} visibility
				 */
				setVisibility : function(visibility) {
					return this.setAsString("visibility", visibility);
				},

				/**
				 * Sets Indicator whether the currently authenticated user wants to receive notifications as people edit the document. Options are on or off.
				 * @param {Boolean} notification
				 * @returns
				 */
				setNotification : function(notification) {
					return this.setAsBoolean("notification", notification ? "on" : "off");
				},
				/**
				 * Loads the file object with the atom entry associated with the file. By default, a network call is made to load the atom entry document in the
				 * file object.
				 * 
				 * @method load
				 * @param {Object} [args] Argument object
				 * @param {Boolean} [isPublic] Optinal flag to indicate whether to load public file which does not require authentication
				 */
				load : function(args, isPublic, url) {
					// detect a bad request by validating required arguments
					var fileUuid = this.getFileId();
					var promise = this.service.validateField("fileId", fileUuid);
					if (promise) {
						return promise;
					}
					if(isPublic) {
						promise = this.service.validateField("libraryId", this.getLibraryId());
						if (promise) {
							return promise;
						}
					}

					var self = this;
					var callbacks = {
						createEntity : function(service, data, response) {
							self.dataHandler = new XmlDataHandler({
								data : data,
								namespaces : consts.Namespaces,
								xpath : consts.FileXPath
							});
							return self;
						}
					};

					var requestArgs = lang.mixin({
						fileUuid : fileUuid
					}, args || {});
					var options = {
						// handleAs : "text",
						query : requestArgs
					};

					if (!url) {						
						if (isPublic) {
							url = this.service.constructUrl(consts.AtomFileInstancePublic, null, {
								"documentId" : fileUuid,
								"libraryId" : this.getLibraryId()
							});
						}
						else {
							url = this.service.constructUrl(consts.AtomFileInstance, null, {
								"documentId" : fileUuid							
							});
						}
					}
					return this.service.getEntity(url, options, fileUuid, callbacks);
				},
				/**
				 * Save this file
				 * 
				 * @method save
				 * @param {Object} [args] Argument object
				 */
				save : function(args) {
					if (this.getFileId()) {
						return this.service.updateFileMetadata(this, args);
					}
				},
				/**
				 * Adds a comment to the file.
				 * 
				 * @method addComment
				 * @param {String} comment the comment to be added
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				addComment : function(comment, args) {
					return this.service.addCommentToFile(this.getAuthor().authorUserId, this.getFileId(), comment, args);
				},
				/**
				 * Pin th file, by sending a POST request to the myfavorites feed.
				 * 
				 * @method pin
				 * @param {Object} [args] Argument object.
				 */
				pin : function(args) {
					return this.service.pinFile(this.getFileId(), args);
				},
				/**
				 * Unpin the file, by sending a DELETE request to the myfavorites feed.
				 * 
				 * @method unPin
				 * @param {Object} [args] Argument object.
				 */
				unpin : function(args) {
					return this.service.unpinFile(this.getFileId(), args);
				},
				/**
				 * Lock the file
				 * 
				 * @method lock
				 * @param {Object} [args] Argument object
				 */
				lock : function(args) {
					return this.service.lockFile(this.getFileId(), args);
				},
				/**
				 * UnLock the file
				 * 
				 * @method unlock
				 * @param {Object} [args] Argument object
				 */
				unlock : function(args) {
					return this.service.unlockFile(this.getFileId(), args);
				},
				/**
				 * Deletes the file.
				 * 
				 * @method remove
				 * @param {Object} [args] Argument object
				 */
				remove : function(args) {
					return this.service.deleteFile(this.getFileId(), args);
				},
				/**
				 * Update the Atom document representation of the metadata for the file
				 * 
				 * @method update
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				update : function(args) {
					return this.service.updateFileMetadata(this, args);
				},
				/**
				 * Downloads the file
				 * 
				 * @method download
				 */
				download : function() {
					return this.service.downloadFile(this.getFileId(), this.getLibraryId());
				}

			});

			/**
			 * Callbacks used when reading a feed that contains File entries.
			 */
			var FileFeedCallbacks = {
				createEntities : function(service, data, response) {
					return new XmlDataHandler({
						data : data,
						namespaces : consts.Namespaces,
						xpath : consts.FileFeedXPath
					});
				},
				createEntity : function(service, data, response) {
					var entry = null;
					if (typeof data == "object") {
						entry = data;
					} else {
						var feedHandler = new XmlDataHandler({
							data : data,
							namespaces : consts.Namespaces,
							xpath : consts.FileXPath
						});
						entry = feedHandler.data;
					}
					var entryHandler = new XmlDataHandler({
						data : entry,
						namespaces : consts.Namespaces,
						xpath : consts.FileXPath
					});
					return new File({
						service : service,
						dataHandler : entryHandler
					});
				}
			};

			/**
			 * Callbacks used when reading a feed that contains File Comment entries.
			 */
			var CommentCallbacks = {
				createEntities : function(service, data, response) {
					return new XmlDataHandler({
						data : data,
						namespaces : consts.Namespaces,
						xpath : consts.CommentFeedXPath
					});
				},
				createEntity : function(service, data, response) {
					var entry = null;
					if (typeof data == "object") {
						entry = data;
					} else {
						var feedHandler = new XmlDataHandler({
							data : data,
							namespaces : consts.Namespaces,
							xpath : consts.CommentXPath
						});
						entry = feedHandler.data;
					}
					var entryHandler = new XmlDataHandler({
						data : entry,
						namespaces : consts.Namespaces,
						xpath : consts.CommentXPath
					});
					return new Comment({
						service : service,
						dataHandler : entryHandler
					});
				}
			};

			/**
			 * FileService class.
			 * 
			 * @class FileService
			 * @namespace sbt.connections
			 */
			var FileService = declare(BaseService, {

				contextRootMap : {
					files : "files"
				},

				/**
				 * Constructor for FileService
				 * 
				 * @constructor
				 * @param args
				 */
				constructor : function(args) {
					var endpointName = args ? (args.endpoint ? args.endpoint : this.getDefaultEndpointName()) : this.getDefaultEndpointName();
					if (!this.endpoint) {
						this.endpoint = config.findEndpoint(endpointName);
					}
				},

				/**
				 * Return the default endpoint name if client did not specify one.
				 * 
				 * @returns {String}
				 */
				getDefaultEndpointName : function() {
					return "connections";
				},

				/**
				 * Callbacks used when reading a feed that contains File entries.
				 */
				getFileFeedCallbacks : function() {
					return FileFeedCallbacks;
				},

				/**
				 * Callbacks used when reading a feed that contains File Comment entries.
				 */
				getCommentFeedCallbacks : function() {
					return CommentCallbacks;
				},

				/**
				 * Returns a File instance from File or JSON or String. Throws an error if the argument was neither.
				 * 
				 * @param {Object} fileOrJsonOrString The file Object or json String for File
				 */
				newFile : function(fileOrJsonOrString) {
					if (fileOrJsonOrString instanceof File) {
						return fileOrJsonOrString;
					} else {
						if (lang.isString(fileOrJsonOrString)) {
							fileOrJsonOrString = {
								id : fileOrJsonOrString
							};
						}
						return new File({
							service : this,
							_fields : lang.mixin({}, fileOrJsonOrString)
						});
					}
				},

				/**
				 * Loads File with the ID passed
				 * 
				 * @method getFile
				 * @param {String} fileId the Id of the file to be loaded
				 * @param {Object} [args] Argument object
				 */
				getFile : function(fileId, args) {
					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}
					var file = this.newFile({
						id : fileId
					});
					return file.load(args);

				},

				/**
				 * Loads Community File 
				 * 
				 * @method getFile
				 * @param {String} fileId the Id of the file to be loaded
				 * @param {String} communityId the Id of the community to which it belongs
				 * @param {Object} [args] Argument object
				 */
				getCommunityFile : function(fileId, communityId, args) {
					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("communityId", communityId);
					}
					if (promise) {
						return promise;
					}
					var file = this.newFile({
						id : fileId
					});
					var url = this.constructUrl(consts.AtomGetCommunityFile, null, {
						communityId : communityId,
						documentId : file.getFileId()
					});
					return file.load(args, null, url);

				},
				/**
				 * Get my files from IBM Connections
				 * 
				 * @method getMyFiles
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getMyFiles : function(args) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomFilesMy, options, this.getFileFeedCallbacks());
				},

				/**
				 * Get community files from IBM Connections (community files refer to
				 * files which the user uploaded to the community. Calling this function
				 * will not list files that have been shared with this community).
				 * 
				 * @method getCommunityFiles
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getCommunityFiles : function(communityId, args) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					var url = this.constructUrl(consts.AtomGetAllFilesInCommunity, null, {
						communityId : communityId
					});

					return this.getEntities(url, options, this.getFileFeedCallbacks());
				},
				/**
				 * Get files shared with logged in user from IBM Connections
				 * 
				 * @method getFilesSharedWithMe
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getFilesSharedWithMe : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",

						query : lang.mixin({
							direction : "inbound"
						}, args ? args : {})
					};

					return this.getEntities(consts.AtomFilesShared, options, this.getFileFeedCallbacks());
				},
				/**
				 * Get files shared by the logged in user from IBM Connections
				 * 
				 * @method getFilesSharedByMe
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getFilesSharedByMe : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",

						query : lang.mixin({
							direction : "outbound"
						}, args ? args : {})
					};

					return this.getEntities(consts.AtomFilesShared, options, this.getFileFeedCallbacks());
				},
				/**
				 * Get public files from IBM Connections
				 * 
				 * @method getPublicFiles
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getPublicFiles : function(args) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {},
						headers : {}
					};

					return this.getEntities(consts.AtomFilesPublic, options, this.getFileFeedCallbacks());
				},
				/**
				 * Get my folders from IBM Connections
				 * 
				 * @method getMyFolders
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getMyFolders : function(args) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomFoldersMy, options, this.getFileFeedCallbacks());
				},
				/**
				 * A feed of comments associated with files to which you have access. You must authenticate this request.
				 * 
				 * @method getMyFolders
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getMyFileComments : function(userId, fileId, args) {

					return this.getFileComments(fileId, userId, false, null, args);
				},
				/**
				 * A feed of comments associated with all public files. Do not authenticate this request.
				 * 
				 * @method getPublicFileComments
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				getPublicFileComments : function(userId, fileId, args) {

					return this.getFileComments(fileId, userId, true, null, args);
				},

				/**
				 * Adds a comment to the specified file.
				 * 
				 * @method addCommentToFile
				 * @param {String} [userId] the userId for the author
				 * @param {String} fileId the ID of the file
				 * @param {String} comment the comment to be added
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				addCommentToFile : function(userId, fileId, comment, url, args) {
					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("comment", comment);
					}
					if (promise) {
						return promise;
					}
					var options = {
						method : "POST",
						query : args || {},
						headers : consts.AtomXmlHeaders,
						data : this._constructPayloadForComment(false, comment)
					};

					if (!url) {
						if (!userId) {
							url = this.constructUrl(consts.AtomAddCommentToMyFile, null, {
								documentId : fileId
							});
						} else {
							url = this.constructUrl(consts.AtomAddCommentToFile, null, {
								userId : userId,
								documentId : fileId
							});
						}
					}
					return this.updateEntity(url, options, this.getCommentFeedCallbacks());
				},

				/**
				 * Adds a comment to the specified file of logged in user.
				 * 
				 * @method addCommentToMyFile
				 * @param {String} fileId the ID of the file
				 * @param {String} comment the comment to be added
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				addCommentToMyFile : function(fileId, comment, args) {
					return this.addCommentToFile(null, fileId, comment, null, args);
				},

				/**
				 * Method to add comments to a Community file <p> Rest API used : /files/basic/api/communitylibrary/<communityId>/document/<fileId>/feed
				 * 
				 * @method addCommentToCommunityFile
				 * @param {String} fileId
				 * @param {String} comment
				 * @param {String} communityId
				 * @param {Object} [args]
				 * @return {Comment} comment
				 */
				addCommentToCommunityFile : function(fileId, comment, communityId, args) {
					var url = this.constructUrl(consts.AtomAddCommentToCommunityFile, null, {
						communityId : communityId,
						documentId : fileId
					});
					return this.addCommentToFile(null, fileId, comment, url, args);
				},

				/**
				 * Update the Atom document representation of the metadata for a file from logged in user's library.
				 * 
				 * @method updateFileMetadata
				 * @param {Object} fileOrJson file or json representing the file to be updated
				 * @param {Object} [args] Argument object. Object representing various parameters that can be passed. The parameters must be exactly as they are
				 * supported by IBM Connections like ps, sortBy etc.
				 */
				updateFileMetadata : function(fileOrJson, url, args) {

					var promise = this.validateField("fileOrJson", fileOrJson);
					if (promise) {
						return promise;
					}

					var file = this.newFile(fileOrJson);
					var options = {
						method : "PUT",
						query : args || {},
						headers : consts.AtomXmlHeaders,
						data : this._constructPayload(file._fields, file.getFileId())
					};

					if (!url) {
						url = this.constructUrl(consts.AtomUpdateFileMetadata, null, {
							documentId : file.getFileId()
						});
					}
					return this.updateEntity(url, options, this.getFileFeedCallbacks());
				},

				/**
				 * Method to update Community File's Metadata <p> Rest API used : /files/basic/api/library/<libraryId>/document/<fileId>/entry <p>
				 * @method updateCommunityFileMetadata
				 * @param {Object} fileOrJson
				 * @param {String} libraryId
				 * @param {Object} [args]
				 * @return {File}
				 */
				updateCommunityFileMetadata : function(fileOrJson, communityId, args) {
					var promise = this.validateField("fileOrJson", fileOrJson);
					if (promise) {
						return promise;
					}
					var file = this.newFile(fileOrJson);
					promise = new Promise();
					var _this = this;
					var update = function() {
						var url = _this.constructUrl(consts.AtomUpdateCommunityFileMetadata, null, {
							libraryId : file.getLibraryId(),
							documentId : file.getFileId()
						});
						_this.updateFileMetadata(file, url, args).then(function(file) {
							promise.fulfilled(file);
						}, function(error) {
							promise.rejected(error);
						});
					};
					if (file.isLoaded()) {
						update();
					} else {
						var url = _this.constructUrl(consts.AtomGetCommunityFile, null, {
							communityId : communityId,
							documentId : file.getFileId()
						});
						file.load(null, null, url).then(function() {
							update();
						}, function(error) {
							promise.rejected(error);
						});
					}
					return promise;
				},

				/**
				 * Pin a file, by sending a POST request to the myfavorites feed.
				 * 
				 * @method pinFile
				 * @param {String} fileId ID of file which needs to be pinned
				 * @param {Object} [args] Argument object.
				 */
				pinFile : function(fileId, args) {

					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}
					var parameters = args ? lang.mixin({}, args) : {};
					parameters["itemId"] = fileId;

					var options = {
						method : "POST",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : parameters
					};

					var callbacks = {
						createEntity : function(service, data, response) {
							return "Success";
						}
					};

					return this.updateEntity(consts.AtomPinFile, options, callbacks);

				},

				/**
				 * Unpin a file, by sending a DELETE request to the myfavorites feed.
				 * 
				 * @method unpinFile
				 * @param {String} fileId ID of file which needs to be unpinned
				 * @param {Object} [args] Argument object.
				 */
				unpinFile : function(fileId, args) {
					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}

					var parameters = args ? lang.mixin({}, args) : {};
					parameters["itemId"] = fileId;

					var options = {
						method : "DELETE",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : parameters
					};

					return this.deleteEntity(consts.AtomPinFile, options, fileId);

				},

				/**
				 * Add a file or files to a folder.
				 * 
				 * You cannot add a file from your local directory to a folder; the file must already have been uploaded to the Files application. To add a file
				 * to a folder you must be an editor of the folder.
				 * 
				 * @method addFilesToFolder
				 * @param {String} folderId the Id of the folder
				 * @param {List} fileIds list of file Ids to be added to the folder
				 * @param {Object} [args] Argument object.
				 */
				addFilesToFolder : function(fileIds, folderId, args) {

					var promise = this.validateField("fileIds", fileIds);
					if (!promise) {
						promise = this.validateField("folderId", folderId);
					}

					var options = {
						method : "POST",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};
					var url = this.constructUrl(consts.AtomAddFilesToFolder, null, {
						collectionId : folderId
					});

					var separatorChar = "?";
					for ( var counter in fileIds) {
						url += separatorChar + "itemId=" + fileIds[counter];
						separatorChar = "&";
					}

					var callbacks = {
						createEntity : function(service, data, response) {
							return "Success";
						}
					};

					return this.updateEntity(url, options, callbacks);

				},

				/**
				 * Gets the files pinned by the logged in user.
				 * 
				 * @method getPinnedFiles
				 * @param {Object} [args] Argument object for the additional parameters like pageSize etc.
				 */
				getPinnedFiles : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomFilesMyPinned, options, this.getFileFeedCallbacks());
				},

				/**
				 * Delete a file.
				 * 
				 * @method deleteFile
				 * @param {String} fileId Id of the file which needs to be deleted
				 * @param {Object} [args] Argument object
				 */
				deleteFile : function(fileId, args) {

					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}

					var options = {
						method : "DELETE",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};
					var url = this.constructUrl(consts.AtomDeleteFile, null, {
						documentId : fileId
					});

					return this.deleteEntity(url, options, fileId);
				},

				/**
				 * Lock a file
				 * 
				 * @method lockFile
				 * @param {String} fileId Id of the file which needs to be locked
				 * @param {Object} [args] Argument object
				 */
				lockFile : function(fileId, args) {
					var parameters = args ? lang.mixin({}, args) : {};
					parameters["type"] = "HARD";

					var options = {
						method : "POST",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : args || {}
					};
					var url = this.constructUrl(consts.AtomLockUnlockFile, parameters, {
						documentId : fileId
					});

					var callbacks = {
						createEntity : function(service, data, response) {
							return "Success";
						}
					};

					return this.updateEntity(url, options, callbacks, args);
				},

				/**
				 * unlock a file
				 * 
				 * @method lockFile
				 * @param {String} fileId Id of the file which needs to be unlocked
				 * @param {Object} [args] Argument object
				 */
				unlockFile : function(fileId, args) {
					var parameters = args ? lang.mixin({}, args) : {};
					parameters["type"] = "NONE";

					var options = {
						method : "POST",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : args || {}
					};
					var url = this.constructUrl(consts.AtomLockUnlockFile, parameters, {
						documentId : fileId
					});

					var callbacks = {
						createEntity : function(service, data, response) {
							return "Success";
						}
					};

					return this.updateEntity(url, options, callbacks, args);
				},

				/**
				 * Uploads a new file for logged in user.
				 * 
				 * @method uploadFile
				 * @param {Object} fileControlOrId The Id of html control or the html control
				 * @param {Object} [args] The additional parameters for upload
				 */
				uploadFile : function(fileControlOrId, args) {

					var promise = this.validateField("File Control Or Id", fileControlOrId);
					if (promise) {
						return promise;
					}
					promise = this.validateHTML5FileSupport();
					if (promise) {
						return promise;
					}
					var files = null;
					if (typeof fileControlOrId == "string") {
						var fileControl = document.getElementById(fileControlOrId);
						filePath = fileControl.value;
						files = fileControl.files;
					} else if (typeof fileControlOrId == "object") {
						filePath = fileControlOrId.value;
						files = fileControlOrId.files;
					} else {
						return this.createBadRequestPromise("File Control or ID is required");
					}

					var file = files[0];
					var data = new FormData();
					data.append("file", file);

					return this.uploadFileBinary(data, file.name, args);
				},

				/**
				 * Uploads a new file for logged in user.
				 * 
				 * @method uploadFile
				 * @param {Object} binaryContent The binary content of the file
				 * @param {String} filename The name of the file
				 * @param {Object} [args] The additional parameters of metadata of file for upload like visibility, tag, etc.
				 */
				uploadFileBinary : function(binaryContent, fileName, args) {

					var promise = this.validateField("Binary Content", binaryContent);
					if (promise) {
						return promise;
					}
					promise = this.validateField("File Name", fileName);
					if (promise) {
						return promise;
					}
					if (util.getJavaScriptLibrary().indexOf("Dojo 1.4.3") != -1) {
						return this.createBadRequestPromise("Dojo 1.4.3 is not supported for File upload");
					}
					// /files/<<endpointName>>/<<serviceType>>/<<operation>>/fileName eg. /files/smartcloud/connections/UploadFile/fileName?args
					var url = this.constructUrl(config.Properties.serviceUrl + "/files/" + this.endpoint.proxyPath + "/" + "connections" + "/" + "UploadFile"
							+ "/" + encodeURIComponent(fileName), args && args.parameters ? args.parameters : {});
					var headers = {
						"Content-Type" : false,
						"Process-Data" : false // processData = false is reaquired by jquery
					};
					var options = {
						method : "POST",
						headers : headers,
						query : args || {},
						data : binaryContent
					};

					return this.updateEntity(url, options, this.getFileFeedCallbacks());
				},

				/**
				 * Upload new version of a file.
				 * 
				 * @method uploadNewVersion
				 * @param {Object} fileId The ID of the file
				 * @param {Object} fileControlOrId The Id of html control or the html control
				 * @param {Object} [args] The additional parameters ffor updating file metadata
				 */
				uploadNewVersion : function(fileId, fileControlOrId, args) {

					var promise = this.validateField("File Control Or Id", fileControlOrId);
					if (!promise) {
						promise = this.validateField("File ID", fileId);
					}
					if (promise) {
						return promise;
					}
					promise = this.validateHTML5FileSupport();
					if (promise) {
						return promise;
					}
					var files = null;
					if (typeof fileControlOrId == "string") {
						var fileControl = document.getElementById(fileControlOrId);
						filePath = fileControl.value;
						files = fileControl.files;
					} else if (typeof fileControlOrId == "object") {
						filePath = fileControlOrId.value;
						files = fileControlOrId.files;
					} else {
						return this.createBadRequestPromise("File Control or ID is required");
					}

					var file = files[0];
					var data = new FormData();
					data.append("file", file);

					return this.uploadNewVersionBinary(data, fileId, file.name, args);
				},

				/**
				 * Uploads new Version of a File.
				 * 
				 * @method uploadNewVersionBinary
				 * @param {Object} binaryContent The binary content of the file
				 * @param {String} fileId The ID of the file
				 * @param {Object} [args] The additional parameters for upding file metadata
				 */
				uploadNewVersionBinary : function(binaryContent, fileId, fileName, args) {

					var promise = this.validateField("Binary Content", binaryContent);
					if (promise) {
						return promise;
					}
					promise = this.validateField("File ID", fileId);
					if (promise) {
						return promise;
					}
					promise = this.validateField("File Name", fileName);
					if (promise) {
						return promise;
					}
					if (util.getJavaScriptLibrary().indexOf("Dojo 1.4.3") != -1) {
						return this.createBadRequestPromise("Dojo 1.4.3 is not supported for File Upload");
					}
					// /files/<<endpointName>>/<<serviceType>>/<<operation>>/fileId?args eg./files/smartcloud/connections/UpdateFile/fileId/fileName?args
					var url = this.constructUrl(config.Properties.serviceUrl + "/files/" + this.endpoint.proxyPath + "/" + "connections" + "/"
							+ "UploadNewVersion" + "/" + encodeURIComponent(fileId) + "/" + encodeURIComponent(fileName),
							args && args.parameters ? args.parameters : {});
					var headers = {
						"Content-Type" : false,
						"Process-Data" : false  // processData = false is reaquired by jquery
					};
					var options = {
						method : "PUT",
						headers : headers,
						data : binaryContent
					};
					var promise = new Promise();
					var _this = this;

					this.updateEntity(url, options, this.getFileFeedCallbacks()).then(function(file) {
						if (args) {
							_this.updateFile(file.getFileId(), args).then(function(updatedFile) {
								promise.fulfilled(updatedFile);
							});
						} else {
							promise.fulfilled(file);
						}
					}, function(error) {
						promise.rejected(error);
					});
					return promise;
				},

				/**
				 * Updates metadata of a file programatically using a PUT call
				 * @param [String] fileId the File ID
				 * @param [Object] args The parameters for update. Supported Input parameters are commentNotification, created, identifier, includePath,
				 * mediaNotification, modified, recommendation, removeTag, sendNotification, sharePermission, shareSummary, shareWith, tag and visibility
				 * @returns
				 */
				updateFile : function(fileId, args) {
					var promise = this.validateField("File ID", fileId);
					if (promise) {
						return promise;
					}
					var url = this.constructUrl(consts.AtomFileInstance, null, {
						documentId : fileId
					});
					var separatorChar = "?";
					if (args && args.tags) {
						var tags = args.tags.split(",");
						for ( var counter in tags) {
							url += separatorChar + "tag=" + stringUtil.trim(tags[counter]);
							separatorChar = "&";
						}
						delete args.tags;
					}
					if (args && args.removeTags) {
						var removeTags = args.removeTags.split(",");
						for ( var counter in removeTags) {
							url += separatorChar + "removeTag=" + stringUtil.trim(removeTags[counter]);
							separatorChar = "&";
						}
						delete args.removeTags;
					}

					var options = {
						method : "PUT",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : args || {}
					};

					return this.updateEntity(url, options, this.getFileFeedCallbacks());
				},

				/**
				 * Downloads a file.
				 * 
				 * @method downloadFile
				 * @param {String} fileId The ID of the file
				 * @param {String} libraryId The library ID of the file
				 */
				downloadFile : function(fileId, libraryId) {
					var url = config.Properties.serviceUrl + "/files/" + this.endpoint.proxyPath + "/" + "connections" + "/" + "DownloadFile" + "/" + fileId
							+ "/" + libraryId;
					window.open(url);
				},

				actOnCommentAwaitingApproval : function(commentId, action, actionReason) {

				},
				actOnFileAwaitingApproval : function(fileId, action, actionReason) {

				},
				/**
				 * Add a file to a folder or list of folders.
				 * 
				 * You cannot add a file from your local directory to a folder; the file must already have been uploaded to the Files application. To add a file
				 * to a folder you must be an editor of the folder.
				 * 
				 * @method addFilesToFolder
				 * @param {String} fileId the Id of the file
				 * @param {List} folderIds list of folder Ids
				 * @param {String} [userId] the userId of the user in case of own file
				 * @param {Object} [args] Argument object.
				 */
				addFileToFolders : function(fileId, folderIds, userId, args) {
					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("folderIds", folderIds);
					}
					if (promise) {
						return promise;
					}

					var url = null;

					if (!userId) {
						url = this.constructUrl(consts.AtomAddMyFileToFolders, null, {
							documentId : fileId
						});
					} else {
						url = this.constructUrl(consts.AtomAddFileToFolders, null, {
							userId : userId,
							documentId : fileId
						});
					}

					var payload = this._constructPayloadForMultipleEntries(folderIds, "itemId", "collection");

					var options = {
						method : "POST",
						headers : consts.AtomXmlHeaders,
						data : payload
					};

					var callbacks = {
						createEntity : function(service, data, response) {
						}
					};

					return this.updateEntity(url, options, callbacks);
				},

				/**
				 * Create a new Folder
				 * 
				 * @method createFolder <p> Rest API used : /files/basic/api/collections/feed
				 * 
				 * @param {String} name name of the folder to be created
				 * @param {String} [description] description of the folder
				 * @param {String} [shareWith] If the folder needs to be shared, specify the details in this parameter. <br> Pass Coma separated List of id,
				 * (person/community/group) or role(reader/Contributor/owner) in order
				 * @return {Object} Folder
				 */
				createFolder : function(name, description, shareWith) {
					var promise = this.validateField("folderName", name);
					if (promise) {
						return promise;
					}
					var url = consts.AtomCreateFolder;
					var payload = this._constructPayloadFolder(name, description, shareWith, "create");

					var options = {
						method : "POST",
						headers : lang.mixin(consts.AtomXmlHeaders, {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}),
						data : payload
					};

					return this.updateEntity(url, options, this.getFileFeedCallbacks());

				},

				/**
				 * Delete Files From Recycle Bin
				 * 
				 * @param {String} userId The ID of user
				 */
				deleteAllFilesFromRecycleBin : function(userId) {

					var url = null;

					if (!userId) {
						url = consts.AtomDeleteMyFilesFromRecyclebBin;
					} else {
						url = this.constructUrl(consts.AtomDeleteAllFilesFromRecyclebBin, null, {
							userId : userId
						});
					}
					var options = {
						method : "DELETE"
					};
					return this.deleteEntity(url, options, "");
				},

				/**
				 * Delete all Versions of a File before the given version
				 * 
				 * @param {String} fileId the ID of the file
				 * @param {String} [versionLabel] The version from which all will be deleted
				 * @param {Object} [args] additional arguments
				 */
				deleteAllVersionsOfFile : function(fileId, versionLabel, args) {

					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("versionLabel", versionLabel);
					}
					if (promise) {
						return promise;
					}

					var requestArgs = lang.mixin({
						category : "version",
						deleteFrom : versionLabel
					}, args || {});

					var options = {
						method : "DELETE",
						query : requestArgs,
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};
					var url = this.constructUrl(consts.AtomDeleteAllVersionsOfAFile, null, {
						documentId : fileId
					});

					return this.deleteEntity(url, options, fileId);
				},

				/**
				 * Delete a Comment for a file
				 * 
				 * @param {String} fileId the ID of the file
				 * @param {String} commentId the ID of comment
				 * @param {String} [userId] the ID of the user, if not provided logged in user is assumed
				 * @param {Object} [args] the additional arguments
				 */
				deleteComment : function(fileId, commentId, userId, args) {
					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("commentId", commentId);
					}
					if (promise) {
						return promise;
					}

					var url = null;

					if (userId) {
						url = this.constructUrl(consts.AtomDeleteComment, null, {
							userId : userId,
							documentId : fileId,
							commentId : commentId
						});
					} else {
						url = this.constructUrl(consts.AtomDeleteMyComment, null, {
							documentId : fileId,
							commentId : commentId
						});
					}

					var options = {
						method : "DELETE",
						query : args || {},
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};

					return this.deleteEntity(url, options, commentId);
				},

				/**
				 * Delete File from RecycleBin of a user
				 * @param {String} fileId the Id of the file
				 * @param {String} [userId] the Id of the user
				 * @param {Object} args the additional arguments
				 * @returns
				 */
				deleteFileFromRecycleBin : function(fileId, userId, args) {
					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}

					var url = null;

					if (userId) {
						url = this.constructUrl(consts.AtomDeleteFileFromRecycleBin, null, {
							userId : userId,
							documentId : fileId
						});
					} else {
						url = this.constructUrl(consts.AtomDeleteMyFileFromRecycleBin, null, {
							documentId : fileId
						});
					}

					var options = {
						method : "DELETE",
						query : args || {},
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};

					return this.deleteEntity(url, options, fileId);
				},

				/**
				 * deletes a File Share
				 * @param {String} fileId the ID of the file
				 * @param {String} userId the ID of the user
				 * @param {Object} args the additional arguments
				 */
				deleteFileShare : function(fileId, userId, args) {
					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}

					var requestArgs = lang.mixin({
						sharedWhat : fileId
					}, args || {});

					if (userId) {
						requestArgs.sharedWith = userId;
					}

					var url = consts.AtomDeleteFileShare;

					var options = {
						method : "DELETE",
						query : requestArgs,
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};

					return this.deleteEntity(url, options, fileId);
				},

				/**
				 * Deletes a Folder
				 * @param {String} folderId the ID of the folder
				 */
				deleteFolder : function(folderId) {
					var promise = this.validateField("folderId", folderId);
					if (promise) {
						return promise;
					}

					var options = {
						method : "DELETE",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						}
					};
					var url = this.constructUrl(consts.AtomDeleteFFolder, null, {
						collectionId : folderId
					});

					return this.deleteEntity(url, options, folderId);
				},

				/**
				 * Get all user Files
				 * @param {String} userId the ID of the user
				 * @param {Object} args the addtional arguments
				 * @returns {Object} Files
				 */
				getAllUserFiles : function(userId, args) {

					var promise = this.validateField("userId", userId);
					if (promise) {
						return promise;
					}

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					var url = this.constructUrl(consts.AtomGetAllUsersFiles, null, {
						userId : userId
					});

					return this.getEntities(url, options, this.getFileFeedCallbacks());
				},

				/**
				 * Get file Comments
				 * @param {String} fileId the ID of the file
				 * @param {String} [userId] the ID of the user
				 * @param {Boolean} [isAnnonymousAccess] flag to indicate annonymous access
				 * @param {String} [commentId] the ID of the comment
				 * @param {String} [communityId] required in case the file in a community file
				 * @param {Object} args the additional arguments
				 * @returns {Array} Comments List
				 */
				getFileComments : function(fileId, userId, isAnnonymousAccess, commentId, communityId, args) {

					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}

					var url = null;
					if(communityId){
						url = this.constructUrl(consts.AtomAddCommentToCommunityFile, null, {
							communityId : communityId,
							documentId : fileId
						});
					}
					else if (commentId) {
						if (userId) {
							url = this.constructUrl(consts.AtomGetFileComment, null, {
								userId : userId,
								documentId : fileId,
								commentId : commentId
							});
						} else {
							url = this.constructUrl(consts.AtomGetMyFileComment, null, {
								documentId : fileId,
								commentId : commentId
							});
						}
					} else {
						var promise = this.validateField("userId", userId);
						if (promise) {
							return promise;
						}
						if (isAnnonymousAccess) {
							url = this.constructUrl(consts.AtomFileCommentsPublic, null, {
								userId : userId,
								documentId : fileId
							});
						} else {
							url = this.constructUrl(consts.AtomFileCommentsMy, null, {
								userId : userId,
								documentId : fileId
							});
						}
					}

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(url, options, this.getCommentFeedCallbacks());
				},
				
				 /**
			     * Method to get All comments of a Community File
			     * <p>
			     * Rest API Used : 
			     * /files/basic/api/communitylibrary/<communityId>/document/<fileId>/feed
			     * <p>
			     * @method getAllCommunityFileComments
			     * @param {String} fileId
			     * @param {String} communityId
			     * @param {Object} [args]
			     * @returns {Array} comments
			     */
			    getAllCommunityFileComments : function(fileId, communityId, args) {
			    	
			    	var promise = this.validateField("fileId", fileId);
			    	if(!promise){
			    		promise = this.validateField("communityId", communityId);
			    	}
					if (promise) {
						return promise;
					}
					
					return this.getFileComments(fileId, null, null, null, communityId, args);			    	
			    },

				/**
				 * Get Files from recycle bin
				 * @param {Object} [args] the additional arguments
				 * @returns {Object} Files
				 */
				getFileFromRecycleBin : function(fileId, userId, args) {

					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};
					var url = this.constructUrl(consts.AtomGetFileFromRecycleBin, null, {
						userId : userId,
						documentId : fileId
					});

					var callbacks = {
						createEntity : function(service, data, response) {
							self.dataHandler = new XmlDataHandler({
								data : data,
								namespaces : consts.Namespaces,
								xpath : consts.FileXPath
							});
							return self;
						}
					};

					return this.getEntity(url, options, callbacks);
				},

				/**
				 * Get Files awaiting approval
				 * @param {Object} [args] the additional arguments
				 * @returns {Object} Files
				 */
				getFilesAwaitingApproval : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomGetFilesAwaitingApproval, options, this.getFileFeedCallbacks());
				},

				/**
				 * Get File Shares
				 * @param {Object} [args] the additional arguments
				 * @returns {Object} Files
				 */
				getFileShares : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomGetFileShares, options, this.getFileFeedCallbacks());
				},

				/**
				 * Get Files in a folder
				 * @param {String} folderId the ID of the folder
				 * @param {Object} [args] the additional arguments
				 * @returns {Object} Files
				 */
				getFilesInFolder : function(folderId, args) {

					var url = this.constructUrl(consts.AtomGetFilesInFolder, null, {
						collectionId : folderId
					});

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(url, options, this.getFileFeedCallbacks());

				},

				/**
				 * Get Files in my recycle bin
				 * @param {Object} [args] the addtional arguments
				 * @returns
				 */
				getFilesInMyRecycleBin : function(args) {
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomGetFilesInMyRecycleBin, options, this.getFileFeedCallbacks());
				},

				/**
				 * Get a file with given version
				 * @param {String} fileId the ID of the file
				 * @param {String} versionId the ID of the version
				 * @param {Object} [args] the additional arguments
				 * @returns {Object} File
				 */
				getFileWithGivenVersion : function(fileId, versionId, args) {

					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}
					if (!versionId) {
						return this.getFile(fileId, args);
					}
					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};
					var url = this.constructUrl(consts.AtomGetFileWithGivenVersion, null, {
						documentId : fileId,
						versionId : versionId
					});

					var callbacks = {
						createEntity : function(service, data, response) {
							self.dataHandler = new XmlDataHandler({
								data : data,
								namespaces : consts.Namespaces,
								xpath : consts.FileXPath
							});
							return self;
						}
					};

					return this.getEntity(url, options, callbacks);
				},

				/**
				 * Get a folder
				 * @param {String} folderId the ID of the folder
				 * @returns
				 */
				getFolder : function(folderId) {
					var promise = this.validateField("folderId", folderId);
					if (promise) {
						return promise;
					}
					var options = {
						method : "GET",
						handleAs : "text"
					};
					var url = this.constructUrl(consts.AtomGetFolder, null, {
						collectionId : folderId
					});

					var callbacks = {
						createEntity : function(service, data, response) {
							self.dataHandler = new XmlDataHandler({
								data : data,
								namespaces : consts.Namespaces,
								xpath : consts.FileXPath
							});
							return self;
						}
					};

					return this.getEntity(url, options, callbacks);
				},

				/**
				 * Get Folders With Recently Added Files
				 * @param {Object} [args] the additional arguents
				 * @returns {Object} List of Files
				 */
				getFoldersWithRecentlyAddedFiles : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomGetFoldersWithRecentlyAddedFiles, options, this.getFileFeedCallbacks());

				},

				/**
				 * Gets the folders pinned by the logged in user.
				 * 
				 * @method getPinnedFolders
				 * @param {Object} [args] Argument object for the additional parameters like pageSize etc.
				 */
				getPinnedFolders : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomGetPinnedFolders, options, this.getFileFeedCallbacks());
				},

				/**
				 * Get public folders
				 * 
				 * @param {Object} [args] Additional arguments like ps, sort by, etc
				 */
				getPublicFolders : function(args) {

					var options = {
						method : "GET",
						handleAs : "text",
						query : args || {}
					};

					return this.getEntities(consts.AtomGetPublicFolders, options, this.getFileFeedCallbacks());
				},

				/**
				 * Pin a folder, by sending a POST request to the myfavorites feed.
				 * 
				 * @method pinFolder
				 * @param {String} folderId ID of folder which needs to be pinned
				 * @param {Object} [args] Argument object.
				 */
				pinFolder : function(folderId, args) {

					var promise = this.validateField("folderId", folderId);
					if (promise) {
						return promise;
					}
					var parameters = args ? lang.mixin({}, args) : {};
					parameters["itemId"] = folderId;

					var options = {
						method : "POST",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : parameters
					};

					var callbacks = {
						createEntity : function(service, data, response) {
						}
					};

					return this.updateEntity(consts.AtomPinFolder, options, callbacks);

				},

				/**
				 * Remove a File from a Folder
				 * 
				 * @param {String} folderId the ID of the folder
				 * @param {Stirng} fileId The ID of the File
				 */
				removeFileFromFolder : function(folderId, fileId) {

					var promise = this.validateField("folderId", folderId);
					if (promise) {
						return promise;
					}
					promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}
					var parameters = args ? lang.mixin({}, args) : {};
					parameters["itemId"] = fileId;

					var url = this.constructUrl(consts.AtomRemoveFileFromFolder, null, {
						collectionId : folderId
					});
					var options = {
						method : "DELETE",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : parameters
					};

					return this.deleteEntity(url, options, fileId);

				},

				/**
				 * Restore a File from Recycle Bin (trash)
				 * 
				 * @param {String} fileId the ID of the file
				 * @param {String} userId the ID of the user
				 */
				restoreFileFromRecycleBin : function(fileId, userId) {

					var promise = this.validateField("fileId", fileId);
					if (promise) {
						return promise;
					}
					promise = this.validateField("userId", userId);
					if (promise) {
						return promise;
					}
					var parameters = args ? lang.mixin({}, args) : {};
					parameters["undelete"] = "true";

					var options = {
						method : "POST",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : parameters
					};

					var callbacks = {
						createEntity : function(service, data, response) {
							self.dataHandler = new XmlDataHandler({
								data : data,
								namespaces : consts.Namespaces,
								xpath : consts.FileXPath
							});
							return self;
						}
					};

					var url = this.constructUrl(consts.AtomRestoreFileFromRecycleBin, null, {
						userId : userId,
						documentId : fileId
					});

					return this.updateEntity(url, options, callbacks);
				},

				/**
				 * Share a File with community(ies)
				 * 
				 * @param {String} fileId the ID of the file
				 * @param {Object} communityIds The list of community IDs
				 * @param {Object} args the additional arguments
				 */
				shareFileWithCommunities : function(fileId, communityIds, args) {

					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("communityIds", communityIds);
					}
					if (promise) {
						return promise;
					}

					var url = this.constructUrl(consts.AtomShareFileWithCommunities, null, {
						documentId : fileId
					});

					var payload = this._constructPayloadForMultipleEntries(communityIds, "itemId", "community");

					var options = {
						method : "POST",
						headers : consts.AtomXmlHeaders,
						data : payload
					};

					var callbacks = {
						createEntity : function(service, data, response) {
							return response;
						}
					};

					return this.updateEntity(url, options, callbacks);
				},

				/**
				 * Unpin a folder, by sending a DELETE request to the myfavorites feed.
				 * 
				 * @method unpinFolder
				 * @param {String} folderId ID of folder which needs to be unpinned
				 * @param {Object} [args] Argument object.
				 */
				unpinFolder : function(folderId, args) {
					var promise = this.validateField("folderId", folderId);
					if (promise) {
						return promise;
					}

					var parameters = args ? lang.mixin({}, args) : {};
					parameters["itemId"] = folderId;

					var options = {
						method : "DELETE",
						headers : {
							"X-Update-Nonce" : "{X-Update-Nonce}"
						},
						query : parameters
					};

					return this.deleteEntity(consts.AtomPinFolder, options, folderId);

				},

				/**
				 * Update comment created by logged in user
				 * @param {String} fileId the ID of the file
				 * @param {String}commentId the ID of the comment
				 * @param {String} comment the updated comment
				 * @param {Object} args the additional arguments
				 * @returns
				 */
				updateMyComment : function(fileId, commentId, comment, args) {

					return updateComment(fileId, commentId, comment, null, args);
				},

				/**
				 * updates a comment
				 * @param {String} fileId the ID of the file
				 * @param {String} commentId the ID of the comment
				 * @param {String} comment the comment
				 * @param {String} [userId] the ID of the user
				 * @param {Object} [args] the additional arguments
				 * @returns {Object} the updated Comment
				 */
				updateComment : function(fileId, commentId, comment, userId, args) {

					var promise = this.validateField("fileId", fileId);
					if (!promise) {
						promise = this.validateField("comment", comment);
					}
					if (promise) {
						return promise;
					}
					if (!promise) {
						promise = this.validateField("commentId", commentId);
					}
					if (promise) {
						return promise;
					}
					var options = {
						method : "POST",
						query : args || {},
						headers : consts.AtomXmlHeaders,
						data : this._constructPayloadForComment(false, comment)
					};
					var url = null;

					if (!userId) {
						url = this.constructUrl(consts.AtomUpdateMyComment, null, {
							documentId : fileId,
							commentId : commentId
						});
					} else {
						url = this.constructUrl(consts.AtomUpdateComment, null, {
							userId : userId,
							documentId : fileId,
							commentId : commentId
						});
					}
					return this.updateEntity(url, options, this.getCommentFeedCallbacks());
				},

				/**
				 * Add a file to a folder.
				 * 
				 * You cannot add a file from your local directory to a folder; the file must already have been uploaded to the Files application. To add a file
				 * to a folder you must be an editor of the folder.
				 * 
				 * @method addFileToFolder
				 * @param {String} fileId the Id of the file
				 * @param {String} folderId the ID of the folder
				 * @param {String} [userId] the userId of the user in case of own file
				 * @param {Object} [args] Argument object.
				 */
				addFileToFolder : function(fileId, folderId, userId, args) {

					return this.addFileToFolders(fileId, [ folderId ], userId, args);
				},

				_constructPayloadFolder : function(name, description, shareWith, operation, entityId) {
					var _this = this;
					var shareWithId = null;
					var shareWithWhat = null;
					var shareWithRole = null;
					if (shareWith && stringUtil.trim(shareWith) != "") {
						var parts = shareWith.split(",");
						if (parts.length == 3) {
							shareWithId = parts[0];
							shareWithWhat = parts[1];
							shareWithRole = parts[2];
						}
					}
					var trans = function(value, key) {
						if (key == "category") {
							value = xml.encodeXmlEntry("collection");
						} else if (key == "id") {
							value = xml.encodeXmlEntry(entityId);
						} else if (key == "label") {
							value = xml.encodeXmlEntry(name);
						} else if (key == "title") {
							value = xml.encodeXmlEntry(name);
						} else if (key == "summary") {
							value = xml.encodeXmlEntry(description);
						} else if (key == "visibility") {
							value = xml.encodeXmlEntry("private");
						} else if (key == "shareWithId" && shareWithId) {
							value = xml.encodeXmlEntry(shareWithId);
						} else if (key == "shareWithWhat" && shareWithWhat) {
							value = xml.encodeXmlEntry(shareWithWhat);
						} else if (key == "shareWithRole" && shareWithRole) {
							value = xml.encodeXmlEntry(shareWithRole);
						}
						return value;
					};
					var transformer = function(value, key) {
						if (key == "getCategory") {
							value = stringUtil.transform(FileCategoryTmpl, _this, trans, _this);
						} else if (key == "getId" && entityId) {
							value = stringUtil.transform(FileIdTmpl, _this, trans, _this);
						} else if (key == "getFolderLabel") {
							value = stringUtil.transform(FolderLabelTmpl, _this, trans, _this);
						} else if (key == "getTitle") {
							value = stringUtil.transform(FileTitleTmpl, _this, trans, _this);
						} else if (key == "getSummary") {
							value = stringUtil.transform(FileSummaryTmpl, _this, trans, _this);
						} else if (key == "getVisibility") {
							value = stringUtil.transform(FileVisibilityTmpl, _this, trans, _this);
						} else if (key == "getVisibilityShare" && shareWithId) {
							value = stringUtil.transform(FileVisibilityShareTmpl, _this, trans, _this);
						}
						return value;
					};
					var postData = stringUtil.transform(FolderTmpl, this, transformer, this);					
					return stringUtil.trim(postData);
				},
				_constructPayloadForMultipleEntries : function(listOfIds, multipleEntryId, category) {
					var payload = FileFeedTmpl;
					var entriesXml = "";
					var categoryXml = "";
					var itemXml = "";
					var currentId = "";
					var transformer = function(value, key) {
						if (key == "category") {
							value = xml.encodeXmlEntry(category);
						} else if (key == "getCategory") {
							value = categoryXml;
						} else if (key == "fileId") {
							value = xml.encodeXmlEntry(currentId);
						} else if (key == "getItem") {
							value = itemXml;
						} else if (key == "getEntries") {
							value = entriesXml;
						}
						return value;
					};
					var _this = this;

					for ( var counter in listOfIds) {
						currentId = listOfIds[counter];
						var entryXml = FileItemEntryTmpl;
						if (category) {
							categoryXml = stringUtil.transform(FileCategoryTmpl, _this, transformer, _this);
						}
						itemXml = stringUtil.transform(FileItemIdTmpl, _this, transformer, _this);
						entryXml = stringUtil.transform(entryXml, _this, transformer, _this);
						entriesXml = entriesXml + entryXml;
					}

					if (entriesXml != "") {
						payload = stringUtil.transform(payload, _this, transformer, _this);
					}					
					return payload;
				},
				_constructPayloadForComment : function(isDelete, comment) {

					var payload = FileCommentsTmpl;
					var categoryXml = "";
					var contentXml = "";
					var deleteXml = "";
					var _this = this;

					var transformer = function(value, key) {
						if (key == "category") {
							value = xml.encodeXmlEntry("comment");
						} else if (key == "content") {
							value = xml.encodeXmlEntry(comment);
						} else if (key == "deleteWithRecord") {
							value = "true";
						} else if (key == "getCategory" && categoryXml != "") {
							value = categoryXml;
						} else if (key == "getContent" && contentXml != "") {
							value = contentXml;
						} else if (key == "getDeleteComment" && deleteXml != "") {
							value = deleteXml;
						}
						return value;
					};

					categoryXml = stringUtil.transform(FileCategoryTmpl, _this, transformer, _this);

					contentXml = stringUtil.transform(FileContentTmpl, _this, transformer, _this);
					if (isDelete) {
						deleteXml = stringUtil.transform(FileDeleteCommentTmpl, _this, transformer, _this);
					}

					payload = stringUtil.transform(payload, this, transformer, this);
					return payload;
				},
				_constructPayload : function(payloadMap, documentId) {

					var payload = FileEntryTmpl;
					var categoryXml = "";
					var idXml = "";
					var uuidXml = "";
					var labelXml = "";
					var titleXml = "";
					var summaryXml = "";
					var visibilityXml = "";
					var itemXml = "";
					var tagsXml = "";
					var notificationXml = "";
					var currentValue = null;
					var transformer = function(value, key) {
						if (currentValue) {
							value = xml.encodeXmlEntry(currentValue);
						} else if (key == "getCategory" && categoryXml != "") {
							value = categoryXml;
						} else if (key == "getId" && idXml != "") {
							value = idXml;
						} else if (key == "getUuid" && uuidXml != "") {
							value = uuidXml;
						} else if (key == "getLabel" && labelXml != "") {
							value = labelXml;
						} else if (key == "getTitle" && titleXml != "") {
							value = titleXml;
						} else if (key == "getSummary" && summaryXml != "") {
							value = summaryXml;
						} else if (key == "getVisibility" && visibilityXml != "") {
							value = visibilityXml;
						} else if (key == "getItem" && itemXml != "") {
							value = itemXml;
						} else if (key == "getTags" && tagsXml != "") {
							value = tagsXml;
						} else if (key == "getNotification" && notificationXml != "") {
							value = notificationXml;
						}
						return value;
					};

					for ( var currentElement in payloadMap) {
						currentValue = payloadMap[currentElement];
						if (currentElement.indexOf("category") != -1) {
							categoryXml = stringUtil.transform(FileCategoryTmpl, this, transformer, this);
						} else if (currentElement.indexOf("id") != -1) {
							idXml = stringUtil.transform(FileIdTmpl, this, transformer, this);
						} else if (currentElement.indexOf("uuid") != -1) {
							uuidXml = stringUtil.transform(FileUuidTmpl, this, transformer, this);
						} else if (currentElement.indexOf("label") != -1) {
							labelXml = stringUtil.transform(FileLabelTmpl, this, transformer, this);
							titleXml = stringUtil.transform(FileTitleTmpl, this, transformer, this);
						} else if (currentElement.indexOf("summary") != -1) {
							summaryXml = stringUtil.transform(FileSummaryTmpl, this, transformer, this);
						} else if (currentElement.indexOf("visibility") != -1) {
							visibilityXml = stringUtil.transform(FileVisibilityTmpl, this, transformer, this);
						} else if (currentElement.indexOf("itemId") != -1) {
							itemXml = stringUtil.transform(FileItemIdTmpl, this, transformer, this);
						} else if (currentElement.indexOf("tags") != -1) {
							var tags = currentValue;
							for ( var tag in tags) {
								tagsXml += stringUtil.transform(TagsTmpl, {
									"tag" : tags[tag]
								});
							}
						} else if (currentElement.contains("notification")) {
							notificationXml = stringUtil.transform(NotificationTmpl, this, transformer, this);
						}
					}
					currentValue = null;

					payload = stringUtil.transform(payload, this, transformer, this);
					return payload;
				}
			});
			return FileService;
		});

},
'sbt/connections/FileConstants':function(){
/*
 * � Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Definition of constants for FileService.
 */
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin({    	
               
        /**
         * XPath expressions used when parsing a Connections Files ATOM feed
         */
        FileFeedXPath : conn.ConnectionsFeedXPath,
        
        /**
         * XPath expressions used when parsing a Connections Comments ATOM feed
         */
        CommentFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * XPath expressions to be used when reading a File Entry
         */
        FileXPath : {        	
            // used by getEntityData
            entry : "/a:entry",
            id : "a:id",
            // used by getEntityId
            uid : "td:uuid",            
            label : "td:label",
            selfUrl : "a:link[@rel='self']/@href",
            alternateUrl : "a:link[@rel='alternate']/@href",
            downloadUrl : "a:link[@rel='enclosure']/@href",
            type : "a:link[@rel='enclosure']/@type",
            length : "a:link[@rel='enclosure']/@length",
			editLink : "a:link[@rel='edit']/@href",
			editMediaLink : "a:link[@rel='edit-media']/@href",
			thumbnailUrl : "a:link[@rel='thumbnail']/@href",
			commentsUrl : "a:link[@rel='replies']/@href",
									
			authorName : "a:author/a:name",			
			authorUserId : "a:author/snx:userid",
			authorEmail : "a:author/a:email",
			authorUserState : "a:author/snx:userState",
			
			title: "a:title",
			published : "a:published",
			updated : "a:updated",
			created: "td:created",
			modified: "td:modified",
			lastAccessed : "td:lastAccessed",
			
			
			modifierName : "td:modifier/td:name",			
			modifierUserId : "td:modifier/snx:userid",
			modifierEmail : "td:modifier/td:email",
			modifierUserState : "td:modifier/snx:userState",
			
			
			visibility : "td:visibility",
			libraryId : "td:libraryId",
			libraryType : "td:libraryType",
			versionUuid : "td:versionUuid",
			versionLabel : "td:versionLabel",
			propagation : "td:propagation",
			recommendationsCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']",
			commentsCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']",
			sharesCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/share']",
			foldersCount :  "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/collections']",
			attachmentsCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/attachments']",
			versionsCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/versions']",
			referencesCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/references']",
			totalMediaSize : "td:totalMediaSize",
			summary : "a:summary",
			contentUrl : "a:content/@src",
			contentType : "a:content/@type",
			objectTypeId : "td:objectTypeId",
			lock : "td:lock/@type",
			acls : "td:permissions",
			hitCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/hit']",
			anonymousHitCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/anonymous_hit']",
			tags : "a:category/@term",
			category : "a:category/@label"
        },
        

        /**
         * XPath expressions to be used when reading a Comment
         */
		CommentXPath : {
			entry : "a:entry",
			id : "a:id",
			uid : "td:uuid",
			title : "a:title",
			content : "a:content[@type='text']",
			created : "td:created",
			modified : "td:modified",
			versionLabel : "td:versionLabel",
			updated : "a:updated",
			published : "a:published",
			modifierName : "td:modifier/td:name",			
			modifierUserId : "td:modifier/snx:userid",
			modifierEmail : "td:modifier/td:email",
			modifierUserState : "td:modifier/snx:userState",		
			authorName : "a:author/a:name",			
			authorUserId : "a:author/snx:userid",
			authorEmail : "a:author/a:email",
			authorUserState : "a:author/snx:userState",
			language : "td:language",			
			deleteWithRecord : "td:deleteWithRecord"		
		},   
		
		/**
		 * Get a Feed for a File 
		 */
		AtomFileInstance : "${files}/basic/api/myuserlibrary/document/{documentId}/entry",
		
		/**
		 * 
		 */
		AtomFileInstancePublic : "${files}/basic/anonymous/api/library/{libraryId}/document/{documentId}/entry",

        /**
         * A feed of files of which the authenticated user owns.
         * 
         * Get the My Files feed to see a list of the files which the authenticated owns. 
         * Supports: acls , includePath , includeQuota , includeTags , page , ps , shared , sI , since , sortBy , sortOrder , tag , visibility
         */
        AtomFilesMy : "/${files}/basic/api/myuserlibrary/feed",
        
        /**
         * A feed of files with are shared by or with the authenticated user.
         * 
         * Get the My Files feed to see a list of the files which the authenticated owns. 
         * Supports: direction (default inbound : with me, outbound : by me),  acls , includePath , includeQuota , includeTags , page , ps , shared , sI , since , sortBy , sortOrder , tag , visibility
         */
        AtomFilesShared : "/${files}/basic/api/documents/shared/feed",
        
        /**
         * Get a feed that lists all public files.
         *         
         * Supports: acls , includePath , includeQuota , includeTags , page , ps , shared , sI , since , sortBy , sortOrder , tag , visibility
         */
        AtomFilesPublic : "/${files}/basic/anonymous/api/documents/feed?visibility=public",
        
        /**
         * Get feed of recycled files
         */
        AtomFilesRecycled : "/${files}/basic/api/myuserlibrary/view/recyclebin/feed",
        
        /**
         * Get a feed that lists your folders
         *         
         * Supports: access(editor or manager), creator, page, ps, shared, sharedWithMe, sI, sortBy, sortOrder, title, visibility 
         */
        AtomFoldersMy : "/${files}/basic/api/collections/feed",
        
        /**
         * Feed of public folders
         */
        AtomFoldersPublic : "/${files}/basic/anonymous/api/collections/feed",
        
        /**
         * Feed of folders you recently added files to
         */
        AtomFoldersActive : "/${files}/basic/api/collections/addedto/feed",
        
        /**
         * A feed of comments associated with all public files. Do not authenticate this request.
         *         
         * Supports: acls, category  Note: This parameter is required., commentId, identifier, page, ps, sI, sortBy, sortOrder  
         */
        AtomFileCommentsPublic : "/${files}/basic/anonymous/api/userlibrary/{userId}/document/{documentId}/feed?category=comment",
        
        /**
         * A feed of comments associated with files to which you have access. You must authenticate this request. 
         *         
         * Supports: acls, category  Note: This parameter is required., commentId, identifier, page, ps, sI, sortBy, sortOrder  
         */
        AtomFileCommentsMy : "/${files}/basic/api/userlibrary/{userId}/document/{documentId}/feed?category=comment",
        
        /**
         * Adds a comment to the specified file.
         * 
         * Supports : identifier - Indicates how the document is identified in the {document-id} variable segment of the web address. By default, look up is performed with the expectation that the URL contains the value from the <td:uuid> element of a File Atom entry. Specify "label" if the URL instead contains the value from the <td:label> element of a File Atom entry. 
         */
        AtomAddCommentToFile : "/${files}/basic/api/userlibrary/{userId}/document/{documentId}/feed",
        
        /**
         * Adds a comment to the specified file for logged in user.
         * 
         * Supports : identifier - Indicates how the document is identified in the {document-id} variable segment of the web address. By default, look up is performed with the expectation that the URL contains the value from the <td:uuid> element of a File Atom entry. Specify "label" if the URL instead contains the value from the <td:label> element of a File Atom entry. 
         */
        AtomAddCommentToMyFile : "/${files}/basic/api/myuserlibrary/document/{documentId}/feed",        
        
        /**
         * 	Update the Atom document representation of the metadata for a file from logged in user's library.
         * 
         * supports : 
         * propagate	Indicates if users that are shared with can share this document. The default value is true.
         * sendEmail	Indicates if an email notification is sent when sharing with the specified user. The default value is true.
         */
        AtomUpdateFileMetadata : "/${files}/basic/api/myuserlibrary/document/{documentId}/entry",
        
        /**
         * Get pinned files from my my favorites feed.
         * 
         */
        AtomFilesMyPinned : "/${files}/basic/api/myfavorites/documents/feed",
        
        /**
         * Add a file to my favorites feed of logged in user
         * 
         */
        AtomPinFile : "/${files}/basic/api/myfavorites/documents/feed",
        
        /**
         * Add file of list of files to folder
         */
        AtomAddFilesToFolder : "/${files}/basic/api/collection/{collectionId}/feed",
        
        /**
         * Delete a file and the Atom document representation of its associated metadata from logged in user's collection.
         */
        AtomDeleteFile :  "/${files}/basic/api/myuserlibrary/document/{documentId}/entry",
        
        /**
         * Lock or unlock a file
         */
        AtomLockUnlockFile : "/${files}/basic/api/document/{documentId}/lock",
        
        /**
         * Add the document to collections specified by atom entry or feed.
         */
        AtomAddFileToFolders : "/${files}/basic/api/userlibrary/{userId}/document/{documentId}/feed",
        /**
        * Add the document of logged in user to collections specified by atom entry or feed.
        */
        AtomAddMyFileToFolders : "/${files}/basic/api/myuserlibrary/document/{documentId}/feed",
        
        /**
         * Create a file folder programmatically.
         */
        AtomCreateFolder : "/${files}/basic/api/collections/feed",
        
        /**
         * Delete all files from recycle bin of specified user
         */
        AtomDeleteAllFilesFromRecyclebBin : "/${files}/basic/api/userlibrary/{userId}/view/recyclebin/feed",
        
        /**
         * Delete all files from recycle bin of logged in user
         */
        AtomDeleteMyFilesFromRecyclebBin : "/${files}/basic/api/myuserlibrary/view/recyclebin/feed",
        
        /**
         * Delete All Versions of a File
         */
        AtomDeleteAllVersionsOfAFile : "/${files}/basic/api/myuserlibrary/document/{documentId}/feed",
        
        /**
         * Delete a Comment for a File
         */
        AtomDeleteComment : "/${files}/basic/api/userlibrary/{userId}/document/{documentId}/comment/{commentId}/entry",
        
        /**
         * Delete a comment on file for logged in user         
         */
        AtomDeleteMyComment : "/${files}/basic/api/myuserlibrary/document/{documentId}/comment/{commentId}/entry",
        
        /**
         * Purge a file from Recycle Bin
         */
        AtomDeleteFileFromRecycleBin : "/${files}/basic/api/userlibrary/{userId}/view/recyclebin/{documentId}/entry",
        
        /**
         * Purge a file from REcycle Bin for Logged in user
         */
        AtomDeleteMyFileFromRecycleBin : "/${files}/basic/api/myuserlibrary/view/recyclebin/{documentId}/entry",
        
        /**
         * Remove a file Share
         */
        AtomDeleteFileShare : "/${files}/basic/api/shares/feed",
        
        /**
         * Delete a Folder
         */
        AtomDeleteFolder : "/${files}/basic/api/collection/{collectionId}/entry",
        
        /**
         * Get Files for a user 
         */
        AtomGetAllUsersFiles : "/${files}/basic/anonymous/api/userlibrary/{userId}/feed",
        
        /**
         * Get a comment for a file
         */
        AtomGetFileComment : "/${files}/basic/api/userlibrary/{userId}/document/{documentId}/comment/{commentId}/entry",
        
        /**
         * Get a comment for a File for logged in user
         */
        AtomGetMyFileComment : "/${files}/basic/api/myuserlibrary/document/{documentId}/comment/{commentId}/entry",
        
        /**
         * Get File from Recycle Bin
         */
        AtomGetFileFromRecycleBin : "/${files}/basic/api/userlibrary/{userId}/view/recyclebin/{documentId}/entry",
        
        /**
         * Get Files Awaiting Approval
         */
        AtomGetFilesAwaitingApproval : "/${files}/basic/api/approval/documents",
        
        /**
         * Get File Shares
         */
        AtomGetFileShares : "/${files}/basic/api/documents/shared/feed",
        
        /**
         * Get All Files in a Folder
         */
        AtomGetFilesInFolder : "/${files}/basic/api/collection/{collectionId}/feed",
        
        /**
         * Get Files in Recycle Bin of logged in user
         */
        AtomGetFilesInMyRecycleBin : "/${files}/basic/api/myuserlibrary/view/recyclebin/feed",
        
        /**
         * Get file with given version
         */
        AtomGetFileWithGivenVersion : "/${files}/basic/api/myuserlibrary/document/{documentId}/version/{versionId}/entry",
        
        /**
         * Get a folder
         */
        AtomGetFolder : "/${files}/basic/api/collection/{collectionId}/entry",
        
        /**
         * Get Folder with Recenty Added Files
         */
        AtomGetFoldersWithRecentlyAddedFiles : "/${files}/basic/api/collections/addedto/feed",
        
        /**
         * Get Pinned Folders
         */
        AtomGetPinnedFolders : "/${files}/basic/api/myfavorites/collections/feed",
        
        /**
         * Get Public Folders
         */
        AtomGetPublicFolders : "/${files}/basic/anonymous/api/collections/feed",
        
        /**
         * Pin/unpin a Folder
         */
        AtomPinFolder : "/${files}/basic/api/myfavorites/collections/feed",
        
        /**
         * Remove File from Folder
         */
        AtomRemoveFileFromFolder : "/${files}/basic/api/collection/{collectionId}/feed",
        
        /**
         * Restore File from Recycle Bin
         */
        AtomRestoreFileFromRecycleBin : "/${files}/basic/api/userlibrary/{userId}/view/recyclebin/{documentId}/entry",
        
        /**
         * Share File with Community or communities
         */
        AtomShareFileWithCommunities : "/${files}/basic/api/myuserlibrary/document/{documentId}/feed",
        
       /**
        * Update a Comment
        */
        AtomUpdateComment : "/${files}/basic/api/userlibrary/{userId}/document/{documentId}/comment/{commentId}/entry",
        
        /**
         * Update comment of logged in user
         */
        AtomUpdateMyComment : "/${files}/basic/api/myuserlibrary/document/{documentId}/comment/{commentId}/entry",
        
        /**
         * Add Comment To Community File
         */
        AtomAddCommentToCommunityFile : "/${files}/basic/api/communitylibrary/{communityId}/document/{documentId}/feed",
        
        /**
         * Get All Files in a Community
         */
        AtomGetAllFilesInCommunity : "/${files}/basic/api/communitylibrary/{communityId}/feed",
        
        /**
         * Get Community File
         */
        AtomGetCommunityFile : "/${files}/basic/api/communitylibrary/{communityId}/document/{documentId}/entry",
       
        /**
         * Update metadata of community File
         */
        AtomUpdateCommunityFileMetadata : "/${files}/basic/api/library/{libraryId}/document/{documentId}/entry"
    }, conn);
});
},
'sbt/connections/controls/forums/BackAction':function(){
/*
 * � Copyright IBM Corp. 2013
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

define([ "../../../declare", "../../../controls/grid/GridAction","../../../i18n!./nls/ForumGridRenderer"], 
        function(declare, GridAction, nls) {

    /**
     * @class BackAction
     * @namespace sbt.connections.controls.forum
     * @module sbt.connections.controls.forum.BackAction
     */
    var BackAction = declare(GridAction, {
       
    	
        /**ForumAction Constructor function
         * @method constructor
         * */
        constructor: function(args) {

        },
        
        showForums: function(item, grid, event){
        	var options = {
                start: grid.data.start, count: grid.pageSize
            }; 
        	grid.getForums(options);
        },
        showTopics: function(item, grid, event){
        	var options = {
                start: grid.data.start, count: grid.pageSize
            }; 
        	grid.getTopics("",options);
        }

    });

    return BackAction;
});
},
'sbt/connections/controls/forums/ForumAction':function(){
/*
 * � Copyright IBM Corp. 2013
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

define([ "../../../declare", "../../../controls/grid/GridAction","../../../i18n!./nls/ForumGridRenderer","../../../stringUtil"], 
        function(declare, GridAction, nls, stringUtil) {

    /**
     * @class ForumAction
     * @namespace sbt.connections.controls.forum
     * @module sbt.connections.controls.forum.forumAction
     */
    var ForumAction = declare(GridAction, {
        
    	nls: {
            tooltip: "Go to {title}"
        },
       
    	
        /**ForumAction Constructor function
         * @method constructor
         * */
        constructor: function() {
        },
        
        /**
         * Handles displaying a tooltip for an item
         * @method getTooltip
         * @param item The element that will use the tooltip
         */
        getTooltip: function(item) {
        	 return stringUtil.replace(this.nls.tooltip, { title : item.getValue("title") });
        },
        
        /**
         * The execute function is called from the handle click function
         * 
         * @method execute
         * @param item The item which fired the event
         * @param opts
         * @param event The event
         */
        execute: function(item, grid, event) {
        
        	var startOfId = item.getValue("id").lastIndexOf(":")+1;
        	var id = item.getValue("id").substring(startOfId,item.getValue("id").length);
        	
        	var options = {
                start: grid.data.start, count: grid.pageSize
            };
        	
        	if(grid.renderer.template == grid.renderer.topicTemplate){
        		grid.getTopicReplies(id,options);      		
        	}else{
            	grid.getTopics(id,options);
        	}
	
        }

    });

    return ForumAction;
});

},
'sbt/connections/controls/forums/ForumGrid':function(){
/*
 * � Copyright IBM Corp. 2013
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

define(["../../../declare",
        "../../../controls/grid/Grid",
        "../../../store/parameter",
        "./ForumGridRenderer", 
		 "./ForumAction",
		 "./BackAction",
        "../../../connections/ForumConstants"], 
    function(declare, Grid, parameter, ForumGridRenderer, ForumAction, BackAction, consts){
	
		/**Values that forums Can be sorted By, NOTE Sotring is not enabled in Connections*/
		var sortVals = {
				created: "created",
	       		modified: "modified" 
		};
		
		/**URL parameters */
		var ParamSchema = {
			pageNumber: parameter.oneBasedInteger("page"),	
			pageSize: parameter.oneBasedInteger("ps"),
			sortBy: parameter.sortField("sortBy",sortVals),
			sortOrder: parameter.sortOrder("sortOrder")						
		};
		
		/**
		 * @class ForumGrid 
		 * @namespace sbt.connections.controls.forum
		 * @module sbt.connections.controls.forum.ForumGrid
		 */
	    var ForumGrid = declare(Grid,{
	    	
	    	/**Hide the table header */
	    	hideHeader: false,
	    	
	        options : {
	            "my" : {
	                storeArgs : {
	                    url : consts.AtomForumsMy,
	                    attributes : consts.ForumXPath,
	                    feedXPath : consts.ForumsFeedXPath,
	                    paramSchema: ParamSchema
	                },
	                rendererArgs : {
	                    type : "my"
	                }
	            },
	            "public" : {
	                storeArgs : {
	                    url : consts. AtomForumsPublic,
	                    attributes : consts.ForumXPath,
	                    feedXPath : consts.ForumsFeedXPath,
	                    paramSchema: ParamSchema
	                },
	                rendererArgs : {
	                    type : "public"
	                }
	            },
	            "myTopics" : {
	                storeArgs : {
	                    url : consts. AtomTopicsMy,
	                    attributes : consts.ForumTopicXPath,
	                    feedXPath : consts.ForumsFeedXPath,
	                    paramSchema: ParamSchema
	                },
	                rendererArgs : {
	                    type : "myTopics"
	                }
	            }	            
	        },
		    
	        /**The default Forum Grid that will be created, if another type is not specified */
	        defaultOption: "my",
	        
	        /**forumAction handles onClick and tooltip functions */
	        forumAction : new ForumAction(),
	        backAction: new BackAction(),
	        hideBreadCrumb: true,
	        
	        /**
	         * The constructor function.
	         * @method constructor
	         * @param args
	         */
	        constructor: function(args){
	        	if(args.hideHeader){
	        		this.hideHeader = args.hideHeader;
	        	}
	        	if(args.baseProfilesUrl){
	        		this.baseProfilesUrl = args.baseProfilesUrl;
	        	}
	        	
	        },
	        
	        /**
	         * Creates a renderer for the grid.The renderer is responsible for 
	         * loading the grid's HTML content.
	         * @method createDefaultRenderer
	         * @param args sets the template the renderer will use, by checking args.type
	         * @returns an instance of a ForumGridRenderer.
	         */
	        createDefaultRenderer : function(args) {
	            return new ForumGridRenderer(args);
	        },
	        
	        /**
	         * In the grid HTML an element can have an event attached 
	         * using dojo-attach-event="onClick: handleClick".
	         * This method is the handler for the onclick event.
	         * @method handleClick
	         * @param el the element that fired the event
	         * @param data all of the items from the current row of the grid. 
	         * @param ev the event 
	         */
	        handleClick: function(el, data, ev) {
	            if (this.forumAction) {
	                this._stopEvent(ev);
	                this.forumAction.execute(data, this , ev);
	            }
	        },
	    	
	        /**
	         * In the grid HTML an element can have an event attached 
	         * using dojo-attach-event="onClick: handleClick".
	         * This method is the handler for the onclick event.
	         * This function is for viewing the profile of the forus author. 
	         * @method handleClick
	         * @param el the element that fired the event
	         * @param data all of the items from the current row of the grid. 
	         * @param ev the event 
	         */
	        
	        getForums: function(options){
	        	
	        	this.renderer.template = this.renderer.forumTemplate;
	        	this.renderer.headerTemplate = this.renderer.forumHeader;
	        	this.store.setAttributes(consts.ForumXPath);
	        	this.hideBreadCrumb = true;
	        	var endpoint = this.store.getEndpoint();
	        	
	        	if(this.params.type == "my"){
	        		var url = this.buildUrl(consts.AtomForumsMy, {},endpoint);
	        		this.store.setUrl(url);
	        	}else{
	        		var url = this.buildUrl(consts.AtomForumsPublic, {},endpoint);
	        		this.store.setUrl(url);
	        	}

	        	this.update(null);
	        },
	        
	        _forumID: "",
	        
	        /**
	         * 
	         * Show forum Topics
	         * @param forumId
	         * @param options
	         */
	        getTopics: function(forumId,options){
	        		        
	        	if(forumId != ""){
	        		this._forumID = forumId;
	        	}

	        	this.renderer.template = this.renderer.topicTemplate;
	        	this.renderer.headerTemplate = this.renderer.topicHeader;
	        	this.renderer.breadCrumb = this.renderer.topicBreadCrumb;
	        	this.store.setAttributes(consts.ForumTopicXPath);
	        	this.hideBreadCrumb = false;
	        	var endpoint = this.store.getEndpoint();
	        	        	
	        	if(this.params.type=="myTopics"){
	        		var url = this.buildUrl(consts.AtomTopicsMy, {},endpoint);
	        		this.store.setUrl(url);
	        		this.hideBreadCrumb = true;
	        	}else{
	        		var url = this.buildUrl(consts.AtomTopics+"?forumUuid="+this._forumID, {}, endpoint);
	        		this.store.setUrl(url);
	        	}
	        	
	        	this.update(null);
	        },
	        
	        getTopicReplies: function(topicId,options){
	        	
	        	this.renderer.template = this.renderer.replyTemplate;
	        	this.renderer.headerTemplate = this.renderer.replyHeader;
	        	this.store.setAttributes(consts.ForumReplyXPath);
	        	this.hideBreadCrumb = false;
	        	var endpoint = this.store.getEndpoint();
	        	if(this.params.type=="myTopics"){
	        		this.renderer.breadCrumb = this.renderer.myTopicsBreadCrumb;
	        	}else{
	        		this.renderer.breadCrumb = this.renderer.replyBreadCrumb;
	        	}
	        	
	        	
	        	var url = this.buildUrl(consts.AtomReplies+"?topicUuid="+topicId,{},endpoint);
	        	this.store.setUrl(url);
	        	
	        	this.update(null);
	        },
	        
	        showTopics: function(el, data, ev){
	        	if (this.backAction) {
	                this._stopEvent(ev);
	                this.backAction.showTopics(data, this , ev);
	            }
	        },
	        
	        showForums: function(el, data, ev){
	        	if (this.backAction) {
	                this._stopEvent(ev);
	                this.backAction.showForums(data, this , ev);
	            }
	        },
	        
	        /**
	         * Add the since parameter to the URL, so that all forums will be 
	         * displayed and not just those that have been recently modified.
	         * The since parameter returns all entries last modified since a specified date. 
	         * Specify the date in the number of milliseconds since Unix EPOCH.  
	         * In this case 1 is used so all forums will be displayed.
	         * @param url The Rest API URL for the forum feed
	         * @param args
	         * @param endpoint An endpoint which may contain custom service mappings.
	         * @returns
	         */
	        buildUrl: function(url, args, endpoint) {	        	
	            var urlParams = { since: 1};
	            if (this.query) {
	            	params = lang.mixin(params, this.query);
	            }
	            if (this.direction) {
	            	params = lang.mixin(params, { direction : this.direction });
	            } 
	            return this.constructUrl(url, urlParams, {}, endpoint);
	        }
	        

		});
	
	    return ForumGrid;
});

},
'sbt/connections/controls/forums/ForumGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

define(["../../../declare",
        "../ConnectionsGridRenderer",
        "../../../i18n",
        "../../../text!./templates/ForumRow.html",
        "../../../text!./templates/TableHeader.html",
        "../../../text!./templates/TopicRow.html",
        "../../../text!./templates/TopicHeader.html",
        "../../../text!./templates/ReplyRow.html",
        "../../../text!./templates/ReplyHeader.html",
        "../../../text!./templates/ReplyBreadCrumb.html",
        "../../../text!./templates/TopicBreadCrumb.html",
        "../../../text!./templates/MyTopicsBreadCrumb.html",
        "../../../i18n!./nls/ForumGridRenderer"], 

    function(declare, ConnectionsGridRenderer, i18n, ForumRow, tableHeader, TopicRow, 
    		TopicHeader, ReplyTemplate, ReplyHeader,ReplyBreadCrumb,TopicBreadCrumb, 
    		MyTopicsBreadCrumb, nls){
		
		/**
		 * @class ForumGridRenderer
		 * @namespace sbt.connections.controls.forum
		 * @module sbt.connections.controls.forum.ForumGridRenderer
		 */
	    var ForumGridRenderer = declare(ConnectionsGridRenderer,{
	    	
	    	/**Strings used by the forum grid */
	    	_nls:nls,
	    	
	    	topicTemplate: TopicRow,
	    	
	    	topicHeader: TopicHeader,
	    	
	    	replyTemplate: ReplyTemplate,
	    	
	    	replyHeader: ReplyHeader,
	    	
	    	forumTemplate: ForumRow,
	    	
	    	forumHeader: tableHeader,
	    	
	    	headerTemplate: tableHeader,
	    	
	    	replyBreadCrumb: ReplyBreadCrumb,
	    	
	    	topicBreadCrumb: TopicBreadCrumb,
	    	
	    	myTopicsBreadCrumb:MyTopicsBreadCrumb,
	    	
	    	breadCrumb: ReplyBreadCrumb,
	    	
	    	/**
	    	 * The constructor function
	    	 * @method constructor
	    	 * @param args
	    	 */
	    	constructor: function(args){
	    		
	    		if(args.type=="myTopics"){
	    			this.template = this.topicTemplate;
	    			this.headerTemplate = this.topicHeader;
	    		}else{
	    			this.template = this.forumTemplate;
	    		}
	    		
	    	},
	    	
	    	/**
	         * Function to render the Paging, the table and the table header.
	         * @method - render
	         * @param grid - the grid
	         * @param el - the grid DOM node
	         * @param items - the items in the grid row, for example icon, displayName,email etc.
	         * @param data - the data associated with the grid row
	         */
	        render: function(grid, el, items, data) {
	           while (el.childNodes[0]) {
	               this._destroy(el.childNodes[0]);
	           }
	           var size = items.length;
	           if (size === 0) {
	              this.renderEmpty(grid, el);
	           }
	           else {
	              this.renderPager(grid, el, items, data);
	              
	              this.renderBreadCrumb(grid, el, items, data);
	              
	              var tbody = this.renderTable(grid, el, items, data);
	              
	              this.renderHeader(grid, el, items, data, tbody);
	              
	              for (var i=0; i<items.length; i++) {
	                  this.renderItem(grid, tbody, data, items[i], i, items);
	              }
	             
	           }
	        },
	        
	    	/**
	         * Checks if a tableHeader exists ,if so, the HTML template is converted to
	         * a DOM node, and attached to the body.
	         * @method - renderHeader
	         * @param grid - the grid
	         * @param el - the current element
	         * @param items - the items in the current element
	         * @param data - the data associated with the current element
	         */
	        renderHeader : function(grid,el,items,data,tbody) {
	            if (this.headerTemplate && !grid.hideHeader) {
	                var node;
	                if (this._isString(this.headerTemplate)) {
	                    var domStr = this._substituteItems(this.headerTemplate, grid, this, items, data);
	                    node = this._toDom(domStr, el.ownerDocument);
	                } else {
	                    node = this.headerTemplate.cloneNode(true);
	                }
	                tbody.appendChild(node);
	                
	                this._doAttachEvents(grid, tbody, data);
	            }
	        },
	        
	        renderBreadCrumb: function(grid,el,items,data) {
	            if (this.breadCrumb && !grid.hideBreadCrumb) {
	                var node;
	                if (this._isString(this.breadCrumb)) {
	                    var domStr = this._substituteItems(this.breadCrumb, grid, this, items, data);
	                    node = this._toDom(domStr, el.ownerDocument);
	                } else {
	                    node = this.breadCrumb.cloneNode(true);
	                }
	                el.appendChild(node);
	                
	                this._doAttachEvents(grid, el, data);
	            }
	        },
	    	 /***
	         * Creates a table and table body, Attaches the table body to the 
	         * table, and returns the table body
	         * @method - renderTable
	         * @param grid - the grid
	         * @param el - the current element
	         * @param items - all of the items in the current row
	         * @param data - the data associated with the current row
	         * @returns - A table body element, that is attached to a table
	         */
	        renderTable: function(grid, el, items, data) {       	
	            var table = this._create("table", {
	                "class": this.tableClass,
	                border:"0",
	                cellspacing:"0",
	                cellpadding:"0",
	               // summary:this.nls.summary
	                role:"presentation"
	            }, el);
	            var tbody = this._create("tbody", null, table);
	            return tbody;
	        },
	        
	        /**
	         * Creates a Div, with a different CSS class, to display a grid that has no results
	         * @method - renderEmpty
	         * @param - grid - The Grid
	         * @param - el - The Current Element
	         */
	        renderEmpty: function(grid, el,data) {
	           while (el.childNodes[0]) {
	               this._destroy(el.childNodes[0]);
	           }
	            
	           this.renderBreadCrumb(grid, el, data );
	           
	           var ediv = this._create("div", {
		             "class": this.emptyClass,
		             innerHTML: "<h2>" + this.nls.noResults +"</h2>",
		             role: "document",
		             tabIndex: 0
		           }, el, "only");
	           
	           this._doAttachEvents(grid, el, data);
	        },
	        
	        
	        getDateLabel: function(grid, item, i, items){
	             var result = i18n.getUpdatedLabel(item.getValue('updated'));
	             return result;
	        },
	        
	        getParentLink: function(grid, item, i, items){
	        	
	        	if(grid.params.type == "myTopics"){
	        		return item.getValue("topicForumTitle");
	        	}
	        	return '<a class="lotusBreakWord" href="#" data-dojo-attach-event="onclick: previousPage" >'+item.getValue("topicForumTitle")+'</a>';
	        },
	         
	        /**
	          * Displays a tooltip by calling the getTooltip function in the ForumAction class
	          * @method tooltip
	          * @param grid The Grid element
	          * @param item the element to display the tooltip
	          * @param i the number of the current row
	          * @param items all of the items in the grid row
	          * @returns A String used as a tooltip
	          */
	         tooltip: function(grid, item, i, items) {
	             if (grid.forumAction) {
	                 return grid.forumAction.getTooltip(item);
	             }
	         },
	    	
	         getUserProfileHref: function(grid,item,i,items){
	        	 return this.getProfileUrl(grid,item.getValue("authorUserid"));
	         }
	    });
	
	return ForumGridRenderer;
});
},
'sbt/connections/controls/nls/ConnectionsGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
  root: {
      empty : "Empty",
      loading : "Loading...",
      previous : "Previous",
      previousPage : "Previous Page",
      next : "Next",
      nextPage : "Next Page",
      pagingResults : "${start} - ${end} of ${totalCount}",
      sortBy : "Sort by:",
      msgNoData : "Please wait...",
      show10Items : "Show 10 items",
      show25Items : "Show 25 items",
      show50Items : "Show 50 items",
      show100Items : "Show 100 items",
      items : "items",
      feed : "${nls.feed}"
  }
});


},
'sbt/connections/controls/nls/WidgetWrapper':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
  root: {
      empty : "Empty",
      loading : "Loading...",
      msgNoData : "Please wait...",
      defaultTemplate : "No template specified."
  }
});


},
'sbt/connections/controls/profiles/ColleagueGrid':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../../declare", "../../../config", "../../../lang", "../../../xml", "../../../xpath", "../../../stringUtil", 
         "../../../connections/ConnectionsConstants",
         "./ProfileGrid", "./ColleagueGridRenderer", "../ViewAllAction" ], 
        function(declare, config, lang, xml, xpath, stringUtil, conn, ProfileGrid, ColleagueGridRenderer, ViewAllAction) {

    /**
     * @class ColleagueGrid
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ColleagueGrid
     */
	var ColleagueGrid = declare(ProfileGrid, {

		/**
		 * Flag used to display the View All link, set
		 * to true to hide this link
		 */
        hideViewAll: false,
        
        /**
         * A view all action, defines default behaviour for when 
         * View All link is selected
         */
        viewAllAction: new ViewAllAction(),
                
        /**
         * Creates a renderer for the grid.The renderer is responsible for 
         * loading the grid's HTML content.
         * @method createDefaultRenderer
         * @param args sets the template the renderer will use, by checking args.type, but for
         * profile grids this will always be "profile"
         * @returns an instance of a  profile gird renderer.
         */
        createDefaultRenderer : function(args) {
            return new ColleagueGridRenderer(args);
        },
        
        contextRootMap: {
            profiles: "profiles"
        },
        
        /**
         * Override buildUrl to add outputType, format and email/userid's
         * 
         * @method buildUrl
         * @param url base url
         * @param args arguments that will be passed to the store
         * @param endpoint An endpoint which may contain custom service mappings.
         * @returns Built url
         */
        buildUrl: function(url, args, endpoint) {
            var params = { 
            	connectionType : "colleague",
            	outputType : "profile",
            	format : "full"
            };
            
            if (this.query) {
            	params = lang.mixin(params, this.query);
            }
            if (this.email) {
            	params = lang.mixin(params, { email : this.email });
            } 
            if (this.email1 && this.email2) {
            	params = lang.mixin(params, { email : this.email1 + "," + this.email2 });
            } 
            if (this.userid) {
            	params = lang.mixin(params, { userid : this.userid });
            } 
            if (this.userid1 && this.userid2) {
            	params = lang.mixin(params, { userid : this.userid1 + "," + this.userid2 });
            } 

            return this.constructUrl(url, params, this.getUrlParams(), endpoint);
        },

        /**
         * The post create function is called, after the grid has been created.
         * The function will call the super classes post create
         * then if target emails/ids were set will.
         * @method postCreate
         */
        postCreate: function() {            
            this.inherited(arguments);
            
            if (this.targetEmails || this.targetUserids) {
                this.addColleagues();
            }
        },
        
        /**
         * Compute colleagues based on specified targets and add to the Grid.
         * 
         * @method addColleagues
         */
        addColleagues: function() {
            var targets = arguments.targetEmails || arguments.targetUserids || this.targetEmails || this.targetUserids;
            var id = arguments.email || arguments.userid || this.email || this.userid;
            if (!id || (!targets || targets.length == 0)) {
                return;
            }
            var endpoint = config.findEndpoint(this.endpoint || "connections");
            var baseUrl = "/profiles/atom/connection.do?connectionType=colleague";
            if (this._isEmail(id)) {
                baseUrl += "&sourceEmail=" + encodeURIComponent(id);
            } else {
            	baseUrl += "&sourceUserid=" + encodeURIComponent(id);
            }
            if (arguments.targetEmails || this.targetEmails) {
                baseUrl += "&targetEmail=";
            } else {
                baseUrl += "&targetUserid=";
            }
            var self = this;
            for (var i=0; i<targets.length; i++) {
                if (id == targets[i]) {
                    self.addProfile(endpoint, id);
                } else {
                    var targetUrl = baseUrl + encodeURIComponent(targets[i]);      
                    endpoint.xhrGet({
                        serviceUrl: targetUrl,
                        handleAs: "text",
                        load: function(response) {
                            var document = xml.parse(response);
                            var email = self._getColleagueEmail(document, id);
                            if (email) {
                            	self.addProfile(endpoint, email);
                            }
                        },
                        error: function(error) {
                            // can ignore this, means user is not a colleague
                        }
                    });
                }
            }
        },
        
        /**
         * @method addProfile
         * @param endpoint
         * @param id
         * @param index
         */
        addProfile: function(endpoint, id, index) {
            if (!index) {
                index = 0;
            }
            var self = this;
            var content = {};
            if (this._isEmail(id)) {
                content.email = id;
            } else {
                content.userid = id;
            }
            endpoint.xhrGet({
                serviceUrl : "/profiles/atom/profile.do",
                handleAs: "text",
                content: content,
                load: function(profile) {
                    self.insertItem(xml.parse(profile), index);
                    self.update();
                },
                error: function(error) {
                    self._updateWithError(error);
                }
            });
        },
        
        /**
         * In the grid HTML an element can have an event attached 
         * using dojo-attach-event="onClick: handleViewAll".
         * This method is the handler for the onclick event.
         * @method handleViewAll
         * @param el the element that fired the event
         * @param data all of the items from the current row of the grid. 
         * @param ev the event 
         */
        handleViewAll: function(el, data, ev) {
            if (this.viewAllAction) {
                console.log(data);
                this._stopEvent(ev);
                
                this.viewAllAction.execute(data, { grid : this.grid }, ev);
            }
        },
        
        // Internals
        
        _getColleagueEmail: function(doc, id) {
        	var userids = this._selectArray(doc, "/a:entry/snx:connection/a:contributor/snx:userid");
        	var emails = this._selectArray(doc, "/a:entry/snx:connection/a:contributor/a:email");
        	if (this._isEmail(id) && emails.indexOf(id) >= 0 && emails.length > 1) {
        		var index = (emails.indexOf(id) == 0) ? 1 : 0;
                return emails[index];
            } else if(userids.indexOf(id) >= 0 && userids.length > 1) {
        		var index = (userids.indexOf(id) == 0) ? 1 : 0;
                return userids[index];
            }
        },
        
        _selectArray : function(doc, expr) {
            var nodes = xpath.selectNodes(doc, expr, conn.Namespaces);
            var ret = [];
            if (nodes) {
                for ( var i = 0; i < nodes.length; i++) {
                    ret.push(stringUtil.trim(nodes[i].text || nodes[i].textContent));
                }
            }
            return ret;
        },
        
        _isEmail: function(id) {
        	return id && id.indexOf('@') >= 0;
        }
        
    });

    return ColleagueGrid;
});
},
'sbt/connections/controls/profiles/ProfileGrid':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.connections.controls.profiles.ProfileGrid
 */
define([ "../../../declare", 
		 "../../../config",
		 "../../../lang",
		 "../../../controls/grid/Grid", 
		 "./ProfileGridRenderer", 
		 "./ProfileAction", 
		 "../../../connections/controls/vcard/SemanticTagService", 
		 "../../../store/parameter",
		 "../../../connections/ProfileConstants",
		 "../../../connections/CommunityConstants"], 
        function(declare, sbt, lang, Grid, ProfileGridRenderer, ProfileAction, SemanticTagService, parameter, consts, communities) {

	var sortVals = {
			displayName: "displayName",
       		recent: "3" 
	};
	
	var communityMembersSortVals = {
			displayName: "displayName",
       		created: "created" 
	};
	
	var ParamSchema = {
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortBy",sortVals),
		sortOrder: parameter.sortOrder("sortOrder")
	};
	
	var CommunityMembersParamSchema = {
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortBy", communityMembersSortVals),
		sortOrder: parameter.sortOrder("sortOrder")
	};
	
    /**
     * @class ProfileGrid
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ProfileGrid
     */
    var ProfileGrid = declare(Grid, {
    	
    	/**
    	 * @param options, This is a list of all
    	 * the different types of profile grids available.
    	 * Depending on which one is selected specific arguments will be given to
    	 * the atom store and grid renderer.
    	 */
        options : {
            "profile" : {
                storeArgs : {
                    url : consts.AtomProfileDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "colleagues" : {
                storeArgs : {
                     url : consts.AtomConnectionsDo,
                     attributes : consts.ProfileXPath,
                     feedXPath : consts.ProfileFeedXPath,
                     paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "connectionsInCommon" : {
                storeArgs : {
                    url : consts.AtomConnectionsInCommonDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "peopleManaged" : {
                storeArgs : {
                    url : consts.AtomPeopleManagedDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "reportingChain" : {
                storeArgs : {
                    url : consts.AtomReportingChainDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "search" : {
                storeArgs : {
                    url : consts.AtomSearchDo,
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "dynamic" : {
                storeArgs : {
                    attributes : consts.ProfileXPath,
                    feedXPath : consts.ProfileFeedXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "profile"
                }
            },
            "communityMembers" : {
                storeArgs : {
                	url : communities.AtomCommunityMembers,
                    attributes : communities.MemberXPath,
                    feedXPath : communities.CommunityFeedXPath,
                    paramSchema: CommunityMembersParamSchema
                },
                rendererArgs : {
                    type : "communityMembers"
                }
            }
        },
        
        /**
         * A profile action, defines default behaviour for when 
         * items in the grid are clicked on or hovered on,
         * it is possible to override these actions
         */
        profileAction: new ProfileAction(),
        
        /**
         * This is the default grid that will be created if no 
         * arguments are given.
         */
        defaultOption: "colleagues",
        
        /**Constructor function
         * @method constructor
         * */
        constructor: function(args){
        	if(args.type == "peopleManaged" || args.type == "reportingChain" || args.type == "profile") {
        		this.hideSorter = true;
        	} 	
        	
            var nls = this.renderer.nls;

            if (args.type == "communityMembers") {
            	
            	this._sortInfo = {
            			displayName: { 
            				title: nls.displayName, 
       	                    sortMethod: "sortByTitle",
       	                    sortParameter: "title"   
            			},
       	                recent: {
       	                	title: nls.created, 
       	                    sortMethod: "sortByCreated",
       	                    sortParameter: "created"   
       	                }
            	};
       		 	this._activeSortAnchor = this._sortInfo.created;
       		 	this._activeSortIsDesc = false;
            } else {
            	this._sortInfo = {
            			displayName: { 
            				title: nls.displayName, 
            				sortMethod: "sortByDisplayName",
            				sortParameter: "displayName" 
            			},
            			recent: {
            				title: nls.recent, 
            				sortMethod: "sortByRecent",
            				sortParameter: "recent"   
            			}
               
            	};
            	this._activeSortAnchor = this._sortInfo.recent;
                this._activeSortIsDesc = false;
            }
            
        },
        
        contextRootMap: {
            profiles: "profiles"
        },
        
        /**
         * Override buildUrl to add outputType, format and email/userid's
         * 
         * @method buildUrl
         * @param url base url
         * @param args arguments that will be passed to the store
         * @param endpoint An endpoint which may contain custom service mappings.
         * @returns Built url
         */
        buildUrl: function(url, args, endpoint) {
            var params = { 
            	outputType : "profile",
            	format : "full"
            };
            
            if (this.query) {
            	params = lang.mixin(params, this.query);
            }
            if (this.type == "colleagues") {
            	params = lang.mixin(params, { connectionType : "colleague" });
            } else if (this.type == "communityMembers") {
            	params = lang.mixin(params, { communityUuid : this.communityUuid });
            }
         
            if (this.email) {
            	params = lang.mixin(params, { email : this.email });
            } 
            if (this.email1 && this.email2) {
            	params = lang.mixin(params, { email : this.email1 + "," + this.email2 });
            } 
            if (this.userid) {
            	params = lang.mixin(params, { userid : this.userid });
            } 
            if (this.userid1 && this.userid2) {
            	params = lang.mixin(params, { userid : this.userid1 + "," + this.userid2 });
            } 

            return this.constructUrl(url, params, this.getUrlParams(), endpoint);
        },

        /**
         * The post create function is called, after the grid has been created.
         * The function will call the super classes post create
         * then load the semantic tag service. The semantic tag service
         * is Javascript for creating business card functionality.
         * @method postCreate
         */
        postCreate: function() {        	
        	this.inherited(arguments);
        	
        	SemanticTagService.loadSemanticTagService();
        },
        
        /**
         * Creates a renderer for the grid.The renderer is responsible for 
         * loading the grid's HTML content.
         * @method createDefaultRenderer
         * @param args sets the template the renderer will use, by checking args.type, but for
         * profile grids this will always be "profile"
         * @returns an instance of a  profile gird renderer.
         */
        createDefaultRenderer : function(args) {
            return new ProfileGridRenderer(args);
        },
        
        /**
         * In the grid HTML an element can have an event attached 
         * using dojo-attach-event="onClick: handleClick".
         * This method is the handler for the onclick event.
         * @method handleClick
         * @param el the element that fired the event
         * @param data all of the items from the current row of the grid. 
         * @param ev the event 
         */
        handleClick: function(el, data, ev) {
            if (this.profileAction) {
            	console.log(data);
                this._stopEvent(ev);
                
                this.profileAction.execute(data, { grid : this.grid }, ev);
            }
        },
        
        /**
         * @method getSortInfo
         * @returns A list of strings that describe how the grid can be sorted
         * for profile grids these strings are "Display Name" and "Recent"
         */
        getSortInfo: function() {
        	return {
        		active: {
        			anchor: this._activeSortAnchor,
        			isDesc: this._activeSortIsDesc
        		},
        		list: [this._sortInfo.displayName, this._sortInfo.recent]
            };
        	
        },
                
        sortByDisplayName: function(el, data, ev){
        	this._sort("displayName", true, el, data, ev);
        },

        sortByRecent: function(el, data, ev){
        	this._sort("recent", true, el, data, ev);
        },
        
        sortByCreated: function(el, data, ev){
        	this._sort("created", true, el, data, ev);
        },
        
        sortByTitle: function(el, data, ev){
        	this._sort("title", true, el, data, ev);
        }

        // Internals
        
    });

    return ProfileGrid;
});
},
'sbt/connections/controls/profiles/ProfileGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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
        "../../../i18n!./nls/ProfileGridRenderer",
        "../../../text!./templates/ProfileRow.html",
        "../../../text!./templates/SharedConnectionsRow.html",
        "../../../text!./templates/StatusUpdateRow.html",
        "../../../text!./templates/CommunityMemberRow.html"], 
        function(declare, ConnectionsGridRenderer, nls, profileTemplate, sharedConnTemplate, statusUpdateTemplate, communityMemberTemplate) {
		
    /**
     * @class ProfileGridRenderer
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ProfileGridRenderer
     */
    var ProfileGridRenderer = declare(ConnectionsGridRenderer, {

          /**The strings used in the grid, these are stored in a separate file, in the nls folder*/
         _nls: nls, 
        
         /**
          * The constructor function
          * @method constructor
          * @param args
          */
         constructor: function(args) {
             if (args.type == "profile") {
                 this.template = profileTemplate;
             } else if(args.type == "statusUpdates"){
            	 this.template = statusUpdateTemplate;
             } else if (args.type == "communityMembers") {
            	 this.template = communityMemberTemplate;
             }
         },

         /**
          * Sets the css class for the row
          * @method rowClass
          * @param grid The Grid Dijit 
          * @param item the current row
          * @param i the number of the current row, ie 0, 1, 2 etc
          * @param items all of the rows in the grid
          */
         rowClass: function(grid, item, i, items) {
             item.rowClass = (i === 0 ? "lotusFirst" : (i % 2 === 1 ? "lotusAltRow" : null));
         },
         
         /**
          * Displays a tooltip by calling the getTooltip function in the ProfileAction class
          * @method tooltip
          * @param grid The Grid Dijit
          * @param item the element to display the tooltip
          * @param i the number of the current row
          * @param items all of the items in the grid row
          * @returns A Tooltip the default for profiles is to display the vCard
          */
         tooltip: function(grid, item, i, items) {
             if (grid.profileAction) {
                 return grid.profileAction.getTooltip(item);
             }
         },
         
         /**
          * Generates the profile photo URL for displaying the photos of community members
          * @method photoUrl
          * @param grid The Grid Dijit
          * @param item the element containing the uid
          * @param i the number of the current row
          * @param items all of the items in the grid row
          * @returns A profile photo URL for retrieving a user's profile picture
          */
         photoUrl: function(grid, item, i, items) {
        	 var store = grid.store;
        	 var endpoint = store.getEndpoint();
        	 var photoUrl = endpoint.baseUrl + "/profiles/photo.do?email=" + item.getValue("email");
      	 
        	 return photoUrl;
         }
    });
    
    return ProfileGridRenderer;
});
},
'sbt/connections/controls/profiles/ProfileAction':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../../declare", "../../../controls/grid/GridAction" ], 
        function(declare, GridAction) {

    /**
     * @class ProfileAction
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ProfileAction
     */
    var ProfileAction = declare(GridAction, {
        
    	/**Strings used in the actions */
        nls: {
            tooltip: "Go to ${name}"
        },
        
        /**ProfileAction Constructor function
         * @method constructor
         * */
        constructor: function() {
        },
        
        /**
         * Handles displaying a tooltip for an item
         * For profiles, the tooltip by default will be a business card
         * So nothing is done in this function
         * @method getTooltip
         * @param item The element that will use the tooltip
         */
        getTooltip: function(item) {
        	
        	//for default the semantic tag service will pop up the business card
        	//so do nothing here

        },
        
        /**
         * The execute function is called from the handle click function
         * For Profiles by default the business card functionality is used
         * which works from the Semantic tag service so nothing is done here.
         * @method execute
         * @param item The item which fired the event
         * @param opts
         * @param event The event
         */
        execute: function(item, opts, event) {
        
        	//for default the semantic tag service will pop up the business card
        	//so do nothing here
        }

    });

    return ProfileAction;
});
},
'sbt/connections/controls/profiles/ColleagueGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../../declare", "../../../stringUtil",
        "../ConnectionsGridRenderer",
        "../../../i18n!./nls/ColleagueGridRenderer",
        "../../../text!./templates/ColleagueItem.html",
        "../../../text!./templates/ViewAll.html",
        "../../../text!./templates/ColleagueItemFull.html"], 
        function(declare, stringUtil, ConnectionsGridRenderer, nls, colleagueItemTemplate, viewAllTemplate, colleagueItemFullTemplate) {
		
    /**
     * @class ColleagueGridRenderer
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ColleagueGridRenderer
     */
    var ColleagueGridRenderer = declare(ConnectionsGridRenderer, {

         _nls: nls,
         
         containerClass: "lotusChunk",
         
         template: colleagueItemTemplate,
         viewAllTemplate: viewAllTemplate,
         fullTemplate: colleagueItemFullTemplate,
         
         countAlign: "left",
         verticalAlign: "middle",
        
         render: function(grid, el, items, data) {
             while (el.childNodes[0]) {
                 this._destroy(el.childNodes[0]);
             }
             var size = items.length;
             if (size === 0) {
                this.renderEmpty(grid, el);
             }
             else {
                var container = this.renderContainer(grid, el, items, data);
                for (var i=0; i<items.length; i++) {
                    this.renderItem(grid, container, data, items[i], i, items);
                }
                this.renderViewAll(grid, el, items, data);
             }
          },

          renderContainer: function(grid, el, items, data) {          
              return this._create("div", { "class": this.containerClass }, el);
          },
          
          renderViewAll: function(grid, el, items, data) {          
              if (this.viewAllTemplate && !grid.hideViewAll) {
                  var node;
                  if (this._isString(this.viewAllTemplate)) {
                      var domStr = this._substituteItems(this.viewAllTemplate, grid, this, items, data);
                      node = this._toDom(domStr, el.ownerDocument);
                  } else {
                      node = this.sortTemplate.cloneNode(true);
                  }
                  el.appendChild(node);
                  
                  this._doAttachEvents(grid, el, data);
              }
          },
          
          viewAllTitle: function(grid, renderer, items, data) {
              var str = (data.totalCount == 1) ? nls.person : nls.people;
              var totalCount = stringUtil.replace(str, { totalCount : data.totalCount });
              return stringUtil.replace(nls.viewAll, { totalCount : totalCount });
          }
    });   
    return ColleagueGridRenderer;
});
},
'sbt/connections/controls/profiles/ProfilePanel':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.connections.controls.profiles.ProfilePanel
 */
define(["../../../declare", "../../../lang", "../../../config", 
        "../../../connections/ProfileService",
        "../../../controls/panel/_ProfilePanel",
        "../../../text!sbt/connections/controls/profiles/templates/ProfilePanel.html"], 
        function(declare, lang, config, ProfileService, _ProfilePanel, PanelTmpl) {

    var basicPeopleMe = "/connections/opensocial/basic/rest/people/@me/";
    var oauthPeopleMe = "/connections/opensocial/oauth/rest/people/@me/";
	
    /**
     * @module sbt.connections.controls.profiles.ProfilePanel
     */
    var ProfilePanel = declare([ _ProfilePanel ], {
    	
    	template: PanelTmpl,
    	profileService: null,
                
        constructor: function(args) {
        },
        
        getMyProfile: function() {
            var endpoint = this._getEndpoint();
            var path = basicPeopleMe;
            if (endpoint.authType == 'oauth') {
                path = oauthPeopleMe;
            }
            
            var self = this;
            endpoint.request(path, { handleAs : "json", preventCache : true }).then(
                function(response) {
                    var userid = response.entry.id.replace('urn:lsid:lconn.ibm.com:profiles.person:', '');
                    self.getProfile(userid);
                },
                function(error) {
                    self._displayError(error);
                }
            );
        },
        
        getProfile: function(id) {
        	var self = this;
            var promise = this._getProfileService().getProfile(id);
            promise.then(    
                function(profile) {
                	self.profile = profile;
                	self.showProfile();
                },
                function(error) {            	
                    self.profile = null;
                    self._displayError(error);
                }
            );
        },
        
        // Internals
        
        _getProfileService: function() {
        	if (!this.profileService) {
                this.profileService = new ProfileService(this._getEndpointName());
        	}
        	return this.profileService;
        },
        
        _getEndpoint: function() {
        	return config.findEndpoint(this._getEndpointName());
        },
        
        _getEndpointName: function() {
        	return this.endpoint || "connections";
        }        
                
    });
    
    return ProfilePanel;
});
},
'sbt/connections/ProfileService':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * JavaScript API for IBM Connections Profile Service.
 * 
 * @module sbt.connections.ProfileService
 */
define([ "../declare", "../lang", "../config", "../stringUtil", "./ProfileConstants", "../base/BaseService", "../base/BaseEntity", "../base/AtomEntity", "../base/XmlDataHandler", "../base/VCardDataHandler", "../Cache", "../util"  ], function(
        declare,lang,config,stringUtil,consts,BaseService,BaseEntity,AtomEntity,XmlDataHandler, VCardDataHandler, Cache, util) {

	var updateProfileXmlTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns:app=\"http://www.w3.org/2007/app\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:fh=\"http://purl.org/syndication/history/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns=\"http://www.w3.org/2005/Atom\"><category term=\"profile\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category><content type=\"text\">\nBEGIN:VCARD\nVERSION:2.1\n${jobTitle}${address}${telephoneNumber}${building}${floor}END:VCARD\n</content></entry>";
    var updateProfileAttributeTemplate = "${attributeName}:${attributeValue}\n";
    var updateProfileAddressTemplate = "ADR;WORK:;;${streetAddress},${extendedAddress};${locality};${region};${postalCode};${countryName}\n";
    var createProfileTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns:app=\"http://www.w3.org/2007/app\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:fh=\"http://purl.org/syndication/history/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns=\"http://www.w3.org/2005/Atom\"><category term=\"profile\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category><content type=\"application/xml\"><person xmlns=\"http://ns.opensocial.org/2008/opensocial\"><com.ibm.snx_profiles.attrib>${guid}${email}${uid}${distinguishedName}${displayName}${givenNames}${surname}${userState}</com.ibm.snx_profiles.attrib></person></content></entry>";
    var createProfileAttributeTemplate = "<entry><key>${attributeName}</key><value><type>text</type><data>${attributeValue}</data></value></entry>";
    
    var CategoryConnection = "<category term=\"connection\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" />";
    var CategoryConnectionType = "<category term=\"${getConnectionType}\" scheme=\"http://www.ibm.com/xmlns/prod/sn/connection/type\" />";
    var CategoryStatus = "<category term=\"${getStatus}\" scheme=\"http://www.ibm.com/xmlns/prod/sn/status\" />";
   
    var OAuthString = "/oauth";
    var basicAuthString = "";
    var defaultAuthString = "";
    
    /**
     * Profile class.
     * 
     * @class Profile
     * @namespace sbt.connections
     */
    var Profile = declare(BaseEntity, {

        /**
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {            
        },

        /**
         * Get id of the profile
         * 
         * @method getUserid
         * @return {String} id of the profile
         * 
         */
        getUserid : function() {
            return this.getAsString("userid");
        },

        /**
         * Get name of the profile
         * 
         * @method getName
         * @return {String} name of the profile
         * 
         */
        getName : function() {
            return this.getAsString("name");
        },

        /**
         * Get email of the profile
         * 
         * @method getEmail
         * @return {String} email of the profile
         */
        getEmail : function() {
            return this.getAsString("email");
        },

        /**
         * Get groupware mail of the profile
         * 
         * @method getGroupwareMail
         * @return {String} groupware mail of the profile
         */
        getGroupwareMail : function() {
            return this.getAsString("groupwareMail");
        },

        /**
         * Get thumbnail URL of the profile
         * 
         * @method getThumbnailUrl
         * @return {String} thumbnail URL of the profile
         */
        getThumbnailUrl : function() {
            return this.getAsString("photoUrl");
        },

        /**
         * Get job title of the profile
         * 
         * @method getJobTitle
         * @return {String} job title of the profile
         */
        getJobTitle : function() {
            return this.getAsString("jobTitle");
        },

        /**
         * Get department of the profile
         * 
         * @method getDepartment
         * @return {String} department of the profile
         */
        getDepartment : function() {
            return this.getAsString("organizationUnit");
        },

        /**
         * Get address of the profile
         * 
         * @method getAddress
         * @return {Object} Address object of the profile
         */
        getAddress : function() {
            return this.getAsObject(consts.AddressFields);
        },
        /**
         * Get telephone number of the profile
         * 
         * @method getTelephoneNumber
         * @return {String} Phone number of the profile
         */
        getTelephoneNumber : function() {
            return this.getAsString("telephoneNumber");
        },

        /**
         * Get profile URL of the profile
         * 
         * @method getProfileUrl
         * @return {String} profile URL of the profile
         */
        getProfileUrl : function() {
            return this.getAsString("fnUrl");
        },
        /**
         * Get building name of the profile
         * 
         * @method getBuilding
         * @return {String} building name of the profile
         */
        getBuilding : function() {
            return this.getAsString("building");
        },
        /**
         * Get floor address of the profile
         * 
         * @method getFloor
         * @return {String} floor address of the profile
         */
        getFloor : function() {
            return this.getAsString("floor");
        },

        /**
         * Get Pronunciation URL of the profile
         * 
         * @method getPronunciationUrl
         * @return {String} Pronunciation URL of the profile
         */
        getPronunciationUrl : function() {
            return this.getAsString("soundUrl");
        },

        /**
         * Get summary of the profile
         * 
         * @method getSummary
         * @return {String} description of the profile
         */
        getSummary : function() {
            return this.getAsString("summary");
        },

        /**
         * Set work phone number of the profile in the field object
         * 
         * @method setTelephoneNumber
         * @param {String} telephoneNumber work phone number of the profile
         */
        setTelephoneNumber : function(telephoneNumber) {
            this.setAsString("telephoneNumber", telephoneNumber);
        },
        
        /**
         * Set building of the profile in the field object
         * 
         * @method setBuilding
         * @param {String} building building name of the profile
         */
        setBuilding : function(building) {
            this.setAsString("building", building);
        },
        
        /**
         * Set floor number of the profile in the field object
         * 
         * @method setFloor
         * @param {String} floor floor number of the profile
         */
        setFloor : function(floor) {
            this.setAsString("floor", floor);
        },

        /**
         * Set job title of the profile in the field object
         * 
         * @method setJobTitle
         * @param {String} title job title of the profile
         */
        setJobTitle : function(title) {
            this.setAsString("jobTitle", title);
        },

        /**
         * Set the location of the file input element in the markup for editing
         * profile photo in the field object
         * 
         * @method setPhotoLocation
         * @param {String} imgLoc location of the file input element
         */
        setPhotoLocation : function(imgLoc) {
            this.setAsString("imageLocation", imgLoc);
        },

        /**
         * Set the address of the profile in the field object
         * 
         * @method setAddress
         * @param {Object} address Address object of the profile.
         */
        setAddress : function(address) {
            this.setAsObject(address);
        },

        /**
         * Loads the profile object with the profile entry document associated
         * with the profile. By default, a network call is made to load the
         * profile entry document in the profile object.
         * 
         * @method load
         * @param {Object} [args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         * 
         */
        load : function(args) {        	
            var profileId = this.getUserid() || this.getEmail();
            var promise = this.service._validateProfileId(profileId);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.dataHandler = new XmlDataHandler({
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.ProfileXPath
                    });
                    self.id = self.dataHandler.getEntityId();
                    return self;
                }
            };
            var requestArgs = {};
            if (this.service.isEmail(profileId)) {
            	requestArgs.email = profileId;
            } else {
            	requestArgs.userid = profileId;
            }            	
            lang.mixin(requestArgs, args || {});            
            var options = {
                handleAs : "text",
                query : requestArgs
            };
            
            var url = this.service.constructUrl(consts.AtomProfileDo, {}, {authType : this.service._getProfileAuthString()});
            return this.service.getEntity(url, options, profileId, callbacks, args);
        },

        /**
         * Updates the profile of a user.
         * 
         * @method update
         * @param {Object} [args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        update : function(args) {
        	return this.service.updateProfile(this, args);
        },
        
        /**
         * Get colleagues of the profile.
         * 
         * @method getColleagues
         * @param {Object} [args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getColleagues : function(args){
        	return this.service.getColleagues(this, args);
        },
        /**
         * Get colleague connections of the profile.
         * 
         * @method getColleagueConnections
         * @param {Object} [args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getColleagueConnections : function(args){
        	return this.service.getColleagueConnections(this, args);
        }
    });
    
    /**
     * ColleagueConnection class.
     * 
     * @class ConnectionEntry
     * @namespace sbt.connections
     */
    var ColleagueConnection = declare(BaseEntity, {

        /**
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {            
        },

        /**
         * Get id of the profile
         * 
         * @method getAuthorUserId
         * @return {String} author id of the profile
         * 
         */
        getAuthorUserId : function() {
            return this.getAsString("authorUserid");
        },
        
        /**
         * Get id of the profile
         * 
         * @method getContributorUserId
         * @return {String} contributor id of the profile
         * 
         */
        getContributorUserId : function() {
            return this.getAsString("contributorUserid");
        },

        /**
         * Get name of the profile
         * 
         * @method getAuthorName
         * @return {String} author name of the profile
         * 
         */
        getAuthorName : function() {
            return this.getAsString("authorName");
        },
        
        /**
         * Get name of the profile
         * 
         * @method getAuthorName
         * @return {String} contributor name of the profile
         * 
         */
        getContributorName : function() {
            return this.getAsString("contributorName");
        },

        /**
         * Get email of the profile
         * 
         * @method getAuthorEmail
         * @return {String} contributor email of the profile
         */
        getAuthorEmail : function() {
            return this.getAsString("authorEmail");
        },
        
        /**
         * Get email of the profile
         * 
         * @method getContributorEmail
         * @return {String} contributor email of the profile
         */
        getContributorEmail : function() {
            return this.getAsString("contributorEmail");
        },

        /**
         * Get job title of the profile
         * 
         * @method getTitle
         * @return {String} job title of the profile
         */
        getTitle : function() {
            return this.getAsString("title");
        },
        
        /**
         * Get job title of the profile
         * 
         * @method getContent
         * @return {String} content of the profile
         */
        getContent : function() {
            return this.getAsString("content");
        },

        /**
         * Get profile URL of the profile
         * 
         * @method getSelfLink
         * @return {String} profile URL of the profile
         */
        
        getSelfLink : function() {
            return this.getAsString("selfLink");
        },
        
        /**
         * Get profile URL of the profile
         * 
         * @method getEditLink
         * @return {String} profile URL of the profile
         */
        
        getEditLink : function() {
            return this.getAsString("editLink");
        },
        
        /**
         * Get profile URL of the profile
         * 
         * @method getUpdated
         * @return {String} profile URL of the profile
         */
        
        getUpdated : function() {
            return this.getAsString("updated");
        }

    });

    /**
     * ProfileTag class.
     * 
     * @class ProfileTag
     * @namespace sbt.connections
     */
    var ProfileTag = declare(BaseEntity, {

        /**
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {            
        },

        /**
         * Get term of the profile tag
         * 
         * @method getTerm
         * @return {String} term of the profile tag
         * 
         */
        getTerm : function() {
            return this.getAsString("term");
        },
        
        /**
         * Get frequency of the profile tag
         * 
         * @method getFrequency
         * @return {Number} frequency of the profile tag
         * 
         */
        getFrequency : function() {
            return this.getAsNumber("frequency");
        },
        
        /**
         * Get intensity of the profile tag
         * 
         * @method getIntensity
         * @return {Number} intensity of the profile tag
         * 
         */
        getIntensity : function() {
            return this.getAsNumber("intensity");
        },
        
        /**
         * Get visibility of the profile tag
         * 
         * @method getVisibility
         * @return {Number} visibility of the profile tag
         * 
         */
        getVisibility : function() {
            return this.getAsNumber("visibility");
        }
        
    });
    
    /**
     * Invite class.
     * 
     * @class Invite
     * @namespace sbt.connections
     */
    var Invite = declare(AtomEntity, {

    	xpath : consts.InviteXPath,
    	contentType : "html",
    	categoryScheme : CategoryConnection,
    	    	
        /**
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {            
        },
        
        /**
         * Return extra entry data to be included in post data for this entity.
         * 
         * @returns {String}
         */
        createEntryData : function() {
        	var entryData = "";
        	entryData += stringUtil.transform(CategoryConnectionType, this, function(v,k) { return v; }, this);
        	entryData += stringUtil.transform(CategoryStatus, this, function(v,k) { return v; }, this);
            return stringUtil.trim(entryData);
        },
        
        /**
         * Return the connection type associated with this invite. 
         * 
         * @method getConnectionType
         * @return {String} status
         */
        getConnectionType : function() {
        	var connectionType = this.getAsString("connectionType");
        	return connectionType || consts.TypeColleague;
        },
                
        /**
         * Set the connection type associated with this invite. 
         * 
         * @method setConnectionType
         * @param {String} status
         */
        setConnectionType : function(connectionType) {
        	return this.setAsString("connectionType", connectionType);
        },
                
        /**
         * Return the status associated with this invite. 
         * 
         * @method getStatus
         * @return {String} status
         */
        getStatus : function() {
        	var status = this.getAsString("status");
        	return status || consts.StatusPending;
        },
        
        /**
         * Set the status associated with this invite. 
         * 
         * @method setStatus
         * @param {String} status
         */
        setStatus : function(status) {
        	return this.setAsString("status", status);
        },
        
        /**
         * Return the connection id associated with this invite. 
         * 
         * @method getConnectionId
         * @return {String} connectionId
         */
        getConnectionId : function() {
        	return this.getAsString("connectionId");
        },
        
        /**
         * Set connection id associated with this invite. 
         * 
         * @method setConnectionId
         * @param connectionId
         * @return {Invite} 
         */
        setConnectionId : function(connectionId) {
        	return this.setAsString("connectionId", connectionId);
        }
    });
    
    /**
     * Callbacks used when reading an entry that contains a Profile.
     */
    var ProfileCallbacks = {
        createEntity : function(service,data,response) {
            var entryHandler = null;
            if (response.args && response.args.format == "vcard") {
                entryHandler = new VCardDataHandler({
                    data : data,
                    namespaces : consts.Namespaces,
                    xpath : consts.ProfileVCardXPath
                });
            } else {
                entryHandler = new XmlDataHandler({
                    data : data,
                    namespaces : consts.Namespaces,
                    xpath : consts.ProfileXPath
                });
            }
            return new Profile({
                service : service,
                id : entryHandler.getEntityId(),
                dataHandler : entryHandler
            });
        }
    };
    
    /**
     * Callbacks used when reading a feed that contains Profile entries.
     */
    var ProfileFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ProfileFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = null;
            if (response.args && response.args.format == "vcard") {
                entryHandler = new VCardDataHandler({
                    data : data,
                    namespaces : consts.Namespaces,
                    xpath : consts.ProfileVCardXPath
                });
            } else {
                entryHandler = new XmlDataHandler({
                    data : data,
                    namespaces : consts.Namespaces,
                    xpath : consts.ProfileXPath
                });
            }
            return new Profile({
                service : service,
                id : entryHandler.getEntityId(),
                dataHandler : entryHandler
            });
        }
    };
    
    /**
     * Callbacks used when reading a feed that contains ColleagueConnections
     */
    var ColleagueConnectionFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ProfileFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = null;
            entryHandler = new XmlDataHandler({
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ColleagueConnectionXPath
            });
            return new ColleagueConnection({
                service : service,
                id : entryHandler.getEntityId(),
                dataHandler : entryHandler
            });
        }
    };
    
    /**
     * Callbacks used when reading a feed that contains Profile Tag entries.
     */
    var ProfileTagFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ProfileTagsXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ProfileTagsXPath
            });
            return new ProfileTag({
                service : service,
                id : entryHandler.getEntityId(),
                dataHandler : entryHandler
            });
        }
    };
    
    /**
     * ProfileService class.
     * 
     * @class ProfileService
     * @namespace sbt.connections
     */
    var ProfileService = declare(BaseService, {
        
        contextRootMap: {
            profiles: "profiles"
        },

        /**
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        	if (!this.endpoint) {
                this.endpoint = config.findEndpoint(this.getDefaultEndpointName());
            }
        	if(!this._cache){
        		if(config.Properties.ProfileCacheSize || consts.DefaultCacheSize){
        			this._cache = new Cache(config.Properties.ProfileCacheSize || consts.DefaultCacheSize);
        		}        		
        	}            
        },
        
        /**
         * Return the default endpoint name if client did not specify one.
         * @returns {String}
         */
        getDefaultEndpointName: function() {
            return "connections";
        },
        
        /**
        * Create a Profile object with the specified data.
        * 
        * @method newProfile
        * @param {Object} args Object containing the fields for the 
        *            new Profile 
        */
        newProfile : function(args) {
            return this._toProfile(args);
        },

        /**
         * Create a Invite object with the specified data.
         * 
         * @method newInvite
         * @param {Object} args Object containing the fields for the 
         *            new Invite 
         */
         newInvite : function(args) {
             return this._toInvite(args);
         },

        /**
         * Get the profile of a user.
         * 
         * @method getProfile
         * @param {String} userIdOrEmail Userid or email of the profile
         * @param {Object} [args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getProfile : function(userIdOrEmail, args) {           
        	var profile = this._toProfile(userIdOrEmail);
            var promise = this._validateProfile(profile);
            if (promise) {
                return promise;
            }
            return profile.load(args);
        },
        
        /**
         * Update an existing profile
         * 
         * @method updateProfile
         * @param {Object} profileOrJson Profile object to be updated
         * @param {Object} [args] Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        updateProfile : function(profileOrJson,args) {
            var profile = this._toProfile(profileOrJson);
            var promise = this._validateProfile(profile);
            if (promise) {
                return promise;
            }

            var requestArgs = {};
            profile.getUserid() ? requestArgs.userid = profile.getUserid() : requestArgs.email = profile.getEmail();
            lang.mixin(requestArgs, args || {});
            
            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {                
                return profile;              
            };
            
            var options = {
                    method : "PUT",
                    query : requestArgs,
                    headers : consts.AtomXmlHeaders,
                    data : this._constructProfilePostData(profile)
                };   
            var url = this.constructUrl(consts.AtomProfileEntryDo, {}, {authType : this._getProfileAuthString()});

            return this.updateEntity(url, options, callbacks, args);
        },      
        
        /**
         * Get the tags for the specified profile
         * 
         * @method getTags
         * @param {String} id userId/email of the profile
         * @param {Object} args Object representing various query parameters that can be passed. The parameters must 
         * be exactly as they are supported by IBM Connections.
         */
        getTags : function(id, args) {
            // detect a bad request by validating required arguments
            var idObject = this._toTargetObject(id);
            var promise = this._validateTargetObject(idObject);
            if (promise) {
                return promise;
            }
            
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin(idObject, args || {})
            };
            var url = this.constructUrl(consts.AtomTagsDo, {}, {authType : this._getProfileAuthString()});

            return this.getEntities(url, options, this.getProfileTagFeedCallbacks(), args);
        },
        
        /**
         * Get the colleagues for the specified profile
         * 
         * @method getColleagues
         * @param {String} id userId/email of the profile
         * @param {Object} args Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getColleagues : function(id, args) {
            // detect a bad request by validating required arguments
            var idObject = this._toIdObject(id);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin(idObject, {
                connectionType : "colleague",
                outputType : "profile"
            }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            var url = this.constructUrl(consts.AtomConnectionsDo, {}, {authType : this._getProfileAuthString()});
            return this.getEntities(url, options, this.getProfileFeedCallbacks(), args);
        },
        
        /**
         * Get the colleagues for the specified profile as Collegue Connection entries
         * 
         * @method getColleagueConnections
         * @param {String} id userId/email of the profile
         * @param {Object} args Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getColleagueConnections : function(id, args) {
            // detect a bad request by validating required arguments
            var idObject = this._toIdObject(id);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin(idObject, {
                connectionType : "colleague",
				outputType : "connection"
            }, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            var url = this.constructUrl(consts.AtomConnectionsDo, {}, {authType : this._getProfileAuthString()});
            return this.getEntities(url, options, this.getColleagueConnectionFeedCallbacks(), args);
        },
        
        /**
         * Get the reporting chain for the specified person.
         * 
         * @method getReportingChain
         * @param {String} id userId/email of the profile
         * @param {Object} args Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getReportingChain : function(id, args) {
            // detect a bad request by validating required arguments
            var idObject = this._toIdObject(id);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin(idObject, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            var url = this.constructUrl(consts.AtomReportingChainDo, {}, {authType : this._getProfileAuthString()});
            return this.getEntities(url, options, this.getProfileFeedCallbacks(), args);
        },
        
        /**
         * Get the people managed for the specified person.
         * 
         * @method getPeopleManaged
         * @param {String} id userId/email of the profile
         * @param {Object} args Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        getPeopleManaged : function(id, args) {
            // detect a bad request by validating required arguments
            var idObject = this._toIdObject(id);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }
            
            var requestArgs = lang.mixin(idObject, args || {});
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs
            };
            var url = this.constructUrl(consts.AtomPeopleManagedDo, {}, {authType : this._getProfileAuthString()});
            return this.getEntities(url, options, this.getProfileFeedCallbacks(), args);
        },
                
        /**
         * Search for a set of profiles that match a specific criteria and return them in a feed.
         * 
         * @method search
         * @param {Object} args Object representing various query parameters
         *            that can be passed. The parameters must be exactly as they are
         *            supported by IBM Connections.
         */
        search : function(args) {
            // detect a bad request by validating required arguments
            if (!args) {
            	return this.createBadRequestPromise("Invalid arguments, one or more of the input parameters to narrow the search must be specified.");
            }
            
            var options = {
                method : "GET",
                handleAs : "text",
                query : args
            };
            var url = this.constructUrl(consts.AtomSearchDo, {}, {authType : this._getProfileAuthString()});
            return this.getEntities(url, options, this.getProfileFeedCallbacks(), args);
        },
        
        /**
		 * Updates the profile photo of a user.
		 * @method updateProfilePhoto
		 * @param {Object} fileControlOrId The Id of html control or the html control
		 * @param @param {String} id userId/email of the profile 
		 * @param {Object} [args] The additional parameters
		 */
		updateProfilePhoto: function (fileControlOrId, id, args) {
			var promise = this.validateField("File Control Or Id", fileControlOrId);
			if (promise) {
				return promise;
			}
			promose = this.validateHTML5FileSupport();
			if(promise){
				return promise;
			}			
			
			var idObject = this._toIdObject(id);
			var files = null;
			if (typeof fileControlOrId == "string") {
				var fileControl = document.getElementById(fileControlOrId);
				filePath = fileControl.value;
				files = fileControl.files;
			} else if (typeof fileControlOrId == "object") {
				filePath = fileControlOrId.value;
				files = fileControlOrId.files;
			} else {
				return this.createBadRequestPromise("File Control or ID is required");
			}

			if(files.length != 1){
				return this.createBadRequestPromise("Only one file needs to be provided to this API");
			}
			
			var file = files[0];
			var formData = new FormData();
			formData.append("file", file);
			var requestArgs = lang.mixin(idObject, args || {});		
			var url = this.constructUrl(config.Properties.serviceUrl + "/files/" + this.endpoint.proxyPath + "/" + "connections" + "/" +  "UpdateProfilePhoto" + "/" + encodeURIComponent(file.name),
					args && args.parameters ? args.parameters : {});
			var headers = {
				"Content-Type" : false,
				"Process-Data" : false // processData = false is reaquired by jquery 
			};			
			var options = {
				method : "PUT",
				headers : headers,
				query : requestArgs || {},
				data : formData
			};
			var callbacks = {
					createEntity : function(service, data, response) {
						return data; // Since this API does not return any response in case of success, returning empty data
					}
			};

			return this.updateEntity(url, options, callbacks);			
		},
        
        /**
         * Invite a person to become your colleague.
         * 
         * @method inviteColleague
         * @param id
         * @param inviteOrJson
         * @param args
         */
		createInvite : function(id, inviteOrJson, args) {
            // detect a bad request by validating required arguments
            var idObject = this._toIdObject(id);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }
            
            var invite = this._toInvite(inviteOrJson);

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
                invite.setData(data);
                var connectionId = this.getLocationParameter(response, "connectionId");
                invite.setConnectionId(connectionId);
                return invite;
            };

            var requestArgs = lang.mixin(idObject, args || {});
            var options = {
                method : "POST",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : invite.createPostData()
            };
            
            var url = this.constructUrl(consts.AtomConnectionsDo, {}, {authType : this._getProfileAuthString()});
            return this.updateEntity(url, options, callbacks, args);
        },

        //
        // Internals
        //
        
        /*
         * Return callbacks for a profile feed
         */
        getProfileFeedCallbacks : function() {
            return ProfileFeedCallbacks;
        },
        
        /*
         * Return callbacks for a ColleagueConnection feed
         */
        getColleagueConnectionFeedCallbacks : function() {
            return ColleagueConnectionFeedCallbacks;
        },

        /*
         * Return callbacks for a profile entry
         */
        getProfileCallbacks : function() {
            return ProfileCallbacks;
        },

        /*
         * Return callbacks for a profile tag feed
         */
        getProfileTagFeedCallbacks : function() {
            return ProfileTagFeedCallbacks;
        },
        
        /*
         * Convert profile or key to id object
         */
        _toIdObject : function(profileOrId) {
            var idObject = {};
            if (lang.isString(profileOrId)) {
                var userIdOrEmail = profileOrId;
                if (this.isEmail(userIdOrEmail)) {
                    idObject.email = userIdOrEmail;
                } else {
                    idObject.userid = userIdOrEmail;
                }
            } else if (profileOrId instanceof Profile) {
                if (profileOrId.getUserid()) {
                    idObject.userid = profileOrId.getUserid();
                }
                else if (profileOrId.getEmail()) {
                    idObject.email = profileOrId.getEmail();
                }
            }
            return idObject;
        },
        
        /*
         * Convert profile or key to target object
         */
        _toTargetObject : function(profileOrId) {
            var targetObject = {};
            if (lang.isString(profileOrId)) {
                var userIdOrEmail = profileOrId;
                if (this.isEmail(userIdOrEmail)) {
                	targetObject.targetEmail = userIdOrEmail;
                } else {
                	targetObject.targetKey = userIdOrEmail;
                }
            } else if (profileOrId instanceof Profile) {
                if (profileOrId.getUserid()) {
                	targetObject.targetKey = profileOrId.getUserid();
                }
                else if (profileOrId.getEmail()) {
                	targetObject.targetEmail = profileOrId.getEmail();
                }
            }
            return targetObject;
        },
        
        /*
         * Validate an ID object
         */
        _validateIdObject : function(idObject) {
            if (!idObject.userid && !idObject.email) {
                return this.createBadRequestPromise("Invalid argument, userid or email must be specified.");
            }
        },
        
        /*
         * Validate an Target object
         */
        _validateTargetObject : function(idObject) {
            if (!idObject.targetKey && !idObject.targetEmail) {
                return this.createBadRequestPromise("Invalid argument, userid or email must be specified.");
            }
        },
        
        /*
         * Return a Profile instance from Profile or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toProfile : function(profileOrJsonOrString,args) {
            if (profileOrJsonOrString instanceof Profile) {
                return profileOrJsonOrString;
            } else {
            	var profileJson = profileOrJsonOrString;
            	if (lang.isString(profileOrJsonOrString)) {
            		profileJson = {};
            		if(this.isEmail(profileOrJsonOrString)){
            			profileJson.email = profileOrJsonOrString;
            		}else{
            			profileJson.userid = profileOrJsonOrString;
            		}
                }else{ // handle the case when the profileJson has id attribute. id can take either userid or email.
                	if(profileJson && profileJson.id && !profileJson.userid && !profileJson.email){
                		this.isEmail(profileJson.id) ? profileJson.email = profileJson.id : profileJson.userid = profileJson.id;
                		delete profileJson.id;
                	}
                }
                return new Profile({
                    service : this,
                    _fields : lang.mixin({}, profileJson)
                });
            }
        },
        
        /*
         * Return a Invite instance from Invite or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toInvite : function(inviteOrJsonOrString,args) {
            if (inviteOrJsonOrString instanceof Invite) {
                return inviteOrJsonOrString;
            } else {
            	if (lang.isString(inviteOrJsonOrString)) {
            		inviteOrJsonOrString = {
            			content : inviteOrJsonOrString
            		};
                } 
                return new Invite({
                    service : this,
                    _fields : lang.mixin({}, inviteOrJsonOrString)
                });
            }
        },
        
        /*
         * Returns true if an address field has been set.
         */
        _isAddressSet : function(profile){
        	return (profile._fields["streetAddress"] || profile._fields["extendedAddress"] || profile._fields["locality"] || profile._fields["region"] || profile._fields["postalCode"] || profile._fields["countryName"]);
        },
        
        /*
         * Constructs update profile request body.
         */
        _constructProfilePostData : function(profile) {
            var transformer = function(value,key) {
                if (key == "address") {                	
                	value = profile.service._isAddressSet(profile) ? stringUtil.transform(updateProfileAddressTemplate, {"streetAddress" : profile._fields["streetAddress"], 
                	"extendedAddress" : profile._fields["extendedAddress"], "locality" : profile._fields["locality"], "region" : profile._fields["region"],
                	"postalCode" : profile._fields["postalCode"], "countryName" : profile._fields["countryName"]}) : null;
                } 
                else{                	
                	value = (profile._fields[key])? stringUtil.transform(updateProfileAttributeTemplate, {"attributeName" : consts.ProfileVCardXPath[key], "attributeValue" : profile._fields[key]}) : null;
                	
                }
                return value;
            };
            return stringUtil.transform(updateProfileXmlTemplate, profile, transformer, profile);
        },
        
        /*
         * Constructs update profile request body.
         */
        _constructProfilePutData : function(profile) {
            var transformer = function(value,key) {
            	if(profile._fields[key]){
	                value = stringUtil.transform(createProfileAttributeTemplate, {"attributeName" : consts.profileCreateAttributes[key], "attributeValue" : profile._fields[key]});
	                return value;
            	}
            };
            return stringUtil.transform(createProfileTemplate, profile, transformer, profile);
        },

        /*
         * Validate a Profile object
         */
        _validateProfile : function(profile) {
            if (!profile || (!profile.getUserid() && !profile.getEmail())) {
                return this.createBadRequestPromise("Invalid argument, profile with valid userid or email must be specified.");
            }            
        },
        
        /*
         * Validate a Profile id
         */
        _validateProfileId : function(profileId) {
            if (!profileId || profileId.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected userid or email");
            }
        },
        
        _getProfileAuthString : function(){
        	if (this.endpoint.authType == consts.AuthTypes.Basic) {
        		return basicAuthString;
        	} else if (this.endpoint.authType == consts.AuthTypes.OAuth) {
        		return OAuthString;
        	} else {
        		return defaultAuthString;
        	}
        }

    });
    return ProfileService;
});

},
'sbt/base/VCardDataHandler':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * JavaScript API for IBM Connections Profile Service.
 * 
 * @module sbt.connections.ProfileService
 */
define([ "../declare", "../lang", "../config", "../stringUtil", "./XmlDataHandler" ], 
    function(declare,lang,config,stringUtil,XmlDataHandler) {

    /**
     * VCardDataHandler class.
     * 
     * @class ProfileDataHandler
     * @namespace sbt.connections
     */
    var VCardDataHandler = declare(XmlDataHandler, {
        
        lineDelim : "\n",
        itemDelim : ":",
        
        _vcard : null,
        
        /**
         * @constructor
         * @param {Object}
         *            args Arguments for this data handler.
         */
        constructor : function(args) {
            this.parseVCard();
        },
        
        /**
         * Parse the vcard data from the specified element.
         * 
         * @method parseVCard 
         */
        parseVCard : function() {
            var content = stringUtil.trim(this.getAsString("vcard"));
            var lines = content.split(this.lineDelim);
            this._vcard = {};
            for (var i=1; i<lines.length-1; i++) {
                var line = stringUtil.trim(lines[i]);
                var index = line.indexOf(this.itemDelim);
                var key = line.substring(0, index);
                var value = line.substring(index+1);
                this._vcard[key] = value;
            }
        },
        
        /*
         * Override this method to handle VCard properties.
         */
        _selectText : function(property) {
            var xpath = this._getXPath(property);
            if (this._vcard && this._vcard.hasOwnProperty(xpath)) {
                return this._vcard[xpath];
            } else {
                try {
                    return this.inherited(arguments, [ property ]);
                } catch (error) {
                    // vcard expressions may cause an error
                    // if they are treated as xpath expressions
                    return null;
                }
            }
        }
        
    });
    return VCardDataHandler;
});
},
'sbt/connections/controls/profiles/ProfileTagAction':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../../declare", "../../../controls/grid/GridAction" ], 
        function(declare, GridAction) {

    /**
     * @class ProfileTagAction
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ProfileTagAction
     */
    var ProfileTagAction = declare(GridAction, {
        
    	/**Strings used in the actions */
        nls: {
            tooltip: ""
        },
        
        /**ProfileTagAction Constructor function
         * @method constructor
         * */
        constructor: function() {
        },
        
        /**
         * Handles displaying a tooltip for an item
         * For profiles, the tooltip by default will be a business card
         * So nothing is done in this function
         * @method getTooltip
         * @param item The element that will use the tooltip
         */
        getTooltip: function(item) {
        	return item.getTerm();
        },
        
        /**
         * The execute function is called from the handle click function
         * For Profiles by default the business card functionality is used
         * which works from the Semantic tag service so nothing is done here.
         * @method execute
         * @param item The item which fired the event
         * @param opts
         * @param event The event
         */
        execute: function(item, opts, event) {
        }

    });

    return ProfileTagAction;
});
},
'sbt/connections/controls/profiles/ProfileTagsGrid':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.connections.controls.profiles.ProfileTagsGrid
 */
define([ "../../../declare", 
         "../../../lang",
		 "../../../config",
		 "../../../controls/grid/Grid", 
		 "./ProfileTagsGridRenderer", 
		 "./ProfileTagAction", 
		 "../../../store/parameter",
		 "../../../connections/ProfileConstants"], 
        function(declare, lang, sbt, Grid, ProfileTagsGridRenderer, ProfileTagAction, parameter, consts) {

    /**
     * @class ProfileTagsGrid
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ProfileTagsGrid
     */
    var ProfileTagsGrid = declare(Grid, {
    	
    	/**
    	 * @param lite Specifies how much tag information you want to retrieve. The options are lite or full.
    	 */
    	format : "full",
    	
    	/**
    	 * @param options, This is a list of all
    	 * the different types of profile tags grids available.
    	 * Depending on which one is selected specific arguments will be given to
    	 * the atom store and grid renderer.
    	 */
        options : {
            "list" : {
                storeArgs : {
                    url : consts.AtomTagsDo,
                    feedXPath : consts.ProfileTagsXPath,
                    attributes : consts.ProfileTagsXPath,
                    namespaces : consts.Namespaces
                },
                rendererArgs : {
                    type : "list"
                }
            }
        },
        
        /**
         * A profile tag action, defines default behaviour for when 
         * items in the grid are clicked on or hovered on,
         * it is possible to override these actions
         */
        profileTagAction: new ProfileTagAction(),
        
        /**
         * This is the default grid that will be created if no 
         * arguments are given.
         */
        defaultOption: "list",
        
        /**Constructor function
         * @method constructor
         * */
        constructor: function(args) {
        },
        
        contextRootMap: {
            profiles: "profiles"
        },
        
        /**
         * Override buildUrl to add format, target and source
         * 
         * @method buildUrl
         * @param url base url
         * @param args arguments that will be passed to the store
         * @param endpoint An endpoint which may contain custom service mappings.
         * @returns Built url
         */
        buildUrl: function(url, args, endpoint) {
            var params = { format : this.format };
            
            if (this.query) {
            	params = lang.mixin(params, this.query);
            }
            if (this.targetEmail) {
            	params = lang.mixin(params, { targetEmail : this.targetEmail });
            } 
            if (this.targetKey) {
            	params = lang.mixin(params, { targetKey : this.targetKey });
            } 
            if (this.sourceEmail) {
            	params = lang.mixin(params, { sourceEmail : this.sourceEmail });
            } 
            if (this.sourceKey) {
            	params = lang.mixin(params, { sourceKey : this.sourceKey });
            } 

            return this.constructUrl(url, params, this.getUrlParams(), endpoint);
        },
        
        /**
         * The post create function is called, after the grid has been created.
         * The function will call the super classes post create
         * then load the semantic tag service. The semantic tag service
         * is Javascript for creating business card functionality.
         * @method postCreate
         */
        postCreate: function() {        	
        	this.inherited(arguments);
        },
        
        /**
         * Creates a renderer for the grid.The renderer is responsible for 
         * loading the grid's HTML content.
         * @method createDefaultRenderer
         * @param args sets the template the renderer will use, by checking args.type, but for
         * profile grids this will always be "profile"
         * @returns an instance of a  profile gird renderer.
         */
        createDefaultRenderer : function(args) {
            return new ProfileTagsGridRenderer(args);
        },
        
        /**
         * In the grid HTML an element can have an event attached 
         * using dojo-attach-event="onClick: handleClick".
         * This method is the handler for the onclick event.
         * @method handleClick
         * @param el the element that fired the event
         * @param data all of the items from the current row of the grid. 
         * @param ev the event 
         */
        handleClick: function(el, data, ev) {
            if (this.profileTagAction) {
            	console.log(data);
                this._stopEvent(ev);
                
                this.profileTagAction.execute(data, { grid : this.grid }, ev);
            }
        }
        
        // Internals
        
    });

    return ProfileTagsGrid;
});
},
'sbt/connections/controls/profiles/ProfileTagsGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../../declare", "../../../stringUtil", "../../../xpath", "../../../xml",
        "../ConnectionsGridRenderer",
        "../../../i18n!sbt/connections/controls/profiles/nls/ProfileTagsGridRenderer",
        "../../../text!sbt/connections/controls/profiles/templates/TagListRow.html",
        "../../../text!sbt/connections/controls/profiles/templates/TagListHeader.html"],
        function(declare, stringUtil, xpath, xml, ConnectionsGridRenderer, nls, tagListTmpl, tagListHdrTmpl) {
		
    /**
     * @class ProfileTagsGridRenderer
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ProfileTagsGridRenderer
     */
    var ProfileTagsGridRenderer = declare(ConnectionsGridRenderer, {

          /**
           * The strings used in the grid, these are stored in a separate file, in the nls folder
           */
         _nls: nls,
         
         containerClass: "lotusChunk",
         tagListClass: "lotusList lotusEditable lotusTags lotusMeta",
         
         _numberOfContributors: 0,
        
         /**
          * The constructor function
          * @method constructor
          * @param args
          */
         constructor: function(args) {
        	 if (args.type == "list") {
                 this.template = tagListTmpl;
                 this.listHeaderTemplate = tagListHdrTmpl;
             }
         },
         
         render: function(grid, el, items, data) {
             while (el.childNodes[0]) {
                 this._destroy(el.childNodes[0]);
             }
             var size = items.length;
             if (size === 0) {
                this.renderEmpty(grid, el);
             }
             else {
            	 this._computeRenderValues(grid, el, items, data);
            	 
            	 //this.renderTagHeader(grid, el, items, data);
            	 //this.renderInputForm(grid, el, items, data);
            	 this.renderTagListHeader(grid, el, items, data);
            	 var container = this.renderContainer(grid, el, items, data);
            	 for (var i=0; i<items.length; i++) {
                    this.renderItem(grid, container, data, items[i], i, items);
            	 }
             }
          },
          
          renderTagListHeader: function(grid, el, items, data) {   
        	  if (this.listHeaderTemplate && !grid.hideListHeader) {
                  var node;
                  if (this._isString(this.listHeaderTemplate)) {
                      var domStr = this._substituteItems(this.listHeaderTemplate, grid, this, items, data);
                      node = this._toDom(domStr, el.ownerDocument);
                  } else {
                      node = this.listHeaderTemplate.cloneNode(true);
                  }
                  el.appendChild(node);
                  
                  this._doAttachEvents(grid, el, data);
              }
          },

          renderContainer: function(grid, el, items, data) {          
              var div = this._create("div", { "class": this.containerClass }, el);
              return this._create("ul", { "class": this.tagListClass }, div);
          },

          /**
           * Return the tag header label.
           */
          tagHeader: function(grid, renderer, items, data) {
        	  if (grid.sourceEmail || grid.sourceKey) {
        		  var params = { 
        			  tagSource : grid.sourceName || grid.sourceEmail || grid.sourceKey,
        			  tagTarget : grid.targetName || grid.targetEmail || grid.targetKey
        		  };
        		  return stringUtil.replace(nls.taggedBy, params);
        	  } else {
        		  var str = (this._numberOfContributors == 1) ? nls.taggedByPerson : nls.taggedByPeople;
        		  return stringUtil.replace(str, { numberOfContributors : this._numberOfContributors });
        	  }
          },
          
          //Internals
          
          _computeRenderValues: function(grid, el, items, data) {
        	  var document = xml.parse(data.response);
        	  this._numberOfContributors = parseInt(xpath.selectText(document, 
        			  grid._storeArgs.feedXPath.numberOfContributors, 
        			  grid._storeArgs.namespaces));
          }

    });
    
    return ProfileTagsGridRenderer;
});
},
'sbt/connections/controls/search/SearchBox':function(){
/*
 * � Copyright IBM Corp. 2013
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

define(["../../../declare", "../../../lang", "../../../dom", "../../../widget/_TemplatedWidget", "./SearchBoxRenderer",
        "../../SearchService", "../../CommunityService"], 
		function(declare, lang, dom, _TemplatedWidget, SearchBoxRenderer,SearchService,CommunityService){
	/**
	 * @class SearchBox
	 * @namespace sbt.connections.controls.search
	 * @module sbt.connections.controls.search.SearchBox
	 */
	var SearchBox = declare(_TemplatedWidget,{
		
		// TODO why is type not listed here? what does that parameter mean?
		
		/** 
		 * TODO The "Loading" string needs to be externalied into nls. This should be in the renderer?
		 * Loading template 
		 */
		templateString: "<div><strong> Loading... </strong></div>",
        
		/**
		 * The Renderer For this control 
		 */
		renderer: null,
        
		/** 
		 * The Application the user will select for example files, wikis etc, initially set to all 
		 */
        selectedApplication: "allconnections",       	
        
        /** 
         * TODO should this be public?
         * The phrase the user will search for.
         */
        searchQuery: "",
        
        /** 
         * Search type, valid values are 'my' || 'public' (defaut value is 'my')
         */
        searchType: "my",

        /** 
         * Search suffix, will be appended to every search query 
         */
        searchSuffix: null,
        
        /**
         * Search arguments, will be added to each search request
         */
        searchArgs: null,

        /** 
         * Search Service, used to perform the search 
         */
        searchService: null,
        
        /**
         * Community service to perform searches on my Communities
         */
        communityService: null,

		/** 
		 * 	TODO  Better pattern is to set is during postMixInProperties 
		 * This list is used to keep track of selected members 
		 */
		members: [],
		
		/**
		 * 	TODO  Better pattern is to set is during postMixInProperties 
		 * The result the user has chosen from the search suggestions 
		 */
		_selectedResultItem : {text:"",id:""},
		
		/*
		 * Selected row is used for keyboard navigation in the applications pop up
		 */
		_selectedRow : -1,
		
		/*
		 * 
		 */
		_searchInput: null,
		
        /**
         * @method constructor The constructor for the SearchBox class
         * @param args
         */
		constructor: function(args){
			lang.mixin(this, args);
			
		},

		/**
		 * TODO Document Me 
		 */
		setInputValue: function(value){
			this._searchInput.value = value;
		},
		
		/**
		 * Invoked before rendering occurs, and before any dom nodes are created.
		 * This is the place to change the widget properties before it is rendered.
		 */
		postMixInProperties : function() {
		},
		
		/**
		 * TODO Document Me 
		 */
		getInputValue: function(){
			return this._searchInput.value;
		},
		
		/**
		 * Set the disabled state of the input control for the SearchBox.
		 * 
		 * @param disabled True to disable the input control
		 * @return this
		 */
		setInputDisabled: function(disabled) {
			this._searchInput.disabled = disabled;
			return this;
		},
		
		/**
		 * Return the disabled state of the input control for the SearchBox.
		 * 
		 * @return True is the input control is disabled and otherwise false
		 */
		isInputDisabled: function(){
			return this._searchInput.disabled;
		},
		
		/**
		 * TODO Document Me 
		 */
		getSelectedResult: function(){
			return this._selectedResultItem;
		},
		
		/**
		 * TODO is this needed
		 * TODO Document Me 
		 */
		setSelectedResult: function(name,id){
			if(name){
				this._selectedResultItem.name = name;
			}
			if(id){
				this._selectedResultItem.id = id;
			}
		},
		 
		/**
		 * Function is called after this class has been constructed
		 * the functions in the post create need to be called after the class has been
		 * instantiated so parameters in the dijit base classes can be initialised. 
		 * @method postCreate 
		 * @param args
		 */
		postCreate: function(args){
			this.createDefaultRenderer(args);
			this.domNode = this.renderer.getDomNode(this);
			this.renderer.render(this,this.domNode,{});
			
			if (this.memberList) {
				// Create member list
				this.renderer.renderMemberList(this.domNode);
			}
		},
		
		/**
		 * Creates a SearchBoxRenderer and sets it as the renderer for this class.
		 * @method CreateDefaultRenderer
		 * @param args
		 */
		createDefaultRenderer: function(args){
			this.renderer = new SearchBoxRenderer(args);
		},
		
		/** 
		 * When the user clicks the apps lists, this function renderers a pop up
		 * of all the different applications they can select from
		 * @method handleClick
		 */
		handleClick: function(){
			this.searchBoxAction.renderPopUp(this);
		},
		
		/**
		 * event handler for blur events
		 * @method handleBlur
		 * @param element the element that fired the event
		 * @param obj
		 * @param event the blur event
		 */
		handleBlur: function(element,obj,event){
			if(!event){
				event = window.event;
			}
			// TODO why two seperate calls to removePopUp, logic looks overly complex here
			//For Keyboard Accessibility, this only needs to work for firefox(accessible path) if other browsers 
			//do not support this property that is okay
			if(event.explicitOriginalTarget){
				if(event.explicitOriginalTarget.nodeName == "TD" || event.explicitOriginalTarget.nodeName == "TR" || event.explicitOriginalTarget.nodeName == "#text"){
					
				}else{
					this.searchBoxAction.removePopUp(this);
				}
			}else{
				this.searchBoxAction.removePopUp(this);
			}	
		},
		
		/**
		 * When a user selects an application from the apps pop up
		 * there choice is stored, and the pop up is removed. 
		 * @method setSelectedApplication
		 * @param element  The pop up HTML element
		 * @param object 
		 * @param event  The Event 
		 */
		setSelectedApplication: function(element,object,event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.setSelectedApplication(element,object,event,this);
		},

		/**
		 * When the user hovers over an application in the applications pop up, the background gets highlighted
		 * @method displayHighlight
		 * @param element The pop up HTML element
		 * @param object
		 * @param event The event
		 */
		displayHighlight: function(element,object,event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.highLight(element,object,event);
		},
		
		/**
		 * Closes a member item (by removing it from its parent; the member list)
		 * @method closeMemberItem
		 * @param element  The member list item
		 * @param object 
		 * @param event  The Event 
		 */
		closeMemberItem: function(element,object,event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.closeMemberItem(this, element,object,event);
		},
		
		/**
		 * When the user moves the mouse away from the application item in the pop up
		 * the background highlight it removed.
		 * @method removeHighlight
		 * @param element The pop up HTML element
		 * @param object
		 * @param event The Event
		 */
		removeHighlight: function(element,object,event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.removeHighlight(element,object,event);
		},
		
		/**
		 * When the user types a search query it is stored in this.searchQuery
		 * @method setSearchQuery
		 * @param event The Event 
		 */
		setSearchQuery: function(element,object,event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.setSearchQuery(event,this);
		},
		
		/**
		 * perform a search based on the users query
		 * @method search
		 * @param event The event
		 */
		search: function(element,object,event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.search(event,this);
		},
		
		/**
		 * Used to handle keyboard events
		 * @method onKeyPress
		 * @param element the HTML element
		 * @param obj
		 * @param event the Event
		 */
		onKeyPress: function(element, obj, event){
			if(!event){
				event = window.event;
			}
			this.searchBoxAction.onKeyPress(element, obj, event, this);
		},
		
		/**
		 * TODO Document Me
		 * @param element
		 * @param obj
		 * @param event
		 */
		suggest: function(element, obj, event){
			if(!event){
				event = window.event;
			}
			if(this.searchSuggest == "on"){
				this.searchBoxAction.suggest(event, this);
			}			
		},
		
		/**
		 * SearchBoxAction contains functions to handle events
		 * this should be overridden to change the action 
		 * of the event handler function  
		 */
		searchBoxAction : {
			
			_appsPopUp: null,
			
			_suggestionPopUp: null,
			
			_searchInput: null,
			
			/**
			 * Handles keyboard navigation 
			 * @param element the table element that fired the onKeypressEvent
			 * @param obj
			 * @param event the Event
			 */
			onKeyPress: function(element, obj, event,self){
				//If the user presses enter
				if(event.key == "Enter"){
					self.setSelectedApplication(element.rows[self._selectedRow], obj, event);
					self._selectedRow = -1;
				//If the user press the down arrow key to navigate down the list	
				}else if(event.key == "Down"){
					
					if(self._selectedRow != element.rows.length ){
						if(self._selectedRow != element.rows.length-1){
							self._selectedRow++;
						}
						self.displayHighlight(element.rows[self._selectedRow], obj, event);
						if(self._selectedRow > 0){
							self.removeHighlight(element.rows[self._selectedRow-1], obj, event);
						}	
					}
				//If the user presses the up key to navigate up the list	
				}else if(event.key == "Up"){
					
					if(self._selectedRow <= 0){
						self.searchBoxAction.removePopUp(self);
						self._selectedRow = -1;
					}else{
						self._selectedRow--;
						self.removeHighlight(element.rows[self._selectedRow+1], obj, event);
						if(self.selectedRow !=0){
							self.displayHighlight(element.rows[self._selectedRow], obj, event);
						}		
					}	
					
				}else if(event.key == "Tab"){
					self._selectedRow = -1;
				}	
			},
			
			/**
			 * Closes a member item (by removing it from its parent; the member list)
			 * @method closeMemberItem
			 * @param context
			 * @param element  The member list item
			 * @param object 
			 * @param event  The Event 
			 */
			closeMemberItem: function(context, element,object,event){
				// Get parent node
				var item = event.target.parentNode;

				// TODO if someone overrides the renderer will this work?
				
				// Prepare HTML for comparison
				var memberNode = item.parentNode;
				var memberNodeHtml = memberNode.innerHTML;
				memberNodeHtml = memberNodeHtml.replace(/\s+/g, '');
				memberNodeHtml = memberNodeHtml.replace(/ /g, '');
				
				// Remove member from list
				for (var i = 0; i < context.members.length; i++) {
					var member = context.members[i];
					var html = member.html;
					
					// Skip null entries (null entries represent entries that have been deleted)
					if (html == null) {
						continue;
					}
					
					// Remove dojo action listeners
					html = html.replace(/data-dojo-attach-event=".*"/, "");
					html = html.replace(/data-dojo-attach-event='.*'/, "");
					
					// Remove whitespace
					html = html.replace(/\s+/g, '');
					html = html.replace(/ /g, ' ');
					
					// Compare strings
					if (html == memberNodeHtml) {
						// Delete objects by setting their properties to null
						context.members[i].html = null;
						context.members[i].id = null;
						context.members[i].name = null;
						break;
					}
				}
				
				// Remove it
				item.parentNode.removeChild(item);
			},
			
			/**
			 * Opens the pop up showing a list of applications
			 * @method renderPopUp 
			 * @param self Context
			 */
			renderPopUp: function(self){
				// TODO shouldn't this just pass thru to the renderer?
				if(this._suggestionPopUp ){
					for(var i=0;i<self.domNode.children.length;i++){
						if(self.domNode.children[i] === this._suggestionPopUp){
							self.renderer.removeSuggestionPopUp(self.domNode,this._suggestionPopUp);
						}
					}
				}
				this._appsPopUp = self.renderer.renderPopUp(self,self.domNode);
			},
			
			/**
			 * Remove the Applications pop up
			 * @method removePopUp
			 * @param self Context 
			 */
			removePopUp: function(self){
				self.renderer.removePopUp(self.domNode,this._appsPopUp);
			},
			
			/**
			 * Stores the name of the application the user has selected and closes the application pop up
			 * @method setSelectedApplication
			 * @param element The HTML element 
			 * @param object
			 * @param event The Event
			 * @param self Context
			 */
			setSelectedApplication: function(element,object,event,self){
				this.removeHighlight(element, object, event);
				self.selectedApplication = element.children[1].textContent;
				self.renderer.removePopUp(self.domNode,this._appsPopUp);
				self.renderer.changeSelectedApplication(element.children[1].textContent,element.children[0].children[0]);
				
			},
			
			/**
			 * TODO Document Me
			 * 
			 * @param event
			 * @param popUp
			 * @param context
			 */
			setSuggestedSearch: function(event,popUp,context){
				var targetElement;
				var value;
				if(!event){
					event = window.event;
					targetElement = event.srcElement;
					value = targetElement.innerText;
				}else{
					targetElement = event.target;
					value = targetElement.textContent;
				}
			 
				var id = targetElement.id;
				var input = this._searchInput;
				
    			context._selectedResultItem.text = value;
    			context._selectedResultItem.id = id;
    			
				context.searchQuery = value;
				
				
				// Member list feature enabled
				if (context.memberList) {
					// We don't want to display the suggested search term (instead
					// we will just add it to the members list - see below)
					input.value = "";
					
					// Create member list item
					context.renderer.renderMemberListItem(context, value, id);

				} else {
					input.value = value;
				}
				
				while (popUp.firstChild) { 
				    popUp.removeChild(popUp.firstChild); 
				}
				context.renderer.removeSuggestionPopUp(context.domNode,popUp);
			},
			
			/**
			 * Highlights an element
			 * @method highLight
			 * @param element The HTML element to highlight
			 * @param object
			 * @param event the Event
			 */
			highLight: function(element,object,event){
				element.style.backgroundColor = "#E1F4F9";
			},
			
			/**
			 * Remove background highLight from an element which looses focus
			 * @method removeHighlight
			 * @param element the HTML element to remove the background highlight from
			 * @param object
			 * @param event the event 
			 */
			removeHighlight: function(element,object,event){
				element.style.backgroundColor = "#FFFFFF";
			},
			
			/**
			 * Sets the user's search query
			 * @method setSearchQuery
			 * @param event
			 * @param self
			 */
			setSearchQuery: function(event,self){
				self.searchQuery = event.target.value;
			},
			
			/**
			 * Provides a suggestion as to what the user is trying to search for
			 * @method suggest
			 * @param event the event
			 * @param context the context this
			 */
			// TODO What is the difference between suggest and search?
			suggest: function(event,context){
				var applicationParam = context.selectedApplication.toLocaleLowerCase();
            	applicationParam = applicationParam.replace(/ /g,'');
				
            	var inputBox = event.target;
				this._searchInput = inputBox;
				
				var query = inputBox.value;
				if(context.searchSuffix) {
					query = query + context.searchSuffix;
				}
				var popUp = context.renderer.renderSuggestionPopUp(context,context.domNode);
				this._suggestionPopUp = popUp;								
				
				var requestArgs;
				if(context.constraint){
					var jsonString = JSON.stringify(context.constraint);
					requestArgs = {"component": applicationParam, constraint:jsonString};
				}else{
					requestArgs = {"component": applicationParam};
				}
				if(context.searchArgs){
					lang.mixin(requestArgs,context.searchArgs);	
				}
				
				// TODO the && is not needed. Why do all the previous stuff if empty query is ignored?
				if(query && query != ""){
					// TODO This should only happen once!
					if(context.endpoint){
						searchService = new SearchService({endpoint:context.endpoint});
					}else{
						searchService = new SearchService();
					}
					
					var promise;
					if (context.searchType == "my") {
						promise = searchService.getMyResults(query, requestArgs);
					} else if (context.searchType == "myCommunities"){
						
						var args = {search:query};
						if(context.endpoint){
							communityService = new CommunityService({endpoint:context.endpoint});
						}else{
							communityService = new CommunityService();
						}						
						
						promise = communityService.getMyCommunities(args);
					}	else {
					
						promise = searchService.getResults(query, requestArgs);
					}
			        promise.then(
			            function(results) {
			            	context.searchBoxAction.handleSuggestResult(results,context,popUp,context.searchType);
			            },
			            
			            function(error) {
			                console.log(error);
			            }
			        );
				}
			},
			
			/**
			 * 
			 * @method handleSuggestResult
			 * @param results the results from the suggested search
			 * @param context the This of the outer class
			 * @param popUp the popUp Element where results are displayed 
			 */
			handleSuggestResult: function(results,context,popUp,searchType){
				// TODO should the renderer handle this?
				while (popUp.firstChild) { 
				    popUp.removeChild(popUp.firstChild); 
				}
				for(var i=0;i<results.length;i++){
            		var row = document.createElement("tr");
            		var data = document.createElement("td");
            		var title = results[i].getTitle();
            		var id="";
            		if(searchType=="myCommunities"){
            			id = results[i].getCommunityUuid();
            		}else{
            		    id = results[i].getId();
            		}
            		data.innerHTML = title;
            		data.id = id;
            		data.setAttribute("style","cursor:pointer");
            		data.onclick = function (event) { 
            			context.searchBoxAction.setSuggestedSearch(event,popUp,context);
            		};    		
            		row.appendChild(data);
            		popUp.appendChild(row);
            	}
				
			},
			
			/**
			 * When the user clicks the search button 
			 * @method search
			 * @param event The Event
			 */
			search: function(event,context){
				if(this._suggestionPopUp ){
					for(var i=0;i<context.domNode.children.length;i++){
						if(context.domNode.children[i] === this._suggestionPopUp){
							context.renderer.removeSuggestionPopUp(context.domNode,this._suggestionPopUp);
						}
					}
				}
				
				var applicationParam = context.selectedApplication.toLocaleLowerCase();
            	applicationParam = applicationParam.replace(/ /g,'');
				
            	//if this control is going to retrieve the search results from the server
				if(context.type == "full"){
					var requestArgs;
					if(context.constraint){
						var jsonString = JSON.stringify(context.constraint);
						requestArgs = {"component": applicationParam, constraint:jsonString};
					}else{
						requestArgs = {"component": applicationParam};
					}
					if(context.searchArgs){
						lang.mixin(requestArgs,context.searchArgs);	
					}
					if(context.endpoint){
						searchService = new SearchService({endpoint:context.endpoint});
					}else{
						searchService = new SearchService();
					}
					
					var query = context.searchQuery;
					if(context.searchSuffix) {
						query = query + context.searchSuffix;
					}
					var self = context;
				   
					var promise;
					if (context.searchType == "my") {
						promise = searchService.getMyResults(query, requestArgs);
					} else if (context.searchType == "myCommunities"){
						
						var args = {search:query};
						if(context.endpoint){
							communityService = new CommunityService({endpoint:context.endpoint});
						}else{
							communityService = new CommunityService();
						}						
						
						promise = communityService.getMyCommunities(args);
					}	else {
					
						promise = searchService.getResults(query, requestArgs);
					}
			        promise.then(
			            function(results) {
			            	if (context.memberList) {
			            		// If the member list feature is enabled then we need
			            		// to use a different search result event since we want the
			            		// members to be added to the members list and NOT
			            		// just displayed in the results table
			            		for(var i = 0; i < newResults.length; i++) {
			            			// Render each item in the search results
			            			if(context.searchType=="myCommunities"){
			            				context.renderer.renderMemberListItem(context, newResults[i].getTitle(), results[i].getCommunityUuid());
			            			}else{
			            				context.renderer.renderMemberListItem(context, newResults[i].getTitle(), results[i].getId());
			            			}
			            			
			            		}	   
			            	} else {
			            		var evt = document.createEvent("Event");
			            		evt.initEvent("searchResultEvent",true,true);
			            		evt.results = results;
			            		self.domNode.dispatchEvent(evt);
			            		evt = null;				
			            	}
			            },
			            function(error) {
			                console.log(error);
			            }
			        );
				}else {
					//use another component to retrieve
					var evt = document.createEvent("Event");
	            	evt.initEvent("searchResultEvent",true,true);
	            	evt.selectedApplication = applicationParam;
	            	evt.searchQuery = context.searchQuery;
	            	
	            	context.domNode.dispatchEvent(evt);
	            	evt = null;
				}
				
			}
		
		}
		
	});

	return SearchBox;
});

},
'sbt/connections/controls/search/SearchBoxRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

define(["../../../declare",
        "../../../lang",
        "../../../text!./templates/SearchBoxTemplate.html",
        "../../../text!./templates/SearchSuggestTemplate.html",
        "../../../text!./templates/PopUpTemplate.html",
        "../../../text!./templates/SuggestPopUpTemplate.html",
        "../../../i18n!./nls/SearchBoxRenderer",
        "../../../text!./templates/MemberListItemTemplate.html",
        "../../../text!./templates/MemberListTemplate.html",
        "../../../text!./templates/SingleApplicationSearch.html",
        "../../../text!./templates/SingleSearchPopUp.html"], 
        function(declare,lang, template, SuggestTemplate, PopUpTemplate, SuggestionPopUp ,nls,
        		MemberListItemTemplate, MemberListTemplate, SingleApplicationSearch, SingleSearchPopUp){
	/**
	 * @class SearchBoxRenderer
	 * @namespace sbt.connections.controls.search
	 * @module sbt.connections.controls.search.SearchBoxRenderer
	 */
	var SearchBoxRenderer = declare(null,{
		
		nls: null,
		
		_appsPopUp: null,
		
		_appsMemberListItem: null,
		
		_appsMemberList: null,
		
		_suggestionPopUp: null,
		
		_suggestionContainer: null,
		
		/**
		 * SearchBoxRenderer class constructor function
		 * @method constructor
		 * @param args
		 */
		constructor: function(args){	
			this.nls = nls;
		},
		
		/**
		 * Converts the HTML pop up template into a DOM node.
		 * Creates a Div element and uses the template as the div's inner HTML
		 * @method getDomeNode
		 * @returns The Search Box Dom Node 
		 */
		getDomNode: function(SearchBox){
			
			var htmlTemplate = "";
			
			if(SearchBox.predefinedSearch){
				var domStr = this._substituteItems(SingleApplicationSearch, this);
				SingleApplicationSearch = domStr;
				htmlTemplate = SingleApplicationSearch;
			}else{
				var domStr = this._substituteItems(template, this);
				template = domStr;
				htmlTemplate = template;
			}

			var div = this._convertToDomNode(htmlTemplate);
			
			this._suggestionContainer = document.createElement("div");
			this._suggestionContainer.setAttribute("style", "position:relative;broder:none");
			div.appendChild(this._suggestionContainer);
			
			var temp = div.getElementsByTagName("input");
			var input = temp[0];
			SearchBox._searchInput = input;
			
			return div;	
		},
		
		/**
		 * Converts the HTML pop up template into a DOM node.
		 * Creates a div and sets it's inner html to be the pop up template
		 * @method getPopUpNode
		 * @returns the applications list pop up DOM Node
		 */
		getPopUpNode: function(){
			
			var domstr = this._substituteItems(PopUpTemplate, this);
			PopUpTemplate = domstr;
			
			var div = this._convertToDomNode(PopUpTemplate);
			return div;	
		},
		
		/**
		 * Converts the HTML member list item template into a DOM node.
		 * Creates a div and sets it's inner html to be the member list item template
		 * @method getMemberListItemNode
		 * @param memberName The name to display
		 * @returns the applications list pop up DOM Node
		 */
		getMemberListItemNode: function(memberName){
			var domstr = this._substituteItems(MemberListItemTemplate, this);
			domstr = this._substituteMemberName(domstr, memberName);
			
			var obj = this._convertToDomNode(domstr);
			
			return obj;	
		},
		
		/**
		 * Converts the HTML member list template into a DOM node.
		 * Creates a div and sets it's inner html to be the member list template
		 * @method getMemberListItemNode
		 * @returns the applications list pop up DOM Node
		 */
		getMemberListNode: function(){
			
			var domstr = this._substituteItems(MemberListTemplate, this);
			MemberListTemplate = domstr;
			
			var div = this._convertToDomNode(MemberListTemplate);
			return div;	
		},
		
		/**
		 * Attaches events to the template 
		 * @method render
		 * @param searchBox
		 * @param el
		 * @param data
		 */
		render: function(searchBox, el, data) {
			searchBox.templateString = template;
			this._doAttachEvents(searchBox,el,data);			
		},
		
		/**
		 * renders the applications lists pop up
		 * @method renderPopUp
		 * @param searchBox The SearchBox class
		 * @param el The searchBox Element
		 */
		renderPopUp: function(searchBox,el){
			
			if(!this._appsPopUp){
				this._appsPopUp = this.getPopUpNode();
				this._doAttachEvents(searchBox,this._appsPopUp,{});	
			}
			
			el.appendChild(this._appsPopUp);
			this._appsPopUp.focus();
			return this._appsPopUp;
		},
		
		/**
		 * renders a member list item
		 * @method renderMemberListItem
		 * @param searchBox the SearchBox class
		 * @param memberName The member name to display
		 * @param memberId The member's profile id - this acts as a unique identifier (needed when, for example,
		 * 					  creating a new community)
		 */
		renderMemberListItem: function(searchBox, memberName, memberId){
			// Get node
			this._appsMemberListItem = this.getMemberListItemNode(memberName);
			
			// Create member object for storage
			var newMember = new Object();
			newMember.html = this._appsMemberListItem.innerHTML;
			newMember.id = memberId;
			newMember.name = memberName;
			// Make sure that the member hasn't already been selected
			for (var i = 0; i < searchBox.members.length; i++) {
				var member = searchBox.members[i];
				if (member.html == newMember.html) {
					return;
				}
			}
			
			// Add it to the list
			this._appsMemberList.appendChild(this._appsMemberListItem);
			
			// Attach event listeners
			this._doAttachEvents(searchBox,this._appsMemberListItem,{});	
			
			// Keep track of the added member
			searchBox.members.push(newMember);
			
			return this._appsMemberListItem;
		},
		
		/**
		 * renders the member list
		 * @method renderMemberList
		 * @param el HTML Element / DOM Node
		 */
		renderMemberList: function(el){
			
			if(!this._appsMemberList){
				// Generate the DOM object representing the member list
				this._appsMemberList = this.getMemberListNode();
			}
			
			// Append the list to the parent
			el.appendChild(this._appsMemberList);
		
			// Request focus
			this._appsMemberList.focus();
			
			return this._appsMemberList;
		},
		
		
		/**
		 * Renders the suggestions that appear under the search box.
		 * @method renderSuggestionPopUp 
		 * @param searchBox The searchBox class
		 * @param el The SearchBox DOM node / HTML element
		 * @returns The search suggestion pop up as a DOM Node
		 */
		renderSuggestionPopUp: function(searchBox,el){
			
			if(!this._suggestionPopUp){
				if(searchBox.predefinedSearch){
					this._suggestionPopUp= this._convertToDomNode(SingleSearchPopUp);
				}else{
					this._suggestionPopUp= this._convertToDomNode(SuggestionPopUp);
				}
				
			}
			this._suggestionContainer.appendChild(this._suggestionPopUp);
			return this._suggestionContainer.firstChild;
		},
		
		/**
		 * Removes the applications pop up
		 * @method removePopUp
		 * @param searchBoxElement The searchBox HTML Element
		 * @param popUp The DOM node that represents the popup that displays the list of applications on connections
		 */
		removePopUp: function(searchBoxElement,popUp){
			searchBoxElement.removeChild(popUp); 
		},
		
		/**
		 * remove the list of suggestions that appeas when searching
		 * @method removeSuggestionPopUp
		 * @param searchBoxElement The DOM Node that represents the search box
		 * @param popUpElement the DOM node pop up
		 */
		removeSuggestionPopUp: function(searchBoxElement,popUpElement){
			this._suggestionContainer.removeChild(popUpElement);
		},
		
		/**
		 * Changes the Text displayed in the "currently selected" application text field 
		 * @method changeSelectedApplication
		 * @param selectedApplication The name of the application to display
		 * @param el The table row element
		 */
		changeSelectedApplication: function(selectedApplication, trImgIcon){
			var elements = document.getElementsByTagName("*");
			for(var i=0;i<elements.length;i++){
				if(elements[i].textContent == nls.selectedApplication){
					
					//change the text showing the selected application
					elements[i].textContent = selectedApplication;
					nls.selectedApplication = selectedApplication;
					
					//change the css of the image icon to relate to the selected application
					var previous = i -1; // index
					var imgEl = elements[previous];
					imgEl.classList.remove(imgEl.classList[imgEl.classList.length-1]);
					var index = trImgIcon.classList.length-1;
					var newClass = trImgIcon.classList[index];
					imgEl.classList.add(newClass);
				}
			}
		},
		
		/*
		 * Converts a HTML String to a DOM Node, 
		 * @method _convertToDomNode
		 * @param template the html string to be converted to a DOM node
		 * @returns A DOM Node 
		 */
		_convertToDomNode: function(template){
			var div = null;
			if(typeof template =="string"){
				var wrapper= document.createElement('div');
				wrapper.innerHTML= template;
				wrapper.tabIndex = 0;
				div= wrapper;
			}
			return div.firstChild;	
		},
		
		/*
		 * Override _substitureItems as there are only NLS strings to be substituted 
		 * no XPath values, functions etc. 
		 * @param template
		 * @param renderer
		 * @returns
		 */
		_substituteItems: function(template,renderer){
			var text = template;
			if(text.indexOf("${nls.") != -1){
				var nls = renderer.nls;

				var startIndex = text.indexOf("${nls.");
				var endIndex = text.indexOf("}",startIndex);
	
				var nlsIndex = text.substring(startIndex+6,endIndex);
				var stringToReplace = text.substring(startIndex,endIndex+1);
	
				var replacingString = nls[nlsIndex];
	
				text = text.replace(stringToReplace,replacingString);
				
				//if there are more strings to substitute keep substituting 
				if(text.indexOf("${nls.") != -1){
					return this._substituteItems(text,renderer);
				}
			}
			//if no more strings to substitute return the final string	
			return text;
		},
		
		/*
		 * Override _substituteMemberName as there are only NLS strings to be substituted 
		 * no XPath values, functions etc. This function substitutes the member name in the template
		 * with the actual member item name.
		 * @method _substituteMemberName
		 * @param template
		 * @param renderer
		 * @returns
		 */
		_substituteMemberName: function(template,memberName){
			return template.replace("${memberName}", memberName);
		},
		
		/*
		 * connects events to event handlers  
		 */
		_doAttachEvents: function(searchBox, el, data) {
            var nodes = (el.all || el.getElementsByTagName("*"));
            for (var i in nodes) {
                var attachEvent = (nodes[i].getAttribute) ? nodes[i].getAttribute("data-dojo-attach-event") : null;
                if (attachEvent) {
                    nodes[i].removeAttribute("data-dojo-attach-event");
                    var event, events = attachEvent.split(/\s*,\s*/);
                    while((event = events.shift())) {
                        if (event) {
                            var func = null;
                            if (event.indexOf(":") != -1) {
                                var eventFunc = event.split(":");
                                event = lang.trim(eventFunc[0]);
                                func = lang.trim(eventFunc[1]);
                            } else {
                                event = lang.trim(event);
                            }
                            if (!func) {
                                func = event;
                            }
                            var callback = searchBox._hitch(searchBox, searchBox[func], nodes[i], data);
                            searchBox._connect(nodes[i], event, callback);
                        }
                    }
                }
            }
        }
	});
	
	return SearchBoxRenderer;
});
},
'sbt/connections/SearchService':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * Use the Search API to perform searches across the installed Connections applications.
 * 
 * Returns a list of results with the specified text in the title, description, or content. Encode the strings. By default, spaces are treated as an AND operator. The following operators are supported:
 *
 *  AND or &&: Searches for items that contain both words. For example: query=red%20AND%20test returns items that contain both the word red and the word test. AND is the default operator.
 *  NOT or !: Excludes the word that follows the operator from the search. For example: query=test%20NOT%20red returns items that contain the word test, but not the word red.
 *  OR: Searches for items that contain either of the words. For example: query=test%20OR%20red
 *  To search for a phrase, enclose the phrase in quotation marks (" ").
 *  +: The plus sign indicates that the word must be present in the result. For example: query=+test%20red returns only items that contain the word test and many that also contain red, but none that contain only the word red.
 *  ?: Use a question mark to match individual characters. For example: query=te%3Ft returns items that contain the words test, text, tent, and others that begin with te.
 *  -: The dash prohibits the return of a given word. This operator is similar to NOT. For example: query=test%20-red returns items that contains the word test, but not the word red.
 *
 * Note: Wildcard searches are permitted, but wildcard only searches (*) are not.
 * For more details about supported operators, see Advanced search options in the Using section of the product documentation.
 * 
 * @module sbt.connections.SearchService
 */
define([ "../declare", "../config", "../lang", "../stringUtil", "../Promise", "../json", "./SearchConstants", 
         "../base/BaseService", "../base/BaseEntity", "../base/AtomEntity", "../base/XmlDataHandler" ], 
    function(declare,config,lang,stringUtil,Promise,json,consts,BaseService,BaseEntity,AtomEntity,XmlDataHandler) {

    /**
     * Scope class represents an entry for a scopes feed returned by the
     * Connections REST API.
     * 
     * @class Scope
     * @namespace sbt.connections
     */
    var Scope = declare(AtomEntity, {

        /**
         * Construct a Scope entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        }

    });
    
    /**
     * Result class represents an entry for a search feed returned by the
     * Connections REST API.
     * 
     * @class Result
     * @namespace sbt.connections
     */
    var Result = declare(AtomEntity, {

        /**
         * Construct a Scope entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
        
        /**
         * Indicates a relative assessment of relevance for a particular search 
         * result with respect to the search query.
         * 
         * @method getRelevance
         * @return {String} Relative assessment of relevance
         */
        getRelevance : function() {
            return this.getAsNumber("relevance");
        }

    });
    
    /**
     * FacetValue class represents an entry for a search facet returned by the
     * Connections REST API.
     * 
     * @class FacetValue
     * @namespace sbt.connections
     */
    var FacetValue = declare(BaseEntity, {

        /**
         * Construct an FacetValue.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        	// create XML data handler
        	this.dataHandler = new XmlDataHandler({
                service : args.service,
                data : args.data,
                namespaces : lang.mixin(consts.Namespaces, args.namespaces || {}),
                xpath : lang.mixin(consts.FacetValueXPath, args.xpath || this.xpath || {})
            });
        	this.id = this.getAsString("uid");
        },
        
        /**
         * Return the value of id from facet entry.
         * 
         * @method getId
         * @return {String} ID of the facet entry
         */
        getId : function() {
            var id = this.getAsString("id");
            var parts = id.split("/");
            return (parts.length == 1) ? parts[0] : parts[1];
        },

        /**
         * Return the value of label from facet entry.
         * 
         * @method getLabel
         * @return {String} Facet entry label
         */
        getLabel : function() {
            return this.getAsString("label");
        },

        /**
         * Return the value of weigth from facet entry.
         * 
         * @method getWeight
         * @return {Number} Facet entry weight
         */
        getWeight : function() {
            return this.getAsNumber("weight");
        }

    });
    
    /*
     * Callbacks used when reading a feed that contains scope entries.
     */
    var ScopeFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                namespaces : consts.Namespaces,
                xpath : consts.SearchFeedXPath,
                service : service,
                data : data
            });
        },
        createEntity : function(service,data,response) {
            return new Scope({
            	namespaces : consts.Namespaces,
                service : service,
                data : data
            });
        }
    };

    /*
     * Callbacks used when reading a feed that contains search entries.
     */
    var ResultFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                namespaces : consts.Namespaces,
                xpath : consts.SearchFeedXPath,
                service : service,
                data : data
            });
        },
        createEntity : function(service,data,response) {
            return new Result({
            	namespaces : consts.Namespaces,
                xpath : consts.SearchXPath,
                service : service,
                data : data
            });
        }
    };
    
    /*
     * Callbacks used when reading a feed that contains search facets.
     */
    var FacetsCallbacks = {
        createEntities : function(service,data,response) {
        	var xpathExprs = lang.mixin({}, consts.SingleFacetXPath);
        	// facet param looks like this "{"id": "Person"}"
        	var facet = json.parse(response.options.query.facet);
        	xpathExprs.entries = xpathExprs.entries.replace("{facet.id}", facet.id);
            return new XmlDataHandler({
                namespaces : consts.Namespaces,
                xpath : xpathExprs,
                service : service,
                data : data
            });
        },
        createEntity : function(service,data,response) {
            return new FacetValue({
            	namespaces : consts.Namespaces,
                xpath : consts.FacetValueXPath,
                service : service,
                data : data
            });
        }
    };
    
    /**
     * SearchService class.
     * 
     * @class SearchService
     * @namespace sbt.connections
     */
    var SearchService = declare(BaseService, {
        
        contextRootMap: {
            search: "search"
        },

        /**
         * Constructor for SearchService
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
            if (!this.endpoint) {
                this.endpoint = config.findEndpoint(this.getDefaultEndpointName());
            }
        },

        /**
         * Return the default endpoint name if client did not specify one.
         * 
         * @method getDefaultEndpointName
         * @returns {String}
         */
        getDefaultEndpointName: function() {
            return "connections";
        },
        
        /**
         * Returns the set of supported values that can be passed to the "scope" parameter of the Search API. 
         * Scopes relating to Connections applications that have not been installed will not be returned.
         * 
         * @method getScopes
         * @param requestArgs
         */
        getScopes: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.AtomScopes, options, ScopeFeedCallbacks);
        },
        
        /**
         * Search Lotus Connection for public information.
         * 
         * @method getResults
         * @param query Text to search for
         * @param requestArgs
         */
        getResults: function(queryArg, requestArgs) {
        	requestArgs = this._stringifyRequestArgs(requestArgs);
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ query : queryArg } , requestArgs || {})
            };
            
            return this.getEntities(consts.AtomSearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search Lotus Connections for both public information and 
         * private information that you have access to. You must provide 
         * authentication information in the request to retrieve this 
         * resource.
         * 
         * @method getMyResults
         * @param query Text to search for
         * @param requestArgs
         */
        getMyResults: function(queryArg, requestArgs) {
        	requestArgs = this._stringifyRequestArgs(requestArgs);
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ query : queryArg } , requestArgs || {})
            };
            
            return this.getEntities(consts.AtomMySearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search IBM Connections for public information, tagged 
         * with the specified tags.
         * 
         * @method getTagged
         * @param tags tags to search for
         * @param requestArgs
         */
        getResultsByTag: function(tags, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 
                		constraint : this._createTagConstraint(tags)
                	} , 
                	requestArgs || {})
            };
                
            return this.getEntities(consts.AtomSearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search IBM Connections for both public information and private 
         * information that you have access to, tagged 
         * with the specified tags.
         * 
         * @method getMyResultsByTag
         * @param tags Tags to search for
         * @param requestArgs
         */
        getMyResultsByTag: function(tags, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 
            			constraint : this._createTagConstraint(tags)
                	} , 
                	requestArgs || {})
            };
                
            return this.getEntities(consts.AtomMySearch, options, ResultFeedCallbacks);
        },
        
        /**
         * Search IBM Connections Profiles for people using the specified 
         * query string and return public information.
         * 
         * @method getPeople
         * @param query Text to search for
         * @param requestArgs
         */
        getPeople: function(queryArg, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 
                		query : queryArg,
                		pageSize : "0",
                		facet : "{\"id\": \"Person\"}"
                	} , 
                	requestArgs || {})
            };
            
            return this.getEntities(consts.AtomSearch, options, FacetsCallbacks);
        },
        
        /**
         * Search IBM Connections Profiles for people using the specified 
         * query string and return public information.
         * 
         * @method getMyPeople
         * @param query Text to search for
         * @param requestArgs
         */
        getMyPeople: function(queryArg, requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : lang.mixin({ 
                		query : queryArg,
                		pageSize : "0",
                		facet : "{\"id\": \"Person\"}"
                	} , 
                	requestArgs || {})
            };
                
            return this.getEntities(consts.AtomMySearch, options, FacetsCallbacks);
        },
        
        //
        // Internals
        //
        
        /*
         * 
         */
        _stringifyRequestArgs: function(requestArgs) {
            if (!requestArgs) {
                return null;
            }
            var _requestArgs = {};
            for(var name in requestArgs){
                var value = requestArgs[name];
                if (lang.isObject(value)) {
                	_requestArgs[name] = json.stringify(value);
                } else {
                	_requestArgs[name] = value;
                }
            }
            return _requestArgs;
        },
        
        /*
         * Create a contraint JSON string for the specified tags
         */
        _createTagConstraint: function(tags) {
        	var jsonObj = { "type" : "category", "values" : new Array() };
        	if (lang.isArray(tags)) {
        		for (var i=0;i<tags.length;i++) {
        			jsonObj.values[i] = "Tag/" + tags[i];
        		}
        	} else {
        		jsonObj.values[0] = "Tag/" + tags;
        	}
        	return json.stringify(jsonObj);
        }
    });
    return SearchService;
});

},
'sbt/connections/SearchConstants':function(){
/*
 * � Copyright IBM Corp. 2013
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
         * XPath expressions used when parsing a Connections Search facets feed
         * that only contains a single facet
         */
        SingleFacetXPath : {
            // used by getEntitiesDataArray
            entries : "/a:feed/ibmsc:facets/ibmsc:facet[@id='{facet.id}']/ibmsc:facetValue"
            // used by getSummary
            //totalResults : "",
            //startIndex : "",
            //itemsPerPage : ""
        },

        /**
         * XPath expressions used when parsing a Connections Search facet
         */
        FacetValueXPath : {
            // used by getEntityData
            entry : "/ibmsc:facetValue",
            // used by getEntityId
            uid : "@id",
            // used by getters
            id : "@id",
            label : "@label",
            weight : "@weight"
        },

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
         * Returns the set of supported values that can be passed to the "scope" parameter of the Search API. 
         * Scopes relating to Connections applications that have not been installed will not be returned.
         */
        AtomScopes : "/${search}/atom/scopes",
        
        /**
         * API call for searching IBM Connections to find content that, for example, contains a specific text 
         * string in its title or content, or is tagged with a specific tag.
         */
        AtomSearch : "/${search}/atom/search",
        
        /**
         * API call for searching IBM Connections to find content that, for example, contains a specific text 
         * string in its title or content, or is tagged with a specific tag.
         */
        AtomMySearch : "/${search}/atom/mysearch",
        
		/**
         * These API's are all deprecated
         */
        publicSearch : "/${search}/atom/search/results",
        mySearch : "/${search}/atom/mysearch/results",
        peopleSearch : "/${search}/atom/search/facets/people",
        myPeopleSearch : "/${search}/atom/mysearch/facets/people",
        tagsSearch : "/${search}/atom/search/facets/tags",
        myTagsSearch : "/${search}/atom/mysearch/facets/tags",
        dateSearch : "/${search}/atom/search/facets/date",
        myDateSearch : "/${search}/atom/mysearch/facets/date",
        sourceSearch : "/${search}/atom/search/facets/source",
        mySourceSearch : "/${search}/atom/mysearch/facets/source"
        

    });
});
},
'sbt/connections/controls/search/SearchGrid':function(){
/*
 * � Copyright IBM Corp. 2013
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
define([ "../../../declare", 
         "../../../lang", 
         "../../../config", 
         "../../../controls/grid/Grid", 
         "./SearchGridRenderer", 
         "../../../store/parameter",
         "../../../connections/SearchConstants"], 
        function(declare, lang, sbt, Grid, SearchGridRenderer, parameter, consts) {

    var sortVals = {
        relevance: "",
        date: "date"
    };
    
    var ParamSchema = {
        pageNumber: parameter.oneBasedInteger("page"),  
        pageSize: parameter.oneBasedInteger("ps"),
        sortBy: parameter.sortField("sortKey",sortVals),
        sortOrder: parameter.sortOrder("sortOrder") 
    };
	
    /**
     * @class SearchGrid
     * @namespace sbt.connections.controls.search
     * @module sbt.connections.controls.search
     */
    var searchGrid = declare(Grid, {

        options : {
            "all" : {
                storeArgs : {
                    url : consts.mySearch,
                    attributes : consts.SearchXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "all"
                }
            },
            
            "public" : {
                storeArgs : {
                    url : consts.publicSearch,
                    attributes : consts.SearchXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "all"
                }
            },
            
            "people" : {
                storeArgs : {
                    url : consts.searchPeople,
                    attributes : consts.SearchXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "people"
                }
            },
            
            "tags" : {
                storeArgs : {
                    url : consts.tagsSearch,
                    attributes : consts.SearchXPath
                },
                rendererArgs : {
                    type : "tags"
                }
            },
            
            "apps" : {
                storeArgs : {
                    url : consts.sourceSearch,
                    attributes : consts.SearchXPath,
                    paramSchema: ParamSchema
                },
                rendererArgs : {
                    type : "apps"
                }
            }
        },

        defaultOption : "all",
        
        constructor: function() {
            var nls = this.renderer.nls;
            
            this._sortInfo = {
                relevance: { 
                    title: nls.sortByRelevance, 
                    sortMethod: "sortByRelevance",
                    sortParameter: "relevance" 
                },
                date: {
                    title: nls.sortByDate, 
                    sortMethod: "sortByDate",
                    sortParameter: "date"   
                }
               
            };

            this._activeSortAnchor = this._sortInfo.relevance;
            this._activeSortIsDesc = true;
        },
        
        contextRootMap: {
            search: "search"
        },

        /**
         * Override buildUrl to add format and component
         * 
         * @method buildUrl
         * @param url base url
         * @param args arguments that will be passed to the store
         * @param endpoint An endpoint which may contain custom service mappings.
         * @returns Built url
         */
        buildUrl: function(url, args, endpoint) {
            var params = { format : this.format };
            
            if(url.indexOf("?") != -1){
            	url = url.substring(0, url.indexOf("?"));
            }
            
            if (this.query) {
            	params = lang.mixin(params, this.query);
            }
            if (this.component) {
            	params = lang.mixin(params, { component : this.component });
            } 

            return this.constructUrl(url, params, this.getUrlParams(), endpoint);
        },

        /**
         * Return an instance of SearchGridRenderer.
         * 
         * @method createDefaultRenderer
         * @param args
         */
        createDefaultRenderer : function(args) {
            return new SearchGridRenderer(args);
        },
        
        /**
         * @method getSortInfo
         * @returns A list of strings that describe how the grid can be sorted
         * for profile grids these strings are "Display Name" and "Recent"
         */
        getSortInfo: function() {
            return {
                active: {
                    anchor: this._activeSortAnchor,
                    isDesc: this._activeSortIsDesc
                },
                list: [this._sortInfo.relevance, this._sortInfo.date]
            };
        },
        
        /**
         * @method sortByRelevance
         * @param el
         * @param data
         * @param ev
         */
        sortByRelevance: function(el, data, ev){
            this._sort("relevance", true, el, data, ev);
        },

        /**
         * Sort the grid rows by last modified date
         * @method sortByDate
         * @param el The element that was clicked, typically a "sort by" button
         * @param data the data associated with the element
         * @param ev the event
         */
        sortByDate: function(el, data, ev) {
            this._sort("date", true, el, data, ev);
        }
        
        // Internals

    });

    return searchGrid;
});

},
'sbt/connections/controls/search/SearchGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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
        "../../../i18n!./nls/SearchGridRenderer",
        "../../../text!./templates/BookmarkBody.html",
        "../../../text!./templates/CalendarBody.html",
        "../../../text!./templates/CommunityBody.html",
        "../../../text!./templates/DefaultBody.html",
        "../../../text!./templates/DefaultHeader.html",
        "../../../text!./templates/DefaultSummary.html",
        "../../../text!./templates/ProfileBody.html",
        "../../../text!./templates/ProfileHeader.html",
        "../../../text!./templates/PersonCard.html",
        "../../../text!./templates/StatusUpdateExtraHeader.html",
        "../../../text!./templates/StatusUpdateHeader.html",
        "../../../text!./templates/a.html",
        "../../../text!./templates/td.html",
        "../../../text!./templates/tr.html",
        "../../../text!./templates/li.html",
        "../../../text!./templates/ul.html",
        "../../../text!./templates/span.html",
        "../../../text!./templates/img.html",
        "../../../text!./templates/em.html",
        "../../../text!./templates/div.html"],
        function(declare, ConnectionsGridRenderer, lang, stringUtil, i18n, nls, bookmarkBodyTemplate, calendarBodyTemplate, communityBodyTemplate, defaultBodyTemplate, defaultHeaderTemplate, defaultSummaryTemplate, profileBodyTemplate, profileHeaderTemplate, personCardTemplate, statusUpdateExtraHeaderTemplate, statusUpdateHeaderTemplate, aElement, tdElement, trElement, liElement, ulElement, spanElement, imgElement, emElement, divElement) {

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
            if(tagCount === 0){
                return "";
            }
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
                if(i === 2 || i === tagCount - 1){
                    liClass = "lotusLast";
                }
                var aHref = "javascript:void(0);";
                var aOnClick = "onclick=\"searchObject.performTagFilter('" + tags[i] + "');\"";
                var aAlt = currentTag;
                
                if(item.getValue("highlightField")){
                    var aClass = currentTag;
                }else{ 
                    aClass = undefined;
                }
                var aContent = currentTag;
                
                var a = this.buildElement(aElement, {
                    hrefAttr: aHref,
                    onclickAttr: aOnClick,
                    altAttr: aAlt,
                    classAttr: aClass,
                    content: aContent
                });
                var liContent = a;
                if(i != tagCount - 1 && i != 2){
                    liContent += ",&nbsp";
                }
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
            }else if(this.resultType != this.resultTypes.statusUpdates){
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
                    content: this._nls.comment + item.getValue("commentsSummary")
                });
                return this.buildElement(divElement, {
                    styleAttr: "clear:both;",
                    content: divSpan
                });
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
                if(this.componentcontains(item, "blogs:ideationblogs:idea")){
                    aContent = this._nls.fromAnIdeationBlog;
                }else{
                    aContent = this._nls.fromABlog;
                }
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
            }else{
                return "";
            }
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
                }else if(this.componentContains(item, "activities:activity") || this.componentContains(item, "activities:community_activity") || this.componentContains(item, "activities:community_activity+members") || this.componentContains(item, "activities:explicit_membership_community_activity")){
                    spanContent = this._nls.activity;
                }else if(this.componentContains(item, "activities:bookmark")){
                    spanContent = this._nls.activityBookmark;
                }else if(this.componentContains(item, "activities:section")){
                    spanContent = this._nls.activitySection;
                }else if(this.componentContains(item, "activities:reply")){
                    spanContent = this._nls.activityComment;
                }else{
                    spanContent = this._nls.activityEntry;
                }
                return this.buildElement(spanElement, {
                    classAttr: "lotusMeta",
                    content: spanContent
                 });
            case resultTypes.blogs:
                if(this.componentContains(item, "blogs:ideationblogs:ideationblog")){
                    spanContent = this._nls.ideationBlog;
                }else if(this.componentContains(item, "blogs:ideationblogs:idea")){
                    spanContent = this._nls.idea;
                }else if(this.componentContains(item, "blogs:ideationblogs:comment")){
                    spanContent = this._nls.ideaComment;
                }else if(this.componentContains(item, "blogs:entry")){
                    spanContent = this._nls.blogEntry;
                }else if(this.componentContains(item, "blogs:comment")){
                    spanContent = this._nls.blogComment;
                }else{
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
                }else if(this.componentContains(item, "communities:feed")){
                    return this.buildElement(spanElement, {
                        classAttr: "lotusMeta",
                        content: this._nls.feed
                     });
                }else if(this.componentContains(item, "communities:bookmark")){
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
                }else if(this.componentContains(item, "communities:forums:category")){
                    spanContent = this._nls.forumCategory;
                }else{
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
                }else if(this.componentContains(item, "wikis:file")){
                    spanContent = this._nls.wikiFile;
                }else{
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
            if(item.getValue("primaryComponent") === appString){
                return true;
            }
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
            
            if(item.getValue("allDayEvent") ==="true"){
                allDayEventLi = this.buildElement(liElement,{
                    roleAttr: "listitem",
                    content: this._nls.eventIsAllDay
                });
            }
            if(item.getValue("repeatingEvent") ==="true"){
                allDayEventLi = this.buildElement(liElement,{
                    roleAttr: "listitem",
                    content: this._nls.eventRepeats
                });
            }
            if(item.getValue("location.length") > 1){
                allDayEventLi = this.buildElement(liElement,{
                    roleAttr: "listitem",
                    content: item.getValue("location")
                });
            }
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
            }else{
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
            if(item.getValue("authorState") != 'active'){
                return "lotusPersonInactive";
            }else{
                return "vcard";
            }
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
            if(item.getValue("authorJobTitle") && item.getValue("authorJobTitle").length != 0){
                return this.buildElement(liElement, {
                    content: item.getValue("authorJobTitle")+"&nbsp;",
                    classAttr: "lotusFirst",
                    roleAttr: "listitem"
                });
            }else{
                return this._nls.emptyString;
            }
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
            }else{
                return "";
            }
        },
        
        bodyUpdatedLi: function(grid, item, i, items){
            var liClass = "searchDateClass";
            if(item.getValue("authorName").length==0){
                liClass+= " lotusFirst";
            }
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
                
                if(this.application ==="dogear"){
                    ulContent += this.buildElement(liElement, {
                        content: this._nls.bookmarksTitle
                    }) + "\n";
                }
                if(this.application ==="activities:bookmark"){
                    ulContent += this.buildElement(liElement, {
                        content: this._nls.activitiesTitle
                    }) + "\n";
                }
                if(this.application ==="communities:bookmark"){
                    ulContent += this.buildElement(liElement, {
                        content: this._nls.communitiesTitle
                    }) + "\n";
                }
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
            if(this.componentContains(item, "activities:reply")){
                return this._nls.commentOn;
            }else{
                return this._nls.emptyString;
            }
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
            if(this.componentContains(item, "wikis:file")){
                return 'dir="' + this._nls.ltr + '"';
            }else{
                return this._nls.emptyString;
            }
        },
        
        inactiveLabel: function(grid, item, i, items){
            if(this.componentContains(item, "profiles")){
                return this._nls.inactiveLabel;
            }else{
                return this._nls.emptyString;
            }
        },
        
        colspan: function(grid, item, i, items){
            if(!this.componentContains(item, "status_update")){
                return 'colspan="2"';
            }else{
                return '';
            }
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
            if(item.getValue("authorState") === "inactive"){
                return "lotusDim";
            }else{
                return "lconnSearchComponentCategory";
            }
        },
        

        summaryStyle: function(grid, item, i, items){
            if(item.getValue("authorState") === "inactive"){
                return "filter: alpha(opacity = 50)";
            }else{
                return "";
            }
        },
        
        getApplication: function(item){
            if(typeof item.getValue("application") === "string"){
                return item.getValue("application");
            }else{
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
            }else if (primaryComponent.indexOf("communities") === 0){
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
            if(i===0){
                trClass = "lotusFirst";
            }
            var tdColspan = undefined;
            if(true){
                tdColspan = "2";
            }
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
                classAttr: trClass
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
               innerHTML: this._nls.empty
             }, lotusEmptyDiv);
        },
        
        tableClass: "lotusTable lconnSearchResults"
    });    
    return searchGridRenderer;
});

},
'sbt/connections/controls/sharebox/InputFormWrapper':function(){
/*
 * � Copyright IBM Corp. 2013
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

define(["sbt/declare", "sbt/connections/controls/WidgetWrapper", "sbt/text!./templates/InputFormContent.html"], function(declare, WidgetWrapper, defaultTemplate) {

    /**
     * @class sbt.controls.sharebox.InputFormWrapper
     */
    var InputFormWrapper = declare([ WidgetWrapper ], {
        /**
         * The html template of the iframe's inner html.
         * 
         * @property defaultTemplate
         * @type String
         */
        defaultTemplate: defaultTemplate,
        
        /**
         * The args object which will be substituted into the defaultTemplate.
         * 
         * @property args
         * @type Object
         */
        args: null,
        
        /**
         * Get args of the connections InputForm and store them so that they can be substituted into the defaultTemplate later.
         * 
         * @method constructor
         * @param {Object} args 
         *     @param {String} args.shareBoxNode Should contain the id of the html element to add the InputForm to.
         */
        constructor: function(args){
            this.args = args;
        }
        
    });
    
    return InputFormWrapper;
});
},
'sbt/connections/controls/vcard/CommunityVCard':function(){
/*
 * � Copyright IBM Corp. 2013
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
        "../../../text!./templates/CommunityVCard.html"], 
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
},
'sbt/connections/controls/vcard/ProfileVCard':function(){
/*
 * � Copyright IBM Corp. 2013
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
         * A url flag specifying whether or not connections should return dojo with the semantic tag service.
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
        	
        	SemanticTagService.loadSemanticTagService();
        },
        
        /**
         * @method renderError
         * @param el
         * @param error
         */
        renderError: function(el, error) {
            var ediv = domConstruct.create("div", {
                "class": this.errorClass,
                innerHTML: error.message,
                role: "alert",
                tabIndex: 0
              }, el, "only");
        }
        
    });
    
    return profileVCard;
});
},
'sbt/connections/controls/vcard/ProfileVCardInline':function(){
/*
 * � Copyright IBM Corp. 2013
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
        "../../../text!./templates/ProfileVCardInline.html"], 
        function(declare, dom, _TemplatedWidget, lang, SemanticTagService, template) {

    /**
     * @module sbt.controls.vcard.connections.ProfileVCardInline
     */
    var profileVcardInline = declare([ _TemplatedWidget ], {

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
         * A url flag specifying whether or not connections should return dojo with the semantic tag service.
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
         * 
         * @param args
         */
        constructor: function(args) {
        	lang.mixin(args);
        },
        
        postCreate: function() {
        	dom.setAttr(this.idNode, "class", (this.userId.indexOf("@") >= 0)  ? "email" : "x-lconn-userid");
        	
        	this.inherited(arguments);
        	
        	SemanticTagService.loadSemanticTagService();
        },
        
        renderError: function(el, error) {
            var ediv = domConstruct.create("div", {
                "class": this.errorClass,
                innerHTML: error.message,
                role: "alert",
                tabIndex: 0
              }, el, "only");
        }
        
    });
    
    return profileVcardInline;
});
},
'sbt/connections/controls/wrappers/FileGridWrapper':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../../declare", "../../../config", "../../../util", "../../../lang", "../../../url", "../../../connections/controls/WidgetWrapper", "../../../text!../templates/FileGridWrapperContent.html"], function(declare, config, util, lang, Url, WidgetWrapper, defaultTemplate) {

    /**
     * The wrapper for the ActivityStream. 
     * This class just has to provide its own template and the args it receives back to to the WidgetWrapper, which will take care of everything else.
     * 
     * @class sbtx.controls.astream.ActivityStreamWrapper
     */
    var FileGridWrapper = declare([ WidgetWrapper ], {

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
         * @method getTransformObject
         * @returns {Object}
         */
        getTransformObject: function(){
            var connectionsUrl = this._endpoint.baseUrl;
            var libUrl = new Url(config.Properties.libraryUrl);
            var libQuery = libUrl.getQuery();
            var libQueryObj = util.splitQuery(libQuery, "&");
            
            lang.mixin(libQueryObj, {
                lib: "dojo",
                ver: "1.8.0"
            });
            libQuery = util.createQuery(libQueryObj, "&");
            libUrl.setQuery(libQuery);
            
            var sbtProps = lang.mixin({}, config.Properties);
            lang.mixin(sbtProps, {
                libraryUrl: libUrl.getUrl(),
                loginUi: "popup"
            });
            var templateReplacements = {
                args: JSON.stringify(this.args),
                connectionsUrl: connectionsUrl,
                libraryUrl: libUrl.getUrl(),
                sbtProps: JSON.stringify(sbtProps)
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
        
        constructor: function(args){
            this.args = args;
        }
        
    });
    
    return FileGridWrapper;
});
},
'sbt/connections/controls/wrappers/ProfileCardWrapper':function(){
/*
 * � Copyright IBM Corp. 2013
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
define(["../../../declare", "../../../config", "../../../util", "../../../lang", "../../../url", "../../../connections/controls/WidgetWrapper", "../../../text!../templates/ProfileCardWrapperContent.html"], function(declare, config, util, lang, Url, WidgetWrapper, defaultTemplate) {

    /**
     * The wrapper for the ActivityStream. 
     * This class just has to provide its own template and the args it receives back to to the WidgetWrapper, which will take care of everything else.
     * 
     * @class sbtx.controls.astream.ActivityStreamWrapper
     */
    var ProfileCardWrapper = declare([ WidgetWrapper ], {

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
         * @method getTransformObject
         * @returns {Object}
         */
        getTransformObject: function(){
            var connectionsUrl = this._endpoint.baseUrl;
            var libUrl = new Url(config.Properties.libraryUrl);
            var libQuery = libUrl.getQuery();
            var libQueryObj = util.splitQuery(libQuery, "&");
            
            lang.mixin(libQueryObj, {
                lib: "dojo",
                ver: "1.8.0"
            });
            libQuery = util.createQuery(libQueryObj, "&");
            libUrl.setQuery(libQuery);
            
            var sbtProps = lang.mixin({}, config.Properties);
            lang.mixin(sbtProps, {
                libraryUrl: libUrl.getUrl(),
                loginUi: "popup"
            });
            var templateReplacements = {
                args: JSON.stringify(this.args),
                connectionsUrl: connectionsUrl,
                libraryUrl: libUrl.getUrl(),
                sbtProps: JSON.stringify(sbtProps)
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
        
        constructor: function(args){
            this.args = args;
        }
        
    });
    
    return ProfileCardWrapper;
});
},
'sbt/smartcloud/controls/nls/BaseGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
  root: {
      empty : "Empty",
      loading : "Loading...",
      previous : "Previous",
      previousPage : "Previous Page",
      next : "Next",
      nextPage : "Next Page",
      pagingResults : "${start} - ${end} of ${totalCount}",
      sortBy : "Sort by:",
      msgNoData : "Please wait..."
  }
});


},
'sbt/smartcloud/controls/profiles/ColleagueGrid':function(){
define(["../../../declare",
        "./ProfileGrid",
        "./ColleagueGridRenderer"], 
  function(declare,ProfileGrid,ColleagueGridRenderer){
	
	var ColleagueGrid = declare(ProfileGrid,{
		
		hideViewAll: false,
		
		createDefaultRenderer : function(args) {
            return new ColleagueGridRenderer(args);
        },
        
		handleViewAll: function(item, opts, event){
			this.hideViewAll = true;
			this.renderer.template = this.renderer.fullTemplate;
            this.renderer.render(this, this.domNode, opts.items, opts);
	
		}
	});
	
	 
	
	return ColleagueGrid;
});
},
'sbt/smartcloud/controls/profiles/ProfileGrid':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.smartcloud.controls.profiles.ProfileGrid
 */
define([ "../../../declare", 
		 "../../../config",
		 "../../../controls/grid/Grid", 
		 "./ProfileGridRenderer", 
		 "./ProfileAction", 
		 "../../../store/parameter",
		 "../../ProfileConstants"], 
    function(declare, sbt, Grid, ProfileGridRenderer, ProfileAction, parameter, ProfileConstants) {

	var ProfileXPath = {
        id : "o:person/o:id",
	 	entry : "/o:response/o:entry",
		uid : "o:person/o:id",
		name : "o:person/o:displayName",
		email : "o:person/o:emailAddress",
		title : "o:person/o:jobtitle",
		profileUrl : "o:person/o:profileUrl",
		orgName : "o:person/o:org/o:name",
		address : "o:person/o:address",
		phoneNumbers : "o:person/o:phoneNumbers/o:phone",
		photos : "o:person/o:photos/o:value"
	};
	
	var FeedXPath = {
        "entry" : "/o:response/o:entry",
        "entries" : "/o:response/o:entry",
        "totalResults" : "/o:response/o:totalResults",
        "startIndex" : "/o:response/o:startIndex",
        "itemsPerPage" : "/o:response/o:itemsPerPage"
	};
	
	var Namespaces = {
		o : "http://ns.opensocial.org/2008/opensocial"
	};
	
	var ParamSchema = {
        startIndex : parameter.zeroBasedInteger("startIndex"),
        pageSize : parameter.oneBasedInteger("count")
    };
	
    /**
     * @class ProfileGrid
     * @namespace sbt.connections.controls.profiles
     */
    var ProfileGrid = declare(Grid, {
   
    	/**
    	 * @param options, This is a list of all
    	 * the different types of profile grids available.
    	 * Depending on which one is selected specific arguments will be given to
    	 * the atom store and grid renderer.
    	 */
        options : {
            "contacts" : {
                storeArgs : {
                    url : ProfileConstants.GetMyContacts,
                    attributes : ProfileXPath,
                    paramSchema : ParamSchema,
                    feedXPath : FeedXPath,
                    namespaces : Namespaces
                },
                rendererArgs : {
                    type : "contacts"
                }
            },
        	"friends" : {
	            storeArgs : {
	                url : ProfileConstants.GetMyConnections,
	                attributes : ProfileXPath,
	                paramSchema : ParamSchema,
	                feedXPath : FeedXPath,
	                namespaces : Namespaces
	            },
	            rendererArgs : {
	                type : "friends"
	            }
	        }
        },
        
        /**
         * Endpoint to use, default is 'smartcloud'
         */
    	endpoint: "smartcloud",
    	
        /**
         * A profile action, defines default behaviour for when 
         * items in the grid are clicked on or hovered on,
         * it is possible to override these actions
         */
        profileAction: new ProfileAction(),
        
        /**
         * This is the default grid that will be created if no 
         * arguments are given.
         */
        defaultOption: "contacts",
        
        /**
         * Constructor function
         * @method constructor
         */
        constructor: function(args){
        },
        
        /**
         * Creates a renderer for the grid.The renderer is responsible for 
         * loading the grid's HTML content.
         * @method createDefaultRenderer
         * @param args sets the template the renderer will use, by checking args.type, but for
         * profile grids this will always be "profile"
         * @returns an instance of a  profile gird renderer.
         */
        createDefaultRenderer : function(args) {
            return new ProfileGridRenderer(args);
        },
        
        /**
         * In the grid HTML an element can have an event attached 
         * using dojo-attach-event="onClick: handleClick".
         * This method is the handler for the onclick event.
         * @method handleClick
         * @param el the element that fired the event
         * @param data all of the items from the current row of the grid. 
         * @param ev the event 
         */
        handleClick: function(el, data, ev) {
            if (this.profileAction) {
            	console.log(data);
                dojo.stopEvent(ev);
                
                this.profileAction.execute(data, { grid : this.grid }, ev);
            }
        },        

        // Internals
        
        /*
         * Override to change count if needed
         */
        _doQuery: function(store, options) {
        	// OpenSocial API doesn't like requests for more than is available
        	if (options && options.total) {
        		options.count = Math.min(options.count, options.total - options.start);
        	}
        	
        	this.inherited(arguments);
        }
        
    });

    return ProfileGrid;
});
},
'sbt/smartcloud/controls/profiles/ProfileGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.smartcloud.controls.profiles.ProfileGridRenderer
 */
define(["../../../declare", "../../../lang", "../../../stringUtil", "../BaseGridRenderer",
        "../../../i18n!./nls/ProfileGridRenderer",
        "../../../text!./templates/ProfileRow.html"], 
        function(declare, lang, stringUtil, BaseGridRenderer, nls, profileTemplate) {
		
    /**
     * @class ProfileGridRenderer
     * @namespace sbt.smartcloud.controls.profiles
     */
    var ProfileGridRenderer = declare(BaseGridRenderer, {

         /*
          * The strings used in the grid, these are stored in a separate file, in the nls folder
          */
         _nls: nls,
         
         /**
          * The template used to display a row in the grid
          */
         template: profileTemplate,
         
         /**
          * The template used to construct a photo url 
          */
         contactImageUrl: "{baseUrl}/contacts/img/photos/{photo}",
         
         /**
          * The template used to construct a no photo url
          */
         noContactImageUrl: "{baseUrl}/contacts/img/noContactImage.gif",
        
         /**
          * The constructor function
          * @method constructor
          * @param args
          */
         constructor: function(args) {
         },

         /**
          * Sets the css class for the row
          * @method rowClass
          * @param grid The Grid Dijit 
          * @param item the current row
          * @param i the number of the current row, ie 0, 1, 2 etc
          * @param items all of the rows in the grid
          */
         rowClass: function(grid, item, i, items) {
             item.rowClass = (i === 0 ? "lotusFirst" : (i % 2 === 1 ? "lotusAltRow" : null));
         },
         
         /**
          * 
          * @param grid
          * @param item
          * @param i
          * @param items
          * @returns {String}
          */
         photoUrl: function(grid, item, i, items) {
        	 var ep = grid.store.getEndpoint();
        	 if (!ep) return null;
        	 
        	 var photos = item.getValue("photos");
        	 if (photos && lang.isArray(photos) && photos.length > 1) {
        		 return stringUtil.replace(this.contactImageUrl, { baseUrl : ep.baseUrl , photo : photos[1] });
        	 } else {
        		 return stringUtil.replace(this.noContactImageUrl, { baseUrl : ep.baseUrl });
        	 }
         },
         
         /**
          * 
          * @param grid
          * @param item
          * @param i
          * @param items
          * @returns {String}
          */
         primaryTelephone: function(grid, item, i, items) {
        	 var phoneNumbers = item.getValue("phoneNumbers");
        	 if (phoneNumbers && lang.isArray(phoneNumbers) && phoneNumbers.length > 0) {
        		 return phoneNumbers[0];
        	 } else {
        		 return undefined;
        	 }
         },
         
         /**
          * Displays a tooltip by calling the getTooltip function in the ProfileAction class
          * @method tooltip
          * @param grid The Grid control
          * @param item the element to display the tooltip
          * @param i the number of the current row
          * @param items all of the items in the grid row
          * @returns A Tooltip the default for profiles is to display the vCard
          */
         tooltip: function(grid, item, i, items) {
             if (grid.profileAction) {
                 return grid.profileAction.getTooltip(item);
             }
         }
         
    });
    
    return ProfileGridRenderer;
});
},
'sbt/smartcloud/controls/profiles/ProfileAction':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.smartcloud.controls.profiles.ProfileAction 
 */
define([ "../../../declare", "../../../controls/grid/GridAction", "dojo/string" ], 
        function(declare, GridAction, string) {

    /**
     * @class ProfileAction
     * @namespace sbt.smartcloud.controls.profiles
     */
    var ProfileAction = declare(GridAction, {
        
    	/**Strings used in the actions */
        nls: {
            tooltip: "Go to ${name}"
        },
        
        /**ProfileAction Constructor function
         * @method constructor
         * */
        constructor: function() {
        },
        
        /**
         * Handles displaying a tooltip for an item
         * For profiles, the tooltip by default will be a business card
         * So nothing is done in this function
         * @method getTooltip
         * @param item The element that will use the tooltip
         */
        getTooltip: function(item) {
        	
        	//for default the semantic tag service will pop up the business card
        	//so do nothing here

        },
        
        /**
         * The execute function is called from the handle click function
         * For Profiles by default the business card functionality is used
         * which works from the Semantic tag service so nothing is done here.
         * @method execute
         * @param item The item which fired the event
         * @param opts
         * @param event The event
         */
        execute: function(item, opts, event) {
        
        	//for default the semantic tag service will pop up the business card
        	//so do nothing here
        }

    });

    return ProfileAction;
});
},
'sbt/smartcloud/ProfileConstants':function(){
/*
 * � Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Definition of constants for ProfileService.
 */
define([ "../lang" ], function(lang) {

    return lang.mixin( {} , {
        /**
         * Default size for the profile cache
         */
        DefaultCacheSize : 10,
        
		/**
         * Retrieve the profile entry of the logged in user.
         */
        GetProfile : "/lotuslive-shindig-server/social/rest/people/@me/@self",
        
        /**
         * Retrieve the logged in user's profile connections.
         */
        GetMyConnections : "/lotuslive-shindig-server/social/rest/people/@me/@friends",
        
        /**
         * Retrieve a profile's user Identity.
         */
        GetUserIdentity : "/manage/oauth/getUserIdentity",
        
        /**
         * Retrieve a Contact's Profile.
         */
        GetContactByGUID : "/lotuslive-shindig-server/social/rest/people/lotuslive:contact:{idToBeReplaced}/@self",
        
        /**
         * Retrieve a profiles entry using GUID.
         */
        GetProfileByGUID : "/lotuslive-shindig-server/social/rest/people/lotuslive:user:{idToBeReplaced}/@self",
        
        /**
         * Retrieve the logged in user's profile contacts.
         */
        GetMyContacts : "/lotuslive-shindig-server/social/rest/people/@me/@all",
        
        /**
         * JsonPath expressions to be used when reading a Profile Entry
         */
        ProfileJsonPath : { 
        	thumbnailUrl : "$..photo",
        	address : "$..address",
        	department : "$..name",
        	jobTitle : "$..jobtitle",
        	telephone : "$..telephone",
        	about : "$..aboutMe",
        	id : "$..id",
        	objectId : "$..objectId",
        	displayName : "$..displayName",
        	emailAddress : "$..emailAddress",
        	profileUrl : "$..profileUrl",
        	country : "$..country",
        	orgId : "$..orgId",
        	org : "$..org.name",
        	global : "$..",
        	firstElement : "$[0]",
        	totalResults : "totalResults",
        	startIndex : "startIndex",
        	itemsPerPage : "itemsPerPage"
        }
    });
});
},
'sbt/smartcloud/controls/profiles/ColleagueGridRenderer':function(){
define(["../../../declare",
        "../BaseGridRenderer",
        "../../../text!./templates/ColleagueItem.html",
        "../../../stringUtil",
        "../../../lang",
        "./nls/ColleagueGridRenderer",
        "../../../text!./templates/ColleagueItemFull.html",
        "../../../text!./templates/ViewAll.html"], 
		
function(declare, BaseGridRenderer, ColleagueItemTemplate, stringUtil, lang, nls, colleagueItemFullTemplate, viewAllTemplate){
	
	var ColleagueGridRenderer = declare(BaseGridRenderer, {
		
		_nls: nls,
		
		containerClass: "lotusChunk",
		
		/** The template used to construct a photo url  */
        contactImageUrl: "{baseUrl}/contacts/img/photos/{photo}",
        
        /** The template used to construct a no photo url*/
        noContactImageUrl: "{baseUrl}/contacts/img/noContactImage.gif",
        
        /**The table row template*/
		template: ColleagueItemTemplate,
		
		fullTemplate: colleagueItemFullTemplate,
		
		viewAllTemplate: viewAllTemplate,
		
		/**The HTML template to show paging */
		pagerTemplate : null,
       
		/**The HTML template to show sorting options*/
        sortTemplate : null,
        
        /**The HTML template for sort Anchors*/
        sortAnchor : null,
		
		/**
         * The constructor function
         * @method constructor
         * @param args
         */
        constructor: function(args) {
        },
        
        /**
         * @param grid
         * @param item
         * @param i
         * @param items
         * @returns {String}
         */
        photoUrl: function(grid, item, i, items) {
        	var ep = grid.store.getEndpoint();
       		if (!ep) return null;
       	 
       		var photos = item.getValue("photos");
       		if (photos && lang.isArray(photos) && photos.length > 1) {
       			return stringUtil.replace(this.contactImageUrl, { baseUrl : ep.baseUrl , photo : photos[1] });
       		}else{
       			return stringUtil.replace(this.noContactImageUrl, { baseUrl : ep.baseUrl });
       		}
        },
        
        render: function(grid, el, items, data) {
            while (el.childNodes[0]) {
                this._destroy(el.childNodes[0]);
            }
            var size = items.length;
            if (size === 0) {
               this.renderEmpty(grid, el);
            }
            else {
               var container = this.renderContainer(grid, el, items, data);
               for (var i=0; i<items.length; i++) {
                   this.renderItem(grid, container, data, items[i], i, items);
               }
               this.renderViewAll(grid, el, items, data);
            }
         },
         
         renderContainer: function(grid, el, items, data) {          
             return this._create("div", { "class": this.containerClass }, el);
         },
         
         renderViewAll: function(grid, el, items, data){
        	if (this.viewAllTemplate && !grid.hideViewAll) {
                var node;
                if (this._isString(this.viewAllTemplate)) {
                    var domStr = this._substituteItems(this.viewAllTemplate, grid, this, items, data);
                    node = this._toDom(domStr, el.ownerDocument);
                } else {
                    node = this.sortTemplate.cloneNode(true);
                }
                el.appendChild(node);
                
                this._doAttachEvents(grid, el, data);
            }
         },
         
         viewAllTitle: function(grid, renderer, items, data) {
            var str = (data.totalCount == 1) ? this.nls.root.person : this.nls.root.people;
            var totalCount = stringUtil.replace(str, { totalCount : data.totalCount });
            return stringUtil.replace(nls.root.viewAll, { totalCount : totalCount });
         }
		
	});
	return ColleagueGridRenderer;
});
},
'sbt/smartcloud/controls/profiles/nls/ColleagueGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
  root: {
	  viewAll : "View All ({totalCount})",
	  empty : "No colleagues in the list.",
	  people: "{totalCount} people",
	  person: "{totalCount} person"
  }
});

  
},
'sbt/smartcloud/controls/profiles/ProfilePanel':function(){
/*
 * � Copyright IBM Corp. 2013
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
 * @module sbt.connections.controls.profiles.ProfilePanel
 */
define(["../../../declare", "../../../lang", "../../../config", 
        "../../../smartcloud/ProfileService",
        "../../../controls/panel/_ProfilePanel",
        "../../../text!sbt/smartcloud/controls/profiles/templates/ProfilePanel.html"], 
        function(declare, lang, config, ProfileService, _ProfilePanel, PanelTmpl) {

    var getUserIdentity = "/manage/oauth/getUserIdentity";
	
    /**
     * @module sbt.connections.controls.profiles.ProfilePanel
     */
    var ProfilePanel = declare([ _ProfilePanel ], {
    	
    	template: PanelTmpl,
    	profileService: null,
                
        constructor: function(args) {
        },
        
        getMyProfile: function() {
            var path = getUserIdentity;
            
            if (this.userid) {
            	this.getProfile(this.userid);
            } else {
                var self = this;
                var endpoint = this._getEndpoint();
                endpoint.request(path, { handleAs : "json", preventCache : true }).then(
                    function(response) {
                    	var userid = response.subscriberid;
                        self.getProfile(userid);
                    },
                    function(error) {
                        self._displayError(error);
                    }
                );
            }
        },
        
        getProfile: function(id) {
        	var self = this;
            var promise = this._getProfileService().getProfile(id);
            promise.then(    
                function(profile) {
                	self.profile = profile;
                	self.showProfile();
                },
                function(error) {            	
                    self.profile = null;
                    self._displayError(error);
                }
            );
        },
        
        // Internals
        
        _getProfileService: function() {
        	if (!this.profileService) {
                this.profileService = new ProfileService(this._getEndpointName());
        	}
        	return this.profileService;
        },
        
        _getEndpoint: function() {
        	return config.findEndpoint(this._getEndpointName());
        },
        
        _getEndpointName: function() {
        	return this.endpoint || "smartcloud";
        }
        
    });
    
    return ProfilePanel;
});
},
'sbt/smartcloud/ProfileService':function(){
/*
 * � Copyright IBM Corp. 2012
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
 *	Social Business Toolkit SDK.
 *	@author Vimal Dhupar
 *
 *	Javascript APIs for IBM SmartCloud Profiles Service.
 *	@module sbt.smartcloud.ProfileService
**/

define(["../declare","../lang", "../config","../stringUtil","../Cache","./Subscriber","../Jsonpath","../base/BaseService", "../base/JsonDataHandler", "./ProfileConstants", "../base/BaseEntity","../Promise"],
		function(declare, lang, config, StringUtil, Cache, Subscriber, JsonPath, BaseService, JsonDataHandler, Consts, BaseEntity, Promise) {
	/**
     * Profile class representing the Smartcloud User Profile.
     * 
     * @class Profile
     * @namespace sbt.smartcloud
     */
	var Profile = declare(BaseEntity, {
		/**
		 * Profile Class Constructor
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },
		
        /**
         * Loads the profile object with the profile entry document associated
         * with the profile. By default, a network call is made to load the
         * profile entry document in the profile object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         * 
         */
		load: function(args) {
			var profileId = this.getId();
            var promise = this.service._validateProfileId(profileId);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    return new JsonDataHandler({
                        data : data,
                        jsonpath : Consts.ProfileJsonPath
                    });
                }
            };
            var requestArgs = {};
            requestArgs.userid = profileId;
            lang.mixin(requestArgs, args || {format:"json"});            
            var options = {
                handleAs : "json",
                query : requestArgs
            };
            return this.service.getEntity(consts.GetProfile, options, profileId, callbacks, args);
		},
		
		/**
		 * Returns the id of the User
		 * @method getId
		 * @return {String} id of the User	
		**/
		getId: function () {
			return this.getAsString("id");
		},
		
		/**
		 * Returns the object id of the User
		 * @method getObjectId
		 * @return {String} id of the User	
		**/
		getObjectId: function () {
			return this.getAsString("objectId");
		},
		/**
		Get display name of the User
		@method getDisplayName
		@return {String} display name of the User	
		**/
		getDisplayName: function () {
			return this.getAsString("displayName");
		},
		/**
		Get email of the User
		@method getEmail
		@return {String} email of the User	
		**/
		getEmail: function () {
			return this.getAsString("emailAddress");
		},
		/**
		Get thumbnail URL of the User
		@method getThumbnailUrl
		@return {String} thumbnail URL of the User
		**/
		getThumbnailUrl: function () {
			var image = this.getAsString("thumbnailUrl");
			if(image)
				image = this.service.endpoint.baseUrl+"/contacts/img/photos/"+ image;  // TODO : work in making this generic
			return image;
		},
		/**
		Get address of the profile
		@method getAddress
		@return {String} Address object of the profile
		**/
		getAddress: function () { 
			var address = this.getAsArray("address");
			address = this.dataHandler.extractFirstElement(address);
			return address;
		},
		/**
		Get department of the profile
		@method getDepartment
		@return {String} department of the profile
		**/
		getDepartment: function () {
			return this.getAsString("department"); 
		},
		/**
		Get job title of the profile
		@method getJobTitle
		@return {String} job title of the profile
		**/
		getJobTitle: function () {
			return this.getAsString("jobTitle");
		},
		/**
		Get profile URL of the profile
		@method getProfileUrl
		@return {String} profile URL of the profile
		**/
		getProfileUrl: function () {
			return this.getAsString("profileUrl");
		},
		/**
		Get telephone number of the profile
		@method getTelehoneNumber
		@return {String} Telephone number object of the profile
		**/
		getTelephoneNumber: function () {
			return this.getAsString("telephone"); 
		},
		/**
		Get Country of the profile
		@method getCountry
		@return {String} country of the profile
		**/
		getCountry: function () { 
			return this.getAsString("country"); 
		},
		/**
		Get Organization Id of the profile
		@method getOrgId
		@return {String} Organization Id of the profile
		**/
		getOrgId: function () { 
			return this.getAsString("orgId"); 
		},
		/**
		Get Organization of the profile
		@method getOrg
		@return {String} Organization of the profile
		**/
		getOrg: function () { 
			return this.getAsString("org"); 
		},
		/**
		Get "About Me"/description of the profile
		@method getAbout
		@return {String} description of the profile
		**/
		getAbout: function () { 
			return this.getAsString("about"); 
		}
	});
	
	/**
     * Callbacks used when reading an entry that contains a Profile.
     */
    var ProfileCallbacks = {
        createEntity : function(service,data,response) {
        	var entryHandler = new JsonDataHandler({
                    data : data,
                    jsonpath : Consts.ProfileJsonPath
                });

            return new Profile({
                service : service,
                id : entryHandler.getEntityId(),
                dataHandler : entryHandler
            });
        }
    };
    
    /**
     * Callbacks used when reading a feed that contains multiple Profiles.
     */
    var ProfileFeedCallbacks = {
        createEntities : function(service,data,response) {
        	return new JsonDataHandler({
                    data : data,
                    jsonpath : Consts.ProfileJsonPath
                });
        }, 
        
        createEntity : function(service,data,response) {
        	var entryHandler = new JsonDataHandler({
                data : data,
                jsonpath : Consts.ProfileJsonPath
            });

        return new Profile({
            service : service,
            id : entryHandler.getEntityId(),
            dataHandler : entryHandler
        });
        }
    };
	
	/**
	 * 	Profile service class associated with a profile service of IBM SmartCloud.
     * 
     * @class ProfileService
     * @namespace sbt.smartcloud
     */
	var ProfileService = declare(BaseService, {		
		_profiles: null,

		 /**
         * 
         * @constructor
         * @param args
         */
		constructor : function(args) {
            if (!this.endpoint) {
                this.endpoint = config.findEndpoint(this.getDefaultEndpointName());
            }
            if(!this._cache){
        		if(config.Properties.ProfileCacheSize || Consts.DefaultCacheSize){
        			this._cache = new Cache(config.Properties.ProfileCacheSize || Consts.DefaultCacheSize);
        		}        		
        	}     
        },

        /**
         * Return the default endpoint name if client did not specify one.
         * @returns {String}
         */
        getDefaultEndpointName: function() {
            return "smartcloud";
        },
		
        /**
         * Get the profile of a user.
         * 
         * @method getProfile
         * @param {String} userId Userid of the profile
         * @param {Object} args Argument object
         */
        getProfile : function(userId, args) {
            var idObject = this._toIdObject(userId);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin(idObject, args || {format:"json"});
            var options = {
                method : "GET",
                handleAs : "json", 
                query : args || { format: "json" }
            };
            var entityId = encodeURIComponent(idObject.userid);
            var url = this.constructUrl(Consts.GetProfileByGUID, {}, {idToBeReplaced : entityId});
            return this.getEntity(url, options, entityId, this.getProfileCallbacks());
        },
        
        /**
         * Get the profile of a user.
         * 
         * @method getProfileByGUID
         * @param {String} userId Userid of the profile
         * @param {Object} args Argument object
         * @deprecated Use getProfile instead.
         */
        getProfileByGUID : function(userId, args) {
            return this.getProfile(userId, args);
        },
        
        /**
         * Get the profile of a logged in user.
         * 
         * @method getMyProfile
         * @param {Object} args Argument object
         */
        getMyProfile : function(args) {
            var self = this;
        	var url = Consts.GetUserIdentity;
        	
        	var promise = new Promise();
        	this.endpoint.request(url, { handleAs : "json" }).then(function(response) {
        		
        		var idObject = self._toIdObject(response.subscriberid);
	            var promise1 = self._validateIdObject(idObject);
	            if (promise1) {
	                return promise1;
	            }
	
	            var requestArgs = lang.mixin(idObject, args || {format:"json"});
	            var options = {
	                method : "GET",
	                handleAs : "json", 
	                query : requestArgs
	            };
	            var entityId = encodeURIComponent(idObject.userid);
	            var url = self.constructUrl(Consts.GetProfileByGUID, {}, {idToBeReplaced : entityId});
	            (self.getEntity(url, options, entityId, self.getProfileCallbacks())).then(function(response) {
	            	promise.fulfilled(response);
				},
				function(error) {
					promise.rejected(error);
				});
        	},
        	function(error) {
        		promise.rejected(error);
        	}
        );
        return promise;
        },
        
        /**
         * Get the contact details of a user.
         * 
         * @method getContact
         * @param {String} userId Userid of the profile
         * @param {Object} args Argument object
         */
        getContact : function(userId, args) {
            var idObject = this._toIdObject(userId);
            var promise = this._validateIdObject(idObject);
            if (promise) {
                return promise;
            }

            var requestArgs = lang.mixin(idObject, args || {format:"json"});
            var options = {
                method : "GET",
                handleAs : "json", 
                query : requestArgs
            };
            var entityId = idObject.userid;
            var url = this.constructUrl(Consts.GetContactByGUID, {}, {idToBeReplaced : entityId});
            return this.getEntity(url, options, entityId, this.getProfileCallbacks());
        },
        
        /**
         * Get the contact details of a user.
         * 
         * @method getContactByGUID
         * @param {String} userId Userid of the profile
         * @param {Object} args Argument object
         * @deprecated Use getContact instead.
         */
        getContactByGUID : function(userId, args) {
           return this.getContact(userId, args);
        },
        
        /**
         * Get logged in user's Connections
         * 
         * @method getMyConnections
         * @param {Object} args Argument object
         */
        getMyConnections : function(args) {
        	var options = {
        		method : "GET",
                handleAs : "json",
                 query : args || {format:"json"}
        	};
            return this.getEntities(Consts.GetMyConnections, options, this.getProfileFeedCallbacks());
        },
        
        /**
         * Get logged in user's Contacts
         * 
         * @method getMyContacts
         * @param {Object} args Argument object
         */
        getMyContacts : function(args) {
        	var options = {
            		method : "GET",
                    handleAs : "json",
                    query : args || {format:"json"}
        	};
        	return this.getEntities(Consts.GetMyContacts, options, this.getProfileFeedCallbacks());
        },
        
        /**
         * Get logged in user's Contacts considering the startIndex and count as provided by the user
         * 
         * @method getMyContactsByIndex
         * @param startIndex
         * @param count
         * @param {Object} args Argument object
         */
        getMyContactsByIndex : function(startIndex, count, args) {
        	var requestArgs = { "startIndex" : startIndex, "count" : count };
        	var options = {
            		method : "GET",
                    handleAs : "json",
                    query : lang.mixin(requestArgs , args || {format:"json"}) 
        	};
        	return this.getEntities(Consts.GetMyContacts, options, this.getProfileFeedCallbacks());
        },
        
        /**
         * Return callbacks for a profile entry
        **/
        getProfileCallbacks : function() {
            return ProfileCallbacks;
        },
        
        /**
         * Return callbacks for a profile feed
        **/
        getProfileFeedCallbacks : function() {
            return ProfileFeedCallbacks;
        },
        
        _toIdObject : function(profileOrId) {
            var idObject = {};
            if (lang.isString(profileOrId)) {
            	idObject.userid = profileOrId;
            } else if (profileOrId instanceof Profile) {
            	idObject.userid = profileOrId.getUserid();
            }
            return idObject;
        },
       
        _validateIdObject : function(idObject) {
            if (!idObject.userid) {
                return this.createBadRequestPromise("Invalid argument, userid must be specified.");
            }
        },
        _validateProfileId : function(profileId) {
            if (!profileId || profileId.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected userid");
            }
        }
   	});
	return ProfileService;
});

},
'sbt/smartcloud/Subscriber':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * 
 * @author Vimal Dhupar 
 * Definition of a Subscriber Helper for getting the Subscriber ID for a SmartCloud User.
 */
define([ "../declare", "../Endpoint" ], function(declare, Endpoint) {
	/**
	 * Subscriber Helper Class for getting the Subscriber ID for a SmartCloud User.
	 * @class Subscriber
	 * @namespace sbt.smartcloud
	 */
	var Subscriber = declare(null, {
		
		endpoint : null,
		
		/**
		 * @constructor
		 * @param endpoint
		 * @param callback
		 */
		constructor : function(endpoint, callback) {
			this.endpoint = endpoint;
			if (callback)
				this.load(callback);
		},
		
		/**
		 * Load method is responsible for making the network call to fetch the user identity
		 * @method load
		 * @param callback
		 */
		load : function(callback) {
			var _self = this;
			this.endpoint.xhrGet({
				serviceUrl : "/manage/oauth/getUserIdentity",
				loginUi : this.endpoint.loginUi,
				handleAs : "json",
				load : function(response) {
					callback(_self, response);
				},
				error : function(error) {
					callback(_self, null);
					console.log("Error fetching feed for getUserIdentity");
				}
			});
		},
		
		/**
		 * Method to get the Subscriber Id of the user.
		 * @method getSubscriberId
		 * @param response
		 * @returns
		 */
		getSubscriberId : function(response) {
			if (response && response.subscriberid) {
				return response.subscriberid;
			}
			return null;
		}
	});
	return Subscriber;
});
},
'sbt/Jsonpath':function(){
/*
 * � Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK 
 * JSONPath 0.8.0 - XPath for JSON
 * Would be replaced with JsonPath version of Github
 */
define(['./declare'],function(declare){
		return function(obj, expr, arg)
		{
			 var P = {
					 resultType: arg && arg.resultType || "VALUE",
				      result: [],
				      normalize: function(expr) {
				         var subx = [];
				         return expr.replace(/[\['](\??\(.*?\))[\]']/g, function($0,$1){return "[#"+(subx.push($1)-1)+"]";})
				                    .replace(/'?\.'?|\['?/g, ";")
				                    .replace(/;;;|;;/g, ";..;")
				                    .replace(/;$|'?\]|'$/g, "")
				                    .replace(/#([0-9]+)/g, function($0,$1){return subx[$1];});
				      },
				      asPath: function(path) {
				         var x = path.split(";"), p = "$";
				         for (var i=1,n=x.length; i<n; i++)
				            p += /^[0-9*]+$/.test(x[i]) ? ("["+x[i]+"]") : ("['"+x[i]+"']");
				         return p;
				      },
				      store: function(p, v) {
				         if (p) P.result[P.result.length] = P.resultType == "PATH" ? P.asPath(p) : v;
				         return !!p;
				      },
				      trace: function(expr, val, path) {
				         if (expr) {
				            var x = expr.split(";"), loc = x.shift();
				            x = x.join(";");
				            if (val && val.hasOwnProperty(loc))
				               P.trace(x, val[loc], path + ";" + loc);
				            else if (loc === "*")
				               P.walk(loc, x, val, path, function(m,l,x,v,p) { P.trace(m+";"+x,v,p); });
				            else if (loc === "..") {
				               P.trace(x, val, path);
				               P.walk(loc, x, val, path, function(m,l,x,v,p) { typeof v[m] === "object" && P.trace("..;"+x,v[m],p+";"+m); });
				            }
				            else if (/,/.test(loc)) { // [name1,name2,...]
				               for (var s=loc.split(/'?,'?/),i=0,n=s.length; i<n; i++)
				                  P.trace(s[i]+";"+x, val, path);
				            }
				            else if (/^\(.*?\)$/.test(loc)) // [(expr)]
				               P.trace(P.eval(loc, val, path.substr(path.lastIndexOf(";")+1))+";"+x, val, path);
				            else if (/^\?\(.*?\)$/.test(loc)) // [?(expr)]
				               P.walk(loc, x, val, path, function(m,l,x,v,p) { if (P.eval(l.replace(/^\?\((.*?)\)$/,"$1"),v[m],m)) P.trace(m+";"+x,v,p); });
				            else if (/^(-?[0-9]*):(-?[0-9]*):?([0-9]*)$/.test(loc)) // [start:end:step]  phyton slice syntax
				               P.slice(loc, x, val, path);
				         }
				         else
				            P.store(path, val);
				      },
				      walk: function(loc, expr, val, path, f) {
				         if (val instanceof Array) {
				            for (var i=0,n=val.length; i<n; i++)
				               if (i in val)
				                  f(i,loc,expr,val,path);
				         }
				         else if (typeof val === "object") {
				            for (var m in val)
				               if (val.hasOwnProperty(m))
				                  f(m,loc,expr,val,path);
				         }
				      },
				      slice: function(loc, expr, val, path) {
				         if (val instanceof Array) {
				            var len=val.length, start=0, end=len, step=1;
				            loc.replace(/^(-?[0-9]*):(-?[0-9]*):?(-?[0-9]*)$/g, function($0,$1,$2,$3){start=parseInt($1||start);end=parseInt($2||end);step=parseInt($3||step);});
				            start = (start < 0) ? Math.max(0,start+len) : Math.min(len,start);
				            end   = (end < 0)   ? Math.max(0,end+len)   : Math.min(len,end);
				            for (var i=start; i<end; i+=step)
				               P.trace(i+";"+expr, val, path);
				         }
				      },
				      eval: function(x, _v, _vname) {
				         try { return $ && _v && eval(x.replace(/@/g, "_v")); }
				         catch(e) { throw new SyntaxError("jsonPath: " + e.message + ": " + x.replace(/@/g, "_v").replace(/\^/g, "_a")); }
				      }
			 };
			 var $ = obj;
			   if (expr && obj && (P.resultType == "VALUE" || P.resultType == "PATH")) {
			      P.trace(P.normalize(expr).replace(/^\$;/,""), obj, "$");
			      return P.result.length ? P.result : false;
			   }
		};	
});
},
'sbt/base/JsonDataHandler':function(){
/*
 * � Copyright IBM Corp. 2012,2013
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
 * Helpers for the base capabilities of data handlers.
 * 
 * @module sbt.base.JsonDataHandler
 */
define(["../declare", "../lang", "../json", "./DataHandler", "../Jsonpath", "../stringUtil"], 
    function(declare, lang, json, DataHandler, jsonPath, stringUtil) {

    /**
     * JsonDataHandler class
     * 
     * @class JsonDataHandler
     * @namespace sbt.base
     */     
	var JsonDataHandler = declare(DataHandler, {	
		
        /**
         * Data type for this DataHandler is 'json'
         */
        dataType : "json",
		
        /**
         * Set of jsonpath expressions used by this handler.
         */
        jsonpath : null,
        
        /**
         * Set of values that have already been read.
         */
        _values : null,
        
	    /**
	     * @constructor
	     * @param {Object} args Arguments for this data handler.
	     */     
		constructor : function(args) {
            lang.mixin(this, args);
            this._values = {}; 
            this.data = args.data;
		},
        
        /**
         * @method getAsString
         * @param data
         * @returns
         */
        getAsString : function(property) {
        	this._validateProperty(property, "getAsString");
        	if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._get(property).toString();
                }
                return this._values[property];
            } else {
                return _get(property);
            }
        },

        /**
         * @method getAsNumber
         * @returns
         */
        getAsNumber : function(property) {
        	this._validateProperty(property, "getAsNumber");
        	if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._getNumber(property); 
                }
                return this._values[property];
            } else {
                return _getNumber(property);
            }
        },

        /**
         * @method getAsDate
         * @returns
         */
        getAsDate : function(property) {
        	this._validateProperty(property, "getAsDate");
        	if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._getDate(property); 
                }
                return this._values[property];
            } else {
                return _getDate(property);
            }
        },

        /**
         * @method getAsBoolean
         * @returns
         */
        getAsBoolean : function(property) {
        	this._validateProperty(property, "getAsBoolean");
        	if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._getBoolean(property); 
                }
                return this._values[property];
            } else {
                return _getBoolean(property);
            }                    
        },

        /**
         * @method getAsArray
         * @returns
         */
        getAsArray : function(property) {
        	this._validateProperty(property, "getAsArray");
        	if (this._values) {
                if (!this._values.hasOwnProperty(property)) {
                    this._values[property] = this._get(property);
                }
                return this._values[property];
            } else {
                return _get(property);
            }       
        },

        /**
         * @method getEntityId
         * @returns
         */
        getEntityId : function(data) {
        	return stringUtil.trim(this.getAsString(this.jsonpath["id"]));
        },

        /**
         * getEntityData
         * @returns
         */
        getEntityData : function() {            
            return this.data;
        },
        
        /**
         * @method getSummary
         * @returns
         */
        getSummary : function() {
        	if (!this._summary && this._get("totalResults")[0]) {
                this._summary = {
                	totalResults : this.getAsNumber("totalResults"),
                	startIndex : this.getAsNumber("startIndex"),
                	itemsPerPage : this.getAsNumber("itemsPerPage")
                };
            }
            return this._summary;
        },
        
        /**
         * @method getEntitiesDataArray
         * @returns {Array}
         */
        getEntitiesDataArray : function() {
        	var entityJPath = this._getJPath("entry");
        	var resultingArray = this._get(entityJPath);
        	return resultingArray[0];
        },
        
        /**
         * @method toJson
         * @returns {Object}
         */
        toJson : function() {
        	return this.data;
        },
        
        //
        // Internals
        //
        
        _getDate : function(property) {
        	var text = this._get(property)[0];
        	if(text instanceof Date) {
        		return text;
        	}
        	else {
        		return new Date(text);
        	}
        },
        
        _getBoolean : function(property) {
        	var text = this._get(property)[0];
        	return text ? true : false;
        },
        
        _getNumber : function(property) {
        	var text = this._get(property)[0];
        	// if it is a Number
        	if(typeof text === 'number')
        		return text;
        	//if its an array, we return the length of the array
        	else if(lang.isArray(text))
        		return text.length;
        	//if its a string or any other data type, we convert to number and return. Invalid data would throw an error here.
        	else return Number(text);
        },
        
        /**
         Validate that the property is valid
         **/
        _validateProperty : function(property, method) {
            if (!property) {
                var msg = stringUtil.substitute("Invalid argument for JsonDataHandler.{1} {0}", [ property, method ]);
                throw new Error(msg);
            }
        },
        
        /**
         Validate that the object is valid
         **/
        _validateObject : function(object) {
            if (!object) {
                var msg = stringUtil.substitute("Invalid argument for JsonDataHandler.{0}", [ object ]);
                throw new Error(msg);
            }
        },
        
		_evalJson: function(jsonQuery){
			return jsonPath(this.data,jsonQuery);			
		},
	
		_evalJsonWithData: function(jsonQuery, data){
			return jsonPath(data,jsonQuery);			
		},
		
		_get: function(property){

			this._validateObject(this.data);
			var jsonQuery = this._getJPath(property);

			var result = this._evalJson(jsonQuery);
			return result;
		},
		
		_getJPath: function(property) {
			return this.jsonpath[property] || property;
		},
		
		extractFirstElement: function(result){
			return this._evalJsonWithData("$[0]", result);
		}
	});
	return JsonDataHandler;
});
},
'sbt/connections/controls/activities/nls/ActivityGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
    root: {
    	updatedBy: "Updated by",
    	modified: "Last Updated",
    	dueDate: "Due Date",
    	name: "Name",
        feed : "Feed for these Activities"
    }
});
},
'sbt/connections/controls/bookmarks/nls/BookmarkGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
    root: {
    	tags : "Tags: ",
        date: "Date",
        popularity: "Popularity",
        feed : "Feed for these Bookmarks"
    }
});
},
'sbt/connections/controls/communities/nls/CommunityGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
    root: {
        summary : "Public Communities",
        filterByTag : "Click to filter by tag",
        updatedBy : "Updated by ",
        person : "1 person",
        people : "{memberCount} people",
        tags : "Tags: ",
        date: "Date",
        popularity: "Popularity",
        name: "Name",
        ariaVcard: "Press Control And Enter For Business Card",
        feed : "Feed for these Communities",
        restricted : "Restricted"
    }
});

},
'sbt/connections/controls/communities/nls/CommunityMembersGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
  root: {
	  summary : "Profiles Grid",
      updatedBy : "Updated by ",
      displayName: "Display Name",
      recent: "Recent",
      telephone: "Telephone:",
      email: "Email:",
      altEmail: "Alternate Email",
      clickToEmail: "Click here to email the user, using their alternate email address",
      ariaVcard: "Press control and enter for the business card",
      role: "Role",
      created: "Created",
      edit: "Edit",
      remove: "Remove",
      save: "Save",
      cancel: "Close",
      member: "Member",
      owner: "Owner"
   }
});

  
},
'sbt/connections/controls/files/nls/FileGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
  root: {
      updatedBy : "Updated by",
      name: "Name",
      updated: "Updated",
      downloads: "Downloads",
      comments: "Comments",
      likes: "Likes",
      noLikes: "No one likes this",
      oneLike: "1 person likes this",
      nLikes: "{recommendationCount} people like this",
      downloads: "Downloads",
      download: "Download",
      more: "More",
      created: "Created",
      pin: "Pin This Item",
      commented: "Commented",
      _delete: "Delete",
      edit: "Edit",
      files:"Files",
      ariaCheckbox: "checkbox",
      ariaVcard: "ariaCheckbox",
      feed : "Feed for these Files"
  }
});

},
'sbt/connections/controls/forums/nls/ForumGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
    root: {
    	forums: "Forums",
    	forum: "Forum",
    	threads: "Topics",
    	latestPost: "Latest Post",
    	goToForum: "Go To The Forum",
    	viewProfile: "View Profile",
    	replies: "Replies",
    	topics: "Topic",
    	topic: "Topic",
    	author: "Author",
    	date: "Date",
    	back:"Back",
    	noResults: "There are no results",
        feed : "Feed for these Forums"
    }
});
},
'sbt/connections/controls/profiles/nls/ColleagueGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
  root: {
	  viewAll : "View All ({totalCount})",
	  empty : "No colleagues in the list.",
	  people: "{totalCount} people",
	  person: "{totalCount} person"
  }
});

  
},
'sbt/connections/controls/profiles/nls/ProfileGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
  root: {
	  summary : "Profiles Grid",
      updatedBy : "Updated by ",
      displayName: "Display Name",
      recent: "Recent",
      telephone: "Telephone:",
      email: "Email:",
      altEmail: "Alternate Email",
      clickToEmail: "Click here to email the user, using their alternate email address",
      ariaVcard: "Press control and enter for the business card",
      role: "Role",
      created: "Created",
      feed : "Feed for these Profiles"
  }
});

  
},
'sbt/connections/controls/profiles/nls/ProfileTagsGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
  root: {
	  taggedByPerson : "Tagged by 1 person:",
	  taggedByPeople : "Tagged by {numberOfContributors} people:",
	  taggedByMe     : "My tags for this profile:",
	  taggedBy       : "Tagged by {tagSource}:"
  }
});

  
},
'sbt/connections/controls/search/nls/SearchBoxRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
    root: {
        selectedApplication: "All Connections",
        allConnections: "All Connections",
        statusUpdates: "Status Updates",
        activities: "Activities",
        blogs: "Blogs",
        bookmarks: "Bookmarks",
        communities: "Communities",
        files: "Files",
        forums: "Forums",
        profiles: "Profiles",
        wikis: "Wikis",
        refine: "Refine Search Results",
        search: "Search",
        allConnectionsIcon: "All Connections Icon",
        icon: "Icon"
    }
});

},
'sbt/connections/controls/search/nls/SearchGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
  root: {
      name: "Name",
      updated: "Updated",
      downloads: "Downloads",
      likes: "Likes",
      loading : "Loading...", 
      previous : "Previous",
      previousPage : "Previous Page",
      next : "Next",
      nextPage : "Next Page",
      pagingResults : "${start} - ${count} of ${totalCount}",
      updatedBy : "Updated by",
      sortByRelevance : "Relevance",
      sortByDate: "Date",
      feed : "Feed for these search results",
      
      
      /**
       * Search specific Misc
       */
      empty : "No results were found for that search", 
      summary : "Search Results",
      emptyString: "",
      ltr: "ltr",
      help: "Help",
      tags: "Tags:",
      tagsMore: "and {0} more",
      noDescription: "No description or summary for this result.",
      members: "members",
      people: "People",
      feed: "feed",
      commentOn: "Comment on: ",
      comments: "comments",
      comment: "Comment:",
      oneComment: "1 comment",
      entry: "Entry: ",
      topic: "Topic:",
      
      /**
       * Activity-Related
       */
      activities: "Activities",
      activitiesTitle: "Activities",
      activity: "Activity: ",
      activityComment: "Comment:",
      activityToDo:"To Do:",
      activityEntry: "Activity:",
      activityBookmark: "Bookmark:",
      activitySection: "Section:",
      fromAnActivity: "Activity",
      
      /**
       * Blog-Related
       */
      blog: "Blog: ",
      blogs: "Blogs",
      blogEntry: "Blog: ",
      blogComment: "Comment:",
      idea: "Idea: ",
      ideaComment: "Comment: ",
      ideationBlog: "Ideation Blog: ",
      fromABlog: "Blog",
      fromAnIdeationBlog: "Ideation Blog",
      
      /**
       * Bookmark-Related
       */
      bookmarks: "Bookmarks",
      bookmark: "Bookmark: ",
      bookmarksTitle: "Bookmarks",
      
      /**
       * Calendar-Related
       */
      calendar: "Calendar: ",
      calendars: "Calendars",
      eventIsAllDay: "All day",
      eventRepeats: "Repeats",
      eventDateOn: "On: ",
      
      /**
       * CommunityRelated
       */
      communities: "Communities",
      community: "Community: ",
      communitiesTitle: "Communities",
      fromACommunity: "Community",
      
      /**
       * File-Related
       */
      file: "File: ",
      files: "Files",
      
      /**
       * Forum-Related
       */
      forum: "Forum: ",
      forums: "Forums",
      forumTopic: "Topic:",
      forumCategory: "Category:",
      fromAForum: "Forum",
      
      /**
       * Profile-Related
       */
      profile: "Profile: ",
      profiles: "Profiles",
      inactiveLabel: "(inactive)",
      
      /**
       * StatusUpdate-Related
       */
      statusUpdate: "Status Update: ",
      statusUpdates: "Status updates",
      fromAStatusUpdate: "Status Update",
      
      /**
       * Wiki-Related
       */
      wiki: "Wiki: ",
      wikis: "Wikis",
      wikiType: "Wiki: ",
      wikiFile: "File: ",
      wikiPage: "Page: ",
      fromAWiki: "Wiki"
  }
});
            

},
'sbt/smartcloud/controls/profiles/nls/ProfileGridRenderer':function(){
/*
 * � Copyright IBM Corp. 2013
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

// NLS_CHARSET=UTF-8
define({
  root: {
	  summary : "Profiles Grid",
      updatedBy : "Updated by ",
      displayName: "Display Name",
      recent: "Recent",
      telephone: "Telephone:",
      email: "Email:",
      altEmail: "Alternate Email",
      clickToEmail: "Click here to email the user, using their alternate email address",
      ariaVcard: "Press control and enter for the business card"
  }
});

  
}}});
define([], 1);
