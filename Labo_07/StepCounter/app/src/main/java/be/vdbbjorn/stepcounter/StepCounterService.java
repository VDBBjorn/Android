package be.vdbbjorn.stepcounter;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * Created by bjorn on 14/12/15.
 */
public class StepCounterService extends Service implements SensorEventListener {

    private final String TAG = "StepCounterService";
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private final Vector<Double> magnitude = new Vector<>();
    private final Double top_threshold = 1.5;
    private final Double bottom_threshold = -1.5;
    private long time_top = 0;
    private int steps = 0;
    private int previous_steps = 0;

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        StepCounterService getService() {
            Log.d(TAG,"localbinder");
            return StepCounterService.this;
        }
    }

    //Service

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onbind " + intent.toString());
        return mBinder;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "stepCounterService is active", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"on create");
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "on start command");
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "on destroy");
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG,"on config changed");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG,"on low memory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG,"on trim memory");
        super.onTrimMemory(level);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "on unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG,"on rebind");
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG,"on task removed");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        Log.d(TAG,"dump");
        super.dump(fd, writer, args);
    }

    //sensor

    @Override
    public void onSensorChanged(SensorEvent event) {
        float axisX = event.values[0];
        float axisY = event.values[1];
        float axisZ = event.values[2];
        magnitude.add(Math.sqrt(Math.pow(axisX, 2) + Math.pow(axisY, 2) + Math.pow(axisZ, 2)));
        Vector<Double> result = (Vector<Double>)magnitude.clone();
        Vector<Double> trend = (Vector<Double>)magnitude.clone();
        result = lowpass(result, 0.1);
        trend = lowpass(trend, 0.01);
        for(int i = 0; i<result.size();i++) {
            Double x = result.elementAt(i) - trend.elementAt(i);
            result.setElementAt(x,i);
        }
        for(int i=0;i<result.size();i++) {
            if(result.elementAt(i) > top_threshold) {
                time_top = event.timestamp;
            }
            else if(result.elementAt(i) < bottom_threshold && (event.timestamp - time_top) <  150000000) {
                steps += 1;
                time_top = 0;
            }
        }
        if(steps >= (previous_steps+10)) {
            Log.d("Steps","Broadcasting steps");
            Intent i = new Intent();
            i.setAction("STEPS_SERVICE");
            i.putExtra("steps",steps);
            sendBroadcast(i);
            previous_steps = steps;
        }
        Log.d("Steps",""+steps);
    }

    public Vector<Double> lowpass(Vector<Double> d, double alpha) {
        for(int i=1; i<d.size();i++) {
            Double x = d.elementAt(i-1) + alpha * (d.elementAt(i) - d.elementAt(i-1));
            d.setElementAt(x,i);
        }
        return d;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG,"onAccuracyChanged");
    }
}
