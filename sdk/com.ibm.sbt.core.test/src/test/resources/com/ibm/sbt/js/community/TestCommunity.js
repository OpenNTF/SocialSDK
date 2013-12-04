require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService",
		"sbt/connections/CommunityConstants" ], function(dom, json,
		CommunityService, CommunityConstants) {

	var title = 'TestJsCommunity Name ' + new Date().getTime();

	var createCommunity = function(communityService, title, content) {
		currentCommunity = null;
		var community = communityService.newCommunity();
		community.setTitle(title);
		community.setCommunityType(CommunityConstants.Public);
		community.setContent(content);
		// community.setTags(tags);
		return communityService.createCommunity(community);
	};

	var communityService = new CommunityService();

	var promise = createCommunity(communityService, title, "Test Content");

	promise.then(

	function(community) {
		console.log('A' + community.getCommunityUuid());
		var uuid=community.getCommunityUuid();
		community.load().then(

				function(community) {
					console.log('B');
					var secondCreation = createCommunity(communityService,
							title, "Test Content");
					secondCreation.then(function(community) {
						console.log('C');
						community.remove();
						console.log('DELETED');
						fail('second creation succeeded');

					}, function(error) {
						console.log('D');
						if (!(409 == error.response.status)) {
							fail('second creation succeeded');
						} else {
							console.log('SUCCESS');
						}

					});

				}, function(error) {
					console.log('E');
					console.log(error);
					fail('failed to load the community' + error.message);
				});
		
		communityService.deleteCommunity(uuid);
	}, function(error) {
		console.log('F');
		console.log(error);
		fail('failed to create the community' + error.message);
	}

	);

});
