<div
	id="<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>"></div>
<?php
// Ensure that element IDs are unique
$timestamp = time ();

if ((isset ( $instance ['ibm-sbtk-template'] ) && $instance ['ibm-sbtk-template'] != "")) {
	require_once BASE_PATH . "{$instance['ibm-sbtk-template']}";
} else {
	require_once 'templates/ibm-sbt-communities-grid-row.php';
}
?>
<script type="text/javascript">
require(["sbt/dom", "sbt/connections/controls/communities/CommunityGrid"], function(dom, CommunityGrid) {
	var domNode = dom.byId("communityRow-<?php echo $timestamp; ?>");
    var CommunityRow = domNode.text || domNode.textContent;
    domNode = dom.byId("communityPagingHeader-<?php echo $timestamp; ?>");
    var PagingHeader = domNode.text || domNode.textContent;
    domNode = dom.byId("communityPagingFooter-<?php echo $timestamp; ?>");
    var PagingFooter = domNode.text || domNode.textContent;
    
    var grid = new CommunityGrid({
    	type : "<?php echo $instance['ibm-sbtk-communities-type'];?>",
        hidePager: <?php echo (isset($instance['ibm-sbtk-grid-pager']) && $instance['ibm-sbtk-grid-pager'] == 'pager' ? "false" : "true"); ?>,
        hideSorter: <?php echo (isset($instance['ibm-sbtk-grid-sorter']) && $instance['ibm-sbtk-grid-sorter'] == 'sorter' ? "false" : "true"); ?>,
        hideFooter: <?php echo (isset($instance['ibm-sbtk-grid-footer']) && $instance['ibm-sbtk-grid-footer'] == 'footer' ? "false" : "true"); ?>,
        endpoint: "<?php echo (isset($instance['ibm-sbtk-endpoint']) ? $instance['ibm-sbtk-endpoint'] : 'connections'); ?>",
        pageSize: <?php echo $instance['ibm-sbtk-grid-page-size']; ?>,
        rendererArgs : { template : CommunityRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter }
    });

    grid.renderer.tableClass = "table";
    grid.renderer.template = CommunityRow;
             
    dom.byId("<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : $this->elID); ?>").appendChild(grid.domNode);
             
    grid.update();
});

</script>
