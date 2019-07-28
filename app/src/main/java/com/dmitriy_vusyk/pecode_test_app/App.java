package com.dmitriy_vusyk.pecode_test_app;

import android.app.Application;

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static void setInstance(App instance) {
        App.instance = instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
    }
}
