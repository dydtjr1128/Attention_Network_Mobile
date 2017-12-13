package hifly.ac.kr.attention_mobile.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.data.Call;
import kr.ac.hifly.attention.data.User;
import kr.ac.hifly.attention.messageCore.MessageService;
import kr.ac.hifly.attention.value.Values;



public class Main_Friend_Call_Receive_Activity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton call_refuseFab;
    private FloatingActionButton call_receiveFab;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView textView;
    private Messenger messenger;
    private boolean isCalling = false;
    private String roomName;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new Messenger(iBinder);
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

    /*
    FLAG_KEEP_SCREEN_ON : Screen 을 켜진 상태로 유지
    FLAG_DISMISS_KEYGUARD : Keyguard를 해지
    FLAG_TURN_SCREEN_ON : Screen On
    FLAG_SHOW_WHEN_LOCKED : Lock 화면 위로 실행
    */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_friend_call_receive);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        String name = getIntent().getStringExtra("name");
        roomName = getIntent().getStringExtra(Values.VOICE_ROOM);
        textView = (TextView) findViewById(R.id.main_friend_call_receive_textview);
        call_refuseFab = (FloatingActionButton) findViewById(R.id.main_friend_call_refuse_fab);
        call_receiveFab = (FloatingActionButton) findViewById(R.id.main_friend_call_receive_fab);
        call_refuseFab.setOnClickListener(this);
        call_receiveFab.setOnClickListener(this);
        if (name != null) {
            textView.setText(name + "에게 전화 왔습니다...");
        }
        Intent intent = new Intent(this, MessageService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        Message message = new Message();

        if (view == call_refuseFab) {//call refuse
            Log.i(Values.TAG,"전화거절!!!!!!!!!!!!");
            if(isCalling) {
                message.what = Values.END_CALL;
                message.obj = Values.END;
                try {
                    messenger.send(message);
                } catch (Exception e) {
                    e.getStackTrace();
                }
                onBackPressed();
            }
            else{
                message.what = Values.REFUSE_CALL;
                message.obj = Values.REFUSE;
                try {
                    messenger.send(message);
                } catch (Exception e) {
                    e.getStackTrace();
                }
                onBackPressed();
            }

        } else if (view == call_receiveFab) {//call receive
            databaseReference.child(Values.VOICE).child(roomName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Call call = snapshot.getValue(Call.class);
                        if(!call.getCaller().equals(Values.myUUID)){
                            Message msg = new Message();
                            msg.what = Values.RECEIVE_CALL;
                            msg.obj = snapshot.child(Values.VOICE_USER_IP).getValue(String.class) + " " + snapshot.child(Values.VOICE_USER_PORT).getValue(Integer.class) + " " + Values.USER;
                            Log.i(Values.TAG,"전화받기!!!!!!!!!!!!");
                            try {
                                messenger.send(msg);
                            } catch (Exception e) {
                                e.getStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Log.i(Values.TAG,"이제 애니메이션!!!!!!!!!!!!");
            call_receiveFab.setVisibility(View.GONE);
            TranslateAnimation ani = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.4f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            ani.setFillAfter(true); // 애니메이션 후 이동한좌표에
            ani.setDuration(2000); //지속시간
            call_refuseFab.startAnimation(ani);
            isCalling = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
      /*  try {
            mp = MediaPlayer.create(this, R.raw.ioi);
            mp.seekTo(0);
            mp.start();
        } catch (Exception e) {
            e.getStackTrace();
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
    /*    mp.release();
        mp = null;*/

    }
}
