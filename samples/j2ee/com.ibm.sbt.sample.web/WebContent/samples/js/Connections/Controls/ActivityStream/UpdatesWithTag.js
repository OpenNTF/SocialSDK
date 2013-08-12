require(["sbt/dom", "sbt/connections/controls/astream/ActivityStreamWrapper"], function(dom, ActivityStreamWrapper) {
    var activityStreamWrapper = new ActivityStreamWrapper({
        feedUrl : "/basic/rest/activitystreams/@me/@all/@tags?rollup=true&filterBy=tag&filterOp=equals&filterValue=test",
        activityStreamNode : "activityStream",
        shareBoxNode: "inputForm",
        extensions: {
            commenting: true,
            saving: true,
            refreshButton: true,
            deleteButton: true
        }
    });

    dom.byId("activityStreamDiv").appendChild(activityStreamWrapper.domNode);
    activityStreamWrapper.startup();
});