/*
 * © Copyright IBM Corp. 2013
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

package nsf.playground.json;

import java.util.Iterator;

import com.ibm.commons.util.IteratorWrapper;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;



/**
 * JSON implementation for a JsonObject in Java that has its properties case insensitive.
 * <p>
 * </p>  
 * @ibm-api
 */
public class JsonJavaObjectI extends JsonJavaObject {

    public static final JsonJavaFactory instanceExI = new JsonJavaFactory() {
        public Object createObject(Object parent, String propertyName) {
            return new JsonJavaObjectI();
        }
        public Object getProperty(Object parent, String propertyName) throws JsonException {
        	return super.getProperty(parent, convertKey(propertyName));
        }
        public void setProperty(Object parent, String propertyName, Object value) throws JsonException {
        	super.setProperty(parent, convertKey(propertyName), value);
        }
        public Iterator<String> iterateObjectProperties(Object object) throws JsonException {
        	return new IteratorWrapper<String>(super.iterateObjectProperties(object)) {
				@Override
				protected String wrap(Object o) {
					return convertKey((String)o);
				}
			};
        }
    };
    
    
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // TEMPORARY IMPLEMENTATION UNTIL WE HAVE D9 IN THE BUILD
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public JsonJavaObjectI() {
	}

	public Object get(Object key) {
		return super.get(convertKey((String)key));
	}

	public Object get(String key) {
		return super.get(convertKey(key));
	}

//	public void put(String property, Object value) {
//		super.put(convertKey(property),value);
//		//return null;
//	}
	
	public static String convertKey(String o) {
		return ((String)o).toLowerCase();
	}
	
	/**
	 * Singleton instance using a case insentive JavaObject.
	 */
/*    
    public static final JsonJavaFactory instanceExI = new JsonJavaFactory() {
        public Object createObject(Object parent, String propertyName) {
            return new JsonJavaObjectI();
        }
        public List<Object> createTemporaryArray(Object parent) throws JsonException {
            return new JsonJavaArray();
        }
		public Object createArray(Object parent, String propertyName, List<Object> values) {
			if(values instanceof JsonJavaArray) {
				return values;
			}
            return new JsonJavaArray(values);
        }
    };

	private static final long serialVersionUID = 1L;
	
	public JsonJavaObjectI() {
	}

	@SuppressWarnings("unchecked")
	public JsonJavaObjectI(Map<String,Object> props) {
		putAll(props);
	}

	public JsonJavaObjectI(Object...properties) {
		super(properties);
	}

	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(convertKey(key));
	}

	@Override
	public Object get(Object key) {
		return super.get(convertKey(key));
	}

	@Override
	public Object get(String key) {
		return super.get(convertKey(key));
	}

	@Override
	public Object put(String property, Object value) {
		return super.put(convertKey(property),value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> map) {
		if(map!=null) {
			for(Map.Entry<? extends String, ? extends Object> e: map.entrySet()) {
				put(e.getKey(),e.getValue());
			}
		}
	}

	@Override
	public Object remove(Object key) {
		return super.remove(convertKey(key));
	}

	@Override
	public void remove(String key) {
		super.remove(convertKey(key));
	}

	private static Object convertKey(Object o) {
		if(o instanceof String) {
			return ((String)o).toLowerCase();
		}
		return null;
	}
	private static String convertKey(String o) {
		return ((String)o).toLowerCase();
	}
*/	
}
