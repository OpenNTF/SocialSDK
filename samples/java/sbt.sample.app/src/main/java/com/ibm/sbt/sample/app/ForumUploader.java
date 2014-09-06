package com.ibm.sbt.sample.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.forums.Forum;
import com.ibm.sbt.services.client.connections.forums.ForumFileField;
import com.ibm.sbt.services.client.connections.forums.ForumReply;
import com.ibm.sbt.services.client.connections.forums.ForumService;
import com.ibm.sbt.services.client.connections.forums.ForumTopic;
import com.ibm.sbt.services.client.connections.forums.ForumUrls;
import com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity;
import com.ibm.sbt.services.client.connections.forums.serializers.ForumSerializer;
import com.ibm.sbt.services.endpoints.BasicEndpoint;

/**
 * @author Francis
 */
@SuppressWarnings("deprecation")
//needs to use deprecated classes because addPart is not public in MultipartEntityBuilder, a bug fixed in the apache trunk.
public class ForumUploader {

	public static void main(String[] args) {
		if(args.length < 4){
			System.out.println("Not enough arguments");
			System.out.println("Usage: connections_url forumId forumDirectory username password");
			System.out.println("Where: \n\nconnections_url is the url of connections or smartcloud");
			System.out.println("forumId is the id of the forum to upload to");
			System.out.println("forumDirectory is the directory you exported a forum to with forumExporter");
			System.out.println("username and password are the credentials of someone with access to forumId on connections_url");
			return;
		}
		String connectionsUrl = args[0], 
				forumId = args[1],
				forumDirectory = args[2],
				username = args[3],
				password = args[4];
		
		ForumUploader forumUploader = null;
		try {
			long start = System.currentTimeMillis();
			
			File forumDirectoryFile = new File(forumDirectory);
	    	if(!forumDirectoryFile.exists() ){
	    		throw new FileNotFoundException("Could not find directory " + forumDirectory);
	    	}
			forumUploader = new ForumUploader(connectionsUrl, username, password);
			forumUploader.uploadForum(forumId, forumDirectoryFile);
			
			double duration = (System.currentTimeMillis() - start) / 1000d;
	        System.out.println("Took: " + duration + " seconds");
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLException e) {
			e.printStackTrace();
		}
	}

	private ForumService forumService;
	private File forumDirectoryFile;
	private SimpleDateFormat dateFormat;
	//private static final FilenameFilter FORUM_FILTER = new PrefixFileFilter("forum_");
	private static final FilenameFilter TOPIC_FILTER = new PrefixFileFilter("topics_");
	private List<String> extraTags = new ArrayList<String>();
	//Store the mapping between the old topic uuid and the new one, to update reply-to fields.
	private Map<String, String> topicRepliesMap = new HashMap<String, String>();
	
	/**
     * 
     * @param url
     * @param user
     * @param password
     * @throws AuthenticationException
     */
    public ForumUploader(String url, String user, String password)
            throws AuthenticationException {
        BasicEndpoint basicEndpoint = new BasicEndpoint();
        basicEndpoint.setUrl(url);
        basicEndpoint.setForceTrustSSLCertificate(true);
        basicEndpoint.login(user,  password);
        
        forumService = new ForumService();
        forumService.setEndpoint(basicEndpoint);
        extraTags.add("author");
        extraTags.add("modifier");
        extraTags.add("uploaded");
		
		dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
    }
    
