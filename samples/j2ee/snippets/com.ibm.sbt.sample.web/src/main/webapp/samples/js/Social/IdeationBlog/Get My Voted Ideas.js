require(["sbt/connections/IdeationBlogService", "sbt/dom"], 
    function(IdeationBlogService, dom) {
	    var createRow = function(idea) {
	        var table = dom.byId("blogsTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        dom.setText(td, idea.getTitle());
	        tr.appendChild(td);
	        td = document.createElement("td");
	        dom.setText(td, idea.getBlogPostUuid());
	        tr.appendChild(td);
	    };
    
    	var ideationBlogService = new IdeationBlogService();
    	ideationBlogService.getMyVotedIdeas({ ps: 5 }).then(
            function(ideas){
                if (ideas.length == 0) {
                    text = "All idea blogs returned no results.";
                    dom.setText("content", text);
                } else {
                    for(var i=0; i<ideas.length; i++){
                        var idea = ideas[i];
                        createRow(idea);
                    }
                }
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);