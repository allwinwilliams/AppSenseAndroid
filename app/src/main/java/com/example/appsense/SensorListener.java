package com.example.appsense;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SensorListener extends Activity implements SensorEventListener {
    private float sensorX;
    private float sensorY;
    private float sensorZ;
    private Display mDisplay;
    private SensorManager sm;
    private PowerManager mPowerManager;
    private WindowManager mWindowManager;
    private Sensor game_vector, accelerometer;
    private int scale;
    private String instrument_type;

//    private RadioGroup typeSelectionGroup;
//    private RadioButton padButton, stringButton, santoorButton, pianoButton, drumsButton;

    TextView gameVecViewX, gameVecViewY, gameVecViewZ, accelerometerViewX, accelerometerViewY, accelerometerViewZ, scaleValue;


//    public SensorListener() {
//        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
//        game_vector = sm.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
//        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get an instance of the SensorManager
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        game_vector = sm.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        // Get an instance of the PowerManager
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);

        // Get an instance of the WindowManager
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();


        setContentView(R.layout.sensor);
        gameVecViewX = (TextView)findViewById(R.id.gameVecsensorValueX);
        gameVecViewY = (TextView)findViewById(R.id.gameVecsensorValueY);
        gameVecViewZ = (TextView)findViewById(R.id.gameVecsensorValueZ);

        accelerometerViewX = (TextView)findViewById(R.id.accelerometerSensorValueX);
        accelerometerViewY = (TextView)findViewById(R.id.accelerometerSensorValueY);
        accelerometerViewZ = (TextView)findViewById(R.id.accelerometerSensorValueZ);

        scaleValue = (TextView)findViewById(R.id.scaleValue);

//        typeSelectionGroup = (RadioGroup)findViewById(R.id.typeSelectionGroup);
//
//        padButton = (RadioButton)findViewById(R.id.radioButtonPad);
//        stringButton = (RadioButton)findViewById(R.id.radioButtonStrings);
//        santoorButton = (RadioButton)findViewById(R.id.radioButtonSantoor);
//        pianoButton = (RadioButton)findViewById(R.id.radioButtonPiano);
//        drumsButton = (RadioButton)findViewById(R.id.radioButtonDrums);

//        padButton.setChecked(true);
        instrument_type = "pad";

//        typeSelectionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId){
//                    case R.id.radioButtonPad:
//                        instrument_type = "pad";
//                        break;
//                    case R.id.radioButtonStrings:
//                        instrument_type = "strings";
//                        break;
//                    case R.id.radioButtonSantoor:
//                        instrument_type = "santoor";
//                        break;
//                    case R.id.radioButtonPiano:
//                        instrument_type = "piano";
//                        break;
//                    case R.id.radioButtonDrums:
//                        instrument_type = "drums";
//                        break;
//                }
//            }
//        });

        scale = 0;

        SeekBar scaleSelector = (SeekBar) findViewById(R.id.scaleSelector);
        scaleSelector.setProgress(12);
        scaleValue.setText(String.valueOf(scale));

        scaleSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 12;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                scale = progressChangedValue - 12;
                scaleValue.setText(String.valueOf(scale));
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() != Sensor.TYPE_GAME_ROTATION_VECTOR && event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION)
            return;

        if(event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            sensorX = event.values[0];
            sensorY = event.values[1];
            sensorZ = event.values[2];

            float[] processed_sensor_values = SensorValueProcessor.process(event.values);

            Log.i("processed_sensor_values", String.valueOf(processed_sensor_values[0]));

            gameVecViewX.setText(String.valueOf(processed_sensor_values[0]));
            gameVecViewY.setText(String.valueOf(processed_sensor_values[1]));
            gameVecViewZ.setText(String.valueOf(processed_sensor_values[2]));


            NetworkActivity networkActivity = new NetworkActivity();
            networkActivity.sendPost("game_rotation_vector", processed_sensor_values, 1, instrument_type, scale);
        }
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            sensorX = event.values[0];
            sensorY = event.values[1];
            sensorZ = event.values[2];

            float[] processed_sensor_values = SensorValueProcessor.process(event.values);

            Log.i("processed_sensor_values", String.valueOf(processed_sensor_values[0]));

            accelerometerViewX.setText(String.valueOf(processed_sensor_values[0]));
            accelerometerViewY.setText(String.valueOf(processed_sensor_values[1]));
            accelerometerViewZ.setText(String.valueOf(processed_sensor_values[2]));


            NetworkActivity networkActivity = new NetworkActivity();
            networkActivity.sendPost("linear_acceleration", processed_sensor_values, 1, instrument_type, scale);
        }

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        switch (mDisplay.getRotation()) {
//            case Surface.ROTATION_0:
//                sensorX = event.values[0];
//                sensorY = event.values[1];
//                sensorZ = event.values[2];
//                viewX.setText(String.valueOf( sensorX));
//                break;
//            case Surface.ROTATION_90:
//                sensorX = -event.values[1];
//                sensorY = event.values[0];
//                viewX.setText(String.valueOf( sensorX));
//                break;
//            case Surface.ROTATION_180:
//                sensorX = -event.values[0];
//                sensorY = -event.values[1];
//                viewX.setText(String.valueOf( sensorX));
//                break;
//            case Surface.ROTATION_270:
//                sensorX = event.values[1];
//                sensorY = -event.values[0];
//                viewX.setText(String.valueOf( sensorX));
//                break;
//        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, game_vector, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
        super.onStop();
    }
}
