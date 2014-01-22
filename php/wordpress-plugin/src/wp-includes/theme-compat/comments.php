***REMOVED***
/**
 * @package WordPress
 * @subpackage Theme_Compat
 * @deprecated 3.0
 *
 * This file is here for Backwards compatibility with old themes and will be removed in a future version
 *
 */
_deprecated_file( sprintf( __( 'Theme without %1$s' ), basename(__FILE__) ), '3.0', null, sprintf( __('Please include a %1$s template in your theme.'), basename(__FILE__) ) );

// Do not delete these lines
	if (!empty($_SERVER['SCRIPT_FILENAME']) && 'comments.php' == basename($_SERVER['SCRIPT_FILENAME']))
		die ('Please do not load this page directly. Thanks!');

	if ( post_password_required() ) { ?>
		<p class="nocomments">***REMOVED*** _e('This post is password protected. Enter the password to view comments.'); ?></p>
	***REMOVED***
		return;
	}
?>

<!-- You can start editing here. -->

***REMOVED*** if ( have_comments() ) : ?>
	<h3 id="comments">***REMOVED***	printf( _n( 'One Response to %2$s', '%1$s Responses to %2$s', get_comments_number() ),
									number_format_i18n( get_comments_number() ), '&#8220;' . get_the_title() . '&#8221;' ); ?></h3>

	<div class="navigation">
		<div class="alignleft">***REMOVED*** previous_comments_link() ?></div>
		<div class="alignright">***REMOVED*** next_comments_link() ?></div>
	</div>

	<ol class="commentlist">
	***REMOVED*** wp_list_comments();?>
	</ol>

	<div class="navigation">
		<div class="alignleft">***REMOVED*** previous_comments_link() ?></div>
		<div class="alignright">***REMOVED*** next_comments_link() ?></div>
	</div>
 ***REMOVED*** else : // this is displayed if there are no comments so far ?>

	***REMOVED*** if ( comments_open() ) : ?>
		<!-- If comments are open, but there are no comments. -->

	 ***REMOVED*** else : // comments are closed ?>
		<!-- If comments are closed. -->
		<p class="nocomments">***REMOVED*** _e('Comments are closed.'); ?></p>

	***REMOVED*** endif; ?>
***REMOVED*** endif; ?>

***REMOVED*** if ( comments_open() ) : ?>

<div id="respond">

<h3>***REMOVED*** comment_form_title( __('Leave a Reply'), __('Leave a Reply to %s' ) ); ?></h3>

<div id="cancel-comment-reply">
	<small>***REMOVED*** cancel_comment_reply_link() ?></small>
</div>

***REMOVED*** if ( get_option('comment_registration') && !is_user_logged_in() ) : ?>
<p>***REMOVED*** printf(__('You must be <a href="%s">logged in</a> to post a comment.'), wp_login_url( get_permalink() )); ?></p>
***REMOVED*** else : ?>

<form action="***REMOVED*** echo site_url(); ?>/wp-comments-post.php" method="post" id="commentform">

***REMOVED*** if ( is_user_logged_in() ) : ?>

<p>***REMOVED*** printf(__('Logged in as <a href="%1$s">%2$s</a>.'), get_edit_user_link(), $user_identity); ?> <a href="***REMOVED*** echo wp_logout_url(get_permalink()); ?>" title="***REMOVED*** esc_attr_e('Log out of this account'); ?>">***REMOVED*** _e('Log out &raquo;'); ?></a></p>

***REMOVED*** else : ?>

<p><input type="text" name="author" id="author" value="***REMOVED*** echo esc_attr($comment_author); ?>" size="22" tabindex="1" ***REMOVED*** if ($req) echo "aria-required='true'"; ?> />
<label for="author"><small>***REMOVED*** _e('Name'); ?> ***REMOVED*** if ($req) _e('(required)'); ?></small></label></p>

<p><input type="text" name="email" id="email" value="***REMOVED*** echo esc_attr($comment_author_email); ?>" size="22" tabindex="2" ***REMOVED*** if ($req) echo "aria-required='true'"; ?> />
<label for="email"><small>***REMOVED*** _e('Mail (will not be published)'); ?> ***REMOVED*** if ($req) _e('(required)'); ?></small></label></p>

<p><input type="text" name="url" id="url" value="***REMOVED*** echo  esc_attr($comment_author_url); ?>" size="22" tabindex="3" />
<label for="url"><small>***REMOVED*** _e('Website'); ?></small></label></p>

***REMOVED*** endif; ?>

<!--<p><small>***REMOVED*** printf(__('<strong>XHTML:</strong> You can use these tags: <code>%s</code>'), allowed_tags()); ?></small></p>-->

<p><textarea name="comment" id="comment" cols="58" rows="10" tabindex="4"></textarea></p>

<p><input name="submit" type="submit" id="submit" tabindex="5" value="***REMOVED*** esc_attr_e('Submit Comment'); ?>" />
***REMOVED*** comment_id_fields(); ?>
</p>
***REMOVED*** do_action('comment_form', $post->ID); ?>

</form>

***REMOVED*** endif; // If registration required and not logged in ?>
</div>

***REMOVED*** endif; // if you delete this the sky will fall on your head ?>
