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
 * SBTK edit form
 *
 * @author Benjamin Jakobus
 */
class block_ibmsbtk_edit_form extends block_edit_form {
 
    protected function specific_definition($mform) {
 
        // Section header title according to language file.
        $mform->addElement('header', 'configheader', get_string('blocksettings', 'block')); 

        // List of available samples. Note: keys must reflect relative path from
        // core/samples and must omit the .php file extension
        $types = array('Custom' => 'Custom', 
				'<optgroup label="Blogs"></optgroup>',
        		'social/blogs/all-blogs-grid' => '&nbsp;&nbsp;&nbsp;All blogs',
        		'social/blogs/blog-comments-grid' => '&nbsp;&nbsp;&nbsp;Blog comments',
        		'social/blogs/my-blogs-grid' => '&nbsp;&nbsp;&nbsp;My blogs',
        		'social/blogs/featured-blogs-grid' => '&nbsp;&nbsp;&nbsp;Featured blogs',
        		'social/blogs/featured-posts-grid' => '&nbsp;&nbsp;&nbsp;Featured posts',
        		
        		'<optgroup label="Bookmarks"></optgroup>',
        		'social/bookmarks/bookmarks-grid' => '&nbsp;&nbsp;&nbsp;My bookmarks',
        		
        		'<optgroup label="Communities"></optgroup>',
        		'social/communities/communities-grid' => '&nbsp;&nbsp;&nbsp;My communities',
        		'social/communities/community-members-grid' => '&nbsp;&nbsp;&nbsp;Community members',
        		'social/communities/my-community-invites' => '&nbsp;&nbsp;&nbsp;My community invites',
        		'social/communities/start-new-community' => '&nbsp;&nbsp;&nbsp;Start a new community',
        		'social/communities/update-community-logo' => '&nbsp;&nbsp;&nbsp;Update community logo',
        		
        		'<optgroup label="Files"></optgroup>',
        		'social/files/files-grid' => '&nbsp;&nbsp;&nbsp;My Files', 
        		'social/files/files-shared-by-me-grid' => '&nbsp;&nbsp;&nbsp;Files shared by me', 
        		'social/files/files-shared-with-me-grid' => '&nbsp;&nbsp;&nbsp;Files shared with me', 
        		'social/files/my-folders-grid' => '&nbsp;&nbsp;&nbsp;My Folders',
        		'social/files/my-file-comments-grid' => '&nbsp;&nbsp;&nbsp;My file comments',
        		'social/files/public-file-comments-grid' => '&nbsp;&nbsp;&nbsp;Public file comments',
        		'social/files/public-files-grid' => '&nbsp;&nbsp;&nbsp;Public files</br>',
        		
        		'<optgroup label="Forums"></optgroup>',
        		'social/forums/forum-answered-topics-grid' => '&nbsp;&nbsp;&nbsp;Answered forum topics',
        		'social/forums/forum-entries-grid' => '&nbsp;&nbsp;&nbsp;Forum entries',
        		'social/forums/forums-grid' => '&nbsp;&nbsp;&nbsp;Forums',
        		'social/forums/topics-grid' => '&nbsp;&nbsp;&nbsp;Forum topics',
        		
        		'<optgroup label="Profiles"></optgroup>',
        		'social/profiles/my-profile-panel' => '&nbsp;&nbsp;&nbsp;My profile panel',
        		
        		'<optgroup label="Sametime"></optgroup>',
        		'social/sametime/smartcloud-chat' => '&nbsp;&nbsp;&nbsp;SmartCloud Chat Client'
        );
        // Info note
        $mform->addElement('html', '<p><strong style="color: red;">NOTE:</strong> For custom JavaScript and HTML to become active, "Type" must equal "Custom".</p>');
        
        $mform->addElement('html', '<p>For documentation, tutorials and guides on how to use the Social Business SDK, go to 
        		<a href="https://www.ibmdw.net/social/">https://www.ibmdw.net/social/</a> or visit <a href="https://greenhousestage.lotus.com/sbt/sbtplayground.nsf">our playground</a>
        		 directly if you need JavaScript snippets.</p>');
        
        // Type dropdown
        $mform->addElement('select', 'config_type', 'Type:', $types);
        $mform->setDefault('config_type', 'social/files/files-grid');
        
        // Block title
        $mform->addElement('text', 'config_title', 'Title:');
        $mform->setDefault('config_title', 'default value');
        $mform->setType('config_title', PARAM_MULTILANG);
        
        // Custom JavaScript code
        $mform->addElement('editor', 'config_customCode', 'Custom JavaScript:');
        $mform->setDefault('config_customCode', "alert('Some sample JavaScript here');");
        $mform->setType('config_customCode', PARAM_RAW);
        
        // Custom HTML code
        $mform->addElement('editor', 'config_customHTML', 'Custom HTML:');
        $mform->setDefault('config_customHTML', '<div id="gridDiv"></div>');
        $mform->setType('config_customHTML', PARAM_RAW);
        
        // Print hidden fields
        require 'hidden_sample_code.php';
 
    }
}