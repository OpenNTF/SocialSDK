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

dojo.provide('sbt.main');

/**
* @module sbt.main
*/
define([
    'sbt/Cache',
    'sbt/DebugTransport',
    'sbt/Endpoint',
    'sbt/ErrorTransport',
    'sbt/Gadget',
    'sbt/GadgetTransport',
    'sbt/IWidget',
    'sbt/Jsonpath',
    'sbt/MockTransport',
    'sbt/Portlet',
    'sbt/Promise',
    'sbt/Proxy',
    'sbt/_config',
    'sbt/compat',
    'sbt/config',
    'sbt/declare',
    'sbt/defer',
    'sbt/dom',
    'sbt/emailService',
    'sbt/i18n',
    'sbt/itemFactory',
    'sbt/json',
    'sbt/lang',
    'sbt/log',
    'sbt/pathUtil',
    'sbt/ready',
    'sbt/stringUtil',
    'sbt/text',
    'sbt/url',
    'sbt/util',
    'sbt/validate',
    'sbt/xml',
    'sbt/xpath',
    'sbt/xsl',
    'sbt/authenticator/Basic',
    'sbt/authenticator/GadgetOAuth',
    'sbt/authenticator/OAuth',
    'sbt/authenticator/SSO',
    'sbt/base/AtomEntity',
    'sbt/base/BaseConstants',
    'sbt/base/BaseEntity',
    'sbt/base/BaseService',
    'sbt/base/DataHandler',
    'sbt/base/JsonDataHandler',
    'sbt/base/VCardDataHandler',
    'sbt/base/XmlDataHandler',
    'sbt/base/core',
    'sbt/connections/ActivityConstants',
    'sbt/connections/ActivityService',
    'sbt/connections/ActivityStreamConstants',
    'sbt/connections/ActivityStreamService',
    'sbt/connections/BlogConstants',
    'sbt/connections/BlogService',
    'sbt/connections/BookmarkConstants',
    'sbt/connections/BookmarkService',
    'sbt/connections/CommunityConstants',
    'sbt/connections/CommunityService',
    'sbt/connections/ConnectionsConstants',
    'sbt/connections/ConnectionsService',
    'sbt/connections/FileConstants',
    'sbt/connections/FileService',
    'sbt/connections/FollowConstants',
    'sbt/connections/FollowService',
    'sbt/connections/ForumConstants',
    'sbt/connections/ForumService',
    'sbt/connections/ProfileAdminService',
    'sbt/connections/ProfileConstants',
    'sbt/connections/ProfileService',
    'sbt/connections/SearchConstants',
    'sbt/connections/SearchService',
    'sbt/connections/Tag',
    'sbt/connections/WikiConstants',
    'sbt/connections/WikiService',
    'sbt/data/AtomReadStore',
    'sbt/nls/Endpoint',
    'sbt/nls/ErrorTransport',
    'sbt/nls/loginForm',
    'sbt/nls/messageSSO',
    'sbt/nls/util',
    'sbt/nls/validate',
    'sbt/smartcloud/CommunityConstants',
    'sbt/smartcloud/ProfileConstants',
    'sbt/smartcloud/ProfileService',
    'sbt/smartcloud/SmartcloudConstants',
    'sbt/smartcloud/Subscriber',
    //'sbt/store/AtomStore',
    'sbt/store/parameter',
    'sbt/authenticator/nls/SSO',
    /*
    'sbt/authenticator/templates/ConnectionsLogin.html',
    'sbt/authenticator/templates/ConnectionsLoginDialog.html',
    'sbt/authenticator/templates/Message.html',
    'sbt/authenticator/templates/MessageDialogSSO.html',
    'sbt/authenticator/templates/MessageSSO.html',
    'sbt/authenticator/templates/login.html',
    'sbt/authenticator/templates/login',
    'sbt/authenticator/templates/loginDialog.html',
    'sbt/authenticator/templates/messageSSO',
    */
    'sbt/connections/nls/CommunityService',
    'sbt/connections/nls/ConnectionsService',
    'sbt/connections/nls/ProfileService'
],function() {
       return;
});
