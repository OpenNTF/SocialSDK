/**
 * Helper module for interacting with the acme airlines REST APIs.
 * @module
 */
define(["sbt/_bridge/declare", "sbt/config", "sbt/json"], function(declare, config, json) {
    var acmeAirEndpoint = config.findEndpoint('acmeAir');
    var acmeGet = function(service, loadCallback, errorCallback) {
        acmeAirEndpoint.xhrGet({
            serviceUrl : service,
            handleAs : "json",
            preventCache : true,
            load: function(response) {
                if(loadCallback) {
                    loadCallback(response);
                }
            },
            error: function(error){
                if(errorCallback) {
                    errorCallback(error);
                }
            }
      });
    };
    
    var acmePost = function(service, loadCallback, errorCallback, postBody) {
        acmeAirEndpoint.xhrPost({
           serviceUrl : service,
           preventCache : true,
           headers: {"Content-Type" : "application/json"},
           load: function(response) {
               if(loadCallback) {
                   loadCallback(response);
               }
           },
           error: function(error){
               if(errorCallback) {
                   errorCallback(error);
               }
           },
           postData : postBody
        });
    };
    
    var acmePut = function(service, loadCallback, errorCallback, putBody) {
        acmeAirEndpoint.xhrPut({
           serviceUrl : service,
           preventCache : true,
           handleAs : "json",
           headers: {"Content-Type" : "application/json"},
           load: function(response) {
               if(loadCallback) {
                   loadCallback(response);
               }
           },
           error: function(error){
               if(errorCallback) {
                   errorCallback(error);
               }
           },
           putData : putBody
        });
    };
    var restPath = '/acme.social.sample.dataapp/rest/flights';
    return {
        /**
         * Gets all the flight information.
         * @static
         * @param {Object} [args] Arguments to get all flight information.
         * @example
         *  var myLoadCallback = function(flights) {
         *    for(var i = 0; i < flights.length; i++) {
         *      //do something with the flight information
         *    }
         *  };
         *  
         *  var myErrorCallback = function(error) {
         *    //an error occurred do something
         *  };
         *  
         *  
         *  flights.getAllFlights({loadCallback: myLoadCallback, errorCallback: myErrorCallback});
         */
        getAllFlights : function(args) {
            var loadCallback = args['loadCallback'];
            var errorCallback = args['errorCallback'];
            acmeGet(restPath + '/lists', loadCallback, errorCallback);
        },
      
        /**
         * Gets all the flights for a user.
         * @param {Object} [args] Arguments to get the users flight information.
         * @example
         *  var myLoadCallback = function(flights) {
         *    for(var i = 0; i < flights.length; i++) {
         *      //do something with the flight information
         *    }
         *  };
         *  
         *  var myErrorCallback = function(error) {
         *    //an error occurred do something
         *  };
         *  
         *  flights.getFlightsForUser({userId: "example@renovations.com", loadCallback: myLoadCallback, errorCallback: myErrorCallback});
         */
        getFlightsForUser : function(args) {
            var loadCallback = args['loadCallback'];
            var errorCallback = args['errorCallback'];
            var userId = args['userId'];
            acmeGet(restPath + '/' + userId + '/lists', loadCallback, errorCallback);
        },
        
        /**
         * Gets all the flights for a user.
         * @param {Object} [args] Arguments to get the users flight information.
         * @example
         *  var myLoadCallback = function(flights) {
         *    for(var i = 0; i < flights.length; i++) {
         *      //do something with the flight information
         *    }
         *  };
         *  
         *  var myErrorCallback = function(error) {
         *    //an error occurred do something
         *  };
         *  
         *  flights.getFlightsForUser({userId: "example@renovations.com", loadCallback: myLoadCallback, errorCallback: myErrorCallback});
         */
        getUsersForFlight : function(args) {
            var loadCallback = args['loadCallback'];
            var errorCallback = args['errorCallback'];
            var flightId = args['flightId'];
            acmeGet(restPath + '/' + flightId + '/users', loadCallback, errorCallback);
        },
        
        /**
         * Updates a booked flight.
         * @param {Object} [args] Arguments to update a user's booked flight.
         * @example
         * var myLoadCallback = function(flights) {
         *    for(var i = 0; i < flights.length; i++) {
         *      //do something with the flight information
         *    }
         *  };
         *  
         *  var myErrorCallback = function(error) {
         *    //an error occurred do something
         *  };
         *  
         *  var updatedFlight = {
         *    UserId : "example1@renovations.com",
         *    FlightId : "123",
         *    ApproverId : "example2@renovations.com",
         *    Reason : "I love this place"
         *  }
         *  
         *  flights.bookFlight({flight: myFlight, loadCallback: myLoadCallback, errorCallback: myErrorCallback});
         */
        updateMyFlight : function(args) {
            var putBody = json.stringify(args['flight']);
            var loadCallback = args['loadCallback'];
            var errorCallback = args['errorCallback'];
            var userId = args.flight['UserId'];
            var flightId = args.flight['FlightId'];
            acmePut(restPath + '/' + userId + '/lists/' + flightId, loadCallback, errorCallback, putBody);   
        },
      
        /**
         * Books a flight.
         * @param {Object} [args] Arguments to book a flight.
         * @example
         *  var myLoadCallback = function(flights) {
         *    for(var i = 0; i < flights.length; i++) {
         *      //do something with the flight information
         *    }
         *  };
         *  
         *  var myErrorCallback = function(error) {
         *    //an error occurred do something
         *  };
         *  
         *  var myFlight = {
         *    UserId : "example1@renovations.com",
         *    FlightId : "123",
         *    ApproverId : "example2@renovations.com",
         *    Reason : "I love this place"
         *  }
         *  
         *  flights.bookFlight({flight: myFlight, loadCallback: myLoadCallback, errorCallback: myErrorCallback});
         */
        bookFlight : function(args) {
            var postBody = json.stringify(args['flight']);
            var loadCallback = args['loadCallback'];
            var errorCallback = args['errorCallback'];
            var userId = args.flight['UserId'];
            acmePost(restPath + '/' + userId + '/lists', loadCallback, errorCallback, postBody);  
        }
    };
});