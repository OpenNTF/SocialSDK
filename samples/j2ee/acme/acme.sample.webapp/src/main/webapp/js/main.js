/**
 * Main JS file for the airlines app.
 */
require(['dojo/domReady!'], function() {
    require(['acme/widgets/MenuViewWidget', 'acme/menu'], function(MenuView, menu) {
        var menuView = new MenuView(menu);
        menuView.render();
        menuView.showHome();
    });
});
