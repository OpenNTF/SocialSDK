/*
 * © Copyright IBM Corp. 2012-2013
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

package com.ibm.commons.util.profiler;

import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.TextUtil;


/**
 * Estimate the size of an object in memory. This is just an estimation and is
 * used by profilers to give the developer a fair estimation of the memory.
 * 
 * To get a better estimation a use of the Java 1.5 Instrumentation API can be used as well.
 * 
 * @ibm-not-published 
 */
public class MemoryInspector {
    
    private static final String ENTRY_TAG = "Object"; // $NON-NLS-1$
    
    
    /** 
     * Callback class used by the Inspector when inspecting an hierarchy of objects.
     * @author priand
     */
    public interface Callback {
        public void begin();
        public void end(long size);
        public Object startObject(Stack<Object> params, Object parent, Field parentField, Object object);
        public void endObject(Stack<Object> params, Object object, long objectSize, long childrenSize);
    }
    
    /**
     * Simple Stack Class.
     */
    public interface Stack<T> {
        public boolean isEmpty();
        public int size();
        public T pop();
        public void push(T o);
        public T get();
        public T get(int idx);
    }
    @SuppressWarnings("unchecked") // $NON-NLS-1$
    private static final class StackImpl<T> implements Stack<T> {
        private int count;
        private Object[] data;
        StackImpl() {
            data = new Object[128];
        }
        public boolean isEmpty() {
            return count==0;
        }
        public int size() {
            return count;
        }
        public T pop() {
            return (T)data[--count];
        }
        public void push(T o) {
            if(count==data.length) {
                Object[] nd = new Object[count+32];
                System.arraycopy(data, 0, nd, 0, count);
                data = nd;
            }
            data[count++] = o;
        }
        public T get() {
            return (T)data[count-1];
        }
        public T get(int idx) {
            return (T)data[count-idx-1];
        }
    }
    
    /**
     * Callback implementation that retains all the entries in memory.
     * @author priand
     */
    public static class CollectEntryCallBack implements Callback {
        public static class Entry {
            Entry       parent;
            Entry       next;
            Entry       firstChild;
            Field       parentField;
            Object      object;
            long        objectSize;
            long        childrenSize;
            public Entry(Entry parent, Field parentField, Object object) {
                this.parent = parent;
                this.parentField = parentField;
                this.object = object;
            }
            public Entry getParent() {
                return parent;
            }
            public boolean isRoot() {
            	return parent==null;
            }
            public Field getParentField() {
                return parentField;
            }
            public Object getObject() {
                return object;
            }
            public long getObjectSize() {
                return objectSize;
            }
            public long getChildrenSize() {
                return childrenSize;
            }
            public Entry getNext() {
                return next;
            }
            public Entry getFirstChild() {
                return firstChild;
            }
            void add(Entry child) {
                child.next = firstChild;
                firstChild = child;
            }
        }
        
        private Entry rootEntry;
        private Stack<Entry> stack = new StackImpl<Entry>();

        public CollectEntryCallBack() {
        }

        public Entry getRootEntry() {
            return rootEntry;
        }
        
        public Stack<Entry> getEntryStack() {
            return stack;
        }

        public void begin() {
            this.rootEntry = createRootEntry();
            stack.push(rootEntry);
        }
        
        protected Entry createRootEntry() {
            return new Entry(null,null,"<root>"); // $NON-NLS-1$
        }
        
        public void end(long size) {
            this.rootEntry.childrenSize = size;
        }
        
        public Object startObject(Stack<Object> params, Object parent, Field parentField, Object object) {
            Entry e = createEntry(params,parent,parentField,object);
            if(isPersistent(params,parentField,object)) {
                stack.get().add(e);
                stack.push(e);
            }
            return e;
        }
        
        public void endObject(Stack<Object> params, Object object, long objectSize, long childrenSize) {
            Entry e = stack.get();
            if(e.object==object) {
                e.objectSize = objectSize;
                e.childrenSize = childrenSize;
                stack.pop();
            }
        }

