***REMOVED***
	// Autoloader
	require_once '../autoload.php';

	$store = new CredentialStore();
	$store->deleteTokens();
	session_destroy();
	echo "Token deleted";

	?>