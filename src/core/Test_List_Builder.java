package core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import common.Framework_Exception;
import common.Globals;

public class Test_List_Builder {
	// TODO: Parse path from config file
	private static final String PACKAGE_NAME = "tests.functional.dht"; // Package name of tests
	private static final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

	public static LinkedList<Object> create_test_list(String test_dir_path) throws Exception {
		LinkedList<String> dirs_to_scan = new LinkedList<String>();
		dirs_to_scan.add(test_dir_path);

		File classes_dir = new File(Globals.BIN_PATH);

		LinkedList<Object> tests_to_run = new LinkedList<Object>();

		while (!dirs_to_scan.isEmpty()) {
			File tests_dir = new File(dirs_to_scan.remove());
			String tests_dir_path = tests_dir.getAbsolutePath();

			File[] entries = tests_dir.listFiles();

			for (File entry : entries) {
				String test_name_java = entry.getName();

				if (entry.isFile() && test_name_java.startsWith(Globals.TEST_PREFIX)
						&& test_name_java.endsWith(".java")) {
					/*
					 * This is a test - Compile, instantiate, create methods map and it add to the
					 * test list
					 */
					String test_path = new String(tests_dir_path + "/" + test_name_java);

					/* Get key values */
					String helper = tests_dir_path.substring(0, tests_dir_path.lastIndexOf("/"));
					String test_type = helper.substring(helper.lastIndexOf("/") + 1, helper.length());
					String component = tests_dir_path.substring(tests_dir_path.lastIndexOf("/") + 1,
							tests_dir_path.length());
					String test_name = test_name_java.substring(0, test_name_java.indexOf("."));

					/* Compile the test */
					compile_test(test_path);

					/* Load the test class */
					Class<?> test_class = load_test_class(classes_dir, test_name_java);

					/* Instantiate the test */
					Object test_instance = instantiate_test(test_class, test_type, component, test_name);

					tests_to_run.add(test_instance);
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
	private static Class<?> load_test_class(File classes_dir, String test_name) throws Exception {
		Class<?> test_class = null;

		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { classes_dir.toURI().toURL() });
		test_class = Class.forName(PACKAGE_NAME + "." + test_name.substring(0, test_name.indexOf(".")), true,
				classLoader);

		return test_class;
	}

	/* Instantiate a test */
	private static Object instantiate_test(Class<?> test_class, String test_type, String component, String test_name)
			throws Exception {
		Object test_instance = null;
		test_instance = test_class.getDeclaredConstructor(String.class, String.class, String.class)
				.newInstance(test_type, component, test_name);

		return test_instance;
	}

	/* Compile a test */
	private static void compile_test(String test_path) throws Exception {
		OutputStream os = new ByteArrayOutputStream();

		int res = compiler.run(null, null, os, "-d", Globals.BIN_PATH, test_path);

		if (res != Globals.SUCCESS) {
			throw new Framework_Exception("Compilation of test " + test_path + " failed:\n" + os);
		}
	}
}
