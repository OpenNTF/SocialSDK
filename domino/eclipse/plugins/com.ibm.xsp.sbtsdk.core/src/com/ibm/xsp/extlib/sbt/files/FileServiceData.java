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
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.DataModel;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.component.UIFileuploadEx.UploadedFile;
import com.ibm.xsp.extlib.model.DataAccessor;
import com.ibm.xsp.extlib.model.DataAccessorModel;
import com.ibm.xsp.extlib.model.DataAccessorSource;
import com.ibm.xsp.extlib.sbt.model.RestDataBlockAccessor;
import com.ibm.xsp.extlib.sbt.model.RestObjectDataSource;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.http.IUploadedFile;
import com.ibm.xsp.model.DataContainer;
import com.ibm.xsp.util.FacesUtil;

/**
 * File service data source.
 * @author Philippe Riand
 * @author Justin Murray
 */
public class FileServiceData extends RestObjectDataSource {

    @Override
    public DataModel getDataModel() {
        return new FileServiceDataModel(this, (Container) getDataContainer());
    }

    private class FileServiceDataModel extends DataAccessorModel {

        public FileServiceDataModel(DataAccessorSource source, Container container) {
            super(source, container);
        }

        @Override
        public String getRowId() {
            Container cont = getDataContainer();
            Object rowInfo = cont.getDataAccessor().get(getRowIndex());
            if (rowInfo instanceof FileEntry) {
                FileEntry fEntry = (FileEntry) rowInfo;
                String uniqueId = fEntry.getUniqueId();
                if (StringUtil.isNotEmpty(uniqueId)) {
                    return uniqueId;
                }
            }
            return super.getRowId();
        }
    }

    public static class FileServiceAccessor extends RestDataBlockAccessor {

        private static final long serialVersionUID = 1L;

        private Object      uploadedFile;

        public FileServiceAccessor() {
        }

        public FileServiceAccessor(FileServiceData ds) {
            super(ds);
        }

        @Override
        protected Block loadBlock(int index, int blockSize) {
            String urlForLog = null;
            String fileServiceName = null;
            try {
                Endpoint provider = findEndpointBean();
                IFileType fileService = findService();
                if (fileService == null) {
                    throw new FacesExceptionEx(null, "The file service type is not assigned (null)");
                }

                // Compose the URL to the service
                // (delegate to the service type)
                Map<String, String> params = getParameters(fileService, index, blockSize); 

                ClientService svc;
                String serviceUrl = getServiceUrl();
                if(StringUtil.isEmpty(serviceUrl)){
                    //TODO we need to tidy this up - the fileServiceData should not have a serviceUrl attribute - that should only be associated with the FileType
                    serviceUrl = fileService.getServiceUrl();
                }
                urlForLog = serviceUrl;
                fileServiceName = fileService.getType();
                // Check if rootFolder param is being used
                if (getRootFolder() == null || getRootFolder().length() < 1) {
                    // Create the service and and read the file entries
                    if(StringUtil.isNotEmpty(serviceUrl)){
                        serviceUrl = ExtLibUtil.concatPath(serviceUrl, getCurrentDirectory(), '/');
                    }
                    svc = createService(provider, fileService, serviceUrl);
                }
                else {
                    if (getRootFolder().charAt(0) != '/') {
                        setRootFolder("/" + getRootFolder());
                    }
                    String root = getRootFolder();
                    String curr = getCurrentDirectory();
                    String path = "";
                    if (curr.length() > root.length()) {
                        path = curr.substring(root.length(), curr.length());
                    }
                    // Create the service and and read the file entries
                    svc = createService(provider, fileService, serviceUrl + root + path);
                }
                //TODO - Padraic 

//                urlForLog = svc.getUrl();
                
                urlForLog = serviceUrl;
                List<FileEntry> entries = fileService.readFileEntries(svc, this, params);

                FileEntry[] data = entries.toArray(new FileEntry[entries.size()]);
                return new ArrayBlock(index, data);
            } catch (Exception ex) {
                throw new FacesExceptionEx(ex, "Error while calling the file service: {0}. Failed to access URL: {1}", fileServiceName, urlForLog);
            }
        }

        protected IFileType findService() {
            FileServiceData ds = (FileServiceData) getDataSource();
            return ds.getServiceType();
        }

        // ---------------
        public String getCurrentDirectory() {
            FileServiceData ds = (FileServiceData) getDataSource();
            return ds.getCurrentDirectory();
        }

        public boolean isRootDirectory() {
            FileServiceData ds = (FileServiceData) getDataSource();
            return ds.isRootDirectory();
        }

        public String getParentDirectory() {
            FileServiceData ds = (FileServiceData) getDataSource();
            return ds.getParentDirectory();
        }

        public String getRootFolder() {
            FileServiceData ds = (FileServiceData) getDataSource();
            return ds.getRootFolder();
        }

        public void setRootFolder(String root) {
            FileServiceData ds = (FileServiceData) getDataSource();
            ds.setRootFolder(root);
        }

        public void setCurrentDirectory(String newDirectory) {
            FileServiceData ds = (FileServiceData) getDataSource();
            ds.setCurrentDirectory(newDirectory);
        }

        public void refresh() {
            FileServiceData ds = (FileServiceData) getDataSource();
            ds.refresh();
        }

        // ---------------

