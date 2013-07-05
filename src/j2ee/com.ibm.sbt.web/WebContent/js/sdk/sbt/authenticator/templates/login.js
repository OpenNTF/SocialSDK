/*
 * © Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
function submitOnClick(contentForm) {
    if (contentForm.username.value == "" || contentForm.password.value == "") {
        document.getElementById("wrongCredsMessage").style.display = "block";
        return;
    }
    var argsMap = getArgsMap();// get map of query string arguments
    var actionURL = decodeURIComponent(argsMap.actionURL);
    var loginUi = decodeURIComponent(argsMap.loginUi);
    if (loginUi.length == 0) {
        loginUi = "mainWindow";
    }
    if (loginUi == "popup") {
        contentForm.action = actionURL + "?loginUi=popup&redirectURLToLogin="
                + encodeURIComponent(document.URL)+"&endpointAlias="+window.globalEndpointAlias;
    } else if (loginUi == "mainWindow") {
        var redirectURL = argsMap.redirectURL;
        contentForm.action = actionURL
                + "?loginUi=mainWindow&redirectURLToLogin="
                + encodeURIComponent(document.URL) + "&redirectURL="
                + encodeURIComponent(redirectURL)+"&endpointAlias="+window.globalEndpointAlias;
    }
    contentForm.submit();
}

function cancelOnClick() {
    var argsMap = getArgsMap();// get map of query string arguments
    var redirectURL = decodeURIComponent(argsMap.redirectURL);
    var loginUi = decodeURIComponent(argsMap.loginUi);
    if (loginUi == "popup") {
        if(window.cancel){
            window.cancel();
            delete window.cancel;
        }
        window.close();
    } else {
        window.location.href = redirectURL;
    }
}

function onLoginPageLoad() {
    var argsMap = getArgsMap();// get map of query string arguments
    var showWrongCredsMessage = argsMap.showWrongCredsMessage;
    if (showWrongCredsMessage == "true") {
        document.getElementById("wrongCredsMessage").style.display = "block";
    }
    if(window.globalLoginFormStrings){
        var loginForm = window.globalLoginFormStrings;
    	document.getElementById('wrongCredsMessage').innerHTML = loginForm.wrong_creds_message;
    	document.getElementById('basicLoginFormUsername').innerHTML = loginForm.username;
    	document.getElementById('basicLoginFormPassword').innerHTML = loginForm.password;
    	document.getElementById('basicLoginFormOK').value = loginForm.login_ok;
    	document.getElementById('basicLoginFormCancel').value = loginForm.login_cancel;
    }
//    else{
//    	require(["sbt/i18n!sbt/nls/loginForm"], function(loginForm) {
//    		document.getElementById('wrongCredsMessage').innerHTML = loginForm.wrong_creds_message;
//        	document.getElementById('basicLoginFormUsername').innerHTML = loginForm.username;
//        	document.getElementById('basicLoginFormPassword').innerHTML = loginForm.password;
//        	document.getElementById('basicLoginFormOK').value = loginForm.login_ok;
//        	document.getElementById('basicLoginFormCancel').value = loginForm.login_cancel;
//		});
//    }

}

function getArgsMap() {
    try {
        var qString = location.search.substring(1);// getting query string args
        var qStringParams = qString.split("&");// getting array of all query
                                                // string arg key value pairs
        var argsMap = {};
        var i;
        for (i = 0; i < qStringParams.length; i++) {
            var argArray = qStringParams[i].split("=");
            argsMap[argArray[0]] = argArray[1];
        }
        return argsMap;
    } catch (err) {
        console.log("Error making agrs map in login.js " + err);
    }
}