require(["sbt/dom", "sbt/config", "sbt/connections/controls/astream/ActivityStreamWrapper"], function(dom, config, ActivityStreamWrapper) {
    config.Properties["loginUi"] = "popup";
    var asConfig = {
            urlTemplate : "/connections/opensocial/rest/activitystreams/@me/@involved/${appId}",
            defaultUrlTemplateValues : {
                userId : "@me",
                groupId : "@all",
                appId : "@all",
                rollup : "true"
            },
            views : {
                simpleView : {
                    filters : {
                        label : "Show:",
                        options : {
                            all : {
                                params : {
                                    appId : "@all",
                                },
                                label : "All Types"
                            },
                            communities : {
                                params : {
                                    appId : "communities",
                                },
                                label : "Communities"
                            },
                            profiles : {
                                params : {
                                    appId : "profiles"
                                },
                                label : "Profiles"
                            }
                        }
                    }
                }
            }
        };
    
    var activityStreamWrapper = new ActivityStreamWrapper({
        config : asConfig,
        activityStreamNode: "activityStream",
        shareBoxNode : "inputForm",
        sideNavNode : "sideNav"
    });
    
    dom.byId("activityStreamDiv").appendChild(activityStreamWrapper.domNode);
    activityStreamWrapper.startup();
});
