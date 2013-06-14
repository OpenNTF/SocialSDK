/* ***************************************************************** */
/*                                                                   */
/* IBM Confidential                                                  */
/*                                                                   */
/* OCO Source Materials                                              */
/*                                                                   */
/* Copyright IBM Corp. 2004, 2011                                    */
/*                                                                   */
/* The source code for this program is not published or otherwise    */
/* divested of its trade secrets, irrespective of what has been      */
/* deposited with the U.S. Copyright Office.                         */
/*                                                                   */
/* ***************************************************************** */

package nsf.playground.json;

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
	
	private static String convertKey(String o) {
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
