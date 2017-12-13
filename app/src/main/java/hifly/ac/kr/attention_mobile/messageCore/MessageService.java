package hifly.ac.kr.attention_mobile.messageCore;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.adapter.Main_Chat_Room_RecyclerView_Adapter;
import kr.ac.hifly.attention.adapter_item.ChatActivity_RecyclerView_Item;
import kr.ac.hifly.attention.adapter_item.Main_Chat_Room_RecyclerView_Item;
import kr.ac.hifly.attention.data.Call;
import kr.ac.hifly.attention.data.User;
import kr.ac.hifly.attention.main.ChatRoomWrapper;
import kr.ac.hifly.attention.main.MainActivity;
import kr.ac.hifly.attention.main.Main_Friend_Call_Receive_Activity;
import kr.ac.hifly.attention.value.Values;
import kr.ac.hifly.attention.voiceCore.Call_Receive_Thread;
import kr.ac.hifly.attention.voiceCore.Call_Thread;

public class MessageService extends Service {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ReceiveThread receiveThread;
    private Messenger mRemote;
    private boolean isCalling = false;
    private boolean isFirst = true;
    private String voiceRoomName;
    private static PowerManager.WakeLock sCpuWakeLock;
    private String myUUID;
    private String myName;
    private MediaPlayer mp;

    private Main_Chat_Room_RecyclerView_Adapter main_chat_room_recyclerView_adapter;
    private List<Main_Chat_Room_RecyclerView_Item> main_chat_room_recyclerView_items;
    private String value;
    private ChatRoomWrapper chatRoomWrapper;

    private Call_Thread call_thread;
    private Call_Receive_Thread call_receive_thread;

    private String value_chat_room_name;
    private String innerValue;
    private StringBuilder real_chat_room_name;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private StringBuilder shared_chat_room_name;
    private SoundPool soundPool;
    private int soundID;
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

    public void screenOn() {

        if (sCpuWakeLock != null) {
            return;
        }
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        sCpuWakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "hi");

