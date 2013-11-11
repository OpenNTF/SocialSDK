/**
 * The Playground preferences dialog.
 *We override this module because we need to place the backdrop div inside the a bootstrap scopped div.
 *
 * @module playground/widgets/gadgetarea/PlaygroundPreferencesDialog
 * @augments module:explorer/widgets/gadgetarea/PreferencesDialog
 */
define(['dojo/_base/declare', 'explorer/widgets/gadgetarea/PreferencesDialog', 'dojo/dom-construct', 'dojo/dom-class'],
        function(declare, PreferencesDialog, domConstruct, domClass, query) {
  return declare('PlaygroundPreferencesDialog', [ PreferencesDialog ], {
	    /**
	     * Shows the ModalDialog in the dom.
	     *
	     * @memberof module:playground/widgets/gadgetarea/PlaygroundPreferencesDialog#
	     */
	    show : function() {
	      domClass.remove(this.domNode, 'hide');
	      domClass.add(this.domNode, 'in');
	      domConstruct.place('<div class="modal-backdrop fade in"></div>', this.domNode.parentNode, 'last');
	    }
  });
});