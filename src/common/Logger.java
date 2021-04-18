package common;

public class Logger {
	private static final boolean is_prints_enabled = true;

	public static void pre_op_log(String cmd, String host) {
		// Info level
		if (is_prints_enabled) {
			System.out.println("Executing command \"" + cmd + "\" on host \"" + host + "\"");
		}
	}

	public static void log_success(String cmd, String host) {
		// Info level
		if (is_prints_enabled) {
			System.out
					.println("Execution of command \"" + cmd + "\" on host \"" + host + "\" completed successfully\n");
		}
	}

	public static void log_failure(Exception e) {
		// Error level
		if (is_prints_enabled) {
			if (e instanceof Framework_Exception) {
				System.out.print(((Framework_Exception) e).err_msg);
				StackTraceElement[] ste = e.getStackTrace();
				for (int i = 0; i < 4; ++i) { // 4 relevant stack frames
					System.out.println(ste[i]);
				}
			} else {
				e.printStackTrace();
			}
		}
	}

	public static void handle_expected_exception(Exception e) throws Exception {
		// Info level
		if (is_prints_enabled) {
			if (e instanceof Framework_Exception) {
				String err_msg = ((Framework_Exception) e).err_msg;
				err_msg = err_msg.substring(0, err_msg.length() - 1);
				System.out.println(err_msg + ", as expected");
			} else {
				throw e;
			}
		}
	}

	public static String construct_failure_string(String cmd, String host, String err_msg) {
		return new String("Execution of command \"" + cmd + "\" on host \"" + host + "\" failed due to:\n" + err_msg);
	}

	public static void log_cmd_output(String cmd_output) {
		if (is_prints_enabled) {
			if (!cmd_output.isEmpty()) {
				System.out.println(cmd_output);
			}
		}
	}

	public static void print(String msg) {
		if (is_prints_enabled) {
			System.out.println(msg);
		}
	}
}
