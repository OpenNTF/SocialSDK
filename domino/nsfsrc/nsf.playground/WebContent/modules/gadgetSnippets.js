require(['dojo/on', 'dojo/ready', 'playground/widgets/gadgetarea/PlaygroundGadgetArea', 
         'dojo/dom-construct', 'dojo/dom-class',
         'dojo/_base/event', 'dojo/query', 'playground/securityToken', 'dojo/hash', 'dojo/NodeList-manipulate', 
         'dojo/NodeList-dom', 'dojo/NodeList-traverse'], 
         function(on, ready, PlaygroundGadgetArea, domConstruct, domClass,
        		 event, query, securityToken, hash) {
	
	//TODO this is duplicated :(
	function updateNavSelection() {
		// When a tree is created
		if(dojo.byId(pageGlobal.snippetsTree)) {
			treeSelectId(pageGlobal.snippetsTree,pageGlobal.id);
		}
    }
	
	//TODO this is not used today, but saving for future use
	function handleNavigateEE(rel, opt_gadgetInfo, opt_viewTarget, opt_coordinates, parentSite, opt_callback) {
		var title = 'Embedded Experiences';
	    if(opt_gadgetInfo && opt_gadgetInfo.modulePrefs && opt_gadgetInfo.modulePrefs.title) {
	      title = opt_gadgetInfo.modulePrefs.title;
	    }
	    
	    require([
	             "dijit/TooltipDialog",
	             "dijit/popup",
	             "dojo/dom",
	             "dojo/dom-construct",
	             "dojo/domReady!"
	         ], function(TooltipDialog, popup, dom, domConstruct){
	    		 var myDialog = new TooltipDialog({
	    			content: '<div id="eePopup" class="lotusPopup lotusPopupLeft" style="width: 350px" aria-hidden="false" role="dialog" aria-labelledby="popupExampleHeader">'+
		'<header class="lotusPopupHeader">' +
			'<h1 id="popupExampleHeader" class="lotusHeading">Embedded Experience</h1>' +
		'</header>' +
		'<div class="lotusPopupContent">' +
			'<div id="eePopupContent" class="lotusPopupContentArea">' +
			'</div>'+
		'</div>' +
		'<div class="lotusPopupFooter">' +
			'<ul class="lotusInlinelist lotusActions">' +
				'<li class="lotusFirst"><a href="javascript:;"></a></li>' +
			'</ul>' +
		'</div>' +
		'<div class="lotusPopupConnector"></div>' +
	'</div>' 
	    		 });
	             myDialog.connect(myDialog, 'onShow', function(e) {
	            	 //Must have focus for the onBlur event to be fired
	            	 myDialog.focus();
	            	 opt_callback(document.getElementById('eePopupContent'));
	            	 myDialog.connect(myDialog, 'onBlur', function(e) {
	            		 domConstruct.destroy('eePopup')
	                	 popup.close(myDialog);
	                 });
	             });
	             popup.open({
	                 popup: myDialog,
	                 around: dom.byId('asgadget'),
	                 orient: ['before', 'above-alt']
	             });
	             
	         });
	}
	
	ready(function() {
		on(window, 'resize', resize);
		resize();

		//Don't like the fact that there is this pageGlobal variable, hopefully
		//we can get rid of it at some point
		securityToken.get().then(function(response) {
			var gadgetArea = new PlaygroundGadgetArea(pageGlobal);
			domConstruct.place(gadgetArea.domNode, 'osgadget', 'replace');
			gadgetArea.startup();
			gadgetArea.updateContainerSecurityToken(response.token, response.ttl);
			if(hash()) {
				gadgetArea.loadFromHash()
			}
			query('html').on('click', function(e) {
				query('.dropdown-menu').parent().removeClass('open');
			});
			var dropDownMenuParents = query('.dropdown-menu').parent('div,.dropdown-parent');
			dropDownMenuParents.on('click', function(e) {
				if(!domClass.contains(e.currentTarget, 'open')) {
					domClass.add(e.currentTarget, 'open');
					event.stop(e);
				}
			});
		}, function(error) {
			console.error(error);
		});

	});
	
	function prepend(h,f) {
		return function() {
			if(f) {
				f.apply(this,arguments);
			}
			if(h) {
				h.apply(this,arguments);
			}
		};
	}
	XSP._partialRefresh = prepend(XSP._partialRefresh,updateNavSelection);
});