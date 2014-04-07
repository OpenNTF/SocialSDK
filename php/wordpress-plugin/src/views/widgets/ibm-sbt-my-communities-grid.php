<div id="***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>"></div>
***REMOVED*** 
// Ensure that element IDs are unique
$timestamp = time();

if ((isset($instance['ibm-sbtk-template']) && $instance['ibm-sbtk-template'] != "")) {
	require_once BASE_PATH . "{$instance['ibm-sbtk-template']}";
} else {
	require_once 'templates/ibm-sbt-communities-grid-row.php';
}
?>
<script type="text/javascript">
require(["sbt/dom", "sbt/connections/controls/communities/CommunityGrid"], function(dom, CommunityGrid) {
	var domNode = dom.byId("communityRow-***REMOVED*** echo $timestamp; ?>");
    var CommunityRow = domNode.text || domNode.textContent;
    domNode = dom.byId("communityPagingHeader-***REMOVED*** echo $timestamp; ?>");
    var PagingHeader = domNode.text || domNode.textContent;
    domNode = dom.byId("communityPagingFooter-***REMOVED*** echo $timestamp; ?>");
    var PagingFooter = domNode.text || domNode.textContent;
    
    var grid = new CommunityGrid({
    	type : "***REMOVED*** echo $instance['ibm-sbtk-communities-type'];?>",
        hidePager: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-pager']) && $instance['ibm-sbtk-grid-pager'] == 'pager' ? "false" : "true"); ?>,
        hideSorter: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-sorter']) && $instance['ibm-sbtk-grid-sorter'] == 'sorter' ? "false" : "true"); ?>,
        hideFooter: ***REMOVED*** echo (isset($instance['ibm-sbtk-grid-footer']) && $instance['ibm-sbtk-grid-footer'] == 'footer' ? "false" : "true"); ?>,
        endpoint: "***REMOVED*** echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>",
        pageSize: ***REMOVED*** echo $instance['ibm-sbtk-grid-page-size']; ?>,
        rendererArgs : { template : CommunityRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter }
    });

    grid.renderer.tableClass = "table";
    grid.renderer.template = CommunityRow;
             
    dom.byId("***REMOVED*** echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>").appendChild(grid.domNode);
             
    grid.update();
});

</script>
