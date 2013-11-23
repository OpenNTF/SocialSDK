function getArgsMap(){
		try{
			var qString = location.search.substring(1);//getting query string args
			var qStringParams=qString.split("&");//getting array of all query string arg key value pairs
			var agrsMap = {};
			for(i=0;i<qStringParams.length;i++){
					var argArray = qStringParams[i].split("=");
					agrsMap[argArray[0]] = argArray[1];
			}
			return agrsMap;
		}catch(err){
			console.log("Error making agrs map in login.js "+err);
		}
	}