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
 * Files grid widget.
 *
 * @author Benjamin Jakobus
 */
class SBTKFilesWidget extends SBTKBaseGridWidget {
	
	private $widget_id = 'ibm_sbtk_files_widget';
	private $widget_name = 'Files Widget';
	private $widget_description = 'Displays a files grid';
	private $widget_location =  '/views/widgets/ibm-sbtk-files-grid.php';

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
		
		if (isset($instance['ibm-sbtk-files-pin-file'])) {
			$pinFile = $instance['ibm-sbtk-files-pin-file'];
		} else {
			$pinFile = false;
		}
		
		if (isset($instance['ibm-sbtk-files-type'])) {
			$type = $instance['ibm-sbtk-files-type'];
		} else {
			$type = "publicFiles";
		}
		
		if (isset($instance['ibm-sbtk-files-created'])) {
			$created = $instance['ibm-sbtk-files-created'];
		} else {
			$created = false;
		}
		
		if (isset($instance['ibm-sbtk-files-author'])) {
			$author = $instance['ibm-sbtk-files-author'];
		} else {
			$author = false;
		}
		
		if (isset($instance['ibm-sbtk-files-downloads-count'])) {
			$downloads = $instance['ibm-sbtk-files-downloads-count'];
		} else {
			$downloads = false;
		}
		
		if (isset($instance['ibm-sbtk-files-comments-count'])) {
			$comments = $instance['ibm-sbtk-files-comments-count'];
		} else {
			$comments = false;
		}
		?>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-files-pin-file'); ?>">Pin file:</label> 
				<input <?php echo ($pinFile ? 'checked="checked"' : ''); ?> id="<?php echo $this->get_field_id('ibm-sbtk-files-pin-file'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-files-pin-file'); ?>" type="checkbox" value="pin" />
			</p>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-files-created'); ?>"><?php echo $GLOBALS[LANG]['creation-date']?>:</label> 
				<input <?php echo ($created ? 'checked="checked"' : ''); ?> id="<?php echo $this->get_field_id('ibm-sbtk-files-created'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-files-created'); ?>" type="checkbox" value="created" />
			</p>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-files-author'); ?>"><?php echo $GLOBALS[LANG]['author']?>:</label> 
				<input <?php echo ($author ? 'checked="checked"' : ''); ?> id="<?php echo $this->get_field_id('ibm-sbtk-files-author'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-files-author'); ?>" type="checkbox" value="author" />
			</p>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-files-downloads-count'); ?>"><?php echo $GLOBALS[LANG]['downloads-count']?>:</label> 
				<input <?php echo ($downloads ? 'checked="checked"' : ''); ?> id="<?php echo $this->get_field_id('ibm-sbtk-files-downloads-count'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-files-downloads-count'); ?>" type="checkbox" value="downloads" />
			</p>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-files-comments-count'); ?>"><?php echo $GLOBALS[LANG]['comments-count']?>:</label> 
				<input <?php echo ($comments ? 'checked="checked"' : ''); ?> id="<?php echo $this->get_field_id('ibm-sbtk-files-comments-count'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-files-comments-count'); ?>" type="checkbox" value="comments" />
			</p>
			<p>
				<label for="<?php echo $this->get_field_id('ibm-sbtk-files-type'); ?>"><?php echo $GLOBALS[LANG]['file-type']?>:</label> 
				<select id="<?php echo $this->get_field_id('ibm-sbtk-files-type'); ?>" name="<?php echo $this->get_field_name('ibm-sbtk-files-type'); ?>">
					<option <?php echo ($type == 'myFiles' ? 'selected="selected"' : ''); ?> value="myFiles"><?php echo $GLOBALS[LANG]['my-files']?></option>
					<option <?php echo ($type == 'publicFiles' ? 'selected="selected"' : ''); ?> value="publicFiles"><?php echo $GLOBALS[LANG]['public-files']?></option>
					<option <?php echo ($type == 'myPinnedFiles' ? 'selected="selected"' : ''); ?> value="myPinnedFiles"><?php echo $GLOBALS[LANG]['pinned-files']?></option>
					<option <?php echo ($type == 'myFolders' ? 'selected="selected"' : ''); ?> value="myFolders"><?php echo $GLOBALS[LANG]['my-folders']?></option>
					<option <?php echo ($type == 'publicFolders' ? 'selected="selected"' : ''); ?> value="publicFolders"><?php echo $GLOBALS[LANG]['public-folders']?></option>
					<option <?php echo ($type == 'myPinnedFolders' ? 'selected="selected"' : ''); ?> value="myPinnedFolders"><?php echo $GLOBALS[LANG]['my-pinned-folders']?></option>
					<option <?php echo ($type == 'activeFolders' ? 'selected="selected"' : ''); ?> value="activeFolders"><?php echo $GLOBALS[LANG]['active-folders']?></option>
					<option <?php echo ($type == 'fileShares' ? 'selected="selected"' : ''); ?> value="fileShares"><?php echo $GLOBALS[LANG]['file-shares']?></option>
					<option <?php echo ($type == 'communityFiles' ? 'selected="selected"' : ''); ?> value="communityFiles"><?php echo $GLOBALS[LANG]['community-files']?></option>
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
		$instance['ibm-sbtk-files-pin-file'] = (!empty($new_instance['ibm-sbtk-files-pin-file'])) ? strip_tags($new_instance['ibm-sbtk-files-pin-file'] ) : '';
		$instance['ibm-sbtk-files-type'] = (!empty($new_instance['ibm-sbtk-files-type'])) ? strip_tags($new_instance['ibm-sbtk-files-type'] ) : '';
		$instance['ibm-sbtk-files-created'] = (!empty($new_instance['ibm-sbtk-files-created'])) ? strip_tags($new_instance['ibm-sbtk-files-created'] ) : '';
		$instance['ibm-sbtk-files-author'] = (!empty($new_instance['ibm-sbtk-files-author'])) ? strip_tags($new_instance['ibm-sbtk-files-author'] ) : '';
		$instance['ibm-sbtk-files-downloads-count'] = (!empty($new_instance['ibm-sbtk-files-downloads-count'])) ? strip_tags($new_instance['ibm-sbtk-files-downloads-count'] ) : '';
		$instance['ibm-sbtk-files-comments-count'] = (!empty($new_instance['ibm-sbtk-files-comments-count'])) ? strip_tags($new_instance['ibm-sbtk-files-comments-count'] ) : '';
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