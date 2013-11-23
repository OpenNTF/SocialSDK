require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
  
  var communityId = "%{name=CommunityService.communityUuid|label=Community Id|helpSnippetId=Social_Communities_Get_My_Communities}";
  var startDate = "%{name=sample.startDate|label=Start Date}";

  var communityService = new CommunityService();
  var promise = communityService.getCommunityEvents(communityId, startDate);
  promise.then(
      function(events) {
          if( events.length === 0) {
              dom.setText("json", "There are no events scheduled after: "+startDate);
          }
          dom.setText("json", json.jsonBeanStringify(events));
      },
      function(error) {
          dom.setText("json", json.jsonBeanStringify(error));
      }
  );
});