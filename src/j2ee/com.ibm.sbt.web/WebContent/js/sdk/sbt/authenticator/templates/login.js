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
    var agrsMap = getArgsMap();// get map of query string arguments
    var actionURL = decodeURIComponent(agrsMap.actionURL);
    var loginUi = decodeURIComponent(agrsMap.loginUi);
    if (loginUi.length == 0) {
        loginUi = "mainWindow";
    }
    if (loginUi == "popup") {
        contentForm.action = actionURL + "?loginUi=popup&redirectURLToLogin="
                + encodeURIComponent(document.URL);
    } else if (loginUi == "mainWindow") {
        var redirectURL = agrsMap.redirectURL;
        contentForm.action = actionURL
                + "?loginUi=mainWindow&redirectURLToLogin="
                + encodeURIComponent(document.URL) + "&redirectURL="
                + encodeURIComponent(redirectURL);
    }
    contentForm.submit();
}

function cancelOnClick() {
    var agrsMap = getArgsMap();// get map of query string arguments
    var redirectURL = decodeURIComponent(agrsMap.redirectURL);
    var loginUi = decodeURIComponent(agrsMap.loginUi);
    if (loginUi == "popup") {
    	if(window.opener.sbt.cancel){
        	window.opener.sbt.cancel();
            delete window.opener.sbt.cancel;
        }
        delete window.opener.sbt.callback;
        window.close();
    } else {
        window.location.href = redirectURL;
    }
}

function onLoginPageLoad() {
    var agrsMap = getArgsMap();// get map of query string arguments
    var showWrongCredsMessage = agrsMap.showWrongCredsMessage;
    if (showWrongCredsMessage == "true") {
        document.getElementById("wrongCredsMessage").style.display = "block";
    }
}

function getArgsMap() {
    try {
        var qString = location.search.substring(1);// getting query string args
        var qStringParams = qString.split("&");// getting array of all query
                                                // string arg key value pairs
        var agrsMap = {};
        for (i = 0; i < qStringParams.length; i++) {
            var argArray = qStringParams[i].split("=");
            agrsMap[argArray[0]] = argArray[1];
        }
        return agrsMap;
    } catch (err) {
        console.log("Error making agrs map in login.js " + err);
    }
}