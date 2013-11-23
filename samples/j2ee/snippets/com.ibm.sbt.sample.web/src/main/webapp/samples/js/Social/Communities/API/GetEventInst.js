require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
  
  var eventInstId = "%{name=CommunityService.eventInstUuid|label=eventInstUuid|helpSnippetId=Social_Communities_API_GetCommunityEventInsts}";
  
  var communityService = new CommunityService();
  var promise = communityService.getEventInst(eventInstId);
  promise.then(
      function(eventInst) {
          dom.setText("json", json.jsonBeanStringify(eventInst));
      },
      function(error) {
          dom.setText("json", json.jsonBeanStringify(error));
      }
  );
});