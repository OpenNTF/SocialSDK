/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.smartcloud.bss;

import java.lang.reflect.Method;
import java.text.MessageFormat;

import com.ibm.commons.runtime.util.ParameterProcessor;
import com.ibm.commons.util.StringUtil;

/**
 * @author mwallace
 *
 */
abstract public class BaseJsonBuilder {
	
	protected boolean strict = true;
	protected String template;


	/**
	 * @param strict the strict to set
	 */
	public void setStrict(boolean strict) {
		this.strict = strict;
	}
	
	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}
		
	/**
	 * Return a string representation of this Customer instance
	 * @return
	 */
	public String toJson() {
		
		String json = ParameterProcessor.process(template, new ParameterProcessor.ParameterProvider() {
			@Override
			public String getParameter(String name) {
				try {
					Method method = BaseJsonBuilder.this.getClass().getMethod(name);
					Object value = method.invoke(BaseJsonBuilder.this);
					return (value == null) ? "" : String.valueOf(value);
				} catch (Exception e) {
					e.printStackTrace();
					String msg = "Error processing {0} caused by: {1}";
					return MessageFormat.format(msg, name, e.getMessage());
				}
			}
		});
		
		return json;
	}
	
	//
	// Internals
	//
	
	public class JsonField {
		private String name;
		private boolean required;
		private int maxLength;
		private Object[] values;
		private Object value;
		
		JsonField(String name, boolean required, int maxLength) {
			this(name, required, maxLength, null);
		}
		
		JsonField(String name, boolean required, int maxLength, Object[] values) {
			this.name = name;
			this.required = required;
			this.maxLength = maxLength;
			this.values = values;
		}
		
		void setValue(Object value) {
			if (strict && (value instanceof String)) {
				String strValue = (String)value;
				if (StringUtil.isEmpty(strValue) && required) {
					String msg = "{0} is a required customer field.";
					msg = MessageFormat.format(msg, name);
					throw new IllegalArgumentException(msg);
				}
				if (maxLength != -1 && StringUtil.isNotEmpty(strValue) && strValue.length() > maxLength) {
					String msg = "Maximum length for {0} is {1}.";
					msg = MessageFormat.format(msg, name, maxLength);
					throw new IllegalArgumentException(msg);
				}
				if (values != null) {
					boolean valid = false;
					for (Object validValue : values) {
						if (validValue.equals(value)) {
							valid = true;
							break;
						}
					}
					if (!valid) {
						String msg = "Value for {0} must be one of: {1}.";
						msg = MessageFormat.format(msg, name, values);
						throw new IllegalArgumentException(msg);
					}
				}
			}
			
			this.value = value;
		}
		
		Object getValue() {
			return this.value;
		}
	}
}
