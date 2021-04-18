package common;

import core.parsing.XML_Parser;
import ssh.Remote_Executioner.Op_Res;
import common.distributed_executioner.Distributed_Executioner;

public abstract class Abstract_Ops {
	protected Distributed_Executioner distributed_executioner;

	public Abstract_Ops(Distributed_Executioner distributed_executioner_arg) {
		this.distributed_executioner = distributed_executioner_arg;
	}

	public void execute_abstract_server_op(String cmd, String host) throws Exception {
		Op_Res op_res = distributed_executioner.execute_cmd_on_single_server(cmd, host);

		if (op_res.res != Globals.SUCCESS) {
			throw new Framework_Exception(op_res.msg);
		}

		Logger.log_cmd_output(op_res.msg);
	}

	public void execute_abstract_client_op(String cmd, String host) throws Exception {
		Op_Res op_res = distributed_executioner.execute_cmd_on_single_client(cmd, host);

		if (op_res.res != Globals.SUCCESS) {
			throw new Framework_Exception(op_res.msg);
		}

		Logger.log_cmd_output(op_res.msg);
	}

	public void execute_abstract_server_op_xml(String cmd, String host, String xml_tag) throws Exception {
		Op_Res op_res = distributed_executioner.execute_cmd_on_single_server(cmd, host);

		if (op_res.res != Globals.SUCCESS) {
			throw new Framework_Exception(op_res.msg);
		}

		XML_Parser.parse_xml_response(op_res.msg, xml_tag);
	}
}