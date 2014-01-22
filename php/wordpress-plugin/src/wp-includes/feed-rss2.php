***REMOVED***
/**
 * RSS2 Feed Template for displaying RSS2 Posts feed.
 *
 * @package WordPress
 */

header('Content-Type: ' . feed_content_type('rss-http') . '; charset=' . get_option('blog_charset'), true);
$more = 1;

echo '<?xml version="1.0" encoding="'.get_option('blog_charset').'"?'.'>'; ?>

<rss version="2.0"
	xmlns:content="http://purl.org/rss/1.0/modules/content/"
	xmlns:wfw="http://wellformedweb.org/CommentAPI/"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:atom="http://www.w3.org/2005/Atom"
	xmlns:sy="http://purl.org/rss/1.0/modules/syndication/"
	xmlns:slash="http://purl.org/rss/1.0/modules/slash/"
	***REMOVED***
	/**
	 * Fires at the end of the RSS root to add namespaces.
	 *
	 * @since 2.0.0
	 */
	do_action( 'rss2_ns' );
	?>
>

<channel>
	<title>***REMOVED*** bloginfo_rss('name'); wp_title_rss(); ?></title>
	<atom:link href="***REMOVED*** self_link(); ?>" rel="self" type="application/rss+xml" />
	<link>***REMOVED*** bloginfo_rss('url') ?></link>
	<description>***REMOVED*** bloginfo_rss("description") ?></description>
	<lastBuildDate>***REMOVED*** echo mysql2date('D, d M Y H:i:s +0000', get_lastpostmodified('GMT'), false); ?></lastBuildDate>
	<language>***REMOVED*** bloginfo_rss( 'language' ); ?></language>
	***REMOVED***
	$duration = 'hourly';
	/**
	 * Filter how often to update the RSS feed.
	 *
	 * @since 2.1.0
	 *
	 * @param string $duration The update period.
	 *                         Default 'hourly'. Accepts 'hourly', 'daily', 'weekly', 'monthly', 'yearly'.
	 */
	?>
	<sy:updatePeriod>***REMOVED*** echo apply_filters( 'rss_update_period', $duration ); ?></sy:updatePeriod>
	***REMOVED***
	$frequency = '1';
	/**
	 * Filter the RSS update frequency.
	 *
	 * @since 2.1.0
	 *
	 * @param string $frequency An integer passed as a string representing the frequency
	 *                          of RSS updates within the update period. Default '1'.
	 */
	?>
	<sy:updateFrequency>***REMOVED*** echo apply_filters( 'rss_update_frequency', $frequency ); ?></sy:updateFrequency>
	***REMOVED***
	/**
	 * Fires at the end of the RSS2 Feed Header.
	 *
	 * @since 2.0.0
	 */
	do_action( 'rss2_head');

	while( have_posts()) : the_post();
	?>
	<item>
		<title>***REMOVED*** the_title_rss() ?></title>
		<link>***REMOVED*** the_permalink_rss() ?></link>
		<comments>***REMOVED*** comments_link_feed(); ?></comments>
		<pubDate>***REMOVED*** echo mysql2date('D, d M Y H:i:s +0000', get_post_time('Y-m-d H:i:s', true), false); ?></pubDate>
		<dc:creator><![CDATA[***REMOVED*** the_author() ?>]]></dc:creator>
		***REMOVED*** the_category_rss('rss2') ?>

		<guid isPermaLink="false">***REMOVED*** the_guid(); ?></guid>
***REMOVED*** if (get_option('rss_use_excerpt')) : ?>
		<description><![CDATA[***REMOVED*** the_excerpt_rss(); ?>]]></description>
***REMOVED*** else : ?>
		<description><![CDATA[***REMOVED*** the_excerpt_rss(); ?>]]></description>
	***REMOVED*** $content = get_the_content_feed('rss2'); ?>
	***REMOVED*** if ( strlen( $content ) > 0 ) : ?>
		<content:encoded><![CDATA[***REMOVED*** echo $content; ?>]]></content:encoded>
	***REMOVED*** else : ?>
		<content:encoded><![CDATA[***REMOVED*** the_excerpt_rss(); ?>]]></content:encoded>
	***REMOVED*** endif; ?>
***REMOVED*** endif; ?>
		<wfw:commentRss>***REMOVED*** echo esc_url( get_post_comments_feed_link(null, 'rss2') ); ?></wfw:commentRss>
		<slash:comments>***REMOVED*** echo get_comments_number(); ?></slash:comments>
***REMOVED*** rss_enclosure(); ?>
	***REMOVED***
	/**
	 * Fires at the end of each RSS2 feed item.
	 *
	 * @since 2.0.0
	 */
	do_action( 'rss2_item' );
	?>
	</item>
	***REMOVED*** endwhile; ?>
</channel>
</rss>
