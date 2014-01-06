gadgets.util.registerOnLoadHandler(function() {
  document.getElementById('post').onclick = post;
});

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
  activityEntry.connections.rollupid = document.getElementById('activity').value;
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
      popup.createOpenerOnClick()();
    } else if (response.data) {
      updateStatus('Success!')
    } else {
      updateStatus('ERROR!!!!');
    }
  };

  gadgets.io.makeRequest(asUrl, callback, params);
};

function updateStatus(update) {
  document.getElementById('status').innerHTML = update;
}

var activityEntry =     
{
    "generator": {
      "image": {
        "url": "/homepage/nav/common/images/iconProfiles16.png"
      }, 
      "id": "demoApp", 
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
      "rollupid": "bootstrapEE1348685889763"
    }
  };

var asUrl = 'https://ics-connections.swg.usma.ibm.com/connections/opensocial/oauth/rest/activitystreams/@me/@all';