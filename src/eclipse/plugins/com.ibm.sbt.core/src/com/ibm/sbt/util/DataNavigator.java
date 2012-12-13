/*
 * © Copyright IBM Corp. 2012
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

package com.ibm.sbt.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonFactory;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.util.XMIConverter;


/**
 * Data Navigator.
 * Supported XPath:
 *   //
 *   [prefix:]name
 *   [xx]
 *   [
 * @author Philippe Riand
 */
public abstract class DataNavigator implements Cloneable /*extends DataObject*/ {
    
    private static final int TYPE_NODE          = -1;
    private static final int TYPE_OBJECT        = 0;
    private static final int TYPE_STRING        = 1;
    private static final int TYPE_INT           = 2;
    private static final int TYPE_LONG          = 3;
    private static final int TYPE_DOUBLE        = 4;
    private static final int TYPE_BOOLEAN       = 5;
    private static final int TYPE_DATE          = 6;
    
    
    public static interface ISelect {
        public boolean matches(Object object);
    }
    
    public static class Xml extends DataNavigator {
        private NamespaceContext nsContext;
        private Node root;
        private List<Object> currentNodes;
        
        public Xml(Node root) {
            this.root = root;
            if(root!=null) {
                this.currentNodes = Collections.singletonList((Object)root);
                this.nsContext = DOMUtil.getSelectionNamespaces(DOMUtil.getOwnerDocument(root));
            } else {
                this.currentNodes = Collections.emptyList();
            }
        }
        protected Xml(Xml copy, List<Object> nodes) {
            this.root = copy.root;
            this.nsContext = copy.nsContext;
            this.currentNodes = nodes;
        }
        @Override
        public List<Object> getCurrentNodes() {
            return currentNodes;
        }
        @Override
        protected DataNavigator create(List<Object> nodes) {
            return new Xml(this,nodes);
        }
        @Override
        protected Node root() {
            return root;
        }
        @Override
        protected List<Object> children() {
            return currentNodes;
        }
        @Override
        protected void extract(Object node, List<Object> result, String name, int type, boolean global) {
            // Split the name between the namespace and the local name
            String uri = null;
            String localName = null;
            if(StringUtil.isNotEmpty(name)) {
                int pos = name.indexOf(':');
                if(pos>=0) {
                    if(nsContext==null) {
                        return;
                    }
                    uri = nsContext.getNamespaceURI(name.substring(0,pos));
                    if(uri==null) {
                        return;
                    }
                    localName = name.substring(pos+1);
                } else {
                    localName = name;
                }
            } else {
                localName = name;
            }
            // Then find the children for all the current nodes
            if(type==TYPE_NODE) {
                extractChildren(result, (Node)node, uri, localName, global);
            } else {
                extractValues(result, (Node)node, uri, localName, type, global);
            }
        }
        private void extractChildren(List<Object> result, Node parent, String uri, String name, boolean global) {
            // Look for an attribute
            if(name.startsWith("@")) {
                if(parent.getNodeType()==Node.ELEMENT_NODE) {
                    Element e = (Element)parent;
                    String attrName = name.substring(1);
                    Attr attr = (uri==null) ? e.getAttributeNode(attrName) : e.getAttributeNodeNS(uri,attrName);
                    if(attr!=null) {
                        result.add(attr);
                    }
                }
            } else {
                if(name.equals(".")) {
                    if(!result.contains(parent)) {
                        result.add(parent);
                    }
                } else {
                    NodeList children = parent.getChildNodes();
                    for(int i=0; i<children.getLength(); i++) {
                        Node node = children.item(i);
                        if(node.getNodeType()==Node.ELEMENT_NODE && matches(node,uri,name)) {
                            if(!result.contains(node)) {
                                result.add(node);
                            }
                        }
                    }
                }
            }
            if(global) {
                NodeList list = parent.getChildNodes();
                for(int i=0; i<list.getLength(); i++) {
                    Node node = list.item(i);
                    if(node.getNodeType()==Node.ELEMENT_NODE) {
                        extractChildren(result, node, uri, name, global);
                    }
                }
            }
        }
        private void extractValues(List<Object> result, Node parent, String uri, String name, int type, boolean global) {
            // Look for an attribute
            if(name.startsWith("@")) {
                if(parent.getNodeType()==Node.ELEMENT_NODE) {
                    Element e = (Element)parent;
                    String attrName = name.substring(1);
                    Attr attr = (uri==null) ? e.getAttributeNode(attrName) : e.getAttributeNodeNS(uri,attrName);
                    if(attr!=null) {
                        Object v = convertTo(attr.getValue(),type);
                        if(v!=null) {
                            result.add(v);
                        }
                    }
                }
                return;
            } else {
                if(name.equals(".")) {
                    if(parent.getNodeType()==Node.ELEMENT_NODE) {
                        String text = DOMUtil.getText(parent);
                        if(StringUtil.isNotEmpty(text)) {
                            Object v = convertTo(text,type);
                            result.add(v);
                        }
                    }
                } else {
                    NodeList children = parent.getChildNodes();
                    for(int i=0; i<children.getLength(); i++) {
                        Node node = children.item(i);
                        if(node.getNodeType()==Node.ELEMENT_NODE && matches(node,uri,name)) {
                            String text = DOMUtil.getText(node);
                            if(StringUtil.isNotEmpty(text)) {
                                Object v = convertTo(text,type);
                                result.add(v);
                            }
                        }
                    }
                }
            }
            if(global) {
                NodeList list = parent.getChildNodes();
                for(int i=0; i<list.getLength(); i++) {
                    Node node = list.item(i);
                    if(node.getNodeType()==Node.ELEMENT_NODE) {
                        extractValues(result, node, uri, name, type, global);
                    }
                }
            }
        }
        private boolean matches(Node node, String uri, String name) {
            if(name.equals("*")) {
                return true;
            }
            if(uri==null) {
                return name.equals(node.getLocalName());
            } else {
                return uri.equals(node.getNamespaceURI()) && name.equals(node.getLocalName());
            }
        }
        private Object convertTo(String value, int type) {
            try {
                switch(type) {
                    case TYPE_INT:      return Integer.parseInt(value);
                    case TYPE_LONG:     return Long.parseLong(value);
                    case TYPE_DOUBLE:   return Double.parseDouble(value);
                    case TYPE_BOOLEAN:  return Boolean.parseBoolean(value);
                    case TYPE_DATE:     return XMIConverter.parseUtilDate(value);
                }
                return value;
            } catch(NumberFormatException ex) {
                return null;
            }
        }
    }

    
    public static class Json extends DataNavigator {
        private JsonFactory factory;
        private Object root;
        private List<Object> currentNodes;
        
