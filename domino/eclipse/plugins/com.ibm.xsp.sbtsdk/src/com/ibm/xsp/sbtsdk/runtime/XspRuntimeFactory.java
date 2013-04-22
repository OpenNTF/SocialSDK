/*
 * © Copyright IBM Corp. 2012
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

package com.ibm.xsp.sbtsdk.runtime;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.servlet.RuntimeFactoryServlet;
import com.ibm.commons.util.NotImplementedException;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.application.events.ApplicationListener;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.event.FacesContextListener;



/**
 * Runtime factory implementation for an XPages environment.
 * 
 * We assume that we are running within an XPages application.
 * In this case, the JSf application must be created for the NSFComponentModule.
 * 
 * 
 * 
 * 
 * @author Philippe Riand
 */
public class XspRuntimeFactory extends RuntimeFactoryServlet {
	
	private static XspRuntimeFactory instance = new XspRuntimeFactory();
	public static final RuntimeFactory get() {
		return instance;
	}

	protected ClassLoader getContextClassLoader() {
		// Get it from the faces context, if available
		FacesContextEx ctx = FacesContextEx.getCurrentInstance();
		if(ctx!=null) {
			return ctx.getContextClassLoader();
		}
		// Else, execute this is a privileged block
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
        		return Thread.currentThread().getContextClassLoader();
            }
        });
	}

	public XspApplication getApplicationUnchecked() {
		XspApplication app = (XspApplication)super.getApplicationUnchecked();
		if(app==null) {
			// There is no application created
			// This might be because we are in a faces request, and no call to the SBT library had been made so far
			// In this case, we create the application object and we destroy it when the XPages application is destroyed
			FacesContextEx facesContext = FacesContextEx.getCurrentInstance();
			if(facesContext!=null) {
				final XspApplication newApp = app = (XspApplication)initApplication(facesContext.getExternalContext().getContext());
				ApplicationEx facesApplication = facesContext.getApplicationEx();
				facesApplication.addApplicationListener(new ApplicationListener() {
					public void applicationDestroyed(ApplicationEx application) {
					}
					public void applicationCreated(ApplicationEx application) {
						destroyApplication(newApp);
					}
				});
			}
		}
		return app;
	}

	@Override
	public Application createApplication(Object context) {
		ApplicationEx facesApplication = ApplicationEx.getInstance();
		// Try to find the application name
		// We start from the the NotesContext, which should be defined
		String name = null;
		NotesContext ctx = NotesContext.getCurrentUnchecked();
		if(ctx!=null) {
			name = ctx.getModule().getModuleName();
		} else {
			name = facesApplication.getApplicationId();
		}
		return new XspApplication(facesApplication,(ServletContext)context,name);
	}

	public XspContext getContextUnchecked() {
		XspContext ctx = (XspContext)super.getContextUnchecked();
		if(ctx==null) {
			FacesContextEx facesContext = FacesContextEx.getCurrentInstance();
			if(facesContext!=null) {
				Application app = getApplicationUnchecked();
				// We initialize a temporary context
				ExternalContext extCtx = facesContext.getExternalContext();
				final XspContext newContext = ctx = (XspContext)initContext(app, extCtx.getRequest(), extCtx.getResponse());
				// we should clear the context when the JSF context is discarded
				facesContext.addRequestListener(new FacesContextListener() {
					public void beforeRenderingPhase(FacesContext facesContext) {
					}
					public void beforeContextReleased(FacesContext facesContext) {
						destroyContext(newContext);
					}
				});
			}
		}
		return ctx;
	}

	@Override
	public Context createContext(Application application, Object request, Object response) {
		boolean deleteFacesContext = false;
		// FacesContext must had been created by initContext(), when not in a faces request
		FacesContextEx facesContext = FacesContextEx.getCurrentInstance();
		if(facesContext==null) {
			// We create a temporary context
			facesContext = createFacesContext(application, request, response);
			deleteFacesContext = true;
		}
		return new XspContext(application,facesContext,(HttpServletRequest)request,(HttpServletResponse)response,deleteFacesContext);
	}
	
	
	
	//
	// Fake FacesContext for non faces requests
	//

	protected FacesContextEx createFacesContext(Application application, Object servletRequest, Object servletResponse) {
		ServletContext servletContext = (ServletContext)application.getApplicationContext();
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        
        // Create a temporary FacesContext and make it available
        FacesContextFactory contextFactory = (FacesContextFactory)FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        FacesContextEx context = (FacesContextEx)contextFactory.getFacesContext(servletContext, request, response, dummyLifeCycle);
        return context;
    }
    
    protected void releaseContext(FacesContext context) {
        context.release();
    }
    

    // The FacesContext factory requires a lifecycle parameter which is not used, but when not present, it generates
    // a NullPointerException. Silly thing! So we create an empty one that does nothing... 
    private static Lifecycle dummyLifeCycle = new Lifecycle() {
        @Override
        public void render(FacesContext context) throws FacesException {
            throw new NotImplementedException();
        }
        @Override
        public void removePhaseListener(PhaseListener listener) {
            throw new NotImplementedException();
        }
        @Override
        public PhaseListener[] getPhaseListeners() {
            throw new NotImplementedException();
        }
        @Override
        public void execute(FacesContext context) throws FacesException {
            throw new NotImplementedException();
        }
        @Override
        public void addPhaseListener(PhaseListener listener) {
            throw new NotImplementedException();
        }
    };

}
