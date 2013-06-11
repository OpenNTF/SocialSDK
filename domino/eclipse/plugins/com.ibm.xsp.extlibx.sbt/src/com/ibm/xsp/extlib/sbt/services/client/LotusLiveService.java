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
package com.ibm.xsp.extlib.sbt.services.client;


import java.io.File;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.FileEntity;

import sun.net.dns.ResolverConfiguration.Options;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.xsp.extlib.beans.UserBean;
import com.ibm.xsp.extlib.log.ExtlibCoreLogger;
import com.ibm.xsp.extlib.sbt.files.type.LotusLiveFiles;


/**
 * LotusLiveFiles service.
 * @author Philippe Riand
 * @author doconnor
 */
public class LotusLiveService extends ClientService {
    private String mimeForUpload;
    public LotusLiveService(Endpoint endpoint) {
        super(endpoint);
    }    
    
    
    @Override
    protected void addUrlParts(StringBuilder b, Args args)
    		throws ClientServicesException {
    	// TODO Auto-generated method stub
    	
        Map<String,String> parameters = args.getParameters();
        /* 
         * /files/basic/cmis/repository/{repositoryId}/folderc/snx:files
         */
        if(parameters!=null) {
            String subscriberId = "20108978"; /*(String)UserBean.get().getPerson().getField(LotusLiveFiles.LOTUS_LIVE_SUBSCRIBER_ID); */
            b.append("/p!"); 
            if(StringUtil.isEmpty(subscriberId)){
                if(ExtlibCoreLogger.SBT.isErrorEnabled()){
                    ExtlibCoreLogger.SBT.errorp(this, "addUrlParts", "LotusLive subscriber ID is null. Repository IDs will not be resolved. Ensure that the \"extlib.people.provider\" property has been set in the application's xsp.properties (e.g. \nextlib.people.provider=lotuslive\nor some variation of this must be set in xsp.properties)");
                }
            }
            b.append(subscriberId);
            b.append("/folderc/snx:files");
        /*  b.append("!");
            b.append(subscriberId); Removed since LotusLive upgraded to 'Connections API' 12/15/11
         */
        }
    	super.addUrlParts(b, args);
    }
    
    
    /**
     * @return the mimeForUpload
     */
    public String getMimeForUpload() {
        return mimeForUpload;
    }

    @Override
    protected boolean isValidUrlParameter(Args args, String name)
    		throws ClientServicesException {
    	// TODO Auto-generated method stub
    	if(name.equals("subscriberId")) {
            return false;
        }
    	return super.isValidUrlParameter(args, name);
    }
    
    
    @Override
    protected void prepareRequest(HttpClient httpClient,HttpRequestBase httpRequestBase, Args args, Content content)
    		throws ClientServicesException {
    	
    	Object contents = args.getHandler();
        if(contents instanceof File){
            String name = args.getParameters().get("file");
            FileEntity fileEnt = new FileEntity((File) contents, getMimeForUpload());
            //fileEnt.setContentEncoding(FORMAT_BINARY);
            httpRequestBase.setHeader("slug", name);
            httpRequestBase.setHeader("Content-type", getMimeForUpload());
            if (fileEnt != null && (httpRequestBase instanceof HttpEntityEnclosingRequestBase)) {
                ((HttpEntityEnclosingRequestBase) httpRequestBase).setEntity(fileEnt);
            }
        }
    	// TODO Auto-generated method stub
    	super.prepareRequest(httpClient, httpRequestBase, args, content);
    }

    
    /**
     * @param mimeForUpload the mimeForUpload to set
     */
    public void setMimeForUpload(String mimeForUpload) {
        if(StringUtil.isEmpty(mimeForUpload)){
            this.mimeForUpload = "application/octet-stream";
            return;
        }
        this.mimeForUpload = mimeForUpload;
    }
}
