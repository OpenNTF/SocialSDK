require(["sbt/dom", "sbt/connections/controls/vcard/ProfileVCard"], function(dom, ProfileVCard) {
    var profileCard = new ProfileVCard({ userName : "%{name=sample.displayName1}", userId : "%{name=sample.id1|helpSnippetId=Social_Profiles_Get_Profile}" });
   
    dom.byId("vcardDiv").appendChild(profileCard.domNode);
});
