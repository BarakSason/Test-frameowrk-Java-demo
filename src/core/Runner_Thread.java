package core;

import java.lang.reflect.Method;
import java.util.HashMap;

import common.Globals;
import common.Logger;
import test.Test_Wrapper;

public class Runner_Thread extends Thread {
	private static final String GET_VOL_TYPES_METHOD = "get_vol_types"; // Name of the init method (should be same
																		// across all tests)
	private static final String INIT_METHOD = "init"; // Name of the init method (should be same across
														// all tests)
	private static final String EXECUTE_TEST_METHOD = "abstract_execute_test"; // Name of the run method (should be same
																				// across all tests)
	private static final String TERMINATE_METHOD = "terminate"; // Name of the terminate method (should be same across
																// all tests)
	private Test_Wrapper test_wrapper;
	private Logger logger;

	public Runner_Thread(Logger logger_arg, Test_Wrapper test_wrapper_arg) {
		this.logger = logger_arg;
		this.test_wrapper = test_wrapper_arg;
	}

	@Override
	public void run() {
		try {
			/* Instantiate the test */
			Object test_instance = instantiate_test(test_wrapper.test_class);

			/* Create a map of test methods */
			HashMap<String, Method> methods_map = create_methods_map(test_instance.getClass());

			/* Invoke the test method */
			Method get_vol_types_method = methods_map.get(GET_VOL_TYPES_METHOD);
			String vol_types_str = (String) get_vol_types_method.invoke(test_instance);

			String[] vol_types = vol_types_str.split(";");

			for (String vol_type : vol_types) {
				logger.log("*** Running " + "Test " + test_wrapper.test_type + "/" + test_wrapper.component + "/"
						+ test_wrapper.test_name + " ***");

				/* Invoke the test method */
				Method init_method = methods_map.get(INIT_METHOD);
				init_method.invoke(test_instance, test_wrapper.test_type, test_wrapper.component,
						test_wrapper.test_name, vol_type);

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

	/* Create a map of test methods */
	private static HashMap<String, Method> create_methods_map(Class<?> cls) {
		Method[] methods = cls.getMethods();

		HashMap<String, Method> methods_map = new HashMap<String, Method>();

		for (Method method : methods) {
			methods_map.put(method.getName(), method);
		}

		return methods_map;
	}

	/* Instantiate a test */
	private Object instantiate_test(Class<?> test_class) throws Exception {
		Object test_instance = null;
		test_instance = test_class.getDeclaredConstructor().newInstance();

		return test_instance;
	}
}
