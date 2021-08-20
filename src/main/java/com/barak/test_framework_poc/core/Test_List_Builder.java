package com.barak.test_framework_poc.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.barak.test_framework_poc.common.Framework_Exception;
import com.barak.test_framework_poc.common.Globals;
import com.barak.test_framework_poc.test.Test_Wrapper;

public abstract class Test_List_Builder {
	// TODO: Parse path from config file
	private static final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

	public static LinkedList<Test_Wrapper> create_test_list(String test_dir_path) throws Exception {
		/* Add the path of of the tests as provided by the user */
		LinkedList<String> dirs_to_scan = new LinkedList<String>();
		dirs_to_scan.add(test_dir_path);

		/* Create tests bin dir */
		File classes_dir = new File(Params_Handler.read_value("bin_path"));

		LinkedList<Test_Wrapper> tests_to_run = new LinkedList<Test_Wrapper>();

		while (!dirs_to_scan.isEmpty()) {
			File tests_dir = new File(dirs_to_scan.remove());
			String tests_dir_path = tests_dir.getAbsolutePath();

			File[] entries = tests_dir.listFiles();

			for (File entry : entries) {
				String test_name_java = entry.getName();

				if (entry.isFile() && test_name_java.startsWith(Globals.TEST_PREFIX)
						&& test_name_java.endsWith(".java")) {
					/* This is a test - Get key info, compile it and load the test class */
					String test_path = new String(tests_dir_path + "/" + test_name_java);

					/* Get key values */
					String tmp = tests_dir_path.substring(0, tests_dir_path.lastIndexOf("/"));
					String test_type = tmp.substring(tmp.lastIndexOf("/") + 1, tmp.length());
					String component = tests_dir_path.substring(tests_dir_path.lastIndexOf("/") + 1,
							tests_dir_path.length());
					String test_name = test_name_java.substring(0, test_name_java.indexOf("."));

					/* Compile the test */
					compile_test(test_path);

					/* Load the test class */
					Class<?> test_class = load_test_class(classes_dir, test_type, component, test_name);

					tests_to_run.add(new Test_Wrapper(test_class, test_type, component, test_name));
				} else {
					/* This is a dir - Add the path of the next dir to process */
					if (entry.isDirectory()) {
						dirs_to_scan.add(entry.getAbsolutePath());
					}
				}
			}
		}

		return tests_to_run;
	}

	/* Load a test class */
	private static Class<?> load_test_class(File classes_dir, String test_type, String component, String test_name)
			throws Exception {
		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { classes_dir.toURI().toURL() });
		return Class.forName("tests." + test_type + "." + component + "." + test_name, true, classLoader);
	}

	/* Compile a test */
	private static void compile_test(String test_path) throws Exception {
		OutputStream os = new ByteArrayOutputStream();

		int res = compiler.run(null, null, os, "-d", Params_Handler.read_value("bin_path"), test_path);

		if (res != Globals.SUCCESS) {
			throw new Framework_Exception("Compilation of test " + test_path + " failed:\n" + os);
		}
	}
}
