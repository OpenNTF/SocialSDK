require(["sbt/declare", "sbt/dom", "sbt/controls/view/BaseView", "sbt/controls/view/Action"], 
	function(declare, dom, BaseView, Action) {
	    var baseView = new BaseView({ 
	    	title : "Show Message", 
	    	iconClass : "lotusIcon iconsComponentsBlue24 iconsComponentsBlue24-CommunitiesBlue24"
	    });
	    
		var MessageAction = declare([ Action ], {
			execute: function(selection, context) {
				this.view.displayMessage(this.message, this.isError);
			}
		});
	
	    baseView.addAction(new MessageAction({ "name":"Success", "isError":false, "message":"This is a success message." }));
	    baseView.addAction(new MessageAction({ "name":"Error", "isError":true, "message":"This is an error message." }));
	    
	    dom.byId("baseViewDiv").appendChild(baseView.domNode);
	}
);
