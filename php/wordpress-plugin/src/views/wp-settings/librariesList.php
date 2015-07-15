<select onchange="save_library_selection();" id="libraries_list" name="libraries_list">
	<optgroup label="Compressed"></optgroup>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'none' ? 'selected="selected' : '')?> value="none">None</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.8.4' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.8.4">Dojo Toolkit 1.8.4</option>
	<optgroup label="Uncompressed"></optgroup>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.8.4 uncompressed' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.8.4 uncompressed">Dojo Toolkit 1.8.4 uncompressed</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'JQuery 1.8.3 uncompressed' ? 'selected="selected' : '')?> value="JQuery 1.8.3 unminified">JQuery 1.8.3 uncompressed</option>
</select>
