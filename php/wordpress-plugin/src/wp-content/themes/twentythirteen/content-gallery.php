***REMOVED***
/**
 * The template for displaying posts in the Gallery post format
 *
 * @package WordPress
 * @subpackage Twenty_Thirteen
 * @since Twenty Thirteen 1.0
 */
?>

<article id="post-***REMOVED*** the_ID(); ?>" ***REMOVED*** post_class(); ?>>
	<header class="entry-header">
		***REMOVED*** if ( is_single() ) : ?>
		<h1 class="entry-title">***REMOVED*** the_title(); ?></h1>
		***REMOVED*** else : ?>
		<h1 class="entry-title">
			<a href="***REMOVED*** the_permalink(); ?>" rel="bookmark">***REMOVED*** the_title(); ?></a>
		</h1>
		***REMOVED*** endif; // is_single() ?>
	</header><!-- .entry-header -->

	<div class="entry-content">
		***REMOVED*** if ( is_single() || ! get_post_gallery() ) : ?>
			***REMOVED*** the_content( __( 'Continue reading <span class="meta-nav">&rarr;</span>', 'twentythirteen' ) ); ?>
			***REMOVED*** wp_link_pages( array( 'before' => '<div class="page-links"><span class="page-links-title">' . __( 'Pages:', 'twentythirteen' ) . '</span>', 'after' => '</div>', 'link_before' => '<span>', 'link_after' => '</span>' ) ); ?>
		***REMOVED*** else : ?>
			***REMOVED*** echo get_post_gallery(); ?>
		***REMOVED*** endif; // is_single() ?>
	</div><!-- .entry-content -->

	<footer class="entry-meta">
		***REMOVED*** twentythirteen_entry_meta(); ?>

		***REMOVED*** if ( comments_open() && ! is_single() ) : ?>
		<span class="comments-link">
			***REMOVED*** comments_popup_link( '<span class="leave-reply">' . __( 'Leave a comment', 'twentythirteen' ) . '</span>', __( 'One comment so far', 'twentythirteen' ), __( 'View all % comments', 'twentythirteen' ) ); ?>
		</span><!-- .comments-link -->
		***REMOVED*** endif; // comments_open() ?>
		***REMOVED*** edit_post_link( __( 'Edit', 'twentythirteen' ), '<span class="edit-link">', '</span>' ); ?>

		***REMOVED*** if ( is_single() && get_the_author_meta( 'description' ) && is_multi_author() ) : ?>
			***REMOVED*** get_template_part( 'author-bio' ); ?>
		***REMOVED*** endif; ?>
	</footer><!-- .entry-meta -->
</article><!-- #post -->
