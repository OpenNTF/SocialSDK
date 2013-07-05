package com.ibm.xsp.extlib.sbt.files.proxy;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.IServletFactory;
import com.ibm.designer.runtime.domino.adapter.ServletMatch;

public class ExtLibxServletFactory implements IServletFactory {
    
    private static final String SERVLET_PROXY_NAME     = "XPages ExtLib Proxy Servlet"; // $NON-NLS-1$

    private ComponentModule module;
    
    public void init(ComponentModule module) {
        this.module = module;
    }

//    public ServletMatch getServletMatch(String contextPath, String path) throws ServletException {
//        if( path.startsWith("/xsp/.proxy/") ) { // $NON-NLS-1$
//            int len = "/xsp/.proxy".length(); // $NON-NLS-1$
//            String servletPath = path.substring(0,len);
//            String pathInfo = path.substring(len);
//            return new ServletMatch(getProxyServlet(),servletPath,pathInfo);
//        }
//        
//        return null;
//    }
//
//    public Servlet getProxyServlet() throws ServletException {
//        if(proxyServlet==null) {
//            synchronized (this) {
//                if(proxyServlet==null) {
//                    proxyServlet = (ServiceServlet)module.createServlet(new ServiceServlet(),"",null);
//                }
//            }
//        }
//        return proxyServlet;
//    }   
    public ServletMatch getServletMatch(String contextPath, String path)
    		throws ServletException {
    	// TODO Auto-generated method stub
    	return null;
    }
}