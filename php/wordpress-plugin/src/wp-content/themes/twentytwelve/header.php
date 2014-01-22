***REMOVED***
/**
 * The Header template for our theme
 *
 * Displays all of the <head> section and everything up till <div id="main">
 *
 * @package WordPress
 * @subpackage Twenty_Twelve
 * @since Twenty Twelve 1.0
 */
?><!DOCTYPE html>
<!--[if IE 7]>
<html class="ie ie7" ***REMOVED*** language_attributes(); ?>>
<![endif]-->
<!--[if IE 8]>
<html class="ie ie8" ***REMOVED*** language_attributes(); ?>>
<![endif]-->
<!--[if !(IE 7) | !(IE 8)  ]><!-->
<html ***REMOVED*** language_attributes(); ?>>
<!--<![endif]-->
<head>
<meta charset="***REMOVED*** bloginfo( 'charset' ); ?>" />
<meta name="viewport" content="width=device-width" />
<title>***REMOVED*** wp_title( '|', true, 'right' ); ?></title>
<link rel="profile" href="http://gmpg.org/xfn/11" />
<link rel="pingback" href="***REMOVED*** bloginfo( 'pingback_url' ); ?>" />
***REMOVED*** // Loads HTML5 JavaScript file to add support for HTML5 elements in older IE versions. ?>
<!--[if lt IE 9]>
<script src="***REMOVED*** echo get_template_directory_uri(); ?>/js/html5.js" type="text/javascript"></script>
<![endif]-->
***REMOVED*** wp_head(); ?>
</head>

<body ***REMOVED*** body_class(); ?>>
<div id="page" class="hfeed site">
	<header id="masthead" class="site-header" role="banner">
		<hgroup>
			<h1 class="site-title"><a href="***REMOVED*** echo esc_url( home_url( '/' ) ); ?>" title="***REMOVED*** echo esc_attr( get_bloginfo( 'name', 'display' ) ); ?>" rel="home">***REMOVED*** bloginfo( 'name' ); ?></a></h1>
			<h2 class="site-description">***REMOVED*** bloginfo( 'description' ); ?></h2>
		</hgroup>

		<nav id="site-navigation" class="main-navigation" role="navigation">
			<h3 class="menu-toggle">***REMOVED*** _e( 'Menu', 'twentytwelve' ); ?></h3>
			<a class="assistive-text" href="#content" title="***REMOVED*** esc_attr_e( 'Skip to content', 'twentytwelve' ); ?>">***REMOVED*** _e( 'Skip to content', 'twentytwelve' ); ?></a>
			***REMOVED*** wp_nav_menu( array( 'theme_location' => 'primary', 'menu_class' => 'nav-menu' ) ); ?>
		</nav><!-- #site-navigation -->

		***REMOVED*** if ( get_header_image() ) : ?>
		<a href="***REMOVED*** echo esc_url( home_url( '/' ) ); ?>"><img src="***REMOVED*** header_image(); ?>" class="header-image" width="***REMOVED*** echo get_custom_header()->width; ?>" height="***REMOVED*** echo get_custom_header()->height; ?>" alt="" /></a>
		***REMOVED*** endif; ?>
	</header><!-- #masthead -->

	<div id="main" class="wrapper">