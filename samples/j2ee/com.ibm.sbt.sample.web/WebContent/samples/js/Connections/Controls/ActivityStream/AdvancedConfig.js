require(["sbt/dom", "sbt/config", "sbt/connections/controls/astream/ActivityStreamWrapper"], function(dom, config, ActivityStreamWrapper) {
    config.Properties["loginUi"] = "popup";
    var asConfig = {
        defaultUrlTemplateValues : {
            userId : "@me",
            groupId : "@all",
            appId : "@all"
        },
        views : {
            imFollowing : {
                label : "I'm Following",
                params : {
                    userId : "@me",
                    rollup : "true"
                },
                extensions : [
                    "lconn.homepage.as.extension.SavedActionExtension",
                    // "lconn.homepage.as.extension.UnfollowExtension",
                    "com.ibm.social.as.lconn.extension.MicroblogDeletionExtension" 
                ],
                filters : {
                    label : "Filter By:",
                    options : {
                        all : {
                            label : "All",
                            params : {
                                appId : "@all"
                            },
                            extensions : [ "com.ibm.social.as.lconn.extension.ShareboxStatusUpdateExtension" ],
                            selected : true
                        },
                        statusUpdates : {
                            label : "Status Updates",
                            params : {
                                appId : "@status"
                            },
                            extensions : [ "com.ibm.social.as.lconn.extension.ShareboxStatusUpdateExtension" ]
                        },
                        activities : {
                            label : "Activities",
                            params : {
                                appId : "activities"
                            }
                        },
                        blogs : {
                            label : "Blogs",
                            params : {
                                appId : "blogs"
                            }
                        },
                        bookmarks : {
                            label : "Bookmarks",
                            params : {
                                appId : "bookmarks"
                            }
                        },
                        communities : {
                            label : "Communities",
                            params : {
                                appId : "@communities"
                            }
                        },
                        files : {
                            label : "Files",
                            params : {
                                appId : "files"
                            }
                        },
                        forums : {
                            label : "Forums",
                            params : {
                                appId : "forums"
                            }
                        },
                        people : {
                            label : "People",
                            params : {
                                appId : "@people"
                            }
                        },
                        wikis : {
                            label : "Wikis",
                            params : {
                                appId : "wikis"
                            }
                        },
                        tags : {
                            label : "Tags",
                            params : {
                                appId : "@tags"
                            },
                        // extensions : [
                        // "lconn.homepage.as.extension.TagManagerExtension"
                        // ]
                        }
                    }
                },
                selected : true
            },
            statusUpdates : {
                label : "Status Updates",
                params : {
                    userId : "@me",
                    rollup : "true"
                },
                extensions : [
                        "lconn.homepage.as.extension.SavedActionExtension",
                        "com.ibm.social.as.lconn.extension.MicroblogDeletionExtension" 
                ],
                filters : {
                    label : "Filter By:",
                    options : {
                        all : {
                            label : "All",
                            params : {
                                groupId : "@all",
                                appId : "@status"
                            },
                            extensions : [
                            // "lconn.homepage.as.extension.UnfollowExtension",
                            "com.ibm.social.as.lconn.extension.ShareboxStatusUpdateExtension" ]
                        },
                        networkAndFollow : {
                            label : "My Network and People I Follow",
                            params : {
                                groupId : "@following&@friends",
                                appId : "@status"
                            }
                        },
                        myNetwork : {
                            label : "My Network",
                            params : {
                                groupId : "@friends",
                                appId : "@status"
                            }
                        },
                        people : {
                            label : "People I Follow",
                            params : {
                                groupId : "@following",
                                appId : "@status"
                            },
                        // extensions : [
                        // "lconn.homepage.as.extension.UnfollowExtension"
                        // ]
                        },
                        myUpdates : {
                            label : "My Updates",
                            params : {
                                groupId : "@self",
                                appId : "@status"
                            },
                            extensions : [ "com.ibm.social.as.lconn.extension.ShareboxStatusUpdateExtension" ]
                        },
                        communities : {
                            label : "Communities",
                            params : {
                                groupId : "@all",
                                appId : "communities",
                                broadcast : "true"
                            },
                        // extensions : [
                        // "lconn.homepage.as.extension.UnfollowExtension"
                        // ]
                        }
                    }
                }
            },
            myNotifications : {
                label : "My Notifications",
                params : {
                    userId : "@me"
                },
                extensions : [
                        "lconn.homepage.as.extension.SavedActionExtension",
                        "lconn.homepage.as.extension.ActivityReplyExtension",
                        "com.ibm.social.as.lconn.extension.DisableDynamicLoadExtension" 
                ],
                filters : {
                    label : "Filter By:",
                    options : {
                        all : {
                            label : "All",
                            params : {
                                appId : "@all"
                            },
                            filters : {
                                label : "Show:",
                                options : {
                                    forme : {
                                        label : "For Me",
                                        params : {
                                            groupId : "@responses"
                                        }
                                    },
                                    fromme : {
                                        label : "From Me",
                                        params : {
                                            groupId : "@notesfromme"
                                        }
                                    }
                                }
                            }
                        },
                        activities : {
                            label : "Activities",
                            params : {
                                appId : "activities"
                            },
                            filters : {
                                label : "Show:",
                                options : {
                                    forme : {
                                        label : "For Me",
                                        params : {
                                            groupId : "@responses"
                                        }
                                    },
                                    fromme : {
                                        label : "From Me",
                                        params : {
                                            groupId : "@notesfromme"
                                        }
                                    }
                                }
                            }
                        },
                        blogs : {
                            label : "Blogs",
                            params : {
                                appId : "blogs"
                            },
                            filters : {
                                label : "Show:",
                                options : {
                                    forme : {
                                        label : "For Me",
                                        params : {
                                            groupId : "@responses"
                                        }
                                    },
                                    fromme : {
                                        label : "From Me",
                                        params : {
                                            groupId : "@notesfromme"
                                        }
                                    }
                                }
                            }
                        },
                        bookmarks : {
                            label : "Bookmarks",
                            params : {
                                appId : "bookmarks"
                            },
                            filters : {
                                label : "Show:",
                                options : {
                                    forme : {
                                        label : "For Me",
                                        params : {
                                            groupId : "@responses"
                                        }
                                    },
                                    fromme : {
                                        label : "From Me",
                                        params : {
                                            groupId : "@notesfromme"
                                        }
                                    }
                                }
                            }
                        },
                        communities : {
                            label : "Communities",
                            params : {
                                appId : "@communities"
                            },
                            filters : {
                                label : "Show:",
                                options : {
                                    forme : {
                                        label : "For Me",
                                        params : {
                                            groupId : "@responses"
                                        }
                                    },
                                    fromme : {
                                        label : "From Me",
                                        params : {
                                            groupId : "@notesfromme"
                                        }
                                    }
                                }
                            }
                        },
                        files : {
                            label : "Files",
                            params : {
                                appId : "files"
                            },
                            filters : {
                                label : "Show:",
                                options : {
                                    forme : {
                                        label : "For Me",
                                        params : {
                                            groupId : "@responses"
                                        }
                                    },
                                    fromme : {
                                        label : "From Me",
                                        params : {
                                            groupId : "@notesfromme"
                                        }
                                    }
                                }
                            }
                        },
                        forums : {
                            label : "Forums",
                            params : {
                                appId : "forums"
                            },
                            filters : {
                                label : "Show:",
                                options : {
                                    forme : {
                                        label : "For Me",
                                        params : {
                                            groupId : "@responses"
                                        }
                                    },
                                    fromme : {
                                        label : "From Me",
                                        params : {
                                            groupId : "@notesfromme"
                                        }
                                    }
                                }
                            }
                        },
                        profiles : {
                            label : "Profiles",
                            params : {
                                appId : "profiles"
                            },
                            filters : {
                                label : "Show:",
                                options : {
                                    forme : {
                                        label : "For Me",
                                        params : {
                                            groupId : "@responses"
                                        }
                                    },
                                    fromme : {
                                        label : "From Me",
                                        params : {
                                            groupId : "@notesfromme"
                                        }
                                    }
                                }
                            }
                        },
                        wikis : {
                            label : "Wikis",
                            params : {
                                appId : "wikis"
                            },
                            filters : {
                                label : "Show:",
                                options : {
                                    forme : {
                                        label : "For Me",
                                        params : {
                                            groupId : "@responses"
                                        }
                                    },
                                    fromme : {
                                        label : "From Me",
                                        params : {
                                            groupId : "@notesfromme"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            actionRequired : {
                label : "Action Required",
                params : {
                    groupId : "@actions"
                },
                extensions : [
                        "lconn.homepage.as.extension.ActionRequiredViewExtension",
                        "lconn.homepage.as.extension.SavedActionExtension",
                        "lconn.homepage.as.extension.ActivityReplyExtension",
                        "lconn.homepage.as.extension.NetworkInviteExtension" ],
                filters : {
                    label : "Filter By:",
                    options : {
                        all : {
                            label : "All",
                            params : {
                                appId : "@all"
                            },
                        // extensions : [
                        // "lconn.homepage.as.extension.UpdateBadgeExtension"
                        // ]
                        },
                        statusUpdates : {
                            label : "Status Updates",
                            params : {
                                appId : "@all",
                                broadcast : "true"
                            }
                        },
                        activities : {
                            label : "Activities",
                            params : {
                                appId : "activities"
                            }
                        },
                        blogs : {
                            label : "Blogs",
                            params : {
                                appId : "blogs"
                            }
                        },
                        bookmarks : {
                            label : "Bookmarks",
                            params : {
                                appId : "bookmarks"
                            }
                        },
                        communities : {
                            label : "Communities",
                            params : {
                                appId : "@communities"
                            }
                        },
                        files : {
                            label : "Files",
                            params : {
                                appId : "files"
                            }
                        },
                        forums : {
                            label : "Forums",
                            params : {
                                appId : "forums"
                            }
                        },
                        profiles : {
                            label : "Profiles",
                            params : {
                                appId : "profiles"
                            }
                        },
                        wikis : {
                            label : "Wikis",
                            params : {
                                appId : "wikis"
                            }
                        }
                    }
                }
            },
            saved : {
                label : "Saved",
                params : {
                    groupId : "@saved"
                },
                extensions : [
                        "lconn.homepage.as.extension.SavedViewExtension",
                        "lconn.homepage.as.extension.ActivityReplyExtension",
                        "com.ibm.social.as.lconn.extension.DisableDynamicLoadExtension" ],
                filters : {
                    label : "Filter By:",
                    options : {
                        all : {
                            label : "All",
                            params : {
                                appId : "@all"
                            }
                        },
                        statusUpdates : {
                            label : "Status Updates",
                            params : {
                                appId : "@all",
                                broadcast : "true"
                            }
                        },
                        activities : {
                            label : "Activities",
                            params : {
                                appId : "activities"
                            }
                        },
                        blogs : {
                            label : "Blogs",
                            params : {
                                appId : "blogs"
                            }
                        },
                        bookmarks : {
                            label : "Bookmarks",
                            params : {
                                appId : "bookmarks"
                            }
                        },
                        communities : {
                            label : "Communities",
                            params : {
                                appId : "@communities"
                            }
                        },
                        files : {
                            label : "Files",
                            params : {
                                appId : "files"
                            }
                        },
                        forums : {
                            label : "Forums",
                            params : {
                                appId : "forums"
                            }
                        },
                        profiles : {
                            label : "Profiles",
                            params : {
                                appId : "profiles"
                            }
                        },
                        wikis : {
                            label : "Wikis",
                            params : {
                                appId : "wikis"
                            }
                        }
                    }
                }
            },
            discover : {
                label : "Discover",
                params : {
                    userId : "@public",
                    rollup : "true"
                },
                extensions : [
                        "lconn.homepage.as.extension.SavedActionExtension",
                        "com.ibm.social.as.lconn.extension.MicroblogDeletionExtension" 
                ],
                filters : {
                    label : "Filter By:",
                    options : {
                        all : {
                            label : "All",
                            params : {
                                appId : "@all"
                            },
                            extensions : [ "com.ibm.social.as.lconn.extension.ShareboxStatusUpdateExtension" ]
                        },
                        statusUpdates : {
                            label : "Status Updates",
                            params : {
                                appId : "@all",
                                broadcast : "true"
                            },
                            extensions : [ "com.ibm.social.as.lconn.extension.ShareboxStatusUpdateExtension" ]
                        },
                        activities : {
                            label : "Activities",
                            params : {
                                appId : "activities"
                            }
                        },
                        blogs : {
                            label : "Blogs",
                            params : {
                                appId : "blogs"
                            }
                        },
                        bookmarks : {
                            label : "Bookmarks",
                            params : {
                                appId : "bookmarks"
                            }
                        },
                        communities : {
                            label : "Communities",
                            params : {
                                appId : "@communities"
                            }
                        },
                        files : {
                            label : "Files",
                            params : {
                                appId : "files"
                            }
                        },
                        forums : {
                            label : "Forums",
                            params : {
                                appId : "forums"
                            }
                        },
                        profiles : {
                            label : "Profiles",
                            params : {
                                appId : "profiles"
                            },
                            extensions : [ "com.ibm.social.as.lconn.extension.ShareboxStatusUpdateExtension" ]
                        },
                        wikis : {
                            label : "Wikis",
                            params : {
                                appId : "wikis"
                            }
                        }
                    }
                }
            }
        },
        dirtyChecker : "true",
        extensions : [ 
            "com.ibm.social.as.extension.CommentExtension",
            "com.ibm.social.as.extension.DirtyCheckExtension" 
        ],
        activityStreamModelClass : "com.ibm.social.as.ActivityStreamModel",
        pagingHandlerClass : "com.ibm.social.as.paging.PagingHandler",
        newsItemFactoryClass : "com.ibm.social.as.item.manager.NewsItemFactory",
        newsItemManagerClass : "com.ibm.social.as.item.manager.NewsItemManager",
        searchHashtagUtilClass : "com.ibm.social.as.util.hashtag.HashtagUtil",
        connections : {
            isAdmin : "false"
        }
    };

    var activityStreamWrapper = new ActivityStreamWrapper({
        config : asConfig,
        activityStreamNode : "activityStream",
        shareBoxNode : "inputForm",
        sideNavNode : "sideNav"
    });

    dom.byId("activityStreamDiv").appendChild(activityStreamWrapper.domNode);
    activityStreamWrapper.startup();
});