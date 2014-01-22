***REMOVED***
/**
 * The template for displaying Search Results pages
 *
 * @package WordPress
 * @subpackage Twenty_Twelve
 * @since Twenty Twelve 1.0
 */

get_header(); ?>

	<section id="primary" class="site-content">
		<div id="content" role="main">

		***REMOVED*** if ( have_posts() ) : ?>

			<header class="page-header">
				<h1 class="page-title">***REMOVED*** printf( __( 'Search Results for: %s', 'twentytwelve' ), '<span>' . get_search_query() . '</span>' ); ?></h1>
			</header>

			***REMOVED*** twentytwelve_content_nav( 'nav-above' ); ?>

			***REMOVED*** /* Start the Loop */ ?>
			***REMOVED*** while ( have_posts() ) : the_post(); ?>
				***REMOVED*** get_template_part( 'content', get_post_format() ); ?>
			***REMOVED*** endwhile; ?>

			***REMOVED*** twentytwelve_content_nav( 'nav-below' ); ?>

		***REMOVED*** else : ?>

			<article id="post-0" class="post no-results not-found">
				<header class="entry-header">
					<h1 class="entry-title">***REMOVED*** _e( 'Nothing Found', 'twentytwelve' ); ?></h1>
				</header>

				<div class="entry-content">
					<p>***REMOVED*** _e( 'Sorry, but nothing matched your search criteria. Please try again with some different keywords.', 'twentytwelve' ); ?></p>
					***REMOVED*** get_search_form(); ?>
				</div><!-- .entry-content -->
			</article><!-- #post-0 -->

		***REMOVED*** endif; ?>

		</div><!-- #content -->
	</section><!-- #primary -->

***REMOVED*** get_sidebar(); ?>
***REMOVED*** get_footer(); ?>