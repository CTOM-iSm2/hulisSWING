package sqlbridge;

import java.net.URL;

import util.io.SwingIO;

public class RegisterDuration extends Thread {

	private long userId;
	private String lastCommand;

	public RegisterDuration(long userId) {
		this.userId = userId;
		this.lastCommand = "";
	}

	public RegisterDuration(long userId, String lastCommand) {
		this.userId = userId;
		this.lastCommand = lastCommand;
	}

	
	public void run() {
		try {
			String sLastCommand = "";
			if (this.lastCommand !="")
				sLastCommand = "&lastCommand=" + this.lastCommand;
			new URL(ConnectSQL.DURATION + "?id_site_user=" + this.userId + sLastCommand
					)
					.openStream();
		} catch (Exception e1) {
			SwingIO.warning(this.getClass().getName(), "run",
					"cannot register duration", e1);

		}

		
	}
}
