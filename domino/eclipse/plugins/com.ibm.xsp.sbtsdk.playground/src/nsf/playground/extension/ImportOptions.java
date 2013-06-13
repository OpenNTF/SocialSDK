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

package nsf.playground.extension;

import java.io.IOException;
import java.util.Properties;


/**
 * API importer options 
 */
public abstract class ImportOptions {

    public ImportOptions() {
	}
    
	public String adjustExplorerPath(String[] products, String path) throws IOException {
		return null;
	}

	public void createProperties(Properties properties, String[] products, String path) throws IOException {
	}
}
