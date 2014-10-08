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
package com.ibm.sbt.services.client.smartcloud.bss;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.v4_0;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

/**
 * @author mwallace
 *
 */
public enum BssUrls implements URLContainer {

	API_RESOURCE_CUSTOMER(new VersionedUrl(v4_0, "/api/bss/resource/customer")),
	API_RESOURCE_CUSTOMER_CUSTOMERID(new VersionedUrl(v4_0, "/api/bss/resource/customer/{customerId}")),
	API_RESOURCE_GET_CUSTOMERS_BY_CONTACT_EMAIL(new VersionedUrl(v4_0, "/api/bss/resource/customer?_namedQuery=getCustomersByContactEmail&emailAddress={emailAddress}")),
	API_RESOURCE_GET_CUSTOMERS_BY_SUBSCIBER_EMAIL(new VersionedUrl(v4_0, "/api/bss/resource/customer?_namedQuery=getCustomersBySubscriberEmail&emailAddress={emailAddress}")),
	API_RESOURCE_GET_CUSTOMER_BY_ORGNAME(new VersionedUrl(v4_0, "/api/bss/resource/customer?_namedQuery=getCustomerByOrgName&orgName={orgName}")),
	API_RESOURCE_SUBSCRIBER(new VersionedUrl(v4_0, "/api/bss/resource/subscriber")),
	API_RESOURCE_SUBSCRIBER_SUBSCRIBERID(new VersionedUrl(v4_0, "/api/bss/resource/subscriber/{subscriberId}")),
	API_RESOURCE_ENTITLE_SUBSCRIBER(new VersionedUrl(v4_0, "/api/bss/resource/subscriber/{subscriberId}/subscription/{subscriptionId}?acceptTOU={acceptTOU}")),
	API_RESOURCE_REVOKE_SUBSCRIBER(new VersionedUrl(v4_0, "/api/bss/resource/subscriber/{subscriberId}/seat/{seatId}?_force={force}")),
	API_RESOURCE_GET_SUBSCRIBERS_BY_EMAIL_ADDRESS(new VersionedUrl(v4_0, "/api/bss/resource/subscriber?_namedQuery=getSubscriberByEmailAddress&emailAddress={emailAddress}")),
	API_RESOURCE_GET_SUBSCRIBER_BY_CUSTOMER(new VersionedUrl(v4_0, "/api/bss/resource/subscriber?_namedQuery=getSubscriberByCustomer&customer={customerId}")),
	API_RESOURCE_SUBSCRIPTION(new VersionedUrl(v4_0, "/api/bss/resource/subscription")),
	API_RESOURCE_SUBSCRIPTION_SUBSCRIPTIONID(new VersionedUrl(v4_0, "/api/bss/resource/subscription/{subscriptionId}")),
	API_RESOURCE_GET_SUBSCRIPTION_BY_CUSTOMERID(new VersionedUrl(v4_0, "/api/bss/resource/subscription?_namedQuery=getSubscriptionByCustomer&customerId=customerId")),
	API_RESOURCE_SUBSCRIPTION_SEAT(new VersionedUrl(v4_0, "/api/bss/resource/subscription/{subscriptionId}/seat/{seatId}")),
	API_AUTHORIZATION_GETROLELIST(new VersionedUrl(v4_0, "/api/bss/service/authorization/getRoleList?loginName={loginName}")),
	API_AUTHORIZATION_ASSIGNROLE(new VersionedUrl(v4_0, "/api/bss/service/authorization/assignRole?loginName={loginName}&role={role}")),
	API_AUTHORIZATION_UNASSIGNROLE(new VersionedUrl(v4_0, "/api/bss/service/authorization/unassignRole?loginName={loginName}&role={role}")),
	API_AUTHENTICATION_CHANGEPASSWORD(new VersionedUrl(v4_0, "/api/bss/service/authentication/changePassword")),
	API_AUTHENTICATION_RESETPASSWORD(new VersionedUrl(v4_0, "/api/bss/service/authentication/resetPassword?loginName={loginName}")),
	API_AUTHENTICATION_SETONETIMEPASSWORD(new VersionedUrl(v4_0, "/api/bss/service/authentication/setOneTimePassword")),
	API_RESOURCE_WORKFLOW(new VersionedUrl(v4_0, "/api/bss/resource/workflow")),
	API_RESOURCE_GET_WORKFLOW_BY_CUSTOMERID(new VersionedUrl(v4_0, "/api/bss/resource/workflow?_namedQuery=getWorkflowByCustomerId&customerId={customerId}")),
	API_RESOURCE_GET_WORKFLOW_BY_SUBSCRIBERID(new VersionedUrl(v4_0, "/api/bss/resource/workflow?_namedQuery=getWorkflowBySubscriberId&subscriberId={subscriberId}")),
	API_RESOURCE_GET_WORKFLOW_BY_STATUS(new VersionedUrl(v4_0, "/api/bss/resource/workflow?_namedQuery=getWorkflowByStatus&workflowStatus={workflowStatus}")),
	API_AUTHENTICATION_SETUSERPASSWORD(new VersionedUrl(v4_0, "/api/bss/service/authentication/setUserPassword?bypassPolicy={bypassPolicy}"));
	
	private URLBuilder builder;
	
	public static NamedUrlPart customerId(String customerId){
		return new NamedUrlPart("customerId", customerId);		
	}
		
	public static NamedUrlPart subscriberId(String subscriberId){
		return new NamedUrlPart("subscriberId", subscriberId);		
	}
		
	public static NamedUrlPart subscriptionId(String subscriptionId){
		return new NamedUrlPart("subscriptionId", subscriptionId);		
	}
		
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
	
	private BssUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}

}
