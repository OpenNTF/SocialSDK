***REMOVED***
/**
 * The template for displaying posts in the Status post format
 *
 * @package WordPress
 * @subpackage Twenty_Twelve
 * @since Twenty Twelve 1.0
 */
?>

	<article id="post-***REMOVED*** the_ID(); ?>" ***REMOVED*** post_class(); ?>>
		<div class="entry-header">
			<header>
				<h1>***REMOVED*** the_author(); ?></h1>
				<h2><a href="***REMOVED*** the_permalink(); ?>" title="***REMOVED*** echo esc_attr( sprintf( __( 'Permalink to %s', 'twentytwelve' ), the_title_attribute( 'echo=0' ) ) ); ?>" rel="bookmark">***REMOVED*** echo get_the_date(); ?></a></h2>
			</header>
			***REMOVED***
			/**
			 * Filter the status avatar size.
			 *
			 * @since Twenty Twelve 1.0
			 *
			 * @param int $size The height and width of the avatar in pixels.
			 */
			$status_avatar = apply_filters( 'twentytwelve_status_avatar', 48 );
			echo get_avatar( get_the_author_meta( 'ID' ), $status_avatar );
			?>
		</div><!-- .entry-header -->

		<div class="entry-content">
			***REMOVED*** the_content( __( 'Continue reading <span class="meta-nav">&rarr;</span>', 'twentytwelve' ) ); ?>
		</div><!-- .entry-content -->

		<footer class="entry-meta">
			***REMOVED*** if ( comments_open() ) : ?>
			<div class="comments-link">
				***REMOVED*** comments_popup_link( '<span class="leave-reply">' . __( 'Leave a reply', 'twentytwelve' ) . '</span>', __( '1 Reply', 'twentytwelve' ), __( '% Replies', 'twentytwelve' ) ); ?>
			</div><!-- .comments-link -->
			***REMOVED*** endif; // comments_open() ?>
			***REMOVED*** edit_post_link( __( 'Edit', 'twentytwelve' ), '<span class="edit-link">', '</span>' ); ?>
		</footer><!-- .entry-meta -->
	</article><!-- #post -->
