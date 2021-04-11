package common.ops;

import common.Abstract_Ops;
import common.distributed_executioner.Distributed_Executioner;

public class Mount_Ops extends Abstract_Ops {

	public Mount_Ops(Distributed_Executioner distributed_executioner_arg) {
		super(distributed_executioner_arg);
	}

	public void mount_volume(String host, String volname, String mountpoint) throws Exception {
		String cmd = "mount -t glusterfs " + host + ":/" + volname + " " + mountpoint;

		execute_abstract_server_op(cmd, host);
	}

	// TODO: Add APIs without hostname, once an API to get a random host has been
	// added
//	public void mount_volume(String volname, String mountpoint) throws Exception {
//		mount_volume(null, volname, mountpoint);
//	}

	public void unmount_volume(String host, String volname, String mountpoint) throws Exception {
		String cmd = "umount " + mountpoint;

		execute_abstract_server_op(cmd, host);
	}
}
