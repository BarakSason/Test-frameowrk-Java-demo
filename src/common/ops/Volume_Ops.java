package common.ops;

import java.util.ArrayList;

import common.Abstract_Ops;
import common.Logger;
import common.distributed_executioner.Distributed_Executioner;
import core.Params_Handler;

public class Volume_Ops extends Abstract_Ops {

	public Volume_Ops(Logger logger, Distributed_Executioner distributed_executioner) {
		super(logger, distributed_executioner);
	}

	public void volume_create(String host, String volname, boolean force) throws Exception {
		ArrayList<String> servers = Params_Handler.get_servers();
		StringBuilder bricks_str = new StringBuilder();

		for (int i = 0; i < servers.size(); ++i) {
			bricks_str.append(servers.get(i) + ":" + "/root/bricks" + volname + "-" + i + " "); // TODO: Parse and
																								// randomize brick
																								// path per server
		}

		String cmd = "gluster volume create " + volname + " " + bricks_str.toString();

		if (force) {
			cmd += " force";
		}

		execute_abstract_server_op(cmd, host);
	}

	public void volume_start(String host, String volname) throws Exception {
		String cmd = "gluster volume start " + volname;

		execute_abstract_server_op(cmd, host);
	}

	public void volume_status(String host, String volname) throws Exception {
		String cmd = "gluster volume status " + volname;

		execute_abstract_server_op(cmd, host);
	}

	public void get_brick_paths(String host, String volname) throws Exception {
		String cmd = "gluster volume status " + volname + " --xml";

		execute_abstract_server_op_xml(cmd, host, "path");
	}

	public void get_brick_pids(String host, String volname) throws Exception {
		String cmd = "gluster volume status " + volname + " --xml";

		execute_abstract_server_op_xml(cmd, host, "pid");
	}

	public void volume_stop(String host, String volname) throws Exception {
		String cmd = "gluster volume stop " + volname + " --mode=script";

		execute_abstract_server_op(cmd, host);
	}

	public void volume_delete(String host, String volname) throws Exception {
		String cmd = "gluster volume delete " + volname + " --mode=script";

		execute_abstract_server_op(cmd, host);
	}
}
