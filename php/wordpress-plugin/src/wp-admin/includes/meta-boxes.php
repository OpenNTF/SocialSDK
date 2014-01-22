***REMOVED***

// -- Post related Meta Boxes

/**
 * Display post submit form fields.
 *
 * @since 2.7.0
 *
 * @param object $post
 */
function post_submit_meta_box($post, $args = array() ) {
	global $action;

	$post_type = $post->post_type;
	$post_type_object = get_post_type_object($post_type);
	$can_publish = current_user_can($post_type_object->cap->publish_posts);
?>
<div class="submitbox" id="submitpost">

<div id="minor-publishing">

***REMOVED*** // Hidden submit button early on so that the browser chooses the right button when form is submitted with Return key ?>
<div style="display:none;">
***REMOVED*** submit_button( __( 'Save' ), 'button', 'save' ); ?>
</div>

<div id="minor-publishing-actions">
<div id="save-action">
***REMOVED*** if ( 'publish' != $post->post_status && 'future' != $post->post_status && 'pending' != $post->post_status ) { ?>
<input ***REMOVED*** if ( 'private' == $post->post_status ) { ?>style="display:none"***REMOVED*** } ?> type="submit" name="save" id="save-post" value="***REMOVED*** esc_attr_e('Save Draft'); ?>" class="button" />
***REMOVED*** } elseif ( 'pending' == $post->post_status && $can_publish ) { ?>
<input type="submit" name="save" id="save-post" value="***REMOVED*** esc_attr_e('Save as Pending'); ?>" class="button" />
***REMOVED*** } ?>
<span class="spinner"></span>
</div>
***REMOVED*** if ( $post_type_object->public ) : ?>
<div id="preview-action">
***REMOVED***
if ( 'publish' == $post->post_status ) {
	$preview_link = esc_url( get_permalink( $post->ID ) );
	$preview_button = __( 'Preview Changes' );
} else {
	$preview_link = set_url_scheme( get_permalink( $post->ID ) );
	$preview_link = esc_url( apply_filters( 'preview_post_link', add_query_arg( 'preview', 'true', $preview_link ) ) );
	$preview_button = __( 'Preview' );
}
?>
<a class="preview button" href="***REMOVED*** echo $preview_link; ?>" target="wp-preview" id="post-preview">***REMOVED*** echo $preview_button; ?></a>
<input type="hidden" name="wp-preview" id="wp-preview" value="" />
</div>
***REMOVED*** endif; // public post type ?>
<div class="clear"></div>
</div><!-- #minor-publishing-actions -->

<div id="misc-publishing-actions">

<div class="misc-pub-section misc-pub-post-status"><label for="post_status">***REMOVED*** _e('Status:') ?></label>
<span id="post-status-display">
***REMOVED***
switch ( $post->post_status ) {
	case 'private':
		_e('Privately Published');
		break;
	case 'publish':
		_e('Published');
		break;
	case 'future':
		_e('Scheduled');
		break;
	case 'pending':
		_e('Pending Review');
		break;
	case 'draft':
	case 'auto-draft':
		_e('Draft');
		break;
}
?>
</span>
***REMOVED*** if ( 'publish' == $post->post_status || 'private' == $post->post_status || $can_publish ) { ?>
<a href="#post_status" ***REMOVED*** if ( 'private' == $post->post_status ) { ?>style="display:none;" ***REMOVED*** } ?>class="edit-post-status hide-if-no-js">***REMOVED*** _e('Edit') ?></a>

<div id="post-status-select" class="hide-if-js">
<input type="hidden" name="hidden_post_status" id="hidden_post_status" value="***REMOVED*** echo esc_attr( ('auto-draft' == $post->post_status ) ? 'draft' : $post->post_status); ?>" />
<select name='post_status' id='post_status'>
***REMOVED*** if ( 'publish' == $post->post_status ) : ?>
<option***REMOVED*** selected( $post->post_status, 'publish' ); ?> value='publish'>***REMOVED*** _e('Published') ?></option>
***REMOVED*** elseif ( 'private' == $post->post_status ) : ?>
<option***REMOVED*** selected( $post->post_status, 'private' ); ?> value='publish'>***REMOVED*** _e('Privately Published') ?></option>
***REMOVED*** elseif ( 'future' == $post->post_status ) : ?>
<option***REMOVED*** selected( $post->post_status, 'future' ); ?> value='future'>***REMOVED*** _e('Scheduled') ?></option>
***REMOVED*** endif; ?>
<option***REMOVED*** selected( $post->post_status, 'pending' ); ?> value='pending'>***REMOVED*** _e('Pending Review') ?></option>
***REMOVED*** if ( 'auto-draft' == $post->post_status ) : ?>
<option***REMOVED*** selected( $post->post_status, 'auto-draft' ); ?> value='draft'>***REMOVED*** _e('Draft') ?></option>
***REMOVED*** else : ?>
<option***REMOVED*** selected( $post->post_status, 'draft' ); ?> value='draft'>***REMOVED*** _e('Draft') ?></option>
***REMOVED*** endif; ?>
</select>
 <a href="#post_status" class="save-post-status hide-if-no-js button">***REMOVED*** _e('OK'); ?></a>
 <a href="#post_status" class="cancel-post-status hide-if-no-js">***REMOVED*** _e('Cancel'); ?></a>
</div>

***REMOVED*** } ?>
</div><!-- .misc-pub-section -->

<div class="misc-pub-section misc-pub-visibility" id="visibility">
***REMOVED*** _e('Visibility:'); ?> <span id="post-visibility-display">***REMOVED***

if ( 'private' == $post->post_status ) {
	$post->post_password = '';
	$visibility = 'private';
	$visibility_trans = __('Private');
} elseif ( !empty( $post->post_password ) ) {
	$visibility = 'password';
	$visibility_trans = __('Password protected');
} elseif ( $post_type == 'post' && is_sticky( $post->ID ) ) {
	$visibility = 'public';
	$visibility_trans = __('Public, Sticky');
} else {
	$visibility = 'public';
	$visibility_trans = __('Public');
}

echo esc_html( $visibility_trans ); ?></span>
***REMOVED*** if ( $can_publish ) { ?>
<a href="#visibility" class="edit-visibility hide-if-no-js">***REMOVED*** _e('Edit'); ?></a>

<div id="post-visibility-select" class="hide-if-js">
<input type="hidden" name="hidden_post_password" id="hidden-post-password" value="***REMOVED*** echo esc_attr($post->post_password); ?>" />
***REMOVED*** if ($post_type == 'post'): ?>
<input type="checkbox" style="display:none" name="hidden_post_sticky" id="hidden-post-sticky" value="sticky" ***REMOVED*** checked(is_sticky($post->ID)); ?> />
***REMOVED*** endif; ?>
<input type="hidden" name="hidden_post_visibility" id="hidden-post-visibility" value="***REMOVED*** echo esc_attr( $visibility ); ?>" />
<input type="radio" name="visibility" id="visibility-radio-public" value="public" ***REMOVED*** checked( $visibility, 'public' ); ?> /> <label for="visibility-radio-public" class="selectit">***REMOVED*** _e('Public'); ?></label><br />
***REMOVED*** if ( $post_type == 'post' && current_user_can( 'edit_others_posts' ) ) : ?>
<span id="sticky-span"><input id="sticky" name="sticky" type="checkbox" value="sticky" ***REMOVED*** checked( is_sticky( $post->ID ) ); ?> /> <label for="sticky" class="selectit">***REMOVED*** _e( 'Stick this post to the front page' ); ?></label><br /></span>
***REMOVED*** endif; ?>
<input type="radio" name="visibility" id="visibility-radio-password" value="password" ***REMOVED*** checked( $visibility, 'password' ); ?> /> <label for="visibility-radio-password" class="selectit">***REMOVED*** _e('Password protected'); ?></label><br />
<span id="password-span"><label for="post_password">***REMOVED*** _e('Password:'); ?></label> <input type="text" name="post_password" id="post_password" value="***REMOVED*** echo esc_attr($post->post_password); ?>"  maxlength="20" /><br /></span>
<input type="radio" name="visibility" id="visibility-radio-private" value="private" ***REMOVED*** checked( $visibility, 'private' ); ?> /> <label for="visibility-radio-private" class="selectit">***REMOVED*** _e('Private'); ?></label><br />

<p>
 <a href="#visibility" class="save-post-visibility hide-if-no-js button">***REMOVED*** _e('OK'); ?></a>
 <a href="#visibility" class="cancel-post-visibility hide-if-no-js">***REMOVED*** _e('Cancel'); ?></a>
</p>
</div>
***REMOVED*** } ?>

</div><!-- .misc-pub-section -->

***REMOVED***
// translators: Publish box date format, see http://php.net/date
$datef = __( 'M j, Y @ G:i' );
if ( 0 != $post->ID ) {
	if ( 'future' == $post->post_status ) { // scheduled for publishing at a future date
		$stamp = __('Scheduled for: <b>%1$s</b>');
	} else if ( 'publish' == $post->post_status || 'private' == $post->post_status ) { // already published
		$stamp = __('Published on: <b>%1$s</b>');
	} else if ( '0000-00-00 00:00:00' == $post->post_date_gmt ) { // draft, 1 or more saves, no date specified
		$stamp = __('Publish <b>immediately</b>');
	} else if ( time() < strtotime( $post->post_date_gmt . ' +0000' ) ) { // draft, 1 or more saves, future date specified
		$stamp = __('Schedule for: <b>%1$s</b>');
	} else { // draft, 1 or more saves, date specified
		$stamp = __('Publish on: <b>%1$s</b>');
	}
	$date = date_i18n( $datef, strtotime( $post->post_date ) );
} else { // draft (no saves, and thus no date specified)
	$stamp = __('Publish <b>immediately</b>');
	$date = date_i18n( $datef, strtotime( current_time('mysql') ) );
}

if ( ! empty( $args['args']['revisions_count'] ) ) :
	$revisions_to_keep = wp_revisions_to_keep( $post );
?>
<div class="misc-pub-section misc-pub-revisions">
***REMOVED***
	if ( $revisions_to_keep > 0 && $revisions_to_keep <= $args['args']['revisions_count'] ) {
		echo '<span title="' . esc_attr( sprintf( __( 'Your site is configured to keep only the last %s revisions.' ),
			number_format_i18n( $revisions_to_keep ) ) ) . '">';
		printf( __( 'Revisions: %s' ), '<b>' . number_format_i18n( $args['args']['revisions_count'] ) . '+</b>' );
		echo '</span>';
	} else {
		printf( __( 'Revisions: %s' ), '<b>' . number_format_i18n( $args['args']['revisions_count'] ) . '</b>' );
	}
?>
	<a class="hide-if-no-js" href="***REMOVED*** echo esc_url( get_edit_post_link( $args['args']['revision_id'] ) ); ?>">***REMOVED*** _ex( 'Browse', 'revisions' ); ?></a>
</div>
***REMOVED*** endif;

if ( $can_publish ) : // Contributors don't get to choose the date of publish ?>
<div class="misc-pub-section curtime misc-pub-curtime">
	<span id="timestamp">
	***REMOVED*** printf($stamp, $date); ?></span>
	<a href="#edit_timestamp" class="edit-timestamp hide-if-no-js">***REMOVED*** _e('Edit') ?></a>
	<div id="timestampdiv" class="hide-if-js">***REMOVED*** touch_time(($action == 'edit'), 1); ?></div>
</div>***REMOVED*** // /misc-pub-section ?>
***REMOVED*** endif; ?>

***REMOVED*** do_action('post_submitbox_misc_actions'); ?>
</div>
<div class="clear"></div>
</div>

<div id="major-publishing-actions">
***REMOVED*** do_action('post_submitbox_start'); ?>
<div id="delete-action">
***REMOVED***
if ( current_user_can( "delete_post", $post->ID ) ) {
	if ( !EMPTY_TRASH_DAYS )
		$delete_text = __('Delete Permanently');
	else
		$delete_text = __('Move to Trash');
	?>
<a class="submitdelete deletion" href="***REMOVED*** echo get_delete_post_link($post->ID); ?>">***REMOVED*** echo $delete_text; ?></a>***REMOVED***
} ?>
</div>

<div id="publishing-action">
<span class="spinner"></span>
***REMOVED***
if ( !in_array( $post->post_status, array('publish', 'future', 'private') ) || 0 == $post->ID ) {
	if ( $can_publish ) :
		if ( !empty($post->post_date_gmt) && time() < strtotime( $post->post_date_gmt . ' +0000' ) ) : ?>
		<input name="original_publish" type="hidden" id="original_publish" value="***REMOVED*** esc_attr_e('Schedule') ?>" />
		***REMOVED*** submit_button( __( 'Schedule' ), 'primary button-large', 'publish', false, array( 'accesskey' => 'p' ) ); ?>
***REMOVED***	else : ?>
		<input name="original_publish" type="hidden" id="original_publish" value="***REMOVED*** esc_attr_e('Publish') ?>" />
		***REMOVED*** submit_button( __( 'Publish' ), 'primary button-large', 'publish', false, array( 'accesskey' => 'p' ) ); ?>
***REMOVED***	endif;
	else : ?>
		<input name="original_publish" type="hidden" id="original_publish" value="***REMOVED*** esc_attr_e('Submit for Review') ?>" />
		***REMOVED*** submit_button( __( 'Submit for Review' ), 'primary button-large', 'publish', false, array( 'accesskey' => 'p' ) ); ?>
***REMOVED***
	endif;
} else { ?>
		<input name="original_publish" type="hidden" id="original_publish" value="***REMOVED*** esc_attr_e('Update') ?>" />
		<input name="save" type="submit" class="button button-primary button-large" id="publish" accesskey="p" value="***REMOVED*** esc_attr_e('Update') ?>" />
***REMOVED***
} ?>
</div>
<div class="clear"></div>
</div>
</div>

***REMOVED***
}

