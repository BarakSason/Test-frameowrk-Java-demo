package test;

import common.Distributed_Executioner;
import common.Globals;
import common.Logger;
import common.ops.*;

public abstract class Abstract_Test {
	/*
	 * This class contains generic info and methods which are needed by all (or
	 * most) tests, such as: Test result var / A methods to create a test-specific
	 * vol / A method to clean the vol once the test is done / Etc'
	 */
	protected Logger logger;

	protected int test_res = Globals.SUCCESS;

	/*
	 * General purpose Ops libs will be declared here, if a test needs additional
	 * Ops libs, they should declare them in the test code
	 */
	protected Volume_Ops volume_ops;
	protected Mount_Ops mount_ops;
	protected IO_Ops io_ops;

	protected Distributed_Executioner distributed_executioner;

	protected String test_type;
	protected String component;
	protected String test_name;
	protected String vol_type;

	protected String server_1;
	protected String client_1;
	protected String volname;
	protected String mountpoint;

	private long start_time;
	private long end_time;
	private long execution_time;

	/* The abstract test method (must be implemented by all tests) */
	protected abstract void execute_test() throws Exception;

	/* Abstract test execution method to be invoked by the execution thread */
	public int abstract_execute_test() throws Exception {
		try {
			logger.log_and_print("Executing test " + test_type + "/" + component + "/" + test_name + "-" + vol_type);
			execute_test();
		} catch (Exception e) {
			test_res = Globals.FAILURE;
			logger.handle_failure(e);
		}

		return test_res;
	}

	/*
	 * Core initialization functionality required by absolutely ALL tests. If a test
	 * does not require functionality that exists in the "init" method (e.g. volume
	 * creation), the test can overwrite it's init method (in the test code) to call
	 * "core_init" only
	 */
	private void core_init() throws Exception {
		logger = new Logger(test_type, component, test_name, vol_type);
		logger.log("Test " + test_type + "/" + component + "/" + test_name + "-" + vol_type + " initializes");
		distributed_executioner = new Distributed_Executioner(logger);
	}

	/* Init method to be invoked by the execution thread */
	public int init(String test_type_arg, String component_arg, String test_name_arg, String vol_type_arg)
			throws Exception {
		start_time = System.currentTimeMillis();

		this.test_type = test_type_arg;
		this.component = component_arg;
		this.test_name = test_name_arg;
		this.vol_type = vol_type_arg;

		try {
			/* Perform basic initialization operations */
			core_init();

			/* Instantiate basic ops libs */
			volume_ops = new Volume_Ops(logger, distributed_executioner);
			mount_ops = new Mount_Ops(logger, distributed_executioner);
			io_ops = new IO_Ops(logger, distributed_executioner);

			/* Randomize 1 server and 1 client machines */
			server_1 = distributed_executioner.randomize_server();
			client_1 = distributed_executioner.randomize_client();

			/* Generate names & paths */
			volname = test_name + "-" + vol_type;
			mountpoint = "/mnt/" + volname; // TODO: Parse mountpoint from config file, per server
											// (don't assume /mnt)

			/* Volume create and start */
			volume_ops.volume_create(server_1, volname, true);
			volume_ops.volume_start(server_1, volname);

			/* Create mountpoint and mount volume */
			io_ops.execute_io_cmd(client_1, "mkdir " + mountpoint);
			mount_ops.mount_volume(client_1, server_1, volname, mountpoint);
		} catch (Exception e) {
			test_res = Globals.FAILURE;
			logger.handle_failure(e);
		}

		return test_res;
	}

	/* Terminate method to be invoked by the execution thread */
	public int terminate() throws Exception {
		try {
			logger.log("Test " + test_type + "/" + component + "/" + test_name + "-" + vol_type + " terminaiting");

			/* Delete all data created by the test */
			io_ops.execute_io_cmd(client_1, "rm -rf " + mountpoint + "/*");

			/* Volume stop, delete, mount and remove mountpoint */
			volume_ops.volume_stop(server_1, volname);
			volume_ops.volume_delete(server_1, volname);

			/* Unmount and delete mountpoint */
			mount_ops.unmount_volume(client_1, volname, mountpoint);
			io_ops.execute_io_cmd(client_1, "rm -rf " + mountpoint);
		} catch (Exception e) {
			test_res = Globals.FAILURE;
			logger.handle_failure(e);
			// TODO: An additional layer of cleaning is needed - All data MUST be cleaned,
			// even in the event of a failure.
			// One implementation might be the framework itself iterates over all mountponts
			// and bricks and ensures
			// all data has been deleted
		}

		/* Disconnect all session which were opened by the test */
		if (distributed_executioner != null) {
			distributed_executioner.disconnect_sessions();
		}

		end_time = System.currentTimeMillis();

		if (test_res == Globals.SUCCESS) {
			execution_time = get_execution_time();

			logger.log_only("*** " + "Test " + test_type + "/" + component + "/" + test_name + "-" + vol_type
					+ " Passed ***, executed in " + execution_time / 1000 + "." + execution_time % 1000 + " seconds");
		} else {
			logger.log_only(
					"*** " + "Test " + test_type + "/" + component + "/" + test_name + "-" + vol_type + " Failed ***");
		}

		return test_res;
	}

	public long get_execution_time() {
		return end_time - start_time;
	}
}
