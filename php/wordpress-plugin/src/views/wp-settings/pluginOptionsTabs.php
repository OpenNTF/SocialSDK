<?php screen_icon(); ?>
<h2 class="nav-tab-wrapper">
<?php
foreach ( $viewData ['settings_tabs'] as $tab_key => $tab_caption ) {
	$active = $viewData ['current_tab'] == $tab_key ? 'nav-tab-active' : '';
	echo '<a class="nav-tab ' . $active . '" href="?page=' . $viewData ['settings_page'] . '&tab=' . $tab_key . '">' . $tab_caption . '</a>';
}
?>
</h2>