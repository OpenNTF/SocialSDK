
<div id="***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>"></div>

***REMOVED*** 
	if ((isset($instance['ibm-sbtk-template']) && $instance['ibm-sbtk-template'] != "")) {
		require_once BASE_PATH . "{$instance['ibm-sbtk-template']}";
	} else {
		require_once BASE_PATH . '/user_widgets/views/templates/ibm-sbt-sample-community-template.php';
	}
?>


<script type="text/javascript">
var grid;

require(["sbt/dom", 
         "sbt/connections/controls/communities/CommunityGrid",
         "sbt/connections/CommunityService",
         "sbt/lang"], 

function(dom, CommunityGrid, CommunityService, lang) {
    
	var communityService = new CommunityService(); 
	var domNode = dom.byId("communityRow");
	var CommunityRow = domNode.text || domNode.textContent;
    domNode = dom.byId("pagingHeader");
    var PagingHeader = domNode.text || domNode.textContent;
    domNode = dom.byId("pagingFooter");
    var PagingFooter = domNode.text || domNode.textContent;
    
	grid = new CommunityGrid({
        type: "my",
        hideSorter: true,
        rendererArgs : { template : CommunityRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter}
   	});
            
   dom.byId("***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>").appendChild(grid.domNode);
   grid.renderer.tableClass = "table";
   grid.renderer.template = CommunityRow;
   grid.update();
    
    dom.byId("selectedBtn").onclick = function(evt) {
        var title = document.getElementById("titleTextField").value;
        
        if(!title || !title.length > 0){
        	dom.byId("success").style.display = "none";
            dom.byId("error").style.display = "";
            dom.setText("error", "You Must Enter A Title For The Community");
        	return;
        }
        var content = document.getElementById("contentTextField").value;
        var tags = document.getElementById("tagsTextField").value;
        
        var community = communityService.newCommunity(); 
        community.setTitle(title);
        
        if(content && content.length > 0){
        	community.setContent(content);	
        }
        if(tags && tags.length > 0){
        	community.setTags(tags);
        }

        dom.byId("success").style.display = "block";
        dom.byId("error").style.display = "none";
        dom.setText("success", "Creating community...");

        communityService.createCommunity(community).then(  
                function(community) { 
                    community.load().then(
                        function(community) { 
                        	dom.byId("success").style.display = "block";
            	            dom.byId("error").style.display = "none";
            	            dom.setText("success", "Successfully created Community");
            	            grid.update(null);
                        },
                        function(success) {
                        	dom.byId("success").style.display = "block";
            	            dom.byId("error").style.display = "none";
            	            dom.setText("success", "Successfully created Community");
            	            grid.update(null);
                        }
                    );
                },
                function(error) {
                    console.log(error);
                }
            );
    };
   
});
</script>