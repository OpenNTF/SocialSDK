function parseLotusLiveCommunities(xml){
	var navigator  = new sbt.XmlNavigator(xml).get("feed/entry");
    
    if(navigator != null){
    	var contacts = new Array(navigator.getCount());
        for(var i = 0; i < navigator.getCount(); i++){
            var nav = navigator.get(i);
            
            var name = nav.stringValue("title");
            var owner = nav.stringValue("author/name");
            var link = nav.get("link").selectEq("@rel", "alternate").stringValue("@href");
            var descr = nav.stringValue("summary");
            if(descr == null){
            	descr = "(Description not provided)"
            }
            contacts[i] = {
            	"title" : name,
            	"owner" : owner,
            	"link" : link,
            	"descr" : descr
            }
        }
        return contacts;
    }
    return null;
}