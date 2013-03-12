package nsf.playground.snippets;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import lotus.domino.Database;
import lotus.domino.Document;
import nsf.playground.jobs.AsyncAction;

import com.ibm.commons.Platform;
import com.ibm.commons.runtime.util.URLEncoding;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.Node;
import com.ibm.sbt.playground.assets.NodeFactory;
import com.ibm.sbt.playground.assets.apis.APIDescription;
import com.ibm.sbt.playground.assets.apis.APINodeFactory;
import com.ibm.sbt.playground.vfs.VFSFile;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.RestClient;
import com.ibm.xsp.FacesExceptionEx;

/**
 * Class for importing API Descriptions.
 * 
 * @author priand
 *
 */
public class APIImporter extends AssetImporter {

	public static final String TYPE = "api";
	
	public APIImporter(Database db) {
		super(db);
	}
	
	protected String getAssetType() {
		return TYPE;
	}

	protected String getAssetView() {
		return "AllAPIsByImportSource";
	}

	protected NodeFactory getNodeFactory() {
		return new APINodeFactory();
	}
	
	protected void saveAsset(ImportSource source, VFSFile root, AssetNode node) throws Exception {
		APIDescription snippet = (APIDescription)node.load(root);
		saveAsset(node.getUnid(), node.getCategory(), node.getName(), source.getName(), snippet.getJson(), snippet.getPropertiesAsString());
	}

	protected void saveAsset(String id, String category, String name, String source, String json, String properties) throws Exception {
		Document doc = getDatabase().createDocument();
		try {
			setItemValue(doc,"Form", "APIDescription");
			setItemValue(doc,"Author", doc.getParentDatabase().getParent().getUserName()); // Should we make this private (reader field)?
			setItemValue(doc,"Id", id);
			setItemValue(doc,"Category", category);
			setItemValue(doc,"Name", name);
			setItemValue(doc,"ImportSource", source);
			setItemValueRichText(doc,"Json", json);
			setItemValueRichText(doc,"Properties", properties);
			
			doc.save();
		} finally {
			doc.recycle();
		}
	}
	
	@Override
	protected int importAssets(ImportSource source, final AsyncAction action) throws Exception {
		if(StringUtil.equals(source.getSource(), "apidoc")) {
			String database=source.getLocation().trim();
			if(StringUtil.isEmpty(database)) {
				throw new FacesExceptionEx(null, "NSF Database path: Path is empty", source.getLocation());
			}
			return importAssetsNSF(source, action);
		} else {
			return super.importAssets(source, action);
		}
	}

	protected int importAssetsNSF(ImportSource source, AsyncAction action) throws Exception {
		Map<String,APIDocument> apiDocs = new HashMap<String, APIImporter.APIDocument>();

		RestClient client = createDominoClient(source.getLocation(),source.getUserName(),source.getPassword());
		
		List<DocEntry> list = loadEntries(client,source);
		if(action!=null&&action.isCancelled()) {
			return 0;
		}
		for(int i=0; i<list.size(); i++) {
			System.out.println("Importing #"+i+":"+list.get(i).toString());
			if(action!=null&&action.isCancelled()) {
				return 0;
			}
			importAssetsNSF(client, source, apiDocs, list.get(i),action);
		}

		for(Map.Entry<String, APIDocument> ed: apiDocs.entrySet()) {
			String id = Node.encodeUnid(ed.getValue().path); 
			String category = trimSeparator(extractCategory(ed.getValue().path));
			String name = trimSeparator(extractName(ed.getValue().path));
			String json = JsonGenerator.toJson(JsonJavaFactory.instanceEx, ed.getValue().content);
			String properties = createPropertiesAsString(ed.getValue().path);
			saveAsset(id, category, name, source.getName(), json, properties);
		}
		
		return apiDocs.size();
	}
	private String createPropertiesAsString(String path) throws IOException {
		Properties properties = new Properties();
		// For now, hard coded
		if(StringUtil.indexOfIgnoreCase(path,"domino")>=0) {
			properties.put("endpoint", "domino");
			properties.put("basedocurl", "http://www-10.lotus.com/ldd/ddwiki.nsf");
		} else if(StringUtil.indexOfIgnoreCase(path,"smartcloud")>=0) {
			properties.put("endpoint", "smartcloud");
		} else {
			properties.put("endpoint", "connections");
		}
		StringWriter sw = new StringWriter();
		properties.store(sw, null);
		return sw.toString();
	}
	private String extractCategory(String path) {
		int pos = path.lastIndexOf('/');
		if(pos>=0) {
			return path.substring(0,pos);
		}
		return "";
	}
	private String extractName(String path) {
		int pos = path.lastIndexOf('/');
		if(pos>=0) {
			return path.substring(pos+1);
		}
		return path;
	}
	private String trimSeparator(String path) {
		if(path.startsWith("/")) {
			path = path.substring(1);
		}
		if(path.endsWith("/")) {
			path = path.substring(0,path.length()-1);
		}
		return path;
	}

