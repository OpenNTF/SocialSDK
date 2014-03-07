

<button id="actionBtn" href="javascript:;" role="button">Start a Forum</button>
<div id="***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>"></div>

***REMOVED*** 
	if ((isset($instance['ibm-sbtk-template']) && $instance['ibm-sbtk-template'] != "")) {
		require_once BASE_PATH . "{$instance['ibm-sbtk-template']}";
	} else {
		require_once BASE_PATH . '/user_widgets/views/templates/ibm-sbt-sample-forum-template.php';
	}
?>


<script type="text/javascript">
require(["sbt/dom", 
         "sbt/connections/controls/forums/ForumGrid",
         "sbt/connections/ForumService"], 
    function(dom, ForumGrid, ForumService) {
		// display My Forums
		var domNode = dom.byId("sampleForumRow");
		var ForumRow = domNode.text || domNode.textContent;
		domNode = dom.byId("sampleForumPagingHeader");
	    var PagingHeader = domNode.text || domNode.textContent;
		var PagingHeader = domNode.text || domNode.textContent;
		domNode = dom.byId("sampleForumPagingFooter");
		var PagingFooter = domNode.text || domNode.textContent;
			
		var forumGrid = new ForumGrid({
	        type : "my",
	        hideSorter: false,
	        hidePager: false,
	        rendererArgs : { template : ForumRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}
	    });
	    dom.byId("***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>").appendChild(forumGrid.domNode);

	    forumGrid.renderer.tableClass = "table";
	    forumGrid.renderer.template = ForumRow;
	    
	    forumGrid.update();
	    
	    // create ForumService
	    var forumService = new ForumService();
	    addStartForumBehaviour(forumGrid, forumService, dom);
	}
);

function addStartForumBehaviour(forumGrid, forumService, dom) {
	var actionBtn = dom.byId("actionBtn");
    actionBtn.onclick = function(evt) {
    	var title = prompt("Please enter a title for the new forum", "");
        if (title) {
        	var forum = forumService.newForum(); 
            forum.setTitle(title);
            forum.setContent("");
            var promise = forumService.createForum(forum);
            promise.then(
                function(forum) {
                	forumGrid.refresh();
                	alert("Forum started successfully!");
                },
                function(error) {
                	alert("Error starting forum: "+error);
                }
            );
        }
    };
}
</script>