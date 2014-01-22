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
?><!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
     <title>***REMOVED*** printf(__('%1$s - Comments on %2$s'), get_option('blogname'), the_title('','',false)); ?></title>

	<meta http-equiv="Content-Type" content="***REMOVED*** bloginfo('html_type'); ?>; charset=***REMOVED*** echo get_option('blog_charset'); ?>" />
	<style type="text/css" media="screen">
		@import url( ***REMOVED*** bloginfo('stylesheet_url'); ?> );
		body { margin: 3px; }
	</style>

</head>
<body id="commentspopup">

<h1 id="header"><a href="" title="***REMOVED*** echo get_option('blogname'); ?>">***REMOVED*** echo get_option('blogname'); ?></a></h1>

***REMOVED***
/* Don't remove these lines. */
add_filter('comment_text', 'popuplinks');
if ( have_posts() ) :
while( have_posts()) : the_post();
?>
<h2 id="comments">***REMOVED*** _e('Comments'); ?></h2>

<p><a href="***REMOVED*** echo esc_url( get_post_comments_feed_link($post->ID) ); ?>">***REMOVED*** _e('<abbr title="Really Simple Syndication">RSS</abbr> feed for comments on this post.'); ?></a></p>

***REMOVED*** if ( pings_open() ) { ?>
<p>***REMOVED*** printf(__('The <abbr title="Universal Resource Locator">URL</abbr> to TrackBack this entry is: <em>%s</em>'), get_trackback_url()); ?></p>
***REMOVED*** } ?>

***REMOVED***
// this line is WordPress' motor, do not delete it.
$commenter = wp_get_current_commenter();
extract($commenter);
$comments = get_approved_comments($id);
$post = get_post($id);
if ( post_password_required($post) ) {  // and it doesn't match the cookie
	echo(get_the_password_form());
} else { ?>

***REMOVED*** if ($comments) { ?>
<ol id="commentlist">
***REMOVED*** foreach ($comments as $comment) { ?>
	<li id="comment-***REMOVED*** comment_ID() ?>">
	***REMOVED*** comment_text() ?>
	<p><cite>***REMOVED*** comment_type(); ?> ***REMOVED*** printf(__('by %1$s &#8212; %2$s @ <a href="#comment-%3$s">%4$s</a>'), get_comment_author_link(), get_comment_date(), get_comment_ID(), get_comment_time()); ?></cite></p>
	</li>

***REMOVED*** } // end for each comment ?>
</ol>
***REMOVED*** } else { // this is displayed if there are no comments so far ?>
	<p>***REMOVED*** _e('No comments yet.'); ?></p>
***REMOVED*** } ?>

***REMOVED*** if ( comments_open() ) { ?>
<h2>***REMOVED*** _e('Leave a comment'); ?></h2>
<p>***REMOVED*** printf(__('Line and paragraph breaks automatic, e-mail address never displayed, <acronym title="Hypertext Markup Language">HTML</acronym> allowed: <code>%s</code>'), allowed_tags()); ?></p>

<form action="***REMOVED*** echo site_url(); ?>/wp-comments-post.php" method="post" id="commentform">
***REMOVED*** if ( $user_ID ) : ?>
	<p>***REMOVED*** printf(__('Logged in as <a href="%1$s">%2$s</a>. <a href="%3$s" title="Log out of this account">Log out &raquo;</a>'), get_edit_user_link(), $user_identity, wp_logout_url(get_permalink())); ?></p>
***REMOVED*** else : ?>
	<p>
	  <input type="text" name="author" id="author" class="textarea" value="***REMOVED*** echo esc_attr($comment_author); ?>" size="28" tabindex="1" />
	   <label for="author">***REMOVED*** _e('Name'); ?></label>
	</p>

	<p>
	  <input type="text" name="email" id="email" value="***REMOVED*** echo esc_attr($comment_author_email); ?>" size="28" tabindex="2" />
	   <label for="email">***REMOVED*** _e('E-mail'); ?></label>
	</p>

	<p>
	  <input type="text" name="url" id="url" value="***REMOVED*** echo esc_attr($comment_author_url); ?>" size="28" tabindex="3" />
	   <label for="url">***REMOVED*** _e('<abbr title="Universal Resource Locator">URL</abbr>'); ?></label>
	</p>
***REMOVED*** endif; ?>

	<p>
	  <label for="comment">***REMOVED*** _e('Your Comment'); ?></label>
	<br />
	  <textarea name="comment" id="comment" cols="70" rows="4" tabindex="4"></textarea>
	</p>

	<p>
	  <input type="hidden" name="comment_post_ID" value="***REMOVED*** echo $id; ?>" />
	  <input type="hidden" name="redirect_to" value="***REMOVED*** echo esc_attr($_SERVER["REQUEST_URI"]); ?>" />
	  <input name="submit" type="submit" tabindex="5" value="***REMOVED*** esc_attr_e('Say It!' ); ?>" />
	</p>
	***REMOVED*** do_action('comment_form', $post->ID); ?>
</form>
***REMOVED*** } else { // comments are closed ?>
<p>***REMOVED*** _e('Sorry, the comment form is closed at this time.'); ?></p>
***REMOVED*** }
} // end password check
?>

<div><strong><a href="javascript:window.close()">***REMOVED*** _e('Close this window.'); ?></a></strong></div>

***REMOVED*** // if you delete this the sky will fall on your head
endwhile; // have_posts()
else: // have_posts()
?>
<p>***REMOVED*** _e('Sorry, no posts matched your criteria.'); ?></p>
***REMOVED*** endif; ?>
<!-- // this is just the end of the motor - don't touch that line either :) -->
***REMOVED*** //} ?>
<p class="credit">***REMOVED*** timer_stop(1); ?> <cite>***REMOVED*** printf(__('Powered by <a href="%s" title="Powered by WordPress, state-of-the-art semantic personal publishing platform"><strong>WordPress</strong></a>'), 'http://wordpress.org/'); ?></cite></p>
***REMOVED*** // Seen at http://www.mijnkopthee.nl/log2/archive/2003/05/28/esc(18) ?>
<script type="text/javascript">
<!--
document.onkeypress = function esc(e) {
	if(typeof(e) == "undefined") { e=event; }
	if (e.keyCode == 27) { self.close(); }
}
// -->
</script>
</body>
</html>
