package nsf.playground.beans;

import java.io.Serializable;

import nsf.playground.jobs.ImportSnippetAction;
import nsf.playground.snippets.SnippetImporter;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class ImportSources implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public ImportSources() {
	}
	
	public void importSnippets(String sourceName) throws Exception {
		importSnippets(sourceName,false);
	}

	public void importSnippets(String sourceName, boolean async) throws Exception {
		// TEMP: Async is only supported on 9.0 because of an issue in 853
		if(ExtLibUtil.isXPages853()) {
			async = false;
		}
		if(async) {
			String dbName = ExtLibUtil.getCurrentDatabase().getFilePath();
			ImportSnippetAction.start(dbName,sourceName);
		} else {
			SnippetImporter imp = new SnippetImporter(ExtLibUtil.getCurrentDatabase());
			imp.importSnippets(sourceName,null);
		}
	}

	public void deleteSnippets(String sourceName) throws Exception {
		SnippetImporter imp = new SnippetImporter(ExtLibUtil.getCurrentDatabase());
		imp.deleteSnippets(sourceName);
	}
}
