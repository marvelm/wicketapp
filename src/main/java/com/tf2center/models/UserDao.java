package com.tf2center.models;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.tf2center.util.ApplicationContextProvider;

public class UserDao {
	private static ApplicationContext ctx = ApplicationContextProvider.applicationContext; 

	private static final SessionFactory sessionFactory = (SessionFactory) ctx
			.getBean("sessionFactory");
	// new Configuration().configure().buildSessionFactory();

	public static void insert(User user) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
		session.close();
	}

	public static User findByEmail(String email) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.eq("email", email).ignoreCase());
		User user = (User) criteria.uniqueResult();
		session.close();
		return user;
	}

	public static User findByUsername(String username) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.eq("username", username).ignoreCase());
		User user = (User) criteria.uniqueResult();
		session.close();
		return user;
	}

	public static boolean checkCredentials(String username, String password) {
		User user = findByUsername(username);
		if (user == null) {
			return false;
		}
		return BCrypt.checkpw(password, user.getPasswordHash());
	}

	public static List<User> search(String query) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.or(Restrictions.like("email", query, MatchMode.ANYWHERE)
						.ignoreCase(),
				Restrictions.like("username", query, MatchMode.ANYWHERE).ignoreCase()));
		List<User> users = (List<User>) criteria.list();
		session.getTransaction().commit();
		session.close();
		return users;
	};

	public static List<User> getAll() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(User.class);
		List<User> users = (List<User>) criteria.list();
		session.getTransaction().commit();
		session.close();
		return users;
	}
}
