require(["sbt/dom", "sbt/config", "sbt/connections/controls/astream/ActivityStreamWrapper"], function(dom, config, ActivityStreamWrapper) {
    var activityStreamWrapper = new ActivityStreamWrapper({
        feedUrl: "/basic/rest/activitystreams/@public/@all/@all?rollup=true",
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
