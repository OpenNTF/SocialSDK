require(["sbt/dom", "sbt/connections/controls/vcard/ProfileVCard"], function(dom, ProfileVCard) {
    var profileCard1 = new ProfileVCard({ userName : "%{name=sample.displayName1}", userId : "%{name=sample.id1|helpSnippetId=Social_Profiles_Get_Profile}" });
    dom.byId("vcardDiv1").appendChild(profileCard1.domNode);

    var profileCard2 = new ProfileVCard({ userName : "%{name=sample.displayName2}", userId : "%{name=sample.email2}" });
    dom.byId("vcardDiv2").appendChild(profileCard2.domNode);
});
