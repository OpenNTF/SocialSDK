require(["sbt/dom","sbt/config"], function(dom,config) {
	var ep = config.findEndpoint("smartcloudBasic");
	if(ep.isAuthenticated){
		dom.setText("smartcloudLoginStatus","You are authenticated");
		dom.byId("smartcloudLogin").style.display = "none";
		dom.byId("smartcloudLogout").style.display = "inline";
	}else{
		dom.setText("smartcloudLoginStatus","You are not authenticated");
		dom.byId("smartcloudLogin").style.display = "inline";
		dom.byId("smartcloudLogout").style.display = "none";
	}
});

function login(loginUi) {
	require(["sbt/dom","sbt/config"], function(dom,config) {
		config.Properties["loginUi"] = loginUi;
		config.findEndpoint("smartcloudBasic").authenticate().then(
			function(response){
				dom.setText("smartcloudLoginStatus","You are authenticated");
				dom.byId("smartcloudLogin").style.display = "none";
				dom.byId("smartcloudLogout").style.display = "inline";	
			},
			function(){
				dom.setText("content","You need to Login to Proceed");
			}
		);
	});
}

function logout() {
	require(['sbt/config',"sbt/dom"], function(config,dom) {
		config.findEndpoint("smartcloudBasic").logout().then(
			function(response){
				dom.setText("smartcloudLoginStatus","You are not authenticated");
				dom.byId("smartcloudLogin").style.display = "inline";
				dom.byId("smartcloudLogout").style.display = "none";
			},
			function(){
				dom.setText("content","Failed to Logout");
			}	
		);
	});
}