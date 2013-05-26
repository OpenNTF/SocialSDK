/**
 * Helper module for logging in and retrieving the reporting chain for the logged in user.
 * @module
 */
define(["sbt/declare", "sbt/Endpoint", "sbt/json", "sbt/xml", "sbt/xpath", "sbt/connections/ConnectionsConstants"], 
    function(declare, endpoint, json, xml, xpath, connections) {

    var basicPeopleMe = '/connections/opensocial/basic/rest/people/@me/';
    var oauthPeopleMe = '/connections/opensocial/oauth/rest/people/@me/';
    
    var basicReportingChain = "/profiles/atom/reportingChain.do?userid="; 
    var oauthReportingChain = "/profiles/oauth/atom/reportingChain.do?userid="; 
    
    var approverEmailXPath = "/a:feed/a:entry[2]/a:contributor/a:email";
    var approverUserIdXPath = "/a:feed/a:entry[2]/a:contributor/snx:userid";

    var endpoint = endpoint.find('connections');
    
    var personObject = null;
    var reportingChain = null;
    
    var getPeopleMe = function(onSuccess, onError) {
        var path = basicPeopleMe;
        if (endpoint.authType == 'oauth') {
            path = oauthPeopleMe;
        }
        
        endpoint.xhrGet({
            serviceUrl : path,
            handleAs : "json",
            loginUi : "dialog",
            preventCache : true,
            load: function(response) {
                personObject = response.entry;
                personObject.id = response.entry.id.replace('urn:lsid:lconn.ibm.com:profiles.person:', '');
                if(onSuccess) {
                    onSuccess(personObject);
                }
            },
            error: function(error){
                personObject = null;
                if(onError) {
                    onError(error);
                }
            }
      });
    };

    var getReportingChain = function(onSuccess, onError) {
        var path = basicReportingChain;
        if (endpoint.authType == 'oauth') {
            path = oauthReportingChain;
        }
        path += personObject.id;
        
        endpoint.xhrGet({
            serviceUrl : path,
            loginUi : "dialog",
            load: function(response) {
                reportingChain = xml.parse(response);;
                if(onSuccess) {
                    onSuccess(response);
                }
            },
            error: function(error){
                reportingChain = null;
                if(onError) {
                    onError(error);
                }
            }
      });
    };

    return {
        /**
         * 
         * @static
         * @param {Object} [args] .
         */
        doLogin : function(args) {
            var success = args['success'];
            var error = args['error'];
            getPeopleMe(success, error);
        },
        
        /**
         * 
         * @static
         * @param {Object} [args] .
         */
        doGetReportingChain : function(args) {
            var success = args['success'];
            var error = args['error'];
            getReportingChain(success, error);
        },
        
        /**
         * 
         * @static
         * @param {Object} [args] .
         */
        getPerson : function() {
            return personObject;
        },
        
        /**
         * 
         * @static
         * @param {Object} [args] .
         */
        getReportingChain : function() {
            return reportingChain;
        },
        
        /**
         * 
         * @static
         * @param {Object} [args] .
         */
        getUserEmail: function() {
            if (!personObject) {
                return null;
            }
            return personObject.emails[0].value;
        },
        
        /**
         * 
         * @static
         * @param {Object} [args] .
         */
        getUserDisplayName: function() {
            if (!personObject) {
                return null;
            }
            return personObject.displayName;
        },
        
        /**
         * 
         * @static
         * @param {Object} [args] .
         */
        getApproverEmail: function() {
            if (!reportingChain) {
                return null;
            }
            return xpath.selectText(reportingChain, approverEmailXPath, connections.Namespaces);
        },
        
        /**
         * 
         * @static
         * @param {Object} [args] .
         */
        getApproverUserId: function() {
            if (!reportingChain) {
                return null;
            }
            return xpath.selectText(reportingChain, approverUserIdXPath, connections.Namespaces);
        }
    };
});