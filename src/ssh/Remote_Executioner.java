package ssh;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

import common.Globals;
import common.Logger;

public class Remote_Executioner {
	private Logger logger;

	public class Op_Res {
		public int res = Globals.SUCCESS;
		public String msg;
	}

	public Remote_Executioner(Logger logger_arg) {
		this.logger = logger_arg;
	}

	protected Op_Res execute_remote_cmd(String cmd, Session session) throws Exception {
		Op_Res op_res = new Op_Res();
		String host = session.getHost();
		Channel channel = null;

		try {
			channel = session.openChannel("exec");

			((ChannelExec) channel).setCommand(cmd);

			InputStream in = channel.getInputStream();
			InputStream err = channel.getExtInputStream();

			channel.connect(); // Command execution

			op_res.msg = cmd_callback(channel, in);
			if (channel.getExitStatus() == 0) {
				logger.log_success(cmd, host);
			} else {
				String err_msg = cmd_callback(channel, err);
				op_res.res = Globals.FAILURE;
				op_res.msg = logger.construct_failure_string(cmd, host, err_msg);
			}
		} catch (Exception e) {
		} finally {
			if (channel != null && channel.isConnected()) {
				channel.disconnect();
			}
		}

		return op_res;
	}

	private String cmd_callback(Channel channel, InputStream stream) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();

		while (true) {
			while (reader.ready()) {
				sb.append(reader.readLine() + "\n");
			}

			if (channel.isClosed()) {
				break;
			}
		}

		return sb.toString();
	}
}
