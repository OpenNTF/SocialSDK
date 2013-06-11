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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.w3c.dom.Document;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.designer.runtime.util.MIME;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.HandlerJson;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.ClientService.HandlerXml;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.util.DataNavigator;
import com.ibm.sbt.util.XmlNavigator;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.extlib.beans.UserBean;
import com.ibm.xsp.extlib.log.ExtlibCoreLogger;
import com.ibm.xsp.extlib.sbt.files.FileEntry;
import com.ibm.xsp.extlib.sbt.files.FileServiceData;
import com.ibm.xsp.extlib.sbt.files.FileServiceData.FileServiceAccessor;
import com.ibm.xsp.extlib.sbt.model.RestDataBlockAccessor;
import com.ibm.xsp.extlib.sbt.services.client.LotusLiveService;
import com.ibm.xsp.util.ManagedBeanUtil;

/**
 * LotusLiveFiles File type.
 * @author Philippe Riand
 * @author Justin Murray
 * @author doconnor
 */
public class LotusLiveFiles extends AbstractType {

    public static final String LOTUS_LIVE_SUBSCRIBER_ID = "lotusLiveSubscriberId";
    public static final String SERVICE_URL              = "files/basic/cmis/repository";
    public static final String TYPE                     = "lotuslive";

    public LotusLiveFiles() {
        super();
    }

    public void addUrlParameters(Map<String, String> map, int index, int blockSize) throws ClientServicesException {
        map.put("skipCount", String.valueOf(index));
        map.put("maxItems", String.valueOf(blockSize));
    }

    private void authenticate(RestDataBlockAccessor accessor) throws ClientServicesException {
        String endpoint = accessor.getEndpoint();
        if (StringUtil.isEmpty(endpoint)) {
            endpoint = EndpointFactory.SERVER_LOTUSLIVE;
        }
        Endpoint ep = EndpointFactory.getEndpoint(endpoint);
        if (ep != null) {
            if (!ep.isAuthenticated()) {
                ep.authenticate(false);
            }
        }
    }

    protected String calculateBytes(String bytes) {
        Double x = Double.parseDouble(bytes);
        DecimalFormat oneDigit = new DecimalFormat("#,##0.0");

        // Conversion to GB
        if (x > 1073741824) {
            x = x / 1073741824;
            return oneDigit.format(x) + " GB";
        }

        // Conversion to MB
        if (x > 1048576) {
            x = (x / 1048576);
            return oneDigit.format(x) + " MB";
        }

        // Conversion to KB
        if (x > 768) {
            x = x / 1024;
            return oneDigit.format(x) + " KB";
        }

        return bytes + " bytes";

    }

    private String composeRepositoryID(FileEntry entry) {
        return "p!" + entry.getUserId();
    }

