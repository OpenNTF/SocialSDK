package nsf.playground.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import nsf.playground.environments.PlaygroundEnvironment;

import com.ibm.commons.util.QuickSort;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;
import com.ibm.xsp.util.ManagedBeanUtil;


/**
 * This is a managed bean used to cache and access the configuration data in the DB.
 */
public abstract class DataAccessBean {

	public static final String BEAN_NAME = "dataAccess";

	public static final String CUSTOM = "Custom...";

	private static boolean TRACE = false;
	
	public static DataAccessBean get() {
		return (DataAccessBean)ManagedBeanUtil.getBean(FacesContext.getCurrentInstance(), "dataAccess");
	}
	
	//
	// Managing environments
	//
	private PlaygroundEnvironment customEnvironment = new PlaygroundEnvironment(CUSTOM) {
		{
			setDescription("This is a custom, transient environment that can be defined to point to your own servers, or use our own OAuth keys");
		}
		// This cannot be set for the custom env, as it is not persisted
		public void setNoteID(String noteID) {
		}
	};

	private boolean cacheFilled;
	private HashMap<String,PlaygroundEnvironment> environments = new HashMap<String,PlaygroundEnvironment>();
	private String[] envNames = StringUtil.EMPTY_STRING_ARRAY;
	private String preferredEnvironment;
	
	public DataAccessBean() {
	}
	
	public synchronized void clearCache() {
		if(TRACE) {
			System.out.println("Clear cache");
		}
		cacheFilled = false;
		preferredEnvironment = null;
		envNames = StringUtil.EMPTY_STRING_ARRAY;
		environments.clear();
	}
	
	public synchronized String getPreferredEnvironment() throws IOException {
		updateCache();
		return preferredEnvironment;
	}
	
	public synchronized String[] getEnvironments() throws IOException {
		updateCache();
		return envNames;
	}

	public synchronized PlaygroundEnvironment getEnvironment(String name) throws IOException {
		updateCache();
		if(StringUtil.isEmpty(name)) {
			name = preferredEnvironment;
		}
		return environments.get(name);
	}

	public PlaygroundEnvironment getCustomEnvironment() {
		return customEnvironment;
	}
	
	private synchronized void updateCache() throws IOException {
		if(cacheFilled) {
			return;
		}
		try {
			if(TRACE) {
				System.out.println("Filling cache...");
			}
			String[] envs = StringUtil.splitString(OptionsBean.get().getEnvironments(),',');

			cacheFilled = true;
			List<String> allEnvs = new ArrayList<String>();
			Database db = ExtLibUtil.getCurrentDatabase();
			if(db!=null) {
				View v = db.getView("AllEnvironments");
				if(v!=null) {
					ViewEntryCollection vc = v.getAllEntries();
					for(ViewEntry e=vc.getFirstEntry(); e!=null; e=vc.getNextEntry()) {
						Document d = e.getDocument();
						try {
							PlaygroundEnvironment env = readEnvironment(d);
							if(envs.length==0 || StringUtil.contains(envs, env.getName())) {
								environments.put(env.getName(), env);
								if(TRACE) {
									System.out.println("Loading environment: "+env.getName());
								}
								allEnvs.add(env.getName());
							}
						} finally {
							d.recycle();
						}
					}
				}
			}
			environments.put(CUSTOM, getCustomEnvironment());
			if(TRACE) {
				System.out.println("Loading environment: "+CUSTOM);
			}
			
			(new QuickSort.JavaList(allEnvs)).sort();
			allEnvs.add(CUSTOM); // Always the last one...
			
			this.envNames = allEnvs.toArray(new String[allEnvs.size()]);
		} catch(NotesException ex) {
			throw new IOException(ex);
		}
	}
	
	
	
