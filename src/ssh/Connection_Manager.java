package ssh;

import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Connection_Manager {
	private static final String user = "root";
	private static final int port = 22;
	private final static String privateKey = "/root/.ssh/private_key";
	private final static String knownHosts = "/root/.ssh/known_hosts";

	private static JSch jsch;

	public static void init() throws Exception {
		jsch = new JSch();
		add_identity(jsch);
	}

	private static void add_identity(JSch jsch) throws Exception {
		jsch.setKnownHosts(knownHosts);
		jsch.addIdentity(privateKey);
		System.out.println("Identity added");
	}

	public static Session createSession(String host) throws Exception {
		Session session = jsch.getSession(user, host, port);
		System.out.println("Session created for host \"" + host + "\"");

		/* Required for passwordless ssh */
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);

		session.connect();
		System.out.println("Session connected  for host \"" + host + "\"");

		return session;
	}
}
