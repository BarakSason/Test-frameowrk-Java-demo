package core;

import java.util.LinkedList;

public class Test_Runner {
	public static LinkedList<Object> tests_to_run;

	public static void run_tests() {

		for (Object test : tests_to_run) {
			Runner_Thread rt = new Runner_Thread(test);
			rt.start();
			try {
				rt.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
