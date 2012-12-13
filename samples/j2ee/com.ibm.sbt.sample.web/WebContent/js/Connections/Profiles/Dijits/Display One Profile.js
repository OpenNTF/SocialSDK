require([ "dijit/form/Button", "sbt/dojo/connections/DisplayProfile" ]);

function showProfile1() {
    var profile1 = dijit.byId("profile1");
    profile1.setValue("%{sample.id1}");
}