/**
 * Display attachment submit form fields.
 *
 * @since 3.5.0
 *
 * @param object $post
 */
function attachment_submit_meta_box( $post ) {
	global $action;

	$post_type = $post->post_type;
	$post_type_object = get_post_type_object($post_type);
	$can_publish = current_user_can($post_type_object->cap->publish_posts);
?>
<div class="submitbox" id="submitpost">

<div id="minor-publishing">

***REMOVED*** // Hidden submit button early on so that the browser chooses the right button when form is submitted with Return key ?>
<div style="display:none;">
***REMOVED*** submit_button( __( 'Save' ), 'button', 'save' ); ?>
</div>


<div id="misc-publishing-actions">
	***REMOVED***
	// translators: Publish box date format, see http://php.net/date
	$datef = __( 'M j, Y @ G:i' );
	$stamp = __('Uploaded on: <b>%1$s</b>');
	$date = date_i18n( $datef, strtotime( $post->post_date ) );
	?>
	<div class="misc-pub-section curtime misc-pub-curtime">
		<span id="timestamp">***REMOVED*** printf($stamp, $date); ?></span>
	</div><!-- .misc-pub-section -->

	***REMOVED*** do_action('attachment_submitbox_misc_actions'); ?>
</div><!-- #misc-publishing-actions -->
<div class="clear"></div>
</div><!-- #minor-publishing -->

<div id="major-publishing-actions">
	<div id="delete-action">
	***REMOVED***
	if ( current_user_can( 'delete_post', $post->ID ) )
		if ( EMPTY_TRASH_DAYS && MEDIA_TRASH ) {
			echo "<a class='submitdelete deletion' href='" . get_delete_post_link( $post->ID ) . "'>" . __( 'Trash' ) . "</a>";
		} else {
			$delete_ays = ! MEDIA_TRASH ? " onclick='return showNotice.warn();'" : '';
			echo  "<a class='submitdelete deletion'$delete_ays href='" . get_delete_post_link( $post->ID, null, true ) . "'>" . __( 'Delete Permanently' ) . "</a>";
		}
	?>
	</div>

	<div id="publishing-action">
		<span class="spinner"></span>
		<input name="original_publish" type="hidden" id="original_publish" value="***REMOVED*** esc_attr_e('Update') ?>" />
		<input name="save" type="submit" class="button-primary button-large" id="publish" accesskey="p" value="***REMOVED*** esc_attr_e('Update') ?>" />
	</div>
	<div class="clear"></div>
</div><!-- #major-publishing-actions -->

</div>

***REMOVED***
}

