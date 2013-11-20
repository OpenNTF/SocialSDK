
/**
 * A modal window that displays the results of the open-views gadget example.
 * We override this module because we need to place the backdrop div inside the a bootstrap scopped div.
 * 
 * @module playground/widgets/gadgetarea/PlaygroundGadgetModalDialog
 * @augments module:explorer/widgets/gadgetarea/GadgetModalDialog
 */
define(['dojo/_base/declare',  'explorer/widgets/gadgetarea/GadgetModalDialog', 'dojo/dom-construct', 'dojo/dom-class'],
        function(declare, GadgetModalDialog, domConstruct, domClass) {
  return declare('PlaygroundGadgetModalDialogWidget', [ GadgetModalDialog ], {
	    /**
	     * Shows the ModalDialog in the dom.
	     *
	     * @memberof module:playground/widgets/gadgetarea/PlaygroundGadgetModalDialog#
	     */
	    show : function() {
	      domClass.remove(this.domNode, 'hide');
	      domClass.add(this.domNode, 'in');
	      domConstruct.place('<div class="modal-backdrop fade in"></div>', this.domNode.parentNode, 'last');
	    }
  });
});