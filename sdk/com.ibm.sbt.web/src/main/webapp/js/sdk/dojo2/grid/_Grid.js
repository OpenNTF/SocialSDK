/*
 * © Copyright IBM Corp. 2014
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
define([ "../../declare", "../../store/AtomStore", "dojo/_base/lang", "dojo/_base/Deferred", 
         "../../widget/_TemplatedWidget", "../../text!../../controls/grid/templates/Grid.html"], 
        function(declare, AtomStore, lang, Deferred, _TemplatedWidget, GridTemplate) {

    /*
     * @module sbt._bridge.grid._Grid
     */
    var _Grid = declare([ _TemplatedWidget ], {

        templateString: GridTemplate,
        
        /*
         * Creates an instance of an atom store.
         */
        _createDefaultStore: function(args) {
            return new AtomStore(args);
        },        
        
        /* 
         * Retrieves the data from the Atom Store
         */
        _doQuery: function(store, options, query) {
            query = query || {};
            var self = this;
            var errCallback = lang.hitch(this, this._updateWithError);
            var results = store.query(query, options);
            
            Deferred.when(results.total, function(totalCount) {
                Deferred.when(results, function(items) {
                    self.data = {
                     items : items,
                     start : options.start,
                     end : options.start + items.length,
                     count : items.length,
                     totalCount : totalCount,
                     response : results.response
                    };
                    try {
                        self.renderer.render(self, self.gridNode, items, self.data);
                        self.onUpdate(self.data);
                    } catch (error) {
                        self.renderer.renderError(self, self.gridNode, error);
                    }
                    return results;
                }, errCallback);
            }, errCallback);
        }
    
    });
    
    return _Grid;
});