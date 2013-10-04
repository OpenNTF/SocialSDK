require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
  var communityService = new CommunityService({endpoint: "w3connections"});
        var promise = communityService.getCommunityEvents();
        
        promise.then(
            function(events) {
              var detailedEvents = [];
              var i;
              for(i=0; i < events.length; i++){
                events[i].getDetailedEvent().then(function(event){
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
    }
);