/**
 * Display post format form elements.
 *
 * @since 3.1.0
 *
 * @param object $post
 */
function post_format_meta_box( $post, $box ) {
	if ( current_theme_supports( 'post-formats' ) && post_type_supports( $post->post_type, 'post-formats' ) ) :
	$post_formats = get_theme_support( 'post-formats' );

	if ( is_array( $post_formats[0] ) ) :
		$post_format = get_post_format( $post->ID );
		if ( !$post_format )
			$post_format = '0';
		// Add in the current one if it isn't there yet, in case the current theme doesn't support it
		if ( $post_format && !in_array( $post_format, $post_formats[0] ) )
			$post_formats[0][] = $post_format;
	?>
	<div id="post-formats-select">
		<input type="radio" name="post_format" class="post-format" id="post-format-0" value="0" ***REMOVED*** checked( $post_format, '0' ); ?> /> <label for="post-format-0" class="post-format-icon post-format-standard">***REMOVED*** echo get_post_format_string( 'standard' ); ?></label>
		***REMOVED*** foreach ( $post_formats[0] as $format ) : ?>
		<br /><input type="radio" name="post_format" class="post-format" id="post-format-***REMOVED*** echo esc_attr( $format ); ?>" value="***REMOVED*** echo esc_attr( $format ); ?>" ***REMOVED*** checked( $post_format, $format ); ?> /> <label for="post-format-***REMOVED*** echo esc_attr( $format ); ?>" class="post-format-icon post-format-***REMOVED*** echo esc_attr( $format ); ?>">***REMOVED*** echo esc_html( get_post_format_string( $format ) ); ?></label>
		***REMOVED*** endforeach; ?><br />
	</div>
	***REMOVED*** endif; endif;
}

/**
 * Display post tags form fields.
 *
 * @since 2.6.0
 *
 * @param object $post
 */
