<?php
/**
 * (C) Copyright IBM Corp. 2014
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
 * Cryptography settings
 *
 * @author Benjamin Jakobus
 */

// If you don't have php-mcrypt installed, set this to false (NOTE: It is strongly recommended that you install php-mcrypt)
if (!defined('IBM_SBT_CRYPTO_ENABLED')) {
	define('IBM_SBT_CRYPTO_ENABLED', extension_loaded('mcrypt'));
}

// When in demo mode, security warnings will not be displayed
if (!defined('IBM_SBT_DEMO_MODE')) {
	define('IBM_SBT_DEMO_MODE', false);
}

// Change this phrase
$your_secret_phrase = 'something secret 123';
$hash = sha1($your_secret_phrase);
$hash = substr($hash, 0, 32);
define('IBM_SBT_SETTINGS_KEY', $hash);

function ibm_sbt_encrypt($key, $data, $iv){
	if (defined('IBM_SBT_CRYPTO_ENABLED') && IBM_SBT_CRYPTO_ENABLED) {
		$b = mcrypt_get_block_size(MCRYPT_RIJNDAEL_256, MCRYPT_MODE_CBC);
		$enc = mcrypt_module_open(MCRYPT_RIJNDAEL_256, '', MCRYPT_MODE_CBC, '');
		mcrypt_generic_init($enc, $key, $iv);
		$dataPad = $b - (strlen($data) % $b);
		$data .= str_repeat(chr($dataPad), $dataPad);
	
		$encrypted_data = mcrypt_generic($enc, $data);
	
		mcrypt_generic_deinit($enc);
		mcrypt_module_close($enc);
		return addslashes(base64_encode($encrypted_data));
	} else {
		return $data;
	}
}

function ibm_sbt_decrypt($key, $encryptedData, $iv) {
	if (defined('IBM_SBT_CRYPTO_ENABLED') && IBM_SBT_CRYPTO_ENABLED) {
		$encryptedData = stripslashes($encryptedData);
	
		$enc = mcrypt_module_open(MCRYPT_RIJNDAEL_256, '', MCRYPT_MODE_CBC, '');
		mcrypt_generic_init($enc, $key, $iv);
	
		$encryptedData = base64_decode($encryptedData);
		$data = mdecrypt_generic($enc, $encryptedData);
		mcrypt_generic_deinit($enc);
		mcrypt_module_close($enc);
	
		$dataPad = ord($data[strlen($data) - 1]);
	
		return substr($data, 0, -$dataPad);
	} else {
		return $encryptedData;
	}
}
