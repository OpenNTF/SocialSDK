package com.ibm.xsp.extlib.sbt.files.proxy.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.conn.BasicManagedEntity;
import org.apache.http.message.BasicHttpResponse;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.service.basic.ProxyEndpointService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.ClientService.HandlerInputStream;
import com.ibm.sbt.services.client.connections.ConnectionsService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.extlib.sbt.files.type.AbstractType;
import com.ibm.xsp.util.ManagedBeanUtil;

public class ConnectionsFilesProxyEndPointService extends ProxyEndpointService{
	public static final String TYPE    = "connections";

	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse servletResponse)
			throws ServletException, IOException {                                                                                                                
	         String endpointName = request.getParameter(AbstractType.PARAM_ENDPOINT_NAME);                                                       
	         if (StringUtil.isEmpty(endpointName)) {                                                                                
	             endpointName = TYPE;                                                                                               
	         }                                                                                                                      
	         Endpoint bean = (Endpoint) ManagedBeanUtil.getBean(FacesContext.getCurrentInstance(), endpointName);                   
	                                                                                                                                
	         if (bean == null) {                                                                                                    
	             throw new ServletException("AuthorizationBean not found in application scope");                                    
	         }                                                                                                                      
	                                                                                                                                
	         String fileId = request.getParameter(AbstractType.PARAM_ID);                                                                        
	         String title = request.getPathInfo();                                                                                  
	         if (StringUtil.isNotEmpty(title)) {                                                                                    
	             String[] split = title.split("/");                                                                                 
	             if (split != null && split.length > 0) {                                                                           
	                 title = split[split.length - 1];                                                                               
	             }                                                                                                                  
	         }                                                                                                                      
	                                                                                                                                
	         String repositoryId = request.getParameter(AbstractType.PARAM_REPOSITORY_ID);                                                       
	         // https://server/connections/files/basic/anonymous/api/library/repID/document/fileID/media/file.ext                   
	         title = URLEncoder.encode(title, "UTF-8");                                                                             
	         String serviceUrl = "files/basic/anonymous/api/library/" + repositoryId + "/document/" + fileId + "/media/" + title;   
	         ConnectionsService svc = new ConnectionsService(bean);                                                                 
	         HandlerInputStream inputStream= new HandlerInputStream();                                                              
	         Object httpResp = null;                                                                                                
	                                                                                                                                
	         try {                                                                                                                  
	         	httpResp = svc.get(serviceUrl,inputStream);                                                                        
	                                                                                                                                
	         } catch (ClientServicesException e) {                                                                                  
	             throw new FacesExceptionEx(e, "Failed to perform proxy request");                                                  
	         }                                                                                                                      
	                                                                                                                                
//	           servletResponse.setContentType(((BasicHttpResponse)httpResp).getEntity().getContentType().getValue());                                    
//	           // TODO - what is this?                                                                                              
//	           String status = ((BasicHttpResponse)httpResp).getStatusLine().toString();                                                                 
//	           status = status.substring(status.indexOf(" ") + 1, status.lastIndexOf(" "));                                         
//	           try {                                                                                                                
//	               Integer statusInt = Integer.parseInt(status);                                                                    
//	               servletResponse.setStatus(statusInt);                                                                            
//	           } catch (NumberFormatException nfe) {                                                                                
//	           }                                                                                                                    
//	                                      	
	           ServletOutputStream servletOut = servletResponse.getOutputStream();                                                    
	           BufferedReader rd = new BufferedReader(new InputStreamReader((InputStream)httpResp));                                  
	                                                                       
	           if((InputStream)httpResp!=null){
	           try
	               {
	        	   servletResponse.setContentType("text/plain");
	        	   servletOut=servletResponse.getOutputStream(); 
	               int c;
	               while((c=((InputStream)httpResp).read())!=-1)
	                   {
	            	   servletOut.write((char)Character.toUpperCase(c));
	                   }
	               }
	           catch(IOException err)
	               {
	               //ignore
	               }
	           finally
	               {
	               if(servletOut!=null) servletOut.flush();
	               if(servletOut!=null) servletOut.close();
	               }
	           }
	           //entity.writeTo(new InputStreamReader((InputStream)httpResp));                                                                                          
	           //servletOut.close();                                                                                                    
	     }


}
