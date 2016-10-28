package com.zackmatthews.hellomidi;

import android.media.midi.MidiReceiver;

import java.io.IOException;

/**
 * Created by zackmatthews on 10/28/16.
 */

public class SimpleReceiver extends MidiReceiver {
    private MidiHelper.MidiHelperEventListener mListener;
    public SimpleReceiver(MidiHelper.MidiHelperEventListener listener){
        this.mListener = listener;
    }
    @Override
    public void onSend(byte[] msg, int offset, int count, long timestamp) throws IOException {
        String debug_msg = "Received onSend event with message: %d offset: %d count: %d timestamp: %d";
        mListener.onMidiHelperStatusEvent(String.format(debug_msg,  msg[offset], offset, count, timestamp));
    }
}
