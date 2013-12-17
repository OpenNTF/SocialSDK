require(["sbt/declare", "sbt/dom", "sbt/controls/view/ActionBar", "sbt/controls/view/Action"], 
	function(declare, dom, ActionBar, Action) {
	    var actionBar = new ActionBar();
	    
		var AlertAction = declare([ Action ], {
			execute: function(selection, context) {
				alert(this.name);
			}
		});
		
		actionBar.addAction(new AlertAction({ "name":"First Action" }));
		actionBar.addAction(new AlertAction({ "name":"Second Action" }));
		actionBar.addAction(new AlertAction({ "name":"Third Action" }));
	   
	    dom.byId("actionBarDiv").appendChild(actionBar.domNode);
	}
);
