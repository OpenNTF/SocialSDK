/**
 * Utility functions for working with things in the DOM.
 * 
 * @module playground/util
 */
define(['dojo/dom', 'dijit/registry'], function(dom, registry) {
	return {
		/**
		 * Update the label in the toolbar.
		 * 
		 * @memberof module:playground/util#
		 * @param {Object} r - Response object.
		 */
	  	updateLabel : function(r) {
			var tt = dom.byId("CurrentLabel");
			if(tt) {
				var label = r ? (r.category+"/"+(r.name||"")) : "[New Gadget]";
				// Use text here!
				tt.innerHTML = label; 
			}
		},
		
		/**
		 * Update the selection for view.
		 * 
		 * @memberof module:playground/util#
		 */
		updateNavSelection : function() {
			// When a tree is created
			if(dom.byId(pageGlobal.snippetsTree)) {
				treeSelectId(pageGlobal.snippetsTree,pageGlobal.id);
			}
		},
		
		/**
		 * Selects a tab.
		 * 
		 * @memberof module:playground/util#
		 * @param {String} tab - The tab to select.
		 */
		selectTab : function(tab) {
			var tc = registry.byId(pageGlobal.tabContainer);
			var pn = registry.byId(tab);
			tc.selectChild(pn);
		},
		
		/**
		 * Updates the contents of the editors.
		 * 
		 * @memberof module:playground/util#
		 * @param {Object} r - Response object containing editor information.
		 * @param {String} id - The ID of the gadget sniped being displayed.
		 */
		updateEditorContent : function(r, id) {
			pageGlobal.id = id;
			pageGlobal.unid = r.unid;
			if(pageGlobal.gadgetEditor) { pageGlobal.gadgetEditor.setValue(r.gadget); this.selectTab(pageGlobal.tabGadget); }
			if(pageGlobal.htmlEditor) pageGlobal.htmlEditor.setValue(r.html);
			if(pageGlobal.jsEditor) pageGlobal.jsEditor.setValue(r.js);
			if(pageGlobal.cssEditor) pageGlobal.cssEditor.setValue(r.css);
			if(pageGlobal.jsonEditor) pageGlobal.jsonEditor.setValue(r.json);
			if(pageGlobal.propertiesEditor) pageGlobal.propertiesEditor.setValue(r.properties);
			if(pageGlobal.documentationPanel) pageGlobal.documentationPanel.innerHTML = r.documentation;
			this.selectTab(pageGlobal.tabGadget);
			this.updateLabel(r);
			this.updateNavSelection();
		},
		
		/**
		 * Clears the contents of the editors.
		 * 
		 * @memberof module:playground/util#
		 */
		clearEditors : function() {
			pageGlobal.id = "";
			pageGlobal.unid = "";
			if(pageGlobal.gadgetEditor) {
				pageGlobal.gadgetEditor.setValue("");
				this.selectTab(pageGlobal.tabGadget);
			}
			if(pageGlobal.htmlEditor) {
				pageGlobal.htmlEditor.setValue("");
			}
			if(pageGlobal.jsEditor) {
				pageGlobal.jsEditor.setValue("");
			}
			if(pageGlobal.cssEditor) {
				pageGlobal.cssEditor.setValue("");
			}
			if(pageGlobal.jsonEditor) {
				pageGlobal.jsonEditor.setValue("");
			}
			if(pageGlobal.propertiesEditor) {
				pageGlobal.propertiesEditor.setValue("");
			}
			if(pageGlobal.documentationPanel) {
				pageGlobal.documentationPanel.innerHTML = "";
			}
			this.updateLabel(null);
			this.updateNavSelection();
		}
	};
});