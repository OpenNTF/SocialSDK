require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
  
  var eventInstId = "%{name=CommunityService.eventInstUuid|label=communityId|helpSnippetId=Social_Communities_Get_Community_Events}";
  
  var communityService = new CommunityService();
  var promise = communityService.getCommunityEvent(eventInstId);
  promise.then(
      function(event) {
          dom.setText("json", json.jsonBeanStringify(event));
      },
      function(error) {
          dom.setText("json", json.jsonBeanStringify(error));
      }
  );
});