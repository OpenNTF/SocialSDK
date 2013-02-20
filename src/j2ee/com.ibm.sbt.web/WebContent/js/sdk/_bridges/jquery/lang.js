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
 * Social Business Toolkit SDK - Some language utilities.
 */
define(["has", "jquery"],function(has, $) {
	// Helper methods
    var _extraNames =
        has("bug-for-in-skips-shadowed") ?
            "hasOwnProperty.valueOf.isPrototypeOf.propertyIsEnumerable.toLocaleString.toString.constructor".split(".") : [];
    var _extraLen = _extraNames.length;
    var opts = Object.prototype.toString;
    var _pattern = /\{([^\}]+)\}/g;
    
    function getProp(/*Array*/parts, /*Boolean*/create, /*Object*/context){
        var p, i = 0, global = this;
        if(!context){
            if(!parts.length){
                return global;
            }else{
                p = parts[i++];
                context = context || (p in global ? global[p] : (create ? global[p] = {} : undefined));
            }
        }
        while(context && (p = parts[i++])){
            context = (p in context ? context[p] : (create ? context[p] = {} : undefined));
        }
        return context; // mixed
    };

    function efficient(obj, offset, startWith){
        return (startWith||[]).concat(Array.prototype.slice.call(obj, offset||0));
    };
    
	return {
		mixin: function(dest,sources) {
			return $.extend(dest,sources);
		},
		isArray: function(obj) {
			return $.isArray(o);
		},
		isString: function(o) {
		    return (typeof o=="string"||o instanceof String);
		},
        isFunction: function(o) {
            return typeof o == 'function';
        },
		clone: function(o) {
			return $.clone(o);
		},
        concatPath: function () {
        	var a = arguments;
        	if(a.length==1 && this.isArray(a[0])) {
        		a = a[0];
        	}
        	var s = "";
        	for(var i=0; i<a.length; i++) {
        		var o = a[i].toString();
        		if(s) {
        			s = s + "/";
        		}
        		s = s + (o.charAt(0)=='/'?o.substring(1):o);
        	}
        	return s;
        },
        setObject: function(name, value, context){
            // summary:
            //      Set a property from a dot-separated string, such as "A.B.C"
            // description:
            //      Useful for longer api chains where you have to test each object in
            //      the chain, or when you have an object reference in string format.
            //      Objects are created as needed along `path`. Returns the passed
            //      value if setting is successful or `undefined` if not.
            // name: String
            //      Path to a property, in the form "A.B.C".
            // value: anything
            //      value or object to place at location given by name
            // context: Object?
            //      Optional. Object to use as root of path. Defaults to
            //      `dojo.global`.
            // example:
            //      set the value of `foo.bar.baz`, regardless of whether
            //      intermediate objects already exist:
            //  | lang.setObject("foo.bar.baz", value);
            // example:
            //      without `lang.setObject`, we often see code like this:
            //  | // ensure that intermediate objects are available
            //  | if(!obj["parent"]){ obj.parent = {}; }
            //  | if(!obj.parent["child"]){ obj.parent.child = {}; }
            //  | // now we can safely set the property
            //  | obj.parent.child.prop = "some value";
            //      whereas with `lang.setObject`, we can shorten that to:
            //  | lang.setObject("parent.child.prop", "some value", obj);

            var parts = name.split("."), p = parts.pop(), obj = getProp(parts, true, context);
            return obj && p ? (obj[p] = value) : undefined; // Object
        }
	};
});