           <table class="table table-bordered" id="answeredTopicsTable">
				<tr class="label label-info">
					<th><strong>Title</strong></th>
					<th><strong>Answered</strong></th>
				</tr>
			</table>
          	
            <script type="text/javascript">
            require(["sbt/connections/ForumService", "sbt/dom"], 
            	    function(ForumService,dom) {
            	        var createRow = function(topic) {
            	            var table = dom.byId("answeredTopicsTable");
            	            var tr = document.createElement("tr");
            	            table.appendChild(tr);
            	            var td = document.createElement("td");
            	            td.appendChild(dom.createTextNode(topic.getTitle()));
            	            tr.appendChild(td);
            	            td = document.createElement("td");
            	            td.appendChild(dom.createTextNode(topic.isAnswered()));
            	            tr.appendChild(td);
            	        };

            	        var forumService = new ForumService();
            	        forumService.getMyTopics({ filter : "answeredquestions" }).then(
            	        	function(topics) {
            	        		if (topics.length == 0) {
            	        			dom.setText("content", "You have no answered topics.");
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