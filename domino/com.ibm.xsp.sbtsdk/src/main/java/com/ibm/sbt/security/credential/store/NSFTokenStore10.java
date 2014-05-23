package com.ibm.sbt.security.credential.store;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.ConsumerToken;
import com.ibm.sbt.security.authentication.oauth.consumer.OAuth1Handler;
import com.ibm.sbt.security.authentication.oauth.consumer.OAuth2Handler;
import com.ibm.xsp.model.domino.CredStoreUtil;


/**
 * Domino OAuth application token store.
 * @author Philippe Riand
 */
public class NSFTokenStore10 extends AbstractNSFTokenStore<AccessToken,ConsumerToken> implements CredentialStore {

    protected static final String CONSUMERTOKENS_VIEW  = "OAuthConsumer_10a";
    protected static final String ACCESSTOKENS_VIEW    = "OAuthAccess_10a";
    protected static final String ACCESSTOKEN_FORM     = "OAuthAccess_10a";

    protected static final String CF_AUTHURI = "AuthorizationUri";
    protected static final String CF_ACCESSURI = "AccessTokenUri";
    protected static final String CF_REQUESTURI = "RequestTokenUri";
    protected static final String CF_KEY = "ConsumerKey";
    protected static final String CF_SECRET = "ConsumerSecret";
    protected static final String CF_SIGMETHOD = "SignatureMethod";
    protected static final String CF_INCLUDEBODYHASH = "IncludeBodyHash";

    protected static final String TF_FORM = "Form";
    protected static final String TF_APPID = "AppId";
    protected static final String TF_SERVICE = "ServiceName";
    protected static final String TF_USERID = "UserId";
    protected static final String TF_CONSUMERKEY = "ConsumerKey";
    protected static final String TF_MODULEID = "ModuleId";
    protected static final String TF_TOKENNAME = "TokenName";
    protected static final String TF_ACCESSTOKEN = "AccessToken";
    protected static final String TF_ACCESSSECRET = "AccessSecret";
    protected static final String TF_EXPIRESIN = "ExpiresIn";
    protected static final String TF_AUTHEXPIRESIN = "AuthorizationExpiresIn";
    protected static final String TF_SESSIONHANDLE = "SessionHandle";

    private static final String CLASSNAME = NSFTokenStore10.class.getName();
    private static final Logger logger = Logger.getLogger( CLASSNAME );

    public NSFTokenStore10() 
    {
        super( CONSUMERTOKENS_VIEW, 
                ACCESSTOKENS_VIEW,
                "OAuth 1.0a Consumer token store",
        "OAuth 1.0a Access token store" );
    }

    public Object load(String serviceName, String type, String appId) throws CredentialStoreException 
    {
    	final String _method = "loadConsumerToken";
        logger.entering( CLASSNAME, _method, new Object[] { appId, serviceName } );
        
        //Get NSF name that is requesting the service
        String applicationName= findApplicationName();
		if(StringUtil.isNotEmpty(applicationName))
			applicationName=applicationName.replace(".nsf", "");
		
        Context context = Context.getUnchecked();
        Object result = null;
		
        try {
			if(type==OAuth1Handler.ACCESS_TOKEN_STORE_TYPE || type==OAuth2Handler.ACCESS_TOKEN_STORE_TYPE){				
				result =loadAccessToken(serviceName, type,applicationName,context.getCurrentUserId());
			}
			if(type==OAuth1Handler.CONSUMER_TOKEN_STORE_TYPE || type==OAuth2Handler.CONSUMER_TOKEN_STORE_TYPE) {
				result=loadConsumerTokenFromNSF(applicationName, serviceName);
			}
			
		} catch (OAuthException e) {
			e.printStackTrace();
		}

        logger.exiting( CLASSNAME, _method, result );
        return result;
    }

    @Override
    protected boolean verifyConsumerToken( ConsumerToken token, Session session ) throws NotesException
    {
    	
    	return false;
//        return StringUtil.equals(token.get, session.getEffectiveUserName());
    }

    @Override
    protected ConsumerToken createConsumerToken( Document doc ) throws NotesException
    {
        final String _method = "createConsumerToken";
        logger.entering( CLASSNAME, _method, doc );

        String requestTokenUri = doc.getItemValueString(CF_REQUESTURI);
        String authorizationUri = doc.getItemValueString(CF_AUTHURI);
        String accessTokenUri = doc.getItemValueString(CF_ACCESSURI);
        String consumerKeyType = doc.getItemValueString(CF_SIGMETHOD);
        String consumerKeyString = doc.getItemValueString(CF_KEY);;
		String consumerSecretString =doc.getItemValueString(CF_SECRET);
		
		ConsumerToken token = new ConsumerToken(requestTokenUri,authorizationUri,accessTokenUri,
				/*verifierId*/null,consumerKeyString,consumerKeyType,consumerSecretString);
          
        logger.exiting( CLASSNAME, _method, token );
        
        return token;
    }


   
    //
    // User tokens
    //

