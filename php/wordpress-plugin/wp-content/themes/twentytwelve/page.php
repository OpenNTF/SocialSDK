***REMOVED***
/**
 * The template for displaying all pages
 *
 * This is the template that displays all pages by default.
 * Please note that this is the WordPress construct of pages
 * and that other 'pages' on your WordPress site will use a
 * different template.
 *
 * @package WordPress
 * @subpackage Twenty_Twelve
 * @since Twenty Twelve 1.0
 */

get_header(); ?>

	<div id="primary" class="site-content">
		<div id="content" role="main">

			***REMOVED*** while ( have_posts() ) : the_post(); ?>
				***REMOVED*** get_template_part( 'content', 'page' ); ?>
				***REMOVED*** comments_template( '', true ); ?>
			***REMOVED*** endwhile; // end of the loop. ?>

		</div><!-- #content -->
	</div><!-- #primary -->

***REMOVED*** get_sidebar(); ?>
***REMOVED*** get_footer(); ?>