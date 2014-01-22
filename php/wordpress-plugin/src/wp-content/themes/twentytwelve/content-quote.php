***REMOVED***
/**
 * The template for displaying posts in the Quote post format
 *
 * @package WordPress
 * @subpackage Twenty_Twelve
 * @since Twenty Twelve 1.0
 */
?>

	<article id="post-***REMOVED*** the_ID(); ?>" ***REMOVED*** post_class(); ?>>
		<div class="entry-content">
			***REMOVED*** the_content( __( 'Continue reading <span class="meta-nav">&rarr;</span>', 'twentytwelve' ) ); ?>
		</div><!-- .entry-content -->

		<footer class="entry-meta">
			<a href="***REMOVED*** the_permalink(); ?>" title="***REMOVED*** echo esc_attr( sprintf( __( 'Permalink to %s', 'twentytwelve' ), the_title_attribute( 'echo=0' ) ) ); ?>" rel="bookmark">***REMOVED*** echo get_the_date(); ?></a>
			***REMOVED*** if ( comments_open() ) : ?>
			<div class="comments-link">
				***REMOVED*** comments_popup_link( '<span class="leave-reply">' . __( 'Leave a reply', 'twentytwelve' ) . '</span>', __( '1 Reply', 'twentytwelve' ), __( '% Replies', 'twentytwelve' ) ); ?>
			</div><!-- .comments-link -->
			***REMOVED*** endif; // comments_open() ?>
			***REMOVED*** edit_post_link( __( 'Edit', 'twentytwelve' ), '<span class="edit-link">', '</span>' ); ?>
		</footer><!-- .entry-meta -->
	</article><!-- #post -->