    /**
     * Upload a forum which was exported to the forumDirectory directory. 
     * 
     * @see com.ibm.sbt.sample.app.ForumExporter#exportForum(String, String)
     * @param forumDirectory Directory of the exported forum.
     * @param connectionsUrl Connections to upload to.
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws SAXException 
     * @throws XMLException 
     */
    public String uploadForum(String forumId, File forumDirectoryFile) throws FileNotFoundException, XMLException{
    	try {
			Forum forum = forumService.getForum(forumId);
			if("deleted".equals(forum.getModeration())){
				System.out.println("Can't upload, the forum with id " + forumId + " has been deleted.");
				System.exit(0);
			}
			System.out.println("Found forum with title: " + forum.getTitle());
			System.out.println("Do you want to upload to this forum? y/n");
			String answer = Character.toString((char) System.in.read());
			if(answer.equalsIgnoreCase("y")){
				setForumDirectoryFile(forumDirectoryFile);
		    	postTopics(forumId);
		    	
		    	return forumId;
			}else{
				return "";
			}
		} catch (ClientServicesException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
    }
    
    public String postForum(File forumFile) throws FileNotFoundException, ClientServicesException, XMLException{
    	IFeedHandler<Forum> forumFeedHandler = forumService.getForumFeedHandler(false);
		Forum forum = forumFeedHandler.createEntity(getXmlResponse(forumFile));
		
		return forumService.createForum(forum).getForumUuid();
    }
    
    public void postTopics(String forumId) throws FileNotFoundException, XMLException {
    	File[] topicFiles = getForumDirectoryFile().listFiles(TOPIC_FILTER);
    	IFeedHandler<ForumTopic> topicFeedHandler = forumService.getForumTopicFeedHandler(false);
    	System.out.println(topicFiles.length + " topic feeds found.");
    	for(int i = topicFiles.length - 1; i > -1 ; i--){
    		EntityList<ForumTopic> topics = topicFeedHandler.createEntityList(getXmlResponse(topicFiles[i]));
    		System.out.println(topics.size() + " topics in feed " + (topicFiles.length - i));
    		for(int j = topics.size() - 1; j > -1 ; j--){
        		ForumTopic topic = topics.get(j);
        		String oldTopicUuid = topic.getTopicUuid();
        		List<ForumFileField> fileFields = topic.getFileFields();
        		String topicUuid = "";
        		if(fileFields.size() > 0){
        			try {
						topicUuid = postTopicWithAttachments(forumId, topic, getDownloadedFiles(fileFields));
					} catch (ClientServicesException e) {
						System.err.println("failed to upload topic with original id: " + oldTopicUuid);
						System.err.println(e.getMessage());
						continue;
					}
        		}else{
        			try {
						topicUuid = postTopic(forumId, topic);
					} catch (FileNotFoundException e) {
						System.err.println("failed to upload topic with original id: " + oldTopicUuid);
						System.err.println(e.getMessage());
						continue;
					} catch (ClientServicesException e) {
						System.err.println("failed to upload topic with original id: " + oldTopicUuid);
						System.err.println(e.getMessage());
						continue;
					}
        		}
        		topicRepliesMap.clear();
    			FilenameFilter repliesfilter = new PrefixFileFilter("replies_" + oldTopicUuid);
    			File[] replyFeeds = getForumDirectoryFile().listFiles(repliesfilter);
    			if(replyFeeds.length != 0){
    				try {
						postTopicReplies(forumId, topicUuid, replyFeeds);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (ClientServicesException e) {
						e.printStackTrace();
					} catch (XMLException e) {
						e.printStackTrace();
					}
    			}
    			System.out.println("Finished uploading topic " + (topics.size() - j) + " of " + topics.size() + " in feed " + (topicFiles.length - i));
        	}
    	}
    }
    
    public String postTopic(String forumId, ForumTopic topic) throws FileNotFoundException, ClientServicesException{
		topic.setForumUuid(forumId);
		topic.setTitle(getDecoratedTitle(topic));
		List<String> tags = new ArrayList<String>(topic.getBaseTags());
		tags.addAll(extraTags);
		topic.setBaseTags(tags);
		
		return forumService.createForumTopic(topic, forumId).getTopicUuid();
	}
    
    private String getDecoratedTitle(BaseForumEntity forumEntity){
    	return forumEntity.getTitle() + "; [Created: " + dateFormat.format(forumEntity.getPublished()) + ", " + " by " + forumEntity.getAuthor().getName() + "]";
    }

	public void postTopicReplies(String forumId, String topicId, File[] topicReplyFiles) throws FileNotFoundException, ClientServicesException, XMLException{
		IFeedHandler<ForumReply> replyFeedHandler = forumService.getForumReplyFeedHandler(true);
		EntityList<ForumReply> replies = null;
		for(int i = topicReplyFiles.length - 1; i > -1 ; i--){
			File topicReplyFile = topicReplyFiles[i];
			replies = replyFeedHandler.createEntityList(getXmlResponse(topicReplyFile));
			for(int j = replies.size() - 1; j > -1 ; j--){
				ForumReply reply = replies.get(j);
				List<ForumFileField> fileFields = reply.getFileFields();
				String newReplyUuid = "";
	    		if(fileFields.size() > 0){
	    			newReplyUuid = postTopicReplyWithAttachments(topicId, reply, getDownloadedFiles(fileFields));
	    		}else{
	    			newReplyUuid = postTopicReply(topicId, reply);
	    		}
	    		topicRepliesMap.put(reply.getUid(), newReplyUuid);
			}
		}
		
	}

	public String postTopicReply(String topicUuid, ForumReply reply) throws ClientServicesException{
		reply.setTopicUuid(topicUuid);
		String replyMapping = topicRepliesMap.get(reply.getReplyToPostUuid());
		// If the id that this is a reply to is in our map of replies, then it is replying to a reply. 
		// Otherwise it's replying tothe topic itself.
		String replyToUuid = replyMapping != null ? replyMapping : topicUuid; 
		reply.setReplyToPostUuid(replyToUuid);
		reply.setTitle(getDecoratedTitle(reply));
		List<String> tags = new ArrayList<String>(reply.getBaseTags());
		tags.addAll(extraTags);
		reply.setBaseTags(tags);
		
		return forumService.createForumReply(topicUuid, reply).getReplyUuid();
	}

	private String postTopicWithAttachments(String forumId, ForumTopic topic, File[] attachments) throws ClientServicesException {
		String url = ForumUrls.FORUM_TOPICS.format(forumService, ForumUrls.forumPart(forumId));
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "multipart/related;type=\"application/atom+xml\" ;boundary=MIME_boundary");
		topic.setTitle(getDecoratedTitle(topic));
		List<String> tags = new ArrayList<String>(topic.getBaseTags());
		tags.addAll(extraTags);
		topic.setBaseTags(tags);
		
		MultipartEntity mEntity = createMultipartEntity(new ForumSerializer(topic).generateCreate(), attachments);
		String uploadedTopicId = "";
		uploadedTopicId = forumService.getForumTopicFeedHandler(false).createEntity(forumService.createData(url, null, headers, mEntity)).getTopicUuid();
		return uploadedTopicId;
	}
    
    private String postTopicReplyWithAttachments(String topicId, ForumReply topicReply, File[] attachments) {
		String url = ForumUrls.TOPIC_REPLIES.format(forumService, ForumUrls.topicPart(topicId));
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "multipart/related;type=\"application/atom+xml\" ;boundary=MIME_boundary");
		topicReply.setTopicUuid(topicId);
		topicReply.setReplyToPostUuid(topicId);
		topicReply.setTitle(getDecoratedTitle(topicReply));
		List<String> tags = new ArrayList<String>(topicReply.getBaseTags());
		tags.addAll(extraTags);
		topicReply.setBaseTags(tags);
		
		MultipartEntity mEntity = createMultipartEntity(new ForumSerializer(topicReply).generateCreate(), attachments);
		String uploadedReplyId = "";
		try {
			uploadedReplyId = forumService.getForumReplyFeedHandler(false).createEntity(forumService.createData(url, null, headers, mEntity)).getTopicUuid();
		} catch (ClientServicesException e) {
			e.printStackTrace();
		}
		return uploadedReplyId;
	}
    
