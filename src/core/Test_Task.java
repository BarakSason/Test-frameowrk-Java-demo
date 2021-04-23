package core;

import java.lang.reflect.Method;
import java.util.HashMap;

import common.Globals;
import common.Logger;
import test.Test_Wrapper;
import test.Test_Wrapper.Test_Res;

public class Test_Task implements Runnable {
	private static final String INIT_METHOD = "init";
	private static final String EXECUTE_TEST_METHOD = "abstract_execute_test";
	private static final String TERMINATE_METHOD = "terminate";
	private static final String TIME_METHOD = "get_execution_time";
	private Test_Wrapper test_wrapper;
	private Logger logger;
	private HashMap<String, Method> methods_map;
	private Object test_instance;
	private String vol_type;

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
		long execution_time = 0;
		try {
			int test_res;

			/* Invoke the test method */
			Method init_method = methods_map.get(INIT_METHOD);
			test_res = (int) init_method.invoke(test_instance, test_wrapper.test_type, test_wrapper.component,
					test_wrapper.test_name, vol_type);

			if (test_res == Globals.SUCCESS) {
				/* Invoke the test method */
				Method test_method = methods_map.get(EXECUTE_TEST_METHOD);
				test_res = (int) test_method.invoke(test_instance);
			}

			/* Invoke the terminate method */
			Method terminate_method = methods_map.get(TERMINATE_METHOD);
			test_res = (int) terminate_method.invoke(test_instance);

			if (test_res == Globals.SUCCESS) {
				/* Invoke the terminate method */
				Method time_method = methods_map.get(TIME_METHOD);
				execution_time = (long) time_method.invoke(test_instance);

				logger.log_and_print("*** " + "Test " + test_wrapper.test_type + "/" + test_wrapper.component + "/"
						+ test_wrapper.test_name + "-" + vol_type + " Passed ***, executed in " + execution_time / 1000
						+ "." + execution_time % 1000 + " seconds");
			} else {
				logger.log_and_print("*** " + "Test " + test_wrapper.test_type + "/" + test_wrapper.component + "/"
						+ test_wrapper.test_name + "-" + vol_type + " Failed ***");

			}
			synchronized (test_wrapper) {
				test_wrapper.res_list.add(new Test_Res(test_res, vol_type, execution_time));
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
