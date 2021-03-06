package com.barak.test_framework_poc.common.ops;

import com.barak.test_framework_poc.common.Abstract_Ops;
import com.barak.test_framework_poc.common.Distributed_Executioner;
import com.barak.test_framework_poc.common.Logger;

public class Mount_Ops extends Abstract_Ops {

	public Mount_Ops(Logger logger, Distributed_Executioner distributed_executioner) {
		super(logger, distributed_executioner);
	}

	public void mount_volume(String client, String server, String volname, String mountpoint) throws Exception {
		String cmd = "mount -t glusterfs " + server + ":/" + volname + " " + mountpoint;

		execute_abstract_client_op(cmd, client);
	}

	public void unmount_volume(String host, String volname, String mountpoint) throws Exception {
		String cmd = "umount " + mountpoint;

		execute_abstract_client_op(cmd, host);
	}
}
