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
 * Communities widget.
 *
 * @author Benjamin Jakobus
 */
class SBTCommunitiesWidget extends SBTBaseGridWidget {
	
	private $widget_id = 'ibm_sbtk_communities_widget';
	private $widget_name = 'IBM Communities';
	private $widget_description = 'Use this widget to display communities from IBM SmartCloud for Social Business or IBM Connections on Premise.';
	private $widget_location =  '/views/widgets/ibm-sbt-my-communities-grid.php';

	/**
	 * Constructor.
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
	
		if (isset($instance['ibm-sbtk-communities-type'])) {
			$type = $instance['ibm-sbtk-communities-type'];
		} else {
			$type = "public";
		}
		?>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-communities-type'); ?>"><?php echo $GLOBALS[LANG]['community-type']?>:</label> 
				<select id="<?php echo $this->get_field_id('ibm-sbtk-communities-type'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-communities-type'); ?>">
					<option <?php echo ($type == 'my' ? 'selected="selected"' : ''); ?> value="my"><?php echo $GLOBALS[LANG]['my-communities']?></option>
					<option <?php echo ($type == 'public' ? 'selected="selected"' : ''); ?> value="public"><?php echo $GLOBALS[LANG]['public-communities']?></option>
					<option <?php echo ($type == 'invited' ? 'selected="selected"' : ''); ?> value="invited"><?php echo $GLOBALS[LANG]['invited-communities']?></option>
				</select>
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
			$instance['ibm-sbtk-communities-type'] = (!empty($new_instance['ibm-sbtk-communities-type'])) ? strip_tags($new_instance['ibm-sbtk-communities-type'] ) : '';
			return $instance;
		}
		
		public function widget($args, $instance) {
			parent::widget($args, $instance);
		}
}