function post_tags_meta_box($post, $box) {
	$defaults = array('taxonomy' => 'post_tag');
	if ( !isset($box['args']) || !is_array($box['args']) )
		$args = array();
	else
		$args = $box['args'];
	extract( wp_parse_args($args, $defaults), EXTR_SKIP );
	$tax_name = esc_attr($taxonomy);
	$taxonomy = get_taxonomy($taxonomy);
	$user_can_assign_terms = current_user_can( $taxonomy->cap->assign_terms );
	$comma = _x( ',', 'tag delimiter' );
?>
<div class="tagsdiv" id="***REMOVED*** echo $tax_name; ?>">
	<div class="jaxtag">
	<div class="nojs-tags hide-if-js">
	<p>***REMOVED*** echo $taxonomy->labels->add_or_remove_items; ?></p>
	<textarea name="***REMOVED*** echo "tax_input[$tax_name]"; ?>" rows="3" cols="20" class="the-tags" id="tax-input-***REMOVED*** echo $tax_name; ?>" ***REMOVED*** disabled( ! $user_can_assign_terms ); ?>>***REMOVED*** echo str_replace( ',', $comma . ' ', get_terms_to_edit( $post->ID, $tax_name ) ); // textarea_escaped by esc_attr() ?></textarea></div>
 	***REMOVED*** if ( $user_can_assign_terms ) : ?>
	<div class="ajaxtag hide-if-no-js">
		<label class="screen-reader-text" for="new-tag-***REMOVED*** echo $tax_name; ?>">***REMOVED*** echo $box['title']; ?></label>
		<div class="taghint">***REMOVED*** echo $taxonomy->labels->add_new_item; ?></div>
		<p><input type="text" id="new-tag-***REMOVED*** echo $tax_name; ?>" name="newtag[***REMOVED*** echo $tax_name; ?>]" class="newtag form-input-tip" size="16" autocomplete="off" value="" />
		<input type="button" class="button tagadd" value="***REMOVED*** esc_attr_e('Add'); ?>" /></p>
	</div>
	<p class="howto">***REMOVED*** echo $taxonomy->labels->separate_items_with_commas; ?></p>
	***REMOVED*** endif; ?>
	</div>
	<div class="tagchecklist"></div>
</div>
***REMOVED*** if ( $user_can_assign_terms ) : ?>
<p class="hide-if-no-js"><a href="#titlediv" class="tagcloud-link" id="link-***REMOVED*** echo $tax_name; ?>">***REMOVED*** echo $taxonomy->labels->choose_from_most_used; ?></a></p>
***REMOVED*** endif; ?>
***REMOVED***
}

/**
 * Display post categories form fields.
 *
 * @since 2.6.0
 *
 * @param object $post
 */
function post_categories_meta_box( $post, $box ) {
	$defaults = array('taxonomy' => 'category');
	if ( !isset($box['args']) || !is_array($box['args']) )
		$args = array();
	else
		$args = $box['args'];
	extract( wp_parse_args($args, $defaults), EXTR_SKIP );
	$tax = get_taxonomy($taxonomy);

	?>
	<div id="taxonomy-***REMOVED*** echo $taxonomy; ?>" class="categorydiv">
		<ul id="***REMOVED*** echo $taxonomy; ?>-tabs" class="category-tabs">
			<li class="tabs"><a href="#***REMOVED*** echo $taxonomy; ?>-all">***REMOVED*** echo $tax->labels->all_items; ?></a></li>
			<li class="hide-if-no-js"><a href="#***REMOVED*** echo $taxonomy; ?>-pop">***REMOVED*** _e( 'Most Used' ); ?></a></li>
		</ul>

		<div id="***REMOVED*** echo $taxonomy; ?>-pop" class="tabs-panel" style="display: none;">
			<ul id="***REMOVED*** echo $taxonomy; ?>checklist-pop" class="categorychecklist form-no-clear" >
				***REMOVED*** $popular_ids = wp_popular_terms_checklist($taxonomy); ?>
			</ul>
		</div>

		<div id="***REMOVED*** echo $taxonomy; ?>-all" class="tabs-panel">
			***REMOVED***
            $name = ( $taxonomy == 'category' ) ? 'post_category' : 'tax_input[' . $taxonomy . ']';
            echo "<input type='hidden' name='{$name}[]' value='0' />"; // Allows for an empty term set to be sent. 0 is an invalid Term ID and will be ignored by empty() checks.
            ?>
			<ul id="***REMOVED*** echo $taxonomy; ?>checklist" data-wp-lists="list:***REMOVED*** echo $taxonomy?>" class="categorychecklist form-no-clear">
				***REMOVED*** wp_terms_checklist($post->ID, array( 'taxonomy' => $taxonomy, 'popular_cats' => $popular_ids ) ) ?>
			</ul>
		</div>
	***REMOVED*** if ( current_user_can($tax->cap->edit_terms) ) : ?>
			<div id="***REMOVED*** echo $taxonomy; ?>-adder" class="wp-hidden-children">
				<h4>
					<a id="***REMOVED*** echo $taxonomy; ?>-add-toggle" href="#***REMOVED*** echo $taxonomy; ?>-add" class="hide-if-no-js">
						***REMOVED***
							/* translators: %s: add new taxonomy label */
							printf( __( '+ %s' ), $tax->labels->add_new_item );
						?>
					</a>
				</h4>
				<p id="***REMOVED*** echo $taxonomy; ?>-add" class="category-add wp-hidden-child">
					<label class="screen-reader-text" for="new***REMOVED*** echo $taxonomy; ?>">***REMOVED*** echo $tax->labels->add_new_item; ?></label>
					<input type="text" name="new***REMOVED*** echo $taxonomy; ?>" id="new***REMOVED*** echo $taxonomy; ?>" class="form-required form-input-tip" value="***REMOVED*** echo esc_attr( $tax->labels->new_item_name ); ?>" aria-required="true"/>
					<label class="screen-reader-text" for="new***REMOVED*** echo $taxonomy; ?>_parent">
						***REMOVED*** echo $tax->labels->parent_item_colon; ?>
					</label>
					***REMOVED*** wp_dropdown_categories( array( 'taxonomy' => $taxonomy, 'hide_empty' => 0, 'name' => 'new'.$taxonomy.'_parent', 'orderby' => 'name', 'hierarchical' => 1, 'show_option_none' => '&mdash; ' . $tax->labels->parent_item . ' &mdash;' ) ); ?>
					<input type="button" id="***REMOVED*** echo $taxonomy; ?>-add-submit" data-wp-lists="add:***REMOVED*** echo $taxonomy ?>checklist:***REMOVED*** echo $taxonomy ?>-add" class="button category-add-submit" value="***REMOVED*** echo esc_attr( $tax->labels->add_new_item ); ?>" />
					***REMOVED*** wp_nonce_field( 'add-'.$taxonomy, '_ajax_nonce-add-'.$taxonomy, false ); ?>
					<span id="***REMOVED*** echo $taxonomy; ?>-ajax-response"></span>
				</p>
			</div>
		***REMOVED*** endif; ?>
	</div>
	***REMOVED***
}

