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
class SBTKCommunitiesWidget extends SBTKBaseGridWidget {
	
	private $widget_id = 'ibm_sbtk_communities_widget';
	private $widget_name = 'Communities Widget';
	private $widget_description = 'Displays a communities grid';
	private $widget_location =  '/views/widgets/ibm-sbtk-my-communities-grid.php';

	/**
	 * Constructor.
	 * 
	 * @author Benjamin Jakobus
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
		
		if (isset($instance['ibm-sbtk-communities-tags'])) {
			$tags = $instance['ibm-sbtk-communities-tags'];
		} else {
			$tags = false;
		}
		
		if (isset($instance['ibm-sbtk-communities-member-count'])) {
			$count = $instance['ibm-sbtk-communities-member-count'];
		} else {
			$count = false;
		}
		
		if (isset($instance['ibm-sbtk-communities-icon'])) {
			$icon = $instance['ibm-sbtk-communities-icon'];
		} else {
			$icon = false;
		}
		
		if (isset($instance['ibm-sbtk-communities-updated-by'])) {
			$updated = $instance['ibm-sbtk-communities-updated-by'];
		} else {
			$updated = false;
		}
		
		if (isset($instance['ibm-sbtk-communities-description'])) {
			$description = $instance['ibm-sbtk-communities-description'];
		} else {
			$description = false;
		}
		?>
			<script type="text/javascript">
				window.onload = function () {
					document.getElementById("<?php echo $this->get_field_id('ibm-sbtk-integrate'); ?>").onclick = function() {
						if (document.getElementById("<?php echo $this->get_field_id('ibm-sbtk-integrate'); ?>").checked) {
							document.getElementById("<?php echo $this->get_field_id('ibm-sbtk-communities-tags'); ?>").disabled = false;
							document.getElementById("<?php echo $this->get_field_id('ibm-sbtk-communities-member-count'); ?>").disabled = false;
							document.getElementById("<?php echo $this->get_field_id('ibm-sbtk-communities-icon'); ?>").disabled = false;
							document.getElementById("<?php echo $this->get_field_id('ibm-sbtk-communities-updated-by'); ?>").disabled = false;
							document.getElementById("<?php echo $this->get_field_id('ibm-sbtk-communities-description'); ?>").disabled = false;
						} else {
							document.getElementById("<?php echo $this->get_field_id('ibm-sbtk-communities-tags'); ?>").disabled = true;
							document.getElementById("<?php echo $this->get_field_id('ibm-sbtk-communities-member-count'); ?>").disabled = true;
							document.getElementById("<?php echo $this->get_field_id('ibm-sbtk-communities-icon'); ?>").disabled = true;
							document.getElementById("<?php echo $this->get_field_id('ibm-sbtk-communities-updated-by'); ?>").disabled = true;
							document.getElementById("<?php echo $this->get_field_id('ibm-sbtk-communities-description'); ?>").disabled = true;
						}
					}
					
				}
			</script>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-communities-type'); ?>"><?php echo $GLOBALS[LANG]['community-type']?>:</label> 
				<select id="<?php echo $this->get_field_id('ibm-sbtk-communities-type'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-communities-type'); ?>">
					<option <?php echo ($type == 'my' ? 'selected="selected"' : ''); ?> value="my"><?php echo $GLOBALS[LANG]['my-communities']?></option>
					<option <?php echo ($type == 'public' ? 'selected="selected"' : ''); ?> value="public"><?php echo $GLOBALS[LANG]['public-communities']?></option>
					<option <?php echo ($type == 'invited' ? 'selected="selected"' : ''); ?> value="invited"><?php echo $GLOBALS[LANG]['invited-communities']?></option>
				</select>
			</p>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-communities-tags'); ?>"><?php echo $GLOBALS[LANG]['tags']?>:</label> 
				<input <?php echo ($tags ? 'checked="checked"' : ''); ?> id="<?php echo $this->get_field_id('ibm-sbtk-communities-tags'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-communities-tags'); ?>" type="checkbox" value="tags" />
			</p>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-communities-member-count'); ?>"><?php echo $GLOBALS[LANG]['member-count']?>:</label> 
				<input <?php echo ($count ? 'checked="checked"' : ''); ?> id="<?php echo $this->get_field_id('ibm-sbtk-communities-member-count'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-communities-member-count'); ?>" type="checkbox" value="count" />
			</p>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-communities-icon'); ?>"><?php echo $GLOBALS[LANG]['icons']?>:</label> 
				<input <?php echo ($icon ? 'checked="checked"' : ''); ?> id="<?php echo $this->get_field_id('ibm-sbtk-communities-icon'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-communities-icon'); ?>" type="checkbox" value="icon" />
			</p>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-communities-updated-by'); ?>"><?php echo $GLOBALS[LANG]['updated-by']?>:</label> 
				<input <?php echo ($updated ? 'checked="checked"' : ''); ?> id="<?php echo $this->get_field_id('ibm-sbtk-communities-updated-by'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-communities-updated-by'); ?>" type="checkbox" value="updated" />
			</p>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-communities-description'); ?>"><?php echo $GLOBALS[LANG]['description']?>:</label> 
				<input <?php echo ($description ? 'checked="checked"' : ''); ?> id="<?php echo $this->get_field_id('ibm-sbtk-communities-description'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-communities-description'); ?>" type="checkbox" value="description" />
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
			$instance['ibm-sbtk-communities-tags'] = (!empty($new_instance['ibm-sbtk-communities-tags'])) ? strip_tags($new_instance['ibm-sbtk-communities-tags'] ) : '';
			$instance['ibm-sbtk-communities-member-count'] = (!empty($new_instance['ibm-sbtk-communities-member-count'])) ? strip_tags($new_instance['ibm-sbtk-communities-member-count'] ) : '';
			$instance['ibm-sbtk-communities-icon'] = (!empty($new_instance['ibm-sbtk-communities-icon'])) ? strip_tags($new_instance['ibm-sbtk-communities-icon'] ) : '';
			$instance['ibm-sbtk-communities-updated-by'] = (!empty($new_instance['ibm-sbtk-communities-updated-by'])) ? strip_tags($new_instance['ibm-sbtk-communities-updated-by'] ) : '';
			$instance['ibm-sbtk-communities-description'] = (!empty($new_instance['ibm-sbtk-communities-description'])) ? strip_tags($new_instance['ibm-sbtk-communities-description'] ) : '';
			return $instance;
		}
		
		public function widget($args, $instance) {
			parent::widget($args, $instance);
		}
}