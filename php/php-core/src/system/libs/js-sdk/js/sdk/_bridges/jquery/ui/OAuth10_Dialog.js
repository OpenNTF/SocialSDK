/*
 * © Copyright IBM Corp. 2012
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
 * Social Business Toolkit SDK.
 * 
 * Definition of a jQuery UI based dialog for OAuth 1.0.
 */
define(['jquery/ui'], function() {
	return {
		show: function(url) {
			var _title = "Authentication";
			var _isModal = false;
			var _width = 650;
			var _height = 700;
			var _autoOpen = true;
			var _resizeable = true;
			var _draggable = true;
			var frameUrl = url + "?loginUi=dialog";
			var d = jQuery("<iframe src='"+frameUrl+"' style='width: 650px; height=700px'></iframe>");
			jQuery.dialog({
				title : _title,
				modal: _isModal,
				width: _width,
				height: _height,
				resizeable: _resizeable,
				draggable: _draggable,
				buttons: {'Close': function(){ jQuery(this).dialog('close'); } },
				autoOpen : _autoOpen
			});
		}
	};
});
