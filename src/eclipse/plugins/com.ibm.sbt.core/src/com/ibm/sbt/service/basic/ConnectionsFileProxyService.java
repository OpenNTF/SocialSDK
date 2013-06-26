package com.ibm.sbt.service.basic;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Content;
import com.ibm.sbt.services.client.ClientService.ContentFile;
import com.ibm.sbt.services.client.ClientService.Handler;

/**
 * 
 * @author Vineet Kanwal
 *
 */
public class ConnectionsFileProxyService extends AbstractFileProxyService {

	private static final String UPLOAD_URL = "/files/basic/api/myuserlibrary/feed"; // TODO set as property on endpoint
	private static final String DOWNLOAD_URL = "/files/basic/api/library/{0}/document/{1}/media";
	
	@Override
	protected Content getFileContent(File file, long length, String name) {
		return new ContentFile(file);
	}

	@Override
	protected String getRequestURI(String method, String authType, Map<String, String[]> params) throws ServletException{
		if("POST".equalsIgnoreCase(method)){
			return UPLOAD_URL;
		}
		else if("GET".equalsIgnoreCase(method)){
			if (fileNameOrId == null) {
				throw new ServletException("File ID is required in URL for download.");
			}
			if (libraryId == null) {
				throw new ServletException("Library ID is required in URL for download");
			}
			return StringUtil.format(DOWNLOAD_URL, libraryId, fileNameOrId);
		}
		return null;
	}

	@Override
	protected Map<String, String> createHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Slug", fileNameOrId);
		return headers;
	}

	@Override
	protected Handler getFormat() {		
		return ClientService.FORMAT_XML;
	}

}