        public Json(Object root) {
            this(null,root);
        }
        public Json(JsonFactory factory, Object root) {
            this.root = root;
            if(root!=null) {
                this.currentNodes = Collections.singletonList(root);
            } else {
                this.currentNodes = Collections.emptyList();
            }
            if(factory==null) {
                factory = JsonJavaFactory.instanceEx;
            }
            this.factory = factory;
        }
        protected Json(Json copy, List<Object> nodes) {
            this.root = copy.root;
            this.factory = copy.factory;
            this.currentNodes = nodes;
        }
        @Override
        public List<Object> getCurrentNodes() {
            return currentNodes;
        }
        @Override
        protected DataNavigator create(List<Object> nodes) {
            return new Json(this,nodes);
        }
        @Override
        protected Object root() {
            return root;
        }
        @Override
        protected List<Object> children() {
            return currentNodes;
        }

        @Override
        protected void extract(Object node, List<Object> result, String name, int type, boolean global) {
            // Find the children for all the current nodes
            if(type==TYPE_NODE) {
                extractChildren(result, node, name, global);
            } else {
                extractValues(result, node, name, type, global);
            }
        }
        private void extractChildren(List<Object> result, Object parent, String name, boolean global) {
            try {
                if(name.equals("*")) {
                    for(Iterator<String> it=factory.iterateObjectProperties(parent); it.hasNext(); ) {
                        String prop = it.next();
                        _extractChildren(result, parent, prop, global);
                    }
                } if(name.equals(".")) {
                    if(factory.isObject(parent)) {
                        if(!result.contains(parent)) {
                            result.add(parent);
                        }
                    }
                } else {
                    _extractChildren(result, parent, name, global);
                }
            } catch(JsonException ex) {}
        }
        private void _extractChildren(List<Object> result, Object parent, String name, boolean global) throws JsonException {
            Object prop = factory.getProperty(parent,name);
            if(factory.isObject(prop)) {
                if(!result.contains(prop)) {
                    result.add(prop);
                }
            } else if(factory.isArray(prop)) {
                for( Iterator<Object> it=factory.iterateArrayValues(prop); it.hasNext(); ) {
                    Object val = it.next();
                    if(factory.isObject(val) && !result.contains(val)) {
                        result.add(val);
                    }
                }
            }
            if(global) {
                _extractChildrenGlobal(result, parent, name);
            }
        }
        private void _extractChildrenGlobal(List<Object> result, Object parent, String name) throws JsonException {
            for(Iterator<String> it=factory.iterateObjectProperties(parent); it.hasNext(); ) {
                Object o = factory.getProperty(parent, it.next());
                if(factory.isObject(o)) {
                    extractChildren(result, o, name, true);
                } else if(factory.isArray(o)) {
                    for( Iterator<Object> it2=factory.iterateArrayValues(o); it2.hasNext(); ) {
                        Object val = it2.next();
                        extractChildren(result, val, name, true);
                    }
                }
            }
        }
        private void extractValues(List<Object> result, Object parent, String name, int type, boolean global) {
            try {
                _extractValues(result, parent, name, type, global);
            } catch(JsonException ex) {}
        }
        private void _extractValues(List<Object> result, Object parent, String name, int type, boolean global) throws JsonException {
            if(name.equals("*")) {
                for(Iterator<String> it=factory.iterateObjectProperties(parent); it.hasNext(); ) {
                    String prop = it.next();
                    Object value = factory.getProperty(parent,prop);
                    _addValues(result, value, type);
                }
            } if(name.equals(".")) {
                // Not supported here... Only for XML!
            } else {
                Object value = factory.getProperty(parent,name);
                _addValues(result, value, type);
            }
            if(global) {
                _extractValuesGlobal(result, parent, name, type);
            }
        }
        private void _extractValuesGlobal(List<Object> result, Object parent, String name, int type) throws JsonException {
            for(Iterator<String> it=factory.iterateObjectProperties(parent); it.hasNext(); ) {
                Object o = factory.getProperty(parent, it.next());
                if(factory.isObject(o)) {
                    _extractValues(result, o, name, type, true);
                } else if(factory.isArray(o)) {
                    for( Iterator<Object> it2=factory.iterateArrayValues(o); it2.hasNext(); ) {
                        Object val = it2.next();
                        _extractValuesGlobal(result, val, name, type);
                    }
                }
            }
        }
        private void _addValues(List<Object> result, Object value, int type) throws JsonException {
            if(factory.isString(value)) {
                result.add(convertTo(factory.getString(value), type));
            } else if(factory.isNumber(value)) {
                result.add(convertTo(factory.getNumber(value), type));
            } else if(factory.isBoolean(value)) {
                result.add(convertTo(factory.getBoolean(value), type));
            } else if(factory.isArray(value)) {
                for( Iterator<Object> it=factory.iterateArrayValues(value); it.hasNext(); ) {
                    Object val = it.next();
                    _addValues(result, val, type);
                }
            }
        }
        private Object convertTo(String value, int type) {
            try {
                switch(type) {
                    case TYPE_INT:      return Integer.parseInt(value);
                    case TYPE_LONG:     return Long.parseLong(value);
                    case TYPE_DOUBLE:   return Double.parseDouble(value);
                    case TYPE_BOOLEAN:  return Boolean.parseBoolean(value);
                    case TYPE_DATE:     return XMIConverter.parseUtilDate(value);
                }
                return value;
            } catch(NumberFormatException ex) {
                return null;
            }
        }
        private Object convertTo(double value, int type) {
            try {
                switch(type) {
                    case TYPE_INT:      return (int)value;
                    case TYPE_LONG:     return (long)value;
                    case TYPE_DOUBLE:   return value;
                    case TYPE_BOOLEAN:  return value!=0.0;
                    case TYPE_DATE:     return new Date((long)value);
                }
                return value;
            } catch(NumberFormatException ex) {
                return null;
            }
        }
        private Object convertTo(boolean value, int type) {
            try {
                switch(type) {
                    case TYPE_INT:      return Integer.valueOf(value?1:0);
                    case TYPE_LONG:     return Long.valueOf(value?1L:0L);
                    case TYPE_DOUBLE:   return Double.valueOf(value?1.0:0.0);
                    case TYPE_BOOLEAN:  return value;
                    case TYPE_DATE:     return new Date(0L);
                }
                return value;
            } catch(NumberFormatException ex) {
                return null;
            }
        }
    }
    
