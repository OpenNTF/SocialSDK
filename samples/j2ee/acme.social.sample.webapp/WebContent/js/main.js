/**
 * Main JS file for the airlines app.
 */
require(['dojo/domReady!'], function() {
    require(['acme/widgets/MenuViewWidget', 'acmesocial/login', 'acmesocial/menu'], 
        function(MenuView, login, menu) {
            if (login.getPerson()) {
                return;
            }
        
            login.doLogin({
                success: function(response) {
                    var menuView = new MenuView(menu);
                    menuView.render();
                    menuView.showHome();
                },
                error: function(error) {
                    // Only display the Home tab when login fails
                    var home = [ menu[0] ];
                    var menuView = new MenuView(home);
                    menuView.render();
                    menuView.showHome();
                }
            });
    });
});
