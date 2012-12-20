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
package com.ibm.commons.runtime.impl.app;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.impl.AbstractRuntimeFactory;

/**
 * @author Mark Wallace
 * 
 * @date 6 Dec 2012
 */
public class RuntimeFactoryStandalone extends AbstractRuntimeFactory {

    public RuntimeFactoryStandalone() {
	set(this);
    }

    /* (non-Javadoc)
     * @see com.ibm.commons.runtime.RuntimeFactory#createApplication(java.lang.Object)
     */
    @Override
    public Application createApplication(Object applicationContext) {
	return new ApplicationStandalone(applicationContext);
    }

    /* (non-Javadoc)
     * @see com.ibm.commons.runtime.RuntimeFactory#createContext(com.ibm.commons.runtime.Application, java.lang.Object, java.lang.Object)
     */
    @Override
    public Context createContext(Application application, Object request, Object response) {
	return new ContextStandalone(application, request, response);
    }

}
