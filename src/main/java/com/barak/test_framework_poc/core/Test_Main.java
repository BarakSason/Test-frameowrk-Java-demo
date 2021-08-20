package com.barak.test_framework_poc.core;

import java.io.File;
import java.util.LinkedList;

import com.barak.test_framework_poc.common.Distributed_Executioner;
import com.barak.test_framework_poc.common.Globals;
import com.barak.test_framework_poc.common.Logger;
import com.barak.test_framework_poc.common.ops.Gluster_Ops;
import com.barak.test_framework_poc.common.ops.Peer_Ops;
import com.barak.test_framework_poc.ssh.Connection_Manager;

public class Test_Main {
	private static Logger logger;

	public static void main(String args[]) throws Exception {
		long start_time = System.currentTimeMillis();
		long end_time;
		Distributed_Executioner distributed_executioner = null;

		try {
			/* Parsing */
			Params_Handler.parseConfigFile(args[0]); // args[0] - Path of config file

			/* Instantiate a logger for the framework */
			logger = new Logger();

			/* Initiating SSH library */
			Connection_Manager.init();

			/* Selecting tests to run */
			Test_Runner.tests_to_run = Test_List_Builder.create_test_list(Params_Handler.read_value("tests_path"));

			/* Instantiate Ops libs required by framework for cluster creation */
			distributed_executioner = new Distributed_Executioner(logger);
			Gluster_Ops gluster_ops = new Gluster_Ops(logger, distributed_executioner);
			Peer_Ops peer_ops = new Peer_Ops(logger, distributed_executioner);

			/* Creating cluster */
			String framework_server = distributed_executioner.randomize_server();
			create_cluster(distributed_executioner, gluster_ops, peer_ops, framework_server);

			/* Running tests */
			Test_Runner.run_tests(logger);

			/* Destroying cluster */
			destroy_cluster(distributed_executioner, gluster_ops, peer_ops, framework_server);

			/* Display and log results */
			Results_Handler.display_results(logger);
		} catch (Exception e) {
			logger.handle_failure(e);
		}

		/* Disconnecting sessions */
		if (distributed_executioner != null) {
			distributed_executioner.disconnect_sessions();
		}

		/* Remove compiled test binaries */
		delete_test_binaries();

		end_time = System.currentTimeMillis();
		long execution_time = end_time - start_time;

		logger.log_and_print(
				"*** Framework executed in " + execution_time / 1000 + "." + execution_time % 1000 + " seconds ***");

	}

	private static void create_cluster(Distributed_Executioner distributed_executioner, Gluster_Ops gluster_ops,
			Peer_Ops peer_ops, String framework_server) throws Exception {
		gluster_ops.glusterd_start(framework_server);

		for (String server : distributed_executioner.availble_servers) {
			gluster_ops.glusterd_start(server);
			peer_ops.peer_probe(framework_server, server);
		}

		peer_ops.peer_status(framework_server);

		// TODO: Ensure cluster is operational before continuing to avoid race cases
	}

	private static void destroy_cluster(Distributed_Executioner distributed_executioner, Gluster_Ops gluster_ops,
			Peer_Ops peer_ops, String framework_server) throws Exception {
		for (String server : distributed_executioner.availble_servers) {
			peer_ops.peer_detach(framework_server, server);
			gluster_ops.glusterd_stop(server);
		}
		gluster_ops.glusterd_stop(framework_server);
	}

	private static void delete_test_binaries() throws Exception {
		// TODO: Parse path from config file

		LinkedList<String> dirs_to_scan = new LinkedList<String>();
		dirs_to_scan.add(Params_Handler.read_value("bin_path"));

		while (!dirs_to_scan.isEmpty()) {
			try {
				File cur_dir = new File(dirs_to_scan.remove());
				File[] entries = cur_dir.listFiles();

				for (File entry : entries) {
					if (entry.isFile() && entry.getName().startsWith(Globals.TEST_PREFIX)) {
						entry.delete();
					} else {
						if (entry.isDirectory()) {
							dirs_to_scan.add(entry.getAbsolutePath());
						}
					}
				}
			} catch (Exception e) {
				logger.handle_failure(e);
			}
		}
	}
}
