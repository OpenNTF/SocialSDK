***REMOVED***
/**
 * The default template for displaying content
 *
 * Used for both single and index/archive/search.
 *
 * @package WordPress
 * @subpackage Twenty_Twelve
 * @since Twenty Twelve 1.0
 */
?>

	<article id="post-***REMOVED*** the_ID(); ?>" ***REMOVED*** post_class(); ?>>
		***REMOVED*** if ( is_sticky() && is_home() && ! is_paged() ) : ?>
		<div class="featured-post">
			***REMOVED*** _e( 'Featured post', 'twentytwelve' ); ?>
		</div>
		***REMOVED*** endif; ?>
		<header class="entry-header">
			***REMOVED*** the_post_thumbnail(); ?>
			***REMOVED*** if ( is_single() ) : ?>
			<h1 class="entry-title">***REMOVED*** the_title(); ?></h1>
			***REMOVED*** else : ?>
			<h1 class="entry-title">
				<a href="***REMOVED*** the_permalink(); ?>" rel="bookmark">***REMOVED*** the_title(); ?></a>
			</h1>
			***REMOVED*** endif; // is_single() ?>
			***REMOVED*** if ( comments_open() ) : ?>
				<div class="comments-link">
					***REMOVED*** comments_popup_link( '<span class="leave-reply">' . __( 'Leave a reply', 'twentytwelve' ) . '</span>', __( '1 Reply', 'twentytwelve' ), __( '% Replies', 'twentytwelve' ) ); ?>
				</div><!-- .comments-link -->
			***REMOVED*** endif; // comments_open() ?>
		</header><!-- .entry-header -->

		***REMOVED*** if ( is_search() ) : // Only display Excerpts for Search ?>
		<div class="entry-summary">
			***REMOVED*** the_excerpt(); ?>
		</div><!-- .entry-summary -->
		***REMOVED*** else : ?>
		<div class="entry-content">
			***REMOVED*** the_content( __( 'Continue reading <span class="meta-nav">&rarr;</span>', 'twentytwelve' ) ); ?>
			***REMOVED*** wp_link_pages( array( 'before' => '<div class="page-links">' . __( 'Pages:', 'twentytwelve' ), 'after' => '</div>' ) ); ?>
		</div><!-- .entry-content -->
		***REMOVED*** endif; ?>

		<footer class="entry-meta">
			***REMOVED*** twentytwelve_entry_meta(); ?>
			***REMOVED*** edit_post_link( __( 'Edit', 'twentytwelve' ), '<span class="edit-link">', '</span>' ); ?>
			***REMOVED*** if ( is_singular() && get_the_author_meta( 'description' ) && is_multi_author() ) : // If a user has filled out their description and this is a multi-author blog, show a bio on their entries. ?>
				<div class="author-info">
					<div class="author-avatar">
						***REMOVED***
						/** This filter is documented in author.php */
						$author_bio_avatar_size = apply_filters( 'twentytwelve_author_bio_avatar_size', 68 );
						echo get_avatar( get_the_author_meta( 'user_email' ), $author_bio_avatar_size );
						?>
					</div><!-- .author-avatar -->
					<div class="author-description">
						<h2>***REMOVED*** printf( __( 'About %s', 'twentytwelve' ), get_the_author() ); ?></h2>
						<p>***REMOVED*** the_author_meta( 'description' ); ?></p>
						<div class="author-link">
							<a href="***REMOVED*** echo esc_url( get_author_posts_url( get_the_author_meta( 'ID' ) ) ); ?>" rel="author">
								***REMOVED*** printf( __( 'View all posts by %s <span class="meta-nav">&rarr;</span>', 'twentytwelve' ), get_the_author() ); ?>
							</a>
						</div><!-- .author-link	-->
					</div><!-- .author-description -->
				</div><!-- .author-info -->
			***REMOVED*** endif; ?>
		</footer><!-- .entry-meta -->
	</article><!-- #post -->
