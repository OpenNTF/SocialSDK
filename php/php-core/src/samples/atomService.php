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
 * This sample demonstrates how to retrieve the raw atom feed.
 * 
 * @author Benjamin Jakobus
 */

include_once '../autoload.php';
include_once BASE_PATH . '/controllers/services/SBTAtomService.php';

// Create SBTSettings instance
$settings = new SBTSettings();

$service = new SBTAtomService('SmartCloud');

// Make request to SmartCloud
$ret = @$service->makeRequest('GET', '/files/basic/api/myuserlibrary/feed', 'SmartCloud');

// Get all entry tags
$tags = $service->getAllTags('entry');

// Print tags
$service->printTags($tags);