***REMOVED***
/**
 * RSS 0.92 Feed Template for displaying RSS 0.92 Posts feed.
 *
 * @package WordPress
 */

header('Content-Type: ' . feed_content_type('rss-http') . '; charset=' . get_option('blog_charset'), true);
$more = 1;

echo '<?xml version="1.0" encoding="'.get_option('blog_charset').'"?'.'>'; ?>
<rss version="0.92">
<channel>
	<title>***REMOVED*** bloginfo_rss('name'); wp_title_rss(); ?></title>
	<link>***REMOVED*** bloginfo_rss('url') ?></link>
	<description>***REMOVED*** bloginfo_rss('description') ?></description>
	<lastBuildDate>***REMOVED*** echo mysql2date('D, d M Y H:i:s +0000', get_lastpostmodified('GMT'), false); ?></lastBuildDate>
	<docs>http://backend.userland.com/rss092</docs>
	<language>***REMOVED*** bloginfo_rss( 'language' ); ?></language>

	***REMOVED***
	/**
	 * Fires at the end of the RSS Feed Header.
	 *
	 * @since 2.0.0
	 */
	do_action( 'rss_head' );
	?>

***REMOVED*** while (have_posts()) : the_post(); ?>
	<item>
		<title>***REMOVED*** the_title_rss() ?></title>
		<description><![CDATA[***REMOVED*** the_excerpt_rss() ?>]]></description>
		<link>***REMOVED*** the_permalink_rss() ?></link>
		***REMOVED***
		/**
		 * Fires at the end of each RSS feed item.
		 *
		 * @since 2.0.0
		 */
		do_action( 'rss_item' );
		?>
	</item>
***REMOVED*** endwhile; ?>
</channel>
</rss>
