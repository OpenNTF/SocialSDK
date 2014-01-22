***REMOVED***
/**
 * The template used for displaying page content in page.php
 *
 * @package WordPress
 * @subpackage Twenty_Twelve
 * @since Twenty Twelve 1.0
 */
?>

	<article id="post-***REMOVED*** the_ID(); ?>" ***REMOVED*** post_class(); ?>>
		<header class="entry-header">
			***REMOVED*** if ( ! is_page_template( 'page-templates/front-page.php' ) ) : ?>
			***REMOVED*** the_post_thumbnail(); ?>
			***REMOVED*** endif; ?>
			<h1 class="entry-title">***REMOVED*** the_title(); ?></h1>
		</header>

		<div class="entry-content">
			***REMOVED*** the_content(); ?>
			***REMOVED*** wp_link_pages( array( 'before' => '<div class="page-links">' . __( 'Pages:', 'twentytwelve' ), 'after' => '</div>' ) ); ?>
		</div><!-- .entry-content -->
		<footer class="entry-meta">
			***REMOVED*** edit_post_link( __( 'Edit', 'twentytwelve' ), '<span class="edit-link">', '</span>' ); ?>
		</footer><!-- .entry-meta -->
	</article><!-- #post -->
