package tests;

import common.Globals;
import common.Logger;
import common.distributed_executioner.Distributed_Executioner;
import common.ops.*;

public abstract class Abstract_Test {
	/*
	 * This class contains generic info and methods which are needed by all (or
	 * most) tests, such as: Test result var / A methods to create a test-specific
	 * vol / A method to clean the vol once the test is done / Etc'
	 */
	protected Logger logger;

	private int test_res = Globals.SUCCESS;

	/*
	 * General purpose Ops libs will be declared here, if a test needs additional
	 * Ops libs, the test should declare them in the test code
	 */
	protected Volume_Ops volume_ops;
	protected Mount_Ops mount_ops;

	protected Distributed_Executioner distributed_executioner;

	protected String test_type;
	protected String component;
	protected String test_name;

	protected abstract void execute_test() throws Exception;

	public int abstract_execute_test() throws Exception {
		try {
			execute_test();
		} catch (Exception e) {
			test_res = Globals.FAILURE;
			logger.log_failure(e);
		}

		return test_res;
	}

	public Abstract_Test(String test_type_arg, String component_arg, String test_name_arg) throws Exception {
		this.test_type = test_type_arg;
		this.component = component_arg;
		this.test_name = test_name_arg;

		core_init();

		volume_ops = new Volume_Ops(logger, distributed_executioner);
		mount_ops = new Mount_Ops(logger, distributed_executioner);
	}

	private void core_init() throws Exception {
		logger = new Logger(test_type, component, test_name);
		logger.log("Test " + test_type + "/" + component + "/" + test_name + " initializes");
		distributed_executioner = new Distributed_Executioner(logger);
	}

	// TODO: return and check value
	public void init() {
		// Volume creation & start
		// Volume mount
	}

	public void terminate() throws Exception {
		logger.log("Test " + test_type + "/" + component + "/" + test_name + " terminaiting");

		if (distributed_executioner != null) {
			distributed_executioner.disconnect_sessions();
		}
	}
}
