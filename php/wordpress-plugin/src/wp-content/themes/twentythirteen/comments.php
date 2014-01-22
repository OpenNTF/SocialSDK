***REMOVED***
/**
 * The template for displaying Comments
 *
 * The area of the page that contains comments and the comment form.
 *
 * @package WordPress
 * @subpackage Twenty_Thirteen
 * @since Twenty Thirteen 1.0
 */

/*
 * If the current post is protected by a password and the visitor has not yet
 * entered the password we will return early without loading the comments.
 */
if ( post_password_required() )
	return;
?>

<div id="comments" class="comments-area">

	***REMOVED*** if ( have_comments() ) : ?>
		<h2 class="comments-title">
			***REMOVED***
				printf( _nx( 'One thought on &ldquo;%2$s&rdquo;', '%1$s thoughts on &ldquo;%2$s&rdquo;', get_comments_number(), 'comments title', 'twentythirteen' ),
					number_format_i18n( get_comments_number() ), '<span>' . get_the_title() . '</span>' );
			?>
		</h2>

		<ol class="comment-list">
			***REMOVED***
				wp_list_comments( array(
					'style'       => 'ol',
					'short_ping'  => true,
					'avatar_size' => 74,
				) );
			?>
		</ol><!-- .comment-list -->

		***REMOVED***
			// Are there comments to navigate through?
			if ( get_comment_pages_count() > 1 && get_option( 'page_comments' ) ) :
		?>
		<nav class="navigation comment-navigation" role="navigation">
			<h1 class="screen-reader-text section-heading">***REMOVED*** _e( 'Comment navigation', 'twentythirteen' ); ?></h1>
			<div class="nav-previous">***REMOVED*** previous_comments_link( __( '&larr; Older Comments', 'twentythirteen' ) ); ?></div>
			<div class="nav-next">***REMOVED*** next_comments_link( __( 'Newer Comments &rarr;', 'twentythirteen' ) ); ?></div>
		</nav><!-- .comment-navigation -->
		***REMOVED*** endif; // Check for comment navigation ?>

		***REMOVED*** if ( ! comments_open() && get_comments_number() ) : ?>
		<p class="no-comments">***REMOVED*** _e( 'Comments are closed.' , 'twentythirteen' ); ?></p>
		***REMOVED*** endif; ?>

	***REMOVED*** endif; // have_comments() ?>

	***REMOVED*** comment_form(); ?>

</div><!-- #comments -->