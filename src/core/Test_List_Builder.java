package core;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.LinkedList;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import tests.TestWrapper;

public class Test_List_Builder {
	//TODO: Parse path from config file
	private static final String BIN_PATH = "/root/eclipse-workspace-java/Test_Framework/bin"; // Path to place compiled
																								// test classes
	private static final String PACKAGE_NAME = "tests.functional.dht"; // Package name of tests

	public static LinkedList<TestWrapper> create_test_list(LinkedList<String> dirs_to_scan) {
		File classes_dir = new File(BIN_PATH);

		LinkedList<TestWrapper> tests_to_run = new LinkedList<TestWrapper>();

		while (!dirs_to_scan.isEmpty()) {
			try {
				File tests_dir = new File(dirs_to_scan.remove());
				String tests_dir_path = tests_dir.getAbsolutePath();

				File[] entries = tests_dir.listFiles();

				for (File entry : entries) {
					String test_name_java = entry.getName();

					if (entry.isFile() && test_name_java.startsWith("Test_") && test_name_java.endsWith(".java")) {
						/*
						 * This is a test - Compile, instantiate, create methods map and it add to the
						 * test list
						 */
						String test_path = new String(tests_dir_path + "/" + test_name_java);

						/* Get key paths */
						String component = tests_dir_path.substring(tests_dir_path.lastIndexOf("/") + 1,
								tests_dir_path.length());
						String test_name = test_name_java.substring(0, test_name_java.indexOf("."));

						/* Compile the test */
						compile_test(test_path);

						/* Load the test class */
						Class<?> test_class = load_test_class(classes_dir, test_name_java);

						/* Instantiate the test */
						Object test_instance = instantiate_test(test_class, component, test_name);

						/* Create a map of test methods */
						HashMap<String, Method> methods_map = create_methods_map(test_class);

						/* Add test info to list */
						TestWrapper cur_test = new TestWrapper(test_instance, methods_map);
						tests_to_run.add(cur_test);
					} else {
						/* This is a dir - Add the path of the next dir to process */
						if (entry.isDirectory()) {
							dirs_to_scan.add(entry.getAbsolutePath());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
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
	private static Object instantiate_test(Class<?> test_class, String component, String test_name) throws Exception {
		Object test_instance = null;
		test_instance = test_class.getDeclaredConstructor(String.class, String.class).newInstance(component, test_name);

		return test_instance;
	}

	/* Create a map of test methods */
	private static HashMap<String, Method> create_methods_map(Class<?> cls) {
		Method[] methods = cls.getMethods();

		HashMap<String, Method> methods_map = new HashMap<String, Method>();

		for (Method m : methods) {
			methods_map.put(m.getName(), m);
		}

		return methods_map;
	}

	/* Compile a test */
	private static void compile_test(String test_path) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		compiler.run(null, null, null, "-d", BIN_PATH, test_path);

		System.out.println("Compiled test" + test_path);
	}
}
