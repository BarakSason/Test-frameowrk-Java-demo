package core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import common.Logger;
import test.Test_Wrapper;

public class Test_Runner {
	public static LinkedList<Test_Wrapper> tests_to_run;
	private static final boolean is_parallel = true;

	public static void run_tests(Logger logger) throws Exception {

		if (is_parallel) {
			ExecutorService threadpool = Executors.newFixedThreadPool(50); // TODO: Parse threads number from the config
																			// file

			for (Test_Wrapper test_wrapper : tests_to_run) {
				/* Instantiate the test */
				Object test_instance_obj = instantiate_test(test_wrapper.test_class);

				/* Create a map of test methods */
				HashMap<String, Method> methods_map = create_methods_map(test_wrapper.test_class);

				/* Read vol types for the test */
				HashMap<String, Field> fields_map = create_fields_map(test_wrapper.test_class);
				String vol_types_str = (String) fields_map.get("vol_types").get(test_instance_obj);
				String[] vol_types = vol_types_str.split(";");

				for (String vol_type : vol_types) {
					Object test_instance = instantiate_test(test_wrapper.test_class);

					threadpool.execute(new Test_Task(logger, test_wrapper, methods_map, test_instance, vol_type));
				}
			}

			threadpool.shutdown();
			threadpool.awaitTermination(1, TimeUnit.MINUTES); // TODO: Parse timeout from the config file
		} else {
			for (Test_Wrapper test_wrapper : tests_to_run) {
				/* Instantiate the test */
				Object test_instance_obj = instantiate_test(test_wrapper.test_class);

				/* Create a map of test methods */
				HashMap<String, Method> methods_map = create_methods_map(test_wrapper.test_class);

				/* Read vol types for the test */
				HashMap<String, Field> fields_map = create_fields_map(test_wrapper.test_class);
				String vol_types_str = (String) fields_map.get("vol_types").get(test_instance_obj);
				String[] vol_types = vol_types_str.split(";");

				for (String vol_type : vol_types) {
					Object test_instance = instantiate_test(test_wrapper.test_class);

					Thread t = new Thread(new Test_Task(logger, test_wrapper, methods_map, test_instance, vol_type));
					t.start();
					try {
						t.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/* Create a map of test methods */
	private static HashMap<String, Method> create_methods_map(Class<?> cls) {
		Method[] methods = cls.getMethods();

		HashMap<String, Method> methods_map = new HashMap<String, Method>();

		for (Method method : methods) {
			methods_map.put(method.getName(), method);
		}

		return methods_map;
	}

	/* Create a map of test fields */
	private static HashMap<String, Field> create_fields_map(Class<?> cls) {
		Field[] fields = cls.getDeclaredFields();

		HashMap<String, Field> fields_map = new HashMap<String, Field>();

		for (Field field : fields) {
			fields_map.put(field.getName(), field);
		}

		return fields_map;
	}

	/* Instantiate a test */
	private static Object instantiate_test(Class<?> test_class) throws Exception {
		Object test_instance = null;
		test_instance = test_class.getDeclaredConstructor().newInstance();

		return test_instance;
	}
}
