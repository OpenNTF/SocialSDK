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
import com.ibm.sbt.services.util.AuthUtil;

/**
 * 
 * @author Vineet Kanwal
 * 
 */
public class ConnectionsFileProxyService extends AbstractFileProxyService {

	private static final String UPLOAD_URL = "/files/basic/api/myuserlibrary/feed"; // TODO set as property on endpoint
	private static final String DOWNLOAD_URL = "/files/basic/api/library/{0}/document/{1}/media";

	public static final String FILEPROXYNAME = "connections";
	
	@Override
	protected Content getFileContent(File file, long length, String name) {
		Content content;
		String contentType = null;
		int dot = fileNameOrId.lastIndexOf('.');
		String ext = null;
		if (dot > -1) {
			ext = fileNameOrId.substring(dot + 1); // add one for the dot!
		}
		if (!StringUtil.isEmpty(ext)) {
			if (ext.equalsIgnoreCase("jpg")) {
				contentType = "image/jpeg"; // content-type should be image/jpeg for file extension jpg
			} else {
				contentType = "image/" + ext;
			}
		}
		content = new ContentFile(file, contentType);
		return content;
	}

	@Override
	protected String getRequestURI(String method, String authType, Map<String, String[]> params) throws ServletException {
		if ("POST".equalsIgnoreCase(method)) {
			return UPLOAD_URL;
		} else if ("PUT".equalsIgnoreCase(method)) {
			// return PROFILE_PIC_UPLOAD_URL;
			StringBuilder profileUrl = new StringBuilder("/profiles");
			if (AuthUtil.INSTANCE.getAuthValue(endpoint).equalsIgnoreCase("oauth")) {
				profileUrl.append("/").append("oauth");
			}
			profileUrl.append("/").append("photo.do");
			return profileUrl.toString();
		} else if ("GET".equalsIgnoreCase(method)) {
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
		return new HashMap<String, String>();
	}

	@Override
	protected Handler getFormat() {
		if ("PUT".equalsIgnoreCase(method)) {
			return ClientService.FORMAT_NULL;
		}
		return ClientService.FORMAT_XML;
	}

}
