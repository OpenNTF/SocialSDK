<?php
$scripts = array(
		'social/blogs/all-blogs-grid',
		'social/blogs/blog-comments-grid',
		'social/blogs/my-blogs-grid',
		'social/blogs/featured-blogs-grid',
		'social/blogs/featured-posts-grid',

		'social/bookmarks/bookmarks-grid',

		'social/communities/community-grid',
		'social/communities/community-members-grid',
		'social/communities/my-community-invites-grid',
		'social/communities/start-new-community',
		'social/communities/update-community-logo',

		'social/files/files-grid',
		'social/files/files-shared-by-me-grid',
		'social/files/files-shared-with-me-grid',
		'social/files/my-folders-grid',
		'social/files/my-file-comments-grid',
		'social/files/public-file-comments-grid',
		'social/files/public-files-grid',

		'social/forums/forum-answered-topics-grid',
		'social/forums/forum-entries-grid',
		'social/forums/forums-grid',
		'social/forums/topics-grid',

		'social/profiles/my-profile-panel',
		
		'social/sametime/smartcloud-chat'
);

// Print script contents
foreach ($scripts as $script) {
	$sbtkPlugin = __DIR__ . '/core/views/' . $script . '.php';
	$file = file_get_contents($sbtkPlugin);

	// Extract HTML
	$html = preg_replace('#<script.*</script>#is', '', $file);

	// Extract JavaScript
	preg_match('#<script.*</script>#is', $file, $javascript);
	$javascript = str_replace('</script>', '', $javascript);
	$javascript = str_replace('<script type="text/javascript">', '', $javascript);
	$javascript = preg_replace("/\<\?php.*?>/is", "true", $javascript);
	$javascript = str_replace('rendererArgs : { containerType : "true" }', '', $javascript);
	
	$mform->addElement('html', '<textarea  id="html_/core/views/' . $script . '.php" style="display: none;">' . $html . '</textarea>');
	$mform->addElement('html', '<textarea  id="javascript_/core/views/' . $script . '.php" style="display: none;">' . $javascript[0] . '</textarea>');
}

	// Get custom code
	try {
		global $DB;
		$blockInstance = $DB->get_record('block_instances', array('id' => $_GET['bui_editid']), '*', IGNORE_MISSING);

		$ibmsbtk = block_instance('ibmsbtk', $blockInstance);

		$mform->addElement('html', '<textarea  id="html_custom" style="display: none;">' . $ibmsbtk->config->customHTML['text'] . '</textarea>');
		$mform->addElement('html', '<textarea  id="javascript_custom" style="display: none;">' . $ibmsbtk->config->customCode['text'] . '</textarea>');
	} catch(Exception $e) {
		
	}
	
	$pageURL = (@$_SERVER["HTTPS"] == "on") ? "https://" : "http://";
	$pageURL .= $_SERVER["SERVER_NAME"] . ":" . $_SERVER["SERVER_PORT"] . dirname($_SERVER["REQUEST_URI"]);
	
	print '<link href="' . $pageURL . '/../blocks/ibmsbtk/core/system/libs/code-mirror/lib/codemirror.css" type="text/css" rel="stylesheet" />';
	print '<link href="' . $pageURL . '/../blocks/ibmsbtk/views/css/codeEditor.css" type="text/css" rel="stylesheet" />';
	
	print '<script type="text/javascript">';
	include BASE_PATH . '/system/libs/code-mirror/lib/codemirror.js';
	include BASE_PATH . '/system/libs/code-mirror/mode/javascript/javascript.js';
	print '</script>';

	ob_start();
	include BASE_PATH . '/../views/js/customPluginEditor.js';
	
	// Print JavaScript for adding event listener to type dropdown and integrate CodeMirror
	$mform->addElement('html', '<script type="text/javascript">' . ob_get_clean() . '</script>');

