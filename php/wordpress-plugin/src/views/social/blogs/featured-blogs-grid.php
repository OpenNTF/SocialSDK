<div role="main">
	<table class="table table-bordered" id="featuredBlogsTable">
		<tr class="label label-info">
			<th><strong>Blog Title</strong></th>
		</tr>
	</table>
</div>
          	
            <script type="text/javascript">
            require(["sbt/connections/BlogService", "sbt/dom"], 
            	    function(BlogService, dom) {
            		    var createRow = function(blog) {
            		        var table = dom.byId("featuredBlogsTable");
            		        var tr = document.createElement("tr");
            		        table.appendChild(tr);
            		        var td = document.createElement("td");
            		        dom.setText(td, blog.getTitle());
            		        tr.appendChild(td);
            		    };
            	    
            	    	var blogService = new BlogService();
            	    	blogService.getFeaturedBlogs({ ps: 5 }).then(
            	            function(blogs){
            	                if (blogs.length == 0) {
            	                    text = "My blogs returned no results.";
            	                    dom.setText("content", text);
            	                } else {
            	                    for(var i=0; i<blogs.length; i++){
            	                        var blog = blogs[i];
            	                        createRow(blog);
            	                    }
            	                }
            	            },
            	            function(error){
            	                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            	            }       
            	    	);
            	    }
            	);
		</script>            


