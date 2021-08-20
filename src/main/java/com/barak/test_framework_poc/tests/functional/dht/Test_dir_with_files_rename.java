package com.barak.test_framework_poc.tests.functional.dht;

import com.barak.test_framework_poc.test.Abstract_Test;

public class Test_dir_with_files_rename extends Abstract_Test {
	public static String vol_types = "dist;dist-rep;disp";

	public void execute_test() throws Exception {
		io_ops.execute_io_cmd(client_1, "cd " + mountpoint + "; mkdir dir_1; cd dir_1; touch {1..100}");
		io_ops.execute_io_cmd(client_1, "ls -l " + mountpoint + "/dir_1");
		io_ops.execute_io_cmd(client_1, "cd " + mountpoint + "; mv dir_1 dir_2");
		io_ops.execute_io_cmd(client_1, "ls -l " + mountpoint + "/dir_2");
	}
}