    public DataNavigator() {
    }
    
    @Override
    public DataNavigator clone() {
        try {
            return (DataNavigator)super.clone();
        } catch(CloneNotSupportedException ex){return null;} 
    }
    
    /**
     * Get the current nodes from this navigator.
     */
    public abstract List<Object> getCurrentNodes();
    
    /**
     * Get the current first node from this navigator.
     */
    public Object getCurrentNode() {
        List<Object> l = getCurrentNodes();
        if(l!=null && !l.isEmpty()) {
            return l.get(0);
        }
        return null;
    }
    
    /**
     * Get the number of elements in this navigator.
     * @return
     */
    public int getCount() {
        return children().size();
        
    }
    
    /**
     * Create a navigator from one indexed entry.
     * @param index
     * @return
     */
    public DataNavigator get(int index) {
        List<Object> l = children();
        if(index>=0 && index<l.size()) {
            List<Object> singleton = Collections.singletonList(l.get(index));
            return create(singleton);
        }
        return create(Collections.emptyList());
    }
    
    /**
     * Extract a subpath from the current navigator. 
     * @param path
     * @return
     */
    public DataNavigator get(String path) {
        List<Object> result = xpath(path, TYPE_NODE);
        return create(result); 
    }
    
    /**
     * Select. 
     * @param path
     * @return
     */
    public DataNavigator select(ISelect select) {
        List<Object> l = children();
        int size = l.size(); 
        if(size==1) {
            Object o = l.get(0);
            if(select==null || select.matches(o)) {
                return create(Collections.singletonList(o));
            }
        } else if(size>1) {
            List<Object> result = new ArrayList<Object>();
            for(int i=0; i<size; i++) {
                Object o = l.get(i);
                if(select==null || select.matches(o)) {
                    result.add(o);
                }
            }
            return create(result);
        }
        return this;
    }
    public DataNavigator selectEq(final String path, final String value) {
        return select(new ISelect() {
            @Override
			public boolean matches(Object object) {
                List<Object> res = DataNavigator.this.xpath(object,path,TYPE_STRING);
                for(int i=0; i<res.size(); i++) {
                    if(StringUtil.equals((String)res.get(i), value)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }
    public DataNavigator selectEq(final String path, final double value) {
        return select(new ISelect() {
            @Override
			public boolean matches(Object object) {
                List<Object> res = DataNavigator.this.xpath(object,path,TYPE_DOUBLE);
                for(int i=0; i<res.size(); i++) {
                    if(((Number)res.get(i)).doubleValue()==value) {
                        return true;
                    }
                }
                return false;
            }
        });
    }
    public DataNavigator selectEq(final String path, final boolean value) {
        return select(new ISelect() {
            @Override
			public boolean matches(Object object) {
                List<Object> res = DataNavigator.this.xpath(object,path,TYPE_BOOLEAN);
                for(int i=0; i<res.size(); i++) {
                    if(((Boolean)res.get(i)).booleanValue()==value) {
                        return true;
                    }
                }
                return false;
            }
        });
    }
    
    
    public List<Object> nodes(String path) {
        return xpath(path, TYPE_NODE);
    }
    public Object node(String path) {
        return _value(path,TYPE_NODE,null);
    }
    public Object node(String path, Object def) {
        return _value(path,TYPE_NODE,def);
    }
    
    public List<Object> values(String path) {
        return xpath(path, TYPE_OBJECT);
    }
    public Object value(String path) {
        return _value(path,TYPE_OBJECT,null);
    }
    public Object value(String path, Object def) {
        return _value(path,TYPE_OBJECT,def);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<String> stringValues(String path) {
        return (List)xpath(path, TYPE_STRING);
    }
    public String stringValue(String path) {
        return (String)_value(path,TYPE_STRING,null);
    }
    public String stringValue(String path, String def) {
        return (String)_value(path,TYPE_STRING,def);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Integer> intValues(String path) {
        return (List)xpath(path, TYPE_INT);
    }
    public int intValue(String path) {
        return (Integer)_value(path,TYPE_INT,null);
    }
    public int intValue(String path, String def) {
        return (Integer)_value(path,TYPE_INT,def);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Long> longValues(String path) {
        return (List)xpath(path, TYPE_LONG);
    }
    public long longValue(String path) {
        return (Long)_value(path,TYPE_LONG,null);
    }
    public long longValue(String path, String def) {
        return (Long)_value(path,TYPE_LONG,def);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Double> doubleValues(String path) {
        return (List)xpath(path, TYPE_DOUBLE);
    }
    public double doubleValue(String path) {
        return (Double)_value(path,TYPE_DOUBLE,null);
    }
    public double doubleValue(String path, String def) {
        return (Double)_value(path,TYPE_DOUBLE,def);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Boolean> booleanValues(String path) {
        return (List)xpath(path, TYPE_BOOLEAN);
    }
    public boolean booleanValue(String path) {
        return (Boolean)_value(path,TYPE_BOOLEAN,false);
    }
    public boolean booleanValue(String path, String def) {
        return (Boolean)_value(path,TYPE_BOOLEAN,def);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Date> dateValues(String path) {
        return (List)xpath(path, TYPE_DATE);
    }
    public Date dateValue(String path) {
        return (Date)_value(path,TYPE_DATE,null);
    }
    public Date dateValue(String path, String def) {
        return (Date)_value(path,TYPE_DATE,def);
    }
    
    
    //
    // Abstract methods that must be implemented
    //
    protected abstract Object root();
    protected abstract List<Object> children();
    protected abstract DataNavigator create(List<Object> nodes); 
    protected abstract void extract(Object node, List<Object> result, String name, int type, boolean global);

    
    //
    // Utility: access to the values
    //
    private Object _value(String path, int type, Object def) {
        List<Object> l = xpath(path, type);
        if(l.size()==1) {
            return l.get(0);
        }
        return def;
    }
    
    //
    // Internal path execution
    //
    private List<Object> xpath(String path, int type) {
        return xpath(children(), path, type);
    }
    private List<Object> xpath(List<Object> current, String path, int type) {
        if(StringUtil.isNotEmpty(path)) {
            for(int start=0; start<path.length(); ) {
                int end = path.indexOf('/',start);
                if(end!=start) {
                    List<Object> result = new ArrayList<Object>();
                    boolean global = start>=2 && path.charAt(start-1)=='/' && path.charAt(start-2)=='/';
                    if(end>=0) {
                        String part = path.substring(start,end);
                        start = end+1;
                        int size = current.size();
                        for(int i=0; i<size; i++) {
                            extract(current.get(i),result,part,TYPE_NODE,global);
                        }
                        current = result;
                    } else {
                        String part = start>0 ? path.substring(start) : path;
                        int size = current.size();
                        for(int i=0; i<size; i++) {
                            extract(current.get(i),result,part,type,global);
                        }
                        return result;
                    }
                } else {
                    start++;
                }
            }
        }
        return Collections.emptyList();
    }
    private List<Object> xpath(Object current, String path, int type) {
        if(StringUtil.isNotEmpty(path)) {
            for(int start=0; start<path.length(); ) {
                List<Object> result = new ArrayList<Object>();
                int end = path.indexOf('/',start);
                if(end!=start) {
                    boolean global = start>=2 && path.charAt(start-1)=='/' && path.charAt(start-2)=='/';
                    if(end>=0) {
                        String part = path.substring(start,end);
                        start = end+1;
                        extract(current,result,part,TYPE_NODE,global);
                    } else {
                        String part = start>0 ? path.substring(start) : path;
                        extract(current,result,part,type,global);
                        return result;
                    }
                } else {
                    start++;
                }
            }
        }
        return Collections.emptyList();
    }

}
