package com.barak.test_framework_poc.core.parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

public class Config_Parser {
	/* This class is responsible for parsing the config file */

	public static HashMap<String, Object> parse_config_file(String config_file_path) throws Exception {
		/* Parse config file located at "path" */
		Yaml yaml = new Yaml();
		InputStream inputStream = new FileInputStream(new File(config_file_path));
		return yaml.load(inputStream);
	}
}
