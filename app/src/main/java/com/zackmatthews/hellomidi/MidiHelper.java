package com.zackmatthews.hellomidi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by zachmathews on 10/24/16.
 */
public class MidiHelper {

    private static MidiManager midiManager;

    public static void presentDevices(final Context context){
        midiManager = getMidiManager(context);

        if(midiManager == null) return;
        final MidiDeviceInfo[] devices = getDevices(midiManager);
        AlertDialog dialog = new AlertDialog.Builder(context).setAdapter(new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return true;
            }

            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return devices.length;
            }

            @Override
            public Object getItem(int position) {
                return devices[position];
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    if(inflater != null) {
                        convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
                        TextView text = (TextView)convertView.findViewById(android.R.id.text1);
                        text.setText(devices[position].getProperties().getString(MidiDeviceInfo.PROPERTY_PRODUCT, "Generic Midi Device"));
                    }
                }
                return convertView;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();
        dialog.show();
    }

    public static MidiDeviceInfo[] getDevices(MidiManager midiManager){
        if(midiManager == null) return null;
        return midiManager.getDevices();
    }
    public static MidiManager getMidiManager(Context context) {
        if(midiManager == null && context != null){
            midiManager = (MidiManager)context.getSystemService(Context.MIDI_SERVICE);
        }
        return midiManager;
    }

}
