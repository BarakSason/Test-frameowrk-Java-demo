package core;

import java.io.File;
import java.util.LinkedList;

import common.Globals;
import common.Logger;
import common.distributed_executioner.Distributed_Executioner;
import common.ops.*;
import ssh.Connection_Manager;

public class Test_Main {
	public static void main(String args[]) throws Exception {
		Distributed_Executioner distributed_executioner = null;

		try {
			Params_Handler.parseConfigFile(args[0]); // args[0] - Path of config file

			String test_dir_path = args[1]; // args[0] - Path tests dir

			/* Initiating SSH connection */
			Connection_Manager.init();

			/* Instantiate Ops libs required by framework for cluster creation */
			distributed_executioner = new Distributed_Executioner();
			Gluster_Ops gluster_ops = new Gluster_Ops(distributed_executioner);
			Peer_Ops peer_ops = new Peer_Ops(distributed_executioner);

			/* Creating cluster */
			create_cluster(gluster_ops, peer_ops);

			/* Running tests */
			LinkedList<String> tests_path = new LinkedList<String>();
			tests_path.add(test_dir_path);
			Test_Runner.tests_to_run = Test_List_Builder.create_test_list(tests_path);
			Test_Runner.run_tests();

			/* Destroying cluster */
			destroy_cluster(gluster_ops, peer_ops);
		} catch (Exception e) {
			Logger.log_failure(e);
		}

		/* Disconnecting sessions */
		if (distributed_executioner != null) {
			distributed_executioner.disconnect_sessions();
		}

		/* Remove compiled test binaries */
		delete_test_binaries();

	}

	private static void create_cluster(Gluster_Ops gluster_ops, Peer_Ops peer_ops) throws Exception {
		/*
		 * TODO: Check number of servers in config file - If there is a single server,
		 * the framework will operate locally, if there are 2 servers, the 1st will be
		 * used as management and the 2nd will be used as both server and client. In
		 * both of these cases, cluster creation is not needed
		 */
		/* TODO: Parse hosts from config file */
		gluster_ops.glusterd_start(Globals.server_1);
		gluster_ops.glusterd_start(Globals.server_2);
		gluster_ops.glusterd_start(Globals.server_3);

		peer_ops.peer_probe(Globals.server_1, Globals.server_2);
		peer_ops.peer_probe(Globals.server_1, "VM3");
	}

	private static void destroy_cluster(Gluster_Ops gluster_ops, Peer_Ops peer_ops) throws Exception {
		peer_ops.peer_detach(Globals.server_1, Globals.server_2);
		peer_ops.peer_detach(Globals.server_1, Globals.server_3);

		gluster_ops.glusterd_stop(Globals.server_1);
		gluster_ops.glusterd_stop(Globals.server_2);
		gluster_ops.glusterd_stop(Globals.server_3);
	}

	private static void delete_test_binaries() {
		// TODO: Parse path from config file

		LinkedList<String> dirs_to_scan = new LinkedList<String>();
		dirs_to_scan.add(Globals.BIN_PATH);

		while (!dirs_to_scan.isEmpty()) {
			try {
				File cur_dir = new File(dirs_to_scan.remove());
				File[] entries = cur_dir.listFiles();

				for (File entry : entries) {
					if (entry.isFile() && entry.getName().startsWith(Globals.test_prefix)) {
						entry.delete();
					} else {
						if (entry.isDirectory()) {
							dirs_to_scan.add(entry.getAbsolutePath());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace(); //TODO: replace with logging
			}
		}
	}
}
