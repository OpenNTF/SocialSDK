***REMOVED***

include_once '../autoload.php';
include_once BASE_PATH . '/controllers/services/SBTJsonService.php';

// // Your settings




// Create SBTSettings instance
$settings = new SBTSettings();

$service = new SBTJsonService('SmartCloud');

$service->makeRequest('GET', 'connections/files/basic/api/myuserlibrary/feed?page=1&ps=10&sortBy=modified&sortOrder=desc&dojo.preventCache=1396526818994', 'SmartCloud');
