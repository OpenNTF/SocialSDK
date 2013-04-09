require(["dojox/grid/EnhancedGrid", 
         "dojox/grid/enhanced/plugins/Pagination", 
         "dojo/data/ObjectStore", 
         "sbt/store/AtomStore",
         "sbt/store/parameter",
         "sbt/dom",
         "sbt/connections/CommunityConstants"],
function(EnhancedGrid, Pagination, ObjectStore, AtomStore, parameter, dom, communityConstants) {
    var attributes = {
        "communityUuid" : "snx:communityUuid",
        "title" : "a:title",
        "summary" : "a:summary[@type='text']",
        "communityUrl" : "a:link[@rel='self']/@href",
        "logoUrl" : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href",
        "tags" : "a:category/@term",
        "content" : "a:content[@type='html']",
        "memberCount" : "snx:membercount",
        "communityType" : "snx:communityType",
        "published" : "a:published",
        "updated" : "a:updated",
        "authorUid" : "a:author/snx:userid",
        "contributorUid" : "a:contributor/snx:userid"
    };

    var options = {
        url : "/communities/service/atom/communities/all",
        attributes : communityConstants.xpath_community,
        feedXPath: communityConstants.xpath_feed_community,
        paramSchema: parameter.communities.all
    };
    
    var atomStore = new AtomStore(options);
    var dataStore = ObjectStore({objectStore: atomStore});

    var communitiesLayout = [ {
        name : "Logo",
        field : "modified",
        fields : [ "logoUrl" ],
        editable : false,
        navigable : false,
        width : "10%",
        formatter : function(fields) {
            var logoUrl = fields[0];
            return "<img src='" + logoUrl +"'width=64 height=64></img>";
        }
    }, {
        name : "Details",
        field: "title",
        fields : [ "title", "communityUrl", "summary", "memberCount", "updated", "tags" ],
        editable : false,
        navigable : true,
        width : "90%",
        formatter : function(fields) {
            var title = fields[0];
            var communityUrl = fields[1];
            var summary = fields[2];
            var memberCount = fields[3];
            var updated = fields[4];
            var tags = fields[5];
            return "<b>" + title + "</b><br/>" + memberCount + " people | " + updated + " | Tags: " + tags + "<br/>" + summary;
        }
    } ];

    var grid = new EnhancedGrid({
        id : 'grid',
        autowidth : 'true',
        autoHeight : 'true',
        store : dataStore,
        structure : communitiesLayout,
        updateDelay: 4000,
        plugins : {
            pagination : {
                pageSizes : [ "5", "10", "100", "All" ],
                defaultPageSize : 5,
                description : true,
                sizeSwitch : true,
                pageStepper : true,
                gotoButton : true,
                position : "top"
            }
        }
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.startup();
});