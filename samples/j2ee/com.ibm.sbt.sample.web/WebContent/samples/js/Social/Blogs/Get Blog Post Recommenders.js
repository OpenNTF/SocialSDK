require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"],
	    function(BlogService, dom, json) {
			var createRow = function(recommender) {
		        var table = dom.byId("blogsTable");
		        var tr = document.createElement("tr");
		        table.appendChild(tr);
		        var td = document.createElement("td");
		        dom.setText(td, recommender.getTitle());
		        tr.appendChild(td);
		        td = document.createElement("td");
		        dom.setText(td, recommender.getRecommenderUuid());
		        tr.appendChild(td);
		    };
	        var blogService = new BlogService(); 
	    	blogService.getRecommendedPosts({ ps: 1 }).then(
				function(posts){
					return blogService.getBlogPostRecommenders(posts[0],{ ps: 1 }); // returning newly created comment
	            }
	    	).then(
                function(recommenders){
                    if (recommenders.length == 0) {
                        text = "All blogs returned no results.";
                        dom.setText("content", text);
                    } else {
                        for(var i=0; i<recommenders.length; i++){
                            var recommender = recommenders[i];
                            createRow(recommender);
                        }
                    }
                },
                function(error){
                    dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
                }       
        	);
		}
	);