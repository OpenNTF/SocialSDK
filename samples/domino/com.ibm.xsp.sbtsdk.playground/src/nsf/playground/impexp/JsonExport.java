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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaArray;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;



/**
 * Class for JSON export.
 * 
 * @author priand
 */
public class JsonExport extends JsonImportExport {
	
	public interface ExportTarget {
		public void startExport() throws IOException;
		public void endExport() throws IOException;
		public void startDocument(Document doc) throws IOException;
		public void endDocument() throws IOException;
		public void addItem(Item item) throws IOException;
	}
	
	public static class ZipExportTarget implements ExportTarget {
		private boolean compact;
		private boolean itemFlags;
		private ZipOutputStream zipOs;
		private Document document;
		private JsonJavaObject jsonDocument;
		public ZipExportTarget(OutputStream os, boolean compact, boolean itemFlags) throws IOException {
			this.zipOs = new ZipOutputStream(os);
			this.compact = compact;
			this.itemFlags = itemFlags; 
		}
		public void startExport() throws IOException {
		}
		public void endExport() throws IOException {
			zipOs.finish();
			zipOs = null;
		}
		public void startDocument(Document doc) throws IOException {
			document = doc;
			jsonDocument = new JsonJavaObject();
			if(itemFlags) {
				jsonDocument.put(FLAGS_FIELD, new JsonJavaObject());
			}
		}
		public void endDocument() throws IOException {
			try {
				String entryName = encodeFileName(getFileName());
				ZipEntry e = new ZipEntry(entryName);
				zipOs.putNextEntry(e);
				Writer w = new OutputStreamWriter(zipOs,"UTF-8");
				JsonGenerator.toJson(JsonJavaFactory.instanceEx2, w, jsonDocument, compact);
				w.flush();
				zipOs.closeEntry();
				document = null;
				jsonDocument = null;
			} catch(JsonException e) {
				throw new IOException(e);
			}
		}
		protected String getFileName() throws IOException {
			try {
				String fileName = document.getUniversalID();
				String form = document.getItemValueString("form");
				if(StringUtil.isNotEmpty(form)) {
					fileName = form + '/' + fileName;
				}
				return fileName+JsonImportExport.DOCUMENT_EXTENSION;
			} catch(NotesException e) {
				throw new IOException(e);
			}
		}
		public void addItem(Item item) throws IOException {
			try {
				Vector<?> values = item.getValues();
				if(values==null || values.size()==0) {
					return;
				}
				if(values.size()>1) {
					JsonJavaArray array = new JsonJavaArray();
					for(int i=0; i<values.size(); i++) {
						array.add(asJsonValue(values.get(i)));
					}
					jsonDocument.put(item.getName(),array);
				} else {
					jsonDocument.put(item.getName(),asJsonValue(values.get(0)));
				}
				if(itemFlags) {
					String flags = getFlags(item);
					if(StringUtil.isNotEmpty(flags)) {
						((JsonJavaObject)jsonDocument.get(FLAGS_FIELD)).put(item.getName(),flags);
					}
				}
			} catch(NotesException e) {
				throw new IOException(e);
			}
		}
		private String getFlags(Item item) throws IOException, NotesException {
			String flags = null;
			if(item.isNames()) {
				flags = addFlag(flags, FLAGS_NAMES);
			}
			if(item.isReaders()) {
				flags = addFlag(flags, FLAGS_READERS);
			}
			if(item.isAuthors()) {
				flags = addFlag(flags, FLAGS_AUTHORS);
			}
			return flags;
		}
		private String addFlag(String flags, String flag) {
			if(StringUtil.isNotEmpty(flags)) {
				return flags + ',' + flag;
			}
			return flag;
		}
		private Object asJsonValue(Object v) throws IOException, NotesException {
			if(v==null) {
				return null;
			} else if(v instanceof Number) {
				return v;
			} else if(v instanceof DateTime) {
				Date dt = ((DateTime)v).toJavaDate(); 
				return JsonGenerator.dateToString(dt);
			} else if(v instanceof String) {
				return v;
			} else {
				throw new IOException("Invalid Domino type: "+v.getClass());
			}
		}
	}
	
	private ExportTarget target;
	
	public JsonExport(ExportTarget target) {
		this.target = target;
	}

	public void exportDocuments(Database database) throws IOException, NotesException {
		target.startExport();
		try {
			DocumentCollection docs = database.getAllDocuments();
			try {
				for(Document doc=docs.getFirstDocument(); doc!=null; doc=docs.getNextDocument(doc)) {
					exportDocument(doc);
				}
			} finally {
				docs.recycle();
			}
		} finally {
			target.endExport();
		}
	}

	public void exportDocuments(View view) throws IOException, NotesException {
		exportDocuments(view,null);
	}
	public void exportDocuments(View view, Object keys) throws IOException, NotesException {
		target.startExport();
		try {
			ViewEntryCollection entries = keys!=null ? view.getAllEntriesByKey(keys) : view.getAllEntries();
			try {
				for(ViewEntry entry=entries.getFirstEntry(); entry!=null; entry=entries.getNextEntry(entry)) {
					Document doc = entry.getDocument();
					try {
						exportDocument(doc);
					} finally {
						doc.recycle();
					}
				}
			} finally {
				entries.recycle();
			}
		} finally {
			target.endExport();
		}
	}

	protected void exportDocument(Document doc) throws IOException, NotesException {
		if(getDocumentFilter()!=null && !getDocumentFilter().accept(doc)) {
			return;
		}
		target.startDocument(doc);
		Vector<?> items = doc.getItems();
		for(int i=0; i<items.size(); i++) {
			Item item = (Item)items.get(i);
			exportItem(item);
		}
		target.endDocument();
	}
	
	protected void exportItem(Item item) throws IOException, NotesException {
		if(getItemFilter()!=null && !getItemFilter().accept(item)) {
			return;
		}
		target.addItem(item);
	}	
}
