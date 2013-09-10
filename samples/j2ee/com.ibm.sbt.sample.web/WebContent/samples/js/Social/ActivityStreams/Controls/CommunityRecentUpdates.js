require(["sbt/dom", "sbt/connections/controls/astream/ActivityStreamWrapper"], function(dom, ActivityStreamWrapper) {
    var activityStreamWrapper = new ActivityStreamWrapper({
        feedUrl : "/basic/rest/activitystreams/urn:lsid:lconn.ibm.com:communities.community:%{name=sample.communityId}/@all/@all?rollup=true",
        activityStreamNode : "activityStream",
        shareBoxNode: "inputForm"
    });

    dom.byId("activityStreamDiv").appendChild(activityStreamWrapper.domNode);
    activityStreamWrapper.startup();
});