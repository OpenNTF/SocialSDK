//if popup or mainWindow login UI is used, then loginPage property can be used to change default login form.
require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
	var ep = Endpoint.find("connections");
	config.Properties["loginUi"] = "popup";
	config.Properties["loginPage"] = "/sbt/authenticator/templates/ConnectionsLogin.html";
	ep.authenticate({ forceAuthentication: true }).then(
		function(response){
			dom.setText("content","You successfully logged in to connections");	
		},
		function(){
			dom.setText("content","You are not logged in to connections");
		}
	);
});