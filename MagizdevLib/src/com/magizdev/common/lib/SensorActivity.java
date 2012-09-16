package com.magizdev.common.lib;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class SensorActivity extends Activity implements SensorEventListener {

	private SensorManager sensorMgr;
	private Sensor sensor;
	private long lastUpdate = -1;
	private float x, y, z;
	private float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 300;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	@Override
	public void finish(){
		if (sensorMgr != null) {
			sensorMgr.unregisterListener(this);
			sensorMgr = null;
		}
		super.finish();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			long curTime = System.currentTimeMillis();
			if ((curTime - lastUpdate) > 100) {
				long diffTime = (curTime - lastUpdate);
				lastUpdate = curTime;

				x = event.values[0];
				y = event.values[1];
				z = event.values[2];

				float speed = Math.abs(x + y + z - last_x - last_y - last_z)
						/ diffTime * 1000;
				if (speed > SHAKE_THRESHOLD) {
					onShake();
				}
				last_x = x;
				last_y = y;
				last_z = z;
			}
		}
	}
	
	public void onShake(){
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
}
