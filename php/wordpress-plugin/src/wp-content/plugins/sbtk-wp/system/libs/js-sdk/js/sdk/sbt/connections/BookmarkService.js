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
 * The Bookmarks API can be used to save, organize, and share Internet and intranet bookmarks. The Bookmarks API allows
 * application programs to read and write to the collections of bookmarks stored in the Bookmarks application..
 * 
 * @module sbt.connections.BookmarkService
 */
define([ "../declare", "../config", "../lang", "../stringUtil", "../Promise", "./BookmarkConstants", "../base/BaseService",
         "../base/AtomEntity", "../base/XmlDataHandler",  "./Tag"], 
    function(declare,config,lang,stringUtil,Promise,consts,BaseService,AtomEntity,XmlDataHandler, Tag) {
    
	var BookmarkTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\"><title type=\"text\">${getTitle}</title><category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"bookmark\"/><link href='${getUrl}'/>${isPrivate}<content type=\"html\">${getContent}</content>${getTags}</entry>";
	var CategoryPrivateFlag = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/flags\" term=\"private\" />"
	CategoryTmpl = "<category term=\"${tag}\"></category>";
	var CategoryBookmark = "<category term=\"bookmark\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>";
	
    /**
     * Bookmark class represents an entry for a Bookmarks feed returned by the
     * Connections REST API.
     * 
     * @class Bookmark
     * @namespace sbt.connections
     */
    var Bookmark = declare(AtomEntity, {

    	xpath : consts.BookmarkXPath,
    	namespaces : consts.BookmarkNamespaces,
        categoryScheme : CategoryBookmark,
        /**
         * Construct a Bookmark entity.
         * 
         * @constructor
         * @param args
         */
        constructor : function(args) {
        },

        /**
         * Return the value of IBM Connections bookmark ID from bookmark ATOM
         * entry document.
         * 
         * @method getBookmarkUuid
         * @return {String} ID of the bookmark
         */
        getBookmarkUuid : function() {
        	return this.getAsString("BookmarkUuid");
        },

        /**
         * Sets id of IBM Connections bookmark.
         * 
         * @method setBookmarkUuid
         * @param {String} bookmarkUuid of the bookmark
         */
        setBookmarkUuid : function(BookmarkUuid) {
            return this.setAsString("BookmarkUuid", BookmarkUuid);
        },

        /**
         * Return the value of private flag
         * 
         * @method isPrivate
         * @return {String} accessibility flag of the bookmark 
         */
        isPrivate : function() {
            return this.getAsBoolean("privateFlag");
        },

        /**
         * Sets the value of private flag
         * 
         * @method setPrivate
         * @param {String} accessibility flag of the bookmark 
         */
        setPrivate : function(privateFlag) {
            return this.setAsBoolean("privateFlag", privateFlag);
        },

        /**
         * Return the value of IBM Connections bookmark URL from bookmark ATOM
         * entry document.
         * 
         * @method getUrl
         * @return {String} Bookmark URL of the bookmark
         */
        getUrl : function() {
            return this.getAsString("link");
        },

        /**
         * Sets title of IBM Connections bookmark.
         * 
         * @method setUrl
         * @param {String} title Title of the bookmark
         */
        setUrl : function(link) {
            return this.setAsString("link", link);
        },
        
        /**
         * Return the click count of the IBM Connections bookmark.
         * 
         * @method getClickCount
         * @return {String} click count of the bookmark
         */
        getClickCount : function() {
            return this.getAsString("clickcount");
        },
        
        /**
         * Return the category type of the IBM Connections bookmark.
         * 
         * @method getCategoryType
         * @return {String} content of the bookmark
         */
        getCategoryType : function() {
            return this.getAsString("categoryType");
        },
        
        /**
         * Return the edit link for IBM Connections bookmark.
         * 
         * @method getEditLink
         * @return {String} edit link of the bookmark
         */
        getEditLink : function() {
            return this.getAsString("linkEdit");
        },
        
        /**
         * Return the same link for IBM Connections bookmark.
         * 
         * @method getSameLink
         * @return {String} same link of the bookmark
         */
        getSameLink : function() {
            return this.getAsString("linkSame");
        },

        /**
         * Gets an author of IBM Connections Bookmark.
         * 
         * @method getAuthor
         * @return {Member} Author of the bookmark
         */
        getAuthor : function() {
            return this.getAsObject([ "authorId", "authorName", "authorEmail", "authorUri" ]);
        },

        /**
         * Return tags of IBM Connections bookmark
         * document.
         * 
         * @method getTags
         * @return {Object} Array of tags of the bookmark
         */
        getTags : function() {
            return this.getAsArray("tags");
        },

        /**
         * Set new tags to be associated with this IBM Connections bookmark.
         * 
         * @method setTags
         * @param {Object} Array of tags to be added to the bookmark
         */

        setTags : function(tags) {
            return this.setAsArray("tags", tags);
        },

        /**
         * Loads the bookmark object with the atom entry associated with the
         * bookmark. By default, a network call is made to load the atom entry
         * document in the bookmark object.
         * 
         * @method load
         * @param {Object} [args] Argument object
         */
        load : function(args) {
            // detect a bad request by validating required arguments
            var bookmarkUrl = this.getUrl();
            var promise = this.service._validateBookmarkUrl(bookmarkUrl);
            if (promise) {
                return promise;
            }

            var self = this;
            var callbacks = {
                createEntity : function(service,data,response) {
                    self.setDataHandler(new XmlDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.BookmarkXPath
                    }));
                    return self;
                }
            };
            var requestArgs = lang.mixin({
            	url : bookmarkUrl
            }, args || {});

            var options = {
                handleAs : "text",
                query : requestArgs
            };
            return this.service.getEntity(consts.AtomBookmarkCreateUpdateDelete, options, bookmarkUrl, callbacks);
        },

        /**
         * Remove this bookmark
         * 
         * @method remove
         * @param {Object} [args] Argument object
         */
        remove : function(args) {
            return this.service.deleteBookmark(this.getUrl(), args);
        },

        /**
         * Update this bookmark
         * 
         * @method update
         * @param {Object} [args] Argument object
         */
        update : function(args) {
            return this.service.updateBookmark(this, args);
        },
        
        /**
         * Save this bookmark
         * 
         * @method save
         * @param {Object} [args] Argument object
         */
        save : function(args) {
            if (this.getBookmarkUuid()) {
                return this.service.updateBookmark(this, args);
            } else {
                return this.service.createBookmark(this, args);
            }
        }

    });
    
    /*
     * Callbacks used when reading a feed that contains bookmark entries.
     */
    var ConnectionsBookmarkFeedCallbacks = {
        createEntities : function(service,data,response) {
            return new XmlDataHandler({
                service :  service,
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.BookmarkFeedXPath
            });
        },
        createEntity : function(service,data,response) {
            return new Bookmark({
                service : service,
                data : data
            });
       }
    };
    
    /*
     * Callbacks used when reading a feed that contains bookmark entries.
     */
    var ConnectionsTagsFeedCallbacks = {
		createEntities : function(service,data,response) {
            return new XmlDataHandler({
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.TagsXPath
            });
        },
        createEntity : function(service,data,response) {
            var entryHandler = new XmlDataHandler({
                data : data,
                namespaces : consts.Namespaces,
                xpath : consts.TagsXPath
            });
            return new Tag({
                service : service,
                id : entryHandler.getEntityId(),
                dataHandler : entryHandler
            });
        }
    };
    
    /**
     * BookmarkService class.
     * 
     * @class BookmarkService
     * @namespace sbt.connections
     */
    var BookmarkService = declare(BaseService, {
    	
    	contextRootMap: {
        	dogear : "dogear"
        },
    	
        /**
         * Constructor for BookmarkService
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
        getDefaultEndpointName : function() {
            return "connections";
        },
        
        /**
         * Get the All Bookmarks feed to see a list of all bookmarks to which the 
         * authenticated user has access.
         * 
         * @method getAllBookmarks
         * 
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of all bookmarks. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getAllBookmarks : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            return this.getEntities(consts.AtomBookmarksAll, options, this.getBookmarkFeedCallbacks());
        },
        
        /**
         * Get the popular Bookmarks feed.
         * 
         * @method getPopularBookmarks
         * 
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of all bookmarks. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getPopularBookmarks : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomBookmarksPopular, options, this.getBookmarkFeedCallbacks());
        },
        
        /**
         * A feed of bookmarks that others notified me about.
         * 
         * @method getBookmarksMyNotifications
         * 
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of all bookmarks. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getBookmarksMyNotifications : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomBookmarksMyNotifications, options, this.getBookmarkFeedCallbacks());
        },
        
        /**
         * A feed of bookmarks about which I notified others..
         * 
         * @method getBookmarksMySentNotifications
         * 
         * @param {Object} [args] Object representing various parameters
         * that can be passed to get a feed of all bookmarks. The
         * parameters must be exactly as they are supported by IBM
         * Connections like ps, sortBy etc.
         */
        getBookmarksMySentNotifications : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomBookmarksINotifiedMySentNotifications, options, this.getBookmarkFeedCallbacks());
        },
        
        /**
         * Retrieve a bookmark instance.
         * 
         * @method getBookmark
         * @param {String } bookmarkUrl
         * @param {Object} args Object containing the query arguments to be 
         * sent (defined in IBM Connections Bookmarks REST API) 
         */
        getBookmark : function(bookmarkUrl, args) {
            var bookmark = new Bookmark({
                service : this,
                _fields : { link : bookmarkUrl }
            });
            return bookmark.load(args);
        },

        /**
         * Create a bookmark by sending an Atom entry document containing the 
         * new bookmark.
         * 
         * @method createBookmark
         * @param {String/Object} bookmarkOrJson Bookmark object which denotes the bookmark to be created.
         * @param {Object} [args] Argument object
         */
        createBookmark : function(bookmarkOrJson,args) {
            var bookmark = this._toBookmark(bookmarkOrJson);
            var promise = this._validateBookmark(bookmark, false, args);
            if (promise) {
                return promise;
            }

            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
        		if (data) {
            		var dataHandler = new XmlDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.BookmarkXPath
                    });
                	bookmark.setDataHandler(dataHandler);
            	}
            	bookmark.setData(data);
                return bookmark;
            };

            var options = {
                method : "POST",
                query : args || {},
                headers : consts.AtomXmlHeaders,
                data : this._constructBookmarkPostData(bookmark)
            };
            
            return this.updateEntity(consts.AtomBookmarkCreateUpdateDelete, options, callbacks, args);
        },

        /**
         * Update a bookmark by sending a replacement bookmark entry document 
         * to the existing bookmark's edit web address.
         * All existing bookmark entry information will be replaced with the new data. To avoid 
         * deleting all existing data, retrieve any data you want to retain first, and send it back 
         * with this request. For example, if you want to add a new tag to a bookmark entry, retrieve 
         * the existing tags, and send them all back with the new tag in the update request.
         * 
         * @method updateBookmark
         * @param {String/Object} bookmarkOrJson Bookmark object
         * @param {Object} [args] Argument object
         */
        updateBookmark : function(bookmarkOrJson,args) {
            var bookmark = this._toBookmark(bookmarkOrJson);
            var promise = this._validateBookmark(bookmark, true, args);
            if (promise) {
                return promise;
            }
            var callbacks = {};
            callbacks.createEntity = function(service,data,response) {
            	var bookmarkUuid = bookmark.getBookmarkUuid();
            	if (data) {
            		var dataHandler = new XmlDataHandler({
                        service :  service,
                        data : data,
                        namespaces : consts.Namespaces,
                        xpath : consts.BookmarkXPath
                    });
            		bookmark.setDataHandler(dataHandler);
            	}
            	bookmark.setBookmarkUuid(bookmarkUuid);
                return bookmark;
            };

            var requestArgs = lang.mixin({
            	url : bookmark.getUrl()
            }, args || {});
            var options = {
                method : "PUT",
                query : requestArgs,
                headers : consts.AtomXmlHeaders,
                data : this._constructBookmarkPostData(bookmark)
            };
            return this.updateEntity(consts.AtomBookmarkCreateUpdateDelete, options, callbacks, args);
        },

        /**
         * Delete a bookmark, use the HTTP DELETE method.
         * 
         * @method deleteBookmark
         * @param {String} bookmarkUuid bookmark id of the bookmark or the bookmark object (of the bookmark to be deleted)
         * @param {Object} [args] Argument object
         */
        deleteBookmark : function(bookmarkUrl,args) {
            var promise = this._validateBookmarkUrl(bookmarkUrl);
            if (promise) {
                return promise;
            }            
           
            var requestArgs = lang.mixin({
            	url : bookmarkUrl
            }, args || {});
            
            var options = {
                method : "DELETE",
                query : requestArgs,
                handleAs : "text"
            };
            
            return this.deleteEntity(consts.AtomBookmarkCreateUpdateDelete, options, bookmarkUrl);
        },

        /**
         * Get the tags feed to see a list of the tags for all bookmarks.
         * 
         * @method getBookmarksTags
         * @param {Object} [args] Object representing various parameters. 
         * The parameters must be exactly as they are supported by IBM
         * Connections.
         */
        getBookmarksTags : function(args) {
            var options = {
                method : "GET",
                handleAs : "text",
                query : args || {}
            };
            
            return this.getEntities(consts.AtomBookmarksTags, options, this.getTagsFeedCallbacks(), args);
        },
        
        /**
         * Create a Bookmark object with the specified data.
         * 
         * @method newBookmark
         * @param {Object} args Object containing the fields for the 
         * new bookmark 
         */
        newBookmark : function(args) {
            return this._toBookmark(args);
        },
        
        /*
         * Callbacks used when reading a feed that contains Bookmark entries.
         */
        getBookmarkFeedCallbacks: function() {
            return ConnectionsBookmarkFeedCallbacks;
        },
        
        /*
         * Callbacks used when reading a feed that contains Bookmark entries.
         */
        getTagsFeedCallbacks: function() {
            return ConnectionsTagsFeedCallbacks;
        },
        
        /*
         * Return a Bookmark instance from Bookmark or JSON or String. Throws
         * an error if the argument was neither.
         */
        _toBookmark : function(bookmarkOrJsonOrString) {
            if (bookmarkOrJsonOrString instanceof Bookmark) {
                return bookmarkOrJsonOrString;
            } else {
                if (lang.isString(bookmarkOrJsonOrString)) {
                    bookmarkOrJsonOrString = {
                        bookmarkUuid : bookmarkOrJsonOrString
                    };
                }
                return new Bookmark({
                    service : this,
                    _fields : lang.mixin({}, bookmarkOrJsonOrString)
                });
            }
        },

        /*
         * Validate a bookmark UUID, and return a Promise if invalid.
         */
        _validateBookmarkUuid : function(bookmarkUuid) {
            if (!bookmarkUuid || bookmarkUuid.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected bookmarkUuid.");
            }
        },
        
        /*
         * Validate a bookmark UUID, and return a Promise if invalid.
         */
        _validateBookmarkUrl : function(bookmarkUrl) {
            if (!bookmarkUrl || bookmarkUrl.length == 0) {
                return this.createBadRequestPromise("Invalid argument, expected bookmarkUrl.");
            }
        },

        /*
         * Validate a bookmark, and return a Promise if invalid.
         */
        _validateBookmark : function(bookmark,checkUuid) {
            if (!bookmark || !bookmark.getTitle()) {
                return this.createBadRequestPromise("Invalid argument, bookmark with title must be specified.");
            }
            if (checkUuid && !bookmark.getBookmarkUuid()) {
                return this.createBadRequestPromise("Invalid argument, bookmark with UUID must be specified.");
            }
        },
        
        /*
         * Construct a post data for a Bookmark
         */
        _constructBookmarkPostData : function(bookmark) {
            var transformer = function(value,key) {
                if (key == "getTags") {
                    var tags = value;
                    value = "";
                    for (var tag in tags) {
                        value += stringUtil.transform(CategoryTmpl, {
                            "tag" : tags[tag]
                        });
                    }
                } else if (key == "getTitle" && !value) {
					value = bookmark.getTitle();
				} else if (key == "getUrl" && !value) {
					value = bookmark.getUrl();
				} else if (key == "getContent" && !value) {
					value = bookmark.getContent();
				} else if (key == "isPrivate" && !value) {
					if(bookmark.isPrivate()){
						value = true;
					}else{
						value = false;
					}
				}
                return value;
            };
            var postData = stringUtil.transform(BookmarkTmpl, bookmark, transformer, bookmark);
            return stringUtil.trim(postData);
        }
        
    });
    return BookmarkService;
});
