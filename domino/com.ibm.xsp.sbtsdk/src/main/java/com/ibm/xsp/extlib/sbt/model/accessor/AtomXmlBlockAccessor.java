/*
 *  Copyright IBM Corp. 2010
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

package com.ibm.xsp.extlib.sbt.model.accessor;

import com.ibm.xsp.extlib.sbt.model.RestDataSource;


/**
 * Data accessor holding ATOM XML documents.
 * <p>
 * </p>
 * @author Philippe Riand
 */
public abstract class AtomXmlBlockAccessor extends XmlArrayBlockAccessor {
    
    private static final long serialVersionUID = 1L;
        
    public AtomXmlBlockAccessor() {
    }
    public AtomXmlBlockAccessor(RestDataSource ds) {
        super(ds);
    }
    
    @Override
    public String getEntryXPath() {
        return "/feed/entry";
    }
    
    @Override
    public String getTotalCountXPath() {
        return "/feed/opensearch:totalResults";
    }
}
