/*
 * © Copyright IBM Corp. 2011
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.designer.runtime.util.MIME;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.HandlerInputStream;
import com.ibm.sbt.services.client.ClientService.HandlerXml;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.ConnectionsService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.util.DataNavigator;
import com.ibm.sbt.util.XmlNavigator;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.extlib.sbt.files.FileEntry;
import com.ibm.xsp.extlib.sbt.files.FileServiceData;
import com.ibm.xsp.extlib.sbt.files.FileServiceData.FileServiceAccessor;
import com.ibm.xsp.extlib.sbt.model.RestDataBlockAccessor;
import com.ibm.xsp.util.ManagedBeanUtil;

/**
 * @author doconnor
 *
 */
public class ConnectionsFiles extends AbstractType {
    
    private static final String ATTR_HREF         = "@href";
    private static final String ATTR_VAL_FILES    = "Files";
    private static final String ATTR_VAL_MY_FILES = "My Files";
    private static final String TAG_AUTHOR        = "author";
    private static final String TAG_COLLECTION    = "collection";
    private static final String TAG_ENTRY         = "entry";
    private static final String TAG_FEED          = "feed";
    private static final String TAG_ID            = "id";
    private static final String TAG_LABEL         = "label";
    private static final String TAG_MODIFIED      = "modified";
    private static final String TAG_NAME          = "name";
    private static final String TAG_PUBLISHED     = "published";
    private static final String TAG_SERVICE       = "service";
    private static final String TAG_SUMMARY       = "summary";
    private static final String TAG_TITLE         = "title";
    private static final String TAG_UUID          = "uuid";
    private static final String TAG_VERSION       = "versionLabel";
    private static final String TAG_VISIBILITY    = "visibility";
    private static final String TAG_WORKSPACE     = "workspace";
    public static final String  TYPE              = "connections";

