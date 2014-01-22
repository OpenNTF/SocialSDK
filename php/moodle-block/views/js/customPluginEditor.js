/**	
 * (C) Copyright IBM Corp. 2012
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
 * @author Benjamin Jakobus 
 */


	
	
	function type_change() {
		
		var html_editor = document.getElementById("id_config_customHTML");
		var javascript_editor = document.getElementById("id_config_customCode");
		var script_list = document.getElementById("id_config_type");
		var selected_script = script_list.options[script_list.selectedIndex].value;

		var html = "";
		var javascript = "";
		if (selected_script == "Custom") {
			html = document.getElementById("html_custom").value;
			javascript = document.getElementById("javascript_custom").value;
		} else {
			html = document.getElementById("html_/core/views/" + selected_script + ".php").value;
			javascript = document.getElementById("javascript_/core/views/" + selected_script + ".php").value;
		}
		htmlEditor.setValue(html);
		jsEditor.setValue(javascript);
	}
