package core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import common.Globals;
import common.Logger;
import test.Test_Wrapper;

public class Test_Task implements Runnable {
	private static final String INIT_METHOD = "init"; // Name of the init method (should be same across all tests)
	private static final String EXECUTE_TEST_METHOD = "abstract_execute_test"; // Name of the run method (should be same
																				// across all tests)
	private static final String TERMINATE_METHOD = "terminate"; // Name of the terminate method (should be same across
																// all tests)
	private Test_Wrapper test_wrapper;
	private Logger logger;
	HashMap<String, Method> methods_map;
	Object test_instance;
	String vol_type;

	public Test_Task(Logger logger_arg, Test_Wrapper test_wrapper_arg, HashMap<String, Method> methods_map_arg,
			Object test_instance_arg, String vol_type_arg) {
		this.logger = logger_arg;
		this.test_wrapper = test_wrapper_arg;
		this.methods_map = methods_map_arg;
		this.test_instance = test_instance_arg;
		this.vol_type = vol_type_arg;
	}

	@Override
	public void run() {
		try {
			/* Invoke the test method */
			Method init_method = methods_map.get(INIT_METHOD);
			init_method.invoke(test_instance, test_wrapper.test_type, test_wrapper.component, test_wrapper.test_name,
					vol_type);

			/* Invoke the test method */
			Method test_method = methods_map.get(EXECUTE_TEST_METHOD);
			int test_result = (int) test_method.invoke(test_instance);

			/* Invoke the terminate method */
			Method terminate_method = methods_map.get(TERMINATE_METHOD);
			terminate_method.invoke(test_instance);

			if (test_result == Globals.SUCCESS) {
				logger.log("*** " + "Test " + test_wrapper.test_type + "/" + test_wrapper.component + "/"
						+ test_wrapper.test_name + " Passed ***");
			} else {
				logger.log("*** " + "Test " + test_wrapper.test_type + "/" + test_wrapper.component + "/"
						+ test_wrapper.test_name + " Failed ***");
			}
		} catch (Exception e) {
			try {
				logger.log_failure(e);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}
}
