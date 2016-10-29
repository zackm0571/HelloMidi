package com.zackmatthews.hellomidi;

import android.content.Context;
import android.media.midi.MidiReceiver;

import java.io.IOException;
import java.util.Stack;

/**
 * Created by zackmatthews on 10/28/16.
 */

public class SimpleReceiver extends MidiReceiver {
    private static Stack<String> mLastNotesPressed;
    private MidiHelper.MidiHelperEventListener mListener;
    private Context context;
    public SimpleReceiver(Context context, MidiHelper.MidiHelperEventListener listener){
        this.mListener = listener;
        this.context = context;
    }


    @Override
    public void onSend(byte[] msg, int offset, int count, long timestamp) throws IOException {
        String debug_msg = "Received onSend event with message: %d offset: %d count: %d timestamp: %d";
        if(msg[offset] != EventTriggerHelper.EMPTY && count > 1) {
            mListener.onMidiHelperStatusEvent(String.format(debug_msg, msg[offset+1], offset, count, timestamp));
            getmLastNotesPressed().push(String.valueOf(msg[offset+1]));
            if(MidiHelper.STATE == MidiHelper.STATE_MIDI_LISTENING) {
                EventTriggerHelper.instance().evaluateInputEvent(context, msg[offset + 1]);
            }
            else if(MidiHelper.STATE == MidiHelper.STATE_MIDI_ASSIGNMENT){
                MidiHelper.instance(context).updateNotePicker();
            }
        }

//        if(msg[offset] == EventTriggerHelper.NOTE_ON) {
//            TaskerIntent i = new TaskerIntent("Test");
//            context.sendBroadcast(i);
//        }

//        if(msg[offset] == EventTriggerHelper.NOTE_ON) { //NOTE_ON event
//            mListener.onMidiHelperStatusEvent(EventTriggerHelper.LAUNCH_APP_EVENT_TRIGGER);
//        }
    }

    public static Stack<String> getmLastNotesPressed() {
        if(mLastNotesPressed == null){
            mLastNotesPressed = new Stack<>();
        }
        return mLastNotesPressed;
    }
}
