***REMOVED***
/**
 * The main template file
 *
 * This is the most generic template file in a WordPress theme
 * and one of the two required files for a theme (the other being style.css).
 * It is used to display a page when nothing more specific matches a query.
 * For example, it puts together the home page when no home.php file exists.
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
		***REMOVED*** if ( have_posts() ) : ?>

			***REMOVED*** /* Start the Loop */ ?>
			***REMOVED*** while ( have_posts() ) : the_post(); ?>
				***REMOVED*** get_template_part( 'content', get_post_format() ); ?>
			***REMOVED*** endwhile; ?>

			***REMOVED*** twentytwelve_content_nav( 'nav-below' ); ?>

		***REMOVED*** else : ?>

			<article id="post-0" class="post no-results not-found">

			***REMOVED*** if ( current_user_can( 'edit_posts' ) ) :
				// Show a different message to a logged-in user who can add posts.
			?>
				<header class="entry-header">
					<h1 class="entry-title">***REMOVED*** _e( 'No posts to display', 'twentytwelve' ); ?></h1>
				</header>

				<div class="entry-content">
					<p>***REMOVED*** printf( __( 'Ready to publish your first post? <a href="%s">Get started here</a>.', 'twentytwelve' ), admin_url( 'post-new.php' ) ); ?></p>
				</div><!-- .entry-content -->

			***REMOVED*** else :
				// Show the default message to everyone else.
			?>
				<header class="entry-header">
					<h1 class="entry-title">***REMOVED*** _e( 'Nothing Found', 'twentytwelve' ); ?></h1>
				</header>

				<div class="entry-content">
					<p>***REMOVED*** _e( 'Apologies, but no results were found. Perhaps searching will help find a related post.', 'twentytwelve' ); ?></p>
					***REMOVED*** get_search_form(); ?>
				</div><!-- .entry-content -->
			***REMOVED*** endif; // end current_user_can() check ?>

			</article><!-- #post-0 -->

		***REMOVED*** endif; // end have_posts() check ?>

		</div><!-- #content -->
	</div><!-- #primary -->

***REMOVED*** get_sidebar(); ?>
***REMOVED*** get_footer(); ?>