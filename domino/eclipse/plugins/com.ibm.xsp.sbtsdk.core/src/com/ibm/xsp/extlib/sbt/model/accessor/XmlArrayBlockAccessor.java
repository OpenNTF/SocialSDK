/*
 * © Copyright IBM Corp. 2010
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

package com.ibm.xsp.extlib.sbt.model.accessor;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.faces.FacesException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.commons.util.AbstractIOException;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMAccessor;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.XResult;
//import com.ibm.commons.xml.io.XmlSerializer;
import com.ibm.xsp.extlib.sbt.model.RestDataBlockAccessor;
import com.ibm.xsp.extlib.sbt.model.RestDataSource;


/**
 * Data accessor holding XML documents.
 * <p>
 * </p>
 * @author Philippe Riand
 */
public abstract class XmlArrayBlockAccessor extends RestDataBlockAccessor {
    
    private static final long serialVersionUID = 1L;
    
    public static class XmlBlock extends Block {
        private static final long serialVersionUID = 1L;
        private Document doc;
        private transient Object[] nodes;
        public XmlBlock() {} // Serializable
        public XmlBlock(int index, Document doc) {
            super(index);
            this.doc = doc;
        }
        public Document getDocument() {
            return doc;
        }
        @Override
        public int getLength() {
            return nodes.length;
        }
        @Override
        public Object getData(int index) {
            return nodes[index];
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            super.writeExternal(out);
            try {
//                XmlSerializer.writeDOMObject(out, doc);
            } catch(Throwable ex) {
                throw new AbstractIOException(ex);
            }
        }
        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            super.readExternal(in);
            try {
//                this.doc = XmlSerializer.readDOMObject(in);
                this.nodes = null;
            } catch(Throwable ex) {
                throw new AbstractIOException(ex);
            }
        }
    }
    
    public XmlArrayBlockAccessor() {} // Serializable
    public XmlArrayBlockAccessor(RestDataSource ds) {
        super(ds);
    }


    // ================================================================
    // Access to the nodes
    // ================================================================

    @Override
    protected void blockLoaded(Block b) {
        XmlBlock xmlBlock = (XmlBlock)b;
        try {
            // Initialize the document with its namespace context, for XPath evaluation
            DOMUtil.setSelectionNamespaces(xmlBlock.getDocument(),getNamespaceContext());
            
            // Extract the entry nodes
            String xpathExpr=getEntryXPath();
            if(StringUtil.isNotEmpty(xpathExpr)) {
                XResult r = DOMUtil.evaluateXPath(xmlBlock.getDocument(),xpathExpr);
                xmlBlock.nodes = r.getNodes();
            } else {
                xmlBlock.nodes = getRootNodes(xmlBlock.getDocument());
            }
            // Extract the total count
            String countXPath=getTotalCountXPath();
            if(StringUtil.isNotEmpty(countXPath)) {
                int count = (int)DOMAccessor.getIntValue(xmlBlock.getDocument(),countXPath);
                if(count>0) {
                    setTotalCount(count);
                }
            }
        } catch (XMLException e) {
            throw new FacesException(e); // Should not happen anyway
        }
    }
    private Node[] getRootNodes(Document doc) {
        NodeList l = doc.getChildNodes();
        if(l!=null && l.getLength()>0) {
            Node[] nodes = new Node[l.getLength()];
            for(int i=0; i<nodes.length; i++) {
                nodes[i] = l.item(i);
            }
            return nodes;
        } else {
            return new Node[0];
        }
    }
    
    public NamespaceContext getNamespaceContext() {
        return null;
    }

    public String getEntryXPath() {
        return null;
    }

    public String getTotalCountXPath() {
        return null;
    }
}
