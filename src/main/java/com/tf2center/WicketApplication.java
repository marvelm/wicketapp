package com.tf2center;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 *
 * @see com.tf2center.Start#main(String[])
 */
public class WicketApplication extends AuthenticatedWebApplication {
    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();

        getComponentInstantiationListeners().add(new SpringComponentInjector(this));

        this.mountPage("/register", RegisterPage.class);
        this.mountPage("/user", UserPage.class);
        this.mountPage("/login", LoginPage.class);
        this.mountPage("/userList", UserListPage.class);
        this.mountPage("/recentGames", RecentGamesPage.class);
    }

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return LoginPage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return AppSession.class;
	}
}
