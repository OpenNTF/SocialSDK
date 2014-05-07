package com.ibm.sbt.automation.core.test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.activity.Activity;
import com.ibm.sbt.services.client.connections.activity.ActivityService;
import com.ibm.sbt.services.client.connections.activity.ActivityServiceException;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileService;
import com.ibm.sbt.services.client.connections.files.model.FileCreationParameters;
import com.ibm.sbt.services.client.connections.forums.Forum;
import com.ibm.sbt.services.client.connections.forums.ForumService;

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
	
	/**The Activity Service Used to create activities  */
	private ActivityService activityService;
	
	/**The Id of the test activity */
	private String testActivityID;
	
	private ForumService forumService;
	
	private String testForumID;
	
	/**Constructor*/
	public BaseGridTestSetup(){
		
	}
	
	public void createForum(){
		try {
			loginConnections();
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		forumService = new ForumService();
		Forum forum = new Forum(forumService);
		forum.setTitle("Test forum 1ab" + System.currentTimeMillis());
		forum.setContent("Test forum created by Create Forum Java snippet");
		List<String> tags = new ArrayList<String>();
		tags.add("tag1"); 
		tags.add("tag2"); 
		forum.setTags(tags);
		try {
			forum = forum.save();
			testForumID = forum.getUid();
		} catch (ClientServicesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void deleteForum(){
		try {
			this.forumService.deleteForum(testForumID);
		} catch (ClientServicesException e) {
			e.printStackTrace();
		}
	}
	
	public void createBookmark(){
		
		try {
			loginConnections();
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		super.launchSnippet("Social_Bookmarks_API_CreateBookmark",AuthType.AUTO_DETECT);
		
	}
	
	public void deleteBookmark(){
		//TODO Implement when this is implemented in the Java API
	}
	
	public void createActivity(){
		try {
			loginConnections();
		} catch (AuthenticationException e1) {
			e1.printStackTrace();
		}
		try {
			this.activityService = new ActivityService();
			Activity activity = new Activity(activityService);
			activity.setTitle("JSPActivity" + System.currentTimeMillis());
			activity.setContent("GoalOfActivity - " + System.currentTimeMillis());
			List<String> tagList = new ArrayList<String>();
			tagList.add("tag1");
			tagList.add("tag2");
			activity.setTags(tagList);
			activity.setDueDate(new Date());
			activity = activityService.createActivity(activity);
			this.testActivityID = activity.getActivityId();
		} catch (ActivityServiceException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteActivity(){
		if(this.activityService ==  null){
			this.activityService = new ActivityService();
		}else if (this.activityService != null){
			try {
				this.activityService.deleteActivity(testActivityID);
				System.out.println("Activty Deleted");
			} catch (ActivityServiceException e) {
				e.printStackTrace();
			}
		}
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
	public Community createCommunity(String title, String type, String content, ArrayList<String> tags, boolean retry) {
        Community community = null;
        try {
    		CommunityService communityService = new CommunityService();
    		long start = System.currentTimeMillis();
     		community =  new Community(communityService, "");
     		community.setTitle("Test Community 1ab" + System.currentTimeMillis()); 
     		community.setContent("");
    		community.setCommunityType("public");
    		community.setTags(tags);
     		community = community.save(); 
     		long duration = System.currentTimeMillis() - start;
     		Trace.log("Created test community: "+community.getCommunityUuid() + " took "+duration+"(ms)");
	     	} catch (ClientServicesException e) {
	     		System.out.println("Grid Setup Error: cannot create community");

	     		fail("Error creating test community", e);
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
            } catch (ClientServicesException cse) {
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
		} catch (ClientServicesException e) {
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
		} catch (ClientServicesException cse) {
			fileEntry = null;
	        cse.printStackTrace();
			Assert.fail("Error creating test file: " + cse.getMessage());
		} catch (TransformerException te) {
			te.printStackTrace();
			Assert.fail("Error creating test file: " + te.getMessage());
		}
	}
	
	public void deleteFileAndQuit() {
		if (fileEntry != null) {
			try {
				fileService.deleteFile(fileEntry.getFileId());
			} catch (ClientServicesException cse) {
				fileEntry = null;
				if (failIfAfterDeletionFails()) {
					Assert.fail("Error deleting test file: " + cse.getMessage());
					cse.printStackTrace();
				}
			}
		}
		if (folder != null) {
			try {
				fileService.deleteFolder(folder.getFileId());
			} catch (ClientServicesException cse) {
				folder = null;
				if (failIfAfterDeletionFails()) {
					Assert.fail("Error deleting test folder: " + cse.getMessage());
					cse.printStackTrace();
				}
			}
		}

	}
	
	public void emptyTrash(){
		try {
			fileService.deleteAllFilesFromRecycleBin();
		} catch (ClientServicesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 protected void fail(String message, ClientServicesException cse) {
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
