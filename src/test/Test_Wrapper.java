package test;

import java.util.LinkedList;

public class Test_Wrapper {
	public Class<?> test_class;
	public String test_type;
	public String component;
	public String test_name;
	public LinkedList<Test_Res> res_list;

	public Test_Wrapper(Class<?> test_class_arg, String test_type_arg, String component_arg, String test_name_arg) {
		this.test_class = test_class_arg;
		this.test_type = test_type_arg;
		this.component = component_arg;
		this.test_name = test_name_arg;
		res_list = new LinkedList<Test_Res>();
	}

	public static class Test_Res {
		public int res;
		public String vol_type;
		public long execution_time;

		public Test_Res(int res_arg, String vol_type_arg, long execution_time_arg) {
			this.res = res_arg;
			this.vol_type = vol_type_arg;
			this.execution_time = execution_time_arg;
		}
	}
}
