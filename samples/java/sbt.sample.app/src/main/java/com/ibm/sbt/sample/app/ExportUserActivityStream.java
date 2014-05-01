package com.ibm.sbt.sample.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamEntityList;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamService;
import com.ibm.sbt.services.client.connections.profiles.Profile;
import com.ibm.sbt.services.client.connections.profiles.ProfileService;
import com.ibm.sbt.services.client.connections.profiles.ProfileServiceException;
import com.ibm.sbt.services.client.connections.profiles.model.ProfileXPath;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

public class ExportUserActivityStream {
	
	/**
	 * The username used to authenticate with connections 
	 */
	public String USERNAME = "";
	
	/**
	 * The password used to authenticate with connections
	 */
	public String PASSWORD = "";
	
	/**
	 * The id of the user to retrieve activity stream data from
	 */
	public String USER_ID = "";
	
	/**
	 * The number of results to retrieve 
	 */
	public String resultCount = "";
	
	/**
	 * the file path where the two csv files will be created
	 */
	public String filePath = "";
	
	/**
	 * Service maps for the where the open socail application is installed
	 */
	public String serviceMap = "";
	
	/**
	 * The url to the connections server
	 */
	public String connectionsUrl = "";

	private RuntimeFactory runtimeFactory;
	private Context context;
	private Application application;
	private BasicEndpoint endpoint;
	private ActivityStreamService activityStreamService;

	private String endpointName = "connections";
	
	/**
	 * Main method and entry point to the application
	 * @param args 
	 */
	public static void main(String [] args){
		Properties mainProperties = new Properties();
	    FileInputStream file;
	    String path = "./sbt.properties";
	    try {
			file = new FileInputStream(path);
		    try {
				mainProperties.load(file);
				file.close();
			} catch (IOException e) {
				System.out.println("Error reading the properties file");
				e.printStackTrace();
			}			    
		} catch (FileNotFoundException e) {
			System.out.println("Properties file not found, the sbt.properties should be in the same directory as the jar file");
			e.printStackTrace();
		}

	    String url = mainProperties.getProperty("url");
	    String username = mainProperties.getProperty("username");
	    String password = mainProperties.getProperty("password");
	    String userid = mainProperties.getProperty("userid");
	    String count = mainProperties.getProperty("count");
	    String filePath = mainProperties.getProperty("filePath");
	    String serviceMap = mainProperties.getProperty("serviceMap");
	    
	    String[] params = {url,username,password,userid,count,filePath,serviceMap};
	    for(int i=0;i<params.length;i++){
		   if(params[i] == null || params[i] == "" ){
			   System.out.println("Not all properties are set, please check the sbt.properties file.");
			   return;
		   }
	    }
	    
	    new ExportUserActivityStream(params);
		
	}
	
	public ExportUserActivityStream(String[] args){
		this.connectionsUrl = args[0];
		this.USERNAME = args[1];
		this.PASSWORD = args[2];
		this.USER_ID = args[3];
		this.resultCount = args[4];
		this.filePath = args[5];
		this.serviceMap = args[6];		

		System.out.println("Initialising environment and authenticating with connections...");
		
		this.initEnvironment();
		
		endpoint = new BasicEndpoint();
		endpoint.setUrl(this.connectionsUrl);
        endpoint.setForceTrustSSLCertificate(true);
        endpoint.setUser(this.USERNAME);
        endpoint.setPassword(this.PASSWORD);
		
		//set service maps
		Map<String,String> map = new HashMap<String,String>();
		map.put("connections", this.serviceMap);
		endpoint.setServiceMappings(map);
		
		this.getUserActivityStream();
		this.getProfileProperties();

		System.out.println("Done");		
	}
	
