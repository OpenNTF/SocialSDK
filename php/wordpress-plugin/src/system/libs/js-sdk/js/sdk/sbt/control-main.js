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
* distributed under the License is distributed on an AS IS BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
* implied. See the License for the specific language governing
* permissions and limitations under the License.
*/

/**
* @module sbt.main
*/
define([
    'sbt/connections/controls/ConnectionsGridRenderer',
    'sbt/connections/controls/ViewAllAction',
    'sbt/connections/controls/WidgetWrapper',
    'sbt/connections/controls/_ConnectionsWidget',
    'sbt/controls/dialog/Dialog',
    'sbt/controls/grid/Grid',
    'sbt/controls/grid/GridAction',
    'sbt/controls/grid/GridRenderer',
    'sbt/controls/panel/_ProfilePanel',
    'sbt/smartcloud/controls/BaseGridRenderer',
    'sbt/connections/controls/activities/ActivityAction',
    'sbt/connections/controls/activities/ActivityGrid',
    'sbt/connections/controls/activities/ActivityGridRenderer',
    'sbt/connections/controls/astream/ActivityStreamWrapper',
    'sbt/connections/controls/astream/_ActivityStream',
    'sbt/connections/controls/astream/_SbtAsConfigUtil',
    'sbt/connections/controls/astream/_XhrHandler',
    'sbt/connections/controls/bookmarks/BookmarkGrid',
    'sbt/connections/controls/bookmarks/BookmarkGridRenderer',
    'sbt/connections/controls/bootstrap/CommunityRendererMixin',
    'sbt/connections/controls/bootstrap/FileRendererMixin',
    'sbt/connections/controls/bootstrap/ProfileRendererMixin',
    'sbt/connections/controls/communities/CommunityAction',
    'sbt/connections/controls/communities/CommunityGrid',
    'sbt/connections/controls/communities/CommunityGridRenderer',
    'sbt/connections/controls/communities/CommunityMembersAction',
    'sbt/connections/controls/communities/CommunityMembersGrid',
    'sbt/connections/controls/communities/CommunityMembersGridRenderer',
    'sbt/connections/controls/files/FileAction',
    'sbt/connections/controls/files/FileGrid',
    'sbt/connections/controls/files/FileGridRenderer',
    'sbt/connections/controls/forums/BackAction',
    'sbt/connections/controls/forums/ForumAction',
    'sbt/connections/controls/forums/ForumGrid',
    'sbt/connections/controls/forums/ForumGridRenderer',
    'sbt/connections/controls/nls/ConnectionsGridRenderer',
    'sbt/connections/controls/nls/WidgetWrapper',
    'sbt/connections/controls/profiles/ColleagueGrid',
    'sbt/connections/controls/profiles/ColleagueGridRenderer',
    'sbt/connections/controls/profiles/EditProfilePhoto.html',
    'sbt/connections/controls/profiles/ProfileAction',
    'sbt/connections/controls/profiles/ProfileGrid',
    'sbt/connections/controls/profiles/ProfileGridRenderer',
    'sbt/connections/controls/profiles/ProfilePanel',
    'sbt/connections/controls/profiles/ProfileTagAction',
    'sbt/connections/controls/profiles/ProfileTagsGrid',
    'sbt/connections/controls/profiles/ProfileTagsGridRenderer',
    'sbt/connections/controls/search/SearchBox',
    'sbt/connections/controls/search/SearchBoxRenderer',
    'sbt/connections/controls/search/SearchGrid',
    'sbt/connections/controls/search/SearchGridRenderer',
    'sbt/connections/controls/sharebox/InputFormWrapper',
    'sbt/connections/controls/sharebox/_InputForm',
    'sbt/connections/controls/templates/FileGridWrapperContent.html',
    'sbt/connections/controls/templates/LoadingPage.html',
    'sbt/connections/controls/templates/ProfileCardWrapperContent.html',
    'sbt/connections/controls/templates/WidgetFrame.html',
    'sbt/connections/controls/vcard/CommunityVCard',
    'sbt/connections/controls/vcard/ProfileVCard',
    'sbt/connections/controls/vcard/ProfileVCardInline',
    'sbt/connections/controls/vcard/SemanticTagService',
    'sbt/connections/controls/wrappers/FileGridWrapper',
    'sbt/connections/controls/wrappers/ProfileCardWrapper',
    'sbt/controls/dialog/nls/dialog',
    'sbt/controls/dialog/templates/Dialog.html',
    'sbt/controls/grid/bootstrap/GridRendererMixin',
    'sbt/controls/grid/templates/Grid.html',
    'sbt/controls/grid/templates/GridFooter.html',
    'sbt/controls/grid/templates/GridPager.html',
    'sbt/controls/grid/templates/GridSorter.html',
    'sbt/controls/grid/templates/SortAnchor.html',
    'sbt/smartcloud/controls/nls/BaseGridRenderer',
    'sbt/smartcloud/controls/profiles/ColleagueGrid',
    'sbt/smartcloud/controls/profiles/ColleagueGridRenderer',
    'sbt/smartcloud/controls/profiles/ProfileAction',
    'sbt/smartcloud/controls/profiles/ProfileGrid',
    'sbt/smartcloud/controls/profiles/ProfileGridRenderer',
    'sbt/smartcloud/controls/profiles/ProfilePanel',
    'sbt/connections/controls/activities/nls/ActivityGridRenderer',
    'sbt/connections/controls/activities/templates/ActivityRow.html',
    'sbt/connections/controls/astream/templates/ActivityStreamContent.html',
    'sbt/connections/controls/bookmarks/nls/BookmarkGridRenderer',
    'sbt/connections/controls/bookmarks/templates/BookmarkListItem.html',
    'sbt/connections/controls/bookmarks/templates/BookmarkRow.html',
    'sbt/connections/controls/bookmarks/templates/TagAnchor.html',
    'sbt/connections/controls/bootstrap/templates/CommunityRow.html',
    'sbt/connections/controls/bootstrap/templates/FileRow.html',
    'sbt/connections/controls/bootstrap/templates/ProfileRow.html',
    'sbt/connections/controls/bootstrap/templates/TagAnchor.html',
    'sbt/connections/controls/communities/nls/CommunityGridRenderer',
    'sbt/connections/controls/communities/nls/CommunityMembersGridRenderer',
    'sbt/connections/controls/communities/templates/CommunityMemberRow.html',
    'sbt/connections/controls/communities/templates/CommunityRow.html',
    'sbt/connections/controls/communities/templates/TagAnchor.html',
    'sbt/connections/controls/files/nls/FileGridRenderer',
    'sbt/connections/controls/files/templates/CommentRow.html',
    'sbt/connections/controls/files/templates/FileRow.html',
    'sbt/connections/controls/files/templates/FolderRow.html',
    'sbt/connections/controls/files/templates/RecycledFileRow.html',
    'sbt/connections/controls/forums/nls/ForumGridRenderer',
    'sbt/connections/controls/forums/templates/ForumRow.html',
    'sbt/connections/controls/forums/templates/MyTopicsBreadCrumb.html',
    'sbt/connections/controls/forums/templates/ReplyBreadCrumb.html',
    'sbt/connections/controls/forums/templates/ReplyHeader.html',
    'sbt/connections/controls/forums/templates/ReplyRow.html',
    'sbt/connections/controls/forums/templates/TableHeader.html',
    'sbt/connections/controls/forums/templates/TopicBreadCrumb.html',
    'sbt/connections/controls/forums/templates/TopicHeader.html',
    'sbt/connections/controls/forums/templates/TopicRow.html',
    'sbt/connections/controls/profiles/nls/ColleagueGridRenderer',
    'sbt/connections/controls/profiles/nls/ProfileGridRenderer',
    'sbt/connections/controls/profiles/nls/ProfileTagsGridRenderer',
    'sbt/connections/controls/profiles/templates/ColleagueItem.html',
    'sbt/connections/controls/profiles/templates/ColleagueItemFull.html',
    'sbt/connections/controls/profiles/templates/ColleagueRow.html',
    'sbt/connections/controls/profiles/templates/CommunityMemberRow.html',
    'sbt/connections/controls/profiles/templates/ProfilePanel.html',
    'sbt/connections/controls/profiles/templates/ProfileRow.html',
    'sbt/connections/controls/profiles/templates/SharedConnectionsRow.html',
    'sbt/connections/controls/profiles/templates/StatusUpdateRow.html',
    'sbt/connections/controls/profiles/templates/TagListHeader.html',
    'sbt/connections/controls/profiles/templates/TagListRow.html',
    'sbt/connections/controls/profiles/templates/ViewAll.html',
    'sbt/connections/controls/search/nls/SearchBoxRenderer',
    'sbt/connections/controls/search/nls/SearchGridRenderer',
    'sbt/connections/controls/search/templates/BookmarkBody.html',
    'sbt/connections/controls/search/templates/CalendarBody.html',
    'sbt/connections/controls/search/templates/CommunityBody.html',
    'sbt/connections/controls/search/templates/DefaultBody.html',
    'sbt/connections/controls/search/templates/DefaultHeader.html',
    'sbt/connections/controls/search/templates/DefaultSummary.html',
    'sbt/connections/controls/search/templates/MemberListItemTemplate.html',
    'sbt/connections/controls/search/templates/MemberListTemplate.html',
    'sbt/connections/controls/search/templates/NoResults.html',
    'sbt/connections/controls/search/templates/PersonCard.html',
    'sbt/connections/controls/search/templates/PopUpTemplate.html',
    'sbt/connections/controls/search/templates/ProfileBody.html',
    'sbt/connections/controls/search/templates/ProfileHeader.html',
    'sbt/connections/controls/search/templates/SearchBoxTemplate.html',
    'sbt/connections/controls/search/templates/SearchSuggestTemplate.html',
    'sbt/connections/controls/search/templates/SingleApplicationSearch.html',
    'sbt/connections/controls/search/templates/SingleSearchPopUp.html',
    'sbt/connections/controls/search/templates/StatusUpdateExtraHeader.html',
    'sbt/connections/controls/search/templates/StatusUpdateHeader.html',
    'sbt/connections/controls/search/templates/SuggestPopUpTemplate.html',
    'sbt/connections/controls/search/templates/a.html',
    'sbt/connections/controls/search/templates/div.html',
    'sbt/connections/controls/search/templates/em.html',
    'sbt/connections/controls/search/templates/img.html',
    'sbt/connections/controls/search/templates/li.html',
    'sbt/connections/controls/search/templates/span.html',
    'sbt/connections/controls/search/templates/td.html',
    'sbt/connections/controls/search/templates/tr.html',
    'sbt/connections/controls/search/templates/ul.html',
    'sbt/connections/controls/sharebox/templates/InputFormContent.html',
    'sbt/connections/controls/vcard/templates/CommunityVCard.html',
    'sbt/connections/controls/vcard/templates/ProfileVCard.html',
    'sbt/connections/controls/vcard/templates/ProfileVCardInline.html',
    'sbt/controls/grid/bootstrap/templates/GridPager.html',
    'sbt/controls/grid/bootstrap/templates/GridSorter.html',
    'sbt/controls/grid/bootstrap/templates/SortAnchor.html',
    'sbt/smartcloud/controls/profiles/nls/ColleagueGridRenderer',
    'sbt/smartcloud/controls/profiles/nls/ProfileGridRenderer',
    'sbt/smartcloud/controls/profiles/templates/ColleagueItem.html',
    'sbt/smartcloud/controls/profiles/templates/ColleagueItemFull.html',
    'sbt/smartcloud/controls/profiles/templates/CommunityMember.html',
    'sbt/smartcloud/controls/profiles/templates/ProfilePanel.html',
    'sbt/smartcloud/controls/profiles/templates/ProfileRow.html',
    'sbt/smartcloud/controls/profiles/templates/ViewAll.html'
],function() {
       return;
});
