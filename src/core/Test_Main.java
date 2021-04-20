package core;

import java.io.File;
import java.util.LinkedList;

import common.Globals;
import common.Logger;
import common.distributed_executioner.Distributed_Executioner;
import common.ops.*;
import ssh.Connection_Manager;

public class Test_Main {
	static Logger logger;

	public static void main(String args[]) throws Exception {
		Distributed_Executioner distributed_executioner = null;

		try {
			Params_Handler.parseConfigFile(args[0]); // args[0] - Path of config file
			logger = new Logger();
			String test_dir_path = Params_Handler.read_value("tests_path");

			/* Initiating SSH connection */
			Connection_Manager.init();

			/* Instantiate Ops libs required by framework for cluster creation */
			distributed_executioner = new Distributed_Executioner(logger);
			Gluster_Ops gluster_ops = new Gluster_Ops(logger, distributed_executioner);
			Peer_Ops peer_ops = new Peer_Ops(logger, distributed_executioner);

			/* Creating cluster */
			String random_server = distributed_executioner.randomize_server();
			create_cluster(distributed_executioner, gluster_ops, peer_ops, random_server);

			/* Selecting tests to run */
			Test_Runner.tests_to_run = Test_List_Builder.create_test_list(test_dir_path);

			/* Running tests */
			Test_Runner.run_tests(logger);

			/* Destroying cluster */
			destroy_cluster(distributed_executioner, gluster_ops, peer_ops, random_server);
		} catch (Exception e) {
			logger.log_failure(e);
		}

		/* Disconnecting sessions */
		if (distributed_executioner != null) {
			distributed_executioner.disconnect_sessions();
		}

		/* Remove compiled test binaries */
		delete_test_binaries();

	}

	private static void create_cluster(Distributed_Executioner distributed_executioner, Gluster_Ops gluster_ops,
			Peer_Ops peer_ops, String random_server) throws Exception {
		gluster_ops.glusterd_start(random_server);

		for (String server : distributed_executioner.availble_servers) {
			gluster_ops.glusterd_start(server);
			peer_ops.peer_probe(random_server, server);
		}
	}

	private static void destroy_cluster(Distributed_Executioner distributed_executioner, Gluster_Ops gluster_ops,
			Peer_Ops peer_ops, String random_server) throws Exception {
		for (String server : distributed_executioner.availble_servers) {
			peer_ops.peer_detach(random_server, server);
			gluster_ops.glusterd_stop(server);
		}
		gluster_ops.glusterd_stop(random_server);
	}

	private static void delete_test_binaries() throws Exception {
		// TODO: Parse path from config file

		LinkedList<String> dirs_to_scan = new LinkedList<String>();
		dirs_to_scan.add(Globals.BIN_PATH);

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
				logger.log_failure(e);
			}
		}
	}
}
