<div role="main">
	<table class="table table-bordered" id="invitesTable">
		<tr class="label label-info">
			<th><strong>Title</strong></th>
			<th><strong>Community</strong></th>
		</tr>
	</table>
</div>
<script type="text/javascript">
require(["sbt/connections/CommunityService", "sbt/dom"], 
    function(CommunityService,dom) {
        var createRow = function(invite) {
            var table = dom.byId("invitesTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.appendChild(document.createTextNode(invite.getTitle()));
            tr.appendChild(td);
            td = document.createElement("td");
            td.appendChild(document.createTextNode(invite.getCommunityUuid()));
            tr.appendChild(td);
        };

        var communityService = new CommunityService();
    	communityService.getMyInvites().then(
            function(invites) {
                if (invites.length == 0) {
                    text = "You do not have any outstanding invites.";
                } else {
                    for(var i=0; i<invites.length; i++){
                        var invite = invites[i];
                        createRow(invite);
                    }
                }
            },
            function(error) {
            	console.log(error);
            }       
    	);
    }
);
</script>