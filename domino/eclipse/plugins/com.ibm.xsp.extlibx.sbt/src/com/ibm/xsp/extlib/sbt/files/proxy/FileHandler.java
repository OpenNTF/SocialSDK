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
package com.ibm.xsp.extlib.sbt.files.proxy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.proxy.handlers.AbstractProxyHandler;
import com.ibm.xsp.extlib.sbt.files.IFileType;
import com.ibm.xsp.extlib.sbt.files.type.ConnectionsFiles;
import com.ibm.xsp.extlib.sbt.files.type.DropboxFiles;
import com.ibm.xsp.extlib.sbt.files.type.LotusLiveFiles;

/**
 * FileProxy Handler.
 * @author priand
 */
public class FileHandler extends AbstractProxyHandler {
    
    public static final String URL_PATH = "files";

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Access the filetype to use
        String type = request.getParameter("type");
        
        if(StringUtil.isEmpty(type)) {
            throw new ServletException(StringUtil.format("'type' is not specified"));
        }
        IFileType fileType = loadFileType(type);
        if(fileType==null) {
            throw new ServletException(StringUtil.format("File type {0} is invalid", type));
        }
        
        // Then delegate to it
        fileType.serviceProxy(request, response);
    }
    
    protected IFileType loadFileType(String type) {
        if(type.equals(LotusLiveFiles.TYPE)) {
            return new LotusLiveFiles();
        }
        if(type.equals(DropboxFiles.TYPE)) {
            return new DropboxFiles();
        }
        if(type.equals(ConnectionsFiles.TYPE)){
            return new ConnectionsFiles();
        }
        return null;
    }
}