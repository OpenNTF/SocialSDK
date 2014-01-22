***REMOVED***
/**
 * The default template for displaying content
 *
 * Used for both single and index/archive/search.
 *
 * @package WordPress
 * @subpackage Twenty_Thirteen
 * @since Twenty Thirteen 1.0
 */
?>

<article id="post-***REMOVED*** the_ID(); ?>" ***REMOVED*** post_class(); ?>>
	<header class="entry-header">
		***REMOVED*** if ( has_post_thumbnail() && ! post_password_required() ) : ?>
		<div class="entry-thumbnail">
			***REMOVED*** the_post_thumbnail(); ?>
		</div>
		***REMOVED*** endif; ?>

		***REMOVED*** if ( is_single() ) : ?>
		<h1 class="entry-title">***REMOVED*** the_title(); ?></h1>
		***REMOVED*** else : ?>
		<h1 class="entry-title">
			<a href="***REMOVED*** the_permalink(); ?>" rel="bookmark">***REMOVED*** the_title(); ?></a>
		</h1>
		***REMOVED*** endif; // is_single() ?>

		<div class="entry-meta">
			***REMOVED*** twentythirteen_entry_meta(); ?>
			***REMOVED*** edit_post_link( __( 'Edit', 'twentythirteen' ), '<span class="edit-link">', '</span>' ); ?>
		</div><!-- .entry-meta -->
	</header><!-- .entry-header -->

	***REMOVED*** if ( is_search() ) : // Only display Excerpts for Search ?>
	<div class="entry-summary">
		***REMOVED*** the_excerpt(); ?>
	</div><!-- .entry-summary -->
	***REMOVED*** else : ?>
	<div class="entry-content">
		***REMOVED*** the_content( __( 'Continue reading <span class="meta-nav">&rarr;</span>', 'twentythirteen' ) ); ?>
		***REMOVED*** wp_link_pages( array( 'before' => '<div class="page-links"><span class="page-links-title">' . __( 'Pages:', 'twentythirteen' ) . '</span>', 'after' => '</div>', 'link_before' => '<span>', 'link_after' => '</span>' ) ); ?>
	</div><!-- .entry-content -->
	***REMOVED*** endif; ?>

	<footer class="entry-meta">
		***REMOVED*** if ( comments_open() && ! is_single() ) : ?>
			<div class="comments-link">
				***REMOVED*** comments_popup_link( '<span class="leave-reply">' . __( 'Leave a comment', 'twentythirteen' ) . '</span>', __( 'One comment so far', 'twentythirteen' ), __( 'View all % comments', 'twentythirteen' ) ); ?>
			</div><!-- .comments-link -->
		***REMOVED*** endif; // comments_open() ?>

		***REMOVED*** if ( is_single() && get_the_author_meta( 'description' ) && is_multi_author() ) : ?>
			***REMOVED*** get_template_part( 'author-bio' ); ?>
		***REMOVED*** endif; ?>
	</footer><!-- .entry-meta -->
</article><!-- #post -->
