define(['./declare'], function(declare){
    // regexp    /^(?:(scheme)(:))?(\/\/)(?:(userna)(:)(passwo)(@))?(domain  )(?:(:)(port   ))?(path      )?(?:(\?)(query ))?(?:(#)(fr))?$/
    var URL_RE = /^(?:([A-z]+)(:))?(\/\/)(?:([^?#]*)(:)([^?#]*)(@))?([\w.\-]+)(?:(:)(\d{0,5}))?([\w.\/\-]+)?(?:(\?)([^?#]*))?(?:(#)(.*))?$/;
    var URL_RE_GROUPS = {
        'URL': 0,
        'SCHEME': 1,
        'SCHEME_COLON': 2,
        'SCHEME_SLASHES': 3,
        'USER': 4,
        'PASSWORD_COLON':5,
        'PASSWORD': 6,
        'USER_AT': 7,
        'HOSTNAME': 8,
        'PORT_COLON':9,
        'PORT': 10,
        'PATH': 11,
        'QUERY_QUESTION':12,
        'QUERY': 13,
        'FRAGMENT_HASH': 14,
        'FRAGMENT': 15
    };
    
    /**
    A class for representing urls.

    @class url 
    @constructor
    **/
    var url = declare(null, {
        /*
        Holds the parts of the url after URL_RE parses the url.

        @property _resultStore 
        @type Array
        **/
        _resultStore: [],
        
        /*
         * Ensures that, when setting a value, the required delimiter is before it, and when setting a value null, the relevant delimiter is not before it.
         * 
         * e.g. setQuery(query). If there was no ? in the url then the url will still not have one when you set the query. It needsto be set in this case. If there was one and you set it null, it needs to be removed.
         */
        _ensureDelimiter: function(urlPart, delimGroupNum, delim){
            if(!urlPart && this._resultStore[delimGroupNum]){// if we are setting port empty, ensure there is no : before the port.
                this._resultStore[delimGroupNum] = undefined;
            }else if(urlPart && !this._resultStore[delimGroupNum]){// if we are setting port not empty, ensure there is a : before the port.
                this._resultStore[delimGroupNum] = delim;
            } 
        },
        
        /*
        @method constructor
        **/
        constructor: function(url){
            this.setUrl(url);
        },
        
        /**
        @method getUrl
        @return {String} Returns the url in its current state.
        **/
        getUrl: function(){
            return this._resultStore.slice(1).join("");
        },
        
        /**
        @method setUrl
        @param url {String}
        **/
        setUrl: function(url){
            this._resultStore = URL_RE.exec(url);
        },
        
        /**
        @method getScheme
        @return {String} Returns the scheme in its current state.
        **/
        getScheme: function(){
            return this._resultStore[URL_RE_GROUPS.SCHEME];
        },
        
        /**
        @method setScheme
        @param scheme {String}
        @param keepSlashes {Boolean} If true, keep the // even if the scheme is set empty. e.g. //ibm.com is a valid url
        **/
        setScheme: function(scheme, keepSlashes){
            this._ensureDelimiter(scheme, URL_RE_GROUPS.SCHEME_COLON, ':');
            if(!keepSlashes || scheme){ // If they want to keep slashes and the scheme provided is empty, do not do the ensure part.
                this._ensureDelimiter(scheme, URL_RE_GROUPS.SCHEME_SLASHES, '//'); 
            }else{ // they want to keep slashes and the scheme provided is empty
                this._resultStore[URL_RE_GROUPS.SCHEME_SLASHES] = '//';
            }
            
            this._resultStore[URL_RE_GROUPS.SCHEME] = scheme;
        },
        
        /**
        @method getUser
        @return {String} Returns the username in its current state.
        **/
        getUser: function(){
            return this._resultStore[URL_RE_GROUPS.USER];
        },
        
        /**
        @method setUser
        @param user {String}
        **/
        setUser: function(user){
            this._ensureDelimiter(user, URL_RE_GROUPS.USER_AT, '@');
            this._resultStore[URL_RE_GROUPS.USER] = user;
        },
        
        /**
        @method getPassword
        @return {String} Returns the password (domain) in its current state.
        **/
        getPassword: function(){
            return this._resultStore[URL_RE_GROUPS.PASSWORD];
        },
        
        /**
        @method setPassword
        @param password {String}
        **/
        setPassword: function(password){
            this._ensureDelimiter(password, URL_RE_GROUPS.PASSWORD_COLON, ':');
            this._resultStore[URL_RE_GROUPS.PASSWORD] = password;
        },
        
        /**
        @method getHostName
        @return {String} Returns the hostname (domain) in its current state.
        **/
        getHostName: function(){
            return this._resultStore[URL_RE_GROUPS.HOSTNAME];
        },
        
        /**
        @method setHostName
        @param hostName {String}
        **/
        setHostName: function(hostName){
            this._resultStore[URL_RE_GROUPS.HOSTNAME] = hostName;
        },

        /**
        @method getPort
        @return {String} Returns the port in its current state.
        **/
        getPort: function(){
            return this._resultStore[URL_RE_GROUPS.PORT];
        },
        
        /**
        @method setPort
        @param port {Number}
        **/
        setPort: function(port){
            this._ensureDelimiter(port, URL_RE_GROUPS.PORT_COLON, ':');
            this._resultStore[URL_RE_GROUPS.PORT] = port;
        },
        
        /**
        @method getPath
        @return {String} Returns the path in its current state.
        **/
        getPath: function(){
            return this._resultStore[URL_RE_GROUPS.PATH];
        },
        
        /**
        @method setPath
        @param path {String}
        **/
        setPath: function(path){
            this._resultStore[URL_RE_GROUPS.PATH] = path;
        },
        
        /**
        @method getQuery
        @return {String} Returns the query in its current state.
        **/
        getQuery: function(){
            return this._resultStore[URL_RE_GROUPS.QUERY];
        },
        
        /**
        @method setQuery
        @param query {String}
        **/
        setQuery: function(query){
            this._ensureDelimiter(query, URL_RE_GROUPS.QUERY_QUESTION, '?');
            this._resultStore[URL_RE_GROUPS.QUERY] = query;
        },
        
        /**
        @method getFragment
        @return {String} Returns the fragment in its current state.
        **/
        getFragment: function(){
            return this._resultStore[URL_RE_GROUPS.FRAGMENT];
        },
        
        /**
        @method setFragment
        @param fragment {String}
        **/
        setFragment: function(fragment){
            this._ensureDelimiter(fragment, URL_RE_GROUPS.FRAGMENT_HASH, '#');
            this._resultStore[URL_RE_GROUPS.FRAGMENT] = fragment;
        },
        
        /**
        @method getBaseUrl
        @return {String} Utility method, returns the url up until before the query.
        **/
        getBaseUrl: function(){
            return this._resultStore.slice(1, URL_RE_GROUPS.QUERY).join("");
        }
    });
    return url;
});