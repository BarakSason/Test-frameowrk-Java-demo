package test;

public class Test_Wrapper {
	public Class<?> test_class;
	public String test_type;
	public String component;
	public String test_name;

	public Test_Wrapper(Class<?> test_class_arg, String test_type_arg, String component_arg, String test_name_arg) {
		this.test_class = test_class_arg;
		this.test_type = test_type_arg;
		this.component = component_arg;
		this.test_name = test_name_arg;
	}
}
