var selectedSmartCloudCommunityUuid = null;
require(["sbt/connections/FileService", 
         "sbt/dom",
         "sbt/connections/CommunityService",
         "sbt/config",
         "sbt/Endpoint"], 
         
    function(FileService,dom, CommunityService, config, Endpoint) {
		// TODO: Remove hardcoded ID
		var communityId = "c57821be-1511-4ba3-8284-cb773513a24b";
		
		// Community Service
		var communityService = new CommunityService({
			endpoint: config.findEndpoint("smartcloud")
		});
		
		endpoint = config.findEndpoint("smartcloud");
		
		// Set action listener for the dropdown box which lists
		// the user's SmartCloud communities
		var commList = document.getElementById("commList");
		commList.onclick = function(evt) {
			selectedSmartCloudCommunityUuid = commList.options[commList.selectedIndex].value;
		};
		
		// Fetch user's SmartCloud communities
		communityService.getMyCommunities().then(
	            function(communities) {
	                if (communities.length == 0) {
	                    text = "You are not a member of any communities.";
	                } else {
	                	var commList = document.getElementById("commList");
	                    for(var i=0; i<communities.length; i++){
	                        var community = communities[i];
	                        var title = community.getTitle(); 
	                        var communityUuid = community.getCommunityUuid(); 
	                        var option = document.createElement("option");
	                        option.value = communityUuid;
	                        option.innerHTML = title;
	                        commList.appendChild(option);
	                    }
	                }
	            },
	            function(error) {
	                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
	            }       
	    	);
		
		// Set action listeners for the modal dialog
		var btnCloseDialog = dom.byId("closeDialog");
		btnCloseDialog.onclick = function(evt) {
			closeDialog();
		};
		
		// Set action listener for the X button which closes the modal dialog
		var btnX = dom.byId("btnX");
		btnX.onclick = function(evt) {
			closeDialog();
		};
		
		// Set action listener for the file sharing button in the modal dialog
		var btnShareFile = dom.byId("shareFile");
		btnShareFile.onclick = function(evt) {
			// Share file
			
			// Close dialog box
			closeDialog();
		};
	
        var createRow = function(i) {
            var table = dom.byId("filesTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.setAttribute("id", "title"+i);
            tr.appendChild(td);
            td = document.createElement("td");
            td.setAttribute("id", "id"+i);
            tr.appendChild(td);
            td = document.createElement("td");
            td.setAttribute("id", "action"+i);
            tr.appendChild(td);
        };

        var fileService = new FileService();
    	fileService.getCommunityFiles(communityId).then(
            function(files) {
                if (files.length == 0) {
                	dom.setText("content", "No files have been shared with this community.");
                } else {
                    for(var i=0; i<files.length; i++){
                        var file = files[i];
                        createRow(i);
                        dom.setText("title"+i, file.getTitle()); 
                        dom.setText("id"+i, file.getFileId()); 
                        var action = dom.byId("action"+i);
                        action.innerHTML = "<input id=\"btnShare" + i + "\" value=\"Share\" class=\"btn btn-primary\" type=\"submit\">";
                        var btnShare = dom.byId("btnShare" + i);
                       
                        btnShare.onclick = function(evt) {
                        	var dialog = document.getElementById("shareDialog");
                        	dialog.style.display = "block";
                        	document.getElementById("fileToShare").innerHTML = file.getTitle();
                        };

                    }
                }
            },
            function(error) {
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);

function closeDialog() {
	var dialog = document.getElementById("shareDialog");
	dialog.style.display = "none";
}