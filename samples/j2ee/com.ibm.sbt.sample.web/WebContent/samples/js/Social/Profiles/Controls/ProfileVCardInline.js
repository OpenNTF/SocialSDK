require(["sbt/dom", "sbt/connections/controls/vcard/ProfileVCardInline"], function(dom, ProfileVCardInline) {
    var profileCard = new ProfileVCardInline({ userName : "%{name=sample.displayName1}", userId : "%{name=sample.id1|helpSnippetId=Social_Profiles_Get_Profile}" });
   
    dom.byId("vcardDiv").appendChild(profileCard.domNode);
});
