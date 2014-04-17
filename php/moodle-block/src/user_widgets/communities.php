

<?php 
// Ensure that element IDs are unique
$timestamp = time();

global $CFG;
require_once $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-communities-grid-row.php';
?>
<div id="<?php echo $this->config->elementID;?>"></div>
<script type="text/javascript">
	var grid;
	
	require(["sbt/dom", 
	         "sbt/connections/controls/communities/CommunityGrid",
	         "sbt/connections/CommunityService",
	         "sbt/lang"], 
	
	function(dom, CommunityGrid, CommunityService, lang) {
	    
		var communityService = new CommunityService({endpoint: "<?php echo $this->config->endpoint; ?>"}); 
		var domNode = dom.byId("communityRow-<?php echo $timestamp; ?>");
		var CommunityRow = domNode.text || domNode.textContent;
	    domNode = dom.byId("pagingHeader-<?php echo $timestamp; ?>");
	    var PagingHeader = domNode.text || domNode.textContent;
	    domNode = dom.byId("pagingFooter-<?php echo $timestamp; ?>");
	    var PagingFooter = domNode.text || domNode.textContent;
	    
		grid = new CommunityGrid({
	    	type : "my",
	    	endpoint: "<?php echo $this->config->endpoint; ?>",
	        hidePager: false,
	        hideSorter: true,
	        hideFooter: false,
	        rendererArgs : { template : CommunityRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter }
	   	});
	            
	   dom.byId("<?php echo $this->config->elementID;?>").appendChild(grid.domNode);
	   grid.renderer.tableClass = "table";
	   grid.renderer.template = CommunityRow;
	   grid.update();
	    
	    dom.byId("ibm-sbt-create-community-button-<?php echo $timestamp; ?>").onclick = function(evt) {
	        var title = document.getElementById("ibm-sbt-community-title-<?php echo $timestamp; ?>").value;
	        
	        if(!title || !title.length > 0){
	        	dom.byId("ibm-sbt-success-<?php echo $timestamp; ?>").style.display = "none";
	            dom.byId("ibm-sbt-error-<?php echo $timestamp; ?>").style.display = "";
	            dom.setText("ibm-sbt-error-<?php echo $timestamp; ?>", "You Must Enter A Title For The Community");
	        	return;
	        }
	        var content = document.getElementById("ibm-sbt-community-content-<?php echo $timestamp; ?>").value;
	        var tags = document.getElementById("ibm-sbt-community-tags-<?php echo $timestamp; ?>").value;
	        
	        var community = communityService.newCommunity(); 
	        community.setTitle(title);
	        
	        if(content && content.length > 0){
	        	community.setContent(content);	
	        }
	        if(tags && tags.length > 0){
	        	community.setTags(tags);
	        }
	
	        dom.byId("ibm-sbt-success-<?php echo $timestamp; ?>").style.display = "block";
	        dom.byId("ibm-sbt-error-<?php echo $timestamp; ?>").style.display = "none";
	        dom.setText("ibm-sbt-success-<?php echo $timestamp; ?>", "Creating community...");
	
	        communityService.createCommunity(community).then(  
	                function(community) { 
	                    community.load().then(
	                        function(community) { 
	                        	dom.byId("ibm-sbt-success-<?php echo $timestamp; ?>").style.display = "block";
	            	            dom.byId("ibm-sbt-error-<?php echo $timestamp; ?>").style.display = "none";
	            	            dom.setText("ibm-sbt-success-<?php echo $timestamp; ?>", "Successfully created Community");
	            	            document.getElementById('ibm-sbt-create-community-<?php echo $timestamp; ?>').style.display = 'none';
	            	            grid.update(null);
	                        },
	                        function(success) {
	                        	dom.byId("ibm-sbt-success-<?php echo $timestamp; ?>").style.display = "block";
	            	            dom.byId("ibm-sbt-error-<?php echo $timestamp; ?>").style.display = "none";
	            	            dom.setText("ibm-sbt-success-<?php echo $timestamp; ?>", "Successfully created Community");
	            	            document.getElementById('ibm-sbt-create-community-<?php echo $timestamp; ?>').style.display = 'none';
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