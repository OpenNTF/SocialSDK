
            <div id="communityGridMembersDiv"></div>

            <script type="text/javascript">
            require(["sbt/dom", "sbt/connections/controls/communities/CommunityMembersGrid"], 
           		function(dom, CommunityMembersGrid) {
	                var grid = new CommunityMembersGrid({
	                    type : "communityMembers",
	                    communityUuid : "<?php echo $communityUuid; ?>"
	                });
	
	                dom.byId("communityGridMembersDiv").appendChild(grid.domNode);
	
	                grid.update();
            });
				</script>            
