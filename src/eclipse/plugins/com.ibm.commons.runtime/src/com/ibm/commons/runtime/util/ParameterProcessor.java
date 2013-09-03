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
package com.ibm.commons.runtime.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;

/**
 * @author mwallace
 */
public class ParameterProcessor {
	
	static final String DELIM_START = "%{"; //$NON-NLS-1$
	static final String DELIM_END	= "}"; //$NON-NLS-1$
	
	static ParameterProvider defaultProvider;

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String process(String input) {
		if(StringUtil.isNotEmpty(input)) {
			// default behaviour is to use context or if not available use application properties
			final Context context = Context.getUnchecked();
			final Application application = Application.getUnchecked();
			ParameterProvider provider = new ParameterProvider() {
				@Override
				public String getParameter(String name) {
					if (context != null) {
						return context.getProperty(name);
					}
					return (application == null) ? null : application.getProperty(name);
				}
			};
			
			return process(input, DELIM_START, DELIM_END, provider);
		}
		return input;
	}
	
	public static List<String> getParameters(String input){
	    Pattern paramsPattern = Pattern.compile("%\\{(.*?)\\}");
	    Matcher paramsMatcher = paramsPattern.matcher(input);
	    ArrayList<String> result = new ArrayList<String>();
	    while(paramsMatcher.find()){
	        result.add(paramsMatcher.group(1));
	    }
	    return result;
	}
	
	public static ParameterProvider getDefaultProvider(){
	    if(defaultProvider != null)
	        return defaultProvider;
	    final Context context = Context.getUnchecked();
	    final Application application = Application.getUnchecked();
	    defaultProvider = new ParameterProvider() {
            @Override
            public String getParameter(String name) {
                if (context != null) {
                    return context.getProperty(name);
                }
                return (application == null) ? null : application.getProperty(name);
            }
        };
        return defaultProvider;
	}

	public static ParameterProvider getDefaultProvider(final ParameterProvider provider){
        final ParameterProvider defaultProvider = getDefaultProvider();
	    ParameterProvider result = new ParameterProvider() {
            @Override
            public String getParameter(String name) {
                if (provider != null) {
                    String value = provider.getParameter(name);
                    if (value != null) {
                        return value;
                    }
                }
                return defaultProvider.getParameter(name);
            }
        };
        return result;
    }
	
    /**
     * 
     * @param input
     * @return
     */
    public static String process(String input, final ParameterProvider provider, boolean includeDefault) {
        if (!includeDefault) {
            return process(input, provider);
        }
        if(StringUtil.isNotEmpty(input)) {
            // default behaviour is to use context or if not available use application properties
            final Context context = Context.getUnchecked();
            final Application application = Application.getUnchecked();
            ParameterProvider defaultProvider = new ParameterProvider() {
                @Override
                public String getParameter(String name) {
                    if (provider != null) {
                        String value = provider.getParameter(name);
                        if (value != null) {
                            return value;
                        }
                    }
                    if (context != null) {
                        return context.getProperty(name);
                    }
                    return (application == null) ? null : application.getProperty(name);
                }
            };
            
            return process(input, DELIM_START, DELIM_END, defaultProvider);
        }
        return input;
    }

	/**
	 * 
	 * @param input
	 * @param properties
	 * @return
	 */
	public static String process(String input, final Properties properties) {
		if(StringUtil.isNotEmpty(input)) {
			ParameterProvider provider = new ParameterProvider() {
				@Override
				public String getParameter(String name) {
					return (properties == null) ? null : properties.getProperty(name);
				}
			};
			
			return process(input, DELIM_START, DELIM_END, provider);
		}
		return input;
	}

	/**
	 * 
	 * @param input
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String process(String input, final Map map) {
		if(StringUtil.isNotEmpty(input)) {
			ParameterProvider provider = new ParameterProvider() {
				@Override
				public String getParameter(String name) {
					return (map == null) ? null : (String)map.get(name);
				}
			};
			
			return process(input, DELIM_START, DELIM_END, provider);
		}
		return input;
	}

	/**
	 * 
	 * @param input
	 * @param provider
	 * @return
	 */
	public static String process(String input, ParameterProvider provider) {
		return process(input, DELIM_START, DELIM_END, provider);
	}

