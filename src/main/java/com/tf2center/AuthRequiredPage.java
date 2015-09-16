package com.tf2center;

import org.apache.wicket.markup.html.WebPage;

/**
 * Redirects unauthenticated users to the login page.
 * It also sets a 'redirectTo' attribute which allows the
 * `LoginPage` to return the user to whichever page he/she was
 * trying to access
 */
abstract class AuthRequiredPage extends WebPage {
	@Override
	public void onConfigure() {
		super.onConfigure();
		if (!AppSession.get().isSignedIn()) {
			AppSession.get().setAttribute("needsAuth", true);
			AppSession.get().setAttribute("redirectTo", this.getClass());
			setResponsePage(LoginPage.class);
		}
	}
}