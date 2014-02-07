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

import com.ibm.sbt.services.client.smartcloud.bss.AddSubscriberTest;
import com.ibm.sbt.services.client.smartcloud.bss.CancelSubscriptionTest;
import com.ibm.sbt.services.client.smartcloud.bss.CreateSubscriptionTest;
import com.ibm.sbt.services.client.smartcloud.bss.DeleteSubscriberTest;
import com.ibm.sbt.services.client.smartcloud.bss.GetCustomerByIdTest;
import com.ibm.sbt.services.client.smartcloud.bss.GetCustomersTest;
import com.ibm.sbt.services.client.smartcloud.bss.GetSubscriberByIdTest;
import com.ibm.sbt.services.client.smartcloud.bss.GetSubscribersPagingTest;
import com.ibm.sbt.services.client.smartcloud.bss.GetSubscribersTest;
import com.ibm.sbt.services.client.smartcloud.bss.RegisterCustomerTest;
import com.ibm.sbt.services.client.smartcloud.bss.SuspendCustomerTest;
import com.ibm.sbt.services.client.smartcloud.bss.SuspendSubscriberTest;
import com.ibm.sbt.services.client.smartcloud.bss.SuspendSubscriptionTest;
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
	AddSubscriberTest.class, 
	CancelSubscriptionTest.class,
	CreateSubscriptionTest.class,
	DeleteSubscriberTest.class,
	GetCustomerByIdTest.class,
	GetCustomersTest.class,
	GetSubscriberByIdTest.class,
	GetSubscribersPagingTest.class,
	GetSubscribersTest.class,
	RegisterCustomerTest.class,
	SuspendCustomerTest.class,
	SuspendSubscriberTest.class,
	SuspendSubscriptionTest.class,
	UnregisterCustomerTest.class,
	UnsuspendCustomerTest.class,
	UnsuspendSubscriberTest.class,
	UnsuspendSubscriptionTest.class,
	UpdateCustomerProfileTest.class,
	UpdateSubscriberProfileTest.class
})

public class BssTestSuite {
}
