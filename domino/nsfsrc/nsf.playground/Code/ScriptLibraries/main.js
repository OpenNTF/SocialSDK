/**
 * Fix the issue with the tab container and code mirror CTRL HOME+END.
 * Else, the key strokes are handled by the tab container and the tab switch
 */
dojo.addOnLoad(function() {
	if(pageGlobal && pageGlobal.tabContainer) {
   		var tbList = dijit.byId(pageGlobal.tabContainer).tablist;
   		var oldkeyPress = tbList.onkeypress;  
   		tbList.onkeypress=function(e){
   			if(e.ctrlKey && (e.charOrCode==dojo.keys.HOME || e.charOrCode==dojo.keys.END)) {
   				return;
   			}
   	   		oldkeyPress.apply(this,arguments);
     	}
   	}
});

function toggleMaximize(pane) {
	require(["dojo/window","dojo/dom-geometry","dojo/dom-style"], function(win,domGeom,domStyle){
		var scrollRoot = (dojo.doc.compatMode == 'BackCompat') ? dojo.body() : dojo.doc.documentElement;
		var w = 1600; //scrollRoot.clientWidth;
		var h = 800; //scrollRoot.clientHeight;
		var p = domGeom.position(pane.parentNode);
		domStyle.set( pane, {
			position: "absolute",
			top: "-"+p.y+"px",
			left: "-"+p.x+"px",
			zIndex: 0,
			width: w+"px",
			height: h+"px"
		});
	});
}

/**
 * Resize the main border container when the window is resized
 * This is required as it should substract the size of the headers and footers
 * @return
 */
function resize() {
	require(["dojo/_base/window","dojo/dom-geometry","dojo/dom-class"], function(win,domGeom,domClass){
		function windowHeight() {
			var scrollRoot = (dojo.doc.compatMode == 'BackCompat') ? dojo.body() : dojo.doc.documentElement;
			return scrollRoot.clientHeight;
		}
		function eltHeight(c) {
			var e = dojo.query(c);
			var h = e && e.length>0?domGeom.getMarginBox(e[0]).h:0;
			return h;
		}
		var h = windowHeight();
		if(domClass.contains(win.body(),"lotusui30")) {
			var hd = eltHeight("#nav_bar_include")+eltHeight(".lotusBanner")+eltHeight(".lotusTitleBar2")+eltHeight(".lotusTitleBar")+eltHeight(".lotusPlaceBar");
			var ft = eltHeight(".mastFooter")+eltHeight(".lotusFooter")+eltHeight(".lotusLegal");
			dojo.query(".lotusMain").style("height",(h-hd-ft)+"px");
			//console.log("h="+h+", hd="+hd+", ft="+ft+", result="+(h-hd-ft-25))
			dijit.byId(pageGlobal.borderContainer).resize()
		} else if(domClass.contains(win.body(),"dbootstrap")) {
			var hd = eltHeight("#nav_bar_include")+eltHeight(".lotusBanner")+eltHeight(".lotusTitleBar2")+eltHeight(".lotusTitleBar")+eltHeight(".lotusPlaceBar");
			var ft = eltHeight(".mastFooter")+eltHeight(".lotusFooter")+eltHeight(".lotusLegal");
			dojo.query(".container-fluid").style("height",(h-hd-ft)+"px");
			dojo.query(".row-fluid").style("height","100%");
			dojo.query(".span12").style("height","100%");
			//console.log("h="+h+", hd="+hd+", ft="+ft+", result="+(h-hd-ft-25))
			dijit.byId(pageGlobal.borderContainer).resize()
		}
	});
}

//
// Tree helpers
//

function treeCollapseAll(tree) {
	tree = dijit.byId(tree);
	tree.collapseAll();
}

function treeExpandAll(tree) { 
	tree = dijit.byId(tree);
	tree.expandAll();
}

function treeFindPath(tree,id) {
	tree = dijit.byId(tree);
	function itemPath(model,items,item){
	    items.push(item.id.toString()); // Make it a string else it is an object!
	    if(item.id==id || item.jspUrl==id){
	    	return items;
	    }
	    var cc = item.children;
	    for(var i in cc){
	        var it = itemPath(model,items,cc[i]);
	        if(it) {
	        	return it;
	        }
	    }
	    items.pop();
	    return null;
	}
	return itemPath(tree.model,[],tree.model.root);
}

function treeSelectId(tree,id) {
	tree = dijit.byId(tree);
    var path = treeFindPath(tree,id);
    if(path){
        tree.set('paths',[path])
    }
}

function treeClearSelection(tree) {
	tree = dijit.byId(tree);
	tree.attr('paths',[])
}

/**
 * Cross browser utilities
 */
function fireOnClick(target){
	target = dojo.byId(target);
	if(target) {
		if(dojo.isIE) {
			target.fireEvent('onclick');
		} else {
			var o = document.createEvent('MouseEvents');
			o.initEvent( 'click', true, false );
			target.dispatchEvent(o);
		}
	}
}

/**
 * Attach an event to hode/show a label
 */
function attachLabel(editor,label) {
	require(["dojo/on","dojo/_base/fx"], function(on,fx){
		on(editor.parentNode,"mouseover", function() {
			fx.fadeOut({node:label}).play();
		});
		on(editor.parentNode,"mouseout", function() {
			fx.fadeIn({node:label}).play();
		});
	});	
}

/**
 * Show the documentation panel
 */
function _showDocumentation(show) {
	if(pageGlobal.resultStack) {
		var sc = dijit.byId(pageGlobal.resultStack);
		var pn = sc.getChildren()[show ? 1 : 0];
		sc.selectChild(pn);
	}
}

/**
 * Set an icon to code mirror tab
 */
function attachTabIcon(tabId,cm) {
	if(tabId) {
		cm.on('change', function() {
			function isEmpty(cm) {
				return (cm.lineCount() === 1) && (cm.getLine(0) === "");
			}
			dijit.byId(tabId).set('iconClass',isEmpty(cm)?'iconTabEmpty':'iconTabFull');
		});
	} 
}
