package nsf.playground.playground;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.View;

import com.ibm.xsp.model.domino.DominoUtils;

import nsf.playground.impexp.JsonExport;
import nsf.playground.impexp.JsonExport.ExportTarget;
import nsf.playground.impexp.JsonImportExport;


public class PreviewExportHandler extends PreviewHandler {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String key = req.getParameter("type");
		
		try {
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/zip");
			OutputStream os = resp.getOutputStream();
			try {
				ExportTarget tgt = new JsonExport.ZipExportTarget(os,false);
				JsonExport exp = new JsonExport(tgt);
				exp.setItemFilter(new JsonImportExport.ItemFilter() {
					@Override
					public boolean accept(Item item) throws NotesException {
						String name = item.getName();
						// We need this field to keep the history!
						//if(name.equalsIgnoreCase("$UpdatedBy")) {
						//	return false;
						//}
						return true;
					}
				});
				View view = DominoUtils.getCurrentDatabase().getView("AllSnippetsById");
				try {
					exp.exportDocuments(view,key);
				} finally {
					view.recycle();
				}
			} finally {
				os.flush();
			}
		} catch(NotesException ex) {
			throw new IOException(ex);
		}
	}

}
