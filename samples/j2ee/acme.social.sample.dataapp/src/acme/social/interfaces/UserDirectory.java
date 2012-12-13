package acme.social.interfaces;

public interface UserDirectory {
		
	/**
	 * checks the userid and pass
	 * @param userid
	 * @param pass
	 * @return
	 */
	public boolean check(String userid, String pass);
	
	/**
	 * gets the person details for a person with a given userid
	 * @param userid
	 * @return
	 */
	public Object getPersonDetails(String userid);
	
}
