***REMOVED***
/**
 * The template for displaying posts in the Status post format
 *
 * @package WordPress
 * @subpackage Twenty_Thirteen
 * @since Twenty Thirteen 1.0
 */
?>

<article id="post-***REMOVED*** the_ID(); ?>" ***REMOVED*** post_class(); ?>>
	<div class="entry-content">
		***REMOVED*** the_content( __( 'Continue reading <span class="meta-nav">&rarr;</span>', 'twentythirteen' ) ); ?>
		***REMOVED*** wp_link_pages( array( 'before' => '<div class="page-links"><span class="page-links-title">' . __( 'Pages:', 'twentythirteen' ) . '</span>', 'after' => '</div>', 'link_before' => '<span>', 'link_after' => '</span>' ) ); ?>
	</div><!-- .entry-content -->

	<footer class="entry-meta">
		***REMOVED*** twentythirteen_entry_meta(); ?>
		***REMOVED*** edit_post_link( __( 'Edit', 'twentythirteen' ), '<span class="edit-link">', '</span>' ); ?>

		***REMOVED*** if ( is_single() && get_the_author_meta( 'description' ) && is_multi_author() ) : ?>
			***REMOVED*** get_template_part( 'author-bio' ); ?>
		***REMOVED*** endif; ?>
	</footer><!-- .entry-meta -->
</article><!-- #post -->
