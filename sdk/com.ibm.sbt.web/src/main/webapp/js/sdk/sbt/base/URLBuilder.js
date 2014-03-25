/*
 * � Copyright IBM Corp. 2012,2013
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
 * Social Business Toolkit SDK. Definition of constants for CommunityService.
 * 
 * @module sbt.connections.CommunityConstants
 */
define([ "../declare" ], function(declare) {
    var URLBuilder = declare(null, {

        constructor : function() {
        },
        
        _normalizeVersion : function(version) {
            var tokenized = version.split(".");
            while (tokenized.lenght<3) {
                tokenized.push('0');
            } 
            if (tokenized.lenght>3) {
                tokenized = tokenized.slice(0,3);
            }
            for (var key in tokenized) {
                if (tokenized.hasOwnProperty(key)) {
                  tokenized[key]=Number(tokenized[key]);
                }
            }
            return tokenized;
        },
        _compareVersion: function(a,b) {
            if (!b) return -1;

            if(a[0]<b[0]) {
                return -1;
            }
            if(a[0]>b[0]) {
                return 1;
            }
            if(a[1]<b[1]) {
                return -1;
            }
            if(a[1]>b[1]) {
                return 1;
            }
            if(a[2]<b[2]) {
                return -1;
            }
            if(a[2]>b[2]) {
                return 1;
            }
            return 0;
        },
        
        build: function(versionedUrl, maxVersion, args) {
            var url;
            if (typeof versionedUrl === "object"){
	            
	            var compatible=null;
	            var key = null;
	            if (maxVersion)
	            maxVersion = this._normalizeVersion(maxVersion);
	            for (var version in versionedUrl) {
	                versionN = this._normalizeVersion(version);
	                if (this._compareVersion(versionN,maxVersion)>0) continue;
	                if (compatible) {
	                    if (this._compareVersion(compatible,versionN)<0) {
	                        compatible=versionN;
	                        key = version;
	                    }
	                }else {
	                    compatible = versionN;
	                    key = version;
	                }
	              }
	            url = versionedUrl[key];
            } else {
                url = versionedUrl;
            }

            if (args) {
	            for (var key in args) {
	                  if (args.hasOwnProperty(key)) {
	                      url=url.replace('{'+key+'}',args[key]);
	                  }
	            }
            }
            url = url.replace("//","/");
            return url;
        }
    });
    
    return URLBuilder;
});