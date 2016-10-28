package com.zackmatthews.hellomidi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by zackmatthews on 10/28/16.
 */

public class MidiMapperHelper {
    private static MidiMapperHelper helper;

    public static MidiMapperHelper instance(){
        if(helper == null){
            helper = new MidiMapperHelper();
        }
        return helper;
    }

    public void storeMidiMapping(Context context, String action, int note){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if(pref == null) return;

        pref.edit().putString(String.valueOf(note), action).commit();
    }

    public String getActionForMidiVal(Context context, int note){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if(pref == null) return "";
        return pref.getString(String.valueOf(note), EventTriggerHelper.NULL_MAPPING);
    }

}
