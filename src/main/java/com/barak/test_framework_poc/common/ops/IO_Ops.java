package com.barak.test_framework_poc.common.ops;

import com.barak.test_framework_poc.common.Abstract_Ops;
import com.barak.test_framework_poc.common.Distributed_Executioner;
import com.barak.test_framework_poc.common.Logger;

public class IO_Ops extends Abstract_Ops {
	public IO_Ops(Logger logger, Distributed_Executioner distributed_executioner) {
		super(logger, distributed_executioner);
	}

	public void execute_io_cmd(String host, String cmd) throws Exception {
		execute_abstract_server_op(cmd, host);
	}
}
