<div id="bookmarksGridDiv"></div>

<script type="text/javascript">
	require(["sbt/dom", "sbt/connections/controls/bookmarks/BookmarkGrid"], 
    	function(dom, BookmarkGrid) {
	   		var grid = new BookmarkGrid({
	    		type : "my",
	         	hidePager : ***REMOVED*** echo ($hidePager == 1 ? "true" : "false") ?>,
	         	hideSorter : ***REMOVED*** echo ($hideSorter == 1 ? "true" : "false") ?>,
	      		rendererArgs : { containerType : "***REMOVED*** echo $containerType; ?>" }
	   		});
	                
	   		dom.byId("bookmarksGridDiv").appendChild(grid.domNode);
	        grid.update();
   		});
</script>

