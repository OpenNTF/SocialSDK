require(["sbt/dom", "sbt/connections/controls/vcard/CommunityVCard"], function(dom, CommunityVCard) {
    var communityCard = new CommunityVCard({
        name : "",
        uuid : "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}",
        selectedWidgetId : ""
    });

    dom.byId("cardDiv").appendChild(communityCard.domNode);
});