/**
 * Display post excerpt form fields.
 *
 * @since 2.6.0
 *
 * @param object $post
 */
function post_excerpt_meta_box($post) {
?>
<label class="screen-reader-text" for="excerpt">***REMOVED*** _e('Excerpt') ?></label><textarea rows="1" cols="40" name="excerpt" id="excerpt">***REMOVED*** echo $post->post_excerpt; // textarea_escaped ?></textarea>
<p>***REMOVED*** _e('Excerpts are optional hand-crafted summaries of your content that can be used in your theme. <a href="http://codex.wordpress.org/Excerpt" target="_blank">Learn more about manual excerpts.</a>'); ?></p>
***REMOVED***
}

/**
 * Display trackback links form fields.
 *
 * @since 2.6.0
 *
 * @param object $post
 */
function post_trackback_meta_box($post) {
	$form_trackback = '<input type="text" name="trackback_url" id="trackback_url" class="code" value="'. esc_attr( str_replace("\n", ' ', $post->to_ping) ) .'" />';
	if ('' != $post->pinged) {
		$pings = '<p>'. __('Already pinged:') . '</p><ul>';
		$already_pinged = explode("\n", trim($post->pinged));
		foreach ($already_pinged as $pinged_url) {
			$pings .= "\n\t<li>" . esc_html($pinged_url) . "</li>";
		}
		$pings .= '</ul>';
	}

?>
<p><label for="trackback_url">***REMOVED*** _e('Send trackbacks to:'); ?></label> ***REMOVED*** echo $form_trackback; ?><br /> (***REMOVED*** _e('Separate multiple URLs with spaces'); ?>)</p>
<p>***REMOVED*** _e('Trackbacks are a way to notify legacy blog systems that you&#8217;ve linked to them. If you link other WordPress sites they&#8217;ll be notified automatically using <a href="http://codex.wordpress.org/Introduction_to_Blogging#Managing_Comments" target="_blank">pingbacks</a>, no other action necessary.'); ?></p>
***REMOVED***
if ( ! empty($pings) )
	echo $pings;
}

/**
 * Display custom fields form fields.
 *
 * @since 2.6.0
 *
 * @param object $post
 */
function post_custom_meta_box($post) {
?>
<div id="postcustomstuff">
<div id="ajax-response"></div>
***REMOVED***
$metadata = has_meta($post->ID);
foreach ( $metadata as $key => $value ) {
	if ( is_protected_meta( $metadata[ $key ][ 'meta_key' ], 'post' ) || ! current_user_can( 'edit_post_meta', $post->ID, $metadata[ $key ][ 'meta_key' ] ) )
		unset( $metadata[ $key ] );
}
list_meta( $metadata );
meta_form( $post ); ?>
</div>
<p>***REMOVED*** _e('Custom fields can be used to add extra metadata to a post that you can <a href="http://codex.wordpress.org/Using_Custom_Fields" target="_blank">use in your theme</a>.'); ?></p>
***REMOVED***
}

/**
 * Display comments status form fields.
 *
 * @since 2.6.0
 *
 * @param object $post
 */
function post_comment_status_meta_box($post) {
?>
<input name="advanced_view" type="hidden" value="1" />
<p class="meta-options">
	<label for="comment_status" class="selectit"><input name="comment_status" type="checkbox" id="comment_status" value="open" ***REMOVED*** checked($post->comment_status, 'open'); ?> /> ***REMOVED*** _e( 'Allow comments.' ) ?></label><br />
	<label for="ping_status" class="selectit"><input name="ping_status" type="checkbox" id="ping_status" value="open" ***REMOVED*** checked($post->ping_status, 'open'); ?> /> ***REMOVED*** printf( __( 'Allow <a href="%s" target="_blank">trackbacks and pingbacks</a> on this page.' ), __( 'http://codex.wordpress.org/Introduction_to_Blogging#Managing_Comments' ) ); ?></label>
	***REMOVED*** do_action('post_comment_status_meta_box-options', $post); ?>
</p>
***REMOVED***
}

/**
 * Display comments for post table header
 *
 * @since 3.0.0
 *
 * @param array $result table header rows
 * @return array
 */
function post_comment_meta_box_thead($result) {
	unset($result['cb'], $result['response']);
	return $result;
}

/**
 * Display comments for post.
 *
 * @since 2.8.0
 *
 * @param object $post
 */
function post_comment_meta_box( $post ) {
	global $wpdb;

	wp_nonce_field( 'get-comments', 'add_comment_nonce', false );
	?>
	<p class="hide-if-no-js" id="add-new-comment"><a href="#commentstatusdiv" onclick="commentReply.addcomment(***REMOVED*** echo $post->ID; ?>);return false;">***REMOVED*** _e('Add comment'); ?></a></p>
	***REMOVED***

	$total = get_comments( array( 'post_id' => $post->ID, 'number' => 1, 'count' => true ) );
	$wp_list_table = _get_list_table('WP_Post_Comments_List_Table');
	$wp_list_table->display( true );

	if ( 1 > $total ) {
		echo '<p id="no-comments">' . __('No comments yet.') . '</p>';
	} else {
		$hidden = get_hidden_meta_boxes( get_current_screen() );
		if ( ! in_array('commentsdiv', $hidden) ) {
			?>
			<script type="text/javascript">jQuery(document).ready(function(){commentsBox.get(***REMOVED*** echo $total; ?>, 10);});</script>
			***REMOVED***
		}

		?>
		<p class="hide-if-no-js" id="show-comments"><a href="#commentstatusdiv" onclick="commentsBox.get(***REMOVED*** echo $total; ?>);return false;">***REMOVED*** _e('Show comments'); ?></a> <span class="spinner"></span></p>
		***REMOVED***
	}

	wp_comment_trashnotice();
}

/**
 * Display slug form fields.
 *
 * @since 2.6.0
 *
 * @param object $post
 */
function post_slug_meta_box($post) {
?>
<label class="screen-reader-text" for="post_name">***REMOVED*** _e('Slug') ?></label><input name="post_name" type="text" size="13" id="post_name" value="***REMOVED*** echo esc_attr( apply_filters('editable_slug', $post->post_name) ); ?>" />
***REMOVED***
}

