package common.ops;

import common.Abstract_Ops;
import common.distributed_executioner.Distributed_Executioner;

public class Gluster_Ops extends Abstract_Ops {

	public Gluster_Ops(Distributed_Executioner distributed_executioner_arg) {
		super(distributed_executioner_arg);
	}

	public void glusterd_start(String host) throws Exception {
		String cmd = "systemctl start glusterd";

		execute_abstract_server_op(cmd, host);
	}

	public void glusterd_stop(String host) throws Exception {
		String cmd = "systemctl stop glusterd";

		execute_abstract_server_op(cmd, host);
	}
}
