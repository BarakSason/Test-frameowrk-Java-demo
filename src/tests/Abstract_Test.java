package tests;

import common.Logger;
import common.distributed_executioner.Distributed_Executioner;
import common.ops.*;

public abstract class Abstract_Test {
	/*
	 * This class contains generic info and methods which are needed by all (or
	 * most) tests, such as: Test result var / A methods to create a test-specific
	 * vol / A method to clean the vol once the test is done / Etc'
	 */
	private static boolean PASS = true; // TODO: move these values to GLOBALS class
	private static boolean FAIL = false;

	private boolean test_res = PASS;

	/*
	 * General purpose Ops libs will be declared here, if a test needs additional
	 * Ops libs, the test should declare them in the test code
	 */
	protected Gluster_Ops gluster_ops;
	protected Volume_Ops volume_ops;
	protected Mount_Ops mount_ops;
	protected IO_Ops io_ops;

	protected Distributed_Executioner distributed_executioner;

	protected String component;
	protected String test_name;

	protected abstract void execute_test() throws Exception;

	public boolean abstract_execute_test() {
		try {
			execute_test();
		} catch (Exception e) {
			test_res = FAIL;
			Logger.post_op_log_failure(e);
		}

		return test_res;
	}

	public Abstract_Test(String component_arg, String test_name_arg) throws Exception {
		this.component = component_arg;
		this.test_name = test_name_arg;

		core_init();

		gluster_ops = new Gluster_Ops(distributed_executioner);
		volume_ops = new Volume_Ops(distributed_executioner);
		mount_ops = new Mount_Ops(distributed_executioner);
		io_ops = new IO_Ops(distributed_executioner);
	}

	public void core_init() {
		System.out.println(test_name + " from component " + component + " inits");
		// Init logger
		distributed_executioner = new Distributed_Executioner();
	}

	public void terminate() {
		System.out.println(test_name + " terminates");

		if (distributed_executioner != null) {
			distributed_executioner.disconnect_sessions();
		}
	}
}
