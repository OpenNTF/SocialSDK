/*
 * © Copyright IBM Corp. 2010
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

package nsf.playground.jobs;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;
import nsf.playground.snippets.AssetImporter;

public class ImportAssetAction extends AsyncAction {

	public static final String ACTION_ID	= "ImportAsset";
	
	public static boolean cancel() {
		return cancel(ACTION_ID);
	}
	
	public static synchronized boolean start(String databaseName, String type, String sourceName) {
		if(!actionExists(ACTION_ID)) {
			registerAction(ACTION_ID, new ImportAssetAction());
		}
		return runAction(ACTION_ID, new String[]{databaseName,type,sourceName});
	}
	
	public ImportAssetAction() {
	}
	
    public String getActionLabel() {
    	return "Import asset";
    }
    
    public void run(Session session, Object parameters) throws NotesException {
    	try {
    		String databaseName = ((String[])parameters)[0];
    		String type = ((String[])parameters)[1];
    		String sourceName = ((String[])parameters)[2];
    		
    		Database db = session.getDatabase(null, databaseName);
    		try {
        		AssetImporter imp = AssetImporter.createImporter(type, db);
    			if(imp!=null) {
    				imp.importAssets(sourceName,this);
    			}
    		} finally {
    			db.recycle();
    		}
    	} catch(Throwable e) {
    		updateException(e);
    		e.printStackTrace();
    	}
    }
}
