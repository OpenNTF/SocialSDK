/**
 * Works with OpenSocial security tokens.
 * 
 * @module playground/securityToken
 */
define(['dojo/request/xhr', 'dojo/_base/url'], function(xhr, Url) {
	//TODO change this to be our new container name
	var container = 'default';
	var url = new Url(window.location.href);
	var domain = url.host;
	var appUrl = encodeURIComponent(url.scheme + '://' + url.authority + url.path);
	var appId = appUrl;
	
	function createSecurityTokenUrl() {
		return 'osplayground/container/stgen?c=' + container + '&d=' + domain + '&i=' + appId + '&u=' + appUrl + '&m=0';
	};
	return {
		/**
		 * Fetches the container security token.
		 * 
		 * @memberof module:playground/securityToken#
		 * @return {module:dojo/promise/Promise} Returns a 
         * {@link http://dojotoolkit.org/reference-guide/1.8/dojo/promise/Promise.html#dojo-promise-promise|Dojo Promise}.
         * Call the then method of this Promise with a function that takes in one parameter, the token response.  The
         * token response has two properties, token and ttl.  The token property is the security token, the ttl is the
         * time to live for that token.
		 */
	  	get: function() {
			return xhr(createSecurityTokenUrl(), {
				handleAs : "json"
			});
		}
	}
});