require(["sbt/connections/BookmarkService", "sbt/dom"], 
    function(BookmarkService, dom) {
	    var createRow = function(displayLanguage) {
	        var table = dom.byId("languagesTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        dom.setText(td, displayLanguage.label);
	        tr.appendChild(td);
	        td = document.createElement("td");
	        dom.setText(td, displayLanguage.term);
	        tr.appendChild(td);
	    };
    
    	var connectionsService = new BookmarkService();
    	var promise = connectionsService.getServiceConfigEntries();
    	promise.then(
            function(){
            	var displayLanguagesArray = promise.summary.displayLanguages;
                for(var i=0; i<displayLanguagesArray.length; i++){
                    var displayLanguage = displayLanguagesArray[i];
                    createRow(displayLanguage);
                }
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);