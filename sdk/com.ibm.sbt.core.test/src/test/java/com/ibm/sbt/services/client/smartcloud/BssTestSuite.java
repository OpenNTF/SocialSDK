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
package com.ibm.sbt.services.client.smartcloud;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.services.client.smartcloud.bss.ActivateSubscriberTest;
import com.ibm.sbt.services.client.smartcloud.bss.AddSubscriberTest;
import com.ibm.sbt.services.client.smartcloud.bss.AssignRoleTest;
import com.ibm.sbt.services.client.smartcloud.bss.CancelSubscriptionTest;
import com.ibm.sbt.services.client.smartcloud.bss.ChangePasswordTest;
import com.ibm.sbt.services.client.smartcloud.bss.CreateSubscriptionTest;
import com.ibm.sbt.services.client.smartcloud.bss.DeleteSubscriberTest;
import com.ibm.sbt.services.client.smartcloud.bss.EntitleSubscriberTest;
import com.ibm.sbt.services.client.smartcloud.bss.GetCustomerByIdTest;
import com.ibm.sbt.services.client.smartcloud.bss.GetCustomersTest;
import com.ibm.sbt.services.client.smartcloud.bss.GetRolesTest;
import com.ibm.sbt.services.client.smartcloud.bss.GetSubscriberByIdTest;
import com.ibm.sbt.services.client.smartcloud.bss.GetSubscribersPagingTest;
import com.ibm.sbt.services.client.smartcloud.bss.GetSubscribersTest;
import com.ibm.sbt.services.client.smartcloud.bss.ProvisionSubscriberTest;
import com.ibm.sbt.services.client.smartcloud.bss.RegisterCustomerTest;
import com.ibm.sbt.services.client.smartcloud.bss.ResetPasswordTest;
import com.ibm.sbt.services.client.smartcloud.bss.SetOneTimePasswordTest;
import com.ibm.sbt.services.client.smartcloud.bss.SuspendCustomerTest;
import com.ibm.sbt.services.client.smartcloud.bss.SuspendSubscriberTest;
import com.ibm.sbt.services.client.smartcloud.bss.SuspendSubscriptionTest;
import com.ibm.sbt.services.client.smartcloud.bss.UnassignRoleTest;
import com.ibm.sbt.services.client.smartcloud.bss.UnregisterCustomerTest;
import com.ibm.sbt.services.client.smartcloud.bss.UnsuspendCustomerTest;
import com.ibm.sbt.services.client.smartcloud.bss.UnsuspendSubscriberTest;
import com.ibm.sbt.services.client.smartcloud.bss.UnsuspendSubscriptionTest;
import com.ibm.sbt.services.client.smartcloud.bss.UpdateCustomerProfileTest;
import com.ibm.sbt.services.client.smartcloud.bss.UpdateSubscriberProfileTest;

/**
 * @author mwallace
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
	ActivateSubscriberTest.class,
	AddSubscriberTest.class, 
	AssignRoleTest.class,
	CancelSubscriptionTest.class,
	ChangePasswordTest.class,
	CreateSubscriptionTest.class,
	DeleteSubscriberTest.class,
	EntitleSubscriberTest.class,
	GetCustomerByIdTest.class,
	GetCustomersTest.class,
	GetRolesTest.class,
	GetSubscriberByIdTest.class,
	GetSubscribersPagingTest.class,
	GetSubscribersTest.class,
	ProvisionSubscriberTest.class,
	RegisterCustomerTest.class,
	ResetPasswordTest.class,
	SetOneTimePasswordTest.class,
	SuspendCustomerTest.class,
	SuspendSubscriberTest.class,
	SuspendSubscriptionTest.class,
	UnassignRoleTest.class,
	UnregisterCustomerTest.class,
	UnsuspendCustomerTest.class,
	UnsuspendSubscriberTest.class,
	UnsuspendSubscriptionTest.class,
	UpdateCustomerProfileTest.class,
	UpdateSubscriberProfileTest.class
})

public class BssTestSuite {
}
