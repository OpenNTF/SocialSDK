<?php
/*
 * © Copyright IBM Corp. 2013
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
 * Uninstall hook
 * 
 * @author Benjamin Jakobus
 */


if (!defined('ENDPOINTS')) {
	define('ENDPOINTS', 'ibm_sbt_endpoints');
}

if (!defined('SESSION_NAME')) {
	define('SESSION_NAME', 'ibm_sbt_session');
}

if (!defined('MOODLE_INTERNAL')) {
	die();
}

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




