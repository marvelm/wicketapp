package com.tf2center;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class LoginPage extends WebPage {
	public LoginPage(final PageParameters params) {
		super(params);
		
		// Redirect authenticated users
		if (AppSession.get().isSignedIn()) {
			setResponsePage(UserPage.class);
			return;
		}

		this.add(new FeedbackPanel("feedback"));

		final TextField<String> username = new TextField<>("username", Model.of(""));
		username.setRequired(Boolean.TRUE);

		final PasswordTextField password = new PasswordTextField("password",
				Model.of(""));
		password.setRequired(Boolean.TRUE);

		Form form = new Form("loginForm") {
			@Override
			protected void onSubmit() {
				// The username and password are valid.
				try {
					// Redirect to wherever the user was
					setResponsePage((Class) AppSession.get().getAttribute("redirectTo"));
					AppSession.get().removeAttribute("redirectTo");
				} catch (Exception e) {
					setResponsePage(UserPage.class);
				}
			}
		};

		form.add(new AbstractFormValidator() {
			@Override
			public void validate(Form<?> form) {
				boolean valid = AppSession.get().signIn(username.getInput(),
						password.getInput());
				if (!valid) {
					form.error("Invalid username or password");
				}
			}

			@Override
			public FormComponent<?>[] getDependentFormComponents() {
				return null;
			}
		});

		form.add(username);
		form.add(password);

		this.add(form);
	}
}