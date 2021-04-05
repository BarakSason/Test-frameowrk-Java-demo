package com.redant.core;

import java.util.ArrayList;
import java.util.HashMap;

import com.redant.core.parsing.Config_Parser;

public class Params_Handler {
	private static HashMap<String, ArrayList<String>> param_map; // Static hashmap for all tests and
	// other components to read from

	public static void parseConfigFile(String config_file_path) throws Exception {
		param_map = Config_Parser.parse_config_file(config_file_path);
	}

	/* A set of APIs to interact with the param hashmap */
	public static String read_server(int server_num) {
		ArrayList<String> servers = param_map.get("servers");
		return servers.get(server_num - 1);
	}

	/* A set of APIs to interact with the param hashmap */
	public static int get_servers_num() {
		ArrayList<String> servers = param_map.get("servers");
		return servers.size();
	}

	//TODO: Handle brick root(s) per server
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static ArrayList<String> get_servers_info() {
//		ArrayList<String> brick_paths = new ArrayList<String>();
//
//		Object severs_ino_obj = param_map.get("servers_info");
//		LinkedHashMap severs_info_map = (LinkedHashMap<String, String>) severs_ino_obj;
//		Set<String> hosts = severs_info_map.keySet();
//
//		for (String host : hosts) {
//			LinkedHashMap server_info = (LinkedHashMap<String, String>) severs_info_map.get(host);
//			ArrayList<String> brick_roots = (ArrayList<String>) server_info.get("brick_root");
//			brick_paths.add(brick_roots.get(0));
//		}
//
//		return brick_paths;
//	}
}
