define(["../../../declare",
        "../../../controls/grid/Grid",
        "../../../store/parameter",
        "../../../connections/BookmarkConstants",
        "./BookmarkGridRenderer"], 
    function(declare,Grid,parameter,consts,BookmarkGridRenderer){
	
	var sortVals = {
			date: "created",
            popularity: "popularity"
	};
	
	var ParamSchema = {	
		pageNumber: parameter.oneBasedInteger("page"),	
		pageSize: parameter.oneBasedInteger("ps"),
		sortBy: parameter.sortField("sortBy",sortVals),
		sortOrder: parameter.sortOrder("sortOrder")			
	};
	
	var BookmarkGrid = declare(Grid,{
		
		 options : {
	            "any" : {
	                storeArgs : {
	                    url : consts.AtomBookmarksAll,
	                    attributes : consts.BookmarkXPath,
	                    feedXPath : consts.BookmarkFeedXPath,
	                    paramSchema : ParamSchema
	                },
	                rendererArgs : null
	            }
	     },
	        
	     /**
	      * Default grid option.
	      */
	     defaultOption: "any", 
	     constructor: function(args){
	    	 
	    	var nls = this.renderer.nls;
            this._sortInfo = {
                date: { 
                    title: nls.date, 
                    sortMethod: "sortByDate",
                    sortParameter: "date"
                },
                popularity: {
                    title: nls.popularity, 
                    sortMethod: "sortByPopularity",
                    sortParameter: "popularity"
                }
            },
            this._activeSortAnchor = this._sortInfo.date;
            this._activeSortIsDesc = true;
	     }, 
	     
	     buildUrl: function(url, args) {
	    	 var urlParams;
	    	 
	    	 if(this.type == "private"){
	    		 urlParams = { access: "private"};
	    	 }else if(this.type == "public") {
	    		 urlParams = {access: "public"};
	    	 }else{
	    		 urlParams = {access: "any"};
	    	 }

	          return this.constructUrl(url, urlParams, {});
	        },
	     
	     createDefaultRenderer: function(args){
	    	 return new BookmarkGridRenderer(args);
	     },
	     
	     /**
         * Gets sorting information, such as
         * if the results are ascending or descending, and the sort anchors
         * @method getSortInfo
         * @returns An object containing sorting information
         */
        getSortInfo: function() {
            return {
                active: {
                    anchor: this._activeSortAnchor,
                    isDesc: this._activeSortIsDesc
                },
                list: [this._sortInfo.date, this._sortInfo.popularity]
            };
        },
	     
	    /**
         * Filter the bookmarks by specified tag
         * @param el
         * @param data
         * @param ev
         */
        filterByTag: function(el, data, ev){
        	this._stopEvent(ev);
            
        	var options = {
            	tag: el.text
            };

        	this._filter(options,data);
        },
        
        sortByDate: function(el, data, ev){
        	this._sort("date", true, el, data, ev);
        },
        
        sortByPopularity: function(el, data, ev){
        	this._sort("popularity", true, el, data, ev);
        },
        
        handleClick: function(el, data, ev){
        	
        	this._stopEvent(ev);
        	this.bookmarkAction.execute(data);
        },
        
        bookmarkAction: {
        	
        	getTooltip: function(item){
        		return item.getValue("title");
        	},
        	
        	
        	execute: function(data){
        		var url = data.getValue("url");
        		window.location.assign(url);
        	}
        },
	
	});
	
	
	return BookmarkGrid;
	
});