        protected Map<String, String> getParameters(IFileType service, int index, int blockSize) throws ClientServicesException {
            Map<String, String> map = new HashMap<String, String>();
            map.putAll(getUrlParameters());
            service.addUrlParameters(map, index, blockSize);
            return map;
        }

        protected ClientService createService(Endpoint endpoint, IFileType fileService, String url) throws ClientServicesException {
            ClientService svc = fileService.createClientService(endpoint, url);
            return svc;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.ibm.xsp.extlib.model.DataAccessor#deleteRow(java.lang.String)
         */
        @Override
        public void deleteRow(String rowId) {
            Endpoint authBean = findEndpointBean();
            IFileType ift = (IFileType) findService();
            ift.deleteRow(this, authBean, rowId);
        }

        public void createFolder(String folderName) throws UnsupportedEncodingException, IOException {
            Endpoint authBean = findEndpointBean();
            IFileType ift = (IFileType) findService();
            ift.createFolder(authBean, folderName);
        }

        public boolean useFolders() {
            IFileType ift = (IFileType) findService();
            return ift.useFolders();
        }

        /**
         * @return the uploadedFile
         */
        public Object getUploadedFile() {
            return uploadedFile;
        }

        /**
         * @param uploadedFileObj the uploadedFile to set
         * @throws CloneNotSupportedException 
         */
        public void setUploadedFile(Object uploadedFileObj) throws CloneNotSupportedException {
            if (uploadedFileObj instanceof UploadedFile) {
                UploadedFile uploadedFile = (UploadedFile) uploadedFileObj;
                this.uploadedFile = uploadedFile;
            }
        }
    }

    private IFileType serviceType;
    private String    rootFolder;
    private String    currentDirectory = "/";

    public FileServiceData() {
    }
    
    @Override
    public String getDefaultEndpoint() {
        IFileType type = getServiceType();
        return type!=null ? type.getDefaultEndpoint() : null;
    }

    public String getRootFolder() {
        if (null != rootFolder) {
            return rootFolder;
        }
        ValueBinding valueBinding = getValueBinding("rootFolder");
        if (valueBinding != null) {
            String value = (String) valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    @Override
    protected RestDataBlockAccessor createAccessor() {
        return new FileServiceAccessor(this);
    }

    public IFileType getServiceType() {
        return this.serviceType;
    }

    public String getCurrentDirectory() {
        if (null != currentDirectory) {
            return currentDirectory;
        }
        ValueBinding valueBinding = getValueBinding("currentDirectory");
        if (valueBinding != null) {
            String value = (String) valueBinding.getValue(getFacesContext());
            return value;
        }
        return "/";
    }

    public boolean isRootDirectory() {
        // return false;
        boolean result = getCurrentDirectory().equals("/") || getCurrentDirectory().equals(getRootFolder());
        return result;
    }

    public void setServiceType(IFileType serviceType) {
        this.serviceType = serviceType;
    }

    public void setCurrentDirectory(String newDirectory) {
        this.currentDirectory = newDirectory;
    }

    public String getParentDirectory() {
        String parentDir = this.currentDirectory;

        if (parentDir != null) {
            for (int pos = parentDir.length() - 1; pos >= 0; pos--) {
                if (parentDir.charAt(pos) == '/') {
                    parentDir = parentDir.substring(0, pos);
                    if (parentDir.equals(""))
                        parentDir = "/";
                    return parentDir;
                }
            }
        }
        return "/";
    }

    @Override
    public void restoreState(FacesContext _context, Object _state) {
        Object _values[] = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        this.serviceType = (IFileType) FacesUtil.objectFromSerializable(_context, getComponent(), _values[1]);
        this.currentDirectory = (String) _values[2];
        this.rootFolder = (String) _values[3];
    }

    @Override
    public Object saveState(FacesContext _context) {
        Object _values[] = new Object[4];
        _values[0] = super.saveState(_context);
        _values[1] = FacesUtil.objectToSerializable(_context, serviceType);
        _values[2] = this.currentDirectory;
        _values[3] = this.rootFolder;
        return _values;
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.model.AbstractDataSource#save(javax.faces.context.FacesContext, boolean)
     */
    @Override
    public boolean save(FacesContext context, boolean force) throws FacesExceptionEx {
        DataContainer container = getDataContainer();
        if(container instanceof Container){
            DataAccessor accessor = ((Container)container).getDataAccessor();
            if(accessor instanceof FileServiceAccessor){
                UploadedFile uploadedFile = (UploadedFile)((FileServiceAccessor)accessor).getUploadedFile();
                if(uploadedFile != null){
                    IUploadedFile file = uploadedFile.getUploadedFile();
                    HashMap<String, String> params = new HashMap<String, String>();
                    String filename = uploadedFile.getFilename();
                    params.put("file", filename);

                    if (null != file) {
                        File serverFile = file.getServerFile();
                        if (null != serverFile) {
                            Endpoint endpoint = ((FileServiceAccessor)accessor).findEndpointBean();
                            IFileType ift = (IFileType) ((FileServiceAccessor)accessor).findService();
                            try {
                                ift.uploadFile(endpoint, serverFile, this, params);
                                refresh();
                            } catch (CloneNotSupportedException e) {
                                throw new FacesExceptionEx(e, "Failed to upload file to {0}", endpoint.getLabel());
                            }
                        }
                    }
                }
            }
        }
        return super.save(context, force);
    }
    
}
