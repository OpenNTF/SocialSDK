<select id="authentication_method" name="authentication_method" onchange="change_authentication_method();">
	<option <?php echo ($viewData['authentication_method'] == 'oauth1' ? 'selected="selected"' : ''); ?> value="oauth1">OAuth 1.0</option>
	<option <?php echo ($viewData['authentication_method'] == 'basic' ? 'selected="selected"' : ''); ?> value="basic">Basic</option>
</select>