	public void getUserActivityStream(){
		
		System.out.println("Getting Activity Stream Data....");
		String content = "";
		try {
			activityStreamService = new ActivityStreamService();
			activityStreamService.setEndpoint(this.endpoint);
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("count", this.resultCount);
			ActivityStreamEntityList entries = activityStreamService.getUpdatesFromUser(this.USER_ID,map);
			
			for(int i=0;i<entries.size();i++){
				String id = entries.get(i).getAsString("actor/id");
				if(id.contains("urn")){
					id = this.removeUrnPrefix(id);
				}
				String verb = entries.get(i).getAsString("verb");
				String targetName = entries.get(i).getAsString("target/objectType");
				//Parent id/name etc is not available
				String objectId = entries.get(i).getAsString("object/id");
				if(objectId.contains("urn")){
					objectId = this.removeUrnPrefix(objectId);
				}
				String objectTitle = entries.get(i).getAsString("object/displayName");
				String actionDate = entries.get(i).getAsString("published");
				String updateDate = entries.get(i).getAsString("updated");
				//tagValueList not available
				String mentionId="";
				if(verb == "mention" || verb.contains("mention")){
					mentionId = entries.get(i).getAsString("object/author/id");
					if(mentionId.contains("urn")){
						mentionId = this.removeUrnPrefix(mentionId);
					}
				}
				
				//view count not available
				String likeCount = entries.get(i).getAsString("object/likes/totalItems");
				
				content = content + (id +","+verb+","+targetName+","+""+","+objectId+","+objectTitle+","+actionDate+","+updateDate+","+""+","+mentionId+","+""+","+likeCount+"\n");
				
			}
			String headings = "PersonId,Action,targetObjectType,ParentObjectID,TargetObjectID,TargetObjectTitle,ActionDate,UpdateDate,TagValueList,MentionPersonIDList,ViewCount,LikeCount,CommentCount \n";
			String path = this.filePath+"socialGraph.csv";
			writeToFile(path, content,headings);
		} catch (Throwable e) {
			System.out.println("Erorr retrieving ActivityStream Data");
			e.printStackTrace();
		}
	}
	
	public void getProfileProperties(){
		
		System.out.println("\nGetting Profile Properties...");
		ProfileService connProfSvc = new ProfileService();
		connProfSvc.setEndpoint(this.endpoint);
		Map<String, String> params = new HashMap<String, String>();
		params.put("userid", this.USER_ID);
		String content = "";
		try {
			Profile profile = connProfSvc.getProfile(this.USER_ID, params);
			
			//some strings contain commas index will be used to find and remove them
			int index = -1;
			
			String id = profile.getUserid();
			String name = profile.getName();
			
			System.out.println(name);
			if(name !=null){
				index = name.indexOf(",");
				if(index != -1){
					name = name.substring(0,index)+name.substring(index+1,name.length());
				}
			}
			
			String email = profile.getEmail();
			
			String org = profile.getAsString(ProfileXPath.organizationUnit);
			if(org !=null){
				index=-1;
				index = org.indexOf(",");
				if(index != -1){
					org = org.substring(0,index)+org.substring(index+1,org.length());
				}
			}
			
			String country = profile.getAsString(ProfileXPath.countryName);
			String isManager = profile.getAsString(ProfileXPath.isManager);
			String userState = profile.getAsString(ProfileXPath.userState);
			String role = profile.getAsString(ProfileXPath.role);
			if(role != null){
				index=-1;
				index = role.indexOf(",");
				if(index != -1){
					role = role.substring(0,index)+role.substring(index+1,role.length());
				}
			}
			
			content = id+","+name+","+email+","+org+","+country+","+isManager+","+userState+","+role;				
			String headings = "PersonID,PersonDisplayName,PersonEmail,PersonOrganization,PersonCountry,PersonIsManager,PersonStatus,EmployeeType\n";
			String path = this.filePath+"personProperties.csv";
			this.writeToFile(path, content, headings);
			
		} catch (ProfileServiceException e) {
			System.out.println("ERROR getting profile information");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The endpoint used in this class.
	 */
	public BasicEndpoint getEndpoint() {
		return this.endpoint;
	}
		
	/**
	 * Initialise the Context, needed for Services and Endpoints.
	 */
	public void initEnvironment() {
		runtimeFactory = new RuntimeFactoryStandalone();
		application = runtimeFactory.initApplication(null);
		context = Context.init(application, null, null);
	}
	
	/**
	 * Destroy the Context.
	 */
	public void destroy() {
		if (context != null) {
			Context.destroy(context);
		}
		if (application != null) {
			Application.destroy(application);
		}
	}
	
	private void writeToFile(String filePath, String content,String headings) {
		try {
			System.out.println("Creating CSV File");
			File file = new File(filePath);
			
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(headings);
			bw.write(content);
			bw.close();
 
			System.out.println("CSV file created at "+filePath);
 
		} catch (IOException e) {
			System.out.println("There was an error creating the output file");
			e.printStackTrace();
		}
	}
	
	private String removeUrnPrefix(String id){
		int index = id.lastIndexOf(":")+1;
		id = id.substring(index, id.length());
		return id;
	}

}
