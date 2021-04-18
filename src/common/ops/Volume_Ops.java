package common.ops;

import common.Abstract_Ops;
import common.distributed_executioner.Distributed_Executioner;

public class Volume_Ops extends Abstract_Ops {

	public Volume_Ops(Distributed_Executioner distributed_executioner_arg) {
		super(distributed_executioner_arg);
	}

	// TODO: Add missing argumnets and eliminate hardcoded values
	public void volume_create(String host, String volname, boolean force) throws Exception {
		String bricks = "VM1:/root/bricks/brick1 VM2:/root/bricks/brick2 VM3:/root/bricks/brick3";
		String cmd = "gluster volume create " + volname + " " + bricks;

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