    /**
     * This method retrieves an access token from the database.  
     * @param appId The requestTokenURI
     * @param service The consumer secret
     * @param userId The user ID
     */
    public AccessToken loadAccessToken(String service, String type, String applicationName, String userId) throws OAuthException 
    {
        return loadAccessToken(applicationName, type, service, null, null, userId);
    }


    @Override
    protected boolean verifyAccessToken( AccessToken token, Session session ) throws NotesException
    {
        return 	StringUtil.equals(token.getUserId(), session.getEffectiveUserName());
    }
    
    @Override
    protected AccessToken createAccessToken( Document doc, String consumerKey, String accessKey,String accessSecret ) throws NotesException
    {
        final String _method = "createAccessToken";
        logger.entering( CLASSNAME, _method, doc );

        String appId = doc.getItemValueString( TF_APPID );
        String serviceName = doc.getItemValueString( TF_SERVICE );
//        String consumerKey = doc.getItemValueString( TF_CONSUMERKEY );
        String userId = doc.getItemValueString( TF_USERID );
        String moduleId = doc.getItemValueString( TF_MODULEID );
        String tokenName = doc.getItemValueString( TF_TOKENNAME );

        Date expiresIn = convertToDate(doc.getItemValue( TF_EXPIRESIN ));
        Date authorizationExpiresIn = convertToDate(doc.getItemValue( TF_AUTHEXPIRESIN ));
        String sessionHandle = doc.getItemValueString( TF_SESSIONHANDLE );
        
        AccessToken token = new AccessToken(appId,
                serviceName,
                consumerKey,
                accessKey,
                accessSecret,
                userId,
                moduleId,
                tokenName,
                expiresIn,
                authorizationExpiresIn,
                sessionHandle);

        logger.exiting( CLASSNAME, _method, token );
        return token;
    }

    
    /**
     * This method retrieves an access token, such as was created by an OpenSocial container,  from the database.  
     * The combination of the five parameters is considered to be a unique key
     * @param uri Application URI
     * @param service Service associated with the application
     * @param consumerKey Consumer key for the token
     * @param tokenName Token name
     * @param userId User submitting the request
     * @throws OAuthException
     */
    public AccessToken loadAccessToken(String applicationName, String type, String serviceName, String moduleId, String tokenName, String userId ) throws OAuthException
    {
        final String _method = "loadAccessToken";

        logger.entering( CLASSNAME, _method, new Object[] { applicationName, serviceName,  moduleId, tokenName, userId } );
        
        ConsumerToken token= loadConsumerTokenFromNSF(applicationName,serviceName );     
        String key = getAccessViewKey(applicationName, serviceName, token.getConsumerKey(), userId);
   
        Vector<Object> keys = new Vector<Object>();
        keys.add(applicationName);
        keys.add(serviceName);
        keys.add(token.getConsumerKey());
        keys.add(userId);
        
        AccessToken result=null;
		try {
			result = (AccessToken)loadAccessTokenFromNSF( key, keys );
		} catch (NotesException e) {
			e.printStackTrace();
		}

        logger.exiting( CLASSNAME, _method, result );
        return result;
    }
    public void saveAccessToken(AccessToken token) throws OAuthException 
    {
        final String _method = "saveAccessToken";
        logger.entering( CLASSNAME, _method, token );

        Session session = null;
        try {
            session = getAccessStoreSession();
            String key = getAccessViewKey(token.getAppId(), token.getServiceName(), token.getConsumerKey(), token.getUserId() );
            
            Vector<Object> keys = new Vector<Object>();
            keys.add(token.getAppId());
            keys.add(token.getServiceName());
            keys.add(token.getConsumerKey());
            keys.add(token.getUserId());

            saveAccessTokenToNSF(session, token, key, keys);
        } finally {
            releaseAccessStoreSession( session );
        }
        logger.exiting( CLASSNAME, _method );
    }

