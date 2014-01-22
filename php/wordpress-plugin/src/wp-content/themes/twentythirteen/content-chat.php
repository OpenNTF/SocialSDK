***REMOVED***
/**
 * The template for displaying posts in the Chat post format
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
		***REMOVED*** the_content(); ?>
		***REMOVED*** wp_link_pages( array( 'before' => '<div class="page-links"><span class="page-links-title">' . __( 'Pages:', 'twentythirteen' ) . '</span>', 'after' => '</div>', 'link_before' => '<span>', 'link_after' => '</span>' ) ); ?>
	</div><!-- .entry-content -->

	<footer class="entry-meta">
		***REMOVED*** twentythirteen_entry_meta(); ?>
		***REMOVED*** edit_post_link( __( 'Edit', 'twentythirteen' ), '<span class="edit-link">', '</span>' ); ?>
	</footer><!-- .entry-meta -->
</article><!-- #post -->
