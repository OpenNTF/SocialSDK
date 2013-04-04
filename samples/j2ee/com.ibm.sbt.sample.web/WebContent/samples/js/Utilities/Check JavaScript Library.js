// This snippet shows you how to test the underlying JavaScript library
// It just use straight JS so the result can be used to load the SDK library 
function update(text) {
    document.getElementById('content').innerHTML = "You are using: <span style='font-weight: bold'>"+text+"</span>";
}

if(window.dojo) {
	if(dojo.version) {
		update("Dojo "+dojo.version);
	}
} else if(define && define.amd && define.amd.vendor && define.amd.vendor === "dojotoolkit.org") {
	require(["dojo/_base/kernel"], function(kernel){ 
		update("Dojo AMD "+kernel.version);
	});
} else if(window.jQuery) {
	update("JQuery "+jQuery.fn.jquery);
} else {
	update("Unknown");
}
