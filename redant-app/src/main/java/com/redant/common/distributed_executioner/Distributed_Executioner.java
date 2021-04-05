package com.redant.common.distributed_executioner;

import java.util.HashMap;

import com.jcraft.jsch.Session;

import com.redant.common.Logger;
import com.redant.core.Params_Handler;
import com.redant.ssh.Connection_Manager;
import com.redant.ssh.Remote_Executioner;

public class Distributed_Executioner extends Remote_Executioner {
	private HashMap<String, Session> sessions = new HashMap<String, Session>();

	public Op_Res execute_cmd_on_single_server(String cmd, String host) throws Exception {
		// TODO: differentiate between client and sever ops
		if (host == null) {
			host = randomize_host();
		}

		/*
		 * Pre-op log (INFO level) - Log operation (e.g.
		 * "Executing command "X" on host "Y")
		 */
//		System.out.println("Executing command \"" + cmd + "\" on host \"" + host + "\"");
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
	public String randomize_host() {
		int hosts_num = Params_Handler.get_servers_num();

		int max = hosts_num;
		int random_server_num = (int) (Math.random() * max + 1);
		return Params_Handler.read_server(random_server_num);
	}

	private Session session_connect(String host) throws Exception {
		Session session = sessions.get(host);

		if (session == null) {
			session = Connection_Manager.createSession(host);
			sessions.put(host, session);
		}

		return session;
	}

	public void disconnect_sessions() {
		for (Session session : sessions.values()) {
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
