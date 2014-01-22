***REMOVED***




function sbtk_community_grid($data) {

	$postcontent = '<div id="tagCommunityGridID"></div>
			<script type="text/javascript">
			require(["sbt/dom", "sbt/connections/controls/communities/CommunityGrid"],
			function(dom, CommunityGrid) {
			var grid = new CommunityGrid({
			// type : "my",
		
	                    hidePager : true,
	                    hideSorter : true,
			});

			dom.byId("tagCommunityGridID").appendChild(grid.domNode);
			 
			grid.update();
			});
			</script>';
	//todo: add support div id generation

	preg_match_all("/sbtk_community_grid/",$data,$results, PREG_SET_ORDER);

	if($results)  {
		foreach($results as $result) {
				

			$data = str_replace( "[".$result[0]."]", $postcontent, $data );
		}
	}
	return $data;

}



add_filter('the_content', 'sbtk_community_grid');

?>
