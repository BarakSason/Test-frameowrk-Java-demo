package com.redant.common;

public class Ops_Exception extends Exception {
	public String err_msg;

	public Ops_Exception(String msg_arg) {
		this.err_msg = msg_arg;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4968383600219832289L;

}
