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
        "../../../stringUtil", 
        "../../../i18n",
        "../ConnectionsGridRenderer",
        "../../../i18n!sbt/connections/controls/files/nls/FileGridRenderer",
        "../../../text!sbt/connections/controls/files/templates/FileRow.html",
        "../../../text!sbt/connections/controls/files/templates/RecycledFileRow.html",
        "../../../text!sbt/connections/controls/files/templates/FolderRow.html", 
        "../../../text!sbt/connections/controls/files/templates/CommentRow.html"], 
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
        	
        }

       
    });
    
    return FileGridRenderer;
});