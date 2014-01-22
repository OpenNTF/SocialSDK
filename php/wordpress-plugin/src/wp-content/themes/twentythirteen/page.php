***REMOVED***
/**
 * The template for displaying all pages
 *
 * This is the template that displays all pages by default.
 * Please note that this is the WordPress construct of pages and that other
 * 'pages' on your WordPress site will use a different template.
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

				<article id="post-***REMOVED*** the_ID(); ?>" ***REMOVED*** post_class(); ?>>
					<header class="entry-header">
						***REMOVED*** if ( has_post_thumbnail() && ! post_password_required() ) : ?>
						<div class="entry-thumbnail">
							***REMOVED*** the_post_thumbnail(); ?>
						</div>
						***REMOVED*** endif; ?>

						<h1 class="entry-title">***REMOVED*** the_title(); ?></h1>
					</header><!-- .entry-header -->

					<div class="entry-content">
						***REMOVED*** the_content(); ?>
						***REMOVED*** wp_link_pages( array( 'before' => '<div class="page-links"><span class="page-links-title">' . __( 'Pages:', 'twentythirteen' ) . '</span>', 'after' => '</div>', 'link_before' => '<span>', 'link_after' => '</span>' ) ); ?>
					</div><!-- .entry-content -->

					<footer class="entry-meta">
						***REMOVED*** edit_post_link( __( 'Edit', 'twentythirteen' ), '<span class="edit-link">', '</span>' ); ?>
					</footer><!-- .entry-meta -->
				</article><!-- #post -->

				***REMOVED*** comments_template(); ?>
			***REMOVED*** endwhile; ?>

		</div><!-- #content -->
	</div><!-- #primary -->

***REMOVED*** get_sidebar(); ?>
***REMOVED*** get_footer(); ?>