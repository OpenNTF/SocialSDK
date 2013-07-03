require(["sbt/dom", "sbt/connections/controls/astream/ActivityStreamWrapper"], function(dom, ActivityStreamWrapper) {
    var config = {
        urlTemplate : "/connections/opensocial/rest/activitystreams/@me/@all/${appId}",
        defaultUrlTemplateValues : {
            userId : "@me",
            groupId : "@all",
            appId : "@all",
            rollup : "true"
        },
        views : {
            view1 : {
                label : "View 1",
                filters : {
                    label : "Filter 1:",
                    options : {
                        all : {
                            params : {
                                appId : "@all",
                            },
                            label : "All 1"
                        },
                        blogs : {
                            params : {
                                appId : "activities",
                            },
                            label : "Activities"
                        },
                        profiles : {
                            params : {
                                appId : "files"
                            },
                            label : "Files"
                        }
                    }
                }
            },
            
            view2 : {
                label : "View 2",
                filters : {
                    label : "Filter 2:",
                    options : {
                        all : {
                            params : {
                                appId : "@all",
                            },
                            label : "All 2"
                        },
                        blogs : {
                            params : {
                                appId : "blogs",
                            },
                            label : "Blogs"
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
        config : config,
        activityStreamNode: "activityStream",
        shareBoxNode : "inputForm",
        sideNavNode : "sideNav"
    });
    
    dom.byId("activityStreamDiv").appendChild(activityStreamWrapper.domNode);
    activityStreamWrapper.startup();
});
