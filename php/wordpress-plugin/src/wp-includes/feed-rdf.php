***REMOVED***
/**
 * RSS 1 RDF Feed Template for displaying RSS 1 Posts feed.
 *
 * @package WordPress
 */

header('Content-Type: ' . feed_content_type('rdf') . '; charset=' . get_option('blog_charset'), true);
$more = 1;

echo '<?xml version="1.0" encoding="'.get_option('blog_charset').'"?'.'>'; ?>
<rdf:RDF
	xmlns="http://purl.org/rss/1.0/"
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:sy="http://purl.org/rss/1.0/modules/syndication/"
	xmlns:admin="http://webns.net/mvcb/"
	xmlns:content="http://purl.org/rss/1.0/modules/content/"
	***REMOVED***
	/**
	 * Fires at the end of the feed root to add namespaces.
	 *
	 * @since 2.0.0
	 */
	do_action( 'rdf_ns' );
	?>
>
<channel rdf:about="***REMOVED*** bloginfo_rss("url") ?>">
	<title>***REMOVED*** bloginfo_rss('name'); wp_title_rss(); ?></title>
	<link>***REMOVED*** bloginfo_rss('url') ?></link>
	<description>***REMOVED*** bloginfo_rss('description') ?></description>
	<dc:date>***REMOVED*** echo mysql2date('Y-m-d\TH:i:s\Z', get_lastpostmodified('GMT'), false); ?></dc:date>
	***REMOVED*** /** This filter is documented in wp-includes/feed-rss2.php */ ?>
	<sy:updatePeriod>***REMOVED*** echo apply_filters( 'rss_update_period', 'hourly' ); ?></sy:updatePeriod>
	***REMOVED*** /** This filter is documented in wp-includes/feed-rss2.php */ ?>
	<sy:updateFrequency>***REMOVED*** echo apply_filters( 'rss_update_frequency', '1' ); ?></sy:updateFrequency>
	<sy:updateBase>2000-01-01T12:00+00:00</sy:updateBase>
	***REMOVED***
	/**
	 * Fires at the end of the RDF feed header.
	 *
	 * @since 2.0.0
	 */
	do_action( 'rdf_header' );
	?>
	<items>
		<rdf:Seq>
		***REMOVED*** while (have_posts()): the_post(); ?>
			<rdf:li rdf:resource="***REMOVED*** the_permalink_rss() ?>"/>
		***REMOVED*** endwhile; ?>
		</rdf:Seq>
	</items>
</channel>
***REMOVED*** rewind_posts(); while (have_posts()): the_post(); ?>
<item rdf:about="***REMOVED*** the_permalink_rss() ?>">
	<title>***REMOVED*** the_title_rss() ?></title>
	<link>***REMOVED*** the_permalink_rss() ?></link>
	<dc:date>***REMOVED*** echo mysql2date('Y-m-d\TH:i:s\Z', $post->post_date_gmt, false); ?></dc:date>
	<dc:creator><![CDATA[***REMOVED*** the_author() ?>]]></dc:creator>
	***REMOVED*** the_category_rss('rdf') ?>
***REMOVED*** if (get_option('rss_use_excerpt')) : ?>
	<description><![CDATA[***REMOVED*** the_excerpt_rss() ?>]]></description>
***REMOVED*** else : ?>
	<description><![CDATA[***REMOVED*** the_excerpt_rss() ?>]]></description>
	<content:encoded><![CDATA[***REMOVED*** the_content_feed('rdf') ?>]]></content:encoded>
***REMOVED*** endif; ?>
	***REMOVED***
	/**
	 * Fires at the end of each RDF feed item.
	 *
	 * @since 2.0.0
	 */
	do_action( 'rdf_item' );
	?>
</item>
***REMOVED*** endwhile;  ?>
</rdf:RDF>
