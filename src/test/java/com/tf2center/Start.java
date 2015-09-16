package com.tf2center;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import com.tf2center.models.User;
import com.tf2center.models.UserDao;
import com.tf2center.util.ApplicationContextProvider;

import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Separate startup class for people that want to run the examples directly. Use
 * parameter -Dcom.sun.management.jmxremote to startup JMX (and e.g. connect
 * with jconsole).
 */
public class Start {
	/**
	 * Main function, starts the jetty server.
	 * Creates a user with username 'john' and
	 * password 'hunter2'.
	 * It also randomly generates 10 users to demo
	 * the User search functionality.
 	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		User johndoe = new User();
		johndoe.setUsername("john");
		johndoe.setEmail("johndoe@example.com");
		johndoe.setPasswordHash(encoder.encode("hunter2"));
		UserDao.insert(johndoe);

		for (int i = 1; i <= 9; i++) {
			User user = new User();
			user.setUsername(RandomStringUtils.randomAlphanumeric(6));
			user.setEmail(String.format("%s@example.com", RandomStringUtils.randomAlphanumeric(6)));
			user.setPasswordHash(encoder.encode("hunter2"));
			UserDao.insert(user);
		}

		System.out.println(UserDao.getAll());

		System.setProperty("wicket.configuration", "development");

		Server server = new Server();

		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(8443);
		http_config.setOutputBufferSize(32768);

		ServerConnector http = new ServerConnector(server,
				new HttpConnectionFactory(http_config));
		http.setPort(8080);
		http.setIdleTimeout(1000 * 60 * 60);

		server.addConnector(http);

		Resource keystore = Resource.newClassPathResource("/keystore");
		if (keystore != null && keystore.exists()) {
			// if a keystore for a SSL certificate is available, start a SSL
			// connector on port 8443.
			// By default, the quickstart comes with a Apache Wicket Quickstart
			// Certificate that expires about half way september 2021. Do not
			// use this certificate anywhere important as the passwords are
			// available in the source.

			SslContextFactory sslContextFactory = new SslContextFactory();
			sslContextFactory.setKeyStoreResource(keystore);
			sslContextFactory.setKeyStorePassword("wicket");
			sslContextFactory.setKeyManagerPassword("wicket");

			HttpConfiguration https_config = new HttpConfiguration(http_config);
			https_config.addCustomizer(new SecureRequestCustomizer());

			ServerConnector https = new ServerConnector(server,
					new SslConnectionFactory(sslContextFactory, "http/1.1"),
					new HttpConnectionFactory(https_config));
			https.setPort(8443);
			https.setIdleTimeout(500000);

			server.addConnector(https);
			System.out
					.println("SSL access to the examples has been enabled on port 8443");
			System.out.println(
					"You can access the application using SSL on https://localhost:8443");
			System.out.println();
		}

		WebAppContext bb = new WebAppContext();
		bb.setServer(server);
		bb.setContextPath("/");
		bb.setWar("src/main/webapp");

		// uncomment next line if you want to test with JSESSIONID encoded in
		// the urls
		// ((AbstractSessionManager)
		// bb.getSessionHandler().getSessionManager()).setUsingCookies(false);

		server.setHandler(bb);

		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
		server.addEventListener(mBeanContainer);
		server.addBean(mBeanContainer);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
	}
}
