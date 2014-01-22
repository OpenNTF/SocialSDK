***REMOVED***
/**
 * Tools Administration Screen.
 *
 * @package WordPress
 * @subpackage Administration
 */

/** WordPress Administration Bootstrap */
require_once( dirname( __FILE__ ) . '/admin.php' );

$title = __('Tools');

get_current_screen()->add_help_tab( array(
	'id'      => 'press-this',
	'title'   => __('Press This'),
	'content' => '<p>' . __('Press This is a bookmarklet that makes it easy to blog about something you come across on the web. You can use it to just grab a link, or to post an excerpt. Press This will even allow you to choose from images included on the page and use them in your post. Just drag the Press This link on this screen to your bookmarks bar in your browser, and you&#8217;ll be on your way to easier content creation. Clicking on it while on another website opens a popup window with all these options.') . '</p>',
) );
get_current_screen()->add_help_tab( array(
	'id'      => 'converter',
	'title'   => __('Categories and Tags Converter'),
	'content' => '<p>' . __('Categories have hierarchy, meaning that you can nest sub-categories. Tags do not have hierarchy and cannot be nested. Sometimes people start out using one on their posts, then later realize that the other would work better for their content.' ) . '</p>' .
	'<p>' . __( 'The Categories and Tags Converter link on this screen will take you to the Import screen, where that Converter is one of the plugins you can install. Once that plugin is installed, the Activate Plugin &amp; Run Importer link will take you to a screen where you can choose to convert tags into categories or vice versa.' ) . '</p>',
) );

get_current_screen()->set_help_sidebar(
	'<p><strong>' . __('For more information:') . '</strong></p>' .
	'<p>' . __('<a href="http://codex.wordpress.org/Tools_Screen" target="_blank">Documentation on Tools</a>') . '</p>' .
	'<p>' . __('<a href="http://wordpress.org/support/" target="_blank">Support Forums</a>') . '</p>'
);

require_once( ABSPATH . 'wp-admin/admin-header.php' );

?>
<div class="wrap">
***REMOVED*** screen_icon(); ?>
<h2>***REMOVED*** echo esc_html( $title ); ?></h2>

***REMOVED*** if ( current_user_can('edit_posts') ) : ?>
<div class="tool-box">
	<h3 class="title">***REMOVED*** _e('Press This') ?></h3>
	<p>***REMOVED*** _e('Press This is a bookmarklet: a little app that runs in your browser and lets you grab bits of the web.');?></p>

	<p>***REMOVED*** _e('Use Press This to clip text, images and videos from any web page. Then edit and add more straight from Press This before you save or publish it in a post on your site.'); ?></p>
	<p class="description">***REMOVED*** _e('Drag-and-drop the following link to your bookmarks bar or right click it and add it to your favorites for a posting shortcut.') ?></p>
	<p class="pressthis"><a onclick="return false;" oncontextmenu="if(window.navigator.userAgent.indexOf('WebKit')!=-1||window.navigator.userAgent.indexOf('MSIE')!=-1){jQuery('.pressthis-code').show().find('textarea').focus().select();return false;}" href="***REMOVED*** echo htmlspecialchars( get_shortcut_link() ); ?>"><span>***REMOVED*** _e('Press This') ?></span></a></p>
	<div class="pressthis-code" style="display:none;">
	<p class="description">***REMOVED*** _e('If your bookmarks toolbar is hidden: copy the code below, open your Bookmarks manager, create new bookmark, type Press This into the name field and paste the code into the URL field.') ?></p>
	<p><textarea rows="5" cols="120" readonly="readonly">***REMOVED*** echo htmlspecialchars( get_shortcut_link() ); ?></textarea></p>
	</div>
</div>
***REMOVED***
endif;

if ( current_user_can( 'import' ) ) :
$cats = get_taxonomy('category');
$tags = get_taxonomy('post_tag');
if ( current_user_can($cats->cap->manage_terms) || current_user_can($tags->cap->manage_terms) ) : ?>
<div class="tool-box">
    <h3 class="title">***REMOVED*** _e( 'Categories and Tags Converter' ) ?></h3>
    <p>***REMOVED*** printf( __('If you want to convert your categories to tags (or vice versa), use the <a href="%s">Categories and Tags Converter</a> available from the Import screen.'), 'import.php' ); ?></p>
</div>
***REMOVED***
endif;
endif;

do_action( 'tool_box' );
?>
</div>
***REMOVED***
include( ABSPATH . 'wp-admin/admin-footer.php' );
