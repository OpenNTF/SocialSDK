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
package com.ibm.sbt.provisioning.sample.app.weightedBSSCall;

import java.util.concurrent.Callable;

import com.ibm.sbt.provisioning.sample.app.WeightManager;

/**
 * Instances of this class represent weighted HTTP calls to the BSS API .
 * */
public abstract class WeightedBSSCall<T> implements Callable<T>, BSSCall {
	
	/**
	 * This method is <code>final</code> in order to prevent the subclasses from overriding it.<br>
	 * Its implementation will simply invoke the 
	 * {@link com.ibm.sbt.provisioning.sample.app.WeightManager#updateCurrentWeight} method with the goal
	 * of keeping the current weight associated with the organization updated.
	 * If the current weight is not going to exceed the threshold for that organization, the logic 
	 * for querying the BSS API will be triggered. This will happen thanks to the invocation of the {@link #doCall()}
	 *  method on the same instance this method ( {@link #call} ) has been invoked (<code>this</code>). 
	 * The BSS call's weight is retrieved by the  
	 * {@link com.ibm.sbt.provisioning.sample.app.WeightManager#updateCurrentWeight} method 
	 * thanks to the BSS call's key (as specified by the {@link BSSCall} <code>interface</code>)
	 * passed to it as argument
	 * 
	 */
	@Override
	public final T call() throws Exception {
		T toReturn = null ;
    	if(WeightManager.getInstance().updateCurrentWeight(this.getUrl(), this.getMethod())){
			toReturn = this.doCall();
		}
		return toReturn ;
	}

	/**
	 * The classes belonging to the package <code>com.ibm.sbt.provisioning.sample.app.weightedBSSCall</code> and extending
	 * this abstract class will implement this method for encapsulating in it the logic needed for triggering the HTTP calls to the BSS API.
	 * This logic is triggered by mean of invocation of the <code>final</code> {@link #call()} method of this class
	 *  on the instances of the classes belonging to that package. 
	 *  The <code>final</code> {@link #call()} method is the only one visible outside the package.
	 *  This design prevent the code outside the package from directly calling this method in order to avoid querying the BSS API 
	 *  without keeping the current weight associated with the organization updated.
	 */
	protected abstract T doCall() throws Exception ;
}
