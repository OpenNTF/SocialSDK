***REMOVED***
	extract($args);
	echo $before_widget;
	echo $before_title . 'My Communities' . $after_title; 
	?>
            <div id="gridDiv"></div>
          	
          	***REMOVED*** include_once BASE_PATH . '/views/sbtk-row-template.html';?>
          	
            <script type="text/javascript">
            require(["sbt/dom", "sbt/connections/controls/communities/CommunityGrid"], 
            	function(dom, CommunityGrid) {
            		var domNode = dom.byId("communityRow");
                	var CustomCommunityRow = domNode.text || domNode.textContent;
                
	                var grid = new CommunityGrid({
	                    type : "my",
	                    hidePager : true,
	                    hideSorter : true,
	                    rendererArgs : { containerType : "ul" }
	                });

	                grid.renderer.template = CustomCommunityRow;
	                
	                dom.byId("gridDiv").appendChild(grid.domNode);
	                         
	                grid.update();
            });
				</script>            
        ***REMOVED*** echo $after_widget; ?>
***REMOVED***