	public void duplicateEnvironment(String id) throws NotesException, IOException {
		Document d = ExtLibUtil.getCurrentDatabase().getDocumentByID(id);
		Document newDoc = ExtLibUtil.getCurrentDatabase().createDocument();
		d.replaceItemValue("name", StringUtil.format("Copy of {0}", d.getItemValueString("Name")));
		d.copyAllItems(newDoc, true);
		newDoc.save();
	}
	public void copyEnvironment(DominoDocument doc, String name) throws NotesException, IOException {
		View v = ExtLibUtil.getCurrentDatabase().getView("AllEnvironments");
		ViewEntry ve = v.getEntryByKey(name);
		if(ve!=null) {
			Document d = ve.getDocument();
			d.copyAllItems(doc.getDocument(), true);
			doc.setDocument(doc.getDocument());
		}
	}
	public PlaygroundEnvironment readEnvironment(Document d) throws NotesException, IOException {
		PlaygroundEnvironment env = new PlaygroundEnvironment();
		return readEnvironment(env, d);
	}
	public PlaygroundEnvironment readEnvironment(PlaygroundEnvironment env, Document d) throws NotesException, IOException {
		env.setNoteID(d.getNoteID());
		env.setName(d.getItemValueString("Name"));
		env.setDescription(d.getItemValueString("Description"));
		env.setProperties(d.getItemValueString("Properties"));
		boolean def = StringUtil.equals(d.getItemValueString("Preferred"),"1");
		if(def) {
			preferredEnvironment = env.getName();
		}
		
		// Playground specific data
		env.setEndpoint_Connections(d.getItemValueString("Endpoint_Connections"));
		env.setEndpoint_Smartcloud(d.getItemValueString("Endpoint_Smartcloud"));
		env.setEndpoint_Domino(d.getItemValueString("Endpoint_Domino"));
		
		env.setCon_URL(d.getItemValueString("Con_URL"));
		env.setCon_OA2_ConsumerKey(d.getItemValueString("Con_OA2_ConsumerKey"));
		env.setCon_OA2_ConsumerSecret(d.getItemValueString("Con_OA2_ConsumerSecret"));
		env.setCon_OA2_AuthorizationURL(d.getItemValueString("Con_OA2_AuthorizationURL"));
		env.setCon_OA2_AccessTokenURL(d.getItemValueString("Con_OA2_AccessTokenURL"));
		
		env.setSma_URL(d.getItemValueString("Sma_URL"));
		env.setSma_OA_ConsumerKey(d.getItemValueString("Sma_OA_ConsumerKey"));
		env.setSma_OA_ConsumerSecret(d.getItemValueString("Sma_OA_ConsumerSecret"));
		env.setSma_OA_RequestTokenURL(d.getItemValueString("Sma_OA_RequestTokenURL"));
		env.setSma_OA_AuthorizationURL(d.getItemValueString("Sma_OA_AuthorizationURL"));
		env.setSma_OA_AccessTokenURL(d.getItemValueString("Sma_OA_AccessTokenURL"));
		env.setSma_OA2_ConsumerKey(d.getItemValueString("Sma_OA2_ConsumerKey"));
		env.setSma_OA2_ConsumerSecret(d.getItemValueString("Sma_OA2_ConsumerSecret"));
		env.setSma_OA2_AuthorizationURL(d.getItemValueString("Sma_OA2_AuthorizationURL"));
		env.setSma_OA2_AccessTokenURL(d.getItemValueString("Sma_OA2_AccessTokenURL"));

		env.setDom_URL(d.getItemValueString("Dom_URL"));

		env.setSt_URL(d.getItemValueString("St_URL"));

		env.setTwitter_OA_ConsumerKey(d.getItemValueString("Twitter_OA_ConsumerKey"));
		env.setTwitter_OA_ConsumerSecret(d.getItemValueString("Twitter_OA_ConsumerSecret"));
		
		return env;
	}
	public PlaygroundEnvironment writeEnvironment(PlaygroundEnvironment env, Document d) throws NotesException, IOException {
		d.replaceItemValue("Properties",env.getProperties());

		// Playground specific data
		d.replaceItemValue("Endpoint_Connections",env.getEndpoint_Connections());
		d.replaceItemValue("Endpoint_Smartcloud",env.getEndpoint_Smartcloud());
		d.replaceItemValue("Endpoint_Domino",env.getEndpoint_Domino());

		d.replaceItemValue("Con_URL",env.getCon_URL());
		d.replaceItemValue("Con_OA2_ConsumerKey",env.getCon_OA2_ConsumerKey());
		d.replaceItemValue("Con_OA2_ConsumerSecret",env.getCon_OA2_ConsumerSecret());
		d.replaceItemValue("Con_OA2_AuthorizationURL",env.getCon_OA2_AuthorizationURL());
		d.replaceItemValue("Con_OA2_AccessTokenURL",env.getCon_OA2_AccessTokenURL());

		d.replaceItemValue("Sma_URL",env.getSma_URL());
		d.replaceItemValue("Sma_OA_ConsumerKey",env.getSma_OA_ConsumerKey());
		d.replaceItemValue("Sma_OA_ConsumerSecret",env.getSma_OA_ConsumerSecret());
		d.replaceItemValue("Sma_OA_RequestTokenURL",env.getSma_OA_RequestTokenURL());
		d.replaceItemValue("Sma_OA_AuthorizationURL",env.getSma_OA_AuthorizationURL());
		d.replaceItemValue("Sma_OA_AccessTokenURL",env.getSma_OA_AccessTokenURL());
		d.replaceItemValue("Sma_OA2_ConsumerKey",env.getSma_OA2_ConsumerKey());
		d.replaceItemValue("Sma_OA2_ConsumerSecret",env.getSma_OA2_ConsumerSecret());
		d.replaceItemValue("Sma_OA2_AuthorizationURL",env.getSma_OA2_AuthorizationURL());
		d.replaceItemValue("Sma_OA2_AccessTokenURL",env.getSma_OA2_AccessTokenURL());

		d.replaceItemValue("Dom_URL",env.getDom_URL());

		d.replaceItemValue("Twitter_OA_ConsumerKey",env.getTwitter_OA_ConsumerKey());
		d.replaceItemValue("Twitter_OA_ConsumerSecret",env.getTwitter_OA_ConsumerSecret());
		
		return env;
	}
}
