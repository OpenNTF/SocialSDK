define(["../../../../declare", "../../../../dom", "../../../../lang", "../../../../json", 
        "../../../../config", "../../../../url", "../../../../widget/_TemplatedWidget", 
        "../../../../text!./templates/ConfigOverlay.html",
        "../../../../connections/controls/search/SearchBox"], 
        function(declare, dom, lang, json, config, Url, _TemplatedWidget, template, SearchBox){
    var configExtension = declare([_TemplatedWidget], {
        templateString: template,
        backgroundColor: "yellow",
        height: "100%",
        width: "100%",
        eventName: null,
        _endpoint: null,
        _profileIds: [],
        _communityIds: [],
        _tags: [],
        _searchBox: null,
        
        constructor: function(args){
        	this.endpoint = config.findEndpoint("connections");
        	this.eventName = args.eventName;
        	
        	this._searchBox = new SearchBox({
        	    type: "full",
        	    searchSuggest: "on",
        	    selectedApplication: "profiles",
        	    searchType: "public"
        	});
        	var self = this;
        	lang.mixin(this._searchBox.searchBoxAction, {
        	    handleSuggestResult: function(results,context,popUp,searchType){
                    while (popUp.firstChild) { 
                        popUp.removeChild(popUp.firstChild); 
                    }
                    for(var i=0;i<results.length;i++){
                        var data = document.createElement("li");
                        var title = results[i].getTitle();
                        var id="";
                        if(searchType=="myCommunities"){
                            id = results[i].getCommunityUuid();
                            data['data-id-type'] = "community";
                        }else{
                            id = results[i].getId();
                            if(document.getElementById("searchAppType").innerText=="Communities"){
                                data['data-id-type'] = "Community";
                            }else if(document.getElementById("searchAppType").innerText=="Profiles"){
                                data['data-id-type'] = "Profile";
                            }else{
                                data['data-id-type'] = "tag";
                            }
                            
                        }
                        data['data-id-name'] = title;
                        dom.setText(data, title);
                        data.id = id;
                        
                        data.onclick = function (event) {
                            var input = self._searchBox._searchInput;
                            input.value = "";
                            self.addToFilters(this.id,  this['data-id-name'], this['data-id-type']);
                        };

                        popUp.appendChild(data);
                    }
                },
                search: function(event, context){
                    self.addTag();
                }
        	});
        },
        
        postMixInProperties: function(){
        	
        },
        
        postCreate: function(){
        	dojo.forEach(this._profileIds, dojo.hitch(this, function(profileObj){
        		this._addToUL(profileObj.id, profileObj.name, profileObj.type, this.profileIds);
        	}));
        	dojo.forEach(this._communityIds, dojo.hitch(this, function(communityObj){
        	    this._addToUL(communityObj.id, communityObj.name, communityObj.type, this.communityIds);
            }));
        	dojo.forEach(this._tags, dojo.hitch(this, function(tag){
        	    var li = dojo.create("li", null, this.tags);
                dom.setText(li, tag);
            }));
        	
            this.searchBoxAP.appendChild(this._searchBox.domNode);
        },
        
        /**
         * check the array contains the given id.
         * 
         * @method checkContainsId
         */
        checkContainsId : function(array, id){
            dojo.forEach(array, dojo.hitch(this, function(obj){
                if(obj.id == id){
                    return true;
                }
            }));
            return false;
        },
        
        /**
         * Remove a community id from the profile ids store.
         * 
         * @method removeProfileId
         */
        removeProfileId : function(id){
            this._profileIds = dojo.filter(this._profileIds, function(item){
                return item.id != id;
            });
        },
        
        /**
         * Remove a community id from the community ids store.
         * 
         * @method removeCommunityId
         */
        removeCommunityId : function(id){
            this._communityIds = dojo.filter(this._communityIds, function(item){
                return item.id != id;
            });
        },
        
        /**
         * Remove a community id from the community ids store.
         * 
         * @method removeCommunityId
         */
        removeTag : function(targetTag){
            this._tags = dojo.filter(this._tags, function(tag){
                return tag != targetTag;
            });
        },
        
        /*
         * Returns the button which can be used to delete the filter from the overlay and 
         * from the array of filters.
         * 
         * @method _createDeletebutton
         */
        _createDeleteButton : function(type, idOrTag){
            var deleteButton = document.createElement("a");
            deleteButton.href = "#";
            
            var img =document.createElement("img");
            img.src = config.Properties.sbtUrl + "/sbt/connections/controls/astream/extensions/images/black-x.gif";
            
            deleteButton.id = idOrTag;
            img.id = idOrTag;
            
            var removalMethod;
            if(type == "Profile"){
                removalMethod = dojo.hitch(this, "removeProfileId");
            }
            else if(type == "Community"){
                removalMethod = dojo.hitch(this, "removeCommunityId");
            }else{
                removalMethod = dojo.hitch(this, "removeTag");
            }
            deleteButton.appendChild(img);
            
            dojo.connect(deleteButton, "onclick", function(evt){
                var target = evt.target;
                removalMethod(target.id || target);
                var current = target;
                var parent = target.parentNode;
                while(parent.tagName!="UL"){
                    parent = parent.parentNode;
                    current = current.parentNode;
                }
                parent.removeChild(current);
            });
            
            return deleteButton;
        },
        
        /*
         * Internal method used to add a list item to the overlay lists.
         * 
         * @method _addToUrl
         */
        _addToUL : function(id, name, type, UL){
            var li = dojo.create("li", null, UL);
            var deleteButton = this._createDeleteButton(type, id);
            li.appendChild(deleteButton);
            
            var p = document.createElement("p");
            dom.setText(p, name);
            li.appendChild(p);
        },
        
        /**
         * Add the id to the proper list, internally and in the overlay.
         * 
         * @method addToFilters
         */
        addToFilters : function(id, name, type){
    		if(type == "Profile" && id && !this.checkContainsId(this._profileIds, id)){
    			this._profileIds.push({
    			    id: id,
    			    name: name,
    			    type: type
    			});
    			this._addToUL(id, name, type, this.profileIds);
			    
    		}else if(type == "Community" && id && !this.checkContainsId(this._communityIds, id)){
                this._communityIds.push({
                    id: id,
                    name: name,
                    type: type
                });
                this._addToUL(id, name, type, this.communityIds);
            }else{
                this.addTag();
            }
        },
        
        /**
         * Close the configuration overlay and reload the stream with the filters in place.
         * 
         * @method saveConfig
         */
        saveConfig : function(){
        	var url = this.getFilterUrl();
        	dojo.publish(this.eventName, [{filters:{}, url:url}]);
        },
        
        /**
         * Get the url needed to get the activitystream filtered by the 
         * currently selected ids and tags.
         * 
         * @method getFilterUrl
         */
        getFilterUrl: function(){
        	var filters = [];
        	if(this._profileIds.length > 0){
        		var profileFilters = {
        			type:'actor',
        			values: []
        		};
        		dojo.forEach(this._profileIds, function(id, index){
            	    profileFilters.values.push(id.id);
                });
        		filters.push(profileFilters);
        	}
        	
        	if(this._communityIds.length > 0){
        		var communityFilters = {
        			type:'community',
        			values: []
                };
        		dojo.forEach(this._communityIds, function(id, index){
        			communityFilters.values.push(id.id);
                });
        		filters.push(communityFilters);
        	}
	    	
            if(this._tags.length > 0){
            	var tagFilters = {
        			type:'tag',
        			values: []
                };
            	dojo.forEach(this._tags, function(tag, index){
    			    tagFilters.values.push(tag);
    			});
            	filters.push(tagFilters);
        	}
        	
            filters = json.stringify(filters).replace(/\{/g, "%7B").replace(/\}/g, "%7D").replace(/ /g, "%7D");
        	var currUrl = document.getElementById("com_ibm_social_as_feed_FeedLink_0").firstChild.href;
        	var url = new Url(currUrl);
        	var newQuery = "rollup=true" + (filters ? "&filters=" + filters : "");
        	url.setQuery(newQuery);
        	
        	return url.getUrl();
        },
        
        /**
         * Add a tag to the inner tag list and to the tags li.
         * 
         * @method addTag
         */
        addTag : function(){
            var input = this._searchBox._searchInput;
            var tag = input.value;
            this._tags.push(tag);
            
            var li = dojo.create("li", null, this.tags);
            var deleteButton = this._createDeleteButton("tag", tag);
            var p = document.createElement("p");
            dom.setText(p, tag);
            li.appendChild(deleteButton);
            li.appendChild(p);
            input.value="";
        }
    });
    
    return configExtension;
});
