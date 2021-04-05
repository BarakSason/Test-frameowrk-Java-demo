package com.redant.common.ops;

import com.redant.common.Abstract_Ops;
import com.redant.common.distributed_executioner.Distributed_Executioner;

public class Peer_Ops extends Abstract_Ops {

	public Peer_Ops(Distributed_Executioner distributed_executioner_arg) {
		super(distributed_executioner_arg);
	}

	public void peer_probe(String host, String peer) throws Exception {
		String cmd = "gluster peer probe " + peer;

		execute_abstract_op(cmd, host);
	}

	public void peer_detach(String host, String peer) throws Exception {
		String cmd = "gluster peer detach " + peer + " --mode=script";

		execute_abstract_op(cmd, host);
	}
}
