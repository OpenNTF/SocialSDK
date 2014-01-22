***REMOVED***
/**
 * @package WordPress
 * @subpackage Theme_Compat
 * @deprecated 3.0
 *
 * This file is here for Backwards compatibility with old themes and will be removed in a future version
 *
 */
_deprecated_file( sprintf( __( 'Theme without %1$s' ), basename(__FILE__) ), '3.0', null, sprintf( __('Please include a %1$s template in your theme.'), basename(__FILE__) ) );
?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" ***REMOVED*** language_attributes(); ?>>

<head profile="http://gmpg.org/xfn/11">
<meta http-equiv="Content-Type" content="***REMOVED*** bloginfo('html_type'); ?>; charset=***REMOVED*** bloginfo('charset'); ?>" />

<title>***REMOVED*** wp_title('&laquo;', true, 'right'); ?> ***REMOVED*** bloginfo('name'); ?></title>

<link rel="stylesheet" href="***REMOVED*** bloginfo('stylesheet_url'); ?>" type="text/css" media="screen" />
<link rel="pingback" href="***REMOVED*** bloginfo('pingback_url'); ?>" />

<style type="text/css" media="screen">

***REMOVED***
// Checks to see whether it needs a sidebar
if ( empty($withcomments) && !is_single() ) {
?>
	#page { background: url("***REMOVED*** bloginfo('stylesheet_directory'); ?>/images/kubrickbg-***REMOVED*** bloginfo('text_direction'); ?>.jpg") repeat-y top; border: none; }
***REMOVED*** } else { // No sidebar ?>
	#page { background: url("***REMOVED*** bloginfo('stylesheet_directory'); ?>/images/kubrickbgwide.jpg") repeat-y top; border: none; }
***REMOVED*** } ?>

</style>

***REMOVED*** if ( is_singular() ) wp_enqueue_script( 'comment-reply' ); ?>

***REMOVED*** wp_head(); ?>
</head>
<body ***REMOVED*** body_class(); ?>>
<div id="page">

<div id="header" role="banner">
	<div id="headerimg">
		<h1><a href="***REMOVED*** echo home_url(); ?>/">***REMOVED*** bloginfo('name'); ?></a></h1>
		<div class="description">***REMOVED*** bloginfo('description'); ?></div>
	</div>
</div>
<hr />
