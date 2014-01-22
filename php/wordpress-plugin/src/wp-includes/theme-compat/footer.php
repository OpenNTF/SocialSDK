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

<hr />
<div id="footer" role="contentinfo">
<!-- If you'd like to support WordPress, having the "powered by" link somewhere on your blog is the best way; it's our only promotion or advertising. -->
	<p>
		***REMOVED*** printf(__('%1$s is proudly powered by %2$s'), get_bloginfo('name'),
		'<a href="http://wordpress.org/">WordPress</a>'); ?>
		<br />***REMOVED*** printf(__('%1$s and %2$s.'), '<a href="' . get_bloginfo('rss2_url') . '">' . __('Entries (RSS)') . '</a>', '<a href="' . get_bloginfo('comments_rss2_url') . '">' . __('Comments (RSS)') . '</a>'); ?>
		<!-- ***REMOVED*** printf(__('%d queries. %s seconds.'), get_num_queries(), timer_stop(0, 3)); ?> -->
	</p>
</div>
</div>

<!-- Gorgeous design by Michael Heilemann - http://binarybonsai.com/kubrick/ -->
***REMOVED*** /* "Just what do you think you're doing Dave?" */ ?>

		***REMOVED*** wp_footer(); ?>
</body>
</html>
