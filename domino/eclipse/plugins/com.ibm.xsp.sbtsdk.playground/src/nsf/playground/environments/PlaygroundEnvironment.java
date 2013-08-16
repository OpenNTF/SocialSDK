package nsf.playground.environments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nsf.playground.beans.DataAccessBean;
import nsf.playground.extension.Endpoints;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.sbt.playground.extension.PlaygroundExtensionFactory;

/**
 * This is an extended environment class holding extra playground specific information
 * @author priand
 *
 */
public class PlaygroundEnvironment extends SBTEnvironment {
	
	public static PlaygroundEnvironment getCurrentEnvironment() {
		return getCurrentEnvironment(null);
	}
	public static PlaygroundEnvironment getCurrentEnvironment(String envName) {
		return DataAccessBean.get().getCurrentEnvironment(envName);
	}

	public static String getCurrentEnvironmentName() {
		return getCurrentEnvironmentName(null);
	}
	public static String getCurrentEnvironmentName(String envName) {
		return getCurrentEnvironment(envName).getName();
	}
	
	private static final class FieldMap extends HashMap<String, String> {
		private static final long serialVersionUID=1L;
		public String get(Object key) {
			return super.get(convertKey((String)key));
		}
		public String put(String key, String value) {
			return super.put(convertKey(key),value);
		}
		private static String convertKey(String o) {
			return ((String)o).toLowerCase();
		}
	}

	private String noteID;
	private String description;
	private boolean preferred;	
	
	private FieldMap fields = new FieldMap();
		
	public PlaygroundEnvironment() {
		this(null,null);
	}

	public PlaygroundEnvironment(String name) {
		this(name,null);
	}
	
	public PlaygroundEnvironment(String name, Property[] properties) {
		super(name,
			  null,
			  properties);
		setEndpointsArray(createEndpoints());
	}
	
	public Map<String,String> getFieldMap() {
		return fields;
	}
	public String getField(String name) {
		return fields.get(name);
	}
	public void putField(String name, String value) {
		fields.put(name,value);
	}
	
	private SBTEnvironment.Endpoint[] createEndpoints() {
		ArrayList<SBTEnvironment.Endpoint> endpoints = new ArrayList<SBTEnvironment.Endpoint>();

		List<Endpoints> envext = PlaygroundExtensionFactory.getExtensions(Endpoints.class);
		for(int ev=0; ev<envext.size(); ev++) {
			Endpoints e = envext.get(ev);
			String[] sp = StringUtil.splitString(e.getEndpointNames(), ',', true);
			for(int i=0; i<sp.length; i++) {
				if(StringUtil.isNotEmpty(sp)) {
					endpoints.add(new SBTEnvironment.Endpoint(sp[i],null));
				}
			}
		}
		return endpoints.toArray(new SBTEnvironment.Endpoint[endpoints.size()]);
	}

	
	public String getNoteID() {
		return noteID;
	}
	public void setNoteID(String noteID) {
		this.noteID = noteID;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isPreferred() {
		return preferred;
	}
	public void setPreferred(boolean preferred) {
		this.preferred = preferred;
	}

	public void prepareEndpoints() {
		List<Endpoints> endpoints = PlaygroundExtensionFactory.getExtensions(Endpoints.class);
		for(int i=0; i<endpoints.size(); i++) {
			endpoints.get(i).prepareEndpoints(this);
		}
	}
}
