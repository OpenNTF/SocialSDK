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

define(["../../../declare",
        "../ConnectionsGridRenderer",
        "../../../i18n",
        "../../../dom",
        "../../../lang",
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

    function(declare, ConnectionsGridRenderer, i18n, dom, lang, ForumRow, tableHeader, TopicRow, 
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
                       dom.destroy(el.childNodes[0]);
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
                        if (lang.isString(this.headerTemplate)) {
                            var domStr = this._substituteItems(this.headerTemplate, grid, this, items, data);
                            node = dom.toDom(domStr, el.ownerDocument);
                        } else {
                            node = this.headerTemplate.cloneNode(true);
                        }
                        tbody.appendChild(node);
                        
                        grid._doAttachEvents(tbody, data);
                    }
                },
                
                renderBreadCrumb: function(grid,el,items,data) {
                    if (this.breadCrumb && !grid.hideBreadCrumb) {
                        var node;
                        if (lang.isString(this.breadCrumb)) {
                            var domStr = this._substituteItems(this.breadCrumb, grid, this, items, data);
                            node = dom.toDom(domStr, el.ownerDocument);
                        } else {
                            node = this.breadCrumb.cloneNode(true);
                        }
                        el.appendChild(node);
                        
                        grid._doAttachEvents(el, data);
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
                    var table = dom.create("table", {
                        "class": this.tableClass,
                        border:"0",
                        cellspacing:"0",
                        cellpadding:"0",
                        role:"presentation"
                    }, el);
                    var tbody = dom.create("tbody", null, table);
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
                       dom.destroy(el.childNodes[0]);
                   }
                    
                   this.renderBreadCrumb(grid, el, data );
                   
                   var ediv = dom.create("div", {
                             "class": this.emptyClass,
                             innerHTML: "<h2>" + this.nls.noResults +"</h2>",
                             role: "document",
                             tabIndex: 0
                           }, el, "only");
                   
                   grid._doAttachEvents(el, data);
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