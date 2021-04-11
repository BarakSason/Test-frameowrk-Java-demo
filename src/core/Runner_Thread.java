package core;

import java.lang.reflect.Method;
import java.util.HashMap;

import common.Globals;
import tests.TestWrapper;

public class Runner_Thread extends Thread {
	private static final String EXECUTE_TEST_METHOD = "abstract_execute_test"; // Name of the run method (should be same
																				// across all tests)
	private static final String TERMINATE_METHOD = "terminate"; // Name of the terminate method (should be same across
																// all tests)
	private TestWrapper tw;

	public Runner_Thread(TestWrapper tw_arg) {
		this.tw = tw_arg;
	}

	@Override
	public void run() {
		try {
			Object test_instance = tw.test_instance;
			HashMap<String, Method> methods_map = tw.methods_map;

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
}
