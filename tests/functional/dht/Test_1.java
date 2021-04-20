package tests.functional.dht;

import test.Abstract_Test;

public class Test_1 extends Abstract_Test {
	public Test_1() {
		vol_types = "dist;dist-rep;disp";
	}

	public void execute_test() throws Exception {
		io_ops.execute_io_cmd(random_client, "ls -l /root");
		volume_ops.volume_status(random_server, volname);
		io_ops.execute_io_cmd(random_client, "cd " + mountpoint + "; touch {1..100}");
		io_ops.execute_io_cmd(random_client, "ls -l " + mountpoint);
		io_ops.execute_io_cmd(random_client, "cd " + mountpoint + "; rm -rf *");
		io_ops.execute_io_cmd(random_client, "ls -l " + mountpoint);

		try {
			io_ops.execute_io_cmd(random_client, "ls -l /non-exsisting-path"); // An op which is expected to fail
		} catch (Exception e) {
			logger.handle_expected_exception(e);
		}
	}
}