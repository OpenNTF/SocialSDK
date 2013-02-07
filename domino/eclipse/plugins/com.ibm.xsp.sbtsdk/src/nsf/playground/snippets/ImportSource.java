package nsf.playground.snippets;

import java.io.Serializable;

/**
 * Import Source.
 * 
 * @author priand
 * 
 */
public class ImportSource implements Serializable {

	private static final long serialVersionUID=1L;

	private String name;
	private String source;
	private String location;
	private String userName;
	private String password;

	public ImportSource(String name, String source, String location, String userName, String password) {
		this.name=name;
		this.source=source;
		this.location=location;
		this.userName=userName;
		this.password=password;
	}

	public String getName() {
		return name;
	}

	public String getSource() {
		return source;
	}

	public String getLocation() {
		return location;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
}
