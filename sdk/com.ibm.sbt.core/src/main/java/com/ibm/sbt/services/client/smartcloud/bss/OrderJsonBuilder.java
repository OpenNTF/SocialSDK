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

import java.math.BigInteger;


/**
 * @author mwallace
 *
 */
public class OrderJsonBuilder extends BaseJsonBuilder {
	
	private JsonField DurationUnits = new JsonField("DurationUnits", true, -1);
	private JsonField DurationLength = new JsonField("DurationLength", true, -1);
	private JsonField PartNumber = new JsonField("PartNumber", true, -1);
	private JsonField PartQuantity = new JsonField("PartQuantity", true, -1);
	private JsonField BillingFrequency = new JsonField("BillingFrequency", false, -1);
	private JsonField ServiceCode = new JsonField("ServiceCode", false, -1);
	private JsonField OrderNumber = new JsonField("OrderNumber", false, -1);
	private JsonField CustomerId = new JsonField("CustomerId", true, -1);
	
	public static final String ORDER =
					"{" +
					   "\"Order\": {" +
					       "\"OrderItemSet\":  [" +
					           "{" +
					               "\"DurationUnits\": \"%{getDurationUnits}\"," +
					               "\"DurationLength\": %{getDurationLength}," +
					               "\"PartNumber\": \"%{getPartNumber}\"," +
					               "\"PartQuantity\": %{getPartQuantity}," +
					               "\"BillingFrequency\": \"%{getBillingFrequency}\"," +
					               "\"ServiceCode\": \"%{getServiceCode}\"," +
					               "\"OrderNumber\": \"%{getOrderNumber}\"" +
					           "}" +
					       "]," +
					       "\"CustomerId\": %{getCustomerId}" +
					   "}" +
					"}"; 

	/**
	 * Default constructor
	 */
	public OrderJsonBuilder() {
		template = ORDER;
	}

	/**
	 * @return the durationUnits
	 */
	public String getDurationUnits() {
		return (String)DurationUnits.getValue();
	}

	/**
	 * @param durationUnits the durationUnits to set
	 */
	public OrderJsonBuilder setDurationUnits(SubscriptionManagementService.DurationUnits durationUnits) {
		DurationUnits.setValue(durationUnits.name());
		return this;
	}

	/**
	 * @return the durationLength
	 */
	public Integer getDurationLength() {
		Object value = DurationLength.getValue();
		return (value == null) ? 0 : (Integer)value;
	}

	/**
	 * @param durationLength the durationLength to set
	 */
	public OrderJsonBuilder setDurationLength(int durationLength) {
		DurationLength.setValue(durationLength);
		return this;
	}

	/**
	 * @return the partNumber
	 */
	public String getPartNumber() {
		return (String)PartNumber.getValue();
	}

	/**
	 * @param partNumber the partNumber to set
	 */
	public OrderJsonBuilder setPartNumber(String partNumber) {
		PartNumber.setValue(partNumber);
		return this;
	}

	/**
	 * @return the partQuantity
	 */
	public Integer getPartQuantity() {
		Object value = PartQuantity.getValue();
		return (value == null) ? 0 : (Integer)value;
	}

	/**
	 * @param partQuantity the partQuantity to set
	 */
	public OrderJsonBuilder setPartQuantity(int partQuantity) {
		PartQuantity.setValue(partQuantity);
		return this;
	}

	/**
	 * @return the customerId
	 */
	public BigInteger getCustomerId() {
		Object value = CustomerId.getValue();
		return (value == null) ? null : (BigInteger)value;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public OrderJsonBuilder setCustomerId(BigInteger customerId) {
		CustomerId.setValue(customerId);
		return this;
	}

	/**
	 * @return the billingFrequency
	 */
	public String getBillingFrequency() {
		return (String)BillingFrequency.getValue();
	}

	/**
	 * @param billingFrequency the billingFrequency to set
	 */
	public OrderJsonBuilder setBillingFrequency(SubscriptionManagementService.BillingFrequency billingFrequency) {
		BillingFrequency.setValue(billingFrequency.name());
		return this;
	}

	/**
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return (String)ServiceCode.getValue();
	}

	/**
	 * @param serviceCode the serviceCode to set
	 */
	public OrderJsonBuilder setServiceCode(String serviceCode) {
		ServiceCode.setValue(serviceCode);
		return this;
	}

	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return (String)OrderNumber.getValue();
	}

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public OrderJsonBuilder setOrderNumber(String orderNumber) {
		OrderNumber.setValue(orderNumber);
		return this;
	}

}
