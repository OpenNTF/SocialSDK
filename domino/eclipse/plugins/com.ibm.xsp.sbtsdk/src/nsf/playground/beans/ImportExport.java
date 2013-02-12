package nsf.playground.beans;

import java.io.Serializable;

import nsf.playground.jobs.ImportAssetAction;
import nsf.playground.snippets.AssetImporter;

import com.ibm.xsp.extlib.util.ExtLibUtil;

/**
 * Managed bean used to import/export assets.
 * 
 * @author priand
 */
public class ImportExport implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public ImportExport() {
	}
	
	public void importAssets(String type, String sourceName) throws Exception {
		importAssets(type, sourceName,false);
	}

	public void importAssets(String type, String sourceName, boolean async) throws Exception {
		// TEMP: Async is only supported on 9.0 because of an issue in 853
		if(ExtLibUtil.isXPages853()) {
			async = false;
		}
		if(async) {
			String dbName = ExtLibUtil.getCurrentDatabase().getFilePath();
			ImportAssetAction.start(dbName,type,sourceName);
		} else {
    		AssetImporter imp = AssetImporter.createImporter(type, ExtLibUtil.getCurrentDatabase());
			if(imp!=null) {
				imp.importAssets(sourceName,null);
			}
		}
	}

	public void deleteAssets(String type, String sourceName) throws Exception {
		AssetImporter imp = AssetImporter.createImporter(type, ExtLibUtil.getCurrentDatabase());
		imp.deleteAssets(sourceName);
	}
}
