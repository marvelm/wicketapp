package com.tf2center;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import com.tf2center.models.User;
import com.tf2center.models.UserDao;

/**
 * Modified Session containing a User object Wicket will use the `authenticate`
 * method to check the user's credentials against the database.
 */
public class AppSession extends AuthenticatedWebSession {
	private User user;

	public AppSession(Request request) {
		super(request);
	}

	@Override
	public final boolean authenticate(final String username, final String password) {
		boolean correct = UserDao.checkCredentials(username, password);
		if (correct) {
			setUser(UserDao.findByUsername(username));
			get().removeAttribute("needsAuth");
		}
		return correct;
	}

	@Override
	public void signOut() {
		super.signOut();
		setUser(null);
	};

	@Override
	public Roles getRoles() {
		return null;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
