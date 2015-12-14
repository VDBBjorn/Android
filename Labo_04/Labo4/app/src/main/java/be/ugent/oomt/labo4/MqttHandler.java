package be.ugent.oomt.labo4;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.android.service.MqttTraceHandler;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by elias on 19/01/15.
 */
public class MqttHandler implements MqttCallback, IMqttActionListener, MqttTraceHandler {

    // Mqtt server address
    public static final String serverURI = "tcp://iot.eclipse.org:1883";
    // unique client id for identifying to Mqtt server
    public static final String clientId = "bjorn.vandenbussche@ugent.be";
    // TODO: fill in start topics
    // topics to subscribe to on application start
    public static final String[] start_topics = new String[]{"/topic/state"};
    // Quality Of Service levels
    // 0: msg are only delivered when online, 1: msg are send once, 2: msg are send and checked
    public static final int qos = 2;
    //public static final String serverURI = "tcp://iot.eclipse.org:1883"; //public mqtt server
    private static final String TAG = MqttHandler.class.getCanonicalName();
    private static MqttHandler instance;
    // when connection is lost do not clean up the subscriptions of the user
    private final boolean cleanSession = false;
    private final Context context;
    private MqttAndroidClient client;

    private MqttHandler(Context context) {
        Log.d(TAG, "Constructor");
        this.context = context;
        createClient();
    }

    public static MqttHandler getInstance() {
        if (instance == null) {
            instance = new MqttHandler(MyApplication.getContext());
        }
        return instance;
    }

    private void createClient() {
        Log.d(TAG, "Creating client");
        client = new MqttAndroidClient(context, serverURI, clientId);
        client.setCallback(this);
        client.setTraceCallback(this);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        try {
            client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }
//        MqttMessage message = new MqttMessage();
//        message.setQos(qos);
//        message.setPayload(new String("Bjorn").getBytes());
//        client.publish("message);
    }

    /*
        Use this method to get the Mqtt client from anywhere in your application. The client can be used to send messages and subscribe to new topics.
     */
    public MqttAndroidClient getClient() {
        return client;
    }

    // TODO: implement IMqttActionListener, MqttCallback interfaces and MqttTraceHandler

    @Override
    public void connectionLost(Throwable cause) {
        Log.d(TAG, "Connection lost");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d(TAG, "Message arrived in " + topic + ": " + message.getPayload().toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "Delivery complete");
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        Log.d(TAG, "onSuccess");
        try {
            MqttMessage message = new MqttMessage(new String(client.getClientId()).getBytes());
            message.setQos(qos);
            message.setRetained(true);
            client.publish("/users", message);
            client.subscribe("/users",qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Log.d(TAG, "onFailure");
    }

    @Override
    public void traceDebug(String source, String message) {
        Log.d(TAG, "traceDebug");
    }

    @Override
    public void traceError(String source, String message) {
        Log.d(TAG, "traceError");
    }

    @Override
    public void traceException(String source, String message, Exception e) {
        Log.d(TAG, "traceException");
    }
}
