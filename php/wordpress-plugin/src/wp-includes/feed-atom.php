***REMOVED***
/**
 * Atom Feed Template for displaying Atom Posts feed.
 *
 * @package WordPress
 */

header('Content-Type: ' . feed_content_type('atom') . '; charset=' . get_option('blog_charset'), true);
$more = 1;

echo '<?xml version="1.0" encoding="'.get_option('blog_charset').'"?'.'>'; ?>
<feed
  xmlns="http://www.w3.org/2005/Atom"
  xmlns:thr="http://purl.org/syndication/thread/1.0"
  xml:lang="***REMOVED*** bloginfo_rss( 'language' ); ?>"
  xml:base="***REMOVED*** bloginfo_rss('url') ?>/wp-atom.php"
  ***REMOVED***
  /**
   * Fires at end of the Atom feed root to add namespaces.
   *
   * @since 2.0.0
   */
  do_action( 'atom_ns' );
  ?>
 >
	<title type="text">***REMOVED*** bloginfo_rss('name'); wp_title_rss(); ?></title>
	<subtitle type="text">***REMOVED*** bloginfo_rss("description") ?></subtitle>

	<updated>***REMOVED*** echo mysql2date('Y-m-d\TH:i:s\Z', get_lastpostmodified('GMT'), false); ?></updated>

	<link rel="alternate" type="***REMOVED*** bloginfo_rss('html_type'); ?>" href="***REMOVED*** bloginfo_rss('url') ?>" />
	<id>***REMOVED*** bloginfo('atom_url'); ?></id>
	<link rel="self" type="application/atom+xml" href="***REMOVED*** self_link(); ?>" />

	***REMOVED***
	/**
	 * Fires just before the first Atom feed entry.
	 *
	 * @since 2.0.0
	 */
	do_action( 'atom_head' );

	while ( have_posts() ) : the_post();
	?>
	<entry>
		<author>
			<name>***REMOVED*** the_author() ?></name>
			***REMOVED*** $author_url = get_the_author_meta('url'); if ( !empty($author_url) ) : ?>
			<uri>***REMOVED*** the_author_meta('url')?></uri>
			***REMOVED*** endif;

			/**
			 * Fires at the end of each Atom feed author entry.
			 *
			 * @since 3.2.0
			 */
			do_action( 'atom_author' );
		?>
		</author>
		<title type="***REMOVED*** html_type_rss(); ?>"><![CDATA[***REMOVED*** the_title_rss() ?>]]></title>
		<link rel="alternate" type="***REMOVED*** bloginfo_rss('html_type'); ?>" href="***REMOVED*** the_permalink_rss() ?>" />
		<id>***REMOVED*** the_guid() ; ?></id>
		<updated>***REMOVED*** echo get_post_modified_time('Y-m-d\TH:i:s\Z', true); ?></updated>
		<published>***REMOVED*** echo get_post_time('Y-m-d\TH:i:s\Z', true); ?></published>
		***REMOVED*** the_category_rss('atom') ?>
		<summary type="***REMOVED*** html_type_rss(); ?>"><![CDATA[***REMOVED*** the_excerpt_rss(); ?>]]></summary>
***REMOVED*** if ( !get_option('rss_use_excerpt') ) : ?>
		<content type="***REMOVED*** html_type_rss(); ?>" xml:base="***REMOVED*** the_permalink_rss() ?>"><![CDATA[***REMOVED*** the_content_feed('atom') ?>]]></content>
***REMOVED*** endif; ?>
	***REMOVED*** atom_enclosure();
	/**
	 * Fires at the end of each Atom feed item.
	 *
	 * @since 2.0.0
	 */
	do_action( 'atom_entry' );
		?>
		<link rel="replies" type="***REMOVED*** bloginfo_rss('html_type'); ?>" href="***REMOVED*** the_permalink_rss() ?>#comments" thr:count="***REMOVED*** echo get_comments_number()?>"/>
		<link rel="replies" type="application/atom+xml" href="***REMOVED*** echo esc_url( get_post_comments_feed_link(0, 'atom') ); ?>" thr:count="***REMOVED*** echo get_comments_number()?>"/>
		<thr:total>***REMOVED*** echo get_comments_number()?></thr:total>
	</entry>
	***REMOVED*** endwhile ; ?>
</feed>
