***REMOVED***
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
class SBTSampleForumWidget extends SBTBaseWidget {
	
	private $widget_id = 'ibm_sbtk_sample_forum_widget'; // The ID must be unique
	private $widget_name = 'IBM Sample Forum Widget'; // This will be your widget name
	private $widget_description = 'This is a sample widget that demonstrates how to list forums and create a new forum.'; // Describe your widget
	private $widget_location =  '/user_widgets/views/ibm-sbt-forum-sample.php'; // Location of your widget, relative to the plugin's base path

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
	}
		
	/**
	 * Processing widget options on save
	 *
	 * @param array $new_instance The new options
     * @param array $old_instance The previous options
	 */
	public function update($new_instance, $old_instance) {
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
