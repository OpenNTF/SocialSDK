***REMOVED*** 

// Autoloader
require_once '../autoload.php';

$store = SBTCredentialStore::getInstance();

$store->storeRequestToken("TEST 123");
echo "Stored<br/><br/><br/>";

echo $store->getRequestToken();