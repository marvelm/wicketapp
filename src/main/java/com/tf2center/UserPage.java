package com.tf2center;

import org.apache.wicket.markup.html.link.Link;

public class UserPage extends AuthRequiredPage {
	public UserPage() {
		Link logoutLink = new Link("logoutButton") {
			@Override
			public void onClick() {
				AppSession.get().signOut();
				setResponsePage(LoginPage.class);
			}
		};

		Link userListLink = new Link("userList") {
			@Override
			public void onClick() {
				setResponsePage(UserListPage.class);
			}
		};

		Link recentGamesLink = new Link("recentGames") {
			@Override
			public void onClick() {
				setResponsePage(RecentGamesPage.class);
			}
		};

		this.add(logoutLink);
		this.add(userListLink);
		this.add(recentGamesLink);
	}
}
