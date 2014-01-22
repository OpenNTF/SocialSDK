           <table class="table table-bordered" id="entriesTable">
				<tbody>
					<tr class="label label-info">
						<th><strong>Title</strong></th>
						<th><strong>Locked</strong></th>
						<th><strong>Question</strong></th>
					</tr>
				</tbody>
			</table>
          	
            <script type="text/javascript">
            require(["sbt/connections/ForumService", "sbt/dom"], 
            	    function(ForumService,dom) {
            	        var createRow = function(topic) {
            	            var table = dom.byId("entriesTable");
            	            var tr = document.createElement("tr");
            	            table.appendChild(tr);
            	            var td = document.createElement("td");
            	            td.appendChild(dom.createTextNode(topic.getTitle()));
            	            tr.appendChild(td);
            	            td = document.createElement("td");
            	            td.appendChild(dom.createTextNode(topic.isLocked()));
            	            tr.appendChild(td);
            	            td = document.createElement("td");
            	            td.appendChild(dom.createTextNode(topic.isQuestion()));
            	            tr.appendChild(td);
            	        };

            	        var forumService = new ForumService();
            	        forumService.getMyTopics().then(
            	        	function(topics) {
            	        		if (topics.length == 0) {
            	        			dom.setText("content", "You have no topics.");
            	        		}
            	        		for(var i=0; i<topics.length; i++) {
            	                    var topic = topics[i];
            	                    createRow(topic);
            	                }
            	        	},
            	        	function(error) {
            	        		console.log(error);
            	        	}
            	        );
            	    }
            	);
				</script>            