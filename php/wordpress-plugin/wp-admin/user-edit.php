***REMOVED***
/**
 * Edit user administration panel.
 *
 * @package WordPress
 * @subpackage Administration
 */

/** WordPress Administration Bootstrap */
require_once( dirname( __FILE__ ) . '/admin.php' );

wp_reset_vars( array( 'action', 'user_id', 'wp_http_referer' ) );

$user_id = (int) $user_id;
$current_user = wp_get_current_user();
if ( ! defined( 'IS_PROFILE_PAGE' ) )
	define( 'IS_PROFILE_PAGE', ( $user_id == $current_user->ID ) );

if ( ! $user_id && IS_PROFILE_PAGE )
	$user_id = $current_user->ID;
elseif ( ! $user_id && ! IS_PROFILE_PAGE )
	wp_die(__( 'Invalid user ID.' ) );
elseif ( ! get_userdata( $user_id ) )
	wp_die( __('Invalid user ID.') );

wp_enqueue_script('user-profile');

$title = IS_PROFILE_PAGE ? __('Profile') : __('Edit User');
if ( current_user_can('edit_users') && !IS_PROFILE_PAGE )
	$submenu_file = 'users.php';
else
	$submenu_file = 'profile.php';

if ( current_user_can('edit_users') && !is_user_admin() )
	$parent_file = 'users.php';
else
	$parent_file = 'profile.php';

$profile_help = '<p>' . __('Your profile contains information about you (your &#8220;account&#8221;) as well as some personal options related to using WordPress.') . '</p>' .
	'<p>' . __('You can change your password, turn on keyboard shortcuts, change the color scheme of your WordPress administration screens, and turn off the WYSIWYG (Visual) editor, among other things. You can hide the Toolbar (formerly called the Admin Bar) from the front end of your site, however it cannot be disabled on the admin screens.') . '</p>' .
	'<p>' . __('Your username cannot be changed, but you can use other fields to enter your real name or a nickname, and change which name to display on your posts.') . '</p>' .
	'<p>' . __('Required fields are indicated; the rest are optional. Profile information will only be displayed if your theme is set up to do so.') . '</p>' .
	'<p>' . __('Remember to click the Update Profile button when you are finished.') . '</p>';

get_current_screen()->add_help_tab( array(
	'id'      => 'overview',
	'title'   => __('Overview'),
	'content' => $profile_help,
) );

get_current_screen()->set_help_sidebar(
    '<p><strong>' . __('For more information:') . '</strong></p>' .
    '<p>' . __('<a href="http://codex.wordpress.org/Users_Your_Profile_Screen" target="_blank">Documentation on User Profiles</a>') . '</p>' .
    '<p>' . __('<a href="http://wordpress.org/support/" target="_blank">Support Forums</a>') . '</p>'
);

$wp_http_referer = remove_query_arg(array('update', 'delete_count'), $wp_http_referer );

$user_can_edit = current_user_can( 'edit_posts' ) || current_user_can( 'edit_pages' );

/**
 * Optional SSL preference that can be turned on by hooking to the 'personal_options' action.
 *
 * @since 2.7.0
 *
 * @param object $user User data object
 */
function use_ssl_preference($user) {
?>
	<tr>
		<th scope="row">***REMOVED*** _e('Use https')?></th>
		<td><label for="use_ssl"><input name="use_ssl" type="checkbox" id="use_ssl" value="1" ***REMOVED*** checked('1', $user->use_ssl); ?> /> ***REMOVED*** _e('Always use https when visiting the admin'); ?></label></td>
	</tr>
***REMOVED***
}

// Only allow super admins on multisite to edit every user.
if ( is_multisite() && ! current_user_can( 'manage_network_users' ) && $user_id != $current_user->ID && ! apply_filters( 'enable_edit_any_user_configuration', true ) )
	wp_die( __( 'You do not have permission to edit this user.' ) );

