package sqlbridge;

import java.net.URL;

import util.io.SwingIO;
import views.ErrorReport;

public class RegisterDisconnectApplet extends Thread {

	private long userId;

	public RegisterDisconnectApplet(long userId) {
		this.userId = userId;
	}

	public void run() {
		try {
			new URL(ConnectSQL.DECONNECT_URL + "?id_site_user=" + this.userId
					+"&error=" + (ErrorReport.isErrorReported() ? 1 : 0))
					.openStream();
		
			} catch (Exception e1) {
				System.out.println("OK");
			SwingIO.warning(this.getClass().getName(), "run",
					"cannot unregister user", e1);

		}

	
	}
}
