package be.vdbbjorn.stepcounter;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class StepCounterService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    private int amountOfSteps;
    boolean hasPeaked;
    private float[] previousValueAccelerometer;
    private float[] currentValueAccelerometer;

    static final String ACTION_STEPCOUNT = "stepcounter.stepcount";

    public StepCounterService() {
    }

    // LIFECYCLE METHODS (unbounded service)

    @Override
    public void onCreate() {
        Log.i("StepCounterService", "onCreate()");
        super.onCreate();

        Log.i("StepCounterService", "amountOfStepsOnCreate: " + amountOfSteps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorEventListener = (SensorEventListener) this;

        amountOfSteps = 0;
        hasPeaked = false;
        currentValueAccelerometer = new float[3];
        previousValueAccelerometer = new float[3];
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(ACTION_STEPCOUNT, "onstartcommand");
        startForeground(201, getCompatNotification());
        broadcastIntent();
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("StepCounterService", "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("StepCounterService", "onTaskRemoved");
        //super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i("StepCounterService", "onRebind()");
        super.onRebind(intent);
    }

    @Override
    public void onLowMemory() {

        Log.i("StepCounterService", "onLowmemory()");
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        Log.i("StepCounterService", "onDestroy()");
        sensorManager.unregisterListener(sensorEventListener);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("StepCounterService", "onBind");
        return null;
    }


    // SENSOREVENTLISTENER METHODS

    @Override
    public void onSensorChanged(SensorEvent event) {
        currentValueAccelerometer = event.values.clone();
        calculateStep();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("StepCounterService", "onAccuracyChanged(Sensor sensor, int accuracy)");
    }

    // FUNCTIONS

    private double getMagnitude(float x, float y, float z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

    private double getLowpass(double previous, double current, double alpha) {
        // NOTE the  -9.81 because of gravity !!!
        return previous + alpha * (current - previous) - 9.81;
    }

    private void calculateStep() {
        double currentMagnitude = getMagnitude(currentValueAccelerometer[0],
                currentValueAccelerometer[1], currentValueAccelerometer[2]);
        double previousMagnitude = getMagnitude(currentValueAccelerometer[0],
                currentValueAccelerometer[1], currentValueAccelerometer[2]);

        double result = getLowpass(previousMagnitude, currentMagnitude, 0.1);

        double top_threshold = 1.5;
        double bottom_threshold = -1.5;

        if(result > top_threshold){
            hasPeaked = true;
        }
        else if(result < bottom_threshold && hasPeaked){
            Log.i("StepCounterService", "added 1 step");
            amountOfSteps++;
            hasPeaked=false;

            if(amountOfSteps % 10 == 0){
                broadcastIntent();
            }
        }

        previousValueAccelerometer = currentValueAccelerometer.clone();
    }

    // BROADCAST INTENTS

    private void broadcastIntent(){
        Log.i("StepCounterService", "broadcastIntent()");
        Log.i("StepCounterService", amountOfSteps+"");
        Intent intent = new Intent();
        intent.putExtra("steps", amountOfSteps);
        intent.setAction(ACTION_STEPCOUNT);
        sendBroadcast(intent);
    }

    private Notification getCompatNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Service started").setTicker("Service running").setWhen(System.currentTimeMillis());
        Intent startIntent = new Intent(getApplicationContext(),StepCounterActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 200, startIntent, 0);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        return notification;
    }
}
