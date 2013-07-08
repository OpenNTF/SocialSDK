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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicHttpResponse;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.designer.runtime.util.MIME;
import com.ibm.jscript.std.ObjectObject;
import com.ibm.sbt.service.core.handlers.FileHandler;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientService.HandlerInputStream;
import com.ibm.sbt.services.client.ClientService.HandlerJson;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.AbstractEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.util.DataNavigator;
import com.ibm.sbt.util.JsonNavigator;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.extlib.sbt.files.FileEntry;
import com.ibm.xsp.extlib.sbt.files.FileServiceData;
import com.ibm.xsp.extlib.sbt.files.FileServiceData.FileServiceAccessor;
import com.ibm.xsp.extlib.sbt.model.RestDataBlockAccessor;
import com.ibm.xsp.extlib.sbt.services.client.DropboxService;
import com.ibm.xsp.extlib.sbt.services.client.endpoints.DropboxEndpoint;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.util.URLEncoding;

/**
 * DropboxFiles File type.
 * @author Philippe Riand
 * @author Justin Murray
 * @author Daneel Reif
 * @author doconnor
 */
public class DropboxFiles extends AbstractType {
    public static final String SVC_URL_1 = "1/metadata/dropbox";
    public static final String TYPE    = "dropbox";
    public static final String VERSION_0 = "0";
    public static final String VERSION_1 = "1";
    private String latestAPI = VERSION_1;

    /**
     * Returns a url that allows the caller to download a file
     * https://api-content.dropbox.com/<VERSION>/files/dropbox
     * @param path
     *          path to the file to be downloaded
     * @return
     */
    public String buildHref(String path, Endpoint bean) {
        try {
            // Encode path URL for spaces & special characters
            path = URLEncoding.encodeURIString(path, null, 0, false);
        } catch (IOException e) {
            throw new FacesExceptionEx(e, "Failed to encode URI string: {0}", path);
        }
        String href = ExtLibUtil.concatPath(getDropBoxApiVersion(bean), "files/dropbox", '/');
        href = ExtLibUtil.concatPath(href, path, '/');
        return href;
    }

    public DropboxFiles() {
    }

    public void addUrlParameters(Map<String, String> map, int index, int blockSize) throws ClientServicesException {

    }

