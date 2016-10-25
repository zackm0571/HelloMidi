package com.zackmatthews.hellomidi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by zackmathews on 10/24/16.
 */
public class MidiHelper extends MidiManager.DeviceCallback{
    private static final String DEVICE_CONNECTED_EVENT="Status: Found new device";
    private static final String DEVICE_OPENED_EVENT="Status: Connected to %s";
    private static final String DEVICE_REMOVED_EVENT="Status: Disconnected, please re-connect your device";
    public interface MidiHelperEventListener{
        public void onMidiHelperStatusEvent(String statusText);
    }

    private static MidiHelper midiHelper;
    private MidiManager midiManager;
    private AlertDialog devicePickerDialog;
    private Context context;
    private MidiHelperEventListener midiHelperEventListener;
    private MidiManager.OnDeviceOpenedListener onDeviceOpenedListener;

    public static MidiHelper instance(Context context){
        if(midiHelper == null){ midiHelper = new MidiHelper(); }
        midiHelper.context = context;
        return midiHelper;
    }

    public void registerMidiHelperEventListener(MidiHelperEventListener listener){
        this.midiHelperEventListener = listener;
        midiHelper.getMidiManager().registerDeviceCallback(midiHelper, new Handler());
    }

    public void presentDevices(){
        midiManager = getMidiManager();

        if(midiManager == null) return;
        if(devicePickerDialog != null && devicePickerDialog.isShowing()) devicePickerDialog.dismiss();

        final MidiDeviceInfo[] devices = getDevices(midiManager);
        devicePickerDialog = new AlertDialog.Builder(context).setAdapter(new ListAdapter() {
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
                connectToDevice(devices[which], getOnDeviceOpenedListener(), new Handler());
            }
        }).create();
        devicePickerDialog.show();
    }

    public void connectToDevice(MidiDeviceInfo info, MidiManager.OnDeviceOpenedListener listener, Handler handler){
        getMidiManager().openDevice(info, listener, handler);
    }

    public  MidiDeviceInfo[] getDevices(MidiManager midiManager){
        if(midiManager == null) return null;
        return midiManager.getDevices();
    }
    public MidiManager getMidiManager() {
        if(midiManager == null && context != null){
            midiManager = (MidiManager)context.getSystemService(Context.MIDI_SERVICE);
        }
        return midiManager;
    }

    @Override
    public void onDeviceAdded(MidiDeviceInfo device) {
        super.onDeviceAdded(device);
        presentDevices();

        sendStatusEvent(DEVICE_CONNECTED_EVENT);
    }

    @Override
    public void onDeviceRemoved(MidiDeviceInfo device) {
        super.onDeviceRemoved(device);
        if(getDevices(getMidiManager()).length > 0) {
            presentDevices();
        }
        else if(devicePickerDialog != null && devicePickerDialog.isShowing()){
            devicePickerDialog.dismiss();
        }

        sendStatusEvent(DEVICE_REMOVED_EVENT);
    }

    protected void sendStatusEvent(String statusEvent){
        if(midiHelperEventListener != null){
            midiHelperEventListener.onMidiHelperStatusEvent(statusEvent);
        }
    }

    public MidiManager.OnDeviceOpenedListener getOnDeviceOpenedListener() {
        if(onDeviceOpenedListener == null){
            onDeviceOpenedListener = new MidiManager.OnDeviceOpenedListener() {
                @Override
                public void onDeviceOpened(MidiDevice device) {
                    String deviceName = "Generic Midi Device";
                    MidiDeviceInfo info = device.getInfo();
                    if(info != null){
                        Bundle properties = info.getProperties();
                        if(properties != null){
                            deviceName = properties.getString(MidiDeviceInfo.PROPERTY_PRODUCT, deviceName);
                        }
                    }
                    sendStatusEvent(String.format(DEVICE_OPENED_EVENT, deviceName));
                }
            };
        }
        return onDeviceOpenedListener;
    }

    public void setOnDeviceOpenedListener(MidiManager.OnDeviceOpenedListener onDeviceOpenedListener) {
        this.onDeviceOpenedListener = onDeviceOpenedListener;
    }
}
