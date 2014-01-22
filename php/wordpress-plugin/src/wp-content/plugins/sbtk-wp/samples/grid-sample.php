***REMOVED***

	// Autoloader
	$autoload = __FILE__;
	$autoload = str_replace(basename(__FILE__) , "", $autoload);
	require_once $autoload . '../../../autoload.php';
	
	$plugin = new SBTKPlugin();

	$plugin->createHeader();

	// Create a community grid
	$plugin->createCommunityGrid(array());

	// Create a file grid
	$plugin->createFilesGrid(array());
	
	// Create a bookmarks grid
	$plugin->createBookmarkGrid(array());

	?>