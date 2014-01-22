***REMOVED*** 

if (isset($viewData['plugins'])) {

	// Print contents of all plugins / widgets
	foreach ($viewData['plugins'] as $plugin) {
		$pluginData = $viewData[$plugin];
		$javascript = $pluginData['javascript'];
		$html = $pluginData['html'];
		
		print '<textarea style="display: none;" id="' . $plugin . '_js">' . $javascript . '</textarea>';
		print '<textarea style="display: none;" id="' . $plugin . '_html">' . $html . '</textarea>';
	}
}
?>
