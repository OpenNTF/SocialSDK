***REMOVED***
/**
 * Upgrade WordPress Page.
 *
 * @package WordPress
 * @subpackage Administration
 */

/**
 * We are upgrading WordPress.
 *
 * @since 1.5.1
 * @var bool
 */
define( 'WP_INSTALLING', true );

/** Load WordPress Bootstrap */
require( dirname( dirname( __FILE__ ) ) . '/wp-load.php' );

nocache_headers();

timer_start();
require_once( ABSPATH . 'wp-admin/includes/upgrade.php' );

delete_site_transient('update_core');

if ( isset( $_GET['step'] ) )
	$step = $_GET['step'];
else
	$step = 0;

// Do it. No output.
if ( 'upgrade_db' === $step ) {
	wp_upgrade();
	die( '0' );
}

$step = (int) $step;

$php_version    = phpversion();
$mysql_version  = $wpdb->db_version();
$php_compat     = version_compare( $php_version, $required_php_version, '>=' );
if ( file_exists( WP_CONTENT_DIR . '/db.php' ) && empty( $wpdb->is_mysql ) )
	$mysql_compat = true;
else
	$mysql_compat = version_compare( $mysql_version, $required_mysql_version, '>=' );

@header( 'Content-Type: ' . get_option( 'html_type' ) . '; charset=' . get_option( 'blog_charset' ) );
?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" ***REMOVED*** language_attributes(); ?>>
<head>
	<meta http-equiv="Content-Type" content="***REMOVED*** bloginfo( 'html_type' ); ?>; charset=***REMOVED*** echo get_option( 'blog_charset' ); ?>" />
	<title>***REMOVED*** _e( 'WordPress &rsaquo; Update' ); ?></title>
	***REMOVED***
	wp_admin_css( 'install', true );
	wp_admin_css( 'ie', true );
	?>
</head>
<body class="wp-core-ui">
<h1 id="logo"><a href="***REMOVED*** echo esc_url( __( 'http://wordpress.org/' ) ); ?>">***REMOVED*** _e( 'WordPress' ); ?></a></h1>

***REMOVED*** if ( get_option( 'db_version' ) == $wp_db_version || !is_blog_installed() ) : ?>

<h2>***REMOVED*** _e( 'No Update Required' ); ?></h2>
<p>***REMOVED*** _e( 'Your WordPress database is already up-to-date!' ); ?></p>
<p class="step"><a class="button button-large" href="***REMOVED*** echo get_option( 'home' ); ?>/">***REMOVED*** _e( 'Continue' ); ?></a></p>

***REMOVED*** elseif ( !$php_compat || !$mysql_compat ) :
	if ( !$mysql_compat && !$php_compat )
		printf( __('You cannot update because <a href="http://codex.wordpress.org/Version_%1$s">WordPress %1$s</a> requires PHP version %2$s or higher and MySQL version %3$s or higher. You are running PHP version %4$s and MySQL version %5$s.'), $wp_version, $required_php_version, $required_mysql_version, $php_version, $mysql_version );
	elseif ( !$php_compat )
		printf( __('You cannot update because <a href="http://codex.wordpress.org/Version_%1$s">WordPress %1$s</a> requires PHP version %2$s or higher. You are running version %3$s.'), $wp_version, $required_php_version, $php_version );
	elseif ( !$mysql_compat )
		printf( __('You cannot update because <a href="http://codex.wordpress.org/Version_%1$s">WordPress %1$s</a> requires MySQL version %2$s or higher. You are running version %3$s.'), $wp_version, $required_mysql_version, $mysql_version );
?>
***REMOVED*** else :
switch ( $step ) :
	case 0:
		$goback = wp_get_referer();
		$goback = esc_url_raw( $goback );
		$goback = urlencode( $goback );
?>
<h2>***REMOVED*** _e( 'Database Update Required' ); ?></h2>
<p>***REMOVED*** _e( 'WordPress has been updated! Before we send you on your way, we have to update your database to the newest version.' ); ?></p>
<p>***REMOVED*** _e( 'The update process may take a little while, so please be patient.' ); ?></p>
<p class="step"><a class="button button-large" href="upgrade.php?step=1&amp;backto=***REMOVED*** echo $goback; ?>">***REMOVED*** _e( 'Update WordPress Database' ); ?></a></p>
***REMOVED***
		break;
	case 1:
		wp_upgrade();

			$backto = !empty($_GET['backto']) ? wp_unslash( urldecode( $_GET['backto'] ) ) : __get_option( 'home' ) . '/';
			$backto = esc_url( $backto );
			$backto = wp_validate_redirect($backto, __get_option( 'home' ) . '/');
?>
<h2>***REMOVED*** _e( 'Update Complete' ); ?></h2>
	<p>***REMOVED*** _e( 'Your WordPress database has been successfully updated!' ); ?></p>
	<p class="step"><a class="button button-large" href="***REMOVED*** echo $backto; ?>">***REMOVED*** _e( 'Continue' ); ?></a></p>

<!--
<pre>
***REMOVED*** printf( __( '%s queries' ), $wpdb->num_queries ); ?>

***REMOVED*** printf( __( '%s seconds' ), timer_stop( 0 ) ); ?>
</pre>
-->

***REMOVED***
		break;
endswitch;
endif;
?>
</body>
</html>
