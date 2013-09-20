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
 * The Activities application of IBM® Connections enables a team to collect, organize, share, and reuse work related to a project goal. 
 * The Activities API allows application programs to create new activities, and to read and modify existing activities.
 * 
 * @module sbt.connections.ActivityService
 */
define([ "../declare", "../config", "../lang", "../stringUtil", "../Promise", "./ActivityConstants", "../base/BaseService",
         "../base/BaseEntity", "../base/XmlDataHandler" ], 
    function(declare,config,lang,stringUtil,Promise,consts,BaseService,BaseEntity,XmlDataHandler) {

    /**
     * Activity class represents an entry for a activities feed returned by the
     * Connections REST API.
     * 
     * @class Activity
     * @namespace sbt.connections
     */
    var Activity = declare(BaseEntity, {
        
        /**
         * Construct a Result entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of id from result ATOM
         * entry document.
         * 
         * @method getId
         * @return {String} ID of the result
         */
        getId : function() {
            return this.getAsString("id");
        },

        /**
         * Return the value of  from activity ATOM
         * entry document.
         * 
         * @method getActivityId
         * @return {String}  of the activity
         */
        getActivityId : function() {
            return this.getAsString("activity");
        },

        /**
         * Return the value of position from activity ATOM
         * entry document.
         * 
         * @method getPosition
         * @return {String}  of the activity
         */
        getPosition : function() {
            return this.getAsNumber("position");
        },

        /**
         * Return the value of depth from activity ATOM
         * entry document.
         * 
         * @method getDepth
         * @return {String}  of the activity
         */
        getDepth : function() {
            return this.getAsNumber("depth");
        },

        /**
         * Return the value of permissions from activity ATOM
         * entry document.
         * 
         * @method getPermissions
         * @return {String}  of the activity
         */
        getPermissions : function() {
            return this.getAsString("permissions");
        },

        /**
         * Return the value of icon from activity ATOM
         * entry document.
         * 
         * @method getIcon
         * @return {String}  of the activity
         */
        getIcon : function() {
            return this.getAsString("icon");
        },

        /**
         * Return the value of IBM Connections activity title from activity
         * ATOM entry document.
         * 
         * @method getTitle
         * @return {String} Activity title of the activity
         */
        getTitle : function() {
            return this.getAsString("title");
        },

        /**
         * Sets title of IBM Connections activity.
         * 
         * @method setTitle
         * @param {String} title Title of the activity
         */
        setTitle : function(title) {
            return this.setAsString("title", title);
        },
        
        /**
         * Return the value of IBM Connections activity description from
         * activity ATOM entry document.
         * 
         * @method getContent
         * @return {String} Activity description of the activity
         */
        getContent : function() {
            return this.getAsString("content");
        },

        /**
         * Sets description of IBM Connections activity.
         * 
         * @method setContent
         * @param {String} content Description of the activity
         */
        setContent : function(content) {
            return this.setAsString("content", content);
        },

        /**
         * Return tags of IBM Connections activity from activity ATOM entry
         * document.
         * 
         * @method getTags
         * @return {Object} Array of tags of the activity
         */
        getTags : function() {
            return this.getAsArray("tags");
        },

        /**
         * Set new tags to be associated with this IBM Connections activity.
         * 
         * @method setTags
         * @param {Object} Array of tags to be added to the activity
         */

        setTags : function(tags) {
            return this.setAsArray("tags", tags);
        },

        /**
         * Gets an author of IBM Connections activity.
         * 
         * @method getAuthor
         * @return {Member} author Author of the activity
         */
        getAuthor : function() {
            return this.getAsObject([ "authorUserid", "authorName", "authorEmail" ]);
        },

        /**
         * Gets a contributor of IBM Connections activity.
         * 
         * @method getContributor
         * @return {Member} contributor Contributor of the activity
         */
        getContributor : function() {
            return this.getAsObject([ "contributorUserid", "contributorName", "contributorEmail" ]);
        },
        
        /**
         * Return the published date of the IBM Connections activity from
         * activity ATOM entry document.
         * 
         * @method getPublished
         * @return {Date} Published date of the Activity
         */
        getPublished : function() {
            return this.getAsDate("published");
        },

        /**
         * Return the last updated date of the IBM Connections activity from
         * activity ATOM entry document.
         * 
         * @method getUpdated
         * @return {Date} Last updated date of the Activity
         */
        getUpdated : function() {
            return this.getAsDate("updated");
        }
        
    });
    
    /*
     * Callbacks used when reading a feed that contains activities entries.
     */
    var ActivityFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ActivitiesFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
                service : service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.ActivityXPath
            });
            return new Activity({
                service : service,
                dataHandler : entryHandler
            });
        }
    };

    /**
     * ActivitiesService class.
     * 
     * @class ActivitiesService
     * @namespace sbt.connections
     */
    var ActivityService = declare(BaseService, {
        
        contextRootMap: {
            activities: "activities"
        },

        /**
         * Constructor for ActivitiesService
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
            if (!this.endpoint) {
                this.endpoint = config.findEndpoint(this.getDefaultEndpointName());
            }
        },

        /**
         * Return the default endpoint name if client did not specify one.
         * @returns {String}
         */
        getDefaultEndpointName: function() {
            return "connections";
        },
        
        /**
         * Get a list of all active activities that match a specific criteria.
         * 
         * @method getMyActivities
         * @param requestArgs
         */
        getMyActivities: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.AtomActivitiesMy, options, ActivityFeedCallbacks);
        } ,      
        
        /**
         * Search for content in all of the activities, both completed and active, that matches a specific criteria.
         * 
         * @method getAllActivities
         * @param requestArgs
         */
        getAllActivities: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.AtomActivitiesEverything, options, ActivityFeedCallbacks);
        },      

        /**
         * Search for a set of completed activities that match a specific criteria.
         * 
         * @method getCompletedActivities
         * @param requestArgs
         */
        getCompletedActivities: function(requestArgs) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : requestArgs || {}
            };
            
            return this.getEntities(consts.AtomActivitiesCompleted, options, ActivityFeedCallbacks);
        }        

    });
    return ActivityService;
});
