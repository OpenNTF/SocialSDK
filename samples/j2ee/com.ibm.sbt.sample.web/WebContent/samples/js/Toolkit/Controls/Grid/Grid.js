require(["sbt/controls/grid/Grid", 
         "sbt/controls/grid/GridRenderer", 
         "sbt/config",
         "sbt/dom"],
        function(Grid, GridRenderer, sbt, dom) {

			var xpath_community = {
				"entry"				:"/a:entry",
				"communityUuid"		:"snx:communityUuid",
				"uid"				:"snx:communityUuid",
				"title"				:"a:title",
				"summary"			:"a:summary[@type='text']",
				"communityUrl"      :"a:link[@rel='alternate']/@href",
				"communityFeedUrl"  :"a:link[@rel='self']/@href",
				"logoUrl"			:"a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href",
				"tags"				:"a:category/@term",
				"content"			:"a:content[@type='html']",
		        "memberCount"       :"snx:membercount",
		        "communityType"     :"snx:communityType",
		        "published"         :"a:published",
		        "updated"           :"a:updated",
		        "authorUid"			:"a:author/snx:userid",
		        "authorName"		:"a:author/a:name",
		        "authorEmail"		:"a:author/a:email",
				"contributorUid"	:"a:contributor/snx:userid",
				"contributorName"	:"a:contributor/a:name",
				"contributorEmail"	:"a:contributor/a:email"				
			};
         
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
                        "href": "javascript:;"
                     }, h4);
                     a.appendChild(dom.createTextNode(this.getValue(item, "title")));
                     var div = dom.create("div", {
                        "class": "lotusMeta",
                     }, td);
                     var ul = dom.create("ul", {
                        "class": "lotusInlinelist",
                     }, div);
                     
                     var liUpdatedBy = dom.create("li", {}, ul);
                     liUpdatedBy.appendChild(dom.createTextNode("Updated By "));
                     
                     var aUpdatedBy = dom.create("a", {
                        "class": "lotusPerson"
                     }, liUpdatedBy);
                     aUpdatedBy.appendChild(dom.createTextNode(this.getValue(item, "contributorName")));
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
                    url : "/communities/service/atom/communities/all",
                    attributes : xpath_community
                },
                renderer : gridRenderer
            });
             
            dom.byId("gridDiv").appendChild(grid.domNode);
             
            grid.update();
        }
);