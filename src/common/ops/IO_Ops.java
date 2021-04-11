package common.ops;

import common.Abstract_Ops;
import common.distributed_executioner.Distributed_Executioner;

public class IO_Ops extends Abstract_Ops {
	public IO_Ops(Distributed_Executioner distributed_executioner_arg) {
		super(distributed_executioner_arg);
	}

	public void execute_io_cmd(String host, String cmd) throws Exception {
		execute_abstract_server_op(cmd, host);
	}
}
