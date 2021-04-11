package common.distributed_executioner;

import java.util.ArrayList;
import java.util.HashMap;

import com.jcraft.jsch.Session;

import common.Logger;
import core.Params_Handler;
import ssh.Connection_Manager;
import ssh.Remote_Executioner;

public class Distributed_Executioner extends Remote_Executioner {
	public ArrayList<String> availble_servers;
	public ArrayList<String> in_use_servers;
	private HashMap<String, Session> server_sessions;

	public Distributed_Executioner() {
		availble_servers = Params_Handler.get_servers_list();
		in_use_servers = new ArrayList<String>();
		server_sessions = new HashMap<String, Session>();
	}

	public Op_Res execute_cmd_on_single_server(String cmd, String host) throws Exception {
		// TODO: differentiate between client and sever ops
//		if (host == null) {
//			if (!sessions.isEmpty()) {
//				host = first_host;
//			} else {
//				host = randomize_host();
//				first_host = host;
//			}
//		}
		
		Logger.pre_op_log(cmd, host);
		Session session = session_connect(host);
		Op_Res op_res = execute_remote_cmd(cmd, session);
		/*
		 * Debug log - (e.g. "Execution of command "X" on host "Y" returned...)
		 */
		return op_res;
	}

	/*
	 * TODO: When randomizing host, something it's important what host it is (e.g.
	 * peer probe can't probe itself), so need at add a mechanism to ensure certain
	 * operations don't get executed on the random host
	 */
	public String randomize_server() {
		int servers_num = availble_servers.size();

		int random_server_num = (int) (Math.random() * servers_num);
		String random_server = availble_servers.remove(random_server_num);
		// TODO: Add to in_use_servers here?
		in_use_servers.add(random_server);

		return random_server;
	}

	private Session session_connect(String host) throws Exception {
		Session session = server_sessions.get(host);

		if (session == null) {
			session = Connection_Manager.createSession(host);
			server_sessions.put(host, session);
		}

		return session;
	}

	public void disconnect_sessions() {
		for (Session session : server_sessions.values()) {
			if (session.isConnected()) {
				session.disconnect();
			}
		}
	}
//
//	protected Op_Res run_cmd_on_multiple_servers(String cmd, String ips) {
//		Op_Res ret = ssh_execute_on_multiple_machines(cmd + ips);
//		/* Log operation - DEBUG level (e.g. "about to run cmd X on server Y") */
//		return ret;
//	}
}
