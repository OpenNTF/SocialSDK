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

package com.ibm.commons.runtime;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * SBT Context.
 *
 * This class encapsulate a context that can be reused by all the helper classes.
 *  
 * @author Philippe Riand
 */
public abstract class Context {
	
	public static final String ANONYMOUS = "anonymous";

	public static Context init(Application application, Object request, Object response) {
		return RuntimeFactory.get().initContext(application, request, response);
	}
	public static void destroy(Context ctx) {
		RuntimeFactory.get().destroyContext(ctx);
	}
	public static Context get() {
		Context ctx = RuntimeFactory.get().getContextUnchecked();
		if(ctx==null) {
			throw new IllegalStateException("SBT context is not initialized for the request");
		}
		return ctx;
	}
	public static Context getUnchecked() {
		return RuntimeFactory.get().getContextUnchecked();
	}
	
	private Application application;
	
	protected Context(Application application) {
		this.application = application;
	}
	
	public void close() {
	}

	public ClassLoader getClassLoader() {
		return getApplication().getClassLoader();
	}
	
	public abstract String getCurrentUserId();
	
	public boolean isCurrentUserAnonymous() {
		return getCurrentUserId().equals(ANONYMOUS);
	}
	
	public abstract Object getHttpRequest();

	public abstract Object getHttpResponse();
	
	public Application getApplication() {
		return application;
	}
	
	public Application getApplicationUnchecked() {
		if(application==null) {
			application = Application.getUnchecked();
		}
		return application;
	}
	
	public abstract String getProperty(String propertyName);

	public abstract String getProperty(String propertyName, String defaultValue);
	
	public abstract void setProperty(String propertyName, String value);
	
	public abstract Object getBean(String beanName);

	public abstract void sendRedirect(String redirectUrl) throws IOException;
	
	// Find a better name for this method - getActionUrl()?
	public abstract String encodeUrl(String url);

	
	//
	// Access to the scopes
	//
	public static final int SCOPE_NONE 			= 0;
	public static final int SCOPE_GLOBAL 		= 1;
	public static final int SCOPE_APPLICATION 	= 2;
	public static final int SCOPE_SESSION 		= 3;
	public static final int SCOPE_REQUEST 		= 4;

	public Map<String,Object> getScope(int scope) {
		switch(scope) {
			case SCOPE_GLOBAL:			return getGlobalMap();
			case SCOPE_APPLICATION:		return getApplicationMap();
			case SCOPE_SESSION:			return getSessionMap();
			case SCOPE_REQUEST:			return getRequestMap();
		}
		return null;
	}
	
	private static HashMap<String,Object> globalScope = new HashMap<String, Object>(); 
	public Map<String,Object> getGlobalMap() {
		return globalScope;
	}

	private Map<String,Object> applicationMap;
	public Map<String,Object> getApplicationMap() {
		if(applicationMap==null) {
			applicationMap = createApplicationMap();
		}
		return applicationMap; 
	}
	protected Map<String,Object> createApplicationMap() {
		return new ApplicationMap(this);
	}
	
	private Map<String,Object> sessionMap;
	public Map<String,Object> getSessionMap() {
		if(sessionMap==null) {
			sessionMap = createSessionMap();
		}
		return sessionMap; 
	}
	protected abstract Map<String,Object> createSessionMap();
	
	private Map<String,Object> requestMap;
	public Map<String,Object> getRequestMap() {
		if(requestMap==null) {
			requestMap = createRequestMap();
		}
		return requestMap; 
	}
	protected abstract Map<String,Object> createRequestMap();
	
	private Map<String,Object> requestParameterMap;
	public Map<String,Object> getRequestParameterMap() {
		if(requestParameterMap==null) {
			requestParameterMap = createRequestParameterMap();
		}
		return requestParameterMap; 
	}
	protected abstract Map<String,Object> createRequestParameterMap();
	
	private Map<String,Object> requestCookieMap;
	public Map<String,Object> getRequestCookieMap() {
		if(requestCookieMap==null) {
			requestCookieMap = createRequestCookieMap();
		}
		return requestCookieMap; 
	}
	protected abstract Map<String,Object> createRequestCookieMap();

	
	/**
	 * Utility class for creating maps.
	 * @author priand
	 */
	protected static abstract class AbstractScopeMap extends AbstractMap<String,Object> {

	    @Override
		public void clear() {
	        throw new UnsupportedOperationException();
	    }

	    @Override
		public void putAll(Map<? extends String,? extends Object> p) {
	        throw new UnsupportedOperationException();
	    }

	    @Override
		public Object remove(Object key) {
	        throw new UnsupportedOperationException();
	    }

	    @Override
		public boolean equals(Object obj) {
	        if (obj.getClass()==this.getClass()) {
		        return super.equals(obj);
	        }
            return false;
	    }
	    
	    protected static class MapEntry implements Map.Entry<String,Object> {

	        private final String key;
	        private final Object value;

	        public MapEntry(String key, Object value) {
	            this.key = key;
	            this.value = value;
	        }

	        @Override
			public String getKey() {
	            return key;
	        }

	        @Override
			public Object getValue() {
	            return value;
	        }

	        @Override
			public Object setValue(Object value) {
	            throw new UnsupportedOperationException();
	        }

	        @Override
			public int hashCode() {
	            return ((key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode()));
	        }

	        @Override
			public boolean equals(Object obj) {
	            if (obj == null || !(obj instanceof Map.Entry)) {
	                return false;
	            }

	            @SuppressWarnings("unchecked")
				Map.Entry<String,Object> input = (Map.Entry<String,Object>) obj;
	            String inputKey = input.getKey();
	            Object inputValue = input.getValue();

	            if (inputKey == key || (inputKey != null && inputKey.equals(key))) {
	                if (inputValue == value || (inputValue != null && inputValue.equals(value))) {
	                    return true;
	                }
	            }
	            return false;
	        }
	    }
	}	

	private static class ApplicationMap extends AbstractScopeMap {

	    private Context context;

	    ApplicationMap(Context context) {
	        this.context = context;
	    }

	    @Override
		public Object get(Object key) {
	        Application app = context.getApplicationUnchecked();
	        return app!=null ? app.getScope().get((String)key) : null;
	    }

	    @Override
		public Object put(String key, Object value) {
	    	Application app = context.getApplication();
	        return app.getScope().put(key, value);
	    }

	    @Override
		public Object remove(Object key) {
	    	Application app = context.getApplicationUnchecked();
	        if(app!=null) {
	        	return app.getScope().remove(key);
	        }
	        return null;
	    }

	    @Override
		public Set<Entry<String, Object>> entrySet() {
	    	Application app = context.getApplicationUnchecked();
	        if(app!=null) {
		        return app.getScope().entrySet();
	        } else {
	        	return Collections.emptySet();
	        }
	    }
	}

}
