package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import core.parsing.Config_Parser;

public abstract class Params_Handler {
	private static HashMap<String, Object> param_map; // Static hashmap for all tests and other components to read from

	public static void parseConfigFile(String config_file_path) throws Exception {
		param_map = Config_Parser.parse_config_file(config_file_path);
	}

	/* A set of APIs to interact with the param hashmap */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> get_servers() {
		LinkedHashMap<String, ArrayList<String>> servers_info = (LinkedHashMap<String, ArrayList<String>>) param_map
				.get("servers");
		return new ArrayList<String>(servers_info.keySet());
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> get_clients() {
		LinkedHashMap<String, ArrayList<String>> cleints_info = (LinkedHashMap<String, ArrayList<String>>) param_map
				.get("clients");
		return new ArrayList<String>(cleints_info.keySet());
	}

	public static String read_value(String key) {
		return param_map.get(key).toString();
	}
}
