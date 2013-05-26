require([ "sbt/lang", "sbt/dom", "sbt/json" ], 
    function(lang,dom,json) {
    
    
    var object1 = {
            one : "object1_one"        
    };
    
    var object2 = {
            one : "object2_one",        
            two : "object2_two"        
    };

    var object3 = {
            one : "object3_one",        
            two : "object3_two",        
            three : "object3_three"        
    };
    
    var results = [];
    
    results.push(lang.mixin({}, object1));
    results.push(lang.mixin({}, object1, object2));
    results.push(lang.mixin({}, object1, object2, object3));
    results.push(lang.mixin({}, object3, object2, object1));
    results.push(lang.mixin({}, object3, object1));
    results.push(lang.mixin({}, object2, object1));
    
    dom.setText("json", json.jsonBeanStringify(results));
});
