/**
 * Helper module for dealing with DOJO templates.
 * @module
 */
define(["dojo/query"], function(query){
   return {
       /**
        * Gets a string of HTML from a DOM node.
        * @param {String} domId The ID of the DOM node.
        * @returns {String} The inner HTML of the DOM node.
        */
       getTemplateString: function(domId) {
           var domNode = query(domId);
           return domNode.length > 0 ? domNode.innerHTML() : '';
       }
   };
});