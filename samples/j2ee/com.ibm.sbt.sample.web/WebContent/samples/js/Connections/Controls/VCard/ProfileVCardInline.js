require(["sbt/dom", "sbt/connections/controls/vcard/ProfileVCardInline"], function(dom, ProfileVCardInline) {
    var profileCard = new ProfileVCardInline({ userName : "%{sample.displayName1}", userId : "%{sample.id1}" });
   
    dom.byId("vcardDiv").appendChild(profileCard.domNode);
});
