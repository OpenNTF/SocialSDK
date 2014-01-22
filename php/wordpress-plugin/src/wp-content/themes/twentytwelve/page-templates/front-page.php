***REMOVED***
/**
 * Template Name: Front Page Template
 *
 * Description: A page template that provides a key component of WordPress as a CMS
 * by meeting the need for a carefully crafted introductory page. The front page template
 * in Twenty Twelve consists of a page content area for adding text, images, video --
 * anything you'd like -- followed by front-page-only widgets in one or two columns.
 *
 * @package WordPress
 * @subpackage Twenty_Twelve
 * @since Twenty Twelve 1.0
 */

get_header(); ?>

	<div id="primary" class="site-content">
		<div id="content" role="main">

			***REMOVED*** while ( have_posts() ) : the_post(); ?>
				***REMOVED*** if ( has_post_thumbnail() ) : ?>
					<div class="entry-page-image">
						***REMOVED*** the_post_thumbnail(); ?>
					</div><!-- .entry-page-image -->
				***REMOVED*** endif; ?>

				***REMOVED*** get_template_part( 'content', 'page' ); ?>

			***REMOVED*** endwhile; // end of the loop. ?>

		</div><!-- #content -->
	</div><!-- #primary -->

***REMOVED*** get_sidebar( 'front' ); ?>
***REMOVED*** get_footer(); ?>