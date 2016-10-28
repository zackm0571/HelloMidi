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
        if(msg[offset] != EventTriggerHelper.EMPTY && count > 1) {
            mListener.onMidiHelperStatusEvent(String.format(debug_msg, msg[offset+1], offset, count, timestamp));
        }
        EventTriggerHelper.instance().evaluateInputEvent(msg[offset]);

//        if(msg[offset] == EventTriggerHelper.NOTE_ON) { //NOTE_ON event
//            mListener.onMidiHelperStatusEvent(EventTriggerHelper.LAUNCH_APP_EVENT_TRIGGER);
//        }
    }
}
