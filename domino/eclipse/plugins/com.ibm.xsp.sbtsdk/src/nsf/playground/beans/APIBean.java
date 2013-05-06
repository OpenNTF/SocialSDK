package nsf.playground.beans;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.ViewEntry;

import com.ibm.commons.Platform;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.jscript.JSContext;
import com.ibm.jscript.json.JsonJavaScriptFactory;
import com.ibm.jscript.std.ArrayObject;
import com.ibm.jscript.std.ObjectObject;
import com.ibm.jscript.types.FBSBoolean;
import com.ibm.jscript.types.FBSString;
import com.ibm.jscript.types.FBSUtility;
import com.ibm.jscript.types.FBSValue;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.Node;
import com.ibm.sbt.playground.assets.apis.APIAssetNode;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.util.JavaScriptUtil;

/**
 * Classes that encapsulates the business logic for an API Description.
 * 
 * @author priand
 */
public abstract class APIBean extends AssetBean {
	
	public static final boolean TRACE = false;

	public static final String FORM = "APIDescription";
	
	private static JSContext jsContext = JavaScriptUtil.getJSContext();

	private String id;
	private String unid;
	private ObjectObject cache;
	
	public APIBean() {
	}
	
	protected String getAssetForm() {
		return FORM;
	}
	
	public String getToolkitUrl() throws UnsupportedEncodingException, IOException {
		Map<String,Object> sessionScope = ExtLibUtil.getSessionScope();

		DataAccessBean dataAccess = DataAccessBean.get();
		String envName = (String)sessionScope.get("environment");
		if(StringUtil.isEmpty(envName)) {
			envName = dataAccess.getPreferredEnvironment();
		}
		String url = "xsp/.sbtlibrary";
		if(StringUtil.isNotEmpty(envName)) {
			url += "?env="+encodeUrl(envName);
		}
		return url;
	}

	protected AssetNode createAssetNode(String notesUnid, CategoryNode parent, String name, String category, String assetId) {
		return new APIAssetNode(parent,name,category,notesUnid,assetId);
	}
	
	public ObjectObject loadAPI(String id) throws NotesException {
		return loadAPI(id,null);
	}
	public ObjectObject loadAPI(String id, String unid) throws NotesException {
		// The method can be called when the page is created and the in the rendeing pahse
		// we cache the value to avoid multiple DB access
		// Not that this requires the bean to be set to the request scope, and not shared
		if(cache!=null) {
			if(StringUtil.equals(id, this.id) && StringUtil.equals(unid, this.unid)) {
				return cache;
			}
			this.cache = null;
			this.id = null;
			this.unid = null;
		}
		ObjectObject o = new ObjectObject(jsContext);
		try {
			if(StringUtil.isNotEmpty(id)) {
				Database database = ExtLibUtil.getCurrentDatabase();
				Vector v = new Vector();
				v.add(FORM);
				v.add(id);
				ViewEntry e = database.getView("AllSnippetsById").getEntryByKey(v);
				if(e!=null) {
					Document doc = e.getDocument();
					Properties p = new Properties();
					String props = doc.getItemValueString("Properties");
					if(StringUtil.isNotEmpty(props)) {
						p.load(new StringReader(props));
					}
					String endpoint = p.getProperty("endpoint");
					if(StringUtil.isNotEmpty(endpoint)) {
						o.put("endpoint", FBSString.get(endpoint));
					}
					String baseDocUrl = doc.getItemValueString("basedocurl");
					if(StringUtil.isNotEmpty(endpoint)) {
						o.put("doc_url", FBSString.get(baseDocUrl));
					}
					String json = doc.getItemValueString("Json");
					if(StringUtil.isNotEmpty(json)) {
						FBSValue value = (FBSValue)JsonParser.fromJson(new JsonJavaScriptFactory(jsContext), json);
						o.put("items", value);
					}
					fixAPIObject(o,baseDocUrl,unid);
				}
			}
		} catch(Exception ex) {
			Platform.getInstance().log(ex);
		}
		this.cache = o;
		this.id = id;
		this.unid = unid;
		return o;
	}
	
	// Make sure the Json objects are what is expected 
	protected void fixAPIObject(ObjectObject o, String baseDocUrl, String unid) throws Exception {
		ArrayObject items = (ArrayObject)o.get("items");

		ArrayObject result = items;
		if(StringUtil.isNotEmpty(unid)) {
			result = new ArrayObject();
			o.put("items", result);
		}
		int count = items.getArrayLength();
		for(int i=0; i<count; i++) {
			ObjectObject item = (ObjectObject)items.getArrayElement(i);
			// Fix the unid if it doesn't exist
			fixSnippetUnid(item);
			// And then filter it, or just fix it
			if(result!=items) {
				String oid = item.get("unid").stringValue(); 
				if(oid.equals(unid)) {
					fixItem(item,baseDocUrl);
					result.addArrayValue(item);
					return;
				}
			} else {
				fixItem(item,baseDocUrl);
			}
		}
	}
	protected void fixItem(ObjectObject item, String baseDocUrl) throws Exception {
		// Load the URI parameters
		fixUriParameters(item);
		// Load the Query parameters
		fixQueryParameters(item);
		// Load the full documentation URL
		fixDocUrl(item,baseDocUrl);
	}
	
