/**
 * The Playground preferences dialog.
 *
 * @module playground/widgets/gadgetarea/PlaygroundPreferencesDialog
 * @augments explorer/widgets/gadgetarea/PreferencesDialog
 */
define(['dojo/_base/declare', 'explorer/widgets/gadgetarea/PreferencesDialog', 'dojo/dom-construct', 'dojo/dom-class',
        'dojo/query', 'dojo/NodeList-manipulate', 'dojo/NodeList-dom'],
        function(declare, PreferencesDialog, domConstruct, domClass, query) {
  return declare('PlaygroundPreferencesDialog', [ PreferencesDialog ], {
	    /**
	     * Shows the ModalDialog in the dom.
	     *
	     * @memberof module:explorer/widgets/ModalDialog#
	     */
	    show : function() {
	      domClass.remove(this.domNode, 'hide');
	      domClass.add(this.domNode, 'in');
	      domConstruct.place('<div class="modal-backdrop fade in"></div>', this.domNode.parentNode, 'last');
	    },

	    /**
	     * Hides the ModalDialog in the dom.
	     *
	     * @memberof module:explorer/widgets/ModalDialog#
	     */
	    hide : function() {
	      domClass.add(this.domNode, 'hide');
	      domClass.remove(this.domNode, 'in');
	      query('div.modal-backdrop').remove();
	    }
  });
});