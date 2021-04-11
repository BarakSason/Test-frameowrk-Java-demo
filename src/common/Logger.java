package common;

public class Logger {
	private static final boolean is_prints_enabled = false;

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
				System.out.println(((Framework_Exception) e).err_msg);
			} else {
				e.printStackTrace();
			}
		}
	}

	public static void handle_expected_exception(Framework_Exception e) {
		// Info level
		if (is_prints_enabled) {
			String err_msg = e.err_msg;
			err_msg = err_msg.substring(0, err_msg.length() - 1);
			System.out.println(err_msg + ", as expected");
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
