require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
	var ep = Endpoint.find("connections");
	if(ep.isAuthenticated){
		dom.setText("connectionsLoginStatus","Logged in");
		dom.byId("connectionsLogin").style.display = "none";
		dom.byId("connectionsLogout").style.display = "inline";
	}else{
		dom.setText("connectionsLoginStatus","Not Logged in");
		dom.byId("connectionsLogin").style.display = "inline";
		dom.byId("connectionsLogout").style.display = "none";
	}
});

function login(loginUi) {
	require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
		config.Properties["loginUi"] = loginUi;
		Endpoint.find("connections").authenticate({
			success: function(response){
				dom.setText("connectionsLoginStatus","Logged in");
				dom.byId("connectionsLogin").style.display = "none";
				dom.byId("connectionsLogout").style.display = "inline";	
			},
			cancel: function(){
				dom.setText("content","You need to Login to Proceed");
			}
		});
	});
}

function logout() {
	require(['sbt/Endpoint',"sbt/dom"], function(Endpoint,dom) {
		Endpoint.find("connections").logout({
			success: function(response){
				dom.setText("connectionsLoginStatus","Not Logged in");
				dom.byId("connectionsLogin").style.display = "inline";
				dom.byId("connectionsLogout").style.display = "none";
			}	
		});
	});
}