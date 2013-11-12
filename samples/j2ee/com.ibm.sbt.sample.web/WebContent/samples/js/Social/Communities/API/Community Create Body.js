require([ "sbt/dom", "sbt/json", "sbt/stringUtil", "sbt/connections/CommunityConstants", "sbt/connections/CommunityService"], 
    function(dom,json,stringUtil,consts,CommunityService) {
        try {
            var domNode = dom.byId("communityTmpl");
            var CommunityTmpl = domNode.text || domNode.textContent;
            domNode = dom.byId("categoryTmpl");
            var CategoryTmpl = domNode.text || domNode.textContent;
            
            var results = [];
            
            var transformer = function(value, key) {
                if (key == "getTags") {
                    var tags = value;
                    value = "";
                    for (var tag in tags) {
                        value += stringUtil.transform(CategoryTmpl, { "tag" : tags[tag] });
                    }
                }
                return value;
            };
            
            var communityJson = {
                getCommunityType : consts.Public,
                getTitle : "Community Title",
                getContent : "Community Content",
                getTags : [ "tag1", "tag2", "tag3" ]
            };
            
            var requestBody = stringUtil.transform(CommunityTmpl, communityJson, transformer, this);
            results.push({ "requestBody" : requestBody });
            
            var communityService = new CommunityService();
            var community = communityService.newCommunity();
            community.setCommunityType(consts.Public);
            community.setTitle("Community Title");
            community.setContent("Community Content");
            community.setTags([ "tag1", "tag2", "tag3" ]);
            
            requestBody = stringUtil.transform(CommunityTmpl, community, transformer, community);
            results.push({ "requestBody" : requestBody });
            
            dom.setText("json", json.jsonBeanStringify(results));
        } catch (error) {
            dom.setText("json", error.message);
        }
    }
);
