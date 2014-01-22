***REMOVED***
/**
 * The template for displaying all single posts
 *
 * @package WordPress
 * @subpackage Twenty_Thirteen
 * @since Twenty Thirteen 1.0
 */

get_header(); ?>

	<div id="primary" class="content-area">
		<div id="content" class="site-content" role="main">

			***REMOVED*** /* The loop */ ?>
			***REMOVED*** while ( have_posts() ) : the_post(); ?>

				***REMOVED*** get_template_part( 'content', get_post_format() ); ?>
				***REMOVED*** twentythirteen_post_nav(); ?>
				***REMOVED*** comments_template(); ?>

			***REMOVED*** endwhile; ?>

		</div><!-- #content -->
	</div><!-- #primary -->

***REMOVED*** get_sidebar(); ?>
***REMOVED*** get_footer(); ?>