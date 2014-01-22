***REMOVED***
/**
 * The Template for displaying all single posts
 *
 * @package WordPress
 * @subpackage Twenty_Twelve
 * @since Twenty Twelve 1.0
 */

get_header(); ?>

	<div id="primary" class="site-content">
		<div id="content" role="main">

			***REMOVED*** while ( have_posts() ) : the_post(); ?>

				***REMOVED*** get_template_part( 'content', get_post_format() ); ?>

				<nav class="nav-single">
					<h3 class="assistive-text">***REMOVED*** _e( 'Post navigation', 'twentytwelve' ); ?></h3>
					<span class="nav-previous">***REMOVED*** previous_post_link( '%link', '<span class="meta-nav">' . _x( '&larr;', 'Previous post link', 'twentytwelve' ) . '</span> %title' ); ?></span>
					<span class="nav-next">***REMOVED*** next_post_link( '%link', '%title <span class="meta-nav">' . _x( '&rarr;', 'Next post link', 'twentytwelve' ) . '</span>' ); ?></span>
				</nav><!-- .nav-single -->

				***REMOVED*** comments_template( '', true ); ?>

			***REMOVED*** endwhile; // end of the loop. ?>

		</div><!-- #content -->
	</div><!-- #primary -->

***REMOVED*** get_sidebar(); ?>
***REMOVED*** get_footer(); ?>