/**
 * Display form field with list of authors.
 *
 * @since 2.6.0
 *
 * @param object $post
 */
function post_author_meta_box($post) {
	global $user_ID;
?>
<label class="screen-reader-text" for="post_author_override">***REMOVED*** _e('Author'); ?></label>
***REMOVED***
	wp_dropdown_users( array(
		'who' => 'authors',
		'name' => 'post_author_override',
		'selected' => empty($post->ID) ? $user_ID : $post->post_author,
		'include_selected' => true
	) );
}

/**
 * Display list of revisions.
 *
 * @since 2.6.0
 *
 * @param object $post
 */
function post_revisions_meta_box( $post ) {
	wp_list_post_revisions( $post );
}

// -- Page related Meta Boxes

/**
 * Display page attributes form fields.
 *
 * @since 2.7.0
 *
 * @param object $post
 */
function page_attributes_meta_box($post) {
	$post_type_object = get_post_type_object($post->post_type);
	if ( $post_type_object->hierarchical ) {
		$dropdown_args = array(
			'post_type'        => $post->post_type,
			'exclude_tree'     => $post->ID,
			'selected'         => $post->post_parent,
			'name'             => 'parent_id',
			'show_option_none' => __('(no parent)'),
			'sort_column'      => 'menu_order, post_title',
			'echo'             => 0,
		);

		$dropdown_args = apply_filters( 'page_attributes_dropdown_pages_args', $dropdown_args, $post );
		$pages = wp_dropdown_pages( $dropdown_args );
		if ( ! empty($pages) ) {
?>
<p><strong>***REMOVED*** _e('Parent') ?></strong></p>
<label class="screen-reader-text" for="parent_id">***REMOVED*** _e('Parent') ?></label>
***REMOVED*** echo $pages; ?>
***REMOVED***
		} // end empty pages check
	} // end hierarchical check.
	if ( 'page' == $post->post_type && 0 != count( get_page_templates() ) ) {
		$template = !empty($post->page_template) ? $post->page_template : false;
		?>
<p><strong>***REMOVED*** _e('Template') ?></strong></p>
<label class="screen-reader-text" for="page_template">***REMOVED*** _e('Page Template') ?></label><select name="page_template" id="page_template">
<option value='default'>***REMOVED*** _e('Default Template'); ?></option>
***REMOVED*** page_template_dropdown($template); ?>
</select>
***REMOVED***
	} ?>
<p><strong>***REMOVED*** _e('Order') ?></strong></p>
<p><label class="screen-reader-text" for="menu_order">***REMOVED*** _e('Order') ?></label><input name="menu_order" type="text" size="4" id="menu_order" value="***REMOVED*** echo esc_attr($post->menu_order) ?>" /></p>
<p>***REMOVED*** if ( 'page' == $post->post_type ) _e( 'Need help? Use the Help tab in the upper right of your screen.' ); ?></p>
***REMOVED***
}

// -- Link related Meta Boxes

/**
 * Display link create form fields.
 *
 * @since 2.7.0
 *
 * @param object $link
 */
function link_submit_meta_box($link) {
?>
<div class="submitbox" id="submitlink">

<div id="minor-publishing">

***REMOVED*** // Hidden submit button early on so that the browser chooses the right button when form is submitted with Return key ?>
<div style="display:none;">
***REMOVED*** submit_button( __( 'Save' ), 'button', 'save', false ); ?>
</div>

<div id="minor-publishing-actions">
<div id="preview-action">
***REMOVED*** if ( !empty($link->link_id) ) { ?>
	<a class="preview button" href="***REMOVED*** echo $link->link_url; ?>" target="_blank">***REMOVED*** _e('Visit Link'); ?></a>
***REMOVED*** } ?>
</div>
<div class="clear"></div>
</div>

<div id="misc-publishing-actions">
<div class="misc-pub-section misc-pub-private">
	<label for="link_private" class="selectit"><input id="link_private" name="link_visible" type="checkbox" value="N" ***REMOVED*** checked($link->link_visible, 'N'); ?> /> ***REMOVED*** _e('Keep this link private') ?></label>
</div>
</div>

</div>

<div id="major-publishing-actions">
***REMOVED*** do_action('post_submitbox_start'); ?>
<div id="delete-action">
***REMOVED***
if ( !empty($_GET['action']) && 'edit' == $_GET['action'] && current_user_can('manage_links') ) { ?>
	<a class="submitdelete deletion" href="***REMOVED*** echo wp_nonce_url("link.php?action=delete&amp;link_id=$link->link_id", 'delete-bookmark_' . $link->link_id); ?>" onclick="if ( confirm('***REMOVED*** echo esc_js(sprintf(__("You are about to delete this link '%s'\n 'Cancel' to stop, 'OK' to delete."), $link->link_name )); ?>') ) {return true;}return false;">***REMOVED*** _e('Delete'); ?></a>
***REMOVED*** } ?>
</div>

<div id="publishing-action">
***REMOVED*** if ( !empty($link->link_id) ) { ?>
	<input name="save" type="submit" class="button-large button-primary" id="publish" accesskey="p" value="***REMOVED*** esc_attr_e('Update Link') ?>" />
***REMOVED*** } else { ?>
	<input name="save" type="submit" class="button-large button-primary" id="publish" accesskey="p" value="***REMOVED*** esc_attr_e('Add Link') ?>" />
***REMOVED*** } ?>
</div>
<div class="clear"></div>
</div>
***REMOVED*** do_action('submitlink_box'); ?>
<div class="clear"></div>
</div>
***REMOVED***
}

/**
 * Display link categories form fields.
 *
 * @since 2.6.0
 *
 * @param object $link
 */