    private MultipartEntity createMultipartEntity(String xml, File[] attachments){
    	MultipartEntity mEntity = new MultipartEntity(HttpMultipartMode.STRICT, "MIME_boundary", Charset.forName("UTF-8"));
		
		StringBody xmlBody = new StringBody(xml, ContentType.APPLICATION_ATOM_XML);
		FormBodyPart xmlBodyPart = new FormBodyPart("xml", xmlBody);
		mEntity.addPart(xmlBodyPart);
		xmlBodyPart.getHeader().removeFields("Content-Disposition");
		
		for(File attachment : attachments){
			FileBody fileBody = new FileBody(attachment, ContentType.parse((new MimetypesFileTypeMap()).getContentType(attachment.getName())));
			FormBodyPart fileBodyPart = new FormBodyPart("", fileBody);
			fileBodyPart.getHeader().removeFields("Content-Disposition");
			String fileName = attachment.getName().substring(attachment.getName().indexOf('.') + 1);
			fileBodyPart.addField("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			mEntity.addPart(fileBodyPart);
		}
		return mEntity;
    }

	private File[] getDownloadedFiles(List<ForumFileField> fileFields){
		File[] result = new File[fileFields.size()];;
		int i = 0;
		for(ForumFileField fileField : fileFields){
			result[i] = getDownloadedFile(fileField);
			i++;
		}
		
		return result;
	}

	private File getDownloadedFile(ForumFileField fileField) {
		File result = new File(forumDirectoryFile.getAbsolutePath() + File.separator + fileField.getFid() + "." + fileField.getName());
		return result;
	}

	private Response getXmlResponse(File xmlFile) throws FileNotFoundException, XMLException{
		Document doc = parseXml(xmlFile);
		Response response = new Response(doc);
		
		return response;
	}

	private Document parseXml(File xmlFile) throws FileNotFoundException, XMLException{
		InputStream xmlStream = new FileInputStream(xmlFile);
		return DOMUtil.createDocument(xmlStream);
	}

	public File getForumDirectoryFile() {
		return forumDirectoryFile;
	}

	public void setForumDirectoryFile(File forumDirectoryFile) {
		this.forumDirectoryFile = forumDirectoryFile;
	}
}
