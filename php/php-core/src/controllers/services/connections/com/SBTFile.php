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
defined('SBT_SDK') OR exit('Access denied.');


/**
 * File encapsulation
 * 
 * @author Benjamin Jakobus
 */
class SBTFile
{
	private $id;
	private $label;	
	private $selfUrl;
	private $alternateUrl;
	private $downloadUrl;
	private $type;
	private $category;
	private $size;
	private $editLink;
	private $editMediaLink;
	private $thumbnailUrl;
	private $commentsUrl;
	private $author;
	private $title;
	
	/**
	 * Constructor.
	 * 
	 * @param string $endpointName
	 */
	function __construct()
	{
	}
	
	public function setFileId($id) 
	{
		$this->id = $id;
		return $this->id;
	}
	
	public function setLabel($label)
	{
		$this->label = $label;
		return $this->label;
	}
	
	public function getFileId()
	{
		return $this->id;
	}
	
	public function getLabel()
	{
		return $this->label;
	}
	
	public function getSelfUrl() 
	{
		return $this->selfUrl;
	}
	
	/**
	 * Returns the alternate URL
	 *
	 * @method getAlternateUrl
	 * @returns {String} alternate URL
	 */
	public function getAlternateUrl() 
	{
		return $this->alternateUrl;
	}
	
	/**
	 * Returns the download URL
	 *
	 * @method getDownloadUrl
	 * @returns {String} download URL
	 */
	public function getDownloadUrl() 
	{
		return $this->downloadUrl;
	}
	
	/**
	 * Returns the type
	 *
	 * @method getType
	 * @returns {String} type
	 */
	public function getType() 
	{
		return $this->type;
	}
	
	/**
	 * Returns the Category
	 *
	 * @method getCategory
	 * @returns {String} category
	 */
	public function getCategory() 
	{
		return $this->category;
	}
	
	/**
	 * Returns the size
	 *
	 * @method getSize
	 * @returns {Number} length
	 */
	public function getSize() 
	{
		return $this->size;
	}
	
	/**
	 * Returns the Edit Link
	 *
	 * @method getEditLink
	 * @returns {String} edit link
	 */
	public function getEditLink() 
	{
		return $this->editLink;
	}
	
	/**
	 * Returns the Edit Media Link
	 *
	 * @method getEditMediaLink
	 * @returns {String} edit media link
	 */
	public function getEditMediaLink() 
	{
		return $this->editMediaLink;
	}
	
	/**
	 * Returns the Thumbnail URL
	 *
	 * @method getThumbnailUrl
	 * @returns {String} thumbnail URL
	 */
	public function getThumbnailUrl() 
	{
		return $this->thumbnailUrl;
	}
	
	/**
	 * Returns the Comments URL
	 *
	 * @method getCommentsUrl
	 * @returns {String} comments URL
	 */
	public function getCommentsUrl() 
	{
		return $this->commentsUrl;
	}
	
	/**
	 * Returns the author
	 *
	 * @method getAuthor
	 * @returns array author
	 */
	public function getAuthor() 
	{
		return $this->author;
	}
	
	/**
	 * Returns the Title
	 *
	 * @method getTitle
	 * @returns {String} title
	 */
	public function getTitle() 
	{
		return $this->title;
	}
	
	/**
	 * Sets the selfUrl.
	 * 
	 * @param string $selfUrl
	 * @return $selfUrl
	 */
	public function setSelfUrl($selfUrl)
	{
		$this->selfUrl = $selfUrl;
		return $this->selfUrl;
	}
	
	/**
	 * Sets the alternateUrl.
	 * 
	 * @param string $alternateUrl
	 * @return $alternateUrl
	 */
	public function setAlternateUrl($alternateUrl)
	{
		$this->alternateUrl = $alternateUrl;
		return $this->alternateUrl;
	}
	
	/**
	 * Sets the downloadUrl.
	 *
	 * @param string $downloadUrl
	 * @return $downloadUrl
	 */
	public function setDownloadUrl($downloadUrl)
	{
		$this->downloadUrl = $downloadUrl;
		return $this->downloadUrl;
	}
	
	/**
	 * Sets the type.
	 *
	 * @param string $type
	 * @return $type
	 */
	public function setType($type)
	{
		$this->type = $type;
		return $this->type;
	}
	
	/**
	 * Sets the category.
	 *
	 * @param string $category
	 * @return $type
	 */
	public function setCategory($category)
	{
		$this->category = $category;
		return $this->category;
	}
	
	/**
	 * Sets the size.
	 *
	 * @param string $size
	 * @return $size
	 */
	public function setSize($size)
	{
		$this->size = $size;
		return $this->size;
	}
	
	/**
	 * Sets the edit link.
	 *
	 * @param string $editLink
	 * @return $editLink
	 */
	public function setEditLink($editLink)
	{
		$this->editLink = $editLink;
		return $this->editLink;
	}
	
	/**
	 * Sets the edit media link.
	 *
	 * @param string $editMediaLink
	 * @return $editMediaLink
	 */
	public function setEditMediaLink($editMediaLink)
	{
		$this->editMediaLink = $editMediaLink;
		return $this->editMediaLink;
	}
	
	/**
	 * Sets the thumbnail Url.
	 *
	 * @param string $thumbnailUrl
	 * @return $thumbnailUrl
	 */
	public function setThumbnailUrl($thumbnailUrl)
	{
		$this->thumbnailUrl = $thumbnailUrl;
		return $this->thumbnailUrl;
	}
	
	/**
	 * Sets the comments Url.
	 *
	 * @param string $commentsUrl
	 * @return $commentsUrl
	 */
	public function setCommentsUrl($commentsUrl)
	{
		$this->commentsUrl = $commentsUrl;
		return $this->commentsUrl;
	}
	
	/**
	 * Sets the author.
	 *
	 * @param array $author
	 * @return $author
	 */
	public function setAuthor($author)
	{
		$this->author = $author;
		return $this->author;
	}
	
	/**
	 * Sets the title.
	 *
	 * @param array $title
	 * @return $title
	 */
	public function setTitle($title)
	{
		$this->title = $title;
		return $this->title;
	}
	
}
?>
