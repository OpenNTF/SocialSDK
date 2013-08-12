require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], function(CommunityService, dom, json) {
    try {
        var communityService = new CommunityService();
        var communityUuid = "%{sample.communityId}";
        var id = "%{sample.id2}";
        var promise = communityService.addMember(communityUuid, id);
        promise.then(
            function(member) {
                // member must be loaded if access to
                // the full data is required
                member.load().then(
                    function(member) {
                        dom.setText("json", json.jsonBeanStringify(member));
                    },
                    function(error) {
                        dom.setText("json", json.jsonBeanStringify(error));
                    }
                );
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    } catch (err) {
        dom.setText("json", json.jsonBeanStringify(err));
    }
});
