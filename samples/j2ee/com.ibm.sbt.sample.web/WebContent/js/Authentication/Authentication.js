require(["sbt/dom", "sbt/Endpoint", "sbt/config"], function(dom, Endpoint, config) {
	
	/*creating table*/
	var table = document.createElement("table");
	table.setAttribute("class", "table table-bordered table-striped");
	document.getElementById("content").appendChild(table);
	/*creating table*/
	
	/*creating header rows*/
	var headRow = document.createElement("tr");
	headRow.setAttribute("class", "label label-info");
	table.appendChild(headRow);
	var endpointNameTH = document.createElement("th");
	var loginStatusTH = document.createElement("th");
	var loginActionTH = document.createElement("th");
	headRow.appendChild(endpointNameTH);
	headRow.appendChild(loginStatusTH);
	headRow.appendChild(loginActionTH);
	endpointNameTH.setAttribute("id", "thEPName");
	loginStatusTH.setAttribute("id", "thLoginStatus");
	loginStatusTH.setAttribute("id", "thLoginStatus");
	loginActionTH.setAttribute("id", "thLoginAction");
	dom.setText("thEPName", "Endpoint");
	dom.setText("thLoginStatus", "Login Statusss");
	dom.setText("thLoginAction", "Login Action");
	/*creating heared rows*/
	
	/*creating rows*/
	var endpoints = sbt.Endpoints;
	for (var endpointName in endpoints) {
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
		td3.appendChild(logoutButton);
		logoutButton.setAttribute("id", "logout"+endpointName);
		logoutButton.setAttribute('name',endpointName);
		logoutButton.setAttribute('type','button');
		logoutButton.setAttribute('value','Logout');
		logoutButton.setAttribute('class','btn');
		var loginButton = document.createElement("input");
		td3.appendChild(loginButton);
		loginButton.setAttribute("id", "login"+endpointName);
		loginButton.setAttribute('name',endpointName);
		loginButton.setAttribute('type','button');
		loginButton.setAttribute('value','Login');
		loginButton.setAttribute('class','btn');
		/*creating login logout buttons*/
		
		var ep = Endpoint.find(endpointName);
		if(ep.isAuthenticated){
			loginButton.style.display = "none";
			dom.setText("td2"+endpointName, "Logged in");
			logoutButton.onclick= function (){
				Endpoint.find(this.name).logout({
					callback: function(logoutResult){
						document.location.reload()
					}
				}); 
			};
		}else{
			logoutButton.style.display = "none";
			dom.setText("td2"+endpointName, "Not Logged in");
			loginButton.onclick= function (){
				Endpoint.find(this.name).authenticate(false, {
					callback: function(loginResult){
					}
				}); 
			};
		}
	}
	/*creating rows*/
});
