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
defined('SBT_SDK') OR exit('Access denied.');


/**
 * PHP API for IBM Connections File Service.
 * 
 * @author Benjamin Jakobus
 */
class SBTFileService extends SBTAtomService
{
	protected $files = array();
	protected $settings;
	
	/**
	 * Constructor.
	 * 
	 * @param string $endpointName
	 */
	function __construct($endpointName) 
	{
		parent::__construct($endpointName);
		$this->update();
	}
	
	/**
	 * Extract files from the atom feed and store them as private members.
	 */
	private function _extractFiles() 
	{
		$entries = $this->getAllTags('entry');
		$author = null;
		foreach($entries as $entry) {
			$file = new SBTFile();
			
			foreach ($entry as $item) {
				if ($item['tag'] == 'td:uuid') {
					$file->setFileId($item['value']);
				} else if ($item['tag'] == 'td:label') {
					$file->setLabel($item['value']);
				} else if ($item['tag'] == 'td:selfUrl') {
					$file->setSelfUrl($item['value']);
				} else if ($item['tag'] == 'content') {
					$file->setDownloadUrl($item['attributes']['src']);
				} else if ($item['tag'] == 'td:objectTypeName') {
					$file->setType($item);
				} else if ($item['tag'] == 'category') {
					$file->setCategory($item);
				} else if ($item['tag'] == 'td:totalMediaSize') {
					$file->setSize($item);
				} else if ($item['tag'] == 'link') {
					if (isset($item['attributes']['rel']) && $item['attributes']['rel'] == 'edit-media') {
						$file->setEditMediaLink($item['attributes']['href']);
					} else if (isset($item['attributes']['href']) && $item['attributes']['rel'] == 'edit') {
						$file->setEditLink($item['attributes']['href']);
					} else if (isset($item['attributes']['href']) && $item['attributes']['rel'] == 'thumbnail') {
						$file->setThumbnailUrl($item['attributes']['href']);
					}
				} else if ($item['tag'] == 'author') {
					if ($item['type'] == 'open') {
						$author = array('tag' => 'author');
					} else {
						$file->setAuthor($author);
						$author = null;
					}
				} else if ($author != null) {
					if ($item['tag'] == 'name') {
						$author['name'] = $item['value'];
					} else if ($item['tag'] == 'snx:userid') {
						$author['userid'] = $item['value'];
					} else if ($item['tag'] == 'snx:orgId') {
						$author['orgId'] = $item['value'];
					} else if ($item['tag'] == 'snx:orgName') {
						$author['orgName'] = $item['value'];
					} else if ($item['tag'] == 'snx:userState') {
						$author['userState'] = $item['value'];
					}
				} else if ($item['tag'] == 'title') {
					$file->setTitle($item['value']);
				} 
			}
			array_push($this->files, $file);
		}
	}
	
	/**
	 * Return all files.
	 * 
	 * @return multitype	Array containing file objects.
	 */
	public function getFiles()
	{
		return $this->files;
	}
	
	/**
	 * 
	 */
	public function update() 
	{
		// Get files
		@$this->makeRequest('GET', '/files/basic/api/myuserlibrary/feed', $this->endpointName);
		
		// Extract files from document
		$this->_extractFiles();
	}
}
?>
