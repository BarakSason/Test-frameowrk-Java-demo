package core;

import java.util.LinkedList;

import common.Logger;

public class Test_Runner {
	public static LinkedList<Object> tests_to_run;

	public static void run_tests(Logger logger) {

		for (Object test : tests_to_run) {
			Runner_Thread rt = new Runner_Thread(logger, test);
			rt.start();
			try {
				rt.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
