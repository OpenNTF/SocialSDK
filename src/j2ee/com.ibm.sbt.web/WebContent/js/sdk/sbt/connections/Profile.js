define(['sbt/_bridge/declare','sbt/validate','sbt/base/BaseEntity', 'sbt/connections/core', 'sbt/connections/ProfileConstants'],function(declare, validate, BaseEntity, con, profileConstants) {
    /**
    Profile class associated with a profile. 
    @class Profile
    @namespace connections
    **/
    
    var Profile = declare("sbt.connections.Profile", BaseEntity, {
        
        _idType:    "",// can take value - userId / email / id  
        _email:     "",
        _userId:    "",
        _name:      "", 
        
        constructor: function(svc, id, xmlHandler, name, email) {
            
            var args = { entityName : "profile", Constants: profileConstants, con: con, dataHandler: xmlHandler};
            this.inherited(arguments, [svc, id, args]);
            if(this._service._isEmail(id)){
                this._email = id;
            }else{
                this._userId = id;
            }
            this._name = name;
            this._email = email;
            
        },
        /**
        Loads the profile object with the profile entry document associated with the profile. By
        default, a network call is made to load the profile entry document in the profile object.
    
        @method load
        @param {Object} [args]  Argument object
            @param {Boolean} [args.loadIt=true] Loads the profile object with profile entry document. To 
            instantiate an empty profile object associated with a profile (with no profile entry
            document), the load method must be called with this parameter set to false. By default, this 
            parameter is true.
            @param {Function} [args.load] The function profile.load invokes when the profile is 
            loaded from the server. The function expects to receive one parameter, 
            the loaded profile object.
            @param {Function} [args.error] Sometimes the load calls  fail. Often these are 404 errors 
            or server errors such as 500. The error parameter is another callback function
            that is only invoked when an error occurs. This allows to control what happens
            when an error occurs without having to put a lot of logic into your load function
            to check for error conditions. The parameter passed to the error function is a 
            JavaScript Error object indicating what the failure was. From the error object. one can get access to the 
            javascript library error object, the status code and the error message.
            @param {Function} [args.handle] This callback is called regardless of whether 
            the call to load the profile completes or fails. The  parameter passed to this callback
            is the profile object (or error object). From the error object. one can get access to the 
            javascript library error object, the status code and the error message.
        
        **/
        
        load: function(args) {
            this._data = this._service._load(this, args);           
        },
        
        /**
        Updates the profile of a user.
    
        @method update
        @param {Object} args  Argument object
            @param {Boolean} [args.reloadIt=true] Reloads the profile object with 
            the updated profile entry document. By default, this parameter is true.
            @param {Function} [args.load] The function profile.update invokes when the 
            profile is updated. The function expects to receive one parameter, 
            the updated profile object.
            @param {Function} [args.error] Sometimes the update calls fails. The error parameter is 
            a callback function that is only invoked when an error occurs. This allows to 
            control what happens when an error occurs. The parameter passed to the error function 
            is a JavaScript Error object indicating what the failure was. From the error object. 
            you can get access to the javascript library error object, the status code and the 
            error message.
            @param {Function} [args.handle] This callback is called regardless of whether 
            the call to update the profile completes or fails. The  parameter passed to this callback
            is the profile object (or error object). From the error object. one can get access to the 
            javascript library error object, the status code and the error message.
        
        **/
        
        update: function(args) {
            this._service.updateProfile(this, args);
            
        },
        
        /**
        Get id of the profile
        @method getId
        @return {String} id of the profile  
        **/
        getId: function () {
            if(!this._userId){
                this._userId = this.get("uid");
            }
            return this._userId;
        },
        /**
        Get display name of the profile
        @method getDisplayName
        @return {String} display name of the profile    
        **/
        getDisplayName: function () {   
            if(!this._name){
                this._name = this.get("name");
            }
            return this._name;
            
        },  
        /**
        Get groupware mail of the profile
        @method getGroupwareMail
        @return {String} email groupware mail of the profile    
        **/
        getGroupwareMail: function () {
            return this.get("groupwareMail");
        },
        /**
        Get email of the profile
        @method getEmail
        @return {String} email of the profile   
        **/
        getEmail: function () {
            if(!this._email){
                this._email = this.get("email");
            }
            return this._email;
        },
        /**
        Get thumbnail URL of the profile
        @method getThumbnailUrl
        @return {String} thumbnail URL of the profile
        **/
        getThumbnailUrl: function () {
            return this.get("photo");
        },
        /**
        Get title of the profile
        @method getTitle
        @return {String} title of the profile
        **/
        getTitle: function() {
            return this.get("title");
        },
        /**
        Get department of the profile
        @method getDepartment
        @return {String} department of the profile
        **/
        getDepartment: function() {
            return this.get("organizationUnit");
        },  
        /**
        Get address of the profile
        @method getAddress
        @return {Object} Address object of the profile
        **/
        getAddress: function() {            
            var address = {};
            if(this.get("countryName")){
                address.country = this.get("countryName");
            }
            if(this.get("locality")){
                address.locality = this.get("locality");
            }
            if(this.get("postalCode")){
                address.postalCode = this.get("postalCode");
            }
            if(this.get("region")){
                address.region = this.get("region");
            }
            if(this.get("streetAddress")){
                address.streetAddress = this.get("streetAddress");
            }
            if(this.get("extendedAddress")){
                address.extendedAddress = this.get("extendedAddress");
            }
            if(this.get("bldgId")){
                address.building = this.get("bldgId");
            }
            if(this.get("floor")){
                address.floor = this.get("floor");
            }
            return address;
        },  
        /**
        Get phone number of the profile
        @method getPhoneNumber
        @return {Object} Phone number object of the profile
        **/
        getPhoneNumber: function() {            
            return this.get("telephoneNumber");
        },  
        /**
        Get profile URL of the profile
        @method getProfileUrl
        @return {String} profile URL of the profile
        **/
        getProfileUrl: function() {
            return this.get("fnUrl");
        },
        /**
        Get Pronunciation URL of the profile
        @method getPronunciationUrl
        @return {String} Pronunciation URL of the profile
        **/
        getPronunciationUrl: function() {
            return this.get("soundUrl");
        },  
        /**
        Get "About" / description of the profile
        @method getAbout
        @return {String} description of the profile
        **/
        getAbout: function() {
            return this.get("summary");
        },
        /**
        Set work phone number of the profile in the field object
        @method setPhoneNumber
        @param {String} work phone number of the profile
        **/
        setPhoneNumber: function(newPhonenumber){
            this.set("telephoneNumber",newPhonenumber);
        },
        /**
        Set title of the profile in the field object
        @method setTitle
        @param {String} title of the profile
        **/
        setTitle: function(title){
            this.set("title",title);
        },
        /**
        Set the location of the file input element in the markup for editing profile photo
        in the field object
        @method setPhotoLocation
        @param {String} location of the file input element
        **/
        setPhotoLocation: function(imgLoc){
            this.set("imageLocation",imgLoc);
        },
        /**
        Set the address of the profile in the field object
        @method setPhotoLocation
        @param {Object} Address object of the profile.
        **/
        setAddress: function(address) {
            var countryName = address.country;
            var locality = address.locality;
            var postalCode = address.postalCode;
            var region = address.region;
            var streetAddress = address.streetAddress;
            var bldgId = address.building;
            var floor = address.floor;  
            if(countryName) {
                this.set("countryName",countryName);
            }
            if(locality) {
                this.set("locality",locality);
            }
            if(postalCode) {
                this.set("postalCode",postalCode);
            }
            if(region) {
                this.set("region",region);
            }
            if(streetAddress) {
                this.set("streetAddress",streetAddress);
            }
            if(bldgId) {
                this.set("bldgId",bldgId);
            }
            if(floor) {
                this.set("floor",floor);
            }
        },
        _validate : function(className, methodName, args, validateMap) {
            
            if (validateMap.isValidateType && !(validate._validateInputTypeAndNotify(className, methodName, "Profile", this, "sbt.connections.Profile", args))) {
                return false;
            }
            if (validateMap.isValidateId && !(validate._validateInputTypeAndNotify(className, methodName, "Profile Id", this._id, "string", args))) {
                return false;
            }           
            return true;
        }
    });
    
    return Profile;
});