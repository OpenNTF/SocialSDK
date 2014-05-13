

***REMOVED*** 
// Ensure that element IDs are unique
$timestamp = time();

global $CFG;
require_once $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-communities-grid-row.php';
?>
<div id="***REMOVED*** echo $this->config->elementID;?>"></div>
<script type="text/javascript">
	var grid;
	
	require(["sbt/dom", 
	         "sbt/connections/controls/communities/CommunityGrid",
	         "sbt/connections/CommunityService",
	         "sbt/lang"], 
	
	function(dom, CommunityGrid, CommunityService, lang) {
	    
		var communityService = new CommunityService({endpoint: "***REMOVED*** echo $this->config->endpoint; ?>"}); 
		var domNode = dom.byId("communityRow-***REMOVED*** echo $timestamp; ?>");
		var CommunityRow = domNode.text || domNode.textContent;
	    domNode = dom.byId("pagingHeader-***REMOVED*** echo $timestamp; ?>");
	    var PagingHeader = domNode.text || domNode.textContent;
	    domNode = dom.byId("pagingFooter-***REMOVED*** echo $timestamp; ?>");
	    var PagingFooter = domNode.text || domNode.textContent;
	    
		grid = new CommunityGrid({
	    	type : "my",
	    	endpoint: "***REMOVED*** echo $this->config->endpoint; ?>",
	        hidePager: false,
	        hideSorter: true,
	        hideFooter: false,
	        rendererArgs : { template : CommunityRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter }
	   	});
	            
	   dom.byId("***REMOVED*** echo $this->config->elementID;?>").appendChild(grid.domNode);
	   grid.renderer.tableClass = "table";
	   grid.renderer.template = CommunityRow;
	   grid.update();
	    
	    dom.byId("ibm-sbt-create-community-button-***REMOVED*** echo $timestamp; ?>").onclick = function(evt) {
	        var title = document.getElementById("ibm-sbt-community-title-***REMOVED*** echo $timestamp; ?>").value;
	        
	        if(!title || !title.length > 0){
	        	dom.byId("ibm-sbt-success-***REMOVED*** echo $timestamp; ?>").style.display = "none";
	            dom.byId("ibm-sbt-error-***REMOVED*** echo $timestamp; ?>").style.display = "";
	            dom.setText("ibm-sbt-error-***REMOVED*** echo $timestamp; ?>", "You Must Enter A Title For The Community");
	        	return;
	        }
	        var content = document.getElementById("ibm-sbt-community-content-***REMOVED*** echo $timestamp; ?>").value;
	        var tags = document.getElementById("ibm-sbt-community-tags-***REMOVED*** echo $timestamp; ?>").value;
	        
	        var community = communityService.newCommunity(); 
	        community.setTitle(title);
	        
	        if(content && content.length > 0){
	        	community.setContent(content);	
	        }
	        if(tags && tags.length > 0){
	        	community.setTags(tags);
	        }
	
	        dom.byId("ibm-sbt-success-***REMOVED*** echo $timestamp; ?>").style.display = "block";
	        dom.byId("ibm-sbt-error-***REMOVED*** echo $timestamp; ?>").style.display = "none";
	        dom.setText("ibm-sbt-success-***REMOVED*** echo $timestamp; ?>", "***REMOVED*** echo get_string('creating_community', 'block_ibmsbt'); ?>...");
	
	        communityService.createCommunity(community).then(  
	                function(community) { 
	                    community.load().then(
	                        function(community) { 
	                        	dom.byId("ibm-sbt-success-***REMOVED*** echo $timestamp; ?>").style.display = "block";
	            	            dom.byId("ibm-sbt-error-***REMOVED*** echo $timestamp; ?>").style.display = "none";
	            	            dom.setText("ibm-sbt-success-***REMOVED*** echo $timestamp; ?>", "Successfully created Community");
	            	            document.getElementById('ibm-sbt-create-community-***REMOVED*** echo $timestamp; ?>').style.display = 'none';
	            	            grid.update(null);
	                        },
	                        function(success) {
	                        	dom.byId("ibm-sbt-success-***REMOVED*** echo $timestamp; ?>").style.display = "block";
	            	            dom.byId("ibm-sbt-error-***REMOVED*** echo $timestamp; ?>").style.display = "none";
	            	            dom.setText("ibm-sbt-success-***REMOVED*** echo $timestamp; ?>", "Successfully created Community");
	            	            document.getElementById('ibm-sbt-create-community-***REMOVED*** echo $timestamp; ?>').style.display = 'none';
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