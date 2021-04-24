package common.ops;

import common.Abstract_Ops;
import common.Distributed_Executioner;
import common.Logger;

public class Gluster_Ops extends Abstract_Ops {

	public Gluster_Ops(Logger logger, Distributed_Executioner distributed_executioner) {
		super(logger, distributed_executioner);
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
