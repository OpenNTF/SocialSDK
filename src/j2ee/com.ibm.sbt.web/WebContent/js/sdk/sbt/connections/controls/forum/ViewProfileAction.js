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

define([ "../../../declare", "../../../controls/grid/GridAction","../../../i18n!./nls/ForumGridRenderer","../../../stringUtil"], 
        function(declare, GridAction, nls, stringUtil) {

    /**
     * @class ViewProfileAction
     * @namespace sbt.connections.controls.forum
     * @module sbt.connections.controls.forum.ViewProfileAction
     */
    var ViewProfileAction = declare(GridAction, {
       
    	
        /**ForumAction Constructor function
         * @method constructor
         * */
        constructor: function(args) {

        },
        
      
        openAuthorProfile: function(data,store,baseProfilesUrl){
        	var endpoint = store.getEndpoint();
        	var profileURL = endpoint.baseUrl + baseProfilesUrl +"/html/myProfileView.do?userid="+data.authorId;
        	document.location.href = profileURL;
        }

    });

    return ViewProfileAction;
});