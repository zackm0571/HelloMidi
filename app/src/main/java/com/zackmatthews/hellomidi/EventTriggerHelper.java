package com.zackmatthews.hellomidi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by zackmatthews on 10/28/16.
 */

public class EventTriggerHelper {
    public static final String LAUNCH_APP_EVENT_TRIGGER = "Launching... ";
    public static final int NOTE_ON=-112;
    public static final int EMPTY=-8;
    private static EventTriggerHelper helper;

    public static EventTriggerHelper instance(){
        if(helper == null){
            helper = new EventTriggerHelper();
        }
        return helper;
    }

    /** Open another app.
     * @param context current Context, like Activity, App, or Service
     * @param packageName the full package name of the app to open
     * @return true if likely successful, false if unsuccessful
     */
    public boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

}
