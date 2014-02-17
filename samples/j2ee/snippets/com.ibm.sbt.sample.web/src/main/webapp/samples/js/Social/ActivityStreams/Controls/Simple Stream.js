require(["sbt/dom", "sbt/config", "sbt/connections/controls/astream/ActivityStreamWrapper"], function(dom, config, ActivityStreamWrapper) {
    var activityStreamWrapper = new ActivityStreamWrapper({
        feedUrl: "/basic/rest/activitystreams/@me/@all/@all?rollup=true"
    });
    
    dom.byId("activityStreamDiv").appendChild(activityStreamWrapper.domNode);
    activityStreamWrapper.startup();
});
