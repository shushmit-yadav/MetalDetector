package com.shushmit.metaldetector;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity<MetalDetectorActivity> extends Activity implements SensorEventListener {

	public TextView xtvResult;
	public TextView ytvResult;
	public TextView ztvResult;
	int sensorValue=0;
	
	private ProgressBar mProgress;

	private SensorManager sensorManager = null;
	private float[] geomag = new float[3];
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up a SensorManager
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		setContentView(R.layout.main);

		// Text view setup
		xtvResult = (TextView) findViewById(R.id.xtvResult);
		ytvResult = (TextView) findViewById(R.id.ytvResult);
		ztvResult = (TextView) findViewById(R.id.ztvResult);

		// Initialise values.
		xtvResult.setText("X: 0.00");
		ytvResult.setText("Y: 0.00");
		ztvResult.setText("Z: 0.00");
		
		// Setup progress bar value.
		mProgress = (ProgressBar) findViewById(R.id.progress_bar);
		mProgress.setMax(100);
	}

	public void onSensorChanged(SensorEvent sensorEvent) {
		synchronized (this) {
			geomag = sensorEvent.values.clone();

			if (geomag != null) {

				xtvResult.setText("X: "+Float.toString(geomag[0]));
				ytvResult.setText("Y: "+Float.toString(geomag[1]));
				ztvResult.setText("Z: "+Float.toString(geomag[2]));    
				
				mProgress.setProgress(getProgressBarValue(geomag[0]));
				
				//Metal detected.
				if( Math.abs(geomag[0]) > sensorValue){
					playAlarm();
					Toast.makeText(this, "Metal detected", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
		

	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// Register this class as a listener for the sensors.
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		// Unregister the listener
		sensorManager.unregisterListener(this);
	}
	
	/**
	 * Method to play an alarm sound to signal half time or full time.
	 */
	private void playAlarm() {
		MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.buzzer);
		
		if(!mp.isPlaying()){ //If mediaplayer is not already playing.
			
			mp.start();
			mp.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
				}

			});
		}

	}
	
	/**
	 * Takes in a sensor value and returns a value to set the
	 * progress bar to depending on the sensor value.
	 * @param sensorValue
	 * @return
	 */
	public int getProgressBarValue(float sensorValue){
		sensorValue = Math.abs(sensorValue);
		return (int) sensorValue;
		
//		if(sensorValue >= 10 &&  sensorValue < 30)
//			return 15;
//		else if(sensorValue >= 30 &&  sensorValue < 50)
//			return 30;
//		else if(sensorValue >= 30 &&  sensorValue < 50)
//			return 30;
//		else if(sensorValue >= 50 &&  sensorValue < 70)
//			return 50;
//		else if(sensorValue >= 70 &&  sensorValue < 100)
//			return 75;
//		else if(sensorValue >= 100)
//			return 99;
//		else 
//			return 0;
	}

}