function callUploadFile() {	
    require(['sbt/connections/FileService','sbt/dom'], function(FileService,dom) {    	
    	var fileService = new FileService();
    	fileService.uploadFile({fileLocation: "your-files",       	
        	load: function(status){
        		dom.setText('status',status);							
			},
			error : function(e){
				dom.setText('status',"Error received. Error Code = " +  e.code + ". Error Message = " + e.message);
			}	
        });
    });       
}

