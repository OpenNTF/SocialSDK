***REMOVED***
/**
 * Media settings administration panel.
 *
 * @package WordPress
 * @subpackage Administration
 */

/** WordPress Administration Bootstrap */
require_once( dirname( __FILE__ ) . '/admin.php' );

if ( ! current_user_can( 'manage_options' ) )
	wp_die( __( 'You do not have sufficient permissions to manage options for this site.' ) );

$title = __('Media Settings');
$parent_file = 'options-general.php';

$media_options_help = '<p>' . __('You can set maximum sizes for images inserted into your written content; you can also insert an image as Full Size.') . '</p>';

if ( ! is_multisite() && ( get_option('upload_url_path') || ( get_option('upload_path') != 'wp-content/uploads' && get_option('upload_path') ) ) ) {
	$media_options_help .= '<p>' . __('Uploading Files allows you to choose the folder and path for storing your uploaded files.') . '</p>';
}

$media_options_help .= '<p>' . __('You must click the Save Changes button at the bottom of the screen for new settings to take effect.') . '</p>';

get_current_screen()->add_help_tab( array(
	'id'      => 'overview',
	'title'   => __('Overview'),
	'content' => $media_options_help,
) );

get_current_screen()->set_help_sidebar(
	'<p><strong>' . __('For more information:') . '</strong></p>' .
	'<p>' . __('<a href="http://codex.wordpress.org/Settings_Media_Screen" target="_blank">Documentation on Media Settings</a>') . '</p>' .
	'<p>' . __('<a href="http://wordpress.org/support/" target="_blank">Support Forums</a>') . '</p>'
);

include( ABSPATH . 'wp-admin/admin-header.php' );

?>

<div class="wrap">
***REMOVED*** screen_icon(); ?>
<h2>***REMOVED*** echo esc_html( $title ); ?></h2>

<form action="options.php" method="post">
***REMOVED*** settings_fields('media'); ?>

<h3 class="title">***REMOVED*** _e('Image sizes') ?></h3>
<p>***REMOVED*** _e( 'The sizes listed below determine the maximum dimensions in pixels to use when adding an image to the Media Library.' ); ?></p>

<table class="form-table">
<tr valign="top">
<th scope="row">***REMOVED*** _e('Thumbnail size') ?></th>
<td>
<label for="thumbnail_size_w">***REMOVED*** _e('Width'); ?></label>
<input name="thumbnail_size_w" type="number" step="1" min="0" id="thumbnail_size_w" value="***REMOVED*** form_option('thumbnail_size_w'); ?>" class="small-text" />
<label for="thumbnail_size_h">***REMOVED*** _e('Height'); ?></label>
<input name="thumbnail_size_h" type="number" step="1" min="0" id="thumbnail_size_h" value="***REMOVED*** form_option('thumbnail_size_h'); ?>" class="small-text" /><br />
<input name="thumbnail_crop" type="checkbox" id="thumbnail_crop" value="1" ***REMOVED*** checked('1', get_option('thumbnail_crop')); ?>/>
<label for="thumbnail_crop">***REMOVED*** _e('Crop thumbnail to exact dimensions (normally thumbnails are proportional)'); ?></label>
</td>
</tr>

<tr valign="top">
<th scope="row">***REMOVED*** _e('Medium size') ?></th>
<td><fieldset><legend class="screen-reader-text"><span>***REMOVED*** _e('Medium size'); ?></span></legend>
<label for="medium_size_w">***REMOVED*** _e('Max Width'); ?></label>
<input name="medium_size_w" type="number" step="1" min="0" id="medium_size_w" value="***REMOVED*** form_option('medium_size_w'); ?>" class="small-text" />
<label for="medium_size_h">***REMOVED*** _e('Max Height'); ?></label>
<input name="medium_size_h" type="number" step="1" min="0" id="medium_size_h" value="***REMOVED*** form_option('medium_size_h'); ?>" class="small-text" />
</fieldset></td>
</tr>

<tr valign="top">
<th scope="row">***REMOVED*** _e('Large size') ?></th>
<td><fieldset><legend class="screen-reader-text"><span>***REMOVED*** _e('Large size'); ?></span></legend>
<label for="large_size_w">***REMOVED*** _e('Max Width'); ?></label>
<input name="large_size_w" type="number" step="1" min="0" id="large_size_w" value="***REMOVED*** form_option('large_size_w'); ?>" class="small-text" />
<label for="large_size_h">***REMOVED*** _e('Max Height'); ?></label>
<input name="large_size_h" type="number" step="1" min="0" id="large_size_h" value="***REMOVED*** form_option('large_size_h'); ?>" class="small-text" />
</fieldset></td>
</tr>

***REMOVED*** do_settings_fields('media', 'default'); ?>
</table>

***REMOVED*** if ( isset( $GLOBALS['wp_settings']['media']['embeds'] ) ) : ?>
<h3 class="title">***REMOVED*** _e('Embeds') ?></h3>
<table class="form-table">
***REMOVED*** do_settings_fields( 'media', 'embeds' ); ?>
</table>
***REMOVED*** endif; ?>

***REMOVED*** if ( !is_multisite() ) : ?>
<h3 class="title">***REMOVED*** _e('Uploading Files'); ?></h3>
<table class="form-table">
***REMOVED***
// If upload_url_path is not the default (empty), and upload_path is not the default ('wp-content/uploads' or empty)
if ( get_option('upload_url_path') || ( get_option('upload_path') != 'wp-content/uploads' && get_option('upload_path') ) ) :
?>
<tr valign="top">
<th scope="row"><label for="upload_path">***REMOVED*** _e('Store uploads in this folder'); ?></label></th>
<td><input name="upload_path" type="text" id="upload_path" value="***REMOVED*** echo esc_attr(get_option('upload_path')); ?>" class="regular-text code" />
<p class="description">***REMOVED*** _e('Default is <code>wp-content/uploads</code>'); ?></p>
</td>
</tr>

<tr valign="top">
<th scope="row"><label for="upload_url_path">***REMOVED*** _e('Full URL path to files'); ?></label></th>
<td><input name="upload_url_path" type="text" id="upload_url_path" value="***REMOVED*** echo esc_attr( get_option('upload_url_path')); ?>" class="regular-text code" />
<p class="description">***REMOVED*** _e('Configuring this is optional. By default, it should be blank.'); ?></p>
</td>
</tr>
***REMOVED*** endif; ?>
<tr>
<th scope="row" colspan="2" class="th-full">
<label for="uploads_use_yearmonth_folders">
<input name="uploads_use_yearmonth_folders" type="checkbox" id="uploads_use_yearmonth_folders" value="1"***REMOVED*** checked('1', get_option('uploads_use_yearmonth_folders')); ?> />
***REMOVED*** _e('Organize my uploads into month- and year-based folders'); ?>
</label>
</th>
</tr>

***REMOVED*** do_settings_fields('media', 'uploads'); ?>
</table>
***REMOVED*** endif; ?>

***REMOVED*** do_settings_sections('media'); ?>

***REMOVED*** submit_button(); ?>

</form>

</div>

***REMOVED*** include( ABSPATH . 'wp-admin/admin-footer.php' ); ?>
