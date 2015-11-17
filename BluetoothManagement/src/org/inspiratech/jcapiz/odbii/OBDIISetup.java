package org.inspiratech.jcapiz.odbii;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.commands.temperature.AmbientAirTemperatureCommand;
import com.github.pires.obd.enums.ObdProtocols;

public class OBDIISetup extends AsyncTask<String, String, String> {

	private static final String MY_UUID = "4dd1cec3-6b0c-4036-a805-49b21ef84f9f";
	private static final String TAG = "TUL";
	private final BluetoothSocket socket;
	private final BluetoothDevice device;

	public OBDIISetup(BluetoothDevice device) {
		this.device = device;
		BluetoothSocket temp = null;
		try {
			temp = this.device.createRfcommSocketToServiceRecord(UUID
					.fromString(MY_UUID));
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket = temp;
	}

	@Override
	protected String doInBackground(String... args) {
		String result = null;
		while (!Thread.currentThread().isInterrupted()) {
			try {
				new EchoOffCommand().run(socket.getInputStream(),
						socket.getOutputStream());
				new LineFeedOffCommand().run(socket.getInputStream(),
						socket.getOutputStream());
				new TimeoutCommand(125).run(socket.getInputStream(),
						socket.getOutputStream());
				new SelectProtocolCommand(ObdProtocols.AUTO).run(
						socket.getInputStream(), socket.getOutputStream());
				new AmbientAirTemperatureCommand().run(socket.getInputStream(),
						socket.getOutputStream());
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		stopActions();
		return result;
	}

	@Override
	protected void onPostExecute(String params) {
	}

	public void stopActions() {
		Thread.currentThread().interrupt();
	}
}
