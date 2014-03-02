/*
 * � Copyright IBM Corp. 2013
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
 
dojo.provide("sbt.connections.controls.files.DownloadFileAction");

/**
 * 
 */
define('sbt/connections/controls/files/DownloadFileAction',[ "../../../declare", 
         "../../../controls/view/Action", 
         "./nls/files"], 
	function(declare, Action, nls) {

	/*
	 * @module sbt.connections.controls.communities.DownloadFileAction
	 */
	var DownloadFileAction = declare([ Action ], {
		
		name : nls.root.download,
		
		
		/**
		 * Constructor method for the BaseView.
		 * 
		 * @method constructor
		 * @param args
		 */
		constructor : function(args) {
			this.args = args;
		},

		/**
		 * Only enabled when at least on file is selected.
		 */
		isEnabled : function(selection, context) {
			return (selection.length > 0);
		},

		/**
		 * Perform the action on the specific selection. An action may be
		 * invoked multiple times with different selections or context
		 * parameters in the same lifetime.
		 * 
		 * @param selection
		 * @param context
		 * 
		 * @method execute
		 */
		execute : function(selection, context) {
			for (var i = 0; i < selection.length; i++) {			
				var src = selection[i].downloadUrl;	
				window.open(src);
				
			}
		}
		
	});

	return DownloadFileAction;
});
