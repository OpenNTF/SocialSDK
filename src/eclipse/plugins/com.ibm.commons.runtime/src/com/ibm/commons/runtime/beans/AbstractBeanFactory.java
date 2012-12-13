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

package com.ibm.commons.runtime.beans;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.ibm.commons.runtime.impl.ManagedBeanFactory;
import com.ibm.commons.runtime.util.ParameterProcessor;
import com.ibm.commons.util.StringUtil;


/**
 * SBT ManagedBean Factory.
 *
 * This class creates the managed beans when necessary.
 *  
 * @author Philippe Riand
 */
public class AbstractBeanFactory extends ManagedBeanFactory {
	
	public static class BeanProperty {
		private String name;
		private String value;
		public BeanProperty(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}
	public static class Factory implements BeanFactory {
		private int scope;
		private String name;
		private String className;
		private BeanProperty[] properties;
		public Factory(int scope, String name, String className, BeanProperty[] properties) {
			this.scope = scope;
			this.name = name;
			this.className = className;
			this.properties = properties;
		}
		@Override
		public int getScope() {
			return scope;
		}
		@Override
		public Object create(ClassLoader classLoader) {
			try {
				Class<?> c = classLoader.loadClass(className);
				Object o = c.newInstance();
				if(properties!=null) {
					for(int i=0; i<properties.length; i++) {
						BeanProperty p = properties[i];
						setProperty(o, p.name, p.value);
					}
				}
				return o;
			} catch (Exception e) {
				IllegalStateException ie = new IllegalStateException(StringUtil.format("Error while instanciating bean {0}, class {1}",name,className));
				ie.initCause(e);
				throw ie;
			}
		}
		protected void setProperty(Object o, String name, String value) {
			try {
				// Handle substitution variables
				value = replaceVariables(value);
				
				// A cache is already managed by the JVM for beaninfo
				BeanInfo bi = Introspector.getBeanInfo(o.getClass());
				if(bi!=null) {
					PropertyDescriptor[] desc = bi.getPropertyDescriptors();
					for(int i=0; i<desc.length; i++) {
						PropertyDescriptor d = desc[i];
						if(d.getName().equals(name)) {
							Method m = d.getWriteMethod();
							if(m!=null) {
								Class<?> pType = d.getPropertyType();
								if(pType==String.class) {
									m.invoke(o, value);
									return;
								} else if(pType==Boolean.class || pType==Boolean.TYPE) {
									boolean v = Boolean.parseBoolean(value);
									m.invoke(o, v);
									return;
								} else if(pType==Byte.class || pType==Byte.TYPE) {
									Byte v = Byte.parseByte(value.trim());
									m.invoke(o, v);
									return;
								} else if(pType==Short.class || pType==Short.TYPE) {
									Short v = Short.parseShort(value.trim());
									m.invoke(o, v);
									return;
								} else if(pType==Integer.class || pType==Integer.TYPE) {
									Integer v = Integer.parseInt(value.trim());
									m.invoke(o, v);
									return;
								} else if(pType==Long.class || pType==Long.TYPE) {
									Long v = Long.parseLong(value.trim());
									m.invoke(o, v);
									return;
								} else if(pType==Float.class || pType==Float.TYPE) {
									Float v = Float.parseFloat(value.trim());
									m.invoke(o, v);
									return;
								} else if(pType==Double.class || pType==Double.TYPE) {
									Double v = Double.parseDouble(value.trim());
									m.invoke(o, v);
									return;
								} else {
									throw new IllegalArgumentException(StringUtil.format("Invalid property type bean {0}, class {1}, property {2}, type {3}",name,className,d.getName(),pType));
								}
							}
						}
					}
				}
			} catch (Exception e) {
				IllegalStateException ie = new IllegalStateException(StringUtil.format("Error while setting bean properties, bean {0}, class {1}",name,className));
				ie.initCause(e);
				throw ie;
			}
		}
		
		private String replaceVariables(String value) {
			return ParameterProcessor.process(value);
		}
	}
	
	private Factory[] factories;
	
	public AbstractBeanFactory() {
	}
	
	public void setFactories(Factory[] factories) {
		this.factories = factories;
	}

	@Override
	public BeanFactory getBeanFactory(String name) {
		if(factories!=null) {
			for(int i=0; i<factories.length; i++) {
				Factory f = factories[i];
				if(StringUtil.equals(name, f.name)) {
					return f;
				}
			}
		}
		return null;
	}
}
