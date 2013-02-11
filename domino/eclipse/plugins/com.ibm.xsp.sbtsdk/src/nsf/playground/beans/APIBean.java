package nsf.playground.beans;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.ViewEntry;

import com.ibm.commons.runtime.util.URLEncoding;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.UrlUtil;
import com.ibm.jscript.std.ArrayObject;
import com.ibm.jscript.std.ObjectObject;
import com.ibm.jscript.types.FBSString;
import com.ibm.jscript.types.FBSUtility;
import com.ibm.jscript.types.FBSValue;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.apis.APIAssetNode;
import com.ibm.xsp.extlib.util.ExtLibUtil;

/**
 * Classes that encapsulates the business logic for an API Description.
 * 
 * @author priand
 */
public class APIBean extends AssetBean {

	protected String getFlatView() {
		return "AllAPIsFlat";
	}

	protected String getAllView() {
		return "AllAPIs";
	}
	
	public String getToolkitUrl() throws UnsupportedEncodingException, IOException {
		Map<String,Object> sessionScope = ExtLibUtil.getSessionScope();
		String env = (String)sessionScope.get("environment");
		String url = "xsp/.sbtlibrary"+(StringUtil.isNotEmpty(env)?"?env="+encodeUrl(env):"");
		return url;
	}

	protected AssetNode createAssetNode(String notesUnid, CategoryNode parent, String name, String category, String assetId) {
		return new APIAssetNode(parent,name,category,notesUnid,assetId);
	}
	
	public String loadAPI(String id) throws NotesException {
		if(StringUtil.isNotEmpty(id)) {
			Database database = ExtLibUtil.getCurrentDatabase();
			ViewEntry e = database.getView("AllAPIsById").getEntryByKey(id);
			if(e!=null) {
				Document doc = e.getDocument();
				return doc.getItemValueString("Json");
			}
		}
		return null;
	}
	
	public Object loadParameters(Object o) throws Exception {
		ArrayObject a = new ArrayObject();
		if(o instanceof ObjectObject) {
			ObjectObject oo = (ObjectObject)o;
			extract(a,oo,"http_headers");
			extract(a,oo,"uri");
		}
		return a;
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
					String p = value.substring(start+1,end).trim();
					ObjectObject o = new ObjectObject();
					o.put("name",FBSUtility.wrap(p));
					o.put("value",FBSString.emptyString);
					o.put("type",FBSUtility.wrap("string"));
					o.put("description",FBSString.emptyString);
                    a.put(a.getArrayLength(),o);
                    pos = end + 1;
                    continue;
				}
			}
			break;
		} while(true);
	}
//	"http_headers": "{http_headers}",
//	"uri": "/files/basic/api/myuserlibrary/document/{document-id}/media?{parameters}", 	
}
