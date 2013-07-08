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
package com.ibm.xsp.extlib.sbt.services.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;


/**
 * DropboxFiles service.
 * @author Philippe Riand
 * @author doconnor
 */
public class DropboxService extends ClientService {
    private String mimeForUpload;
    
    public DropboxService(String endpoint) {
        super(endpoint);
    }
    
    //, DropboxFiles.SVC_URL_1
    public DropboxService(Endpoint endpoint) {
        super(endpoint);
    }  

    
    /**
     * @return the mimeForUpload
     */
    public String getMimeForUpload() {
        return mimeForUpload;
    }
    
    
    //TODO- Padraic
    /* (non-Javadoc)
     * @see com.ibm.xsp.extlib.sbt.services.client.ClientService#findUrl(com.ibm.xsp.extlib.sbt.services.client.ClientService.Options)
     */
//    @Override
//    protected String findUrl(Options options) {
//        if(options != null && options.getParameters() != null){
//            if(StringUtil.equals("post", options.getMethod()) && options.getContent() instanceof File){
//                //file upload
//                return ExtLibUtil.concatPath("https://api-content.dropbox.com", getServiceUrl(), '/');
//            }
//        }
//        return super.findUrl(options);
//    }  

@Override
protected String composeRequestUrl(Args args)
		throws ClientServicesException {
	String url=super.composeRequestUrl(args);
	if(url.contains("files_put")){
		url=url.replace("https://api.dropbox.com", "https://api-content.dropbox.com");
	}
	return url;
}
    /**
     * @param mimeForUpload the mimeForUpload to set
     */
    public void setMimeForUpload(String mimeForUpload) {
        this.mimeForUpload = mimeForUpload;
    }
    
    
    
    
    
}
