package be.ugent.oomt.labo5;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class PongActivity extends Activity {

    private SensorManager sensorManager;
    private SensorEventListener sensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pong);

        // get the sensor
        sensorListener = (SensorEventListener) findViewById(R.id.fullscreen_content);
        // list all sensors available for this device in Logcat
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Log.i("Test", sensorManager.getSensorList(Sensor.TYPE_ALL).toString());
    }

    // TODO: override onResume and onPause to register and unregister the listener for the sensor
    @Override
    protected void onResume() {
        super.onResume();
        Sensor gyro = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(sensorListener, gyro, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorListener);
    }
}