    @Override
    protected Document writeAccessTokenToDocument( Database db, Document doc, AccessToken token ) throws NotesException
    {
        final String _method = "writeAccessTokenToDocument";
        logger.entering( CLASSNAME, _method, new Object[] { db, doc, token } );
        
        HashMap<String, String> itemsValues= new HashMap<String, String>();
        itemsValues.put(TF_FORM, ACCESSTOKEN_FORM);
        itemsValues.put(TF_APPID, token.getAppId().toLowerCase());
        itemsValues.put(TF_SERVICE, token.getServiceName());
        itemsValues.put(TF_USERID, token.getUserId());;
        itemsValues.put(TF_CONSUMERKEY,token.getConsumerKey());
        
        HashMap<String, String> itemsValuesToEncrypt= new HashMap<String, String>();
        itemsValuesToEncrypt.put(TF_ACCESSTOKEN, token.getAccessToken());
        itemsValuesToEncrypt.put(TF_ACCESSSECRET, token.getTokenSecret());

        Date ex = token.getExpiresIn();
        if(ex!=null) {
            doc.replaceItemValue(TF_EXPIRESIN, db.getParent().createDateTime(ex));
            itemsValues.put(TF_EXPIRESIN, db.getParent().createDateTime(ex).toString());
        }
        Date aex = token.getAuthorizationExpiresIn();
        if(aex!=null) {
            doc.replaceItemValue(TF_AUTHEXPIRESIN, db.getParent().createDateTime(aex));
            itemsValues.put(TF_AUTHEXPIRESIN, db.getParent().createDateTime(ex).toString());
        }
        
        if(StringUtil.isNotEmpty(token.getSessionHandle())){
        	itemsValues.put(TF_SESSIONHANDLE, token.getSessionHandle());
        	doc.replaceItemValue(TF_SESSIONHANDLE, token.getSessionHandle());
        }

        logger.exiting( CLASSNAME, _method, doc );
        
        String encrytedDocId=CredStoreUtil.createDocWithEncryption(db, token.getAppId(), token.getServiceName(), itemsValues, itemsValuesToEncrypt);
        doc=db.getDocumentByUNID(encrytedDocId);

        logger.exiting( CLASSNAME, _method, doc );
        return doc;
    }

    @Override
    protected int getUpdatedByIndex()
    {
        return 6;
    }

    /**
     * This method removes an access token, such as provided by an OpenSocial container into the database.  The combination of the five
     * parameters is considered to be a unique key
     * @param appId Application URI
     * @param serviceName Service associated with the application
     * @param consumerKey The consumer secret
     * @param moduleId Sub identifier
     * @param tokenName Token name
     * @param userId User submitting the request
     * @throws OAuthException
     */
    public void deleteAccessToken(String appId, String serviceName, String consumerKey, String userId ) throws OAuthException {
        deleteAccessToken(appId, serviceName, consumerKey, null, null, userId);
    }
    public void deleteAccessToken(String appId, String serviceName, String consumerKey, String moduleId, String tokenName, String userId ) throws OAuthException
    {
        final String _method = "deleteAccessToken";
        logger.entering( CLASSNAME, _method, new Object[] { appId, serviceName, consumerKey, moduleId, tokenName, userId } );

       String accessViewKey = getAccessViewKey(appId, serviceName, consumerKey, userId);
        Vector<Object> keys = new Vector<Object>();
        keys.add(appId);
        keys.add(serviceName);
        keys.add(consumerKey);
        keys.add(userId);

        deleteAccessTokenFromNSF( accessViewKey, keys );

        logger.exiting( CLASSNAME, _method );
    }

    protected String getAccessViewKey(String appId, String serviceName, String consumerKey, String userId)
    {
        StringBuilder sb = new StringBuilder( 200 );
        sb.append( appId ).append( '|' ).
        append( serviceName ).append( '|' ).
        append( consumerKey ).append( '|' ).
        append( userId );
        return sb.toString();
    }

	public void store(String service, String type, String user,	Object credentials) throws CredentialStoreException {
		final String _method = "saveAccessToken";
		
		
		AccessToken token=(AccessToken)credentials;
        logger.entering( CLASSNAME, _method, token );

        Session session = null;
        try {
            session = getAccessStoreSession();
        
            String key = getAccessViewKey(token.getServiceName(),token.getAppId(), token.getConsumerKey(), token.getUserId() );
          
            Vector<Object> keys = new Vector<Object>();         
            keys.add(token.getAppId().toLowerCase());
            keys.add(token.getServiceName());
            keys.add(token.getConsumerKey());
            keys.add(token.getUserId());
            

            saveAccessTokenToNSF(session, token, key, keys);
        } catch (OAuthException ex) {
        	OAuthException oae = new OAuthException(ex);
            logger.throwing( CLASSNAME, _method, oae );
            try {
				throw oae;
			} catch (OAuthException e) {
				e.printStackTrace();
			}
        } finally {
            releaseAccessStoreSession( session );
        }
        logger.exiting( CLASSNAME, _method );
	}

	public void remove(String service, String type, String user)
			throws CredentialStoreException {
		
		
	}

	@Override
	protected Session getConsumerStoreSession() throws NotesException {
		NotesContext nc = NotesContext.getCurrentUnchecked();
        if(nc!=null) {
            return nc.getSessionAsSigner();
        }
        return null;
	}

	@Override
	 protected Session getAccessStoreSession() {
        NotesContext nc = NotesContext.getCurrentUnchecked();
        if(nc!=null) {
            return nc.getCurrentSession();
        }
        return null;
    } 	    
  
}