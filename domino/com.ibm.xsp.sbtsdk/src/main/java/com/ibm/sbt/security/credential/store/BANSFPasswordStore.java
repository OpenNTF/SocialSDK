package com.ibm.sbt.security.credential.store;

import lotus.domino.Session;

import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.sbt.security.authentication.password.PasswordException;
import com.ibm.sbt.security.authentication.password.consumer.UserPassword;

public class BANSFPasswordStore extends AbstractNSFPasswordStore {
    
    public BANSFPasswordStore() {
    }
    
    @Override
    protected Session getUserSession() {
        NotesContext nc = NotesContext.getCurrentUnchecked();
        if(nc!=null) {
            return nc.getCurrentSession();
        }
        return null;
    }    
    @Override
    protected Session getDesignerSession() {
        NotesContext nc = NotesContext.getCurrentUnchecked();
        if(nc!=null) {
            return nc.getSessionAsSigner();
        }
        return null;
    }

	public Object load(String service, String type, String user)
			throws CredentialStoreException {
		
		try {
			Object userPassword=loadUserPassword(service);
			
			return userPassword;
		} catch (PasswordException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void store(String service, String type, String user,
			Object credentials) throws CredentialStoreException {
		Context context = new Context();
		try {
			saveAccessTokenToNSF(context, service, user, (UserPassword)credentials);
		} catch (PasswordException e) {
			e.printStackTrace();
		}
		
	}

	public void remove(String service, String type, String user)
			throws CredentialStoreException {
		try {
			deleteUserPassword(service);
		} catch (PasswordException e) {
			e.printStackTrace();
		}
		
	}
    
}