        public Entry createEntry(Stack<Object> params, Object parent, Field parentField, Object object) {
            Entry e = new Entry((Entry)parent,parentField,object);
            return e;
        }
        
        public boolean isPersistent(Stack<Object> params, Field parentField, Object object) {
            return true;
        }
    }

    /**
     * Class that dumps a collection of entries.
     * 
     * @author priand
     */
    public static class CollectEntryDump {
        
        public enum Format {
            FORMAT_TEXT,
            FORMAT_XML
        }
        private CollectEntryCallBack callBack;
        private int initialLevel; 
        private Format format;
        
        public CollectEntryDump(CollectEntryCallBack callBack, Format format) {
            this.callBack = callBack;
            this.format = format;
        }
        
        public int getInitialLevel() {
            return initialLevel;
        }
        
        public void setInitialLevel(int initialLevel) {
            this.initialLevel = initialLevel;
        }
        
        public CollectEntryCallBack getCallBack() {
            return callBack;
        }
        
        public void dump(PrintStream ps) {
            dump(ps,callBack.getRootEntry(),initialLevel);
        }
        
        protected void dump(PrintStream ps, CollectEntryCallBack.Entry entry, int level) {
            boolean p = shouldDump(ps,entry,level);
            if(p) {
                printEntryStart(ps, entry, level);
                level++;
            }
            for( CollectEntryCallBack.Entry c=entry.getFirstChild(); c!=null; c=c.getNext()) {
                dump(ps,c,level);
            }
            if(p) {
                level--;
                printEntryEnd(ps, entry, level);
            }
        }
        protected boolean shouldDump(PrintStream ps, CollectEntryCallBack.Entry entry, int level) {
            return true;
        }
        protected void printEntryStart(PrintStream ps, CollectEntryCallBack.Entry entry, int level) {
            printIndent(ps, level);
            StringBuilder b = new StringBuilder();
            Object o = entry.getObject();
            if(format==Format.FORMAT_TEXT) {
                String fn = getFieldName(entry);
                if(fn!=null) {
                    b.append(fn);
                    b.append(':');
                }
                b.append(o.getClass().getSimpleName());
                if(o.getClass().isArray()) {
                    b.append('[');
                    b.append(Integer.toString(Array.getLength(o)));
                    b.append(']');
                }
                b.append(", Size="); // $NON-NLS-1$
                b.append(Long.toString(entry.getObjectSize()));
                b.append(", Total Size="); // $NON-NLS-1$
                b.append(Long.toString(entry.getObjectSize()+entry.getChildrenSize()));

                appendObjectString(b, o);
                
                ps.println(b.toString());
            } else if(format==Format.FORMAT_XML) {
                ps.print("<");
                ps.print(ENTRY_TAG);
                if(!entry.isRoot()) {
                	printXmlAttr(ps,"fieldName",getFieldName(entry)); // $NON-NLS-1$
                	String className = o.getClass().getSimpleName();
                	if(o.getClass().isArray()) {
                		className += '[' + Integer.toString(Array.getLength(o)) + ']';
                	}
                	printXmlAttr(ps,"class",className); // $NON-NLS-1$
                    printXmlAttr(ps,"size",Long.toString(entry.getObjectSize())); // $NON-NLS-1$
                }
                printXmlAttr(ps,"totalSize",Long.toString(entry.getObjectSize()+entry.getChildrenSize())); // $NON-NLS-1$
                
                appendObjectString(b, o);
                if(b.length()>0) {
                    printXmlAttr(ps,"value",b.toString()); // $NON-NLS-1$
                    b.setLength(0);
                }

                if(entry.getFirstChild()==null) {
                    ps.println("/>");
                } else {
                    ps.println(">");
                }
            }
        }
        protected void printXmlAttr(PrintStream ps, String attrName, String attrValue) {
            if(StringUtil.isNotEmpty(attrValue)) {
                ps.print(" ");
                ps.print(attrName);
                ps.print("='");
                ps.print(TextUtil.toXMLString(attrValue));
                ps.print("'");
            }
        }
        protected void printEntryEnd(PrintStream ps, CollectEntryCallBack.Entry entry, int level) {
            if(format==Format.FORMAT_XML) {
                if(entry.getFirstChild()!=null) {
                    printIndent(ps, level);
                    ps.print  ("</");
                    ps.print  (ENTRY_TAG);
                    ps.println(">");
                }
            }
        }
        protected String getFieldName(CollectEntryCallBack.Entry entry) {
            if(entry.getParentField()!=null) {
                return entry.getParentField().getName();
            }
            return null;
        }
        protected void appendObjectString(StringBuilder b, Object o) {
            if(o instanceof String) {
                String s = format(o.toString());
                if(b.length()>0) {
                    b.append(", ");
                }
                b.append(s);
                return;
            }
            if(o instanceof Number) {
                String s = o.toString();
                if(b.length()>0) {
                    b.append(", ");
                }
                b.append(s);
                b.append("");
                return;
            }
            if(o instanceof Map<?,?>) {
                if(b.length()>0) {
                    b.append(", ");
                }
                b.append("count="); // $NON-NLS-1$
                b.append(((Map<?,?>)o).size());
                return;
            }
            if(o instanceof Map.Entry<?,?>) {
                if(b.length()>0) {
                    b.append(", ");
                }
                b.append("key="); // $NON-NLS-1$
                b.append(format(((Map.Entry<?,?>)o).getKey().toString()));
                return;
            }
            if(o instanceof List<?>) {
                if(b.length()>0) {
                    b.append(", ");
                }
                b.append("count="); // $NON-NLS-1$
                b.append(((List<?>)o).size());
                return;
            }
            if(o instanceof Set<?>) {
                if(b.length()>0) {
                    b.append(", ");
                }
                b.append("count="); // $NON-NLS-1$
                b.append(((Set<?>)o).size());
                return;
            }
            if(o.getClass().isPrimitive() ){
                if(b.length()>0) {
                    b.append(", ");
                }
                b.append(o.toString());
                return;
            }
        }
        protected void printIndent(PrintStream ps, int level) {
            for(int i=0; i<level; i++) {
                ps.print("  ");
            }
        }
        protected String format(String s) {
            s = TextUtil.toJavaString(s,false);
            if(s.length()>96) {
                s = s.substring(0,96) + "...";
            }
            return s;
        }
    }
    
