require(["sbt/declare", "sbt/dom", "sbt/controls/view/TabBar", "sbt/controls/view/TabItem"],
	function(declare, dom, TabBar, TabItem) {
	    var tabBar = new TabBar();
		var TabBarItem = declare("TabBarAction", [TabItem], {
			execute: function(selection, context, element) {
				alert("Open " + this.tabLabel);
			}
		});
	
		tabBar.addItem(new TabBarItem({ "tabLabel":"First Tab", selected : true, id : 1 }));
		tabBar.addItem(new TabBarItem({ "tabLabel":"Second Tab", selected : false, id : 2 }));
		tabBar.addItem(new TabBarItem({ "tabLabel":"Third Tab", selected : false, id : 3 }));
	   
	    dom.byId("navBar").appendChild(tabBar.domNode);
	}
);