require(["sbt/Endpoint", "sbt/json", "sbt/dom"], 
    function(Endpoint, json, dom) {

    var basicPeopleMe = '/connections/opensocial/basic/rest/people/@me/';
    var oauthPeopleMe = '/connections/opensocial/oauth/rest/people/@me/';

    var endpoint = Endpoint.find('connections');
    
    var personObject = null;
    
    var getPeopleMe = function(onSuccess, onError) {
        var path = basicPeopleMe;
        if (endpoint.authType == 'oauth') {
            path = oauthPeopleMe;
        }
        
        endpoint.request(path, { handleAs : "json", preventCache : true }).then(
            function(response) {
                personObject = response.entry;
                personObject.id = response.entry.id.replace('urn:lsid:lconn.ibm.com:profiles.person:', '');
                if(onSuccess) {
                    onSuccess(personObject);
                }
            },
            function(error){
                personObject = null;
                if(onError) {
                    onError(error);
                }
            }
        );
    };

    // demonstrate calling the method
    var onSuccess = function(person) {
        dom.setText("json", json.jsonBeanStringify(person));
    };
    var onError = function(error) {
        dom.setText("json", json.jsonBeanStringify(error));
    };
    getPeopleMe(onSuccess, onError);
});