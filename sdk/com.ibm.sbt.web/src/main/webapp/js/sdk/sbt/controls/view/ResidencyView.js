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

/**
 * 
 */
define([ "../../declare", "../../lang", "../../stringUtil", "../../log",
         "../../connections/controls/astream/ActivityStreamWrapper",
         "./TabbedBaseView"],
        function(declare, lang, stringUtil, log, ActivityStream, TabbedBaseView) {

    /*
     * @module sbt.controls.view.CommunitiesView
     */
    var ResidencyView = declare([ TabbedBaseView ], {

        title : "Residency PoC", 
    
        iconClass : "lotusIcon iconsComponentsBlue24 iconsComponentsBlue24-CommunitiesBlue24",
        
        searchArgs : { selectedApplication : "communities" },
        
        hideActions : true,
        
        hideActivityStream : false,
        
        activityStream : null,
        
        /**
         * 
         */
        postMixInProperties : function() {
            this.inherited(arguments);

        },

        /**
         * Post create function is called after widget has been created.
         * 
         * @method - postCreate
         */
        postCreate : function() {
            this.inherited(arguments);
            
            
            if (!this.activityStream && !this.hideActivityStream) {
                this.activityStream = new ActivityStream(this.args);
                this.setContent(this.activityStream);
            }
            
//            if (this.defaultActions) {
//            }

        }
        
    
        //
        // Internals
        //

    });

    return ResidencyView;
});