var selectedSmartCloudCommunityUuid = null;
var fileToShare = null;
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
	                    dom.byId("community").innerHTML = "You are not a member of any communities.";
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
	       // Display first part of sharing dialog
            var download = document.getElementById("download");
            download.style.display = "block";
            
            var footer = document.getElementById("dialogFooter");
            footer.style.display = "block";
			
			// Hide wait message
            var msg = document.getElementById("confirmationMessage");
            msg.style.display = "none";
			
			// Share file
			var smartCloudFileService = new FileService({
				endpoint: config.findEndpoint("smartcloud")
			});
			
			// Download file
			fileToShare.download();
			
			// Hide sharing dialog
			download.style.display = "none";
            footer.style.display = "none";
			
			 // Display upload dialog
            var msg = document.getElementById("upload");
            msg.style.display = "block";
			
			// "your-files" is the ID of the HTML5 File Control. Refer to Upload File.html
            smartCloudFileService.uploadFile("your-files", {
				// additional parameters to add file metadata			
				visibility : "public"
			}).then(function(file) {
				alert("TEST!!!");
	            // Display success message
	            var msg = document.getElementById("confirmationMessage");
	            msg.style.display = "block";
//	            file.getFileId();
//	            shareFileWithCommunities : function(fileId, communityIds, args) 
				
			}, function(error) {
				// TODO remove alert
				alert(error);
//				handleError(dom, error);
//				dom.byId("loading").style.visibility = "hidden";
			});
			
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
                        	fileToShare = file;
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