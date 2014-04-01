package nsf.playground.playground;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import lotus.domino.NotesException;
import lotus.domino.View;
import nsf.playground.impexp.JsonImport;

import com.ibm.commons.util.io.StreamUtil;
import com.ibm.xsp.model.domino.DominoUtils;


public class PreviewImportHandler extends PreviewHandler {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("GET Upload");
	}
	
//	@Override
//	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		String key = req.getParameter("type");
//		System.out.println("POST Upload key="+key);
//	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if(isMultipart) {
			try {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = upload.parseRequest(req);
				String key = req.getParameter("type");
				for(FileItem item: items) {
					if(!item.isFormField()) {
						String fileName = item.getName();
						InputStream is = item.getInputStream();
						try {
							JsonImport imp = new JsonImport(new JsonImport.ZipImportSource(is));
							
							// Delete all the documents
							View view = DominoUtils.getCurrentDatabase().getView("AllSnippetsById");
							try {
								view.getAllEntriesByKey(key).removeAll(true);
							} finally {
								view.recycle();
							}
							
							// And import the new ones
							imp.importDocuments(DominoUtils.getCurrentDatabase());
							
							resp.setStatus(HttpServletResponse.SC_OK);
							resp.setContentType("text/html");
							String result = "<html><body><textarea>OK</textarea></body></html>";
							Writer w = resp.getWriter();
							w.write(result);
							w.flush();
						} finally {
							StreamUtil.close(is);
						}
					}
				}
			} catch(NotesException ex) {
				throw new IOException(ex);
			} catch(FileUploadException ex) {
				throw new IOException(ex);
			}
		}
	}

}
