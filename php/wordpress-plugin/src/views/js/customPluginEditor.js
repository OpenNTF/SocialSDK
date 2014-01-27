function plugin_change() {
	var plugin_name = document.getElementById("plugin_name");
	var plugin_list = document.getElementById("plugin_list");
	var selected_plugin = plugin_list.options[plugin_list.selectedIndex].value;
	plugin_name.value = selected_plugin;
	
	var name = plugin_name.value;
	if (name.indexOf('.php') == name.length - 4) {
		document.getElementById("btn_delete_widget").setAttribute('disabled', true);
	} else {
		document.getElementById("btn_delete_widget").removeAttribute('disabled');
	}
	
	var html = document.getElementById(selected_plugin + "_html").value;

	var javascript = document.getElementById(selected_plugin + "_js").value;
	htmlEditor.setValue(html);
	jsEditor.setValue(javascript);
	
	document.getElementById("plugin_name").value = name;
}

function new_widget() {
	document.getElementById("plugin_name").value = "";
	document.getElementById("selected_custom_plugin").value = "";
	jsEditor.setValue("\n\n\n\n\n\n\n\n\n\n\n\n");
	htmlEditor.setValue("\n\n\n\n\n\n\n\n\n\n\n\n");
}

function confirm_delete_widget() {
	var conf = confirm("Are you sure you want to delete this widget?");	
	
	if(conf == true){
		document.getElementById("delete_widget").value = "yes";
	    document.getElementById("submit").click();
	}
}