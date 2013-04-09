require(["sbt/dom", "sbt/controls/vcard/connections/CommunityVCard"], function(dom, CommunityVCard) {
    var communityCard = new CommunityVCard({
        name : "testCommunity",
        uuid : "2c508289-ab76-4dc4-b5e5-bd7c75d66601",
        selectedWidgetId : ""
    });

    dom.byId("cardDiv").appendChild(communityCard.domNode);
});
