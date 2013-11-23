require([ "sbt/lang", "sbt/dom", "sbt/json", 
          "sbt/connections/ProfileConstants", "sbt/base/BaseService", "sbt/base/BaseEntity", "sbt/base/XmlDataHandler" ], 
    function(lang,dom,json,consts,BaseService,BaseEntity,XmlDataHandler) {
        var domNode = dom.byId("ProfileEntry");
        var ProfileEntry = domNode.text || domNode.textContent;
    
        var dataHandler = new XmlDataHandler({
            data : ProfileEntry,
            namespaces : consts.Namespaces,
            xpath : lang.mixin({ number : "/a:feed/a:number", boolean : "a:boolean"}, consts.ProfileXPath)
        });
        
        var baseEntity = new BaseEntity({
            service : {},
            dataHandler : dataHandler
        });

        try {
            var results = [];

            results.push(testSetAsString(baseEntity, "userid", "userid123"));       
            results.push(testSetAsString(baseEntity, "userid", [ "foo", "bar" ]));       
            results.push(testSetAsString(baseEntity, null, "userid123"));       
            results.push(testSetAsString(baseEntity, undefined, "userid123"));       
            results.push(testSetAsString(baseEntity, "userid", { foo : "bar" }));       
            results.push(testSetAsString(baseEntity, "userid", null));       
            results.push(testSetAsString(baseEntity, "userid", undefined));       
            
            results.push(testSetAsNumber(baseEntity, "number", 8));       
            results.push(testSetAsNumber(baseEntity, "number", "8"));       
            results.push(testSetAsNumber(baseEntity, "number", null));       
            results.push(testSetAsNumber(baseEntity, null, "0"));       
            results.push(testSetAsNumber(baseEntity, undefined, "0"));       
            results.push(testSetAsNumber(baseEntity, "number", "foo"));       
            results.push(testSetAsNumber(baseEntity, "number", { foo : "bar" }));       
            results.push(testSetAsNumber(baseEntity, "number", [ "foo" ]));       
            results.push(testSetAsNumber(baseEntity, "number", undefined));       
            
            results.push(testSetAsDate(baseEntity, "updated", new Date(2013,4,1)));       
            results.push(testSetAsDate(baseEntity, "updated", "2013-03-28T21:14:14.649Z"));       
            results.push(testSetAsDate(baseEntity, "updated", null));       
            results.push(testSetAsDate(baseEntity, null, new Date(2013,4,1)));       
            results.push(testSetAsDate(baseEntity, undefined, new Date(2013,4,1)));       
            results.push(testSetAsDate(baseEntity, "updated", "foo"));       
            results.push(testSetAsDate(baseEntity, "updated", { foo : "bar" }));       
            results.push(testSetAsDate(baseEntity, "updated", [ "foo" ]));       
            results.push(testSetAsDate(baseEntity, "updated", undefined));       

            results.push(testSetAsBoolean(baseEntity, "boolean", true));       
            results.push(testSetAsBoolean(baseEntity, "boolean", false));       
            results.push(testSetAsBoolean(baseEntity, "boolean", "true"));       
            results.push(testSetAsBoolean(baseEntity, "boolean", "false"));       
            results.push(testSetAsBoolean(baseEntity, "boolean", null));       
            results.push(testSetAsBoolean(baseEntity, null, false));       
            results.push(testSetAsBoolean(baseEntity, undefined, false));       
            results.push(testSetAsBoolean(baseEntity, "boolean", "foo"));       
            results.push(testSetAsBoolean(baseEntity, "boolean", { foo : "bar" }));       
            results.push(testSetAsBoolean(baseEntity, "boolean", [ "foo" ]));       
            results.push(testSetAsBoolean(baseEntity, "boolean", undefined));       

            results.push(testSetAsArray(baseEntity, "a:link/@href", [ "foo", "bar", "baz" ]));       
            results.push(testSetAsArray(baseEntity, "a:link/@href", "foo,bar,baz"));       
            results.push(testSetAsArray(baseEntity, "a:link/@href", "foo"));       
            results.push(testSetAsArray(baseEntity, "a:link/@href", 8));
            results.push(testSetAsArray(baseEntity, "a:link/@href", true));
            results.push(testSetAsArray(baseEntity, "a:link/@href", null));       
            results.push(testSetAsArray(baseEntity, null, [ "foo", "bar", "baz" ]));       
            results.push(testSetAsArray(baseEntity, undefined, [ "foo", "bar", "baz" ]));       
            results.push(testSetAsArray(baseEntity, "a:link/@href", undefined));
            
            dom.setText("json", json.jsonBeanStringify(results));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    }
);

function testSetAsString(baseEntity, fieldName, strValue) {
    var results = {};
    try {
        results[fieldName] = baseEntity.setAsString(fieldName, strValue).getAsString(fieldName);
    } catch (error) {
        results[fieldName] = error.message;
    }
    return results;
}

function testSetAsNumber(baseEntity, fieldName, numberValue) {
    var results = {};
    try {
        results[fieldName] = baseEntity.setAsNumber(fieldName, numberValue).getAsNumber(fieldName);
    } catch (error) {
        results[fieldName] = error.message;
    }
    return results;
}

function testSetAsDate(baseEntity, fieldName, dateValue) {
    var results = {};
    try {
        results[fieldName] = baseEntity.setAsDate(fieldName, dateValue).getAsDate(fieldName);
    } catch (error) {
        results[fieldName] = error.message;
    }
    return results;
}

function testSetAsBoolean(baseEntity, fieldName, boolValue) {
    var results = {};
    try {
        results[fieldName] = baseEntity.setAsBoolean(fieldName, boolValue).getAsBoolean(fieldName) ? "true" : "false";
    } catch (error) {
        results[fieldName] = error.message;
    }
    return results;
}

function testSetAsArray(baseEntity, fieldName, arrayValue) {
    var results = {};
    try {
        results[fieldName] = baseEntity.setAsArray(fieldName, arrayValue).getAsArray(fieldName);
    } catch (error) {
        results[fieldName] = error.message;
    }
    return results;
}
