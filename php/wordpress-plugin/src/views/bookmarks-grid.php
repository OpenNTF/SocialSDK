***REMOVED***
	extract($args);
	echo $before_widget;
	echo $before_title . 'My Bookmarks' . $after_title; 
	?>
            <div id="gridDiv"></div>
          	
            <script type="text/javascript">
            require(["sbt/dom", "sbt/connections/controls/bookmarks/BookmarkGrid"], 
            	function(dom, BookmarkGrid) {

	                var grid = new BookmarkGrid({
	                    type : "my",
	                    hidePager : true,
	                    hideSorter : true,
	                    rendererArgs : { containerType : "ul" }
	                });
	                
	                dom.byId("gridDiv").appendChild(grid.domNode);
	                         
	                grid.update();
            });
				</script>            
        ***REMOVED*** echo $after_widget; ?>
***REMOVED***


