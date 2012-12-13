/**
 * Main JS file for the airlines app.
 */
require(['dojo/domReady!'], function() {
    require(['acme/widgets/gadgets/airlines/GadgetMenuViewWidget', 'acme/menu'], function(GadgetMenuViewWidget, menu) {
        var menuView = new GadgetMenuViewWidget(menu);
        menuView.render();
        gadgets.window.adjustHeight();
    });   
});
