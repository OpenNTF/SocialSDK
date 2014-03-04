***REMOVED***
ob_start();
//change this to your path
$path = '/home/benjamin/PHPWorkspace/wordpress-develop/tests/phpunit/includes/bootstrap.php';

if (file_exists($path)) {
	$GLOBALS['wp_tests_options'] = array(
			'active_plugins' => array('ibm-sbtk/ibm-sbt.php')
	);
	require_once $path;
} else {
	exit("Couldn't find wordpress-tests/bootstrap.php");
}


activate_plugin( WP_CONTENT_DIR . '/plugins/ibm-sbtk/ibm-sbt.php', '', TRUE, TRUE );
