<?php 

require_once BASE_PATH . '/PluginUpdate.php';
register_setting(
'community_grid_settings_group', // Option group
'community_grid_settings', // Option name
array() // Sanitize
);

if (isset($_POST['submitted'])) {
	$optionsUpdate = new PluginUpdate('community');
}

$pluginSettings = get_option('community_grid_settings');

?>
Would you like to display a pager? <br/>
<input type="radio" name="community_grid_pager" value="yes" <?php ($pluginSettings['community_grid_pager'] == 'yes' ? 'checked' : '') ?>/> Yes<br/>
<input type="radio" name="community_grid_pager" value="no" <?php ($pluginSettings['community_grid_pager'] == 'no' ? 'checked' : '') ?>/> No<br/>
<br/>
Would you like to display a sorter? <br/>
<input type="radio" name="community_grid_sorter" value="yes" <?php ($pluginSettings['community_grid_sorter'] == 'yes' ? 'checked' : '') ?>/> Yes<br/>
<input type="radio" name="community_grid_sorter" value="no" <?php ($pluginSettings['community_grid_sorter'] == 'no' ? 'checked' : '') ?>/> No<br/>
<br/>
What container type would you like? <br/>
<input type="radio" name="community_grid_container_type" value="ol" <?php ($pluginSettings['community_grid_container_type'] == 'ol' ? 'checked' : '') ?>/> Ordered list<br/>
<input type="radio" name="community_grid_container_type" value="ul" <?php ($pluginSettings['community_grid_container_type'] == 'ul' ? 'checked' : '') ?>/> Unordered list<br/>
<input type="radio" name="community_grid_container_type" value="table" <?php ($pluginSettings['community_grid_container_type'] == 'table' ? 'checked' : '') ?>/> Table<br/>

<input type="hidden" name="submitted" value="1" />