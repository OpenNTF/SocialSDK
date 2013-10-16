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
	private static final String UPDATE_URL = "/files/basic/api/myuserlibrary/document/{0}/media";

	private static final String X_UPDATE_NONCE = "x-update-nonce";

	public static final String FILEPROXYNAME = "connections";
	
	@Override
	protected Content getFileContent(File file, long length, String contentType) {
		Content content;
		content = new ContentFile(file, contentType);
		return content;
	}


	@Override
	protected String getRequestURI(String method, String authType, Map<String, String[]> params) throws ServletException {
		if ("POST".equalsIgnoreCase(method)) {
			return UPLOAD_URL;
		} else if ("PUT".equalsIgnoreCase(method) && Operations.UPDATE_PROFILE_PHOTO.toString().equals(operation)) {
			// return PROFILE_PIC_UPLOAD_URL;
			StringBuilder profileUrl = new StringBuilder("/profiles");
			if (AuthUtil.INSTANCE.getAuthValue(endpoint).equalsIgnoreCase("oauth")) {
				profileUrl.append("/").append("oauth");
			}
			profileUrl.append("/").append("photo.do");
			return profileUrl.toString();
		} else if ("PUT".equalsIgnoreCase(method) && Operations.UPLOAD_NEW_VERSION.toString().equals(operation)) {
			String fileId = parameters.get("FileId");
			if (fileId == null) {
				throw new ServletException("File ID is required in URL for upload new version.");
			}
			return StringUtil.format(UPDATE_URL,fileId);
		} else if ("GET".equalsIgnoreCase(method)) {
			String fileId = parameters.get("FileId");
			String libraryId = parameters.get("LibraryId");
			if (fileId == null) {
				throw new ServletException("File ID is required in URL for download.");
			}
			if (libraryId == null) {
				throw new ServletException("Library ID is required in URL for download");
			}
			return StringUtil.format(DOWNLOAD_URL, libraryId, fileId);
		}
		return null;
	}

	@Override
	protected Map<String, String> createHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(X_UPDATE_NONCE, "");
		return headers;
	}

	@Override
	protected Handler getFormat() {
		if ("PUT".equalsIgnoreCase(method) && Operations.UPDATE_PROFILE_PHOTO.toString().equals(operation)) {
			return ClientService.FORMAT_NULL;
		}
		return ClientService.FORMAT_XML;
	}

	@Override
	protected void getParameters(String[] tokens) throws ServletException {
		if (operation == null) {
			return;
		}
		if (Operations.UPLOAD_FILE.toString().equals(operation)) {
			if (tokens.length < 6) {
				throw new ServletException("Invalid url for File Download");
			}
			parameters.put("FileName", tokens[5]);
		} else if (Operations.DOWNLOAD_FILE.toString().equals(operation)) {
			if (tokens.length < 7) {
				throw new ServletException("Invalid url for File Download");
			}
			parameters.put("FileId", tokens[5]);
			parameters.put("LibraryId", tokens[6]);
		} else if (Operations.UPDATE_PROFILE_PHOTO.toString().equals(operation)) {
			parameters.put("FileName", tokens[5]);
		} else if (Operations.UPLOAD_NEW_VERSION.toString().equals(operation)) {
			if (tokens.length < 7) {
				throw new ServletException("Invalid url for Upload New Version of File");
			}
			parameters.put("FileId", tokens[5]);
			parameters.put("FileName", tokens[6]);
		}

	}

}
