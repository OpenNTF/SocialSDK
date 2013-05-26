function setText(id,text) {
    var node = document.getElementById(id);
    node.innerHTML = text;        
}

var results = "";
try {
    //require([ "sbt/dom" ], function(dom) {
    //    results = dom.byId("content");
    //});
} catch (error) {
    results = error.message;
}
setText("content", results);
