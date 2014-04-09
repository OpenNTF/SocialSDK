<?php
/*
 * © Copyright IBM Corp. 2014
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at:
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
* implied. See the License for the specific language governing
* permissions and limitations under the License.
*/

/**
 * Unit testing bootstrap
 *
 * @author Benjamin Jakobus
 */

define('IBM_SBT_MOODLE_TEST_BASE_PATH', '/home/benjamin/PHPWorkspace/moodle-unit-testing');
define('CLI_SCRIPT', true);

if (!defined('SESSION_NAME')) {
	define('SESSION_NAME', 'ibm_sbt_session');
}

if (!defined('BASIC_AUTH_USERNAME')) {
	define('BASIC_AUTH_USERNAME', 'basicauthusername');
}

if (!defined('BASIC_AUTH_PASSWORD')) {
	define('BASIC_AUTH_PASSWORD', 'basicauthpassword');
}

if (!defined('TOKEN')) {
	define('TOKEN', 'token');
}

if (!defined('REQUEST_TOKEN')) {
	define('REQUEST_TOKEN', 'requesttoken');
}

if (!defined('TOKEN_TYPE')) {
	define('TOKEN_TYPE', 'tokentype');
}

if (!defined('OAUTH_TOKEN')) {
	define('OAUTH_TOKEN', 'oauthtoken');
}

if (!defined('OAUTH_TOKEN_SECRET')) {
	define('OAUTH_TOKEN_SECRET', 'oauthtokensecret');
}

if (!defined('OAUTH_VERIFIER_TOKEN')) {
	define('OAUTH_VERIFIER_TOKEN', 'oauthverifiertoken');
}

if (!defined('OAUTH_REQUEST_TOKEN')) {
	define('OAUTH_REQUEST_TOKEN', 'oauthrequesttoken');
}

if (!defined('OAUTH_REQUEST_TOKEN_SECRET')) {
	define('OAUTH_REQUEST_TOKEN_SECRET', 'oauthrequesttokensecret');
}

if (!defined('BASE_LOCATION')) {
	$autoload = __DIR__ . '/core/autoload.php';
	$autoload = str_replace('tests', '', $autoload);
	include $autoload;
}

require_once(IBM_SBT_MOODLE_TEST_BASE_PATH . '/config.php');

function dropTables()
{
	global $DB;
	$dbman = $DB->get_manager();
	$endpoints_table = new xmldb_table(ENDPOINTS);
	$session_table = new xmldb_table(SESSION_NAME);
	
	if ($dbman->table_exists($endpoints_table)) {
		$DB->delete_records(ENDPOINTS, array());
		$dbman->drop_table($endpoints_table);
	}
	
	if ($dbman->table_exists($session_table)) {
		$DB->delete_records(SESSION_NAME, array());
		$dbman->drop_table($session_table);
	}
}

