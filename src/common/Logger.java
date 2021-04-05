package common;

public class Logger {
	public static void pre_op_log(String cmd, String host) {
		// Info level
		System.out.println("Executing command \"" + cmd + "\" on host \"" + host + "\"");
	}

	public static void post_op_log_success(String cmd, String host) {
		// Info level
		System.out.println("Execution of command \"" + cmd + "\" on host \"" + host + "\" completed successfully\n");
	}

	public static void post_op_log_failure(Exception e) {
		// Error level
		if (e instanceof Ops_Exception) {
			System.out.println(((Ops_Exception) e).err_msg);
		} else {
			e.printStackTrace();
		}
	}

	public static void handle_expected_exception(Ops_Exception e) {
		// Info level
		String err_msg = e.err_msg;
		err_msg = err_msg.substring(0, err_msg.length() - 1);
		System.out.println(err_msg + ", as expected");
	}

	public static String construct_failure_string(String cmd, String host, String err_msg) {
		return new String("Execution of command \"" + cmd + "\" on host \"" + host + "\" failed due to:\n" + err_msg);
	}

	public static void log_cmd_output(String cmd_output) {
		if (!cmd_output.isEmpty()) {
			System.out.println(cmd_output);
		}
	}
}
