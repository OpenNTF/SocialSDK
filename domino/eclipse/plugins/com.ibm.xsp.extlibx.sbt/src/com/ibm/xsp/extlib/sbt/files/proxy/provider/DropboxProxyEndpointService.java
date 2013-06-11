/**
 * 
 */
package com.ibm.xsp.extlib.sbt.files.proxy.provider;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHttpResponse;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.service.basic.ProxyEndpointService;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.AbstractEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.extlib.sbt.files.type.AbstractType;
import com.ibm.xsp.extlib.sbt.services.client.DropboxService;
import com.ibm.xsp.extlib.sbt.services.client.endpoints.DropboxEndpoint;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.util.URLEncoding;

/**
 * @author doconnor
 *
 */
public class DropboxProxyEndpointService extends ProxyEndpointService {
	public static final String TYPE    = "dropbox";

	@Override
	public void service(HttpServletRequest request, HttpServletResponse servletResponse)
			throws ServletException, IOException {
	       //TODO make use of findUrl functionality here instead of cloning the EndpointBean
        try {
            String endpointName = request.getParameter(AbstractType.PARAM_ENDPOINT_NAME);
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
            DropboxService svc = new DropboxService(buildHref(path, bean));
            BasicHttpResponse httpResp = null;
            Args args = new Args();
            try {
                httpResp = (BasicHttpResponse) svc.get(args);
            } catch (ClientServicesException e) {
                throw new FacesExceptionEx(e, "Failed to execute proxy request");
            }

            servletResponse.setContentType(request.getParameter("mimeType"));

            String status = httpResp.getStatusLine().toString();
            status = status.substring(status.indexOf(" ") + 1, status.lastIndexOf(" "));
            try {
                Integer statusInt = Integer.parseInt(status);
                servletResponse.setStatus(statusInt);
            } catch (NumberFormatException nfe) {
            }

            HttpEntity entity = httpResp.getEntity();
            ServletOutputStream servletOut = servletResponse.getOutputStream();
            entity.writeTo(servletOut);
            servletOut.close();
        } catch (Exception e) {
            throw new ServletException(e);
        }
	}
	
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
    
    public String getDropBoxApiVersion(Endpoint ep){
        if(StringUtil.equals(DropboxEndpoint.DEFAULT_API_VERSION, ((DropboxEndpoint)ep).getApiVersion())){
            return "1";//latestAPI;
        }
        return ((DropboxEndpoint)ep).getApiVersion();
        
    }
	

}
