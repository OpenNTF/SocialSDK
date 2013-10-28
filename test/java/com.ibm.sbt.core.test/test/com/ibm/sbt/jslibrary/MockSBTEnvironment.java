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
package com.ibm.sbt.jslibrary;

/**
 * @author mwallace
 *
 */
public class MockSBTEnvironment extends SBTEnvironment {
	
	public MockSBTEnvironment() {
		setEndpoints("connections,smartcloud");
		setProperties("mock.email");
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.SBTEnvironment#getEndpointsArray()
	 */
	@Override
	public Endpoint[] getEndpointsArray() {
		// TODO Auto-generated method stub
		return super.getEndpointsArray();
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.SBTEnvironment#getPropertiesArray()
	 */
	@Override
	public Property[] getPropertiesArray() {
		// TODO Auto-generated method stub
		return super.getPropertiesArray();
	}

}
