package org.inspiratech.jcapiz.odbii;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;
import eu.lighthouselabs.obd.reader.command.EngineRPMObdCommand;
import eu.lighthouselabs.obd.reader.command.SpeedObdCommand;

public class OBDIISetup extends AsyncTask<String,String,String>{

	private static final String MY_UUID = "4dd1cec3-6b0c-4036-a805-49b21ef84f9f";
	private static final String TAG = "TUL";
    private final BluetoothSocket socket;
    private final BluetoothDevice device;

    public OBDIISetup(BluetoothDevice device){
        this.device = device;
        BluetoothSocket temp = null;
        try{
            temp = this.device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
        }catch(IOException e){
            e.printStackTrace();
        }
        socket = temp;
    }

    @Override
    protected String doInBackground(String... args){
        String result = null;
		EngineRPMObdCommand engineRpmCommand = new EngineRPMObdCommand();
		SpeedObdCommand speedCommand = new SpeedObdCommand();
		while (!Thread.currentThread().isInterrupted())
		{
		    engineRpmCommand.run();
		    speedCommand.run();
		    // TODO handle commands result
		    Log.d(TAG, "RPM: " + engineRpmCommand.getResult());
		    Log.d(TAG, "Speed: " + speedCommand.getResult());
		}
		stopActions();
        return result;
    }

    @Override
    protected void onPostExecute(String params){
    }

    public void stopActions(){
    	Thread.currentThread().interrupt();
    }
}
