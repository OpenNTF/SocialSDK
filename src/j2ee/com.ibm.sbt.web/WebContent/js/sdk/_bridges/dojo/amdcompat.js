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
 * Social Business Toolkit SDK.
 * 
 * Compatibility file for dojo14/15
 */
dojo.provide("sbt._bridge.amdcompat");

window._sbt_bridge_compat = true;

(function() {
   /*
	* Emulate the AMD syntax with older version of Dojo (1.4.3...). This file is
	* not mean to be included using a require statement, but simply as a <script>
	* tag like any regular JS files.
	* 
	* Note that this is *not* an asynchonous loader, as the module are still loaded
	* synchronously. It just 
	* 
	* References: http://bugs.dojotoolkit.org/ticket/11869
	* http://bugs.dojotoolkit.org/changeset/23053/dojo
	*  
	* Parts are extracted from Dojo 1.6.1 source code
	* addition to support AMD module format
	* replace the bootstrap define function (defined in _base/boostrap.js) with the
	* dojo v1.x simulated AMD loader...
	* 
	*/ 
	var global = window;
	var currentModule = null;
	
	var _load = dojo._loadModule; 
	dojo._loadModule = dojo.require = function(/*String*/moduleName, /*Boolean?*/omitModuleCheck){
		var oldModule = currentModule; 
		currentModule = moduleName;
		try {
			return _load.apply(null,arguments);
		} catch(ex){ 
			console.log(ex.message);
		} finally {
			currentModule = oldModule;
		}
	};
	
	// ["myPackage/nls/myBundle", "myPackage/nls/fr/myBundle", "myPackage/nls/fr-ca/myBundle"]
	var findBundles = function(root,locale,bundle) {
		var result = [];
		var parts = locale.split("-");
		for(var current = "", i = 0; i<parts.length; i++){
			current += (current ? "-" : "") + parts[i];
			if(!root || root[current]){
				result.push(bundle.substring(0,bundle.lastIndexOf('.')+1) + current + "." + bundle.substring(bundle.lastIndexOf('.')+1));
			}
		}
		return result;
	};

	function _define(name, deps, def) {
		if(name) {
			var dottedName = name.replace(/\//g, ".");
			var exports = dojo.provide(dottedName);
		}

		function resolvePath(relativeId) {
			// do relative path resolution
			if (relativeId.charAt(0) === '.') {
				relativeId = name.substring(0, name.lastIndexOf('/') + 1)
							+ relativeId;
					while (lastId !== relativeId) {
						var lastId = relativeId;
						relativeId = relativeId.replace(/\/[^\/]*\/\.\.\//, '/');
					}
				
				relativeId = relativeId.replace(/\/\.\//g, '/');
			}
			return relativeId.replace(/\//g, ".");
		}
		
		for ( var args = [], depName, i = 0; i < deps.length; i++) {
			depName = resolvePath(deps[i]);
            var arg;
			// check has an plugin been specified
			var exclamationIndex = depName.indexOf("!");
			if (exclamationIndex > -1) {
			    var pluginName = depName.substring(0, exclamationIndex);
				if (pluginName == "sbt.i18n") {
                    var bundleName = depName.substring(exclamationIndex+1);
                    var mod = dojo.require(bundleName);
                    arg = mod.root||mod;
                    var bundles = findBundles(mod.root?mod:null,dojo.locale,bundleName);
                    for(var mi=0; mi<bundles.length; mi++) {
                        dojo.mixin(arg,dojo.require(bundles[mi]));
                    }
				} else if (pluginName == "sbt.text") {
					exclamationIndex = deps[i].indexOf("!");
                    var fileName = deps[i].substring(exclamationIndex+1,deps[i].length);
                    if (fileName.charAt(0) == '.') {
                        var loc = dojo.doc.location;
                        var index = loc.pathname.indexOf('/', 1);
                        var url = loc.protocol + "//" + loc.host + loc.pathname.substring(0, index);
                        url += fileName.substring(1);
                        arg = dojo.cache(new dojo._Url(url));
                    } else {
                        var moduleIndex = fileName.indexOf("/");
                        var extnIndex = fileName.lastIndexOf(".");
                        var moduleId = fileName.substring(0, moduleIndex);
                        var url = fileName.substring(moduleIndex+1);
                        arg = dojo.cache(moduleId, url);
                    } 
                } else {
					arg = null;
				}
			} else {
				switch (depName) {
				case "require":
					arg = function(relativeId) {
						return dojo.require(resolvePath(relativeId));
					};
					break;
				case "exports":
					arg = exports;
					break;
				case "module":
					var module = arg = {
						exports : exports
					};
					break;
				case "dojox":
					arg = dojo.getObject(depName);
					break;
				case "dojo/lib/kernel":
				case "dojo/lib/backCompat":
					arg = dojo;
					break;
				default:
					arg = dojo.require(depName);
				}
			}
			args.push(arg);
		}
		
		var returned;
		if (typeof def == "function") {
		    returned = def.apply(null, args);
		} else {
			returned = def;
		}

		if(name) {
			if (returned) {
				dojo._loadedModules[dottedName] = returned;
				dojo.setObject(dottedName, returned);
			}
			if (module) {
				dojo._loadedModules[dottedName] = module.exports;
			}
			return returned;
		}

	};
	
	global.define = function(name, deps, def) {
		if (!def) {
			// less than 3 args
			if (deps) {
				// 2 args
				def = deps;
				deps = name;
			} else {
				// one arg
				def = name;
				deps = typeof def == "function" ? [ "require","exports","module" ].slice(0, def.length) : [];
			}
			name = currentModule ? currentModule.replace(/\./g, '/') : "anon";
			//console.log("currentModule="+currentModule+", name="+name);
		}
		return _define(name,deps,def);
	};

	global.require = function(deps, def) {
		return _define(null,deps,def);
	};

	define.vendor = "dojotoolkit.org";
	define.version = dojo.version;
	define("dojo/lib/kernel", [], dojo);
	define("dojo/lib/backCompat", [], dojo);
	define("dojo", [], dojo);
	define("dijit", [], this.dijit || (this.dijit = {}));
	
	/*
     * Some common Dojo method
     */	
	define("dojo/ready", [], function() {
		return dojo.ready || dojo.addOnLoad;	
	});
	define("dojo/declare", [], function() {
		return dojo.declare;	
	});
	
	// 4/17/2013: make the new AMD syntax available to the dojo parser
	var oldGetObject = dojo.getObject; 
	dojo.getObject = function(/*String*/name, /*Boolean?*/create, /*Object?*/context){
		if(dojo.isString(name)&&name.indexOf('/')>=0) {
			// create=true, not supported as we cannot create modules this way
			// context!=null, not supported as modules cannot have a root context
			if(!create && !context) {
				var dottedName = name.replace(/\//g, ".");
				var m = dojo.require(dottedName);
				return m;
			}
		}
		return oldGetObject.apply(this,arguments);
	};	
})();