// Execute confirmed email change. See send_confirmation_on_profile_email().
if ( is_multisite() && IS_PROFILE_PAGE && isset( $_GET[ 'newuseremail' ] ) && $current_user->ID ) {
	$new_email = get_option( $current_user->ID . '_new_email' );
	if ( $new_email[ 'hash' ] == $_GET[ 'newuseremail' ] ) {
		$user = new stdClass;
		$user->ID = $current_user->ID;
		$user->user_email = esc_html( trim( $new_email[ 'newemail' ] ) );
		if ( $wpdb->get_var( $wpdb->prepare( "SELECT user_login FROM {$wpdb->signups} WHERE user_login = %s", $current_user->user_login ) ) )
			$wpdb->query( $wpdb->prepare( "UPDATE {$wpdb->signups} SET user_email = %s WHERE user_login = %s", $user->user_email, $current_user->user_login ) );
		wp_update_user( $user );
		delete_option( $current_user->ID . '_new_email' );
		wp_redirect( add_query_arg( array('updated' => 'true'), self_admin_url( 'profile.php' ) ) );
		die();
	}
} elseif ( is_multisite() && IS_PROFILE_PAGE && !empty( $_GET['dismiss'] ) && $current_user->ID . '_new_email' == $_GET['dismiss'] ) {
	delete_option( $current_user->ID . '_new_email' );
	wp_redirect( add_query_arg( array('updated' => 'true'), self_admin_url( 'profile.php' ) ) );
	die();
}

