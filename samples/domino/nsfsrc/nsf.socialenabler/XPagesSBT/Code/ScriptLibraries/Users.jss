function parseSmartCloudProfile(nav){
	var url = nav.getAsString("profileUrl");

	var myname = nav.getAsString("displayName");
	var aboutMe = nav.getAsString("aboutMe");
	var country = nav.getAsString("country");
	var email = nav.getAsString("emailAddress");
	
	var photo = nav.getEntries("photos");
  
    photo = photo.get(0).getAsString("value");
    if(photo != null){
    	photo = "https://apps.na.collabserv.com/contacts/img/photos/" + photo;
    }
    else{
    	photo = "/no_pic.jpg";
    }
    
    var addresses = nav.getEntries("addresses");

   	address = addresses.get(4).getAsString("address");
    var phone = nav.getEntries("phoneNumbers");
    phone = phone.get(4).getAsString("phone");

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
function parseSmartCloudContacts(profiles){

	var entries = profiles.size();

    if(entries != null){
    	var contacts = new Array(profiles.size());
        for(var i = 0; i < profiles.size(); i++){
            var nav = profiles.get(i).getDataHandler();
            var photos = nav.getEntries("photos"); 
            var photo=null;        
            if(photos.size()==2){
            	photo = photos.get(1).getAsString("value");          	
        		if(photo != null && photo != "PROFILES"){
            		photo = "https://apps.na.collabserv.com/contacts/img/photos/" + photo;
            	}
            	else{
            		photo = "/no_pic.jpg";
            		}
            	}
            
            var name = nav.getAsString("displayName");
            
            var email = nav.getAsString("emailAddress");
            
            var profile = nav.getAsString("profileUrl");
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