function link_categories_meta_box($link) {
?>
<div id="taxonomy-linkcategory" class="categorydiv">
	<ul id="category-tabs" class="category-tabs">
		<li class="tabs"><a href="#categories-all">***REMOVED*** _e( 'All Categories' ); ?></a></li>
		<li class="hide-if-no-js"><a href="#categories-pop">***REMOVED*** _e( 'Most Used' ); ?></a></li>
	</ul>

	<div id="categories-all" class="tabs-panel">
		<ul id="categorychecklist" data-wp-lists="list:category" class="categorychecklist form-no-clear">
			***REMOVED***
			if ( isset($link->link_id) )
				wp_link_category_checklist($link->link_id);
			else
				wp_link_category_checklist();
			?>
		</ul>
	</div>

	<div id="categories-pop" class="tabs-panel" style="display: none;">
		<ul id="categorychecklist-pop" class="categorychecklist form-no-clear">
			***REMOVED*** wp_popular_terms_checklist('link_category'); ?>
		</ul>
	</div>

	<div id="category-adder" class="wp-hidden-children">
		<h4><a id="category-add-toggle" href="#category-add">***REMOVED*** _e( '+ Add New Category' ); ?></a></h4>
		<p id="link-category-add" class="wp-hidden-child">
			<label class="screen-reader-text" for="newcat">***REMOVED*** _e( '+ Add New Category' ); ?></label>
			<input type="text" name="newcat" id="newcat" class="form-required form-input-tip" value="***REMOVED*** esc_attr_e( 'New category name' ); ?>" aria-required="true" />
			<input type="button" id="link-category-add-submit" data-wp-lists="add:categorychecklist:link-category-add" class="button" value="***REMOVED*** esc_attr_e( 'Add' ); ?>" />
			***REMOVED*** wp_nonce_field( 'add-link-category', '_ajax_nonce', false ); ?>
			<span id="category-ajax-response"></span>
		</p>
	</div>
</div>
***REMOVED***
}

/**
 * Display form fields for changing link target.
 *
 * @since 2.6.0
 *
 * @param object $link
 */
function link_target_meta_box($link) { ?>
<fieldset><legend class="screen-reader-text"><span>***REMOVED*** _e('Target') ?></span></legend>
<p><label for="link_target_blank" class="selectit">
<input id="link_target_blank" type="radio" name="link_target" value="_blank" ***REMOVED*** echo ( isset( $link->link_target ) && ($link->link_target == '_blank') ? 'checked="checked"' : ''); ?> />
***REMOVED*** _e('<code>_blank</code> &mdash; new window or tab.'); ?></label></p>
<p><label for="link_target_top" class="selectit">
<input id="link_target_top" type="radio" name="link_target" value="_top" ***REMOVED*** echo ( isset( $link->link_target ) && ($link->link_target == '_top') ? 'checked="checked"' : ''); ?> />
***REMOVED*** _e('<code>_top</code> &mdash; current window or tab, with no frames.'); ?></label></p>
<p><label for="link_target_none" class="selectit">
<input id="link_target_none" type="radio" name="link_target" value="" ***REMOVED*** echo ( isset( $link->link_target ) && ($link->link_target == '') ? 'checked="checked"' : ''); ?> />
***REMOVED*** _e('<code>_none</code> &mdash; same window or tab.'); ?></label></p>
</fieldset>
<p>***REMOVED*** _e('Choose the target frame for your link.'); ?></p>
***REMOVED***
}

/**
 * Display checked checkboxes attribute for xfn microformat options.
 *
 * @since 1.0.1
 *
 * @param string $class
 * @param string $value
 * @param mixed $deprecated Never used.
 */
function xfn_check( $class, $value = '', $deprecated = '' ) {
	global $link;

	if ( !empty( $deprecated ) )
		_deprecated_argument( __FUNCTION__, '0.0' ); // Never implemented

	$link_rel = isset( $link->link_rel ) ? $link->link_rel : ''; // In PHP 5.3: $link_rel = $link->link_rel ?: '';
	$rels = preg_split('/\s+/', $link_rel);

	if ('' != $value && in_array($value, $rels) ) {
		echo ' checked="checked"';
	}

	if ('' == $value) {
		if ('family' == $class && strpos($link_rel, 'child') === false && strpos($link_rel, 'parent') === false && strpos($link_rel, 'sibling') === false && strpos($link_rel, 'spouse') === false && strpos($link_rel, 'kin') === false) echo ' checked="checked"';
		if ('friendship' == $class && strpos($link_rel, 'friend') === false && strpos($link_rel, 'acquaintance') === false && strpos($link_rel, 'contact') === false) echo ' checked="checked"';
		if ('geographical' == $class && strpos($link_rel, 'co-resident') === false && strpos($link_rel, 'neighbor') === false) echo ' checked="checked"';
		if ('identity' == $class && in_array('me', $rels) ) echo ' checked="checked"';
	}
}

/**
 * Display xfn form fields.
 *
 * @since 2.6.0
 *
 * @param object $link
 */
