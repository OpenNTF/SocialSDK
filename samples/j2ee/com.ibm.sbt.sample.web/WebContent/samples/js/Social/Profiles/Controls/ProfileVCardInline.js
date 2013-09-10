require(["sbt/dom", "sbt/connections/controls/vcard/ProfileVCardInline"], function(dom, ProfileVCardInline) {
    var profileCard = new ProfileVCardInline({ userName : "%{name=sample.displayName1}", userId : "%{name=sample.id1}" });
   
    dom.byId("vcardDiv").appendChild(profileCard.domNode);
});
