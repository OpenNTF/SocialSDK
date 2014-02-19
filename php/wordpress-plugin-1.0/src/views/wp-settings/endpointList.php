<select onchange="endpoint_change();" id="enpoint_list" name="endpoint_list">
    	***REMOVED*** 
    	foreach ($viewData['endpoints'] as $endpoint) {
    		if ($viewData['selected_endpoint'] == $endpoint) {
    			print '<option selected="selected" value="' . $endpoint . '">' . $endpoint . '</option>';
    		} else {
    			print '<option value="' . $endpoint . '">' . $endpoint . '</option>';
    		}
    	}
    	?>
</select>