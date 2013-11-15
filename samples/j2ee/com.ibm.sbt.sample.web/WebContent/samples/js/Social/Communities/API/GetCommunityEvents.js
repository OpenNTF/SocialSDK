require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
  
  var communityId = "%{name=CommunityService.communityUuid|label=Community Id|helpSnippetId=Social_Communities_Get_My_Communities}";
  var startDate = "%{name=sample.startDate|label=Start Date}";
  
  alert(communityId);
  
  var communityService = new CommunityService();
  var promise = communityService.getCommunityEvents(communityId, startDate);
  promise.then(
      function(events) {
          if( events.length === 0) {
              dom.setText("json", "There are no events scheduled after: "+startDate);
          }
          var detailedEvents = [];
          for (var i=0; i < events.length; i++) {
              events[i].load().then( 
                  function(event) {
                      detailedEvents.push(event);
                      return i;
                  },
                  function(error) {
                	  dom.setText("json", json.jsonBeanStringify(error));
                  }
              ).then(function(index) {
                  if (index === events.length) {
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