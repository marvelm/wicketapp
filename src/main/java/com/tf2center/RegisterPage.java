package com.tf2center;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tf2center.models.User;
import com.tf2center.models.UserDao;
import com.tf2center.util.InputPatterns;

public class RegisterPage extends WebPage {
	public RegisterPage(final PageParameters params) {
		super(params);

		if (AppSession.get().isSignedIn()) {
			setResponsePage(UserPage.class);
			return;
		}

		this.add(new FeedbackPanel("feedback"));

		final TextField<String> username = new TextField<String>("username", Model.of(""));
		username.add(new PatternValidator(InputPatterns.USERNAME_PATTERN));
		username.setRequired(Boolean.TRUE);
		username.add(new IValidator<String>() {
			@Override
			public void validate(IValidatable<String> validatable) {
				String usernameIn = validatable.getValue();
				if (StringUtils.isNotBlank(usernameIn)) {
					User user = UserDao.findByUsername(usernameIn);
					if (user != null) {
						error("Username is already taken");
					}
				}
			}
		});

		final TextField<String> email = new TextField<String>("email", Model.of(""));
		email.add(new PatternValidator(InputPatterns.EMAIL_PATTERN));
		email.setRequired(Boolean.TRUE);
		email.add(new IValidator<String>() {
			@Override
			public void validate(IValidatable<String> validatable) {
				String emailIn = validatable.getValue();
				if (StringUtils.isNotBlank(emailIn)) {
					User user = UserDao.findByEmail(emailIn);
					if (user != null) {
						error("Email is already taken");
					}
				}
			}
		});

		final PasswordTextField password = new PasswordTextField("password", Model.of(""));
		password.setRequired(Boolean.TRUE);

		final PasswordTextField confirmPassword = new PasswordTextField("confirmPassword",
				Model.of(""));
		confirmPassword.setRequired(Boolean.TRUE);

		Form form = new Form("registrationForm") {
			@Override
			protected void onSubmit() {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				User user = new User();
				user.setEmail(email.getInput());
				user.setUsername(username.getInput());
				user.setPasswordHash(encoder.encode(password.getInput()));
				UserDao.insert(user);

				params.add("name", user.getUsername());
				params.add("email", user.getEmail());

				AppSession.get().signIn(user.getUsername(), password.getInput());
				setResponsePage(UserPage.class);
			}
		};

		form.add(username);
		form.add(email);
		form.add(password);
		form.add(confirmPassword);
		form.add(new EqualPasswordInputValidator(password, confirmPassword));

		this.add(form);
	}
}