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
define([ "../../declare", "../../data/AtomReadStore", "../../widget/_TemplatedWidget","../../lang"], 
        function(declare, AtomReadStore, _TemplatedWidget,lang){
    /**
     * @module sbt.widget.grid._Grid
     */
    var _Grid = declare([ _TemplatedWidget ], {

        templatePath: dojo.moduleUrl("sbt", "controls/grid/templates/Grid.html"),
     
        /*
         * Creates an instance of an atom store.
         */
        _createDefaultStore: function(args) {
            return new AtomReadStore(args);
        },        
        
        /* 
         * Retrieves the data from the Atom Store
         */
        _doQuery: function(store, options) {
            var self = this;
            
            var handleError = function(error) {
                self.renderer.renderError(self, self.gridNode, error);
            };
            
            var handleComplete = function(items, args) {
                self.data = {
                    items : items,
                    start : options.start,
                    end : options.start + items.length,
                    count : items.length,
                    totalCount : store.totalResults,
                    response : store.response
                };
                try {
                    self.renderer.render(self, self.gridNode, items, self.data);
                    self.onUpdate(self.data);
                } catch (error) {
                    self.renderer.renderError(self, self.gridNode, error);
                }
            };
            
            var args = lang.mixin(options, {
                scope: this,
                onComplete: handleComplete,
                onError: handleError
            });
            
            store.fetch(args);
        }
    
    });
    
    return _Grid;
});