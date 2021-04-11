package tests.functional.dht;

import common.Globals;
import common.Logger;
//import core.Params_Handler;
import common.Framework_Exception;
import tests.Abstract_Test;

public class Test_1 extends Abstract_Test {

	public Test_1(String component, String test_name) throws Exception {
		super(component, test_name);
	}

	public void execute_test() throws Exception {
		Logger.print(test_name + " running");

//		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//		System.out.println(path);
//
//		String server_1 = Params_Handler.read_server(1);
//
//		TODO: Replace sleep by validation that all peers are in connected state
//		Thread.sleep(1000);

		String volname = "dist";
		String mountpoint = "/mnt/dist"; // TODO: Parse mountpoint per server from config file

		String random_server = distributed_executioner.randomize_server();

		io_ops.execute_io_cmd(Globals.io_client, "ls -l /root");
		volume_ops.volume_create(random_server, volname, true);
		volume_ops.volume_start(random_server, volname);
		volume_ops.volume_status(random_server, volname);
		io_ops.execute_io_cmd(Globals.io_client, "cd /mnt; mkdir " + volname);
		mount_ops.mount_volume(Globals.io_client, volname, mountpoint);
		io_ops.execute_io_cmd(Globals.io_client, "cd " + mountpoint + "; touch {1..100}");
		io_ops.execute_io_cmd(Globals.io_client, "ls -l " + mountpoint);
		io_ops.execute_io_cmd(Globals.io_client, "cd " + mountpoint + "; rm -rf *");
		io_ops.execute_io_cmd(Globals.io_client, "ls -l " + mountpoint);

		try {
			io_ops.execute_io_cmd(Globals.io_client, "ls -l /non-exsisting-path"); // An op which is expected to fail
		} catch (Exception e) {
			Logger.handle_expected_exception((Framework_Exception) e);
		}

		volume_ops.volume_stop(random_server, volname);
		volume_ops.volume_delete(random_server, volname);
		mount_ops.unmount_volume(Globals.io_client, volname, mountpoint);
		io_ops.execute_io_cmd(Globals.io_client, "rm -rf " + mountpoint);
	}
}
