package tests.functional.dht;

import test.Abstract_Test;

public class Test_1 extends Abstract_Test {
	public static String vol_types="dist;dist-rep;disp";

	public void execute_test() throws Exception {
		io_ops.execute_io_cmd(client_1, "ls -l /root");
		volume_ops.volume_status(server_1, volname);
		io_ops.execute_io_cmd(client_1, "cd " + mountpoint + "; touch {1..100}");
		io_ops.execute_io_cmd(client_1, "ls -l " + mountpoint);
		io_ops.execute_io_cmd(client_1, "cd " + mountpoint + "; rm -rf *");
		io_ops.execute_io_cmd(client_1, "ls -l " + mountpoint);

		try {
			io_ops.execute_io_cmd(client_1, "ls -l /non-exsisting-path"); // An op which is expected to fail
		} catch (Exception e) {
			logger.handle_expected_exception(e);
		}
	}
}