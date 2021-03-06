package com.barak.test_framework_poc.common;

import java.util.ArrayList;
import java.util.HashMap;

import com.barak.test_framework_poc.core.Params_Handler;
import com.barak.test_framework_poc.ssh.Connection_Manager;
import com.barak.test_framework_poc.ssh.Remote_Executioner;
import com.jcraft.jsch.Session;

public class Distributed_Executioner extends Remote_Executioner {
	private Logger logger;

	// TODO: make all fields private
	public ArrayList<String> availble_servers;
	public ArrayList<String> in_use_servers;
	private HashMap<String, Session> server_sessions;

	public ArrayList<String> availble_clients;
	public ArrayList<String> in_use_clients;
	private HashMap<String, Session> client_sessions;

	public Distributed_Executioner(Logger logger_arg) {
		super(logger_arg);
		this.logger = logger_arg;

		availble_servers = Params_Handler.get_servers();
		in_use_servers = new ArrayList<String>();
		server_sessions = new HashMap<String, Session>();

		availble_clients = Params_Handler.get_clients();
		in_use_clients = new ArrayList<String>();
		client_sessions = new HashMap<String, Session>();
	}

	public Op_Res execute_cmd_on_single_server(String cmd, String host) throws Exception {
		logger.pre_op_log(cmd, host);
		Session session = connect_server_session(host);
		Op_Res op_res = execute_remote_cmd(cmd, session);
		return op_res;
	}

	public Op_Res execute_cmd_on_single_client(String cmd, String host) throws Exception {
		logger.pre_op_log(cmd, host);
		Session session = connect_client_session(host);
		Op_Res op_res = execute_remote_cmd(cmd, session);
		return op_res;
	}

	// TODO: Handle failures
	public String randomize_server() {
		int servers_num = availble_servers.size();

		int random_server_num = (int) (Math.random() * servers_num);
		String random_server = availble_servers.remove(random_server_num);
		in_use_servers.add(random_server);

		return random_server;
	}

	// TODO: Handle failures
	public String randomize_client() {
		int clients_num = availble_clients.size();

		int random_client_num = (int) (Math.random() * clients_num);
		String random_client = availble_clients.remove(random_client_num);
		in_use_clients.add(random_client);

		return random_client;
	}

	private Session connect_server_session(String host) throws Exception {
		Session session = server_sessions.get(host);

		if (session == null) {
			session = Connection_Manager.createSession(host);
			server_sessions.put(host, session);
		}

		return session;
	}

	private Session connect_client_session(String host) throws Exception {
		Session session = client_sessions.get(host);

		if (session == null) {
			session = Connection_Manager.createSession(host);
			client_sessions.put(host, session);
		}

		return session;
	}

	public void disconnect_sessions() {
		for (Session session : server_sessions.values()) {
			if (session.isConnected()) {
				session.disconnect();
			}
		}

		for (Session session : client_sessions.values()) {
			if (session.isConnected()) {
				session.disconnect();
			}
		}
	}
}
