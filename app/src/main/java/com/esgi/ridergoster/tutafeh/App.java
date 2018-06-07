package com.esgi.ridergoster.tutafeh;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.esgi.ridergoster.tutafeh.services.RealmActions;

import io.realm.Realm;

/**
 * Created by vincentk on 26/06/2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("app", "hello world");
        Realm.init(this);

        RealmActions.initCSV(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
