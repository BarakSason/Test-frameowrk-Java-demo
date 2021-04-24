package tests.functional.dht;

import common.Globals;
import test.Abstract_Test;

public class Test_negative_file_operations extends Abstract_Test {
	public static String vol_types = "dist;dist-rep;disp";

	public void execute_test() throws Exception {
		ls_non_existing_path();
		rename_non_existing_file();
	}

	private void ls_non_existing_path() throws Exception {
		try {
			io_ops.execute_io_cmd(client_1, "ls -l " + mountpoint + "/non_existing_path");
			test_res = Globals.FAILURE;
		} catch (Exception e) {
			logger.handle_expected_exception(e);
		}
	}

	private void rename_non_existing_file() throws Exception {
		try {
			io_ops.execute_io_cmd(client_1, "cd " + mountpoint + "; mv non_existing_file non_existing_file_2");
			test_res = Globals.FAILURE;
		} catch (Exception e) {
			logger.handle_expected_exception(e);
		}
	}
}