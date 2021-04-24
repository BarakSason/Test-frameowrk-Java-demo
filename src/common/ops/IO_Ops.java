package common.ops;

import common.Abstract_Ops;
import common.Distributed_Executioner;
import common.Logger;

public class IO_Ops extends Abstract_Ops {
	public IO_Ops(Logger logger, Distributed_Executioner distributed_executioner) {
		super(logger, distributed_executioner);
	}

	public void execute_io_cmd(String host, String cmd) throws Exception {
		execute_abstract_server_op(cmd, host);
	}
}
