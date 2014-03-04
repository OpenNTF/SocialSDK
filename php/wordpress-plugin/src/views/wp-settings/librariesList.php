<select onchange="save_library_selection();" id="libraries_list" name="libraries_list">
	<optgroup label="Compressed"></optgroup>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'none' ? 'selected="selected' : '')?> value="none">None</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.4.3' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.4.3">Dojo Toolkit 1.4.3</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.5.2' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.5.2">Dojo Toolkit 1.5.2</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.6.1' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.6.1">Dojo Toolkit 1.6.1</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.7.4' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.7.4">Dojo Toolkit 1.7.4</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.8.4' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.8.4">Dojo Toolkit 1.8.4</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.9.0' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.9.0">Dojo Toolkit 1.9.0</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'JQuery 1.8.3' ? 'selected="selected' : '')?> value="JQuery 1.8.3">JQuery 1.8.3</option>
	<optgroup label="Uncompressed"></optgroup>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.4.3 uncompressed' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.4.3 uncompressed">Dojo Toolkit 1.4.3 uncompressed</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.5.2 uncompressed' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.5.2 uncompressed">Dojo Toolkit 1.5.2 uncompressed</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.6.1 uncompressed' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.6.1 uncompressed">Dojo Toolkit 1.6.1 uncompressed</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.7.4 uncompressed' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.7.4 uncompressed">Dojo Toolkit 1.7.4 uncompressed</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.8.4 uncompressed' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.8.4 uncompressed">Dojo Toolkit 1.8.4 uncompressed</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'Dojo Toolkit 1.9.0 uncompressed' ? 'selected="selected' : '')?> value="Dojo Toolkit 1.9.0 uncompressed">Dojo Toolkit 1.9.0 uncompressed</option>
	<option <?php echo (isset($viewData['js_library']) && $viewData['js_library'] == 'JQuery 1.8.3 uncompressed' ? 'selected="selected' : '')?> value="JQuery 1.8.3 unminified">JQuery 1.8.3 uncompressed</option>
</select>
