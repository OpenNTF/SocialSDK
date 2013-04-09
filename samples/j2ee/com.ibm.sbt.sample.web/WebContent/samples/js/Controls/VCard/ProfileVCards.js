require(["sbt/dom", "sbt/controls/vcard/connections/ProfileVCard"], function(dom, ProfileVCard) {
    var profileCard1 = new ProfileVCard({ userName : "%{sample.displayName1}", userId : "%{sample.id1}" });
    dom.byId("vcardDiv1").appendChild(profileCard1.domNode);

    var profileCard2 = new ProfileVCard({ userName : "%{sample.displayName2}", userId : "%{sample.email2}" });
    dom.byId("vcardDiv2").appendChild(profileCard2.domNode);
});
