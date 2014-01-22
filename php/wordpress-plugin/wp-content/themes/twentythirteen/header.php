***REMOVED***
/**
 * The Header template for our theme
 *
 * Displays all of the <head> section and everything up till <div id="main">
 *
 * @package WordPress
 * @subpackage Twenty_Thirteen
 * @since Twenty Thirteen 1.0
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
	<meta charset="***REMOVED*** bloginfo( 'charset' ); ?>">
	<meta name="viewport" content="width=device-width">
	<title>***REMOVED*** wp_title( '|', true, 'right' ); ?></title>
	<link rel="profile" href="http://gmpg.org/xfn/11">
	<link rel="pingback" href="***REMOVED*** bloginfo( 'pingback_url' ); ?>">
	<!--[if lt IE 9]>
	<script src="***REMOVED*** echo get_template_directory_uri(); ?>/js/html5.js"></script>
	<![endif]-->
	***REMOVED*** wp_head(); ?>
</head>

<body ***REMOVED*** body_class(); ?>>
	<div id="page" class="hfeed site">
		<header id="masthead" class="site-header" role="banner">
			<a class="home-link" href="***REMOVED*** echo esc_url( home_url( '/' ) ); ?>" title="***REMOVED*** echo esc_attr( get_bloginfo( 'name', 'display' ) ); ?>" rel="home">
				<h1 class="site-title">***REMOVED*** bloginfo( 'name' ); ?></h1>
				<h2 class="site-description">***REMOVED*** bloginfo( 'description' ); ?></h2>
			</a>

			<div id="navbar" class="navbar">
				<nav id="site-navigation" class="navigation main-navigation" role="navigation">
					<h3 class="menu-toggle">***REMOVED*** _e( 'Menu', 'twentythirteen' ); ?></h3>
					<a class="screen-reader-text skip-link" href="#content" title="***REMOVED*** esc_attr_e( 'Skip to content', 'twentythirteen' ); ?>">***REMOVED*** _e( 'Skip to content', 'twentythirteen' ); ?></a>
					***REMOVED*** wp_nav_menu( array( 'theme_location' => 'primary', 'menu_class' => 'nav-menu' ) ); ?>
					***REMOVED*** get_search_form(); ?>
				</nav><!-- #site-navigation -->
			</div><!-- #navbar -->
		</header><!-- #masthead -->

		<div id="main" class="site-main">
