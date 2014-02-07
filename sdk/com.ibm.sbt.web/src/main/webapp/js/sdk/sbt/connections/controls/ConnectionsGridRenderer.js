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
define([ "../../declare", "../../controls/grid/GridRenderer",
        "../../text!../../controls/grid/templates/GridPager.html", 
        "../../text!../../controls/grid/bootstrap/templates/GridPager.html", 
        "../../text!../../controls/grid/templates/GridSorter.html",
        "../../text!../../controls/grid/bootstrap/templates/GridSorter.html",
        "../../text!../../controls/grid/templates/SortAnchor.html",
        "../../text!../../controls/grid/bootstrap/templates/SortAnchor.html",
        "../../i18n!./nls/ConnectionsGridRenderer",
        "../../text!../../controls/grid/templates/GridFooter.html",
        "../../text!../../controls/grid/bootstrap/templates/GridFooter.html",

        "../../lang"],
        function(declare, GridRenderer, GridPager,BootstrapGridPager, 
        		GridSorter,BootstrapGridSorter, SortAnchor,BootstrapSortAnchor, 
        		nls, GridFooter,BootstrapGridFooter,lang) {

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
        /**The bootstrap pager template*/
        bootstrapPagerTemplate: BootstrapGridPager,
        /**The HTML template to use to show the grid footer */
        footerTemplate : GridFooter,
        /**The bootstrap footer template*/
        bootstrapFooterTemplate: BootstrapGridFooter,
        /**The HTML template to show sorting options*/
        sortTemplate : GridSorter,
        /**The bootstrap sorter template*/
        bootstrapSortTemplate: BootstrapGridSorter,
        /**The HTML template for sort Anchors*/
        sortAnchor : SortAnchor,
        /**The bootstrap html sorting anchor template */
        bootstrapSortAnchor: BootstrapSortAnchor,
        
        /**
         * Merge this class with the GridRenderer Class
         * @method constructor
         * @param args
         */
        constructor : function(args) {
        	if(!this.grid.tableClass){
        		if(this.grid.theme == "bootstrap"){
        			this.tableClass = "table";
        		}
        	}
            lang.mixin(this, args);
            this.nls = lang.mixin(nls, this._nls);
        }

    });

    return ConnectionsGridRenderer;
});