/**
 * Handles making requests to the server.
 *
 * @module playground/gadget-spec-service
 */
define(['dojo/dom-form', 'dojo/request/xhr'], function(domForm, xhr) {
	return {
		
		/**
		 * Posts the gadget spec to the server.
		 * 
		 * @memberof playground/gadget-spec-service#
		 * @param {Element} form - The form element containing the gadget code.
		 * @return {module:dojo/promise/Promise} Returns a 
         * {@link http://dojotoolkit.org/reference-guide/1.8/dojo/promise/Promise.html#dojo-promise-promise|Dojo Promise}.
		 */
		postGadgetSpec : function(form) {
			return xhr.post(form.action, {
				data : domForm.toObject(form),
				handleAs : "text"
			});
		},
		
		/**
		 * Gets a gadget spec.
		 * 
		 * @memberof playground/gadget-spec-service#
		 * @param {String} id - The ID of the gadget spec to get.
		 * @return {module:dojo/promise/Promise} Returns a 
         * {@link http://dojotoolkit.org/reference-guide/1.8/dojo/promise/Promise.html#dojo-promise-promise|Dojo Promise}.
		 */
		getGadgetSpec : function(id) {
			//WARNING: global server value
			return server.loadSnippet(id);
		}
	};
});