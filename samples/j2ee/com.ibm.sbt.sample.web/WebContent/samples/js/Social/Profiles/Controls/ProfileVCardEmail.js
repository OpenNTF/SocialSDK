require(["sbt/dom", "sbt/connections/controls/vcard/ProfileVCard"], function(dom, ProfileVCard) {
    var profileCard = new ProfileVCard({ userName : "%{sample.displayName1}", userId : "%{sample.email1}" });
   
    dom.byId("vcardDiv").appendChild(profileCard.domNode);
});
