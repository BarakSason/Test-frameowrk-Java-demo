package common.ops;

import common.Abstract_Ops;
import common.Logger;
import common.distributed_executioner.Distributed_Executioner;

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
