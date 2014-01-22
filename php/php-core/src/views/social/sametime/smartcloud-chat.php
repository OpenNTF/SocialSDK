<script type="text/javascript">
var wChatPopup = null;

function SCLogin() {

	var width = 2350;
	var height = 2550;

	var left = 99;
	var top = 99;

	URL= "https://apps.na.collabserv.com/manage/account/dashboardHandler/input";
       
	window.open(URL,'SCLogin', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');
}

function SCLogout() {

	var width = 350;
	var height = 350;

	var left = 99;
	var top = 99;

	URL= "https://apps.na.collabserv.com/manage/account/logoutSSO";
       
	window.open(URL,'SCLogout', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');
}

function Chat(URL) {

	var width = 350;
	var height = 550;

	var left = 99;
	var top = 99;

	if (wChatPopup && !wChatPopup.closed) {
    		wChatPopup.focus();
  		} else {
		wChatPopup = window.open(URL,'wchat','height=550,width=350,left=99,top=99,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=yes');
  	}
}

function JoinMeeting() {
	var MeetingID=document.getElementById('mtgid');

	var width = 2350;
	var height = 2550;

	var left = 10;
	var top = 10;

	//
	// If using SmartCloud - use this one
	//
	var URL='https://apps.na.collabserv.com/meetings/join?id=' + MeetingID.value;
	//
	// If using Greenhouse or on premises ST Meetings - use this one
	//

	window.open(URL,'wOldmeet', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');
}

function HostMeeting(URL) {

	var width = 2350;
	var height = 2550;

	var left = 10;
	var top = 10;

	window.open(URL,'wNewmeet', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');
}
</script>


<a href="javascript:Chat('https://webchat.na.collabserv.com/stwebclient/popup.jsp?lang=en-us');">Start a Chat</a><br/>
Join Meeting:
<input id='mtgid' type='text'  value='' size='18' />
<input type='button' onclick='JoinMeeting()' value = 'OK'/>