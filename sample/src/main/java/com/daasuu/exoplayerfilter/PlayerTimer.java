package com.daasuu.exoplayerfilter;

import android.os.Handler;
import android.os.Message;

/**
 * Timer for Seekbar
 * Created by sudamasayuki on 2017/05/18.
 */
public class PlayerTimer extends Handler {

    private static final int DEFAULT_INTERVAL_MILLIS = 1000;

    private boolean isUpdate = false;

    private Callback callback;

    private long startTimeMillis;

    private void init() {
        startTimeMillis = System.currentTimeMillis();
    }

    public void start() {
        init();
        isUpdate = true;
        handleMessage(new Message());
    }

    public void stop() {
        isUpdate = false;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        this.removeMessages(0);
        if (isUpdate) {
            sendMessageDelayed(obtainMessage(0), DEFAULT_INTERVAL_MILLIS);
            callback.onTick(System.currentTimeMillis() - startTimeMillis);
        }
    }

    public interface Callback {

        void onTick(long timeMillis);
    }
}
