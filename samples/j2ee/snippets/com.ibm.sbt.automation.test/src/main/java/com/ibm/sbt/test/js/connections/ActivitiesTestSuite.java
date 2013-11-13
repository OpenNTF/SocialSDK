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
package com.ibm.sbt.test.js.connections;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.test.js.connections.activities.api.AddMemberToActivity;
import com.ibm.sbt.test.js.connections.activities.api.ChangeActivityNodeType;
import com.ibm.sbt.test.js.connections.activities.api.CreateActivity;
import com.ibm.sbt.test.js.connections.activities.api.CreateActivityNode;
import com.ibm.sbt.test.js.connections.activities.api.DeleteActivityMember;
import com.ibm.sbt.test.js.connections.activities.api.DeleteAndRestoreActivity;
import com.ibm.sbt.test.js.connections.activities.api.DeleteAndRestoreActivityNode;
import com.ibm.sbt.test.js.connections.activities.api.GetActivitiesInTrash;
import com.ibm.sbt.test.js.connections.activities.api.GetActivity;
import com.ibm.sbt.test.js.connections.activities.api.GetActivityMember;
import com.ibm.sbt.test.js.connections.activities.api.GetActivityMembers;
import com.ibm.sbt.test.js.connections.activities.api.GetActivityNode;
import com.ibm.sbt.test.js.connections.activities.api.GetActivityNodeTags;
import com.ibm.sbt.test.js.connections.activities.api.GetActivityNodes;
import com.ibm.sbt.test.js.connections.activities.api.GetActivityNodesInTrash;
import com.ibm.sbt.test.js.connections.activities.api.GetActivityTags;
import com.ibm.sbt.test.js.connections.activities.api.GetAllActivities;
import com.ibm.sbt.test.js.connections.activities.api.GetAllTags;
import com.ibm.sbt.test.js.connections.activities.api.GetAllToDos;
import com.ibm.sbt.test.js.connections.activities.api.GetCompletedActivities;
import com.ibm.sbt.test.js.connections.activities.api.GetMyActivities;
import com.ibm.sbt.test.js.connections.activities.api.MoveActivityNodeToSection;
import com.ibm.sbt.test.js.connections.activities.api.UpdateActivity;
import com.ibm.sbt.test.js.connections.activities.api.UpdateActivityMember;
import com.ibm.sbt.test.js.connections.activities.api.UpdateActivityNode;

/**
 * @author mwallace
 * 
 * @date 6 Mar 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	GetMyActivities.class, 
	GetCompletedActivities.class, 
	GetAllActivities.class,
	AddMemberToActivity.class,
	ChangeActivityNodeType.class,
	CreateActivity.class,
	CreateActivityNode.class,
	DeleteActivityMember.class,
	DeleteAndRestoreActivity.class,
	DeleteAndRestoreActivityNode.class,
	GetActivitiesInTrash.class,
	GetActivity.class,
	GetActivityMember.class,
	GetActivityMembers.class,
	GetActivityNodesInTrash.class,
	GetActivityNodeTags.class,
	//GetActivityTags.class,	
	GetAllTags.class,
	GetAllToDos.class,	
	MoveActivityNodeToSection.class,
	UpdateActivity.class,
	UpdateActivityMember.class,
	UpdateActivityNode.class,
	GetActivityNode.class,
	GetActivityNodes.class})
public class ActivitiesTestSuite {
	@AfterClass
	public static void cleanup() {
	}
}
