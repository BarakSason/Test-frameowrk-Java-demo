package common;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import core.Params_Handler;

public class Logger {
	private static final boolean is_prints_enabled = true;
	private File log_file;
	private FileWriter writer;
	private static String logs_path = Params_Handler.read_value("logs_path") + "/logs_"
			+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

	public Logger(String test_type, String component, String test_name) throws Exception {
		init(logs_path + "/" + test_type + "/" + component + "/" + test_name + ".log");
	}

	/* To be used only by the framework itself */
	public Logger() throws Exception {
		init(logs_path + "/main.log");
	}

	private void init(String path) throws Exception {
		log_file = new File(path);
		log_file.getParentFile().mkdirs();
		writer = new FileWriter(log_file);
	}

	public void pre_op_log(String cmd, String host) throws Exception {
		if (is_prints_enabled) {
			System.out.println(
					generate_log_msg_prefix("I") + "Executing command \"" + cmd + "\" on host \"" + host + "\"");
		}
		writer.write(generate_log_msg_prefix("I") + "Executing command \"" + cmd + "\" on host \"" + host + "\"\n");
		writer.flush();
	}

	public void log_success(String cmd, String host) throws Exception {
		if (is_prints_enabled) {
			System.out.println(generate_log_msg_prefix("I") + "Execution of command \"" + cmd + "\" on host \"" + host
					+ "\" completed successfully\n");
		}
		writer.write(generate_log_msg_prefix("I") + "Execution of command \"" + cmd + "\" on host \"" + host
				+ "\" completed successfully\n");
		writer.flush();
	}

	public void log_failure(Exception e) throws Exception {
		if (e instanceof Framework_Exception) {
			if (is_prints_enabled) {
				System.out.print(generate_log_msg_prefix("E") + ((Framework_Exception) e).err_msg);
			}
			writer.write(generate_log_msg_prefix("E") + ((Framework_Exception) e).err_msg);
			writer.flush();
			StackTraceElement[] ste = e.getStackTrace();
			for (int i = 0; i < 4; ++i) { // Due to the hierarchy of the TC, we will always have 4 relevant stack frames
				if (is_prints_enabled) {
					System.out.println(generate_log_msg_prefix("E") + ste[i].toString());
				}
				writer.write(generate_log_msg_prefix("E") + ste[i].toString() + "\n");
				writer.flush();
			}
		} else {
			e.printStackTrace();
		}
	}

	public void handle_expected_exception(Exception e) throws Exception {
		if (e instanceof Framework_Exception) {
			String err_msg = ((Framework_Exception) e).err_msg;
			err_msg = err_msg.substring(0, err_msg.length() - 1);
			if (is_prints_enabled) {
				System.out.println(generate_log_msg_prefix("I") + err_msg + ", as expected");
			}
			writer.write(generate_log_msg_prefix("I") + err_msg + ", as expected\n");
			writer.flush();
		} else {
			throw e;
		}
	}

	public String construct_failure_string(String cmd, String host, String err_msg) {
		return new String("Execution of command \"" + cmd + "\" on host \"" + host + "\" failed due to:\n" + err_msg);
	}

	public void log_cmd_output(String cmd_output) throws Exception {
		if (!cmd_output.isEmpty()) {
			if (is_prints_enabled) {
				System.out.println(generate_log_msg_prefix("I") + cmd_output);
			}

			writer.write(generate_log_msg_prefix("I") + cmd_output);
			writer.flush();
		}
	}

	public void log(String msg) throws Exception {
		if (is_prints_enabled) {
			System.out.println(generate_log_msg_prefix("I") + msg);
		}
		writer.write(generate_log_msg_prefix("I") + msg + "\n");
		writer.flush();
	}

	private String generate_log_msg_prefix(String log_level) {
		return (new SimpleDateFormat("[yyyy-MM-dd-HH-mm-ss]").format(new Date()) + " [" + log_level + "] ");
	}
}
