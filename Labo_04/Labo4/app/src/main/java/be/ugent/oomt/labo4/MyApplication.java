package be.ugent.oomt.labo4;

import android.app.Application;
import android.content.Context;

import be.ugent.oomt.labo4.contentprovider.MessageProvider;

/**
 * Created by elias on 20/01/15.
 */
public class MyApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    // TODO: add static getter method for context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        MessageProvider.addTestData(context);
    }
}
