package hifly.ac.kr.attention_mobile.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.data.Call;
import kr.ac.hifly.attention.data.User;
import kr.ac.hifly.attention.messageCore.MessageService;
import kr.ac.hifly.attention.value.Values;

/**
 * Created by CYSN on 2017-11-12.
 */

public class Main_Friend_Call_Activity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton call_endFab;
    private SoundPool sound;
    private int soundId;
    private TextView textView;

    private User user;

    private Intent intent;

    private Messenger messenger;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String myUUID;
    private String uname;
    private Message message;
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            messenger = new Messenger(service);
            if (messenger != null) {
                Message message = new Message();
                message.what = 0;
                message.obj = new Messenger(new RemoteHandler());
                try {
                    messenger.send(message);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private class RemoteHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    public void onBackPressed() {
        unbindService(serviceConnection);
        stopService(intent);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_friend_call);
        textView = (TextView) findViewById(R.id.main_friend_call_textview);
        call_endFab = (FloatingActionButton) findViewById(R.id.main_friend_call_end_fab);
        message = new Message();
        message.what = Values.START_CALL;
        try {
            messenger.send(message);
        } catch (Exception e) {
            e.getStackTrace();
        }

        user = (User) getIntent().getSerializableExtra("object");

        firebaseDatabase = FirebaseDatabase.getInstance();
        myUUID = getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE).getString(Values.userUUID, "null");
        uname = getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE).getString(Values.userName, "null");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("www.google.com", 80);
                    String ipAddress = socket.getLocalAddress().toString();
                    ipAddress = ipAddress.substring(1);
                    Call call = new Call(myUUID, Values.CALLING, ipAddress,Values.CALLER_SEND_PORT);

                    databaseReference = firebaseDatabase.getReference();
                    String randomUUID = UUID.randomUUID().toString().replace("-", "");
                    databaseReference.child(Values.USER).child(user.getUuid()).child(Values.VOICE).child(Values.VOICE_CALLER).setValue(myUUID);
                    databaseReference.child(Values.USER).child(user.getUuid()).child(Values.VOICE).child(Values.VOICE_ROOM).setValue(Values.VOICE_ROOM_FIRST + randomUUID);
                    databaseReference.child(Values.VOICE).child(Values.VOICE_ROOM_FIRST + randomUUID).child(myUUID).setValue(call);
                    databaseReference.child(Values.VOICE).child(Values.VOICE_ROOM_FIRST + randomUUID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String value = snapshot.child(Values.VOICE_CALL_STATE).getValue(String.class);
                                if (value != null) {
                                    if(!value.equals(myUUID)) {
                                        Log.i(Values.TAG, value + " input!!");
                                        String voiceUserIp = snapshot.child(Values.VOICE_USER_IP).getValue(String.class);
                                        Integer voiceUserPort = snapshot.child(Values.VOICE_USER_PORT).getValue(Integer.class);
                                        if (value.equals(Values.RECEIVE)) {//receive로 바꾸는건 반대쪽 클라이언트가 바꿈
                                            Log.i(Values.TAG, "RECEIVE!! " + voiceUserIp + " " + voiceUserPort);
                                            new Handler().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    textView.setText(user.getName() + "와 통화중...");
                                                }
                                            });
                                            message.what = Values.RECEIVE_CALL;
                                            message.obj = voiceUserIp + " " + voiceUserPort + " " + Values.VOICE_CALLER;
                                            Log.i(Values.TAG,"전화받기!!!!!!!!!!!!");
                                            try {
                                                messenger.send(message);
                                            } catch (Exception e) {
                                                e.getStackTrace();
                                            }
                                            //send voice data
                                        } else if (value.equals(Values.REFUSE)) {// END는 누구나 바꿀수 있고 바꾸면 양쪽다 종료
                                            Log.i(Values.TAG, "REFUSE!!");
                                            onBackPressed();
                                        } else if (value.equals(Values.END)) {// END는 누구나 바꿀수 있고 바꾸면 양쪽다 종료
                                            Log.i(Values.TAG, "END!!");
                                            onBackPressed();
                                            //stop voice data
                                        }
                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } catch (Exception e) {
                    Log.i("ERROR", e.getMessage());
                }
            }
        }).start();

        if (user != null) {
            textView.setText(user.getName() + "에게 전화 거는중...");
        }
        call_endFab.setOnClickListener(this);
        intent = new Intent(this, MessageService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /*    class CallStateListener implements ValueEventListener{
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }*/
    @Override
    public void onClick(View view) {
        databaseReference.child(Values.USER).child(user.getUuid()).child(Values.VOICE).child(Values.VOICE_CALLER).setValue("null");
        databaseReference.child(Values.USER).child(user.getUuid()).child(Values.VOICE).child(Values.VOICE_ROOM).setValue("null");
        message.what = Values.END_CALL;
        message.obj = Values.END;
        try {
            messenger.send(message);
        }catch (Exception e){
            e.getStackTrace();
        }
        if (view == call_endFab) {
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStart() {
        super.onStart();
      /*  AudioAttributes audioAttributes;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            sound = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        }
        else {
            sound = new SoundPool(8, AudioManager.STREAM_NOTIFICATION, 0);
        }

        soundId = sound.load(this, R.raw.ioi, 1);
        sound.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                sound.play(soundId, 1f, 1f, 0, 0, 1f);
            }
        });*/

    }

    @Override
    protected void onStop() {
        super.onStop();

     /* soundId = 0;
        sound.release();
        sound = null;*/
    }
}
