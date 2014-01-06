function $(x) {
  return document.getElementById(x);
}

function showOneSection(toshow) {
  var sections = [ 'main', 'approval', 'waiting' ];
  for (var i=0; i < sections.length; ++i) {
    var s = sections[i];
    var el = $(s);
    if (s === toshow) {
      el.style.display = "block";
    } else {
      el.style.display = "none";
    }
  }
}

function showProfile(profileEntry) {
  var imgElement = document.createElement('img');
  imgElement.src = 'https://apps.na.collabserv.com/contacts/img/photos/' + profileEntry.photo;
  $('photo').appendChild(imgElement);
  $('name').appendChild(document.createTextNode(profileEntry.fullName));
  $('jobtitle').appendChild(document.createTextNode(profileEntry.jobtitle)); 
  showOneSection('main');
  gadgets.window.adjustHeight();
  
}

function fetchData() {
  var url = "https://apps.lotuslive.com/lotuslive-shindig-server/social/rest/people/@me/@self?format=json";
  var params = {};
  params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.OAUTH;
  params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.GET;
  params['OWNER_SIGNED'] = false;
  params['VIEWER_SIGNED'] = false;
  params[gadgets.io.RequestParameters.OAUTH_SERVICE_NAME] = "SmartCloud";

  gadgets.io.makeRequest(url, function (response) {
    if (response.oauthApprovalUrl) {
      var onOpen = function() {
        showOneSection('waiting');
      };
      var onClose = function() {
        fetchData();
      };
      var popup = new gadgets.oauth.Popup(response.oauthApprovalUrl, null, onOpen, onClose);
      $('personalize').onclick = popup.createOpenerOnClick();
      $('approvaldone').onclick = popup.createApprovedOnClick();
      showOneSection('approval');
    } else if (response.data) {
      showProfile(gadgets.json.parse(response.data).entry);
    } else {
      var whoops = document.createTextNode(
          'OAuth error: ' + response.oauthError + ': ' +
          response.oauthErrorText);
      $('main').appendChild(whoops);
      showOneSection('main');
    }
  }, params);
}

gadgets.util.registerOnLoadHandler(fetchData);