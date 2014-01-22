***REMOVED***
/**
 * The template for displaying Category pages
 *
 * @link http://codex.wordpress.org/Template_Hierarchy
 *
 * @package WordPress
 * @subpackage Twenty_Thirteen
 * @since Twenty Thirteen 1.0
 */

get_header(); ?>

	<div id="primary" class="content-area">
		<div id="content" class="site-content" role="main">

		***REMOVED*** if ( have_posts() ) : ?>
			<header class="archive-header">
				<h1 class="archive-title">***REMOVED*** printf( __( 'Category Archives: %s', 'twentythirteen' ), single_cat_title( '', false ) ); ?></h1>

				***REMOVED*** if ( category_description() ) : // Show an optional category description ?>
				<div class="archive-meta">***REMOVED*** echo category_description(); ?></div>
				***REMOVED*** endif; ?>
			</header><!-- .archive-header -->

			***REMOVED*** /* The loop */ ?>
			***REMOVED*** while ( have_posts() ) : the_post(); ?>
				***REMOVED*** get_template_part( 'content', get_post_format() ); ?>
			***REMOVED*** endwhile; ?>

			***REMOVED*** twentythirteen_paging_nav(); ?>

		***REMOVED*** else : ?>
			***REMOVED*** get_template_part( 'content', 'none' ); ?>
		***REMOVED*** endif; ?>

		</div><!-- #content -->
	</div><!-- #primary -->

***REMOVED*** get_sidebar(); ?>
***REMOVED*** get_footer(); ?>