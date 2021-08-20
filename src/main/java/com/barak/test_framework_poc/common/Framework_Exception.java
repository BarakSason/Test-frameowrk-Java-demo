package com.barak.test_framework_poc.common;

public class Framework_Exception extends Exception {
	public String err_msg;

	public Framework_Exception(String msg_arg) {
		this.err_msg = msg_arg;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4968383600219832289L;

}
