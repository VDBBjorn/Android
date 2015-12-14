package be.ugent.tiwi.oomt.beaconhunt;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ugent.tiwi.oomt.beaconhunt.model.Beacon;


public class MainActivity extends Activity {

    private Button scanButton;
    BluetoothAdapter ba;
    boolean isScanning = false;

    HashMap<String, Beacon> beacons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beacons = new HashMap<>();
        beacons.put("vdbbjorn.be", new Beacon("vdbbjorn","http://goo.gl/dgsnbi",R.drawable.beacon1, false ));
        beacons.put("DA:C7:AC:D8:86:31", new Beacon("Estimote II", "DA:C7:AC:D8:86:31", R.drawable.beacon3, false));
        beacons.put("CC:9E:65:5C:6A:09", new Beacon("Estimote III", "CC:9E:65:5C:6A:09", R.drawable.beacon2, false));

        BeaconListAdapter adapter = new BeaconListAdapter(beacons,this);
        ListView beaconList = (ListView)findViewById(R.id.listView);
        beaconList.setAdapter(adapter);

        scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isScanning) {
                    isScanning = false;
                    stopScan();
                }
                else {
                    isScanning = true;
                    startScan();
                }
            }
        });

        ba = BluetoothAdapter.getDefaultAdapter();
        if (ba == null || !ba.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,1);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startScan() {
        Toast t = Toast.makeText(getApplicationContext(), "Scanning started!", Toast.LENGTH_SHORT);
        t.show();
        ba.getBluetoothLeScanner().startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                BluetoothDevice device = result.getDevice();
                for(Beacon b : beacons.values()) {
                    if(device.getAddress() == b.getAddress()) {
                        if(result.getRssi() <= 0.5) {
                            b.setFound(true);
                        }
                        b.setRssi(result.getRssi());
                    }
                }
            }
        });
        scanButton.setText(getText(R.string.stop));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void stopScan() {
        Toast t = Toast.makeText(getApplicationContext(), "Scanning stopped!", Toast.LENGTH_SHORT);
        t.show();
        ba.getBluetoothLeScanner().stopScan(null);
        scanButton.setText(getText(R.string.start));
    }
}

