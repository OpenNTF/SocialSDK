           <table class="table table-bordered" id="forumsTable">
				<tr class="label label-info">
					<th><strong>Forum Title</strong></th>
				</tr>
			</table>
          	
            <script type="text/javascript">
            require(["sbt/connections/ForumService", "sbt/dom"], 
            	    function(ForumService,dom) {
            	        var createRow = function(forum) {
            	            var title = forum.getTitle(); 
            	        	
            	            var table = dom.byId("forumsTable");
            	            var tr = document.createElement("tr");
            	            table.appendChild(tr);
            	            var td = document.createElement("td");
            	            td.appendChild(dom.createTextNode(title));
            	            tr.appendChild(td);
            	        };

            	        var forumService = new ForumService();
            	        forumService.getMyForums({ since : 0 }).then(
            	            function(forums) {
            	                if (forums.length == 0) {
            	                    text = "You do not have any forums.";
            	                } else {
            	                    for(var i=0; i<forums.length; i++){
            	                        var forum = forums[i];
            	                        createRow(forum);
            	                    }
            	                }
            	            },
            	            function(error) {
            	            	console.log(error);
            	            }       
            	    	);
            	    }
            	);
				</script>            