switch ($action) {
case 'update':

check_admin_referer('update-user_' . $user_id);

if ( !current_user_can('edit_user', $user_id) )
	wp_die(__('You do not have permission to edit this user.'));

if ( IS_PROFILE_PAGE )
	do_action('personal_options_update', $user_id);
else
	do_action('edit_user_profile_update', $user_id);

if ( !is_multisite() ) {
	$errors = edit_user($user_id);
} else {
	$user = get_userdata( $user_id );

	// Update the email address in signups, if present.
	if ( $user->user_login && isset( $_POST[ 'email' ] ) && is_email( $_POST[ 'email' ] ) && $wpdb->get_var( $wpdb->prepare( "SELECT user_login FROM {$wpdb->signups} WHERE user_login = %s", $user->user_login ) ) )
		$wpdb->query( $wpdb->prepare( "UPDATE {$wpdb->signups} SET user_email = %s WHERE user_login = %s", $_POST[ 'email' ], $user_login ) );

	// WPMU must delete the user from the current blog if WP added him after editing.
	$delete_role = false;
	$blog_prefix = $wpdb->get_blog_prefix();
	if ( $user_id != $current_user->ID ) {
		$cap = $wpdb->get_var( "SELECT meta_value FROM {$wpdb->usermeta} WHERE user_id = '{$user_id}' AND meta_key = '{$blog_prefix}capabilities' AND meta_value = 'a:0:{}'" );
		if ( !is_network_admin() && null == $cap && $_POST[ 'role' ] == '' ) {
			$_POST[ 'role' ] = 'contributor';
			$delete_role = true;
		}
	}
	if ( !isset( $errors ) || ( isset( $errors ) && is_object( $errors ) && false == $errors->get_error_codes() ) )
		$errors = edit_user($user_id);
	if ( $delete_role ) // stops users being added to current blog when they are edited
		delete_user_meta( $user_id, $blog_prefix . 'capabilities' );

	if ( is_multisite() && is_network_admin() && !IS_PROFILE_PAGE && current_user_can( 'manage_network_options' ) && !isset($super_admins) && empty( $_POST['super_admin'] ) == is_super_admin( $user_id ) )
		empty( $_POST['super_admin'] ) ? revoke_super_admin( $user_id ) : grant_super_admin( $user_id );
}

if ( !is_wp_error( $errors ) ) {
	$redirect = add_query_arg( 'updated', true, get_edit_user_link( $user_id ) );
	if ( $wp_http_referer )
		$redirect = add_query_arg('wp_http_referer', urlencode($wp_http_referer), $redirect);
	wp_redirect($redirect);
	exit;
}

default:
$profileuser = get_user_to_edit($user_id);

if ( !current_user_can('edit_user', $user_id) )
	wp_die(__('You do not have permission to edit this user.'));

include (ABSPATH . 'wp-admin/admin-header.php');
?>

***REMOVED*** if ( !IS_PROFILE_PAGE && is_super_admin( $profileuser->ID ) && current_user_can( 'manage_network_options' ) ) { ?>
	<div class="updated"><p><strong>***REMOVED*** _e('Important:'); ?></strong> ***REMOVED*** _e('This user has super admin privileges.'); ?></p></div>
***REMOVED*** } ?>
***REMOVED*** if ( isset($_GET['updated']) ) : ?>
<div id="message" class="updated">
	***REMOVED*** if ( IS_PROFILE_PAGE ) : ?>
	<p><strong>***REMOVED*** _e('Profile updated.') ?></strong></p>
	***REMOVED*** else: ?>
	<p><strong>***REMOVED*** _e('User updated.') ?></strong></p>
	***REMOVED*** endif; ?>
	***REMOVED*** if ( $wp_http_referer && !IS_PROFILE_PAGE ) : ?>
	<p><a href="***REMOVED*** echo esc_url( $wp_http_referer ); ?>">***REMOVED*** _e('&larr; Back to Users'); ?></a></p>
	***REMOVED*** endif; ?>
</div>
***REMOVED*** endif; ?>
***REMOVED*** if ( isset( $errors ) && is_wp_error( $errors ) ) : ?>
<div class="error"><p>***REMOVED*** echo implode( "</p>\n<p>", $errors->get_error_messages() ); ?></p></div>
***REMOVED*** endif; ?>

<div class="wrap" id="profile-page">
***REMOVED*** screen_icon(); ?>
<h2>
***REMOVED***
echo esc_html( $title );
if ( ! IS_PROFILE_PAGE ) {
	if ( current_user_can( 'create_users' ) ) { ?>
		<a href="user-new.php" class="add-new-h2">***REMOVED*** echo esc_html_x( 'Add New', 'user' ); ?></a>
	***REMOVED*** } elseif ( is_multisite() && current_user_can( 'promote_users' ) ) { ?>
		<a href="user-new.php" class="add-new-h2">***REMOVED*** echo esc_html_x( 'Add Existing', 'user' ); ?></a>
	***REMOVED*** }
} ?>
</h2>

<form id="your-profile" action="***REMOVED*** echo esc_url( self_admin_url( IS_PROFILE_PAGE ? 'profile.php' : 'user-edit.php' ) ); ?>" method="post"***REMOVED*** do_action('user_edit_form_tag'); ?>>
***REMOVED*** wp_nonce_field('update-user_' . $user_id) ?>
***REMOVED*** if ( $wp_http_referer ) : ?>
	<input type="hidden" name="wp_http_referer" value="***REMOVED*** echo esc_url($wp_http_referer); ?>" />
***REMOVED*** endif; ?>
<p>
<input type="hidden" name="from" value="profile" />
<input type="hidden" name="checkuser_id" value="***REMOVED*** echo $user_ID ?>" />
</p>

<h3>***REMOVED*** _e('Personal Options'); ?></h3>

<table class="form-table">
***REMOVED*** if ( rich_edit_exists() && !( IS_PROFILE_PAGE && !$user_can_edit ) ) : // don't bother showing the option if the editor has been removed ?>
	<tr>
		<th scope="row">***REMOVED*** _e('Visual Editor')?></th>
		<td><label for="rich_editing"><input name="rich_editing" type="checkbox" id="rich_editing" value="false" ***REMOVED*** if ( ! empty( $profileuser->rich_editing ) ) checked( 'false', $profileuser->rich_editing ); ?> /> ***REMOVED*** _e( 'Disable the visual editor when writing' ); ?></label></td>
	</tr>
***REMOVED*** endif; ?>
***REMOVED*** if ( count($_wp_admin_css_colors) > 1 && has_action('admin_color_scheme_picker') ) : ?>
<tr>
<th scope="row">***REMOVED*** _e('Admin Color Scheme')?></th>
<td>***REMOVED*** do_action( 'admin_color_scheme_picker' ); ?></td>
</tr>
***REMOVED***
endif; // $_wp_admin_css_colors
if ( !( IS_PROFILE_PAGE && !$user_can_edit ) ) : ?>
<tr>
<th scope="row">***REMOVED*** _e( 'Keyboard Shortcuts' ); ?></th>
<td><label for="comment_shortcuts"><input type="checkbox" name="comment_shortcuts" id="comment_shortcuts" value="true" ***REMOVED*** if ( ! empty( $profileuser->comment_shortcuts ) ) checked( 'true', $profileuser->comment_shortcuts ); ?> /> ***REMOVED*** _e('Enable keyboard shortcuts for comment moderation.'); ?></label> ***REMOVED*** _e('<a href="http://codex.wordpress.org/Keyboard_Shortcuts" target="_blank">More information</a>'); ?></td>
</tr>
***REMOVED*** endif; ?>
<tr class="show-admin-bar">
<th scope="row">***REMOVED*** _e('Toolbar')?></th>
<td><fieldset><legend class="screen-reader-text"><span>***REMOVED*** _e('Toolbar') ?></span></legend>
<label for="admin_bar_front">
<input name="admin_bar_front" type="checkbox" id="admin_bar_front" value="1"***REMOVED*** checked( _get_admin_bar_pref( 'front', $profileuser->ID ) ); ?> />
***REMOVED*** _e( 'Show Toolbar when viewing site' ); ?></label><br />
</fieldset>
</td>
</tr>
***REMOVED*** do_action('personal_options', $profileuser); ?>
</table>
***REMOVED***
	if ( IS_PROFILE_PAGE )
		do_action('profile_personal_options', $profileuser);
?>

<h3>***REMOVED*** _e('Name') ?></h3>

<table class="form-table">
	<tr>
		<th><label for="user_login">***REMOVED*** _e('Username'); ?></label></th>
		<td><input type="text" name="user_login" id="user_login" value="***REMOVED*** echo esc_attr($profileuser->user_login); ?>" disabled="disabled" class="regular-text" /> <span class="description">***REMOVED*** _e('Usernames cannot be changed.'); ?></span></td>
	</tr>

***REMOVED*** if ( !IS_PROFILE_PAGE && !is_network_admin() ) : ?>
<tr><th><label for="role">***REMOVED*** _e('Role') ?></label></th>
<td><select name="role" id="role">
***REMOVED***
// Compare user role against currently editable roles
$user_roles = array_intersect( array_values( $profileuser->roles ), array_keys( get_editable_roles() ) );
$user_role  = array_shift( $user_roles );

// print the full list of roles with the primary one selected.
wp_dropdown_roles($user_role);

// print the 'no role' option. Make it selected if the user has no role yet.
if ( $user_role )
	echo '<option value="">' . __('&mdash; No role for this site &mdash;') . '</option>';
else
	echo '<option value="" selected="selected">' . __('&mdash; No role for this site &mdash;') . '</option>';
?>
</select></td></tr>
***REMOVED*** endif; //!IS_PROFILE_PAGE

if ( is_multisite() && is_network_admin() && ! IS_PROFILE_PAGE && current_user_can( 'manage_network_options' ) && !isset($super_admins) ) { ?>
<tr><th>***REMOVED*** _e('Super Admin'); ?></th>
<td>
***REMOVED*** if ( $profileuser->user_email != get_site_option( 'admin_email' ) || ! is_super_admin( $profileuser->ID ) ) : ?>
<p><label><input type="checkbox" id="super_admin" name="super_admin"***REMOVED*** checked( is_super_admin( $profileuser->ID ) ); ?> /> ***REMOVED*** _e( 'Grant this user super admin privileges for the Network.' ); ?></label></p>
***REMOVED*** else : ?>
<p>***REMOVED*** _e( 'Super admin privileges cannot be removed because this user has the network admin email.' ); ?></p>
***REMOVED*** endif; ?>
</td></tr>
***REMOVED*** } ?>

<tr>
	<th><label for="first_name">***REMOVED*** _e('First Name') ?></label></th>
	<td><input type="text" name="first_name" id="first_name" value="***REMOVED*** echo esc_attr($profileuser->first_name) ?>" class="regular-text" /></td>
</tr>

<tr>
	<th><label for="last_name">***REMOVED*** _e('Last Name') ?></label></th>
	<td><input type="text" name="last_name" id="last_name" value="***REMOVED*** echo esc_attr($profileuser->last_name) ?>" class="regular-text" /></td>
</tr>

<tr>
	<th><label for="nickname">***REMOVED*** _e('Nickname'); ?> <span class="description">***REMOVED*** _e('(required)'); ?></span></label></th>
	<td><input type="text" name="nickname" id="nickname" value="***REMOVED*** echo esc_attr($profileuser->nickname) ?>" class="regular-text" /></td>
</tr>

<tr>
	<th><label for="display_name">***REMOVED*** _e('Display name publicly as') ?></label></th>
	<td>
		<select name="display_name" id="display_name">
		***REMOVED***
			$public_display = array();
			$public_display['display_nickname']  = $profileuser->nickname;
			$public_display['display_username']  = $profileuser->user_login;

			if ( !empty($profileuser->first_name) )
				$public_display['display_firstname'] = $profileuser->first_name;

			if ( !empty($profileuser->last_name) )
				$public_display['display_lastname'] = $profileuser->last_name;

			if ( !empty($profileuser->first_name) && !empty($profileuser->last_name) ) {
				$public_display['display_firstlast'] = $profileuser->first_name . ' ' . $profileuser->last_name;
				$public_display['display_lastfirst'] = $profileuser->last_name . ' ' . $profileuser->first_name;
			}

			if ( !in_array( $profileuser->display_name, $public_display ) ) // Only add this if it isn't duplicated elsewhere
				$public_display = array( 'display_displayname' => $profileuser->display_name ) + $public_display;

			$public_display = array_map( 'trim', $public_display );
			$public_display = array_unique( $public_display );

			foreach ( $public_display as $id => $item ) {
		?>
			<option ***REMOVED*** selected( $profileuser->display_name, $item ); ?>>***REMOVED*** echo $item; ?></option>
		***REMOVED***
			}
		?>
		</select>
	</td>
</tr>
</table>

<h3>***REMOVED*** _e('Contact Info') ?></h3>

<table class="form-table">
<tr>
	<th><label for="email">***REMOVED*** _e('E-mail'); ?> <span class="description">***REMOVED*** _e('(required)'); ?></span></label></th>
	<td><input type="text" name="email" id="email" value="***REMOVED*** echo esc_attr($profileuser->user_email) ?>" class="regular-text" />
	***REMOVED***
	$new_email = get_option( $current_user->ID . '_new_email' );
	if ( $new_email && $new_email['newemail'] != $current_user->user_email && $profileuser->ID == $current_user->ID ) : ?>
	<div class="updated inline">
	<p>***REMOVED*** printf( __('There is a pending change of your e-mail to <code>%1$s</code>. <a href="%2$s">Cancel</a>'), $new_email['newemail'], esc_url( self_admin_url( 'profile.php?dismiss=' . $current_user->ID . '_new_email' ) ) ); ?></p>
	</div>
	***REMOVED*** endif; ?>
	</td>
</tr>

<tr>
	<th><label for="url">***REMOVED*** _e('Website') ?></label></th>
	<td><input type="text" name="url" id="url" value="***REMOVED*** echo esc_attr($profileuser->user_url) ?>" class="regular-text code" /></td>
</tr>

***REMOVED***
	foreach ( wp_get_user_contact_methods( $profileuser ) as $name => $desc ) {
?>
<tr>
	<th><label for="***REMOVED*** echo $name; ?>">***REMOVED*** echo apply_filters('user_'.$name.'_label', $desc); ?></label></th>
	<td><input type="text" name="***REMOVED*** echo $name; ?>" id="***REMOVED*** echo $name; ?>" value="***REMOVED*** echo esc_attr($profileuser->$name) ?>" class="regular-text" /></td>
</tr>
***REMOVED***
	}
?>
</table>

<h3>***REMOVED*** IS_PROFILE_PAGE ? _e('About Yourself') : _e('About the user'); ?></h3>

<table class="form-table">
<tr>
	<th><label for="description">***REMOVED*** _e('Biographical Info'); ?></label></th>
	<td><textarea name="description" id="description" rows="5" cols="30">***REMOVED*** echo $profileuser->description; // textarea_escaped ?></textarea><br />
	<span class="description">***REMOVED*** _e('Share a little biographical information to fill out your profile. This may be shown publicly.'); ?></span></td>
</tr>

***REMOVED***
$show_password_fields = apply_filters('show_password_fields', true, $profileuser);
if ( $show_password_fields ) :
?>
<tr id="password">
	<th><label for="pass1">***REMOVED*** _e('New Password'); ?></label></th>
	<td>
		<input class="hidden" value=" " /><!-- #24364 workaround -->
		<input type="password" name="pass1" id="pass1" size="16" value="" autocomplete="off" /> <span class="description">***REMOVED*** _e("If you would like to change the password type a new one. Otherwise leave this blank."); ?></span>
	</td>
</tr>
<tr>
	<th scope="row"><label for="pass2">***REMOVED*** _e('Repeat New Password'); ?></label></th>
	<td>
	<input name="pass2" type="password" id="pass2" size="16" value="" autocomplete="off" /> <span class="description" for="pass2">***REMOVED*** _e("Type your new password again."); ?></span>
	<br />
	<div id="pass-strength-result">***REMOVED*** _e('Strength indicator'); ?></div>
	<p class="description indicator-hint">***REMOVED*** _e('Hint: The password should be at least seven characters long. To make it stronger, use upper and lower case letters, numbers and symbols like ! " ? $ % ^ &amp; ).'); ?></p>
	</td>
</tr>
***REMOVED*** endif; ?>
</table>

***REMOVED***
	if ( IS_PROFILE_PAGE )
		do_action( 'show_user_profile', $profileuser );
	else
		do_action( 'edit_user_profile', $profileuser );
?>

***REMOVED*** if ( count( $profileuser->caps ) > count( $profileuser->roles ) && apply_filters( 'additional_capabilities_display', true, $profileuser ) ) : ?>
<h3>***REMOVED*** _e( 'Additional Capabilities' ); ?></h3>
<table class="form-table">
<tr>
	<th scope="row">***REMOVED*** _e( 'Capabilities' ); ?></th>
	<td>
***REMOVED***
	$output = '';
	foreach ( $profileuser->caps as $cap => $value ) {
		if ( ! $wp_roles->is_role( $cap ) ) {
			if ( '' != $output )
				$output .= ', ';
			$output .= $value ? $cap : sprintf( __( 'Denied: %s' ), $cap );
		}
	}
	echo $output;
?>
	</td>
</tr>
</table>
***REMOVED*** endif; ?>

<input type="hidden" name="action" value="update" />
<input type="hidden" name="user_id" id="user_id" value="***REMOVED*** echo esc_attr($user_id); ?>" />

***REMOVED*** submit_button( IS_PROFILE_PAGE ? __('Update Profile') : __('Update User') ); ?>

</form>
</div>
***REMOVED***
break;
}
?>
<script type="text/javascript">
	if (window.location.hash == '#password') {
		document.getElementById('pass1').focus();
	}
</script>
***REMOVED***
include( ABSPATH . 'wp-admin/admin-footer.php');
