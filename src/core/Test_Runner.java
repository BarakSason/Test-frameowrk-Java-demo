package core;

import java.util.LinkedList;

import common.Logger;
import test.Test_Wrapper;

public class Test_Runner {
	public static LinkedList<Test_Wrapper> tests_to_run;

	public static void run_tests(Logger logger) {

		for (Test_Wrapper test_wrapper : tests_to_run) {
			Runner_Thread rt = new Runner_Thread(logger, test_wrapper);
			rt.start();
			try {
				rt.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
