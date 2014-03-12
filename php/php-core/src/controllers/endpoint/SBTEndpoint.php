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
 * @author Benjamin Jakobus
 */

interface SBTEndpoint 
{
	/**
	 * Makes the request to the server.
	 *
	 * @param string $server
	 * @param string $service		The rest service to access e.g. /connections/communities/all
	 * @param string $method		GET, POST or PUT
	 * @param array $options
	 * @param string $body
	 * @param string $headers
	 */
	public function makeRequest($server, $service, $method, $options, $body = null, $headers = null, $endpointName = "connections");
}