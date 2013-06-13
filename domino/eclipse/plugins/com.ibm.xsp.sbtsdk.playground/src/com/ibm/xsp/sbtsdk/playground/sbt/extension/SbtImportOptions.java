/* ***************************************************************** */
/*                                                                   */
/* IBM Confidential                                                  */
/*                                                                   */
/* OCO Source Materials                                              */
/*                                                                   */
/* Copyright IBM Corp. 2004, 2011                                    */
/*                                                                   */
/* The source code for this program is not published or otherwise    */
/* divested of its trade secrets, irrespective of what has been      */
/* deposited with the U.S. Copyright Office.                         */
/*                                                                   */
/* ***************************************************************** */

package com.ibm.xsp.sbtsdk.playground.sbt.extension;

import java.io.IOException;
import java.util.Properties;

import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;

import nsf.playground.extension.ImportOptions;


/**
 * API importer options 
 */
public class SbtImportOptions extends ImportOptions {

	// List of supported products
	private enum Product {
		UNKNOWN,
		DOMINO,
		CONNECTIONS,
		SMARTCLOUD,
	};
	private static Product findProduct(String[] products) {
		if(products!=null) {
			// Make Connections a priority
			for(int i=0; i<products.length; i++) {
				if(StringUtil.indexOfIgnoreCase(products[i],"connections")>=0) {
					return Product.CONNECTIONS;
				}
			}
			// Else, look for the others
			for(int i=0; i<products.length; i++) {
				if(StringUtil.indexOfIgnoreCase(products[i],"domino")>=0) {
					return Product.DOMINO;
				}
				if(StringUtil.indexOfIgnoreCase(products[i],"smartcloud")>=0) {
					return Product.SMARTCLOUD;
				}
			}
		}
		return Product.UNKNOWN;
	}
	
	
	public static final SbtImportOptions instance = new SbtImportOptions();
	
    public SbtImportOptions() {
	}
    
    @Override
	public String adjustExplorerPath(String[] products, String path) throws IOException {
		Product p = findProduct(products);
		if(p!=Product.UNKNOWN) {
			// If already imported in the right place, do nothing and return the path
			if(   path.startsWith("/Domino")
			   || path.startsWith("/Connections")
		       || path.startsWith("/Smartcloud")
				) {
				return path;
			}
			
			// Else, add the proper prefix
			switch(p) {
				case DOMINO:		return PathUtil.concat("/Domino", path, '/'); 
				case CONNECTIONS:	return PathUtil.concat("/Connections", path, '/'); 
				case SMARTCLOUD:	return PathUtil.concat("/SmartCloud", path, '/'); 
			}

			// Should never be here...
		}
		return null;
	}

    @Override
	public void createProperties(Properties properties, String[] products, String path) throws IOException {
		Product p = findProduct(products);
		if(p==Product.UNKNOWN) {
			if(path.startsWith("/Domino")) {
				p = Product.DOMINO;
			} else if(path.startsWith("/Connections")) {
				p = Product.CONNECTIONS;
			} else if(path.startsWith("/SmartCloud")) {
				p = Product.SMARTCLOUD;
			}
		}
		if(p!=Product.UNKNOWN) {
			// Else, add the proper prefix
			switch(p) {
				case DOMINO: {
					properties.put("basedocurl", "http://www-10.lotus.com/ldd/ddwiki.nsf");
					properties.put("endpoint", "domino");
				} break;
				case CONNECTIONS: {
					properties.put("endpoint", "connections");
				} break;
				case SMARTCLOUD: {
					properties.put("endpoint", "smartcloud");
				} break;
			}
		}
	}
}
