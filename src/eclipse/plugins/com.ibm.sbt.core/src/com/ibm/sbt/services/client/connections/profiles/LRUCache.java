package com.ibm.sbt.services.client.connections.profiles;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;


/**
 * LRUCache 
 * 
 * @author Swati Singh
 */

public class LRUCache{

    private LinkedHashMap<String, Document> cacheMap;

    public LRUCache(final int maxSize) {
    	cacheMap = new LinkedHashMap<String, Document>(maxSize, 0.75f, true) {

    		private static final long serialVersionUID = 1L;

			@Override
            protected boolean removeEldestEntry(Map.Entry<String, Document> eldest) {
                return size() > maxSize;
            }
        };
    }
    
    public synchronized void put(String key, Document elem) {
        cacheMap.put(key.trim(), elem);
    }

    public synchronized Document get(String key) {
        return cacheMap.get(key.trim());
    }

    public synchronized Set<String> getKeySet() {
    	Set<String> set = cacheMap.keySet();
        return set;
    }
    public synchronized boolean  hasKey(String key){
    	return cacheMap.containsKey(key.trim());
    }
    
    public synchronized void remove(String key) {
    	cacheMap.remove(key.trim());
    }
   
  
}
