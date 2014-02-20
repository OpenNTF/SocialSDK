<div id="<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>"></div>
<?php require_once 'templates/ibm-sbtk-communities-grid-row.php';?>
<script type="text/javascript">
require(["sbt/dom", "sbt/connections/controls/communities/CommunityGrid"], function(dom, CommunityGrid) {
	var domNode = dom.byId("communityRow");
    var CommunityRow = domNode.text || domNode.textContent;
    domNode = dom.byId("pagingHeader");
    var PagingHeader = domNode.text || domNode.textContent;
    domNode = dom.byId("pagingFooter");
    var PagingFooter = domNode.text || domNode.textContent;
    
    var grid = new CommunityGrid({
    	type : "<?php echo $instance['ibm-sbtk-communities-type'];?>",
        hidePager: <?php echo (isset($instance['ibm-sbtk-grid-pager']) && $instance['ibm-sbtk-grid-pager'] == 'pager' ? "false" : "true"); ?>,
        hideSorter: <?php echo (isset($instance['ibm-sbtk-grid-sorter']) && $instance['ibm-sbtk-grid-sorter'] == 'sorter' ? "false" : "true"); ?>,
        hideFooter: <?php echo (isset($instance['ibm-sbtk-grid-footer']) && $instance['ibm-sbtk-grid-footer'] == 'footer' ? "false" : "true"); ?>,
   	 	ps: "<?php echo $instance['ibm-sbtk-grid-page-size']; ?>",
         rendererArgs : { template : CommunityRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter }
    });

    grid.renderer.tableClass = "table";
    grid.renderer.template = CommunityRow;
             
    dom.byId("<?php echo (isset($instance['ibm-sbtk-element-id']) ? $instance['ibm-sbtk-element-id'] : "myIBMElementID"); ?>").appendChild(grid.domNode);
             
    grid.update();
});

</script>
