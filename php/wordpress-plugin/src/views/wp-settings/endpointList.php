<select style="width: 350px; height: 150px;" size="<?php echo sizeof($viewData['endpoints']); ?>" onchange="endpoint_change();" id="enpoint_list" name="endpoint_list">
    	<?php 
    	foreach ($viewData['endpoints'] as $endpoint) {
    		if ($viewData['selected_endpoint'] == $endpoint['name']) {
    			print '<option selected="selected" value="' . $endpoint['name'] . '">' . $endpoint['name'] . ' (' . $endpoint['url'] . ')</option>';
    		} else {
    			print '<option value="' . $endpoint['name'] . '">' . $endpoint['name'] . ' (' . $endpoint['url'] .  ')</option>';
    		}
    	}
    	?>
</select><br/>