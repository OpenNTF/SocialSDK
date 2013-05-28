require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
	var ep = Endpoint.find("domino");
	if(ep.isAuthenticated){
		dom.setText("dominoLoginStatus","You are authenticated");
		dom.byId("dominoLogin").style.display = "none";
		dom.byId("dominoLogout").style.display = "inline";
	}else{
		dom.setText("dominoLoginStatus","You are not authenticated");
		dom.byId("dominoLogin").style.display = "inline";
		dom.byId("dominoLogout").style.display = "none";
	}
});

function login(loginUi) {
	require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
		config.Properties["loginUi"] = loginUi;
		Endpoint.find("domino").authenticate().then(
			function(response){
				dom.setText("dominoLoginStatus","You are authenticated");
				dom.byId("dominoLogin").style.display = "none";
				dom.byId("dominoLogout").style.display = "inline";	
			},
			function(){
				dom.setText("content","You need to Login to Proceed");
			}
		);
	});
}

function logout() {
	require(['sbt/Endpoint',"sbt/dom"], function(Endpoint,dom) {
		Endpoint.find("domino").logout().then(
			function(response){
				dom.setText("dominoLoginStatus","You are not authenticated");
				dom.byId("dominoLogin").style.display = "inline";
				dom.byId("dominoLogout").style.display = "none";
			},
			function(){
				dom.setText("content","Failed to Logout");
			}	
		);
	});
}