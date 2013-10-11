require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
  var communityService = new CommunityService();
  var startDate = new Date();
  startDate.setDate(startDate.getDate()-10);
  
  var promise = communityService.getCommunityEvents("%{name=sample.communityId|label=communityId|helpSnippetId=Social_Communities_Get_My_Communities}", startDate.toISOString());
  promise.then(
      function(events) {
          if(events.length === 0){
              dom.setText("json", "No results, try varying the startDate and endDate parameters");
          }
          var detailedEvents = [];
          var i;
          for(i=0; i < events.length; i++){
              events[i].getFullEvent().then( // we need an extra network request to populate all event information, e.g. its content
                  function(event){
                      detailedEvents.push(event);
                      return i;
              }).then(function(index){
                  if(index === events.length){
                      dom.setText("json", json.jsonBeanStringify(detailedEvents));
                 }
              });
          }
      },
      function(error) {
          dom.setText("json", json.jsonBeanStringify(error));
      }
  );
});