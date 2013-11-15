require(["sbt/connections/CommunityService", "sbt/dom"], 
    function(CommunityService, dom) {
	    var createRow = function(event) {
	        var table = dom.byId("eventsTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        td.appendChild(dom.createTextNode(event.getTitle()));
	        tr.appendChild(td);
	        td = document.createElement("td");
	        td.appendChild(dom.createTextNode(event.getEventInstUuid()));
	        tr.appendChild(td);
	    };
    
	    var communityId = "%{name=CommunityService.communityUuid|label=communityId|helpSnippetId=Social_Communities_Get_My_Communities}";
	    var startDate = new Date();
	    startDate.setFullYear(2012,01,01);
	    
	    var communityService = new CommunityService();
	    var promise = communityService.getCommunityEvents(communityId, startDate.toUTCString());
	    promise.then(
	        function(events) {
	            if( events.length === 0) {
	                dom.setText("content", "There are no events scheduled after: "+startDate);
	            }
	            for (var i=0; i < events.length; i++) {
	                events[i].load().then( 
	                    function(event) {
	                    	createRow(event);
	                    },
	                    function(error) {
	                        dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
	                    }
	                );
	            }
	        },
	        function(error) {
	            dom.setText("json", json.jsonBeanStringify(error));
	        }
	    );
    }
);