require(["sbt/dom", "sbt/config"], function(dom, config) {
	var table = dom.byId('authenticationTable');
	/*creating rows*/
	var endpoints = config.Endpoints;
	for (var endpointName in endpoints) {
		var ep = config.findEndpoint(endpointName);
		if(!ep || ep.invalid) {
			continue;
		}
		var tr = document.createElement("tr");
		table.appendChild(tr);
		var td = document.createElement("td");
		var td2 = document.createElement("td");
		var td3 = document.createElement("td");
		td.setAttribute("id", "td"+endpointName);
		td2.setAttribute("id", "td2"+endpointName);
		td3.setAttribute("id", "td3"+endpointName);
		tr.appendChild(td);
		tr.appendChild(td2);
		tr.appendChild(td3);
		dom.setText("td"+endpointName, endpointName);
		
		/*creating login logout buttons*/
		var logoutButton = document.createElement("input");
		logoutButton.setAttribute("id", "logout"+endpointName);
		logoutButton.setAttribute('name',endpointName);
		logoutButton.setAttribute('type','button');
		logoutButton.setAttribute('value','Logout');
		logoutButton.setAttribute('class','btn');
		td3.appendChild(logoutButton);
		var loginButton = document.createElement("input");
		loginButton.setAttribute("id", "login"+endpointName);
		loginButton.setAttribute('name',endpointName);
		loginButton.setAttribute('type','button');
		loginButton.setAttribute('value','Login');
		loginButton.setAttribute('class','btn');
		td3.appendChild(loginButton);
		/*creating login logout buttons*/
		
		if(ep.isAuthenticated){
			loginButton.style.display = "none";
			dom.setText("td2"+endpointName, "Logged in");
			logoutButton.onclick= function (){
				// Calling logout. (this.name has endpoint name)
				config.findEndpoint(this.name).logout().then(
					function(logoutResult){
						document.location.reload();
					},
					function(logoutResult){
						dom.setText("td2"+endpointName, "Logged in - failed to logout");
					}
				); 
			};
		}else{
			logoutButton.style.display = "none";
			dom.setText("td2"+endpointName, "Not Logged in");
			loginButton.onclick= function (){
				config.findEndpoint(this.name).authenticate();    // Calling authenticate. (this.name has endpoint name)
			};
		}
	}
	/*creating rows*/
});
