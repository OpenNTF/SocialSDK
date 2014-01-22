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

/**
 * The cache implements the Least Recently Used (LRU)Algorithm by doubly linked list. Every node has 4 variables - key, value, next and previous.
 * The key stores the key of the node and value stores the actual data for the key. The next and previous variables point to the 
 * next and previous nodes in the cache. 
 *  The cache has a head variable pointing to the top cache node and a tail variable pointing to the last cache node.
 *
 *   Head ->  A --->  B  --->  C ---> D  <--  Tail
 *              <---     <---   <---
 *
 * Suppose the cache has 4 entries and its max size limit is 4 (the cache is full right now). The structure of the cache would be as described by figure above.
 * The entries are listed as per their order of recent access. 
 * So when a new entry E is added to the cache, the new order of the cache entries would be EABC. D would be deleted from the cache.
 * 
 * @module sbt.Cache
 */
define(['./declare'],function(declare) {
	var Cache = declare(null, {	
		constructor: function(max) {
			this.limit = max;// This is the maximum limit of the cache.
			this._cache = {};//Variable to hold the items in the cache.
			this.head = null;// Pointer to the head of the cache
			this.tail = null;// Pointer to the tail of the cache
			this.count = 0;// Counter for number of items in the cache
		},
		get: function _cg(key) { //Retrieves a cached item.
			var k = this._cache[key];
			if(k){//Item found in the cache. Move the accessed node to the top of the cache.
				if(this.head == k){return k.value;} // the node is already at the top, no need to shift, just return the value.
				else{// shift the node to the top and return the value
					if(k.prev)k.prev.next = k.next;
					if(k.next){k.next.prev = k.prev;} else {this.tail=k.prev;}
					k.next  = this.head; 
					this.head.prev = k; 
					k.prev = null; 
					this.head = k; 
				}
				return k.value;
			}
			return null; // the node is not in the cache
		},
		put: function _cp(key,value) {// puts a node in the cache if the node is not present in the cache. The node is put at the top of the cache.
			if(this._cache[key])//remove the asked node
				{this.remove(key); this.count --;}
			
			var k = this._cache[key] ={key:key, value:value};
			if(this.count==this.limit) //if the cache is full, remove the last node
				{this.remove(this.tail.key);this.count --;}
	        //add the asked node to the top of the list.
			k.next = this.head;
		    if(k.next)k.next.prev = k;else this.tail = k;
		    this.head = k;
		    k.prev = null;
		    this.count ++;
		 },
		remove: function _cr(key) {//removes a node from the cache and updates the next and prev attributes of the surrounding nodes.
			var k = this._cache[key];
			if(k){
				if(k.next)k.next.prev = k.prev;else this.tail = k.prev;
				if(k.prev)k.prev.next = k.next; else this.head = k.next;
				k.next = k.prev = null;
				delete this._cache[key];
				this.count -- ;
			}
			
		},
		
		/**
		 * Function that browse the content of the cache and call a call back method for each entry.
		 * 
		 * @param callback the callback method to invoke for each entry
		 */
		browse: function(callback) {
			if(callback) {
				for(var i in this._cache) {
					var e = this._cache[i];
					var r = callback(e.key,e.value);
					if(r) {
						return r;
					}
				}
				return null;
			}
			return null;
		}
	});

	return Cache;
});