/*
 *  Copyright IBM Corp. 2012-2013
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

package com.ibm.commons.util;



/**
 * Generic object pool.
 * @ibm-not-published
 */
public abstract class ObjectPool {

	private Object[] data;
    private int count = 0;

	public ObjectPool(int poolSize) {
		this.data = new Object[poolSize];
	}
	
    public synchronized Object get() throws Exception {
        synchronized(this) {
            if( count==0 ) {
            	Object object = createObject();
                return object;
            }
            return data[--count];
        }
    }

    public synchronized void recycle(Object object) {
        if( count<data.length ) {
            data[count++] = object;
        }
    }

    protected abstract Object createObject() throws Exception;
}
