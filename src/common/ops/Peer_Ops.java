package common.ops;

import common.Abstract_Ops;
import common.Logger;
import common.distributed_executioner.Distributed_Executioner;

public class Peer_Ops extends Abstract_Ops {
	public Peer_Ops(Logger logger, Distributed_Executioner distributed_executioner) {
		super(logger, distributed_executioner);
	}

	public void peer_probe(String host, String peer) throws Exception {
		String cmd = "gluster peer probe " + peer;

		execute_abstract_server_op(cmd, host);
	}

	public void peer_detach(String host, String peer) throws Exception {
		String cmd = "gluster peer detach " + peer + " --mode=script";

		execute_abstract_server_op(cmd, host);
	}
}
