// Global Variables
var airportCodes = [];
var selectedIndexForFlights = -1;

/**
 * closes the login dialog
 */
require([ "dojo/dom", "dojo/domReady!" ], function(dom) {
	window.closeLoginDialog = function() {
		closeDialog('dialogLogin');
		dom.byId('ipass').value = '';
	};
});

/**
 * Logs in the User
 */
require([ "dojo/dom", "dojo/_base/xhr", "dojo/cookie", "dojo/domReady!" ], function(
		dom, xhr, cookie) {
	window.login = function() {
		// Get the login values
		var uid = dom.byId('uid');
		var pass = dom.byId('ipass');

		// Change Dialogs
		closeLoginDialog();
		openDialog('loginWaitDialog');

		xhr.post({
			content : {
				login : uid,
				password : pass
			},
			url : '/acme.social.sample.dataapp/rest/api/login'
		}).then(
				function(data) {
					cookie("loggedinuser", uid, {
						expires : 5
					});
					closeDialog('loginWaitDialog');
					updateLoggedInUserWelcome();
				},
				function(err) {
					// closeDialog('loginWaitDialog');
					console.log('err on login: ' + err);
					dom.byId('loginStatus').innerHTML(
							'error logging in, response: ' + err);
				});
	};
});

/**
 * Logs out the User
 */
require([ "dojo/dom", "dojo/_base/xhr", "dojo/cookie", "dojo/domReady!" ], function(
		dom, xhr, cookie) {
	window.logout = function() {
		updateLoggedInUserWelcome();
		var loggedinuser = dojo.cookie('loggedinuser');
		if (loggedinuser == null) {
			return;
		}

		xhr.get({
			content : {
				login : loggedinuser
			},
			url : 'rest/api/login/logout'
		}).then(function(data) {
			if (data['response'] != 'loggedout') {
				console.log('Not Logged out');

			} else {
				cookie("loggedinuser", null, {
					expires : -1
				});
				updateLoggedInUserWelcome();
			}
		}, function(err) {
			console.log('error logging out user ' + err);
		});
	};
});

/**
 * update banner with the logged in user welcome
 */
require([ "dojo/dom", "dojo/cookie", "dojo/domReady!" ], function(dom, cookie) {
	window.updateLoggedInUserWelcome = function() {
		var loggedinuser = cookie("loggedinuser");
		if (loggedinuser == null) {
			dom.byId('loggedinwelcome').innerHTML = '';
		} else {
			dom.byId('loggedinwelcome').innerHTML = 'Welcome Back '
					+ loggedinuser;
		}
	};
});

/**
 * maps airport code to airport name
 */
require([ "dojo/_base/array" ], function(array) {
	window.mapAirportCodeToAirportName = function(airportCode) {
		var airportName = '';
		array.filter(airportCodes, function(item) {
			if (item.airportCode == airportCode) {
				airportName = item.airportName;
			}
		});
		return airportName;
	};
});

/**
 * search for flights
 */
require([ "dijit/registry", "dojo/dom", "dojo/domReady!" ], function(registry,
		dom) {
	window.searchForFlights = function() {

	};
});

/**
 * cancel bookings
 */
require([ "dojo/_base/xhr", "dojo/cookie", "dijit/registry", "dojo/dom",
		"dojo/domReady!" ], function(xhr, cookie, registry, dom) {
	window.cancelBookings = function() {
		var loggedinuser = cookie("loggedinuser");
		if (!loggedinuser) {
			openInformationalDialog('Please Login before Proceeding');
		} else if (selectedIndexForFlights == -1) {
			openInformationalDialog('Please select a flight');
		}else {
			// Finally we're good to proceed
			var grid = registry.byId('gridFlights');
			var bookingPK = grid.store.getValue(grid.getItem(selectedIndexForFlights), "pkey");
			xhr.post({
				content : {
					userid : loggedinuser,
					number : bookingPK.id,
					userid : bookingPK.customerId
				},
				url : 'rest/api/bookings/cancelbooking'
			}).then(function(data){
				openInformaitonalDialog('Booking Canceled.');
				createAndStartGrid();
			},function(err){
				openInformationDialog('Error in canceling booking is ' + err);
				console.log(err);
			});
		}

	};
});

