package com.redant.tests;

import java.lang.reflect.Method;
import java.util.HashMap;

public class TestWrapper {
	public Object test_instance;
	public HashMap<String, Method> methods_map;
	
	public TestWrapper(Object test_instance_arg, HashMap<String, Method> methods_map_arg) {
		 this.test_instance = test_instance_arg;
		 this.methods_map = methods_map_arg;
	}
}
