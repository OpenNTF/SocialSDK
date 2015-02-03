package com.ibm.sbt.sample.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.http.ParseException;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.datahandlers.AtomEntityList;
import com.ibm.sbt.services.client.connections.forums.Forum;
import com.ibm.sbt.services.client.connections.forums.ForumReply;
import com.ibm.sbt.services.client.connections.forums.ForumService;
import com.ibm.sbt.services.client.connections.forums.ForumTopic;
import com.ibm.sbt.services.client.connections.forums.ForumFileField;
import com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity;
import com.ibm.sbt.services.endpoints.BasicEndpoint;

/**
 * @author Francis
 */
public class ForumExporter {
	
    public static void main(String[] args){
    	boolean exportTopics = true;
    	boolean exportTopicReplies = true;
		if(args.length < 5){
			System.out.println("Not enough arguments");
			System.out.println("Usage: connections_url forumId exportDirectory username password (optional)noTopics (optional)noTopicReplies");
			System.out.println("If noTopics is included then topics and topic replies won't be exported");
			System.out.println("If noTopicReplies is included then topic replies won't be exported");
			return;
		}else if(args.length == 6){
			if(args[5].equalsIgnoreCase("notopics")){
				exportTopics = false;
			}else if(args[5].equalsIgnoreCase("noTopicReplies")){
				exportTopicReplies = false;
			}
		}else if(args.length == 7){
			if(args[6].equalsIgnoreCase("notopics")){
				exportTopics = false;
			}else if(args[6].equalsIgnoreCase("noTopicReplies")){
				exportTopicReplies = false;
			}
		}
		String connectionsUrl = args[0], 
				forumId = args[1], 
				exportDirectory = args[2],
				username = args[3],
				password = args[4];
		
		ForumExporter forumExporter = null;
		try {
			long start = System.currentTimeMillis();
			
			File exportFolder = new File(exportDirectory + File.separator + "Forum_" + forumId);
	    	if(!exportFolder.exists() && !exportFolder.mkdirs()){
	    		throw new IOException("Could not create directory structure for " + exportDirectory);
	    	}
	    	
			forumExporter = new ForumExporter(connectionsUrl, username, password);
			forumExporter.exportForum(forumId, exportFolder, exportTopics, exportTopicReplies);
			
			double duration = (System.currentTimeMillis() - start) / 1000d;
	        System.out.println("Forum exported in : " + duration + " seconds");
	        System.out.println("Exported to: " + exportFolder.getAbsolutePath());
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (ClientServicesException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLException e) {
			e.printStackTrace();
		}
		
	}

    private ForumService forumService;
    private File forumExportDirectory;
	/**
     * 
     * @param url
     * @param user
     * @param password
     * @throws AuthenticationException
     */
    public ForumExporter(String url, String user, String password)
            throws AuthenticationException {
        BasicEndpoint basicEndpoint = new BasicEndpoint();
        basicEndpoint.setUrl(url);
        basicEndpoint.setForceTrustSSLCertificate(true);
        basicEndpoint.login(user,  password);
        
        forumService = new ForumService();
        forumService.setEndpoint(basicEndpoint);
    }
    
    public void exportForum(String forumId, File exportFolder, boolean exportTopics, boolean exportTopicReplies) throws ClientServicesException, IOException, XMLException{
    	System.out.println("Exporting Forum...");
    	setForumExportDirectory(exportFolder);
    	Forum forum = forumService.getForum(forumId);
    	writeEntry(forum, exportFolder, "forum_" + forum.getUid());
    	
    	if(exportTopics){
    		exportTopics(forumId, exportTopicReplies);
    	}
    }

	public void exportTopics(String forumId, boolean exportReplies) throws XMLException, ParseException, ClientServicesException, IOException{
		System.out.println("Exporting topics...");
		int writtenTopics = 0;
    	int totalTopics = 0;
    	int page = 1;
    	Map<String, String> urlParams = new HashMap<String, String>();
    	AtomEntityList<ForumTopic> forumTopics = null;
		do{
			urlParams.put("page", Integer.toString(page));
	    	urlParams.put("ps", "100");
			forumTopics = (AtomEntityList<ForumTopic>) forumService.getForumTopics(forumId, urlParams);
			totalTopics = forumTopics.getTotalResults();
			if(page == 1){
				System.out.println("Found " + totalTopics + " topics");
			}
			
			if(forumTopics.size() > 0){
				downloadAttachments(forumTopics);
				// write the topics feed to disk before getting the next page
				writeFeed(forumTopics, "topics_" + forumId + "_" + page);
				writtenTopics += forumTopics.size();
				System.out.println("Exported " + page + " topic feeds of " + (((totalTopics - 1) / 100) + 1) + ", containing " + forumTopics.size() + " topics");
				if(exportReplies){
					int writtenReplies = 0;
					System.out.println("Exporting replies for topics in topic feed " + page + "...");
					for(ForumTopic forumTopic : forumTopics){
						exportTopicReplies(forumTopic.getTopicUuid());
						writtenReplies++;
						System.out.println("Exported " + writtenReplies + " topic reply feeds of " + forumTopics.size());
			    	}
				}
			}
	    	page++;
		}while(writtenTopics < totalTopics);
    }
	
	private void downloadAttachments(AtomEntityList<? extends BaseForumEntity> forumEntities) {
		for(BaseForumEntity entity : forumEntities){
			List<ForumFileField> fileFields = entity.getFileFields();
			for(ForumFileField fileField : fileFields){
				downloadFile(fileField, getExportfile(getForumExportDirectory().getAbsolutePath(), fileField.getFid() + "." + fileField.getName()));
			}
		}
	}
    
    private void downloadFile(ForumFileField fileField, File saveFile) {
    	InputStream fileInputStream = null;
    	try {
        	fileInputStream = (InputStream)fileField.getService().getEndpoint().xhrGet(fileField.getFileLink().getHref()).getData();
			FileUtils.copyInputStreamToFile(fileInputStream, saveFile);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClientServicesException e) {
			e.printStackTrace();
		} finally{
			try {
				fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void exportTopicReplies(String topicUuid) throws XMLException, ParseException, IOException, ClientServicesException{
    	int writtenReplies = 0;
    	int totalReplies = 0;
    	int page = 1;
    	Map<String, String> urlParams = new HashMap<String, String>();
		AtomEntityList<ForumReply> topicReplies;
		
		do{
			urlParams.put("page", Integer.toString(page));
	    	urlParams.put("ps", "100");
	    	topicReplies = (AtomEntityList<ForumReply>) forumService.getForumReplies(topicUuid, urlParams);
			downloadAttachments(topicReplies);
	    	totalReplies = topicReplies.getTotalResults();
			// write the topics feed to disk before getting the next page
			if(topicReplies.size() > 0){
				writeFeed(topicReplies, "replies_" + topicUuid + "_" + page);
			}
			writtenReplies += topicReplies.size();
	    	page++;
		}while(writtenReplies < totalReplies);
    }
    
    private void writeFeed(AtomEntityList<? extends AtomEntity> entityList, String fileName) throws XMLException, ParseException, IOException{
    	File exportFile = getExportfile(getForumExportDirectory().getAbsolutePath(), fileName);
        
    	writeXmlToFile(DOMUtil.getXMLString(entityList.getData()), exportFile);
	}
    
    private void writeEntry (AtomEntity entity, File folder, String fileName) throws XMLException, IOException {
    	File exportFile = getExportfile(folder.getAbsolutePath(), fileName);
        
    	writeXmlToFile(DOMUtil.getXMLString(entity.getDataHandler().getData()), exportFile);
	}
    
    private File getExportfile(String folderPath, String fileName){
    	return new File(folderPath + File.separator + fileName);
    }

    private void writeXmlToFile(String xml, File file) throws IOException{
    	FileOutputStream fos = new FileOutputStream(file);
    	OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
    	BufferedWriter bw = new BufferedWriter(osw);
    	PrintWriter out = new PrintWriter(bw);
    	out.write(xml);
    	out.flush();
    	out.close();
    }

	public File getForumExportDirectory() {
		return forumExportDirectory;
	}

	public void setForumExportDirectory(File forumExportDirectory) {
		this.forumExportDirectory = forumExportDirectory;
	}
    
}