	protected void importAssetsNSF(RestClient client, ImportSource source, Map<String,APIDocument> apiDocs, DocEntry entry, AsyncAction action) throws Exception {
		JsonJavaObject doc = loadAPIDocument(client, entry);
		if(doc!=null) {
			if(action!=null) {
				action.updateTask("Importing: {0}", doc.get("Title"));
			}
			List mt = (List)doc.get("RequestsDetails");
			for(int i=0; i<mt.size(); i++) {
				String apiExplorerPath = doc.getAsString("APIExplorerPath");
				APIDocument apiDoc = apiDocs.get(apiExplorerPath);
				if(apiDoc==null) {
					apiDoc = new APIDocument(apiExplorerPath);
					apiDocs.put(apiExplorerPath, apiDoc);
				}
				JsonJavaObject je = createAPIEntry(doc, i, action);
				apiDoc.content.add(je);
			}
		}
	}
	protected JsonJavaObject createAPIEntry(JsonJavaObject e, int method, AsyncAction action) throws Exception {
		JsonJavaObject je = new JsonJavaObject();
		je.putString("name", e.get("Title"));
		je.putString("description", e.get("Abstract"));
		JsonJavaObject rd = (JsonJavaObject)((List)e.get("RequestsDetails")).get(method);
		je.putString("http_method", rd.get("method") );
		je.putString("uri", rd.get("uri") );
		Object upar = e.get("URLParameters");
		je.putObject("uriParameters", e.get("URLParameters") );
		je.putObject("queryParameters", e.get("QueryParameters") );
		je.putObject("headers", e.get("Headers") );
		return je;
	}

	private static class APIDocument {
		String path;
		List<Object> content;
		public APIDocument(String path) {
			this.path = path;
			this.content = new ArrayList<Object>();
		}
		public void add(JsonJavaObject o) {
			content.add(o);
		}
	}
	
	private static class DocEntry {
		String unid;
		String exPath;
		String title;
		public DocEntry(String unid, String exPath, String title) {
			this.unid = unid;
			this.exPath = exPath;
			this.title = title;
		}
		public String toString() {
			return StringUtil.format("Unid: {0}, path: {1}, title: {2}",unid,exPath,title);
		}
	}
	protected List<DocEntry> loadEntries(RestClient client, ImportSource source) throws IOException {
		ArrayList<DocEntry> list = new ArrayList<APIImporter.DocEntry>();
		try {
			String path = URLEncoding.encodeURIString("/api/data/collections/name/AllAPIExplorer","utf-8",0,false);
			Object json = client.get(path,new RestClient.HandlerJson(JsonJavaFactory.instance));
			if(json instanceof List) {
				for(Object entry: (List)json) {
					if(entry instanceof Map) {
						Map m = (Map)entry;
						String unid = (String)m.get("@unid");
						String exPath = (String)m.get("APIExplorerPath");
						String title = (String)m.get("Title");
						list.add(new DocEntry(unid,exPath,title));
					}
				}
			}
			return list;
		} catch(ClientServicesException ex) {
			throw new IOException(ex);
		}
	}
	protected JsonJavaObject loadAPIDocument(RestClient client, DocEntry entry) throws IOException {
		try {
			String path = URLEncoding.encodeURIString("/api/data/documents/unid/"+entry.unid,"utf-8",0,false);
			Object json = client.get(path,new RestClient.HandlerJson(JsonJavaFactory.instanceEx));
			if(json instanceof JsonJavaObject) {
				// Parse the fields that have to be parsed, as they are stored as strings
				JsonJavaObject o = (JsonJavaObject)json;
				parse(o,"URLParameters");
				parse(o,"QueryParameters");
				parse(o,"Headers");
				parse(o,"RequestsDetails");
				return o;
			}
			return null;
		} catch(ClientServicesException ex) {
			throw new IOException(ex);
		}
	}
	protected void parse(JsonJavaObject o, String fieldName) throws IOException {
		Object f = o.get(fieldName);
		if(f instanceof String) {
			try {
				Object parsed = JsonParser.fromJson(JsonJavaFactory.instanceEx, (String)f);
				o.put(fieldName,parsed);
			} catch(JsonException jex) {
				o.remove(fieldName);
			}
		}
	}
	
	protected DominoClient createDominoClient(String baseUrl, String userName, String password) {
		return new DominoClient(baseUrl,userName,password);
	}
	public static class DominoClient extends RestClient {
		public DominoClient(String baseUrl, String userName, String password) {
			super(baseUrl);
			setAuthenticator(new BasicAuthenticator(userName,password));
		}
		// Allow HTTPS, regardless of the certificates
		@Override
		protected boolean isForceTrustSSLCertificate() throws ClientServicesException {
			return true;
		}
	}
	
}