    private Instrumentation instrumentation; 
    private Map<Object, Integer> visited = new IdentityHashMap<Object, Integer>();
    private IdentityHashMap<Class<?>, Field[]> fieldsCache = new IdentityHashMap<Class<?>, Field[]>(); 

    public MemoryInspector() {
        this(new IdentityHashMap<Object, Integer>(), new IdentityHashMap<Class<?>, Field[]>());
    }

    public MemoryInspector(Map<Object, Integer> visited, IdentityHashMap<Class<?>, Field[]> classCache) {
        this.instrumentation = JVMPIInterface.getInstrumentation();
        this.fieldsCache = classCache;
    }
    
    public Instrumentation getInstrumentation() {
        return instrumentation;
    }
    
    public Map<Object, Integer> getVisited() {
        return visited;
    }
    
    public long inspect(Object object, Callback cb) throws IllegalAccessException {
        StackImpl<Object> params = new StackImpl<Object>();
        long size = 0;
        cb.begin();
        params.push(object);
        try {
            size = inspect(params, null, null, object, cb);
            return size;
        } finally {
            cb.end(size);
            params.pop();
        }
    }

    protected long inspect(Stack<Object> params, Object parent, Field parentField, Object object, Callback cb) throws IllegalAccessException {
        // Look if the object should be skipped
        if(object == null) {
            return 0;
        }
        if(visited.containsKey(object)) {
            visited.put(object,Integer.valueOf(visited.get(object)+1));
            return 0;
        }
        
        // Case of an intern 
        if(isIntern(object)) {
            if(skipInterns(object)) {
                return 0;
            }
        }

        // Regular object
        long objectSize = 0;    // Size of the object itself
        long childrenSize = 0;  // Size of the contained children

        // Add it to the list of visited objects
        if(isStoreAsVisited(object)) {
            visited.put(object,Integer.valueOf(1));
        }

        if(instrumentation!=null) {
            // Store the object size coming from the Instrumentation interface
            objectSize = instrumentation.getObjectSize(object);
        }
        
        Object current = cb.startObject(params, parent, parentField, object);
        params.push(object);
        try {
            // We calculate the size of this object by browsing its fields
            Class<?> clazz = object.getClass();
            if(clazz.isArray()) {
                // Java Array
                Class<?> arrayClazz = clazz.getComponentType();
                if(!arrayClazz.isPrimitive()) {
                    int length = Array.getLength(object);
                    for(int i=0; i<length; i++) {
                        Object value = Array.get(object, i);
                        if(value!=null) {
                            if(isFieldValid(params, clazz, null, object, value)) {
                                childrenSize += inspect(params, current, null, value, cb);
                            }
                        }
                    }
                }
            } else {
                // Regular Object
                // We must browser the fields using getDeclaredFields as we need to access all the fields
                // including the private ones
                while (clazz != null) {
                    Field[] fields = getFields(clazz);
                    for (int i=0; i<fields.length; i++) {
                        Field field = fields[i];
                        Object value = field.get(object);
                        if(value!=null) {
                            if(isFieldValid(params, clazz, field, object, value)) {
                                childrenSize += inspect(params, current, field, value, cb);
                            }
                        }
                    }
                    clazz = clazz.getSuperclass();
                }
            }
            return objectSize + childrenSize;
        } finally {
            cb.endObject(params, object, objectSize, childrenSize);
            params.pop();
        }
    }

