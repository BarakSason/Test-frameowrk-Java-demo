package com.barak.test_framework_poc.common;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.barak.test_framework_poc.core.Params_Handler;

public class Logger {
	private static final boolean is_prints_enabled = true;
	private File log_file;
	private FileWriter writer;
	private static String logs_path = Params_Handler.read_value("logs_path") + "/logs_"
			+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

	public Logger(String test_type, String component, String test_name, String vol_type) throws Exception {
		init(logs_path + "/" + test_type + "/" + component + "/" + test_name + "/" + test_name + "-" + vol_type
				+ ".log");
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

	public void handle_failure(Exception e) throws Exception {
		if (e instanceof Framework_Exception) {
			System.out.print(generate_log_msg_prefix("E") + ((Framework_Exception) e).err_msg);
			writer.write(generate_log_msg_prefix("E") + ((Framework_Exception) e).err_msg);
			writer.flush();
		} else {
			e.printStackTrace();
			System.out.print(generate_log_msg_prefix("E") + e.getMessage());
			writer.write(generate_log_msg_prefix("E") + e.getMessage());
			writer.flush();
		}

		StackTraceElement[] ste = e.getStackTrace();
		for (int i = 0; i < ste.length; ++i) {
			System.out.println(ste[i].toString());
			writer.write(ste[i].toString() + "\n");
			writer.flush();
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

	public void log_and_print(String msg) throws Exception {
		System.out.println(generate_log_msg_prefix("I") + msg);
		writer.write(generate_log_msg_prefix("I") + msg + "\n");
		writer.flush();
	}

	public void log_only(String msg) throws Exception {
		writer.write(generate_log_msg_prefix("I") + msg + "\n");
		writer.flush();
	}

	private String generate_log_msg_prefix(String log_level) {
		return (new SimpleDateFormat("[yyyy-MM-dd-HH-mm-ss]").format(new Date()) + " [" + log_level + "] ");
	}

	public void new_line() throws Exception {
		System.out.println();
		writer.write("\n");
		writer.flush();
	}

	public void log_and_print_no_timestamp(String msg) throws Exception {
		System.out.println(msg);
		writer.write(msg + "\n");
		writer.flush();
	}
}
