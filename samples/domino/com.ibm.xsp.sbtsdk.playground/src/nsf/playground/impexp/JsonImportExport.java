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

import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;



/**
 * Base class for JSON import/export capabilities.
 * 
 * @author priand
 */
public abstract class JsonImportExport {
	
	public interface DocumentFilter {
		public boolean accept(Document doc) throws NotesException;
	}

	public interface ItemFilter {
		public boolean accept(Item item) throws NotesException;
	}

	private DocumentFilter documentFilter;
	private ItemFilter itemFilter;
	
	public JsonImportExport() {
	}
	
	public DocumentFilter getDocumentFilter() {
		return documentFilter;
	}

	public void setDocumentFilter(DocumentFilter documentFilter) {
		this.documentFilter=documentFilter;
	}

	public ItemFilter getItemFilter() {
		return itemFilter;
	}

	public void setItemFilter(ItemFilter itemFilter) {
		this.itemFilter=itemFilter;
	}
	
	public static String encodeFileName(String name) {
		return name;
	}

	public static String decodeFileName(String name) {
		return name;
	}
}
