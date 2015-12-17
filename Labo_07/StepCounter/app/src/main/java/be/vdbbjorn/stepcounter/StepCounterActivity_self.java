//package be.vdbbjorn.stepcounter;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.ServiceConnection;
//import android.os.IBinder;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.logging.Filter;
//
//public class StepCounterActivity extends Activity {
//
//    private BroadcastReceiver receiver;
//    private ProgressMeterView progressMeterView;
//    private int steps = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        progressMeterView = new ProgressMeterView(getApplicationContext());
//        progressMeterView.setProgress(0);
//        setContentView(progressMeterView);
//        Intent intent = new Intent(getBaseContext(), StepCounterService.class);
//        startService(intent);
//        registerBroadcastReceiver();
//    }
//
//    private void registerBroadcastReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("STEPS_SERVICE");
//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                steps = intent.getIntExtra("steps",steps);
//                progressMeterView.setProgress(steps/10000);
//            }
//        };
//        registerReceiver(receiver, filter);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(receiver);
//    }
//}
