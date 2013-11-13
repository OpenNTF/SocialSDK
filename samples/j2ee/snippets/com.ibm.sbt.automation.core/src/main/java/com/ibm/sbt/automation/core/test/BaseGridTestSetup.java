package com.ibm.sbt.automation.core.test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileService;
import com.ibm.sbt.services.client.connections.files.FileServiceException;
import com.ibm.sbt.services.client.connections.files.model.FileCreationParameters;

public class BaseGridTestSetup extends BaseApiTest{
	
	/**The FileService for performing File related methods.*/
	protected FileService fileService;
	
	/**fileEntry represents a Test File That will be created */
	protected File fileEntry;
	
	/**folder represents a folder which will be created for testing*/
	protected File folder;
	
	/**Fail the Test if the deletion fails */
	private boolean failIfAfterDeletionFails = true;
	
	/**the Community Service is used to perform community related actions */
	private CommunityService communityService;
	
	/**the Id of the community that is created by the createCommunity Method
	 * this is stored so that the community can be deleted later. */
	private String testCommunityID;
	
	/**Constructor*/
	public BaseGridTestSetup(){
		
	}
	
	/**
	 * create a community for testing
	 * @param title
	 * @param type
	 * @param content
	 * @param tags
	 * @param retry
	 * @return
	 */
	public Community createCommunity(String title, String type, String content, String tags, boolean retry) {
        Community community = null;
        try {
            loginConnections();
            CommunityService communityService = getCommunityService();
            
        	long start = System.currentTimeMillis();
            community = new Community(communityService, "");
            community.setTitle(title+start);
            community.setCommunityType(type);
            community.setContent(content);
            community.setTags(tags);
            String communityUuid = communityService.createCommunity(community);
            testCommunityID = communityUuid;
            
            community = communityService.getCommunity(communityUuid);
            
            long duration = System.currentTimeMillis() - start;
            Trace.log("Created test community: "+communityUuid + " took "+duration+"(ms)");
        } catch (AuthenticationException pe) {
        	if (pe.getCause() != null) {
        		pe.getCause().printStackTrace();
        	}
            Assert.fail("Error authenicating: " + pe.getMessage());
        } catch (CommunityServiceException cse) {
        	// TODO remove this when we upgrade the QSI
        	Throwable t = cse.getCause();
        	if (t instanceof ClientServicesException) {
        		ClientServicesException csex = (ClientServicesException)t;
        		int statusCode = csex.getResponseStatusCode();
        		if (statusCode == 500 && retry) {
        			return createCommunity(title, type, content, tags, false);
        		}
        	}
            fail("Error creating test community", cse);
        } 
        
        return community;
    }
	
	/**
	 * Delete the community that was created by the create community method.
	 */
	public void deleteCommunity() {
        if (testCommunityID != null) {
            try {
            	loginConnections();
                CommunityService communityService = getCommunityService();
                communityService.deleteCommunity(testCommunityID);
            } catch (AuthenticationException pe) {
            	if (pe.getCause() != null) {
            		pe.getCause().printStackTrace();
            	}
                Assert.fail("Error authenicating: " + pe.getMessage());
            } catch (CommunityServiceException cse) {
                fail("Error deleting community "+testCommunityID, cse);
            }
        }
    }
	
	protected CommunityService getCommunityService() {
        if (communityService == null) {
            communityService = new CommunityService(getEndpointName());
        }
        return communityService;
    }
	
	protected void setFailIfAfterDeletionFails(boolean failIfAfterDeletionFails) {
		this.failIfAfterDeletionFails = failIfAfterDeletionFails;
	}
	
	protected FileService getFileService() {
		try {
			loginConnections();
		} catch (AuthenticationException e) {
			Assert.fail("Error logging in to Connections " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		

		if (fileService == null) {
			fileService = new FileService(getEndpointName());
		}
		return fileService;
	}
	
	public void createFolder() {
		setFailIfAfterDeletionFails(true);
		fileService = getFileService();
		try {
			folder = fileService.createFolder("TestFolder");		
			fileService.pinFolder(folder.getFileId());
			Trace.log("Created test folder: " + folder.getFileId());			
		} catch (FileServiceException e) {
			e.printStackTrace();
			Assert.fail("Error creating test folder: " + e.getMessage());
		} catch (TransformerException te) {
			te.printStackTrace();
			Assert.fail("Error creating test folder: " + te.getMessage());
		}
	}

	public void createFile() {

		try {
			setFailIfAfterDeletionFails(true);
			fileService = getFileService();
			String content = "Content uploaded by Grid Setup";
			String id = "File" + System.currentTimeMillis() + ".txt";

			
			FileCreationParameters p = new FileCreationParameters();
			p.visibility = FileCreationParameters.Visibility.PUBLIC;
			p.tags = new ArrayList<String>();
			p.tags.add("text");
			Map<String, String> params = p.buildParameters();			
			
			fileEntry = fileService.uploadFile(new ByteArrayInputStream(content.getBytes()), id, content.length(), params);
			
			//delete the file and folder so there are items in the "trash"
			deleteFileAndQuit();
			
			//recreate the folder and files 
			createFolder();
			fileEntry = fileService.uploadFile(new ByteArrayInputStream(content.getBytes()), id, content.length(), params);

			params = new HashMap<String, String>();
			fileService.addCommentToFile(fileEntry.getFileId(), "Comment added by Grid Setup", params);
			fileService.pinFile(fileEntry.getFileId());
			if(folder != null){
				fileService.addFileToFolder(fileEntry.getFileId(), folder.getFileId());
			}else{
				createFolder();
				fileService.addFileToFolder(fileEntry.getFileId(), folder.getFileId());
			}
			
			
			Trace.log("Created test file: " + fileEntry.getFileId());
		} catch (FileServiceException fse) {
			fileEntry = null;
	        fse.printStackTrace();
			Assert.fail("Error creating test file: " + fse.getMessage());
		} catch (TransformerException te) {
			te.printStackTrace();
			Assert.fail("Error creating test file: " + te.getMessage());
		}
	}
	
	public void deleteFileAndQuit() {
		if (fileEntry != null) {
			try {
				fileService.deleteFile(fileEntry.getFileId());
			} catch (FileServiceException fse) {
				fileEntry = null;
				if (failIfAfterDeletionFails()) {
					Assert.fail("Error deleting test file: " + fse.getMessage());
					fse.printStackTrace();
				}
			}
		}
		if (folder != null) {
			try {
				fileService.deleteFolder(folder.getFileId());
			} catch (FileServiceException fse) {
				folder = null;
				if (failIfAfterDeletionFails()) {
					Assert.fail("Error deleting test folder: " + fse.getMessage());
					fse.printStackTrace();
				}
			}
		}

	}
	
	public void emptyTrash(){
		try {
			fileService.deleteAllFilesFromRecycleBin();
		} catch (FileServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 protected void fail(String message, CommunityServiceException cse) {
	    	String failure = message;
	    	
	    	Throwable cause = cse.getCause();
	    	if (cause != null) {
	    		cause.printStackTrace();
	    		failure += ", " + cause.getMessage();
	    	} else {
	    		cse.printStackTrace();
	    		failure += ", " + cse.getMessage();
	    	}
	    	
	    	Assert.fail(failure);
	    }
	
	protected boolean failIfAfterDeletionFails() {
		return failIfAfterDeletionFails;
	}
	
}
