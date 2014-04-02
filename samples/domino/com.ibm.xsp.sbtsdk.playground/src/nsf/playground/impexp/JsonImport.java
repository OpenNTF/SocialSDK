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

package nsf.playground.impexp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.Session;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaArray;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;



/**
 * Class for JSON import.
 * 
 * @author priand
 */
public class JsonImport extends JsonImportExport {
	
	public static class Entry {
		private String unid;
		private JsonJavaObject object;
		public Entry(String unid, JsonJavaObject object) {
			this.unid = unid;
			this.object = object;
		}
		public String getUnid() {
			return unid;
		}
		public JsonJavaObject getJsonObject() {
			return object;
		}
	}
	
	public interface ImportSource {
		public void startImport() throws IOException;
		public void endImport() throws IOException;
		public Entry next() throws IOException;
	}
	
	public static class ZipImportSource implements ImportSource {
		private ZipInputStream zipIs;
		public ZipImportSource(InputStream is) throws IOException {
			this.zipIs = new ZipInputStream(is);
		}
		public void startImport() throws IOException {
			
		}
		public void endImport() throws IOException {
		}
		
		public Entry next() throws IOException {
			while(zipIs!=null) {
				ZipEntry e = zipIs.getNextEntry();
				if(e==null) {
					zipIs = null;
					break;
				}
				String name = e.getName();
				if(name.endsWith(JsonImportExport.DOCUMENT_EXTENSION)) {
					String unid = decodeFileName(name.substring(0,name.length()-JsonImportExport.DOCUMENT_EXTENSION.length()));
					int pos = unid.lastIndexOf('/');
					if(pos>0) {
						unid = unid.substring(pos+1);
					}
					try {
						JsonJavaObject o = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx2, new InputStreamReader(zipIs,"UTF-8"));
						return new Entry(unid,o);
					} catch (JsonException ex) {
						throw new IOException(ex);
					}
				}
			}
			return null;
		}
	}
	
	private ImportSource source;
	
	public JsonImport(ImportSource source) {
		this.source = source;
	}
	
	public void importDocuments(Database database) throws IOException {
		source.startImport();
		try {
			Session session = database.getParent();
			for( Entry e=source.next(); e!=null; e=source.next() ) {
				Document doc = database.createDocument();
				try {
					doc.setUniversalID(e.getUnid());
					importDocument(session,doc,e.getJsonObject());
					if(getDocumentFilter()==null || getDocumentFilter().accept(doc)) {
						doc.save();
					}
				} finally {
					doc.recycle();
				}
			}
		} catch(NotesException ex) {
			throw new IOException(ex);
		} finally {
			source.endImport();
		}
	}

	protected void importDocument(Session session, Document doc, JsonJavaObject jsDoc) throws IOException, NotesException {
		Map<String,Object> allFlags = (Map<String,Object>)jsDoc.get(FLAGS_FIELD);
		for(Map.Entry<String, Object> e: jsDoc.entrySet()) {
			String k = e.getKey();
			// Ignore the flag field
			if(StringUtil.equals(k, FLAGS_FIELD)) {
				continue;
			}
			// Read the flags for this field
			String[] flags = null;
			if(allFlags!=null) {
				String s = (String)allFlags.get(k);
				if(StringUtil.isNotEmpty(s)) {
					flags = StringUtil.splitString(s, ',');
				}
			}
			// Then convert the field
			Object v = toNotesObject(session,e.getValue(),flags);
			Item item = doc.replaceItemValue(k, v);
			if(getItemFilter()!=null && !getItemFilter().accept(item)) {
				item.remove();
			} else {
				processItem(session, doc, item, flags);
			}
		}
	}
	protected Object toNotesObject(Session session, Object jsonObject, String[] flags) throws IOException, NotesException {
		if(jsonObject==null) {
			return null;
		} else if(jsonObject instanceof List<?>) {
			List<?> jsonArray = (List<?>)jsonObject;
			Vector<Object> v = new Vector<Object>();
			for(int i=0; i<jsonArray.size(); i++) {
				v.add(toNotesObject(session,jsonArray.get(i),flags));
			}
			return v;
		} else if(jsonObject instanceof String) {
			String s = (String)jsonObject;
			// 2013-06-17T14:31:44
			if(s.length()==19 && s.charAt(4)=='-' && s.charAt(7)=='-'&& s.charAt(10)=='T' && s.charAt(13)==':' && s.charAt(16)==':') {
				try {
					Date dt = JsonGenerator.stringToDate(s);
					return session.createDateTime(dt);
				} catch (ParseException e) {
					// This is then a simple string...
				}
			}
			return jsonObject;
		} else if(jsonObject instanceof Number) {
			return jsonObject;
		} else {
			throw new IOException("Invalid Json type "+jsonObject.getClass());
		}
	}
	protected Item processItem(Session session, Document document, Item item, String[] flags) throws IOException, NotesException {
		if(flags!=null) {
			if(StringUtil.contains(flags, FLAGS_NAMES)) {
				item.setNames(true);
			}
			if(StringUtil.contains(flags, FLAGS_READERS)) {
				item.setReaders(true);
			}
			if(StringUtil.contains(flags, FLAGS_AUTHORS)) {
				item.setAuthors(true);
			}
		}
		return item;
	}
}
