***REMOVED***
/**
 * The template for displaying Category pages
 *
 * Used to display archive-type pages for posts in a category.
 *
 * @link http://codex.wordpress.org/Template_Hierarchy
 *
 * @package WordPress
 * @subpackage Twenty_Twelve
 * @since Twenty Twelve 1.0
 */

get_header(); ?>

	<section id="primary" class="site-content">
		<div id="content" role="main">

		***REMOVED*** if ( have_posts() ) : ?>
			<header class="archive-header">
				<h1 class="archive-title">***REMOVED*** printf( __( 'Category Archives: %s', 'twentytwelve' ), '<span>' . single_cat_title( '', false ) . '</span>' ); ?></h1>

			***REMOVED*** if ( category_description() ) : // Show an optional category description ?>
				<div class="archive-meta">***REMOVED*** echo category_description(); ?></div>
			***REMOVED*** endif; ?>
			</header><!-- .archive-header -->

			***REMOVED***
			/* Start the Loop */
			while ( have_posts() ) : the_post();

				/* Include the post format-specific template for the content. If you want to
				 * this in a child theme then include a file called called content-___.php
				 * (where ___ is the post format) and that will be used instead.
				 */
				get_template_part( 'content', get_post_format() );

			endwhile;

			twentytwelve_content_nav( 'nav-below' );
			?>

		***REMOVED*** else : ?>
			***REMOVED*** get_template_part( 'content', 'none' ); ?>
		***REMOVED*** endif; ?>

		</div><!-- #content -->
	</section><!-- #primary -->

***REMOVED*** get_sidebar(); ?>
***REMOVED*** get_footer(); ?>