//Replace this with your connetion server URL and context root (if you have one)
var conServer = '';

gadgets.util.registerOnLoadHandler(function() {
  var prefs = new gadgets.Prefs();
  conServer = prefs.getString('connections_url');
  if(conServer.length != 0) {
    document.getElementById('post').onclick = post;
    checkAuthorize();
  } else {
    document.getElementById('loading').style.display = 'none';
    showError('You must set a Connections server URL in order to use this gadget!'); 
  }
});

function getMakeRequestParams() {
  var params = {};
  
  //Make sure you specify OAuth 2 as the authorization type
  params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.OAUTH2;

  //The value of service name must match the name specified in the OAuth2 section of
  //the gadget XML
  params[gadgets.io.RequestParameters.OAUTH_SERVICE_NAME] = "connections";
  return params;
};

function checkAuthorize() {
  var params = getMakeRequestParams();
  params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.GET;

  var callback = function(response) {
    if(response.oauthApprovalUrl) {
      document.getElementById('loading').style.display = 'none';
      document.getElementById('authorize').style.display = 'block';
      var onOpen = function() {
      };
      var onClose = function() {
        //After the popup window closes call this function again to see if the OAuth
        //dance was performed successfully
        checkAuthorize();
      };
      var popup = new gadgets.oauth.Popup(response.oauthApprovalUrl,
      null, onOpen, onClose);
      document.getElementById('authorizeLink').onclick = popup.createOpenerOnClick();
    } else if (response.data) {
      //We are authorized, now go ahead an display the follow UI
      document.getElementById('loading').style.display = 'none';
      document.getElementById('authorize').style.display = 'none';
      document.getElementById('postUi').style.display = 'block';
    } else {
      document.getElementById('loading').style.display = 'none';
      showError(gadgets.json.stringify(response));
    }
  };
  gadgets.io.makeRequest(conServer + '/connections/opensocial/oauth/rest/people/@me/@self', callback, params);
};

/**
 * Makes the request to post to the Connections activity stream
 */
function post() {
  updateStatus('');
  var activity = document.getElementById('activity').value;
  
  var params = {};
  params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.JSON;
  
  //Make sure you specify OAuth 2 as the authorization type
  params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.OAUTH2;
  params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;
  
  //The value of service name must match the name specified in the OAuth2 section of
  //the gadget XML
  params[gadgets.io.RequestParameters.OAUTH_SERVICE_NAME] = "connections";

  params[gadgets.io.RequestParameters.HEADERS] = {
    "Content-Type" : "application/json; charset=UTF-8",
     "Accept" : "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
  };
  activityEntry.content = document.getElementById('activity').value;
  activityEntry.connections.rollupid = 'connectionsGadget' + new Date().getTime() / 1000;
  activityEntry.object.id = 'statusUpdate' + new Date().getTime() / 1000;
  params[gadgets.io.RequestParameters.POST_DATA] = gadgets.json.stringify(activityEntry);
  var self = this;
  var callback = function(response) {
    if(response.oauthApprovalUrl) {
      var onOpen = function() {
      };
      var onClose = function() {
        post();
      };
      var popup = new gadgets.oauth.Popup(response.oauthApprovalUrl,
      null, onOpen, onClose);
      document.getElementById('loading').style.display = 'none';
      document.getElementById('postUi').style.display = 'none';
      document.getElementById('authorize').style.display = 'block';
      document.getElementById('authorizeLink').onclick = popup.createOpenerOnClick();
    } else if (response.data) {
      updateStatus('Success!')
    } else {
      showError(gadgets.json.stringify(response));
    }
  };

  gadgets.io.makeRequest(conServer + '/connections/opensocial/oauth/rest/activitystreams/@me/@all', 
                         callback, params);
};

function updateStatus(update) {
  document.getElementById('status').innerHTML = update;
}

function showError(errorText) {
  document.getElementById('errorText').innerHTML = errorText;
  document.getElementById('error').style.display = 'block';
  gadgets.window.adjustHeight();
}

var activityEntry =     
{
    "generator": {
      "image": {
        "url": "/homepage/nav/common/images/iconProfiles16.png"
      }, 
      "id": "demoApp123", 
      "displayName": "Demo Application", 
      "url": "http://www.ibm.com/"
    }, 
    "actor": {
      "id": "@me"
    }, 
    "verb": "post", 
    "title": "${share}", 
    "content": "OpenSocial gadget posted from the OpenSocial Explorer", 
    "updated": "2012-01-01T12:00:00.000Z", 
    "object": {
      "summary": "Status Update", 
      "objectType": "status", 
      "id": "statusUpdate", 
      "displayName": "Status Update", 
      "url": "http://www.ibm.com/"
    }, 
    "target": {
      "summary": "IBM Connections", 
      "objectType": "IBM Connections", 
      "id": "IBMConnections", 
      "displayName": "IBM Connections", 
      "url": "www.ibm.com/software/lotus/products/connections/"
    },  
    "connections": {
      "rollupid": ""
    }
  };