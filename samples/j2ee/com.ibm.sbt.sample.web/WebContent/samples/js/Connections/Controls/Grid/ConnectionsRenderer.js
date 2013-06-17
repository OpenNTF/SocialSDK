require(["sbt/controls/grid/Grid", 
         "sbt/connections/controls/ConnectionsGridRenderer", 
         "sbt/config",
         "sbt/i18n",
         "sbt/dom", 
         "sbt/store/parameter",
         "sbt/text!sbt/connections/controls/communities/templates/CommunityRow.html"],
        function(Grid, ConnectionsGridRenderer, sbt, i18n, dom, parameter, CommunityRow) {
    
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
     
		var gridRenderer = new ConnectionsGridRenderer({
            _nls: { 
                summary : "Public Communities",
                updatedBy : "Updated by "
             },
             template: CommunityRow,
             rowClass: function(grid, item, i, items) {
                 return (i === 0 ? "placeRow lotusFirst" : "placeRow");
             },
             numOfMembers: function(grid, item, i, items) {
                 return this.getValue(item, "memberCount");
             },
             updatedDate: function(grid, item, i, items) {
                 return i18n.getUpdatedLabel(this.getValue(item, "updated"));
             },
             displayTags: function(grid, item, i, items) {
                 return "display: none";
             },
             tagsLabel: function(grid, item, i, items) {
                 return "";
             },
             tagsAnchors: function(grid, item, i, items) {
                 return "";
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
                attributes : xpath_community,
                paramSchema : ParamSchema
            },
            renderer : gridRenderer
        });
         
        dom.byId("gridDiv").appendChild(grid.domNode);
         
        grid.update();
    }
);