<select id="basic_auth_method" name="basic_auth_method">
		<?php if ($viewData['basic_auth_method'] == 'global') {
			echo '<option value="global">Global user credentials</option>';
    		echo '<option value="prompt">Prompt for user credentials</option>';

		} else {
			echo '<option value="prompt">Prompt for user credentials</option>';
			echo '<option value="global">Global user credentials</option>';
		}
    	?>
</select>