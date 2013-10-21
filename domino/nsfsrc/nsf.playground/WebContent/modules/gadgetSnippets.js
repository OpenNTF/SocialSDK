require(['dojo/on', 'dojo/ready', 'playground/widgets/gadgetarea/PlaygroundGadgetArea', 
         'dojo/dom-construct'], function(on, ready, PlaygroundGadgetArea, domConstruct) {
	
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
		on(window, 'onresize', resize);
		resize();
		//Don't like the fact that there is this pageGlobal variable, hopefully
		//we can get rid of it at some point
		var gadgetArea = new PlaygroundGadgetArea(pageGlobal);
		domConstruct.place(gadgetArea.domNode, 'osgadget', 'replace');
		gadgetArea.startup();
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