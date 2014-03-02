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
 
dojo.provide("sbt.connections.controls.communities.CommunityMembersAction");

/**
 * 
 */
define(
		[ "../../../declare", "../../../controls/grid/GridAction",
				"sbt/connections/CommunityService",
				"sbt/connections/CommunityConstants" ],

		function(declare, GridAction, CommunityService, CommunityConstants) {

			/**
			 * @class ProfileAction
			 * @namespace sbt.connections.controls.communities
			 * @module sbt.connections.controls.communities.CommunityMembersAction
			 */
			var CommunityMembersAction = declare(
					GridAction,
					{

						/** Strings used in the actions */
						nls : {
							tooltip : "Go to ${name}"
						},

						/**
						 * ProfileAction Constructor function
						 * 
						 * @method constructor
						 */
						constructor : function() {
						},

						/**
						 * Handles displaying a tooltip for an item For
						 * profiles, the tooltip by default will be a business
						 * card So nothing is done in this function
						 * 
						 * @method getTooltip
						 * @param item
						 *            The element that will use the tooltip
						 */
						getTooltip : function(item) {

							// for default the semantic tag service will pop up
							// the business card
							// so do nothing here

						},

						/**
						 * The execute function is called from the handle click
						 * function For Profiles by default the business card
						 * functionality is used which works from the Semantic
						 * tag service so nothing is done here.
						 * 
						 * @method execute
						 * @param item
						 *            The item which fired the event
						 * @param opts
						 * @param event
						 *            The event
						 */
						execute : function(item, opts, event) {

							// for default the semantic tag service will pop up
							// the business card
							// so do nothing here
						},

						/**
						 * The removeMember function is called from the
						 * removeMember function and removes members from the
						 * community specified by currentCommunity
						 * 
						 * @method removeMember
						 * @param grid
						 *            The grid to update after the member has
						 *            been removed.
						 * @param communityService
						 *            The community service instance used to
						 *            remove the member.
						 * @param currentCommunity
						 *            The ID of the community from which to
						 *            remove the member.
						 * @param data
						 *            The data row from which the event was
						 *            fired.
						 */
						removeMember : function(grid, currentCommunity, data) {
							_removeMember(grid, currentCommunity, data);
						},
						
						/**
						 * Updates a community member.
						 * 
						 * @method updateMember
						 * @param grid
						 *            The grid to update.
						 * @param el
						 *            the element that fired the event
						 * @param data
						 *            all of the items from the current row of
						 *            the grid.
						 * @param ev
						 *            the event
						 * @param grid
						 *            The grid to update after the member has
						 *            been removed.
						 * 
						 * @param communityUuid
						 *            The ID of the community from which to
						 *            remove the member.
						 * @param data
						 *            The data row from which the event was
						 *            fired.
						 */
						updateMember : function(grid, el, data, ev, communityUuid) {
							var communityService = new CommunityService();
							var rbOwnerId = "rbOwner" + data.uid;
							var rbOwner = document.getElementById(rbOwnerId);
							communityService.getMembers(communityUuid).then(
									function(members) {
										for ( var i = 0; i < members.length; i++) {
											var member = members[i];
											if (member.getUserid() == data.uid) {
												if (rbOwner.checked) {
													member.setRole(CommunityConstants.Owner);
												} else {
													member.setRole(CommunityConstants.Member);
												}

												// Update community
												var promise = communityService.updateMember(
															communityUuid, member);
												
												promise.then(function(data) {
													grid.update(null);
												}, function(error) {
													console.log(error);
												});
												break;
										}
									}
								}, function(error) {
									console.log(error);
							});
						},
						
						/**
						 * Closes the edit form.
						 * 
						 * @method removeMember
						 * @param grid
						 *            The grid to update.
						 * @param el
						 *            the element that fired the event
						 * @param data
						 *            all of the items from the current row of
						 *            the grid.
						 * @param ev
						 *            the event
						 * @param grid
						 *            The grid to update after the member has
						 *            been removed.
						 * @param data
						 *            The data row from which the event was
						 *            fired.
						 */
						closeEditForm : function(grid, el, data, ev) {
							var id = "editTable" + data.uid;
			                document.getElementById(id).style.display = "none";
						},
						
						/**
						 * Opens the edit form.
						 * 
						 * @method openEditForm
						 * @param data
						 *            The data row from which the event was
						 *            fired.
						 */
						openEditForm : function(data) {
							var id = "editTable" + data.uid;
			                document.getElementById(id).style.display = "block";
						},
						

						/**
						 * Removes a community member.
						 * 
						 * @method removeMember
						 * @param grid
						 *            The grid to update.
						 * @param el
						 *            the element that fired the event
						 * @param data
						 *            all of the items from the current row of
						 *            the grid.
						 * @param ev
						 *            the event
						 * @param grid
						 *            The grid to update after the member has
						 *            been removed.
						 * 
						 * @param communityUuid
						 *            The ID of the community from which to
						 *            remove the member.
						 * @param data
						 *            The data row from which the event was
						 *            fired.
						 */
						removeMember : function(grid, el, data, ev, communityUuid) {
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
																		el.parentNode.removeChild(container);
																	},
																	function(error) {
																		console.log(error);
																	});
														},
														function(error) {
															console.log(error);
														});
												break;
											}
										}
									}, function(error) {
										console.log(error);
									});
						}
					});
			return CommunityMembersAction;
		});
