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

package com.ibm.sbt.services.client.connections.search;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.AtomEntityList;

/**
 * Class used in representing List of Result objects of Search service
 * @author Manish Kataria
 * @author Carlos Manias
 */

public class ScopeList extends AtomEntityList<Scope>{

	public ScopeList(Response requestData, IFeedHandler<Scope> feedHandler) {
		super(requestData, feedHandler);
	}

}