    public ClientService createClientService(Endpoint endpoint, String url) throws ClientServicesException {
        if(StringUtil.isEmpty(url)){
            url = SVC_URL_1;
        }
        DropboxService svc = new DropboxService(endpoint);
        return svc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ibm.xsp.extlib.sbt.files.IFileType#createFolder(com.ibm.xsp.extlib.security.authorization.AuthorizationBean,
     * java.lang.String)
     */
    public void createFolder(Endpoint endpoint, String folderName) {
        //https://api.dropbox.com/<version>/fileops/create_folder
        try {
            DropboxService svc = (DropboxService)createClientService(endpoint, ExtLibUtil.concatPath(getDropBoxApiVersion(endpoint), "fileops/create_folder", '/'));
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("root", "dropbox");
            params.put("path", folderName);

            svc.post(getDropBoxApiVersion(endpoint)+"/fileops/create_folder", params,null);
            
        } catch (ClientServicesException e) {
            throw new FacesExceptionEx(e, "Failed to create folder named \'{0}\'", folderName);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#deleteRow(com.ibm.xsp.extlib.sbt.files.FileServiceData.Accessor,
     * com.ibm.xsp.extlib.security.authorization.AuthorizationBean, java.lang.String)
     */
    public void deleteRow(FileServiceAccessor accessor, Endpoint authBean, String rowId) {
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("root", "dropbox");
            rowId = URLDecoder.decode(rowId, "UTF-8");
            params.put("path", rowId);
            ClientService svc = createClientService(authBean, ExtLibUtil.concatPath(getDropBoxApiVersion(authBean), "fileops/delete", '/'));

            svc.post(getDropBoxApiVersion(authBean)+"/fileops/delete", params,null);

        } catch (ClientServicesException e) {
            throw new FacesExceptionEx(e, "Failed to delete file with ID \'{0}\'", rowId);
        } catch (UnsupportedEncodingException e) {
            throw new FacesExceptionEx(e, "Failed to delete file with ID \'{0}\'", rowId);
        }

    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#getType()
     */
    public String getType() {
        return TYPE;
    }

    public List<FileEntry> readFileEntries(ClientService svc, RestDataBlockAccessor accessor, Map<String, String> params) throws ClientServicesException {
        
    	HandlerJson json= new HandlerJson();  	
    	Object jsonObject = (Object) svc.get(getServiceUrl(),params, json);
        ArrayList<FileEntry> entries = new ArrayList<FileEntry>();
        String epName = accessor.findEndpointName();
        JsonNavigator navigator = new JsonNavigator(jsonObject);
        DataNavigator contentsNav = navigator.get("contents");
        for (int i = 0; i < contentsNav.getCount(); ++i) {
            FileEntry entry = new FileEntry();
            DataNavigator nav = contentsNav.get(i);
            try {
                String title = nav.stringValue("path"); 
                String[] result = title.split("/");
                entry.setTitle(result[result.length - 1]);
                entry.setUpdated(nav.dateValue("modified"));
                entry.setBytes(nav.intValue("bytes"));
                entry.setSize(nav.stringValue("size"));
                entry.setAuthorName(params.get("subscriberId"));
                entry.setVersion(String.valueOf( nav.doubleValue("revision")));
                boolean isDir = nav.booleanValue("is_dir");
                entry.setIsDirectory(isDir);
                if(!isDir){
                    entry.setMimeType(nav.stringValue("mime_type"));
                }
                else{
                    entry.setMimeType("undefined");
                }
                entry.setIcon(MimeIconRegistry.getInstance().get(entry.getMimeType()));
                entry.setPath(nav.stringValue("path"));
                String proxyUrl = "xsp/.proxy/files/" + title + "?" + PARAM_TYPE + "=" + TYPE + "&path=" + entry.getPath() + "&mimeType=" + entry.getMimeType() + "&" + PARAM_ENDPOINT_NAME + "=";
                if(StringUtil.isNotEmpty(epName)){
                    proxyUrl = proxyUrl + epName;
                }
                else{
                    proxyUrl = proxyUrl + TYPE;
                }
                entry.setProxyURL(proxyUrl);
                entry.setUniqueId(entry.getPath());
            } catch (Exception e) {
                throw new FacesExceptionEx(e);
            }
            entries.add(entry);
        }
        return entries;
    }

    @Override
    public void serviceProxy(HttpServletRequest request, HttpServletResponse servletResponse) throws ServletException, IOException {
        //TODO make use of findUrl functionality here instead of cloning the EndpointBean
        try {
            String endpointName = request.getParameter(PARAM_ENDPOINT_NAME);
            if(StringUtil.isEmpty(endpointName)){
                endpointName = TYPE;
            }
            Endpoint bean = EndpointFactory.getEndpoint(endpointName); 

            if (bean == null) {
                throw new ServletException("AuthorizationBean not found in application scope");
            }

            //TODO - padraic review with phil what clone is about
            AbstractEndpoint clonedBean = (AbstractEndpoint) bean;
            clonedBean.setUrl("https://api-content.dropbox.com/");

            String path = request.getParameter("path");

            // DropboxFiles Service - https://api-content.dropbox.com/<version>/files/dropbox/<path>
            DropboxService svc = new DropboxService(endpointName);
            String href=buildHref(path,bean);
            HandlerInputStream handler = new HandlerInputStream();
            Object file;
            try {
                file =  svc.get(href,handler);
            } catch (ClientServicesException e) {
                throw new FacesExceptionEx(e, "Failed to execute proxy request");
            }
            
            servletResponse.setContentType(request.getParameter("mimeType"));
            
            String nameUTF8 = URLEncoder.encode(request.getParameter("path"),"utf-8");//$NON-NLS-1$
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
     * @see com.ibm.xsp.extlib.sbt.files.IFileType#uploadFile(com.ibm.xsp.extlib.services.client.Endpoint, java.io.File,
     * java.util.HashMap)
     */
    public void uploadFile(Endpoint authBean, File serverFile, FileServiceData dataSource, HashMap<String, String> params) throws CloneNotSupportedException {
        String name = params.get("file");
        if(StringUtil.isEmpty(name)){
            throw new FacesExceptionEx(new NullPointerException(), "Name of file being uploaded may not be null");
        }
        
        int dot = name.lastIndexOf('.');
        String ext = null;
        if(dot > -1){
            ext = name.substring(dot + 1); //add one for the dot!
        }
        if(StringUtil.isEmpty(ext)){
            throw new FacesExceptionEx(new NullPointerException(), "Extension of file being uploaded may not be null");
        }
        try {
            final String uploadUrl = "/files_put/dropbox/";
            String path = dataSource.getCurrentDirectory();
            if(StringUtil.isNotEmpty(path)){
                name = ExtLibUtil.concatPath(path, name, '/');
            }
            name = ExtLibUtil.concatPath(uploadUrl, name, '/');
            String uploadURL = getDropBoxApiVersion(authBean) + name;
            DropboxService svc = (DropboxService)createClientService(authBean, uploadURL);
            svc.setMimeForUpload(MIME.getMIMETypeFromExtension(ext));            
            
            svc.post(uploadURL, params, null, serverFile,null);
            
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
        return true;
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.extlib.sbt.files.type.AbstractType#getEndpoint()
     */
    @Override
    public String getDefaultEndpoint() {
        return EndpointFactory.getEndpointName(EndpointFactory.SERVER_DROPBOX);
    }
    
    public String getDropBoxApiVersion(Endpoint ep){
        if(StringUtil.equals(DropboxEndpoint.DEFAULT_API_VERSION, ((DropboxEndpoint)ep).getApiVersion())){
            return latestAPI;
        }
        return ((DropboxEndpoint)ep).getApiVersion();
        
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.extlib.sbt.files.type.AbstractType#getServiceUrl(com.ibm.xsp.extlib.sbt.files.FileServiceData.FileServiceAccessor)
     */
    @Override
    public String getServiceUrl() {
        if(accessor != null){
            Endpoint ep = accessor.findEndpointBean();
            String v = getDropBoxApiVersion(ep);
            String[] split = SVC_URL_1.split("/");
            StringBuffer buffer = new StringBuffer(v + "/");
            for(int i = 1; i < split.length; i++){
                buffer.append(split[i] + "/");
            }
            return buffer.toString();

        }
        return SVC_URL_1;
    }
    
    private FileServiceAccessor accessor;
    
    public void initAccessor(FileServiceAccessor accessor){
        this.accessor = accessor;
    }
}
