require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
	var ep = Endpoint.find("connections");
	if(ep.isAuthenticated){
		dom.setText("connectionsLoginStatus","You are authenticated");
		dom.byId("connectionsLogin").style.display = "none";
		dom.byId("connectionsLogout").style.display = "inline";
	}else{
		dom.setText("connectionsLoginStatus","You are not authenticated");
		dom.byId("connectionsLogin").style.display = "inline";
		dom.byId("connectionsLogout").style.display = "none";
	}
});

function login(loginUi) {
	require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
		config.Properties["loginUi"] = loginUi;
		Endpoint.find("connections").authenticate({
			load: function(response){
				dom.setText("connectionsLoginStatus","You are authenticated");
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
			load: function(response){
				dom.setText("connectionsLoginStatus","You are not authenticated");
				dom.byId("connectionsLogin").style.display = "inline";
				dom.byId("connectionsLogout").style.display = "none";
			}	
		});
	});
}