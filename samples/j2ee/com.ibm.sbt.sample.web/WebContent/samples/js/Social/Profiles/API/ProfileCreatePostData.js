require(["sbt/declare", "sbt/dom", "sbt/json", "sbt/stringUtil", "sbt/base/AtomEntity", "sbt/connections/ProfileService", "sbt/connections/ProfileConstants"], 
    function(declare, dom, json, stringUtil, AtomEntity, ProfileService, consts) {
        var profileService = new ProfileService();
        var profile = profileService.newProfile();
    	
        // create fields
        profile.setAsString("guid", "guid");
        profile.setAsString("email", "email");
        profile.setAsString("uid", "uid");
        profile.setAsString("distinguishedName", "distinguishedName}");
        profile.setAsString("displayName", "displayName}");
        profile.setAsString("givenNames", "givenNames}");
        profile.setAsString("surname", "surname}");
        profile.setAsString("userState", "userState}");
        
        // update fields
        profile.setAsString("jobTitle", "jobTitle");
        profile.setAsString("streetAddress", "streetAddress");
        profile.setAsString("telephoneNumber", "telephoneNumber");
        profile.setAsString("building", "building");
        
        dom.setText("oldPost", profileService._constructProfilePostData(profile));
        dom.setText("oldPut", profileService._constructProfilePutData(profile));

        // ----------------------------------------------------------------------------------------------------------------------------------
        
    	var VCardTmpl = "<content type=\"text\">\nBEGIN:VCARD\nVERSION:2.1\n${jobTitle}${address}${telephoneNumber}${building}${floor}END:VCARD\n</content>";
        var PersonTmpl = "<content type=\"application/xml\"><person xmlns=\"http://ns.opensocial.org/2008/opensocial\"><com.ibm.snx_profiles.attrib>${guid}${email}${uid}${distinguishedName}${displayName}${givenNames}${surname}${userState}</com.ibm.snx_profiles.attrib></person></content>";
        
        var VCardAttributeTmpl = "${attributeName}:${attributeValue}\n";
        var VCardAddressTmpl = "ADR;WORK:;;${streetAddress},${extendedAddress};${locality};${region};${postalCode};${countryName}\n";
        var PersonAttributeTmpl = "<entry><key>${attributeName}</key><value><type>text</type><data>${attributeValue}</data></value></entry>";
        
        var profileCategory = "<category term=\"profile\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>";
        
        var ProfileNamespaces = {
        		"app" : "http://www.w3.org/2007/app",
        		"thr" : "http://purl.org/syndication/thread/1.0",
        		"fh" : "http://purl.org/syndication/history/1.0",
        		"snx" : "http://www.ibm.com/xmlns/prod/sn",
        		"opensearch" : "http://a9.com/-/spec/opensearch/1.1/"	
        };
        
        var Profile = declare(AtomEntity, {
        	
        	_update : false,
        	categoryScheme : profileCategory,
        	xpath : consts.ProfileXPath,
        	namespaces : ProfileNamespaces,
        	
        	createContent : function() {
        		if (this._update) {
                    var transformer = function(value,key) {
                    	if(profile._fields[key]){
        	                value = stringUtil.transform(PersonAttributeTmpl, {"attributeName" : consts.profileCreateAttributes[key], "attributeValue" : this._fields[key]});
        	                return value;
                    	}
                    };
                    return stringUtil.transform(PersonTmpl, this, transformer, this);
        		} else {
                    var transformer = function(value,key) {
                        if (key == "address") {                	
                        	value = this._isAddressSet() ? stringUtil.transform(VCardAddressTmpl, {
                        		"streetAddress" : this._fields["streetAddress"], 
                        		"extendedAddress" : this._fields["extendedAddress"], 
                        		"locality" : this._fields["locality"], 
                        		"region" : this._fields["region"],
                        		"postalCode" : this._fields["postalCode"], 
                        		"countryName" : this._fields["countryName"]}) : null;
                        } 
                        else{                	
                        	value = (this._fields[key])? stringUtil.transform(VCardAttributeTmpl, {"attributeName" : consts.ProfileVCardXPath[key], "attributeValue" : this._fields[key]}) : null;
                        }
                        return value;
                    };
                    return stringUtil.transform(VCardTmpl, this, transformer, this);
        		}
        	},
        	
        	_isAddressSet : function() {
        		return true;
        	}
        });
        
        profile = new Profile({ service : profileService });
    	
        // create fields
        profile.setAsString("guid", "guid");
        profile.setAsString("email", "email");
        profile.setAsString("uid", "uid");
        profile.setAsString("distinguishedName", "distinguishedName}");
        profile.setAsString("displayName", "displayName}");
        profile.setAsString("givenNames", "givenNames}");
        profile.setAsString("surname", "surname}");
        profile.setAsString("userState", "userState}");
        
        // update fields
        profile.setAsString("jobTitle", "jobTitle");
        profile.setAsString("streetAddress", "streetAddress");
        profile.setAsString("telephoneNumber", "telephoneNumber");
        profile.setAsString("building", "building");
                
        dom.setText("newPost", profile.createPostData());
        profile._update = true;
        dom.setText("newPut", profile.createPostData());
                
	}
);
