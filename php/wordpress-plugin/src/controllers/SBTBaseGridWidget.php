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
 * Base grid widget.
 *
 * @author Benjamin Jakobus
 */
class SBTBaseGridWidget extends SBTBaseWidget {
	/**
	 * Constructor.
	 * 
	 * @author Benjamin Jakobus
	 */
	public function __construct($widget_id, $widget_name, $widget_description, $widget_location) {
		parent::__construct($widget_id, $widget_name, $widget_description, $widget_location);
	}
	
	/**
	 * Ouputs the options form on admin
	 *
	 * @param array $instance The widget options
	 */
	public function form($instance) {
		parent::form($instance);
		
		if (isset($instance['ibm-sbtk-grid-page-size'])) {
			$pageSize = $instance['ibm-sbtk-grid-page-size'];
		} else {
			$pageSize = 10;
		}
		
		if (isset($instance['ibm-sbtk-grid-pager'])) {
			$pager = $instance['ibm-sbtk-grid-pager'];
		} else {
			$pager = false;
		}
		
		if (isset($instance['ibm-sbtk-grid-sorter'])) {
			$sorter = $instance['ibm-sbtk-grid-sorter'];
		} else {
			$sorter = false;
		}
		
		if (isset($instance['ibm-sbtk-grid-footer'])) {
			$footer = $instance['ibm-sbtk-grid-footer'];
		} else {
			$footer = false;
		}
		
		?>
			<p>
				<label for="***REMOVED*** echo $this->get_field_id('ibm-sbtk-grid-page-size'); ?>">Items per page:</label> 
				<input class="widefat" id="***REMOVED*** echo $this->get_field_id('ibm-sbtk-grid-page-size'); ?>" name="***REMOVED*** echo $this->get_field_name('ibm-sbtk-grid-page-size'); ?>" type="text" value="***REMOVED*** echo esc_attr($pageSize); ?>"/>
			</p>
			<p>
				<label for="***REMOVED*** echo $this->get_field_id('ibm-sbtk-grid-pager'); ?>">***REMOVED*** echo $GLOBALS[LANG]['pager']?>:</label> 
				<input ***REMOVED*** echo ($pager ? 'checked="checked"' : ''); ?> id="***REMOVED*** echo $this->get_field_id('ibm-sbtk-grid-pager'); ?>" name="***REMOVED*** echo $this->get_field_name('ibm-sbtk-grid-pager'); ?>" type="checkbox" value="pager" />
			</p>
			<p>
				<label for="***REMOVED*** echo $this->get_field_id('ibm-sbtk-grid-sorter'); ?>">***REMOVED*** echo $GLOBALS[LANG]['sorter']?>:</label> 
				<input ***REMOVED*** echo ($sorter ? 'checked="checked"' : ''); ?> id="***REMOVED*** echo $this->get_field_id('ibm-sbtk-grid-sorter'); ?>" name="***REMOVED*** echo $this->get_field_name('ibm-sbtk-grid-sorter'); ?>" type="checkbox" value="sorter" />
			</p>
			<p>
				<label for="***REMOVED*** echo $this->get_field_id('ibm-sbtk-grid-footer'); ?>">***REMOVED*** echo $GLOBALS[LANG]['footer']?>:</label> 
				<input ***REMOVED*** echo ($footer ? 'checked="checked"' : ''); ?> id="***REMOVED*** echo $this->get_field_id('ibm-sbtk-grid-footer'); ?>" name="***REMOVED*** echo $this->get_field_name('ibm-sbtk-grid-footer'); ?>" type="checkbox" value="footer" />
			</p>
		***REMOVED*** 
	}
		
	/**
	 * Processing widget options on save
	 *
	 * @param array $new_instance The new options
     * @param array $old_instance The previous options
	 */
	public function update($new_instance, $old_instance) {
		$instance = parent::update($new_instance, $old_instance);
		$instance['ibm-sbtk-grid-pager'] = (!empty($new_instance['ibm-sbtk-grid-pager'])) ? strip_tags($new_instance['ibm-sbtk-grid-pager'] ) : '';
		$instance['ibm-sbtk-grid-sorter'] = (!empty($new_instance['ibm-sbtk-grid-sorter'])) ? strip_tags($new_instance['ibm-sbtk-grid-sorter'] ) : '';
		$instance['ibm-sbtk-grid-page-size'] = (!empty($new_instance['ibm-sbtk-grid-page-size'])) ? strip_tags($new_instance['ibm-sbtk-grid-page-size'] ) : '';
		$instance['ibm-sbtk-grid-footer'] = (!empty($new_instance['ibm-sbtk-grid-footer'])) ? strip_tags($new_instance['ibm-sbtk-grid-footer'] ) : '';
		return $instance;
	}
}