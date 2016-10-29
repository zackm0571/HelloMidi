package com.zackmatthews.hellomidi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by zackmatthews on 10/28/16.
 */

public class EventTriggerHelper {
    public static final String LAUNCH_APP_KEY_PREFIX="la-";
    public static final String TASKER_TASK_KEY_PREFIX="tskr-";
    public static final String LAUNCH_APP_EVENT_TRIGGER = "Launching... ";
    public static final int NOTE_ON=-112;
    public static final int EMPTY=-8;
    public static final String NULL_MAPPING="<null>";

    private static EventTriggerHelper helper;

    public static EventTriggerHelper instance(){
        if(helper == null){
            helper = new EventTriggerHelper();
        }
        return helper;
    }

    class AppInfo {
        String appname = "";
        String pname = "";
        String versionName = "";
        int versionCode = 0;
        String taskName = "";
        Drawable icon;

    }

    public ArrayList<AppInfo> getInstalledApps(Context context){
        if(context == null) return null;
        List<PackageInfo> apps = context.getPackageManager().getInstalledPackages(0);

        ArrayList<AppInfo> res = new ArrayList<AppInfo>();
        for(int i=0;i<apps.size();i++) {
            PackageInfo p = apps.get(i);

            AppInfo newInfo = new AppInfo();
            newInfo.appname = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon(context.getPackageManager());
            res.add(newInfo);
        }
        return res;
    }

    public ArrayList<AppInfo> getTaskerTasks(Context context){
        if(!TaskerIntent.taskerInstalled(context)){
            MidiHelper.instance(context).sendStatusEvent("To utilize tasker please install tasker from the Play Store.");
        }
        ArrayList<AppInfo> tasks = new ArrayList();
        Cursor c = context.getContentResolver().query( Uri.parse( "content://net.dinglisch.android.tasker/tasks" ), null, null, null, null );

        if ( c != null ) {
            int nameCol = c.getColumnIndex( "name" );
            int projNameCol = c.getColumnIndex( "project_name" );

            while ( c.moveToNext() ) {
                String name = c.getString(nameCol);
                Log.d(TAG, c.getString(projNameCol) + "/" + c.getString(nameCol));
                MidiHelper.instance(context).sendStatusEvent(c.getString(nameCol));
                AppInfo ai = new AppInfo();
                ai.taskName = name;
                tasks.add(ai);
            }
            c.close();
        }

        if(tasks.size() == 0){
            MidiHelper.instance(context).sendStatusEvent("No tasker tasks found");
        }
        return tasks;
    }

//    public void launchTaskerEvent(){
//        if ( TaskerIntent.testStatus( this ).equals( TaskerIntent.Status.OK ) ) {
//            TaskerIntent i = new TaskerIntent( "MY_USER_TASK_NAME" );
//            sendBroadcast( i );
//        }
//    }

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

    public void evaluateInputEvent(Context context, int note){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String action = pref.getString(String.valueOf(note), NULL_MAPPING);
        if(action.equals(NULL_MAPPING)) return;

        if(action.startsWith(LAUNCH_APP_KEY_PREFIX) && action.split("-").length > 1){
            action = action.split("-")[1];
            openApp(context, action);
        }

        else if(action.startsWith(TASKER_TASK_KEY_PREFIX) && action.split("-").length > 1){
            TaskerIntent i = new TaskerIntent( action.split("-")[1]);
            context.sendBroadcast( i );
        }
    }
}
