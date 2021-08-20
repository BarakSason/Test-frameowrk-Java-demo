package com.barak.test_framework_poc.core;

import com.barak.test_framework_poc.common.Globals;
import com.barak.test_framework_poc.common.Logger;
import com.barak.test_framework_poc.test.Abstract_Test;
import com.barak.test_framework_poc.test.Test_Wrapper;
import com.barak.test_framework_poc.test.Test_Wrapper.Test_Res;

public class Test_Task implements Runnable {
	private Test_Wrapper test_wrapper;
	private Logger logger;
	private Abstract_Test test_instance;
	private String vol_type;

	public Test_Task(Logger logger_arg, Test_Wrapper test_wrapper_arg, Object test_instance_arg, String vol_type_arg) {
		this.logger = logger_arg;
		this.test_wrapper = test_wrapper_arg;
		this.test_instance = (Abstract_Test) test_instance_arg;
		this.vol_type = vol_type_arg;
	}

	@Override
	public void run() {
		long execution_time = 0;
		try {
			int test_res;

			logger.log_and_print("*** Executing test " + test_wrapper.test_type + "/" + test_wrapper.component + "/"
					+ test_wrapper.test_name + "-" + vol_type + " ***");

			/* Invoke the test method */
			test_res = test_instance.init(test_wrapper.test_type, test_wrapper.component, test_wrapper.test_name,
					vol_type);

			if (test_res == Globals.SUCCESS) {
				/* Invoke the test method */
				test_res = test_instance.abstract_execute_test();
			}

			/* Invoke the terminate method */
			int terminate_res = test_instance.terminate();

			/*
			 * Ensure that a success for the terminate phase doesn't makr a failed test as
			 * successful
			 */
			if (terminate_res == Globals.FAILURE) {
				test_res = Globals.FAILURE;
			}

			if (test_res == Globals.SUCCESS) {
				/* Invoke the terminate method */
				execution_time = test_instance.get_execution_time();

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
				logger.handle_failure(e);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}
}
