/**
 * Contains the space where the gadget renders.
 *
 * @module playground/widgets/gadgetarea/PlaygroundGadgetArea
 * @augments explorer/widgets/gadgetarea/GadgetArea
 */
define(['dojo/_base/declare', 'explorer/widgets/gadgetarea/GadgetArea', 'dojo/on', 'dojo/topic', 'dojo/hash', 'dojo/_base/lang', 
        'explorer/ExplorerContainer', 'explorer/widgets/Loading', 'dojo/dom', 'playground/gadget-spec-service',
        'dojo/io-query', 'dojo/json', 'dijit/registry', 'playground/util'],
        function(declare, GadgetArea, on, topic, hash, lang, ExplorerContainer, Loading, dom, 
        		gadgetSpecService, ioQuery, json, registry, util) {
	return declare('PlaygroundGadgetAreaWidget', [ GadgetArea ], {
    	  //TODO at some point we actually want to use a real template
    	  templateString : '<div></div>',
 
    	  /**
    	   * Called right after this widget has been added to the DOM.
    	   * 
    	   * @memberof module:playground/widgets/gadgetarea/PlaygroundGadgetArea#
    	   * @see {@link http://dojotoolkit.org/reference-guide/1.8/dijit/_WidgetBase.html|Dojo Documentation}
    	   */
    	  startup : function() {
			//TODO once we have dealt with the loading widget we can remove the majority of this code
          	this.expContainer = new ExplorerContainer();
          	this.siteCounter = 0;
          	this.siteParent = this.domNode;;
          	var self = this;
          	this.loadingWidget = new Loading();
          	this.setupEventListeners();
          	this.setupSubscriptions();
          	if(hash()) {
          		this.loadFromHash()
          	}
          },
          
          /**
           * Setups topic subscriptions for this class.
           *
    	   * @memberof module:playground/widgets/gadgetarea/PlaygroundGadgetArea#
           */
          setupSubscriptions : function() {
        	  this.inherited(arguments);
        	  var self = this;
        	  topic.subscribe('runCode', function(debug) {
        		  self.runCode(debug); 
        	  }); 
        	  topic.subscribe('com.ibm.xsp.playground.loadsnippet', this.loadSnippet);
        	  topic.subscribe('/dojo/hashchange', lang.hitch(this, this.loadFromHash));
          },
          
          /**
           * Loads a gadget from the URL hash.
           * 
           * @memberof module:playground/widgets/gadgetarea/PlaygroundGadgetArea#
           */
          loadFromHash : function() {
        	  if(hash()) {
        		  var obj = ioQuery.queryToObject(hash());
        		  if(obj.snippet) {
        			  var self = this;
        			  setTimeout(function() {
        				  self.loadSnippet(obj.snippet);
  						});
        		  } 
        	  } else {
        		  util.clearEditors();
        		  this.closeOpenSite();
        	  }
          },
  	  
  	     /**
  	      * Load a snippet from the server using a JSON RPC call.
  	      * 
  	      * @memberof module:playground/widgets/gadgetarea/PlaygroundGadgetArea#
  	      * @param {String} id - The id of the gadget to load.
  	 	  */
          loadSnippet : function(id) {
        	  var self = this;
        	  gadgetSpecService.getGadgetSpec(id).then(function(r) {
        		  util.updateEditorContent(r, id);
        		  self.runCode(false);
        	  }, 
        	  function(error) {
        		  alert("Error:\n"+error.msg);
        	  });
          },
	
          /**
           * Renders the gadget.
           * 
           * @memberof module:playground/widgets/gadgetarea/PlaygroundGadgetArea#
           * @param {Boolean} debug - Whether to render gadget in debug
           */
          runCode : function(debug) {
        	  if(pageGlobal._loadingFrame) {
        		  try {
        			  //TODO we need to have another mechanism for loading UI for gadgets
        			  var iDoc = window.frames['preview'].document;
        			  var b = iDoc.getElementsByTagName("body")[0];
        			  b.innerHTML = "<span>Loading...</span>";
        		  } catch(e) {} 
        	  }
        	  // Calculate a random gadget id to cheat shindig cache and have a key on the server side
        	  var gadgetId=(Math.floor(Math.random()*1000000000)).toString(36);
		
        	  // Send the gadget content by posting the data
        	  var form = dom.byId("PreviewForm");
        	  form["fm_gadgetid"].value = gadgetId;
        	  form["fm_gadget"].value = this.gadgetEditor.getValue();
        	  form["fm_html"].value = this.htmlEditor.getValue();
        	  form["fm_js"].value = this.jsEditor.getValue();
        	  form["fm_css"].value = this.cssEditor.getValue();
        	  form["fm_properties"].value = this.propertiesEditor.getValue();
        	  //TODO this doesn't do anything currently, we can render a gadget in debug mode though
        	  form["fm_options"].value = json.stringify({
        		  debug: debug
        	  });
        	  var self = this;
        	  gadgetSpecService.postGadgetSpec(form).then(function(data){
        		  // https require the certificates on the server... 
        		  var url = form.action+"/"+gadgetId+"/gadget.xml";
        		  url = url.replace("https://","http://");
        		  var jsonCode = self.jsonEditor.getValue();
        		  if(!jsonCode) {
        			  self.renderGadget(url);
        		  } else {
        			  self.renderEmbeddedExperience(url, jsonCode);
        		  }
        	  }, function(error) {
        		  alert(error);
        	  });
          }
	});
});