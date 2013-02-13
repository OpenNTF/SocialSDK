/**
 * Resize the main border container when the window is resized
 * This is required as it should substract the size of the headers and footers
 * @return
 */
function resize() {
	require(["dojo/dom-geometry"], function(domGeom){
		function eltHeight(c) {
			var e = dojo.query(c);
			var h = e && e.length>0?domGeom.getMarginBox(e[0]).h:0;
			return h;
		}
		var h = document.body.clientHeight;
		var hd = eltHeight(".lotusBanner")+eltHeight(".lotusTitleBar")+eltHeight(".lotusPlaceBar");
		var ft = eltHeight(".lotusFooter")+eltHeight(".lotusLegal");
		dojo.query(".lotusContent").style("height",(h-hd-ft)+"px");
		//console.log("h="+h+", hd="+hd+", ft="+ft+", result="+(h-hd-ft-25))
		dijit.byId(pageGlobal.borderContainer).resize()
	});
}

//
// Tree helpers
//

function treeCollapseAll(tree) {
	tree = dijit.byId(tree);
	if(tree.collapseAll) { // 1.8 and later
		tree.collapseAll();
	} else {
        function collapse(node) {
        	if(node!=tree.rootNode) {
        		tree._collapseNode(node);
        	}
            dojo.map(node.getChildren(),collapse);
        }
        return collapse(tree.rootNode);
    }
}

function treeExpandAll(tree) { 
	tree = dijit.byId(tree);
	if(tree.expandAll) { // 1.8 and later
		tree.expandAll();
	} else {
        function expand(node) {
        	if(node!=tree.rootNode) {
        		tree._expandNode(node);
        	}
            dojo.map(node.getChildren(),expand);
        }
        return expand(tree.rootNode);
    }
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
