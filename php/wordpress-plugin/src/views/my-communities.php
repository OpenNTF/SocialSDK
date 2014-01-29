<div role="main">
	<table class="table table-bordered" id="communitiesTable">
		<tr class="label label-info">
			<th>My Communities</th>
		</tr>
	</table>
</div>

<script type="text/javascript">
require(["sbt/connections/CommunityService", "sbt/dom"], 
    function(CommunityService,dom) {
        var createItem = function(community) {
            var table = dom.byId("communitiesTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            tr.appendChild(td);
            var a = document.createElement("a");
            a.href = community.getAlternateUrl();
            a.appendChild(document.createTextNode(community.getTitle()));
            td.appendChild(a);
        };

        var communityService = new CommunityService();
    	communityService.getMyCommunities().then(
            function(communities) {
                if (communities.length == 0) {
                	dom.setText("communitiesTable", "You are not a member of any communities.");
                } else {
                    for(var i=0; i<communities.length; i++){
                        var community = communities[i];
                        createItem(community);
                    }
                }
            },
            function(error) {
                dom.setText("communitiesTable", error.message);
            }       
    	);
    }
);
</script>