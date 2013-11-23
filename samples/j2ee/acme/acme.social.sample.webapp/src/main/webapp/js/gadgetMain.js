/**
 * Main JS file for the airlines app.
 */
require(['dojo/domReady!'], function() {
    require(['acmesocial/widgets/gadgets/airlines/GadgetMenuViewWidget', 'acmesocial/menu'], function(GadgetMenuViewWidget, menu) {
        var menuView = new GadgetMenuViewWidget(menu);
        menuView.render();
        gadgets.window.adjustHeight();
    });   
});
