require(["sbt/connections/BookmarkService", "sbt/dom"], 
    function(BookmarkService, dom) {
	    var createRow = function(serviceConfig) {
	        var table = dom.byId("blogsTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        dom.setText(td, serviceConfig.getTitle());
	        tr.appendChild(td);
	        td = document.createElement("td");
	        dom.setText(td, serviceConfig.getAlternateUrl());
	        tr.appendChild(td);
	        td = document.createElement("td");
	        dom.setText(td, serviceConfig.getAlternateSSLUrl());
	        tr.appendChild(td);
	    };
    
    	var connectionsService = new BookmarkService();
    	var promise = connectionsService.getServiceConfigEntries();
    	promise.then(
            function(serviceConfigs){
                if (serviceConfigs.length == 0) {
                    text = "Service configs returned no results.";
                    dom.setText("content", text);
                } else {
                    for(var i=0; i<serviceConfigs.length; i++){
                        var serviceConfig = serviceConfigs[i];
                        createRow(serviceConfig);
                    }
                }
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);