function fn_My_Plugin_3($args) {
			$plugin = new SBTKForums();
	
			if (!$headerCreated) {
				$plugin->createHeader();
				$headerCreated = true;
			}
	
			$pluginData = get_option($args['widget_name']);
			echo $pluginData['html'];
			echo '<script type="text/javascript">' . $pluginData['javascript'] . '</script>';}