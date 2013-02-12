package nsf.playground.beans;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.ViewEntry;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.jscript.JSContext;
import com.ibm.jscript.json.JsonJavaScriptFactory;
import com.ibm.jscript.std.ArrayObject;
import com.ibm.jscript.std.ObjectObject;
import com.ibm.jscript.types.FBSBoolean;
import com.ibm.jscript.types.FBSNull;
import com.ibm.jscript.types.FBSString;
import com.ibm.jscript.types.FBSUtility;
import com.ibm.jscript.types.FBSValue;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.apis.APIAssetNode;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.util.JavaScriptUtil;

/**
 * Classes that encapsulates the business logic for an API Description.
 * 
 * @author priand
 */
public class APIBean extends AssetBean {

	private static JSContext jsContext = JavaScriptUtil.getJSContext();

	protected String getFlatView() {
		return "AllAPIsFlat";
	}

	protected String getAllView() {
		return "AllAPIs";
	}
	
	public String getToolkitUrl() throws UnsupportedEncodingException, IOException {
		Map<String,Object> sessionScope = ExtLibUtil.getSessionScope();

		DataAccess dataAccess = DataAccess.get();
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
		ObjectObject o = new ObjectObject(jsContext);
		try {
			if(StringUtil.isNotEmpty(id)) {
				Database database = ExtLibUtil.getCurrentDatabase();
				ViewEntry e = database.getView("AllAPIsById").getEntryByKey(id);
				if(e!=null) {
					Document doc = e.getDocument();
					String endpoint = doc.getItemValueString("Endpoint");
					if(StringUtil.isNotEmpty(endpoint)) {
						o.put("endpoint", FBSString.get(endpoint));
					}
					String baseDocUrl = doc.getItemValueString("BaseDocUrl");
					if(StringUtil.isNotEmpty(endpoint)) {
						o.put("baseDocUrl", FBSString.get(baseDocUrl));
					}
					String json = doc.getItemValueString("Json");
					if(StringUtil.isNotEmpty(json)) {
						FBSValue value = (FBSValue)JsonParser.fromJson(new JsonJavaScriptFactory(jsContext), json);
						o.put("items", value);
					}
					fixAPIObject(o);
				}
			}
		} catch(Exception ex) {
			Platform.getInstance().log(ex);
		}
		return o;
	}

	// Make sure the Json objects are what is expected 
	protected void fixAPIObject(ObjectObject o) throws Exception {
		ArrayObject a = (ArrayObject)o.get("items");
		int count = a.getArrayLength();
		for(int i=0; i<count; i++) {
			ObjectObject item = (ObjectObject)a.getArrayElement(i);
			fixItem(item);
		}
	}
	protected void fixItem(ObjectObject item) throws Exception {
		// Load the URI parameters
		fixUriParameters(item);
		// Load the Query parameters
		fixQueryParameters(item);
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
					if(!a.contains(v)) {
						System.out.println(StringUtil.format("Missing uri property {0}",p));
						addParam(a, p, FBSString.emptyString, FBSUtility.wrap("string"), FBSString.emptyString);
	                    pos = end + 1;
					}
                    continue;
				}
			}
			break;
		} while(true);
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

		ArrayObject a1 = (ArrayObject)item.get("uriParameters");
		if(a1.getArrayLength()>0) {
			for(int i=0; i<a1.getArrayLength(); i++) {
				a.addArrayValue(a1.getArrayValue(i));
			}
		}
		
		ArrayObject a2 = (ArrayObject)item.get("queryParameters");
		if(a2.getArrayLength()>0) {
			for(int i=0; i<a2.getArrayLength(); i++) {
				a.addArrayValue(a2.getArrayValue(i));
			}
		}

		// Add a pseudo query string parameter
		ObjectObject qs = addParam(a, FBSString.get("query-string"), FBSString.emptyString, FBSUtility.wrap("string"), FBSUtility.wrap("Extra query string parameters"));
		qs.put("optional", FBSBoolean.TRUE);
		
		return a;
	}

}