function link_xfn_meta_box($link) {
?>
<table class="links-table" cellspacing="0">
	<tr>
		<th scope="row"><label for="link_rel">***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('rel:') ?></label></th>
		<td><input type="text" name="link_rel" id="link_rel" value="***REMOVED*** echo ( isset( $link->link_rel ) ? esc_attr($link->link_rel) : ''); ?>" /></td>
	</tr>
	<tr>
		<th scope="row">***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('identity') ?></th>
		<td><fieldset><legend class="screen-reader-text"><span>***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('identity') ?></span></legend>
			<label for="me">
			<input type="checkbox" name="identity" value="me" id="me" ***REMOVED*** xfn_check('identity', 'me'); ?> />
			***REMOVED*** _e('another web address of mine') ?></label>
		</fieldset></td>
	</tr>
	<tr>
		<th scope="row">***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('friendship') ?></th>
		<td><fieldset><legend class="screen-reader-text"><span>***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('friendship') ?></span></legend>
			<label for="contact">
			<input class="valinp" type="radio" name="friendship" value="contact" id="contact" ***REMOVED*** xfn_check('friendship', 'contact'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('contact') ?>
			</label>
			<label for="acquaintance">
			<input class="valinp" type="radio" name="friendship" value="acquaintance" id="acquaintance" ***REMOVED*** xfn_check('friendship', 'acquaintance'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('acquaintance') ?>
			</label>
			<label for="friend">
			<input class="valinp" type="radio" name="friendship" value="friend" id="friend" ***REMOVED*** xfn_check('friendship', 'friend'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('friend') ?>
			</label>
			<label for="friendship">
			<input name="friendship" type="radio" class="valinp" value="" id="friendship" ***REMOVED*** xfn_check('friendship'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('none') ?>
			</label>
		</fieldset></td>
	</tr>
	<tr>
		<th scope="row"> ***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('physical') ?> </th>
		<td><fieldset><legend class="screen-reader-text"><span>***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('physical') ?></span></legend>
			<label for="met">
			<input class="valinp" type="checkbox" name="physical" value="met" id="met" ***REMOVED*** xfn_check('physical', 'met'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('met') ?>
			</label>
		</fieldset></td>
	</tr>
	<tr>
		<th scope="row"> ***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('professional') ?> </th>
		<td><fieldset><legend class="screen-reader-text"><span>***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('professional') ?></span></legend>
			<label for="co-worker">
			<input class="valinp" type="checkbox" name="professional" value="co-worker" id="co-worker" ***REMOVED*** xfn_check('professional', 'co-worker'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('co-worker') ?>
			</label>
			<label for="colleague">
			<input class="valinp" type="checkbox" name="professional" value="colleague" id="colleague" ***REMOVED*** xfn_check('professional', 'colleague'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('colleague') ?>
			</label>
		</fieldset></td>
	</tr>
	<tr>
		<th scope="row">***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('geographical') ?></th>
		<td><fieldset><legend class="screen-reader-text"><span> ***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('geographical') ?> </span></legend>
			<label for="co-resident">
			<input class="valinp" type="radio" name="geographical" value="co-resident" id="co-resident" ***REMOVED*** xfn_check('geographical', 'co-resident'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('co-resident') ?>
			</label>
			<label for="neighbor">
			<input class="valinp" type="radio" name="geographical" value="neighbor" id="neighbor" ***REMOVED*** xfn_check('geographical', 'neighbor'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('neighbor') ?>
			</label>
			<label for="geographical">
			<input class="valinp" type="radio" name="geographical" value="" id="geographical" ***REMOVED*** xfn_check('geographical'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('none') ?>
			</label>
		</fieldset></td>
	</tr>
	<tr>
		<th scope="row">***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('family') ?></th>
		<td><fieldset><legend class="screen-reader-text"><span> ***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('family') ?> </span></legend>
			<label for="child">
			<input class="valinp" type="radio" name="family" value="child" id="child" ***REMOVED*** xfn_check('family', 'child'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('child') ?>
			</label>
			<label for="kin">
			<input class="valinp" type="radio" name="family" value="kin" id="kin" ***REMOVED*** xfn_check('family', 'kin'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('kin') ?>
			</label>
			<label for="parent">
			<input class="valinp" type="radio" name="family" value="parent" id="parent" ***REMOVED*** xfn_check('family', 'parent'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('parent') ?>
			</label>
			<label for="sibling">
			<input class="valinp" type="radio" name="family" value="sibling" id="sibling" ***REMOVED*** xfn_check('family', 'sibling'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('sibling') ?>
			</label>
			<label for="spouse">
			<input class="valinp" type="radio" name="family" value="spouse" id="spouse" ***REMOVED*** xfn_check('family', 'spouse'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('spouse') ?>
			</label>
			<label for="family">
			<input class="valinp" type="radio" name="family" value="" id="family" ***REMOVED*** xfn_check('family'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('none') ?>
			</label>
		</fieldset></td>
	</tr>
	<tr>
		<th scope="row">***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('romantic') ?></th>
		<td><fieldset><legend class="screen-reader-text"><span> ***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('romantic') ?> </span></legend>
			<label for="muse">
			<input class="valinp" type="checkbox" name="romantic" value="muse" id="muse" ***REMOVED*** xfn_check('romantic', 'muse'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('muse') ?>
			</label>
			<label for="crush">
			<input class="valinp" type="checkbox" name="romantic" value="crush" id="crush" ***REMOVED*** xfn_check('romantic', 'crush'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('crush') ?>
			</label>
			<label for="date">
			<input class="valinp" type="checkbox" name="romantic" value="date" id="date" ***REMOVED*** xfn_check('romantic', 'date'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('date') ?>
			</label>
			<label for="romantic">
			<input class="valinp" type="checkbox" name="romantic" value="sweetheart" id="romantic" ***REMOVED*** xfn_check('romantic', 'sweetheart'); ?> />&nbsp;***REMOVED*** /* translators: xfn: http://gmpg.org/xfn/ */ _e('sweetheart') ?>
			</label>
		</fieldset></td>
	</tr>

</table>
<p>***REMOVED*** _e('If the link is to a person, you can specify your relationship with them using the above form. If you would like to learn more about the idea check out <a href="http://gmpg.org/xfn/">XFN</a>.'); ?></p>
***REMOVED***
}

/**
 * Display advanced link options form fields.
 *
 * @since 2.6.0
 *
 * @param object $link
 */
function link_advanced_meta_box($link) {
?>
<table class="links-table" cellpadding="0">
	<tr>
		<th scope="row"><label for="link_image">***REMOVED*** _e('Image Address') ?></label></th>
		<td><input type="text" name="link_image" class="code" id="link_image" maxlength="255" value="***REMOVED*** echo ( isset( $link->link_image ) ? esc_attr($link->link_image) : ''); ?>" /></td>
	</tr>
	<tr>
		<th scope="row"><label for="rss_uri">***REMOVED*** _e('RSS Address') ?></label></th>
		<td><input name="link_rss" class="code" type="text" id="rss_uri" maxlength="255" value="***REMOVED*** echo ( isset( $link->link_rss ) ? esc_attr($link->link_rss) : ''); ?>" /></td>
	</tr>
	<tr>
		<th scope="row"><label for="link_notes">***REMOVED*** _e('Notes') ?></label></th>
		<td><textarea name="link_notes" id="link_notes" rows="10">***REMOVED*** echo ( isset( $link->link_notes ) ? $link->link_notes : ''); // textarea_escaped ?></textarea></td>
	</tr>
	<tr>
		<th scope="row"><label for="link_rating">***REMOVED*** _e('Rating') ?></label></th>
		<td><select name="link_rating" id="link_rating" size="1">
		***REMOVED***
			for ( $r = 0; $r <= 10; $r++ ) {
				echo '<option value="' . $r . '"';
				if ( isset($link->link_rating) && $link->link_rating == $r )
					echo ' selected="selected"';
				echo('>' . $r . '</option>');
			}
		?></select>&nbsp;***REMOVED*** _e('(Leave at 0 for no rating.)') ?>
		</td>
	</tr>
</table>
***REMOVED***
}

/**
 * Display post thumbnail meta box.
 *
 * @since 2.9.0
 */
function post_thumbnail_meta_box( $post ) {
	$thumbnail_id = get_post_meta( $post->ID, '_thumbnail_id', true );
	echo _wp_post_thumbnail_html( $thumbnail_id, $post->ID );
}
