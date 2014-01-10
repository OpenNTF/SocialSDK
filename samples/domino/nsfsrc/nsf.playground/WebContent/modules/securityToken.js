/**
 * Works with OpenSocial security tokens.
 * 
 * @module playground/securityToken
 */
define(['dojo/request/xhr', 'dojo/_base/url'], function(xhr, Url) {
	var url = new Url(window.location.href);
	var domain = url.host;
	var appUrl = encodeURIComponent(url.scheme + '://' + url.authority + url.path);
	var appId = appUrl;
	
	function createSecurityTokenUrl(container) {
		//We need to encode the container id here even though it is already encoded
		//This is because the encoded container id is the ID so if we just place the container
		//id in the URL and it gets decoded than Shindig will not be able to find the container
		return 'sbtos/container/stgen?c=' + encodeURIComponent(container) + 
		'&d=' + domain + '&i=' + appId + '&u=' + appUrl + '&m=0';
	};
	return {
		/**
		 * Fetches the container security token.
		 * @param {String} container - The ID of the container.
		 * 
		 * @memberof module:playground/securityToken#
		 * @return {module:dojo/promise/Promise} Returns a 
         * {@link http://dojotoolkit.org/reference-guide/1.8/dojo/promise/Promise.html#dojo-promise-promise|Dojo Promise}.
         * Call the then method of this Promise with a function that takes in one parameter, the token response.  The
         * token response has two properties, token and ttl.  The token property is the security token, the ttl is the
         * time to live for that token.
		 */
	  	get: function(container) {
			return xhr(createSecurityTokenUrl(container), {
				handleAs : "json"
			});
		}
	}
});