/**
 * retrieves the users bookings and dumps the information onto the page in a grid
 */
require(["dojo/_base/xhr","dojo/cookie", "dijit/registry", 'dojox/grid/DataGrid', 'dojo/data/ItemFileWriteStore'],function(xhr,cookie, registry, DataGrid, ItemFileWriteStore){
	window.createAndStartGrid = function(){
		var loggedinuser = cookie("loggedinuser");
		if(loggedinuser){
			xhr.get({
				url : 'rest/api/bookings/byuser/' + loggedinuser,
				handleAs: 'json',
			}).then(function(data){
				var items = { items : data};
				var store = new ItemFileWriteStore({
					data : items
				});
				
				var flightsGrid = registry.byId('gridFlights');
				flightsGrid.setStore(store);
				
				
				
			}, function(err){
				openInformationalDialog('error with the create and start grid ' + err);
				console.log(err);
			});
		}
	};
});

/**
 * listener for cell click and works with the current grid
 */
require(['dojo/on','dojo/dom', 'dijit/registry'], function(on,dom,registry){
	window.addListener = function(store){
		on(store, 'onRowClick', function(e){
			
		});
	};
});

// --------------------------------------------------------------
// Helper Methods
/**
 * closes any dialog
 */
require([ "dijit/registry", "dojo/domReady!" ], function(registry) {
	window.closeDialog = function(dialogName) {
		registry.byId(dialogName).hide();
	};
});

/**
 * opens any dialog
 */
require([ "dijit/registry", "dojo/domReady!" ], function(registry) {
	window.openDialog = function(dialogName) {
		registry.byId(dialogName).show();
	};
});

/**
 * formats the date for consistent output
 */
require([ "dojo/date/locale" ], function(locale) {
	window.formatDate = function(inDate) {
		return locale.format(new Date(inDate), {
			selector : 'date',
			datePattern : 'MMMM d, yyyy - hh:mm a'
		});
	};
});

/**
 * formats the currency in US Dollars
 */
require([ "dojo/currency" ], function(cur) {
	window.formatCurrency = function(inCur) {
		return cur.format(inCur, {
			currency : "USD"
		});
	};
});

/**
 * populates the airport codes with the required details
 */
function populateAirportCodes() {
	addAirportCode("Boston", "BOS");
	addAirportCode("New York", "NYC");
	addAirportCode("London", "LHR");
	addAirportCode("Paris", "CDG");
}

/**
 * helper method to format the airport code and add each to the global variable.
 * 
 * @param name
 * @param code
 */
function addAirportCode(name, code) {
	airportCodes.push({
		airportName : name,
		airportCode : code
	});
}

// - forces the airport code to be populated.
populateAirportCodes();

/**
 * Puts in custom information in the dialog and
 * Pops up information dialog 
 */
require([ "dojo/dom", "dijit/Dialog" ], function(dom, Dialog) {
	window.openInformationalDialog = function(content) {
		dom.byId('informationDialogContent').innerHTML = content;
		openDialog('informationalDialog');
	};
});

/**
 * extracts the flight number from the data item
 * TODO Cells Support Multivalues?
 */
function formatFlightNumber(item){
	return item.flightSegmentId[0];
}

/**
 * simple function redirect the navigation
 * only if to a different page
 */
function navigate(url){
	if(window.location.href.toLowerCase().indexOf(url) > 0){
		//console.log('already on the page, no need to do the refresh');
	}else{
		window.open(url,'_self');
	}
}

/**
 * Navigation Helper Methods
 */
function navigateHome(){
	navigate('index.html');
}

function navigateFlight(){
	navigate('flights.html');
}

function navigateCheckin(){
	navigate('checkin.html');
}

function openDialogLogin(){
	openDialog('dialogLogin');
}

function navigateProfile(){
	navigate('profile.html');
}

function closeDialogInfo(){
	closeDialog(informationDialog);
}