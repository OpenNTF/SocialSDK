require([ "sbt/base/URLBuilder" ], function(URLBuilder) {
	
	var b = new URLBuilder({
		'GET_MY_COMMUNITIES' : {
			'4.0.0': '/${communities}/service/atom/communities/my',
			'4.5.0': '/${communities}/service/atom/{authentication}/communities/my'
		},
		'GET_MY_INVITES' : {
			'4.0.0': '/${communities}/service/atom/community/invites',
		}
	});
	
	var value = b.build('GET_MY_COMMUNITIES', '4.3', {'authentication':'oauth'});
	if (value != '/${communities}/service/atom/communities/my') fail('1 returned '+value);
	
	var value = b.build('GET_MY_COMMUNITIES', '4.0', {'authentication':'oauth'});
	if (value != '/${communities}/service/atom/communities/my') fail('2 returned '+value);
		
	value = b.build('GET_MY_COMMUNITIES', '4.5', {'authentication':'oauth'});
	if (value != '/${communities}/service/atom/oauth/communities/my') fail('3 returned '+value);
	
	value = b.build('GET_MY_COMMUNITIES', '4.5', {'authentication':''});
	if (value != '/${communities}/service/atom/communities/my') fail('4 returned '+value);

	value = b.build('GET_MY_INVITES', '4.0');
	if (value != '/${communities}/service/atom/community/invites') fail('5 returned '+value);
	
	value = b.build('GET_MY_INVITES', '4.5');
	if (value != '/${communities}/service/atom/community/invites') fail('6 returned '+value);
});
