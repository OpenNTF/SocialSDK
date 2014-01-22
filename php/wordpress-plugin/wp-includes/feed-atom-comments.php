***REMOVED***
/**
 * Atom Feed Template for displaying Atom Comments feed.
 *
 * @package WordPress
 */

header('Content-Type: ' . feed_content_type('atom') . '; charset=' . get_option('blog_charset'), true);
echo '<?xml version="1.0" encoding="' . get_option('blog_charset') . '" ?' . '>';
?>
<feed
	xmlns="http://www.w3.org/2005/Atom"
	xml:lang="***REMOVED*** bloginfo_rss( 'language' ); ?>"
	xmlns:thr="http://purl.org/syndication/thread/1.0"
	***REMOVED*** do_action('atom_ns'); do_action('atom_comments_ns'); ?>
>
	<title type="text">***REMOVED***
		if ( is_singular() )
			printf( ent2ncr( __( 'Comments on %s' ) ), get_the_title_rss() );
		elseif ( is_search() )
			printf( ent2ncr( __( 'Comments for %1$s searching on %2$s' ) ), get_bloginfo_rss( 'name' ), get_search_query() );
		else
			printf( ent2ncr( __( 'Comments for %s' ) ), get_bloginfo_rss( 'name' ) . get_wp_title_rss() );
	?></title>
	<subtitle type="text">***REMOVED*** bloginfo_rss('description'); ?></subtitle>

	<updated>***REMOVED*** echo mysql2date('Y-m-d\TH:i:s\Z', get_lastcommentmodified('GMT'), false); ?></updated>

***REMOVED*** if ( is_singular() ) { ?>
	<link rel="alternate" type="***REMOVED*** bloginfo_rss('html_type'); ?>" href="***REMOVED*** comments_link_feed(); ?>" />
	<link rel="self" type="application/atom+xml" href="***REMOVED*** echo esc_url( get_post_comments_feed_link('', 'atom') ); ?>" />
	<id>***REMOVED*** echo esc_url( get_post_comments_feed_link('', 'atom') ); ?></id>
***REMOVED*** } elseif(is_search()) { ?>
	<link rel="alternate" type="***REMOVED*** bloginfo_rss('html_type'); ?>" href="***REMOVED*** echo home_url() . '?s=' . get_search_query(); ?>" />
	<link rel="self" type="application/atom+xml" href="***REMOVED*** echo get_search_comments_feed_link('', 'atom'); ?>" />
	<id>***REMOVED*** echo get_search_comments_feed_link('', 'atom'); ?></id>
***REMOVED*** } else { ?>
	<link rel="alternate" type="***REMOVED*** bloginfo_rss('html_type'); ?>" href="***REMOVED*** bloginfo_rss('url'); ?>" />
	<link rel="self" type="application/atom+xml" href="***REMOVED*** bloginfo_rss('comments_atom_url'); ?>" />
	<id>***REMOVED*** bloginfo_rss('comments_atom_url'); ?></id>
***REMOVED*** } ?>
***REMOVED*** do_action('comments_atom_head'); ?>
***REMOVED***
if ( have_comments() ) : while ( have_comments() ) : the_comment();
	$comment_post = $GLOBALS['post'] = get_post( $comment->comment_post_ID );
?>
	<entry>
		<title>***REMOVED***
			if ( !is_singular() ) {
				$title = get_the_title($comment_post->ID);
				/** This filter is documented in wp-includes/feed.php */
				$title = apply_filters( 'the_title_rss', $title );
				printf(ent2ncr(__('Comment on %1$s by %2$s')), $title, get_comment_author_rss());
			} else {
				printf(ent2ncr(__('By: %s')), get_comment_author_rss());
			}
		?></title>
		<link rel="alternate" href="***REMOVED*** comment_link(); ?>" type="***REMOVED*** bloginfo_rss('html_type'); ?>" />

		<author>
			<name>***REMOVED*** comment_author_rss(); ?></name>
			***REMOVED*** if (get_comment_author_url()) echo '<uri>' . get_comment_author_url() . '</uri>'; ?>

		</author>

		<id>***REMOVED*** comment_guid(); ?></id>
		<updated>***REMOVED*** echo mysql2date('Y-m-d\TH:i:s\Z', get_comment_time('Y-m-d H:i:s', true, false), false); ?></updated>
		<published>***REMOVED*** echo mysql2date('Y-m-d\TH:i:s\Z', get_comment_time('Y-m-d H:i:s', true, false), false); ?></published>
***REMOVED*** if ( post_password_required($comment_post) ) : ?>
		<content type="html" xml:base="***REMOVED*** comment_link(); ?>"><![CDATA[***REMOVED*** echo get_the_password_form(); ?>]]></content>
***REMOVED*** else : // post pass ?>
		<content type="html" xml:base="***REMOVED*** comment_link(); ?>"><![CDATA[***REMOVED*** comment_text(); ?>]]></content>
***REMOVED*** endif; // post pass
	// Return comment threading information (http://www.ietf.org/rfc/rfc4685.txt)
	if ( $comment->comment_parent == 0 ) : // This comment is top level ?>
		<thr:in-reply-to ref="***REMOVED*** the_guid(); ?>" href="***REMOVED*** the_permalink_rss() ?>" type="***REMOVED*** bloginfo_rss('html_type'); ?>" />
***REMOVED*** else : // This comment is in reply to another comment
	$parent_comment = get_comment($comment->comment_parent);
	// The rel attribute below and the id tag above should be GUIDs, but WP doesn't create them for comments (unlike posts). Either way, it's more important that they both use the same system
?>
		<thr:in-reply-to ref="***REMOVED*** comment_guid($parent_comment) ?>" href="***REMOVED*** echo get_comment_link($parent_comment) ?>" type="***REMOVED*** bloginfo_rss('html_type'); ?>" />
***REMOVED*** endif;
	do_action('comment_atom_entry', $comment->comment_ID, $comment_post->ID);
?>
	</entry>
***REMOVED*** endwhile; endif; ?>
</feed>