	protected void fixSnippetUnid(ObjectObject o) throws Exception {
		FBSValue unid = o.get("unid");
		if(!unid.booleanValue()) {
			unid = FBSString.get(Node.encodeUnid(o.get("name").stringValue()));
			o.put("unid", unid);
		}
	}
	
	protected void fixUriParameters(ObjectObject o) throws Exception {
		FBSValue params = o.get("uriParameters");
		if(!(params instanceof ArrayObject)) {
			params = new ArrayObject();
			((ObjectObject)o).put("uriParameters",params);
		}
		// Look if there are missing parameters in the URI
		extract((ArrayObject)params,o,"uri");
	}
	protected void fixQueryParameters(ObjectObject o) throws Exception {
		FBSValue params = o.get("queryParameters");
		if(!(params instanceof ArrayObject)) {
			params = new ArrayObject();
			((ObjectObject)o).put("queryParameters",params);
		}
	}
	protected void fixDocUrl(ObjectObject o, String baseDocUrl) throws Exception {
		FBSValue params = o.get("doc_url");
		if(!params.isNull()) {
			String url = params.stringValue();
			if(!UrlUtil.isAbsoluteUrl(url) && StringUtil.isNotEmpty(baseDocUrl)) {
				o.put("doc_url", FBSString.get(PathUtil.concat(baseDocUrl, url, '/')));
			}
		} 
	}
	private void extract(ArrayObject a, ObjectObject parent, String prop) throws Exception {
		FBSValue v = parent.get(prop);
		if(!v.isString()) {
			return;
		}
		String value = v.stringValue();
		int pos=0;
		do {
			int start = value.indexOf("{",pos);
			if(start>=0) {
				int end = value.indexOf("}",start);
				if(end>start+1) {
					FBSValue p = FBSUtility.wrap(value.substring(start+1,end).trim());
					if(!contains(a,p.stringValue())) {
						if(TRACE) {
							System.out.println(StringUtil.format("Missing uri property {0}",p));
						}
						addParam(a, p, FBSString.emptyString, FBSUtility.wrap("string"), FBSString.emptyString);
					}
                    pos = end + 1;
                    continue;
				}
			}
			break;
		} while(true);
	}
	private boolean contains(ArrayObject a, String name) throws Exception {
		int count = a.getArrayLength();
		for(int i=0; i<count; i++) {
			FBSValue v = a.getArrayValue(i);
			if(v.isJSObject()) {
				FBSValue n = v.asObject().get("name");
				if(n.isString() && n.stringValue().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}
	private ObjectObject addParam(ArrayObject a, FBSValue name, FBSValue value, FBSValue type, FBSValue description) throws Exception {
		ObjectObject o = new ObjectObject();
		o.put("name",name);
		o.put("value",value);
		o.put("type",type);
		o.put("description",description);
        a.addArrayValue(o);
        return o;
	}
	
	
	public Object loadParameters(Object o) throws Exception {
		ObjectObject item = (ObjectObject)o;
		ArrayObject a = new ArrayObject(jsContext);

		FBSValue a1 = (FBSValue)item.get("uriParameters");
		if(a1.isArray()) {
			for(int i=0; i<a1.getArrayLength(); i++) {
				a.addArrayValue(a1.getArrayValue(i));
			}
		}
		
		FBSValue a2 = (FBSValue)item.get("queryParameters");
		if(a2.isArray()) {
			for(int i=0; i<a2.getArrayLength(); i++) {
				a.addArrayValue(a2.getArrayValue(i));
			}
		}

		// Add a pseudo query string parameter
		boolean qstring = item.get("queryString").booleanValue();
		if(qstring) {
			ObjectObject qs = addParam(a, FBSString.get("query-string"), FBSString.emptyString, FBSUtility.wrap("string"), FBSUtility.wrap("Extra query string parameters, as they would appear in the URL (a=1&b=2...)"));
			qs.put("optional", FBSBoolean.TRUE);
		}
		
		// Now, add the pseudo entries for the POST/PUT content 
		FBSValue v = item.get("http_method");
		if(v.isString()) {
			String m = v.stringValue();
			if(m.equalsIgnoreCase("post")||m.equalsIgnoreCase("put")) {
				ObjectObject ct = addParam(a, FBSString.get("post_content_type"), item.get("post_content_type"), FBSUtility.wrap("string"), FBSUtility.wrap("Content-Type header of the payload"));
				ct.put("optional", FBSBoolean.TRUE);
				ObjectObject bd = addParam(a, FBSString.get("post_content"), item.get("post_content"), FBSUtility.wrap("textarea"), FBSUtility.wrap("Text content sent to the service"));
				bd.put("optional", FBSBoolean.TRUE);
			}
		}
		return a;
	}

}
