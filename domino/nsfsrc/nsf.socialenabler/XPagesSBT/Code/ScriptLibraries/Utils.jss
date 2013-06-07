function parseLotusLiveProfile(json){
	var nav = new sbt.JsonNavigator(json);
	var entry = nav.get("entry");
	var url = entry.stringValue("profileUrl");
	var myname = entry.stringValue("displayName");
	var aboutMe = entry.stringValue("aboutMe");
	var country = entry.stringValue("country");
	var email = entry.stringValue("emailAddress");
	
	var photo = entry.get("photos");
	println(photo);
    photo = photo.selectEq("type", "Photo");
    photo = photo.stringValue("value");
    if(photo != null){
    	photo = "https://apps.lotuslive.com/contacts/img/photos/" + photo;
    }
    else{
    	photo = "/no_pic.jpg";
    }
    
    var address = entry.get("addresses");
    address = address.selectEq("title", "Primary Address");
    address = address.stringValue("address");
    
    var phone = entry.get("phoneNumbers");
    phone = phone.selectEq("title", "Primary Telephone");
    phone = phone.stringValue("phone");

	return {
		"image" : photo,
		"email" : email,
		"phone" : phone,
		"address" : address,
		"url" : url,
		"myname" : myname,
		"aboutMe" : aboutMe,
		"country" : country
	}
}
function parseLotusLiveContacts(json){
	var navigator  = new sbt.JsonNavigator(json).get("entry");
    
    if(navigator != null){
    	var contacts = new Array(navigator.getCount());
        for(var i = 0; i < navigator.getCount(); i++){
            var nav = navigator.get(i);
            var photo = nav.get("photos").selectEq("type", "Photo");
            photo = photo.stringValue("value");
            if(photo != null && photo != "PROFILES"){
            	photo = "https://apps.lotuslive.com/contacts/img/photos/" + photo;
            }
            else{
            	photo = "/no_pic.jpg";
            }
            var name = nav.stringValue("displayName");
            
            var email = nav.stringValue("emailAddress");
            
            var profile = nav.stringValue("profileUrl");
            contacts[i] = {
            	"name" : name,
            	"photo" : photo,
            	"email" : email,
            	"profile" : profile
            }
        }
        return contacts;
    }
    return null;
}