require(["sbt/controls/grid/Grid", 
         "sbt/controls/grid/GridRenderer", 
         "sbt/config",
         "sbt/dom", 
         "sbt/connections/CommunityConstants"],
        function(Grid, GridRenderer, sbt, dom) {

            var gridRenderer = new GridRenderer({
                 nls: { 
                    empty : "Empty", 
                    loading : "Loading...", 
                    previous : "Previous",
                    previousPage : "Previous Page",
                    next : "Next",
                    nextPage : "Next Page",
                    pagingResults : "${start} - ${count} of ${totalCount}",
                    summary : "Public Communities"
                 },
                 tableClass: "lotusTable",
                 emptyClass: "lconnEmpty",
                 errorClass: "lconnEmpty",
                 loadingClass: "",
                 loadingImgClass: "lotusLoading",
                 msgNoData: "Please wait...",
                 renderItem: function(grid, el, data, item, i, items) {
                     var tr = dom.create("tr", {
                        "class": (i === 0 ? "lotusFirst" : (i % 2 === 1 ? "lotusAltRow" : null))
                     }, el);
                     // community logo
                     var td = dom.create("td", {
                        "class": "lotusFirstCell",
                        width: "68",
                     }, tr);
                     var img = dom.create("img", {
                        "class": "lotusFirstCell",
                        width: "64",
                        height: "64",
                        alt: "",
                        src: this.getValue(item, "logoUrl")
                     }, td);
                     // community details
                     var td = dom.create("td", {
                        "class": "lotusLastCell",
                     }, tr);
                     var h4 = dom.create("h4", {
                        "class": "lotusTitle",
                     }, td);
                     var a = dom.create("a", {
                        "href": "javascript:;",
                        innerHTML: this.getValue(item, "title")
                     }, h4);
                     var div = dom.create("div", {
                        "class": "lotusMeta",
                     }, td);
                     var ul = dom.create("ul", {
                        "class": "lotusInlinelist",
                     }, div);
                    // var liPeople = dom.create("ul", {
                    //    "class": "lotusInlinelist",
                    // }, ul);
                     var liUpdatedBy = dom.create("li", {
                        innerHTML: "Updated by "
                     }, ul);
                     var aUpdatedBy = dom.create("a", {
                        "class": "lotusPerson",
                        innerHTML: this.getValue(item, "contributorName")
                     }, liUpdatedBy);
                    // var liUpdatedWhen = dom.create("ul", {
                      //  innerHTML: this.getValue(item, "updated")
                    // }, ul);
                 },
                 getValue: function(item, name) {
                     if (item.hasOwnProperty(name)) {
                         return item[name];
                     } else {
                         return item.getValue(name);
                     }
                 }
            });
             
            var grid = new Grid({
                storeArgs : {
                    url : sbt.connections.communitiesUrls.allCommunities,
                    attributes : sbt.connections.communityConstants.xpath_community
                },
                renderer : gridRenderer
            });
             
            dom.byId("gridDiv").appendChild(grid.domNode);
             
            grid.update();
        }
);