package be.ugent.tiwi.oomt.beaconhunt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.HashMap;

import be.ugent.tiwi.oomt.beaconhunt.model.Beacon;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<String, Beacon> beacons = new HashMap<>();
        beacons.put("D8:18:AF:89:AE:8D", new Beacon("Estimote I","D8:18:AF:89:AE:8D",R.drawable.beacon1, false ));
        beacons.put("DA:C7:AC:D8:86:31", new Beacon("Estimote II","DA:C7:AC:D8:86:31",R.drawable.beacon3, false ));
        beacons.put("CC:9E:65:5C:6A:09", new Beacon("Estimote III","CC:9E:65:5C:6A:09",R.drawable.beacon2, false ));

        BeaconListAdapter adapter = new BeaconListAdapter(beacons,this);
        ListView beaconList = (ListView)findViewById(R.id.listView);
        beaconList.setAdapter(adapter);
    }
}
