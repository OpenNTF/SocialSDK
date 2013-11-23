require(["sbt/dom", "sbt/connections/controls/astream/ActivityStreamWrapper"], function(dom, ActivityStreamWrapper) {
    var activityStreamWrapper = new ActivityStreamWrapper({
        feedUrl : "/basic/rest/activitystreams/urn:lsid:lconn.ibm.com:communities.community:%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}/@all/@all?rollup=true",
        activityStreamNode : "activityStream",
        shareBoxNode: "inputForm"
    });

    dom.byId("activityStreamDiv").appendChild(activityStreamWrapper.domNode);
    activityStreamWrapper.startup();
});