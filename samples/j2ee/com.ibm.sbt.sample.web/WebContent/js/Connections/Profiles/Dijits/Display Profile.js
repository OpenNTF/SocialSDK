require(["dijit/form/Button","sbt/dojo/connections/DisplayProfile"]);

function showProfileOne(){
	dijit.byId('profile1').setValue("%{sample.id1}");
}

function showProfileTwo(){
	dijit.byId('profile2').setValue("%{sample.id2}");
}