require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "communityMembers",
        communityUuid : "a10967bb-d850-4a91-8230-cc78e4208bf5"
    });

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});


