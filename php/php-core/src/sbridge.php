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
 * Session bridge configuration to share session objects with third parties apps as required
 * 
 * Uncomment and configure the needed driver 
 * 
 * file base and memcached base driver examples provided
 * 
 * @author Lorenzo Boccaccia
 */


//session_name('custom session cookie name');

//file based session management
//ini_set('session.save_handler', 'files');
//ini_set('session.save_path', 'set session path');

//session_name('custom session cookie name');

//memcached based session management
//ini_set('session.save_handler', 'memcached');
//ini_set('session.save_path', 'set memcached url');

//ini_set('memcached.sess_prefix', 'session prefix');
//ini_set('memcached.sess_locking', '1'); //enables locking

// Try to configure lock and expire timeouts - ignored if memcached <=2.1.0.
//ini_set('memcached.sess_lock_max_wait', 'acquire timeout');
//ini_set('memcached.sess_lock_expire', 'acquire timeout');


session_name($config['session_name']);

//file based session management
if (!isset($_SESSION)) {
	ini_set('session.save_handler', 'files');
	ini_set('session.save_path', '/home/benjamin/moodle-data/sessions' );
}

