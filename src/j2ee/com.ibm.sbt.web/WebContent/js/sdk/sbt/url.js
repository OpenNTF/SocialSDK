define(['./declare'], function(declare){
    // regexp    /^(?:(scheme)(:))?(\/\/)(?:(userna)(:)(passwo)(@))?(domain  )(?:(:)(port   ))?(path      )?(?:(\?)(query ))?(?:(#)(fr))?$/
    var URL_RE = /^(?:([A-z]+)(:))?(\/\/)(?:([^?#]*)(:)([^?#]*)(@))?([\w.\-]+)(?:(:)(\d{0,5}))?([\w.\/\-]+)?(?:(\?)([^?#]*))?(?:(#)(.*))?$/;
    var URL_RE_GROUPS = {
        'URL': 0,
        'SCHEME': 1,
        'COLON1': 2,
        'SLASHES': 3,
        'USER': 4,
        'COLON2':5,
        'PASSWORD': 6,
        'AT': 7,
        'HOSTNAME': 8,
        'COLON3':9,
        'PORT': 10,
        'PATH': 11,
        'QUESTION':12,
        'QUERY': 13,
        'HASHSIGN': 14,
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
        **/
        setScheme: function(scheme){
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
            this._resultStore[URL_RE_GROUPS.PORT] = port;
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