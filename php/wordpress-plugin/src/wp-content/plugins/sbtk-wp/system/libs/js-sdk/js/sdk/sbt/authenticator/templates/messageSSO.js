/*
 * © Copyright IBM Corp. 2013
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

function cancelOnClick() {
    var argsMap = getArgsMap();// get map of query string arguments
    var redirectURL = decodeURIComponent(argsMap.redirectURL);
    var loginUi = decodeURIComponent(argsMap.loginUi);
    if (loginUi == "popup") {
    	opener.location.reload();
        window.close();
    } else {
        window.location.href = redirectURL;
    }
}

function onLoginPageLoad() {
    var argsMap = getArgsMap();// get map of query string arguments
    if(argsMap.loginUi == "popup"){
        var ssoStrings = window.globalSSOStrings;
    	document.getElementById('reloginMessage').appendChild(document.createTextNode(decodeURIComponent(ssoStrings.message)));
    	document.getElementById('ssoLoginFormOK').value = decodeURIComponent(ssoStrings.relogin_button_text);
    }else{
    	document.getElementById('reloginMessage').appendChild(document.createTextNode(decodeURIComponent(argsMap.message)));
    	document.getElementById('ssoLoginFormOK').value = decodeURIComponent(argsMap.relogin_button_text);
    }

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
        console.log("Error making agrs map in messageSSO.js " + err);
    }
}