package common;

public class Logger {
	public static void pre_op_log(String cmd, String host) {
		System.out.println("Executing command \"" + cmd + "\" on host \"" + host + "\"");
	}
	
	public static void handle_expected_exception(Ops_Exception e) {
		String s = e.err_msg;
		s.stripTrailing();
		s = s.substring(0, s.length() - 1);
		/* INFO level log (NOT ERROR) */
		System.out.println(s + ", as expected");
	}
}
