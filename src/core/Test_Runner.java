package core;

import java.util.LinkedList;

import tests.TestWrapper;

public class Test_Runner {
	public static LinkedList<TestWrapper> tests_to_run;

	public static void run_tests() {

		for (TestWrapper tw : tests_to_run) {
			Runner_Thread rt = new Runner_Thread(tw);
			rt.start();
			try {
				rt.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
