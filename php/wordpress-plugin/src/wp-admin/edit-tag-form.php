***REMOVED***
/**
 * Edit tag form for inclusion in administration panels.
 *
 * @package WordPress
 * @subpackage Administration
 */

// don't load directly
if ( !defined('ABSPATH') )
	die('-1');

if ( empty($tag_ID) ) { ?>
	<div id="message" class="updated"><p><strong>***REMOVED*** _e( 'You did not select an item for editing.' ); ?></strong></p></div>
***REMOVED***
	return;
}

// Back compat hooks
if ( 'category' == $taxonomy )
	do_action('edit_category_form_pre', $tag );
elseif ( 'link_category' == $taxonomy )
	do_action('edit_link_category_form_pre', $tag );
else
	do_action('edit_tag_form_pre', $tag);

do_action($taxonomy . '_pre_edit_form', $tag, $taxonomy); ?>

<div class="wrap">
***REMOVED*** screen_icon(); ?>
<h2>***REMOVED*** echo $tax->labels->edit_item; ?></h2>
<div id="ajax-response"></div>
<form name="edittag" id="edittag" method="post" action="edit-tags.php" class="validate"***REMOVED*** do_action( $taxonomy . '_term_edit_form_tag' ); ?>>
<input type="hidden" name="action" value="editedtag" />
<input type="hidden" name="tag_ID" value="***REMOVED*** echo esc_attr($tag->term_id) ?>" />
<input type="hidden" name="taxonomy" value="***REMOVED*** echo esc_attr($taxonomy) ?>" />
***REMOVED*** wp_original_referer_field(true, 'previous'); wp_nonce_field('update-tag_' . $tag_ID); ?>
	<table class="form-table">
		<tr class="form-field form-required">
			<th scope="row" valign="top"><label for="name">***REMOVED*** _ex('Name', 'Taxonomy Name'); ?></label></th>
			<td><input name="name" id="name" type="text" value="***REMOVED*** if ( isset( $tag->name ) ) echo esc_attr($tag->name); ?>" size="40" aria-required="true" />
			<p class="description">***REMOVED*** _e('The name is how it appears on your site.'); ?></p></td>
		</tr>
***REMOVED*** if ( !global_terms_enabled() ) { ?>
		<tr class="form-field">
			<th scope="row" valign="top"><label for="slug">***REMOVED*** _ex('Slug', 'Taxonomy Slug'); ?></label></th>
			<td><input name="slug" id="slug" type="text" value="***REMOVED*** if ( isset( $tag->slug ) ) echo esc_attr(apply_filters('editable_slug', $tag->slug)); ?>" size="40" />
			<p class="description">***REMOVED*** _e('The &#8220;slug&#8221; is the URL-friendly version of the name. It is usually all lowercase and contains only letters, numbers, and hyphens.'); ?></p></td>
		</tr>
***REMOVED*** } ?>
***REMOVED*** if ( is_taxonomy_hierarchical($taxonomy) ) : ?>
		<tr class="form-field">
			<th scope="row" valign="top"><label for="parent">***REMOVED*** _ex('Parent', 'Taxonomy Parent'); ?></label></th>
			<td>
				***REMOVED*** wp_dropdown_categories(array('hide_empty' => 0, 'hide_if_empty' => false, 'name' => 'parent', 'orderby' => 'name', 'taxonomy' => $taxonomy, 'selected' => $tag->parent, 'exclude_tree' => $tag->term_id, 'hierarchical' => true, 'show_option_none' => __('None'))); ?>
				***REMOVED*** if ( 'category' == $taxonomy ) : ?>
				<p class="description">***REMOVED*** _e('Categories, unlike tags, can have a hierarchy. You might have a Jazz category, and under that have children categories for Bebop and Big Band. Totally optional.'); ?></p>
				***REMOVED*** endif; ?>
			</td>
		</tr>
***REMOVED*** endif; // is_taxonomy_hierarchical() ?>
		<tr class="form-field">
			<th scope="row" valign="top"><label for="description">***REMOVED*** _ex('Description', 'Taxonomy Description'); ?></label></th>
			<td><textarea name="description" id="description" rows="5" cols="50" class="large-text">***REMOVED*** echo $tag->description; // textarea_escaped ?></textarea><br />
			<span class="description">***REMOVED*** _e('The description is not prominent by default; however, some themes may show it.'); ?></span></td>
		</tr>
		***REMOVED***
		// Back compat hooks
		if ( 'category' == $taxonomy )
			do_action('edit_category_form_fields', $tag);
		elseif ( 'link_category' == $taxonomy )
			do_action('edit_link_category_form_fields', $tag);
		else
			do_action('edit_tag_form_fields', $tag);

		do_action($taxonomy . '_edit_form_fields', $tag, $taxonomy);
		?>
	</table>
***REMOVED***
// Back compat hooks
if ( 'category' == $taxonomy )
	do_action('edit_category_form', $tag);
elseif ( 'link_category' == $taxonomy )
	do_action('edit_link_category_form', $tag);
else
	do_action('edit_tag_form', $tag);

do_action($taxonomy . '_edit_form', $tag, $taxonomy);

submit_button( __('Update') );
?>
</form>
</div>
<script type="text/javascript">
try{document.forms.edittag.name.focus();}catch(e){}
</script>
