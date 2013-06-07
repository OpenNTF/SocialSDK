function createUser(userName, password, idfile, certPassword) {
	var done = false;
	if( idfile && idfile.length() > 3 )
	{
		if( ".id" == idfile.substring( idfile.length()-3 ) )
		{
			idfile = idfile.substring( 0, idfile.length()-3 );
		} 
	}
	if( userName )
	{
		if( userName.isEmpty() )return;
		var st = new java.util.StringTokenizer( userName.toString() );
		var tokens = st.countTokens();
		var emptyString = new java.lang.String("");
		var firstName = emptyString, middleName = emptyString, lastName = null;
		if( tokens == 1 )
			lastName = st.nextToken();
		else if( tokens == 2 )
		{
			firstName = st.nextToken();
			lastName = st.nextToken();
		}
		else if( tokens == 3 )
		{
			firstName = st.nextToken();
			middleName = st.nextToken();
			lastName = st.nextToken();
		}
		print( firstName );
		print( middleName );
		print( lastName );
		if( lastName == null )return;
		
		try {
	      var reg = session.createRegistration();
	      reg.setCreateMailDb(true);
	      reg.setCertifierIDFile("/mnt/NotesDrive/notesdata/cert.id");
	      var dt = session.createDateTime("Today");
	      dt.setNow();
	      dt.adjustYear(1);
	      reg.setExpiration(dt);
	      reg.setIDType(lotus.domino.Registration.ID_HIERARCHICAL);
	      reg.setMinPasswordLength(5); // password strength
	      reg.setRegistrationLog("log.nsf");
	      reg.setUpdateAddressBook(true);
	      done = reg.registerNewUser( lastName,
	        idfile + ".id",
	        "",
	        firstName,
	        middleName,
	        certPassword,
	        "", // location field
	        "", // comment field
	        "mail/" + idfile + ".nsf", // mail file
	        "", // forwarding domain
	        password);
	        reg.recycle();

	    } catch( e ) {
	    	sessionScope.userActionResult =  "failed to create. Check your input";
	    	sessionScope.idfile = null;
	        reg.recycle();
	        return;
	    }
	}
	if( done )
	{
		sessionScope.userActionResult = lastName + " created";
		idfile += ".id";
		sessionScope.idfile = idfile;
		var f = new java.io.File( idfile );
		var fileToDownload = "domino/html/" + idfile;
		var fo = new java.io.File( fileToDownload );
		fo.createNewFile();
		var is = new java.io.FileInputStream( f );
		var os = new java.io.FileOutputStream( fo );
		var buffer = new byte[1024];
		for( var nBytes = 0; (nBytes = is.read( buffer )) > 0 ; )
			os.write( buffer, 0, nBytes );
		is.close();
		os.close();
		fo.deleteOnExit();	
	}
	else
	{
		sessionScope.userActionResult = lastName + " not created";
		sessionScope.idfile = null;
	} 
}

function searchUser(userName) {
	if( userName )
	{
		var registration = session.createRegistration();
		var mailServer = new java.lang.StringBuffer();
		var mailFile = new java.lang.StringBuffer();
		var mailDomain = new java.lang.StringBuffer();
		var mailSystem = new java.lang.StringBuffer();
		var profile = new java.util.Vector();
		try
		{
			registration.getUserInfo( userName,
				mailServer,
				mailFile,
				mailDomain,
				mailSystem,
				profile );
		}
		catch( err )
		{
			sessionScope.userActionResult = "Not Found";
			sessionScope.idfile = null;
			registration.recycle();
			return;	
		}
		registration.recycle();	
			
		sessionScope.userActionResult = "Found";
		print( "Mail server: "+mailServer );
		print( "Mail file: "+mailFile );

		var idfile = mailFile.toString();
		var pathSeparator = idfile.indexOf('/');
		if( pathSeparator >= 0 )
			idfile = idfile.substring( pathSeparator+1 );
		var ext = idfile.lastIndexOf('.');
		if( ext >= 0 )
			idfile = idfile.substring(0, ext);
		idfile += ".id";
		
		sessionScope.idfile = idfile;
	}
	else
	{
		return;
	}
}