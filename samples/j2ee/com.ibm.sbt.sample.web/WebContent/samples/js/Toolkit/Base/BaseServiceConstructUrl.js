require([ "sbt/lang", "sbt/dom", "sbt/json", "sbt/base/BaseService" ], 
    function(lang,dom,json,BaseService) {
        try {
            var baseService = new BaseService();

            var results = {};

            results.a = baseService.constructUrl("/communities/service/atom/communities/all");
            results.b = baseService.constructUrl("/communities/service/atom/communities/all", 
                    { ps : 5 , since : "2009-01-04T20:32:31.171Z", email : "john?@foo"});
            results.c = baseService.constructUrl("/communities/service/atom/communities/all?page=1", 
                    { ps : 5 , since : "2009-01-04T20:32:31.171Z", email : "john?@foo"});
            results.d = baseService.constructUrl("/communities/service/atom/communities/all?page=1&", 
                    { ps : 5 , since : "2009-01-04T20:32:31.171Z", email : "john?@foo"});
            results.e = baseService.constructUrl("/connections/opensocial/{authType}/rest/activitystreams/", 
                    {}, 
                    { authType : "oauth" });
            results.f = baseService.constructUrl("/connections/opensocial/{authType}/rest/activitystreams/{userType}", 
                    {}, 
                    { authType : "oauth", userType : "@me" });
            results.g = baseService.constructUrl("/connections/opensocial/{authType}/rest/activitystreams/{userType}/{groupType}/{appType}", 
                    {}, 
                    { authType : "oauth", userType : "@me", groupType : "@following", appType : "@communities" });
            results.h = baseService.constructUrl("/connections/opensocial/{authType}/rest/activitystreams/", 
                    {}, 
                    { authType : "" });
            
            dom.setText("json", json.jsonBeanStringify(results));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    }
);
