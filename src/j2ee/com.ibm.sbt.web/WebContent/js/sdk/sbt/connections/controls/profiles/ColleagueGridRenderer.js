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
define(["../../../declare", "dojo/string",
        "../../../connections/controls/ConnectionsGridRenderer",
        "../../../i18n!sbt/connections/controls/profiles/nls/ColleagueGridRenderer",
        "../../../text!sbt/connections/controls/profiles/templates/ColleagueItem.html",
        "../../../text!sbt/connections/controls/profiles/templates/ViewAll.html",
        "../../../text!sbt/connections/controls/profiles/templates/ColleagueItemFull.html"], 
        function(declare, string, ConnectionsGridRenderer, nls, colleagueItemTemplate, viewAllTemplate, colleagueItemFullTemplate) {
		
    /**
     * @class ColleagueGridRenderer
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ColleagueGridRenderer
     */
    var ColleagueGridRenderer = declare(ConnectionsGridRenderer, {

         _nls: nls,
         
         hideViewAll: false,
         
         containerClass: "lotusChunk",
         spacerClass: "lotusClear",
         
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
              var div = this._create("div", {}, el);
              this._create("div", { "class": this.containerClass }, div);
              return div;
          },
          
          renderViewAll: function(grid, el, items, data) {          
              if (this.viewAllTemplate && !this.hideViewAll) {
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
              var peopleCount = data.count !== 1 ? data.count + nls.people : data.count + nls.person;
              var sub = {
                  totalCount: peopleCount
              };
              return string.substitute(nls.viewAll, sub);
          }
    });   
    return ColleagueGridRenderer;
});