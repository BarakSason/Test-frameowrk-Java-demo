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

	private int test_res = Globals.SUCCESS;

	/*
	 * General purpose Ops libs will be declared here, if a test needs additional
	 * Ops libs, the test should declare them in the test code
	 */
	protected Volume_Ops volume_ops;
	protected Mount_Ops mount_ops;

	protected Distributed_Executioner distributed_executioner;

	protected String component;
	protected String test_name;

	protected abstract void execute_test() throws Exception;

	public int abstract_execute_test() {
		try {
			execute_test();
		} catch (Exception e) {
			test_res = Globals.FAILURE;
			Logger.log_failure(e);
		}

		return test_res;
	}

	public Abstract_Test(String component_arg, String test_name_arg) throws Exception {
		this.component = component_arg;
		this.test_name = test_name_arg;

		core_init();

		volume_ops = new Volume_Ops(distributed_executioner);
		mount_ops = new Mount_Ops(distributed_executioner);
	}

	private void core_init() {
		Logger.print(test_name + " from component " + component + " inits");
		// TODO: Init logger
		distributed_executioner = new Distributed_Executioner();
	}

	// TODO: return and check value
	public void init() {
		// Volume creation & start
		// Volume mount
	}

	public void terminate() {
		Logger.print(test_name + " terminates");

		if (distributed_executioner != null) {
			distributed_executioner.disconnect_sessions();
		}
	}
}
