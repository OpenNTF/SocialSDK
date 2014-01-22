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