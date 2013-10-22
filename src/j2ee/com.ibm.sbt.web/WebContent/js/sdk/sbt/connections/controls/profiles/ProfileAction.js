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
 * 
 */
define([ "../../../declare", 
         "../../../controls/grid/GridAction",
         "sbt/connections/CommunityService",
		 "sbt/connections/CommunityConstants"], 
         
function(declare, GridAction, CommunityService, CommunityConstants) {

    /**
     * @class ProfileAction
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ProfileAction
     */
    var ProfileAction = declare(GridAction, {
        
    	/**Strings used in the actions */
        nls: {
            tooltip: "Go to ${name}",
        },
        
        /**ProfileAction Constructor function
         * @method constructor
         * */
        constructor: function() {
        },
        
        /**
         * Handles displaying a tooltip for an item
         * For profiles, the tooltip by default will be a business card
         * So nothing is done in this function
         * @method getTooltip
         * @param item The element that will use the tooltip
         */
        getTooltip: function(item) {
        	
        	//for default the semantic tag service will pop up the business card
        	//so do nothing here

        },
        
        /**
         * The execute function is called from the handle click function
         * For Profiles by default the business card functionality is used
         * which works from the Semantic tag service so nothing is done here.
         * @method execute
         * @param item The item which fired the event
         * @param opts
         * @param event The event
         */
        execute: function(item, opts, event) {
        
        	//for default the semantic tag service will pop up the business card
        	//so do nothing here
        },
        
        /**
         * The removeMember function is called from the removeMember function
         * and removes members from the community specified by currentCommunity
         * @method removeMember
         * @param grid The grid to update after the member has been removed.
         * @param communityService The community service instance used to remove the member.
         * @param currentCommunity The ID of the community from which to remove the member.
         * @param data The data row from which the event was fired.
         */
        removeMember: function(grid, currentCommunity, data) {
        	_removeMember(grid, currentCommunity, data);
        },
        
      
        
        /**
         * The displayEditMemberForm function is called from the editMember function
         * and displays the edit members form.
         * @method displayEditMemberForm
         * @param grid The grid to update.
         * @param el the element that fired the event
         * @param data all of the items from the current row of the grid. 
         * @param ev the event 
         * @param grid The grid to update after the member has been removed.

         * @param communityUuid The ID of the community from which to remove the member.
         * @param data The data row from which the event was fired.
         */
        displayEditMemberForm: function(grid, el, data, ev, communityUuid) {
         	var container = document.createElement("div");
         	
        	var table = document.createElement("table");
         	container.appendChild(table);
         	
         	var tbody = document.createElement("tbody");
         	table.appendChild(tbody);
         	
         	var tr0 = document.createElement("tr");
         	tbody.appendChild(tr0);
         	
         	var td0 = document.createElement("td");
         	td0.setAttribute("style", "vertical-align: middle; padding: 0px 14px; width: 45px; font-weight: bold;");
         	td0.innerHTML = "Role:";
         	tr0.appendChild(td0);
         	
         	var td1 = document.createElement("td");
         	tr0.appendChild(td1);
         	
         	var table2 = document.createElement("table");
         	td1.appendChild(table2);
         	
         	var tbody2 = document.createElement("tbody");
         	td1.appendChild(tbody2);
        	
         	var tr1 = document.createElement("tr");
         	tbody2.appendChild(tr1);
         	
         	var tr2 = document.createElement("tr");
         	tbody2.appendChild(tr2);
         	
         	var td3 = document.createElement("td");
         	var td4 = document.createElement("td");
         	
         	tbody2.appendChild(tr1);
         	tbody2.appendChild(tr2);
         	
         	tr1.appendChild(td3);
         	tr2.appendChild(td4);
         	
         	var rbOwner = document.createElement("input");
         	rbOwner.type = "radio";
         	rbOwner.id = "rbOwner";
         	rbOwner.value = "owner";
         	rbOwner.name = "role";
         	td3.appendChild(rbOwner);
         	
        	var rbMember = document.createElement("input");
        	rbMember.type = "radio";
        	rbMember.id = "rbMember";
        	rbMember.value = "member";
        	rbMember.name = "role";
        	td4.appendChild(rbMember);
         	
         	var lbOwner = document.createElement("label");
         	lbOwner.innerHTML = "Owner";
         	lbOwner.setAttribute("for", "owner");
         	td3.appendChild(lbOwner);
         	
         	var lbMember = document.createElement("label");
         	lbMember.innerHTML = "Member";
         	lbMember.setAttribute("for", "member");
         	td4.appendChild(lbMember);
         	
         	// Table for input elements
         	var ctrTable = document.createElement("table");
         	container.appendChild(ctrTable);
         	
         	var ctrTbody = document.createElement("tbody");
         	ctrTable.appendChild(ctrTbody);
         	
         	var ctrTr = document.createElement("tr");
         	ctrTbody.appendChild(ctrTr);
         	
         	var ctrTd0 = document.createElement("td");
         	ctrTd0.setAttribute("style", "text-align: left;");
         	ctrTd0.class = "lotusFormFooter";
         	ctrTr.appendChild(ctrTd0);
         	
         	var btnSave = _initSaveActionListener(el, rbOwner, communityUuid, data, grid);
         	
         	var btnClose = _initCloseActionListener(el, container);
         	
         	var btnRemoveMember = _initRemoveActionListener(grid, communityUuid, data);
         	
         	ctrTd0.appendChild(btnSave);
         	ctrTd0.appendChild(btnClose);
         	ctrTd0.appendChild(btnRemoveMember);

        	var communityService = new CommunityService();
        	communityService.getMembers(communityUuid).then(
        	        function(members) {
        	        	for (var i = 0; i < members.length; i++) {
        	        		var member = members[i];
        	        		if (member.getUserid() == data.uid) {
        	        			if(member.getRole() == "owner") {
        	        				rbOwner.checked = 'checked';
        	        			} else {
        	        				rbMember.checked = 'checked';
        	        			}
        	        			el.parentNode.appendChild(container);
        	        			break;
        	        		}
        	        	}
        	        },
        	        function(error) {
        	            console.log(error);
        	        }
        	);
        },
        

        
    });
    
    /**
     * Removes members from the community specified by currentCommunity
     * @method _removeMember
     * @param grid The grid to update after the member has been removed.
     * @param communityService The community service instance used to remove the member.
     * @param currentCommunity The ID of the community from which to remove the member.
     * @param data The data row from which the event was fired.
     */
    function _removeMember(grid, currentCommunity, data) {
    	var communityService = new CommunityService();
    	
    	communityService.getMembers(currentCommunity).then(
    	        function(members) {
    	        	for (var i = 0; i < members.length; i++) {
    	        		var member = members[i];
    	        		if (member.getUserid() == data.uid) {
        	        		// Remove member
    	        			communityService.getCommunity(currentCommunity).then(
    	        					function(community) {
    	        						community.removeMember(member.getUserid(), {}).then(
    	        								function(memberId) {
    	        									grid.update(null);
    	        	        					},
    	        	        					function(error) {
    	        	        						console.log(error);
    	        	        					}
    	        						);
    	        					},
    	        					function(error) {
    	        						console.log(error);
    	        					}
    	        			);	        			
    	        			break;
    	        		}
    	        	}
    	        },
    	        function(error) {
    	            console.log(error);
    	        }
    	);
    }
    
    /**
     * Initializes the action listeners for the close button
     * (contained in the "edit members" form).
     * @method _initCloseActionListener
     * @param el The element that fired the event
     * @param container	The container which contains the "edit members" form 
     * 					(this is what we will close / remove).
     */
    function _initCloseActionListener(el, container) {
    	var btnClose = document.createElement("input"); 	
     	btnClose.type = "button";
     	btnClose.setAttribute("class", "lotusFormButton");
     	btnClose.value = "Cancel";
     	btnClose.onclick = function() {
     		el.parentNode.removeChild(container);
     	};
     	return btnClose;
    }
    
    /**
     * Initializes the action listeners for the save button
     * (contained in the "edit members" form).
     * @method _initSaveActionListener
     * @param el The element that fired the event
     * @param rbOwner
     * @param communityUuid
     * @param grid
     * @param data
     */
    function _initSaveActionListener(el, rbOwner, communityUuid, data, grid) {
    	var btnSave = document.createElement("input");
     	btnSave.type = "button";
     	btnSave.setAttribute("class", "lotusFormButton");
     	btnSave.value = "Save";
     	btnSave.onclick = function() {
        	var communityService = new CommunityService();
        	communityService.getMembers(communityUuid).then(
        	        function(members) {
        	        	for (var i = 0; i < members.length; i++) {
        	        		var member = members[i];
        	        		if (member.getUserid() == data.uid) {
        	        			if(rbOwner.checked) {
        	        				member.setRole(CommunityConstants.Owner);
        	        			} else {
        	        				member.setRole(CommunityConstants.Member);
        	        			}
        	
        	        			// Update community
        	        			 var promise = communityService.updateMember(communityUuid, member);        
        	        		        promise.then(
        	        		            function(data) {
        	        	        			grid.update(null);
        	        		            },
        	        		            function(error) {
        	        		                console.log(error);
        	        		            }
        	        		        );
        	        			break;
        	        		}
        	        	}
        	        },
        	        function(error) {
        	            console.log(error);
        	        }
        	);
     	};
     	return btnSave;
    }
    
    /**
     * Initializes the action listeners for the remove button
     * (contained in the "edit members" form).
     * @method _initRemoveActionListener
     * @param grid The grid to update.
     * @param communityUuid
     * @param data
     */
    function _initRemoveActionListener(grid, communityUuid, data) {
    	var btnRemoveMember = document.createElement("input");
     	btnRemoveMember.type = "button";
     	btnRemoveMember.setAttribute("class", "lotusFormButton");
     	btnRemoveMember.value = "Remove";
     	btnRemoveMember.onclick = function() {
     		_removeMember(grid, communityUuid, data);
     	};
     	return btnRemoveMember;
    }
    
    return ProfileAction;
});

