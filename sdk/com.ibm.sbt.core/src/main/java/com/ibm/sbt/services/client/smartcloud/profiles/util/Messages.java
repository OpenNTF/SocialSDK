/*
 * © Copyright IBM Corp. 2012
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
package com.ibm.sbt.services.client.smartcloud.profiles.util;

/**
 * Class used to retrieve translatable message values from the associated properties file.
 * 
 * @author Vimal Dhupar
 */
public class Messages {

	public static String	InvalidValue_1			= "Problem occurred while reading subscriber id of the user";
	public static String	InvalidValue_2			= "Cant find subscriber Id for the User. Invalid User.";
	public static String	InvalidValue_3			= "Invalid UserId.";
	public static String	ProfileError_1			= "Problem occurred while loading user profile";
	public static String	ProfileError_2			= "Problem occurred while fetching Contacts";
	public static String	ProfileError_3			= "Problem occurred while fetching Connections";

	private Messages() {
	}
}
