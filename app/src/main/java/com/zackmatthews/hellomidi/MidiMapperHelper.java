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
        //Todo come up with better hash to easily lookup by either key or val
        if(pref.contains(action)){
            String midiVal = String.valueOf(getMidiValByAction(context, action));
            pref.edit().remove(midiVal).commit();
            pref.edit().remove(action).commit();
        }

        if(pref.contains(String.valueOf(note))) {
            String existingAction = getActionForMidiVal(context, note);
            pref.edit().remove(String.valueOf(note)).commit();
            pref.edit().remove(existingAction).commit();
        }

        pref.edit().putString(String.valueOf(note), action).commit();
        pref.edit().putInt(action, note).commit();
    }

    public String getActionForMidiVal(Context context, int note){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if(pref == null) return EventTriggerHelper.NULL_MAPPING;
        return pref.getString(String.valueOf(note), EventTriggerHelper.NULL_MAPPING);
    }


    public int getMidiValByAction(Context context, String action){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if(pref == null) return 0;
        return pref.getInt(action, 0);
    }
}
