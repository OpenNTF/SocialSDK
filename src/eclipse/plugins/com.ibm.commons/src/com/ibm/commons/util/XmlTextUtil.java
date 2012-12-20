package com.ibm.commons.util;

public class XmlTextUtil {

	   private static String[] xmlEntities=null;
	   
	   static  {
	        String[] entities=new String[256];
	        entities[34] ="quot";           // '"' //$NON-NLS-1$
	        entities[38] ="amp";            // '&' //$NON-NLS-1$
	        entities[60] ="lt";             // '<' //$NON-NLS-1$
	        entities[62] ="gt";             // '>' //$NON-NLS-1$
	        xmlEntities=entities;
	   }
	   public static String getEntity(char c) {
	            return xmlEntities[c];
	    }
	
	    public static String escapeXMLChars(String s) {
	    	  if( StringUtil.isEmpty(s) ) {
	              return s;
	          }
	          FastStringBuffer b = null;
	          int length = s.length();
	          for( int i=0; i<length; i++ ) {
	              char c = s.charAt(i);

	              // Is it a specific entity ?
	              String replaceLabel=null;
	              String replaceNumber=null;
	             
	              if (c<256) {
	                  replaceLabel=xmlEntities[c];
	              } 
	              if (replaceLabel!=null || replaceNumber!=null) {
	                  if( b==null ) {
	                      b = new FastStringBuffer();
	                      b.append(s, 0, i);
	                  }
	                  b.append("&"); //$NON-NLS-1$
	                  if (replaceLabel!=null) {
	                      b.append(replaceLabel);
	                  } else {
	                      b.append("#"); //$NON-NLS-1$
	                      b.append(replaceNumber);
	                  }
	                  b.append(";"); //$NON-NLS-1$
	              } else if( b!=null ) {
	                  b.append(c);
	              }
	          }
	          return b!=null ? b.toString() : s;
	    	
	    }
}
	    
