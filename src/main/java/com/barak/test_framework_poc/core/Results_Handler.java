package com.barak.test_framework_poc.core;

import com.barak.test_framework_poc.common.Globals;
import com.barak.test_framework_poc.common.Logger;
import com.barak.test_framework_poc.test.Test_Wrapper;
import com.barak.test_framework_poc.test.Test_Wrapper.Test_Res;

public abstract class Results_Handler {
	// TODO: make output length dynamic based on vol name
	private static String format_str = String.format("|%-14s|%-10s|%-20s|%s", "Volume", "Result", "Execution Time", "\n");

	public static void display_results(Logger logger) throws Exception {
		logger.new_line();
		logger.log_and_print("Excution results:");

		for (Test_Wrapper test_wrapper : Test_Runner.tests_to_run) {
			StringBuilder results_string = new StringBuilder();
			results_string.append("Test " + test_wrapper.test_type + "/" + test_wrapper.component + "/"
					+ test_wrapper.test_name + ":\n");
			results_string.append(format_str);

			for (Test_Res test_res : test_wrapper.res_list) {
				results_string.append(String.format("|%-14s|", test_res.vol_type));
				if (test_res.res == Globals.SUCCESS) {
					results_string.append(String.format("%-10s", "PASS"));
					results_string.append(String.format("|%-20s|%-3s%s",
							test_res.execution_time / 1000 + "." + test_res.execution_time % 1000 + " seconds", "",
							"\n"));
				} else {
					results_string.append(String.format("%-10s", "FAIL"));
					results_string.append(String.format("|%-20s|%-3s%s", "N/A", "", "\n"));
				}
			}
			logger.log_and_print_no_timestamp(results_string.toString());
		}
	}
}
