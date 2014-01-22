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
?>
	<div id="sidebar" role="complementary">
		<ul>
			***REMOVED*** 	/* Widgetized sidebar, if you have the plugin installed. */
					if ( !function_exists('dynamic_sidebar') || !dynamic_sidebar() ) : ?>
			<li>
				***REMOVED*** get_search_form(); ?>
			</li>

			<!-- Author information is disabled per default. Uncomment and fill in your details if you want to use it.
			<li><h2>***REMOVED*** _e('Author'); ?></h2>
			<p>A little something about you, the author. Nothing lengthy, just an overview.</p>
			</li>
			-->

			***REMOVED*** if ( is_404() || is_category() || is_day() || is_month() ||
						is_year() || is_search() || is_paged() ) {
			?> <li>

			***REMOVED*** /* If this is a 404 page */ if (is_404()) { ?>
			***REMOVED*** /* If this is a category archive */ } elseif (is_category()) { ?>
			<p>***REMOVED*** printf(__('You are currently browsing the archives for the %s category.'), single_cat_title('', false)); ?></p>

			***REMOVED*** /* If this is a daily archive */ } elseif (is_day()) { ?>
			<p>***REMOVED*** printf(__('You are currently browsing the <a href="%1$s/">%2$s</a> blog archives for the day %3$s.'), get_bloginfo('url'), get_bloginfo('name'), get_the_time(__('l, F jS, Y'))); ?></p>

			***REMOVED*** /* If this is a monthly archive */ } elseif (is_month()) { ?>
			<p>***REMOVED*** printf(__('You are currently browsing the <a href="%1$s/">%2$s</a> blog archives for %3$s.'), get_bloginfo('url'), get_bloginfo('name'), get_the_time(__('F, Y'))); ?></p>

			***REMOVED*** /* If this is a yearly archive */ } elseif (is_year()) { ?>
			<p>***REMOVED*** printf(__('You are currently browsing the <a href="%1$s/">%2$s</a> blog archives for the year %3$s.'), get_bloginfo('url'), get_bloginfo('name'), get_the_time('Y')); ?></p>

			***REMOVED*** /* If this is a search result */ } elseif (is_search()) { ?>
			<p>***REMOVED*** printf(__('You have searched the <a href="%1$s/">%2$s</a> blog archives for <strong>&#8216;%3$s&#8217;</strong>. If you are unable to find anything in these search results, you can try one of these links.'), get_bloginfo('url'), get_bloginfo('name'), esc_html( get_search_query() ) ); ?></p>

			***REMOVED*** /* If this set is paginated */ } elseif (isset($_GET['paged']) && !empty($_GET['paged'])) { ?>
			<p>***REMOVED*** printf(__('You are currently browsing the <a href="%1$s/">%2$s</a> blog archives.'), get_bloginfo('url'), get_bloginfo('name')); ?></p>

			***REMOVED*** } ?>

			</li>
		***REMOVED*** }?>
		</ul>
		<ul role="navigation">
			***REMOVED*** wp_list_pages('title_li=<h2>' . __('Pages') . '</h2>' ); ?>

			<li><h2>***REMOVED*** _e('Archives'); ?></h2>
				<ul>
				***REMOVED*** wp_get_archives(array('type' => 'monthly')); ?>
				</ul>
			</li>

			***REMOVED*** wp_list_categories(array('show_count' => 1, 'title_li' => '<h2>' . __('Categories') . '</h2>')); ?>
		</ul>
		<ul>
			***REMOVED*** /* If this is the frontpage */ if ( is_home() || is_page() ) { ?>
				***REMOVED*** wp_list_bookmarks(); ?>

				<li><h2>***REMOVED*** _e('Meta'); ?></h2>
				<ul>
					***REMOVED*** wp_register(); ?>
					<li>***REMOVED*** wp_loginout(); ?></li>
					<li><a href="http://validator.w3.org/check/referer" title="***REMOVED*** esc_attr_e('This page validates as XHTML 1.0 Transitional'); ?>">***REMOVED*** _e('Valid <abbr title="eXtensible HyperText Markup Language">XHTML</abbr>'); ?></a></li>
					<li><a href="http://gmpg.org/xfn/"><abbr title="***REMOVED*** esc_attr_e('XHTML Friends Network'); ?>">***REMOVED*** _e('XFN'); ?></abbr></a></li>
					<li><a href="http://wordpress.org/" title="***REMOVED*** esc_attr_e('Powered by WordPress, state-of-the-art semantic personal publishing platform.'); ?>">WordPress</a></li>
					***REMOVED*** wp_meta(); ?>
				</ul>
				</li>
			***REMOVED*** } ?>

			***REMOVED*** endif; ?>
		</ul>
	</div>
