require(["sbt/dom", "sbt/connections/controls/vcard/CommunityVCard"], function(dom, CommunityVCard) {
    var communityCard = new CommunityVCard({
        name : "",
        uuid : "%{sample.communityId}",
        selectedWidgetId : ""
    });

    dom.byId("cardDiv").appendChild(communityCard.domNode);
});
