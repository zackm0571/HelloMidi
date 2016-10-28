package com.zackmatthews.hellomidi;

import android.app.Application;
import android.content.Context;

/**
 * Created by zackmatthews on 10/28/16.
 */

public class AppHelper extends Application {
    public static AppHelper instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
}
