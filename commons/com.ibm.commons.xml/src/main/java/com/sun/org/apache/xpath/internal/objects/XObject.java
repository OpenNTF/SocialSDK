/*
 *  Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.sun.org.apache.xpath.internal.objects;

import javax.xml.transform.TransformerException;

import org.w3c.dom.NodeList;


/**
 * Sun implementation classes mock-up, for compilation
 * purposes.
 */
public class XObject {

	public static final int CLASS_NULL		= -1;
	public static final int CLASS_UNKNOWN	= 0;
	public static final int CLASS_BOOLEAN	= 1;
	public static final int CLASS_NUMBER	= 2;
	public static final int CLASS_STRING	= 3;
	public static final int CLASS_NODESET	= 4;
	
	public int getType() { throw new RuntimeException(); }
	public String str() { throw new RuntimeException(); }
	public boolean bool() throws TransformerException { throw new RuntimeException(); }
	public double num() throws TransformerException { throw new RuntimeException(); }
	public NodeList nodelist() { throw new RuntimeException(); }
}
