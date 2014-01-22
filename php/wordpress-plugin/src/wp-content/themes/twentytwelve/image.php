***REMOVED***
/**
 * The template for displaying image attachments
 *
 * @link http://codex.wordpress.org/Template_Hierarchy
 *
 * @package WordPress
 * @subpackage Twenty_Twelve
 * @since Twenty Twelve 1.0
 */

get_header(); ?>

	<div id="primary" class="site-content">
		<div id="content" role="main">

		***REMOVED*** while ( have_posts() ) : the_post(); ?>

				<article id="post-***REMOVED*** the_ID(); ?>" ***REMOVED*** post_class( 'image-attachment' ); ?>>
					<header class="entry-header">
						<h1 class="entry-title">***REMOVED*** the_title(); ?></h1>

						<footer class="entry-meta">
							***REMOVED***
								$metadata = wp_get_attachment_metadata();
								printf( __( '<span class="meta-prep meta-prep-entry-date">Published </span> <span class="entry-date"><time class="entry-date" datetime="%1$s">%2$s</time></span> at <a href="%3$s" title="Link to full-size image">%4$s &times; %5$s</a> in <a href="%6$s" title="Return to %7$s" rel="gallery">%8$s</a>.', 'twentytwelve' ),
									esc_attr( get_the_date( 'c' ) ),
									esc_html( get_the_date() ),
									esc_url( wp_get_attachment_url() ),
									$metadata['width'],
									$metadata['height'],
									esc_url( get_permalink( $post->post_parent ) ),
									esc_attr( strip_tags( get_the_title( $post->post_parent ) ) ),
									get_the_title( $post->post_parent )
								);
							?>
							***REMOVED*** edit_post_link( __( 'Edit', 'twentytwelve' ), '<span class="edit-link">', '</span>' ); ?>
						</footer><!-- .entry-meta -->

						<nav id="image-navigation" class="navigation" role="navigation">
							<span class="previous-image">***REMOVED*** previous_image_link( false, __( '&larr; Previous', 'twentytwelve' ) ); ?></span>
							<span class="next-image">***REMOVED*** next_image_link( false, __( 'Next &rarr;', 'twentytwelve' ) ); ?></span>
						</nav><!-- #image-navigation -->
					</header><!-- .entry-header -->

					<div class="entry-content">

						<div class="entry-attachment">
							<div class="attachment">
***REMOVED***
/*
 * Grab the IDs of all the image attachments in a gallery so we can get the URL of the next adjacent image in a gallery,
 * or the first image (if we're looking at the last image in a gallery), or, in a gallery of one, just the link to that image file
 */
$attachments = array_values( get_children( array( 'post_parent' => $post->post_parent, 'post_status' => 'inherit', 'post_type' => 'attachment', 'post_mime_type' => 'image', 'order' => 'ASC', 'orderby' => 'menu_order ID' ) ) );
foreach ( $attachments as $k => $attachment ) :
	if ( $attachment->ID == $post->ID )
		break;
endforeach;

$k++;
// If there is more than 1 attachment in a gallery
if ( count( $attachments ) > 1 ) :
	if ( isset( $attachments[ $k ] ) ) :
		// get the URL of the next image attachment
		$next_attachment_url = get_attachment_link( $attachments[ $k ]->ID );
	else :
		// or get the URL of the first image attachment
		$next_attachment_url = get_attachment_link( $attachments[ 0 ]->ID );
	endif;
else :
	// or, if there's only 1 image, get the URL of the image
	$next_attachment_url = wp_get_attachment_url();
endif;
?>
								<a href="***REMOVED*** echo esc_url( $next_attachment_url ); ?>" title="***REMOVED*** the_title_attribute(); ?>" rel="attachment">***REMOVED***
								/**
 								 * Filter the image attachment size to use.
								 *
								 * @since Twenty Twelve 1.0
								 *
								 * @param array $size {
								 *     @type int The attachment height in pixels.
								 *     @type int The attachment width in pixels.
								 * }
								 */
								$attachment_size = apply_filters( 'twentytwelve_attachment_size', array( 960, 960 ) );
								echo wp_get_attachment_image( $post->ID, $attachment_size );
								?></a>

								***REMOVED*** if ( ! empty( $post->post_excerpt ) ) : ?>
								<div class="entry-caption">
									***REMOVED*** the_excerpt(); ?>
								</div>
								***REMOVED*** endif; ?>
							</div><!-- .attachment -->

						</div><!-- .entry-attachment -->

						<div class="entry-description">
							***REMOVED*** the_content(); ?>
							***REMOVED*** wp_link_pages( array( 'before' => '<div class="page-links">' . __( 'Pages:', 'twentytwelve' ), 'after' => '</div>' ) ); ?>
						</div><!-- .entry-description -->

					</div><!-- .entry-content -->

				</article><!-- #post -->

				***REMOVED*** comments_template(); ?>

			***REMOVED*** endwhile; // end of the loop. ?>

		</div><!-- #content -->
	</div><!-- #primary -->

***REMOVED*** get_footer(); ?>