    private Field[] getFields(Class<?> clazz) {
        Field[] f = fieldsCache.get(clazz);
        if(f!=null) {
            return f;
        }
        ArrayList<Field> ff = new ArrayList<Field>(); 
        Field[] fields = clazz.getDeclaredFields();
        for (int i=0; i<fields.length; i++) {
            Field field = fields[i];
            // Ignore static fields
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            // If it is a primitive, ignore it
            if(field.getType().isPrimitive()) {
                continue;
            }
            // Ok, it is an object
            // Compute its size recursively
            if(!field.isAccessible()) { // Ensure we can access it
                field.setAccessible(true);
            }
            ff.add(field);
        }
        f = ff.toArray(new Field[ff.size()]);
        fieldsCache.put(clazz,f);
        return f;
    }
    
    protected boolean isStoreAsVisited(Object obj) {
        return true;
    }
    
    protected boolean isIntern(Object obj) {
        // Looks like comparable is a common interface to the following basic objects
        // Limit the checks here...
        if(obj instanceof Comparable) {
            if (obj instanceof Enum) {
                return true;
            } else if(obj==StringUtil.EMPTY_STRING || obj==StringUtil.EMPTY_STRING_ARRAY) {
                return true;
            } else if (obj instanceof String) {
                return (obj == ((String) obj).intern());
            } else if (obj instanceof Boolean) {
                return (obj == Boolean.TRUE || obj == Boolean.FALSE);
            } else if (obj instanceof Integer) {
                return (obj == Integer.valueOf((Integer) obj));
            } else if (obj instanceof Short) {
                return (obj == Short.valueOf((Short) obj));
            } else if (obj instanceof Byte) {
                return (obj == Byte.valueOf((Byte) obj));
            } else if (obj instanceof Long) {
                return (obj == Long.valueOf((Long) obj));
            } else if (obj instanceof Character) {
                return (obj == Character.valueOf((Character) obj));
            }
        }
        // Empty collections
        if(obj==Collections.EMPTY_LIST || obj==Collections.EMPTY_MAP || obj==Collections.EMPTY_SET) {
            return true;
        }
        return false;
    }

    protected boolean skipInterns(Object object) {
        return true;
    }
    
    protected boolean isFieldValid(Stack<Object> params, Class<?> fieldClass, Field field, Object obj, Object value) {
        return true;
    }  
}