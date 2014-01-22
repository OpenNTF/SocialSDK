
            <div id="communityGridDiv"></div>
          	
          	<?php include_once BASE_PATH . '/views/sbtk-row-template.html';?>
          	
            <script type="text/javascript">
            require(["sbt/dom", "sbt/connections/controls/communities/CommunityGrid"], 
            	function(dom, CommunityGrid) {
            		var domNode = dom.byId("communityRow");
                	var CustomCommunityRow = domNode.text || domNode.textContent;
                
	                var grid = new CommunityGrid({
	                    type : "my",
	                    hidePager : <?php echo ($hidePager == 1 ? "true" : "false") ?>,
	                    hideSorter : <?php echo ($hideSorter == 1 ? "true" : "false") ?>,
	                    rendererArgs : { containerType : "<?php echo $containerType; ?>" }
	                });

	                grid.renderer.template = CustomCommunityRow;
	                
	                dom.byId("communityGridDiv").appendChild(grid.domNode);
	                         
	                grid.update();
            });
				</script>            