        sCpuWakeLock.acquire();

        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }

    }

    // Service handler
    private class RemoteHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            String message = null;

            switch (msg.what) {
                case 0:
                    // Register activity hander
                    mRemote = (Messenger) msg.obj;
                    break;
                case Values.START_CALL:
                    Log.i(Values.TAG,"전화 들옴~~~~~~~~~~~~~~~~~~");
                    try {
                        mp.seekTo(0);
                        mp.start();
                    }catch (Exception e){
                        e.getStackTrace();
                    }
                    break;
                case Values.REFUSE_CALL:
                    Log.i(Values.TAG,message + " ");
                    databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).child(Values.VOICE).setValue("null");
                    databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).child(Values.VOICE_CALLER).setValue("null");
                    databaseReference.child(Values.VOICE).child(voiceRoomName).child(myUUID).child(Values.VOICE_CALL_STATE).setValue(Values.REFUSE);
                    message = (String) msg.obj;
                    isCalling = false;
                    break;
                case Values.RECEIVE_CALL:
                    message = (String) msg.obj;//ip
                    String messages[] = message.split(" ");
                    String userIP = messages[0];
                    final int userPort = Integer.parseInt(messages[1]);
                    final String userState = messages[2];
                    if (!isCalling) {
                        isCalling = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Socket socket = new Socket("www.google.com", 80);
                                    String ipAddress = socket.getLocalAddress().toString();
                                    ipAddress = ipAddress.substring(1);
                                    Call call = null;
                                    if(userState.equals(Values.VOICE_CALLER)) {
                                        call = new Call(myUUID, Values.RECEIVE, ipAddress, userPort);
                                    }
                                    else{
                                        call = new Call(myUUID, Values.RECEIVE, ipAddress, userPort + 1);
                                    }
                                    databaseReference.child(Values.VOICE).child(voiceRoomName).child(myUUID).setValue(call);
                                }catch (Exception e){
                                    e.getStackTrace();
                                }
                            }
                        }).start();
                        mp.release();
                        screenOn();
                        if(userState.equals(Values.VOICE_CALLER)) {
                            call_thread = new Call_Thread(userIP,userPort-1);
                            call_receive_thread = new Call_Receive_Thread(userIP,userPort);
                            call_thread.start();
                            call_receive_thread.start();
                        }
                        else{
                            call_thread = new Call_Thread(userIP,userPort+1);
                            call_receive_thread = new Call_Receive_Thread(userIP,userPort);
                            call_thread.start();
                            call_receive_thread.start();
                        }

                    }
                    break;
                case Values.END_CALL:
                    Log.i(Values.TAG,message + " ");
                    isCalling = false;
                    if (voiceRoomName != null && !voiceRoomName.equals("null")) {
                        databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).removeValue();
                        databaseReference.child(Values.VOICE).child(voiceRoomName).child(myUUID).removeValue();
                    }
                    if(call_thread != null){
                        call_thread.interrupt();
                    }
                    if(call_receive_thread != null){
                        call_receive_thread.interrupt();
                    }
                    break;
                case Values.CHAT_ROOM:       // 채팅창 방 업데이트
                    chatRoomWrapper = (ChatRoomWrapper) msg.obj;
                    main_chat_room_recyclerView_adapter = chatRoomWrapper.getAdapter();
                    main_chat_room_recyclerView_items = chatRoomWrapper.getItems();

                    /* shared init */
                    sharedPreferences = getSharedPreferences(Values.shared_name, Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    shared_chat_room_name = new StringBuilder(sharedPreferences.getString(Values.shared_chat_room_name,""));


                    databaseReference.child(Values.USER).child(myUUID).child("friends").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                value = snapshot.getValue(String.class);
                                if (value != null && !value.equals("null")) {
                                    innerValue = value;  // innerValue는 내가 가지고 있는 채팅방 이름

                                    databaseReference.child("ChatRoom").child(innerValue).limitToLast(1).addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot,String s) {

                                            int shared_index = shared_chat_room_name.toString().split("!").length;

                                            Log.i("1어", shared_chat_room_name.toString());
                                            Log.i("2어 init index", Integer.toString(shared_index));

                                            ChatActivity_RecyclerView_Item item = dataSnapshot.getValue(ChatActivity_RecyclerView_Item.class);
                                            value_chat_room_name = dataSnapshot.getRef().getParent().getKey();

                                            if (item == null || item.getSender_name() == null) {
                                                return;
                                            }

                                            int kk = 0;
                                            for(int i=0; i<main_chat_room_recyclerView_items.size(); i++){

                                                real_chat_room_name = new StringBuilder("");

                                                if(main_chat_room_recyclerView_items.get(i).getChatRoomName().equals(value_chat_room_name)){

                                                    for(int aa = 0; aa < MainActivity.users.size(); aa++)
                                                    {
                                                        if(main_chat_room_recyclerView_items.get(i).getChatRoomName().contains(MainActivity.users.get(aa).getUuid())){
                                                            if(real_chat_room_name.toString().contains(MainActivity.users.get(aa).getName())) {
                                                                //if(!main_chat_room_recyclerView_items.get(i).getChatRoomName().contains(MainActivity.users.get(aa).getUuid()))
                                                                    continue;
                                                            }
                                                            real_chat_room_name.append(MainActivity.users.get(aa).getName() + " ");
                                                        }
                                                    }
                                                    if(!shared_chat_room_name.toString().contains(real_chat_room_name)) {
                                                        shared_chat_room_name.append(real_chat_room_name + "!");
                                                        editor.putString(Values.shared_chat_room_name, shared_chat_room_name.toString());
                                                        editor.apply();
                                                    }
                                                    main_chat_room_recyclerView_items.remove(i);   //@@
                                                    main_chat_room_recyclerView_items.add(0, new Main_Chat_Room_RecyclerView_Item(real_chat_room_name.toString(), item.getChat_content(), item.getTime(), value_chat_room_name));
                                                    kk = 1;
                                                    for(int qw=0; qw<MainActivity.users.size(); qw++){
                                                        if(MainActivity.users.get(qw).getUuid().equals(main_chat_room_recyclerView_items.get(i).getChatRoomName().split("  ")[0]));
                                                        main_chat_room_recyclerView_items.get(i).setUser(MainActivity.users.get(qw));
                                                    }
                                                }
                                            }
                                            if(kk == 0){
                                                Log.i("3어", shared_chat_room_name.toString());
                                                shared_chat_room_name = new StringBuilder(sharedPreferences.getString(Values.shared_chat_room_name,""));
                                                Log.i("4어", shared_chat_room_name.toString());
                                                String shared_spilt_value[] = shared_chat_room_name.toString().split("!");
                                                Log.i("5어", Integer.toString(shared_spilt_value.length));
                                                Log.i("6어 index", Integer.toString(shared_index));

                                                main_chat_room_recyclerView_items.add(new Main_Chat_Room_RecyclerView_Item(shared_spilt_value[--shared_index], item.getChat_content(), item.getTime(), value_chat_room_name));

                                            }

                                            if (main_chat_room_recyclerView_items.size() != 0) {
                                                chatRoomWrapper.getNullTextView().setVisibility(chatRoomWrapper.getNullTextView().INVISIBLE);
                                            }

                                            main_chat_room_recyclerView_adapter.notifyDataSetChanged();
                                            soundPool.play(soundID,1f, 1f, 0, 0, 1f);
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    break;
                default:
                    remoteSendMessage("TEST");
                    break;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        myUUID = getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE).getString(Values.userUUID, "null");
        if (receiveThread == null) {
            receiveThread = new ReceiveThread();
            receiveThread.start();
            Log.i(Values.TAG, "ReceiveThread Start!!");
        }
        Log.i(Values.TAG, "MessageService Start!!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mp = MediaPlayer.create(this, R.raw.ioi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        }
        else {
            soundPool = new SoundPool(8, AudioManager.STREAM_NOTIFICATION, 0);
        }
        soundID = soundPool.load(this,R.raw.click,1);
        startForeground(1, new Notification());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiveThread != null)
            receiveThread.interrupt();
    }

    class ReceiveThread extends Thread {
        public void run() {
            try {
                if (!myUUID.equals(null)) {
                    databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).child(Values.VOICE_ROOM).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            voiceRoomName = dataSnapshot.getValue(String.class);
                            if (voiceRoomName != null)
                                Log.i(Values.TAG, voiceRoomName + " " + dataSnapshot.getKey() + "@@@@@@@@@" + databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).getKey() + " " + databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).child(Values.VOICE_CALLER).getKey());
                            if (voiceRoomName != null && !voiceRoomName.equals("null")) {//not doing voice chat;
                                databaseReference.child(Values.USER).child(myUUID).child(Values.VOICE).child(Values.VOICE_CALLER).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String caller = dataSnapshot.getValue(String.class);
                                        Log.i(Values.TAG,"들어옴!!!");
                                        if (caller != null)
                                            Log.i(Values.TAG, caller + " " + dataSnapshot.getKey() + "@@@@@@@@@");
                                        if (caller != null && !caller.equals("null")) {
                                            ArrayList<User> users =  MainActivity.users;
                                            for(int i=0; i< users.size(); i++){
                                                if(users.get(i).getUuid().equals(caller)){
                                                    Intent intent = new Intent(getApplicationContext(), Main_Friend_Call_Receive_Activity.class);
                                                    intent.putExtra("name", users.get(i).getName());
                                                    intent.putExtra(Values.VOICE_ROOM, voiceRoomName);
                                                    Log.i(Values.TAG, "name : " + users.get(i).getName());
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    try {
                                                        mp.seekTo(0);
                                                        mp.start();
                                                    }catch (Exception e){
                                                        e.getStackTrace();
                                                    }
                                                    return;
                                                }
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            } catch (Exception e) {
                e.getStackTrace();
                return;
            }
           /* while (true) {
                try {
                    String message=null;
                    if (message.startsWith("callToMe")) {
                        String messages[] = message.split(" ");//1 : name 2: ip
                        Intent intent = new Intent(getApplicationContext(), Main_Friend_Call_Receive_Activity.class);
                        intent.putExtra("name", messages[1]);
                        intent.putExtra("ip", messages[2]);
                        Log.i(Values.TAG, "name : " + messages[1] + " ip : " + messages[2]);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                    }
                } catch (Exception e) {
                    return;
                }
            }*/
        }
    }
}
