***REMOVED***
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
 * Encapsulates a cookie.
 * 
 * @author Benjamin Jakobus
 */
class SBTKCookie
{
	private static $adapter;
	
	/**
	 * Consructor.
	 * 
	 * @param SBTKCookieAdapter $adp
	 * 
	 * @author Benjamin Jakobus
	 */
	public static function init(SBTKCookieAdapter $adp)
	{
		self::$adapter = $adp;
	}
	
	/**
	 * Retrieve the value from the cookie denoted by $index.
	 * 
	 * @param string $index
	 * @return Cookie contents
	 * 
	 * @author Benjamin Jakobus
	 */
	public static function get($index)
	{
		return self::$adapter->get($index);
	}
	
	/**
	 * Sets the cookie.
	 *
	 * @param string $sessionName
	 * @param string $value
	 * @pparam int $timestamp
	 *
	 * @author Benjamin Jakobus
	 */
	public static function set($sessionName, $value, $timestamp)
	{
		return self::$adapter->set($sessionName, $value, $timestamp);
	}
}