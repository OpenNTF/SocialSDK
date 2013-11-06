define(["sbt/declare", "sbt/json", "sbt/connections/controls/_ConnectionsWidget"],function(declare, json, _ConnectionsWidget){
    /**
     * InputForm
     */
    var _InputForm = declare([_ConnectionsWidget],
    {    
        /**
         * The connections InputForm.
         * 
         * @property inputForm
         * @type Object
         */
        inputForm: null,
        
        /**
         * Set up the connections InputForm.
         * 
         * @method constructor
         * @param {Object} args 
         *     @param {String} args.shareBoxNode Should contain the id of the html element to add the InputForm to.
         */
        constructor: function(args){
            var url = "/${connections}/opensocial/basic/rest/ublog/@config/settings";
            var self = this;
            
            var xhrArgs = {
                serviceUrl: url, //TODO Change these when opensocial oauth and/or endpoints become available
                handleAs: "json",
                load: function(data) {
                    var settings = {
                        maxNumberChars: 1000,
                        boardId: args.boardId || "@me",
                        postType: lconn.news.microblogging.sharebox.Context.SU_CONTEXT
                    };
                    var connectionsSettings = data && data.entry;
                    if(connectionsSettings){
                        settings.maxNumberChars = connectionsSettings["com.ibm.connections.ublog.microblogEntryMaxChars"];
                        self.inputForm = new lconn.news.microblogging.sharebox.InputForm({
                            params : settings,
                            "xhrHandler" : self.xhrHandler,
                            "UBLOG_RELATIVE_PATH" : "/basic/rest/ublog/",
                            isASGadget : false
                        }, dojo.byId(args.shareBoxNode));
                        if(self.inputForm.attachActionButtonNode){
                            dojo.empty(self.inputForm.attachActionButtonNode);
                        }
                        
                        self.modifyPostFunction();
                    }
                },
                error: function() {
                    console.log("Failed to find InputForm settings at " + url);
                }
            };
            
            this.xhrHandler.xhrGet(xhrArgs);
        },
        
        /**
         * A hack needed to overcome a hardcoded dojo.xhr request in the connections InputForm. Overwrites the offending function.
         * 
         * @method modifyPostFunction
         */
        modifyPostFunction: function(){
            this.inputForm.postMicroblog = function (microblogMessage, fileAttachment) {
                this._setPostButtonLabel(this._resourceBundle.POSTING);
                this._setSubmitState(true);
                var requestObj = this._buildRequestObj(microblogMessage, fileAttachment);
                var postUrl = this._getPostUrlForContext();
                postUrl = postUrl.slice(postUrl.lastIndexOf("/connections"));
                this.xhrHandler.xhrPost({
                    serviceUrl: postUrl,
                    postData: json.stringify(requestObj),
                    handleAs: "json",
                    load: dojo.hitch(this, function (data) {
                        if (this.isGlobalSharebox){
                            dojo.publish(this.AS_UPDATE, [data.entry.id]);
                        } else {
                            dojo.publish(this.TOPIC_POST_MESSAGE, [data]);
                        }           
                        this._setInitialState();
                        this._setSubmitState(false);
                        this._displaySuccessMessage();
                        if (this.isGlobalSharebox) {
                            this._closeSharebox();
                            this.setShareMode(lconn.news.microblogging.sharebox.constants.SHAREWITHEVERYONE);
                            dojo.publish(lconn.news.microblogging.sharebox.events.STATUSSHAREBOX_CLOSING);
                        } else {
                            this.mbSuccessClose.focus();
                            this.textBoxControl.collapseTextBox();
                        }

                        this._setPostButtonLabel(this._resourceBundle.POST);

                    }),
                    error: dojo.hitch(this, function (data) {
                        this._handlePostError(data);
                    }),
                    headers: {
                        "Content-Type": "application/json"
                    }
                });
            };
        }
        
    });

    return _InputForm;
});
