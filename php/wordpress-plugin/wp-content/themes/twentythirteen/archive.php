***REMOVED***
/**
 * The template for displaying Archive pages
 *
 * Used to display archive-type pages if nothing more specific matches a query.
 * For example, puts together date-based pages if no date.php file exists.
 *
 * If you'd like to further customize these archive views, you may create a
 * new template file for each specific one. For example, Twenty Thirteen
 * already has tag.php for Tag archives, category.php for Category archives,
 * and author.php for Author archives.
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
				<h1 class="archive-title">***REMOVED***
					if ( is_day() ) :
						printf( __( 'Daily Archives: %s', 'twentythirteen' ), get_the_date() );
					elseif ( is_month() ) :
						printf( __( 'Monthly Archives: %s', 'twentythirteen' ), get_the_date( _x( 'F Y', 'monthly archives date format', 'twentythirteen' ) ) );
					elseif ( is_year() ) :
						printf( __( 'Yearly Archives: %s', 'twentythirteen' ), get_the_date( _x( 'Y', 'yearly archives date format', 'twentythirteen' ) ) );
					else :
						_e( 'Archives', 'twentythirteen' );
					endif;
				?></h1>
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