    public ClientService createClientService(Endpoint endpoint, String url) throws ClientServicesException {
        if(StringUtil.isEmpty(url)){
            url = "files/basic/cmis/repository";
        }
        LotusLiveService svc = new LotusLiveService(endpoint);
        return svc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ibm.xsp.extlib.sbt.files.IFileType#createFolder(com.ibm.xsp.extlib.security.authorization.AuthorizationBean,
     * java.lang.String)
     */
    public void createFolder(Endpoint authBean, String folderName) {
        // Folders not implemented in LotusLiveFiles
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#deleteRow(com.ibm.xsp.extlib.sbt.files.FileServiceData.Accessor,
     * com.ibm.xsp.extlib.security.authorization.AuthorizationBean, java.lang.String)
     */
    public void deleteRow(FileServiceAccessor accessor, Endpoint authBean, String rowId) {
        String deleteURL = accessor.getServiceUrl() + "/" + getRepositoryID() + "/object/snx:file!" + rowId;
        try {
            authenticate(accessor);
            ClientService svc = createClientService(authBean, deleteURL);
            svc.delete(deleteURL);
        } catch (ClientServicesException e) {
            throw new FacesExceptionEx(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.type.AbstractType#getEndpoint()
     */
    @Override
    public String getDefaultEndpoint() {
        return EndpointFactory.getEndpointName(EndpointFactory.SERVER_LOTUSLIVE);
    }

    protected String getRepositoryID() {
        String subId = (String) UserBean.get().getPerson().getField(LOTUS_LIVE_SUBSCRIBER_ID);
        if(StringUtil.isEmpty(subId)){
            if(ExtlibCoreLogger.SBT.isErrorEnabled()){
                ExtlibCoreLogger.SBT.errorp(this, "getRepositoryID", "LotusLive subscriber ID is null. Repository IDs will not be resolved. Ensure that the \"extlib.people.provider\" property has been set in the application's xsp.properties (e.g. \nextlib.people.provider=lotuslive\nor some variation of this must be set in xsp.properties)");
            }
        }
        subId = "p!" + subId;
        return subId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#getType()
     */
    public String getType() {
        return TYPE;
    }

    public List<FileEntry> readFileEntries(ClientService svc, RestDataBlockAccessor accessor, Map<String, String> params)
            throws ClientServicesException {
        authenticate(accessor);
        HandlerXml xml = new HandlerXml();
        Document document = (Document) svc.get("files/basic/cmis/repository",params,xml);

        ArrayList<FileEntry> entries = new ArrayList<FileEntry>();
        // Extract the content from the ATOM feed...
        XmlNavigator navigator = new XmlNavigator(document);
        //TODO Externalize these constants
        DataNavigator mynav = navigator.get("feed/entry");
        if (mynav != null) {
            String epName = accessor.findEndpointName();
            for (int i = 0; i < mynav.getCount(); i++) {
                FileEntry entry = new FileEntry();
                DataNavigator nav = mynav.get(i);
                entry.setUserId(params.get("subscriberId"));
                entry.setTitle(nav.stringValue("title"));
                entry.setUpdated(nav.dateValue("updated"));
                entry.setPublished(nav.dateValue("published"));
                entry.setAuthorName(nav.stringValue("author/name"));
                
                String id = nav.stringValue("id");
                if (StringUtil.isNotEmpty(id)) {
                    int index = id.indexOf("snx:file!");
                    if (index != -1) {
                        index = index + "snx:file!".length();
                        id = id.substring(index);
                    }
                }
                //hack
                id=id.replace("http://www.ibm.com/xmlns/prod/sn/cmis/00000000-0000-0000-0001-000000000000!", "");
                entry.setFileId(id);
                entry.setUniqueId(id);
                DataNavigator propsNavigator = nav.get("object/properties");
                String description = propsNavigator.get("propertyString").selectEq("@displayName", "Description").stringValue("value");
                entry.setDescription(StringUtil.getNonNullString(description));
                String size = propsNavigator.get("propertyInteger").selectEq("@propertyDefinitionId", "cmis:contentStreamLength").stringValue("value");
                entry.setSize(calculateBytes(size));
                String version = propsNavigator.get("propertyString").selectEq("@propertyDefinitionId", "cmis:versionLabel").stringValue("value");
                entry.setVersion(StringUtil.getNonNullString(version));
                String visibility = propsNavigator.get("propertyString").selectEq("@propertyDefinitionId", "snx:visibilityComputed").stringValue("value");
                entry.setVisibility(StringUtil.getNonNullString(visibility));
                String mimeType = propsNavigator.get("propertyString").selectEq("@propertyDefinitionId", "cmis:contentStreamMimeType").stringValue("value");
                entry.setMimeType(mimeType);
                entry.setIcon(MimeIconRegistry.getInstance().get(mimeType));
                
                String proxyUrl = "xsp/.proxy/files/" + entry.getTitle() + "?" + PARAM_TYPE + "=" + TYPE + "&" + PARAM_ID + "="
                        + entry.getFileId() + "&" + PARAM_REPOSITORY_ID + "=" + composeRepositoryID(entry) + "&"+"fileName="+entry.getTitle()+ "&" + PARAM_ENDPOINT_NAME
                        + "=";
                
                if (StringUtil.isNotEmpty(epName)) {
                    proxyUrl = proxyUrl + epName;
                }
                else {
                    proxyUrl = proxyUrl + TYPE;
                }
                entry.setProxyURL(proxyUrl);
                entries.add(entry);
            }
        }
        return entries;
    }

    @Override
    public void serviceProxy(HttpServletRequest request, HttpServletResponse servletResponse) throws ServletException, IOException {

        try {
            String endpoint = request.getParameter(PARAM_ENDPOINT_NAME);
            if (StringUtil.isEmpty(endpoint)) {
                endpoint = TYPE;
            }
            Endpoint bean = (Endpoint) ManagedBeanUtil.getBean(FacesContext.getCurrentInstance(), endpoint);

            if (bean == null) {
                throw new ServletException("AuthorizationBean not found");
            }
            if (!bean.isAuthenticated()) {
                bean.authenticate(false);
            }

            String fileId = request.getParameter(PARAM_ID);
            String repositoryId = request.getParameter(PARAM_REPOSITORY_ID);
            String href = "/" + repositoryId + "/object/snx:file!" + fileId + "/stream/" + fileId;
            LotusLiveService svc = (LotusLiveService) createClientService(bean, href);
            BasicHttpResponse httpResp = null;
            Object file;
            //https://apps.na.collabserv.com/files/app/file/c72336bf-bb61-44f6-9712-5ee657ef17cc
            try {
                file =  svc.get("files/apps/file/"+fileId);
                
            } catch (ClientServicesException e) {
                throw new FacesExceptionEx(e, "Failed to perform proxy request");
            }

            String nameUTF8 = URLEncoder.encode(request.getParameter("fileName"),"utf-8");//$NON-NLS-1$
            nameUTF8 = nameUTF8.replaceAll("\\+", "%20");//$NON-NLS-1$ //$NON-NLS-2$
            
            String userAgent= request.getHeader("User-Agent"); //$NON-NLS-1$
            if(StringUtil.isNotEmpty(userAgent) && userAgent.contains("Firefox")) //$NON-NLS-1$
            	//use the format of value of non-acsii charset: filename*="utf8''nameUTF8"
            	servletResponse.setHeader("Content-Disposition","attachment; filename*=\"utf8\'\'"+nameUTF8+'"');//$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
            else
            	servletResponse.setHeader("Content-Disposition","attachment; filename="+nameUTF8+"");//$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$	
            
			 OutputStream out = servletResponse.getOutputStream();
			    try{
            		StreamUtil.copyStream((InputStream) file, out);
			    }
			    finally{
			    	out.close();
			    }

        } catch (Exception e) {
            throw new ServletException(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#uploadFile(com.ibm.xsp.extlib.services.client.Endpoint, java.io.File,
     * java.util.HashMap)
     */
    public void uploadFile(Endpoint authBean, File serverFile, FileServiceData dataSource, HashMap<String, String> params) throws CloneNotSupportedException {
        String name = params.get("file");
        try {
            int dot = name.lastIndexOf('.');
            String ext = null;
            if (dot > -1) {
                ext = name.substring(dot + 1); // add one for the dot!
            }
            if (StringUtil.isEmpty(ext)) {
                throw new FacesExceptionEx(new NullPointerException(), "Extension of file being uploaded may not be null");
            }
            if (authBean != null && !authBean.isAuthenticated()) {
                authBean.authenticate(false);
            }
            LotusLiveService svc = (LotusLiveService) createClientService(authBean, SERVICE_URL);
            svc.setMimeForUpload(MIME.getMIMETypeFromExtension(ext));
            //svc.execRequest("post", params, serverFile, null);
            //TODO-Padraic review
            HandlerJson json= new HandlerJson();
            svc.post(SERVICE_URL, params, null, json);
            
        } catch (ClientServicesException e) {
            throw new FacesExceptionEx(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#useFolders()
     */
    public boolean useFolders() {
        return false;
    }

}