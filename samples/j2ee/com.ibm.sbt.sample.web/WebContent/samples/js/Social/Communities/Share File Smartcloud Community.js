var selectedSmartCloudCommunityUuid = null;
var fileToShare = null;
require(["sbt/connections/FileService", 
         "sbt/dom",
         "sbt/connections/CommunityService",
         "sbt/config",
         "sbt/Endpoint"], 
         
    function(FileService,dom, CommunityService, config, Endpoint) {
		var communityId = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Share_File_Smartcloud_Community}";
	
		// Community Service
		var communityService = new CommunityService({ endpoint: "smartcloud" }); 
		
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
	                    dom.setText("community", "You are not a member of any communities.");
	                } else {
	                	var commList = document.getElementById("commList");
	                    for(var i=0; i<communities.length; i++){
	                        var community = communities[i];
	                        var title = community.getTitle(); 
	                        var communityUuid = community.getCommunityUuid(); 
	                        var option = document.createElement("option");
	                        option.value = communityUuid;
	                        optiontd.appendChild(dom.createTextNode(title));
	                        commList.appendChild(option);
	                    }
	                }
	            },
	            function(error) {
	            	handleError(dom, error);
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
		
		dom.byId("uploadBtn").onclick = function(evt) {
			// Share file
			var smartCloudFileService = new FileService({
				endpoint : "smartcloud"
			}); 
			uploadFile(smartCloudFileService, dom);

			// Close dialog box
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

			// Download file
			fileToShare.download();
			
			// Hide sharing dialog
			download.style.display = "none";
            footer.style.display = "none";
			
			 // Display upload dialog
            var msg = document.getElementById("upload");
            msg.style.display = "block";
		};
	
        var createRow = function(i, file) {
            var table = dom.byId("filesTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.setAttribute("id", "title"+i);
            tdtd.appendChild(dom.createTextNode(file.getTitle()));
            tr.appendChild(td);
            td = document.createElement("td");
            td.setAttribute("id", "id"+i);
            tdtd.appendChild(dom.createTextNode(file.getFileId()));
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
                        createRow(i, file);
                
                        var actionID = "action" + i;
                        dom.setText(actionID, "<input id=\"btnShare" + i + "\" value=\"Share\" class=\"btn btn-primary\" type=\"submit\">");
                        var btnShare = dom.byId("btnShare" + i);
                        
                        btnShare.onclick = function(evt) {
                        	var dialog = document.getElementById("shareDialog");
                        	dialog.style.display = "block";
                        	dom.setText("fileToShare", file.getTitle());
                        	fileToShare = file;
                        };

                    }
                }
            },
            function(error) {
            	handleError(dom, error);
            }       
    	);
    }
);

function uploadFile(fileService, dom) {
	dom.byId("loading").style.visibility = "visible";
	fileService.uploadFile("your-files", {
		// additional parameters to add file metadata			
		visibility : "private"
	}).then(function(file) {

        // Display success message
        var msg = document.getElementById("confirmationMessage");
        msg.style.display = "block";
        
		// Share file with SmartCloud community
        alert(selectedSmartCloudCommunityUuid);
        fileService.shareFileWithCommunities(file.getFileId(), [selectedSmartCloudCommunityUuid]).then(function(data) {
        	 dom.byId("loading").style.visibility = "hidden";
             
             // Display first part of sharing dialog
             var download = document.getElementById("download");
             download.style.display = "block";
             
             var footer = document.getElementById("dialogFooter");
             footer.style.display = "block";
             
             var upload = document.getElementById("upload");
             upload.style.display = "none";

    	}, function(error) {
    		handleError(dom, error);
    	});         
        
       
        
	}, function(error) {
		// TODO remove alert
		alert(error);
//				handleError(dom, error);
//				dom.byId("loading").style.visibility = "hidden";
	});
}

function closeDialog() {
	var dialog = document.getElementById("shareDialog");
	dialog.style.display = "none";
}

function handleError(dom, error) {
	dom.setText("error", "Error: " + error.message);
}
