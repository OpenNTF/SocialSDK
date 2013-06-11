require([ "sbt/base/JsonDataHandler", "sbt/lang", "sbt/dom", "sbt/json", "sbt/smartcloud/ProfileConstants"], 
    function(JsonDataHandler,lang,dom,json,consts) {

	var numbers = {"number": 123, "decimalNumber" : 12.12, "stringNumber" : "234", "notANumber" : "12h3", "notANumber1" : "1-2 3", "array" : [1,3,4,"f"] };
	
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
    	// Tests for getAsNumber
        "number" : dataHandler.getAsNumber("number"),
        "decimalNumber" : dataHandler.getAsNumber("decimalNumber"),
        "stringNumber" : dataHandler.getAsNumber("stringNumber"),
        "notANumber" : dataHandler.getAsNumber("notANumber"),
        "notANumber1" : dataHandler.getAsNumber("notANumber1"),
        "array" : dataHandler.getAsNumber("array")
    };
}