    /**
     * 
     */
    public ConnectionsFiles() {
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#addUrlParameters(java.util.Map, int, int)
     */
    public void addUrlParameters(Map<String, String> map, int index, int blockSize) throws ClientServicesException {
        map.put("page", String.valueOf(index));
        map.put("pageSize", String.valueOf(blockSize));
        map.put("sortBy", "title");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#createClientService(com.ibm.xsp.extlib.sbt.services.client.Endpoint,
     * java.lang.String)
     */
    public ClientService createClientService(Endpoint endpoint, String url) throws ClientServicesException {
        if(StringUtil.isEmpty(url)){
            url = "files/basic/api/introspection";
        }
        return new ConnectionsService(endpoint);
        //TODO - Padraic
//        return new ConnectionsService(endpoint, url);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#createFolder(com.ibm.xsp.extlib.sbt.services.client.Endpoint,
     * java.lang.String)
     */
    public void createFolder(Endpoint authBean, String folderName) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#deleteRow(com.ibm.xsp.extlib.sbt.files.FileServiceData.Accessor,
     * com.ibm.xsp.extlib.sbt.services.client.Endpoint, java.lang.String)
     */
    public void deleteRow(FileServiceAccessor accessor, Endpoint endpoint, String rowId) {
        // /basic/api/myuserlibrary/document/{document-id}/entry
        int rows = accessor.getCount();
        String rep = null;
        for (int i = 0; i < rows; i++) {
            FileEntry entry = (FileEntry) accessor.get(i);
            if (StringUtil.equals(rowId, entry.getUniqueId())) {
                rep = entry.getRepository();
                break;
            }
        }
        if (StringUtil.isEmpty(rep)) {
            return;
        }
        
        String deleteURL = "files/basic/api/library/" + rep + "/document/" + rowId + "/entry";
        try {
            ClientService svc = createClientService(endpoint, deleteURL);
            svc.delete(deleteURL);
        } catch (ClientServicesException e) {
            // TODO -- revisit this... Connections is thowing an exception here even though the operation worked OK
            // throw new FacesExceptionEx(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.type.AbstractType#getEndpoint()
     */
    @Override
    public String getDefaultEndpoint() {
        return EndpointFactory.getEndpointName(EndpointFactory.SERVER_CONNECTIONS);
    }

    private String getMimeType(String title) {
        if (title != null) {
            String ext = null;
            int dot = title.lastIndexOf('.');
            if (dot > -1) {
                ext = title.substring(dot + 1); // add one for the dot!
            }
            if (StringUtil.isNotEmpty(ext)) {
                return MIME.getMIMETypeFromExtension(ext);
            }
        }
        return "application/octet-stream";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#getType()
     */
    public String getType() {
        return TYPE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#readFileEntries(com.ibm.xsp.extlib.sbt.services.client.Service,
     * java.util.Map)
     */
    public List<FileEntry> readFileEntries(ClientService svc, RestDataBlockAccessor accessor, Map<String, String> params)
            throws ClientServicesException {
        ArrayList<FileEntry> fileEntries = new ArrayList<FileEntry>();
        if (svc != null) {
            
        	HandlerXml xml = new HandlerXml();
        	Object result = svc.get("/files/basic/api/introspection",params, xml);
            
            if (result instanceof Document) {
                XmlNavigator workspacesNavigator = new XmlNavigator((Document) result);
                DataNavigator filesNavigator = workspacesNavigator.get(TAG_SERVICE + "/" + TAG_WORKSPACE).selectEq(TAG_TITLE, ATTR_VAL_FILES);
                String href = filesNavigator.get(TAG_COLLECTION).selectEq(TAG_TITLE, ATTR_VAL_MY_FILES).stringValue(ATTR_HREF);
                if (StringUtil.isNotEmpty(href)) {
                    String start = svc.getEndpoint().getUrl();
                    if (href.indexOf(start) > -1) {
                        href = href.substring(href.indexOf(start) + start.length());
                    }
                    ClientService filesService = createClientService(svc.getEndpoint(), href);
                    result = filesService.get(href, ClientService.FORMAT_XML);
                    if (result instanceof Document) {
                        Document entriesDocument = (Document) result;
                        XmlNavigator entriesNavigator = new XmlNavigator(entriesDocument);
                        DataNavigator entries = entriesNavigator.get(TAG_FEED + "/" + TAG_ENTRY);
                        String repId = entriesNavigator.stringValue(TAG_FEED + "/" + TAG_ID);
                        if (StringUtil.isNotEmpty(repId)) {
                            repId = repId.substring(repId.lastIndexOf(':') + 1);
                        }
                        if (entries != null) {
                            String epName = accessor.findEndpointName();
                            for (int i = 0; i < entries.getCount(); i++) {
                                DataNavigator navigator = entries.get(i);
                                FileEntry fe = new FileEntry();
                                fe.setRepository(repId);
                                fe.setTitle(navigator.stringValue(TAG_LABEL));
                                fe.setUpdated(navigator.dateValue(TAG_MODIFIED));
                                fe.setAuthorName(navigator.stringValue(TAG_AUTHOR + "/" + TAG_NAME));
                                fe.setDescription(navigator.stringValue(TAG_SUMMARY));
                                fe.setFileId(navigator.stringValue(TAG_UUID));
                                fe.setUniqueId(fe.getFileId());
                                fe.setMimeType(getMimeType(navigator.stringValue(TAG_LABEL)));
                                fe.setIcon(MimeIconRegistry.getInstance().get(fe.getMimeType()));
                                String proxyUrl = "xsp/.proxy/files/"+accessor.findEndpointName()+"/connections/" + fe.getTitle() + "?" + PARAM_TYPE + "=" + TYPE + "&" + PARAM_ID
                                        + "=" + fe.getUniqueId() + "&" + PARAM_REPOSITORY_ID + "=" + repId + "&"+"fileName="+fe.getTitle()+"&" + PARAM_ENDPOINT_NAME 
                                        + "=";
                                if (StringUtil.isNotEmpty(epName)) {
                                    proxyUrl = proxyUrl + epName;
                                }
                                else {
                                    proxyUrl = proxyUrl + TYPE;
                                }
                                fe.setProxyURL(proxyUrl);

                                fe.setPublished(navigator.dateValue(TAG_PUBLISHED));
                                fe.setVersion(navigator.stringValue(TAG_VERSION));
                                fe.setVisibility(navigator.stringValue(TAG_VISIBILITY));
                                fileEntries.add(fe);
                            }
                        }
                    }
                }
            }
        }
        return fileEntries;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.type.AbstractType#serviceProxy(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void serviceProxy(HttpServletRequest request, HttpServletResponse servletResponse) throws ServletException, IOException {
        try {
            String endpointName = request.getParameter(PARAM_ENDPOINT_NAME);
            if (StringUtil.isEmpty(endpointName)) {
                endpointName = TYPE;
            }
            Endpoint bean = (Endpoint) ManagedBeanUtil.getBean(FacesContext.getCurrentInstance(), endpointName);

            if (bean == null) {
                throw new ServletException("AuthorizationBean not found in application scope");
            }

            String fileId = request.getParameter(PARAM_ID);
            String title = request.getPathInfo();
            if (StringUtil.isNotEmpty(title)) {
                String[] split = title.split("/");
                if (split != null && split.length > 0) {
                    title = split[split.length - 1];
                }
            }

            String repositoryId = request.getParameter(PARAM_REPOSITORY_ID);
            // https://server/connections/files/basic/anonymous/api/library/repID/document/fileID/media/file.ext
            title = URLEncoder.encode(title, "UTF-8");
            String serviceUrl = "files/basic/anonymous/api/library/" + repositoryId + "/document/" + fileId + "/media/" + title;
            ConnectionsService svc = new ConnectionsService(bean);
            HandlerInputStream inputStream= new HandlerInputStream();
            Object file = null;

            try {
            	file = svc.get(serviceUrl,inputStream);

            } catch (ClientServicesException e) {
                throw new FacesExceptionEx(e, "Failed to perform proxy request");
            }

            servletResponse.setContentType(request.getParameter("mimeType"));
            
            String nameUTF8 = URLEncoder.encode(request.getParameter("fileName"),"utf-8");//$NON-NLS-1$
            nameUTF8 = nameUTF8.replaceAll("\\+", "%20");//$NON-NLS-1$ //$NON-NLS-2$
            
            //PEDS954UQU
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
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#uploadFile(com.ibm.xsp.extlib.sbt.services.client.Endpoint,
     * java.io.File, java.util.HashMap)
     */
    public void uploadFile(Endpoint authBean, File serverFile, FileServiceData dataSource, HashMap<String, String> params) throws CloneNotSupportedException {
        String name = params.get("file");
        int dot = name.lastIndexOf('.');
		String ext = null;
		if (dot > -1) {
		    ext = name.substring(dot + 1); // add one for the dot!
		}
		if (StringUtil.isEmpty(ext)) {
		    throw new FacesExceptionEx(new NullPointerException(), "Extension of file being uploaded may not be null");
		}
		//https://greenhouse.lotus.com/files/basic/api/myuserlibrary/feed?file=IBMNotesInstall.log
		String uploadUrl = "files/basic/api/myuserlibrary/feed";
		ConnectionsService svc = new ConnectionsService (authBean);

		params.put("file", name);

		try {
			svc.post(uploadUrl, params, null, serverFile,null);
		}catch (ClientServicesException e) {
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
