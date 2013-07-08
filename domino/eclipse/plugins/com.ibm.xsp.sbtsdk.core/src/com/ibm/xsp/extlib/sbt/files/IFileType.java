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
package com.ibm.xsp.extlib.sbt.files;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.xsp.extlib.sbt.files.FileServiceData.FileServiceAccessor;
import com.ibm.xsp.extlib.sbt.model.RestDataBlockAccessor;

/**
 * File service type.
 * @author Philippe Riand
 * @author Justin Murray
 */
public interface IFileType {
    
    /**
     * Get the name for the default endpoint.
     */
    public String getDefaultEndpoint();
    
    /**
     * Add the URL parameters specific to this service.
     * @param url
     * @return
     */
    public void addUrlParameters(Map<String, String> map, int index, int blockSize) throws ClientServicesException;

    /**
     * Create the Service object.
     */
    public ClientService createClientService(Endpoint endpoint, String url) throws ClientServicesException;

    /**
     * Makes an appropriate API call toe create a folder
     * @param authBean
     * @param folderName
     */
    public void createFolder(Endpoint authBean, String folderName);

    /**
     * Makes an appropriate API call to delete a file from within the service
     * 
     * @param accessor
     *          the accessor used to access the current service
     * @param authBean
     *          the authorization bean/endpoint for the current service
     * @param rowId
     *          the id of the document that is to be deleted. 
     */
    public void deleteRow(FileServiceAccessor accessor, Endpoint authBean, String rowId);

    /**
     * Determines the type of the service
     * @return
     *      a string that is unique to the current service and that can be used to identify the current service
     */
    public String getType();
    /**
     * Uses API calls to read all of the Files sored in the service
     * 
     * @param svc
     *          the service where the files are to be read from
     * @param accessor TODO
     * @param params
     *          a list of parameters that are to be added to the URL 
     * @return
     *          a list of FileEntry objects, whereby each item in the List maps to a file stored within the service
     * @throws ClientServicesException
     * 
     */
    public List<FileEntry> readFileEntries(ClientService svc, RestDataBlockAccessor accessor, Map<String, String> params) throws ClientServicesException;

    /**
     * Delegates to a proxy for operations such as File Download
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void serviceProxy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    /**
     * @param authBean
     * @param serverFile
     * @param params
     * @throws CloneNotSupportedException 
     */
    public void uploadFile(Endpoint authBean, File serverFile, FileServiceData dataSource, HashMap<String, String> params) throws CloneNotSupportedException;

    /**
     * @return
     */
    public boolean useFolders();
    
    public String getServiceUrl();

}
