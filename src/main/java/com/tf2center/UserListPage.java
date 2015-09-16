package com.tf2center;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import com.tf2center.models.User;
import com.tf2center.models.UserDao;

public class UserListPage extends AuthRequiredPage {
	public UserListPage() {
		super();

		final Label message = new Label("message", Model.of("Users:"));

		final ListView<User> userList = new ListView<User>("userList") {
			@Override
			protected void populateItem(ListItem<User> item) {
				User user = item.getModelObject();
				item.add(new Label("username", user.getUsername()));
				item.add(new Label("email", user.getEmail()));
			}
		};
		userList.setList(UserDao.getAll());

		final TextField<String> query = new TextField<String>("query", Model.of(""));

		Link usernameAsc = new Link("usernameAsc") {
			@Override
			public void onClick() {
				List<User> users = userList.getModelObject();
				Collections.sort(users, new Comparator<User>() {
					@Override
					public int compare(User o1, User o2) {
						return o1.getUsername().compareTo(o2.getUsername());
					}
				});
				userList.render();
			}
		};
		Link usernameDesc = new Link("usernameDesc") {
			@Override
			public void onClick() {
				List<User> users = userList.getModelObject();
				Collections.sort(users, new Comparator<User>() {
					@Override
					public int compare(User o1, User o2) {
						return -o1.getUsername().compareTo(o2.getUsername());
					}
				});
				userList.render();
			}
		};

		Link emailAsc = new Link("emailAsc") {
			@Override
			public void onClick() {
				List<User> users = userList.getModelObject();
				Collections.sort(users, new Comparator<User>() {
					@Override
					public int compare(User o1, User o2) {
						return o1.getEmail().compareTo(o2.getEmail());
					}
				});
				userList.render();
			}
		};
		Link emailDesc = new Link("emailDesc") {
			@Override
			public void onClick() {
				List<User> users = userList.getModelObject();
				Collections.sort(users, new Comparator<User>() {
					@Override
					public int compare(User o1, User o2) {
						return -o1.getEmail().compareTo(o2.getEmail());
					}
				});
				userList.render();
			}
		};

		Form form = new Form("search") {
			@Override
			protected void onSubmit() {
				if (StringUtils.isBlank(query.getInput())) {
					message.setDefaultModel(Model.of("Users:"));
					userList.setList(UserDao.getAll());
				} else {
					message.setDefaultModel(Model
							.of(String.format("Results for '%s'", query.getInput())));
					userList.setList(UserDao.search(query.getInput()));
				}
				render();
			}
		};

		form.add(query);

		this.add(form);
		this.add(message);

		this.add(userList);
		this.add(usernameAsc);
		this.add(usernameDesc);
		this.add(emailAsc);
		this.add(emailDesc);
	}
}