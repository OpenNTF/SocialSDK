***REMOVED***
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
class SBTKCommunities extends BasePluginController {

	
	/**
	 * Creates a new community grid plugin.
	 * 
	 * @param unknown $args
	 * 
	 * @author Benjamin Jakobus
	 */
	public function createCommunityGrid($args) {
		$this->loadModel('SBTKSettings');
		$settings = new SBTKSettings();
		$communityGridSettings = $settings->getCommunityGridSettings();
		
		$viewData['hidePager'] = $communityGridSettings['hidePager'];
		$viewData['hideSorter'] = $communityGridSettings['hideSorter'];
		$viewData['containerType'] = $communityGridSettings['containerType'];
		
		$this->loadView('social/communities/community-grid', $viewData);
	}
	
	/**
	 * Creates a new my community invites grid.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createMyCommunityInvitesGrid($args) {
		$this->loadView('social/communities/my-community-invites-grid', array());
	}
	
	/**
	 * Creates a new my community invites grid.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createUpdateCommunityLogo($args) {
		$this->loadView('social/communities/update-community-logo', array());
	}
	
	/**
	 * Creates a new community members grid.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createCommunityMembersGrid($args) {
		// TODO: Remove hardcoded community Uuid
		$viewData['communityUuid'] = "c57821be-1511-4ba3-8284-cb773513a24b";

		$this->loadView('social/communities/community-members-grid', $viewData);
	}
	
	/**
	 * Creates a new plugin that allows the user to start a new community.
	 *
	 * @param unknown $args
	 *
	 * @author Benjamin Jakobus
	 */
	public function createStartCommunity($args) {
		$this->loadView('social/communities/start-new-community', array());
	}
}

