
<?php 
// 	define('SAMPLES_PATH', BASE_PATH . '/views/social');
	
	$pageURL = (@$_SERVER["HTTPS"] == "on") ? "https://" : "http://";
	$pageURL .= $_SERVER["SERVER_NAME"] . ":" . $_SERVER["SERVER_PORT"] . dirname($_SERVER["REQUEST_URI"]);

	print '<link href="' . $pageURL . '/../wp-content/plugins/sbtk-wp/views/js/codeMirror/lib/codemirror.css" type="text/css" rel="stylesheet" />';
	print '<link href="' . $pageURL . '/../wp-content/plugins/sbtk-wp/views/css/codeEditor.css" type="text/css" rel="stylesheet" />';
	
	print '<script language="javascript" type="text/javascript" src="' . $pageURL . '/../wp-content/plugins/sbtk-wp/system/libs/code-mirror/lib/codemirror.js"></script>';
	print '<script language="javascript" type="text/javascript" src="' . $pageURL . '/../wp-content/plugins/sbtk-wp/system/libs/code-mirror/mode/javascript/javascript.js"></script>';
	$pluginURL = str_replace('/core/', '', $pageURL);
	print '<script language="javascript" type="text/javascript" src="' . $pluginURL . '/../wp-content/plugins/sbtk-wp/views/js/customPluginEditor.js"></script>';
?>
<input type="hidden" id="delete_widget" name="delete_widget" value="no" />
<table>
	<tr>
		<td><strong>Available Widgets:</strong></td>
		<td><select onchange="plugin_change();" id="plugin_list" name="plugin_list">
    	<?php 
    	
    		if (isset($viewData['plugins'])) {
	    		$plugins = $viewData['plugins'];
	    		echo '<optgroup label="Custom Widgets"></optgroup>';
	    		foreach ($plugins as $plugin) {
					if (isset($viewData['selected_custom_plugin']) && $viewData['selected_custom_plugin'] == $plugin) {
						echo '<option selected="selected" value="' . $plugin . '">' . $plugin . '</option>';
					} else {
						echo '<option value="' . $plugin . '">' . $plugin . '</option>';
					}
				}
// 				$plugin = $plugins[0];
// 				$pluginData = $viewData[$plugin];
				if (isset($viewData['selected_custom_plugin'])) {
					$pluginData = $viewData[$viewData['selected_custom_plugin']];
				}
			}
    	?>
		</select></td>
		<td><input type="button" onclick="new_widget();" name="btn_new_widget" id="btn_new_widget" class="button button-primary" value="New Widget" /></td>
	</tr>
	<tr>
		<td><strong>Selected Widget:</strong></td>
		<td><input type="text" name="plugin_name" id="plugin_name" value="<?php echo (isset($viewData['selected_custom_plugin']) ? $viewData['selected_custom_plugin'] : '') ?>" /></td>
		<td><input type="button" onclick="confirm_delete_widget();" name="btn_delete_widget" id="btn_delete_widget" class="button button-primary" value="Delete Widget" /></td>
	</tr>
</table>

<strong>JavaScript:</strong><br/>
<textarea id="javascript" name="javascript"><?php echo (isset($pluginData['javascript']) ? $pluginData['javascript'] : '') ?></textarea>
<br/>
<strong>HTML:</strong><br/>
<textarea id="html" name="html"><?php echo (isset($pluginData['html']) ? $pluginData['html'] : '') ?></textarea>

<input type="hidden" name="update_plugins" id="update_plugins" value="1" />
<script type="text/javascript">
	var jsEditor = CodeMirror.fromTextArea(document.getElementById("javascript"), {
		lineNumbers: true,
        mode: "javascript",
        lineWrapping: true
    });
	jsEditor.on("blur", function() {jsEditor.save();});

	var htmlEditor = CodeMirror.fromTextArea(document.getElementById("html"), {
		lineNumbers: true,
        mode: "text/html",
        lineWrapping: true
    });
	htmlEditor.on("blur", function() {htmlEditor.save();});

	<?php //require_once BASE_PATH . '/views/js/customPluginEditor.js'; ?>
</script>
	
	
<input type="hidden" name="selected_custom_plugin" id="selected_custom_plugin" value="<?php echo (isset($viewData['selected_custom_plugin']) ? $viewData['selected_custom_plugin'] : '') ?>" />