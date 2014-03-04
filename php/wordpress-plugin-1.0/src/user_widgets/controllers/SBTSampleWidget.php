<?php
/*
 * © Copyright IBM Corp. 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

/**
 * A sample widget.
 *
 * @author Benjamin Jakobus
 */
class SBTSampleWidget extends SBTBaseGridWidget {
	
	private $widget_id = 'ibm_sbtk_samples_widget'; // The ID must be unique
	private $widget_name = 'IBM Sample Widget'; // This will be your widget name
	private $widget_description = 'This is a sample widget to demonstrate how to create your own IBM Connections widget.'; // Describe your widget
	private $widget_location =  '/user_widgets/views/ibm-sbt-sample.php'; // Location of your widget, relative to the plugin's base path

	/**
	 * Constructor.
	 * 
	 */
	public function __construct() {
		parent::__construct($this->widget_id, $this->widget_name, $this->widget_description, BASE_PATH . $this->widget_location);
	}
	
	/**
	 * Ouputs the options form on admin
	 *
	 * @param array $instance The widget options
	 */
	public function form($instance) {
		parent::form($instance);
		
		if (isset($instance['ibm-sbtk-files-pin-file'])) {
			$pinFile = $instance['ibm-sbtk-files-pin-file'];
		} else {
			$pinFile = false;
		}
		
		?>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-files-pin-file'); ?>">Pin file:</label> 
				<input <?php echo ($pinFile ? 'checked="checked"' : ''); ?> id="<?php echo $this->get_field_id('ibm-sbtk-files-pin-file'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-files-pin-file'); ?>" type="checkbox" value="pin" />
			</p>
			
		<?php 
	}
		
	/**
	 * Processing widget options on save
	 *
	 * @param array $new_instance The new options
     * @param array $old_instance The previous options
	 */
	public function update($new_instance, $old_instance) {
		$instance = parent::update($new_instance, $old_instance);
		$instance['ibm-sbtk-files-pin-file'] = (!empty($new_instance['ibm-sbtk-files-pin-file'])) ? strip_tags($new_instance['ibm-sbtk-files-pin-file'] ) : '';
		return $instance;
	}
	
	/**
	 * Outputs the content of the widget.
	 *
	 * @param array $args
	 * @param array $instance
	 *
	 * @author Benjamin Jakobus
	 */
	public function widget($args, $instance) {
		parent::widget($args, $instance);
	}
}
