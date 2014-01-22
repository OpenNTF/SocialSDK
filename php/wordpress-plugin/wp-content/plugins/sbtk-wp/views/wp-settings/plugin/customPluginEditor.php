
***REMOVED*** 
// 	define('SAMPLES_PATH', BASE_PATH . '/views/social');
	
	$pageURL = (@$_SERVER["HTTPS"] == "on") ? "https://" : "http://";
	$pageURL .= $_SERVER["SERVER_NAME"] . ":" . $_SERVER["SERVER_PORT"] . dirname($_SERVER["REQUEST_URI"]);

	print '<link href="' . $pageURL . '/../wp-content/plugins/sbtk-wp/views/js/codeMirror/lib/codemirror.css" type="text/css" rel="stylesheet" />';
	print '<link href="' . $pageURL . '/../wp-content/plugins/sbtk-wp/views/css/codeEditor.css" type="text/css" rel="stylesheet" />';
	
	print '<script type="text/javascript">';

	require_once BASE_PATH . '/views/js/codeMirror/lib/codemirror.js';
	require_once BASE_PATH . '/views/js/codeMirror/mode/javascript/javascript.js';
	
	print '</script>';
?>
<input type="hidden" id="delete_widget" name="delete_widget" value="no" />
<table>
	<tr>
		<td><strong>Available Widgets:</strong></td>
		<td><select onchange="plugin_change();" id="plugin_list" name="plugin_list">
    	***REMOVED*** 
    	
    		$sampleTypes = array(
				'blogs',
				'bookmarks',
				'communities',
				'files',
				'forums',
				'profiles'
			);
    	
    		function getPluginName($pluginFile) {
				$plugin = str_replace('.php', '', $pluginFile);
				$plugin = str_replace('-', ' ', $plugin);
				return ucwords($plugin);
			}
			
			function endsWith($haystack, $needle) {
				return $needle === "" || substr($haystack, -strlen($needle)) === $needle;
			}
    	
    		if (isset($viewData['plugins'])) {
	    		$plugins = $viewData['plugins'];
	    		echo '<optgroup label="Custom Widgets"></optgroup>';
	    		foreach ($plugins as $plugin) {
					if (endsWith($plugin, '.php')) {
						continue;
					}
					echo '<option value="' . $plugin . '">' . $plugin . '</option>';
				}
				$plugin = $plugins[0];
				$pluginData = $viewData[$plugin];
			}

			foreach ($sampleTypes as $sample) {
				$samples = scandir(SAMPLES_PATH . '/' . $sample);
				if (isset($samples)) {
					echo '<optgroup label="' . ucwords($sample) . '"></optgroup>';
					foreach ($samples as $plugin) {
						if ($plugin == '.' || $plugin == '..') {
							continue;
						}
						echo '<option value="' . $plugin . '">&nbsp;&nbsp;' . getPluginName($plugin) . '</option>';
					}
				}
			}
    	?>
		</select></td>
		<td><input type="button" onclick="new_widget();" name="btn_new_widget" id="btn_new_widget" class="button button-primary" value="New Widget" /></td>
	</tr>
	<tr>
		<td><strong>Selected Widget:</strong></td>
		<td><input type="text" name="plugin_name" id="plugin_name" value="***REMOVED*** echo (isset($plugins) ? $plugins[0] : '') ?>" /></td>
		<td><input type="button" onclick="confirm_delete_widget();" name="btn_delete_widget" id="btn_delete_widget" class="button button-primary" value="Delete Widget" /></td>
	</tr>
</table>

<strong>JavaScript:</strong><br/>
<textarea id="javascript" name="javascript">***REMOVED*** echo (isset($pluginData['javascript']) ? $pluginData['javascript'] : '') ?></textarea>
<br/>
<strong>HTML:</strong><br/>
<textarea id="html" name="html">***REMOVED*** echo (isset($pluginData['html']) ? $pluginData['html'] : '') ?></textarea>

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

	***REMOVED*** require_once BASE_PATH . '/views/js/customPluginEditor.js'; ?>
</script>