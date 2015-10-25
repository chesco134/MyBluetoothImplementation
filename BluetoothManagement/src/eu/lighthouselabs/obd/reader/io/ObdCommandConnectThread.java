package eu.lighthouselabs.obd.reader.io;

import org.capiz.bluetooth.CustomBluetoothActivity;

import android.bluetooth.BluetoothDevice;
import eu.lighthouselabs.obd.reader.command.ObdCommand;
import eu.lighthouselabs.obd.reader.config.ObdConfig;

public class ObdCommandConnectThread extends ObdConnectThread {

	private ObdCommand cmd = null;
	private CustomBluetoothActivity activity = null;

	public ObdCommandConnectThread(BluetoothDevice dev, CustomBluetoothActivity activity, ObdCommand cmd, double ed, double ve, boolean imperialUnits) {
		super(dev, null, null, null, 0, ed, ve, imperialUnits, false, ObdConfig.getAllCommands());
		this.cmd = cmd;
		this.activity = activity;
	}

	public void run() {
		try {
			activity.logMessage("Starting device...");
			startDevice();
			activity.logMessage("Device started, running " + cmd.getCmd() + "...");
			cmd.setConnectThread(this);
			String res = runCommand(cmd);
			String rawRes = cmd.getResult().replace("\r","\\r").replace("\n","\\n");
			activity.logMessage("Raw result is '" + rawRes + "'");
			results.put(cmd.getDesc(), res);
			if (cmd.getError() != null) {
				activity.logMessage(cmd.getError().getMessage());
				activity.logMessage(getStackTrace(cmd.getError()));
			}
		} catch (Exception e) {
			activity.logMessage("Error running command: " + e.getMessage() + ", result was: '" + cmd.getResult() + "'");
			activity.logMessage("Error was: " + getStackTrace(e));
		} finally {
			close();
		}
	}
}
