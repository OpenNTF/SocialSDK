package com.ibm.sbt.services.endpoints;


/**
 * @author doconnor
 *
 */
public class SameTimeEndpoint extends BasicEndpoint {

    /**
     * 
     */
    public SameTimeEndpoint() {
    }

    /**
     * @param user
     * @param password
     * @param authenticationPage
     */
    public SameTimeEndpoint(String user, String password, String authenticationPage) {
        super(user, password, authenticationPage);
    }

}
