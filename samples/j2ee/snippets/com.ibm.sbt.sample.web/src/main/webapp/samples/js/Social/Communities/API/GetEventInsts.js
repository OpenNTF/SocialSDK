require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
  
  var eventId = "%{name=CommunityService.eventUuid|label=eventUuId|helpSnippetId=Social_Communities_Get_Community_Events}";
  
  var communityService = new CommunityService();
  var promise = communityService.getEventInsts(eventId);
  promise.then(
      function(eventInsts) {
          dom.setText("json", json.jsonBeanStringify(eventInsts));
      },
      function(error) {
          dom.setText("json", json.jsonBeanStringify(error));
      }
  );
});