	/**
	 * 
	 * @param input
	 * @param delimStart
	 * @param delimEnd
	 * @param provider
	 * @return
	 */
	public static String process(String input, String delimStart, String delimEnd, ParameterProvider provider) {
		if(StringUtil.isNotEmpty(input)) {
			StringBuilder output = new StringBuilder(2048);
			int length = input.length();
			for(int index=0; index<length; ) {
				int start = input.indexOf(delimStart, index);
				int end = (start >= 0) ? start : length;
				if(end > index) {
					output.append(input.substring(index, end));
					index = end;
				}
				if(start >= 0) {
					int paramEnd = input.indexOf(delimEnd, start+delimStart.length()); 
					if(paramEnd < 0) {
						// assume malformed delim pair is an error 
						String msg = MessageFormat.format("Unable to find parameter end delimiter {0}", delimEnd);
						throw new IllegalArgumentException(msg);
					}
					
					// process the parameter
					String parameter = input.substring(start+delimStart.length(), paramEnd);
					String replace = processParameter(parameter, provider);
                    replace = StringUtil.trim(replace);
					output.append(replace!=null?replace:""); // avoid 'null' string
	
					index = paramEnd + delimEnd.length();
				}
			}
	
			return output.toString();
		}
		return input;
	}

	/*
     * Process a parameter with the following format:
     *      name[=value][|label=xxx][|required]
     * Just adds the name
     */
    private static String processParameter(String name, ParameterProvider provider) {
        String[] parts = StringUtil.splitString(name, '|');
        String[] namePart = StringUtil.splitString(parts[0], '=');
        if (namePart.length == 2) {
            String value = provider.getParameter(namePart[0]);
            return StringUtil.isEmpty(value) ? namePart[1] : value;
        } else {
            return provider.getParameter(namePart[0]);
        }
    }
	
	   
    /**
     * Gets the specified part out of a parameter of format
     *    name[=value][|label=xxx][|idHelpSnippet=snippet_id][|required]
     *    
     *    name is always first. If a part is followed by '=' then the part after '=' is returned, else it just returns the part.
     * @param parameter
     * @return
     */
    public static String getParameterPart(String parameter, String part){
        String[] parts = StringUtil.splitString(parameter, '|');
        
        if(part.equals("name")){ // return the name not the value
            String[] namePart = StringUtil.splitString(parts[0], '=');
            return namePart[0];
        } 
        else if(part.equals("required")){
            if(parts[parts.length - 1].equals("required")){
                return "required";
            }
            else{
                return null;
            }
        }
        
        for(int i = 1; i < parts.length; i++){
            String[] splitParts = StringUtil.splitString(parts[i], '=');
            if(splitParts[0].equals(part)){
                if(splitParts.length == 1){
                    return splitParts[0];
                }
                else{
                    return splitParts[1];
                }
            }
        }
        
        return null;
    }
    
	/**
	 * Interface to provide parameter values for the processing
	 */
	public interface ParameterProvider {
		
		/**
		 * Returned the parameter for the specified name.
		 * 
		 * @param name
		 * 
		 * @return
		 */
		public String getParameter(String name);
		
	}
	
	/*
	 * Unit tests
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("name1", "value1");
		params.put("name2", "value2");
		params.put("sample.email1", "fadams@renovations.com");
		
		ParameterProvider provider = new ParameterProvider() {
			@Override
			public String getParameter(String name) {
				return params.get(name);
			}
		};
		
		String input1 = "Example 1: %{name1} %{name2=defaultValue2} %{name3=defaultValue3}";
		String result1 = "Example 1: value1 value2 defaultValue3";
		String output1 = process(input1, provider);
		if (!result1.equals(output1)) {
			System.err.println("Example 1 failed: "+output1);
		} else {
			System.out.println("Example 1 passed");
		}
		
		String input2 = "Example 2: %{name1} %{name2=defaultValue2} %{name3=defaultValue3}";
		String result2 = "Example 2: value1 value2 defaultValue3";
		String output2 = process(input2, provider);
		if (!result2.equals(output2)) {
			System.err.println("Example 2 failed: "+output2);
		} else {
			System.out.println("Example 2 passed");
		}

		String input3 = "Example 3: value1 value2 defaultValue3";
		String result3 = "Example 3: value1 value2 defaultValue3";
		String output3 = process(input3, provider);
		if (!result3.equals(output3)) {
			System.err.println("Example 3 failed");
		} else {
			System.out.println("Example 3 passed");
		}

		try {
			String input4 = "Example 4: %{name1";
			process(input4, provider);
			System.err.println("Example 4 failed");
		} catch (IllegalArgumentException iae) {	
			System.out.println("Example 4 passed");
		}
		
		String input5 = "require(['sbt/connections/ProfileService','sbt/dom'], function(ProfileService,dom) {\nvar email = '%{sample.email1}';\nvar svc = new ProfileService();";
		String result5 = "require(['sbt/connections/ProfileService','sbt/dom'], function(ProfileService,dom) {\nvar email = 'fadams@renovations.com';\nvar svc = new ProfileService();";;
		String output5 = process(input5, provider);
		if (!result5.equals(output5)) {
			System.err.println("Example 5 failed: "+output5);
		} else {
			System.out.println("Example 5 passed");
		}
		
	}

}
