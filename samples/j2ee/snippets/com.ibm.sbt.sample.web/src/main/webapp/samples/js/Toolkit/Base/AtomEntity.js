require([ "sbt/lang", "sbt/dom", "sbt/json", "sbt/base/AtomEntity" ], 
    function(lang,dom,json,AtomEntity) {
	    var parseAtomEntry = function(domId) {
	        var domNode = dom.byId(domId);
	        var atomEntity = new AtomEntity({
	            service : {},
	            data : domNode.text || domNode.textContent
	        });
	        return atomEntity;
	    };
	    
		var createAtomObject = function(atomEntity) {
			return {
	            "id" : atomEntity.getId(),
	            "title" : atomEntity.getTitle(),
	            "updated" : atomEntity.getUpdated(),
	            "published" : atomEntity.getPublished(),
	            "categoryTerms" : atomEntity.getCategoryTerms(),
	            "author" : atomEntity.getAuthor(),
	            "contributor" : atomEntity.getContributor(),
	            "summary" : atomEntity.getSummary(),
	            "content" : atomEntity.getContent(),
	            "selfUrl" : atomEntity.getSelfUrl(),
	            "editUrl" : atomEntity.getEditUrl(),
	            "alternateUrl" : atomEntity.getAlternateUrl()
			};
		};   
		
        try {
            var results = [];

            results.push(parseAtomEntry("ProfileEntry").toJson());
            results.push(parseAtomEntry("CommunityEntry").toJson());
            
            dom.setText("json", json.jsonBeanStringify(results));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
        
    }

);