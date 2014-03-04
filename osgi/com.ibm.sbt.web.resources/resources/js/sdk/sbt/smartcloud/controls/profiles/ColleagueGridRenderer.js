

dojo.provide("sbt.smartcloud.controls.profiles.ColleagueGridRenderer");

define(["../../../declare",
        "../BaseGridRenderer",
        "../../../text!./templates/ColleagueItem.html",
        "../../../stringUtil",
        "../../../lang",
        "../../../dom",
        "./nls/ColleagueGridRenderer",
        "../../../text!./templates/ColleagueItemFull.html",
        "../../../text!./templates/ViewAll.html"], 
		
function(declare, BaseGridRenderer, ColleagueItemTemplate, stringUtil, lang, dom, nls, colleagueItemFullTemplate, viewAllTemplate){
	
	var ColleagueGridRenderer = declare(BaseGridRenderer, {
		
		_nls: nls,
		
		containerClass: "lotusChunk",
		
		/** The template used to construct a photo url  */
        contactImageUrl: "{baseUrl}/contacts/img/photos/{photo}",
        
        /** The template used to construct a no photo url*/
        noContactImageUrl: "{baseUrl}/contacts/img/noContactImage.gif",
        
        /**The table row template*/
		template: ColleagueItemTemplate,
		
		fullTemplate: colleagueItemFullTemplate,
		
		viewAllTemplate: viewAllTemplate,
		
		/**The HTML template to show paging */
		pagerTemplate : null,
       
		/**The HTML template to show sorting options*/
        sortTemplate : null,
        
        /**The HTML template for sort Anchors*/
        sortAnchor : null,
		
		/**
         * The constructor function
         * @method constructor
         * @param args
         */
        constructor: function(args) {
        },
        
        /**
         * @param grid
         * @param item
         * @param i
         * @param items
         * @returns {String}
         */
        photoUrl: function(grid, item, i, items) {
        	var ep = grid.store.getEndpoint();
       		if (!ep) return null;
       	 
       		var photos = item.getValue("photos");
       		if (photos && lang.isArray(photos) && photos.length > 1) {
       			return stringUtil.replace(this.contactImageUrl, { baseUrl : ep.baseUrl , photo : photos[1] });
       		}else{
       			return stringUtil.replace(this.noContactImageUrl, { baseUrl : ep.baseUrl });
       		}
        },
        
        render: function(grid, el, items, data) {
            while (el.childNodes[0]) {
                dom.destroy(el.childNodes[0]);
            }
            var size = items.length;
            if (size === 0) {
               this.renderEmpty(grid, el);
            }
            else {
               var container = this.renderContainer(grid, el, items, data);
               for (var i=0; i<items.length; i++) {
                   this.renderItem(grid, container, data, items[i], i, items);
               }
               this.renderViewAll(grid, el, items, data);
            }
         },
         
         renderContainer: function(grid, el, items, data) {          
             return dom.create("div", { "class": this.containerClass }, el);
         },
         
         renderViewAll: function(grid, el, items, data){
        	if (this.viewAllTemplate && !grid.hideViewAll) {
                var node;
                if (lang.isString(this.viewAllTemplate)) {
                    var domStr = this._substituteItems(this.viewAllTemplate, grid, this, items, data);
                    node = dom.toDom(domStr, el.ownerDocument);
                } else {
                    node = this.sortTemplate.cloneNode(true);
                }
                el.appendChild(node);
                
                grid._doAttachEvents(el, data);
            }
         },
         
         viewAllTitle: function(grid, renderer, items, data) {
            var str = (data.totalCount == 1) ? this.nls.root.person : this.nls.root.people;
            var totalCount = stringUtil.replace(str, { totalCount : data.totalCount });
            return stringUtil.replace(nls.root.viewAll, { totalCount : totalCount });
         }
		
	});
	return ColleagueGridRenderer;
});