/*
 * © Copyright IBM Corp. 2010
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
package com.ibm.xsp.extlib.sbt.files.type;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.complex.ValueBindingObjectImpl;
import com.ibm.xsp.extlib.sbt.files.IFileType;



/**
 * Abstract File type.
 * @author Philippe Riand
 */
public abstract class AbstractType extends ValueBindingObjectImpl implements IFileType {
    public final static String PARAM_ENDPOINT_NAME = "endpointName";
    public final static String PARAM_ID = "id";
    public final static String PARAM_REPOSITORY_ID = "repId";
    public final static String PARAM_TYPE = "type";

    public AbstractType() {
        super();
    }
    
    public void serviceProxy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        throw new ServletException(StringUtil.format("Proxy is not implemented for file type {0}",getClass().getName()));
    }
    
    public String getServiceUrl(){
        return null;
    }
    
    public abstract String getDefaultEndpoint();
}
