package nsf.playground.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import nsf.playground.environments.PlaygroundEnvironment;
import nsf.playground.extension.Endpoints;
import nsf.playground.extension.Endpoints.Category;
import nsf.playground.extension.Endpoints.Property;
import nsf.playground.extension.Endpoints.PropertyValues;

import com.ibm.commons.util.QuickSort;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.extension.PlaygroundExtensionFactory;
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
	
	private boolean cacheFilled;
	private HashMap<String,PlaygroundEnvironment> environments = new HashMap<String,PlaygroundEnvironment>();
	private String[] envNames = StringUtil.EMPTY_STRING_ARRAY;
	
	public DataAccessBean() {
	}
	
	public synchronized void clearCache() {
		if(TRACE) {
			System.out.println("Clear cache");
		}
		cacheFilled = false;
		envNames = StringUtil.EMPTY_STRING_ARRAY;
		environments.clear();
	}

	public synchronized String getPreferredEnvironment() throws IOException {
		updateCache();
		PlaygroundEnvironment pref = findPreferredEnvironment();
		if(pref!=null) {
			return pref.getName();
		}
		return null;
	}
	
	public synchronized String[] getEnvironments() throws IOException {
		updateCache();
		return envNames;
	}

	public synchronized PlaygroundEnvironment getEnvironment(String name) throws IOException {
		updateCache();
		if(!StringUtil.isEmpty(name)) {
			if(StringUtil.equals(name,CUSTOM)) {
				PlaygroundEnvironment e = getCustomEnvironment();
				if(e!=null) {
					return e;
				}
			} else {
				PlaygroundEnvironment e = environments.get(name);
				if(e!=null) {
					return e;
				}
			}
		}
		return findPreferredEnvironment();
	}

	
	public PlaygroundEnvironment getCurrentEnvironment(String envName) {
		try {
			if(StringUtil.isEmpty(envName)) {
				FacesContext ctx = FacesContext.getCurrentInstance();
				if(ctx!=null) {
					envName = (String)ctx.getExternalContext().getSessionMap().get("environment");
				}
			}
			if(StringUtil.isNotEmpty(envName)) {
				if(StringUtil.equals(envName,CUSTOM)) {
					PlaygroundEnvironment e = getCustomEnvironment();
					if(e!=null) {
						return e;
					}
				} else {
					PlaygroundEnvironment e = environments.get(envName);
					if(e!=null) {
						return e;
					}
				}
			}
			return findPreferredEnvironment();
		} catch(IOException e) {}
		return null;
	}

	public PlaygroundEnvironment findPreferredEnvironment() throws IOException {
		PlaygroundEnvironment first = null;
		if(!environments.isEmpty()) {
			for(PlaygroundEnvironment e: environments.values()) {
				if(e.isPreferred()) {
					return e;
				}
				if(first==null) {
					first = e;
				}
			}
		}
		return first;
	}

	public PlaygroundEnvironment getCustomEnvironment() {
		// The environment is stored on per session (user) 
		Map<String,Object> sc = ExtLibUtil.getSessionScope();
		PlaygroundEnvironment env = (PlaygroundEnvironment)sc.get("custom-env");
		if(env==null) {
			env = new PlaygroundEnvironment(CUSTOM) {
				{
					setDescription("This is a custom, transient environment that can be defined to point to your own servers, or use our own OAuth keys");
					// Set all the default properties
					List<nsf.playground.extension.Endpoints.Property> props = Endpoints.categories.getAllProperties();
					for(int i=0; i<props.size(); i++) {
						nsf.playground.extension.Endpoints.Property p = props.get(i);
						String defValue = p.getDefaultValue();
						if(StringUtil.isNotEmpty(defValue)) {
							putField(p.getName(), p.getDefaultValue());
						}
					}
				}
				// This cannot be set for the custom env, as it is not persisted
				public void setNoteID(String noteID) {
				}
			};
			sc.put("custom-env",env);
		}
		return env;
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
		env.setNoteID(StringUtil.trim(d.getNoteID()));
		env.setName(StringUtil.trim(d.getItemValueString("Name")));
		env.setDescription(StringUtil.trim(d.getItemValueString("Description")));
		String runtimes = StringUtil.trim(d.getItemValueString("Runtimes"));
		if(StringUtil.isNotEmpty(runtimes)) {
			env.setRuntimes(runtimes);
		}
		env.setProperties(StringUtil.trim(d.getItemValueString("Properties")));
		
		boolean def = StringUtil.equals(StringUtil.trim(d.getItemValueString("Preferred")),"1");
		if(def) {
			env.setPreferred(true);
		}
		
		env.getFieldMap().clear();
		
		List<Endpoints> envext = PlaygroundExtensionFactory.getExtensions(Endpoints.class);
		for(int i=0; i<envext.size(); i++) {
			Category[] cats = envext.get(i).getPropertyList();
			if(cats!=null) {
				for(int j=0; j<cats.length; j++) {
					Property[] props = cats[j].getProperties();
					if(props!=null) {
						for(int k=0; k<props.length; k++) {
							Property p = props[k];
							env.putField(p.getName(), StringUtil.trim(d.getItemValueString(p.getName())));
						}
					}
				}
			}
		}
		return env;
	}
	public PlaygroundEnvironment writeEnvironment(PlaygroundEnvironment env, DominoDocument d) throws NotesException, IOException {
		d.replaceItemValue("Properties",env.getProperties());
		d.replaceItemValue("Runtimes",env.getRuntimes());

		List<Endpoints> envext = PlaygroundExtensionFactory.getExtensions(Endpoints.class);
		for(int i=0; i<envext.size(); i++) {
			Category[] cats = envext.get(i).getPropertyList();
			if(cats!=null) {
				for(int j=0; j<cats.length; j++) {
					Property[] props = cats[j].getProperties();
					if(props!=null) {
						for(int k=0; k<props.length; k++) {
							String pName = props[k].getName();
							String v = env.getField(pName);
							d.replaceItemValue(pName,v);
						}
					}
				}
			}
		}
		return env;
	}
	public void loadProperties(DominoDocument d, Category cat, String name) throws NotesException, IOException {
		if(cat==null) {
			return;
		}
		boolean isDefault = StringUtil.isEmpty(name) || StringUtil.equals(name, "0");
		PropertyValues pv = isDefault? null : cat.getPropertyValues(name);
		Property[] allp = cat.getProperties();
		for(int i=0; i<allp.length; i++) {
			Property p = allp[i];
			if(pv==null) {
				String v = p.getDefaultValue();
				d.replaceItemValue(p.getName(), v);
			} else {
				String v = pv.getValues()[i];
				d.replaceItemValue(p.getName(), v);
			}
		}
	}
}
