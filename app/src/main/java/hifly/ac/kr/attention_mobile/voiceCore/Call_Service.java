package hifly.ac.kr.attention_mobile.voiceCore;/*
package kr.ac.hifly.attention.voiceCore;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import hifly.ac.kr.attention.R;

public class Call_Service extends Service {
    private Call_Thread callThread;
    private Messenger mRemote;
    private MediaPlayer mp;
    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(new RemoteHandler()).getBinder();
    }

    // Send message to activity
    public void remoteSendMessage(String data) {
        if (mRemote != null) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = data;
            try {
                mRemote.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    // Service handler
    private class RemoteHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0 :
                    // Register activity hander
                    mRemote = (Messenger) msg.obj;
                    break;
                default :
                    remoteSendMessage("TEST");
                    break;
            }
        }
    }

    @Override
    public void onCreate() {
        try {
            mp = MediaPlayer.create(this, R.raw.ioi);
            mp.seekTo(0);
            mp.start();
        }
        catch (Exception e){

        }
        callThread = new Call_Thread();
        callThread.start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
        mp = null;
        callThread.interrupt();
    }
}
*/
