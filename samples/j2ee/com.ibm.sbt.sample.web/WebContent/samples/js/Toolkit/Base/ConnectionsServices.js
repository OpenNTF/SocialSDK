require(["sbt/dom", "sbt/json", "sbt/lang", "sbt/base/BaseService", "sbt/base/BaseEntity", 
         "sbt/connections/CommunityService"], 
    function(dom,json,lang,BaseService,BaseEntity,CommunityService) {
	
	var baseService = new BaseService();
	var baseEntity = new BaseEntity({service : baseService});
	
    var results = [];
    addCommunityService(CommunityService, baseService, baseEntity, results);
    dom.setText("json", json.jsonBeanStringify(results));
});

function addCommunityService(CommunityService, baseService, baseEntity, results) {
	var communityService = new CommunityService();
    results.push(getPublicApi("CommunityService", communityService, baseService));
    //results.push(communityService.newCommunity());
    //results.push(communityService.newMember());
    //results.push(communityService.newInvite());
}

function getPublicApi(type, theObj, theBase) {
	var apiDefn = { 
		type : type,
		properties : [],
		methods : []
	};
	for (var property in theObj) {
		if (property.substring(0,1) != "_" && 
			property.substring(0,3) != "new" && 
			!theBase[property]) {
			if (typeof theObj[property] == 'function') { 
				apiDefn.methods.push(property);
			} else {
				apiDefn.properties.push(property);
			}
		}
	}
	return apiDefn;
}
