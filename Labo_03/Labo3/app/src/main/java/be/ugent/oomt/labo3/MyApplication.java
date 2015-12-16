package be.ugent.oomt.labo3;

import android.app.Application;
import android.content.Context;

import be.ugent.oomt.labo3.contentprovider.MessageProvider;

/**
 * Created by Bjorn on 10/12/2015.
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        MessageProvider.addTestData(context);
    }
}
