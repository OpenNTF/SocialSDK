require([ "sbt/base/JsonDataHandler", "sbt/lang", "sbt/dom", "sbt/json", "sbt/smartcloud/ProfileConstants"], 
    function(JsonDataHandler,lang,dom,json,consts) {

	var numbers = {"validDate": new Date(), "stringDateInvalid" : "6-11-2013", "numberDateInvalid" : 6112013, "notADate" : "is this a date?"};
	
	var profileEntryHandler = new JsonDataHandler({
            data : numbers,
            jsonpath : consts.ProfileJPath
        });

        try {
            var results = getResults(profileEntryHandler);
            dom.setText("json", json.jsonBeanStringify(results));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    }
);

function getResults(dataHandler) {
    return {
    	// Tests for getAsDate
        "validDate" : dataHandler.getAsDate("validDate"),
        "stringDateInvalid" : dataHandler.getAsDate("stringDateInvalid"),
        "numberDateInvalid" : dataHandler.getAsDate("numberDateInvalid"),
        "notADate" : dataHandler.getAsDate("notADate"),
    };
}