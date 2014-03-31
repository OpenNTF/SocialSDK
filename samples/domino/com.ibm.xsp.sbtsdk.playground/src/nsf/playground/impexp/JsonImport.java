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

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;

import com.ibm.commons.util.io.json.JsonJavaObject;



/**
 * Class for JSON import.
 * 
 * @author priand
 */
public class JsonImport extends JsonImportExport {
	
	public interface ImportSource {
		public void startImport() throws IOException;
		public void endImport() throws IOException;
		public JsonJavaObject next() throws IOException;
	}
	
	private ImportSource source;
	
	public JsonImport(ImportSource source) {
		this.source = source;
	}
	
	public void importDocuments(Database database) throws IOException {
		source.startImport();
		try {
			for( JsonJavaObject jsDoc=source.next(); jsDoc!=null; jsDoc=source.next() ) {
				Document doc = database.createDocument();
				try {
					importDocument(doc,jsDoc);
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

	protected void importDocument(Document doc, JsonJavaObject jsDoc) throws IOException, NotesException {
		
	}
}
