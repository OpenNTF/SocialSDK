<?php
/*
 * © Copyright IBM Corp. 2013
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
 * Plugin Controller Class
 *
 * This class object handles all logic related to plugin management.
 *
 * @author Benjamin Jakobus
 */
defined('SBT_SDK') OR exit('Access denied.');
class SBTKBlogs extends BasePluginController {
	/**
	 * Creates a grid displaying all blogs.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createAllBlogsGrid($args) {
		$this->loadView('social/blogs/all-blogs-grid', array());
	}
	
	/**
	 * Creates a grid displaying blog comments.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createBlogCommentsGrid($args) {
		$this->loadView('social/blogs/blog-comments-grid', array());
	}
	
	/**
	 * Creates a grid displaying my blogs.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createMyBlogsGrid($args) {
		$this->loadView('social/blogs/my-blogs-grid', array());
	}
	
	/**
	 * Creates a grid displaying my blogs.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createFeaturedPostsGrid($args) {
		$this->loadView('social/blogs/featured-posts-grid', array());
	}
	
	/**
	 * Creates a grid displaying my blogs.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createFeaturedBlogsGrid($args) {
		$this->loadView('social/blogs/featured-blogs-grid', array());
	}

}

