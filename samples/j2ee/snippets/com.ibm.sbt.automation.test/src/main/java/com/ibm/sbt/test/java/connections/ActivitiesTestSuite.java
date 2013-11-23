/*
 * � Copyright IBM Corp. 2013
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
package com.ibm.sbt.test.java.connections;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.test.java.connections.activities.AddMember;
import com.ibm.sbt.test.java.connections.activities.ChangeActivityNodeType;
import com.ibm.sbt.test.java.connections.activities.CreateActivity;
import com.ibm.sbt.test.java.connections.activities.CreateChatNode;
import com.ibm.sbt.test.java.connections.activities.CreateEmailNode;
import com.ibm.sbt.test.java.connections.activities.CreateEntryNode;
import com.ibm.sbt.test.java.connections.activities.CreateReplyNode;
import com.ibm.sbt.test.java.connections.activities.CreateSectionNode;
import com.ibm.sbt.test.java.connections.activities.CreateTodoNode;
import com.ibm.sbt.test.java.connections.activities.DeleteActivity;
import com.ibm.sbt.test.java.connections.activities.DeleteActivityNode;
import com.ibm.sbt.test.java.connections.activities.DeleteMember;
import com.ibm.sbt.test.java.connections.activities.GetActivitiesInTrash;
import com.ibm.sbt.test.java.connections.activities.GetActivity;
import com.ibm.sbt.test.java.connections.activities.GetActivityNode;
import com.ibm.sbt.test.java.connections.activities.GetActivityNodeWithFields;
import com.ibm.sbt.test.java.connections.activities.GetActivityNodesInTrash;
import com.ibm.sbt.test.java.connections.activities.GetActivityNodesOfAnActivity;
import com.ibm.sbt.test.java.connections.activities.GetAllActivities;
import com.ibm.sbt.test.java.connections.activities.GetAllTags;
import com.ibm.sbt.test.java.connections.activities.GetAllTodos;
import com.ibm.sbt.test.java.connections.activities.GetCompletedActivities;
import com.ibm.sbt.test.java.connections.activities.GetMember;
import com.ibm.sbt.test.java.connections.activities.GetMembers;
import com.ibm.sbt.test.java.connections.activities.GetMyActivities;
import com.ibm.sbt.test.java.connections.activities.MoveEntryToSection;
import com.ibm.sbt.test.java.connections.activities.RestoreActivity;
import com.ibm.sbt.test.java.connections.activities.RestoreActivityNode;
import com.ibm.sbt.test.java.connections.activities.UpdateActivity;
import com.ibm.sbt.test.java.connections.activities.UpdateActivityNode;
import com.ibm.sbt.test.java.connections.activities.UpdateMember;

/**
 * @author Vimal Dhupar
 * 
 * @date 15 Nov 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ AddMember.class, ChangeActivityNodeType.class, CreateActivity.class, CreateChatNode.class, CreateEmailNode.class
	, CreateEntryNode.class, CreateReplyNode.class, CreateSectionNode.class, CreateTodoNode.class, DeleteActivity.class, DeleteActivityNode.class, DeleteMember.class, 
	GetActivitiesInTrash.class, GetActivity.class, GetActivityNode.class, GetActivityNodesInTrash.class, GetActivityNodesOfAnActivity.class, 
	GetActivityNodeWithFields.class, GetAllActivities.class, GetAllTags.class, GetAllTodos.class, GetCompletedActivities.class, GetMember.class, GetMembers.class, GetMyActivities.class, 
	MoveEntryToSection.class, RestoreActivity.class, RestoreActivityNode.class, UpdateActivity.class, UpdateActivityNode.class, UpdateMember.class}) 
public class ActivitiesTestSuite {
    @AfterClass
    public static void cleanup() {
        TestEnvironment.cleanup();
    }
}
