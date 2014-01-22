<select onchange="endpoint_change();" id="enpoint_list" name="endpoint_list">
    	<?php 
    	if ($viewData['selected_endpoint'] == 'IBM SmartCloud for Social Business') {
    		print '<option value="IBM SmartCloud for Social Business">IBM SmartCloud for Social Business</option>';
    		print '<option value="IBM Connections on Premises">IBM Connections on Premises</option>';
    	} else {
    		print '<option value="IBM Connections on Premises">IBM Connections on Premises</option>';
    		print '<option value="IBM SmartCloud for Social Business">IBM SmartCloud for Social Business</option>';
    	}
    	?>
</select>