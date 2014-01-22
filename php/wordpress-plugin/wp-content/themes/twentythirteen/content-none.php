***REMOVED***
/**
 * The template for displaying a "No posts found" message
 *
 * @package WordPress
 * @subpackage Twenty_Thirteen
 * @since Twenty Thirteen 1.0
 */
?>

<header class="page-header">
	<h1 class="page-title">***REMOVED*** _e( 'Nothing Found', 'twentythirteen' ); ?></h1>
</header>

<div class="page-content">
	***REMOVED*** if ( is_home() && current_user_can( 'publish_posts' ) ) : ?>

	<p>***REMOVED*** printf( __( 'Ready to publish your first post? <a href="%1$s">Get started here</a>.', 'twentythirteen' ), admin_url( 'post-new.php' ) ); ?></p>

	***REMOVED*** elseif ( is_search() ) : ?>

	<p>***REMOVED*** _e( 'Sorry, but nothing matched your search terms. Please try again with different keywords.', 'twentythirteen' ); ?></p>
	***REMOVED*** get_search_form(); ?>

	***REMOVED*** else : ?>

	<p>***REMOVED*** _e( 'It seems we can&rsquo;t find what you&rsquo;re looking for. Perhaps searching can help.', 'twentythirteen' ); ?></p>
	***REMOVED*** get_search_form(); ?>

	***REMOVED*** endif; ?>
</div><!-- .page-content -->
