<%@page import="com.ibm.sbt.smartcloud.Util"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<title>Smart Cloud Sample</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- Using Dojo as the underlying toolkit in this sample but it could also be JQuery -->
<script type="text/javascript" src="/sbt.dojo180/dojo/dojo.js" djConfig="parseOnLoad: true"></script>
<!-- Configure the SDK JavaScript library and notify that Dojo v1.8.0 is being used -->
<script type="text/javascript" src="/smartcloud.webapp/library?lib=dojo&ver=1.8.0"></script>

<!-- This add's the CSS required for the Smart Cloud navigation bar -->
<link rel="stylesheet" href="<%=Util.getThemeUrl()%>" type="text/css" />
</head>

<body class="lotusui30_body">
<!-- Include the Smart Cloud navigation bar -->
<div class="lotusui30 lotusui30_fonts scloud3">
	<script type="text/javascript" src="<%=Util.getNavBarUrl()%>"></script>
</div>

<script type="text/template" id="communityRow">
<tr class="${rowClass}" >
	<td class=lotusFirstCell width=35>
		<div>
			<input id="${uid}" name="${uid}" value="${uid}" data-dojo-attach-event="onclick: handleCheckBox" type="checkbox" aria-label="${title}"></input>
		</div>
	</td>
	<td class=lotusFirstCell>
		<h4>
			<span dojoAttachPoint="placeLinkNode">${title}</a></span>
		</h4>
	</td>
	<td class="lotusAlignRight lotusLastCell lotusTiny">
	</td>
</tr>
</script>

<div role="form">
	<button class="" id="deleteBtn">Delete Selected Communities</button>
	<br/>
	<div id="gridDiv"></div>
	<hr/>
	<div id="statusDiv"></div>
</div>

<script type="text/javascript">
require(["sbt/dom", "sbt/connections/controls/communities/CommunityGrid", "sbt/connections/CommunityService"], function(dom, CommunityGrid, CommunityService) {
    var grid = new CommunityGrid({
         type: "my",
         pageSize: 20,
         hideSorter: true
    });
             
    var domNode = dom.byId("communityRow");
    var CustomCommunityRow = domNode.text || domNode.textContent;
    grid.renderer.template = CustomCommunityRow;

    dom.byId("gridDiv").appendChild(grid.domNode);
    
    grid.update();
    
	var communityService = new CommunityService();

    dom.byId("deleteBtn").onclick = function(evt) {
        var communities = grid.getSelected();
        
        var str = "";
        for (i in communities) {
        	str += (str.length == 0) ? "    " : "\n    ";
            str += communities[i].data.getValue("title");
        }
        if (str.length == 0) {
        	alert("Select the communities you want to delete.");
        } else {
        	var ret = confirm("Are you sure you want to deleted the following:\n" +
        			str + "\n It is not possible to restore deleted communities!");
        	if (ret) {
        		dom.byId("deleteBtn").disabled = true;
        		deleteCommunities(grid, communityService, dom, communities);
        	}
        }
        
    };
});

function deleteCommunities(grid, communityService, dom, communities) {
	var community = communities.pop();
	if (!community) {
		dom.byId("deleteBtn").disabled = false;
		dom.byId("statusDiv").innerHTML = "Communities deleted";
		grid.refresh();
		return;
	}
	
    var uid = community.data.getValue("uid");
    var communityUuid = extractCommunityUuid(communityService, uid);
    var title = community.data.getValue("title");
    
    var statusDiv = dom.byId("statusDiv");
    statusDiv.innerHTML = "Deleting community: "+title;
    
    communityService.deleteCommunity(communityUuid).then(
        function(communityUuid) {
        	statusDiv.innerHTML = "Sucessfully deleted community: "+title;
        	deleteCommunities(grid, communityService, dom, communities);
        }, function(error) {
            alert("Unable to delete '"+ title + "' aborting operation.");
    		dom.byId("deleteBtn").disabled = false;
    		dom.byId("statusDiv").innerHTML = "Unable to delete '"+ title + "' aborting operation.";
    		grid.refresh();
        }
    );
}

function extractCommunityUuid(service, uid) {
    if (uid && uid.indexOf("http") == 0) {
        return service.getUrlParameter(uid, "communityUuid");
    } else {
        return uid;
    }
};
</script>

</body>

</html>