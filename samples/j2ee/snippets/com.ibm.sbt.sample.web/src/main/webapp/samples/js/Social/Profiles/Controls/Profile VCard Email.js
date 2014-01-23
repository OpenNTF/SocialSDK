require(["sbt/dom", "sbt/connections/controls/vcard/ProfileVCard"], function(dom, ProfileVCard) {
    var profileCard = new ProfileVCard({ 
        userName : "%{name=sample.displayName1}", 
        userId : "%{name=sample.email1}",
        inclCss : true
    });
   
    dom.byId("vcardDiv").appendChild(profileCard.domNode);
});
