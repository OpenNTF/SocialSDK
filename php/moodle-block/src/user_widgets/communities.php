<div id="***REMOVED*** echo $this->config->elementID;?>"></div>

***REMOVED*** 
if (isset($this->config->template)) {
	require_once BASE_PATH . $this->config->template;
} else {
	require_once 'templates/ibm-sbt-communities-grid-row.php';
}
?>

<script type="text/javascript">
require(["sbt/dom", "sbt/connections/controls/communities/CommunityGrid"], function(dom, CommunityGrid) {
	var domNode = dom.byId("communityRow");
    var CommunityRow = domNode.text || domNode.textContent;
    domNode = dom.byId("pagingHeader");
    var PagingHeader = domNode.text || domNode.textContent;
    domNode = dom.byId("pagingFooter");
    var PagingFooter = domNode.text || domNode.textContent;
    
    var grid = new CommunityGrid({
    	type : "my",
    	endpoint: "***REMOVED*** echo $this->config->endpoint; ?>",
        hidePager: false,
        hideSorter: true,
        hideFooter: false,
        rendererArgs : { template : CommunityRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter }
    });

    grid.renderer.tableClass = "table";
    grid.renderer.template = CommunityRow;
             
    dom.byId("***REMOVED*** echo $this->config->elementID;?>").appendChild(grid.domNode);
             
    grid.update();
});
</script>