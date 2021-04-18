package core;

import java.lang.reflect.Method;
import java.util.HashMap;

import common.Globals;

public class Runner_Thread extends Thread {
	private static final String INIT_METHOD = "init"; // Name of the init method (should be same across
														// all tests)
	private static final String EXECUTE_TEST_METHOD = "abstract_execute_test"; // Name of the run method (should be same
																				// across all tests)
	private static final String TERMINATE_METHOD = "terminate"; // Name of the terminate method (should be same across
																// all tests)
	private Object test_instance;

	public Runner_Thread(Object test_instance_arg) {
		this.test_instance = test_instance_arg;
	}

	@Override
	public void run() {
		try {
			/* Create a map of test methods */
			HashMap<String, Method> methods_map = create_methods_map(test_instance.getClass());

			/* Invoke the test method */
			Method init_method = methods_map.get(INIT_METHOD);
			init_method.invoke(test_instance);

			/* Invoke the test method */
			Method test_method = methods_map.get(EXECUTE_TEST_METHOD);
			int test_result = (int) test_method.invoke(test_instance);

			/* Invoke the terminate method */
			Method terminate_method = methods_map.get(TERMINATE_METHOD);
			terminate_method.invoke(test_instance);

			// TODO: Don't use test_instance.getClass().getSimpleName()
			if (test_result == Globals.SUCCESS) {
				System.out.println("*** " + test_instance.getClass().getSimpleName() + " Passed ***");
			} else {
				System.out.println("*** " + test_instance.getClass().getSimpleName() + " Failed ***");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Create a map of test methods */
	private static HashMap<String, Method> create_methods_map(Class<?> cls) {
		Method[] methods = cls.getMethods();

		HashMap<String, Method> methods_map = new HashMap<String, Method>();

		for (Method m : methods) {
			methods_map.put(m.getName(), m);
		}

		return methods_map;
	}
}
