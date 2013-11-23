require(["sbt/connections/BlogService", "sbt/dom"], 
    function(BlogService, dom) {
	    var createRow = function(comment) {
	        var table = dom.byId("blogsTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        dom.setText(td, comment.getTitle());
	        tr.appendChild(td);
	        td = document.createElement("td");
	        dom.setText(td, comment.getCommentUuid());
	        tr.appendChild(td);
	    };
    
    	var blogService = new BlogService();
    	blogService.getBlogsComments({ ps: 5 }).then(
            function(comments){
                if (comments.length == 0) {
                    text = "All blogs returned no results.";
                    dom.setText("content", text);
                } else {
                    for(var i=0; i<comments.length; i++){
                        var comment = comments[i];
                        createRow(comment);
                    }
                }
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);